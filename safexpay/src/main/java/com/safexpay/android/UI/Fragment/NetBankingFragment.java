package com.safexpay.android.UI.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safexpay.android.Model.Bank;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.R;
import com.safexpay.android.UI.Adapter.BankListAdapter;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.FragmentNetBankingBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetBankingFragment extends BaseFragment implements View.OnClickListener {

    private FragmentNetBankingBinding binding;
    private static final String FRAGMENT_NAME = "NetBankingFragment";
    private List<PaymentMode.PaymentModeDetailsList> paymentBankList;
    private String payModeId;

    public NetBankingFragment(List<PaymentMode.PaymentModeDetailsList> paymentBankList, String payModeId) {
        this.paymentBankList = paymentBankList;
        this.payModeId = payModeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetBankingBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        for (PaymentMode.PaymentModeDetailsList paymentBank : paymentBankList)
            paymentBank.setSelected(false);
        binding.netbankingListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.netbankingListView.setAdapter(new BankListAdapter(getActivity(), paymentBankList, payModeId));
        binding.navBackNetbanking.setOnClickListener(this);
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
        int id = v.getId();
        if (id == R.id.nav_back_netbanking){
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
                SessionStore.PG_ID = "";
                SessionStore.SCHEME_ID = "";
                SessionStore.PAYMODE_ID = "";
            }
        }
    }
}
