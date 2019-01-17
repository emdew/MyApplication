package com.example.ed139.myapplication.adapters;

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
import com.example.ed139.myapplication.database.CategoryWithReceiptsPOJO;
import com.example.ed139.myapplication.database.ReceiptEntity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    //private LifecycleRegistry mLifecycleRegistry;
    List<CategoryModel> mCategoriesList;
    private List<ReceiptEntity> mReceiptslist;
    ReceiptsAdapter receiptsAdapter;
    List<CategoryWithReceiptsPOJO> categoriesWithReceipts;
    Context mContext;
    AppDatabase mDb;

    public CategoryAdapter(Context context, List<CategoryWithReceiptsPOJO> categoriesList) {
        mContext = context;
        categoriesWithReceipts = categoriesList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        mLifecycleRegistry = new LifecycleRegistry(this);
//        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
//        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_rv_item, parent, false);
        mDb = AppDatabase.getInstance(mContext);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return categoriesWithReceipts.size();
    }

    public void setCategoryData(List<CategoryModel> categoryData) {
        mCategoriesList = categoryData;
        notifyDataSetChanged();
    }

    public void setCategoryWithReceiptsData(List<CategoryWithReceiptsPOJO> categoryData) {
        categoriesWithReceipts = categoryData;
        notifyDataSetChanged();
    }

//    @NonNull
//    @Override
//    public Lifecycle getLifecycle() {
//        return mLifecycleRegistry;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTv;
        RecyclerView receiptRv;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryTv = itemView.findViewById(R.id.category_tv);
            receiptRv = itemView.findViewById(R.id.receipts_rv);
            receiptsAdapter = new ReceiptsAdapter(mContext, mReceiptslist);
        }

        public void setData(List<ReceiptEntity> list) {
            receiptsAdapter.updateList(list);
        }
    }

    @Override
    public void onBindViewHolder(final @NonNull CategoryAdapter.ViewHolder holder, final int i) {
        holder.categoryTv.setText(categoriesWithReceipts.get(i).getCategoryModel().getCategory());
        holder.receiptRv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.receiptRv.setLayoutManager(manager);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ReceiptEntity receipt = mDb.receiptDao().findRootCategory();
                int categoryId = categoriesWithReceipts.get(i).getCategoryModel().getId();
                mReceiptslist = mDb.receiptDao().findReceiptsForCategory(categoryId);
            }
        });
        holder.setData(mReceiptslist);
        holder.receiptRv.setAdapter(receiptsAdapter);
        holder.receiptRv.setNestedScrollingEnabled(true);
    }
}
