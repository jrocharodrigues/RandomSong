package com.impecabel.randomsong;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;

public class RetainedFragment extends Fragment {

    // data object we want to retain
    private ArrayList<Song> music;
    private ArrayList<Card> cards;
    private int selectedListItem;
    

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(ArrayList<Song> music, ArrayList<Card> cards, int selectedListItem) {
        this.music = music;
        this.cards = cards;
        this.selectedListItem = selectedListItem;
    }

    public ArrayList<Song> getMusic() {
        return music;
    }
    public ArrayList<Card> getCards() {
    	return cards;
    }
    public int getSelectedListItem() {
    	return selectedListItem;
    }
}
