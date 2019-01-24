package com.example.ed139.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ed139.myapplication.R;
import com.example.ed139.myapplication.database.ReceiptEntity;

import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder>{

    // Member variable to handle item clicks
    // final private ItemClickListener mItemClickListener;
    private List<ReceiptEntity> mReceiptsList;
    Context mContext;

    public ReceiptsAdapter(Context context, List<ReceiptEntity> receiptsList) {
        mContext = context;
        mReceiptsList = receiptsList;
    }

    public void updateList(List<ReceiptEntity> items) {
        this.mReceiptsList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiptsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.receipts_rv_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mReceiptsList!=null) {
            return mReceiptsList.size();
        } else{
            return 0;
        }
    }
//
//    public interface ItemClickListener {
//        void onItemClickListener(int receiptId);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView priceTv;
        private TextView locationTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.today_image_view);
            priceTv = (TextView) itemView.findViewById(R.id.price_tv);
            locationTv = (TextView) itemView.findViewById(R.id.location_tv);
        }

        @Override
        public void onClick(View v) {
//            int receiptId = mReceiptsList.get(getAdapterPosition()).getId();
//            mItemClickListener.onItemClickListener(receiptId);
        }
    }

    public void setReceiptData(List<ReceiptEntity> receiptData) {
        mReceiptsList = receiptData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher_round);
        viewHolder.priceTv.setText(String.valueOf(mReceiptsList.get(i).getPrice()));
        viewHolder.locationTv.setText(mReceiptsList.get(i).getLocation());
    }
}
