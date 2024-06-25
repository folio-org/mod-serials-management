package org.olf

import java.time.LocalDate
import java.util.regex.Pattern

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
  .registerHelpers(StringHelpers))


  // This needs to take in an individual piece and ooutput a String label
  public String generateTemplatedLabelForPiece(InternalPiece piece, ArrayList<InternalPiece> internalPieces, TemplateConfig templateConfig, ArrayList<UserConfiguredTemplateMetadata> startingValues) { 
    Template template = hte.createTemplate(templateConfig.templateString);
    // Template template = hte.createTemplate("EA {{chronology1.year}} {{chronologyArray.0.year}} {{test}}")

    StandardTemplateMetadata standardTM = generateStandardMetadata(piece, internalPieces)
    // Having to enforce a sort here
    Set<TemplateMetadataRule> sortedRules = templateConfig.rules?.sort{ it.index }
    ArrayList<UserConfiguredTemplateMetadata> sortedStartingValues = startingValues?.sort{ it.index }

    ArrayList<ChronologyUCTMT> chronologyArray = generateChronologyMetadata(standardTM, sortedRules)
    ArrayList<EnumerationUCTMT> enumerationArray = generateEnumerationMetadata(standardTM, sortedRules, sortedStartingValues)

    LabelTemplateBindings ltb = new LabelTemplateBindings()
    ltb.setupChronologyArray(chronologyArray)
    ltb.setupEnumerationArray(enumerationArray)
    ltb.setupStandardTM(standardTM)

    return template.make(ltb).with { 
      StringWriter sw = new StringWriter()
      writeTo(sw)
      sw.toString()
    }
  }

  public void setLabelsForInternalPieces(ArrayList<InternalPiece> internalPieces, TemplateConfig templateConfig, ArrayList<UserConfiguredTemplateMetadata> startingValues) {
    ListIterator<InternalPiece> iterator = internalPieces?.listIterator()
    while(iterator.hasNext()){
      InternalPiece currentPiece = iterator.next()
      if(currentPiece instanceof InternalRecurrencePiece || currentPiece instanceof InternalCombinationPiece){
        String label = generateTemplatedLabelForPiece(currentPiece, internalPieces, templateConfig, startingValues)
        currentPiece.label = label
        currentPiece.templateString = templateConfig?.templateString
      }
    }
  }

  // This probably doesnt belong here, potentially in different service
  // Grab naive index of piece, treating combination pieces a single piece, using the first date in array of recurrence pieces
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
        containedIndicies = new IntRange(false, currentIndex, currentIndex+ip.recurrencePieces.size())
      }else if(ip instanceof InternalCombinationPiece){
        currentIndex = currentIndex+ip.recurrencePieces.size() 
      }else{
        currentIndex++
      }
    }
    return containedIndicies
  }

  // Grab index of piece, treating combination pieces as individual pieces contained within
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
      }else if(p instanceof InternalCombinationPiece){
        indexCounter += p.recurrencePieces.size()
      }
    }
  }

  public StandardTemplateMetadata generateStandardMetadata(InternalPiece piece, ArrayList<InternalPiece> internalPieces){
    LocalDate date

    if(piece instanceof InternalRecurrencePiece){
      date = piece.date
    }else if(piece instanceof InternalCombinationPiece){
      date = piece.recurrencePieces.getAt(0).date
    }

    Integer index = getIndex(piece, internalPieces)
    Integer naiveIndex = getNaiveIndexOfPiece(piece, internalPieces)
    ArrayList<Integer> containedIndices = getContainedIndexesFromPiece(piece, internalPieces)

    return new StandardTemplateMetadata([date: date, index: index, naiveIndex: naiveIndex, containedIndices: containedIndices])

  }

  public ArrayList<ChronologyUCTMT> generateChronologyMetadata(StandardTemplateMetadata standardTM, Set<TemplateMetadataRule> templateMetadataRules) {
    ArrayList<ChronologyUCTMT> chronologyTemplateMetadataArray = []
    Iterator<TemplateMetadataRule> iterator = templateMetadataRules?.iterator()
    while(iterator?.hasNext()){
      TemplateMetadataRule currentMetadataRule = iterator.next()
      String templateMetadataType = RGX_METADATA_RULE_TYPE.matcher(currentMetadataRule?.templateMetadataRuleType?.value).replaceAll { match -> match.group(1).toUpperCase() }
      if(templateMetadataType == 'chronology'){
        Class<? extends TemplateMetadataRuleType> tmrtc = Class.forName("org.olf.templateConfig.templateMetadataRule.${templateMetadataType.capitalize()}TemplateMetadataRule")
        ChronologyUCTMT chronologyUCTMT = tmrtc.handleType(currentMetadataRule, standardTM.date, standardTM.index)
        chronologyTemplateMetadataArray << chronologyUCTMT
      }
    }
    return chronologyTemplateMetadataArray
  }

  public ArrayList<EnumerationUCTMT> generateEnumerationMetadata(StandardTemplateMetadata standardTM, Set<TemplateMetadataRule> templateMetadataRules, ArrayList<UserConfiguredTemplateMetadata> startingValues) {
    ArrayList<EnumerationUCTMT> enumerationTemplateMetadataArray = []
    Iterator<TemplateMetadataRule> iterator = templateMetadataRules?.iterator()
    while(iterator?.hasNext()){
      TemplateMetadataRule currentMetadataRule = iterator.next()
      String templateMetadataType = RGX_METADATA_RULE_TYPE.matcher(currentMetadataRule?.templateMetadataRuleType?.value).replaceAll { match -> match.group(1).toUpperCase() }
      if(templateMetadataType == 'enumeration'){
        Class<? extends TemplateMetadataRuleType> tmrte = Class.forName("org.olf.templateConfig.templateMetadataRule.${templateMetadataType.capitalize()}TemplateMetadataRule")
        EnumerationUCTMT ruleStartingValues = startingValues.getAt(currentMetadataRule?.index)?.metadataType
        EnumerationUCTMT enumerationUCTMT = tmrte.handleType(currentMetadataRule, standardTM.date, standardTM.index, ruleStartingValues)

        enumerationTemplateMetadataArray << enumerationUCTMT
      }
    }
    return enumerationTemplateMetadataArray
  }


  public TemplateMetadata generateTemplateMetadataForPiece(InternalPiece piece, ArrayList<InternalPiece> internalPieces, TemplateConfig templateConfig, ArrayList<UserConfiguredTemplateMetadata> startingValues){
    // FIXME Presently this method appaneds the "nextPiece" to the array of internal pieces to calculate the standard metadata, requireing us to pop it from the array within the predicted piece set controller
    // Theres almost certainly a better way to handle this, probably by copying the internal pieces array

    // Additionally alot of the variable here can be renamed for easier maintainability
    ArrayList<InternalPiece> ipsPlusNext = internalPieces.clone()
    ipsPlusNext << piece
    StandardTemplateMetadata standardTM = generateStandardMetadata(piece, ipsPlusNext)
    TemplateMetadata tm = new TemplateMetadata([standard : standardTM, userConfigured: []])
    // This block here is doing alot of what was copied from the code blocks above except instead of seperating into chronology and enumeration arrays
    // they are instead being compiled into a single template UserConfiguredTemplateMetadata array
    Set<UserConfiguredTemplateMetadata> uctmArray = []
    Set<TemplateMetadataRule> sortedRules = templateConfig.rules?.sort{ it.index }
    Iterator<TemplateMetadataRule> iterator = sortedRules?.iterator()
    while(iterator?.hasNext()){
      TemplateMetadataRule currentMetadataRule = iterator.next()
      String templateMetadataType = RGX_METADATA_RULE_TYPE.matcher(currentMetadataRule?.templateMetadataRuleType?.value).replaceAll { match -> match.group(1).toUpperCase() }
      Class<? extends TemplateMetadataRuleType> tmrt = Class.forName("org.olf.templateConfig.templateMetadataRule.${templateMetadataType.capitalize()}TemplateMetadataRule")
      if(templateMetadataType == 'enumeration'){
        EnumerationUCTMT ruleStartingValues = startingValues.getAt(currentMetadataRule?.index)?.metadataType
        EnumerationUCTMT enumerationUCTMT = tmrt.handleType(currentMetadataRule, standardTM.date, standardTM.index, ruleStartingValues)

        // FIXME upon creation of a new UserConfiguredTemplateMetadata we use the refdata binding previously seen in recurrence, omission etc.
        // However due to the dynamically assigned class already being created (EnumerationUCTMT) prior to instanciating the UserConfiguredTemplateMetadata this has some weird behaviour
        // It sets the metadataType field to null so we have to directly assign it after the fact, this can almost certainly be resolved within the UserConfiguredTemplateMetadataTypeHelpers class 
        UserConfiguredTemplateMetadata currentUCTM = new UserConfiguredTemplateMetadata([
          userConfiguredTemplateMetadataType: 'enumeration',
          metadataType: enumerationUCTMT,
          index: currentMetadataRule?.index,
          owner: tm
        ])
        currentUCTM.metadataType = enumerationUCTMT
        tm.userConfigured << currentUCTM

      } else {
        ChronologyUCTMT chronologyUCTMT = tmrt.handleType(currentMetadataRule, standardTM.date, standardTM.index)
        
        // Same thing happening here as reference above
        UserConfiguredTemplateMetadata currentUCTM = new UserConfiguredTemplateMetadata([
          userConfiguredTemplateMetadataType: 'chronology',
          metadataType: chronologyUCTMT,
          index: currentMetadataRule?.index,
          owner: tm
        ])
        currentUCTM.metadataType = chronologyUCTMT
        tm.userConfigured << currentUCTM
      }
    }
    return tm
  }
}
