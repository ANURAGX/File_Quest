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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.anurag.file.quest.AppBackup;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.OpenFileDialog;
import org.anurag.file.quest.R;
import org.ultimate.menuItems.BluetoothChooser;

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
	//ZipInputStream zis;
	byte data[] = new byte[Constants.BUFFER];
	long prog ;
	int read ;
	String DEST;
	String name;
	String size;
	long max;
	boolean errors;
	
	Enumeration<? extends ZipEntry> zList;
	
	String dest;
	public ExtractZipFile(final Context ctx ,final ZipObj zFile , final int width , String extractDir ,final File file ,final int mode) {
		// TODO Auto-generated constructor stub
		running = false;
		errors = false;
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
		
		if(mode==2){
			//ZIP ENTRY HAS TO BE SHARED VIA BLUETOOTH,ETC...
			TextView t = (TextView)dialog.findViewById(R.id.preparing);
			t.setText(ctx.getString(R.string.preparingtoshare));
		}
		
		try {
			zList = new ZipFile(file).entries();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			zList = null;
		}
		
	/*	try {
			zis = new ZipInputStream(new FileInputStream(file));
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			zis = null;
		}*/
		
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
							if(running){
								dialog.dismiss();
							    if(mode==0){
							    	//after extracting file ,it has to be opened....
							    	new OpenFileDialog(ctx, Uri.parse(DEST), width);
							    }else if(mode==2){
							    	//FILE HAS TO BE SHARED....
							    	new BluetoothChooser(ctx, new File(dest).getAbsolutePath(), width, null);
							    }
							    else{
							    	if(errors)
							    		Toast.makeText(ctx, ctx.getString(R.string.errorinext), Toast.LENGTH_SHORT).show();
							    	Toast.makeText(ctx, ctx.getString(R.string.fileextracted),Toast.LENGTH_SHORT).show();
							    }
							}
						    
						    break;
					case 3:
							zsize.setText(size);
							progress.setMax((int)max);
							break;
					case 4:
							status.setText(ctx.getString(R.string.preparing));
							break;
					case 5:
							running = false;
							Toast.makeText(ctx, ctx.getString(R.string.extaborted),Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(running){
					if(DEST==null){
						DEST = Environment.getExternalStorageDirectory()+"/Android/data/org.anurag.file.quest";
						new File(DEST).mkdirs();
					}
					
					
					ZipEntry ze;
					while(zList.hasMoreElements()){
					//	++count;
						ze = zList.nextElement();
						handle.sendEmptyMessage(4);
						if(zFile.isFile()){
							//EXTRACTING A SINGLE FILE FROM AN ARCHIVE....
							if(ze.getName().equalsIgnoreCase(zFile.getEntry())){
								try {
								    
									//SENDING CURRENT FILE NAME....
									try{
										name = zFile.getName();
									}catch(Exception e){
										name = zFile.getEntry();
									}
									handle.sendEmptyMessage(0);
									dest = DEST;
									dest = dest + "/"+name;
									FileOutputStream out = new FileOutputStream((dest));
									max = ze.getSize();
									size =AppBackup.size(max, ctx);
									handle.sendEmptyMessage(3);
								//	for(;i<count;++i)
								//		zis.getNextEntry();
									InputStream fin = (new ZipFile(file).getInputStream(ze));
									while((read=fin.read(data))!=-1&&running){
										out.write(data, 0, read);
										prog+=read;
										name = AppBackup.status(prog, ctx);
										handle.sendEmptyMessage(1);
									}										
									out.flush();
									out.close();
									fin.close();
									break;
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									errors = true;
								} catch(IOException e){
									errors = true;
								}
							}
						}else{
							//EXTRACTING A DIRECTORY FROM ZIP ARCHIVE....
							if(ze.getName().startsWith(zFile.getPath())){
								prog = 0;
								dest = DEST;
								name = ze.getName();
								String path = name;
								name = name.substring(name.lastIndexOf("/"), name.length());
								if(!name.startsWith("/"))
									name = "/"+name;
								
								String foname = zFile.getPath();
								if(!foname.startsWith("/"))
									foname = "/"+foname;
								
								path = path.substring(foname.lastIndexOf("/"), path.lastIndexOf("/"));
								if(!path.startsWith("/"))
									path = "/"+path;
								dest = dest+path;
								new File(dest).mkdirs();
								dest = dest+name;
								
								FileOutputStream out;
								try {
									max = ze.getSize();
									out = new FileOutputStream((dest));
									size =AppBackup.size(max, ctx);
									handle.sendEmptyMessage(3);
								
									InputStream fin = (new ZipFile(file).getInputStream(ze));
									while((read=fin.read(data))!=-1&&running){
										out.write(data, 0, read);
										prog+=read;
										name = AppBackup.status(prog, ctx);
										handle.sendEmptyMessage(1);
									}										
									out.flush();
									out.close();
									fin.close();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									errors = true;
								}catch(IOException e){
									errors = true;
								}catch(Exception e){
									errors = true;
								}								
							}
						}
					}				
					
				/*	try {
						zis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					handle.sendEmptyMessage(2);
				}
			}
		});
		
		Button cancel = (Button)dialog.findViewById(R.id.calcelButton);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				handle.sendEmptyMessage(5);
			}
		});
		Button st = (Button)dialog.findViewById(R.id.extractButton);
		st.setVisibility(View.GONE);
		
		dialog.show();
		running = true;
		thread.start();
		dialog.setCancelable(false);
		progress.setVisibility(View.VISIBLE);
	}
}
