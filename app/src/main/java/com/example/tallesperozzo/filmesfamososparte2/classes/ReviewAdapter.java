package com.example.tallesperozzo.filmesfamososparte2.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perozzo.tmdbexample.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final List<Review> mList;
    private final LayoutInflater mLayoutInflater;
    private final Context ctx;

    public ReviewAdapter(List<Review> mList, Context ctx) {
        this.mList = mList;
        mLayoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ctx = ctx;
    }

    public void clear() {
        final int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.bind(ctx, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView author_tv;
        final TextView content_tv;

        ViewHolder(View itemView) {
            super(itemView);
            author_tv = itemView.findViewById(R.id.author_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
        }

        public void bind(Context ctx, int position){
            author_tv.setText(mList.get(position).getAuthor());
            content_tv.setText(mList.get(position).getContent());
        }

    }
}
