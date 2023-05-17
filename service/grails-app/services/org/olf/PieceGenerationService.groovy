package org.olf

import grails.converters.JSON

import java.util.regex.Pattern

import java.time.LocalDate
import java.time.temporal.ChronoField;



import com.k_int.web.toolkit.refdata.RefdataValue

public class PieceGenerationService {
  Integer count = 0
  ArrayList<LocalDate> dates = [];

  private static final Pattern RGX_PATTERN_TYPE = Pattern.compile("_([a-z])")

  // Comparison for recurrence pattern type year_date
  // Checks to see if pattern.day and pattern.month are equal to dates day and month
  private void yearDateComparison(Map ruleset, LocalDate date, Integer index) {
    if (ruleset?.recurrence?.rules[index]?.pattern?.day == date.getDayOfMonth() 
        && ruleset?.recurrence?.rules[index]?.pattern?.month?.toUpperCase() == date.getMonth().toString()){
      dates.add(date)
    }
  }

  private void yearMonthWeekdayComparison(Map ruleset, LocalDate date, Integer index) {
    if (ruleset?.recurrence?.rules[index]?.pattern?.month?.toUpperCase() == date.getMonth().toString() &&
        ruleset?.recurrence?.rules[index]?.pattern?.week == date.get(ChronoField.ALIGNED_WEEK_OF_MONTH) &&
        ruleset?.recurrence?.rules[index]?.pattern?.weekday?.toUpperCase()== date.getDayOfWeek().toString()){
      dates.add(date)
    }
  }

  // Comparison for recurrence pattern year_weekday
  // Comapares pattern.week and weekday against week of year and day of week
  private void yearWeekdayComparison(Map ruleset, LocalDate date, Integer index) {
    if (ruleset?.recurrence?.rules[index]?.pattern?.week == date.get(ChronoField.ALIGNED_WEEK_OF_YEAR) 
        && ruleset?.recurrence?.rules[index]?.pattern?.weekday?.toUpperCase() == date.getDayOfWeek().toString()){
      dates.add(date)
    }
  }
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
    

    String formattedPatternType = RGX_PATTERN_TYPE.matcher(ruleset?.recurrence?.rules[0]?.patternType).replaceAll{ match -> match.group(1).toUpperCase() }

    println(formattedPatternType)
    

    // Year date testing run
    for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
      for( Integer i = 0; i<ruleset?.recurrence?.rules?.size(); i++ ){
        "${formattedPatternType}Comparison"(ruleset, date, i)
      }
      count++;
    }
    def result = [
      total: count,
      dates: dates
      ]
    return result
  }
}
