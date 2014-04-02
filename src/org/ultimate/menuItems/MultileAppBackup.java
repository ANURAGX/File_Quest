package org.ultimate.menuItems;

import java.io.BufferedInputStream;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.anurag.file.quest.AppManager;
import org.anurag.file.quest.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MultileAppBackup{

	ArrayList<ApplicationInfo> list;
	ImageView dialog; 
	TextView title;
	TextView internal;
	TextView avail;
	TextView pName;
	WebView web;
	TextView appV;
	TextView appS;
	TextView appMsg;
	LinearLayout la,la2;
	File file;
	String sav;
	long c;
	private int BUFFER = 1024;
	private byte[] data = new byte[BUFFER];
	private int read = 0;
	PackageManager nPManager;
	String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	String BACKUP_LOCATION = PATH + "/File Quest-Beta/AppBackup/";
	boolean running;
	AppManager manager;
	Context mContext;
	Dialog dial;
	String total,avai;
	long size;
	public MultileAppBackup(Context context,int width,ArrayList<ApplicationInfo> info,
			long count,String to,String av,long s) {
		// TODO Auto-generated constructor stub
		mContext = context;
		size = s;
		total = to;
		avai = av;
		c = count;
		list = info;
		dial = new Dialog(mContext, R.style.custom_dialog_theme);
		dial.setContentView(R.layout.app_backup_dialog);
		dial.getWindow().getAttributes().width = width;
		dial.setCancelable(false);
		onCreate();
	}
	
	
	protected void onCreate() {
		// TODO Auto-generated method stub
				
		//params.width = p.x*7/9;
			
		
		nPManager = mContext.getPackageManager();
		running = true;
		BUFFER = 1024;
		data = new byte[BUFFER];
		read = 0;
		
		PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
		BACKUP_LOCATION = PATH + "/File Quest-Beta/AppBackup/";
		
		
		dialog = (ImageView)dial.findViewById(R.id.appImage);
		title = (TextView)dial.findViewById(R.id.appName);
		internal = (TextView)dial.findViewById(R.id.header);
		avail = (TextView)dial.findViewById(R.id.header2);
		pName = (TextView)dial.findViewById(R.id.appProcessName);
		appV = (TextView)dial.findViewById(R.id.appVersionCode);
		appS = (TextView)dial.findViewById(R.id.appSize);
		appMsg = (TextView)dial.findViewById(R.id.appBackupMessage);
		la =(LinearLayout)dial.findViewById(R.id.appBtnLayout);
		la2 = (LinearLayout)dial.findViewById(R.id.strip);
		web = (WebView)dial.findViewById(R.id.app_Web_View);
		web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		web.setEnabled(false);
		//c = Integer.parseInt(intent.getAction());
		la2.setVisibility(View.GONE);
		la.setVisibility(View.GONE);
		title.setText("Multiple Backups");
		dialog.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_backupall));
		internal.setText(total);
		avail.setText(avai);
		pName.setText("Apps Selected :" + size);
		file = new File(BACKUP_LOCATION);
		File d = new File(PATH + "/File Quest-Beta");
		if(!d.exists()){
			d.mkdir();
			file.mkdir();
		}else{
			if(!file.exists())
				file.mkdir();
		}
		new MulBackup().execute();
	}

	private class MulBackup extends AsyncTask<Void, String, Void>{
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//setResult(RESULT_OK);
			mContext.sendBroadcast(new Intent("FQ_BACKUP"));
			dial.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dial.show();			
		}

		@Override
		protected void onProgressUpdate(String... val) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(val);
			avail.setText(val[0]);
			pName.setText(val[1]);
			appMsg.setText(val[2]);
			appS.setText(val[3]);
			appV.setText(val[4]);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ApplicationInfo in;
			int l = list.size(),j=0;
			for(int i = 0 ; i < l ; ++i){
				in = (ApplicationInfo) list.get(i);
				if(in != null){
					space();
					try{
						String res[] = {sav,"Currently Backing : "+nPManager.getApplicationLabel(in).toString(),
								++j + " of " + c +" backed up" , "App Size :"+size(new File(in.sourceDir)) , 
								"Version : "+nPManager.getPackageInfo(in.packageName, 0).versionName};
						publishProgress(res);
					}catch(NameNotFoundException e){
						
					}
					backup(in);
				}
			}
			return null;
		}
	}
	
	/**
	 * 
	 * @param info
	 */
	void backup(ApplicationInfo info){
		String source = info.sourceDir;
		PackageInfo packageInfo ;
		try {
			packageInfo = nPManager.getPackageInfo(info.packageName, 0);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
			packageInfo = null;
		}
		String out_file = BACKUP_LOCATION + nPManager.getApplicationLabel(info) + "-v"+packageInfo.versionName + ".apk";

		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(out_file));
			while( (read = in.read(data, 0 , BUFFER)) !=-1 )
				out.write(data, 0, read);
			out.flush();
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
			return;
		}
	}
	
	void space(){
		
		StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long av = fs.getAvailableBlocks()*fs.getBlockSize();
		                                                                        
		if(av>1024*1024*1024)
			sav = String.format("Avalaible %.2f GB", (double)av/(1024*1024*1024));
		
		else if(av > 1024*1024)
			sav = String.format("Avalaible %.2f MB", (double)av/(1024*1024));
		
		else if(av>1024)
			sav = String.format("Avalaible %.2f KB", (double)av/(1024));
		else
			sav = String.format("Avalaible %.2f Bytes", (double)av);
		
		
	}
	/**
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	public String size(File f){
		long size = f.length();
		if(size>1024*1024*1024)
			return String.format("%.2f GB", (double)size/(1024*1024*1024));
		
		else if(size > 1024*1024)
			return String.format("%.2f MB", (double)size/(1024*1024));
		
		else if(size>1024)
			return String.format("%.2f KB", (double)size/(1024));
		
		else
			return String.format("%.2f Bytes", (double)size);
	}
	
	
	
	
}
