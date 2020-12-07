package com.safexpay.android.UI.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.safexpay.android.Interfaces.ItemSelectedCallback;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.R;
import com.safexpay.android.UI.Adapter.WalletsAdapter;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.FragmentWalletsBinding;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletsFragment extends BaseFragment implements View.OnClickListener, ItemSelectedCallback {

    private FragmentWalletsBinding binding;
    private String payModeId;
    private List<PaymentMode.PaymentModeDetailsList> paymentWalletList;

    public WalletsFragment(List<PaymentMode.PaymentModeDetailsList> paymentWalletList, String payModeId) {
        this.payModeId = payModeId;
        this.paymentWalletList = paymentWalletList;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWalletsBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.navBackWallet.setOnClickListener(this);
        binding.walletHeaderText.setOnClickListener(this);
        for (PaymentMode.PaymentModeDetailsList paymentModeDetailsList : paymentWalletList)
            paymentModeDetailsList.setSelected(false);
        binding.walletListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.walletListView.setAdapter(new WalletsAdapter(getActivity(), paymentWalletList, payModeId, this));
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

    @Override
    public void onClick(int position) {
        selectCard(position);
        try {
            SessionStore.PG_ID = paymentWalletList.get(position).getPgDetailsResponse().getPgId();
            SessionStore.SCHEME_ID = paymentWalletList.get(position).getSchemeDetailsResponse().getSchemeId();
            SessionStore.PAYMODE_ID = payModeId;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectCard(int position) {
        for (int i = 0; i < paymentWalletList.size(); i++) {
            if (i == position)
                paymentWalletList.get(i).setSelected(true);
            else paymentWalletList.get(i).setSelected(false);
        }
        binding.walletListView.setAdapter(new WalletsAdapter(getActivity(), paymentWalletList, payModeId, this));
    }
}
