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


import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterLoaders {

	static String list[];
	static Context mContext;
	boolean status; 
	public AdapterLoaders(Context context,boolean st){
		mContext = context;
		status =st;
	}	
	
	public static Adapter getCloudAdapter(Context con){
		return new Adapter(con, R.layout.row_list_2, list);
	}
	/**
	 * Adapter for CLOUD SERVICES LIST....
	 * @author anurag
	 *
	 */
	private static class ItemHolder{
		ImageView Icon;
		TextView Name;
	}
	
	public static class Adapter extends ArrayAdapter<String>{
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
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				item.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				item.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(item);
			}else
				item = (ItemHolder)convertView.getTag();
				
			if(position == 0){
					item.Icon.setBackgroundResource(R.drawable.ic_launcher_drop_box);
					item.Name.setText(mContext.getString(R.string.dropbox));
			}
			else if(position == 1){
				item.Icon.setBackgroundResource(R.drawable.ic_launcher_google_drive);
				item.Name.setText(mContext.getString(R.string.googledrive));
			}else if(position == 2){
				item.Icon.setBackgroundResource(R.drawable.ic_launcher_sky_drive);
				item.Name.setText(mContext.getString(R.string.skydrive));
			}	
			return convertView;
		}
	}
	
	
	
	
	
	
	
	
	public LongClickAdapter getLongClickAdapter(){
		ArrayList<String> l = new ArrayList<String>();
		l.add("");l.add("");
		l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");
		ArrayList<String> list = new ArrayList<String>();
		list.add("");list.add("");list.add("");list.add("");list.add("");list.add("");list.add("");
		if(status)
			return new LongClickAdapter(mContext, R.layout.row_list_2, list);
		else
			return new LongClickAdapter(mContext, R.layout.row_list_2,l);
	}
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	private static class LongClickHolder{
		ImageView Icon;
		TextView Name;
	}
	/**
	 * 
	 * @author anurag
	 *
	 */
	private class LongClickAdapter extends ArrayAdapter<String>{
		public LongClickAdapter(Context context, int textViewResourceId ,ArrayList<String> objects) {
			super(context, textViewResourceId, objects);
			
    	}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LongClickHolder ho; 
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				ho = new LongClickHolder();
				ho.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				ho.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(ho);
			}else
				ho = (LongClickHolder)convertView.getTag();
			
			if(status){
				if(position == 0){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_open);
					ho.Name.setText(mContext.getString(R.string.launch));
				}else if(position == 1){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_uninstall);
					ho.Name.setText(mContext.getString(R.string.uninstall));
				}else if(position == 2){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_backupall);
					ho.Name.setText(mContext.getString(R.string.takebackup));
				}else if(position == 3){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_delete);
					ho.Name.setText(mContext.getString(R.string.deleteearlierbackup));
				}else if(position == 4){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_zip_it);
					ho.Name.setText(mContext.getString(R.string.createbackupzip));
				}else if(position == 5){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_share);
					ho.Name.setText(mContext.getString(R.string.shareapp));
				}else if(position == 6){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_stats);
					ho.Name.setText(mContext.getString(R.string.appproperties));
				}
			}
			else{
				if(position == 0){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_open);
					ho.Name.setText(mContext.getString(R.string.open));
				}else if(position == 2){
					if(FileQuest.ZIP_ROOT || FileQuest.ZIP_SIMPLE ||FileQuest.RAR_ROOT||FileQuest.RAR_SIMPLE
							||FileQuest.TAR_ROOT||FileQuest.TAR_SIMPLE){
						ho.Name.setText(mContext.getString(R.string.extractto));
					}else
						ho.Name.setText(mContext.getString(R.string.copy));
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_copy);
				}else if(position == 3){
					if(FileQuest.ZIP_ROOT || FileQuest.ZIP_SIMPLE ||FileQuest.RAR_ROOT||FileQuest.RAR_SIMPLE
							||FileQuest.TAR_ROOT||FileQuest.TAR_SIMPLE){
						ho.Icon.setBackgroundResource(R.drawable.ic_launcher_copy);
						ho.Name.setText(mContext.getString(R.string.extracthere));
					}else{
						ho.Icon.setBackgroundResource(R.drawable.ic_launcher_cut);
						ho.Name.setText(mContext.getString(R.string.cut));
					}					
				}else if(position == 5){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_zip_it);
					ho.Name.setText(mContext.getString(R.string.archive));
				}else if(position == 6){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_delete);
					ho.Name.setText(mContext.getString(R.string.delete));
				}else if(position == 7){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_rename);
					ho.Name.setText(mContext.getString(R.string.rename));
				}else if(position == 8){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_share);
					ho.Name.setText(mContext.getString(R.string.send));
				}else if(position == 10){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_stats);
					ho.Name.setText(mContext.getString(R.string.properties));
				}else if(position == 4){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_paste);
					ho.Name.setText(mContext.getString(R.string.paste));
				}else if(position == 1){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_drop_box);
					ho.Name.setText(mContext.getString(R.string.copytocloud));
				}else if(position == 9){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_gesture);
					ho.Name.setText(mContext.getString(R.string.addgesture));
				}
			}
			return convertView;
		}
	}
	
}
