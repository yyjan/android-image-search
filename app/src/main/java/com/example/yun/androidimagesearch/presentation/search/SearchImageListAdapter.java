package com.example.yun.androidimagesearch.presentation.search;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yun.androidimagesearch.R;
import com.example.yun.androidimagesearch.domain.model.Document;
import com.example.yun.androidimagesearch.presentation.GlideApp;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SearchImageListAdapter extends RecyclerView.Adapter<SearchImageListAdapter.ViewHolder> {

    private List<Document> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.onBind(viewHolder.itemView.getContext(), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<Document> items) {
        this.items.addAll(items);
    }

    public void clear() {
        this.items.clear();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View layoutContent;
        TextView tvCollection;
        TextView tvDatetime;
        ImageView ivThumb;

        ViewHolder(@NonNull View view) {
            super(view);

            ivThumb = view.findViewById(R.id.iv_thumb);
            tvCollection = view.findViewById(R.id.tv_collection);
            tvDatetime = view.findViewById(R.id.tv_date);
            layoutContent = view.findViewById(R.id.layout_content);
        }

        void onBind(Context context, int position) {
            Document item = items.get(position);

            // set image
            int placeholderColor = context.getResources().getColor(R.color.white);
            GlideApp.with(context)
                    .load(item.getImage())
                    .placeholder(new ColorDrawable(placeholderColor))
                    .transition(withCrossFade())
                    .into(ivThumb);

            // set collection
            if (!TextUtils.isEmpty(item.getCollection())) {
                tvCollection.setText(item.getCollection());
                tvCollection.setVisibility(View.VISIBLE);
            } else {
                tvCollection.setVisibility(View.GONE);
            }

            // set datetime
            if (!TextUtils.isEmpty(item.getDatetime())) {
                tvDatetime.setText(item.getDatetime());
                tvDatetime.setVisibility(View.VISIBLE);
            } else {
                tvDatetime.setVisibility(View.GONE);
            }

            // set layout content visibility
            if (tvCollection.getVisibility() == View.VISIBLE || tvDatetime.getVisibility() == View.VISIBLE) {
                layoutContent.setVisibility(View.VISIBLE);
            } else {
                layoutContent.setVisibility(View.GONE);
            }


        }
    }
}
