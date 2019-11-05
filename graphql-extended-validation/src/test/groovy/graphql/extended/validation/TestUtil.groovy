package graphql.extended.validation

import graphql.GraphQL
import graphql.Scalars
import graphql.TypeResolutionEnvironment
import graphql.execution.MergedField
import graphql.execution.MergedSelectionSet
import graphql.introspection.Introspection.DirectiveLocation
import graphql.language.*
import graphql.parser.Parser
import graphql.schema.*
import graphql.schema.idl.*
import graphql.schema.idl.errors.SchemaProblem

import java.util.function.Supplier
import java.util.stream.Collectors

class TestUtil {


    static GraphQLSchema schemaWithInputType(GraphQLInputType inputType) {
        GraphQLArgument.Builder fieldArgument = GraphQLArgument.newArgument().name("arg").type(inputType)
        GraphQLFieldDefinition.Builder name = GraphQLFieldDefinition.newFieldDefinition()
                .name("name").type(Scalars.GraphQLString).argument(fieldArgument)
        GraphQLObjectType queryType = GraphQLObjectType.newObject().name("query").field(name).build()
        new GraphQLSchema(queryType)
    }

    static dummySchema = GraphQLSchema.newSchema()
            .query(GraphQLObjectType.newObject()
                    .name("QueryType")
                    .build())
            .build()

    static GraphQLSchema schemaFile(String fileName) {
        return schemaFile(fileName, mockRuntimeWiring)
    }


    static GraphQLSchema schemaFromResource(String resourceFileName, RuntimeWiring wiring) {
        def stream = TestUtil.class.getClassLoader().getResourceAsStream(resourceFileName)
        return schema(stream, wiring)
    }


    static GraphQLSchema schemaFile(String fileName, RuntimeWiring wiring) {
        def stream = TestUtil.class.getClassLoader().getResourceAsStream(fileName)

        def typeRegistry = new SchemaParser().parse(new InputStreamReader(stream))
        def options = SchemaGenerator.Options.defaultOptions().enforceSchemaDirectives(false)
        def schema = new SchemaGenerator().makeExecutableSchema(options, typeRegistry, wiring)
        schema
    }

    static GraphQLSchema schema(String spec, Map<String, Map<String, DataFetcher>> dataFetchers) {
        def wiring = RuntimeWiring.newRuntimeWiring()
        dataFetchers.each { type, fieldFetchers ->
            def tw = TypeRuntimeWiring.newTypeWiring(type).dataFetchers(fieldFetchers)
            wiring.type(tw)
        }
        schema(spec, wiring)
    }

    static GraphQLSchema schema(String spec, RuntimeWiring.Builder runtimeWiring) {
        schema(spec, runtimeWiring.build())
    }

    static GraphQLSchema schema(String spec) {
        System.out.println("schema \n" + spec)
        schema(spec, mockRuntimeWiring)
    }

    static GraphQLSchema schema(Reader specReader) {
        schema(specReader, mockRuntimeWiring)
    }

    static GraphQLSchema schema(String spec, RuntimeWiring runtimeWiring) {
        schema(new StringReader(spec), runtimeWiring)
    }

    static GraphQLSchema schema(InputStream specStream, RuntimeWiring runtimeWiring) {
        schema(new InputStreamReader(specStream), runtimeWiring)
    }

    static GraphQLSchema schema(Reader specReader, RuntimeWiring runtimeWiring) {
        try {
            def registry = new SchemaParser().parse(specReader)
            def options = SchemaGenerator.Options.defaultOptions().enforceSchemaDirectives(false)
            return new SchemaGenerator().makeExecutableSchema(options, registry, runtimeWiring)
        } catch (SchemaProblem e) {
            assert false: "The schema could not be compiled : ${e}"
            return null
        }
    }

    static GraphQLSchema schema(SchemaGenerator.Options options, String spec, RuntimeWiring runtimeWiring) {
        try {
            def registry = new SchemaParser().parse(spec)
            return new SchemaGenerator().makeExecutableSchema(options, registry, runtimeWiring)
        } catch (SchemaProblem e) {
            assert false: "The schema could not be compiled : ${e}"
            return null
        }
    }

    static GraphQL.Builder graphQL(String spec) {
        return graphQL(new StringReader(spec), mockRuntimeWiring)
    }

    static GraphQL.Builder graphQL(String spec, RuntimeWiring runtimeWiring) {
        return graphQL(new StringReader(spec), runtimeWiring)
    }

    static GraphQL.Builder graphQL(String spec, RuntimeWiring.Builder runtimeWiring) {
        return graphQL(new StringReader(spec), runtimeWiring.build())
    }

    static GraphQL.Builder graphQL(String spec, Map<String, Map<String, DataFetcher>> dataFetchers) {
        toGraphqlBuilder({ -> schema(spec, dataFetchers) })
    }

    static GraphQL.Builder graphQL(Reader specReader, RuntimeWiring runtimeWiring) {
        return toGraphqlBuilder({ -> schema(specReader, runtimeWiring) })
    }

    private static GraphQL.Builder toGraphqlBuilder(Supplier<GraphQLSchema> supplier) {
        try {
            def schema = supplier.get()
            return GraphQL.newGraphQL(schema)
        } catch (SchemaProblem e) {
            assert false: "The schema could not be compiled : ${e}"
            return null
        }
    }

    static WiringFactory mockWiringFactory = new MockedWiringFactory()

    static RuntimeWiring mockRuntimeWiring = RuntimeWiring.newRuntimeWiring().wiringFactory(mockWiringFactory).build()

    static GraphQLScalarType mockScalar(String name) {
        new GraphQLScalarType(name, name, mockCoercing())
    }

    private static Coercing mockCoercing() {
        new Coercing() {
            @Override
            Object serialize(Object dataFetcherResult) {
                return null
            }

            @Override
            Object parseValue(Object input) {
                return null
            }

            @Override
            Object parseLiteral(Object input) {
                return null
            }
        }
    }

    static GraphQLScalarType mockScalar(ScalarTypeDefinition definition) {
        new GraphQLScalarType(
                definition.getName(),
                definition.getDescription() == null ? null : definition.getDescription().getContent(),
                mockCoercing(),
                definition.getDirectives().stream().map({ mockDirective(it.getName()) }).collect(Collectors.toList()),
                definition)
    }

    static GraphQLDirective mockDirective(String name) {
        new GraphQLDirective(name, name, EnumSet.noneOf(DirectiveLocation.class), Collections.emptyList(), false, false, false)
    }

    static TypeRuntimeWiring mockTypeRuntimeWiring(String typeName, boolean withResolver) {
        def builder = TypeRuntimeWiring.newTypeWiring(typeName)
        if (withResolver) {
            builder.typeResolver(new TypeResolver() {
                @Override
                GraphQLObjectType getType(TypeResolutionEnvironment env) {
                    return null
                }
            })
        }
        return builder.build()
    }


    static Document parseQuery(String query) {
        new Parser().parseDocument(query)
    }

    static Type parseType(String typeAst) {
        String docStr = """
            type X {
                field : $typeAst
            }
        """
        try {
            def document = toDocument(docStr)
            ObjectTypeDefinition objTypeDef = document.getDefinitionsOfType(ObjectTypeDefinition.class)[0]
            return objTypeDef.fieldDefinitions[0].getType()
        } catch (Exception ignored) {
            assert false, "Invalid type AST string : $typeAst"
            return null
        }
    }

    static Document toDocument(String query) {
        parseQuery(query)
    }

    static MergedField mergedField(List<Field> fields) {
        return MergedField.newMergedField(fields).build()
    }

    static MergedField mergedField(Field field) {
        return MergedField.newMergedField(field).build()
    }

    static MergedSelectionSet mergedSelectionSet(Map<String, MergedField> subFields) {
        return MergedSelectionSet.newMergedSelectionSet().subFields(subFields).build()
    }

    static Field parseField(String sdlField) {
        String spec = """ query Foo {
        $sdlField
        }
        """
        def document = parseQuery(spec)
        def op = document.getDefinitionsOfType(OperationDefinition.class)[0]
        return op.getSelectionSet().getSelectionsOfType(Field.class)[0] as Field
    }

    static GraphQLDirective[] mockDirectivesWithArguments(String... names) {
        return names.collect { directiveName ->
            def builder = GraphQLDirective.newDirective().name(directiveName)

            names.each { argName ->
                builder.argument(GraphQLArgument.newArgument().name(argName).type(Scalars.GraphQLInt).build())
            }
            return builder.build()
        }.toArray() as GraphQLDirective[]
    }

    static List<GraphQLArgument> mockArguments(String... names) {
        return names.collect { GraphQLArgument.newArgument().name(it).type(Scalars.GraphQLInt).build() }
    }

    static Comparator<? super GraphQLType> byGreatestLength = Comparator.comparing({ it.name },
            Comparator.comparing({ it.length() }).reversed())

}
