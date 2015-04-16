package com.cloudnapps.bethany;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

/**
 * Created by xiaoooyu on 4/15/15.
 *
 * implements BootstrapNotifier thus application can be launched on Beacon detection
 */
public class BethanyApplication extends Application implements BootstrapNotifier {

    // target advertiser's Beacon UUID
    private final static String BEACON_UUID = "189F5F6B-6955-49C0-BE66-8F159D2A796F";
//    private final static String BEACON_UUID = "224B4B7D-B480-4F0C-A633-1BD3890725F2";

    private final static String TAG = "BethanyApplication";

    private BackgroundPowerSaver mBackgroundPowerSaver;
    private BeaconManager mBeaconManager;
    private Region mAllBeaconsRegion;
    private RegionBootstrap mRegionBootstrap;
    private int mNotificationId = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        // Simply constructing this class and holding a reference to it in your custom Application class
        // enables auto battery saving of about 60%
        mBackgroundPowerSaver = new BackgroundPowerSaver(this);

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.setDebug(true);
        //to monitor iBeacon, we need this beacon layout parser
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        //find a balance between quick response and battery saving
        mBeaconManager.setBackgroundScanPeriod(2000l); //1.1 seconds
        mBeaconManager.setBackgroundBetweenScanPeriod(3000l); //3 seconds

        // wake up the app when any beacon is seen (you can specify specific id filers in the parameters below)
        mAllBeaconsRegion = new Region("com.cloudnapps.backgroundRegion", Identifier.parse(BEACON_UUID), null, null);
        mRegionBootstrap = new RegionBootstrap(this, mAllBeaconsRegion);
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "did enter region.");
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Important:  make sure to add android:launchMode="singleInstance" in the manifest
        // to keep multiple copies of this activity from getting created if the user has
        // already manually launched the app.
//        this.startActivity(intent);

        sendNotification();
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    /**
     * send user notification while detect Beacon
     **/
    public void sendNotification(){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Beacon is detected")
                .setContentText("Click to see the detail")
                .setAutoCancel(true);

        // Create an explicit intent for an Activity in app
        Intent intent = new Intent(this, MainActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mNotificationId, builder.build());
    }
}
