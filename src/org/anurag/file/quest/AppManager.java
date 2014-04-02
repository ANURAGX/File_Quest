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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class AppManager {
	private String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public int SHOW_APP = 2;
	private Message msg;
	public ArrayList<ApplicationInfo> ba;
	public ArrayList<ApplicationInfo> no;
	private ApplicationInfo info;
	public ArrayList<ApplicationInfo> nList;
	private Context mContext;
	private PackageManager nPManager;
	private int FLAG_UPDATED_SYS_APP = 0x80;
	private int BACKUP_STARTED = 0;
	private int BACKUP_FINISHED = 2;
	private final int BUFFER = 5*8192;
	private byte[] data = new byte[BUFFER];
	private int read = 0;
	private File file;
	private String BACKUP_LOCATION = PATH + "/File Quest/AppBackup/";
	private Handler mHandler;
	public AppManager(Context context , Handler handler){
		mContext = context;
		nList = new ArrayList<ApplicationInfo>();
		nPManager = mContext.getPackageManager();
		mHandler = new Handler();
		this.mHandler = handler;
	}
	
	public AppManager(Context context){
		mContext = context;
		nList = new ArrayList<ApplicationInfo>();
		nPManager = mContext.getPackageManager();
		ba = new ArrayList<ApplicationInfo>();
		no = new ArrayList<ApplicationInfo>();
	}
	
	
	public ArrayList<ApplicationInfo> get_downloaded_apps() {
		List<ApplicationInfo> all_apps = nPManager.getInstalledApplications(
											PackageManager.GET_META_DATA);
		nList.clear();
		for(ApplicationInfo appInfo : all_apps) {
			if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && 
			   (appInfo.flags & FLAG_UPDATED_SYS_APP) == 0 && 
			   appInfo.flags != 0)
					nList.add(appInfo);
		}
		return nList;
	}
	
	
	public ArrayList<ApplicationInfo> getSysApps() {
		nList.clear();
		List<ApplicationInfo> all_apps = nPManager.getInstalledApplications(
											PackageManager.GET_UNINSTALLED_PACKAGES);
		
		for(ApplicationInfo appInfo : all_apps) {
			if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1 && 
			   (appInfo.flags & FLAG_UPDATED_SYS_APP) == 0 && 
			   appInfo.flags != 0)
				
				nList.add(appInfo);
		}
		return nList;
	}
	
	public ArrayList<ApplicationInfo>getAllApps(){
		ArrayList<ApplicationInfo> list = new ArrayList<ApplicationInfo>();
		List<ApplicationInfo> all_apps = nPManager.getInstalledApplications(ApplicationInfo.FLAG_SYSTEM);
		for(ApplicationInfo appInfo : all_apps) {
			list.add(appInfo);
		}
		return list;
	}
	
	public ArrayList<ApplicationInfo> giveMeAppList(){
		if(SHOW_APP == 1)
			return get_downloaded_apps();
		else if(SHOW_APP == 2)
			return getSysApps();
		else if(SHOW_APP == 3)
			return getAllApps();
		
		return null;
	}
	
	public long backupExists(ApplicationInfo in){
		PackageInfo i = null;
		String PATH = Environment.getExternalStorageDirectory().getPath();
		try {
			i = nPManager.getPackageInfo(in.packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		File file = new File(PATH + "/File Quest/AppBackup");
		if( file.exists() && new File(PATH + "/File Quest").exists()){
			for(File f:file.listFiles()){
				String out_file = nPManager.getApplicationLabel(in) + "-v"+i.versionName + ".apk";
				if(f.getName().equals(out_file)){
					return f.lastModified();
				}
			}
		}
		return 0;
	}
	
	
	
	/**
	 * This Function Takes The Backup Of One App At A Time
	 * @author Anurag
	 *
	 */
	public class BackupOne implements Runnable{
		 
		public BackupOne(ApplicationInfo nInfo){
			info = nInfo;
			file = new File(BACKUP_LOCATION);
			File d = new File(PATH + "/File Quest");
			if(!d.exists()){
				d.mkdir();
				file.mkdir();
			}else{
				if(!file.exists())
					file.mkdir();
			}
		}
		@Override
		public void run() {
			Message msg;
			String source = info.sourceDir;
			PackageInfo packageInfo = null;
			try {
				packageInfo = nPManager.getPackageInfo(info.packageName, 0);
			} catch (NameNotFoundException e1) {
				e1.printStackTrace();
			}
			String out_file = BACKUP_LOCATION + nPManager.getApplicationLabel(info) + "-v"+packageInfo.versionName + ".apk";

			try {
				msg = new Message();
				msg.what = BACKUP_STARTED;
				String[] appInfo = {info.packageName , nPManager.getApplicationLabel(info).toString()};
				msg.obj = appInfo;
				mHandler.sendMessage(msg);
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(out_file));
				while( (read = in.read(data, 0 , BUFFER)) !=-1 )
					out.write(data, 0, read);
				out.flush();
				out.close();
				in.close();
				mHandler.sendEmptyMessage(BACKUP_FINISHED);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public class BackupAll implements Runnable{
		public BackupAll(){
			nList = giveMeAppList();
			file = new File(BACKUP_LOCATION);
			File d = new File(PATH + "/File Quest");
			if(!d.exists()){
				d.mkdir();
				file.mkdir();
			}else{
				if(!file.exists())
					file.mkdir();
			}
		}
		@Override
		public void run() {
			
			for(int i = 0 ; i < nList.size() ; ++i){
				info = nList.get(i);
				String source = info.sourceDir;
				PackageInfo packageInfo = null;
				try {
					packageInfo = nPManager.getPackageInfo(info.packageName, 0);
				} catch (NameNotFoundException e1) {
					e1.printStackTrace();
				}
				String outSource = BACKUP_LOCATION + nPManager.getApplicationLabel(info) + "-v" +packageInfo.versionName + ".apk";
				String[] appInfo = {info.packageName , nPManager.getApplicationLabel(info).toString()};
				try {
					msg = new Message();
					msg.what = BACKUP_STARTED;
					msg.obj = appInfo;
					mHandler.sendMessage(msg);
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outSource));
					while( (read = in.read(data, 0 , BUFFER)) != -1 )
						out.write(data, 0 , read);
						out.flush();
						out.close();
						in.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			mHandler.sendEmptyMessage(BACKUP_FINISHED);
		}
		
	}
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	public class FlashableZips implements Runnable{
		ZipOutputStream out;
		FileOutputStream dest;
		BufferedInputStream origin;
		int flashType;
		/**
		 * 
		 * @param context
		 * @param type 1 for user Apps and other for system apps
		 * @throws FileNotFoundException
		 */
		public FlashableZips(Context context , int type) throws FileNotFoundException {
			File file = new File(PATH + "/File Quest - Beta");
			if(!file.exists())
				file.mkdir();
			dest = new FileOutputStream(new File(PATH + "/File Quest/Flashable.zip"));
			out = new ZipOutputStream(dest);
			origin = null;
			mContext = context;
			flashType = type; 
			nList = get_downloaded_apps();
		}
		@Override
		public void run() {
			for(int i = 0 ; i<nList.size() ; ++i ){
				try {
					String str = nList.get(i).sourceDir;
					String label = nPManager.getApplicationLabel(nList.get(i)).toString();
					msg = new Message();
					msg.what = BACKUP_STARTED;
					msg.obj = label;
					mHandler.sendMessage(msg);
					FileInputStream in =new FileInputStream(str);
					origin = new BufferedInputStream(in , BUFFER);
					ZipEntry entry = new ZipEntry( "system/" + label + ".apk");
					out.putNextEntry(entry);
					while ( (read = origin.read(data, 0 , BUFFER)) != -1 )
						out.write(data, 0 , read);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			try {
				msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
				InputStream in;
				//Copy Script File as per user selection
				if(flashType == 1)
					in = (mContext.getAssets().open("users/updater-script"));
				else 
					in = (mContext.getAssets().open("system/updater-script"));
				origin = new BufferedInputStream(in , BUFFER);
				ZipEntry entry = new ZipEntry( "META-INF/com/google/android/updater-script");
				out.putNextEntry(entry);
				while ( (read = origin.read(data, 0 , BUFFER)) != -1 )
					out.write(data, 0 , read);
				//Copy Binary File From Assets
				in = (mContext.getAssets().open("META-INF/com/google/android/update-binary"));
				origin = new BufferedInputStream(in , BUFFER);
				entry = new ZipEntry( "META-INF/com/google/android/update-binary");
				out.putNextEntry(entry);
				while ( (read = origin.read(data, 0 , BUFFER)) != -1 )
					out.write(data, 0 , read);
				out.close();
				origin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mHandler.sendEmptyMessage(BACKUP_FINISHED);
		}
		
	}
	
	
	public class FlashableZip implements Runnable{
		ZipOutputStream out;
		BufferedInputStream origin;
		FileInputStream in;
		FileOutputStream dest;
		int flashType = 0;
		ZipEntry entry;
		/**
		 * @param info
		 * @param context
		 * @param type
		 * @throws FileNotFoundException 
		 */
		public FlashableZip(String pos , Context context , int type) throws FileNotFoundException {
			File file = new File(PATH + "/File Quest");
			if(!file.exists())
				file.mkdir();
			dest = new FileOutputStream(new File(PATH + "/File Quest/Flashable.zip"));
			out = new ZipOutputStream(dest);
			PackageInfo i = null;
			try {
				i = nPManager.getPackageInfo(pos, 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			in = new FileInputStream(i.applicationInfo.sourceDir);
			entry = new ZipEntry("system/" + nPManager.getApplicationLabel(i.applicationInfo) + ".apk");			origin = new BufferedInputStream(in , BUFFER);
			mContext = context;
			flashType = type;
		}

		@Override
		public void run() {
			try{
				msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
				out.putNextEntry(entry);
				while( (read = origin.read(data, 0 , BUFFER)) !=-1)
					out.write(data, 0 , read);
				InputStream ip;
				if(flashType == 1)
					ip = mContext.getAssets().open("users/updater-script");
				else 
					ip = mContext.getAssets().open("system/updater-script");
				origin = new BufferedInputStream(ip,BUFFER);
				entry = new ZipEntry("META-INF/com/google/android/updater-script");
				out.putNextEntry(entry);
				while((read=origin.read(data, 0, BUFFER))!=-1)
					out.write(data, 0, read);
				ip = mContext.getAssets().open("META-INF/com/google/android/update-binary");
				origin = new BufferedInputStream(ip , BUFFER);
				entry = new ZipEntry("META-INF/com/google/android/update-binary");
				out.putNextEntry(entry);
				while((read = origin.read(data, 0, BUFFER))!=-1)
					out.write(data, 0, read);
				out.close();
				origin.close();
				mHandler.sendEmptyMessage(BACKUP_FINISHED);
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
}
