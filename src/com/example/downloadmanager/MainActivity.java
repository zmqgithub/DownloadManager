package com.example.downloadmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private DownloadManager manager;
	private long dqueue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		findViewById(R.id.button_download).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
	            Request request = new Request(Uri.parse("http://s3.amazonaws.com/hussvideo/01-Neze-per-hai-sare-a3a964.mp3"));
	            dqueue = manager.enqueue(request);
			}
		});
		
		findViewById(R.id.button_view).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 	Intent i = new Intent();
		            i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
		            startActivity(i);
			}
		});
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			 String action = intent.getAction();
			 if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
				 Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
				 long extraDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				 Query query = new Query();
                 query.setFilterById(dqueue);
                 Cursor c = manager.query(query);
                 if (c.moveToFirst()) {
                     int columnIndex = c
                             .getColumnIndex(DownloadManager.COLUMN_STATUS);
                     if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    	 
//                         ImageView view = (ImageView) findViewById(R.id.imageView1);
                         String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//                         view.setImageURI(Uri.parse(uriString));
                     }
                 }
			 }
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
