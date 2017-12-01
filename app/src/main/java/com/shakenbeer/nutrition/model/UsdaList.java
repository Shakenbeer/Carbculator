package com.shakenbeer.nutrition.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsdaList {

    @SerializedName("q")
    private String query;

    private int start;

    private int end;

    private int total;

    @SerializedName("item")
    private List<UsdaItem> items;

    public String getQuery() {
        return query;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getTotal() {
        return total;
    }

    public List<UsdaItem> getItems() {
        return items;
    }
}
