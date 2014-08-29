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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class SDAdapter extends BaseAdapter{
	
	public boolean MULTI_SELECT;
	public boolean[] thumbselection;
	public long C;
	Item item;
	Context ctx;
	ArrayList<Item> list; 
	LayoutInflater inflater;
	ArrayList<Item> MULTI_FILES;
	public SDAdapter(Context context,ArrayList<Item> object) {
		// TODO Auto-generated constructor stub
		ctx = context;
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
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		item = list.get(pos);
		Holder h = new Holder();
		if(convertView == null){
			h = new Holder();
			convertView = inflater.inflate(R.layout.row_list_1, arg2 , false);
			h.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
			h.fName = (TextView)convertView.findViewById(R.id.fileName);
			h.fType = (TextView)convertView.findViewById(R.id.fileType);
			h.fSize = (TextView)convertView.findViewById(R.id.fileSize);
			h.box = (CheckBox)convertView.findViewById(R.id.checkbox);
			convertView.setTag(h);
		}
		else
			h = (Holder)convertView.getTag();
		
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
		h.icon.setImageDrawable(item.getIcon());
		return convertView;
	}

	
}
