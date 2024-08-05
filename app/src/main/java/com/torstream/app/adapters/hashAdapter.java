package com.torstream.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.torstream.app.models.Hash;

import com.torstream.app.R;
import com.torstream.app.models.Movie;

import java.util.ArrayList;

public class hashAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Hash> list;
    private Context context;
    private OnItemClickListener listener;

    public hashAdapter(Context context, ArrayList<Hash> list, OnItemClickListener listener
    ){
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DefaultHolder(LayoutInflater.from(context).inflate(R.layout.hash_, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int var_73623) {
        if(holder instanceof DefaultHolder) ((DefaultHolder) holder).bind(list.get(var_73623));
    }

    @Override public int getItemCount() { return list != null ? list.size(): 0;}

    class DefaultHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,title;
        public DefaultHolder(@NonNull View i) {
            super(i); name = i.findViewById(R.id.name);
            title = i.findViewById(R.id.title);
            i.setOnClickListener(this);

        }
        void bind(Hash h){
            name.setText(h.name);
            title.setText(h.title);
            itemView.setTag(h);

        }

        @Override  public void onClick(View v) { if (listener != null) listener.onItemClick((Hash) itemView.getTag()); }
    }


    public interface OnItemClickListener {void onItemClick(Hash h);}

}
