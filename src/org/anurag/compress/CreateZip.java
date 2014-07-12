/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * @author Anurag
 *
 */
public class CreateZip {

	byte[] data = new byte[Constants.BUFFER];
	static Handler handle;
	boolean running;
	ZipOutputStream zout;
	FileInputStream fin;
	ZipEntry entry;
	String DEST;
	long prog;
	long max;
	
	
	String fsize;
	String fname;
	String stat;
	/**
	 * 
	 * @param ctx
	 * @param width
	 * @param list
	 */
	public CreateZip(final Context ctx,int width , final ArrayList<File> list) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, R.style.custom_dialog_theme);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.zip_file_dialog);
		running = false;
		dialog.getWindow().getAttributes().width = width;
		final Button cancel = (Button)dialog.findViewById(R.id.zipCalcelButton);
		final EditText getname = (EditText)dialog.findViewById(R.id.getArchiveName);
		final ProgressBar progress = (ProgressBar)dialog.findViewById(R.id.zipProgressBar);
		final TextView destination = (TextView)dialog.findViewById(R.id.zipLoc);
		final TextView filesize = (TextView)dialog.findViewById(R.id.zipSize);
		final TextView filename = (TextView)dialog.findViewById(R.id.zipNoOfFiles);
		final TextView status = (TextView)dialog.findViewById(R.id.zipFileLocation);
		if(list.size()==1){
			getname.setText(list.get(0).getName());
			
		}else{
			getname.setText("MULTIPLE_FILE");
		}
		/**
		 * GENERATING DESTINATION PATH FOR ZIP FILE...
		 */
		int l=list.size();
		for(int i=0;i<l;++i){
			File file = list.get(i);
			if(file!=null){
				DEST = file.getParent();
				break;
			}
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
							break;
						
					case 2:
							
							break;
							
					case 3:
							if(running){
								running=false;
								cancel.setText(ctx.getString(R.string.ok));
								status.setText(ctx.getString(R.string.zipsuccessful));
								progress.setVisibility(View.GONE);
								filename.setVisibility(View.GONE);
								filesize.setVisibility(View.GONE);
							}else{
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
						File file = list.get(i);
						if(file!=null){
							zip_It(file,ctx);
						}
					}
					try {
						zout.flush();
						zout.close();
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
					zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(DEST+"/"+getname.getText().toString()+".zip"))));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					zout=null;
				}
				progress.setVisibility(View.VISIBLE);
				filesize.setVisibility(View.VISIBLE);
				filename.setVisibility(View.VISIBLE);
				status.setVisibility(View.VISIBLE);
				getname.setEnabled(false);
				thread.start();
				start.setVisibility(View.GONE);
			}
		});
		
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				//handle.sendEmptyMessage(3);
			}
		});
		dialog.show();
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
					zout.write(data, 0, read);
					prog+=read;
					stat = AppBackup.status(prog, ctx);
					handle.sendEmptyMessage(1);
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
	
	
}
