package com.safexpay.android.Utils;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

public class Validators {

    /**
     * Empty field validator with Required as Default Message.
     * Use {@link #EmptyFieldValidator(String)} to set a custom error message.
     */
    public static class EmptyFieldValidator extends CustomValidator {

        public EmptyFieldValidator(String errorMessage) {
            super(errorMessage);
        }

        public EmptyFieldValidator() {
            super("Required");
        }

        @Override
        public boolean isValid(@NonNull CharSequence charSequence, boolean result) {
            return !result;
        }
    }

    /**
     * Date validator to check the expiry date of the card.
     */
    public static class DateValidator extends CustomValidator {

        public DateValidator() {
            super("Invalid Date");
        }

        @Override
        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
            return !CardUtils.isDateInValid(text.toString());
        }
    }

    /**
     * Card validator to check the validity of the card number with all the Edge cases considered.
     */
    public static class CardValidator extends CustomValidator {

        public CardValidator() {
            super("Invalid Card");
        }

        @Override
        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
            String card = text.toString().trim().replaceAll(" ", "");
            return CardUtils.isCardNumberValid(card);
        }
    }

    /**
     * Virtual Payment address Validator
     */
    public static class VPAValidator extends CustomValidator {


        public VPAValidator() {
            super("Invalid Virtual Payment Address");
        }

        @Override
        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
            if (isEmpty) {
                return false;
            }

            String[] splitData = text.toString().split("@");
            if (splitData.length != 2) {
                return false;
            }

            return !text.toString().contains(".com");
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }
}
