package com.example.project3_test1.ShopFragments;

public class ShoppingItem {
    private int shopImage;
    private String shopItem;
    private String shopPoint;

    public int getShopImage() {
        return shopImage;
    }

    public void setShopImage(int shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopItem() {
        return shopItem;
    }

    public void setShopItem(String shopItem) {
        this.shopItem = shopItem;
    }

    public String getShopPoint() {
        return shopPoint;
    }

    public void setShopPoint(String shopPoint) {
        this.shopPoint = shopPoint;
    }

    public ShoppingItem() {
    }

    public ShoppingItem(int shopImage, String shopItem, String shopPoint) {
        this.shopImage = shopImage;
        this.shopItem = shopItem;
        this.shopPoint = shopPoint;
    }
}
