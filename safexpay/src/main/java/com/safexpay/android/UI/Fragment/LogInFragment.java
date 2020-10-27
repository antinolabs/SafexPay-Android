package com.safexpay.android.UI.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.safexpay.android.R;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.Utils.Validators;
import com.safexpay.android.databinding.FragmentLogInBinding;

import java.util.Objects;

/**
 * LoginFragment {@link BaseFragment} subclass.
 */
public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private FragmentLogInBinding binding;
    private PaymentDetailActivity activity;
    private static final String FRAGMENT_NAME = "PaymentOptionsListFragment";

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        activity = (PaymentDetailActivity) getActivity();
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.proceedBtnSdkLogin.setBackgroundColor(Color.parseColor(SessionStore.headingColor));
        binding.proceedBtnSdkLogin.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.proceed_btn_sdk_login) {
            if (!Objects.requireNonNull(binding.emailIdEtLogin.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(binding.usernameEtLogin.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(binding.mobNumberEtLogin.getText()).toString().isEmpty()) {
                if (Validators.isValidEmail(binding.emailIdEtLogin.getText().toString().trim())) {
                    if (Validators.isValidMobile(binding.mobNumberEtLogin.getText().toString().trim())) {
                        activity.loadFragment(new PaymentOptionsListFragment(), false);
                    } else {
                        binding.mobNumberEtLogin.setError(getString(R.string.enter_valid_mobile));
                    }
                } else {
                    binding.emailIdEtLogin.setError(getString(R.string.enter_valid_email));
                }
            } else {
                Toast.makeText(getActivity(), R.string.fill_required_details, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
