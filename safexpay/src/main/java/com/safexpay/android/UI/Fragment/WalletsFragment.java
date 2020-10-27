package com.safexpay.android.UI.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.Model.Wallet;
import com.safexpay.android.R;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.UI.Adapter.WalletsAdapter;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.FragmentWalletsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletsFragment extends BaseFragment implements View.OnClickListener {

    private PaymentDetailActivity activity;
    private FragmentWalletsBinding binding;
    private String payModeId;
    private List<PaymentMode.PaymentModeDetailsList> paymentWalletList;

    public WalletsFragment(List<PaymentMode.PaymentModeDetailsList> paymentWalletList, String payModeId) {
        this.payModeId = payModeId;
        this.paymentWalletList = paymentWalletList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWalletsBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.navBackWallet.setOnClickListener(this);
        binding.walletHeaderText.setOnClickListener(this);
        binding.walletListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.walletListView.setAdapter(new WalletsAdapter(getActivity(), paymentWalletList, payModeId));
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.nav_back_wallet) {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
                SessionStore.PG_ID = "";
                SessionStore.SCHEME_ID = "";
                SessionStore.PAYMODE_ID = "";
            }
        }
    }
}
