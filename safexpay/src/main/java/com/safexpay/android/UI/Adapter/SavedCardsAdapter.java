package com.safexpay.android.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safexpay.android.Model.SavedCards;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.CustomCardItemBinding;

import java.util.List;

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {
    private Context context;
    private List<SavedCards> savedCardsList;
    private CustomCardItemBinding binding;
    private String payModeId;

    public SavedCardsAdapter(Context context, List<SavedCards> savedCardsList, String payModeId) {
        this.savedCardsList = savedCardsList;
        this.payModeId = payModeId;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CustomCardItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SavedCardsViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SavedCardsViewHolder holder, int position) {
        SavedCards savedCard = savedCardsList.get(position);
        if (savedCard != null) {

//            binding.savedCardTvSdk.setText(savedCard.getNameOnCard());
//            binding.savedCardTvDescSdk.setText(String.format("%s %s** **** %s", savedCard.getFirst6Digits().substring(0, 4),
//                    savedCard.getFirst6Digits().substring(4), savedCard.getLast4Digits()));
//            binding.savedCardExpDateEtSdk.setText(String.format("%s/%s", savedCard.getExpiryMonth(), savedCard.getExpiryYear()));

        }
    }

    @Override
    public int getItemCount() {
        return savedCardsList.size();
    }

    class SavedCardsViewHolder extends RecyclerView.ViewHolder {
        public SavedCardsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
