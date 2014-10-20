package com.impecabel.randomsong;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.impecabel.randomsong.DownloadMusic.OnFinish;

public class MainActivity extends Activity {
	public boolean flag_loading = false;
	private ArrayList<Song> music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * final ProgressDialog dialog = new ProgressDialog(this);
		 * 
		 * dialog.setMessage("Loading..."); dialog.show();
		 * 
		 * DownloadMusic dl = new DownloadMusic(
		 * "http://randomsong.impecabel.com/json_query.php?size=50", new
		 * OnFinish() {
		 * 
		 * @Override public void finishOk(ArrayList<Song> result) {
		 * Toast.makeText(getApplicationContext(), "List Ready!",
		 * Toast.LENGTH_LONG).show(); showResult(result); dialog.dismiss(); }
		 * 
		 * @Override public void finishError() {
		 * Toast.makeText(getApplicationContext(), "Error in communication!",
		 * Toast.LENGTH_LONG) .show(); dialog.dismiss();
		 * 
		 * } }); dl.execute();
		 */

		music = new ArrayList<Song>();
		additems(music, 0);
		ListView lv1 = (ListView) findViewById(R.id.listView1);
		lv1.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount != 0) {
					if (flag_loading == false) {
						flag_loading = true;
						additems(music, totalItemCount - 1);
					}
				}
			}
		});

	}

	private void additems(ArrayList<Song> music, final int lastVisiblePos) {
		final ProgressDialog dialog = new ProgressDialog(this);

		dialog.setMessage("Loading...");
		dialog.show();

		DownloadMusic dl = new DownloadMusic(
				"http://randomsong.impecabel.com/json_query.php?size=10",
				new OnFinish() {

					@Override
					public void finishOk(ArrayList<Song> result) {
						Toast.makeText(getApplicationContext(), "List Ready!",
								Toast.LENGTH_LONG).show();
						showResult(result, lastVisiblePos);
						dialog.dismiss();
						flag_loading = false;
					}

					@Override
					public void finishError() {
						Toast.makeText(getApplicationContext(),
								"Error in communication!", Toast.LENGTH_LONG)
								.show();
						dialog.dismiss();
						flag_loading = false;

					}
				});
		dl.execute(music);
	}

	private void showResult(ArrayList<Song> music, int lastVisiblePos) {
		ListView lv1 = (ListView) findViewById(R.id.listView1);
		CustomAdapter adapter = new CustomAdapter(this, music);
		lv1.setAdapter(adapter);
		//lv1.setSelection(lastVisiblePos - 3);
		
		
	}

}
