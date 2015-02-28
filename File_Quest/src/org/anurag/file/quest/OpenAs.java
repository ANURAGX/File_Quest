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

import java.io.File;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OpenAs {
	
	PackageManager manager;
	Context mContext; 
	Dialog dialog;
	String list[];
	List<ResolveInfo> rList;
	int p = -1;
	Intent intent;
	boolean appSelected = false;
	public OpenAs(final Context con,final int width,final Uri uri) {
		// TODO Auto-generated constructor stub
		final Dialog dialog;
		mContext = con.getApplicationContext();
		manager = mContext.getPackageManager();
		dialog = new Dialog(con, Constants.DIALOG_STYLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.launch_file);
		dialog.getWindow().getAttributes().width = width;
		String lit[] = {mContext.getString(R.string.textfile),
				mContext.getString(R.string.musicfile),
				mContext.getString(R.string.videofile),
				mContext.getString(R.string.docfile),
				mContext.getString(R.string.pdffile),
				mContext.getString(R.string.arcfile)};
		list = lit;
		
		ImageView im = (ImageView)dialog.findViewById(R.id.launchImage);
		im.setBackgroundResource(R.drawable.task);
		
		Button q = (Button)dialog.findViewById(R.id.justOnce);
		Button s = (Button) dialog.findViewById(R.id.always);
		TextView tv = (TextView)dialog.findViewById(R.id.open);
		tv.setText(mContext.getString(R.string.openas));
		
		final ListView lv = (ListView)dialog.findViewById(R.id.launch_list);
		q.setText(mContext.getString(R.string.quit));
		s.setText(mContext.getString(R.string.use));
		lv.setAdapter(new Adapter(con, R.layout.row_list_2, list));
		lv.setSelector(R.drawable.action_item_btn2);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				p = position;
			}
		});
		dialog.show();
		q.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		s.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(p==-1)
					Toast.makeText(con, con.getString(R.string.makeaselection), Toast.LENGTH_SHORT).show();
				else{
					File file = new File(uri.toString());
					if(!appSelected){							
						rList = setIntent(file, p);	
						lv.setAdapter(new OpenItems(con, R.layout.row_list_2, rList));
						appSelected = true;
						p = -1;
					}
					else{
						ResolveInfo info = rList.get(p);
						intent = new Intent(Intent.ACTION_MAIN);
						intent.setAction(Intent.ACTION_VIEW);
						//String NAME = info.activityInfo.packageName;
						//String CLASS_NAME = info.activityInfo.name;
						intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
						//intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
						intent.setData(Uri.fromFile(file));
						con.startActivity(intent);
						dialog.dismiss();
					}
				}
			}
		});
		
		
	}

	/**
	 * This function sets the intent for appropriate file type
	 * @param file
	 */
	public List<ResolveInfo> setIntent(File f ,int it){
		intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		switch(it){
			case 0:
				intent.setDataAndType(Uri.fromFile(f) , "text/plain");	
				break;
			case 1:
				intent.setDataAndType(Uri.fromFile(f), "audio/*");
				break;
			case 2:
				intent.setDataAndType(Uri.fromFile(f), "video/*");
				break;
			case 3:
				intent.setDataAndType(Uri.fromFile(f) , "text/plain");	
				break;
			case 4:
				intent.setDataAndType(Uri.fromFile(f) , "application/pdf");
				break;
			case 5:
				intent.setDataAndType(Uri.fromFile(f),"application/zip");
				break;
			default:
				intent.setDataAndType(Uri.fromFile(f),"*/*");
		}
		rList = manager.queryIntentActivities(intent, 0);
		return rList;
	}
	
	
	
	
	private class ItemHolder{
		ImageView Icon;
		TextView Name;
	}
	
	public class Adapter extends ArrayAdapter<String>{
		public Adapter(Context context, int textViewResourceId,	String[] objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			list = objects;
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ItemHolder item = new ItemHolder();
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				item.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				item.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(item);
			}else
				item = (ItemHolder)convertView.getTag();
				
			if(position == 0){
					item.Icon.setBackgroundResource(R.drawable.docs_icon_hd);
					
			}
			else if(position == 1){
				item.Icon.setBackgroundResource(R.drawable.music_icon_hd);
				
			}else if(position == 2){
				item.Icon.setBackgroundResource(R.drawable.video_icon_hd);
				
			}else if(position == 3){
				item.Icon.setBackgroundResource(R.drawable.docs_icon_hd);
				
			}else if(position == 5)
				item.Icon.setBackgroundResource(R.drawable.archive_icon_hd);
			else if(position==4)
				item.Icon.setBackgroundResource(R.drawable.pdf_icon_hd);
			item.Name.setText(list[position]);
			
			return convertView;
		}
	}
	
	
	
	
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	private class Item{
		ImageView Icon;
		TextView Name;
	}
		
	/**
	 * 
	 * @author Anurag
	 *
	 */
	public class OpenItems extends ArrayAdapter<ResolveInfo>{
		List<ResolveInfo> mList;
		public OpenItems(Context context, int textViewResourceId,List<ResolveInfo> objects) {
			super(context, textViewResourceId , objects);
			mList = objects;
			//mContext = context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResolveInfo info = mList.get(position);
			Item holder;
			if(convertView == null){
				holder = new Item();
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				holder.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				holder.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(holder);
			}else
				holder = (Item)convertView.getTag();
				holder.Name.setText(info.loadLabel(manager));
				holder.Icon.setImageDrawable(info.loadIcon(manager));
			return convertView;
		}
	}
	
}
