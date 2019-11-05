package com.extended.graphql.boot;

import com.oembedler.moon.graphql.boot.GraphQLJavaToolsAutoConfiguration;
import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration;
import com.oembedler.moon.graphql.boot.SchemaStringProvider;
import graphql.language.Document;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
@Configuration
@AutoConfigureAfter({GraphQLJavaToolsAutoConfiguration.class})
@AutoConfigureBefore({GraphQLWebAutoConfiguration.class})
public class GraphQLJavaValidationAutoConfiguration {

    @Autowired(required = false)
    private GraphQLScalarType[] scalars;

    @Primary
    @Bean
    public GraphQLSchema mergeGraphQLSchema(SchemaStringProvider schemaStringProvider, GraphQLSchema graphQLSchema
    ) throws IOException {

        List<String> schemaStrings = schemaStringProvider.schemaStrings();

        StringBuilder schemaBuilder = new StringBuilder();
        for (String string : schemaStrings) {
            if (schemaBuilder.length() > 0) {
                schemaBuilder.append("\n");
            }
            schemaBuilder.append(string);

        }

        graphql.parser.Parser parser = new graphql.parser.Parser();
        Document document = parser.parseDocument(schemaBuilder.toString());
        TypeDefinitionRegistry typeDefinitionRegistry = new graphql.schema.idl.SchemaParser().parse(schemaBuilder.toString());


        // we add this schema wiring to the graphql runtime
        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
        if (scalars != null) {
            Arrays.asList(scalars).forEach(builder::scalar);
        }
        RuntimeWiring runtimeWiring = builder.build();


        //graphQLSchema.getDirectives().addAll(typeDefinitionRegistry.getDirectiveDefinitions());
        GraphQLSchema existingSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
//        graphQLSchema.getDirectives().addAll(existingSchema.getDirectives());
//        GraphQLCodeRegistry existcodeRegistry= graphQLSchema.getCodeRegistry();
//        existcodeRegistry.

        GraphQLSchema newGraphQLSchema = existingSchema.transform(b -> {

//            GraphQLCodeRegistry codeRegistry= graphQLSchema.getCodeRegistry();
//            codeRegistry.transform(codeRegistryBuilder->codeRegistryBuilder.dataFetcher())

            b.codeRegistry(graphQLSchema.getCodeRegistry());
        });


//        GraphQLSchema newGraphQLSchema=  graphQLSchema.transform(schemaBuild -> {
//            schemaBuild.query(existingSchema.getQueryType())
//                    .mutation(existingSchema.getMutationType())
//                    .subscription(existingSchema.getSubscriptionType())
//                    //.codeRegistry(existingSchema.getCodeRegistry())
//                    .clearAdditionalTypes()
//                    .clearDirectives()
//                    .additionalDirectives(new HashSet(existingSchema.getDirectives()))
//                    .additionalTypes(existingSchema.getAdditionalTypes());
//
//
//        });

//        GraphQLSchema graphQLSchemaDone =  GraphQLSchema.newSchema(graphQLSchema).build();;
        return newGraphQLSchema;
    }
}
