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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private final List<Trailer> mList;
    private final LayoutInflater mLayoutInflater;
    final private TrailerClickListener mOnClickListener;
    private final Context ctx;

    public TrailerAdapter(List<Trailer> mList,  TrailerClickListener mOnClickListener, Context ctx) {
        this.mList = mList;
        mLayoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOnClickListener = mOnClickListener;
        this.ctx = ctx;
    }

    public void clear() {
        final int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public interface TrailerClickListener {
        void onTrailerClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView trailer_tv;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            trailer_tv = itemView.findViewById(R.id.trailer_title);
        }

        public void bind(Context ctx, int position){
            trailer_tv.setText(mList.get(position).getName().trim());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onTrailerClick(clickedPosition);
        }
    }
}
