package com.chailijun.joke.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JokeBean {

    @SerializedName("data")
    private List<Item> data;

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }
}
