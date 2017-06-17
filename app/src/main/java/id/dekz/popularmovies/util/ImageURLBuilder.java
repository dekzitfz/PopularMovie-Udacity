package id.dekz.popularmovies.util;

import id.dekz.popularmovies.BuildConfig;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class ImageURLBuilder {

    public static String getPosterURL(String path){
        return BuildConfig.BASE_URL_IMG + "w185" + path;
    }

    public static String getBackdropURL(String path){
        return BuildConfig.BASE_URL_IMG + "w300" + path;
    }
}
