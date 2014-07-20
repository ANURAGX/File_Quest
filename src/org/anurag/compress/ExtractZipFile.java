/**
 * Copyright(c) 2014 ANURAG 
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.anurag.file.quest.AppBackup;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.OpenFileDialog;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Anurag
 *
 */
public class ExtractZipFile {
	
	boolean running ;
	ZipInputStream zis;
	byte data[] = new byte[Constants.BUFFER];
	long prog ;
	int read ;
	String DEST;
	String name;
	long max;
	public ExtractZipFile(final Context ctx ,final ZipObj zFile , final int width , String extractDir , File file ,final int mode) {
		// TODO Auto-generated constructor stub
		running = false;
		prog = 0;
		read = 0;
		final Dialog dialog = new Dialog(ctx, R.style.custom_dialog_theme);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.extract_file);
		dialog.getWindow().getAttributes().width = width;
		DEST = extractDir;
		final ProgressBar progress = (ProgressBar)dialog.findViewById(R.id.zipProgressBar);
		final TextView to = (TextView)dialog.findViewById(R.id.zipFileName);
		final TextView from = (TextView)dialog.findViewById(R.id.zipLoc);
		final TextView cfile = (TextView)dialog.findViewById(R.id.zipSize );
		final TextView zsize = (TextView)dialog.findViewById(R.id.zipNoOfFiles);
		final TextView status = (TextView)dialog.findViewById(R.id.zipFileLocation);
		
		
		if(extractDir==null)
			to.setText(ctx.getString(R.string.extractingto)+" Cache directory");
		else
			to.setText(ctx.getString(R.string.extractingto)+" "+DEST);
		
		from.setText(ctx.getString(R.string.extractingfrom)+" "+file.getName());
		
		Button cancel = (Button)dialog.findViewById(R.id.calcelButton);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		try {
			zis = new ZipInputStream(new FileInputStream(file));
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			zis = null;
		}
		
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0:
						
							progress.setProgress(0);
							cfile.setText(ctx.getString(R.string.extractingfile)+" "+name);
							break;
							
					case 1:
							status.setText(name);
							progress.setProgress((int)prog);
							break;
					case 2:
							dialog.dismiss();
						    if(mode==0){
						    	//after extracting file ,it has to be opened....
						    	new OpenFileDialog(ctx, Uri.parse(DEST), width);
						    }else{
						    	Toast.makeText(ctx, ctx.getString(R.string.fileextracted),Toast.LENGTH_SHORT).show();
						    }
						    
						    break;
					case 3:
							progress.setMax((int)max);
				}
			}
		};
		
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(running){
					if(DEST==null)
						DEST = Environment.getExternalStorageDirectory()+"/Android/data/org.anurag.file.quest";
					new File(DEST).mkdirs();
					name = zFile.getEntry().substring(zFile.getEntry().lastIndexOf("/"), zFile.getEntry().length());
					handle.sendEmptyMessage(0);
					DEST = DEST + "/" + name;
					ZipEntry ze;
					try {
						while((ze=zis.getNextEntry())!=null){
							if(ze.getName().equalsIgnoreCase(zFile.getEntry())){
								try {
									FileOutputStream out = new FileOutputStream((DEST));
									max = ze.getSize();
									handle.sendEmptyMessage(3);
									while((read=zis.read(data))!=-1){
										out.write(data, 0, read);
										prog+=read;
										name = AppBackup.status(prog, ctx);
										handle.sendEmptyMessage(1);
									}										
									out.flush();
									out.close();
									zis.close();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch(IOException e){
									
								}
							}
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handle.sendEmptyMessage(2);
				}
			}
		});
		
		dialog.show();
		running = true;
		thread.start();
		dialog.setCancelable(false);
		progress.setVisibility(View.VISIBLE);
	}
}
