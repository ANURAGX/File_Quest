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

/**
 * 
 * @author Anurag....
 *
 */
public class FGChildAdapter extends BaseExpandableListAdapter{

	private ArrayList<String> group;
	private LayoutInflater inf;
	private int[] icons = {
			R.drawable.favorite_slider,
			R.drawable.ic_launcher_music,
			R.drawable.ic_launcher_apk,
			R.drawable.ic_launcher_images,
			R.drawable.ic_launcher_video,
			R.drawable.ic_launcher_ppt,
			R.drawable.ic_launcher_zip_it,
			R.drawable.ic_launcher_unknown,
			
		};
	private String[] child = {"Delete" , "Move To one location" , "Zip it"};
	private int[] childIcons ={
			R.drawable.ic_launcher_delete,
			R.drawable.ic_launcher_cut,
			R.drawable.zip_it_longclick
		};
	private Context mContext;
	public FGChildAdapter(Context ctx , ArrayList<String> grp) {
		// TODO Auto-generated constructor stub
		group = grp;
		this.mContext = ctx;
		inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
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

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		Holder hld= new Holder();
		if(arg3 == null){
			arg3 = inf.inflate(R.layout.row_list_2, arg4 , false);
			hld.icon = (ImageView)arg3.findViewById(R.id.iconImage2);
			hld.name = (TextView)arg3.findViewById(R.id.directoryName2);
			arg3.setTag(hld);
		}else
			hld = (Holder) arg3.getTag();
		hld.name.setText(child[arg1]);
		hld.icon.setImageDrawable(mContext.getResources().getDrawable(childIcons[arg1]));
		return arg3;
	}

	@Override
	public int getChildrenCount(int group) {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
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
			arg2 = inf.inflate(R.layout.row_list_2 , arg3 , false);
			holder.icon = (ImageView) arg2.findViewById(R.id.iconImage2);
			holder.name = (TextView)arg2.findViewById(R.id.directoryName2);
			arg2.setTag(holder);
		}else
			holder = (Holder) arg2.getTag();
		
		holder.name.setText(group.get(arg0));
		holder.icon.setImageDrawable(mContext.getResources().getDrawable(icons[arg0]));
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
		return false;
	}

}
