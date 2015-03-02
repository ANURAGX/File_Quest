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

package org.anurag.dialogs;

import java.io.File;
import java.util.List;

import org.anurag.file.quest.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * 
 * This class prompts user to open a file to open as per his
 * choice of app type.
 * 
 * @author anurag
 *
 */
public class OpenAs {
	
	private PackageManager manager;
	private Context mContext; 
	private String list[];
	private List<ResolveInfo> rList;
	private int p = -1;
	private Intent intent;
	private ListView lv;
	private boolean appSelected;
	
	/**
	 * 
	 * @param con
	 * @param uri of the file to be opened
	 */
	public OpenAs(final Context con,final Uri uri) {
		// TODO Auto-generated constructor stub
		LayoutInflater inf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inf.inflate(R.layout.list_view_hd, null , false);
		view.setPadding(20, 0, 20, 0);
		mContext = con;
		
		String lit[] = {mContext.getString(R.string.textfile),
				mContext.getString(R.string.musicfile),
				mContext.getString(R.string.videofile),
				mContext.getString(R.string.docfile),
				mContext.getString(R.string.pdffile),
				mContext.getString(R.string.arcfile)};
		list = lit;
		appSelected = false;
		new MaterialDialog.Builder(con)
		.title(R.string.openas)
		.customView(view, false)
		.positiveText(R.string.open)
		.negativeText(R.string.dismiss)
		.autoDismiss(false)
		.callback(new ButtonCallback() {
			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				if(p==-1)
					Toast.makeText(con, con.getString(R.string.makeaselection), Toast.LENGTH_SHORT).show();
				else{
					File file = new File(uri.toString());
					if(!appSelected){							
						rList = setIntent(file, p);	
						lv.setAdapter(new OpenItems(con, R.layout.row_list_2, rList));
						appSelected = true;
					}
					else{
						ResolveInfo info = rList.get(p);
						intent = new Intent(Intent.ACTION_MAIN);
						intent.setAction(Intent.ACTION_VIEW);
						intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
						intent.setData(Uri.fromFile(file));
						con.startActivity(intent);
						dialog.dismiss();
					}
				}
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				dialog.dismiss();
			}			
		})
		.show();
		
		lv = (ListView)view.findViewById(R.id.list_view_hd);		
		lv.setAdapter(new Adapter(con, R.layout.row_list_2, list));
		lv.setSelector(R.color.white_grey);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				p = position;
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
		manager = mContext.getPackageManager();
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
	
	private class Adapter extends ArrayAdapter<String>{
		
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
			item.Name.setTextColor(Color.BLACK);
			return convertView;
		}
	}
	private class Item{
		ImageView Icon;
		TextView Name;
	}
		
	/**
	 * 
	 * @author Anurag
	 *
	 */
	private class OpenItems extends ArrayAdapter<ResolveInfo>{
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
			holder.Name.setTextColor(Color.BLACK); 
			return convertView;
		}
	}
	
}
