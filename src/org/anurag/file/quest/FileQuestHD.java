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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.anurag.adapters.PagerAdapters;
import org.anurag.dialogs.ConfirmTweakTask;
import org.anurag.dialogs.CreateItem;
import org.anurag.dialogs.DeleteFiles;
import org.anurag.dialogs.Rename;
import org.anurag.dialogs.ZipFiles;
import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;
import org.anurag.fragments.AppStore;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;
import org.anurag.gesture.AddGesture;
import org.anurag.gesture.G_Open;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.extra.libs.PagerSlidingTabStrip;
import com.extra.libs.TransitionViewPager;



/**
 * main activity class for File quest....
 * 
 * @author anurag
 *
 */
public class FileQuestHD extends ActionBarActivity implements Toolbar.OnMenuItemClickListener,View.OnClickListener{

	//action bar instance....
	private ActionBar action_bar;
	
	//strip indicator for view pager....
	private static PagerSlidingTabStrip indicator;
	
	//extends view pager 
	public static TransitionViewPager pager;
	
	//adapter for view pager....
	private PagerAdapters adapters;
	
	//true then drawer is open...
	private boolean isDrawerOpen;
	
	//toolbar used as action bar at bottom....
	private Toolbar toolbar;
	
	//toolbar used as action bar at top position...
	private Toolbar top_toolbar;
	
	//extra options shown in this toolbar....
	private Toolbar bottom_options;
	
	//toggle listener for drawer layout...
	private ActionBarDrawerToggle toggle;
	
	//sliding drawer layout....
	private DrawerLayout drawer;
	
	//retrieving the app's preferences....
	private SharedPreferences prefs;
	
	//detecting back press....
	private boolean mBackPressed;
	
	//editor for saving settings....
	private Editor prefs_editor;
	
	//navigation icon in main action bar....
	private ImageView navIcon;
	
	//indicates no. of folder when long press is active....
	private TextView no_folder;
	
	//indicates no. of files when long press is active....
	private TextView no_files;
	
	//indicates no. of selected items when long press is active....
	private TextView no_selection;
	
	//holds the available space of sd card
	private String avail ;
	
	//holds the total space of sd card....
	private String total;
	
	//broadcast for certain actions within the app
	private Receive_Broadcasts broadcasts;
	
	//manager to manager copy ,cut and paste operations
	private QueuedTaskManager mgr;
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
		Constants.LIST_TYPE = prefs.getInt("LIST_TYPE", 2);
		Constants.SORT_TYPE = prefs.getInt("SORT_TYPE", 3);
		Constants.PAGER_ANIM = prefs.getInt("PAGER_ANIM", 3);
		Constants.size = new Point();
		getWindowManager().getDefaultDisplay().getSize(Constants.size);
		Constants.BUILD_ICONS(FileQuestHD.this);
		Constants.ctx = FileQuestHD.this;
		prefs_editor = prefs.edit();
		
		//builds the icons for list view....
		Constants.BUILD_LIST_ICONS(FileQuestHD.this);
		
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
		init_actionbar_custom_view();
		
		toggle = new ActionBarDrawerToggle(FileQuestHD.this, drawer,
				R.drawable.file_quest_icon, R.string.settings){
			public void onDrawerClosed(View view) {
                action_bar.setTitle("");
                isDrawerOpen = false;                
            } 
            public void onDrawerOpened(View drawerView) {
                action_bar.setTitle("");
                isDrawerOpen = true;
            }
		};
		
		drawer.setDrawerListener(toggle);
		
		//loading internal sd card info on action bar
		//in background thread....
		load_sd_space();
		
		adapters = new PagerAdapters(getSupportFragmentManager());
		pager.setAdapter(adapters);
		pager.setOffscreenPageLimit(4);
		pager.setTransitionEffedt(Constants.PAGER_ANIM);
		indicator.setViewPager(pager);
		
		//setting the pager to user's setting....
		pager.setCurrentItem(Constants.PANEL_NO);
		
		init_action_bar();
		init_drawer_menu();
		init_with_device_id();

		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				for(int i = 0 ; i < 4 ; ++i)
					if(Constants.LONG_CLICK[i]){
						invalidateOptionsMenu();
						break;
					}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(broadcasts);
	}
	
	//setting custom views to action bar....
	private void init_actionbar_custom_view() {
		// TODO Auto-generated method stub
		setSupportActionBar(toolbar);
		getSupportActionBar().setCustomView(R.layout.space_action_bar_hd);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		setSupportActionBar(top_toolbar);
		getSupportActionBar().setCustomView(R.layout.space_action_bar_hd);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inf = getMenuInflater();
		if(!Constants.LONG_CLICK[pager.getCurrentItem()]){
			inf.inflate(R.menu.main_actionbar_menu, menu);
			
								
			//setting up action bar to normal....
			action_bar.setTitle("");
			navIcon = (ImageView) toolbar.findViewById(R.id.open_drawer_menu);
			navIcon.setBackgroundColor(Constants.COLOR_STYLE);
			navIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
			navIcon = (ImageView) top_toolbar.findViewById(R.id.open_drawer_menu);
			navIcon.setBackgroundColor(Constants.COLOR_STYLE);
			navIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
			
			no_selection = (TextView) top_toolbar.findViewById(R.id.no_selection);
			no_selection.setText(R.string.internal_sd);
			no_selection = (TextView) toolbar.findViewById(R.id.no_selection);
			no_selection.setText(R.string.internal_sd);
			
			no_folder = (TextView) top_toolbar.findViewById(R.id.total_space);
			no_folder.setText(total);
			no_folder = (TextView) toolbar.findViewById(R.id.total_space);
			no_folder.setText(total);
			
			no_files = (TextView) top_toolbar.findViewById(R.id.avail_space);
			no_files.setText(avail);
			no_folder = (TextView) toolbar.findViewById(R.id.total_space);
			no_folder.setText(total);
			
			if(!mgr.hasTask()){
				//no copy , paste and cut task
				menu.findItem(R.id.action_queued).setVisible(false);
			}else{
				//there's copy , paste or cut task
				//so queue it in the list
				mgr.prepareMenu(menu.findItem(R.id.action_queued).getSubMenu());
			}
		}	
		else{
			inf.inflate(R.menu.long_clk_menu_hd, menu);
			sendBroadcast(new Intent("update_action_bar_long_click"));
		}	
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		int panel = pager.getCurrentItem();
		
		switch(item.getItemId()){
		case R.id.action_exit:
			FileQuestHD.this.finish();
			return true;
			
		case android.R.id.home:
			//if(!Constants.LONG_CLICK)
				//drawer.openDrawer(Gravity.START);
			break;
			
		case R.id.action_setting:
			Intent intent = new Intent(FileQuestHD.this, Settings.class);
			startActivity(intent);
			break;
		
		case R.id.action_delete:
			switch(panel){
			case 0:
				new DeleteFiles(FileQuestHD.this, Constants.size.x*8/9, FileGallery.lists,
						FileGallery.keys , null);
				break;
			case 1:
				new DeleteFiles(FileQuestHD.this, Constants.size.x*8/9,
						RootPanel.get_selected_items(), null);
				break;
				
			case 2:
				new DeleteFiles(FileQuestHD.this, Constants.size.x*8/9,
						SdCardPanel.get_selected_items() , null);
			}
			break;
			
		case R.id.action_add_gesture:
			Intent intnt = new Intent(FileQuestHD.this , AddGesture.class);
			startActivity(intnt);
			break;
			
		case R.id.action_rename:
			//renaming the file...
			if(Constants.LONG_CLICK[panel])
				switch(panel){
				
				case 0:
					//first checking the locked status of files to be renamed....
					{
						ArrayList<Item> re_ls = FileGallery.get_selected_items();
						if(!FileGallery.does_list_has_locked_item())
							new Rename(FileQuestHD.this, re_ls , panel);
						else
							new MasterPassword(FileQuestHD.this, Constants.size.x*8/9, null ,
									prefs, Constants.MODES.RENAME);
					}
					
					break;
				
				case 1:
					//first checking the locked status of files to be renamed....
					{
						ArrayList<Item> re_ls = RootPanel.get_selected_items();
						if(!RootPanel.does_list_has_locked_item())
							new Rename(FileQuestHD.this, re_ls, panel);
						else
							new MasterPassword(FileQuestHD.this, Constants.size.x*8/9, null ,
									prefs, Constants.MODES.RENAME);
					}
					break;
				
				case 2:
					//first checking the locked status of files to be renamed....
					{
						ArrayList<Item> re_ls = SdCardPanel.get_selected_items();
						if(!SdCardPanel.does_list_has_locked_item())
							new Rename(FileQuestHD.this, re_ls, panel);
						else
							new MasterPassword(FileQuestHD.this, Constants.size.x*8/9, null, 
									prefs, Constants.MODES.RENAME);
							
					}
					break;
				}
			break;
			
		case R.id.action_gesture:
			//launching activity to recognize the gesture....
			Intent innt = new Intent(FileQuestHD.this , G_Open.class);
			startActivityForResult(innt , 10);
			break;
			
		case R.id.action_zip:
			//zipping the files...
			if(Constants.LONG_CLICK[panel])
				switch(panel){
				
				case 0:
					//first checking the locked status of files to be renamed....
					{
						ArrayList<Item> re_ls = FileGallery.get_selected_items();
						if(!FileGallery.does_list_has_locked_item())
							new ZipFiles(FileQuestHD.this ,  re_ls);
						else
							new MasterPassword(FileQuestHD.this, Constants.size.x*8/9, null ,
									prefs, Constants.MODES.ARCHIVE);
					}
					
					break;
				
				case 1:
					//first checking the locked status of files to be renamed....
					{
						ArrayList<Item> re_ls = RootPanel.get_selected_items();
						if(!RootPanel.does_list_has_locked_item())
							new ZipFiles(FileQuestHD.this,  re_ls);
						else
							new MasterPassword(FileQuestHD.this, Constants.size.x*8/9, null ,
									prefs, Constants.MODES.ARCHIVE);
					}
					break;
				
				case 2:
					//first checking the locked status of files to be renamed....
					{
						ArrayList<Item> re_ls = SdCardPanel.get_selected_items();
						if(!SdCardPanel.does_list_has_locked_item())
							new ZipFiles(FileQuestHD.this,  re_ls);
						else
							new MasterPassword(FileQuestHD.this, Constants.size.x*8/9, null, 
									prefs, Constants.MODES.ARCHIVE);							
					}
					break;
				}
			break;
			
		case R.id.action_properties:
			//showing properties of files....
			{
				Intent itent = new Intent(FileQuestHD.this, FileProperties.class);
				startActivity(itent);
			}
			break;
			
		case R.id.action_copy:
			{
				QueuedTask task = null;
				switch(panel){
				case 0:
					
					break;
					
				case 1:
					task = new QueuedTask(RootPanel.get_selected_items(), mgr.COPY_ID,
							RootPanel.get_current_working_dir().getParent(),
							RootPanel.folder_count, RootPanel.file_count);
					RootPanel.clear_selected_items();
					break;
					
				case 2:
					task = new QueuedTask(SdCardPanel.get_selected_items(), mgr.COPY_ID,
							SdCardPanel.get_current_working_dir().getParent(),
							SdCardPanel.folder_count, SdCardPanel.file_count);
					SdCardPanel.clear_selected_items();
					break;
				}
				
				Constants.LONG_CLICK[panel] = false;
				task.setId(mgr.getId());
				mgr.add_task(task , task.getId());
				invalidateOptionsMenu();
			}
			break;
			
		case R.id.action_cut:
			{
				
				
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void test() {
		// TODO Auto-generated method stub
		Toast.makeText(FileQuestHD.this, "Got",Toast.LENGTH_LONG).show();
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
		register_receiver();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		int panel = pager.getCurrentItem();
		
	    switch(requestCode){
	    case 10:
	    	if(resultCode == RESULT_OK){
	    		String path = data.getStringExtra("gesture_path");
	    		if(!Constants.db.isLocked(path)) // item is not locked....
	    			open_gesture_recognized_item(path , panel);
	    		
	    		else //item is locked.... 
	    			new MasterPassword(FileQuestHD.this, Constants.size.x*8/9,
	    					new Item(new File(path), null , null , null), 
	    					prefs , Constants.MODES.G_OPEN);
	    	}
	    	break;
	    }
	}
	
	/**
	 * 
	 * @param path
	 * @param panel
	 */
	public static void open_gesture_recognized_item(String path, int panel) {
		// TODO Auto-generated method stub
		File file = new File(path);
		if(file.isDirectory() && file.exists())
			switch(panel){
			case 0:
				if(file.canRead()){
					SdCardPanel.push_path(path);
					FileQuestHD.notify_Title_Indicator(2, file.getName());
					SdCardPanel.resetAdapter();
				}else{
					RootPanel.push_path(path);
					FileQuestHD.notify_Title_Indicator(1, file.getName());
					RootPanel.resetAdapter();
				}    						
				break;
				
			case 3:
				SdCardPanel.push_path(path);
				FileQuestHD.notify_Title_Indicator(2, file.getName());
				SdCardPanel.resetAdapter();
				break;
			
			case 1:
				RootPanel.push_path(path);
				FileQuestHD.notify_Title_Indicator(1, file.getName());
				RootPanel.resetAdapter();
				break;
			
			case 2:
				SdCardPanel.push_path(path);
				FileQuestHD.notify_Title_Indicator(panel, file.getName());
				SdCardPanel.resetAdapter();
				break;
		}
		else
			if(file.exists())
				new OpenFileDialog(Constants.ctx, Uri.parse(path) ,
						Constants.size.x*8/9);
	}

	/**
	 * 
	 * @return the current panel number....
	 */
	public static int getCurrentItem(){
		return pager.getCurrentItem();
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
		bottom_options.setBackgroundColor(color);
		LinearLayout drawermenu = (LinearLayout) findViewById(R.id.drawer_list);
		drawermenu.setBackgroundColor(color);
	}
	
	/**
	 * this function finds the ids of all view used....
	 */
	private void findViewIds() {
		// TODO Auto-generated method stub
		indicator = (PagerSlidingTabStrip) findViewById(R.id.pagerSlidingTabStrip1);
		pager = (TransitionViewPager) findViewById(R.id.view);
		drawer = (DrawerLayout)findViewById(R.id.sliding_drawer);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		bottom_options = (Toolbar) findViewById(R.id.bottom_options);
		top_toolbar = (Toolbar)findViewById(R.id.toolbar_top);
		mgr = new QueuedTaskManager();
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(isDrawerOpen){
			drawer.closeDrawers();
			return;
		}

		int panel = pager.getCurrentItem();
		
		if(Constants.LONG_CLICK[panel]){
			sendBroadcast(new Intent("inflate_normal_menu"));
			return;
		}
		
		if(panel == 0){
			if(FileGallery.isGalleryOpened())
				FileGallery.collapseGallery();
			else if(panel == Constants.PANEL_NO)
				detect_back_press();		
			else if(panel != Constants.PANEL_NO)
				pager.setCurrentItem(Constants.PANEL_NO);
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
		if(position == 1){
			if(title.equalsIgnoreCase(""))
				title = "/";
		}
		PagerAdapters.setTitles(position, title);
		indicator.notifyDataSetChanged();
		pager.setCurrentItem(position);
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
		SystemBarConfig conf = tint.getConfig();
		boolean hasNavBar = conf.hasNavigtionBar();
		if(hasNavBar){
			tint.setNavigationBarTintEnabled(true);
			tint.setNavigationBarTintColor(Constants.COLOR_STYLE);
		}
		LinearLayout slide_menu = (LinearLayout) findViewById(R.id.drawer_list);
		LinearLayout main = (LinearLayout) findViewById(R.id.frame_container);
		
		main.setPadding(0, getStatusBarHeight(), 0, hasNavBar ? getNavigationBarHeight() :0);
		slide_menu.setPadding(0, getStatusBarHeight(), 0, hasNavBar ? getNavigationBarHeight() :0);
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
		
		int panel = pager.getCurrentItem();
		
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
			if(!Constants.LONG_CLICK[panel]){
				switch(panel){
				case 0:
					FileGallery.refresh_list();
					break;
				case 1:
					RootPanel.refresh_list();
					break;
				case 2:
					SdCardPanel.refresh_list();
					break;
				case 3:
					AppStore.refresh_list();
				}
			}
			return true;
			
		case R.id.simple_ls:
			//setting simple list view for the app....
			change_list_type(1 , pager.getCurrentItem());
			bottom_options.getMenu().findItem(R.id.simple_ls).setChecked(true);
			bottom_options.getMenu().findItem(R.id.detail_ls).setChecked(false);
			return true;
			
		case R.id.detail_ls:
			//setting he detailed list view....
			change_list_type(2 , pager.getCurrentItem());
			bottom_options.getMenu().findItem(R.id.simple_ls).setChecked(false);
			bottom_options.getMenu().findItem(R.id.detail_ls).setChecked(true);			
			return true;
		
		case R.id.new_folder:
			switch(panel){
			case 0:
				Toast.makeText(FileQuestHD.this, R.string.cant_create_here, Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(FileQuestHD.this, R.string.cant_create_here, Toast.LENGTH_SHORT).show();
				break;
				
			case 1:
				new CreateItem(FileQuestHD.this, RootPanel.get_current_working_dir()
						, true , panel);
				break;
			case 2:
				new CreateItem(FileQuestHD.this, SdCardPanel.get_current_working_dir() ,
						true , panel);
				break;
			}
			break;
			
		case R.id.new_file:
			switch(panel){
			case 0:
				Toast.makeText(FileQuestHD.this, R.string.cant_create_here, Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(FileQuestHD.this, R.string.cant_create_here, Toast.LENGTH_SHORT).show();
				break;
				
			case 1:
				new CreateItem(FileQuestHD.this, RootPanel.get_current_working_dir()
						, false , panel);
				break;
			case 2:
				new CreateItem(FileQuestHD.this, SdCardPanel.get_current_working_dir() 
						, false , panel);
				break;
			}
			break;

		case R.id.sort_az:
			change_sort_type( 1 , panel);
			break;
			
		case R.id.sort_za:
			change_sort_type( 2 , panel);
			break;
			
		case R.id.size_smaller:
			change_sort_type( 3 , panel);
			break;
			
		case R.id.size_bigger:
			change_sort_type( 4 , panel);
			break;
			
		case R.id.date_new:
			change_sort_type( 5 , panel);
			break;
			
		case R.id.date_old:
			change_sort_type( 6 , panel);
			break;
		}		
		return true;
	}

	/**
	 * 
	 * @param i sort type....
	 * @param panel currently selected panel....
	 */
	private void change_sort_type(int i, int panel) {
		// TODO Auto-generated method stub
		//saving the settings....
		Constants.SORT_TYPE = i;
		prefs_editor.putInt("SORT_TYPE", Constants.SORT_TYPE);
		prefs_editor.commit();
		
		bottom_options.getMenu().findItem(R.id.sort_az).setChecked(false);
		bottom_options.getMenu().findItem(R.id.sort_za).setChecked(false);
		bottom_options.getMenu().findItem(R.id.size_smaller).setChecked(false);
		bottom_options.getMenu().findItem(R.id.size_bigger).setChecked(false);
		bottom_options.getMenu().findItem(R.id.date_new).setChecked(false);
		bottom_options.getMenu().findItem(R.id.date_old).setChecked(false);
		
		switch(Constants.SORT_TYPE){
		case 1:
			bottom_options.getMenu().findItem(R.id.sort_az).setChecked(true);
			break;
		case 2:
			bottom_options.getMenu().findItem(R.id.sort_za).setChecked(true);
			break;
		case 3:
			bottom_options.getMenu().findItem(R.id.size_smaller).setChecked(true);
			break;
		case 4:
			bottom_options.getMenu().findItem(R.id.size_bigger).setChecked(true);
			break;
		case 5:
			bottom_options.getMenu().findItem(R.id.date_new).setChecked(true);
			break;
		case 6:
			bottom_options.getMenu().findItem(R.id.date_old).setChecked(true);
			break;
		}
		
		//setting list view of current pager item....
		switch(panel){
			case 0:
				if(!Constants.LONG_CLICK[0])
					FileGallery.resetAdapter();
				break;
				
			case 1:
				if(!Constants.LONG_CLICK[1])
				RootPanel.resetAdapter();
				break;
			
			case 2:
				if(!Constants.LONG_CLICK[2])
					SdCardPanel.resetAdapter();
				break;
				
			case 3:
				if(!Constants.LONG_CLICK[3])
					AppStore.resetAdapter();
				break;
				
			}
			
			//now setting the list view for other panels which are not visible....
			for(int j = 0 ; j<4 ; ++j){
				if(j != panel){
					switch(j){
					case 0:
						if(!Constants.LONG_CLICK[0])
							FileGallery.resetAdapter();
						break;
						
					case 1:
						if(!Constants.LONG_CLICK[1])
							RootPanel.resetAdapter();
						break;
					
					case 2:
						if(!Constants.LONG_CLICK[2])
							SdCardPanel.resetAdapter();
						break;
						
					case 3:
						if(!Constants.LONG_CLICK[3])
							AppStore.resetAdapter();
						break;					
					}
				}
			}
	}
	
	/**
	 * 
	 * @param i tells the kind of list view....
	 * @param panel current pager item....
	 */
	private void change_list_type(int i , int panel) {
		// TODO Auto-generated method stub
		
		//saving the settings....
		Constants.LIST_TYPE = i;
		prefs_editor.putInt("LIST_TYPE", Constants.LIST_TYPE);
		prefs_editor.commit();
		
		//setting list view of current pager item....
		switch(panel){
		case 0:
			if(!Constants.LONG_CLICK[0])
				FileGallery.resetAdapter();
			break;
			
		case 1:
			if(!Constants.LONG_CLICK[1])
				RootPanel.resetAdapter();
			break;
		
		case 2:
			if(!Constants.LONG_CLICK[2])
				SdCardPanel.resetAdapter();
			break;
			
		case 3:
			if(!Constants.LONG_CLICK[3])
				AppStore.resetAdapter();
			break;
			
		}
		
		//now setting the list view for other panels which are not visible....
		for(int j = 0 ; j<4 ; ++j){
			if(j != panel){
				switch(j){
				case 0:
					if(!Constants.LONG_CLICK[0])
						FileGallery.resetAdapter();
					break;
					
				case 1:
					if(!Constants.LONG_CLICK[1])
						RootPanel.resetAdapter();
					break;
				
				case 2:
					if(!Constants.LONG_CLICK[2])
						SdCardPanel.resetAdapter();
					break;
					
				case 3:
					if(!Constants.LONG_CLICK[3])
						AppStore.resetAdapter();
					break;					
				}
			}
		}
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
		
		navIcon = (ImageView) toolbar.findViewById(R.id.open_drawer_menu);
		navIcon.setBackgroundColor(Constants.COLOR_STYLE);
		navIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
		navIcon = (ImageView) top_toolbar.findViewById(R.id.open_drawer_menu);
		navIcon.setBackgroundColor(Constants.COLOR_STYLE);
		navIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
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
			
		//restoring list type setting....
		if(Constants.LIST_TYPE == 2)
			bottom_options.getMenu().findItem(R.id.detail_ls).setChecked(true);
		else
			bottom_options.getMenu().findItem(R.id.simple_ls).setChecked(true);
		
		//restoring sort setting....
		switch(Constants.SORT_TYPE){
		case 1:
			bottom_options.getMenu().findItem(R.id.sort_az).setChecked(true);
			break;
		case 2:
			bottom_options.getMenu().findItem(R.id.sort_za).setChecked(true);
			break;
		case 3:
			bottom_options.getMenu().findItem(R.id.size_smaller).setChecked(true);
			break;
		case 4:
			bottom_options.getMenu().findItem(R.id.size_bigger).setChecked(true);
			break;
		case 5:
			bottom_options.getMenu().findItem(R.id.date_new).setChecked(true);
			break;
		case 6:
			bottom_options.getMenu().findItem(R.id.date_old).setChecked(true);
			break;
		}
		
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
					action_bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
				
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
				Constants.LONG_CLICK[panel] = true;
				invalidateOptionsMenu();
				
			}else if(action.equalsIgnoreCase("inflate_normal_menu")){
				Constants.LONG_CLICK[panel] = false;
				//long click got disabled,restore default screen.....
				if(panel == 2){
					SdCardPanel.clear_selected_items();
					
				}else if(panel == 1){
					RootPanel.clear_selected_items();
					
				}else if(panel == 3){
					AppStore.clear_selected_items();
				
				}else if(panel == 0){
					FileGallery.clear_selected_items();
				}
				
				invalidateOptionsMenu();
				pager.setCurrentItem(panel);
								
			}else if(action.equalsIgnoreCase("update_action_bar_long_click")){
				//update the action bar as per no. of selected items....
				if(Constants.LONG_CLICK[panel]){
					
					//setting up action bar when an item is long clicked....
					navIcon = (ImageView) toolbar.findViewById(R.id.open_drawer_menu);
					navIcon.setBackgroundColor(Constants.COLOR_STYLE);
					navIcon.setImageDrawable(getResources().getDrawable(R.drawable.long_click_check));
				
					navIcon = (ImageView) top_toolbar.findViewById(R.id.open_drawer_menu);
					navIcon.setBackgroundColor(Constants.COLOR_STYLE);
					navIcon.setImageDrawable(getResources().getDrawable(R.drawable.long_click_check));
				
					switch(panel){
					case 0:
						no_selection = (TextView) top_toolbar.findViewById(R.id.no_selection);
						no_selection.setText(FileGallery.counter + " selected");
						no_selection = (TextView) toolbar.findViewById(R.id.no_selection);
						no_selection.setText(FileGallery.counter + " selected");
						
						no_folder = (TextView) top_toolbar.findViewById(R.id.total_space);
						no_folder.setText(FileGallery.folder_count + " folder");
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(FileGallery.folder_count + " folder");
						
						no_files = (TextView) top_toolbar.findViewById(R.id.avail_space);
						no_files.setText(FileGallery.file_count + " file");
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(FileGallery.file_count + " file");
						
						break;
						
					case 1:
						no_selection = (TextView) top_toolbar.findViewById(R.id.no_selection);
						no_selection.setText(RootPanel.counter + " selected");
						no_selection = (TextView) toolbar.findViewById(R.id.no_selection);
						no_selection.setText(RootPanel.counter + " selected");
						
						no_folder = (TextView) top_toolbar.findViewById(R.id.total_space);
						no_folder.setText(RootPanel.folder_count + " folder");
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(RootPanel.folder_count + " folder");
						
						no_files = (TextView) top_toolbar.findViewById(R.id.avail_space);
						no_files.setText(RootPanel.file_count + " file");
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(RootPanel.file_count + " file");
						
						break;
						
					case 2:
						no_selection = (TextView) top_toolbar.findViewById(R.id.no_selection);
						no_selection.setText(SdCardPanel.counter + " selected");
						no_selection = (TextView) toolbar.findViewById(R.id.no_selection);
						no_selection.setText(SdCardPanel.counter + " selected");
						
						no_folder = (TextView) top_toolbar.findViewById(R.id.total_space);
						no_folder.setText(SdCardPanel.folder_count + " folder");
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(SdCardPanel.folder_count + " folder");
						
						no_files = (TextView) top_toolbar.findViewById(R.id.avail_space);
						no_files.setText(SdCardPanel.file_count + " file");
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(SdCardPanel.file_count + " file");
						break;
						
					case 3:
						no_selection = (TextView) top_toolbar.findViewById(R.id.no_selection);
						no_selection.setText(AppStore.counter + " selected");
						no_selection = (TextView) toolbar.findViewById(R.id.no_selection);
						no_selection.setText(AppStore.counter + " selected");
							
						no_folder = (TextView) top_toolbar.findViewById(R.id.total_space);
						no_folder.setText(total);
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(total);
						
						no_files = (TextView) top_toolbar.findViewById(R.id.avail_space);
						no_files.setText(avail);
						no_folder = (TextView) toolbar.findViewById(R.id.total_space);
						no_folder.setText(total);
						
					}
				}
			}
			
			//list view animation is changed from settings....
			//so notify the fragments about this event....
			else if(action.equalsIgnoreCase("list_view_anim_changed")){
				FileGallery.change_list_anim();
				RootPanel.change_list_anim();
				SdCardPanel.change_list_anim();
				AppStore.change_list_anim();
			}			
			
			else if(action.equalsIgnoreCase("FQ_DELETE")){
				Constants.LONG_CLICK[panel] = false;
				invalidateOptionsMenu();
			}			
		}		
	}
	
	/**
	 * this function is called in onResume function....
	 */
	private void register_receiver(){
		broadcasts = new Receive_Broadcasts();
		IntentFilter filter = new IntentFilter("inflate_long_click_menu");
		filter.addAction("inflate_normal_menu");
		filter.addAction("update_action_bar_long_click");
		filter.addAction("list_view_anim_changed");
		filter.addAction("FQ_DELETE");
		this.registerReceiver(broadcasts, filter);
	}
	
	private void init_drawer_menu() {
		// TODO Auto-generated method stub
		ExpandableListView drLs = (ExpandableListView) drawer.findViewById(R.id.drawer_menu_list);
		drLs.setAdapter(new drAdpt());		
		drLs.setSelector(R.drawable.while_list_selector_hd);
		

		drLs.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case 0:
					Intent intent = new Intent(FileQuestHD.this, GraphAnalysis.class);
					startActivity(intent);
					break;
		
				case 5:
					//checks the new update for file quest....
					update_checker();
					break;
				}
				return false;
			}
		});
		
		drLs.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long arg4) {
				// TODO Auto-generated method stub
				String type = null;
				int tweakwhat = 0;
				switch(arg3){
				case 0:
					tweakwhat = 0;
					type = " favorite ";
					break;
					
				case 1:
					tweakwhat = 1;
					type = " music ";
					break;
					
				case 2:
					tweakwhat = 2;
					type = " apps ";
					break;
						
				case 3:
					tweakwhat = 3;
					type = " photos ";
					break;
					
				case 4:
					tweakwhat = 4;
					type = " videos ";
					break;
					
				case 5:
					tweakwhat = 5;
					type = " documents ";
					break;
					
				case 6:
					tweakwhat = 6;
					type = " archives ";
					break;
					
				case 7:
					tweakwhat = 7;
					type = " unknown ";
					break;					
				}
				
				new ConfirmTweakTask(FileQuestHD.this, arg2, type , tweakwhat);
				
				return false;
			}
		});
	}
	
	/**
	 * adapter for drawer....
	 * @author anurag
	 *
	 */
	class drAdpt extends BaseExpandableListAdapter{

		LayoutInflater inf;
		String[] drawer_ls = getResources().getStringArray(R.array.drawer_ls);
		String[] cleaner = getResources().getStringArray(R.array.cleaner);
		String[] mover = getResources().getStringArray(R.array.mover);
		String[] ziper = getResources().getStringArray(R.array.ziper);
		
		public drAdpt() {
			// TODO Auto-generated constructor stub
			inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
				
		class hold{
			ImageView img;
			TextView nam;
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
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent){
			// TODO Auto-generated method stub
			View view = inf.inflate(R.layout.row_list_2, parent , false);
			hold h = new hold();
			h.img = (ImageView) view.findViewById(R.id.iconImage2);
			h.nam = (TextView) view.findViewById(R.id.directoryName2);
			view.setPadding(30, 0, 0, 0);
			
			view.setTag(h);
			
			switch(groupPosition){
			case 1:
				h.nam.setText(cleaner[childPosition]);
				break;
			case 2:
				h.nam.setText(mover[childPosition]);
				break;
			case 3:
				h.nam.setText(ziper[childPosition]);
				break;
			}
			
			return view;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			switch(arg0){
			case 1:
				return cleaner.length;
				
			case 2:
				return mover.length;
			case 3:
				return ziper.length;
			}
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
			return drawer_ls.length;
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent){
			// TODO Auto-generated method stub
			
			View view = inf.inflate(R.layout.row_list_2, parent , false);
			hold h = new hold();
			h.img = (ImageView) view.findViewById(R.id.iconImage2);
			h.nam = (TextView) view.findViewById(R.id.directoryName2);
			view.setTag(h);
			switch(groupPosition){
			case 0:
				h.img.setBackgroundResource(R.drawable.graph_analysis_hd);
				break;
				
			case 1:
				h.img.setBackgroundResource(R.drawable.file_cleaner_hd);
				break;
				
			case 2:
				h.img.setBackgroundResource(R.drawable.file_mover_hd);
				break;
			
			case 3:
				h.img.setBackgroundResource(R.drawable.file_zipper_hd);
				break;
				
			case 4:
				h.img.setBackgroundResource(R.drawable.file_shield_hd);
				break;
				
			case 5:
				h.img.setBackgroundResource(R.drawable.update_check_hd);
			}
			h.nam.setText(drawer_ls[groupPosition]);
			return view;
			
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
	
	/**
	 * this function checks for update for File Quest
	 * and makes a notification to download link
	 * in playstore.... 
	 */
	private void update_checker() {
		// TODO Auto-generated method stub
		Toast.makeText(FileQuestHD.this, R.string.checking_update, Toast.LENGTH_SHORT).show();
		
		final Handler hand = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 1://update available....
					{
						NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(FileQuestHD.this);
						mBuilder.setSmallIcon(R.drawable.file_quest_icon);																					
						mBuilder.setContentTitle(getString(R.string.app_name));
						mBuilder.setContentText(getString(R.string.update_avail));
						
						mBuilder.setTicker(getString(R.string.update_avail));
						
						Toast.makeText(FileQuestHD.this, R.string.update_avail, Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("market://details?id=org.anurag.file.quest"));
						
						
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
											
						PendingIntent pendint = PendingIntent.getActivity(FileQuestHD.this, 900, intent, 0);
						mBuilder.setContentIntent(pendint);
				
						mBuilder.setAutoCancel(true);
						
						NotificationManager notimgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						notimgr.notify(1, mBuilder.build());
					}
					break;
				case 2://no connectivity....
					Toast.makeText(FileQuestHD.this, R.string.nointernet, Toast.LENGTH_SHORT).show();
					break;
				case 3:
					//failed to check for update....
					Toast.makeText(FileQuestHD.this, 
							R.string.failed_to_check_for_update, Toast.LENGTH_SHORT).show();
				}
			}			
		};
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo info = cm.getActiveNetworkInfo();
					if(!info.isConnected()){
						hand.sendEmptyMessage(2);
						return;
					}	
					Scanner scan = new Scanner(new URL("https://www.dropbox.com/s/x1gp7a6ozdvg81g/FQ_UPDATE.txt?dl=1").openStream());
					String update = scan.next();
					if(!update.equalsIgnoreCase(getString(R.string.version)))
						hand.sendEmptyMessage(1);
					scan.close();
				}catch(Exception e){
					hand.sendEmptyMessage(3);
				}
			}
		});
		th.start();
	}
	
	/**
	 * this function finds the device information and sets in drawer menu....
	 */
	private void init_with_device_id(){
		TextView devId = (TextView)findViewById(R.id.dev_id);
		String dev = Build.MODEL;
		String man = Build.MANUFACTURER;
		if(dev.length() == 0 || dev == null)
			dev = getString(R.string.unknown);
		else{
			if(!dev.contains(man))
				dev = man + " " + dev;
			char a = dev.charAt(0);
			if(!Character.isUpperCase(a)){
				dev = Character.toUpperCase(a) + dev.substring(1);
			}
		}		
		devId.setText(dev);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.open_drawer_menu:
			if(!Constants.LONG_CLICK[pager.getCurrentItem()])
				drawer.openDrawer(Gravity.START);
			break;
		}
	}
	
	/**
	 * this function loads the sd card space and shows it in action
	 * bar.... 
	 */
	private void load_sd_space(){
		new AsyncTask<Void, Void , Void>() {
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				avail = getString(R.string.free);
				total = getString(R.string.total);
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				TextView av = (TextView) toolbar.findViewById(R.id.avail_space);
				TextView to = (TextView) toolbar.findViewById(R.id.total_space);
				av.setText(avail);
				to.setText(total);
				
				av = (TextView) top_toolbar.findViewById(R.id.avail_space);
				to = (TextView) top_toolbar.findViewById(R.id.total_space);
				av.setText(avail);
				to.setText(total);
			
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				long av = Environment.getExternalStorageDirectory().getFreeSpace();
				long to = Environment.getExternalStorageDirectory().getTotalSpace();
				
				if(av > Constants.GB)
					avail = avail + " " + String.format(Constants.GB_STR, (double)av/Constants.GB);
				else if(av > Constants.MB)
					avail = avail + " " + String.format(Constants.MB_STR, (double)av/Constants.MB);
				else if(av > 1024)
					avail = avail + " " + String.format(Constants.KB_STR, (double)av/1024);
				else 
					avail = avail + " " + String.format(Constants.BYT_STR, (double)av);
				
				if(to > Constants.GB)
					total = total + " " + String.format(Constants.GB_STR, (double)to/Constants.GB);
				else if(to > Constants.MB)
					total = total + " " + String.format(Constants.MB_STR, (double)to/Constants.MB);
				else if(to > 1024)
					total = total + " " + String.format(Constants.KB_STR, (double)to/1024);
				else 
					total = total + " " + String.format(Constants.BYT_STR, (double)to);
								
				return null;
			}
		}.execute();
	}	
}