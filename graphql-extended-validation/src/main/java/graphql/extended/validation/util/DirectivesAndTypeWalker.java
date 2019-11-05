package graphql.extended.validation.util;

import graphql.Internal;
import graphql.schema.*;

import java.util.List;
import java.util.function.BiFunction;

@Internal
public class DirectivesAndTypeWalker {

    public static boolean isSuitable(GraphQLArgument argument, BiFunction<GraphQLInputType, GraphQLDirective, Boolean> isSuitable) {
        GraphQLInputType inputType = argument.getType();
        List<GraphQLDirective> directives = argument.getDirectives();
        return walkInputType(inputType, directives, isSuitable);
    }

    private static boolean walkInputType(GraphQLInputType inputType, List<GraphQLDirective> directives, BiFunction<GraphQLInputType, GraphQLDirective, Boolean> isSuitable) {
        GraphQLInputType unwrappedInputType = Util.unwrapNonNull(inputType);
        for (GraphQLDirective directive : directives) {
            if (isSuitable.apply(unwrappedInputType, directive)) {
                return true;
            }
        }
        if (unwrappedInputType instanceof GraphQLInputObjectType) {
            GraphQLInputObjectType inputObjType = (GraphQLInputObjectType) unwrappedInputType;
            for (GraphQLInputObjectField inputField : inputObjType.getFieldDefinitions()) {
                inputType = inputField.getType();
                directives = inputField.getDirectives();

                if (walkInputType(inputType, directives, isSuitable)) {
                    return true;
                }
            }
        }
        if (unwrappedInputType instanceof GraphQLList) {
            GraphQLInputType innerListType = Util.unwrapOneAndAllNonNull(unwrappedInputType);
            if (innerListType instanceof GraphQLDirectiveContainer) {
                directives = ((GraphQLDirectiveContainer) innerListType).getDirectives();
                if (walkInputType(innerListType, directives, isSuitable)) {
                    return true;
                }
            }
        }
        return false;
    }

}
