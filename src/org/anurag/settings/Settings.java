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
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;





@SuppressLint("CommitPrefEdits")
public class Settings extends Activity implements View.OnClickListener{

	
	SharedPreferences settingsPrefs;
	SharedPreferences.Editor edit;
	
	
	public static ImageView applied;
	public static boolean settingsChanged;
	public static boolean pagerAnimSettingsChanged;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsChanged = false;
		setContentView(R.layout.settings_ui);
		settingsPrefs = getSharedPreferences("SETTINGS", 0);
		edit = settingsPrefs.edit();		
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

}
