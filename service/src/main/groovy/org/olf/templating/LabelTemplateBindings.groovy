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
      if(!!element?.value){
        this.put("enumeration${index+1}".toString(), element?.value)
      }else{
        element?.levels?.eachWithIndex  {level, levelIndex ->
          this.put("enumeration${index+1}level${levelIndex+1}".toString(), level?.value)
        }
        // this.put("enumeration${index+1}".toString(), element)
        // element?.levels?.eachWithIndex  {level, levelIndex ->
        //   this.put("level${levelIndex+1}".toString(), level?.value)
        // }
      }
    }

    this.put("enumerationArray", enumerationArray)
  }

  public void setupStandardTM(StandardTemplateMetadata standardTM){
    this.put("standardTM", standardTM)
  }
}
