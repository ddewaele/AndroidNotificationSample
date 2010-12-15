package com.ecs.android.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AndroidNotificationSample extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Intent locationRecordSyncServiceIntent = new Intent(getApplicationContext(), RecordSyncService.class);
        
        Button startSync = (Button) findViewById(R.id.btn_start_sync);
        startSync.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startService(locationRecordSyncServiceIntent);
            }
        });
        
        Button cancelSync = (Button) findViewById(R.id.btn_cancel_sync);
        cancelSync.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	stopService(locationRecordSyncServiceIntent);
            }
        });        
    }
}