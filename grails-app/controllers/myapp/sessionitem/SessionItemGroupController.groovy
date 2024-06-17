package myapp.sessionitem

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

@GrailsCompileStatic
class SessionItemGroupController {

    static allowedMethods = [
            deleteMultiple: "DELETE", show: "GET"
    ]

    MessageSource messageSource

    @Transactional(readOnly = true)
    def show(SessionItemGroup sessionItemGroup) {
        if (sessionItemGroup == null) {
            notFound()
            return
        }

        return render([sessionItemGroup: sessionItemGroup] as JSON)
    }

    /**
     * Delete multiple SessionItemGroups.
     *
     * @param ids a comma separated list of SessionItemGroup id values
     */
    @Transactional
    def deleteMultiple() {
        try {
            List<Long> longIds = ((String) params.ids)?.split(',')?.collect { String id -> id.toLong() }
            List<SessionItemGroup> sessionItemGroups = SessionItemGroup.getAll(longIds)
            // This commented out line works. The line above throws a static type check error.
            // List<SessionItemGroup> sessionItemGroups = SessionItemGroup.getAll(longIds as Iterable<Serializable>)

            if (sessionItemGroups.contains(null)) {
                notFound()
                return
            }

            for (sessionItemGroup in sessionItemGroups) {
                sessionItemGroup.delete(flush: true)
            }

            response.status = NO_CONTENT.value()
        } catch (Exception ex) {
            log.error "${ex.class.name} in SessionItemGroupController.deleteMultiple()", ex
            response.status = INTERNAL_SERVER_ERROR.value()
            String key = "sessionItemGroup.deleteMultiple.defaultException.error"
            return render([message: messageSource.getMessage(key, [ex.message].toArray(),
                    LocaleContextHolder.locale)] as JSON)
        }
    }

    protected void notFound() {
        response.status = NOT_FOUND.value()
        String message = messageSource.getMessage("object.not.found.message",
                null, LocaleContextHolder.locale)
        render([message: message] as JSON)
    }
}
