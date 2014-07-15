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
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;


/**
 * THIS CLASS GENERATES THE LIST OF USER,SYSTEM AND COMBINATION OF KINDS OF APPS
 * IN ARRAYLIST<APPLICATIONINFO>
 * @author anurag
 *
 */
public class AppManager {
	public int SHOW_APP = 2;
	public ArrayList<ApplicationInfo> ba;
	public ArrayList<ApplicationInfo> no;
	public ArrayList<ApplicationInfo> nList;
	private Context mContext;
	private PackageManager nPManager;
	private int FLAG_UPDATED_SYS_APP = 0x80;
	
	
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
}
