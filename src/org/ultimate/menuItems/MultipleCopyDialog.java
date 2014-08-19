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

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import org.ultimate.root.LinuxShell;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

@SuppressLint("HandlerLeak")
public class MultipleCopyDialog {
	
	
	static long max;
	static String copFrom;
	static String copSize;
	static String cop;
	static String status;
	ProgressBar progress;
	static Context mContext;
	//static int BUFFER = 256;
	Dialog dialog;
	ArrayList<File> list;
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
	
	
	
	public MultipleCopyDialog(Context context,ArrayList<File> obj,int windowSize,String dest,boolean comm) {
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
								dialog.dismiss();
								if(running){
									mContext.sendBroadcast(new Intent("FQ_DELETE"));
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
					file = (File) list.get(i);
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
				}
				//COPYING DONE NOW EXITING DIALOG....
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
	public static int copyToDirectory(String old, String newDir) {
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
		if(target.exists() && target.isFile() && target.canWrite())
			target.delete();
		
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteTargetForCut(temp_f);
					else if(temp_f.isFile())
						temp_f.delete();
				}
			}
			if(target.exists())
				if(target.delete()){}
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
}
