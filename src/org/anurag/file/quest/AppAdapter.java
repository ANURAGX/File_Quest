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
 *                             anurag.dev1512@gmail.com
 *
 */


package org.anurag.file.quest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends ArrayAdapter<ApplicationInfo>{
	private static LayoutInflater inflater;
	public static PackageManager nPManager;
	private static ArrayList<ApplicationInfo> nList;
	public static ApplicationInfo info;
	private static Context mContext;
	private static FileHolder nHolder;
	public boolean[] thumbSelection;
	public long C;
	public boolean MULTI_SELECT; 
	static Message msg;
	public ArrayList<ApplicationInfo> MULTI_APPS;
	public AppAdapter(Context context, int textViewResourceId , ArrayList<ApplicationInfo> nInfo) {
		super(context,textViewResourceId,nInfo);
		nList = nInfo;
		C = 0;
		msg = new Message();
		MULTI_APPS = new ArrayList<ApplicationInfo>();
		MULTI_SELECT = false;
		thumbSelection = new boolean[nList.size()];
		mContext = context;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nPManager = context.getPackageManager();
	}
	
	
	private static class FileHolder{
		ImageView FileIcon;
		TextView FileName;
		TextView FileType;
		TextView FileSize;
		CheckBox box;
	}
	@Override
	public View getView( final int position , View convertView, ViewGroup container){
		info = nList.get(position);
		if( convertView == null){
			convertView = inflater.inflate(R.layout.row_list_app, container , false);
			nHolder = new FileHolder();
			nHolder.FileIcon = (ImageView)convertView.findViewById(R.id.fileIcon);
			nHolder.FileName = (TextView)convertView.findViewById(R.id.fileName);
			nHolder.FileSize = (TextView)convertView.findViewById(R.id.fileSize);
			nHolder.FileType = (TextView)convertView.findViewById(R.id.fileType);
			nHolder.box = (CheckBox)convertView.findViewById(R.id.checkbox);
			convertView.setTag(nHolder); 
		}else
			nHolder = (FileHolder)convertView.getTag(); 
		
		MULTI_APPS.add(null);
		if(MULTI_SELECT){
			nHolder.box.setVisibility(View.VISIBLE);
			nHolder.box.setId(position);
			nHolder.box.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox ch = (CheckBox) v;
					int id = ch.getId();
					if(thumbSelection[id]){
						ch.setChecked(false);
						thumbSelection[id] = false;
						MULTI_APPS.remove(id);
						MULTI_APPS.add(id,null);
						C--;
					}else{
						C++;
						MULTI_APPS.remove(id);
						MULTI_APPS.add(id,nList.get(id));
						ch.setChecked(true);
						thumbSelection[id] = true;
					}
				}
			});
			nHolder.box.setChecked(thumbSelection[position]);
		}else
			nHolder.box.setVisibility(View.GONE);
		new AppLoader(nHolder.FileIcon , nHolder.FileName , nHolder.FileSize , nHolder.FileType).execute(info);
		return convertView;
	}
		
	public class AppLoader extends AsyncTask<ApplicationInfo, Void, Void>{
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
			if(date == 0){
				iTv3.setText(mContext.getString(R.string.nobackup));
				iTv.setTextColor(Color.WHITE);
			}else{
				iTv3.setText(mContext.getString(R.string.backupon) + " " + new Date(date));
				iTv.setTextColor(Color.GREEN);
			}
			super.onPostExecute(result);
		}
		@Override
		protected Void doInBackground(ApplicationInfo... arg0) {
			draw = nPManager.getApplicationIcon(arg0[0]);
			name = nPManager.getApplicationLabel(arg0[0]).toString();
			size = size(new File(arg0[0].sourceDir));
			date = backupExists(arg0[0]);
			
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
