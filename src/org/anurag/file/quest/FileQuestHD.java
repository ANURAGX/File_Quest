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

import org.anurag.adapters.PagerAdapters;

import com.astuetz.PagerSlidingTabStrip;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;


/**
 * main activity class for File quest....
 * 
 * @author anurag
 *
 */
public class FileQuestHD extends FragmentActivity {

	private PagerSlidingTabStrip indicator;
	private ActionBar actionBar;
	private ViewPager pager;
	private PagerAdapters adapters;
	private ActionBarDrawerToggle toggle;
	private DrawerLayout drawer;
	private SharedPreferences prefs;
	
	private Editor prefs_editor;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		prefs = getSharedPreferences("SETTINGS", 0);
		
		Constants.SORT_TYPE = prefs.getInt("SORT_TYPE", 2);
		Constants.FOLDER_ICON = prefs.getInt("ICON", 0);
		Constants.SHOW_HIDDEN_FOLDERS = prefs.getBoolean("SHOW_HIDDEN", false);
		Constants.FOLDER_IMAGE = getResources().getDrawable(R.drawable.folder);
		Constants.db = new ItemDB(FileQuestHD.this);
		Constants.size = new Point();
		getWindowManager().getDefaultDisplay().getSize(Constants.size);
		
		prefs_editor = prefs.edit();
		
		setContentView(R.layout.fq_ui_hd);
		findViewIds();
		
		toggle = new ActionBarDrawerToggle(FileQuestHD.this, drawer,
				R.drawable.file_quest_icon, R.string.settings, R.string.app_name){
			public void onDrawerClosed(View view) {
                getActionBar().setTitle(getString(R.string.app_name));
                //isDrawerOpen = false;                
            } 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.settings));
                //isDrawerOpen = true;
            }
		};
			
		styleActionBar(getResources().getColor(R.color.orange));
		drawer.setDrawerListener(toggle);
	
		pager.setAdapter(adapters);
		indicator.setViewPager(pager);
		pager.setOffscreenPageLimit(4);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!prefs.getString("VERSION", "0.0.0").equalsIgnoreCase(getString(R.string.appversion))){
			prefs_editor.putString("VERSION", getString(R.string.appversion));
			prefs_editor.commit();
			new WhatsNew(FileQuestHD.this, Constants.size.x*8/9, Constants.size.y*8/9);
		}
	}
	
	/**
	 * 
	 * @param color
	 */
	private void styleActionBar(int color) {
		// TODO Auto-generated method stub
		actionBar = getActionBar();
		actionBar.setIcon(R.drawable.file_quest_icon);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		Drawable clr = new ColorDrawable(color);
		actionBar.setBackgroundDrawable(clr);
		drawer.setBackgroundColor(color);
		LinearLayout drawermenu = (LinearLayout) findViewById(R.id.drawer_list);
		drawermenu.setBackgroundColor(color);
	}
	
	/**
	 * this function finds the ids of all view used....
	 */
	private void findViewIds() {
		// TODO Auto-generated method stub
		indicator = (PagerSlidingTabStrip) findViewById(R.id.pagerSlidingTabStrip1);
		pager = (ViewPager) findViewById(R.id.view);
		adapters = new PagerAdapters(getSupportFragmentManager());
		drawer = (DrawerLayout)findViewById(R.id.sliding_drawer);
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}	
}
