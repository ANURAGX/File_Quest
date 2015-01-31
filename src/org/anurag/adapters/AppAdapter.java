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

package org.anurag.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import org.anurag.fragments.AppStore;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter{
	
	private static HashMap<String, Drawable> ls;
	private static HashMap<String, String> names;
	private static HashMap<String, String> sizes;
	private static HashMap<String, String> backups;
	
	private LayoutInflater inflater;
	private static PackageManager nPManager;
	private ArrayList<ApplicationInfo> nList;
	private ApplicationInfo info;
	private static Context mContext;
	private FileHolder nHolder;
	
	
	public AppAdapter(Context context, ArrayList<ApplicationInfo> nInfo) {
		nList = nInfo;
		mContext = context;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nPManager = context.getPackageManager();
		ls = new HashMap<String , Drawable>();
		names = new HashMap<String , String>();
		sizes = new HashMap<String , String>();
		backups = new HashMap<String , String>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nList.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return nList.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private static class FileHolder{
		ImageView FileIcon;
		TextView FileName;
		TextView FileType;
		TextView FileSize;
		//CheckBox box;
	}
	@Override
	public View getView( final int position , View convertView2, ViewGroup container){
		info = nList.get(position);
		View convertView = null;
		convertView = inflater.inflate(R.layout.row_list_app, container , false);
		nHolder = new FileHolder();
		nHolder.FileIcon = (ImageView)convertView.findViewById(R.id.fileIcon);
		nHolder.FileName = (TextView)convertView.findViewById(R.id.fileName);
		nHolder.FileSize = (TextView)convertView.findViewById(R.id.fileSize);
		nHolder.FileType = (TextView)convertView.findViewById(R.id.fileType);
		convertView.setTag(nHolder); 
				
		Drawable img = ls.get(nPManager.getApplicationLabel(info));
		if(img == null){
			new AppLoader(nHolder.FileIcon , nHolder.FileName , nHolder.FileSize , nHolder.FileType).execute(info);
		}else{
			String nm = nPManager.getApplicationLabel(info).toString();
			nHolder.FileIcon.setImageDrawable(img);
			nHolder.FileName.setText(names.get(nm));
			nHolder.FileSize.setText(sizes.get(nm));
			nHolder.FileType.setText(backups.get(nm));
		}
		
		//true when multi select is on....
		if(Constants.LONG_CLICK[3]){
			if(AppStore.ITEMS[position] == 1)
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white_grey));				}
		return convertView;
	}
		
	/**
	 * class to load the app image,name,backup date,etc....
	 * @author anurag
	 *
	 */
	public static class AppLoader extends AsyncTask<ApplicationInfo, Void, Void>{
		Drawable draw;
		String name;
		long date;
		final ImageView iView;
		final TextView iTv;
		final TextView iTv2;
		final TextView iTv3;
		String size;
		public AppLoader(ImageView view , TextView tv , TextView tv2 , TextView tv3) {
			iView = view;
			iTv = tv;
			iTv2 = tv2;
			iTv3 = tv3;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {
			iView.setImageDrawable(draw);
			iTv.setText(name);
			iTv2.setText(size);
			iTv3.setText(backups.get(name));
			super.onPostExecute(result);
		}
		@Override
		protected Void doInBackground(ApplicationInfo... arg0) {
			name = nPManager.getApplicationLabel(arg0[0]).toString();
			draw = ls.get(arg0[0].packageName);
			if(draw == null){
				draw = nPManager.getApplicationIcon(arg0[0]);
				ls.put(name, draw);
			}	
			size = size(new File(arg0[0].sourceDir));
			date = backupExists(arg0[0]);
			names.put(name, name);
			sizes.put(name, size);
			if(date == 0)
				backups.put(name, mContext.getString(R.string.nobackup));
			else
				backups.put(name, mContext.getString(R.string.backupon) + " " + new Date(date));
			return null;
		}		
	}	
	
	public static long backupExists(ApplicationInfo in){
		PackageInfo i = null;
		String PATH = Environment.getExternalStorageDirectory().getPath();
		try {
			i = nPManager.getPackageInfo(in.packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	public static String size(File f){
		long size = f.length();
		if(size>Constants.GB)
			return String.format(mContext.getString(R.string.sizegb), (double)size/(Constants.GB));
		else if(size > Constants.MB)
			return String.format(mContext.getString(R.string.sizemb), (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(mContext.getString(R.string.sizekb), (double)size/(1024));		
		else
			return String.format(mContext.getString(R.string.sizebytes), (double)size);
	}
	
}
