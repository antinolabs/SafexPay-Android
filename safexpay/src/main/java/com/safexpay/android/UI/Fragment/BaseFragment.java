package com.safexpay.android.UI.Fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    /**
     * Attach views to Java in this method
     *
     * @param view rootview of the fragment
     */
    public void inflateXML(View view) {
        // No logic
    }

    /**
     * @return Name of the fragment
     */
    public String getFragmentName() {
        return "";
    }
}
