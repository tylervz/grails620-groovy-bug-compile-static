package myapp

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Ignore

import geb.spock.*

/**
 * See https://www.gebish.org/manual/current/ for more instructions
 *
 * Ignore the test so we don't have to worry about having the right version of browsers
 * and/or drivers installed which are needed for the test to pass.
 */
@Ignore
@Integration
@Rollback
class MyappSpec extends GebSpec {

    void "test something"() {
        when:"The home page is visited"
            go '/'

        then:"The title is correct"
            title == "Welcome to Grails"
    }

}
