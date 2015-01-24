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

package org.anurag.compress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.anurag.file.quest.AppBackup;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * @author Anurag
 *
 */
@SuppressLint("HandlerLeak")
public class CreateZipApps {

	byte[] data = new byte[Constants.BUFFER];
	static Handler handle;
	boolean running;
	ZipOutputStream zout;
	FileInputStream fin;
	ZipEntry entry;
	String DEST = Environment.getExternalStorageDirectory().getAbsolutePath()+"/File Quest/Zip Backup";
	long prog;
	long max;
	
	String main;
	String deletename;
	String fsize;
	String fname;
	String stat;
	
	PackageManager mPack;
	/**
	 * 
	 * @param ctx
	 * @param width
	 * @param list
	 */
	public CreateZipApps(final Context ctx,int width , final ArrayList<ApplicationInfo> list) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, Constants.DIALOG_STYLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.zip_file_dialog);
		running = false;
		dialog.getWindow().getAttributes().width = width;
		try{
			if(!new File(DEST).exists())
				new File(DEST).mkdirs();
		}catch(Exception e){
			
		}
		mPack = ctx.getPackageManager();
		final Button cancel = (Button)dialog.findViewById(R.id.zipCalcelButton);
		final EditText getname = (EditText)dialog.findViewById(R.id.getArchiveName);
		final ProgressBar progress = (ProgressBar)dialog.findViewById(R.id.zipProgressBar);
		final TextView destination = (TextView)dialog.findViewById(R.id.zipLoc);
		final TextView filesize = (TextView)dialog.findViewById(R.id.zipSize);
		final TextView filename = (TextView)dialog.findViewById(R.id.zipNoOfFiles);
		final TextView status = (TextView)dialog.findViewById(R.id.zipFileLocation);
		
		final CheckBox delete = (CheckBox)dialog.findViewById(R.id.zipChioce);
		final CheckBox keep = (CheckBox)dialog.findViewById(R.id.tarChioce);
		TextView opt = (TextView)dialog.findViewById(R.id.zipFormat);
		TextView til = (TextView)dialog.findViewById(R.id.zipFileTitle);
		til.setText(ctx.getString(R.string.ziprecovery));
		
		opt.setVisibility(View.GONE);
		delete.setVisibility(View.GONE);
		keep.setVisibility(View.GONE);
		
		filesize.setVisibility(View.VISIBLE);
		filename.setVisibility(View.VISIBLE);
		status.setVisibility(View.VISIBLE);
		
		if(list.size()==1){
			getname.setText(list.get(0).loadLabel(mPack));
			filename.setText(ctx.getString(R.string.currentfile) +" "+list.get(0).loadLabel(mPack) );
			filesize.setText(AppBackup.size(new File(list.get(0).sourceDir).length(), ctx));
			status.setText(AppBackup.status(0, ctx));
		}else{
			getname.setText("MULTIPLE_ZIP_BACKUP");
			filename.setText(ctx.getString(R.string.currentfile) +" ?");
			filesize.setText(ctx.getString(R.string.size));
			status.setText(AppBackup.status(0, ctx));
		}
		/**
		 * GENERATING DESTINATION PATH FOR ZIP FILE...
		 */
		
		
		destination.setText(ctx.getString(R.string.dest)+" "+DEST);
		
		
		
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0:
							prog = 0;
							progress.setProgress(0);
							filesize.setText(fsize);
							filename.setText(fname);
							progress.setMax((int)max);
							break;
							
					case 1:
							status.setText(stat);
							progress.setProgress((int)prog);
							filesize.setText(fsize);
							break;
						
					case 2:
							
							status.setText(ctx.getString(R.string.deleteingfile));
							filename.setText(fname);
							break;
							
					case 3:
							if(running){
								running=false;
								//SENDING BROADCAST TO RELOAD THE LIST ON COMPLETION OF ZIP
								//OPERATION....
								ctx.sendBroadcast(new Intent("FQ_DELETE"));
								cancel.setText(ctx.getString(R.string.ok));
								status.setText(ctx.getString(R.string.zipsuccessful));
								progress.setVisibility(View.GONE);
								filename.setVisibility(View.GONE);
								filesize.setVisibility(View.GONE);
							}else{
								
								try{
									//deleting file when zipping is interrupted...
									new File(main).delete();
								}catch(Exception e){
									
								}
								//SENDING BROADCAST TO RELOAD THE LIST ON COMPLETION OF ZIP
								//OPERATION....
								ctx.sendBroadcast(new Intent("FQ_DELETE"));
								dialog.dismiss();
								Toast.makeText(ctx, ctx.getString(R.string.zipinterrupted), Toast.LENGTH_SHORT).show();
							}
							break;
					case 4:
							Toast.makeText(ctx, ctx.getString(R.string.ziperror), Toast.LENGTH_SHORT).show();
						
				}
			}
		};
		
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(running && zout!=null){
					int len = list.size();
					for(int i=0;i<len;++i){
						ApplicationInfo file = list.get(i);
						if(file!=null){
							zip_It(file,ctx);
						}
					}
					try {
						zout.flush();
						zout.close();
						
						addToFileGallery(new File(main));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					handle.sendEmptyMessage(3);
				}else
					//UNABLE TO CREATE ZIP OUTPUT STREAM....SEND ERROR...
					handle.sendEmptyMessage(4);
			}

			
		});
		
		final Button start = (Button)dialog.findViewById(R.id.zipOkButton);
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				running = true;
				dialog.setCancelable(false);
				if(DEST.endsWith("/"))
					DEST = DEST.substring(0, DEST.length()-1);
				try {
					main = DEST+"/"+getname.getText().toString()+".zip";
					zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(main))));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					zout=null;
				}
				delete.setEnabled(false);
				keep.setEnabled(false);
				progress.setVisibility(View.VISIBLE);
				
				getname.setEnabled(false);
				thread.start();
				start.setVisibility(View.GONE);
			}
		});
		
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//dialog.dismiss();
				//running  =false;
				if(running){
					running = false;
					handle.sendEmptyMessage(3);
				}else{
					ctx.sendBroadcast(new Intent("FQ_ARCHIVE_CREATED"));
					dialog.dismiss();
				}	
			}
		});
		dialog.show();
	}
	
	private void zip_It(ApplicationInfo file , Context ctx) {
		// TODO Auto-generated method stub
		String entname = file.loadLabel(mPack).toString()+".apk";
		entry = new ZipEntry(entname);
		try {
			int read;
			zout.putNextEntry(entry);
			fin = new FileInputStream(file.sourceDir);
			fsize = AppBackup.size(new File(file.sourceDir).length(), ctx);
			fname = ctx.getString(R.string.currentfile)+" "+file.loadLabel(mPack);
			max = new File(file.sourceDir).length();
			handle.sendEmptyMessage(0);
			while((read=fin.read(data, 0, Constants.BUFFER))!=-1 && running){
				prog+=read;
				stat = AppBackup.status(prog, ctx);
				handle.sendEmptyMessage(1);
				zout.write(data, 0, read);
			}
			fin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * function to the backed up apk to file gallery list....
	 * @param f
	 */
	private void addToFileGallery(File f){
		String path = f.getPath();
		Item itm = new Item(f, Constants.ARCHIVE , Utils.arcType, "");
		Utils.zipKey.put(""+Utils.zipCounter++, path);
		Utils.zip.put(path, itm);
		Utils.zipsize+=f.length();
		Utils.zsize = Utils.size(Utils.zipsize);
	}
}
