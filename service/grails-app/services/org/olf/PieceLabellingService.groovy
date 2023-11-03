package org.olf

public class PieceLabellingService {

  private static final Pattern RGX_METADATA_TYPE = Pattern.compile('_([a-z])')

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
      String templateMetadataType = RGX_METADATA_TYPE.matcher(currentMetadataRule?.labelStyle?.value).replaceAll { match -> match.group(1).toUpperCase() }
      Class<? extends LabelStyle> lsc = Class.forName("org.olf.label.labelStyle.LabelStyle${formattedLabelStyleType.capitalize()}")

    }


  }

}
