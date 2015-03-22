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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;


/**
 * 
 * @author anurag
 *
 */
public class BluetoothChooser{
	
	private Context mContext;
	private PackageManager pack;
	private String CLASS;
	private String CLASS_NAME;
	private boolean seleted = false;
	private Intent i;
	private File f;
	private String u;
	
	
	public BluetoothChooser(Context context, String Data, String url) {
		// TODO Auto-generated constructor stub
		mContext = context;
		
		if(Data!=null)
			f = new File(Data);
		if(url!=null)
			u = url;
		pack = mContext.getPackageManager();
		
		LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inf.inflate(R.layout.list_view_hd, null , false);
		
		view.setPadding(20, 0, 20, 0);
		
		new MaterialDialog.Builder(mContext)
		.title(R.string.action_share)
		.customView(view , false)
		.negativeText(R.string.dismiss)
		.positiveText(android.R.string.ok)
		.autoDismiss(false)
		.callback(new ButtonCallback() {

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				if(seleted){
					Intent it = new Intent(Intent.ACTION_SEND);
					it.setComponent(new ComponentName(CLASS, CLASS_NAME));
					if(f!=null)
						it.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
					else
						it.putExtra(Intent.EXTRA_STREAM, (u));
						
					it.setType("*/");
					mContext.startActivity(it);
					dialog.dismiss();
					//Toast.makeText(getBaseContext(), CLASS_NAME, Toast.LENGTH_LONG).show();
					//finish();
				}
				else
					Toast.makeText(mContext, R.string.selectFirst,Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				dialog.dismiss();
			}
			
		})
		.show();
		
		ListView ls = (ListView) view.findViewById(R.id.list_view_hd);


		i = new Intent(android.content.Intent.ACTION_SEND);
		if(f!=null)
			i.setDataAndType(Uri.fromFile(f),"*/*");		
		else 
			i.setType("*/*");	
		
		ls.setSelector(R.color.white_grey);
		final List<ResolveInfo> list  = pack.queryIntentActivities(i, 0);
		ls.setAdapter(new OpenItems(mContext, list));
		
		ls.setOnItemClickListener(new OnItemClickListener() {
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
	class OpenItems extends BaseAdapter{
		List<ResolveInfo> mList;
		public OpenItems(Context context,List<ResolveInfo> objects) {
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
