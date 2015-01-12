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

package org.anurag.dropbox;

import java.util.ArrayList;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DBoxAdapter extends BaseAdapter{

	Context ctx;
	DBoxObj file;
	LayoutInflater inflater;
	ArrayList<DBoxObj> list;
	public DBoxAdapter(Context ct , ArrayList<DBoxObj> ls) {
		// TODO Auto-generated constructor stub
		ctx = ct;
		list = ls;
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
		file = list.get(pos);
		Holder hold = new Holder();
		if(convertView==null){
			convertView = inflater.inflate(R.layout.row_list_1, arg2,false);
			hold.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
			hold.fName = (TextView)convertView.findViewById(R.id.fileName);
			hold.fType = (TextView)convertView.findViewById(R.id.fileType);
			hold.fSize = (TextView)convertView.findViewById(R.id.fileSize);
			hold.box = (CheckBox)convertView.findViewById(R.id.checkbox);
			hold.box.setVisibility(View.GONE);
		}else
			hold = (Holder) convertView.getTag();
		if(file.isDir())
			hold.icon.setImageDrawable(ctx.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]));
		else
			hold.icon.setImageDrawable(Constants.UNKNOWN);
		hold.fName.setText(file.getName());
		hold.fSize.setText(file.getSize());
		hold.fType.setText(file.getType());
		return convertView;
	}

}
