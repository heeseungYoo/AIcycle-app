package com.example.project3_test1.ShopFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project3_test1.PersonFragments.LockScreenActivity;
import com.example.project3_test1.PersonFragments.PersonalInfoRequest;
import com.example.project3_test1.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShopFragment extends Fragment implements ShopAdapter.OnListItemLongSelectedInterface, ShopAdapter.OnListItemSelectedInterface {

    private ArrayList<ShoppingItem> mArrayList;
    private ShopAdapter mAdapter;
    RecyclerView mRecyclerView;
    private View v;
    private String userID;
    private int totalPoint;
    private AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_shop, container, false);

        Intent intent = getActivity().getIntent();
        userID = intent.getStringExtra("userID");

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

        mRecyclerView = v.findViewById(R.id.recycle_shop_list);
        int numberOfColumns = 2;
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(v.getContext(), numberOfColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new ShopAdapter(v.getContext(), mArrayList, this, this);
        mRecyclerView.setAdapter(mAdapter);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
        //        mGridLayoutManager.getOrientation());
        //mRecyclerView.addItemDecoration(dividerItemDecoration);

        getShopList();
        mAdapter.notifyDataSetChanged();

        return v;
    }

    public int getRecyclePoint() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                JSONArray jsonResponse = new JSONArray(response);
                totalPoint = jsonResponse.getJSONObject(0).getInt("totalPoints");
                    final int product = Integer.parseInt(viewHolder.shopPoint.getText().toString());
                    if (totalPoint >= product) {
                        String jsonString = "";
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();

                        try {
                            jsonObject.put("type", "shop");
                            jsonObject.put("date", "2020-01-15");
                            jsonObject.put("point", -product);

                            jsonArray.put(jsonObject);

                            jsonString = jsonObject.toString();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getContext(), viewHolder.shopPoint.getText() + "point 소모되었습니다.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        //db point 소모
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                //Toast.makeText(v.getContext(), product + "point --", Toast.LENGTH_LONG).show();
                                System.out.println("-----response-----" + response);

                            }
                        };

                        PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(userID, jsonString, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        queue.add(personalInfoRequest);
                    }
                    if (totalPoint < product){
                        Toast.makeText(getContext(), "포인트가 부족하여 구매할 수 없습니다.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
            } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(personalInfoRequest);
        return totalPoint;
    }



    private ArrayList<ShoppingItem> getShopList() {
        ShoppingItem shoppingItem = new ShoppingItem(R.drawable.crape, "스페로스페라 크레이프 케이크", "22000");
        ShoppingItem shoppingItem1 = new ShoppingItem(R.drawable.nintendo, "닌텐도 스위치", "150000");
        ShoppingItem shoppingItem2 = new ShoppingItem(R.drawable.ic_mail_outline_black_24dp, "기부하기", "10");
        ShoppingItem shoppingItem3 = new ShoppingItem(R.drawable.lion, "썸머 라이언", "30,000");
        ShoppingItem shoppingItem4 = new ShoppingItem(R.drawable.gongcha, "공차 브라운 슈가 쥬얼리 밀크티", "5000");
        ShoppingItem shoppingItem5 = new ShoppingItem(R.drawable.sulbing, "설빙 애플망고치즈설빙", "8000");
        ShoppingItem shoppingItem6 = new ShoppingItem(R.drawable.jordy, "죠르디 인형", "999,999");
        mArrayList.add(shoppingItem);
        mArrayList.add(shoppingItem1);
        mArrayList.add(shoppingItem2);
        mArrayList.add(shoppingItem3);
        mArrayList.add(shoppingItem4);
        mArrayList.add(shoppingItem5);
        mArrayList.add(shoppingItem6);
        return mArrayList;
    }

    private ShopAdapter.ShopViewHolder viewHolder;

    @Override
    public void onItemSelected(final View v, int position) {
        viewHolder = (ShopAdapter.ShopViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        dialog = builder.setMessage(viewHolder.shopPoint.getText() + "point가 소모됩니다 구매하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getRecyclePoint();
                        /*
                        final int product = Integer.parseInt(viewHolder.shopPoint.getText().toString());
                        if (mypoint >= product) {
                            String jsonString = "";
                            JSONObject jsonObject = new JSONObject();
                            JSONArray jsonArray = new JSONArray();

                            try {
                                jsonObject.put("type", "shop");
                                jsonObject.put("date", "2020-01-15");
                                jsonObject.put("point", -product);

                                jsonArray.put(jsonObject);

                                jsonString = jsonObject.toString();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getContext(), viewHolder.shopPoint.getText() + "point 소모되었습니다.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            //db point 소모
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(v.getContext(), product + "point --", Toast.LENGTH_LONG).show();
                                    System.out.println("-----response-----" + response);

                                }
                            };

                            PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(userID, jsonString, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(v.getContext());
                            queue.add(personalInfoRequest);
                        }
                        if (mypoint < product){
                            Toast.makeText(getContext(), "포인트가 부족하여 구매할 수 없습니다.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }*/
                    }
                })
                .setNegativeButton("취소", noButtonClickListener)
                .create();
        dialog.show();
    }

    private DialogInterface.OnClickListener noButtonClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    @Override
    public void onItemLongSelected(View v, int position) {

    }
}
