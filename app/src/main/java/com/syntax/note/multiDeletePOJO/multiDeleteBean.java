package com.syntax.note.multiDeletePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class multiDeleteBean {
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
