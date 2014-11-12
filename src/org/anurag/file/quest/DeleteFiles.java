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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
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

import com.stericson.RootTools.RootTools;



/**
 * 
 * @author Anurag....
 *
 */
@SuppressLint("HandlerLeak")
public class DeleteFiles{

	//list of files to be deleted
	private  ArrayList<Item> file;

	//showing current message
	private  TextView popupMessage;
	private TextView message;
	private  Button btn1;
	private  Button btn2;
	private  Thread thread;
	private Handler mHandler;
	private Context mContext;
	private Dialog dialog;
	private String nam;
	
	//boolean values true tells type of file deleted
	private boolean music_deleted;
	private boolean app_deleted;
	private boolean img_deleted;
	private boolean vid_deleted;
	private boolean doc_deleted;
	private boolean zip_deleted;
	private boolean mis_deleted;
	private int len;
	
	/**
	 * 
	 * @param ctx context
	 * @param width for dialog
	 * @param list of files to be deleted
	 * @param lsKeys for list of files
	 * @param msg to show....
	 */
	public DeleteFiles(Context ctx,int width , ConcurrentHashMap<String , Item> list ,ConcurrentHashMap<String, String> lsKeys,
			String msg) {
		// TODO Auto-generated constructor stub
		ArrayList<Item> itms = new ArrayList<Item>();
		len = list.size();
		for(int i = 0 ; i<len ; ++i){
			itms.add(list.get(lsKeys.get(""+i)));
		}
		
		new DeleteFiles(ctx, width, itms, msg);
	}
	
	public DeleteFiles(Context context,int width,ArrayList<Item> list,String msg) {
		// TODO Auto-generated constructor stub
		
		music_deleted =	app_deleted = img_deleted = vid_deleted = doc_deleted =
				zip_deleted = mis_deleted = false;
		
		mContext = context;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.delete_files);
		dialog.getWindow().getAttributes().width = width;
		dialog.setCancelable(true);
		file = list;
		if(file != null)
			len = file.size();
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
				File f;
				for(int i=0;i<len;++i){
					try{
						f = file.get(i).getFile();
						if(f!=null){
							if(f.canWrite()){
								deleteFile(f);
							}	
							else
							{					
								RootTools.deleteFileOrDirectory("'"+f.getAbsolutePath()+"'", false);
							}
						}	
					}catch(NullPointerException e){
						
					}
				}			
				
				//after deleting all files regenerating keys....
				regenerate_keys();
				mHandler.sendEmptyMessage(2);
			}
		});
		
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.setCancelable(false);
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
			delete_from_gallery(target);
			target.delete();
		}	
		
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				delete_from_gallery(target);
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteFile(temp_f );
					else if(temp_f.isFile()){
						delete_from_gallery(temp_f);
						temp_f.delete();
					}	
				}
			}
			if(target.exists())
				if(target.delete()){
					delete_from_gallery(target);
					target.delete();
				}
		}
	}	
	
	/**
	 * 
	 * @param file 
	 */
	synchronized void delete_from_gallery(File file){
		if(file.isDirectory())
			return;
		String path = file.getPath();
		
		if(Utils.music.get(path) != null){
			music_deleted = true;
			Utils.musicsize -= file.length();
			Utils.music.remove(path);
		}	
		
		else if(Utils.apps.get(path) != null){
			app_deleted = true;
			Utils.apksize -= file.length();
			Utils.apps.remove(path);
		}	
		
		else if(Utils.img.get(path) != null){
			img_deleted = true;
			Utils.imgsize -= file.length();
			Utils.img.remove(path);
		}	
		
		else if(Utils.vids.get(path) != null){
			vid_deleted = true;
			Utils.vidsize -= file.length();
			Utils.vids.remove(path);
		}	
		
		else if(Utils.doc.get(path) != null){
			doc_deleted = true;
			Utils.docsize -= file.length();
			Utils.doc.remove(path);
		}	
		
		else if(Utils.zip.get(path) != null){
			zip_deleted = true;
			Utils.zipsize -= file.length();
			Utils.zip.remove(path);
		}	
		
		else if(Utils.mis.get(path) != null){
			mis_deleted = true;
			Utils.missize -= file.length();
			Utils.mis.remove(path);
		}	
	}	
	
	/**
	 * function to regenerate the keys after deleting  files...
	 * it is necessary as they keys...
	 * if it is not performed then will cause exception....
	 */
	synchronized void regenerate_keys(){
		
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
