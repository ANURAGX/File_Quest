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
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
	
	//tells whether updating of file gallery needed or not....
	public static boolean update_Needed;
	
	//tells whether updating of favorite items needed or not....
	public static boolean fav_Update_Needed;
	
	
	static String type;
	static Resources res;
	static View v;
	
	//tells whether files are completely loaded or not....
	public static boolean loaded;
	
	//list of files for different file types in file gallery....
	public static ArrayList<Item> music;
	public static ArrayList<Item> apps;
	public static ArrayList<Item> vids;
	public static ArrayList<Item> doc;
	public static ArrayList<Item> zip;
	public static ArrayList<Item> mis;
	public static ArrayList<Item> img;
	public static ArrayList<Item> fav;
	
	//count of favorite folders and files....
	private static int folderCount,fileCount;
	
	//file types icons....
	static Drawable musicImg;
	static Drawable imageImg;
	static Drawable vidImg;
	static Drawable docImg;
	static Drawable arcImg;
	static Drawable misImg;
	static Drawable apkImg;
	static Drawable folderImg;
	
	//file type strings...
	static String musicType;
	static String imageType;
	static String vidType;
	static String docType;
	static String arcType;
	static String misType;
	static String apkType;
	static String folderType;
	static String folderCnt;
	static String fileCnt;
	
	//file sizes in  string....
	static String msize;
	static String asize;
	static String psize;
	static String dsize;
	static String vsize;
	static String zsize;
	static String misize;
	
	//strings....
	static String Items;
	static String sizeGB;
	static String sizeMB;
	static String sizeKB;
	static String sizeByte;
	
	//file sizes in long....
	static long musicsize=0;
	static long apksize=0;
	static long vidsize=0;
	static long docsize=0;
	static long zipsize=0;
	static long missize=0;
	static long imgsize=0;
	
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
	 * simple function....
	 */
	public static void setContext() {
		// TODO Auto-generated constructor stub
		
		update_Needed = false;
		fav_Update_Needed = false;
		
		music = new ArrayList<Item>();
		apps = new ArrayList<Item>();
		vids = new ArrayList<Item>();
		doc = new ArrayList<Item>();
		zip = new ArrayList<Item>();
		mis = new ArrayList<Item>();
		img = new ArrayList<Item>();
			
		musicsize=0;
		apksize=0;
		vidsize=0;
		docsize=0;
		zipsize=0;
		missize=0;
		imgsize=0;
	}
	
	
	private static Handler handler = new Handler(){
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
		
		music = new ArrayList<Item>();
		apps = new ArrayList<Item>();
		vids = new ArrayList<Item>();
		doc = new ArrayList<Item>();
		zip = new ArrayList<Item>();
		mis = new ArrayList<Item>();
		img = new ArrayList<Item>();	
		fav = new ArrayList<Item>();	
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
		folderImg = res.getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]);
		
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
		new MainLoadTask().start();
	}	
	
	/**
	 * stop currently running scanning of files....
	 */
	public static void stop(){
		Utils.loaded = true;
	}
	
	/**
	 * again reload the items...
	 */
	public static void restart(){
		Utils.loaded = false;
		music = new ArrayList<Item>();
		apps = new ArrayList<Item>();
		vids = new ArrayList<Item>();
		doc = new ArrayList<Item>();
		zip = new ArrayList<Item>();
		mis = new ArrayList<Item>();
		img = new ArrayList<Item>();	
		fav = new ArrayList<Item>();
		misize = zsize = vsize = dsize = psize = asize = 
				msize = ctx.getString(R.string.zerosize);
		load();
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
	
	
	private static class MainLoadTask extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!Utils.loaded){
				prepareFavList();
				start(new File("/storage/ext_sd"));
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
					null, 
					null, 
					null, 
					null, 
					null);
			while(cursor.moveToNext()){
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
						Item itm = new Item(file, folderImg, folderType, it);
						fav.add(itm);
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
				for(File fi:fil.listFiles()){
					if(fi.isFile())
						makeIcon(fi , false , handler);
					else if(fi.isDirectory()){
						start(fi);
					}	
				}	
		}					
	}	
	
	
	/**
	 * 
	 * @param f main file to encapsulate....
	 * @param forFavItem if true add it fav list....
	 * @param handler to send message
	 */
	private static void makeIcon(File f , boolean forFavItem , Handler handler){
		String name = f.getName();
		if(name.endsWith(".zip")||name.endsWith(".ZIP")){
			Item itm = new Item(f, arcImg, arcType, RootManager.getSize(f));
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)	
					handler.sendEmptyMessage(0);
				return;
			}
			zip.add(itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
			
		}else if(name.endsWith(".7z")||name.endsWith(".7Z")){
			Item itm = new Item(f, res.getDrawable(R.drawable.ic_launcher_7zip),
					arcType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			zip.add(itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
			
		}else if(name.endsWith(".rar")||name.endsWith(".RAR")){
			Item itm = new Item(f, res.getDrawable(R.drawable.ic_launcher_rar),
					arcType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			zip.add(itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
			
		}else if(name.endsWith(".tar")||name.endsWith(".TAR")||name.endsWith(".tar.gz")||name.endsWith(".TAR.GZ")
				||name.endsWith(".TAT.BZ2")||name.endsWith(".tar.bz2")){
			Item itm = new Item(f, res.getDrawable(R.drawable.ic_launcher_tar),
					arcType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			zip.add(itm);
			zipsize+=f.length();
			zsize = size(zipsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(6);
						
		}
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")||name.endsWith(".MP3")||name.endsWith(".OGG")||name.endsWith(".M4A")||
				name.endsWith(".WAV")||name.endsWith(".AMR")){
			Item itm = new Item(f, musicImg, musicType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			music.add(itm);
			musicsize+=f.length();
			msize = size(musicsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(1);
		}
		else if(name.endsWith(".apk")||name.endsWith(".APK")){
			Item itm = new Item(f, apkImg, apkType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}			
			apps.add(itm);
			apksize+=f.length();
			asize = size(apksize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(2);
			
		}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")||name.endsWith(".FLV")||name.endsWith(".MP4")||name.endsWith(".3GP")||name.endsWith(".AVI")
				||name.endsWith(".MKV")){
			Item itm = new Item(f, vidImg, vidType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			vids.add(itm);
			vidsize+=f.length();
			vsize = size(vidsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(4);
			
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")||name.endsWith(".BMP")||name.endsWith(".GIF")||name.endsWith(".JPEG")||name.endsWith(".JPG")
				||name.endsWith(".PNG")){
			Item itm = new Item(f, imageImg, imageType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}			
			
			img.add(itm);
			imgsize+=f.length();
			psize = size(imgsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(3);
			
		}else if(name.endsWith(".pdf")||name.endsWith(".PDF")){
			Item itm = new Item(f,res.getDrawable(R.drawable.ic_launcher_adobe),
								ctx.getString(R.string.pdf),
								RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			doc.add(itm);
			docsize+=f.length();
			dsize = size(docsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(5);
		}else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
				||name.endsWith(".PPT")||name.endsWith(".DOCX")||name.endsWith(".pptx")||name.endsWith(".PPTX")
				||name.endsWith(".csv")||name.endsWith(".CSV")){
			Item itm = new Item(f,docImg,docType,RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			doc.add(itm);
			docsize+=f.length();
			dsize = size(docsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(5);
		
		}else if(name.endsWith(".txt")||name.endsWith(".TXT")||name.endsWith(".log")||name.endsWith(".LOG")
				||name.endsWith(".ini")||name.endsWith(".INI")){
			Item itm = new Item(f,res.getDrawable(R.drawable.ic_launcher_text),
								ctx.getString(R.string.text),
								RootManager.getSize(f));
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			doc.add(itm);
			docsize+=f.length();
			dsize = size(docsize);
			if(!FileQuest.elementInFocus)
				handler.sendEmptyMessage(5);			
		}
		else{
			Item itm = new Item(f,misImg, misType, RootManager.getSize(f));
			
			if(forFavItem){
				fav.add(itm);
				fileCount++;
				if(!FileQuest.elementInFocus)
					handler.sendEmptyMessage(0);
				return;
			}
			
			mis.add(itm);
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
			musicText.setText(msize);								
			musicTextCount.setText(music.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}					
		
		try{
			//DISPLAYS APPS SIZE...
			appText.setText(asize);								
			appTextCount.setText(apps.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}					
		
		try{
			//displays IMAGE SIZE..
			imgText.setText(psize);								
			imgTextCount.setText(img.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
			
		try{
			//displays video size...
			vidText.setText(vsize);								
			vidTextCount.setText(vids.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
			
		try{
			//DSIPLAYS DOCS SIZE...
			docText.setText(dsize);							
			docTextCount.setText(doc.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
		
		try{
			//displays archive size...
			arcText.setText(zsize);								
			arcTextCount.setText(zip.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}
			
		//displays miscellaneous size...
		try{
			misText.setText(misize);								
			misTextCount.setText(mis.size() + " "+ctx.getString(R.string.items));
		}catch(NullPointerException e){}	
	}
	
	
	public static void notifyAllAppsDeleted(){
		apps = new ArrayList<Item>();
		asize = ctx.getString(R.string.zerosize);
		updateUI();
	}
	
	public static void notifyAllMusicDeleted(){
		music = new ArrayList<Item>();
		msize = ctx.getString(R.string.zerosize);
		updateUI();
	}
	
	public static void notifyAllImageDeleted(){
		img = new ArrayList<Item>();
		psize = ctx.getString(R.string.zerosize);
		updateUI();
	}
	
	public static void notifyAllMisDeleted(){
		mis = new ArrayList<Item>();
		misize = ctx.getString(R.string.zerosize);
		updateUI();
	}
	
	public static void notifyAllZipDeleted(){
		zip = new ArrayList<Item>();
		zsize = ctx.getString(R.string.zerosize);
		updateUI();
	}
	
	public static void notifyAllFavDeleted(){
		fav = new ArrayList<Item>();
		folderCount = 0;
		fileCount = 0;
		updateUI();
	}
	
	public static void notifyAllVideoDeleted(){
		vids = new ArrayList<Item>();
		vsize = ctx.getString(R.string.zerosize);
		updateUI();
	}
	
	public static void notifyAllDocsDeleted(){
		doc = new ArrayList<Item>();
		dsize = ctx.getString(R.string.zerosize);
		updateUI();
	}
	
	/**
	 * 
	 * @param position
	 */
	public static void notifyFileDelete(int position){
		switch(position){
			case 0:
					notifyAllMusicDeleted();
					break;
			case 1:
					notifyAllAppsDeleted();
					break;
			case 2:
					notifyAllDocsDeleted();
					break;
			case 3:
				    notifyAllImageDeleted();
				    break;
			case 4:
					notifyAllVideoDeleted();
					break;
			case 5:
					notifyAllZipDeleted();
					break;
			case 6:
					notifyAllMisDeleted();
					break;
			default:
					notifyAllFavDeleted();
					
		}
	}
	
	/**
	 * function to rebuild the favorite items list after an 
	 * item was added or removed from DB..... 
	 */
	public static void buildFavItems(){
		//if true no need to call the function...
		//as it will be reloaded in restart function....
		if(Utils.update_Needed)
				return;
		
		fav = new ArrayList<Item>();
		folderCount = 0;
		fileCount = 0;
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(FileQuest.elementInFocus)
					FileQuest.element.notifyDataSetChanged();
				else{
					try{
						favText.setText(String.format(folderCnt, folderCount));
						favTextCount.setText(String.format(fileCnt, fileCount));
					}catch(Exception e){
						
					}	
				}					
			}
		};
		
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
		
		Thread thr = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Cursor cursor = Constants.db.getReadableDatabase().query("FAVITEMS", 
						null, 
						null, 
						null, 
						null, 
						null, 
						null);
				while(cursor.moveToNext()){
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
							Item itm = new Item(file, folderImg, folderType, it);
							fav.add(itm);
						}else
							makeIcon(file , true , handler);
					}else if(!file.exists()){
						//item has been deleted,so delete the item from the db also....
						Constants.db.deleteFavItem(cursor.getString(0));
					}
				}
			}		
		});
		thr.start();
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
	
}
