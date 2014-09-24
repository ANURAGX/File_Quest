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

package org.anurag.file.quest;

import java.io.File;
import java.util.ArrayList;

import com.stericson.RootTools.RootTools;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/*
 * TODO 
 * UPDATE THE FILE GALLLERY ITEMS AFTER DELETION IN THIS CLASS...
 *  
 */
/**
 * 
 * @author Anurag....
 *
 */
public class DeleteFiles{

	private  ArrayList<Item> file;
	private  TextView popupMessage;
	private TextView message;
	private  Button btn1;
	private  Button btn2;
	private  Thread thread;
	private Handler mHandler;
	Context mContext;
	Dialog dialog;
	String nam;
	public DeleteFiles(Context context,int width,ArrayList<Item> list,String msg) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.delete_files);
		dialog.getWindow().getAttributes().width = width;
		dialog.setCancelable(true);
		file = list;
		nam=msg;
		onCreate(context);
	}
	
	
	protected void onCreate(final Context ctx) {
		btn1 = (Button)dialog.findViewById(R.id.popupOk);
		btn2 = (Button)dialog.findViewById(R.id.popupCancel);
		popupMessage = (TextView)dialog.findViewById(R.id.textMessage);
		if(nam==null)
			popupMessage.setText(ctx.getString(R.string.confirmdelete));
		else
			popupMessage.setText(nam);
		message = (TextView)dialog.findViewById(R.id.textMessage2);
		mHandler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0 :
						popupMessage.setText(ctx.getString(R.string.waitwhiledeleting));
						btn1.setVisibility(View.GONE);
						btn2.setVisibility(View.GONE);
						break;
						
					case 1:
						message.setText(nam);
						break;
					case 2 :  	
						try{
							mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(new File(Constants.PATH))));
						}catch(Exception e){
							mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Constants.PATH))));
						}
						mContext.sendBroadcast(new Intent("FQ_DELETE"));
						Toast.makeText(mContext, ctx.getString(R.string.deleted),Toast.LENGTH_SHORT).show();
						dialog.dismiss();	
				}
			}
		};
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
				int len = file.size();
				for(int i=0;i<len;++i){
					try{
						File f = file.get(i).getFile();
						if(f!=null){
							if(f.canWrite())
								deleteFile(f);
							else
							{					
								RootTools.deleteFileOrDirectory("'"+f.getAbsolutePath()+"'", false);
							}
						}	
					}catch(NullPointerException e){
						
					}
				}				
				mHandler.sendEmptyMessage(2);
			}
		});
		
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				thread.start();
				//message.setVisibility(View.VISIBLE);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//setResult(-1);
				dialog.dismiss();				
			}
		});
		dialog.show();
	}
	
	public void deleteFile(File file) {
		File target = file;
		if(target.exists() && target.isFile() && target.canWrite()){
			String nam = mContext.getString(R.string.deletingfile)+" " + target.getName();
			mHandler.sendEmptyMessage(1);
			nam = target.getPath();
			updateFileGallery(nam);
			target.delete();			
		}	
		
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				//String nam = mContext.getString(R.string.deletingfile)+" " + target.getName();
				//mHandler.sendEmptyMessage(1);
				//nam = target.getPath();
				//updateFileGallery(nam);
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteFile(temp_f );
					else if(temp_f.isFile()){
						//String nam = mContext.getString(R.string.deletingfile)+" " + temp_f.getName();
						//mHandler.sendEmptyMessage(1);
						//nam = temp_f.getPath();
						//updateFileGallery(nam);
						temp_f.delete();
					}	
				}
			}
			if(target.exists())
				if(target.delete()){
					//String nam = mContext.getString(R.string.deletingfile)+" " + target.getName();
					//mHandler.sendEmptyMessage(1);
					//nam = target.getPath();
					//updateFileGallery(nam);
					target.delete();
				}
		}
	}	
	
	private void updateFileGallery(String name){
		if(name.endsWith(".zip")||name.endsWith(".ZIP")||name.endsWith(".7z")||name.endsWith(".7Z")
				||name.endsWith(".rar")||name.endsWith(".RAR")||name.endsWith(".tar")||name.endsWith(".TAR")||name.endsWith(".tar.gz")||name.endsWith(".TAR.GZ")
				||name.endsWith(".TAT.BZ2")||name.endsWith(".tar.bz2")){
			for(int i = 0; i < Utils.zip.size() ; ++i){
				if(Utils.zip.get(i).getPath().equalsIgnoreCase(name)){
					Utils.zip.remove(i);
					break;
				}
			}
		}	
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")||name.endsWith(".MP3")||name.endsWith(".OGG")||name.endsWith(".M4A")||
				name.endsWith(".WAV")||name.endsWith(".AMR")){
			for(int i = 0; i < Utils.music.size() ; ++i){
				if(Utils.music.get(i).getPath().equalsIgnoreCase(name)){
					Utils.music.remove(i);
					break;
				}
			}
		}	
		else if(name.endsWith(".apk")||name.endsWith(".APK")){
			for(int i = 0; i < Utils.apps.size() ; ++i){
				if(Utils.apps.get(i).getPath().equalsIgnoreCase(name)){
					Utils.apps.remove(i);
					break;
				}
			}
		}	
		else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")||name.endsWith(".FLV")||name.endsWith(".MP4")||name.endsWith(".3GP")||name.endsWith(".AVI")
				||name.endsWith(".MKV")){
			for(int i = 0; i < Utils.vids.size() ; ++i){
				if(Utils.vids.get(i).getPath().equalsIgnoreCase(name)){
					Utils.vids.remove(i);
					break;
				}
			}
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")||name.endsWith(".BMP")||name.endsWith(".GIF")||name.endsWith(".JPEG")||name.endsWith(".JPG")
				||name.endsWith(".PNG")){
			for(int i = 0; i < Utils.img.size() ; ++i){
				if(Utils.img.get(i).getPath().equalsIgnoreCase(name)){
					Utils.img.remove(i);
					break;
				}
			}
		}		
		else if(name.endsWith(".pdf")||name.endsWith(".PDF")){
			for(int i = 0; i < Utils.doc.size() ; ++i){
				if(Utils.doc.get(i).getPath().equalsIgnoreCase(name)){
					Utils.doc.remove(i);
					break;
				}
			}
		}	
		else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
				||name.endsWith(".PPT")||name.endsWith(".DOCX")||name.endsWith(".pptx")||name.endsWith(".PPTX")
				||name.endsWith(".csv")||name.endsWith(".CSV")){
			for(int i = 0; i < Utils.doc.size() ; ++i){
				if(Utils.doc.get(i).getPath().equalsIgnoreCase(name)){
					Utils.doc.remove(i);
					break;
				}
			}
		}	
		else if(name.endsWith(".txt")||name.endsWith(".TXT")||name.endsWith(".log")||name.endsWith(".LOG")
				||name.endsWith(".ini")||name.endsWith(".INI")){
			for(int i = 0; i < Utils.doc.size() ; ++i){
				if(Utils.doc.get(i).getPath().equalsIgnoreCase(name)){
					Utils.doc.remove(i);
					break;
				}
			}
		}
		else{
			for(int i = 0; i < Utils.mis.size() ; ++i){
				if(Utils.mis.get(i).getPath().equalsIgnoreCase(name)){
					Utils.mis.remove(i);
					break;
				}
			}
		}
	}
 }
