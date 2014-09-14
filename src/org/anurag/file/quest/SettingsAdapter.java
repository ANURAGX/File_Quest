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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsAdapter extends BaseExpandableListAdapter{
	
	Context ctx;
	ArrayList<String> parentList;
	LayoutInflater inflater;
	int mode;
	
	public SettingsAdapter(Context context,ArrayList<String> parent , int MODE) {
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.mode = MODE;
		this.parentList = parent;
		this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return parentList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return parentList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	class group{
		ImageView img;
		TextView text;
	}
	
	@Override
	public View getGroupView(int pos, boolean arg1, View convert, ViewGroup arg3) {
		// TODO Auto-generated method stub
		group grp = new group();
		if(convert == null){
			convert = inflater.inflate(R.layout.row_list_expandable, arg3 , false);
			grp.img = (ImageView)convert.findViewById(R.id.iconImage2);
			grp.text = (TextView)convert.findViewById(R.id.directoryName2);
			convert.setTag(grp);
		}else
			grp = (group)convert.getTag();
		
		if(mode == 0){
			if(pos == 0)
				grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_full));
			else
				grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_appreance));
		}else{
			if(pos == 0)
				grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_startup));
			else if(pos == 1)
				grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_lock64));
			else if(pos == 2)
				grp.img.setImageDrawable(ctx.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]));
			else if(pos == 3)
				grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_droid_home));
			else
				grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_gesture));
		}
		
		grp.text.setText(parentList.get(pos));
		
		return convert;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
