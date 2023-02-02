package org.olf

import grails.gorm.MultiTenant

import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

public class Recurrence implements MultiTenant<Recurrence> {
    String id
    Serial owner

    @CategoryId(value="Recurrence.TimeUnits", defaultInternal=true)
    @Defaults(['Day', 'Week', 'Month', 'Year'])
    RefdataValue timeUnit /* defaults Day/Week/Month/Year*/
    // This might be a ref data list for value day/week/month/year
    Integer issues // 7
    Integer period // 3 / Frequency
    Set<RecurrenceRule> rules // Validate to have exactly #issues of these
}

// Example Input
// Issues 6, period every 2, timeUnit Month
// UI on day 1 of month 2
// Rule Ordinal 2 patternType/Pattern month_date
// Month_date 1

public class RecurrenceRule implements MultiTenant<RecurrenceRule> {
    String id
    Recurrence owner
    Integer ordinal /* Validated to be 1 - period of owner. Default is 1 */

    @CategoryId(value="RecurrenceRule.PatternType", defaultInternal=true)
    @Defaults(['Day', 'Week', 'Month Date', 'Month Weekday', 'Year Date', 'Year Weekday', 'Year Month Weekday'])
    RefdataValue patternType
    
    RecurrencePattern pattern // Validate that patternType Year_Weekday -> RecurrencePatternYearWeekday
    /* Day - "" */
    /* Week - Mon/Tue/Wed/Thur/Fri/Sat/Sun */
    /* Month_Date - 1/2/3/../28/-1 */ /* 1/.../31/-1 AND fallback, 29f28 etc OR default any number >28 to fallback to last */
    /* Month_Weekday - 1/2/3/4/-1 + Mon/Tues/.../Sun */
    /* Year_Date - 1/.../31/-1 + Jan/Feb/.../Dec (Validate date against month? (What to do about 29th Feb? - Use last?)) */
    /* Year_Weekday - 1/.../52/-1 + Mon/Tues/.../Sun */
    /* Year_Month_Weekday 1/2/3/4/-1 + Mon/Tues/.../Sun + Jan/Feb/.../Dec */
}

public class RecurrencePattern implements MultiTenant<RecurrencePattern> {
    String id
    RecurrenceRule owner
}

public class RecurrencePatternDay extends RecurrencePattern implements MultiTenant<RecurrencePatternDay> {

}

public class RecurrencePatternWeek extends RecurrencePattern implements MultiTenant<RecurrencePatternWeek> {
    @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
    @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
    RefdataValue weekday
}

public class RecurrencePatternMonthDate extends RecurrencePattern implements MultiTenant<RecurrencePatternMonthDate> {
    Integer day // Validated 1-28 and -1 // OR special refdata
    // Fallback stuff?
}

public class RecurrencePatternMonthWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternMonthWeekday> {
    Integer week // Validated between 1-4 and -1

    @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
    @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
    RefdataValue weekday
}

public class RecurrencePatternYearDate extends RecurrencePattern implements MultiTenant<RecurrencePatternYearDate> {
    Integer day // Validated between 1-31 and -1 against month

    @CategoryId(value="RecurrencePattern.Month" defaultInternal=true)
    @Defaults(['January', 'Febuary', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
    RefdataValue month
}

public class RecurrencePatternYearWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternYearWeekday> {
    Integer week // Validated between 1-52 and -1

    @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
    @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
    RefdataValue weekday
}

public class RecurrencePatternYearMonthWeekday extends RecurrencePattern implements MultiTenant<RecurrencePatternYearMonthWeekday> {
    Integer week // Validated between 1-4 and -1

    @CategoryId(value="RecurrencePattern.Weekday", defaultInternal=true)
    @Defaults(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
    RefdataValue weekday

    @CategoryId(value="RecurrencePattern.Month" defaultInternal=true)
    @Defaults(['January', 'Febuary', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'])
    RefdataValue month
}
