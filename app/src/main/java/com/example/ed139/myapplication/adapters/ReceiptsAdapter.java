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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder>{

    // Member variable to handle item clicks
    private ItemClickListener mItemClickListener;
    private List<ReceiptEntity> mReceiptsList;
    ReceiptEntity mReceipt;
    private Context mContext;

    public ReceiptsAdapter(Context context, List<ReceiptEntity> receiptsList, final ItemClickListener listener) {
        mContext = context;
        mReceiptsList = receiptsList;
        mItemClickListener = listener;
    }

    public void setReceiptData(List<ReceiptEntity> items) {
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

    public interface ItemClickListener {
        void onItemClickListener(int clickedItemIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView priceTv;
        private TextView locationTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.today_image_view);
            priceTv = (TextView) itemView.findViewById(R.id.price_tv);
            locationTv = (TextView) itemView.findViewById(R.id.location_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int receiptId = mReceiptsList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(receiptId);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.ViewHolder viewHolder, int i) {
        mReceipt = mReceiptsList.get(i);
        String imageString = mReceipt.getImage();
        File file = new File(imageString);

        //Uri imageUri = Uri.parse(imageString);

        Picasso.get().load(file).into(viewHolder.imageView);

        //viewHolder.imageView.setImageResource(R.mipmap.ic_launcher_round);
        viewHolder.priceTv.setText(String.valueOf(mReceipt.getPrice()));
        viewHolder.locationTv.setText(mReceipt.getLocation());
    }
}
