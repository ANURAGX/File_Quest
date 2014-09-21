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
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Utils {
	
	static String type;
	Resources res;
	static View v;
	public static boolean loaded = false;
	public static ArrayList<Item> music;
	public static ArrayList<Item> apps;
	public static ArrayList<Item> vids;
	public static ArrayList<Item> doc;
	public static ArrayList<Item> zip;
	public static ArrayList<Item> mis;
	public static ArrayList<Item> img;
	
	Drawable musicImg,imageImg,vidImg,docImg,arcImg,misImg,apkImg;
	String musicType,imageType,vidType,docType,arcType,misType,apkType;
	
	
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
	
	File file; 	
	Handler handle;
	TextView count;
	Context ctx;
	
	static TextView musicText,musicTextCount;
	static TextView appText,appTextCount;
	static TextView imgText,imgTextCount;
	static TextView vidText,vidTextCount;
	static TextView docText,docTextCount;
	static TextView arcText,arcTextCount;
	static TextView misText,misTextCount;
	
	public Utils() {
		// TODO Auto-generated constructor stub
		
		file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		music = new ArrayList<Item>();
		apps = new ArrayList<Item>();
		vids = new ArrayList<Item>();
		doc = new ArrayList<Item>();
		zip = new ArrayList<Item>();
		mis = new ArrayList<Item>();
		img = new ArrayList<Item>();
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
		musicText = (TextView)v.findViewById(R.id.mSize);
		musicTextCount = (TextView)v.findViewById(R.id.mFiles);
		
		appText = (TextView)v.findViewById(R.id.aSize);
		appTextCount = (TextView)v.findViewById(R.id.aFiles);
		
		docText = (TextView)v.findViewById(R.id.dSize);
		docTextCount = (TextView)v.findViewById(R.id.dFile);
		
		imgText = (TextView)v.findViewById(R.id.pSize);
		imgTextCount = (TextView)v.findViewById(R.id.pFiles);
		
		vidText = (TextView)v.findViewById(R.id.vSize);
		vidTextCount = (TextView)v.findViewById(R.id.vFiles);
		
		arcText = (TextView)v.findViewById(R.id.zSize);
		arcTextCount = (TextView)v.findViewById(R.id.zFiles);
		
		misText = (TextView)v.findViewById(R.id.misFiles);
		misTextCount = (TextView)v.findViewById(R.id.misSize);
		
		file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		
		music = new ArrayList<Item>();
		apps = new ArrayList<Item>();
		vids = new ArrayList<Item>();
		doc = new ArrayList<Item>();
		zip = new ArrayList<Item>();
		mis = new ArrayList<Item>();
		img = new ArrayList<Item>();		
		ctx = cont;
		res = ctx.getResources();
		loaded = false;
		musicsize=0;
		apksize=0;
		vidsize=0;
		docsize=0;
		zipsize=0;
		missize=0;
		imgsize=0;
		
		musicImg = res.getDrawable(R.drawable.ic_launcher_music);
		imageImg = res.getDrawable(R.drawable.ic_launcher_images);
		apkImg = res.getDrawable(R.drawable.ic_launcher_apk);
		vidImg = res.getDrawable(R.drawable.ic_launcher_video);
		docImg = res.getDrawable(R.drawable.ic_launcher_ppt);
		misImg = res.getDrawable(R.drawable.ic_launcher_unknown);
		arcImg = res.getDrawable(R.drawable.ic_launcher_zip_it);
		
		musicType = ctx.getString(R.string.music);
		arcType = ctx.getString(R.string.zip);
		apkType = ctx.getString(R.string.application);
		imageType = ctx.getString(R.string.image);
		misType = ctx.getString(R.string.mis);
		vidType = ctx.getString(R.string.vids);
		docType = ctx.getString(R.string.docs);
	}
	
	public void setView(View view){
		v = view;
	}
	
	public void load(){
		new MainAsyncTask().execute();
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
	
	
	private class MainAsyncTask extends AsyncTask<Void, Integer, Void>{
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			Utils.loaded = true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			switch(values[0]){
				case 0:
						
					break;
				case 1:
					try{
						//DISLPAYS MUSIC SIZE..
						musicText.setText(msize);								
						musicTextCount.setText(music.size() + " "+ctx.getString(R.string.items));
					}catch(NullPointerException e){
						
					}					
					break;
					
				case 2:
					try{
						//DISPLAYS APPS SIZE...
						appText.setText(asize);								
						appTextCount.setText(apps.size() + " "+ctx.getString(R.string.items));
					}catch(NullPointerException e){
						
					}					
					break;
					
				case 3:
					try{
						//displays IMAGE SIZE..
						imgText.setText(psize);								
						imgTextCount.setText(img.size() + " "+ctx.getString(R.string.items));
					}catch(NullPointerException e){
						
					}
					break;
					
				case 4:
					try{
						//displays video size...
						vidText.setText(vsize);								
						vidTextCount.setText(vids.size() + " "+ctx.getString(R.string.items));
					}catch(NullPointerException e){
						
					}
					break;
					
				case 5:
					try{
						//DSIPLAYS DOCS SIZE...
						docText.setText(dsize);							
						docTextCount.setText(doc.size() + " "+ctx.getString(R.string.items));
					}catch(NullPointerException e){
						
					}
					break;
					
				case 6:
					try{
						//displays archive size...
						arcText.setText(zsize);								
						arcTextCount.setText(zip.size() + " "+ctx.getString(R.string.items));
					}catch(NullPointerException e){
						
					}
					break;
					
				case 7:
					//displays miscellaneous size...
					try{
						misText.setText(misize);								
						misTextCount.setText(mis.size() + " "+ctx.getString(R.string.items));
					}catch(NullPointerException e){
						
					}
					break;
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			start(file);
			return null;
		}
		
		/**
		 * 
		 * @param fil
		 */
		void start(File fil){
			for(File fi:fil.listFiles())
				if(fi.isFile())
					makeIcon(fi);
				else if(fi.isDirectory())
					start(fi);
		}	
		
		/**
		 * 
		 * @param f
		 */
		private void makeIcon(File f){
			String name = f.getName();
			if(name.endsWith(".zip")||name.endsWith(".ZIP")){
				zip.add(new Item(f, arcImg, arcType, RootManager.getSize(f)));
				zipsize+=f.length();
				zsize = size(zipsize);
				publishProgress(new Integer[]{7});
			}else if(name.endsWith(".7z")||name.endsWith(".7Z")){
				zip.add(new Item(f, res.getDrawable(R.drawable.ic_launcher_7zip),
						arcType, RootManager.getSize(f)));
				zipsize+=f.length();
				zsize = size(zipsize);
				publishProgress(new Integer[]{7});
			}else if(name.endsWith(".rar")||name.endsWith(".RAR")){
				zip.add(new Item(f, res.getDrawable(R.drawable.ic_launcher_rar),
						arcType, RootManager.getSize(f)));
				zipsize+=f.length();
				zsize = size(zipsize);
				publishProgress(new Integer[]{7});
			}else if(name.endsWith(".tar")||name.endsWith(".TAR")||name.endsWith(".tar.gz")||name.endsWith(".TAR.GZ")
					||name.endsWith(".TAT.BZ2")||name.endsWith(".tar.bz2")){
				zip.add(new Item(f, res.getDrawable(R.drawable.ic_launcher_tar),
						arcType, RootManager.getSize(f)));
				zipsize+=f.length();
				zsize = size(zipsize);
				publishProgress(new Integer[]{7});
			}
			else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
					||name.endsWith(".amr")||name.endsWith(".MP3")||name.endsWith(".OGG")||name.endsWith(".M4A")||
					name.endsWith(".WAV")||name.endsWith(".AMR")){
				
				music.add(new Item(f, musicImg, musicType, RootManager.getSize(f)));
				musicsize+=f.length();
				msize = size(musicsize);
				publishProgress(new Integer[]{1});
			}
			else if(name.endsWith(".apk")||name.endsWith(".APK")){
				apps.add(new Item(f, apkImg, apkType, RootManager.getSize(f)));
				apksize+=f.length();
				asize = size(musicsize);
				publishProgress(new Integer[]{2});
			}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
					||name.endsWith(".mkv")||name.endsWith(".FLV")||name.endsWith(".MP4")||name.endsWith(".3GP")||name.endsWith(".AVI")
					||name.endsWith(".MKV")){
				vids.add(new Item(f, vidImg, vidType, RootManager.getSize(f)));
				vidsize+=f.length();
				vsize = size(vidsize);
				publishProgress(new Integer[]{4});
			}	
			else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
					||name.endsWith(".png")||name.endsWith(".BMP")||name.endsWith(".GIF")||name.endsWith(".JPEG")||name.endsWith(".JPG")
					||name.endsWith(".PNG")){
				img.add(new Item(f, imageImg, imageType, RootManager.getSize(f)));
				imgsize+=f.length();
				psize = size(imgsize);
				publishProgress(new Integer[]{3});
			}else if(name.endsWith(".pdf")||name.endsWith(".PDF")){
				doc.add(new Item(f,
						res.getDrawable(R.drawable.ic_launcher_adobe),
						ctx.getString(R.string.pdf),
						RootManager.getSize(f)));
				docsize+=f.length();
				dsize = size(docsize);
				publishProgress(new Integer[]{5});				
			}else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
					||name.endsWith(".PPT")||name.endsWith(".DOCX")||name.endsWith(".pptx")||name.endsWith(".PPTX")
					||name.endsWith(".csv")||name.endsWith(".CSV")){
				doc.add(new Item(f,docImg,docType,RootManager.getSize(f)));
				docsize+=f.length();
				dsize = size(docsize);
				publishProgress(new Integer[]{5});
			}else if(name.endsWith(".txt")||name.endsWith(".TXT")||name.endsWith(".log")||name.endsWith(".LOG")
					||name.endsWith(".ini")||name.endsWith(".INI")){
				doc.add(new Item(f,
						res.getDrawable(R.drawable.ic_launcher_text),
						ctx.getString(R.string.text),
						RootManager.getSize(f)));
				docsize+=f.length();
				dsize = size(docsize);
				publishProgress(new Integer[]{5});
			}
			else{
				mis.add(new Item(f,misImg, misType, RootManager.getSize(f)));
				missize+=f.length();
				misize = size(missize);
				publishProgress(new Integer[]{7});
			}		
		}		
	}	
}
