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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;


public class FileQuestHD extends FragmentActivity {

	private PagerSlidingTabStrip indicator;
	private ActionBar actionBar;
	private ViewPager pager;
	private PagerAdapters adapters;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.fq_ui_hd);
		findViewIds();
		styleActionBar(0xFFC74B46);
		pager.setAdapter(adapters);
		indicator.setViewPager(pager);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		
		indicator.setBackgroundColor(color);
		LinearLayout main = (LinearLayout) findViewById(R.id.main_ui);
		main.setBackgroundColor(color);
	}
	
	/**
	 * this function finds the ids of all view used....
	 */
	private void findViewIds() {
		// TODO Auto-generated method stub
		indicator = (PagerSlidingTabStrip) findViewById(R.id.pagerSlidingTabStrip1);
		pager = (ViewPager) findViewById(R.id.view);
		adapters = new PagerAdapters(getSupportFragmentManager());
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}	
}
