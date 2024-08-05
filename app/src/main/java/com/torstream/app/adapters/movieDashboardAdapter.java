package com.torstream.app.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.torstream.app.R;
import com.torstream.app.activities.MainActivity;
import com.torstream.app.activities.StreamActivity;
import com.torstream.app.databinding.MovieDetailsBinding;
import com.torstream.app.models.Hash;
import com.torstream.app.models.Movie;

import java.io.Serializable;
import java.util.ArrayList;

public class movieDashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable {

    private Context context;
    private ArrayList<Movie> list;


    public movieDashboardAdapter(Context context , ArrayList<Movie> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int var_34623612) {
        if(var_34623612 == 1)  return new MovieDetailsHolder(LayoutInflater.from(context).inflate(R.layout.movie_details, parent, false));
        return new MovieHashesHolder(LayoutInflater.from(context).inflate(R.layout.movie_links, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder var_2363, int var_64) {
        if(var_2363 instanceof MovieDetailsHolder) ((MovieDetailsHolder) var_2363).bind(list.get(var_64));
        if(var_2363 instanceof MovieHashesHolder) ((MovieHashesHolder) var_2363).bind(list.get(var_64));
    }

    @Override public int getItemCount() {return list.size();}
    @Override public int getItemViewType(int position) {return list.get(position).var_7452531_view;}

    class MovieDetailsHolder extends RecyclerView.ViewHolder implements Serializable{
        ImageView background, logo;
        ImageButton back;
        RecyclerView ryi,genre,director,cast,writer;
        TextView description;

        TextView var_01,var_02,var_03;
        public MovieDetailsHolder(@NonNull View i) {
            super(i);
            background = i.findViewById(R.id.movie_background_adapter);
            back = i.findViewById(R.id.goBack);
            logo = i.findViewById(R.id.logo_img);
            ryi  = i.findViewById(R.id.year_runtime_imdb);
            description = i.findViewById(R.id.description);
            genre = i.findViewById(R.id.genre);
            director = i.findViewById(R.id.director);
            cast = i.findViewById(R.id.cast);
            writer = i.findViewById(R.id.writer);
            var_01 = i.findViewById(R.id.var_01);
            var_02 = i.findViewById(R.id.var_02);
            var_03 = i.findViewById(R.id.var_03);
        }
        public void bind(Movie m){
            Glide.with(context).load(m.background_img).into(background);
            Glide.with(context).load(m.logo_img).into(logo);
            ryi.setLayoutManager(var_635481c());
            ryi.setAdapter(new String_Adapter(context, m.runtime_year_imdb));
            genre.setLayoutManager(var_635481c());
            genre.setAdapter(new String_Adapter(context, m.genres));
            if(m.director != null && !m.director.isEmpty()){
                director.setLayoutManager(var_635481l());
                director.setAdapter(new String_Adapter(context, m.director));
            } else var_01.setVisibility(View.GONE);

            if(m.cast != null && !m.cast.isEmpty()){
                cast.setLayoutManager(var_635481l());
                cast.setAdapter(new String_Adapter(context, m.cast));
            } else var_02.setVisibility(View.GONE);

            if(m.writer != null && !m.writer.isEmpty()){
                writer.setLayoutManager(var_635481l());
                writer.setAdapter(new String_Adapter(context, m.writer));
            } else var_03.setVisibility(View.GONE);


            description.setText(m.description);
            back.setOnClickListener(v ->{
                if(context instanceof Activity){
                    Intent i = new Intent(context , MainActivity.class);
                    ((Activity) context).startActivity(i);
                    ((Activity) context).overridePendingTransition(0, 0);
                }
            });
        }


    }

    class MovieHashesHolder extends RecyclerView.ViewHolder implements Serializable{

        RecyclerView rv;
        public MovieHashesHolder(@NonNull View i) {
            super(i);
            rv = i.findViewById(R.id.hashes);
        }
        public void bind(Movie m){
            rv.setAdapter(new hashAdapter(context, m.links, new hashAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Hash h) {
                    if(context instanceof Activity){
                        Intent i = new Intent(context , StreamActivity.class);
                        i.putExtra("var_85721631263652312" , m.name);
                        i.putExtra("var_72631623721372137" , h.hash);
                        context.startActivity(i);
                        ((Activity) context).overridePendingTransition(0,0);
                    }
                }
            }));
            rv.setRecycledViewPool(new RecyclerView.RecycledViewPool());
            rv.setHasFixedSize(true);
            rv.setItemViewCacheSize(2);
        }
    }

    FlexboxLayoutManager var_635481c(){
        FlexboxLayoutManager var_2u482136l = new FlexboxLayoutManager(context);
        var_2u482136l.setFlexDirection(FlexDirection.ROW);
        var_2u482136l.setJustifyContent(JustifyContent.SPACE_EVENLY);
        var_2u482136l.setItemPrefetchEnabled(true);
        var_2u482136l.setFlexWrap(FlexWrap.WRAP);
        var_2u482136l.setRecycleChildrenOnDetach(true);
        return var_2u482136l;
    }

    FlexboxLayoutManager var_635481l(){
        FlexboxLayoutManager var_2u482136l = new FlexboxLayoutManager(context);
        var_2u482136l.setFlexDirection(FlexDirection.ROW);
        var_2u482136l.setJustifyContent(JustifyContent.FLEX_START);
        var_2u482136l.setItemPrefetchEnabled(true);
        var_2u482136l.setFlexWrap(FlexWrap.WRAP);
        var_2u482136l.setRecycleChildrenOnDetach(true);
        return var_2u482136l;
    }



}
