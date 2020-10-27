package com.safexpay.android.UI.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.safexpay.android.R;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.FragmentConfirmationBinding;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class ConfirmationFragment extends DialogFragment implements View.OnClickListener {

    private FragmentConfirmationBinding binding;
    private CancelTransactionListener cancelTransactionListener;

    public ConfirmationFragment(CancelTransactionListener cancelTransactionListener) {
        this.cancelTransactionListener = cancelTransactionListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        setCancelable(false);
        binding.cancelTransactionBtnSdk.setOnClickListener(this);
        binding.closeCancellationDialog.setOnClickListener(this);
        binding.cancelTransactionBtnSdk.setBackgroundColor(Color.parseColor(SessionStore.headingColor));
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadio = binding.getRoot().findViewById(group.getCheckedRadioButtonId());
                if (group.getCheckedRadioButtonId() != -1) {
                    if (selectedRadio.getText().toString().equals(getString(R.string.other))) {
                        binding.cancellationFeedbackEt.setEnabled(true);
                        binding.cancellationFeedbackEt.requestFocus();
                    } else {
                        binding.cancellationFeedbackEt.setEnabled(false);
                        binding.cancellationFeedbackEt.setText("");
                        if (selectedRadio.getText().toString().equals(getString(R.string.other))) {
                            // perform some action
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel_transaction_btn_sdk) {
            cancelTransactionListener.cancelClicked();
        } else if (id == R.id.close_cancellation_dialog) {
            dismiss();
        }
    }

    public interface CancelTransactionListener {
        void cancelClicked();
    }
}
