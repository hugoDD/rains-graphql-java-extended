package graphql.extended.validation.constraints.standard

import graphql.extended.validation.constraints.BaseConstraintTestSupport
import graphql.extended.validation.constraints.DirectiveConstraint
import spock.lang.Unroll

class AssertTrueFalseConstraintTest extends BaseConstraintTestSupport {


    @Unroll
    def "assert true rule constraints"() {

        DirectiveConstraint ruleUnderTest = new AssertTrueConstraint()

        expect:

        def errors = runValidation(ruleUnderTest, fieldDeclaration, "arg", argVal)
        assertErrors(errors, expectedMessage)

        where:

        fieldDeclaration                                             | argVal | expectedMessage
        'field( arg : Boolean @AssertTrue ) : ID'                    | false  | 'AssertTrue;path=/arg;val:false;\t'
        'field( arg : Boolean @AssertTrue ): ID'                     | true   | ''

        'field( arg : Boolean @AssertTrue(message : "custom")) : ID' | false  | 'custom;path=/arg;val:false;\t'

        // nulls are valid
        'field( arg : Boolean @AssertTrue ) : ID'                    | null   | ''
    }

    @Unroll
    def "assert false rule constraints"() {

        DirectiveConstraint ruleUnderTest = new AssertFalseConstraint()

        expect:

        def errors = runValidation(ruleUnderTest, fieldDeclaration, "arg", argVal)
        assertErrors(errors, expectedMessage)

        where:

        fieldDeclaration                                              | argVal | expectedMessage
        'field( arg : Boolean @AssertFalse ) : ID'                    | true   | 'AssertFalse;path=/arg;val:true;\t'
        'field( arg : Boolean @AssertFalse ): ID'                     | false  | ''

        'field( arg : Boolean @AssertFalse(message : "custom")) : ID' | true   | 'custom;path=/arg;val:true;\t'

        // nulls are valid
        'field( arg : Boolean @AssertFalse ) : ID'                    | null   | ''
    }
}