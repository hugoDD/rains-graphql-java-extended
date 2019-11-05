package graphql.extended.validation.constraints.standard;

import graphql.extended.validation.constraints.Documentation;

import java.math.BigDecimal;

public class PositiveConstraint extends AbstractPositiveNegativeConstraint {

    public PositiveConstraint() {
        super("Positive");
    }

    @Override
    public Documentation getDocumentation() {
        return Documentation.newDocumentation()
                .messageTemplate(getMessageTemplate())

                .description("The element must be a positive number.")

                .example("driver( licencePoints : Int @Positive) : DriverDetails")

                .applicableTypeNames(getApplicableTypeNames())

                .directiveSDL("directive @Positive(message : String = \"%s\") " +
                                "on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION",
                        getMessageTemplate())
                .build();
    }

    @Override
    protected boolean isOK(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
    }

}