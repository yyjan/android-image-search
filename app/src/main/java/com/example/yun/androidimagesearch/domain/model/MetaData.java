package com.example.yun.androidimagesearch.domain.model;

import com.google.gson.annotations.SerializedName;

public class MetaData {

    @SerializedName("totalCount")
    private int total_count;

    @SerializedName("pageableCount")
    private int pageable_count;

    @SerializedName("isEnd")
    private boolean is_end;

    public boolean isEnd() {
        return is_end;
    }
}
