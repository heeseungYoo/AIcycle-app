package com.example.project3_test1.RecycleFragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3_test1.R;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.Locale;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    private ArrayList<RecyclePoint> mList;
    private Context mContext;
    private int recycle_icon[] = {R.drawable.recycle_plastic, R.drawable.recycle_metal, R.drawable.recycle_glass, R.drawable.ic_lock_open, R.drawable.ic_shopping_basket_black_24dp};


    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        protected ImageView recycleIcon;
        protected TextView recycleItem;
        protected TextView recycleTime;
        protected TextView recyclePoint;

        public RecycleViewHolder(View view) {
            super(view);
            this.recycleIcon = view.findViewById(R.id.person_recycle_item);
            this.recycleItem = view.findViewById(R.id.person_recycle_item_text);
            this.recycleTime = view.findViewById(R.id.person_recycle_item_time);
            this.recyclePoint = view.findViewById(R.id.person_recycle_item_point);
        }
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_recycle_point_list, viewGroup, false);
        RecycleViewHolder recycleViewHolder = new RecycleViewHolder(view);

        return recycleViewHolder;
    }

    public RecycleAdapter(Context context, ArrayList<RecyclePoint> list) {
        mList = list;
        mContext = context;

    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder recycleViewHolder, int position) {

        recycleViewHolder.recycleItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        recycleViewHolder.recycleItem.setGravity(Gravity.CENTER);
        recycleViewHolder.recycleItem.setText(mList.get(position).getRecycleItem());

        recycleViewHolder.recycleTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        recycleViewHolder.recycleTime.setGravity(Gravity.CENTER);
        recycleViewHolder.recycleTime.setText(mList.get(position).getRecycleTime());

        recycleViewHolder.recyclePoint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        recycleViewHolder.recyclePoint.setGravity(Gravity.CENTER);
        recycleViewHolder.recyclePoint.setText(String.format(Locale.KOREA,"%d", mList.get(position).getRecyclePoint()));

        //recycleViewHolder.recycleIcon.setImageDrawable(mList.get(position).getRecycleIcon());
        recycleViewHolder.recycleIcon.setImageResource(recycle_icon[mList.get(position).getRecycleIcon()]);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
