/**
 * Copyright(c) 2014 DRAWNZER.ORG PROJECTS -> ANURAG
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

import java.io.File;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * 
 * @author Anurag
 *
 */
public class OpenFileDialog {
	
	private Intent intent;
	private Context mContext;
	private String mData;
	private PackageManager manager;
	private	ResolveInfo info;
	private List<ResolveInfo> list;
	private SharedPreferences prefs;
	private SharedPreferences.Editor edit;
	private boolean showDialog;
	private String NAME;
	private String CLASS_NAME;
	private String MUSIC;
	private String MUSIC_CLASS_NAME;
	private String VIDEO;
	private String VIDEO_CLASS_NAME;
	private String APK ;
	private String APK_CLASS_NAME;
	private String PDF ;
	private String PDF_CLASS_NAME;
	private String IMAGE;
	private String IMAGE_CLASS_NAME;
	private String TEXT ;
	private String TEXT_CLASS_NAME;
	private String ZIP ;
	private String ZIP_CLASS_NAME;
	private String RAR ;
	private String RAR_CLASS_NAME;
	private boolean intentSelected;
	private File file;
	
	public OpenFileDialog(Context context,Uri uri,int width ) {
		// TODO Auto-generated constructor stub
		showDialog = true;
		try{
			mData = uri.toString();
			file = new File(mData);
			mContext = context;
			
			/**
			 * IF ZIP FILE IS SELECTED TO OPEN THE ZIP FILE WITH THIS APP(file quest)....
			 */
			if(file.getName().endsWith(".zip")||file.getName().endsWith(".ZIP")){
				Intent intent = new Intent("FQ_ZIP_OPEN");
				intent.putExtra("open_path", file.getAbsolutePath());
				mContext.sendBroadcast(intent);
				showDialog = false;
			}
			/**
			 * IF A RAR FILE IS SELECTED TO OPEN 
			 */
			else if(file.getName().endsWith(".rar")||file.getName().endsWith(".RAR")){
				Intent intent = new Intent("FQ_RAR_OPEN");
				intent.putExtra("open_path", file.getAbsolutePath());
				mContext.sendBroadcast(intent);
				showDialog = false;
			}
			/**
			 * IF A TAR FILE IS SELECTED TO OPEN...
			 */
			else if(file.getName().endsWith(".tar")||file.getName().endsWith(".TAR")
					||file.getName().endsWith(".tar.gz")||file.getName().endsWith(".TAR.GZ")
					||file.getName().endsWith(".bz2")||file.getName().endsWith(".BZ2")){
				Intent intent = new Intent("FQ_TAR_OPEN");
				intent.putExtra("open_path", file.getAbsolutePath());
				mContext.sendBroadcast(intent);
				showDialog = false;
			}
			
			
			prefs = mContext.getSharedPreferences("DEFAULT_APPS", 0);
			edit = prefs.edit();
			manager = mContext.getPackageManager();
			/**
			 * LOADS THE DEFAULT APPS IF SELECTED
			 */
			MUSIC = prefs.getString("MUSIC", null);
			MUSIC_CLASS_NAME = prefs.getString("MUSIC_CLASS_NAME", null);
			VIDEO = prefs.getString("VIDEO", null);
			VIDEO_CLASS_NAME = prefs.getString("VIDEO_CLASS_NAME", null);
			APK = prefs.getString("APK", null);
			APK_CLASS_NAME = prefs.getString("APK_CLASS_NAME", null);
			PDF = prefs.getString("PDF", null);
			PDF_CLASS_NAME = prefs.getString("PDF_CLASS_NAME", null);
			IMAGE = prefs.getString("IMAGE", null);
			IMAGE_CLASS_NAME = prefs.getString("IMAGE_CLASS_NAME", null);
			TEXT = prefs.getString("TEXT", null);
			TEXT_CLASS_NAME = prefs.getString("TEXT_CLASS_NAME", null);
			ZIP = prefs.getString("ZIP", null);
			ZIP_CLASS_NAME = prefs.getString("ZIP_CLASS_NAME", null);	
			RAR = prefs.getString("RAR", null);
			RAR_CLASS_NAME = prefs.getString("RAR_CLASS_NAME", null);	
			if(showDialog)
				init();
		}catch(RuntimeException e){
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * FUNCTION INITIALIZES THE INTENT TO A PARTICULAR FILE TYPE AND MAKING
	 * IT READY TO BE QUERIED,TELLS THAT WHETHER AN APP IS AVAILABLE TO HANDLW IT OR NOT
	 */
	void init(){
		try{
			setIntent(file);
			if(intent != null){
				list = manager.queryIntentActivities(intent , 0);
				if(list.size() == 0){
					/**
					 * NO APPS AVAILABLE TO HANDLE THIS KIND OF FILE TYPE
					 * FINISH THIS CLASS AND SHOW THE MESSAGE THAT NO APP
					 * IS AVAILABLE
					 */
					//Toast.makeText(mContext, R.string.noApp, Toast.LENGTH_SHORT).show();
					//new OpenAs(mContext, wi,Uri.parse(file.getAbsolutePath()));
				}
			}
			else{
				/**
				 * NO APPS AVAILABLE TO HANDLE THIS KIND OF FILE TYPE
				 * FINISH THIS CLASS AND SHOW THE MESSAGE THAT NO APP
				 * IS AVAILABLE
				 */
				//Toast.makeText(mContext, R.string.noApp, Toast.LENGTH_SHORT).show();
				//new OpenAs(mContext, wi,Uri.fromFile(file));
			}
		}catch(RuntimeException e){
			
		}
		
		/**
		 * IF DEFAULT APP IS SELECTED THEN LAUNCHES THE FILE WITH THAT APP
		 */
		try{
			intent = null;
			setIntentType(file);
			intent.setData(Uri.fromFile(file));
			mContext.startActivity(intent);
			
		}catch(RuntimeException e){
			
			/**
			 * THIS DIALOG IS SHOWN IN RUNTIME EXCEPTION, IF THERE IS NO DEFAULT SELECTED APP
			 * THEN IT WILL CAUSE EXCEPTION THEN LIST OF APPS WILL BE OFFERED TO USER TO SELECT
			 */
			
			if(list.size()>0){
				LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inf.inflate(R.layout.list_view_hd, null , false);
				
				new MaterialDialog.Builder(mContext)
				.title(R.string.open)
				.positiveText(R.string.once)
				.negativeText(R.string.always)
				.callback(new ButtonCallback() {
					@Override
					public void onPositive(MaterialDialog dialog) {
						// TODO Auto-generated method stub
						super.onPositive(dialog);
						if(intentSelected){
							try{
								mContext.startActivity(intent);
								dialog.dismiss();
								//OpenFiles.this.finish();
							}catch(ActivityNotFoundException e){
								Toast.makeText(mContext, R.string.xerror, Toast.LENGTH_SHORT).show();
							}catch(SecurityException e){
								Toast.makeText(mContext, R.string.xerror, Toast.LENGTH_SHORT).show();
							}
						}
						else
							Toast.makeText(mContext, R.string.selectFirst, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						// TODO Auto-generated method stub
						super.onNegative(dialog);
						
						if(intentSelected){
							try{
								if(getFileType(file)=="image"){
									edit.putString("IMAGE", NAME);
									edit.putString("IMAGE_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}
								else if(getFileType(file)=="music"){
									edit.putString("MUSIC", NAME);
									edit.putString("MUSIC_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}else if(getFileType(file)=="apk"){
									edit.putString("APK", NAME);
									edit.putString("APK_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}else if(getFileType(file)=="video"){
									edit.putString("VIDEO", NAME);
									edit.putString("VIDEO_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}else if(getFileType(file)=="zip"){
									edit.putString("ZIP", NAME);
									edit.putString("ZIP_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}else if(getFileType(file)=="rar"){
									edit.putString("RAR", NAME);
									edit.putString("RAR_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}else if(getFileType(file)=="text"){
									edit.putString("TEXT", NAME);
									edit.putString("TEXT_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}else if(getFileType(file)=="pdf"){
									edit.putString("PDF", NAME);
									edit.putString("PDF_CLASS_NAME", CLASS_NAME);
									edit.commit();
								}
								mContext.startActivity(intent);
								dialog.dismiss();
							}catch(ActivityNotFoundException e){
								Toast.makeText(mContext, R.string.xerror, Toast.LENGTH_SHORT).show();
							}
						}
						else
							Toast.makeText(mContext, R.string.makeaselection, Toast.LENGTH_SHORT).show();
						
					}					
				})
				.customView(view, true)
				.show();
				
				ListView ls = (ListView) view.findViewById(R.id.list_view_hd);
				ls.setAdapter(new OpenItems(mContext, list));
			}
		}	
	}	
	
	
	
	public String getFileType(File f){
		String na = f.getName();
		if( na.endsWith("jpg")||na.endsWith(".JPG")||  na.endsWith(".png") || na.endsWith(".PNG") || na.endsWith(".gif") || na.endsWith(".GIF")
				|| na.endsWith(".JPEG") || na.endsWith(".jpeg") || na.endsWith(".bmp") || na.endsWith(".BMP"))
			return "image";
		
		if(na.endsWith(".zip") || na.endsWith(".ZIP"))
			return "zip";
		else if(na.endsWith(".rar") || na.endsWith("RAR"))
			return "rar";
		else if(na.endsWith(".apk") || na.endsWith(".APK"))
			return "apk";
		else if(na.endsWith(".pdf") || na.endsWith(".PDF"))
			return "pdf";
		else if(na.endsWith(".mp3") || na.endsWith(".MP3") || na.endsWith(".amr") || na.endsWith(".AMR")
				|| na.endsWith(".ogg") || na.endsWith(".OGG")||na.endsWith(".m4a")||na.endsWith(".M4A"))
			return "music";
		else if(na.endsWith(".doc") || na.endsWith(".DOC")
				|| na.endsWith(".DOCX") || na.endsWith(".docx") || na.endsWith(".ppt") || na.endsWith(".PPT"))
			return "text";
		else if(na.endsWith(".txt") || na.endsWith(".TXT") || na.endsWith(".inf") || na.endsWith(".INF"))
			return "text";
		else if(na.endsWith(".mp4") || na.endsWith(".MP4") || na.endsWith(".avi") || na.endsWith(".AVI")
				|| na.endsWith(".FLV") || na.endsWith(".flv") || na.endsWith(".3GP") || na.endsWith(".3gp"))
			return "video";		
		
		return null;
	}
	
	
	/**
	 * This function sets the intent for appropriate file type
	 * @param file
	 */
	public void setIntent(File f){
		intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String na = f.getName();
		if(na.endsWith(".mp3") ||na.endsWith(".MP3")||na.endsWith(".ogg")||na.endsWith(".OGG")
				||na.endsWith(".m4a")||na.endsWith(".M4A")||na.endsWith(".amr")||na.endsWith(".AMR"))
			intent.setDataAndType(Uri.fromFile(f), "audio/*");
		
		else if(na.endsWith(".WMV")||na.endsWith("wmv")||na.endsWith(".mp4")||na.endsWith("MP4")||na.endsWith(".3gp")||na.endsWith(".flv")||na.endsWith(".FLV"))
			intent.setDataAndType(Uri.fromFile(f), "video/*");
		
		else if(na.endsWith(".jpg")
				||na.endsWith(".JPG")
				||na.endsWith(".jpeg")
				||na.endsWith(".JPEG")
				||na.endsWith(".png")
				||na.endsWith(".PNG")
				||na.endsWith(".gif")
				||na.endsWith(".GIF")
				||na.endsWith(".tiff")
				||na.endsWith(".TIFF"))
			intent.setDataAndType(Uri.fromFile(f), "image/*");
		
		else if(na.endsWith(".apk")
				||na.endsWith(".APK"))
			intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
		else if(na.endsWith(".pdf")||na.endsWith(".PDF"))
			intent.setDataAndType(Uri.fromFile(f) , "application/pdf");
		else if(na.endsWith(".DOC")||na.endsWith(".doc"))
			intent.setDataAndType(Uri.fromFile(f) , "text/plain");
		else if(na.endsWith(".TXT")||na.endsWith(".txt")||na.endsWith(".log")||na.endsWith(".LOG"))
			intent.setDataAndType(Uri.fromFile(f) , "text/plain");	
		
		else {
			intent.setDataAndType(Uri.fromFile(f), "application/unknown");
		}
	}
	

	public void setIntentType(File f){
		String na = f.getName();
		if( na.endsWith("jpg")||na.endsWith(".JPG")||  na.endsWith(".png") || na.endsWith(".PNG") || na.endsWith(".gif") || na.endsWith(".GIF")
				|| na.endsWith(".JPEG") || na.endsWith(".jpeg") || na.endsWith(".bmp") || na.endsWith(".BMP")){
			if(IMAGE != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(IMAGE, IMAGE_CLASS_NAME));
			}
		}	
		else if(na.endsWith(".zip") || na.endsWith(".ZIP")){
			if(ZIP != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(ZIP, ZIP_CLASS_NAME));
			}
		}
			
		else if(na.endsWith(".rar")|| na.endsWith("RAR")){
			if(RAR != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(RAR, RAR_CLASS_NAME));
			}
		}
		else if(na.endsWith(".pdf")|| na.endsWith("PDF")){
			if(PDF != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(PDF, PDF_CLASS_NAME));
			}
		}
		
		else if(na.endsWith(".apk") || na.endsWith(".APK")){
			if(APK != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(APK, APK_CLASS_NAME));
			}
		}			
		else if(na.endsWith(".mp3") || na.endsWith(".MP3") || na.endsWith(".amr") || na.endsWith(".AMR")
				|| na.endsWith(".ogg") || na.endsWith(".OGG")||na.endsWith(".m4a")||na.endsWith(".M4A")){
			if(MUSIC != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(MUSIC, MUSIC_CLASS_NAME));
			}
		}			
		else if(na.endsWith(".doc") || na.endsWith(".DOC")
				|| na.endsWith(".DOCX") || na.endsWith(".docx") || na.endsWith(".ppt") || na.endsWith(".PPT")){
			if(TEXT != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(TEXT, TEXT_CLASS_NAME));
			}
		}		
		else if(na.endsWith(".txt") || na.endsWith(".TXT") || na.endsWith(".inf") || na.endsWith(".INF")|| na.endsWith(".log") || na.endsWith(".LOG")){
			if(TEXT != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(TEXT, TEXT_CLASS_NAME));
			}
		}		
		else if(na.endsWith(".mp4") || na.endsWith(".MP4") || na.endsWith(".avi") || na.endsWith(".AVI")
				|| na.endsWith(".FLV") || na.endsWith(".flv") || na.endsWith(".3GP") || na.endsWith(".3gp")){
			if(VIDEO != null){
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setComponent(new ComponentName(VIDEO, VIDEO_CLASS_NAME));
			}
		}
		else intent = null;		
	}
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	private class ItemHolder{
		ImageView Icon;
		TextView Name;
	}
		
	/**
	 * 
	 * @author Anurag
	 *
	 */
	public class OpenItems extends BaseAdapter{
		List<ResolveInfo> mList;
		public OpenItems(Context context,List<ResolveInfo> objects) {
			mList = objects;
			//mContext = context;
		}
				
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResolveInfo info = mList.get(position);
			ItemHolder holder;
			if(convertView == null){
				holder = new ItemHolder();
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				holder.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				holder.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(holder);
			}else
				holder = (ItemHolder)convertView.getTag();
			
			holder.Name.setText(info.loadLabel(manager));
			holder.Icon.setImageDrawable(info.loadIcon(manager));
			holder.Name.setTextColor(Color.BLACK);
			return convertView;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
	}	
}
