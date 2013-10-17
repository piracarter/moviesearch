package com.moviesearch.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

//import com.moviesearch.android.R;
import com.moviesearch.list.movieAdapter;
import com.moviesearch.model.movie;
import com.moviesearch.android.R;

public final class TomatoesUtils {
	
	public static final String TAG = "TomatoesUtils";
	
	public static final String getResponseServiceAPI(String... uri){
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try
        {
            // make a HTTP request
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK)
            {
                // request successful - read the response and close the connection
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            }
            else
            {
                // request failed - close the connection
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }
        catch (Exception e)
        {
            Log.d("Test", "Couldn't make a successful request!");
            Log.d("Response", "Respuesta Entro a Excepcion: " + e);
        }
        Log.d("Response", "Respuesta " + responseString);
        return responseString;
	}
	
	public static final void updateMovies(String response, ListView view, Context context){
		ArrayList<movie> amovies = new ArrayList<movie>();
		try
        {
            // convert the String response to a JSON object,
            // because JSON is the response format Rotten Tomatoes uses
            JSONObject jsonResponse = new JSONObject(response);

            // fetch the array of movies in the response
            JSONArray movies = jsonResponse.getJSONArray("movies");

            // add each movie's title to an array
            for (int i = 0; i < movies.length(); i++)
            {
            	movie movie_actual = new movie();
                JSONObject movie = movies.getJSONObject(i);
                movie_actual.setTitle(movie.getString("title"));
                movie_actual.setThumbnail(movie.getJSONObject("posters").getString("profile"));
                movie_actual.setYear(movie.getString("year"));
                movie_actual.setMpaa_rating(movie.getString("mpaa_rating"));
                movie_actual.setRuntime(movie.getString("runtime"));
                amovies.add(i,movie_actual);
                //Log.d("Response", "Respuesta onPostExecute " + movie_actual.getTitle() );
                //Log.d("Response", "Respuesta onPostExecute thumbnail" + movie.getJSONObject("posters").getString("thumbnail") );
            }
           view.setAdapter(new movieAdapter(context, R.layout.movie_row, amovies));
        }catch (JSONException ex) {}
	} 
	
	
	
}
