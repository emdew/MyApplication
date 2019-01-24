package com.example.ed139.myapplication.adapters;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements LifecycleOwner {

    private LifecycleRegistry mLifecycleRegistry;
    List<CategoryModel> mCategoriesList;
    private List<ReceiptEntity> mReceiptslist;
    ReceiptsAdapter receiptsAdapter;
    RecyclerView mRecyclerView;
    Context mContext;
    AppDatabase mDb;

    public CategoryAdapter(Context context, List<CategoryModel> categoriesList) {
        mContext = context;
        mCategoriesList = categoriesList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_rv_item, parent, false);
        mDb = AppDatabase.getInstance(mContext);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    public LifecycleRegistry getmLifecycleRegistry() {
        return mLifecycleRegistry;
    }

    @Override
    public int getItemCount() {
        return mCategoriesList.size();
    }

    public void setCategoryData(List<CategoryModel> categoryData) {
        mCategoriesList = categoryData;
        notifyDataSetChanged();
    }

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
        holder.categoryTv.setText(mCategoriesList.get(i).getCategory());
        final String categoryName = mCategoriesList.get(i).getCategory();
//        MainViewModel viewModel = ViewModelProviders.of((FragmentActivity)mContext).get(MainViewModel.class);
//        viewModel.getReceipts().observe(this, new Observer<List<ReceiptEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<ReceiptEntity> receiptEntities) {
//                holder.setData(receiptEntities);
//                holder.receiptRv.setHasFixedSize(true);
//                holder.receiptRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//                holder.receiptRv.setNestedScrollingEnabled(true);
//                holder.receiptRv.setAdapter(receiptsAdapter);
//            }
//        });
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mReceiptslist = mDb.receiptDao().findReceiptsForCategory(categoryName);
                holder.setData(mReceiptslist);
                holder.receiptRv.setHasFixedSize(true);
                holder.receiptRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.receiptRv.setNestedScrollingEnabled(true);
                holder.receiptRv.setAdapter(receiptsAdapter);
            }
        });
    }
}
