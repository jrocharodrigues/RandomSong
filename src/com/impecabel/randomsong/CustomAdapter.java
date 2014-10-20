package com.impecabel.randomsong;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomAdapter extends BaseAdapter {
	
	Activity activity;
	ArrayList<Song> music;
	
	public CustomAdapter(Activity activity, ArrayList<Song> music){
		this.activity = activity;
		this.music = music;
	}
	
	@Override
	public int getCount() {
		return music.size();
	}

	@Override
	public Object getItem(int position) {
		return music.get(position);
	}

	@Override
	public long getItemId(int position) {
		return music.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null){			
			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.music_item, null);
		}
		
		TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
		TextView tvArtist = (TextView) convertView.findViewById(R.id.textViewArtist);
		TextView tvDuration = (TextView) convertView.findViewById(R.id.textViewDuration);
		ImageView ivThumb = (ImageView) convertView.findViewById(R.id.imageViewThumb);
		
		Song s = music.get(position);
		
		tvTitle.setText(s.getTitle());
		tvArtist.setText(s.getArtist());
		tvDuration.setText(s.getDuration());
		
		//DownloadImage di = new DownloadImage(s.getThumbUrl(), ivThumb);
		//di.execute();
		Picasso.with(activity).load(s.getThumbUrl()).placeholder(android.R.drawable.ic_btn_speak_now).error(android.R.drawable.ic_delete).into(ivThumb);
		
		return convertView;
	}
}

