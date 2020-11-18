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

public class WalletsAdapter extends RecyclerView.Adapter<WalletsAdapter.WalletsViewHolder> {

    private Context context;
    private List<PaymentMode.PaymentModeDetailsList> paymentWalletList;
    private CustomBankItemBinding binding;
    private String payModeId;

    public WalletsAdapter(Context context, List<PaymentMode.PaymentModeDetailsList> paymentWalletList, String payModeId) {
        this.payModeId = payModeId;
        this.context = context;
        this.paymentWalletList = paymentWalletList;
    }

    @NonNull
    @Override
    public WalletsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CustomBankItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WalletsViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull WalletsViewHolder holder, int position) {
        PaymentMode.PaymentModeDetailsList item = paymentWalletList.get(position);
        binding.itemName.setText(item.getPgDetailsResponse().getPgName());
        if (item.getSelected())
            binding.selectedIv.setVisibility(View.VISIBLE);
        else binding.selectedIv.setVisibility(View.GONE);
        Glide.with(context).load(item.getPgDetailsResponse().getPgIcon()).into(binding.bankImageSdk);
    }

    public void selectCard(int position) {
        for (int i = 0; i < paymentWalletList.size(); i++) {
            if (i == position)
                paymentWalletList.get(position).setSelected(true);
            else paymentWalletList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return paymentWalletList.size();
    }

    class WalletsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public WalletsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding.bankItemContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bank_item_container){
                int pos = getAdapterPosition();
                selectCard(pos);
                try {
                SessionStore.PG_ID = paymentWalletList.get(pos).getPgDetailsResponse().getPgId();
                SessionStore.SCHEME_ID = paymentWalletList.get(pos).getSchemeDetailsResponse().getSchemeId();
                SessionStore.PAYMODE_ID = payModeId;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
