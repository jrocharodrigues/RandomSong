package com.impecabel.randomsong;

import com.google.android.youtube.player.YouTubeThumbnailView;

public class Card {
	private int song_position;
	
	private YouTubeThumbnailView thumbnail;
	public Card(int song_position, YouTubeThumbnailView thumbnail) {
		this.song_position = song_position;
		this.thumbnail = thumbnail;
	}
	public Card() {
		this.song_position = -1;
	}
	/**
	 * @return the song_postion
	 */
	public int getSong_position() {
		return song_position;
	}
	/**
	 * @param song_postion the song_postion to set
	 */
	public void setSong_position(int song_position) {
		this.song_position = song_position;
	}
	/**
	 * @return the thumbnail
	 */
	public YouTubeThumbnailView getThumbnail() {
		return thumbnail;
	}
	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(YouTubeThumbnailView thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}
