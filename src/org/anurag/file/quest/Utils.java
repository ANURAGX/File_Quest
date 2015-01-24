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

import java.io.FileFilter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;



/**
 * 
 * @author Anurag....
 *
 */
@SuppressLint("HandlerLeak")
public class Utils {
	
	/**
	 * counter variables(keys) for ConcurrentHashMaps....
	 */
	public static int musCounter;
	public static int appCounter;
	public static int imgCounter;
	public static int vidCounter;
	public static int docCounter;
	public static int zipCounter;
	public static int misCounter;
	public static int favCounter;
	public static ConcurrentHashMap<String, String> musicKey;
	public static ConcurrentHashMap<String, String> appKey;
	public static ConcurrentHashMap<String, String> imgKey;
	public static ConcurrentHashMap<String, String> videoKey;
	public static ConcurrentHashMap<String, String> docKey;
	public static ConcurrentHashMap<String, String> zipKey;
	public static ConcurrentHashMap<String, String> misKey;
	public static ConcurrentHashMap<String, String> favKey;
	
	private static MainLoadTask task;
	private static Object lock;
 	
	//tells whether updating of file gallery needed or not....
	public static boolean update_Needed;
	
	//tells whether updating of favorite items needed or not....
	public static boolean fav_Update_Needed;
	
	
	static String type;
	public static Resources res;
	static View v;
	
	//tells whether files are completely loaded or not....
	public static boolean loaded;
	
	//list of files for different file types in file gallery....
	public static ConcurrentHashMap<String , Item> music;
	public static ConcurrentHashMap<String , Item> apps;
	public static ConcurrentHashMap<String , Item> vids;
	public static ConcurrentHashMap<String , Item> doc;
	public static ConcurrentHashMap<String , Item> zip;
	public static ConcurrentHashMap<String , Item> mis;
	public static ConcurrentHashMap<String , Item> img;
	public static ConcurrentHashMap<String ,Item> fav;
	
	//count of favorite folders and files....
	public static int folderCount,fileCount;
	
	//file types icons....
	//public static Drawable musicImg;
	//public static Drawable imageImg;
	//public static Drawable vidImg;
	//public static Drawable docImg;
    //public static Drawable arcImg;
	//public static Drawable misImg;
	//public static Drawable apkImg;
	//static Drawable folderImg;
	
	//file type strings...
	public static String musicType;
	public static String imageType;
	public static String vidType;
	public static String docType;
	public static String arcType;
	public static String misType;
	public static String apkType;
	static String folderType;
	static String folderCnt;
	static String fileCnt;
	
	//file sizes in  string....
	public static String msize;
	public static String asize;
	public static String psize;
	public static String dsize;
	public static String vsize;
	public static String zsize;
	public static String misize;
	
	//strings....
	static String Items;
	static String sizeGB;
	static String sizeMB;
	static String sizeKB;
	static String sizeByte;
	
	//file sizes in long....
	public static long musicsize=0;
	public static long apksize=0;
	public static long vidsize=0;
	public static long docsize=0;
	public static long zipsize=0;
	public static long missize=0;
	public static long imgsize=0;
	
	static Context ctx;
	
	//views for file types in file gallery....
	static TextView musicText,musicTextCount;
	static TextView appText,appTextCount;
	static TextView imgText,imgTextCount;
	static TextView vidText,vidTextCount;
	static TextView docText,docTextCount;
	static TextView arcText,arcTextCount;
	static TextView misText,misTextCount;
	static TextView favText,favTextCount;
	
	
	/**
	 * 
	 */
	public static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
				switch(msg.what){
					case 0:					
						try{
							favText.setText(String.format(folderCnt, folderCount));
							favTextCount.setText(String.format(fileCnt, fileCount));
						}catch(Exception e){
							
						}					
						break;
					case 1:
						try{
							//DISLPAYS MUSIC SIZE..
							musicText.setText(msize);								
							musicTextCount.setText(music.size() + " "+Items);
						}catch(Exception e){
							
						}					
						break;
						
					case 2:
						try{
							//DISPLAYS APPS SIZE...
							appText.setText(asize);								
							appTextCount.setText(apps.size() + " "+Items);
						}catch(Exception e){
							
						}					
						break;
						
					case 3:
						try{
							//displays IMAGE SIZE..
							imgText.setText(psize);								
							imgTextCount.setText(img.size() + " "+Items);					}catch(NullPointerException e){
						}catch(Exception e){
							
						}
						break;
						
					case 4:
						try{
							//displays video size...
							vidText.setText(vsize);								
							vidTextCount.setText(vids.size() + " "+Items);
						}catch(Exception e){
							
						}
						break;
						
					case 5:
						try{
							//DSIPLAYS DOCS SIZE...
							docText.setText(dsize);							
							docTextCount.setText(doc.size() + " "+Items);
						}catch(Exception e){
							
						}
						break;
						
					case 6:
						try{
							//displays archive size...
							arcText.setText(zsize);								
							arcTextCount.setText(zip.size() + " "+Items);
						}catch(Exception e){
							
						}
						break;
						
					case 7:
						//displays miscellaneous size...
						try{
							misText.setText(misize);								
							misTextCount.setText(mis.size() + " "+Items);
						}catch(Exception e){
							
						}
						break;
					
					case 10:
						//pause the loading....
						if(!loaded){
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									handler.postDelayed(this, 1000);
								}
							}, 1000);
						}
						break;
				}

				
		}	

	};
	
	/**
	 * 
	 * @param view to show the file gallery items....
	 * @param cont context....
	 */
	public static void setContext(View view,Context cont) {
		// TODO Auto-generated constructor stub
		
		musCounter = appCounter = imgCounter = vidCounter = docCounter =
				zipCounter = misCounter = favCounter = 0;
		
		v = view;
		update_Needed = false;
		fav_Update_Needed = false;
		
		if(v !=null){
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
			
			favText = (TextView)v.findViewById(R.id.fSize);
			favTextCount = (TextView)v.findViewById(R.id.fFiles);
			
			favText.setText(String.format(folderCnt, 0));
			favTextCount.setText(String.format(fileCnt, 0));
		}
		
		Items = cont.getString(R.string.items);
		sizeGB = cont.getString(R.string.appsizegb);
		sizeMB = cont.getString(R.string.appsizemb);
		sizeKB = cont.getString(R.string.appsizekb);
		sizeByte = cont.getString(R.string.appsizebytes);
		
		music = new ConcurrentHashMap<String , Item>();
		apps = new ConcurrentHashMap<String , Item>();
		vids = new ConcurrentHashMap<String , Item>();
		doc = new ConcurrentHashMap<String , Item>();
		zip = new ConcurrentHashMap<String , Item>();
		mis = new ConcurrentHashMap<String , Item>();
		img = new ConcurrentHashMap<String , Item>();
		fav = new ConcurrentHashMap<String , Item>();
		
		musicKey = new ConcurrentHashMap<String , String>();
		appKey = new ConcurrentHashMap<String , String>();
		videoKey =  new ConcurrentHashMap<String , String>();
		docKey =  new ConcurrentHashMap<String , String>();
		zipKey = new ConcurrentHashMap<String , String>();
		imgKey = new ConcurrentHashMap<String , String>();
		misKey = new ConcurrentHashMap<String , String>();
		favKey = new ConcurrentHashMap<String , String>();
		
		
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
		
		
		
		
		folderCnt = res.getString(R.string.totalfolder);
		fileCnt = res.getString(R.string.totalfiles);
		musicType = ctx.getString(R.string.music);
		arcType = ctx.getString(R.string.zip);
		apkType = ctx.getString(R.string.application);
		imageType = ctx.getString(R.string.image);
		misType = ctx.getString(R.string.mis);
		vidType = ctx.getString(R.string.vids);
		docType = ctx.getString(R.string.docs);
		folderType = res.getString(R.string.directory);
		
		folderCount = fileCount = 0;
		
		misize = zsize = vsize = dsize = psize = asize = msize = ctx.getString(R.string.zerosize);
	}
	
	public static void setView(View view){
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
		
		favText = (TextView)v.findViewById(R.id.fSize);
		favTextCount = (TextView)v.findViewById(R.id.fFiles);
		
		favText.setText(String.format(folderCnt, 0));
		favTextCount.setText(String.format(fileCnt, 0));
	}
	
	/**
	 * scans the files for file gallery....
	 */
	public static void load(){
		lock = new Object();
		task = new MainLoadTask();
		task.start();
	}	
	
	public static void pause(){
		handler.sendEmptyMessage(10);
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public static String size(long size){
		if(size>Constants.GB)
			return String.format(sizeGB, (double)size/(Constants.GB));		
		else if(size > Constants.MB)
			return String.format(sizeMB, (double)size/(Constants.MB));		
		else if(size>1024)
			return String.format(sizeKB, (double)size/(1024));		
		else
			return String.format(sizeByte, (double)size);
	}
	
	
	/**
	 * 
	 * @author Anurag....
	 *
	 */
	public static class MainLoadTask extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!Utils.loaded){
				prepareFavList();
				start(Environment.getExternalStorageDirectory());
				Utils.loaded = true;
			}	
			else{
				handler.sendEmptyMessage(0);
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				handler.sendEmptyMessage(3);
				handler.sendEmptyMessage(4);
				handler.sendEmptyMessage(5);
				handler.sendEmptyMessage(6);
				handler.sendEmptyMessage(7);
			}
		}
		
		/**
		 * prepares the list of favorite items from db....
		 */
		void prepareFavList(){
			Cursor cursor = Constants.db.getReadableDatabase().query("FAVITEMS",
					null, 
					"DUP = 0", 
					null, 
					null, 
					null, 
					null);
			while(cursor.moveToNext()){
				
				//waiting here till user navigates back to file gallery from any selected
				//category....
				//synchronizing the thread with main ui....
				
				//it is almost impossible that thread will cause any issue while reading from
				//sqlite...
				//in any case I m syncing thread with main UI....
				synchronized(lock){
					while(FileQuest.elementInFocus){
						try {
							lock.wait(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				File file = new File(cursor.getString(0));
				if(file.exists()){
					//file exists,so proceed....
					if(file.isDirectory()){
						folderCount++;
						String it = null;
						try{
							it = file.list().length +" "+ Items;
						}catch(Exception e){
							it = ctx.getString(R.string.rootd);
						}
						Item itm = new Item(file, Constants.FOLDER_IMAGE , folderType, it);
						fav.put(file.getPath(), itm);
						favKey.put(""+favCounter++, file.getPath());
					}else
						makeIcon(file , true , handler);
				}else if(!file.exists()){
					//item has been deleted,so delete the item from the db also....
					Constants.db.deleteFavItem(cursor.getString(0));
				}
			}
			cursor.close();
		}
		
		/**
		 * 
		 * @param fil
		 */
		void start(File fil){
			if(!Utils.loaded)
				
				try{//using true block because for loop resulting in NULLPOINTER
					//EXCEPTION in API 14....
					for(File fi:fil.listFiles(new FileFilter() {
						@Override
						public boolean accept(File arg0) {
							// TODO Auto-generated method stub
							if(arg0 == null)
								return false;
							else 
								return true;
						}
					})){
						
						//waiting here till user navigates back to file gallery from any selected
						//category....
						//synchronizing the thread with main ui....
						synchronized(lock){
							while(FileQuest.elementInFocus){
								try {
									lock.wait(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						
						if(fi.isFile())
							makeIcon(fi , false , handler);
						else if(fi.isDirectory()){
							start(fi);
						}	
					}
				}catch(NullPointerException e){
					
				}
		}					
	}	
	
	
	/**
	 * 
	 * @param f main file to encapsulate....
	 * @param forFavItem if true add it fav list....
	 * @param handler to send message
	 */
	public static void makeIcon(File f , boolean forFavItem , Handler handler){
		
		String name = f.getName().toLowerCase(Locale.ENGLISH);
		String path = f.getPath();
		
		if(name.endsWith(".zip")){
			Item itm = new Item(f, Constants.ARCHIVE, arcType, "");
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)	
					handler.sendEmptyMessage(0);
				return;
			}
			
			zipKey.put(""+zipCounter++, path);
			zip.put(path, itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
			
		}else if(name.endsWith(".7z")){
			Item itm = new Item(f, Constants.ARCHIVE,
					arcType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			zipKey.put(""+zipCounter++, path);
			zip.put(path, itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
			
		}else if(name.endsWith(".rar")){
			Item itm = new Item(f, Constants.ARCHIVE,
					arcType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			zipKey.put(""+zipCounter++, path);
			zip.put(path, itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
			
		}else if(name.endsWith(".tar")||name.endsWith(".tar.gz")||
				name.endsWith(".TAT.BZ2")){
			Item itm = new Item(f, Constants.ARCHIVE,arcType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			zipKey.put(""+zipCounter++, path);
			zip.put(path, itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
						
		}
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")){
			Item itm = new Item(f, Constants.MUSIC, musicType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			musicKey.put(""+musCounter++, path);
			music.put(path, itm);
			musicsize+=f.length();
			msize = size(musicsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(1);
		}
		else if(name.endsWith(".apk")){
			Item itm = new Item(f, Constants.APP, apkType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}			
			
			appKey.put(""+appCounter++, path);
			apps.put(path, itm);
			apksize+=f.length();
			asize = size(apksize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(2);
			
		}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")){
			Item itm = new Item(f,Constants.VIDEO, vidType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			videoKey.put(""+vidCounter++, path);
			vids.put(path, itm);
			vidsize+=f.length();
			vsize = size(vidsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(4);
			
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")){
			Item itm = new Item(f, Constants.IMAGE, imageType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}			
			
			imgKey.put(""+imgCounter++, path);
			img.put(path, itm);
			imgsize+=f.length();
			psize = size(imgsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(3);
			
		}else if(name.endsWith(".pdf")){
			Item itm = new Item(f,Constants.PDF,ctx.getString(R.string.pdf),"");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			docKey.put(""+docCounter++, path);
			doc.put(path, itm);
			docsize+=f.length();
			dsize = size(docsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(5);
		}else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
				||name.endsWith(".pptx")||name.endsWith(".csv")){
			Item itm = new Item(f , Constants.DOCS , docType , "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			docKey.put(""+docCounter++, path);
			doc.put(path, itm);
			docsize+=f.length();
			dsize = size(docsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(5);
		
		}else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")){
			Item itm = new Item(f, Constants.DOCS,
								ctx.getString(R.string.text), "");
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			docKey.put(""+docCounter++, path);
			doc.put(path, itm);
			docsize+=f.length();
			dsize = size(docsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(5);			
		}
		else{
			Item itm = new Item(f, Constants.UNKNOWN , misType, "");
			
			if(forFavItem){
				favKey.put(""+favCounter++, path);
				fav.put(path, itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			misKey.put(""+misCounter++, path);
			mis.put(path, itm);
			missize+=f.length();
			misize = size(missize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(7);
			
		}		
	}
	
	
	public static void updateUI(){
		try{
			favText.setText(String.format(folderCnt, folderCount));
			favTextCount.setText(String.format(fileCnt, fileCount));
		}catch(Exception e){}					
		
		try{
			//DISLPAYS MUSIC SIZE..
			msize = size(musicsize);
			musicText.setText(msize);								
			musicTextCount.setText(music.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}					
		
		try{
			//DISPLAYS APPS SIZE...
			asize = size(apksize);
			appText.setText(asize);								
			appTextCount.setText(apps.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}					
		
		try{
			//displays IMAGE SIZE..
			psize = size(imgsize);
			imgText.setText(psize);								
			imgTextCount.setText(img.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
			
		try{
			//displays video size...
			vsize = size(vidsize);
			vidText.setText(vsize);								
			vidTextCount.setText(vids.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
			
		try{
			//DSIPLAYS DOCS SIZE...
			dsize = size(docsize);
			docText.setText(dsize);							
			docTextCount.setText(doc.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
		
		try{
			//displays archive size...
			zsize = size(zipsize);
			arcText.setText(zsize);								
			arcTextCount.setText(zip.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
			
		//displays miscellaneous size...
		try{
			misize = size(missize);
			misText.setText(misize);								
			misTextCount.setText(mis.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}	
	}
	
	/**
	 * function to rebuild the favorite items list after an 
	 * item was added or removed from DB..... 
	 */
	public static void buildFavItems(Item itm , boolean add){
		//if true no need to call the function...
		//as it will be reloaded in restart function....
		if(Utils.update_Needed)
				return;
			
		//checking whether file scanning is running or not...
		//if running,then waiting till it finishes....
		if(!Utils.loaded)
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(!Utils.loaded)
						handler.postDelayed(this, 1000);
				}
			}, 1000);
		
		if(add){
			if(itm.isDirectory())
				folderCount++;
			else
				fileCount++;
			favKey.put(""+favCounter++, itm.getPath());
			fav.put(itm.getPath(), itm);
		}else{
			fav.remove(itm.getPath());
			int counter = 0;
			Utils.favKey.clear();
			for(Map.Entry< String , Item> entry : Utils.fav.entrySet()){
				Utils.favKey.put(""+counter++, entry.getValue().getPath());
			}
			if(itm.isDirectory())
				folderCount--;
			else
				fileCount--;
		}
		
		Utils.fav_Update_Needed = false;
	}
	
	/**
	 * this function simply updates the ui after and item was added or removed to fav list....
	 */
	public static void update_fav(){
		try{
			favText.setText(String.format(folderCnt, folderCount));
			favTextCount.setText(String.format(fileCnt, fileCount));
		}catch(Exception e){
			
		}
	}

	/**
	 * function to empty the fav item list....
	 * @param fPos
	 */
	public static void notifyFavFileDelete(int fPos) {
		// TODO Auto-generated method stub
		fav = new ConcurrentHashMap<String , Item>();
		folderCount = 0;
		fileCount = 0;
		favKey = new ConcurrentHashMap<String , String>();
		update_fav();
	}
	
}
