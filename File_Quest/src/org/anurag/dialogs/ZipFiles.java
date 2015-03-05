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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.dialogs;

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
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;


/**
 * 
 * @author Anurag
 *
 */
public class ZipFiles {

	private byte[] data = new byte[Constants.BUFFER];
	private Handler handle;
	private boolean running;
	private ZipOutputStream zout;
	private FileInputStream fin;
	private ZipEntry entry;
	private String DEST;
	private long prog;
	private long max;
	
	private String main;
	//private String deletename;
	private String fsize;
	private String fname;
	private String stat;
	
	private final CheckBox keep;
	private final CheckBox delete;
	private EditText getname;
	private ProgressBar progress;
	private TextView destination;
	private TextView filesize;
	private TextView filename;
	private TextView status;
	
	private Thread thread;
	
	/**
	 * 
	 * @param ctx
	 * @param width
	 * @param list
	 */
	public ZipFiles(final Context ctx, final ArrayList<Item> list) {
		// TODO Auto-generated constructor stub
		running = false;
		
		LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inf.inflate(R.layout.zip_file_dialog, null , false);
		
		Builder builder = new MaterialDialog.Builder(ctx);
		builder.title(R.string.action_zip)
		.positiveText(R.string.action_zip)
		.negativeText(R.string.dismiss)
		.autoDismiss(false)
		.customView(view, true)
		.callback(new ButtonCallback() {

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				
				if(running){
					//already background thread is running
					return;
				}
				
				running = true;
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
				filesize.setVisibility(View.VISIBLE);
				filename.setVisibility(View.VISIBLE);
				status.setVisibility(View.VISIBLE);
				getname.setEnabled(false);
				thread.start();
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				if(running){
					running = false;
					handle.sendEmptyMessage(3);
				}else
					dialog.dismiss();
			}
			
		});
		
		final MaterialDialog dial = builder.show();
				
		getname = (EditText)view.findViewById(R.id.getArchiveName);
		progress = (ProgressBar)view.findViewById(R.id.zipProgressBar);
		destination = (TextView)view.findViewById(R.id.zipLoc);
		filesize = (TextView)view.findViewById(R.id.zipSize);
		filename = (TextView)view.findViewById(R.id.zipNoOfFiles);
		status = (TextView)view.findViewById(R.id.zipFileLocation);
		
		delete = (CheckBox)view.findViewById(R.id.zipChioce);
		keep = (CheckBox)view.findViewById(R.id.tarChioce);
		
		delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
					keep.setChecked(false);
			}
		});
		
		keep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
					delete.setChecked(false);
			}
		});
		
		if(list.size()==1){
			getname.setText(list.get(0).getName());
			
		}else{
			getname.setText("MULTIPLE_FILE");
		}
		
		/**
		 * GENERATING DESTINATION PATH FOR ZIP FILE...
		 */
		switch(FileQuestHD.getCurrentItem()){
		case 1:
			DEST = RootPanel.get_current_working_dir().getPath();
			break;
			
		case 2:
			DEST= SdCardPanel.get_current_working_dir().getPath(); 
			break;
		}
		
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
							if(!running){
								try{
									new File(main).delete();
								}catch(Exception e){
									
								}
								Toast.makeText(ctx, (R.string.zipinterrupted), Toast.LENGTH_SHORT).show();
							}else{
								
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
								ctx.sendBroadcast(new Intent("FQ_DELETE"));
								Toast.makeText(ctx, R.string.items_zipped, Toast.LENGTH_SHORT).show();
							}
							dial.dismiss();
							break;
					case 4:
							Toast.makeText(ctx, ctx.getString(R.string.ziperror), Toast.LENGTH_SHORT).show();
						
				}
			}
		};
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(running && zout!=null){
					int len = list.size();
					for(int i=0;i<len;++i){
						try{
							File file = list.get(i).getFile();
							if(file!=null){
								zip_It(file,ctx);
							}
						}catch(NullPointerException e){
							
						}
					}
					try {
						zout.flush();
						zout.close();
						
						//adding to file gallery....
						addToFileGallery(new File(main));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(delete.isChecked() && running)
						for(int i=0;i<len&&running;++i){
							File file = list.get(i).getFile();
								if(file!=null){
									deleteFile(file , ctx);
								}						
						}
					handle.sendEmptyMessage(3);
				}
			}		
		});
		/*
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				running = true;
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
				filesize.setVisibility(View.VISIBLE);
				filename.setVisibility(View.VISIBLE);
				status.setVisibility(View.VISIBLE);
				getname.setEnabled(false);
				thread.start();
			}
		});*/
		
		/*
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//dialog.dismiss();
				//running  =false;
				if(running){
					running = false;
					handle.sendEmptyMessage(3);
				}else
					dialog.dismiss();
			}
		});*/
	}
	
	private void zip_It(File file , Context ctx) {
		// TODO Auto-generated method stub
		if(file.isFile()){
			String entname = file.getAbsolutePath();
			entname = entname.substring(DEST.length(), entname.length());
			if(!entname.startsWith("/"))
				entname = "/"+entname;
			entry = new ZipEntry(entname);
			try {
				int read;
				zout.putNextEntry(entry);
				fin = new FileInputStream(file);
				fsize = AppBackup.size(file.length(), ctx);
				fname = ctx.getString(R.string.currentfile)+" "+file.getName();
				max = file.length();
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
		}else
			for(File f:file.listFiles())
				zip_It(f,ctx);
	}
	
	/**
	 * 
	 * @param file
	 */
	public void deleteFile(File file , Context ctx) {
		File target = file;
		if(target.exists() && target.isFile() && target.canWrite()){
			fname = ctx.getString(R.string.currentfile)+" "+file.getName();
			fsize = AppBackup.size(file.length(), ctx);
			handle.sendEmptyMessage(2);
			target.delete();
			
		}	
		
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteFile(temp_f , ctx);
					else if(temp_f.isFile()){
						fname = ctx.getString(R.string.currentfile)+" "+file.getName();
						fsize = AppBackup.size(file.length(), ctx);
						handle.sendEmptyMessage(2);
						temp_f.delete();
					}	
				}
			}
			if(target.exists())
				if(target.delete()){}
		}
	}
	
	/**
	 * function to add the zipped item to file gallery list....
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
