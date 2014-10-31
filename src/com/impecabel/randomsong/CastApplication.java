package com.impecabel.randomsong;

import android.app.Application;
import android.content.Context;

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.utils.Utils;

public class CastApplication extends Application {
    private static VideoCastManager mCastMgr = null;
    public static final double VOLUME_INCREMENT = 0.05;

    /*
     * (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.saveFloatToPreference(getApplicationContext(),
                VideoCastManager.PREFS_KEY_VOLUME_INCREMENT, (float) VOLUME_INCREMENT);

    }

    public static VideoCastManager getCastManager(Context context) {
        if (null == mCastMgr) {
            mCastMgr = VideoCastManager.initialize(context, RandomSongUtils.APP_ID, null, null);
            mCastMgr.enableFeatures(
                    VideoCastManager.FEATURE_NOTIFICATION |
                            VideoCastManager.FEATURE_LOCKSCREEN |
                            VideoCastManager.FEATURE_WIFI_RECONNECT |
                            VideoCastManager.FEATURE_CAPTIONS_PREFERENCE |
                            VideoCastManager.FEATURE_DEBUGGING);

        }
        mCastMgr.setContext(context);
        String destroyOnExitStr = Utils.getStringFromPreference(context,
        		RandomSongUtils.TERMINATION_POLICY_KEY);
        mCastMgr.setStopOnDisconnect(null != destroyOnExitStr
                && RandomSongUtils.STOP_ON_DISCONNECT.equals(destroyOnExitStr));
        return mCastMgr;
    }

}
