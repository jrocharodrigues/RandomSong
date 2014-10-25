package com.impecabel.randomsong;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener;
import com.impecabel.randomsong.DownloadMusic.OnFinish;

public class MainActivity extends YouTubeFailureRecoveryActivity implements
		OnInitializedListener, YouTubePlayer.OnFullscreenListener {
	public boolean flag_loading = false;
	private ArrayList<Song> music;
	@SuppressWarnings("serial")
	private ArrayList<Card> cards = new ArrayList<Card>() {
		{
			add(new Card());
			add(new Card());
			add(new Card());
		}
	};

	private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
			: ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

	private int selectedListItem = -1;
	private YouTubePlayer player;
	private static final int BACK_CARD_ID = 2;
	private static final int MIDDLE_CARD_ID = 1;
	private static final int FRONT_CARD_ID = 0;
	private View frontCard;
	private View middleCard;
	private View backCard;

	private View otherViews;
	private boolean fullscreen;

	private RetainedFragment dataFragment;
	private YouTubePlayerView youTubeView;
	private Song empty_song = new Song();

	/*
	 * boolean fromOrientation=false; SharedPreferences myPrefLogin; Editor
	 * prefsEditor;
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		otherViews = findViewById(R.id.other_views);

		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(Utils.DEVELOPER_KEY, this);
		doLayout();

		// find the retained fragment on activity restarts
		FragmentManager fm = getFragmentManager();
		dataFragment = (RetainedFragment) fm.findFragmentByTag("data");

		// create the fragment and data the first time
		if (dataFragment == null) {
			// add the fragment
			dataFragment = new RetainedFragment();
			fm.beginTransaction().add(dataFragment, "data").commit();
			// load the data from the web
			music = new ArrayList<Song>();
			additems(music, false);
		} else {
			music = dataFragment.getMusic();
			cards = dataFragment.getCards();
			selectedListItem = dataFragment.getSelectedListItem();
			if (!fullscreen) {
				refreshCards();
			}
		}

	}

	private void refreshCards() {

		int backCardSongPos = cards.get(BACK_CARD_ID).getSong_position();

		if (backCardSongPos != -1) {
			// if middle card has information, moves this to back card
			populateCard(backCard, music.get(backCardSongPos), backCardSongPos,
					BACK_CARD_ID);
		}
		int middleCardSongPos = cards.get(MIDDLE_CARD_ID).getSong_position();

		if (middleCardSongPos != -1) {
			// if middle card has information, moves this to back card
			populateCard(middleCard, music.get(middleCardSongPos),
					middleCardSongPos, MIDDLE_CARD_ID);
		}

		int frontCardSongPos = cards.get(FRONT_CARD_ID).getSong_position();
		if (frontCardSongPos != -1) {
			// if front card has information, moves this to back card
			populateCard(frontCard, music.get(frontCardSongPos),
					frontCardSongPos, FRONT_CARD_ID);
		}

		/*
		 * if (YPlayer.isPlaying())
		 * YPlayer.loadVideo(selectedSong.getVideo_id()); else
		 * YPlayer.cueVideo(selectedSong.getVideo_id());
		 */

	}

	private void doLayout() {
		LinearLayout.LayoutParams playerParams = (LinearLayout.LayoutParams) youTubeView
				.getLayoutParams();

		if (fullscreen) {
			playerParams.width = LayoutParams.MATCH_PARENT;
			playerParams.height = LayoutParams.MATCH_PARENT;
			otherViews.setVisibility(View.GONE);
		} else {
			LinearLayout.LayoutParams otherViewsParams = (LinearLayout.LayoutParams) otherViews
					.getLayoutParams();
			// This layout is up to you - this is just a simple example
			// (vertically stacked boxes in
			// portrait, horizontally stacked in landscape).
			otherViews.setVisibility(View.VISIBLE);
			playerParams.width = otherViewsParams.width = MATCH_PARENT;
			playerParams.height = 0;
			playerParams.weight = 50;
			otherViewsParams.height = 0;
			otherViewsParams.weight = 50;
			frontCard = (View) findViewById(R.id.frontCard);
			middleCard = (View) findViewById(R.id.middleCard);
			backCard = (View) findViewById(R.id.backCard);

		}
	}

	@Override
	public void onFullscreen(boolean isFullscreen) {
		fullscreen = isFullscreen;
		doLayout();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// store the data in the fragment
		dataFragment.setData(music, cards, selectedListItem);
	}

	void populateCard(View cardView, Song song, int position, int cardPosition) {

		TextView tvTitle = (TextView) cardView.findViewById(R.id.textViewTitle);
		TextView tvArtist = (TextView) cardView
				.findViewById(R.id.textViewArtist);
		TextView tvDuration = (TextView) cardView
				.findViewById(R.id.textViewDuration);
		YouTubeThumbnailView ytThumb = (YouTubeThumbnailView) cardView
				.findViewById(R.id.youtubethumbnailview);

		tvTitle.setText(song.getTitle());
		tvArtist.setText(song.getArtist());
		tvDuration.setText(song.getDuration());

		ytThumb.setTag(song.getVideo_id());
		ytThumb.initialize(Utils.DEVELOPER_KEY, this);

		cards.set(cardPosition, new Card(position, ytThumb));

	}

	@SuppressWarnings("unchecked")
	private void additems(ArrayList<Song> music, final boolean refreshOnly) {
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
						changeSelected(1);
						dataFragment.setData(result, cards, selectedListItem);

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

		if (selectedListItem > music.size() - 1)
			additems(music, true);
		else {

			if (selectedListItem < 0) {
				selectedListItem = 0;
			} else {
				int backCardSongPos = selectedListItem - BACK_CARD_ID;
				int middleCardSongPos = selectedListItem - MIDDLE_CARD_ID;

				if (backCardSongPos > 0) {
					populateCard(backCard, music.get(backCardSongPos),
							backCardSongPos, BACK_CARD_ID);
				} else {
					populateCard(backCard, empty_song, -1, BACK_CARD_ID);
				}
				if (middleCardSongPos > 0) {
					populateCard(middleCard, music.get(middleCardSongPos),
							middleCardSongPos, MIDDLE_CARD_ID);
				} else {
					populateCard(middleCard, empty_song, -1, MIDDLE_CARD_ID);
				}

				populateCard(frontCard, music.get(selectedListItem),
						selectedListItem, FRONT_CARD_ID);
			}

			if (player.isPlaying())
				player.loadVideo(music.get(selectedListItem).getVideo_id());
			else
				player.cueVideo(music.get(selectedListItem).getVideo_id());

		}

	}

	private void tooglePlay() {
		ImageView imageView = (ImageView) findViewById(R.id.mediaControlPlay);

		if (player.isPlaying()) {
			imageView.setImageResource(android.R.drawable.ic_media_play);
			player.pause();

		} else {
			imageView.setImageResource(android.R.drawable.ic_media_pause);
			player.play();
		}
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		this.player = player;
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
		setRequestedOrientation(PORTRAIT_ORIENTATION);

		player.setOnFullscreenListener(this);

		if (!wasRestored) {
			// player.cueVideo(Utils.TEST_VIDEO); // your video to play

			player.cueVideo(Utils.TEST_VIDEO);
		}

	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return youTubeView;
	}

	@Override
	public void onInitializationSuccess(YouTubeThumbnailView view,
			YouTubeThumbnailLoader loader) {
		String videoId = (String) view.getTag();
		view.setImageResource(R.drawable.ic_launcher);
		loader.setVideo(videoId);

	}

	@Override
	public void onInitializationFailure(YouTubeThumbnailView view,
			YouTubeInitializationResult result) {
		// TODO Auto-generated method stub

	}

}
