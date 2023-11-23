package org.olf.templating

import java.util.HashMap

import org.olf.internalPiece.templateMetadata.*

import groovy.transform.CompileStatic

public class LabelTemplateBindings extends HashMap<String, Object> {
  // FIXME Call this something else and make it more complicated
  
  // public void setEnumerationArray(ArrayList<EnumerationTemplateMetadata> enumerationArray){
  //     put("enumerationArray", enumerationArray)
  // }

  public void setChronologyArray(ArrayList<ChronologyTemplateMetadata> chronologyArray){
    println("LOGDEBUG SCA Called: ${chronologyArray}")
    chronologyArray.eachWithIndex  {element,index -> 
      this.put("chronology${index+1}", element)
    }

    this.put("chronologyArray", chronologyArray)

    println("LOGDEBUG WHAT IS THIS: ${this}")
  }

  // public Object getChronologyArray(){
  //   println("LOGDEBUG ${get("chronologyArray")}")
  //   return get("chronologyArray")
  // }

  // Abusing groovy metaprogramming, overwrite invokeMethod
  // public def invokeMethod(String name, Object args) {
  //   println("LOGDEBUG INVOKE METHOD CALLED: ${name}, ${args}")
  //   if (name == "getChronology1") {
  //     return get("chronology1")
  //   }
  // }
}
