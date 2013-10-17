package com.moviesearch;


import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import com.moviesearch.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.moviesearch.utils.TomatoesUtils;

@SuppressLint("NewApi")
public class MainMovie extends Activity implements OnQueryTextListener, OnFocusChangeListener, SearchView.OnCloseListener, ListView.OnItemClickListener {
    private static final String API_KEY = "mfb4puzp7zkrtsmcn6gjqn5h"; 
    private static final int MOVIE_PAGE_LIMIT = 10;
	private ListView lvTimeline;
	private SearchView mSearchView;
	private boolean isClosed;
	private ListView drawer_list;
	private DrawerLayout drawer_layout;
	private ActionBarDrawerToggle drawer_toggle;
	private PullToRefreshAttacher pull_to_refresh_attacher;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		/* INICIA CONFIGURACION DEL DRAWERLAYOUT*/
		pull_to_refresh_attacher = PullToRefreshAttacher.get(this);
		drawer_list = (ListView) findViewById(R.id.left_drawer);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer_layout == null)
		{
			Log.d("Response", "Respuesta se encontro objeto null");
		}
		else
		{
			Log.d("Response", "Respuesta objeto drawer_layout diferente a nulo");
		}
        ArrayAdapter<String> drawer_adapter = 
        		new ArrayAdapter<String>(this, R.layout.drawerlistitem, getResources().getStringArray(R.array.array_drawer_options));
        drawer_list.setAdapter(drawer_adapter);
        drawer_list.setOnItemClickListener(this);
        
        drawer_toggle = new ActionBarDrawerToggle(this, 
			     drawer_layout,
			     R.drawable.ic_drawer, 
			     R.string.drawer_open, 
			     R.string.drawer_close) {
       	public void onDrawerClosed(View view) {
       		invalidateOptionsMenu();
       	}

       	public void onDrawerOpened(View drawerView) {
       		invalidateOptionsMenu();
       	}
       };	        
       drawer_layout.setDrawerListener(drawer_toggle);
       getActionBar().setDisplayHomeAsUpEnabled(true);
       getActionBar().setHomeButtonEnabled(true);
       
       
        /* FINAL CONFIGURACION DEL DRAWERLAYOUT*/
        
        
		lvTimeline = (ListView) findViewById(R.id.lv_timeline);
		String URL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=" + API_KEY + "&page_limit=20";
		Log.d("Response", "Respuesta: " + URL);
	    new RequestTask().execute(URL);       
	        
	    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchItem.getActionView(); 
		mSearchView.setQueryHint("Search...");
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setOnCloseListener((OnCloseListener) this);
		isClosed=true;
		return true;
		
	}
	
	@Override
	public void onBackPressed() {
		if (mSearchView.isShown()){
            mSearchView.onActionViewCollapsed();  //collapse your ActionView
            mSearchView.setQuery("",false);       //clears your query without submit
            isClosed = true;                     //needed to handle closed by back
        } else{
            super.onBackPressed();
        }
	}
	
	@Override	
	public boolean onQueryTextChange(String newText) {
		//Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();
		return false;
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
		String URL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" + API_KEY + "&q=" + query + "&page_limit=" + MOVIE_PAGE_LIMIT;
		Log.d("Response", "Respuesta: " + URL);
	    new RequestTask().execute(URL);
	    return false;
		
	}
	
	private class RequestTask extends AsyncTask<String, Void, String>
    {
        // make a request to the specified url
        @Override
        protected String doInBackground(String... uri)
        {
            return TomatoesUtils.getResponseServiceAPI(uri);
        }

        // if the request above completed successfully, this method will 
        // automatically run so you can do something with the response
        @Override
        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            if (response != null)
            {
            	Log.d("Response", "Respuesta onPostExecute objeto response no vacio");
                TomatoesUtils.updateMovies(response,lvTimeline,MainMovie.this);
				// update the UI
            }
            Log.d("Response", "Respuesta onPostExecute: " + response);
        }


    }
	
	@Override
	public boolean onClose() {
	    isClosed = true;
	    return false;
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//selectItem(arg2);
		
	}
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawer_toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawer_toggle.onConfigurationChanged(newConfig);
    }
    public PullToRefreshAttacher getAttacher() {
		return pull_to_refresh_attacher;
		
	}
}
