package graphql.extended.validation.constraints.standard;

import graphql.GraphQLError;
import graphql.extended.validation.constraints.AbstractDirectiveConstraint;
import graphql.extended.validation.constraints.Documentation;
import graphql.extended.validation.el.ELSupport;
import graphql.extended.validation.el.StandardELVariables;
import graphql.extended.validation.rules.ValidationEnvironment;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.GraphQLInputType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExpressionConstraint extends AbstractDirectiveConstraint {

    public ExpressionConstraint() {
        super("Expression");
    }


    @Override
    public Documentation getDocumentation() {
        return Documentation.newDocumentation()
                .messageTemplate(getMessageTemplate())

                .description("The provided expression must evaluate to true.  " +
                        "The expression language is <a href=\"https://javaee.github.io/tutorial/jsf-el001.html\">Java EL</a> " +
                        "and expressions MUST resolve to a boolean value, ie. it is valid or not.")

                .example("drivers( first : Int, after : String!, last : Int, before : String) \n" +
                        " : DriverConnection @Expression(value : \"${args.containsOneOf('first','last') }\"")

                .applicableTypeNames("All Types and Scalars")

                .directiveSDL("directive @Expression(value : String!, message : String = \"%s\") " +
                                "on FIELD_DEFINITION | ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION",
                        getMessageTemplate())
                .build();
    }

    @Override
    public boolean appliesToType(GraphQLInputType inputType) {
        return true;
    }

    @Override
    public boolean appliesTo(GraphQLFieldDefinition fieldDefinition, GraphQLFieldsContainer fieldsContainer) {
        return fieldDefinition.getDirective(getName()) != null;
    }

    @Override
    protected List<GraphQLError> runConstraint(ValidationEnvironment validationEnvironment) {
        GraphQLDirective directive = validationEnvironment.getContextObject(GraphQLDirective.class);
        String expression = helpWithCurlyBraces(getStrArg(directive, "value"));

        Object validatedValue = validationEnvironment.getValidatedValue();

        Map<String, Object> variables = StandardELVariables.standardELVars(validationEnvironment);

        ELSupport elSupport = new ELSupport(validationEnvironment.getLocale());
        boolean isOK = elSupport.evaluateBoolean(expression, variables);

        if (!isOK) {
            return mkError(validationEnvironment, directive, mkMessageParams(validatedValue, validationEnvironment,
                    "value", expression));

        }
        return Collections.emptyList();
    }

    private String helpWithCurlyBraces(String expression) {
        expression = expression.trim();
        if (!expression.startsWith("${") && !expression.startsWith("#{")) {
            expression = "${" + expression;
        }
        if (!expression.endsWith("}")) {
            expression = expression + "}";
        }
        return expression;
    }
}
