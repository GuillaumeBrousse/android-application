package com.example.jaee.allocine.utils;

import android.content.Context;
import android.util.TypedValue;

public class MainHelper {
    private static final String API_KEY = "YW5kcm9pZC12Mg";

    public static final String URL_MOVIE_RESEARCH = "http://api.allocine.fr/rest/v3/search?partner=" + API_KEY + "&format=json&count=10&q=";

    public static int getDPValue(Context context, int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

    public static String getUrlMovieSynopsis(int movieCode){
        return "http://api.allocine.fr/rest/v3/movie?partner=" + API_KEY + "&code=" + movieCode + "&profile=large&mediafmt=mp4-lc&format=json&filter=movie&striptags=synopsis";
    }
}
