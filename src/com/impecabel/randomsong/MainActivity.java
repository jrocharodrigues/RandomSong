package com.impecabel.randomsong;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener;
import com.impecabel.randomsong.DownloadMusic.OnFinish;

public class MainActivity extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener, OnInitializedListener {
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
	
	private int selectedListItem = -1;
	private YouTubePlayer YPlayer;
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private static final int BACK_CARD_ID = 2;
	private static final int MIDDLE_CARD_ID = 1;
	private static final int FRONT_CARD_ID = 0;
	private View frontCard;
	private View middleCard;
	private View backCard;
	/*boolean fromOrientation=false;
    SharedPreferences myPrefLogin;
    Editor prefsEditor;*/
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
        setContentView(R.layout.activity_main);
        
        
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(Utils.DEVELOPER_KEY, this);
		
		/*myPrefLogin = this.getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
		prefsEditor = myPrefLogin.edit();        
        fromOrientation = myPrefLogin.getBoolean("fromOrient", false);*/
        frontCard = (View) findViewById(R.id.frontCard);
		middleCard = (View) findViewById(R.id.middleCard);
		backCard = (View) findViewById(R.id.backCard);
		music = new ArrayList<Song>();
		additems(music, false);
       /* if(!fromOrientation)
        {
        	
			
        }*/
		

	}
	
	/*@Override
    public Object onRetainNonConfigurationInstance() {
         prefsEditor.putBoolean("fromOrient", true);
         prefsEditor.commit();
        return null;
    }
	
	@Override
    protected void onDestroy() {
       if(fromOrientation)
       {
        prefsEditor.putBoolean("fromOrient", false);
        prefsEditor.commit();
        }
        super.onDestroy();
    }  */


	private void populateCard(View cardView, Song song, int position,
			int cardPosition) {

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
			int middleCardSongPos = cards.get(MIDDLE_CARD_ID)
					.getSong_position();

			if (middleCardSongPos != -1) {
				// if middle card has information, moves this to back card
				populateCard(backCard, music.get(middleCardSongPos),
						middleCardSongPos, BACK_CARD_ID);
			}

			int frontCardSongPos = cards.get(FRONT_CARD_ID).getSong_position();
			if (frontCardSongPos != -1) {
				// if front card has information, moves this to back card
				populateCard(middleCard, music.get(frontCardSongPos),
						frontCardSongPos, MIDDLE_CARD_ID);
			}
			
			Song selectedSong = music.get(selectedListItem);

			populateCard(frontCard, selectedSong,
					selectedListItem, FRONT_CARD_ID);			
			
			if (YPlayer.isPlaying())
				YPlayer.loadVideo(selectedSong.getVideo_id());
			else
				YPlayer.cueVideo(selectedSong.getVideo_id());
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
			//YPlayer.cueVideo(Utils.TEST_VIDEO); // your video to play
		}

	}

	@Override
	public void onInitializationFailure(YouTubeThumbnailView view,
			YouTubeInitializationResult errorReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(YouTubeThumbnailView view,
			YouTubeThumbnailLoader loader) {
		String videoId = (String) view.getTag();
		view.setImageResource(R.drawable.ic_launcher);
		loader.setVideo(videoId);

	}

}
