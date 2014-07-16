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
package org.ultimate.menuItems;

import java.io.File;
import java.util.List;
import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BluetoothChooser{
	static Context mContext;
	static PackageManager pack;
	String CLASS;
	String CLASS_NAME;
	boolean seleted = false;
	Dialog dialog;
	Intent i;
	File f;
	String u;
	public BluetoothChooser(Context context,String Data,int width,String url) {
		// TODO Auto-generated constructor stub
		mContext = context;
		
		dialog = new Dialog(context, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.launch_file);
		dialog.getWindow().getAttributes().width = width;
		dialog.setCancelable(true);
		ImageView view = (ImageView)dialog.findViewById(R.id.launchImage);
		view.setBackgroundResource(R.drawable.ic_launcher_file_task);
		if(Data!=null)
			f = new File(Data);
		if(url!=null)
			u = url;
		onCreate();
	}
	
	
	public void onCreate() {
		// TODO Auto-generated method stub
		
		
		//final File f = new File(i.getData().toString());
		pack = mContext.getPackageManager();
		i = new Intent(android.content.Intent.ACTION_SEND);
		if(f!=null)
			i.setDataAndType(Uri.fromFile(f),"*/*");		
		else 
			i.setType("*/*");	
		Button q = (Button)dialog.findViewById(R.id.justOnce);
		Button s = (Button) dialog.findViewById(R.id.always);
		TextView tv = (TextView)dialog.findViewById(R.id.open);
		tv.setText(mContext.getString(R.string.selectapp));
		
		
		final List<ResolveInfo> list  = pack.queryIntentActivities(i, 0);
		if(list.size() == 0){
			/**
			 * NO APPS AVAILABLE TO HANDLE THIS KIND OF FILE TYPE
			 * FINISH THIS CLASS AND SHOW THE MESSAGE THAT NO APP
			 * IS AVAILABLE
			 */
			Toast.makeText(mContext, R.string.noApp, Toast.LENGTH_SHORT).show();
		}
		
		ListView lv = (ListView)dialog.findViewById(R.id.launch_list);
		lv.setSelector(R.drawable.blue_button);
		lv.setAdapter(new OpenItems(mContext, R.layout.row_list_2, list));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ResolveInfo info = list.get(position);
				CLASS = info.activityInfo.packageName;
				CLASS_NAME = info.activityInfo.name;
				seleted = true;
			}
		});
		s.setText(R.string.use);
		q.setText(R.string.quit);
		s.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(seleted){
					Intent it = new Intent(Intent.ACTION_SEND);
					it.setComponent(new ComponentName(CLASS, CLASS_NAME));
					if(f!=null)
						it.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
					else
						it.putExtra(Intent.EXTRA_STREAM, (u));
						
					it.setType("*/*");
					mContext.startActivity(it);
					dialog.dismiss();
					//Toast.makeText(getBaseContext(), CLASS_NAME, Toast.LENGTH_LONG).show();
					//finish();
				}
				else
					Toast.makeText(mContext, R.string.selectFirst,Toast.LENGTH_SHORT).show();
			}
		});
		q.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	private static class ItemHolder{
		ImageView Icon;
		TextView Name;
	}
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	public static class OpenItems extends ArrayAdapter<ResolveInfo>{
		List<ResolveInfo> mList;
		public OpenItems(Context context, int textViewResourceId,List<ResolveInfo> objects) {
			super(context, textViewResourceId , objects);
			mList = objects;
			mContext = context;
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
				holder.Name.setText(info.loadLabel(pack));
				holder.Icon.setImageDrawable(info.loadIcon(pack));
			return convertView;
		}
	}
	
	
}
