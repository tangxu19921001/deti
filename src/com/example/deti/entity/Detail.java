package com.example.deti.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/7/20.
 */
public class Detail {
    private int id;
    private List<GoodsDetailImage> imageList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GoodsDetailImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<GoodsDetailImage> imageList) {
        this.imageList = imageList;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }

    public String getFabricDescription() {
        return fabricDescription;
    }

    public void setFabricDescription(String fabricDescription) {
        this.fabricDescription = fabricDescription;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public String getFabricCode() {
        return fabricCode;
    }

    public void setFabricCode(String fabricCode) {
        this.fabricCode = fabricCode;
    }

    private int price;
    private String color;
    private String fabricName;
    private String fabricDescription;
    private String colorHex;
    private String fabricCode;
}
