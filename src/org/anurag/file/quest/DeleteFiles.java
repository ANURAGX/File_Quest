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
			//nam = target.getPath();
			//updateFileGallery(nam);
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
 }
