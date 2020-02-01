package com.idillionaire.app.GoogleAnalytics;

import android.content.Context;
import android.util.Log;

import com.idillionaire.app.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

//public class AnalyticsTrackers {

//    public enum Target{
//        APP,
//    }
//    private static AnalyticsTrackers sInstance;
//
//    public static synchronized void initialize(Context context){
//        if (sInstance != null){
//            throw new IllegalStateException("Extra call to initialize analytics trackers");
//        }
//        sInstance = new AnalyticsTrackers(context);
//    }
//    public static synchronized AnalyticsTrackers getInstance() {
//        if (sInstance == null) {
//            throw new IllegalStateException("Call initialize() before getInstance()");
//        }
//
//        return sInstance;
//    }
//    private final Map<Target, Tracker> mTrackers = new HashMap<Target, Tracker>();
//    private final Context mContext;
//
//    private AnalyticsTrackers(Context context) {
//        mContext = context.getApplicationContext();
//    }
//    public synchronized Tracker get(Target target) {
//        if (!mTrackers.containsKey(target)) {
//            Tracker tracker;
//            switch (target) {
//                case APP:
//                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
//                    break;
//                default:
//                    throw new IllegalArgumentException("Unhandled analytics target " + target);
//            }
//            mTrackers.put(target, tracker);
//        }
//
//        return mTrackers.get(target);
//    }

//}

public class AnalyticsTrackers {

    public enum Target {
        APP,
        // Add more trackers here if you need, and update the code in #get(Target) below
    }

    private static AnalyticsTrackers sInstance;
    public static synchronized void initialize(Context context) {
        if (sInstance == null) {
            sInstance = new AnalyticsTrackers(context);

        }

    }

    public static synchronized AnalyticsTrackers getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Call initialize() before getInstance()");
        }

        return sInstance;
    }

    private final Map<Target, Tracker> mTrackers = new HashMap<Target, Tracker>();
    private final Context mContext;

    /**
     * Don't instantiate directly - use {@link #getInstance()} instead.
     */
    private AnalyticsTrackers(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized Tracker get(Target target,String analyticsId) {
        if (!mTrackers.containsKey(target)) {
            GoogleAnalytics analytics;
            Tracker tracker;
            switch (target) {
                case APP:
                    analytics = GoogleAnalytics.getInstance(mContext);
                    tracker = analytics.newTracker(""+analyticsId);
                    tracker.enableExceptionReporting(true);
                    tracker.enableAdvertisingIdCollection(true);
                    tracker.setSessionTimeout(300);
                    Log.d("tracket",analytics+analyticsId);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
            mTrackers.put(target, tracker);
        }

        return mTrackers.get(target);
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP,"UA-121810080-1");
    }
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(mContext).dispatchLocalHits();
    }
}