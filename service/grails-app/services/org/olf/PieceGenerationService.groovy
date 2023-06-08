package org.olf

import grails.converters.JSON

import java.util.regex.Pattern

import java.time.LocalDate
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

import org.olf.recurrence.recurrencePattern.*

import com.k_int.web.toolkit.refdata.RefdataValue

public class PieceGenerationService {
  ArrayList<String> dates = [];
  private static final Pattern RGX_PATTERN_TYPE = Pattern.compile("_([a-z])")

  public createPiecesJson (Map ruleset) {
    Map<String, Integer> timeUnitValues = [
      day: 1,
      week: 7,
      month: 31,
      year: 365,
    ]

    // day - null
    // month_ date: day - ordinal: month
    // month_weekday: weekday, week - ordinal: month
    // week: weekday - ordrinal: week
    // year_date: day, month - ordinal: year
    // year_month_weekday: week, weekday, month - ordinal: year
    // year_weekday: week, weekday - ordinal: year

    // Calculate minimum whole number of years
    // Time unit * period / 365 - rounded up to next whole number
    Integer minNumberOfYears = Math.ceil(timeUnitValues.get(ruleset?.recurrence?.timeUnit?.value)*Integer.parseInt(ruleset?.recurrence?.period)/365)

    LocalDate startDate = LocalDate.parse(ruleset?.startDate)
    LocalDate endDate = startDate.plusYears(minNumberOfYears)
    final String formattedPatternType = RGX_PATTERN_TYPE.matcher(ruleset?.recurrence?.rules[0]?.patternType).replaceAll{ match -> match.group(1).toUpperCase() }
    final Class<? extends RecurrencePattern> rc = Class.forName("org.olf.recurrence.recurrencePattern.RecurrencePattern${formattedPatternType.capitalize()}")

    Integer currentTimeUnitPeriod = 1
    LocalDate currentTimeUnit = startDate

    Map<String, ChronoField> getTimeUnit = [
      day: ChronoField.DAY_OF_YEAR,
      week: ChronoField.ALIGNED_WEEK_OF_YEAR,
      month: ChronoField.MONTH_OF_YEAR,
      year: ChronoField.YEAR,
    ]

    for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
      if(currentTimeUnit.get(getTimeUnit.get(ruleset?.recurrence?.timeUnit?.value)) != date.get(getTimeUnit.get(ruleset?.recurrence?.timeUnit?.value))){
        currentTimeUnit = currentTimeUnit."plus${ruleset?.recurrence?.timeUnit?.value.capitalize()}s"(1)
        currentTimeUnitPeriod ++
      }

      if(currentTimeUnitPeriod > Integer.parseInt(ruleset?.recurrence?.period)){
        currentTimeUnitPeriod = 1
      }
      for( Integer i = 0; i<ruleset?.recurrence?.rules?.size(); i++ ){
        if(currentTimeUnitPeriod == ruleset?.recurrence?.rules[i]?.ordinal){
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
