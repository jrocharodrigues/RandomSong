package com.impecabel.randomsong;

import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImage extends AsyncTask<Void, Void, Void> {

	private String urlStr;
	private ImageView view;
	private Bitmap pic;

	public DownloadImage(String urlStr, ImageView view) {
		this.urlStr = urlStr;
		this.view = view;
	}

	@Override
	protected Void doInBackground(Void... params) {
		URL url;
		try {
			url = new URL(urlStr);
			URLConnection connection = url.openConnection();

			//InputStream stream = connection.getInputStream();
			pic = BitmapFactory.decodeStream(connection.getInputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		view.setImageBitmap(pic);
	}
	
}
