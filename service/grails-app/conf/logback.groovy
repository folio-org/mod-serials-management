import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter


// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    charset = Charset.forName('UTF-8')

    pattern = '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
              '%clr(%5p) ' + // Log level
              '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
              "%clr(%-30.30logger{29} %15(%replace([%X{tenant:-_NO_TENANT_}]){'\\[_NO_TENANT_\\]',''})){cyan} %clr(:){faint} " +
              '%m%n%wex' // Message
  }
}


logger ('grails.app.init', DEBUG)
logger ('grails.app.controllers', DEBUG)
logger ('grails.app.domains', DEBUG)
logger ('grails.app.jobs', DEBUG)
logger ('grails.app.services', DEBUG)
logger ('com.k_int', DEBUG)
logger ('pubskb', DEBUG)
logger ('okapi', INFO)
logger ('folio', DEBUG)
logger ('org.olf', DEBUG)
logger ('com.k_int.okapi.OkapiTenantAdminService', DEBUG)
logger ('com.k_int.okapi.OkapiSchemaHandler', WARN)
logger ('com.k_int.web.toolkit', TRACE)
logger ('org.grails.gorm.graphql', WARN)
logger ('com.k_int.okapi', WARN)

// logger ('org.olf.okapi.modules.directory.CustomBinders', WARN)

// LOG SQL - VERBOSE!!!!!!
// logger 'org.hibernate.SQL', TRACE, ['STDOUT']

// This one for SQL bind parameters
// logger 'org.hibernate.type.descriptor.sql.BasicBinder', TRACE, ['STDOUT']
 

if ( ( Environment.currentEnvironment == Environment.TEST) ||
     ( Environment.currentEnvironment == Environment.DEVELOPMENT ) ) {
  logger 'groovy.net.http.JavaHttpBuilder', DEBUG
  logger 'groovy.net.http.JavaHttpBuilder.content', DEBUG
  logger 'groovy.net.http.JavaHttpBuilder.headers', DEBUG
  logger 'com.k_int.okapi.OkapiTenantAdminService', TRACE
  logger 'mod_serials_management', DEBUG
  logger 'com.k_int.okapi.OkapiClient', DEBUG
  
  logger 'org.hibernate.loader.criteria', TRACE
  logger 'org.hibernate.SQL', DEBUG
  logger 'org.hibernate.type.descriptor.sql.BasicBinder', TRACE
}
else {
}

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
root(ERROR, ['STDOUT'])
