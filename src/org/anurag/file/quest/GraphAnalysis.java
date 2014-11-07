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

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;

public class GraphAnalysis extends Activity{
	
	private BarChart mChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_analysis);
		
		mChart = (BarChart)findViewById(R.id.chart1);
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
		set.setColors(ColorTemplate.VORDIPLOM_COLORS);
		
		ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
		sets.add(set);
		
		BarData data = new BarData(xVal , sets);
		mChart.setData(data);
		mChart.invalidate();
	}

}
