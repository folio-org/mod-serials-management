package org.olf

import grails.converters.JSON

import java.util.regex.Pattern

import java.time.LocalDate
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField

import org.olf.recurrence.*
import org.olf.omission.*
import org.olf.combination.*

import org.olf.recurrence.recurrencePattern.*
import org.olf.omission.omissionPattern.*
import org.olf.combination.combinationPattern.*

import org.olf.internalPiece.*

import com.k_int.web.toolkit.refdata.RefdataValue

public class PieceGenerationService {

  private static final Pattern RGX_PATTERN_TYPE = Pattern.compile('_([a-z])')

  // This takes in a SerialRuleset and generates pieces without saving any domain objects
  public createPiecesTransient (SerialRuleset ruleset, LocalDate startDate) {
    // TODO Remove eventually "dates"
    ArrayList<String> dates = []
    ArrayList<InternalPiece> internalPieces = []
    Map<String, Integer> timeUnitValues = [
      day: 1,
      week: 7,
      month: 31,
      year: 365,
    ]

    // Calculate minimum whole number of years
    // Time unit * period / 365 - rounded up to next whole number
    Integer minNumberOfYears = Math.ceil(timeUnitValues.get(ruleset?.recurrence?.timeUnit?.value) * ruleset?.recurrence?.period / 365)

    // Establish start and end date
    LocalDate endDate = startDate.plusYears(minNumberOfYears)

    // Convert pattern type i.e year_date to YearDate and grab related recurrence pattern class i.e RecurrencePatternYearDate
    final String formattedRecurrencePatternType = RGX_PATTERN_TYPE.matcher(ruleset?.recurrence?.rules[0]?.patternType?.value).replaceAll { match -> match.group(1).toUpperCase() }
    final Class<? extends RecurrencePattern> rpc = Class.forName("org.olf.recurrence.recurrencePattern.RecurrencePattern${formattedRecurrencePatternType.capitalize()}")

    // May need fixing, establish counter for keeping track of time unit for use with ordinal
    Integer currentTimeUnitPeriod = 1
    LocalDate currentTimeUnit = startDate

    Map<String, ChronoField> getTimeUnit = [
      day: ChronoField.DAY_OF_YEAR,
      week: ChronoField.ALIGNED_WEEK_OF_YEAR,
      month: ChronoField.MONTH_OF_YEAR,
      year: ChronoField.YEAR,
    ]

    // TODO Potential refactor, mapping ordinal blocks outside of for loop?

    // Iterate through days from start date to end date
    for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
      // If current time unit value does not equal the date within the loop, then the iteration must have changed time unit
      // I.e if the currentTimeUnit is 2022-03-04 and we're tracking the month field, this will fire once the date changes to the 5th month
      if (currentTimeUnit.get(getTimeUnit.get(ruleset?.recurrence?.timeUnit?.value)) != date.get(getTimeUnit.get(ruleset?.recurrence?.timeUnit?.value))) {
        //Increment the specified time unit by one
        currentTimeUnit = currentTimeUnit."plus${ruleset?.recurrence?.timeUnit?.value.capitalize()}s"(1)
        currentTimeUnitPeriod ++
      }

      if (currentTimeUnitPeriod > ruleset?.recurrence?.period) {
        // If the currrentTimeUnitPeriod has passed the max number of time unit (period) change back to default
        currentTimeUnitPeriod = 1
      }
      ruleset?.recurrence?.rules.each { rule ->
        // Iterating through recurrence rules, if the ordinal matches current time unit period then it is a valid date
        if (currentTimeUnitPeriod == rule?.ordinal && rpc.compareDate(rule, date)) {
          dates.add([date: date])
          // FIXME Remove date stuff above and DO NOT SAVE
          // internalPieces << new InternalRecurrencePiece([date: date, recurrenceRule: rule]).save()
          internalPieces << new InternalRecurrencePiece([date: date, recurrenceRule: rule])
        }       
      }
    }

    if (!!ruleset?.omission) {
      // TODO Remove and replace with iterator
      //For each omission rule, compare it against all dates within the previously generated recurrence dates
      ruleset?.omission?.rules.each { rule ->
        // Convert pattern type to associated omission pattern i.e day_month -> OmissionPatternDayMonth
        String formattedOmissionPatternType = RGX_PATTERN_TYPE.matcher(rule?.patternType?.value).replaceAll { match -> match.group(1).toUpperCase() }
        Class<? extends OmissionPattern> opc = Class.forName("org.olf.omission.omissionPattern.OmissionPattern${formattedOmissionPatternType.capitalize()}")

        //Once omission pattern has been grabbed, compare dates using the comain models compareDate method
        dates.each { date -> 
          if(opc.compareDate(rule, date.date, internalPieces)){
            date.omitted = true
          }
        }
      }
      // Internalpieces iteratorfor each piece check each rule passing in list of pieces and date from piece to see if it matches
      // If it does remove element
      ListIterator<InternalPiece> iterator = internalPieces.listIterator()
      while(iterator.hasNext()){
        InternalPiece currentPiece = iterator.next()
        ruleset?.omission?.rules.each { rule ->
          // Convert pattern type to associated omission pattern i.e day_month -> OmissionPatternDayMonth
          String formattedOmissionPatternType = RGX_PATTERN_TYPE.matcher(rule?.patternType?.value).replaceAll { match -> match.group(1).toUpperCase() }
          Class<? extends OmissionPattern> opc = Class.forName("org.olf.omission.omissionPattern.OmissionPattern${formattedOmissionPatternType.capitalize()}")

          //Once omission pattern has been grabbed, compare dates using the comain models compareDate method
          if(opc.compareDate(rule, currentPiece.date, internalPieces)) {
            if(currentPiece instanceof InternalRecurrencePiece){
              iterator.remove()
              InternalOmissionPiece omissionPiece = new InternalOmissionPiece([date: currentPiece.date])
              omissionPiece.addToOmissionOrigins(new OmissionOrigin([omissionRule: rule]))
              iterator.add(omissionPiece)
            }else if(currentPiece instanceof InternalOmissionPiece){
              currentPiece.addToOmissionOrigins(new OmissionOrigin([omissionRule: rule]))
            }
            // Grab new internal omission piece
            iterator.previous()
            currentPiece = iterator.next()
            // FIXME Do not save
            // currentPiece.save(flush: true, failOnError: true)
          }
        }
      }
    }

    if (!!ruleset?.combination) {

      ArrayList<String> combinationDates = dates

      for (Integer i = 0; i < ruleset?.combination?.rules?.size(); i++) {

        ArrayList<String> rulesetDates = []
        Integer currentYear = null

        // Convert pattern type to associated combination pattern i.e day_month -> CombinationPatternDayMonth
        String formattedCombinationPatternType = RGX_PATTERN_TYPE.matcher(ruleset?.combination?.rules[i]?.patternType?.value).replaceAll { match -> match.group(1).toUpperCase() }
        Class<? extends CombinationPattern> cpc = Class.forName("org.olf.combination.combinationPattern.CombinationPattern${formattedCombinationPatternType.capitalize()}")

        //Once combination pattern has been grabbed, compare dates using the comain models compareDate method
        for (Integer j = 0; j < combinationDates.size(); j++) {
          if(!dates[j]?.combined && cpc.compareDate(ruleset?.combination?.rules[i], dates[j]?.date, internalPieces) && dates[j].date.get(ChronoField.YEAR) != currentYear){
            rulesetDates.add(combinationDates.subList(j, j + ruleset?.combination?.rules[i].issuesToCombine))
            for(Integer k = j+1; k < (j + (ruleset?.combination?.rules[i].issuesToCombine)); k++){
              combinationDates[k].combined = true;
            }
            currentYear = dates[j].date.get(ChronoField.YEAR)
            }else{
            rulesetDates.add(combinationDates[j])
          }
        }
        combinationDates = rulesetDates
      }
      combinationDates.removeIf(x -> x?.combined == true)
      dates = combinationDates

      // For each piece, find all of the combination rule sets it belongs to and put in temp set
      // Then run through combination rules, and add all internal combination pieces that have that combination rule to a set, must be unique
      // 3 Cases, case 1, there are zero internal combination pieces, in this case, we remove the recurrence, create an inernal combination piece and the combination
      // case 2, there is one combination piece that matches, we remove the current recurrence piece and whilst in memeroy, add it to the single combination pieces array of combination pieces, and change the combination pieces combinations origins the set of 
      // Case 3, there are multiple combination pieces, set up a set as a union of all origins and current origin, somehow, remove all of the combination pieces SOMEHOW AND INSERT A NEW COMBINATION PIECE THAT COMBINES THEM, will need to make union of recurrence pieces
      
      // ListIterator<InternalPiece> iterator = internalPieces.listIterator()
      // while(iterator.hasNext()){
      //   InternalPiece currentPiece = iterator.next()
      //   ruleset?.combination?.rules.each { rule ->
      //     // Convert pattern type to associated combination pattern i.e day_month -> CombinationPatternDayMonth
      //     String formattedCombinationPatternType = RGX_PATTERN_TYPE.matcher(ruleset?.combination?.rules[i]?.patternType?.value).replaceAll { match -> match.group(1).toUpperCase() }
      //     Class<? extends CombinationPattern> cpc = Class.forName("org.olf.combination.combinationPattern.CombinationPattern${formattedCombinationPatternType.capitalize()}")
      //   }

        
      // }
    }

    def result = [
      total: dates?.size(),
      dates: dates
      ]

    return result
  }

}
