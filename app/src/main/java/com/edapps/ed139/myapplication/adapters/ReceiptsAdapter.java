package com.edapps.ed139.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edapps.ed139.myapplication.R;
import com.edapps.ed139.myapplication.database.AppDatabase;
import com.edapps.ed139.myapplication.database.ReceiptEntity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder>{

    // Member variable to handle item clicks
    private ItemClickListener mItemClickListener;
    private List<ReceiptEntity> mReceiptsList;
    private ReceiptEntity mReceipt;
    private Context mContext;
    private AppDatabase mDb;

    public interface ItemClickListener {
        void onItemClickListener(int clickedItemIndex);
    }

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
        mDb = AppDatabase.getInstance(mContext);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        // condition ? if : else
        return (mReceiptsList != null) ? mReceiptsList.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView priceTv;
        private TextView locationTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.today_image_view);
            priceTv = itemView.findViewById(R.id.price_tv);
            locationTv = itemView.findViewById(R.id.location_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int receiptId = mReceiptsList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(receiptId);
        }
    }

    @Override
    public void onBindViewHolder(final @NonNull ReceiptsAdapter.ViewHolder viewHolder, int i) {
        mReceipt = mReceiptsList.get(i);
        DecimalFormat format = new DecimalFormat("0.00");
        String imageString = mReceipt.getImage();
        if (imageString != null) {
            File file = new File(imageString);
            Picasso.get().load(file).into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageResource(R.mipmap.ic_launcher_round);
        }
        viewHolder.priceTv.setText(String.valueOf(format.format(mReceipt.getPrice())));
        viewHolder.locationTv.setText(mReceipt.getLocation());
    }
}
