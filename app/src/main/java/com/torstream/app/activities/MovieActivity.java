package com.torstream.app.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.torstream.app.R;
import com.torstream.app.databinding.ActivityMovieBinding;
import com.torstream.app.models.Hash;
import com.torstream.app.models.Movie;
import com.torstream.app.adapters.movieDashboardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;
    private ArrayList<Movie> list = new ArrayList<>();
    private movieDashboardAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle var_73721362164634656) {
        super.onCreate(var_73721362164634656);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.movieDashboard, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Movie var_2746123 = (Movie) getIntent().getSerializableExtra("var_7362512");
        list.add(var_2746123);
        adapter = new movieDashboardAdapter(this , list);
        binding.movieDashboard.setAdapter(adapter);
        binding.movieDashboard.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        binding.movieDashboard.setHasFixedSize(true);
        binding.movieDashboard.setItemViewCacheSize(2);
        new Thread(() ->{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://torrentio.strem.fun/qualityfilter=brremux,hdrall,dolbyvision,4k,720p,480p,scr,cam/stream/movie/" + var_2746123.imbd_code + ".json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    JSONArray streams = new JSONObject(response.body().string()).getJSONArray("streams");
                    Movie var_72631263 = new Movie(); var_72631263.var_7452531_view = 0;
                    ArrayList<Hash> var_9273612 = new ArrayList<>();
                    for(int i=0; i<streams.length(); i++){
                        Hash var_629123 = new Hash();
                        JSONObject var_ob2321 = streams.getJSONObject(i);
                        var_629123.name = var_ob2321.getString("name");
                        var_629123.title = var_ob2321.getString("title");
                        var_629123.hash = var_ob2321.getString("infoHash");
                        var_9273612.add(var_629123);
                    }
                    var_72631263.links = (var_9273612);
                    runOnUiThread(() -> {
                        list.add(var_72631263);
                        adapter.notifyItemInserted(list.size()-1);
                    });
                } else throw new IOException("Unexpected response code. " + response);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error on fetch: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

        }).start();

    }
}
