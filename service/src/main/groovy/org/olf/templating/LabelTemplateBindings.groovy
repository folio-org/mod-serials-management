package org.olf.templating

import java.util.HashMap

import org.olf.internalPiece.templateMetadata.*

import groovy.transform.CompileStatic

public class LabelTemplateBindings extends HashMap<String, Object> {
  public void setupChronologyArray(ArrayList<ChronologyTemplateMetadata> chronologyArray){
    chronologyArray.eachWithIndex  {element, index ->
      // Bloody make sure you toString() GStrings
      this.put("chronology${index+1}".toString(), element)
    }

    this.put("chronologyArray", chronologyArray)
  }

  public void setupEnumerationArray(ArrayList<EnumerationTemplateMetadata> enumerationArray){
    enumerationArray.eachWithIndex  {element, index ->
      // Bloody make sure you toString() GStrings
      this.put("enumeration${index+1}".toString(), element)
    }

    this.put("enumerationArray", enumerationArray)
  }

  public void setupStandardMetadata(StandardTemplateMetadata standardTM){
    this.put("standardMetadata", standardTM)
  }
}
