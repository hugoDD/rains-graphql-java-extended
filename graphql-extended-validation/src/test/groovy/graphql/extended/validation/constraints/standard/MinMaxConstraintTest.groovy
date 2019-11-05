package graphql.extended.validation.constraints.standard

import graphql.extended.validation.constraints.BaseConstraintTestSupport
import graphql.extended.validation.constraints.DirectiveConstraint
import spock.lang.Unroll

class MinMaxConstraintTest extends BaseConstraintTestSupport {


    @Unroll
    def "min rule constraints"() {

        DirectiveConstraint ruleUnderTest = new MinConstraint()

        expect:

        def errors = runValidation(ruleUnderTest, fieldDeclaration, "arg", argVal)
        assertErrors(errors, expectedMessage)

        where:

        fieldDeclaration                                                | argVal | expectedMessage
        "field( arg : Int @Min(value : 100) ) : ID"                     | 50     | "Min;path=/arg;val:50;\t"
        "field( arg : Int @Min(value : 10) ) : ID"                      | 50     | ""

        'field( arg : Int @Min(value : 100, message : "custom") ) : ID' | 50     | "custom;path=/arg;val:50;\t"
        // edge case
        "field( arg : Int @Min(value : 50) ) : ID"                      | 49     | "Min;path=/arg;val:49;\t"
        "field( arg : Int @Min(value : 50) ) : ID"                      | 50     | ""
        "field( arg : Int @Min(value : 50) ) : ID"                      | 51     | ""

        // nulls are valid
        'field( arg : Int @Min(value : 50) ) : ID'                      | null   | ''

    }

    @Unroll
    def "max rule constraints"() {

        DirectiveConstraint ruleUnderTest = new MaxConstraint()

        expect:

        def errors = runValidation(ruleUnderTest, fieldDeclaration, "arg", argVal)
        assertErrors(errors, expectedMessage)

        where:

        fieldDeclaration                                                | argVal | expectedMessage
        "field( arg : Int @Max(value : 100) ) : ID"                     | 150    | "Max;path=/arg;val:150;\t"
        "field( arg : Int @Max(value : 100) ) : ID"                     | 50     | ""

        'field( arg : Int @Max(value : 100, message : "custom") ) : ID' | 150    | "custom;path=/arg;val:150;\t"
        // edge case
        "field( arg : Int @Max(value : 50) ) : ID"                      | 51     | "Max;path=/arg;val:51;\t"
        "field( arg : Int @Max(value : 50) ) : ID"                      | 50     | ""
        "field( arg : Int @Max(value : 50) ) : ID"                      | 49     | ""

        // nulls are valid
        'field( arg : Int @Max(value : 50) ) : ID'                      | null   | ''
    }
}