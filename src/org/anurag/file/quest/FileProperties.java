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
import java.util.ArrayList;

import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.extra.libs.PagerSlidingTabStrip;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;

/**
 * 
 * @author Anurag....
 *
 */

/*
 * TODO 
 * calculate folder size of folders that needed root access....
 */
@SuppressLint("HandlerLeak")
public class FileProperties extends ActionBarActivity{

	private ArrayList<Item> lss;
	private PageAdpt adapter;
	private ViewPager pager;
	private PagerSlidingTabStrip indicator;
	private PieChart pChart;
	private File file;
	private long size;
	
	private Toolbar toolbar;
	
	//color for pie chart....
	private int color[] = {
				Color.rgb(81, 171, 56),
				Color.rgb(255, 93, 61)
			};

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 1){
				pChart.animateXY(1500, 1500);
				return;
			}
			setPieData();
		//	pChart.animateXY(1500, 1500);
	        Legend l = pChart.getLegend();
	        l.setPosition(LegendPosition.RIGHT_OF_CHART);
	        l.setXEntrySpace(7f);
	        l.setYEntrySpace(5f);
		}
		
	};
	
	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			calculateLength();
			handler.sendEmptyMessage(1);
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stubidPage
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_analysis);
		
		indicator = (PagerSlidingTabStrip) findViewById(R.id.graph_ind);
		pager = (ViewPager) findViewById(R.id.graph_pager);
		toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setIcon(R.drawable.graph_analysis_hd);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Constants.COLOR_STYLE));
		getSupportActionBar().setTitle(R.string.properties);
		
		adapter = new PageAdpt(getSupportFragmentManager());
		
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.file_properties, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		init_system_ui();
	}
	
	private class PageAdpt extends FragmentStatePagerAdapter{

		String[] titles = {"PIE CHART" , "DETAILS"};
		
		public PageAdpt(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch(arg0){
			case 0:
				return new pie_chart();
			case 1:
				return new details();
			}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titles[position];
		}		
	}
	
	private class pie_chart extends Fragment{
		
		public pie_chart() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			return inflater.inflate(R.layout.bar_graph_analysis_fragment, container , false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onViewCreated(view, savedInstanceState);
		}	
	}
	

	private class details extends Fragment{
		
		public details() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			return inflater.inflate(R.layout.bar_graph_analysis_fragment, container , false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onViewCreated(view, savedInstanceState);
		}	
	}
	
	
	/**
	 * restyles the system UI like status bar or navigation bar if present....
	 */
	private void init_system_ui() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemBarTintManager tint = new SystemBarTintManager(FileProperties.this);
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
		indicator.setBackgroundColor(Constants.COLOR_STYLE);
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
	 * this function sets the pie chart data....
	 */
	private void setPieData() {
		// TODO Auto-generated method stub
		ArrayList<Entry> yVal = new ArrayList<Entry>();
		yVal.add(new BarEntry(Environment.getExternalStorageDirectory().getTotalSpace(), 0));
		if(file.isFile()){
			//if(type.equalsIgnoreCase("MUSIC")){
				//if type is music then load the album art....
				MediaMetadataRetriever ret = new MediaMetadataRetriever();
				ret.setDataSource(file.getPath());
				byte[] data = ret.getEmbeddedPicture();
				Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length);
				//if(map != null)
					//prp_img.setImageBitmap(map);
			//}
			yVal.add(new BarEntry(file.length(), 1));
		//	prp_size.setText(getString(R.string.filesize) + " - " + RootManager.getSize(file));
		}	
		else if(file.isDirectory()){
			yVal.add(new BarEntry(size, 1));
			//prp_size.setText(Utils.size(size));
		}	
		
		ArrayList<String> xVal = new ArrayList<String>();
		xVal.add(getResources().getString(R.string.sd));
		xVal.add(file.getName());
		
		PieDataSet set = new PieDataSet(yVal, "");
		set.setSliceSpace(4f);
		
        set.setColors(color);
        PieData data = new PieData(xVal, set);
        pChart.setData(data);
        pChart.highlightValues(null);
        pChart.invalidate();
	}
	
	/**
	 * 
	 * @return
	 */
	private long calculateLength() {
		// TODO Auto-generated method stub
		try{
			getFileSize(file);
		}catch(Exception e){
			return 0;
		}
		return size;
	}
	
	/**
	 * 
	 * @param file
	 */
	private void getFileSize(File file){
		if(file.isFile()){
			size = file.length();
			handler.sendEmptyMessage(0);
		}	
		else if(file.isDirectory() && file.listFiles().length !=0){
			File[] a = file.listFiles();
			for(int i = 0 ; i<a.length ; ++i){
				if(a[i].isFile()){
					size = size + a[i].length();
					handler.sendEmptyMessage(0);
				}	
				else
					getFileSize(a[i]);
			}
		}
	}
	
	

	/**
	 * 
	 * @return the list of items selected by long press....
	 */
	private ArrayList<Item> getLs(){
		lss = new ArrayList<>();
		switch(FileQuestHD.getCurrentItem()){
		case 0:
			if(FileGallery.ITEMS != null){
				int len = FileGallery.lists.size();
				for(int i = 0 ; i < len ; ++i)
					if(FileGallery.ITEMS[i] == 1)
						lss.add(FileGallery.lists.get(FileGallery.keys.get(""+i)));
			}
			break;
			
		case 1:
			if(RootPanel.ITEMS != null){
				ArrayList<Item> ls = RootPanel.get_selected_items();
				int len = ls.size();
				for(int i = 0 ; i < len ; ++i)
					lss.add(ls.get(i));
			}
			break;
			
		case 2:
			if(SdCardPanel.ITEMS != null){
				ArrayList<Item> ls = SdCardPanel.get_selected_items();
				int len = ls.size();
				for(int i = 0 ; i < len ; ++i)
					lss.add(ls.get(i));
			}
			break;
		}
		
		return lss;
	}
	
}
