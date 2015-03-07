/**
 * Copyright(c) 2013 DRAWNZER.ORG PROJECTS -> ANURAG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *                             
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import org.anurag.adapters.AppAdapter;
import org.anurag.fragments.AppStore;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * 
 * this class handles the app backup operation
 * 
 * @author Anurag
 *
 */
public class AppBackup {

	private boolean running;
	private int read;
	private PackageManager mPack;
	private File file ;
	private byte data[] = new byte[Constants.BUFFER];
	private long prog;
	
	private String appsize;
	private String status;
	private String space;
	private String pname;
	private String vname;
	private String appnam;
	private Thread thread;
	private ProgressBar progress;
	private MaterialDialog dialog;
	
	/**
	 * 
	 * @param ctx
	 * @param width FOR DIALOG BOX...
	 * @param list OF APPS TO BE BACKED UP....
	 */
	public AppBackup(final Context ctx , final ArrayList<ApplicationInfo> list) {
		// TODO Auto-generated constructor stub
		running = false;
		
		LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inf.inflate(R.layout.app_backup_dialog, null , false);
		
		
		MaterialDialog.Builder build = new MaterialDialog.Builder(ctx);
		build.title(R.string.app_backup)
		.positiveText(R.string.takebackup)
		.negativeText(R.string.dismiss)
		.customView(view, true)
		.callback(new ButtonCallback() {

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				
				//already running, so returning
				if(running){
					return;
				}
				
				thread.start();
				running = true;
				dialog.setCancelable(false);
				progress.setVisibility(View.VISIBLE);
				
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				running = false;
				dialog.dismiss();
			}
			
		})
		.autoDismiss(false);
		
		
		
		mPack = ctx.getPackageManager();
		progress = (ProgressBar)view.findViewById(R.id.progress);
		final TextView appsize = (TextView)view.findViewById(R.id.appSize);
		final TextView version = (TextView)view.findViewById(R.id.appVersionCode);
		final TextView packagename = (TextView)view.findViewById(R.id.appProcessName);
		final TextView lastbackup = (TextView)view.findViewById(R.id.appBackupMessage);
		final TextView appname = (TextView)view.findViewById(R.id.appname);
		final TextView spaceleft = (TextView)view.findViewById(R.id.header2);
		spaceleft.setText(spaceleft(new File(Environment.getExternalStorageDirectory().getPath()).getFreeSpace(), ctx));
		TextView spaceavail = (TextView)view.findViewById(R.id.header);
		spaceavail.setText(spaceavail(new File(Environment.getExternalStorageDirectory().getPath()).getTotalSpace(), ctx));
		
		
		/**
		 * SINGLE APP HAS TO BE BACKED UP.....
		 */
		if(list.size()==1){
			appname.setText(ctx.getString(R.string.appname)+" "+list.get(0).loadLabel(mPack));
			appsize.setText(size(new File(list.get(0).sourceDir).length(),ctx));
			packagename.setText(ctx.getString(R.string.packagename)+" "+list.get(0).packageName);
			try {
				PackageInfo pin = mPack.getPackageInfo(list.get(0).packageName, 0);
				version.setText(ctx.getString(R.string.appversion)+" "+pin.versionName);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long date =AppAdapter.backupExists(list.get(0));
			if(date==0)
				lastbackup.setText(ctx.getString(R.string.nobackupexists));
			else
				lastbackup.setText(ctx.getString(R.string.backupexists)+" "+new Date(date));
		}	
		/**
		 * MORE THAN ONE APP HAS TO BE BACKED UP.....
		 */
		else{
			int l=list.size(),j=0;
			for(int i=0;i<l;++i)
				if(list.get(i)!=null)
					j++;
			
			appname.setText(ctx.getString(R.string.appname)+" ?");
			appsize.setText(ctx.getString(R.string.totalapps)+" "+j);
			version.setText(ctx.getString(R.string.appversion)+" ?");
			packagename.setText(ctx.getString(R.string.packagename)+" ?");
			lastbackup.setText(ctx.getString(R.string.backupexists)+" ?");
		}	
		progress.setVisibility(View.GONE);
		
		
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0:
							appname.setText(appnam);
							progress.setProgress(0);
							appsize.setText(AppBackup.this.appsize);
							progress.setMax((int)prog);
							prog = 0;
							packagename.setText(pname);
							version.setText(vname);
							break;
					case 1:
							
							spaceleft.setText(space);
							progress.setProgress((int)prog);
							lastbackup.setText(status);
							break;
							
					case 2:
							dialog.dismiss();
							ctx.sendBroadcast(new Intent("UPDATE_SPACE"));
							if(!running){
								Toast.makeText(ctx, ctx.getString(R.string.backupinterrupted), Toast.LENGTH_SHORT).show();
							}if(FileQuestHD.getCurrentItem() == 3){
								AppStore.resetAdapter();
							}
				}
			}
		};
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(running){
					int len = list.size();
					ApplicationInfo info;
					file = new File(Environment.getExternalStorageDirectory().getPath()+"/File Quest/AppBackup");
					if(!file.exists())
						file.mkdirs();
					for(int i=0;i<len&&running;++i){
						info = list.get(i);
						if(info!=null){
							try {
								PackageInfo pin = mPack.getPackageInfo(info.packageName, 0);
								file = new File(new File(Environment.getExternalStorageDirectory().getPath()+"/File Quest/AppBackup"),
										mPack.getApplicationLabel(info) + "-v"+pin.versionName+".apk");
								
								//SENDING APP SIZE....
								prog = new File(info.sourceDir).length();
								AppBackup.this.appsize = size(prog, ctx);
								pname = ctx.getString(R.string.packagename)+" "+info.packageName;
								vname = ctx.getString(R.string.appversion)+" "+pin.versionName;
								appnam = ctx.getString(R.string.appname)+" "+info.loadLabel(mPack);
								handle.sendEmptyMessage(0);
								
								BufferedInputStream in = new BufferedInputStream(new FileInputStream(info.sourceDir));
								BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
								while((read=in.read(data, 0, Constants.BUFFER))!=-1 && running){
									out.write(data, 0, read);
									prog+=read;
									status = status(prog, ctx);
									space = spaceleft(new File(Environment.getExternalStorageDirectory().getPath()).getFreeSpace(), ctx);
									handle.sendEmptyMessage(1);
								}
								out.flush();
								out.close();
								in.close();
								addToFileGallery(file);
								
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}catch(NameNotFoundException e){
								
							}catch(IOException e){
								
							}							
						}
					}
					handle.sendEmptyMessage(2);					
				}
			}
		});			
		dialog = build.show();		
	}
	
	/**
	 * 
	 * @param size
	 * @param ctx
	 * @return
	 */
	static public String size(long size , Context ctx){
		if(size>Constants.GB)
			return String.format(ctx.getString(R.string.appsizegb), (double)size/Constants.GB);
		else if(size>Constants.MB)
			return String.format(ctx.getString(R.string.appsizemb), (double)size/Constants.MB);
		else
			if(size>1024)
				return String.format(ctx.getString(R.string.appsizekb), (double)size/1024);
		else
			return String.format(ctx.getString(R.string.appsizebytes), (double)size);
				
	}	
	
	/**
	 * 
	 * @param size
	 * @param ctx
	 * @return
	 */
	static public String status(long size,Context ctx){
		if(size>Constants.GB)
			return String.format(ctx.getString(R.string.appstatusgb), (double)size/Constants.GB);	
		else if(size>Constants.MB)
			return String.format(ctx.getString(R.string.appstatusmb), (double)size/Constants.MB);
		else
			if(size>1024)
				return String.format(ctx.getString(R.string.appstatuskb), (double)size/1024);
		else
			return String.format(ctx.getString(R.string.appstatusbytes), (double)size);
	}
	
	/**
	 * 
	 * @param size
	 * @param ctx
	 * @return
	 */
	private String spaceavail(long size,Context ctx){
		if(size>Constants.GB)
			return String.format(ctx.getString(R.string.spaceavailgb), (double)size/Constants.GB);
		else if(size>Constants.MB)
			return String.format(ctx.getString(R.string.spaceavailmb), (double)size/Constants.MB);
		else if(size>1024)
				return String.format(ctx.getString(R.string.spaceavailkb), (double)size/1024);
		else
			return String.format(ctx.getString(R.string.spaceavailbytes), (double)size);
	}
	
	/**
	 * 
	 * @param size
	 * @param ctx
	 * @return
	 */
	private String spaceleft(long size,Context ctx){
		if(size>Constants.GB)
			return String.format(ctx.getString(R.string.spaceleftgb), (double)size/Constants.GB);
		else if(size>Constants.MB)
			return String.format(ctx.getString(R.string.spaceleftmb), (double)size/Constants.MB);
		else if(size>1024)
				return String.format(ctx.getString(R.string.spaceleftkb), (double)size/1024);
		else
			return String.format(ctx.getString(R.string.spaceleftbytes), (double)size);
	}
	
	/**
	 * function to the backed up apk to file gallery list....
	 * @param f
	 */
	private void addToFileGallery(File f){
		String path = f.getPath();
		Item itm = new Item(f, Constants.APP, Utils.apkType, "");
		Utils.appKey.put(""+Utils.appCounter++, path);
		Utils.apps.put(path, itm);
		Utils.apksize+=f.length();
		Utils.asize = Utils.size(Utils.apksize);
		
	}
}
