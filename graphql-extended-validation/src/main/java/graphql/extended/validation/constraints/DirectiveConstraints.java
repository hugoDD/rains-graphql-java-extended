package graphql.extended.validation.constraints;

import graphql.PublicApi;
import graphql.extended.validation.constraints.standard.*;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * This contains a map of {@link DirectiveConstraint}s and helps
 * run them against a specific field or argument
 * <p>
 * This ships with a set of standard constraints via {@link #STANDARD_CONSTRAINTS} but you can
 * add your own implementations if you wish
 */
@PublicApi
public class DirectiveConstraints {

    /**
     * These are the standard directive rules that come with the system
     */
    public final static List<DirectiveConstraint> STANDARD_CONSTRAINTS = Arrays.asList(
            new ExpressionConstraint(),
            new AssertFalseConstraint(),
            new AssertTrueConstraint(),
            new DecimalMaxConstraint(),
            new DecimalMinConstraint(),
            new DigitsConstraint(),
            new MaxConstraint(),
            new MinConstraint(),
            new NegativeOrZeroConstraint(),
            new NegativeConstraint(),
            new NotBlankRule(),
            new NotEmptyRule(),
            new PatternConstraint(),
            new PositiveOrZeroConstraint(),
            new PositiveConstraint(),
            new RangeConstraint(),
            new SizeConstraint()
    );

    private final Map<String, DirectiveConstraint> constraints;

    public DirectiveConstraints(Builder builder) {
        this.constraints = Collections.unmodifiableMap((builder.directiveRules));
    }

    public static Builder newDirectiveConstraints() {
        return new Builder();
    }

    public Map<String, DirectiveConstraint> getConstraints() {
        return constraints;
    }

    public String getDirectivesSDL() {
        StringBuilder sb = new StringBuilder();
        for (DirectiveConstraint directiveConstraint : constraints.values()) {
            sb.append("\n   ").append(directiveConstraint.getDocumentation().getDirectiveSDL()).append("\n");
        }
        return sb.toString();
    }

    public TypeDefinitionRegistry getDirectivesDeclaration() {
        TypeDefinitionRegistry registry = new TypeDefinitionRegistry();
        for (DirectiveConstraint directiveConstraint : constraints.values()) {
            registry.merge(directiveConstraint.getDocumentation().getDirectiveDeclaration());
        }
        return registry;
    }

    public List<DirectiveConstraint> whichApplyTo(GraphQLFieldDefinition fieldDefinition, GraphQLFieldsContainer fieldsContainer) {
        return constraints.values()
                .stream()
                .filter(c -> c.appliesTo(fieldDefinition, fieldsContainer))
                .collect(toList());
    }

    public List<DirectiveConstraint> whichApplyTo(GraphQLArgument argument, GraphQLFieldDefinition fieldDefinition, GraphQLFieldsContainer fieldsContainer) {
        return constraints.values()
                .stream()
                .filter(c -> c.appliesTo(argument, fieldDefinition, fieldsContainer))
                .collect(toList());
    }

    public static class Builder {
        private Map<String, DirectiveConstraint> directiveRules = new LinkedHashMap<>();

        public Builder() {
            STANDARD_CONSTRAINTS.forEach(this::addRule);
        }

        public Builder addRule(DirectiveConstraint rule) {
            directiveRules.put(rule.getName(), rule);
            return this;
        }

        public Builder clearRules() {
            directiveRules.clear();
            return this;
        }

        public DirectiveConstraints build() {
            return new DirectiveConstraints(this);
        }
    }
}
