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

import java.util.ArrayList;

import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.extra.libs.PagerSlidingTabStrip;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;

public class GraphAnalysis extends ActionBarActivity{
	
	private PieChart pChart;
	private BarChart mChart;
	private android.support.v7.app.ActionBar actionBar;
	private ViewPager pager; 
	private PagerSlidingTabStrip strip;
	private PagerFragmentAdapter adpt;
	
	//color for bar graph and pie chart....
	private int color[] = {
							Color.rgb(102, 102, 102),
							Color.rgb(81, 171, 56),
							Color.rgb(199, 75, 70),
							Color.rgb(255, 93, 61),
							Color.rgb(63, 159, 224),
							Color.rgb(81, 97, 188),
							Color.rgb(42, 109, 130)
						  };
	
	private Toolbar action_bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_analysis);
		
		action_bar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(action_bar);
		actionBar = getSupportActionBar();
		String title = getString(R.string.graph_stats);
		actionBar.setTitle("   "+title);
		actionBar.setIcon(R.drawable.graph_analysis_hd);
		actionBar.setBackgroundDrawable(new ColorDrawable(Constants.COLOR_STYLE));
		
		
		pager = (ViewPager)findViewById(R.id.graph_pager);
		strip = (PagerSlidingTabStrip)findViewById(R.id.graph_ind);
		adpt = new PagerFragmentAdapter(getSupportFragmentManager());
		pager.setAdapter(adpt);
		strip.setViewPager(pager);
		pager.setBackgroundColor(Constants.COLOR_STYLE);
		strip.setBackgroundColor(Constants.COLOR_STYLE);
		
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
		SystemBarTintManager tint = new SystemBarTintManager(GraphAnalysis.this);
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
		main.setBackgroundColor(Constants.COLOR_STYLE);
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
	 * @author Anurag....
	 *
	 */
	private class PagerFragmentAdapter extends FragmentPagerAdapter{
		String[] titles = getResources().getStringArray(R.array.graph_titles);
		
		public PagerFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch(arg0){
			case 0:
					return new BarFragment();
			default:
					return new PieFragment();
			}
		}		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titles[position];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titles.length;
		}		
	}

	/**
	 * 
	 * @author Anurag....
	 *
	 */
	private class BarFragment extends Fragment{ 
		public BarFragment() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.bar_graph_analysis_fragment, container , false);
			return view;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			mChart = (BarChart)view.findViewById(R.id.chart1);
			mChart.setDrawYValues(false);
			
			mChart.setUnit(" MB");
	        mChart.setDescription("");
	        
	        mChart.setDrawYValues(true);

	        // if more than 60 entries are displayed in the chart, no values will be
	        // drawn
	        mChart.setMaxVisibleValueCount(60);

	        // disable 3D
	        mChart.set3DEnabled(false);
	        // scaling can now only be done on x- and y-axis separately
	        mChart.setPinchZoom(false);

	        mChart.setDrawBarShadow(false);
	        
	        mChart.setDrawVerticalGrid(false);
	        mChart.setDrawHorizontalGrid(false);
	        mChart.setDrawGridBackground(false);
	        mChart.setEnabled(false);
	        XLabels xLabels = mChart.getXLabels();
	        xLabels.setPosition(XLabelPosition.BOTTOM);
	        xLabels.setCenterXLabelText(true);
	        xLabels.setSpaceBetweenLabels(0);

	        mChart.setDrawYLabels(false);
	        mChart.setDrawLegend(false);
	        // add a nice and smooth animation
	        setGraphData();
	        
	        mChart.animateY(2500);
		}		
	}
	
	private class PieFragment extends Fragment{
		public PieFragment() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			return inflater.inflate(R.layout.pie_chart_analysis, container , false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			pChart = (PieChart)view.findViewById(R.id.chart2);
			
			// change the color of the center-hole
	        pChart.setHoleColor(getResources().getColor(R.color.semi_white30));

	        pChart.setHoleRadius(40f);
	        pChart.setDescription("");

	        pChart.setDrawYValues(true);
	        pChart.setDrawCenterText(true);

	        pChart.setDrawHoleEnabled(true);

	        pChart.setRotationAngle(0);

	        // draws the corresponding description value into the slice
	        pChart.setDrawXValues(true);

	        // enable rotation of the chart by touch
	        pChart.setRotationEnabled(true);

	        // display percentage values
	        pChart.setUsePercentValues(true);
	        
	        //pChart.setTouchEnabled(false);

	        pChart.setCenterText("");
	        
	        setPieData();
	        pChart.animateXY(1500, 1500);
	        Legend l = pChart.getLegend();
	        l.setPosition(LegendPosition.RIGHT_OF_CHART);
	        l.setXEntrySpace(7f);
	        l.setYEntrySpace(5f);
		}
		
	}
	
	/**
	 * function setting the bar graph data....
	 */
	private void setGraphData() {
		// TODO Auto-generated method stub
		ArrayList<BarEntry> yVal = new ArrayList<BarEntry>();
		yVal.add(new BarEntry(Utils.musicsize, 0));
		yVal.add(new BarEntry(Utils.apksize, 1));
		yVal.add(new BarEntry(Utils.imgsize , 2));
		yVal.add(new BarEntry(Utils.vidsize, 3));
		yVal.add(new BarEntry(Utils.docsize, 4));
		yVal.add(new BarEntry(Utils.zipsize, 5));
		yVal.add(new BarEntry(Utils.missize, 6));
		
		ArrayList<String> xVal = new ArrayList<String>();
		xVal.add("Music");
		xVal.add("Apps");
		xVal.add("Photos");
		xVal.add("Videos");
		xVal.add("Docs");
		xVal.add("Zips");
		xVal.add("Unknown");
		
		BarDataSet set = new BarDataSet(yVal, "Data Set");
		set.setColors(color);
		
		ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
		sets.add(set);
		
		BarData data = new BarData(xVal , sets);
		mChart.setData(data);
		mChart.invalidate();
	}

	/**
	 * 
	 */
	public void setPieData() {
		// TODO Auto-generated method stub
		ArrayList<Entry> yVal = new ArrayList<Entry>();
		yVal.add(new Entry(Utils.musicsize, 0));
		yVal.add(new Entry(Utils.apksize, 1));
		yVal.add(new Entry(Utils.imgsize , 2));
		yVal.add(new Entry(Utils.vidsize, 3));
		yVal.add(new Entry(Utils.docsize, 4));
		yVal.add(new Entry(Utils.zipsize, 5));
		yVal.add(new Entry(Utils.missize, 6));
		
		ArrayList<String> xVal = new ArrayList<String>();
		xVal.add("Music");
		xVal.add("Apps");
		xVal.add("Photos");
		xVal.add("Videos");
		xVal.add("Docs");
		xVal.add("Zips");
		xVal.add("Unknown");
		
		PieDataSet set = new PieDataSet(yVal, "");
		set.setSliceSpace(4f);
		
		set.setColors(color);
        PieData data = new PieData(xVal, set);
        pChart.setData(data);
        pChart.highlightValues(null);
        pChart.invalidate();
	}
}
