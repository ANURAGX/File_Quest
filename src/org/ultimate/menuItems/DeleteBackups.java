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

import java.io.File;


import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DeleteBackups{

	private File file;
	private TextView popupTitle;
	private TextView popupMessage;
	private Button btn1;
	private Button btn2;
	private Thread thread;
	Context mContext;
	Dialog dialog;
	
	public DeleteBackups(Context context,int width) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dialog = new Dialog(context, Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.delete_files);
		dialog.getWindow().getAttributes().width = width;
		dialog.setCancelable(false);
		onCreate();
	}
	
	
	
	protected void onCreate() {
		//params.width = size.x*4/5;
		String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
		btn1 = (Button)dialog.findViewById(R.id.popupOk);
		ImageView v = (ImageView)dialog.findViewById(R.id.popupImage);
		v.setImageDrawable(mContext.getResources().getDrawable(R.drawable.delete ));
		btn2 = (Button)dialog.findViewById(R.id.popupCancel);
		popupTitle = (TextView)dialog.findViewById(R.id.popupTitle);
		popupMessage = (TextView)dialog.findViewById(R.id.textMessage);
		
		//final WebView web = (WebView)dialog.findViewById(R.id.popup_Web_View);
		//web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		//web.setEnabled(false);			
		
		file = new File(PATH +  "/File Quest");
		if(file.exists()){
			file = new File(PATH + "/File Quest/AppBackup");
			if(file.exists() && file.listFiles().length !=0){
				popupTitle.setText(mContext.getString(R.string.deleteimage));
				popupMessage.setText(mContext.getString(R.string.deletebackupmsg));
				final Handler mHandler = new Handler(){
					public void handleMessage(Message msg){
						switch(msg.what){
							case 0 :
								popupMessage.setText(mContext.getString(R.string.waitwhiledeleting));
							//	web.setVisibility(View.VISIBLE);
								btn1.setVisibility(View.GONE);
								btn2.setVisibility(View.GONE);
								break;
							case 2 :  	
								btn1.setVisibility(View.VISIBLE);
								btn1.setText(mContext.getString(R.string.ok));
								btn2.setVisibility(View.GONE);
							//	web.setVisibility(View.GONE);
								popupMessage.setText(mContext.getString(R.string.backupdeleted));
								mContext.sendBroadcast(new Intent("FQ_BACKUP"));
								
						}
					}
				};
				thread = new Thread(new Runnable() {
					@Override
					public void run() {
						deleteTarget(file);
						mHandler.sendEmptyMessage(2);
					}
				});
				
				btn1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(btn2.getVisibility() == View.GONE){
							thread.interrupt();
							dialog.dismiss();
						}else
							thread.start();
					}
				});
				btn2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//setResult(-1);
						dialog.dismiss();				
					}
				});
			}
			else{
				popupTitle.setText(mContext.getString(R.string.message));
				popupMessage.setText(mContext.getString(R.string.failtolocate));
				btn2.setVisibility(View.GONE);
				btn1.setText(mContext.getString(R.string.ok));
				dialog.setCancelable(true);
				btn1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
		}
		else{
			popupTitle.setText(mContext.getString(R.string.message));
			popupMessage.setText(mContext.getString(R.string.failtolocate));
			btn2.setVisibility(View.GONE);
			btn1.setText(mContext.getString(R.string.ok));
			dialog.setCancelable(true);
			btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		
		dialog.show();
	}
	

	/**
	 * Function To Delete The Given File And Returns Message To Handler
	 * If Deletion is successful returns 0 else returns -1
	 * @param path
	 * @return
	 */
	public void deleteTarget(File file) {
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
						deleteTarget(temp_f);
					else if(temp_f.isFile())
						temp_f.delete();
				}
			}
			if(target.exists())
				if(target.delete()){}
		}
	}
	
	
}
