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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Anurag....
 *
 */
public class SliderExpBaseAdapter extends BaseExpandableListAdapter{
	
	private Context mContext;
	private String[] group;
	private LayoutInflater inflater;
	private int groupIcon[] = {
			R.drawable.analysis,
			R.drawable.file_gallery,
			R.drawable.ic_launcher_drop_box,
			R.drawable.server,
			R.drawable.locker
		};
	private String[] group2;	
	private ArrayList<String> grp2;
	public SliderExpBaseAdapter(Context ctx) {
		// TODO Auto-generated constructor stub
		this.mContext = ctx;
		grp2 = new ArrayList<String>();
		group = ctx.getResources().getStringArray(R.array.slider_group_list);
		group2 = ctx.getResources().getStringArray(R.array.slideList);
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	class childHolder{
		ExpandableListView ls;
	}
	
	@Override
	public View getChildView(int groupPos, int childPos, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		if(groupPos == 1){
			childHolder hld = new childHolder();
			if(arg3 == null){
				arg3 = inflater.inflate(R.layout.child_exp_list_view, arg4 , false);
				hld.ls = (ExpandableListView)arg3.findViewById(R.id.slider_child_ls_view);
				arg3.setTag(hld);
			}else
				hld = (childHolder) arg3.getTag();
			FGChildAdapter adp = new FGChildAdapter(mContext, grp2);
			hld.ls.setAdapter(adp);
			grp2.add(group2[0]);
			adp.notifyDataSetChanged();
			grp2.add(group2[1]);
			adp.notifyDataSetChanged();
			
			
			hld.ls.setSelector(R.drawable.button_click);
			return arg3;
		}
		else
			return null;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(groupPosition == 1)
			return 8;
		else  
			return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.length;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	
	class Holder{
		ImageView icon;
		TextView name;
	}
	
	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		Holder holder = new Holder();
		if(arg2 == null){
			arg2 = inflater.inflate(R.layout.slider_exp_row_list , arg3 , false);
			holder.icon = (ImageView) arg2.findViewById(R.id.iconImage2);
			holder.name = (TextView)arg2.findViewById(R.id.name2);
			arg2.setTag(holder);
		}else
			holder = (Holder) arg2.getTag();
		
		holder.name.setText(group[arg0]);
		holder.icon.setImageDrawable(mContext.getResources().getDrawable(groupIcon[arg0]));
		return arg2;
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
