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


package org.anurag.settings;

import java.util.HashMap;
import java.util.List;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuest;
import org.anurag.file.quest.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsFolderOptAdapter extends BaseExpandableListAdapter {

	private Context ctx;
	private List<String> mainList; // header titles
	
	// child data in format of header title, child title
	private HashMap<String, List<String>> childList;

	public SettingsFolderOptAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this.ctx = context;
		this.mainList = listDataHeader;
		this.childList = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.childList.get(this.mainList.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		Group grp = new Group();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.row_list_expandable_child, null);
			grp.name = (TextView)convertView.findViewById(R.id.directoryName2);
			grp.img = (ImageView)convertView.findViewById(R.id.iconImage2);
			convertView.setTag(grp);
		}else
			grp = (Group) convertView.getTag();
		
		grp.name.setText(childText);
		if(groupPosition == 0)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_startup));
		else if(groupPosition == 1)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_lock64));
		else if(groupPosition == 2){
			if(childText.equalsIgnoreCase("sorting"))
				grp.img.setImageDrawable(ctx.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]));
			else{
				if(FileQuest.SHOW_HIDDEN_FOLDERS)
					grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_multi_select));
				else
					grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_disabled));
				Settings.applied = grp.img;
			}
		}	
		else if(groupPosition == 3)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_droid_home));
		else if(groupPosition == 4)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_gesture));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.childList.get(this.mainList.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.mainList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.mainList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	class Group{
		TextView name;
		ImageView img;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		Group grp = new Group();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.row_list_expandable, null);
			grp.name = (TextView)convertView.findViewById(R.id.directoryName2);
			grp.img = (ImageView)convertView.findViewById(R.id.iconImage2);
			convertView.setTag(grp);  
		}else
			grp = (Group) convertView.getTag();
		grp.name.setText(headerTitle);
		if(groupPosition == 0)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_startup));
		else if(groupPosition == 1)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_lock64));
		else if(groupPosition == 2)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]));
		else if(groupPosition == 3)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_droid_home));
		else if(groupPosition == 4)
			grp.img.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher_gesture));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
