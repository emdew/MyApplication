package com.edapps.ed139.myapplication.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edapps.ed139.myapplication.AppExecutors;
import com.edapps.ed139.myapplication.R;
import com.edapps.ed139.myapplication.adapters.CategoryAdapter;
import com.edapps.ed139.myapplication.adapters.ReceiptsAdapter;
import com.edapps.ed139.myapplication.database.AppDatabase;
import com.edapps.ed139.myapplication.database.CategoryModel;
import com.edapps.ed139.myapplication.database.MainViewModel;
import com.edapps.ed139.myapplication.database.ReceiptEntity;
import com.edapps.ed139.myapplication.widget.TotalIntentService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodayFragment extends Fragment implements ReceiptsAdapter.ItemClickListener {

    private AdView mAdView;
    AppDatabase mDb;
    View rootView;
    CategoryAdapter mAdapter;
    RecyclerView mMainRv;
    List<CategoryModel> mCategoriesList;
    List<ReceiptEntity> mReceiptsList;
    TextView mTotalTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_today, container, false);
        mTotalTextView = rootView.findViewById(R.id.daily_total_text_view);
        mDb = AppDatabase.getInstance((Objects.requireNonNull(getActivity()).getApplicationContext()));
        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setupMainUi();
        return rootView;
    }

    private void setupMainUi() {

        final DecimalFormat format = new DecimalFormat("0.00");

        // get the sum of all price columns
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Double x = mDb.receiptDao().getTotal();
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (x != null) {
                            mTotalTextView.setText(String.valueOf(format.format(x)));
                            TotalIntentService.startActionUpdateTotal(getContext(), x);
                        }
                    }
                });
            }
        });

        // Find RecyclerView and set layout
        mMainRv = rootView.findViewById(R.id.category_rv);
        mMainRv.setHasFixedSize(false);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMainRv.setLayoutManager(manager);

        // set list of CategoryModel objects on adapter
        mCategoriesList = new ArrayList<>();
        mReceiptsList = new ArrayList<>();
        mAdapter = new CategoryAdapter(getContext(), mCategoriesList, this);

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

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra(EditorActivity.EXTRA_RECEIPT_ID, itemId);
        startActivity(intent);
    }
}
