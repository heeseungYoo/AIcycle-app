package com.example.project3_test1.RecycleFragments;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project3_test1.PersonFragments.PersonalInfoRequest;
import com.example.project3_test1.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Locale;

public class RecycleFragment extends Fragment {

    private ArrayList<RecyclePoint> mArrayList;
    private RecycleAdapter mAdapter;
    private View v;
    private TextView RecycleUserName;
    private TextView RecycleUserPoint;
    private ImageView recycleImage;
    private String userID;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recycle, container, false);

        Intent intent = getActivity().getIntent();
        userID = intent.getStringExtra("userID");

        RecycleUserName = v.findViewById(R.id.recycle_user_name);
        RecycleUserPoint = v.findViewById(R.id.recycle_user_point);
        recycleImage = v.findViewById(R.id.person_recycle_item);

        final SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArrayList.clear();

                getRecyclePoint();
                mAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView mRecyclerView = v.findViewById(R.id.recycle_item_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new RecycleAdapter(v.getContext(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
         //       mLinearLayoutManager.getOrientation());
        //mRecyclerView.addItemDecoration(dividerItemDecoration);

        getRecyclePoint();
        mAdapter.notifyDataSetChanged();

        return v;
    }

    private ArrayList<RecyclePoint> getRecyclePoint() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    String userName = jsonResponse.getJSONObject(0).getString("name");
                    RecycleUserName.setText(userName);
                    int totalPoint = jsonResponse.getJSONObject(0).getInt("totalPoints");
                    RecycleUserPoint.setText(String.format(Locale.KOREA, "%d", totalPoint));

                    JSONArray jsonPoint = jsonResponse.getJSONObject(0).getJSONArray("points");
                    for(int i = 0; i < jsonPoint.length(); i++) {
                        RecyclePoint recyclePoint = new RecyclePoint();
                        String type = jsonPoint.getJSONObject(i).getString("type");
                        String date = jsonPoint.getJSONObject(i).getString("date");
                        int point = jsonPoint.getJSONObject(i).getInt("point");
                        int icon = 0;
                        switch (type) {
                            case "plastic":
                                icon = 0;
                                break;
                            case "metal":
                                icon = 1;
                                break;
                            case "glass":
                                icon = 2;
                                break;
                            case "unlock":
                                icon = 3;
                                break;
                            case "shop":
                                icon = 4;
                                break;
                        }
                        recyclePoint.setRecycleItem(type);
                        recyclePoint.setRecycleTime(date);
                        recyclePoint.setRecyclePoint(point);
                        recyclePoint.setRecycleIcon(icon);
                        mArrayList.add(recyclePoint);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        PersonalInfoRequest personalInfoRequest = new PersonalInfoRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(personalInfoRequest);
        return mArrayList;
    }
}
