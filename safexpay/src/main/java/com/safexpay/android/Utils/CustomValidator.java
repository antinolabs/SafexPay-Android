package com.safexpay.android.Utils;

import androidx.annotation.NonNull;

public abstract class CustomValidator {

    protected String errorMessage;

    public CustomValidator(@NonNull String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(@NonNull String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @NonNull
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Abstract method to implement your own validation checking.
     *
     * @param text    The CharSequence representation of the text in the EditText field. Cannot be null, but may be empty.
     * @param isEmpty Boolean indicating whether or not the text param is empty
     * @return True if valid, false if not
     */
    public abstract boolean isValid(@NonNull CharSequence text, boolean isEmpty);
}
