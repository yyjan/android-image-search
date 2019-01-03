package com.example.yun.androidimagesearch.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseData {

    @SerializedName("meta")
    private MetaData metaData;

    @SerializedName("documents")
    private ArrayList<Document> documents;

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public MetaData getMetaData() {
        return metaData;
    }
}
