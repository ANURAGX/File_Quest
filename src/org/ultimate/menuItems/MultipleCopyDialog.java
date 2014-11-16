/**
 * Copyright(c) 2013 DRAWNZER.ORG PROJECTS -> ANURAG
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


package org.ultimate.menuItems;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;
import org.ultimate.root.LinuxShell;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

@SuppressLint({ "HandlerLeak", "SdCardPath" })
public class MultipleCopyDialog {
	
	//boolean values true tells type of file deleted
	private boolean music_deleted;
	private boolean app_deleted;
	private boolean img_deleted;
	private boolean vid_deleted;
	private boolean doc_deleted;
	private boolean zip_deleted;
	private boolean mis_deleted;
	
	static long max;
	static String copFrom;
	static String copSize;
	static String cop;
	static String status;
	ProgressBar progress;
	static Context mContext;
	//static int BUFFER = 256;
	Dialog dialog;
	ArrayList<Item> list;
	static long si = 0;
	String DEST;
	int len = 0;
	Button btn1,btn2;
	TextView copyTo;
	TextView copyFrom;
	TextView copying;
	TextView contentSize;
	TextView time;
	boolean command;
	ImageView iM;
	static Handler handle;
	static boolean running ;
	//private static boolean cut;
	
	
	public MultipleCopyDialog(Context context,ArrayList<Item> obj,int windowSize,String dest,boolean comm) {
		// TODO Auto-generated constructor stub
		mContext = context;
		DEST = dest;
		command = comm;
		si = 0;
	//	BUFFER = 256;
		list = obj;
		running = true;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.copy_dialog);
		dialog.setCancelable(false);
		dialog.getWindow().getAttributes().width = windowSize;
		progress = (ProgressBar)dialog.findViewById(R.id.progress);
		
		//cut = command;
		
		iM = (ImageView)dialog.findViewById(R.id.headerImage);
		iM.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_file_task));
		btn1 = (Button)dialog.findViewById(R.id.copyOk);
		btn2 = (Button)dialog.findViewById(R.id.copyCancel);
		btn1.setVisibility(View.GONE);
		copyTo = (TextView)dialog.findViewById(R.id.copyTo);
		copyFrom = (TextView)dialog.findViewById(R.id.copyFrom);
		copying  = (TextView)dialog.findViewById(R.id.currentFile);
		time = (TextView)dialog.findViewById(R.id.timeLeft);
		contentSize = (TextView)dialog.findViewById(R.id.copyFileSize);
		len = list.size();
		
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				running = false;
				handle.sendEmptyMessage(10);				
			}
		});
		
		copyTo.setText(mContext.getResources().getString(R.string.copyingto) + " "+DEST);
		
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0 :
							copying.setText(mContext.getResources().getString(R.string.copying) + " " + cop);						
							break;
							
					case 1:
							copyFrom.setText(mContext.getResources().getString(R.string.copyingfrom) + " " + copFrom);
							break;
							
					case 2:
							progress.setMax((int)max);
							contentSize.setText(copSize);
							break;
					case 3:
							time.setText(status);
							progress.setProgress((int)si);
							break;
							
					case 4:
						
							progress.setProgress(0);
							break;
							
					case 10:
							if(dialog.isShowing()){
								//scanning the files into the local android db after performing the copying operation....
								try{
									mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(new File(Constants.PATH))));
								}catch(Exception e){
									mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Constants.PATH))));
								}
								dialog.dismiss();
								if(running){
									mContext.sendBroadcast(new Intent("FQ_COPY"));
									Toast.makeText(mContext, mContext.getResources().getString(R.string.copsuccess), Toast.LENGTH_SHORT).show();
									running = false;
								}else
									Toast.makeText(mContext, mContext.getResources().getString(R.string.copintr), Toast.LENGTH_SHORT).show();
								
							}	
				}
			}
		};
		
		
		
		startCopying();
	}

	public void startCopying(){
		dialog.show();
		Thread thr = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file,Dest = new File(DEST);
				//String formated = formatsize();
				for(int i = 0 ; i<len && running;++i){
					try{
						file = list.get(i).getFile();
						if(file!=null){
							copyToDirectory(file.getPath(), DEST);
							if(command){
								if(Dest.canWrite())
									deleteTargetForCut(file);
								else{
									RootTools.deleteFileOrDirectory(file.getPath(), false);
								}
							}	
						}
					}catch(NullPointerException e){
						
					}
				}
				//COPYING DONE NOW REGENERATING KEYS AND EXITING DIALOG....
				regenerate_keys();
				handle.sendEmptyMessage(10);
			}
		});
		thr.start();
	}
	
	
	/**
	 * 
	 * @param old
	 *            the file to be copied
	 * @param newDir
	 *            the directory to move the file to
	 * @return
	 */
	public int copyToDirectory(String old, String newDir) {
		File old_file = new File(old);
		File temp_dir = new File(newDir);
		byte[] data = new byte[Constants.BUFFER];
		int read = 0;

		if (old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()) {
			String file_name = old
					.substring(old.lastIndexOf("/"), old.length());
			File cp_file = new File(newDir + file_name);
			
			/*
			if (cp_file.exists())
				return -2;*/

			try {
				//sending file name that is being copied...
				cop = old_file.getName();
				handle.sendEmptyMessage(0);
				
				//sending file that is being copied...
				copFrom = old_file.getAbsolutePath();
				copFrom = copFrom.substring(0,copFrom.lastIndexOf("/"));
				handle.sendEmptyMessage(1);
				
				BufferedOutputStream o_stream = new BufferedOutputStream(
						new FileOutputStream(cp_file));
				BufferedInputStream i_stream = new BufferedInputStream(
						new FileInputStream(old_file));
				
				max = old_file.length();
				copSize = size(max);
				handle.sendEmptyMessage(2);
				si = 0;
				handle.sendEmptyMessage(4);
				while ((read = i_stream.read(data, 0, Constants.BUFFER)) != -1 && running){
					si=si+read;
					status = status(si);
					handle.sendEmptyMessage(3);
					o_stream.write(data, 0, read);
				}	
				
				o_stream.flush();
				i_stream.close();
				o_stream.close();
				
				//adding the copied file to file gallery....
				addToFileGallery(cp_file);
								
			} catch (FileNotFoundException e) {
				Log.e("FileNotFoundException", e.getMessage());
				return -1;

			} catch (IOException e) {
				Log.e("IOException", e.getMessage());
				return -1;

			}

		} else if (old_file.isDirectory() && temp_dir.isDirectory()
				&& temp_dir.canWrite()) {
			String files[] = old_file.list();
			String dir = newDir
					+ old.substring(old.lastIndexOf("/"), old.length());
			int len = files.length;

			if (!new File(dir).mkdir())
				return -1;

			for (int i = 0; i < len; i++)
				copyToDirectory(old + "/" + files[i], dir);

		} else if (old_file.isFile() && !temp_dir.canWrite()) {
			
			//sending file name that is being copied...
			cop = old_file.getName();
			handle.sendEmptyMessage(0);
			
			//sending file that is being copied...
			copFrom = old_file.getAbsolutePath();
			copFrom = copFrom.substring(0,copFrom.lastIndexOf("/"));
			handle.sendEmptyMessage(1);
			max = old_file.length();
			copSize = size(max);
			handle.sendEmptyMessage(2);
			si = 0;
			handle.sendEmptyMessage(4);
			status = mContext.getString(R.string.reqroot);
			handle.sendEmptyMessage(3);
			int root = moveCopyRoot(old, newDir);

			if (root == 0)
				return 0;
			else
				return -1;

		} else if (!temp_dir.canWrite())
			return -1;

		return 0;
	}
	
	
	// Move or Copy with Root Access using RootTools library
		private static int moveCopyRoot(String old, String newDir) {
			try {
				File f = new File(old);
				if (LinuxShell.isRoot()) {
					if (!readReadWriteFile()) {
						RootTools.remount(newDir + "/"+f.getName(), "rw");
					}
					RootTools.copyFile("'"+old+"'","'"+ newDir+"'", true, true);
					return 0;
				} else {
					return -1;
				}
			} catch (Exception e){
				return -1;
			}
		}
	
	
	
	/**
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	public static String size(long size){
		if(size>Constants.GB){
			return String.format(mContext.getResources().getString(R.string.fsizegb), (double)size/(Constants.GB));
		}
		else if(size > Constants.MB){
			return String.format(mContext.getResources().getString(R.string.fsizemb), (double)size/(Constants.MB));
		}
		else if(size>1024){
			return String.format(mContext.getResources().getString(R.string.fsizekb), (double)size/1024);
		}
		else{
			return String.format(mContext.getResources().getString(R.string.fsizebyte), (double)size);
		}	
	}
	
	
	/**
	 * 
	 * @param size
	 * @return
	 */
	public static String status(long size){
		if(size>Constants.GB){
			return String.format(mContext.getResources().getString(R.string.copstatusgb), (double)size/(Constants.GB));
		}
		else if(size > Constants.MB){
			return String.format(mContext.getResources().getString(R.string.copstatusmb), (double)size/(Constants.MB));
		}
		else if(size>1024){
			return String.format(mContext.getResources().getString(R.string.copstatuskb), (double)size/1024);
		}
		else{
			return String.format(mContext.getResources().getString(R.string.copstatusbyte), (double)size);
		}	
	}
	
	
	/**
	 * 
	 * @param file
	 */
	public void deleteTargetForCut(File file) {
		File target = file;
		if(target.exists() && target.isFile() && target.canWrite()){
			remove_from_gallery(target);
			target.delete();
		}
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				remove_from_gallery(target);
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteTargetForCut(temp_f);
					else if(temp_f.isFile()){
						remove_from_gallery(temp_f);
						temp_f.delete();
					}	
				}
			}
			if(target.exists())
				if(target.delete()){
					remove_from_gallery(target);
				}
		}
	}
	
	
	
	
	// Check if system is mounted
		private static boolean readReadWriteFile() {
			File mountFile = new File("/proc/mounts");
			StringBuilder procData = new StringBuilder();
			if (mountFile.exists()) {
				try {
					FileInputStream fis = new FileInputStream(mountFile.toString());
					DataInputStream dis = new DataInputStream(fis);
					BufferedReader br = new BufferedReader(new InputStreamReader(
							dis));
					String data;
					while ((data = br.readLine()) != null) {
						procData.append(data + "\n");
					}

					br.close();
				} catch (Exception e) {
					e.printStackTrace();
					return false;			
				}
				if (procData.toString() != null) {
					String[] tmp = procData.toString().split("\n");
					for (int x = 0; x < tmp.length; x++) {
						// Kept simple here on purpose different devices have
						// different blocks
						if (tmp[x].contains("/dev/block")
								&& tmp[x].contains("/system")) {
							if (tmp[x].contains("rw")) {
								// system is rw
								return true;
							} else if (tmp[x].contains("ro")) {
								// system is ro
								return false;
							} else {
								return false;
							}
						}
					}
				}
			}
			return false;
		}	
		
		
		/**
		 * function to add the items to file gallery on copying file....
		 * @param f
		 */
		private static void addToFileGallery(File f ){
			
			String name = f.getName().toLowerCase(Locale.ENGLISH);
			String path = f.getPath();
			
			if(name.endsWith(".zip")){
				Item itm = new Item(f, Utils.arcImg, Utils.arcType, "");
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
				
			}else if(name.endsWith(".7z")){
				Item itm = new Item(f, Utils.res.getDrawable(R.drawable.ic_launcher_7zip),
						Utils.arcType, "");
				
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
				
			}else if(name.endsWith(".rar")){
				Item itm = new Item(f, Utils.res.getDrawable(R.drawable.ic_launcher_rar),
						Utils.arcType, "");
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
				
			}else if(name.endsWith(".tar")||name.endsWith(".tar.gz")||
					name.endsWith(".TAT.BZ2")){
				Item itm = new Item(f, Utils.res.getDrawable(R.drawable.ic_launcher_7zip),
						Utils.arcType, "");
				
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
							
			}
			else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
					||name.endsWith(".amr")){
				Item itm = new Item(f, Utils.musicImg, Utils.musicType, "");
				
				Utils.musicKey.put(""+Utils.musCounter++, path);
				Utils.music.put(path, itm);
				Utils.musicsize+=f.length();
				Utils.msize = size(Utils.musicsize);
				
			}
			else if(name.endsWith(".apk")){
				Item itm = new Item(f, Utils.apkImg, Utils.apkType, "");
				
				Utils.appKey.put(""+Utils.appCounter++, path);
				Utils.apps.put(path, itm);
				Utils.apksize+=f.length();
				Utils.asize = size(Utils.apksize);
									
				
			}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
					||name.endsWith(".mkv")){
				Item itm = new Item(f, Utils.vidImg, Utils.vidType, "");
				
				Utils.videoKey.put(""+Utils.vidCounter++, path);
				Utils.vids.put(path, itm);
				Utils.vidsize+=f.length();
				Utils.vsize = size(Utils.vidsize);
				
				
			}	
			else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
					||name.endsWith(".png")){
				Item itm = new Item(f, Utils.imageImg, Utils.imageType, "");
				
				Utils.imgKey.put(""+Utils.imgCounter++, path);
				Utils.img.put(path, itm);
				Utils.imgsize+=f.length();
				Utils.psize = size(Utils.imgsize);
				
				
			}else if(name.endsWith(".pdf")){
				Item itm = new Item(f,Utils.res.getDrawable(R.drawable.ic_launcher_adobe),
									Utils.res.getString(R.string.pdf),"");
				
				Utils.docKey.put(""+Utils.docCounter++, path);
				Utils.doc.put(path, itm);
				Utils.docsize+=f.length();
				Utils.dsize = size(Utils.docsize);
				
			}else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
					||name.endsWith(".pptx")||name.endsWith(".csv")){
				Item itm = new Item(f , Utils.docImg , Utils.docType , "");
				
				Utils.docKey.put(""+Utils.docCounter++, path);
				Utils.doc.put(path, itm);
				Utils.docsize+=f.length();
				Utils.dsize = size(Utils.docsize);
				
			
			}else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")){
				Item itm = new Item(f,Utils.res.getDrawable(R.drawable.ic_launcher_text),
									Utils.res.getString(R.string.text), "");
				
				Utils.docKey.put(""+Utils.docCounter++, path);
				Utils.doc.put(path, itm);
				Utils.docsize+=f.length();
				Utils.dsize = size(Utils.docsize);
					
			}
			else{
				Item itm = new Item(f,Utils.misImg, Utils.misType, "");
				
				Utils.misKey.put(""+Utils.misCounter++, path);
				Utils.mis.put(path, itm);
				Utils.missize+=f.length();
				Utils.misize = size(Utils.missize);
								
			}		
		}
		
		/**
		 * 
		 * @param file to be removed from file gallery....
		 */
		private void remove_from_gallery(File file){
			if(file.isDirectory())
				return;
			String path = file.getAbsolutePath();
			String virtualPath = null;
			
			//checks whether the given path can become the key or not,
			//if not it makes it a key for the hashmap....
			//if not able to generate the key,then return....
			try{
				if(!path.startsWith(Constants.PATH)){
					if(path.startsWith(Constants.LEGACY_PATH)){
						virtualPath = path.substring(Constants.LEGACY_PATH.length(), path.length());
						path = Constants.PATH + virtualPath ;
					}else if(path.startsWith(Constants.EMULATED_PATH)){
						virtualPath = path.substring(Constants.EMULATED_PATH.length(), path.length());
						path = Constants.PATH + virtualPath ;
					}else if(path.startsWith("/sdcard")){
						String str = "/sdcard";
						virtualPath = path.substring(str.length(), path.length());
						path = Constants.PATH + virtualPath ;
					}else if(path.startsWith("/mnt/sdcard")){
						String str = "/mnt/sdcard";
						virtualPath = path.substring(str.length(), path.length());
						path = Constants.PATH + virtualPath ;
					}else if(path.startsWith("/storage/sdcard0")){
						String str = "/storage/sdcard0";
						virtualPath = path.substring(str.length(), path.length());
						path = Constants.PATH + virtualPath ;
					}else if(path.startsWith("/storage/sdcard")){
						String str = "/storage/sdcard";
						virtualPath = path.substring(str.length(), path.length());
						path = Constants.PATH + virtualPath ;
					}else if(path.startsWith("/storage/sd")){
						String str = "/storage/sd";
						virtualPath = path.substring(str.length(), path.length());
						path = Constants.PATH + virtualPath ;
					}else
						return;
				}
			}catch(Exception e){
				path = file.getAbsolutePath();
			}
			
			//removes item from music list of file gallery...
			if(Utils.music.get(path) != null){
				music_deleted = true;
				Utils.musicsize -= file.length();
				Utils.music.remove(path);
			}	
			
			//removes item from app list of file gallery...
			else if(Utils.apps.get(path) != null){
				app_deleted = true;
				Utils.apksize -= file.length();
				Utils.apps.remove(path);
			}	
			
			//removes item from image list of file gallery...
			else if(Utils.img.get(path) != null){
				img_deleted = true;
				Utils.imgsize -= file.length();
				Utils.img.remove(path);
			}	
			
			//removes item from video list of file gallery...
			else if(Utils.vids.get(path) != null){
				vid_deleted = true;
				Utils.vidsize -= file.length();
				Utils.vids.remove(path);
			}	
			
			//removes item from document list of file gallery...
			else if(Utils.doc.get(path) != null){
				doc_deleted = true;
				Utils.docsize -= file.length();
				Utils.doc.remove(path);
			}	
			
			//removes item from zip list of file gallery...
			else if(Utils.zip.get(path) != null){
				zip_deleted = true;
				Utils.zipsize -= file.length();
				Utils.zip.remove(path);
			}	
			
			//removes item from unknown list of file gallery...
			else if(Utils.mis.get(path) != null){
				mis_deleted = true;
				Utils.missize -= file.length();
				Utils.mis.remove(path);
			}	
		}	
		
		/**
		 * function to regenerate keys for file gallery items....
		 */
		void regenerate_keys(){
			
			//regenerating music item's keys....
			if(music_deleted){
				Utils.musicKey.clear();
				int counter = 0;
				for(Map.Entry< String , Item> entry : Utils.music.entrySet()){
					Utils.musicKey.put(""+counter++, entry.getKey());
				}
			}
			
			//regenerating app item's keys....
			if(app_deleted){
				Utils.appKey.clear();
				int counter = 0;
				for(Map.Entry< String , Item> entry : Utils.apps.entrySet()){
					Utils.appKey.put(""+counter++, entry.getKey());
				}
			}
			
			//regenerating image item's keys....
			if(img_deleted){
				Utils.imgKey.clear();
				int counter = 0;
				for(Map.Entry< String , Item> entry : Utils.img.entrySet()){
					Utils.imgKey.put(""+counter++, entry.getKey());
				}
			}
			
			//regenerating video item's keys....
			if(vid_deleted){
				Utils.videoKey.clear();
				int counter = 0;
				for(Map.Entry< String , Item> entry : Utils.vids.entrySet()){
					Utils.videoKey.put(""+counter++, entry.getKey());
				}
			}
			
			//regenerating document item's keys....
			if(doc_deleted){
				Utils.docKey.clear();
				int counter = 0;
				for(Map.Entry< String , Item> entry : Utils.doc.entrySet()){
					Utils.docKey.put(""+counter++, entry.getKey());
				}
			}
			
			//regenerating archive item's keys....
			if(zip_deleted){
				Utils.zipKey.clear();
				int counter = 0;
				for(Map.Entry< String , Item> entry : Utils.zip.entrySet()){
					Utils.zipKey.put(""+counter++, entry.getKey());
				}
			}
			
			//regenerating unknown item's keys....
			if(mis_deleted){
				Utils.misKey.clear();
				int counter = 0;
				for(Map.Entry< String , Item> entry : Utils.mis.entrySet()){
					Utils.misKey.put(""+counter++, entry.getKey());
				}
			}
		}
}
