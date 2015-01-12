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
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
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
public class FileProperties extends Activity{

	private PieChart pChart;
	private File file;
	private long size;
	private String type;
	private ImageView prp_img;
	private TextView prp_name;
	private TextView prp_path;
	private TextView prp_type;
	private TextView prp_size;
	
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_properties);
		
		getActionBar().setIcon(R.drawable.ic_launcher_stats);
		getActionBar().setBackgroundDrawable(new ColorDrawable(0x00000000));
		getActionBar().setTitle(R.string.properties);
		
    	file = new File(getIntent().getStringExtra("path"));
		size = 0;
		pChart = (PieChart)findViewById(R.id.chart);
		prp_img = (ImageView)findViewById(R.id.prop_img);
		prp_name = (TextView)findViewById(R.id.prp_name);
		prp_path = (TextView)findViewById(R.id.prp_path);
		prp_type = (TextView)findViewById(R.id.prp_type);
		prp_size = (TextView)findViewById(R.id.prp_size);
		
		
		prp_img.setImageDrawable(buildIcon(file));
		prp_type.setText((getString(R.string.type) + " - " + type).toUpperCase(Locale.ENGLISH));
		prp_path.setText(getString(R.string.filepath) + " - "+file.getAbsolutePath());
		prp_name.setText(getString(R.string.filename) + " - "+file.getName());
	
		
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
        if(file.isFile())
        	pChart.animateXY(1500, 1500);
        Legend l = pChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        
        if(file.isDirectory())
        	thread.start();
	}
	/**
	 * this function sets the pie chart data....
	 */
	private void setPieData() {
		// TODO Auto-generated method stub
		ArrayList<Entry> yVal = new ArrayList<Entry>();
		yVal.add(new BarEntry(Environment.getExternalStorageDirectory().getTotalSpace(), 0));
		if(file.isFile()){
			if(type.equalsIgnoreCase("MUSIC")){
				//if type is music then load the album art....
				MediaMetadataRetriever ret = new MediaMetadataRetriever();
				ret.setDataSource(file.getPath());
				byte[] data = ret.getEmbeddedPicture();
				Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length);
				if(map != null)
					prp_img.setImageBitmap(map);
			}
			yVal.add(new BarEntry(file.length(), 1));
			prp_size.setText(getString(R.string.filesize) + " - " + RootManager.getSize(file));
		}	
		else if(file.isDirectory()){
			yVal.add(new BarEntry(size, 1));
			prp_size.setText(Utils.size(size));
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
	 * @return icon for the file....
	 */
	private Drawable buildIcon(File f){
		final Resources res = getResources();
		if(f.isDirectory()){
			type = getString(R.string.directory);
			return res.getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]);
		}
		
		String name = f.getName().toLowerCase(Locale.ENGLISH);
		
		if(name.endsWith(".zip")){
			
			type = getString(R.string.zip);
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".7z")){
			
			type = getString(R.string.zip7);
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".rar")){
			
			type = getString(R.string.rar);
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".tar")||name.endsWith(".tar.gz")||name.endsWith(".tar.bz2")){
			
			type = getString(R.string.tar);
			return Constants.ARCHIVE;
			
		}
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")){
			
			type = getString(R.string.music);
			return Constants.MUSIC;
			
		}
		else if(name.endsWith(".apk")){
			
			type = getString(R.string.application);
			return Constants.APP;
			
		}else if(name.endsWith(".sh")||name.endsWith(".prop")||name.endsWith("init")
				||name.endsWith(".default")||name.endsWith(".rc")){
			
			type = getString(R.string.script);
			return Constants.SCRIPT;
			
		}else if(name.endsWith(".pdf")){
			
			type = getString(R.string.pdf);
			return Constants.PDF;
			
		}else if(name.endsWith(".htm")||name.endsWith(".html")||name.endsWith(".mhtml")){
			
			type = getString(R.string.web);
			return Constants.WEB;
			
		}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")){
			
			type = getString(R.string.vids);
			return Constants.VIDEO;
			
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")){
			
			type = getString(R.string.image);
			return Constants.IMAGE;
			
		}else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")){
			
			type = getString(R.string.text);
			return Constants.DOCS;
			
		}
		else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
				||name.endsWith(".pptx")||name.endsWith(".csv")){
			
			type = getString(R.string.docs);
			return Constants.DOCS;
			
		}
		else{
			
			type = getString(R.string.unknown);
			return Constants.UNKNOWN;
			
		}		
	}

	
}
