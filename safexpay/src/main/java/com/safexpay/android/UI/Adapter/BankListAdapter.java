package com.safexpay.android.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.safexpay.android.Model.PaymentMode;
import com.safexpay.android.R;
import com.safexpay.android.Utils.SessionStore;
import com.safexpay.android.databinding.CustomBankItemBinding;

import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.BankListViewHolder> {

    private Context context;
    private List<PaymentMode.PaymentModeDetailsList> paymentBankList;
    private CustomBankItemBinding binding;
    private String payModeId;

    public BankListAdapter(Context context, List<PaymentMode.PaymentModeDetailsList> paymentBankList, String payModeId) {
        this.context = context;
        this.payModeId = payModeId;
        this.paymentBankList = paymentBankList;
    }

    class BankListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public BankListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding.bankItemContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bank_item_container) {
                int pos = getAdapterPosition();
                selectCard(pos);
                try {
                    SessionStore.PG_ID = paymentBankList.get(pos).getPgDetailsResponse().getPgId();
                    SessionStore.SCHEME_ID = paymentBankList.get(pos).getSchemeDetailsResponse().getSchemeId();
                    SessionStore.PAYMODE_ID = payModeId;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectCard(int position) {
        for (int i = 0; i < paymentBankList.size(); i++) {
            if (i == position)
                paymentBankList.get(position).setSelected(true);
            else paymentBankList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
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
