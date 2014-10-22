package com.impecabel.randomsong;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.impecabel.randomsong.DownloadMusic.OnFinish;

public class MainActivity extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {
	public boolean flag_loading = false;
	private ArrayList<Song> music;
	private int mLastClickedPosition = -1;
	private int selectedListItem = -1;
	private int statusPrev = 0;
	private int nextStatusPrev = 0;
	private YouTubePlayer YPlayer;
	private static final String YoutubeDeveloperKey = "YOUR_DEVELOPER_KEY_GOES_HERE";
	private static final int RECOVERY_DIALOG_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(Utils.DEVELOPER_KEY, this);

		music = new ArrayList<Song>();
		additems(music, 0, false);
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
						additems(music, totalItemCount - 1, true);
					}
				}
			}
		});

		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			/*	Toast.makeText(getApplicationContext(),
						"Item #" + position + " id " + id + " clicked",
						Toast.LENGTH_SHORT).show();*/

				// here, "position" is the position of your item and "id" is
				// your
				// item's id in your data set.

				// mLastClickedPosition is a member field of type long which
				// stores the position of the most recently clicked item,
				// initially set to -1
				if (mLastClickedPosition != -1) {
					// do something to pause the item in your list at this
					// position
					if (parent != null) {
						View lastItem = (View) parent
								.getChildAt(mLastClickedPosition);

						ImageView lastImageView = (ImageView) lastItem
								.findViewById(R.id.mediaControl);
						lastImageView
								.setImageResource(android.R.drawable.ic_media_play);
						nextStatusPrev = 1;

					}

				}

				if (mLastClickedPosition != position || statusPrev == 1) {
					nextStatusPrev = 0;

					// next, update mLastClickedPosition
					mLastClickedPosition = position;
					selectedListItem = position;

					// find the image in your view and update it
					ImageView imageView = (ImageView) view
							.findViewById(R.id.mediaControl);
					imageView
							.setImageResource(android.R.drawable.ic_media_pause);

				}
				statusPrev = nextStatusPrev;

			}

		});
		lv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	private void additems(ArrayList<Song> music, final int lastVisiblePos,
			final boolean refreshOnly) {
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
						showResult(result, lastVisiblePos, refreshOnly);
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

	private void showResult(ArrayList<Song> music, int lastVisiblePos,
			boolean refreshOnly) {
		ListView lv1 = (ListView) findViewById(R.id.listView1);
		CustomAdapter adapter = new CustomAdapter(this, music);
		if (refreshOnly == false) {
			lv1.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}

		lv1.setItemChecked(lastVisiblePos, true);
		ensureVisible(lv1, lastVisiblePos);

	}

	public void handlePlayerControls(View v) {

		if (v == findViewById(R.id.mediaControlPrev)) {
			changeSelected(-1);

		} else if (v == findViewById(R.id.mediaControlNext)) {
			changeSelected(1);

		} else {
			tooglePlay();
		}
	}

	private void changeSelected(int way) {
		
		selectedListItem = selectedListItem + (way);
		ListView lv1 = (ListView) findViewById(R.id.listView1);
		
		int first = lv1.getFirstVisiblePosition();
		int last = lv1.getLastVisiblePosition();
		int listCount = lv1.getCount();
		if (selectedListItem < 0) {
			selectedListItem = 0;
		} else if (selectedListItem >= listCount) {
			selectedListItem = listCount;
		}

		Toast.makeText(getApplicationContext(),
				"selectedListItem " + selectedListItem, Toast.LENGTH_LONG)
				.show();

		if (selectedListItem != mLastClickedPosition) {
			//ensureVisible(lv1, selectedListItem);
			lv1.setItemChecked(selectedListItem, true);
			//lv1.performItemClick(lv1.getChildAt(selectedListItem),
			//		selectedListItem, lv1.getItemIdAtPosition(selectedListItem));
			ensureVisible(lv1, selectedListItem);
			lv1.getAdapter().getView(selectedListItem, null, null).performClick();
		}

	}

	public static void ensureVisible(ListView listView, int pos) {
		if (listView == null) {
			return;
		}

		if (pos < 0 || pos >= listView.getCount()) {
			return;
		}

		int first = listView.getFirstVisiblePosition();
		int last = listView.getLastVisiblePosition();

		if (pos < first) {
			listView.smoothScrollToPosition(pos);
			return;
		}

		if (pos >= last) {
			listView.setSelection(1 + pos - (last - first));
			return;
		}
	}

	private void tooglePlay() {
		ImageView imageView = (ImageView) findViewById(R.id.mediaControlPlay);

		if (YPlayer.isPlaying()) {
			imageView.setImageResource(android.R.drawable.ic_media_play);
			YPlayer.pause();

		} else {
			imageView.setImageResource(android.R.drawable.ic_media_pause);
			YPlayer.play();
		}
	}

	@Override
	public void onInitializationFailure(Provider player,
			YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					"There was an error initializing the YouTubePlayer",
					errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			// Retry initialization if user performed a recovery action
			getYouTubePlayerProvider().initialize(Utils.DEVELOPER_KEY, this);
		}
	}

	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			YPlayer = player;
			YPlayer.cueVideo(Utils.TEST_VIDEO); // your video to play
		}

	}

}
