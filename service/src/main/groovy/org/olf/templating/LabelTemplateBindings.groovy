package org.olf.templating

import java.util.HashMap

import org.olf.internalPiece.templateMetadata.*

import groovy.transform.CompileStatic

public class LabelTemplateBindings extends HashMap<String, Object> {
  public void setupChronologyArray(ArrayList<ChronologyUCTMT> chronologyArray){
    chronologyArray.eachWithIndex{  element, index ->
      // Bloody make sure you toString() GStrings
      this.put("chronology${index+1}".toString(), element)
    }

    this.put("chronologyArray", chronologyArray)
  }

  public void setupEnumerationArray(ArrayList<EnumerationUCTMT> enumerationArray){
    enumerationArray.eachWithIndex{ element, index ->
      if(!!element?.value){
        this.put("enumeration${index+1}".toString(), element?.value)
      }else{
        Map<String, Object> levelsMap = new HashMap<String, Object>()
        element?.levels?.sort{ it.index }?.eachWithIndex{ level, levelIndex ->
          levelsMap.put("level${levelIndex+1}".toString(), level?.value)
        }
        this.put("enumeration${index+1}".toString(), levelsMap)
      }
    }
    this.put("enumerationArray", enumerationArray)
  }

  public void setupStandardTM(StandardTemplateMetadata standardTM){
    this.put("standardTM", standardTM)
  }
}
