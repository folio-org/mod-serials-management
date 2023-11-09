package org.olf

import java.time.LocalDate
import java.util.regex.Pattern

// TODO Tidy up imports, some may not be needed

import org.olf.internalPiece.*
import org.olf.internalPiece.templateMetadata.*

import org.olf.templateConfig.*
import org.olf.templateConfig.templateMetadataRule.*
import org.olf.templateConfig.templateMetadataRuleFormat.*

public class PieceLabellingService {

  private static final Pattern RGX_METADATA_RULE_TYPE = Pattern.compile('_([a-z])')

  // This probably doesnt belong here, potentially in different service
  public Integer getNaiveIndexOfPiece(InternalPiece piece, ArrayList<InternalPiece> internalPieces){
    if(piece instanceof InternalRecurrencePiece){
      return internalPieces.findIndexOf{ip ->
        (ip instanceof InternalRecurrencePiece) && (ip.date == piece.date)
      }
    }else if(piece instanceof InternalCombinationPiece){
      // We're assuming that no 2 combination pieces can share recurrence pieces
      LocalDate comparisonDate = piece.recurrencePieces.getAt(0).date
      return internalPieces.findIndexOf{ip ->
        (ip instanceof InternalCombinationPiece) && (ip.recurrencePieces.findIndexOf{rp ->
          comparisonDate == rp.date
        } != -1)
      }
    }else{
      throw new RuntimeException("Cannot get naive index of internal omission piece")
    }
  }

  public ArrayList<Integer> getContainedIndexesFromPiece(InternalPiece piece, ArrayList<InternalPiece> internalPieces){
    if(piece instanceof InternalOmissionPiece){
      throw new RuntimeException("Cannot get contained indicies of omission piece")
    }
    ArrayList<Integer> containedIndicies = [0]
    Integer currentIndex = 0
    internalPieces.each{ip ->
      if(piece instanceof InternalRecurrencePiece && ip instanceof InternalRecurrencePiece && piece.date == ip.date){
        containedIndicies = [currentIndex]
      }else if(piece instanceof InternalCombinationPiece && (ip instanceof InternalCombinationPiece) && (ip.recurrencePieces.findIndexOf{rp -> piece.recurrencePieces.getAt(0).date == rp.date} != -1)){
        containedIndices = new IntRange(false, currentIndex, currentIndex+ip.reccurrencePieces.size())
      }else if(ip instanceof InternalCombinationPiece){
        currentIndex = currentIndex+ip.recurrencePieces.size() 
      }else{
        currentIndex++
      }
    }
  }

  public generateStandardMetadata(){

  }

  public ArrayList<ChronologyTemplateMetadata> generateChronologyMetadata(ArrayList<TemplateMetadataRule> templateMetadataRules) {

    ArrayList<ChronologyTemplateMetadata> chronologyTemplateMetadataArray = []

    // if (!!ruleset?.label) {
    //   ListIterator<InternalPiece> iterator = internalPieces.listIterator()
    //   Integer index = 0
    //   while(iterator.hasNext()){
    //     InternalPiece currentPiece = iterator.next()
    //     ruleset?.label?.rules.each { rule ->
    //       if(currentPiece instanceof InternalRecurrencePiece){
    //         String formattedLabelStyleType = RGX_PATTERN_TYPE.matcher(rule?.labelStyle?.value).replaceAll { match -> match.group(1).toUpperCase() }
    //         Class<? extends LabelStyle> lsc = Class.forName("org.olf.label.labelStyle.LabelStyle${formattedLabelStyleType.capitalize()}")
    //         if(formattedLabelStyleType == 'chronology'){
    //           Map labelObject = lsc.handleStyle(rule, currentPiece.date, index)
    //           currentPiece.addToLabels(new InternalPieceChronologyLabel([
    //             weekday: labelObject?.weekday,
    //             monthDay: labelObject?.monthDay, 
    //             month: labelObject?.month, 
    //             year: labelObject?.year,
    //             labelRule: rule
    //           ]))
    //         }
    //         else if(formattedLabelStyleType == 'enumeration'){
    //           InternalPieceEnumerationLabel enumerationLabel = new InternalPieceEnumerationLabel()
    //           ArrayList<Map> labelObject = lsc.handleStyle(rule, currentPiece.date, index)
    //           ListIterator<Map> enumerationIterator = labelObject.listIterator()
    //           while(enumerationIterator.hasNext()){
    //             Map enumerationLevel = enumerationIterator.next()
    //             enumerationLabel.addToLevels(new EnumerationLabelLevel([
    //               value: enumerationLevel?.value,
    //               level: enumerationLevel?.level,
    //             ]))
    //           }
    //           currentPiece.addToLabels(enumerationLabel)
    //         }
    //       }
    //     }
    //     index ++
    //   }   
    // }

    ListIterator<TemplateMetadataRule> iterator = templateMetadataRules.listIterator()
    while(iterator.hasNext()){
      TemplateMetadataRule currentMetadataRule = iterator.next()
      String templateMetadataType = RGX_METADATA_RULE_TYPE.matcher(currentMetadataRule?.templateMetadataRuleType?.value).replaceAll { match -> match.group(1).toUpperCase() }
      Class<? extends TemplateMetadataRuleType> tmrtc = Class.forName("org.olf.templateConfifg.templateMetadataRule.${templateMetadataType.capitalize()}TemplateMetadataRule")
      if(templateMetadataType == 'chronology'){
        // TODO GRAB date and index from standard template metadata
        ChronologyTemplateMetadata templateMetadata = tmrtc.handleStyle(rule, currentPiece.date, index)

      }
    }
  }
}
