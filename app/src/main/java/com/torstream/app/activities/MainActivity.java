package com.torstream.app.activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.se_bastiaan.torrentstream.TorrentOptions;
import com.github.se_bastiaan.torrentstreamserver.TorrentStreamServer;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.torstream.app.adapters.moviesAdapter;
import com.torstream.app.databinding.ActivityMainBinding;
import com.torstream.app.models.Movie;
import com.torstream.app.models.String_;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private moviesAdapter adapter;
    private ArrayList<Movie> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle var724521862187123) {
        super.onCreate(var724521862187123);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.moviesRv, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        adapter = new moviesAdapter(this, list, m -> {
            Intent i = new Intent(this , MovieActivity.class);
            i.putExtra("var_7362512" , m);
            startActivity(i);
            overridePendingTransition(0,0);
        });
        binding.moviesRv.setLayoutManager(var_635481l());
        binding.moviesRv.setAdapter(adapter);
        fetch_();
        start_http_server();
    }

    void fetch_(){
        new Thread(() ->{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://cinemeta-catalogs.strem.io/top/catalog/movie/top.json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    JSONArray metas = new JSONObject(response.body().string()).getJSONArray("metas");
                    for(int i=0; i<metas.length(); i++){
                        Movie movie_Object = new Movie();
                        JSONObject movie = metas.getJSONObject(i);
                        movie_Object.name = !movie.isNull("name") ? movie.getString("name") : "";
                        movie_Object.imbd_code = !movie.isNull("imdb_id") ? movie.getString("imdb_id") : "";
                        movie_Object.poster_img = !movie.isNull("poster") ? movie.getString("poster") : "";
                        movie_Object.background_img = !movie.isNull("background") ? movie.getString("background") : "";
                        movie_Object.logo_img = !movie.isNull("logo") ? movie.getString("logo") : "";
                        movie_Object.description = !movie.isNull("description") ? movie.getString("description") : "";
                        movie_Object.runtime_year_imdb = fetch_runtime_year_imdb(movie);
                        movie_Object.genres = fetch_genre(movie);
                        movie_Object.director = fetch_director(movie);
                        movie_Object.cast = fetch_cast(movie);
                        movie_Object.writer = fetch_writer(movie);
                        list.add(movie_Object);
                    }
                    runOnUiThread(() -> { adapter.notifyDataSetChanged(); });
                } else throw new IOException("Unexpected response code. " + response);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error on fetch: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    FlexboxLayoutManager var_635481l(){
        FlexboxLayoutManager var_2u482136l = new FlexboxLayoutManager(this);
        var_2u482136l.setFlexDirection(FlexDirection.ROW);
        var_2u482136l.setJustifyContent(JustifyContent.CENTER);
        var_2u482136l.setItemPrefetchEnabled(true);
        var_2u482136l.setFlexWrap(FlexWrap.WRAP);
        var_2u482136l.setRecycleChildrenOnDetach(true);
        return var_2u482136l;
    }

    ArrayList<String_> fetch_runtime_year_imdb(JSONObject movie) throws JSONException {
        ArrayList<String_> var_23762 = new ArrayList<>();
        if(!movie.isNull("runtime")) var_23762.add(new String_(movie.getString("runtime") , false));
        if(!movie.isNull("year")) var_23762.add(new String_(movie.getString("year") , false));
        if(!movie.isNull("imdbRating")) var_23762.add(new String_(movie.getString("imdbRating") , true));
        return var_23762;
    }

    ArrayList<String_> fetch_genre(JSONObject movie) throws JSONException {
        ArrayList<String_> var_23762 = new ArrayList<>();
        if(!movie.isNull("genre")){
            JSONArray genres = movie.getJSONArray("genre");
            for(int i=0; i<genres.length(); i++)
                var_23762.add(new String_(genres.getString(i) , false));
        }
        return var_23762;
    }

    ArrayList<String_> fetch_director(JSONObject movie) throws JSONException {
        ArrayList<String_> var_23762 = new ArrayList<>();
        if(!movie.isNull("director")){
            JSONArray genres = movie.getJSONArray("director");
            for(int i=0; i<genres.length(); i++)
                var_23762.add(new String_(genres.getString(i) , false));
        }
        return var_23762;
    }

    ArrayList<String_> fetch_cast(JSONObject movie) throws JSONException {
        ArrayList<String_> var_23762 = new ArrayList<>();
        if(!movie.isNull("cast")){
            JSONArray genres = movie.getJSONArray("cast");
            for(int i=0; i<genres.length(); i++)
                var_23762.add(new String_(genres.getString(i) , false));
        }
        return var_23762;
    }

    ArrayList<String_> fetch_writer(JSONObject movie) throws JSONException {
        ArrayList<String_> var_23762 = new ArrayList<>();
        if(!movie.isNull("writer")){
            JSONArray genres = movie.getJSONArray("writer");
            for(int i=0; i<genres.length(); i++)
                var_23762.add(new String_(genres.getString(i) , false));
        }
        return var_23762;
    }

    private TorrentStreamServer server;
    void start_http_server(){
        TorrentOptions opts = new TorrentOptions.Builder()
                .saveLocation(getFilesDir())
                .removeFilesAfterStop(false)
                .autoDownload(true)
                .prepareSize(1000l)
                .build();
        String var_64w = "127.0.0.1";
        try {
            InetAddress var_625152 = var_62612637127321662wifi(this);
            if (var_625152 != null) var_64w = var_625152.getHostAddress();
        } catch (UnknownHostException e) {e.printStackTrace();   throw new RuntimeException(e);}
        server = TorrentStreamServer.getInstance();
        server.setTorrentOptions(opts);
        server.setServerHost(var_64w);
        server.setServerPort(8080);
        server.startTorrentStream();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.stopTorrentStream();
    }

    private static InetAddress var_62612637127321662wifi(Context context) throws UnknownHostException {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        if (ip == 0)  return null;
        else {
            byte[] ipAddress = new byte[]{
                    (byte) (ip & 0xFF),
                    (byte) ((ip >> 8) & 0xFF),
                    (byte) ((ip >> 16) & 0xFF),
                    (byte) ((ip >> 24) & 0xFF)};
            return InetAddress.getByAddress(ipAddress);
        }
    }



}