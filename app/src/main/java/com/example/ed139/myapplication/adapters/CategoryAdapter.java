package com.example.ed139.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ed139.myapplication.AppExecutors;
import com.example.ed139.myapplication.R;
import com.example.ed139.myapplication.database.AppDatabase;
import com.example.ed139.myapplication.database.CategoryModel;
import com.example.ed139.myapplication.database.ReceiptEntity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements ReceiptsAdapter.ItemClickListener {

    ReceiptsAdapter.ItemClickListener mListener;
    List<CategoryModel> mCategoriesList;
    private List<ReceiptEntity> mReceiptslist;
    ReceiptsAdapter receiptsAdapter;
    RecyclerView mRecyclerView;
    Context mContext;
    AppDatabase mDb;

    public CategoryAdapter(Context context, List<CategoryModel> categoriesList, ReceiptsAdapter.ItemClickListener listener) {
        mContext = context;
        mCategoriesList = categoriesList;
        mListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_rv_item, parent, false);
        mDb = AppDatabase.getInstance(mContext);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mCategoriesList.size();
    }

    public void setCategoryData(List<CategoryModel> categoryData) {
        mCategoriesList = categoryData;
        notifyDataSetChanged();
    }

    @Override
    public void onItemClickListener(int itemId) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTv;
        RecyclerView receiptRv;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryTv = itemView.findViewById(R.id.category_tv);
            receiptRv = itemView.findViewById(R.id.receipts_rv);
            receiptsAdapter = new ReceiptsAdapter(mContext, mReceiptslist, mListener);
        }

        public void setData(List<ReceiptEntity> list) {
            receiptsAdapter.setReceiptData(list);
        }
    }

    @Override
    public void onBindViewHolder(final @NonNull CategoryAdapter.ViewHolder holder, final int i) {
        holder.categoryTv.setText(mCategoriesList.get(i).getCategory());
        final String categoryName = mCategoriesList.get(i).getCategory();

        holder.receiptRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.receiptRv.setHasFixedSize(true);
        holder.receiptRv.setNestedScrollingEnabled(true);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mReceiptslist = mDb.receiptDao().findReceiptsForCategory(categoryName);
                holder.receiptRv.setAdapter(receiptsAdapter);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        holder.setData(mReceiptslist);
                    }
                });
            }
        });
    }
}
