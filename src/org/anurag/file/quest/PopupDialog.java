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
 * anurag.dev1512@gmail.com
 *
 */
package org.anurag.file.quest;

import java.io.File;
import org.ultimate.root.LinuxShell;

import com.stericson.RootTools.RootTools;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PopupDialog{

	private  File file;
	private  TextView popupTitle;
	private  TextView popupMessage;
	private  Button btn1;
	private  Button btn2;

	
	private  Thread thread;
	Context mContext;
	Dialog dialog;
	String name;
	public PopupDialog(Context context,int width,String data) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.popup_dialog);
		dialog.getWindow().getAttributes().width = width;
		dialog.setCancelable(false);
		file = new File(data);
		onCreate();
	}
	
	
	protected void onCreate() {
		//params.width = size.x*4/5;
		ImageView v = (ImageView)dialog.findViewById(R.id.popupImage);
		v.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_delete));
		
		btn1 = (Button)dialog.findViewById(R.id.popupOk);
		btn2 = (Button)dialog.findViewById(R.id.popupCancel);
		popupTitle = (TextView)dialog.findViewById(R.id.popupTitle);
		final WebView web = (WebView)dialog.findViewById(R.id.popup_Web_View);
		web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		web.setEnabled(false);
		popupMessage = (TextView)dialog.findViewById(R.id.textMessage);
		popupTitle.setText(R.string.condelete);
		popupMessage.setText("Are You Sure to Delete The Folder :-" + file.getName());
		final Handler mHandler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
					case 0 :
						popupMessage.setText("Please Wait While Deleting File");
						web.setVisibility(View.VISIBLE);
						btn1.setVisibility(View.GONE);
						btn2.setVisibility(View.GONE);
						break;
					case 2 :  	
						mContext.sendBroadcast(new Intent("FQ_DELETE"));
						Toast.makeText(mContext, "Deleted Successfully",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();						
				}
			}
		};
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
				if(file.canWrite())
					deleteTargetForCut(file);
				else
				{					
					RootTools.deleteFileOrDirectory("'"+file.getAbsolutePath()+"'", false);
				}
				mHandler.sendEmptyMessage(2);
			}
		});
		
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		dialog.show();
	}
	
	/**
	 * 
	 * @param file
	 */
	public void deleteTargetForCut(File file) {
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
						deleteTargetForCut(temp_f);
					else if(temp_f.isFile())
						temp_f.delete();
				}
			}
			if(target.exists())
				if(target.delete()){}
		}
	}
	
	
}
