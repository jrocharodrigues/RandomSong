package com.impecabel.randomsong;

public class Song {
	private int id;
	private String title;
	private String artist;
	private String duration;
	private String thumbUrl;
	private String lyrics;
	
	public Song( String id, String title, String artist) {
		this.id = Integer.parseInt(id);
		this.title = title;
		this.artist = artist;
		this.duration = null;
		this.thumbUrl = null;
		this.lyrics = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
	
	@Override
	public String toString() {
		return artist + " - " + title;
	}

}
