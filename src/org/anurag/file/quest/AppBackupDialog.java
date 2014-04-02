/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * anurag.dev1512@gmail.com
 *
 */


package org.anurag.file.quest;

import java.io.File;
import java.sql.Date;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.anurag.file.quest.AppManager;

public class AppBackupDialog{
	
	
	private StatFs to;
	private long sizeTotal;
	private long sizeAvail; 
	private File file;
	private TextView header2;
	private ApplicationInfo info;
	private PackageManager mPManager;
	private Button appOk;
	private Button appCache;
	private TextView appName;
	private ImageView appIcon;
	private TextView appProcessName;
	private TextView appVersionCode;
	private TextView appSize;
	private TextView appBackupMessage;
	private TextView header;
	WebView web;
	private String packageName;
	private AppManager nManager;
	private AppManager.BackupOne m;
	private AppManager.BackupAll a;
	private boolean backupAll = false;
	private LinearLayout strip;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			String[] obj = (String[]) msg.obj;
			switch(msg.what){
				case 0:
					
					PackageInfo packageInfo = null;
					try {
						packageInfo = mPManager.getPackageInfo(obj[0], 0);
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					appVersionCode.setText("VERSION :- v" + packageInfo.versionName);
					file = new File(packageInfo.applicationInfo.sourceDir);
					long size = file.length();
					if(size>1024*1024*1024)
						appSize.setText("APK SIZE :- " + size/(1024*1024*1024) + " GB");
					else if(size>1024*1024)
						appSize.setText("APK SIZE :- " + size/(1024*1024)+ " MB");
					else
						appSize.setText("APK SIZE :- " + size/1024 + " KB");
					appProcessName.setText("PROCESS NAME :-" + obj[0]);
					appBackupMessage.setText("Backing Up :- " + obj[1]);
					{
						StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
						String totalSize;
						long vail = fs.getAvailableBlocks()*fs.getBlockSize();
						if(vail>=1024*1024*1024)
							totalSize = String.format("%.2f GB", (double)vail/(1024*1024*1024));
						else if(vail>=1024*1024)
							totalSize = String.format("%.2f MB", (double)vail/(1024*1024));
						else if(vail>=1024)
							totalSize = String.format("%.2f KB", (double)vail/(1024));
						else
							totalSize = String.format("%.2f Byte", (double)vail);
						header2.setText("AVAILABLE SPACE :- " + totalSize);
					}
					break;
				
				case 2:
					appOk.setVisibility(View.GONE);
					appCache.setText("Ok");
					web.setVisibility(View.GONE);
					if(backupAll)
						appBackupMessage.setText("All Apps Successfully Backed Up");
					else
						appBackupMessage.setText("Backup Completed");
					appCache.setEnabled(true);
					dialog.setCancelable(true);
			}
		}
	};
	private Thread thread;
	
	Context mContext;
	Dialog dialog;
	
	public AppBackupDialog(Context context,int  width,String data) {
		// TODO Auto-generated constructor stub
		mContext = context;
		packageName = data;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.app_backup_dialog);
		dialog.getWindow().getAttributes().width = width;
		onCreate();
	}
	
	
	void onCreate() {
		
		
		strip = (LinearLayout)dialog.findViewById(R.id.strip);
		mPManager = mContext.getPackageManager();
		appOk = (Button)dialog.findViewById(R.id.appBackupBtn);
		appCache = (Button)dialog.findViewById(R.id.appCacheBtn);
		
		
		header2 = (TextView)dialog.findViewById(R.id.header2);
		header = (TextView)dialog.findViewById(R.id.header);
		//bar = (ProgressBar)dialog.findViewById(R.id.appProgressBar); 
		nManager = new AppManager(mContext, mHandler);
		appName = (TextView)dialog.findViewById(R.id.appName);
		appIcon = (ImageView)dialog.findViewById(R.id.appImage);
		appProcessName = (TextView)dialog.findViewById(R.id.appProcessName);
		appSize = (TextView)dialog.findViewById(R.id.appSize);
		appVersionCode = (TextView)dialog.findViewById(R.id.appVersionCode);
		appBackupMessage = (TextView)dialog.findViewById(R.id.appBackupMessage);
		web = (WebView)dialog.findViewById(R.id.app_Web_View);
		web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		web.setEnabled(false);
		
		String totalSize = null;
		to = new StatFs(Environment.getExternalStorageDirectory().getPath());
		sizeTotal = to.getBlockCount()*to.getBlockSize();
		if(sizeTotal>=1024*1024*1024)
			totalSize = String.format("%.2f GB", (double)sizeTotal/(1024*1024*1024));
		else if(sizeTotal>=1024*1024)
			totalSize = String.format("%.2f MB", (double)sizeTotal/(1024*1024));
		header.setText("INTERNAL STORAGE :- " + totalSize);
		getAvailSize();	
		
		/**
		 *Condition with handler when all apps have to backed up 
		 */
		if(packageName.endsWith("BackupAll")){
			dialog.setCancelable(false);
			LinearLayout l = (LinearLayout)dialog.findViewById(R.id.appBtnLayout);
			l.setVisibility(View.GONE);
			String data = packageName;
			if(data.charAt(0) == '1'){
				nManager.SHOW_APP = 1;
				appName.setText("Backing Up Downloaded Apps");
				appIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_user));
			}
				
			else if(data.charAt(0) == '2'){
				appIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_system));
				nManager.SHOW_APP = 2;
				appName.setText("Backing Up System Apps");
			}else{
				appIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_both));
				nManager.SHOW_APP = 3;
				appName.setText("Backing Up Both Of Them");
			}
			a = nManager.new BackupAll();
			thread = new Thread(a);
			thread.start();
			appOk.setVisibility(View.GONE);
			appCache.setVisibility(View.GONE);
			strip.setVisibility(View.GONE);
			backupAll = true;
		}
		
		/**
		 * Only one app is backed up 
		 */
		else{
			
			PackageInfo packInfo = null;
			//packageName = intent.getData().toString();
			try {
				packInfo = mPManager.getPackageInfo(packageName, 0);
				info = mPManager.getApplicationInfo(packageName, 0);
				appIcon.setImageDrawable(mPManager.getApplicationIcon(info));
				appName.setText(mPManager.getApplicationLabel(info).toString().toUpperCase());
				file = new File(info.sourceDir);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			long size = file.length();
			if(size>1024*1024*1024)
				appSize.setText("APK SIZE :- " + size/(1024*1024*1024) + " GB");
			else if(size>1024*1024)
				appSize.setText("APK SIZE :- " + size/(1024*1024)+ " MB");
			else
				appSize.setText("APK SIZE :- " + size/1024 + " KB");
			appProcessName.setText("PROCESS NAME :-" + info.packageName);
			appVersionCode.setText("VERSION CODE :- v" + packInfo.versionName);
			web.setVisibility(View.GONE);
			if(nManager.backupExists(info) == 0)
				appBackupMessage.setText("No Backup Taken");
			else{
				Date d = new Date(nManager.backupExists(info));
				appBackupMessage.setText("One Backup Was Taken On :" + d); 
			}
				
			m = nManager.new BackupOne(info);
			thread = new Thread(m);
			appOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					web.setVisibility(View.VISIBLE);
					dialog.setCancelable(false);
					appOk.setEnabled(false);
					appCache.setEnabled(false);
					thread.start();
					backupAll = true;
				}
			});
			appCache.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(backupAll){
						dialog.dismiss();	
						mContext.sendBroadcast(new Intent("FQ_BACKUP"));
					}
					else
						dialog.dismiss();
				}
			});
		}
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if(backupAll)
					mContext.sendBroadcast(new Intent("FQ_BACKUP"));
			}
		});
	}
	
	
	public void getAvailSize(){
		String totalSize;
		sizeAvail = to.getAvailableBlocks()*to.getBlockSize();
		if(sizeAvail>=1024*1024*1024)
			totalSize = String.format("%.2f GB", (double)sizeAvail/(1024*1024*1024));
		else if(sizeAvail>=1024*1024)
			totalSize = String.format("%.2f MB", (double)sizeAvail/(1024*1024));
		else if(sizeAvail>=1024)
			totalSize = String.format("%.2f KB", (double)sizeAvail/(1024));
		else
			totalSize = String.format("%.2f Byte", (double)sizeAvail);
		header2.setText("AVAILABLE SPACE :- " + totalSize);
	}
	
	

}
