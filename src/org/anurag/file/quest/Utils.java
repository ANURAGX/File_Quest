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

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Utils {
	
	static View v;
	public static boolean loaded = false;
	public static ArrayList<File> music;
	public static ArrayList<File> apps;
	public static ArrayList<File> vids;
	public static ArrayList<File> doc;
	public static ArrayList<File> zip;
	public static ArrayList<File> mis;
	public static ArrayList<File> img;
	
	
	static String msize;
	static String asize;
	static String psize;
	static String dsize;
	static String vsize;
	static String zsize;
	static String misize;
	
	
	static long musicsize=0;
	static long apksize=0;
	static long vidsize=0;
	static long docsize=0;
	static long zipsize=0;
	static long missize=0;
	static long imgsize=0;
	int size = 0;
	File file; 	
	String message;
	Handler handle;
	TextView count;
	
	Context ctx;
	
	public Utils() {
		// TODO Auto-generated constructor stub
		
		file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		music = new ArrayList<File>();
		apps = new ArrayList<File>();
		vids = new ArrayList<File>();
		doc = new ArrayList<File>();
		zip = new ArrayList<File>();
		mis = new ArrayList<File>();
		img = new ArrayList<File>();
		//ctx = cont;
		
		musicsize=0;
		apksize=0;
		vidsize=0;
		docsize=0;
		zipsize=0;
		missize=0;
		imgsize=0;
	}
	public Utils(View view,Context cont) {
		// TODO Auto-generated constructor stub
		v = view;
		file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		music = new ArrayList<File>();
		apps = new ArrayList<File>();
		vids = new ArrayList<File>();
		doc = new ArrayList<File>();
		zip = new ArrayList<File>();
		mis = new ArrayList<File>();
		img = new ArrayList<File>();
		ctx = cont;
		loaded = false;
		musicsize=0;
		apksize=0;
		vidsize=0;
		docsize=0;
		zipsize=0;
		missize=0;
		imgsize=0;
	}
	
	public void setView(View view){
		v = view;
	}
	
	public void load(){
		
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 1:
							//Displaying File counts of each type
							loaded = true;
							break;
				
					case 2:
							//DISLPAYS MUSIC SIZE..
							TextView mSize= (TextView)v.findViewById(R.id.mSize);
							mSize.setText(msize);
							
							count = (TextView)v.findViewById(R.id.mFiles);
							count.setText(music.size() + " "+ctx.getString(R.string.items));
							break;
							
					case 3:
							//DISPLAYS APPS SIZE...
							TextView aSize= (TextView)v.findViewById(R.id.aSize);
							aSize.setText(asize);
							
							
							count = (TextView)v.findViewById(R.id.aFiles);
							count.setText(apps.size() + " "+ctx.getString(R.string.items));
							break;
							
					case 4:
						
							//DSIPLAYS DOCS SIZE...
							TextView dSize= (TextView)v.findViewById(R.id.dSize);
							dSize.setText(dsize);
							
							count = (TextView)v.findViewById(R.id.dFile);
							count.setText(doc.size() + " "+ctx.getString(R.string.items));
							break;
					case 5:
							//displays IMAGE SIZE..
							TextView iSize= (TextView)v.findViewById(R.id.pSize);
							iSize.setText(psize);
							
							count = (TextView)v.findViewById(R.id.pFiles);
							count.setText(img.size() + " "+ctx.getString(R.string.items));
							break;
							
					case 6:
							//displays video size...
							TextView vSize= (TextView)v.findViewById(R.id.vSize);
							vSize.setText(vsize);
							
							
							count = (TextView)v.findViewById(R.id.vFiles);
							count.setText(vids.size() + " "+ctx.getString(R.string.items));
							break;
							
							
					case 7:
						
							//displays archive size...
							TextView zSize= (TextView)v.findViewById(R.id.zSize);
							zSize.setText(zsize);
							
							count = (TextView)v.findViewById(R.id.zFiles);
							count.setText(zip.size() + " "+ctx.getString(R.string.items));
							break;
							
					case 8:
							//displays miscellaneous size...
							TextView mmSize= (TextView)v.findViewById(R.id.misFiles);
							mmSize.setText(misize);
							
							count = (TextView)v.findViewById(R.id.misSize);
							count.setText(mis.size() + " "+ctx.getString(R.string.items));
							break;
				}
			}
		};		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//loaded = false;
				makelist(file);
				loaded = true;			
			}
		});
		if(!loaded)
			thread.start();
		else{
			handle.sendEmptyMessage(2);
			handle.sendEmptyMessage(3);
			handle.sendEmptyMessage(4);
			handle.sendEmptyMessage(5);
			handle.sendEmptyMessage(6);
			handle.sendEmptyMessage(7);
			handle.sendEmptyMessage(8);
		}
	}
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public String size(long size){
		if(size>Constants.GB)
			return String.format(ctx.getString(R.string.appsizegb), (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format(ctx.getString(R.string.appsizemb), (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(ctx.getString(R.string.appsizekb), (double)size/(1024));
		
		else
			return String.format(ctx.getString(R.string.appsizebytes), (double)size);
	}
	
	/**
	 * 
	 * @param file
	 */
	void makelist(File file){
		if(file.isFile()){
			identifyType(file);
		}else if(file.isDirectory()){
			for(File f:file.listFiles())
				makelist(f);
		}
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	private void identifyType(File file){
		String name = file.getName();
		if(name.endsWith(".zip")||name.endsWith(".tar")||name.endsWith(".rar")||name.endsWith(".7z")
				||name.endsWith(".tar.gz")||name.endsWith(".tar.bz2")||name.endsWith(".ZIP")||name.endsWith(".TAR")||
				name.endsWith(".RAR")||name.endsWith(".7Z")||name.endsWith(".TAR.GZ")||name.endsWith(".TAR.BZ2")){
			zip.add(file);
			zipsize+=file.length();
			zsize = size(zipsize);
			size = zip.size();
			if(Constants.UPDATE_FILEGALLERY)
				handle.sendEmptyMessage(7);
		}
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")||name.endsWith(".MP3")||name.endsWith(".OGG")||name.endsWith(".M4A")||
				name.endsWith(".WAV")||name.endsWith(".AMR")){
			music.add(file);
			musicsize+=file.length();
			msize = size(musicsize);
			size = music.size();
			if(Constants.UPDATE_FILEGALLERY)
				handle.sendEmptyMessage(2);
		}
		else if(name.endsWith(".apk")||name.endsWith(".APK")){
			apps.add(file);
			apksize+=file.length();
			asize = size(apksize);
			size = apps.size();
			if(Constants.UPDATE_FILEGALLERY)
				handle.sendEmptyMessage(3);
		}		
		else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")||name.endsWith(".FLV")||name.endsWith(".MP4")||name.endsWith(".3GP")||name.endsWith(".AVI")
				||name.endsWith(".MKV")){
			vids.add(file);
			vidsize+=file.length();
			vsize = size(vidsize);
			size = vids.size();
			if(Constants.UPDATE_FILEGALLERY)
				handle.sendEmptyMessage(6);
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")||name.endsWith(".BMP")||name.endsWith(".GIF")||name.endsWith(".JPEG")||name.endsWith(".JPG")
				||name.endsWith(".PNG")){
			img.add(file);
			imgsize+=file.length();
			psize = size(imgsize);
			size = img.size();
			if(Constants.UPDATE_FILEGALLERY)
				handle.sendEmptyMessage(5);
		}
		else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")||name.endsWith(".doc")
				||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".TXT")||name.endsWith(".LOG")||name.endsWith(".INI")||name.endsWith(".DOC")
				||name.endsWith(".PPT")||name.endsWith(".DOCX")){
			doc.add(file);
			docsize+=file.length();
			dsize = size(docsize);
			size = doc.size();
			if(Constants.UPDATE_FILEGALLERY)
				handle.sendEmptyMessage(4);
		}
		else{
			mis.add(file);
			missize+=file.length();
			misize = size(missize);
			size = mis.size();
			if(Constants.UPDATE_FILEGALLERY)
				handle.sendEmptyMessage(8);
		}
		
	}
}
