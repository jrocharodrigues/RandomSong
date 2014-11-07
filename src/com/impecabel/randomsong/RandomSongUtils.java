package com.impecabel.randomsong;

import java.util.List;

import com.google.android.gms.cast.MediaTrack;

public class RandomSongUtils {
    /**
    * Please replace this with a valid API key which is enabled for the 
    * YouTube Data API v3 service. Go to the 
    * <a href=�https://code.google.com/apis/console/�>Google APIs Console</a> to
    * register a new developer key.
    */
    public static final String DEVELOPER_KEY = "AIzaSyCoPvqf-bthT7iJ4WFwNvfto5Uy9ChR9Qw"; 
    public static final String TEST_VIDEO = "9dEW2OE_joU"; 
    public static final String PREFS_NAME = "randomSongPrefsFile";
    public static final String APP_ID = "1CBBAB48";
    public static final String NAMESPACE = "urn:x-cast:com.ls.cast.sample";
    
    
    /*cast*/
    public static final String TERMINATION_POLICY_KEY = "termination_policy";
    public static final String STOP_ON_DISCONNECT = "1";
    public static final String title = "Big Buck Bunny";
    public static final String studio = "Google IO - 2014";
    public static final String subtitle = "Fusce id nisi turpis. Praesent viverra bibendum semper. Donec tristique, orci sed semper lacinia, quam erat rhoncus massa, non congue tellus est quis tellus. Sed mollis orci venenatis quam scelerisque accumsan. Curabitur a massa sit amet mi accumsan mollis sed et magna. Vivamus sed aliquam risus. Nulla eget dolor in elit facilisis mattis. Ut aliquet luctus lacus. Phasellus nec commodo erat. Praesent tempus id lectus ac scelerisque. Maecenas pretium cursus lectus id volutpat.";
    public static final String url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    public static final String imgUrl = "images_480x270/BigBuckBunny.jpg";
    public static final String bigImageUrl = "images_780x1200/BigBuckBunny-780x1200.jpg";
    public static final List<MediaTrack> tracks = null;
    
    
}