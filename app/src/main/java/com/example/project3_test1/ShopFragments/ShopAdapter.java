package com.example.project3_test1.ShopFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3_test1.R;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private ArrayList<ShoppingItem> mList;
    private Context mContext;
    private AlertDialog dialog;

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        public ImageView shopImage;
        public TextView shopItem;
        public TextView shopPoint;

        public ShopViewHolder(View view) {
            super(view);
            this.shopImage = view.findViewById(R.id.shop_item_image);
            this.shopItem = view.findViewById(R.id.shop_item_text);
            this.shopPoint = view.findViewById(R.id.shop_point);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    dialog = builder.setMessage(shopPoint.getText() + "point가 소모됩니다 구매하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext, shopPoint.getText() + "point 소모되었습니다.", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    //db point 소모
                                }
                            })
                            .setNegativeButton("취소", noButtonClickListener)
                            .create();
                    dialog.show();
                }
            });
        }
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_shop_list, viewGroup, false);
        ShopViewHolder shopViewHolder = new ShopViewHolder(view);

        return shopViewHolder;
    }

    public ShopAdapter(Context context, ArrayList<ShoppingItem> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder shopViewHolder, int position) {
        final int pos = position;

        shopViewHolder.shopItem.setText(mList.get(position).getShopItem());
        shopViewHolder.shopPoint.setText(mList.get(position).getShopPoint());
        shopViewHolder.shopImage.setImageResource(mList.get(position).getShopImage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private DialogInterface.OnClickListener yesButtonClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //db에서 point 소모

        }
    };

    private DialogInterface.OnClickListener noButtonClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };
}
