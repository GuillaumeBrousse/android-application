package com.example.jaee.allocine;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaee.allocine.classes.Movie;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;

import static com.example.jaee.allocine.utils.MainHelper.getDPValue;
import static com.example.jaee.allocine.utils.MainHelper.getUrlMovieSynopsis;

public class MovieActivity extends AppCompatActivity {

    private Movie movie = null;
    private Typeface builtTitlingBold, dinProCond, dinProCondBold;

    private TextView tvSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        getMovieFromIntent();
        initFonts();
        initComponent();
        loadSynopsis();
    }

    private void loadSynopsis() {
        new LoadSynopsisTask(new WeakReference<>(this)).execute(getUrlMovieSynopsis(movie.getCode()));
    }

    private void initFonts() {
        builtTitlingBold = Typeface.createFromAsset(getAssets(),"BuiltTitlingBold.ttf");
        dinProCond = Typeface.createFromAsset(getAssets(),"DINPro-Cond.ttf");
        dinProCondBold = Typeface.createFromAsset(getAssets(),"DINPro-CondBold.ttf");
    }

    private void initComponent() {
        ImageView ivPoster = findViewById(R.id.ivPoster);

        if(movie.getPosterLink() != null)
            Picasso.get().load(movie.getPosterLink()).into(ivPoster);
        else
            Picasso.get().load(R.drawable.no_photo).resize(getDPValue(this, 100), getDPValue(this, 100)).into(ivPoster);

        TextView tvTitle = findViewById(R.id.tvMovieTitle);
        tvTitle.setTypeface(builtTitlingBold);
        tvTitle.setText(movie.getTitle());

        TextView tvDirector = findViewById(R.id.tvMovieDirector);
        tvDirector.setTypeface(dinProCondBold);
        tvDirector.setText(String.format("De %s", movie.getDirectors()));

        TextView tvActors = findViewById(R.id.tvMovieActor);
        tvActors.setTypeface(dinProCond);
        tvActors.setText(String.format("Avec %s", movie.getActors()));

        tvSynopsis = findViewById(R.id.tvMovieSynopsis);
        tvSynopsis.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        tvSynopsis.setMovementMethod(new ScrollingMovementMethod());
    }

    private void getMovieFromIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("movie")){
            movie = (Movie) intent.getSerializableExtra("movie");
            Log.e("MOVIE CODE", movie.getCode()+"");
        } else {
            Toast.makeText(this, "Désolé, une erreur s'est produite, veuillez réessayer", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private static class LoadSynopsisTask extends AsyncTask<String, Void, String>{
        private WeakReference<MovieActivity> weakReference;

        LoadSynopsisTask(WeakReference<MovieActivity> weakReference){
            this.weakReference = weakReference;
        }

        @Override
        protected String doInBackground(String... strings) {
            return getData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            weakReference.get().tvSynopsis.setText(s == null ? "Désolé, nous n'avons pas pu trouver de synopsis pour ce film" : s);
        }

        private String getData(String url) {
            try {
                InputStream in = (new URL(url)).openConnection().getInputStream();

                BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String str;
                while ((str = bfr.readLine()) != null) {
                    result.append(str).append("\n");
                }
                bfr.close();

                JSONObject resMovie = new JSONObject(result.toString());
                if(resMovie.getJSONObject("movie").has("synopsis"))
                    return resMovie.getJSONObject("movie").getString("synopsis");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
