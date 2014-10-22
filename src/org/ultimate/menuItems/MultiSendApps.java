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

package org.ultimate.menuItems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.anurag.file.quest.ErrorDialogs;
import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
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

public class MultiSendApps{
	static Context mContext;
	static PackageManager pack;
	String CLASS;
	ArrayList<ApplicationInfo> list2;
	ArrayList<Uri> list3;
	String CLASS_NAME;
	boolean seleted = false;
	Dialog dialog;
	Point p;
	public MultiSendApps(Context context,Point width,ArrayList<ApplicationInfo> list) {
		// TODO Auto-generated constructor stub
		list2 = list;
		p = width;
		mContext = context;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.launch_file);
		ImageView view = (ImageView)dialog.findViewById(R.id.launchImage);
		view.setBackgroundResource(R.drawable.ic_launcher_file_task);
		dialog.getWindow().getAttributes().width = width.x*8/9;
		onCreate();
	}
	protected void onCreate() {
		// TODO Auto-generated method stub
		//params.width = p.x*5/6;
		
		
		pack = mContext.getPackageManager();
		Intent i = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		i.setDataAndType(Uri.fromFile(new File("/")),"*/*");		
		
		Button q = (Button)dialog.findViewById(R.id.justOnce);
		Button s = (Button) dialog.findViewById(R.id.always);
		TextView tv = (TextView)dialog.findViewById(R.id.open);
		tv.setText(R.string.selectFirst);
		
		final List<ResolveInfo> list  = pack.queryIntentActivities(i, 0);
		if(list.size() == 0){
			/**
			 * NO APPS AVAILABLE TO HANDLE THIS KIND OF FILE TYPE
			 * FINISH THIS CLASS AND SHOW THE MESSAGE THAT NO APP
			 * IS AVAILABLE
			 */
			new ErrorDialogs(mContext, p.x*4/5, "NotFound");
		}
		new PushUris().execute();
		ListView lv = (ListView)dialog.findViewById(R.id.launch_list);
		lv.setSelector(R.drawable.button_click);
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
		s.setText(mContext.getString(R.string.use));
		q.setText(mContext.getString(R.string.quit));
		s.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(seleted){
					Intent it = new Intent(Intent.ACTION_SEND_MULTIPLE);
					it.setAction(Intent.ACTION_SEND_MULTIPLE);
					it.setType("*/*");
					it.setComponent(new ComponentName(CLASS, CLASS_NAME));
					//it.putExtra(Intent.EXTRA_STREAM, list3);
					it.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list3);
					if(list3!=null)
						mContext.startActivity(it);
					else
						Toast.makeText(mContext , R.string.senderror, Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					
				}
				else
					Toast.makeText(mContext , R.string.selectFirst,
							Toast.LENGTH_SHORT).show();
			}
		});
		q.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		if(list3.size()>0)
			dialog.show();
	}
	
	
	
	
	


	public class PushUris extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			list3 = new ArrayList<Uri>();
			int l = list2.size();
			ApplicationInfo in;
			for(int i = 0 ; i<l ; ++i){
				in = list2.get(i);
				if(in!=null)
					list3.add(Uri.fromFile(new File(in.sourceDir)));
			}
			return null;
		}
		
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
