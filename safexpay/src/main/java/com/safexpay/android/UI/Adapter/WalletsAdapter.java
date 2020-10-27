package com.safexpay.android.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
//        binding.bankImageSdk.setImageDrawable(context.getResources().getDrawable(item.getDrawable()));
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
                binding.bankItemContainer.setBackgroundColor(Color.parseColor(SessionStore.menuColor));
                SessionStore.PG_ID = paymentWalletList.get(getAdapterPosition()).getPgDetailsResponse().getPgId();
                SessionStore.SCHEME_ID = paymentWalletList.get(getAdapterPosition()).getSchemeDetailsResponse().getSchemeId();
                SessionStore.PAYMODE_ID = payModeId;
            }
        }
    }
}
