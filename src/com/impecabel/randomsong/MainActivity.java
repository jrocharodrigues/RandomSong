package com.impecabel.randomsong;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.impecabel.randomsong.DownloadMusic.OnFinish;

public class MainActivity extends Activity {
	public boolean flag_loading = false;
	private ArrayList<Song> music;
	private int mLastClickedPosition = -1;
	private int selectedListItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						"Item #" + position + " - " + mLastClickedPosition + " - " + id + " clicked", Toast.LENGTH_SHORT)
						.show();

				// here, "position" is the position of your item and "id" is
				// your
				// item's id in your data set.

				// mLastClickedPosition is a member field of type long which
				// stores the position of the most recently clicked item,
				// initially set to -1
				if (mLastClickedPosition != -1) {
					// do something to pause the item in your list at this
					// position
					Toast.makeText(getApplicationContext(),
							"NOT -1", Toast.LENGTH_SHORT)
							.show();
					if (parent != null) {
						Toast.makeText(getApplicationContext(),
								"has parent", Toast.LENGTH_SHORT)
								.show();
						View lastItem = (View) parent
								.getChildAt(mLastClickedPosition);

						ImageView lastImageView = (ImageView) lastItem
								.findViewById(R.id.mediaControl);
						lastImageView
								.setImageResource(android.R.drawable.ic_media_play);
					}

				}

				if (mLastClickedPosition != position) {
					Toast.makeText(getApplicationContext(),
							"Last Clicked", Toast.LENGTH_SHORT)
							.show();
					// next, update mLastClickedPosition
					mLastClickedPosition = position;

					// find the image in your view and update it
					ImageView imageView = (ImageView) view
							.findViewById(R.id.mediaControl);
					imageView
							.setImageResource(android.R.drawable.ic_media_pause);
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
		lv1.setSelection(lastVisiblePos - 3);

	}
	
	public void handlePlayerControls (View v) {
		if (v == findViewById(R.id.mediaControlPrev)){
			changeSelected(-1);
		} else if (v == findViewById(R.id.mediaControlNext)){
			changeSelected(1);
		} else {
			tooglePlay();
		}
	}
	private void changeSelected(int way){
		selectedListItem = selectedListItem + (way);
		ListView lv1 = (ListView) findViewById(R.id.listView1);
		int listCount = lv1.getCount();
		
		if (selectedListItem <= 0){
			selectedListItem = 0;
		} else if (selectedListItem >= listCount - 1) {
			selectedListItem = listCount - 1;
		}
		
		lv1.performItemClick(lv1, selectedListItem, lv1.getItemIdAtPosition(selectedListItem));

		
				
	}
	
	private void tooglePlay(){
		//MAKE CODE
	}
	
}
