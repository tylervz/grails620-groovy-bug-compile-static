package myapp.instructor

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Instructor {

    String firstName
    String lastName
    String email
    // Instructor ID in the external system.
    String userID

    String toString() {
        return firstName + " " + lastName
    }

    static constraints = {
        userID(unique: true)
        lastName(nullable: true)
        firstName(nullable: true)
        email(nullable: true)
    }
}
