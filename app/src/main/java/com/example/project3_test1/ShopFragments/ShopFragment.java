package com.example.project3_test1.ShopFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project3_test1.R;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    private ArrayList<ShoppingItem> mArrayList;
    private ShopAdapter mAdapter;
    private View v;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_shop, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArrayList.clear();
                mAdapter.notifyDataSetChanged();

                getShopList();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView mRecyclerView = v.findViewById(R.id.recycle_shop_list);
        int numberOfColumns = 2;
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(v.getContext(), numberOfColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new ShopAdapter(v.getContext(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        getShopList();
        mAdapter.notifyDataSetChanged();

        return v;
    }

    public ArrayList<ShoppingItem> getShopList() {
        ShoppingItem shoppingItem = new ShoppingItem(R.drawable.crape, "스페로스페라 크레이프 케이크", "22,000");
        ShoppingItem shoppingItem1 = new ShoppingItem(R.drawable.nintendo, "닌텐도 스위치", "150,000");
        mArrayList.add(shoppingItem);
        mArrayList.add(shoppingItem1);
        return mArrayList;
    }
}
