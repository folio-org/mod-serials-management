package org.olf

import java.time.LocalDate
import java.util.regex.Pattern

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

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


  // This needs to take in an individual piece and output a String label
  // PreviousLabelTemplateBindings will b null for first piece
  public LabelTemplateBindings generateTemplateBindingsForPiece(
    InternalPiece piece,
    ArrayList<InternalPiece> internalPieces,
    TemplateConfig templateConfig,
    ArrayList<UserConfiguredTemplateMetadata> startingValues,
    LabelTemplateBindings previousLabelTemplateBindings
  ){
    StandardTemplateMetadata standardTM = generateStandardMetadata(piece, internalPieces)
    // Having to enforce a sort here
    Set<ChronologyTemplateMetadataRule> sortedChronologyRules = templateConfig.chronologyRules?.sort{ it.index }
    Set<EnumerationTemplateMetadataRule> sortedEnumerationRules = templateConfig.enumerationRules?.sort{ it.index }

    ArrayList<UserConfiguredTemplateMetadata> sortedStartingValues = startingValues?.sort{ it.index }

    // Making assumption that chronologies don't have starting values
    ArrayList<ChronologyUCTMT> chronologyArray = generateChronologyMetadata(standardTM, sortedChronologyRules)
    ArrayList<EnumerationUCTMT> enumerationArray = generateEnumerationMetadata(
      standardTM, 
      sortedEnumerationRules, 
      sortedStartingValues, 
      previousLabelTemplateBindings?.enumerationArray
    )

    LabelTemplateBindings ltb = new LabelTemplateBindings()
    ltb.setupChronologyArray(chronologyArray)
    ltb.setupEnumerationArray(enumerationArray)
    ltb.setupStandardTM(standardTM)

    return ltb
  }

  public String generateLabelForPiece(LabelTemplateBindings ltb, String templateString){
    Template template = hte.createTemplate(templateString);
    // Template template = hte.createTemplate("EA {{chronology1.year}} {{chronologyArray.0.year}} {{test}}")
    
    return template.make(ltb).with { 
      StringWriter sw = new StringWriter()
      writeTo(sw)
      sw.toString()
    }
  }

  //Returns the last label template binding for use in next piece
  public LabelTemplateBindings setLabelsForInternalPieces(ArrayList<InternalPiece> internalPieces, TemplateConfig templateConfig, ArrayList<UserConfiguredTemplateMetadata> startingValues) {
    ListIterator<InternalPiece> iterator = internalPieces?.listIterator()
    LabelTemplateBindings previousLabelTemplateBindings = null
    while(iterator.hasNext()){
      InternalPiece currentPiece = iterator.next()
      if(currentPiece instanceof InternalRecurrencePiece || currentPiece instanceof InternalCombinationPiece){
        // String label = generateTemplatedLabelForPiece(currentPiece, internalPieces, templateConfig, startingValues)
        LabelTemplateBindings ltb = generateTemplateBindingsForPiece(currentPiece, internalPieces, templateConfig, startingValues, previousLabelTemplateBindings)
        String label = generateLabelForPiece(ltb, templateConfig?.templateString)
        currentPiece.label = label
        currentPiece.templateString = templateConfig?.templateString
        // Not a huge fan of overwriting a previous binding
        previousLabelTemplateBindings = ltb
      }
      // If the piece is a combined piece, in order to get the correct template bindings which account for all recurrence pieces within a combination piece,
      // we need need to iterate over the recurrence pieces, generating the LTB for each and then passing the final one to the outer loop so labelling can continue
      if(currentPiece instanceof InternalCombinationPiece){
        // Create an iterator for the recurrence pieces
        Iterator recurrencePiecesIterator = currentPiece?.recurrencePieces?.iterator()
          while(recurrencePiecesIterator.hasNext()){
            InternalPiece currentRecurrencePiece = recurrencePiecesIterator.next()
            // Since the recurrence pieces are currently stored as a Set and the method accepts an ArrayList, it needs to be converted
            // TODO Unsure if this needs to be a set vs an array list or vice versa
            List<InternalPiece> recurrencePiecesList = new ArrayList<>(currentPiece?.recurrencePieces);
            LabelTemplateBindings recurrencePieceLTB = generateTemplateBindingsForPiece(currentRecurrencePiece, recurrencePiecesList, templateConfig, startingValues, previousLabelTemplateBindings)
            // Pass the LTB back, when all recurrence pieces have had their ltb generated, the next internal piece will us the last recurrence pieces bindings
            previousLabelTemplateBindings = recurrencePieceLTB
          }
      }
    }
    return previousLabelTemplateBindings
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

  public ArrayList<ChronologyUCTMT> generateChronologyMetadata(StandardTemplateMetadata standardTM, Set<ChronologyTemplateMetadataRule> templateMetadataRules) {
    ArrayList<ChronologyUCTMT> chronologyTemplateMetadataArray = []
    Iterator<ChronologyTemplateMetadataRule> iterator = templateMetadataRules?.iterator()
    while(iterator?.hasNext()){
      ChronologyTemplateMetadataRule currentMetadataRule = iterator.next()
        ChronologyUCTMT chronologyUCTMT = ChronologyTemplateMetadataRule.handleType(currentMetadataRule, standardTM.date, standardTM.index)
        chronologyTemplateMetadataArray << chronologyUCTMT
    }
    return chronologyTemplateMetadataArray
  }

  public ArrayList<EnumerationUCTMT> generateEnumerationMetadata(
    StandardTemplateMetadata standardTM, 
    Set<EnumerationTemplateMetadataRule> templateMetadataRules, 
    ArrayList<UserConfiguredTemplateMetadata> startingValues,
    ArrayList<EnumerationUCTMT> previousEnumerationArray
  ) {
    ArrayList<EnumerationUCTMT> enumerationTemplateMetadataArray = []
    Iterator<EnumerationTemplateMetadataRule> iterator = templateMetadataRules?.iterator()
    
    // This block is due to be refactor when we seperate out chronology and enumeration in the starting values
    // Currently we have to track indexs independently depedending on userConfiguredTemplateMetadataType
    int enumerationIndex = 0
    ArrayList<UserConfiguredTemplateMetadata> enumerationStartingValues = startingValues.findAll { it.userConfiguredTemplateMetadataType == 'enumeration' }

    while(iterator?.hasNext()){
      EnumerationTemplateMetadataRule currentMetadataRule = iterator.next()
        // previousEnumerationArray might be null
        EnumerationUCTMT ruleStartingValues = previousEnumerationArray ? previousEnumerationArray?.getAt(enumerationIndex) : enumerationStartingValues.getAt(currentMetadataRule?.index)?.metadataType
        EnumerationUCTMT enumerationUCTMT = EnumerationTemplateMetadataRule.handleType(currentMetadataRule, standardTM.date, standardTM.index, ruleStartingValues)

        enumerationTemplateMetadataArray << enumerationUCTMT
        enumerationIndex++
    }
    return enumerationTemplateMetadataArray
  }


  public TemplateMetadata generateTemplateMetadataForPiece(
    InternalPiece piece,
    ArrayList<InternalPiece> internalPieces, 
    TemplateConfig templateConfig, 
    ArrayList<UserConfiguredTemplateMetadata> startingValues, 
    ArrayList<EnumerationUCTMT> previousEnumerationArray
    ){
    // TODO alot of the variable here can be renamed for easier maintainability
    // Also do we actually need this anymore?
    ArrayList<InternalPiece> ipsPlusNext = internalPieces.clone()
    ipsPlusNext << piece
    StandardTemplateMetadata standardTM = generateStandardMetadata(piece, ipsPlusNext)
    TemplateMetadata tm = new TemplateMetadata([standard : standardTM, userConfigured: []])
    // This block here is doing alot of what was copied from the code blocks above except instead of seperating into chronology and enumeration arrays
    // they are instead being compiled into a single template UserConfiguredTemplateMetadata array
    Set<UserConfiguredTemplateMetadata> uctmArray = []
    Set<ChronologyTemplateMetadataRule> sortedChronologyRules = templateConfig.chronologyRules?.sort{ it.index }
    Set<EnumerationTemplateMetadataRule> sortedEnumerationRules = templateConfig.enumerationRules?.sort{ it.index }

    // FIXME upon creation of a new UserConfiguredTemplateMetadata we use the refdata binding previously seen in recurrence, omission etc.
    // However due to the dynamically assigned class already being created (EnumerationUCTMT) prior to instanciating the UserConfiguredTemplateMetadata this has some weird behaviour
    // It sets the metadataType field to null so we have to directly assign it after the fact, this can almost certainly be resolved within the UserConfiguredTemplateMetadataTypeHelpers class 
    
    Iterator<ChronologyTemplateMetadataRule> chronologyIterator = sortedChronologyRules?.iterator()
    while(chronologyIterator?.hasNext()){
      ChronologyTemplateMetadataRule currentMetadataRule = chronologyIterator.next()
        ChronologyUCTMT chronologyUCTMT = ChronologyTemplateMetadataRule.handleType(currentMetadataRule, standardTM.date, standardTM.index)
        
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

    Iterator<EnumerationTemplateMetadataRule> enumerationIterator = sortedEnumerationRules?.iterator()

    // This block is due to be refactor when we seperate out chronology and enumeration in the starting values
    // Currently we have to track indexs independently depedending on userConfiguredTemplateMetadataType
    int enumerationIndex = 0
    ArrayList<UserConfiguredTemplateMetadata> enumerationStartingValues = startingValues.findAll { it.userConfiguredTemplateMetadataType == 'enumeration' }

    while(enumerationIterator?.hasNext()){
      EnumerationTemplateMetadataRule currentMetadataRule = enumerationIterator.next()
        EnumerationUCTMT ruleStartingValues = previousEnumerationArray ? previousEnumerationArray?.getAt(enumerationIndex) : enumerationStartingValues.getAt(currentMetadataRule?.index)?.metadataType
        EnumerationUCTMT enumerationUCTMT = EnumerationTemplateMetadataRule.handleType(currentMetadataRule, standardTM.date, standardTM.index, ruleStartingValues)

        UserConfiguredTemplateMetadata currentUCTM = new UserConfiguredTemplateMetadata([
          userConfiguredTemplateMetadataType: 'enumeration',
          metadataType: enumerationUCTMT,
          index: currentMetadataRule?.index,
          owner: tm
        ])
        currentUCTM.metadataType = enumerationUCTMT
        tm.userConfigured << currentUCTM
        enumerationIndex++
    }
    return tm
  }
}
