package myapp.sessionitem

import myapp.instructor.Instructor
import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes='id,name', includeNames=true, includePackage=false)
class SessionItemGroup {

    String name
    // Using a List so that we can have the sessionItems sorted
    // in the order they were added to the SessionItemGroup.
    List<SessionItem> sessionItems

    /**
     * @return the number of sessionItems in the SessionItemGroup
     */
    Integer getSize() {
        return sessionItems ? sessionItems.size() : 0
    }

    // The instructor is the owner of the SessionItemGroup
    static belongsTo = [instructor: Instructor]
    /**
     * There are typically zero sessionItems in each SessionItemGroup immediately after the
     * SessionItemGroup is created. Then the instructor adds sessionItems to it.
     */
    static hasMany = [sessionItems: SessionItem]
    static constraints = {
        // A particular instructor cannot have two SessionItemGroups with the same name
        name nullable: false, blank: false, unique: 'instructor'
        instructor nullable: false
    }
}
