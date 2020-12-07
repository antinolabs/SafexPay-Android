package com.safexpay.android.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.safexpay.android.Interfaces.ItemSelectedCallback;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.R;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.CustomBankItemBinding;

import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.BankListViewHolder> {

    private final ItemSelectedCallback selectedCallback;
    private Context context;
    private List<PaymentMode.PaymentModeDetailsList> paymentBankList;
    private CustomBankItemBinding binding;
    private String payModeId;

    public BankListAdapter(Context context, List<PaymentMode.PaymentModeDetailsList> paymentBankList, String payModeId, ItemSelectedCallback selectedCallback) {
        this.context = context;
        this.payModeId = payModeId;
        this.paymentBankList = paymentBankList;
        this.selectedCallback = selectedCallback;
    }

    class BankListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public BankListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding.bankItemContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bank_item_container) {
                selectedCallback.onClick(getAdapterPosition());
            }
        }
    }

    @NonNull
    @Override
    public BankListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CustomBankItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BankListViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BankListViewHolder holder, int position) {
        PaymentMode.PaymentModeDetailsList item = paymentBankList.get(position);
        binding.itemName.setText(item.getPgDetailsResponse().getPgName());
        if (item.getSelected())
            binding.selectedIv.setVisibility(View.VISIBLE);
        else binding.selectedIv.setVisibility(View.GONE);
        Glide.with(context).load(item.getPgDetailsResponse().getPgIcon()).into(binding.bankImageSdk);
    }

    @Override
    public int getItemCount() {
        return paymentBankList.size();
    }
}
