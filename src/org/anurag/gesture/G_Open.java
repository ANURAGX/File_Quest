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

package org.anurag.gesture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import org.anurag.file.quest.SystemBarTintManager;
import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class G_Open extends ActionBarActivity{
	
	
	private GestureOverlayView gesture;
	private GestureLibrary library;
	private Toolbar bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_gesture);
		
		String title = getResources().getString(R.string.drawgesture);
		bar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(bar);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Constants.COLOR_STYLE));
		getSupportActionBar().setTitle("  " + title);
		getSupportActionBar().setIcon(R.drawable.gesture);
		
		gesture = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		
		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/File Quest");
		if(!file.exists())
			file.mkdirs();
		file = new File(file, ".gesture");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		library = GestureLibraries.fromFile(file);
		library.load();
		
		LinearLayout btns = (LinearLayout) findViewById(R.id.btns);
		btns.setVisibility(View.GONE);
		
		gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
			@Override
			public void onGesturePerformed(GestureOverlayView arg0, Gesture arg1) {
				// TODO Auto-generated method stub
				ArrayList<Prediction> list = library.recognize(arg1);
				if(list.size()>0){
					if(list.get(0).score > 0.9f){
						String name = list.get(0).name;
						Intent intent = new Intent("FQ_G_OPEN");
						intent.putExtra("gesture_path", name);
						setResult(RESULT_OK, intent);
						Toast.makeText(G_Open.this, R.string.opening, Toast.LENGTH_SHORT).show();
						G_Open.this.finish();
					}					
				}
			}
		});
	}

	@Override
	public void onResume(){
		super.onResume();
		init_system_ui();
	}
	
	/**
	 * restyles the system UI like status bar or navigation bar if present....
	 */
	private void init_system_ui() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemBarTintManager tint = new SystemBarTintManager(G_Open.this);
		tint.setStatusBarTintEnabled(true);
		tint.setStatusBarTintColor(Constants.COLOR_STYLE);
		SystemBarConfig conf = tint.getConfig();
		boolean hasNavBar = conf.hasNavigtionBar();
		if(hasNavBar){
			tint.setNavigationBarTintEnabled(true);
			tint.setNavigationBarTintColor(Constants.COLOR_STYLE);
		}
		LinearLayout main = (LinearLayout) findViewById(R.id.main);
		main.setBackgroundColor(Constants.COLOR_STYLE);
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
