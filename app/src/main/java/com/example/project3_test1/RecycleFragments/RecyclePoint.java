package com.example.project3_test1.RecycleFragments;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public class RecyclePoint {
    private int recycleIcon; // int로 해도 될듯
    private String recycleItem;
    private String recycleTime;
    private int recyclePoint;

    public int getRecycleIcon() {
        return recycleIcon;
    }

    public void setRecycleIcon(int recycleIcon) {
        this.recycleIcon = recycleIcon;
    }

    public String getRecycleItem() {
        return recycleItem;
    }

    public void setRecycleItem(String recycleItem) {
        this.recycleItem = recycleItem;
    }

    public String getRecycleTime() {
        return recycleTime;
    }

    public void setRecycleTime(String recycleTime) {
        this.recycleTime = recycleTime;
    }

    public int getRecyclePoint() {
        return recyclePoint;
    }

    public void setRecyclePoint(int recyclePoint) {
        this.recyclePoint = recyclePoint;
    }

    public RecyclePoint() {
    }

    public RecyclePoint(int recycleIcon, String recycleItem, String recycleTime, int recyclePoint) {
        this.recycleIcon = recycleIcon;
        this.recycleItem = recycleItem;
        this.recycleTime = recycleTime;
        this.recyclePoint = recyclePoint;
    }
}
