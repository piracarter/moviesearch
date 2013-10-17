package com.moviesearch.list;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.moviesearch.android.R;
import com.moviesearch.model.movie;
import com.moviesearch.utils.BitmapManager;
import com.moviesearch.android.R;
public class movieAdapter extends ArrayAdapter<movie> {

	private Context context;
	private ArrayList<movie> movies;
	
	public movieAdapter(Context context, int viewResourceId, ArrayList<movie> movies) { 
		super(context, viewResourceId, movies);
		this.context = context;
		this.movies = movies;
	}
	
	static class ViewHolder{
		public TextView Title;
		public ImageView thumbnail;
		public TextView year, mpaa_rating, runtime;
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(R.layout.movie_row, parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Title = (TextView) convertView.findViewById(R.id.Title);
			viewHolder.year = (TextView) convertView.findViewById(R.id.Year);
			viewHolder.mpaa_rating = (TextView) convertView.findViewById(R.id.mpaa_rating);
			viewHolder.runtime = (TextView) convertView.findViewById(R.id.runtime);
			viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
			convertView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		holder.Title.setText(movies.get(position).getTitle());
		holder.year.setText("Year: " + movies.get(position).getYear());
		
		holder.mpaa_rating.setText("Raiting: " +movies.get(position).getMpaa_rating());
		holder.runtime.setText("Runtime: " + movies.get(position).getRuntime());
		
		BitmapManager.getInstance().loadBitmap(movies.get(position).getThumbnail(), holder.thumbnail);
		
		return convertView;
	}
	
}
