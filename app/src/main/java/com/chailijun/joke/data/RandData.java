package com.chailijun.joke.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RandData {

    @SerializedName("reason")
    private String reason;
    @SerializedName("error_code")
    private int error_code;

    @SerializedName("result")
    private List<Item> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<Item> getResult() {
        return result;
    }

    public void setResult(List<Item> result) {
        this.result = result;
    }
}
