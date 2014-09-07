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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SDAdapter extends BaseAdapter{
	private static ThumbnailCreator creator;
	Bitmap image;
	Holder h;
	public static boolean MULTI_SELECT;
	public static boolean[] thumbselection;
	public static long C;
	Item item;
	Context ctx;
	ArrayList<Item> list; 
	LayoutInflater inflater;
	static ArrayList<Item> MULTI_FILES;
	public SDAdapter(Context context,ArrayList<Item> object) {
		// TODO Auto-generated constructor stub
		ctx = context;
		creator = new ThumbnailCreator(50, 50);
		MULTI_FILES = new ArrayList<Item>();
		list = object;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	class Holder{
		ImageView icon;
		TextView fName;
		TextView fType;
		TextView fSize;
		CheckBox box;
		ImageView lockimg;
		ImageView favimg;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		item = list.get(pos);
		h = new Holder();
		if(convertView == null){
			h = new Holder();
			convertView = inflater.inflate(R.layout.row_list_1, arg2 , false);
			h.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
			h.fName = (TextView)convertView.findViewById(R.id.fileName);
			h.fType = (TextView)convertView.findViewById(R.id.fileType);
			h.fSize = (TextView)convertView.findViewById(R.id.fileSize);
			h.box = (CheckBox)convertView.findViewById(R.id.checkbox);
		
			h.lockimg = (ImageView)convertView.findViewById(R.id.lockimg);
			h.favimg = (ImageView)convertView.findViewById(R.id.favimg);
			convertView.setTag(h);
		}
		else
			h = (Holder)convertView.getTag();
		
		
		if(item.isLocked())
			h.lockimg.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_locked));
		else
			h.lockimg.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_unlocked));
		
		h.lockimg.setId(pos);
		h.lockimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView img = (ImageView)v;
				SharedPreferences prefs = ctx.getSharedPreferences("MY_APP_SETTINGS", 0);
				if(!list.get(img.getId()).isLocked()){
					//checking for master password is set or not
					String passwd = prefs.getString("MASTER_PASSWORD", null);
					if(passwd==null){
						Constants.lock = img;
						new MasterPassword(ctx, FileQuest.size.x*8/9, null,prefs,0);
					}
					else{
						list.get(img.getId()).setLockStatus(true);
						img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_locked));
						Constants.db.insertNodeToLock(list.get(img.getId()).getFile().getAbsolutePath(), 1, 1);
						Toast.makeText(ctx, R.string.itemlocked, Toast.LENGTH_SHORT).show();
					}					
				}else{
					//unlocking file,before that asking the password...
					Constants.lock = img;
					new MasterPassword(ctx, FileQuest.size.x*8/9,  null,prefs,0);
				}
			}
		});
		
		h.favimg.setId(pos);
		h.favimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView img = (ImageView)v;
				img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_favorite));
			}
		});
		
		MULTI_FILES.add(null);
		if(MULTI_SELECT){
			h.box.setVisibility(View.VISIBLE);
			h.box.setId(pos);
			h.box.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					// TODO Auto-generated method stub
					CheckBox ch = (CheckBox) v;
					int id = ch.getId();
					if(thumbselection[id]){
						ch.setChecked(false);
						thumbselection[id] = false;
						MULTI_FILES.remove(id);
						MULTI_FILES.add(id,null);
						C--;	
					}else{
						ch.setChecked(true);
						thumbselection[id] = true;
						MULTI_FILES.remove(id);
						MULTI_FILES.add(id, list.get(id));
						C++;
					}
				}
			});
			h.box.setChecked(thumbselection[pos]);
		}else
			h.box.setVisibility(View.GONE);
		
		h.fName.setText(item.getName());
		h.fType.setText(item.getType());
		h.fSize.setText(item.getSize());
		if(!item.getType().equals("Image"))
			h.icon.setImageDrawable(item.getIcon());
		else{
			h.icon.setImageDrawable(item.getIcon());
			image = creator.isBitmapCached(item.getPath());
			if(image == null){
				final Handler handle = new Handler(new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						// TODO Auto-generated method stub
					    notifyDataSetChanged();
						return true;
					}
				});
				creator.createNewThumbnail(list, handle);
				if(!creator.isAlive())
					creator.start();
					
			}else
				h.icon.setImageBitmap(image);
		}
		return convertView;
	}	
}
