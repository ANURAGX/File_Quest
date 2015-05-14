/**
 * Copyright(c) 2015 DRAWNZER.ORG PROJECTS -> ANURAG
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
 *                            anuraxsharma1512@gmail.com
 *
 */


package org.anurag.dialogs;

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
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.LinuxShell;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.stericson.RootTools.RootTools;

/**
 * 
 * This class performs the copying and cutting of files  
 * 
 * @author anurag
 *
 */
public class CopyDialog {
	
	//boolean values true tells type of file deleted
	private boolean music_deleted;
	private boolean app_deleted;
	private boolean img_deleted;
	private boolean vid_deleted;
	private boolean doc_deleted;
	private boolean zip_deleted;
	private boolean mis_deleted;
	
	private View view;
	
	private long max;
	private String copFrom;
	private String copSize;
	private String cop;
	private String status;
	private ProgressBar progress;
	private Context mContext;
	
	private ArrayList<Item> list;
	private long si = 0;
	private String DEST;
	private int len = 0;
	private TextView copyTo;
	private TextView copyFrom;
	private TextView copying;
	private TextView contentSize;
	private TextView time;
	private boolean cut;
	private Handler handle;
	private boolean running ;
	private boolean error;
	private String errmsg;
	/**
	 * 
	 * @param context
	 * @param obj list of files to be copied
	 * @param dest where to copy the files
	 * @param comm true then cut files to dest
	 */
	public CopyDialog(Context context,ArrayList<Item> obj,String dest,boolean comm) {
		// TODO Auto-generated constructor stub
		mContext = context;
		DEST = dest;
		cut = comm;
		si = 0;
		list = obj;
		
		LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view  = inf.inflate(R.layout.copy_dialog, null , false);
		running = true;
		
		
		
		Builder build = new MaterialDialog.Builder(mContext);
		build.title(R.string.copy)
		.customView(view, true)
		.positiveText(R.string.cancel)
		.autoDismiss(false)
		.callback(new ButtonCallback() {
			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				running = false;
				endOfCopy(dialog);
			}
		});
		
		final MaterialDialog dialog = build.show();
		
		progress = (ProgressBar)view.findViewById(R.id.progress);
		copyTo = (TextView)view.findViewById(R.id.copyTo);
		copyFrom = (TextView)view.findViewById(R.id.copyFrom);
		copying  = (TextView)view.findViewById(R.id.currentFile);
		time = (TextView)view.findViewById(R.id.timeLeft);
		contentSize = (TextView)view.findViewById(R.id.copyFileSize);
		
		len = list.size();
		
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
							endOfCopy(dialog);
							if(error){
								Toast.makeText(mContext, errmsg, Toast.LENGTH_SHORT).show();
							}
				}
			}
		};		
		startCopying();
	}

	/**
	 * this function starts the copying of files to destination in background thread
	 * and notifies the dialog via handler
	 */
	private void startCopying(){
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
							if(error){
								break;
							}
							if(cut){
								if(Dest.canWrite())
									deleteTargetForCut(file);
								else{
									RootTools.deleteFileOrDirectory(file.getPath(), false);
								}
							}	
						}
					}catch(NullPointerException e){
						error = true;
						errmsg = e.toString();
						break;
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
	 * this function invoked when copying is complete or interrupted so that the proper message is
	 * displayed and UI is updated
	 */
	private void endOfCopy(MaterialDialog dial){
		//scanning the files into the local android db after performing the copying operation....
		try{
			mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(new File(Constants.PATH))));
		}catch(Exception e){
			mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Constants.PATH))));
		}
		if(running){
			Toast.makeText(mContext, mContext.getResources().getString(R.string.copsuccess), Toast.LENGTH_SHORT).show();
			running = false;
		}else
			Toast.makeText(mContext, mContext.getResources().getString(R.string.copintr), Toast.LENGTH_SHORT).show();
		
		dial.dismiss();
		
		mContext.sendBroadcast(new Intent("UPDATE_SPACE"));
		switch(FileQuestHD.getCurrentItem()){
		case 0:
			try{
				FileGallery.resetAdapter();
			}catch(Exception e){}
			break;
			
		case 1:
			RootPanel.notifyDataSetChanged();
			break;
			
		case 2:
			SdCardPanel.notifyDataSetChanged();
			break;
		}
		Utils.updateUI();
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
				error = true;
				errmsg = e.toString();
				Log.e("FileNotFoundException", e.getMessage());
				return -1;

			} catch (IOException e) {
				error = true;
				errmsg = e.toString();
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
	private int moveCopyRoot(String old, String newDir) {
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
				error = true;
				errmsg = e.toString();
				return -1;
			}
		}
	
	
	
	/**
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	public String size(long size){
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
	public String status(long size){
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
		private void addToFileGallery(File f ){
			
			String name = f.getName().toLowerCase(Locale.ENGLISH);
			String path = f.getPath();
			
			if(name.endsWith(".zip")){
				Item itm = new Item(f, Constants.ARCHIVE, Utils.arcType, "");
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
				
			}else if(name.endsWith(".7z")){
				Item itm = new Item(f, Constants.ARCHIVE,
						Utils.arcType, "");
				
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
				
			}else if(name.endsWith(".rar")){
				Item itm = new Item(f, Constants.ARCHIVE,
						Utils.arcType, "");
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
				
			}else if(name.endsWith(".tar")||name.endsWith(".tar.gz")||
					name.endsWith(".TAT.BZ2")){
				Item itm = new Item(f, Constants.ARCHIVE,
						Utils.arcType, "");
				
				Utils.zipKey.put(""+Utils.zipCounter++, path);
				Utils.zip.put(path, itm);
				Utils.zipsize+=f.length();
				Utils.zsize = size(Utils.zipsize);
				
							
			}
			else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
					||name.endsWith(".amr")){
				Item itm = new Item(f, Constants.MUSIC, Utils.musicType, "");
				
				Utils.musicKey.put(""+Utils.musCounter++, path);
				Utils.music.put(path, itm);
				Utils.musicsize+=f.length();
				Utils.msize = size(Utils.musicsize);
				
			}
			else if(name.endsWith(".apk")){
				Item itm = new Item(f, Constants.APP, Utils.apkType, "");
				
				Utils.appKey.put(""+Utils.appCounter++, path);
				Utils.apps.put(path, itm);
				Utils.apksize+=f.length();
				Utils.asize = size(Utils.apksize);
									
				
			}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
					||name.endsWith(".mkv")){
				Item itm = new Item(f, Constants.VIDEO, Utils.vidType, "");
				
				Utils.videoKey.put(""+Utils.vidCounter++, path);
				Utils.vids.put(path, itm);
				Utils.vidsize+=f.length();
				Utils.vsize = size(Utils.vidsize);
				
				
			}	
			else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
					||name.endsWith(".png")){
				Item itm = new Item(f, Constants.IMAGE, Utils.imageType, "");
				
				Utils.imgKey.put(""+Utils.imgCounter++, path);
				Utils.img.put(path, itm);
				Utils.imgsize+=f.length();
				Utils.psize = size(Utils.imgsize);
				
				
			}else if(name.endsWith(".pdf")){
				Item itm = new Item(f,Constants.PDF,Utils.res.getString(R.string.pdf),"");
				
				Utils.docKey.put(""+Utils.docCounter++, path);
				Utils.doc.put(path, itm);
				Utils.docsize+=f.length();
				Utils.dsize = size(Utils.docsize);
				
			}else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
					||name.endsWith(".pptx")||name.endsWith(".csv")){
				Item itm = new Item(f , Constants.DOCS , Utils.docType , "");
				
				Utils.docKey.put(""+Utils.docCounter++, path);
				Utils.doc.put(path, itm);
				Utils.docsize+=f.length();
				Utils.dsize = size(Utils.docsize);
				
			
			}else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")){
				Item itm = new Item(f, Constants.DOCS,	Utils.res.getString(R.string.text), "");
				
				Utils.docKey.put(""+Utils.docCounter++, path);
				Utils.doc.put(path, itm);
				Utils.docsize+=f.length();
				Utils.dsize = size(Utils.docsize);
					
			}
			else{
				Item itm = new Item(f, Constants.UNKNOWN, Utils.misType, "");
				
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
