package com.safexpay.android.UI.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.safexpay.android.Model.SavedCards;
import com.safexpay.android.R;
import com.safexpay.android.UI.Activity.BaseActivity;
import com.safexpay.android.UI.Activity.PaymentDetailActivity;
import com.safexpay.android.UI.Adapter.SavedCardsAdapter;
import com.safexpay.android.Utils.CardTypes;
import com.safexpay.android.Utils.CardUtils;
import com.safexpay.android.Utils.RecyclerUtils;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.FragmentCardBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends BaseFragment implements View.OnClickListener, View.OnKeyListener {

    private FragmentCardBinding binding;
    private PaymentDetailActivity activity;
    private Mode mode;
    private int selectedTenure;
    private String selectedBankCode;
    private static final String FRAGMENT_NAME = "CardFragment";
    private int cardNumberSelection;
    private CustomTextWatcher textWatcher;
    private String payModeId;
    private List<SavedCards> savedCardsList = new ArrayList<>();


    public CardFragment() {
        // Required empty public constructor
    }

    public static CardFragment getCardForm(Mode mode) {
        CardFragment form = new CardFragment();
        form.mode = mode;
        return form;
    }

    public static CardFragment getCardForm(Mode mode, List<SavedCards> savedCardsList, String payModeId) {
        CardFragment form = new CardFragment();
        form.mode = mode;
        form.savedCardsList = savedCardsList;
        form.payModeId = payModeId;
        return form;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardBinding.inflate(inflater, container, false);
        activity = (PaymentDetailActivity) getActivity();
        init();
        return binding.getRoot();
    }

    private void init() {
        //setting recyclerview
        binding.savedCardsRecycler.setAdapter(new SavedCardsAdapter(getContext(), savedCardsList, payModeId));
        binding.savedCardsRecycler.addItemDecoration(new RecyclerUtils.LinePagerIndicatorDecoration());
        new RecyclerUtils.ScrollByOneItem().attachToRecyclerView(binding.savedCardsRecycler);

        //click listeners
        binding.navBackCard.setOnClickListener(this);
        binding.savedCardSdkTv.setOnClickListener(this);
        binding.showHideArrowIvCards.setOnClickListener(this);

        //editboxes
        binding.cardNumberEtSdk.setNextFocusDownId(R.id.month_et_sdk);
        binding.cardNumberEtSdk.addTextChangedListener(textWatcher = new CustomTextWatcher());
        binding.cardNumberEtSdk.setOnKeyListener(this);
        binding.monthEtSdk.setNextFocusDownId(R.id.cvv_et_sdk);
        binding.monthEtSdk.addTextChangedListener(new TextWatcher() {
            private int previousLength = 0, currentLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = binding.monthEtSdk.getText().toString().trim().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentLength = binding.monthEtSdk.getText().toString().trim().length();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String date = s.toString().trim().replaceAll(" ", "");
                String modifiedDate = date;

                boolean backPressed = previousLength > currentLength;
                if (backPressed) {
                    if (date.length() == 3) {
                        modifiedDate = date.substring(0, 2);
                    }
                } else {
                    if (date.length() == 2) {
                        modifiedDate = date + "/";
                    } else if (previousLength == 2) {
                        modifiedDate = date.substring(0, 2) + "/" + date.substring(2, date.length());
                    }
                }

                applyDateText(binding.monthEtSdk, this, modifiedDate);
                if (modifiedDate.length() == 5) {
                    binding.cvvEtSdk.requestFocus();
                }
            }
        });

        binding.cardNumberEtSdk.post(new Runnable() {
            @Override
            public void run() {
                binding.cardNumberEtSdk.requestFocus();
                InputMethodManager lManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (lManager != null) {
                    lManager.showSoftInput(binding.cardNumberEtSdk, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    private void slide_down() {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        if (a != null) {
            a.reset();
            binding.savedCardsRecycler.clearAnimation();
            binding.savedCardsRecycler.startAnimation(a);
        }
    }

    public void toggle_contents(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            v.animate().alpha(0.0f).setDuration(0);
            v.setVisibility(View.GONE);
            binding.showHideArrowIvCards.setRotation(90);
        } else {
            v.setVisibility(View.VISIBLE);
            v.animate().alpha(1.0f).setDuration(0);
            binding.showHideArrowIvCards.setRotation(-90);
            slide_down();
        }
    }

    private void applyDateText(TextInputEditText dateBox, TextWatcher textWatcher, String modifiedDate) {
        dateBox.removeTextChangedListener(textWatcher);
        dateBox.setText(modifiedDate);
        dateBox.setSelection(modifiedDate.length());
        dateBox.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.saved_card_sdk_tv || id == R.id.show_hide_arrow_iv_cards) {
            toggle_contents(binding.savedCardsRecycler);
        } else if (id == R.id.nav_back_card) {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
                SessionStore.clearPaymentIds();
            }
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hideKeyboard();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                binding.cardNumberEtSdk.removeTextChangedListener(textWatcher);
                return true;
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                binding.cardNumberEtSdk.addTextChangedListener(textWatcher);
                return true;
            }
        }
        return false;
    }

    enum Mode {
        DebitCard,
        CreditCard,
        EMI
    }

    private class CustomTextWatcher implements TextWatcher {

        private int drawable = 0;
        private int previousLength = 0, currentLength = 0;
        private CardTypes cardType = CardTypes.UNKNOWN;

        public CustomTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            previousLength = binding.cardNumberEtSdk.getText().toString().trim().length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String cardNumber = binding.cardNumberEtSdk.getText().toString();
            cardNumber = cardNumber.replaceAll(" ", "");

            // TODO this will be triggered for every text change which may not be required
            // Do this only for few characters not for entire card number
            cardType = CardUtils.getCardType(cardNumber);
            drawable = cardType.getImageResource();

            /*if (cardType == CardTypes.UNKNOWN || cardType == CardTypes.MAESTRO) {
                clearOptionalValidators();
            } else {
                addOptionalValidators();
            }*/

            if (cardNumber.isEmpty()) {
                drawable = R.drawable.ic_accepted_cards;
            }

            binding.cardNumberEtSdk.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);

            if (cardNumber.length() == cardType.getNumberLength()) {
                binding.monthEtSdk.requestFocus();
            }
            currentLength = binding.cardNumberEtSdk.getText().toString().trim().length();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (currentLength < previousLength) {
                return;
            }
            String cardNumber = s.toString().trim();
            cardNumberSelection = binding.cardNumberEtSdk.getSelectionStart();
            if (cardNumber.length() < 4) {
                return;
            }

            String modifiedCard;
            if (currentLength > previousLength) {
                String[] data = cardNumber.replaceAll(" ", "").split("");
                modifiedCard = "";
                CardTypes cardType = CardUtils.getCardType(cardNumber);
                switch (cardType) {
                    case VISA:
                    case MASTER_CARD:
                    case DISCOVER:
                    case RUPAY:
                        for (int index = 1; index < data.length; index++) {
                            modifiedCard = modifiedCard + data[index];
                            if (index == 4 || index == 8 || index == 12) {
                                modifiedCard = modifiedCard + " ";
                                cardNumberSelection++;
                            }
                        }

                        binding.cvvEtSdk.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cardType.getCvvLength())});
                        break;
                    case AMEX:
                        for (int index = 1; index < data.length; index++) {
                            modifiedCard = modifiedCard + data[index];
                            if (index == 4 || index == 11) {
                                modifiedCard = modifiedCard + " ";
                                cardNumberSelection++;
                            }
                        }
                        binding.cvvEtSdk.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cardType.getCvvLength())});
                        break;
                    case DINERS_CLUB:
                        for (int index = 1; index < data.length; index++) {
                            modifiedCard = modifiedCard + data[index];
                            if (index == 4 || index == 10) {
                                modifiedCard = modifiedCard + " ";
                                cardNumberSelection++;
                            }
                        }
                        binding.cvvEtSdk.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cardType.getCvvLength())});
                        break;
                    default:
                        modifiedCard = cardNumber;
                }
            } else {
                modifiedCard = cardNumber;
            }
            applyText(binding.cardNumberEtSdk, this, modifiedCard);
        }
    }

    private void applyText(TextInputEditText editText, TextWatcher watcher, String text) {
        editText.removeTextChangedListener(watcher);
        editText.setText(text);
        cardNumberSelection = Math.min(cardNumberSelection, text.length());
        editText.setSelection(cardNumberSelection);
        editText.addTextChangedListener(watcher);
    }

    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }
}
