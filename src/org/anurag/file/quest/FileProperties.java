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

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;

/**
 * 
 * @author Anurag....
 *
 */
public class FileProperties extends Activity{

	private PieChart pChart;
	private File file;
	private long size;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_properties);
		
		getActionBar().setBackgroundDrawable(new ColorDrawable(0x00000000));
		getActionBar().setTitle(R.string.properties);
		
		file = new File(getIntent().getStringExtra("path"));
		size = 0;
		pChart = (PieChart)findViewById(R.id.chart);
		// change the color of the center-hole
        pChart.setHoleColor(Color.rgb(235, 235, 235));

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

        pChart.setCenterText(getResources().getString(R.string.properties));
        
        setPieData();
        pChart.animateXY(1500, 1500);
        Legend l = pChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
		
	}
	
	private void setPieData() {
		// TODO Auto-generated method stub
		ArrayList<Entry> yVal = new ArrayList<Entry>();
		yVal.add(new BarEntry(Environment.getExternalStorageDirectory().getTotalSpace(), 0));
		if(file.isFile())
			yVal.add(new BarEntry(file.length(), 1));
		else if(file.isDirectory())
			yVal.add(new BarEntry(calculateLength(), 1));
		
		ArrayList<String> xVal = new ArrayList<String>();
		xVal.add(getResources().getString(R.string.sd));
		xVal.add(file.getName());
		
		PieDataSet set = new PieDataSet(yVal, "");
		set.setSliceSpace(4f);
		
		// add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        
        colors.add(ColorTemplate.getHoloBlue());
        
        set.setColors(colors);
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
		getFileSize(file);
		return size;
	}
	
	/**
	 * 
	 * @param file
	 */
	private void getFileSize(File file){
		if(file.isFile())
			size = file.length();
		else if(file.isDirectory() && file.listFiles().length !=0){
			File[] a = file.listFiles();
			for(int i = 0 ; i<a.length ; ++i){
				if(a[i].isFile())
					size = size + a[i].length();
				else
					getFileSize(a[i]);
			}
		}
	}

	
}
