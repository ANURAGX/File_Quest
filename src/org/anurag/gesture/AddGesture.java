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
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.SystemBarTintManager;
import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author anurag
 *
 */
public class AddGesture extends ActionBarActivity{
	
	private GestureOverlayView gesture;
	private Gesture pattern;
	private GestureLibrary library;
	private Toolbar bar;
	private boolean saved;
	private TextView fName;
	private ArrayList<Item> gest_list;
	int i;
	int len;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		saved = false;
		setContentView(R.layout.create_gesture);
		i = 0;
		String title = getResources().getString(R.string.create_gesture);
		
		fName = (TextView) findViewById(R.id.fName);
		
		gesture = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		bar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(bar);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Constants.COLOR_STYLE));
		getSupportActionBar().setTitle("  " + title);
		getSupportActionBar().setIcon(R.drawable.gesture);
		
		gest_list = new ArrayList<>();
		gest_list = getLs();
		len = gest_list.size();
		
		fName.setText(gest_list.get(i).getName().toUpperCase());
		
		final Button save = (Button) findViewById(R.id.done);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(!saved){
					Toast.makeText(AddGesture.this, R.string.invalid_gesture, Toast.LENGTH_SHORT).show();
					return;
				}
				
				File file = new File(Environment.getExternalStorageDirectory().getPath()+"/File Quest");
				if(!file.exists())
					file.mkdirs();
				file = new File(file,".gesture");
				if(!file.exists())
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				library = GestureLibraries.fromFile(file.getAbsolutePath());
				library.load();
				library.addGesture(gest_list.get(i++).getPath(), pattern);
				library.save();
				if(len > i){
					fName.setText(gest_list.get(i).getName().toUpperCase());
					gesture.clear(true);
					Toast.makeText(AddGesture.this, getResources().getString(R.string.gesturesaved), Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(AddGesture.this, getResources().getString(R.string.gesturesaved), Toast.LENGTH_SHORT).show();
					AddGesture.this.finish();
				}
			}
		}); 
		
		Button discard = (Button)findViewById(R.id.discard);
		discard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AddGesture.this.finish();			
			}
		});
		
		gesture.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
			@Override
			public void onGestureStarted(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				//save.setEnabled(false);
				pattern = null;
			}
			
			@Override
			public void onGestureEnded(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				pattern = arg0.getGesture();
				if(pattern.getLength()<120.f){
					saved = false;
				}else
					saved = (true);
			}
			
			@Override
			public void onGestureCancelled(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
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
		SystemBarTintManager tint = new SystemBarTintManager(AddGesture.this);
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
	
	/**
	 * 
	 * @return
	 */
	private ArrayList<Item> getLs(){
		if(FileGallery.ITEMS != null){
			int len = FileGallery.lists.size();
			for(int i = 0 ; i < len ; ++i)
				if(FileGallery.ITEMS[i] == 1)
					gest_list.add(FileGallery.lists.get(FileGallery.keys.get(""+i)));
		}
		
		if(RootPanel.ITEMS != null){
			int len = RootPanel.get_selected_items().size();
			for(int i = 0 ; i < len ; ++i)
				gest_list.add(RootPanel.get_selected_items().get(i));
		}
		
		if(SdCardPanel.ITEMS != null){
			int len = SdCardPanel.get_selected_items().size();
			for(int i = 0 ; i < len ; ++i)
				gest_list.add(SdCardPanel.get_selected_items().get(i));
		}
		
		return gest_list;
	}
	
}
