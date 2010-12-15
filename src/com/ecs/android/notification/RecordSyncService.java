package com.ecs.android.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.RemoteViews;

/**
 * SyncService that simulates records to be synced.
 */
public class RecordSyncService extends Service {

	private static final String NOTIFICATION_TEXT = "RecordSyncService running...";
	private static final String STATUS_TEXT = "Syncing records";
	
    private NotificationManager notificationManager;
    private Notification notification; 
    
    private static final int NOTIFICATION_SYNC = 2;
    
	private int nrOfRecords = 0;
	private int nrOfRecordsProcessed=0;
	
	private boolean isSyncing=false;
	
	public boolean isSyncing() {
		return isSyncing;
	}
	
	public void onCreate() {
		super.onCreate();
		isSyncing=true;

		  new Thread() {
			    @Override public void run() {
					initializeNotification();
					syncRecords();
					cancelNotification();
			    }
			  }.start();
	}
	
	private void initializeNotification() {
	    notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	    notification = new Notification(R.drawable.sync_titlebar_icon, NOTIFICATION_TEXT, System.currentTimeMillis());
	    
	    Intent intent = new Intent(this, RecordSyncService.class);
	    final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

	    notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
	    notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.download_progress);
	    notification.contentIntent = pendingIntent;
	    notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.sync_status_icon);
	    notification.contentView.setTextViewText(R.id.status_text, STATUS_TEXT);
	    notification.contentView.setProgressBar(R.id.status_progress, 100, 0, false);
	    notificationManager.notify(NOTIFICATION_SYNC, notification);	
	}	
	
	private void cancelNotification() {
		notificationManager.cancel(NOTIFICATION_SYNC);
		stopSelf();
	}
	
	private void syncRecords() {
	    nrOfRecords = 10;
	    nrOfRecordsProcessed=0;
	    for (int i=0 ; i<nrOfRecords ; i++) {
	    	nrOfRecordsProcessed++;
	    	SystemClock.sleep(1000);
	        publishProgress();
	    }

	}

	private void publishProgress() {
		float a = nrOfRecordsProcessed;
		float b = nrOfRecords;
		float c= 100;
		Float result = a/b*c;
		notification.contentView.setProgressBar(R.id.status_progress, 100, result.intValue(), false);
		notification.contentView.setTextViewText(R.id.status_text, "Syncing entry " + nrOfRecordsProcessed + "/" + nrOfRecords);
        notificationManager.notify(NOTIFICATION_SYNC, notification);
	}	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isSyncing=false;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
