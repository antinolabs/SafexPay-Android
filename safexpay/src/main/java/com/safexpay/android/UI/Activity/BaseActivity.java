package com.safexpay.android.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Class calligraphyClass = getCalligraphyClass();
        if (calligraphyClass != null) {
            try {
                Method method = calligraphyClass.getMethod("wrap", Context.class);
                super.attachBaseContext((Context) method.invoke(calligraphyClass, newBase));
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Class getCalligraphyClass() {
        try {
            return Class.forName("uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Update the actionbar accordingly
     */
    protected void updateActionBar() {
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }


    /**
     * returnResult is used to pass back the appropriate result to the initiator activity that
     * started this activity for result. The method will call {@link Activity#finish()} and the
     * current activity will be stopped.
     *
     * @param bundle if any extra params that need to be passed back.
     * @param result Appropriate Activity result to be sent back to caller Activity - {@link Activity#RESULT_OK}
     *               on Success or {@link Activity#RESULT_CANCELED} on Failure.
     */

    public void returnResult(Bundle bundle, int result) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(result, intent);
        finish();
    }


    /**
     * Overloading method for {@link BaseActivity#returnResult(Bundle, int)} with null Bundle.
     *
     * @param result Appropriate Activity result to be sent back to caller Activity - {@link Activity#RESULT_OK}
     *               on Success or {@link Activity#RESULT_CANCELED} on Failure.
     */
    public void returnResult(int result) {
        returnResult(null, result);
    }

    /**
     * Hides the Soft keyboard if visible.
     */

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Update the actionbar of the associated activity
     *
     * @param title - title to be set to current activity's actionbar
     */
    public void updateActionBarTitle(@StringRes int title) {
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setTitle(title);
    }
}
