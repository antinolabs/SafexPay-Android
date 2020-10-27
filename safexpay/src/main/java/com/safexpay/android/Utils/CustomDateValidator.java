package com.safexpay.android.Utils;

import java.util.Calendar;

public class CustomDateValidator {

    public static final int MAXIMUM_VALID_YEAR_DIFFERENCE = 20;

    private static final CustomDateValidator INSTANCE = new CustomDateValidator(Calendar.getInstance());

    private final Calendar mCalendar;

    /**
     *
     * delegate to the actual date.
     */
    protected CustomDateValidator(Calendar calendar) {
        mCalendar = calendar;
    }

    /**
     * Helper for determining whether a date is a valid credit card expiry date.
     * @param month Two-digit month
     * @param year Two or four digit year
     * @return Whether the date is a valid credit card expiry date.
     */
    public static boolean isValid(String month, String year) {
        return INSTANCE.isValidHelper(month, year);
    }

    protected boolean isValidHelper(String monthString, String yearString) {
        if (monthString.equals("")) {
            return false;
        }

        if (yearString.equals("")) {
            return false;
        }

        if (!isDigitsOnly(monthString) || !isDigitsOnly(yearString)) {
            return false;
        }

        int month = Integer.parseInt(monthString);
        if (month < 1 || month > 12) {
            return false;
        }

        int currentYear = getCurrentTwoDigitYear();
        int year;
        int yearLength = yearString.length();
        if (yearLength == 2) {
            year = Integer.parseInt(yearString);
        } else if (yearLength == 4) {
            year = Integer.parseInt(yearString.substring(2));
        } else {
            return false;
        }

        if (year == currentYear && month < getCurrentMonth()) {
            return false;
        }

        if (year < currentYear) {
            // account for century-overlapping in 2-digit year representations
            int adjustedYear = year + 100;
            if (adjustedYear - currentYear > MAXIMUM_VALID_YEAR_DIFFERENCE) {
                return false;
            }
        }

        if (year > currentYear + MAXIMUM_VALID_YEAR_DIFFERENCE) {
            return false;
        }

        return true;
    }

    /**
     *
     * dates.
     */
    private int getCurrentMonth() {
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     *
     * with visualized expiration dates.
     */
    private int getCurrentTwoDigitYear() {
        return mCalendar.get(Calendar.YEAR) % 100;
    }

    public static boolean isDigitsOnly(CharSequence str) {
        final int len = str.length();
        for (int cp, i = 0; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(str, i);
            if (!Character.isDigit(cp)) {
                return false;
            }
        }
        return true;
    }
}
