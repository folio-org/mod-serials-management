package org.olf

import grails.converters.JSON

import java.util.regex.Pattern

import java.time.LocalDate
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

import org.olf.recurrence.recurrencePattern.*

import com.k_int.web.toolkit.refdata.RefdataValue

public class PieceGenerationService {
  private static final Pattern RGX_PATTERN_TYPE = Pattern.compile("_([a-z])")

  public createPiecesJson (Map ruleset) {
    ArrayList<String> dates = []
    Map<String, Integer> timeUnitValues = [
      day: 1,
      week: 7,
      month: 31,
      year: 365,
    ]

    // Calculate minimum whole number of years
    // Time unit * period / 365 - rounded up to next whole number
    Integer minNumberOfYears = Math.ceil(timeUnitValues.get(ruleset?.recurrence?.timeUnit?.value)*Integer.parseInt(ruleset?.recurrence?.period)/365)

    // Establish start and end date
    LocalDate startDate = LocalDate.parse(ruleset?.startDate)
    LocalDate endDate = startDate.plusYears(minNumberOfYears)

    // Convert pattern type i.e year_date to YearDate and grab related recurrence pattern class i.e RecurrencePatternYearDate
    final String formattedPatternType = RGX_PATTERN_TYPE.matcher(ruleset?.recurrence?.rules[0]?.patternType).replaceAll{ match -> match.group(1).toUpperCase() }
    final Class<? extends RecurrencePattern> rc = Class.forName("org.olf.recurrence.recurrencePattern.RecurrencePattern${formattedPatternType.capitalize()}")

    // May need fixing, establish counter for keeping track of time unit for use with ordinal
    Integer currentTimeUnitPeriod = (ruleset?.recurrence?.timeUnit?.value == 'year' && Integer.parseInt(ruleset?.recurrence?.period) > 1)  ? 0 : 1
    LocalDate currentTimeUnit = startDate

    Map<String, ChronoField> getTimeUnit = [
      day: ChronoField.DAY_OF_YEAR,
      week: ChronoField.ALIGNED_WEEK_OF_YEAR,
      month: ChronoField.MONTH_OF_YEAR,
      year: ChronoField.YEAR,
    ]

    // Iterate through days from start date to end date
    for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

      // If current time unit value does not equal the date within the loop, then the iteration must have changed time unit
      // I.e if the currentTimeUnit is 2022-03-04 and we're tracking the month field, this will fire once the date changes to the 5th month 
      if(currentTimeUnit.get(getTimeUnit.get(ruleset?.recurrence?.timeUnit?.value)) != date.get(getTimeUnit.get(ruleset?.recurrence?.timeUnit?.value))){

        //Increment the specified time unit by one
        currentTimeUnit = currentTimeUnit."plus${ruleset?.recurrence?.timeUnit?.value.capitalize()}s"(1)
        currentTimeUnitPeriod ++
      }

      if(currentTimeUnitPeriod > Integer.parseInt(ruleset?.recurrence?.period)){

        // If the currrentTimeUnitPeriod has passed the max number of time unit (period) change back to default
        currentTimeUnitPeriod = (ruleset?.recurrence?.timeUnit?.value == 'year' && Integer.parseInt(ruleset?.recurrence?.period) > 1)  ? 0 : 1
      }
      for( Integer i = 0; i<ruleset?.recurrence?.rules?.size(); i++ ){

        // Iterating through recurrence rules, if the ordinal matches current time unit period then it is a valid date
        if(currentTimeUnitPeriod == Integer.parseInt(ruleset?.recurrence?.rules[i]?.ordinal)){
        rc.compareDate(ruleset, date, i) && dates.add(date.toString())
        }
      }
    }

    def result = [
      total: dates?.size(),
      dates: dates
      ]
      
    return result
  }
}
