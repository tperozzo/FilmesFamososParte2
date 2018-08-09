package com.example.tallesperozzo.filmesfamososparte2.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.perozzo.tmdbexample.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private final List<Movie> mList;
    private final LayoutInflater mLayoutInflater;
    final private ListItemClickListener mOnClickListener;
    private final Context ctx;


    public MovieAdapter(Context c, List<Movie> l, ListItemClickListener listener){
        ctx = c;
        mList = l;
        mOnClickListener = listener;
        mLayoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clear() {
        final int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
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
        final ImageView poster_image_iv;
        final ProgressBar poster_image_pb;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            poster_image_iv = itemView.findViewById(R.id.poster_image_iv);
            poster_image_pb = itemView.findViewById(R.id.poster_image_pb);

        }

        public void bind(Context ctx, int position){

            if(mList.get(position).getPoster_path().equals("") || mList.get(position).getPoster_path().equals(APIUtils.IMAGE_URL + "/null")) {
                poster_image_pb.setIndeterminate(false);
                poster_image_pb.setVisibility(View.GONE);
                poster_image_iv.setBackgroundColor(ctx.getResources().getColor(R.color.grey));
                Picasso.get().load(R.drawable.unknown).fit().into(poster_image_iv);
            }
            else {
                Picasso.get().load(mList.get(position).getPoster_path()).fit().into(poster_image_iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        poster_image_pb.setIndeterminate(false);
                        poster_image_pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        poster_image_pb.setIndeterminate(false);
                        poster_image_pb.setVisibility(View.GONE);
                    }

                });
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
