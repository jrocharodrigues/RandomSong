package com.impecabel.randomsong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class CustomAdapter extends BaseAdapter implements
		YouTubeThumbnailView.OnInitializedListener {

	Activity activity;
	ArrayList<Song> music;
	Map<View, YouTubeThumbnailLoader> loaders;

	public CustomAdapter(Activity activity, ArrayList<Song> music) {
		this.activity = activity;
		this.music = music;
		loaders = new HashMap<View, YouTubeThumbnailLoader>();
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

		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.music_item, null);
			YouTubeThumbnailView ytThumb = (YouTubeThumbnailView) convertView
					.findViewById(R.id.youtubethumbnailview);
			ytThumb.setTag(RandomSongUtils.TEST_VIDEO);
			ytThumb.initialize(RandomSongUtils.DEVELOPER_KEY, this);
		} else {
			YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) convertView
					.findViewById(R.id.youtubethumbnailview);
			YouTubeThumbnailLoader loader = loaders.get(thumbnail);
			if (loader == null) {
				// Case 3 - The loader is currently initializing
				thumbnail.setTag(RandomSongUtils.TEST_VIDEO);
			} else {
				// Case 2 - The loader is already initialized
				thumbnail.setImageResource(R.drawable.ic_launcher);
				loader.setVideo(RandomSongUtils.TEST_VIDEO);
			}

		}

		TextView tvTitle = (TextView) convertView
				.findViewById(R.id.textViewTitle);
		TextView tvArtist = (TextView) convertView
				.findViewById(R.id.textViewArtist);
		TextView tvDuration = (TextView) convertView
				.findViewById(R.id.textViewDuration);
		// ImageView ivThumb = (ImageView)
		// convertView.findViewById(R.id.imageViewThumb);

		Song s = music.get(position);

		tvTitle.setText(s.getTitle());
		tvArtist.setText(s.getArtist());
		tvDuration.setText(s.getDuration());

		// DownloadImage di = new DownloadImage(s.getThumbUrl(), ivThumb);
		// di.execute();
		// Picasso.with(activity).load(s.getThumbUrl()).placeholder(android.R.drawable.ic_btn_speak_now).error(android.R.drawable.ic_delete).into(ivThumb);

		return convertView;
	}

	@Override
	public void onInitializationFailure(YouTubeThumbnailView view,
			YouTubeInitializationResult errorReason) {
		// TODO
	}

	@Override
	public void onInitializationSuccess(YouTubeThumbnailView view,
			YouTubeThumbnailLoader loader) {
		String videoId = (String) view.getTag();
		loaders.put(view, loader);
		view.setImageResource(R.drawable.ic_launcher);
		loader.setVideo(videoId);

	}
}
