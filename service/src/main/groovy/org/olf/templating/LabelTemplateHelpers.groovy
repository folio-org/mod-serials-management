package org.olf.templating

import java.util.function.Function

import com.github.jknack.handlebars.Options

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
public class LabelTemplateHelpers {
  
  public static final CharSequence insertAfter (final Object context, final Object param1, final Object param2, final Options options) {
    
    return Optional.ofNullable(Objects.toString(param1, ""))
      
      .map({ final String param -> "(?=(?<=${regexSafe(param)}))" as String })
      .map({ final String regex ->
        final op = { String subject ->
          return subject.replaceFirst(regex, Objects.toString(param2, ""))
        }
        
        return doOperation(context, op, options)
      })
      .orElse(null)
  }
  
  public static final CharSequence insertAfterAll (final Object context, final Object param1, final Object param2, final Options options) {
    return Optional.ofNullable(Objects.toString(param1, ""))
      .map({ final String param -> "(?=(?<=${regexSafe(param)}))" as String })
      .map({ final String regex ->
        final op = { String subject ->
          return subject.replaceAll(regex, Objects.toString(param2, ""))
        }
        
        return doOperation(context, op, options)
      })
      .orElse(null)
  }
  
  public static final CharSequence insertBefore (final Object context, final Object param1, final Object param2, final Options options) {
    return Optional.ofNullable(Objects.toString(param1, ""))
      .map({ final String param -> "(?<=(?=${regexSafe(param)}))" as String })
      .map({ final String regex ->
        final op = { String subject -> 
          return subject.replaceFirst(regex, Objects.toString(param2, ""))
        }
        
        return doOperation(context, op, options)
      })
      .orElse(null)
  }
  
  public static final CharSequence insertBeforeAll (final Object context, final Object param1, final Object param2, final Options options) {
    return Optional.ofNullable(Objects.toString(param1, ""))
      .map({ final String param -> "(?<=(?=${regexSafe(param)}))" as String })
      .map({ final String regex ->
        final op = { String subject ->
          return subject.replaceAll(regex, Objects.toString(param2, ""))
        }
        
        return doOperation(context, op, options)
      })
      .orElse(null)
  }
  
  public static final CharSequence removeProtocol (final Object context, final Options options) {
    return Optional.of("http(s?)://")
      .map({ final String regex ->
        final op = { String subject ->
          return subject.replaceAll(regex, "")
        }
        
        return doOperation(context, op, options)
      })
      .orElse(null)
  }

  public static final CharSequence urlEncode (final Object context, final Options options) {
    
    return Optional.ofNullable(context)
      .filter({ val -> !options.isFalsy(val) })
      .map(Objects.&toString)
      .map({ final String val -> URLEncoder.encode(val, "UTF-8") })
      .orElse(null)
  }
  
  private static final String doOperation (final Object context, Function<String, String> replacementFunction, final Options options) {
    
    return Optional.ofNullable(context)
      .filter({ val -> !options.isFalsy(val) })
      .map(Objects.&toString)
      .map(replacementFunction)
      .orElse(null)
      ;
  }
  
  private static final String regexSafe(String inputString) {
    String outputString = inputString.replace("\\", "\\\\")
      .replace("*", "\\*")
      .replace("(", "\\(")
      .replace(")", "\\)")
      .replace(".", "\\.")
      .replace("|", "\\|")
      .replace("?", "\\?")
      .replace("!", "\\!")
      .replace("+", "\\+")
      .replace("}", "\\}")
      .replace("{", "\\{")
      .replace("]", "\\]")
      .replace("[", "\\[")
      .replace("^", "\\^")
      .replace("\$", "\\\$")

    return outputString
  }
}