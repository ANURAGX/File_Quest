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
import android.widget.ImageView;
import android.widget.TextView;

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

/*
 * TODO 
 * calculate folder size of folders that needed root access....
 */
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
        pChart.animateXY(1500, 1500);
        Legend l = pChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
		
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
			yVal.add(new BarEntry(calculateLength(), 1));
			prp_size.setText(Utils.size(size));
		}	
		
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
			return res.getDrawable(R.drawable.ic_launcher_zip_it);
			
		}else if(name.endsWith(".7z")){
			
			type = getString(R.string.zip7);
			return res.getDrawable(R.drawable.ic_launcher_7zip);
			
		}else if(name.endsWith(".rar")){
			
			type = getString(R.string.rar);
			return res.getDrawable(R.drawable.ic_launcher_rar);
			
		}else if(name.endsWith(".tar")||name.endsWith(".tar.gz")||name.endsWith(".tar.bz2")){
			
			type = getString(R.string.tar);
			return res.getDrawable(R.drawable.ic_launcher_7zip);
			
		}
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")){
			
			type = getString(R.string.music);
			return res.getDrawable(R.drawable.ic_launcher_music);
			
		}
		else if(name.endsWith(".apk")){
			
			type = getString(R.string.application);
			return res.getDrawable(R.drawable.ic_launcher_apk);
			
		}else if(name.endsWith(".sh")||name.endsWith(".prop")||name.endsWith("init")
				||name.endsWith(".default")||name.endsWith(".rc")){
			
			type = getString(R.string.script);
			return res.getDrawable(R.drawable.ic_launcher_sh);
			
		}else if(name.endsWith(".pdf")){
			
			type = getString(R.string.pdf);
			return res.getDrawable(R.drawable.ic_launcher_adobe);
			
		}else if(name.endsWith(".htm")||name.endsWith(".html")||name.endsWith(".mhtml")){
			
			type = getString(R.string.web);
			return res.getDrawable(R.drawable.ic_launcher_web_pages);
			
		}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")){
			
			type = getString(R.string.vids);
			return res.getDrawable(R.drawable.ic_launcher_video);
			
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")){
			
			type = getString(R.string.image);
			return res.getDrawable(R.drawable.ic_launcher_images);
			
		}else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")){
			
			type = getString(R.string.text);
			return res.getDrawable(R.drawable.ic_launcher_text);
			
		}
		else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
				||name.endsWith(".pptx")||name.endsWith(".csv")){
			
			type = getString(R.string.docs);
			return res.getDrawable(R.drawable.ic_launcher_ppt);
			
		}
		else{
			
			type = getString(R.string.unknown);
			return res.getDrawable(R.drawable.ic_launcher_unknown);
			
		}		
	}

	
}
