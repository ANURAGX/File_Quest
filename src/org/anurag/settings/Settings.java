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

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import org.anurag.file.quest.SystemBarTintManager;
import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("CommitPrefEdits")
public class Settings extends ActionBarActivity implements View.OnClickListener{

	
	SharedPreferences settingsPrefs;
	SharedPreferences.Editor edit;
	
	
	public static ImageView applied;
	public static boolean settingsChanged;
	public static boolean pagerAnimSettingsChanged;
	private ExpandableListView listview;
	private Toolbar bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsChanged = false;
		setContentView(R.layout.settings_ui);
		settingsPrefs = getSharedPreferences("SETTINGS", 0);
		edit = settingsPrefs.edit();	
		
		bar = (Toolbar) findViewById(R.id.toolbar_top);
		listview = (ExpandableListView) findViewById(R.id.list);
		
		setSupportActionBar(bar);
		getSupportActionBar().setTitle(R.string.settings);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Constants.COLOR_STYLE)); 
		getSupportActionBar().setIcon(R.drawable.action_settings);
		
		LinearLayout main = (LinearLayout) findViewById(R.id.main);
		main.setBackgroundColor(Constants.COLOR_STYLE);
		listview.setAdapter(new ExpAdapt(Settings.this));
		
		listview.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int group,
					int arg3, long arg4) {
				// TODO Auto-generated method stub
				switch(group){
				case 0:
					Constants.LIST_ANIM = arg3;
					edit.putInt("LIST_ANIM", Constants.LIST_ANIM);
					edit.commit();
					listview.collapseGroup(group);
					listview.expandGroup(group, true);
					sendBroadcast(new Intent("list_view_anim_changed"));
					break;
					
				case 1:
					
					break;
					
				case 2:
					Constants.PANEL_NO = arg3;
					edit.putInt("PANEL_NO", Constants.PANEL_NO);
					edit.commit();
					listview.collapseGroup(group);
					listview.expandGroup(group, true);
					break;
				}
				return false;
			}
		});
		
	}

	@Override
	public void onResume(){
		super.onResume();
		init_system_ui();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(settingsChanged || pagerAnimSettingsChanged){
			this.setResult(100);
			finish();
		}	
	}

	@SuppressLint("CutPasteId")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * restyles the system UI like status bar or navigation bar if present....
	 */
	private void init_system_ui() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemBarTintManager tint = new SystemBarTintManager(Settings.this);
		tint.setStatusBarTintEnabled(true);
		tint.setStatusBarTintColor(Constants.COLOR_STYLE);
		SystemBarConfig conf = tint.getConfig();
		boolean hasNavBar = conf.hasNavigtionBar();
		if(hasNavBar){
			tint.setNavigationBarTintEnabled(true);
			tint.setNavigationBarTintColor(Constants.COLOR_STYLE);
		}
		LinearLayout main = (LinearLayout) findViewById(R.id.main);
		main.setPadding(0, getStatusBarHeight(), 0, hasNavBar ? getNavigationBarHeight() :0);
	}
	
	/**
	 * 
	 * @return height of status bar....
	 */
	private int getStatusBarHeight(){
		int res = 0;
		int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if(resId > 0)
			res = getResources().getDimensionPixelSize(resId);
		return res;
	}
	
	/**
	 * 
	 * @return the height of navigation bar....
	 */
	private int getNavigationBarHeight(){
		int res = 0;
		int resId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if(resId > 0)
			res = getResources().getDimensionPixelSize(resId);
		return res;
	}
	
	private static class ExpAdapt extends BaseExpandableListAdapter{

		String[] groups ;
		String[] animls;
		String[] pagels;
		String[] panels;
		String[] bug;
		String[] lock;
		LayoutInflater inf;
		public ExpAdapt(Context ctx) {
			// TODO Auto-generated constructor stub
			groups = ctx.getResources().getStringArray(R.array.settings_array );
			animls = ctx.getResources().getStringArray(R.array.animls);
			pagels = ctx.getResources().getStringArray(R.array.pagels);
			panels = ctx.getResources().getStringArray(R.array.panels);
			bug = ctx.getResources().getStringArray(R.array.bug);		
			lock = ctx.getResources().getStringArray(R.array.locker);		
			inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		class grpHold{
			ImageView img;
			TextView nam;
		}
		
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			grpHold h = new grpHold();
			View view = inf.inflate(R.layout.row_list_2, parent , false);
			h.img = (ImageView) view.findViewById(R.id.iconImage2);
			h.nam = (TextView) view.findViewById(R.id.directoryName2);
			
			//setting padding to differentiate the expanded sub menus
			//from visible list....
			view.setPadding(30, 0, 0, 0);
			view.setTag(h);

			switch(groupPosition){
			case 0:
				if(Constants.LIST_ANIM == childPosition)
					h.img.setBackgroundResource(R.drawable.long_click_check);
				h.nam.setText(animls[childPosition]);
				break;
				
			case 1:
				if(Constants.PAGER_ANIM == childPosition)
					h.img.setBackgroundResource(R.drawable.long_click_check);
				
				h.nam.setText(pagels[childPosition]);				
				break;
				
			case 2:
				h.nam.setText(panels[childPosition]);	
				if(Constants.PANEL_NO == childPosition)
					h.img.setBackgroundResource(R.drawable.long_click_check);
				break;
			
			case 3:
				h.nam.setText(lock[childPosition]);
				break;
				
			case 7:
				h.nam.setText(bug[childPosition]);				
				break;			
			}		
			return view;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			switch (groupPosition) {
			case 0:
				return animls.length;

			case 1:
				return pagels.length;
				
			case 2:
				return panels.length;
				
			case 3:
				return lock.length;
				
			case 7:
				return bug.length;
			}
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groups.length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			grpHold h = new grpHold();
			View view = inf.inflate(R.layout.row_list_2, parent , false);
			h.img = (ImageView) view.findViewById(R.id.iconImage2);
			h.nam = (TextView) view.findViewById(R.id.directoryName2);
			view.setTag(h);
			
			switch(groupPosition){
			case 0:
				h.img.setBackgroundResource(R.drawable.views);
				h.nam.setText(groups[0]);
				break;
				
			case 1:
				h.img.setBackgroundResource(R.drawable.pager_anim);
				h.nam.setText(groups[groupPosition]);
				break;
				
			case 2:
				h.img.setBackgroundResource(R.drawable.startup_panel);
				h.nam.setText(groups[groupPosition]);
				break;

			case 3:
				h.img.setBackgroundResource(R.drawable.locker);
				h.nam.setText(groups[groupPosition]);
				break;
				
			case 4:
				h.img.setBackgroundResource(R.drawable.action_settings);
				h.nam.setText(groups[groupPosition]);
				break;
				
			case 5:
				h.img.setBackgroundResource(R.drawable.gesture);
				h.nam.setText(groups[groupPosition]);
				break;
				

			case 6:
				h.img.setBackgroundResource(R.drawable.share);
				h.nam.setText(groups[groupPosition]);
				break;
				
			case 7:
				h.img.setBackgroundResource(R.drawable.bug);
				h.nam.setText(groups[groupPosition]);
				break;
		
			case 8:
				h.img.setBackgroundResource(R.drawable.about);
				h.nam.setText(groups[groupPosition]);
				break;
	
			}
			return view;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}		
	}
}
