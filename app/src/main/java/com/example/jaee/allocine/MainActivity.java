package com.example.jaee.allocine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jaee.allocine.adapters.MovieAdapter;
import com.example.jaee.allocine.classes.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.jaee.allocine.utils.MainHelper.URL_MOVIE_RESEARCH;

public class MainActivity extends AppCompatActivity {

    private EditText etSearchMovie;
    private ImageButton ibSearchMovie;
    private RecyclerView rvMovieList;

    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
    }

    private void initComponent() {

        View.OnClickListener btnSearchOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null)
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                String urlSearch = URL_MOVIE_RESEARCH + etSearchMovie.getText().toString();
                new GetResearchTask(new WeakReference<>(MainActivity.this)).execute(urlSearch);
            }
        };

        ibSearchMovie = findViewById(R.id.btnSearch);
        ibSearchMovie.setOnClickListener(btnSearchOnClick);

        etSearchMovie = findViewById(R.id.inputSearch);
        etSearchMovie.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                if(actionId == EditorInfo.IME_ACTION_GO)
                    ibSearchMovie.performClick();

                return false;
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        Drawable divider = getDrawable(R.drawable.divider_recycler_view);
        if(divider != null)
            dividerItemDecoration.setDrawable(divider);

        rvMovieList = findViewById(R.id.rvMovieList);
        rvMovieList.setAdapter(new MovieAdapter(this, movies));
        rvMovieList.setLayoutManager(llm);
        rvMovieList.addItemDecoration(dividerItemDecoration);

        String loadMovie = URL_MOVIE_RESEARCH + "tu";
        new GetResearchTask(new WeakReference<>(this)).execute(loadMovie);
    }

    private static class GetResearchTask extends AsyncTask<String, Void, Void>{

        private WeakReference<MainActivity> weakReference;

        GetResearchTask(WeakReference<MainActivity> weakReference){
            this.weakReference = weakReference;
        }

        @Override
        protected Void doInBackground(String... urls) {
            getData(urls[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            weakReference.get().rvMovieList.getAdapter().notifyDataSetChanged();
        }

        private void getData(String url){
            try {
                InputStream in = (new URL(url)).openConnection().getInputStream();

                BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String str;
                while ((str = bfr.readLine()) != null) {
                    result.append(str).append("\n");
                }
                bfr.close();

                weakReference.get().movies.clear();

                JSONObject resMovie = new JSONObject(result.toString());
                JSONArray resultMovie = resMovie.has("feed") ? resMovie.getJSONObject("feed").getJSONArray("movie") : null;

                parseJSON(Objects.requireNonNull(resultMovie));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parseJSON(JSONArray arrayMovies){
            Integer code;
            String title;
            String actors;
            String directors;
            Integer productionYear;
            String posterLink;
            String link;

            for (Integer i = 0; i < arrayMovies.length(); i++) {
                try {
                    JSONObject objectMovie = arrayMovies.getJSONObject(i);

                    code = Integer.valueOf(objectMovie.getString("code"));

                    if (objectMovie.has("title")) {
                        title = objectMovie.getString("title");
                    } else if (objectMovie.has("originalTitle")) {
                        title = objectMovie.getString("originalTitle");
                    } else {
                        title = "No name found";
                    }

                    if (objectMovie.has("castingShort")) {
                        JSONObject castingShort = objectMovie.getJSONObject("castingShort");

                        actors = castingShort.has("actors") ? castingShort.getString("actors") : "";
                        directors = castingShort.has("directors") ? castingShort.getString("directors") : "";
                    } else {
                        actors = "";
                        directors = "";
                    }

                    productionYear = objectMovie.has("productionYear") ? Integer.valueOf(objectMovie.getString("productionYear")) : 1900;
                    posterLink = objectMovie.has("poster") ? objectMovie.getJSONObject("poster").getString("href") : null;
                    link = objectMovie.has("link") ? (objectMovie.getJSONArray("link").getJSONObject(0).getString("href")) : null;

                    weakReference.get().movies.add(new Movie(code, title, actors, directors, productionYear, posterLink, link, null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
