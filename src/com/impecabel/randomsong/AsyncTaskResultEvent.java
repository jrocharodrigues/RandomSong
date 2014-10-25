package com.impecabel.randomsong;

import java.util.ArrayList;

public class AsyncTaskResultEvent {
	 
	  private ArrayList<Song> result;
	 
	  public AsyncTaskResultEvent(ArrayList<Song> result) {
	    this.result = result;
	  }
	 
	  public ArrayList<Song> getResult() {
	    return result;
	  }
}
