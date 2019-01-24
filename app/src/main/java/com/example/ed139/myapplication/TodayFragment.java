package com.example.ed139.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ed139.myapplication.adapters.CategoryAdapter;
import com.example.ed139.myapplication.database.AppDatabase;
import com.example.ed139.myapplication.database.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class TodayFragment extends Fragment {

    AppDatabase mDb;
    View rootView;
    CategoryAdapter mAdapter;
    RecyclerView mMainRv;
    List<CategoryModel> mCategoriesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_today, container, false);
        //mDailyTotalTextView = rootView.findViewById(R.id.daily_total_text_view);
        mDb = AppDatabase.getInstance((getActivity().getApplicationContext()));
        setupMainUi();
        return rootView;
    }

    private void setupMainUi() {
        // get the sum of all price columns
        //mDailyTotalTextView.setText(String.valueOf(mDb.receiptDao().getSumOfPrices()));

        // Find RecyclerView and set layout
        mMainRv = (RecyclerView) rootView.findViewById(R.id.category_rv);
        mMainRv.setHasFixedSize(false);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMainRv.setLayoutManager(manager);

        // set list of CategoryModel objects on adapter
        mCategoriesList = new ArrayList<>();
        mAdapter = new CategoryAdapter(getContext(), mCategoriesList);

        // set adapter on RecyclerView
        mMainRv.setAdapter(mAdapter);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getCategories().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(@Nullable List<CategoryModel> categoryModels) {
                mAdapter.setCategoryData(categoryModels);
            }
        });
    }

//    @Override
//    public void onItemClickListener(int receiptId) {
//        Intent intent = new Intent(getContext(), EditorActivity.class);
//        intent.putExtra(EditorActivity.EXTRA_RECEIPT_ID, receiptId);
//        startActivity(intent);
//    }
}
