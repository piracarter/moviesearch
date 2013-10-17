package com.moviesearch.utils;

public class ConstantsUtils {
	
	public static final String API_KEY = "mfb4puzp7zkrtsmcn6gjqn5h";
	public static final String SEARCH_TERM = "mejorandroid";
	public static final String END_POINT = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=";
	public static final String URL_AUTHENTICATION = END_POINT + API_KEY;
	public static final String URL_SEARCH = URL_AUTHENTICATION + "&q=" + SEARCH_TERM;
		


}
