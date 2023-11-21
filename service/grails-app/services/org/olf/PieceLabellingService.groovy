package org.olf

import java.time.LocalDate
import java.util.regex.Pattern

// TODO Tidy up imports, some may not be needed

import org.olf.templating.*

import org.olf.internalPiece.*
import org.olf.internalPiece.templateMetadata.*

import org.olf.templateConfig.*
import org.olf.templateConfig.templateMetadataRule.*
import org.olf.templateConfig.templateMetadataRuleFormat.*

import com.github.jknack.handlebars.EscapingStrategy
import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.helper.StringHelpers
import uk.co.cacoethes.handlebars.HandlebarsTemplateEngine

import groovy.text.Template

public class PieceLabellingService {
  private static final Pattern RGX_METADATA_RULE_TYPE = Pattern.compile('_([a-z])')

  private static final HandlebarsTemplateEngine hte = new HandlebarsTemplateEngine(handlebars: new Handlebars().with(new EscapingStrategy() {
    public String escape(final CharSequence value) {
      return value.toString() // No escaping. Return as is.
    }
  })
  .registerHelpers(StringHelpers)
  .registerHelpers(StringTemplateHelpers))


  // This needs to take in an individual piece and ooutput a String label
  public Map generateTemplatedLabelForPiece(InternalPiece piece, ArrayList<InternalPiece> internalPieces, TemplateConfig templateConfig) { 
    // Template template = hte.createTemplate(piece.templateString);

    StandardTemplateMetadata standardTM = generateStandardMetadata(piece, internalPieces)
    ArrayList<ChronologyTemplateMetadata> chronologyTMArray = generateChronologyMetadata(standardTM, templateConfig.rules)
    ArrayList<EnumerationTemplateMetadata> enumerationTMArray = generateEnumerationMetadata(standardTM, templateConfig.rules)

    StringTemplateBindings ltb = new StringTemplateBindings([
      chronology: chronologyTMArray,
      enumeration: enumerationTMArray,
      standardTM: standardTM
    ])

    // return template.make(binding).with { 
    //   StringWriter sw = new StringWriter()
    //   writeTo(sw)
    //   sw.toString()
    // }
  }

  public void setLabelsForInternalPieces(ArrayList<InternalPiece> internalPieces, TemplateConfig templateConfig) {
    ListIterator<InternalPiece> iterator = internalPieces?.listIterator()
    while(iterator.hasNext()){
      InternalPiece currentPiece = iterator.next()
      currentPiece.label = generateTemplatedLabelForPiece(currentPiece, internalPieces, templateConfig)
    }
  }

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

  public Integer getIndex(InternalPiece piece, ArrayList<InternalPiece> internalPieces){
    Integer indexCounter = 0
    ListIterator<InternalPiece> iterator = internalPieces.listIterator()
    while(iterator.hasNext()){
      InternalPiece p = iterator.next()
      if(p instanceof InternalRecurrencePiece && piece instanceof InternalRecurrencePiece && piece.date == p.date){
        return indexCounter
      }else if(p instanceof InternalRecurrencePiece){
        indexCounter ++
      }else if(p instanceof InternalCombinationPiece && piece instanceof InternalCombinationPiece && p.recurrencePieces.getAt(0).date == piece.recurrencePieces.getAt(0).date){
        return indexCounter
      }else{
        indexCounter += p.recurrencePieces.size()
      }
    }
  }

  public StandardTemplateMetadata generateStandardMetadata(InternalPiece piece, ArrayList<InternalPiece> internalPieces){
    LocalDate date = piece.date
    Integer index = getIndex(piece, internalPieces)
    Integer naiveIndex = getNaiveIndexOfPiece(piece, internalPieces)
    ArrayList<Integer> containedIndices = getContainedIndexesFromPiece(piece, internalPieces)

    return new StandardTemplateMetadata([date: date, index: index, naiveIndex: naiveIndex, containedIndices: containedIndices])

  }

  public ArrayList<ChronologyTemplateMetadata> generateChronologyMetadata(StandardTemplateMetadata standardTM, ArrayList<TemplateMetadataRule> templateMetadataRules) {
    ArrayList<ChronologyTemplateMetadata> chronologyTemplateMetadataArray = []
    ListIterator<TemplateMetadataRule> iterator = templateMetadataRules.listIterator()
    while(iterator.hasNext()){
      TemplateMetadataRule currentMetadataRule = iterator.next()
      String templateMetadataType = RGX_METADATA_RULE_TYPE.matcher(currentMetadataRule?.templateMetadataRuleType?.value).replaceAll { match -> match.group(1).toUpperCase() }
      if(templateMetadataType == 'chronology'){
        Class<? extends TemplateMetadataRuleType> tmrtc = Class.forName("org.olf.templateConfig.templateMetadataRule.${templateMetadataType.capitalize()}TemplateMetadataRule")
        ChronologyTemplateMetadata chronologyTemplateMetadata = tmrtc.handleType(currentMetadataRule, standardTM.date, standardTM.index)
        chronologyTemplateMetadataArray << chronologyTemplateMetadata
      }
    }
    return chronologyTemplateMetadataArray
  }

  public ArrayList<EnumerationTemplateMetadata> generateEnumerationMetadata(StandardTemplateMetadata standardTM, ArrayList<TemplateMetadataRule> templateMetadataRules) {
    ArrayList<EnumerationTemplateMetadata> enumerationTemplateMetadataArray = []
    ListIterator<TemplateMetadataRule> iterator = templateMetadataRules.listIterator()
    while(iterator.hasNext()){
      TemplateMetadataRule currentMetadataRule = iterator.next()
      String templateMetadataType = RGX_METADATA_RULE_TYPE.matcher(currentMetadataRule?.templateMetadataRuleType?.value).replaceAll { match -> match.group(1).toUpperCase() }
      if(templateMetadataType == 'enumeration'){
        Class<? extends TemplateMetadataRuleType> tmrte = Class.forName("org.olf.templateConfig.templateMetadataRule.${templateMetadataType.capitalize()}TemplateMetadataRule")
        EnumerationTemplateMetadata enumerationTemplateMetadata = tmrte.handleType(currentMetadataRule, standardTM.date, standardTM.index)

        enumerationTemplateMetadataArray << enumerationTemplateMetadata
      }
    }
    return enumerationTemplateMetadataArray
  }
}
