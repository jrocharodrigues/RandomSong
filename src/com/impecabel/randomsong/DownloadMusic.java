package com.impecabel.randomsong;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

public class DownloadMusic extends AsyncTask<ArrayList<Song>, Void, ArrayList<Song>> {
	
	public interface OnFinish{
		public void finishOk(ArrayList<Song> result);
		public void finishError();
	}
	
	private OnFinish callback;
	private String urlStr;
	
	private ArrayList<Song> music;
	
	public DownloadMusic(String urlStr, OnFinish callback){
		this.urlStr = urlStr;
		this.callback = callback;
	}
	
	@Override
	protected ArrayList<Song> doInBackground(ArrayList<Song>... actual_music) {
		music = actual_music[0];
		try {
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			
			InputStream stream = connection.getInputStream();
			
			Scanner scanner = new Scanner(stream);
			StringBuilder result = new StringBuilder();
			while (scanner.hasNext()){
				
				result.append(scanner.nextLine());
			}
			scanner.close();
			
			try {
				JSONArray ja = new JSONArray(result.toString());
				for (int i = 0; i < ja.length(); i++) {
					String id = ja.getJSONObject(i).getString("id");
					String artist = ja.getJSONObject(i).getString("artist");					
					String title = ja.getJSONObject(i).getString("title");
					String video_id = ja.getJSONObject(i).getString("video_id");
					
					music.add(new Song(id, title, artist, video_id));
				}
				return music;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Song> result) {
		if (callback == null){
			return;
		}
		
		if (result == null){
			callback.finishError();
		} else {
			callback.finishOk(result);
		}
	}

}
