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

import org.anurag.adapters.AppStore;
import org.anurag.adapters.FileGallery;
import org.anurag.adapters.PagerAdapters;
import org.anurag.adapters.RootPanel;
import org.anurag.adapters.SdCardPanel;
import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.fuehlbypa.kddcbytnh159110.Prm;



/**
 * main activity class for File quest....
 * 
 * @author anurag
 *
 */
public class FileQuestHD extends ActionBarActivity implements Toolbar.OnMenuItemClickListener{

	private ActionBar action_bar;
	private static PagerSlidingTabStrip indicator;
	private ViewPager pager;
	private PagerAdapters adapters;
	private boolean isDrawerOpen;
	private Toolbar toolbar;
	private Toolbar top_toolbar;
	private Toolbar bottom_options;
	private ActionBarDrawerToggle toggle;
	private DrawerLayout drawer;
	private SharedPreferences prefs;
	
	private boolean mBackPressed;
	
	private Editor prefs_editor;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		prefs = getSharedPreferences("SETTINGS", 0);
		
		Constants.SORT_TYPE = prefs.getInt("SORT_TYPE", 2);
		Constants.FOLDER_ICON = prefs.getInt("ICON", 0);
		Constants.SHOW_HIDDEN_FOLDERS = prefs.getBoolean("SHOW_HIDDEN", false);
		Constants.PANEL_NO = prefs.getInt("PANEL_NO", 0);
		Constants.COLOR_STYLE = prefs.getInt("COLOR_STYLE", 0xFF5161BC);
		Constants.LIST_ANIM = prefs.getInt("LIST_ANIM", 3);
		Constants.ACTION_AT_TOP = prefs.getBoolean("ACTION_AT_TOP", false);
		Constants.LIST_TYPE = prefs.getInt("LIST_TYPE", 1);
		
		Constants.size = new Point();
		getWindowManager().getDefaultDisplay().getSize(Constants.size);
		Constants.BUILD_ICONS(FileQuestHD.this);
		prefs_editor = prefs.edit();
		
		//building the theme style as per the color selected by user.... 
		ThemeOrganizer.BUILD_THEME(Constants.COLOR_STYLE);
		
		//builds the folder icon after building the theme....
		ThemeOrganizer.BUILD_FOLDER_ICON(FileQuestHD.this);
		
		//reading external pr internal storages.....
		new StorageUtils();
		Constants.db = new ItemDB(FileQuestHD.this);
		setContentView(R.layout.fq_ui_hd);
		findViewIds();
		
		if(!Constants.ACTION_AT_TOP){
			setSupportActionBar(toolbar);
			top_toolbar.setVisibility(View.GONE);
		}	
		else{
			setSupportActionBar(top_toolbar);
			toolbar.setVisibility(View.GONE);
		}	
		
		action_bar = getSupportActionBar();
		styleActionBar(Constants.COLOR_STYLE);
		
		toggle = new ActionBarDrawerToggle(FileQuestHD.this, drawer,
				R.drawable.file_quest_icon, R.string.settings){
			public void onDrawerClosed(View view) {
                action_bar.setTitle(getString(R.string.app_name));
                isDrawerOpen = false;                
            } 
            public void onDrawerOpened(View drawerView) {
                action_bar.setTitle(getString(R.string.settings));
                isDrawerOpen = true;
            }
		};
		
		drawer.setDrawerListener(toggle);
		
		pager.setAdapter(adapters);
		indicator.setViewPager(pager);
		pager.setOffscreenPageLimit(4);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if(Constants.LONG_CLICK)
					sendBroadcast(new Intent("inflate_normal_menu"));
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		init_action_bar();
		init_drawer_menu();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inf = getMenuInflater();
		if(!Constants.LONG_CLICK)
			inf.inflate(R.menu.main_actionbar_menu, menu);
		else
			inf.inflate(R.menu.long_clk_menu_hd, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_exit:
			FileQuestHD.this.finish();
			return true;
			
		case android.R.id.home:
			if(!Constants.LONG_CLICK)
				drawer.openDrawer(Gravity.START);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init_system_ui();
		mBackPressed = false;
		if(!prefs.getString("VERSION", "0.0.0").equalsIgnoreCase(getString(R.string.appversion))){
			prefs_editor.putString("VERSION", getString(R.string.appversion));
			prefs_editor.commit();
			new WhatsNew(FileQuestHD.this, Constants.size.x*8/9, Constants.size.y*8/9);
		}
		Prm prm = new Prm(FileQuestHD.this, null, true);
		prm.run360Ad(FileQuestHD.this, 0, false, null);
		register_receiver();
	}
	
	/**
	 * 
	 * @param color
	 */
	private void styleActionBar(int color) {
		// TODO Auto-generated method stub
		toolbar.setBackgroundColor(color);
		top_toolbar.setBackgroundColor(color);
		indicator.setBackgroundColor(color);
		LinearLayout drawermenu = (LinearLayout) findViewById(R.id.drawer_list);
		drawermenu.setBackgroundColor(color);
		action_bar.setHomeButtonEnabled(true);
		//action_bar.setDisplayHomeAsUpEnabled(true);	
		action_bar.setHomeAsUpIndicator(R.drawable.drawer_menu);
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
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		bottom_options = (Toolbar) findViewById(R.id.bottom_options);
		top_toolbar = (Toolbar)findViewById(R.id.toolbar_top);
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(isDrawerOpen){
			drawer.closeDrawers();
			return;
		}

		int panel = pager.getCurrentItem();
		
		if(Constants.LONG_CLICK){
			sendBroadcast(new Intent("inflate_normal_menu"));
			return;
		}
		
		if(panel == 0){
			if(FileGallery.isGalleryOpened())
				FileGallery.collapseGallery();
			else if(panel == Constants.PANEL_NO)
				detect_back_press();			
		}else if(panel == 1){
			if(RootPanel.isAtTopLevel() && panel != Constants.PANEL_NO)
				pager.setCurrentItem(Constants.PANEL_NO);
			else if(RootPanel.isAtTopLevel())
				detect_back_press();
			else
				RootPanel.navigate_to_back();
		}else if(panel == 2){
			if(SdCardPanel.isAtTopLevel() && panel != Constants.PANEL_NO)
				pager.setCurrentItem(Constants.PANEL_NO);
			else if(SdCardPanel.isAtTopLevel())
				detect_back_press();
			else
				SdCardPanel.navigate_to_back();
		}else if(panel == 3 && panel == Constants.PANEL_NO)
			detect_back_press();
		else
			pager.setCurrentItem(Constants.PANEL_NO);
	}	
	
	
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrate.vibrate(10);
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}

	/**
	 * this function checks how many times back key is pressed
	 * on second press it finishes the app....
	 */
	private void detect_back_press(){
		if(mBackPressed)
			FileQuestHD.this.finish();
		else{
			mBackPressed = true;
			Toast.makeText(FileQuestHD.this, R.string.pressbackagain, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 
	 * @param position
	 * @param title
	 */
	public static void notify_Title_Indicator(int position , String title){
		PagerAdapters.setTitles(position, title);
		indicator.notifyDataSetChanged();
	}
	
	/**
	 * restyles the system UI like status bar or navigation bar if present....
	 */
	private void init_system_ui() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemBarTintManager tint = new SystemBarTintManager(FileQuestHD.this);
		tint.setStatusBarTintEnabled(true);
		tint.setStatusBarTintColor(Constants.COLOR_STYLE);
		
		LinearLayout slide_menu = (LinearLayout) findViewById(R.id.drawer_list);
		LinearLayout main = (LinearLayout) findViewById(R.id.frame_container);
		
		main.setPadding(0, getStatusBarHeight(), 0, 0);
		slide_menu.setPadding(0, getStatusBarHeight(), 0, 0);
		
		SystemBarConfig conf = tint.getConfig();
		if(conf.hasNavigtionBar()){
			tint.setNavigationBarTintEnabled(true);
			tint.setNavigationBarTintColor(Constants.COLOR_STYLE);
			main.setPadding(0, 0, 0, getNavigationBarHeight());
			slide_menu.setPadding(0, 0, 0, getNavigationBarHeight());
		}
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

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.red:
			Constants.COLOR_STYLE = getResources().getColor(R.color.red);
			change_ui_color();
			return true;
		
		case R.id.grey:
			Constants.COLOR_STYLE = getResources().getColor(R.color.grey);
			change_ui_color();
			return true;
			
		case R.id.green:
			Constants.COLOR_STYLE = getResources().getColor(R.color.green);
			change_ui_color();
			return true;
			
		case R.id.Orange:
			Constants.COLOR_STYLE = getResources().getColor(R.color.orange);
			change_ui_color();
			return true;	
		
		case R.id.blue:
			Constants.COLOR_STYLE = getResources().getColor(R.color.blue);
			change_ui_color();
			return true;
			
		case R.id.violet:
			Constants.COLOR_STYLE = getResources().getColor(R.color.violet);
			change_ui_color();
			return true;	
			
		case R.id.action_refresh:
			//refreshing the list view....
			if(pager.getCurrentItem() == 2)
				SdCardPanel.refresh_list();
			else if(pager.getCurrentItem() == 1)
				RootPanel.refresh_list();
			else if(pager.getCurrentItem() == 0)
				FileGallery.refresh_list();
			else 
				AppStore.refresh_list();
			return true;	
		
		}
		
		return true;
	}

	/**
	 * this function is invoked when user changes the color
	 * and new theme is build and the ui is updated....
	 */
	private void change_ui_color() {
		// TODO Auto-generated method stub
		styleActionBar(Constants.COLOR_STYLE);
		init_system_ui();
		ThemeOrganizer.BUILD_THEME(Constants.COLOR_STYLE);
		ThemeOrganizer.BUILD_FOLDER_ICON(FileQuestHD.this);
		//ThemeOrganizer.UPDATE_LIST_SELECTORS();
		ThemeOrganizer.APPLY_FOLDER_THEME(pager.getCurrentItem());
		
		prefs_editor.putInt("COLOR_STYLE", Constants.COLOR_STYLE);
		prefs_editor.putInt("ICON", Constants.FOLDER_ICON);
		prefs_editor.commit();
	}
	
	//this function placement of action bar either at top
	//or bottom....
	private void init_action_bar() {
		// TODO Auto-generated method stub
		//inflating menu in standalone mode for bottom options....
		bottom_options.inflateMenu(R.menu.bottom_options_actionbar_hd);
			if(!Constants.ACTION_AT_TOP)
				bottom_options.setNavigationIcon(R.drawable.up_action);
			else
				bottom_options.setNavigationIcon(R.drawable.down_action);
		bottom_options.setOnMenuItemClickListener(this);
			
		bottom_options.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					if(!Constants.ACTION_AT_TOP){
						bottom_options.setNavigationIcon(R.drawable.down_action);
						top_toolbar.setVisibility(View.VISIBLE);
						toolbar.setVisibility(View.GONE);
						setSupportActionBar(top_toolbar);					
					}else{
						bottom_options.setNavigationIcon(R.drawable.up_action);
						top_toolbar.setVisibility(View.GONE);
						toolbar.setVisibility(View.VISIBLE);
						setSupportActionBar(toolbar);
					}
				
					//updating the menu....
					invalidateOptionsMenu();
					action_bar = getSupportActionBar();
					action_bar.setHomeButtonEnabled(true);
					//action_bar.setDisplayHomeAsUpEnabled(true);	
					action_bar.setHomeAsUpIndicator(R.drawable.drawer_menu);
					
					//saving the changes....
					Constants.ACTION_AT_TOP = !Constants.ACTION_AT_TOP;
					prefs_editor.putBoolean("ACTION_AT_TOP", Constants.ACTION_AT_TOP);
					prefs_editor.commit();
				}
			});
	}
	
	/**
	 * 
	 * @author anurag
	 *
	 */
	private class Receive_Broadcasts extends BroadcastReceiver{
		
		public Receive_Broadcasts() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String action = arg1.getAction();
			int panel = pager.getCurrentItem();
			if(action.equalsIgnoreCase("inflate_long_click_menu")){
				Constants.LONG_CLICK = true;
				invalidateOptionsMenu();
				
				//setting up action bar when an item is long clicked....
				action_bar.setTitle("1");
				action_bar.setHomeAsUpIndicator(R.drawable.long_click_check);
			}else if(action.equalsIgnoreCase("inflate_normal_menu")){
				Constants.LONG_CLICK = false;
				//long click got disabled,restore default screen.....
				if(panel == 2){
					SdCardPanel.clear_selected_items();
					SdCardPanel.ITEMS = null;
				}else if(panel == 1){
					RootPanel.clear_selected_items();
					RootPanel.ITEMS = null;
				}else if(panel == 3){
					AppStore.clear_selected_items();
					AppStore.ITEMS = null;
				}else if(panel == 0){
					FileGallery.clear_selected_items();
					FileGallery.ITEMS = null;
				}
				invalidateOptionsMenu();
				
				//setting up action bar when long click is inactive....
				action_bar.setHomeAsUpIndicator(R.drawable.drawer_menu);
				action_bar.setTitle(R.string.app_name);
			}else if(action.equalsIgnoreCase("update_action_bar_long_click")){
				//update the action bar as per no. of selected items....
				if(Constants.LONG_CLICK){
					if(panel == 0){
						action_bar.setTitle(""+FileGallery.counter);
					}else if(panel == 1){
						action_bar.setTitle(""+RootPanel.counter);
					}else if(panel == 2){
						action_bar.setTitle(""+SdCardPanel.counter);
					}else if(panel == 3){
						action_bar.setTitle(""+AppStore.counter);
					}
				}
			}
		}		
	}
	
	/**
	 * this function is called in onResume function....
	 */
	private void register_receiver(){
		Receive_Broadcasts broadcasts = new Receive_Broadcasts();
		IntentFilter filter = new IntentFilter("inflate_long_click_menu");
		filter.addAction("inflate_normal_menu");
		filter.addAction("update_action_bar_long_click");
		this.registerReceiver(broadcasts, filter);
	}
	
	private void init_drawer_menu() {
		// TODO Auto-generated method stub
		ListView drLs = (ListView) drawer.findViewById(R.id.drawer_menu_list);
		drLs.setAdapter(new drAdpt());		
		drLs.setSelector(R.drawable.while_list_selector_hd);
		
		drLs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		drLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
					Intent intent = new Intent(FileQuestHD.this, GraphAnalysis.class);
					startActivity(intent);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				}
			}
		});
	}
	
	/**
	 * adapter for drawer....
	 * @author anurag
	 *
	 */
	class drAdpt extends BaseAdapter{

		LayoutInflater inf;
		
		public drAdpt() {
			// TODO Auto-generated constructor stub
			inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		class hold{
			ImageView img;
			TextView nam;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = inf.inflate(R.layout.row_list_2, parent , false);
			hold h = new hold();
			h.img = (ImageView) view.findViewById(R.id.iconImage2);
			h.nam = (TextView) view.findViewById(R.id.directoryName2);
			view.setTag(h);
			switch(position){
			case 0:
				h.img.setBackgroundResource(R.drawable.graph_analysis_hd);
				h.nam.setText("Graph Analysis");
				break;
				
			case 1:
				h.img.setBackgroundResource(R.drawable.file_shield_hd);
				h.nam.setText("File Shield");
				break;
				
			case 2:
				h.img.setBackgroundResource(R.drawable.file_cleaner_hd);
				h.nam.setText("File Cleaner");
				break;
			
			case 3:
				h.img.setBackgroundResource(R.drawable.file_mover_hd);
				h.nam.setText("File Mover");
				break;
				
			case 4:
				h.img.setBackgroundResource(R.drawable.file_quest_icon);
				h.nam.setText("File Zipper");
				break;
				
			case 5:
				h.img.setBackgroundResource(R.drawable.file_quest_icon);
				h.nam.setText("Check for update");
			}
			return view;
		}		
	}	
}