package com.safexpay.android.Utils;

import java.util.Calendar;

public class CardUtils {

    public static boolean isCardNumberValid(String cardNumber) {

        int cardLength = cardNumber.length();
        if (cardNumber == null || cardNumber.isEmpty() || cardLength < 4) {
            return false;
        }

        CardTypes cardType = CardUtils.getCardType(cardNumber);

        // No length check for MAESTRO
        if (cardType != CardTypes.MAESTRO && cardType.getNumberLength() != cardLength) {
            return false;
        }

        // If the card number starts with 0
        if (cardNumber.charAt(0) == 0) {
            return false;
        }

        int total = 0;
        boolean isEvenPosition = false;
        for (int i = cardLength - 1; i >= 0; i--) {

            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (isEvenPosition) {
                digit *= 2;
            }

            total += digit / 10; // For 'digit' with two digits
            total += digit % 10;

            isEvenPosition = !isEvenPosition;
        }

        return (total % 10 == 0);
    }

    /**
     * Check for Maestro Card.
     *
     * @param cardNumber Card Number
     * @return True if card is Maestro else False.
     */
    public static boolean isMaestroCard(String cardNumber) {
        return CardTypes.MAESTRO.matches(cardNumber);
    }

    /**
     * Returns the CardType of the card issuer.
     *
     * @param cardNumber Card number.
     * @return CardType
     */
    public static CardTypes getCardType(String cardNumber) {
        for (CardTypes cardType : CardTypes.values()) {
            if (cardType.matches(cardNumber)) {
                return cardType;
            }
        }

        return CardTypes.UNKNOWN;
    }

    /**
     * Check method to see if the card expiry date is valid.
     *
     * @param expiryDateStr Date string in the format - MM/yy.
     * @return True if the Date is expired else False.
     */

    public static boolean isDateInValid(String date) {
        if(!date.contains("/")){
            return true;
        }
        CustomDateValidator dateValidator = new CustomDateValidator(Calendar.getInstance());
        String [] strings =  date.split("/");
        return !dateValidator.isValidHelper(strings[0],strings[1]);
    }
}
