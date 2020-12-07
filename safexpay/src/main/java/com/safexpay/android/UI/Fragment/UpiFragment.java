package com.safexpay.android.UI.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.R;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.FragmentUpiBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpiFragment extends BaseFragment implements View.OnClickListener {

    private FragmentUpiBinding binding;
    private PaymentDetailActivity parentActivity;
    private List<PaymentMode.PaymentModeDetailsList> upiList;
    private String payModeId;
    private static final String FRAGMENT_NAME = "UPIFragment";

    public UpiFragment(List<PaymentMode.PaymentModeDetailsList> upiList, String payModeId) {
        this.upiList = upiList;
        this.payModeId = payModeId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpiBinding.inflate(inflater, container, false);
        parentActivity = (PaymentDetailActivity) getActivity();
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.navBackUpi.setOnClickListener(this);
        SessionStore.PG_ID = upiList.get(0).getPgDetailsResponse().getPgId();
        SessionStore.SCHEME_ID = upiList.get(0).getSchemeDetailsResponse().getSchemeId();
        SessionStore.PAYMODE_ID = payModeId;
        binding.upiIdEtSdk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                SessionStore.Upi_Id = binding.upiIdEtSdk.getText().toString();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.nav_back_upi) {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
                SessionStore.PG_ID = "";
                SessionStore.SCHEME_ID = "";
                SessionStore.PAYMODE_ID = "";
            }
        }
    }
}
