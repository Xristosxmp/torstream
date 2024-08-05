package com.torstream.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.torstream.app.R;
import com.torstream.app.models.Movie;

import java.io.Serializable;
import java.util.ArrayList;

public class moviesAdapter extends RecyclerView.Adapter<moviesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Movie> list;
    private OnItemClickListener listener;

    public moviesAdapter(Context context , ArrayList<Movie> list , OnItemClickListener listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override public int getItemCount() {return list.size();}


    public class ViewHolder extends RecyclerView.
    ViewHolder implements View.OnClickListener{
        ImageView img;
        public ViewHolder(@NonNull View i) {
            super(i);
            img = i.findViewById(R.id.movie_img_adapter);
            img.setClipToOutline(true);
            i.setOnClickListener(this);

        }

        public void bind(Movie m){
            Glide.with(context)
                    .load(m.poster_img)
                    .into(img);
            itemView.setTag(m);
        }
        @Override  public void onClick(View v) { if (listener != null) listener.onItemClick((Movie) itemView.getTag()); }

    }


    public interface OnItemClickListener {void onItemClick(Movie m);}

}
