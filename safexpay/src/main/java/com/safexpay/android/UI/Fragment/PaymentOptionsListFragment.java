package com.safexpay.android.UI.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.Model.SavedCards;
import com.safexpay.android.R;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.UI.Adapter.SavedCardsAdapter;
import com.safexpay.android.Utils.Constants;
import com.safexpay.android.Utils.RecyclerUtils;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.ViewModel.PayModeViewModel;
import com.safexpay.android.databinding.FragmentPaymentOptionsListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentOptionsListFragment extends BaseFragment implements View.OnClickListener {

    private FragmentPaymentOptionsListBinding binding;
    private static final String FRAGMENT_NAME = "PaymentOptionsListFragment";
    private PaymentDetailActivity activity;
    private PayModeViewModel payModeViewModel;
    private List<PaymentMode> paymentModeList = new ArrayList<>();
    private List<SavedCards> savedCardsList = new ArrayList<>();
    private List<PaymentMode.PaymentModeDetailsList> netBankingList = new ArrayList<>();
    private List<PaymentMode.PaymentModeDetailsList> creditCardList = new ArrayList<>();
    private List<PaymentMode.PaymentModeDetailsList> debitCardList = new ArrayList<>();
    private List<PaymentMode.PaymentModeDetailsList> upiList = new ArrayList<>();
    private List<PaymentMode.PaymentModeDetailsList> walletList = new ArrayList<>();
    private String netBankingPayModeId = "", creditCardPayModeId = "", debitCardPayModeId = "", upiPayModeId = "",
    walletPayModeId = "";
    private SavedCardsAdapter savedCardsAdapter;

    public PaymentOptionsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentOptionsListBinding.inflate(inflater, container, false);
        activity = (PaymentDetailActivity) getActivity();
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.paymentOptionsContainer.setVisibility(View.GONE);
        payModeViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PayModeViewModel.class);
        payModeViewModel.init();
        observerViewModel();
        binding.cardLayout.setOnClickListener(this);
        binding.netBankingLayout.setOnClickListener(this);
        binding.emiLayout.setOnClickListener(this);
        binding.walletLayout.setOnClickListener(this);
        binding.upiLayout.setOnClickListener(this);
        getPaymentModeAndCards();
        savedCardsAdapter = new SavedCardsAdapter(getContext(), savedCardsList, "DC");
        binding.savedCardsRecycler.setAdapter(savedCardsAdapter);
        binding.savedCardsRecycler.addItemDecoration(new RecyclerUtils.LinePagerIndicatorDecoration());
        new RecyclerUtils.ScrollByOneItem().attachToRecyclerView(binding.savedCardsRecycler);
    }

    private void observerViewModel() {
        //observer for saved cards
        payModeViewModel.getSavedCardsLiveData().observe(Objects.requireNonNull(getActivity()), new Observer<List<SavedCards>>() {
            @Override
            public void onChanged(List<SavedCards> savedCards) {
                PaymentOptionsListFragment.this.savedCardsList.clear();
                PaymentOptionsListFragment.this.savedCardsList.addAll(savedCards);
                binding.savedCardsTvSdk.setVisibility(View.VISIBLE);
                binding.savedCardsRecycler.setVisibility(View.VISIBLE);
                savedCardsAdapter.notifyDataSetChanged();
            }
        });

        //observer for payment modes
        payModeViewModel.getPaymentModeLiveData().observe(Objects.requireNonNull(getActivity()), new Observer<List<PaymentMode>>() {
            @Override
            public void onChanged(List<PaymentMode> paymentMode) {
                PaymentOptionsListFragment.this.paymentModeList.clear();
                clearLists();
                PaymentOptionsListFragment.this.paymentModeList.addAll(paymentMode);
                invalidatePaymentMethods();
            }
        });
    }

    private void clearLists(){
        creditCardList.clear();
        debitCardList.clear();
        upiList.clear();
        walletList.clear();
        netBankingList.clear();
    }

    private void invalidatePaymentMethods() {
        if (binding != null) {
            binding.netBankingLayout.setVisibility(View.GONE);
            binding.cardLayout.setVisibility(View.GONE);
            binding.upiLayout.setVisibility(View.GONE);
            binding.walletLayout.setVisibility(View.GONE);
            binding.payLaterLayout.setVisibility(View.GONE);
            binding.emiLayout.setVisibility(View.GONE);
            if (paymentModeList != null) {
                for (PaymentMode paymentMode : paymentModeList) {
                    if (paymentMode.getPaymentMode().equals(getString(R.string.net_banking))) {
                        binding.netBankingLayout.setVisibility(View.VISIBLE);
                        netBankingPayModeId = paymentMode.getPayModeId();
                        netBankingList.addAll(paymentMode.getPaymentModeDetailsList());
                    } else if (paymentMode.getPaymentMode().equals(getString(R.string.credit_card)) ||
                            paymentMode.getPaymentMode().equals(getString(R.string.debit_card))) {
                        debitCardPayModeId = paymentMode.getPayModeId();
                        binding.cardLayout.setVisibility(View.VISIBLE);
                        creditCardList.addAll(paymentMode.getPaymentModeDetailsList());
                    } else if (paymentMode.getPaymentMode().equals(getString(R.string.upi))) {
                        upiPayModeId = paymentMode.getPayModeId();
                        binding.upiLayout.setVisibility(View.VISIBLE);
                        upiList.addAll(paymentMode.getPaymentModeDetailsList());
                    } else if (paymentMode.getPaymentMode().equalsIgnoreCase(getString(R.string.wallets))) {
                        walletPayModeId = paymentMode.getPayModeId();
                        binding.walletLayout.setVisibility(View.VISIBLE);
                        walletList.addAll(paymentMode.getPaymentModeDetailsList());
                    } else if (paymentMode.getPaymentMode().equals(getString(R.string.emi))) {
                        binding.emiLayout.setVisibility(View.VISIBLE);
                    } else if (paymentMode.getPaymentMode().equals(getString(R.string.pay_later))) {
                        binding.payLaterLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
            binding.paymentOptionsContainer.setVisibility(View.VISIBLE);
        }
    }

    private void getPaymentModeAndCards() {
        //for getting saved cards
        JsonObject savedCardsObject = new JsonObject();
        savedCardsObject.addProperty("me_id", SessionStore.merchantId);
        payModeViewModel.getSavedCards(savedCardsObject);

        //for getting payment modes
        JsonObject payModeObject = new JsonObject();
        payModeObject.addProperty("me_id", SessionStore.merchantId);
        payModeViewModel.getPayMode(payModeObject);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.wallet_layout) {
            activity.loadFragment(new WalletsFragment(walletList, walletPayModeId), true);
        } else if (id == R.id.net_banking_layout) {
            activity.loadFragment(new NetBankingFragment(netBankingList, netBankingPayModeId), true);
        } /*else if (id == R.id.emi_layout) {
            activity.loadFragment(new EMIFragment(), true);
        }*/ else if (id == R.id.upi_layout) {
            activity.loadFragment(new UpiFragment(upiList, upiPayModeId), true);
        } else if (id == R.id.card_layout) {
            activity.loadFragment(CardFragment.getCardForm(CardFragment.Mode.DebitCard, savedCardsList, debitCardPayModeId), true);
        }
    }

    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }
}
