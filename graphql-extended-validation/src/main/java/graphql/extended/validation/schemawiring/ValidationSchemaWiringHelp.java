package graphql.extended.validation.schemawiring;

import graphql.GraphQLError;
import graphql.PublicApi;
import graphql.extended.validation.interpolation.MessageInterpolator;
import graphql.extended.validation.rules.OnValidationErrorStrategy;
import graphql.extended.validation.rules.TargetedValidationRules;
import graphql.extended.validation.rules.ValidationRules;
import graphql.extended.validation.util.Util;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLDirectiveContainer;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

import java.util.List;
import java.util.Locale;

/**
 * A {@link SchemaDirectiveWiring} that can be used to inject validation rules into the data fetchers
 * when the graphql schema is being built.  It will use the validation rules and ask each one of they apply to the field and or its
 * arguments.
 * <p>
 * If there are rules that apply then it will it will change the {@link DataFetcher} of that field so that rules get run
 * BEFORE the original field fetch is run.
 */
@PublicApi
public class ValidationSchemaWiringHelp {

    private final ValidationRules ruleCandidates;

    private ValidationSchemaWiringHelp(ValidationRules ruleCandidates) {
        this.ruleCandidates = ruleCandidates;
    }

    public static ValidationSchemaWiringHelp newInstance() {
        ValidationRules validationRules = ValidationRules.newValidationRules().locale(Locale.getDefault())
                .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                .build();
        return new ValidationSchemaWiringHelp(validationRules);
    }

    public static ValidationSchemaWiringHelp newInstance(ValidationRules validationRules) {
        return new ValidationSchemaWiringHelp(validationRules);
    }

    public void buildFieldDataFetch(SchemaDirectiveWiringEnvironment<? extends GraphQLDirectiveContainer> env) {
        GraphQLFieldsContainer fieldsContainer = env.getFieldsContainer();
        GraphQLFieldDefinition fieldDefinition = env.getFieldDefinition();
        if (fieldsContainer == null || fieldDefinition == null) {
            return;
        }

        TargetedValidationRules rules = ruleCandidates.buildRulesFor(fieldDefinition, fieldsContainer);

        final DataFetcher currentDF = env.getCodeRegistry().getDataFetcher(fieldsContainer, fieldDefinition);
        if (rules.isEmpty()) {
            return; // no rules - no validation needed
        }

        OnValidationErrorStrategy errorStrategy = ruleCandidates.getOnValidationErrorStrategy();
        MessageInterpolator messageInterpolator = ruleCandidates.getMessageInterpolator();
        Locale locale = ruleCandidates.getLocale();


        final DataFetcher newDF = buildValidatingDataFetcher(rules, errorStrategy, messageInterpolator, currentDF, locale);

        env.getCodeRegistry().dataFetcher(fieldsContainer, fieldDefinition, newDF);


    }

    private DataFetcher buildValidatingDataFetcher(TargetedValidationRules rules, OnValidationErrorStrategy errorStrategy, MessageInterpolator messageInterpolator, DataFetcher currentDF, final Locale defaultLocale) {
        // ok we have some rules that need to be applied to this field and its arguments
        return environment -> {
            List<GraphQLError> errors = rules.runValidationRules(environment, messageInterpolator, defaultLocale);
            if (!errors.isEmpty()) {
                // should we continue?
                if (!errorStrategy.shouldContinue(errors, environment)) {
                    return errorStrategy.onErrorValue(errors, environment);
                }
            }
            // we have no validation errors or they said continue so call the underlying data fetcher
            Object returnValue = currentDF.get(environment);
            if (errors.isEmpty()) {
                return returnValue;
            }
            return Util.mkDFRFromFetchedResult(errors, returnValue);
        };
    }

}
