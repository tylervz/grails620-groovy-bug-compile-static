package myapp

import myapp.instructor.Instructor
import myapp.sessionitem.SessionItem
import myapp.sessionitem.SessionItemGroup

class BootStrap {

    def init = { servletContext ->
        // Save test data if it does not already exist
        SessionItemGroup group = SessionItemGroup.first()
        if (!group) {
            Instructor instructor = new Instructor(firstName: "Homer", lastName: "Simpson",
                    userID: "hsimpson").save(failOnError: true)
            SessionItem sessionItem = new SessionItem(name: "Fruit Salad Crafting 101",
                    itemID: "fruit-salad-101", instructor: instructor).save(failOnError: true)
            SessionItemGroup sessionItemGroup = new SessionItemGroup(instructor: instructor,
                    name: "June Training Sessions", sessionItems: [sessionItem]).save(failOnError: true)
        }
    }

    def destroy = {
    }
}
