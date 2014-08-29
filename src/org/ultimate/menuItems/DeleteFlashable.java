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

import org.anurag.file.quest.R;
import org.anurag.file.quest.RootManager;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class DeleteFlashable{

	private File file;
	private TextView popupTitle;
	private TextView popupMessage;
	private Button btn1;
	private Button btn2;
	private Thread thread;	
	Context mContext;
	Dialog dialog;
	
	public DeleteFlashable(Context context,int width) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.delete_files);
		dialog.getWindow().getAttributes().width = width;
		dialog.setCancelable(false);
		onCreate();
	}
	
	
	
	protected void onCreate() {
		
		String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
		btn1 = (Button)dialog.findViewById(R.id.popupOk);
		btn2 = (Button)dialog.findViewById(R.id.popupCancel);
		popupTitle = (TextView)dialog.findViewById(R.id.popupTitle);
		
		popupMessage = (TextView)dialog.findViewById(R.id.textMessage);
		ImageView v = (ImageView)dialog.findViewById(R.id.popupImage);
		v.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_delete));
		file = new File(PATH +  "/File Quest");
		if(file.exists()){
			file = new File(PATH + "/File Quest/Zip Backup");
			if(file.exists()&&file.listFiles().length>0){
				
				popupTitle.setText(mContext.getString(R.string.deleteimage));
				popupMessage.setText(mContext.getString(R.string.deletezipbackups));
				final Handler mHandler = new Handler(){
					public void handleMessage(Message msg){
						switch(msg.what){
							case 0 :
								popupMessage.setText(mContext.getString(R.string.waitwhiledeleting) );
								//web.setVisibility(View.VISIBLE);
								btn1.setVisibility(View.GONE);
								btn2.setVisibility(View.GONE);
								break;
							case 2 :  	
								btn1.setVisibility(View.VISIBLE);
								btn1.setText(mContext.getString(R.string.ok));
								btn2.setVisibility(View.GONE);
								//web.setVisibility(View.GONE);
								popupMessage.setText(mContext.getString(R.string.backupdeleted));
								
								
						}
					}
				};
				thread = new Thread(new Runnable() {
					@Override
					public void run() {
						RootManager.deleteTarget(file);
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
			dialog.setCancelable(true);
			btn1.setText(mContext.getString(R.string.ok));
			btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		
		dialog.show();
	}
	
	
	
}
