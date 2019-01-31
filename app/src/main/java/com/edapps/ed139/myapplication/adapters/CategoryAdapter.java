package com.edapps.ed139.myapplication.adapters;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edapps.ed139.myapplication.database.MainViewModel;
import com.edapps.ed139.myapplication.R;
import com.edapps.ed139.myapplication.database.AppDatabase;
import com.edapps.ed139.myapplication.database.CategoryModel;
import com.edapps.ed139.myapplication.database.ReceiptEntity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements ReceiptsAdapter.ItemClickListener{

    public static final String LOG_TAG = "CategoryAdapter";
    private ReceiptsAdapter.ItemClickListener mListener;
    private List<CategoryModel> mCategoriesList;
    private List<ReceiptEntity> mReceiptslist;
    private ReceiptsAdapter receiptsAdapter;
    private Context mContext;
    private AppDatabase mDb;

    public CategoryAdapter(Context context, List<CategoryModel> categoriesList, ReceiptsAdapter.ItemClickListener listener) {
        mContext = context;
        mCategoriesList = categoriesList;
        mListener = listener;
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
    public void onItemClickListener(int itemId){}

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTv;
        private RecyclerView receiptRv;

        private ViewHolder(View itemView) {
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
        final String categoryName = mCategoriesList.get(i).getCategory();
        String categoryNamePlusColon = categoryName + ": ";
        holder.categoryTv.setText(categoryNamePlusColon);

        MainViewModel viewModel = ViewModelProviders.of((FragmentActivity) mContext).get(MainViewModel.class);
        viewModel.getReceipts().observe((FragmentActivity) mContext, new Observer<List<ReceiptEntity>>() {
            @Override
            public void onChanged(@Nullable List<ReceiptEntity> receiptEntities) {
                holder.setData(receiptEntities);
            }
        });
//        LiveData<List<ReceiptEntity>> receipts = mDb.receiptDao().findReceiptsForCategory(categoryName);
//        receipts.observe(((FragmentActivity) mContext), new Observer<List<ReceiptEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<ReceiptEntity> receiptEntities) {
//                Log.d(LOG_TAG, "Receiving data from view model");
//                receiptEntities = mReceiptslist;
//
//            }
//        });
        holder.receiptRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.receiptRv.setHasFixedSize(true);
        holder.receiptRv.setNestedScrollingEnabled(true);
        holder.receiptRv.setAdapter(receiptsAdapter);
    }
}
