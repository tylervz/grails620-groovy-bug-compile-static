package myapp.sessionitem

import grails.compiler.GrailsCompileStatic
import myapp.instructor.Instructor

@GrailsCompileStatic
class SessionItem {

    String name
    String itemID
    String description
    /**
     * The {@link SessionItemGroup} that this SessionItem belongs to, if any.
     * A SessionItem can only belong to one SessionItemGroup at a time.
     * This is a transient property.
     */
    SessionItemGroup sessionItemGroup

    static transients = ['sessionItemGroup']
    // The main instructor of the SessionItem
    static belongsTo = [instructor: Instructor]

    static hasMany = [additionalInstructors: Instructor]

    static mapping = {
        name sqlType: "text"
    }

    static constraints = {
        name(nullable: false)
        itemID(nullable: false)
        description(nullable: true, blank: false)
    }
}
