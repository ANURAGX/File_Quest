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

import java.io.FileNotFoundException;



import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jConstants;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;

import android.graphics.Point;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
;

public class ZipDialog {
	private static ProgressMonitor mo = null;
	private static File file;
	private static CheckBox zipOption;
	private static CheckBox tarOption;
	private TextView zipBoxTitle;
	private TextView zipFormat;
	private static TextView zipNoOfFiles;
	private static TextView zipFileLocation;
	private static TextView zipFileName;
	static Dialog dialog;
	private static Thread t;
	private LinearLayout lv;
	private AppManager nManager;
	private static ProgressBar zipProgressBar;
	private static int no = 0;
	private static int count = 0;
	private static Button zipOkBtn;
	private static Button zipCancelBtn;
	private int flashType = 1;
	private static Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
				case 0:
					zipOkBtn.setEnabled(false);
					zipCancelBtn.setEnabled(false);
					zipOption.setEnabled(false);
					tarOption.setEnabled(false);
					zipProgressBar.setVisibility(View.VISIBLE);
					zipNoOfFiles.setText(++count + " Items Packed Out Of " + no);
					zipFileName.setText("Currently Packing :- " + msg.obj);
					break;
				case 1:	
					zipNoOfFiles.setText("All Items Packed ");
					zipFileName.setText(" Adding Script ,Please wait");
					zipProgressBar.setVisibility(View.VISIBLE);
					zipCancelBtn.setEnabled(false);
					zipOkBtn.setEnabled(false);
					zipOption.setEnabled(false);
					tarOption.setEnabled(false);
					break;
				case 2://case 2 when flashable zip file is created successfully	
					zipFileName.setText("Flashable Zip Is Created");
					zipProgressBar.setVisibility(View.GONE);
					zipOkBtn.setVisibility(View.GONE);
					zipCancelBtn.setEnabled(true);
					zipCancelBtn.setText("Finish");
					t = null;
					zipOkBtn.setVisibility(View.GONE);
					zipCancelBtn.setEnabled(true);
					zipCancelBtn.setText("Done");
					dialog.setCancelable(true);
					break;
				
				case 3:
					zipOption.setEnabled(false);
					tarOption.setEnabled(false);
					zipOkBtn.setEnabled(false);
					zipCancelBtn.setClickable(false);
					//WHEN ZIPPING IS IN PROGRESS SETS THE UI WITH CURRENT STATUS 
					if(mo.getState() != ProgressMonitor.STATE_READY){
						zipProgressBar.setVisibility(View.VISIBLE);
						zipNoOfFiles.setText("Please Wait While Zipping");
						zipFileLocation.setText("Zipped File Name : " + file.getName() + ".zip");
						mHandler.sendEmptyMessage(3);
					}else 
						mHandler.sendEmptyMessage(4);
					break;
				case 4:
					//zipOkBtn.setEnabled(true);
					zipOkBtn.setVisibility(View.GONE);
					zipCancelBtn.setVisibility(View.VISIBLE);
					zipCancelBtn.setClickable(true);
					zipCancelBtn.setText("Finish");
					// AFTER ZIPPING SETS THE UI WITH THE REQUIRED STATS
					if(mo.getResult() == ProgressMonitor.RESULT_ERROR){
						zipNoOfFiles.setText("There Was An Error While Zipping");
						zipFileLocation.setText("Unable To Create File : " + file.getName() + ".zip");
					}else
						zipNoOfFiles.setText("Zipping Operation Was Successful");
					zipProgressBar.setVisibility(View.GONE);
					mHandler.sendEmptyMessage(5);
					
					break;
				
			}
		}
	};
	Context mContext;
	
	String action;
	
	
	public ZipDialog(Context con,int w,String data) {
		// TODO Auto-generated constructor stub
		mContext = con;
		action = data;
		dialog = new Dialog(con, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.zip_file_dialog);
		dialog.getWindow().getAttributes().width = w;
		onCreate();
	}
	

	protected void onCreate() {
		
		//params.width = p.x*7/9;
		nManager= new AppManager(mContext , mHandler);
		zipBoxTitle = (TextView)dialog.findViewById(R.id.zipFileTitle);
		zipNoOfFiles = (TextView)dialog.findViewById(R.id.zipNoOfFiles);
		zipFileLocation = (TextView)dialog.findViewById(R.id.zipFileLocation);
		zipProgressBar = (ProgressBar)dialog.findViewById(R.id.zipProgressBar);
		zipFileName = (TextView)dialog.findViewById(R.id.zipFileName);
		lv = (LinearLayout)dialog.findViewById(R.id.zipDeleteCheckLayout);
		zipFormat = (TextView)dialog.findViewById(R.id.zipFormat);
		zipOption = (CheckBox)dialog.findViewById(R.id.zipChioce);
		tarOption = (CheckBox)dialog.findViewById(R.id.tarChioce);
		zipOkBtn = (Button)dialog.findViewById(R.id.zipOkButton);
		zipCancelBtn = (Button)dialog.findViewById(R.id.zipCalcelButton);
		
		if(action.equalsIgnoreCase("FlashableZips")){
			//type = FlashableZips if all apps has to be packed in flashable zip
			//type = FlashableZip if only one app have to be packed in flashable zip
			zipBoxTitle.setText("Flashable Zip");
			zipNoOfFiles.setText("Total Apps " + (no=nManager.get_downloaded_apps().size()));
			zipFileLocation.setText("Location :- /sdcard/Flashable.zip");
			lv.setVisibility(View.GONE);
			zipFormat.setText("Flashable Zip Will Install App as");
			zipOption.setText("User Application");
			tarOption.setText("System Application");
			
			zipOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1){
						tarOption.setChecked(false);
						flashType = 1;
					}else{
						tarOption.setChecked(true);
						flashType = 2;
					}
						
				}
			});
			tarOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1){
						zipOption.setChecked(false);
						flashType = 2;
					}else{
						zipOption.setChecked(true);
						flashType = 1;
					}
				}
			});
		}else{
			//type = FlashableZips if all apps has to be packed in flashable zip
			//type = FlashableZip if only one app have to be packed in flashable zip
			zipBoxTitle.setText("Flashable Zip");
			zipNoOfFiles.setText("One App To Be Packed");
			zipFileLocation.setText("Location :- /sdcard/File Quest-Beta/Flashable.zip");
			lv.setVisibility(View.GONE);
			zipFormat.setText("Flashable Zip Will Install App as");
			zipOption.setText("User Application");
			tarOption.setText("System Application");
			zipOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1){
						tarOption.setChecked(false);
						flashType = 1;
					}else{
						tarOption.setChecked(true);
						flashType = 2;
					}
				}
			});
			tarOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1){
						zipOption.setChecked(false);
						flashType = 2;
					}else{
						zipOption.setChecked(true);
						flashType = 1;
					}
				}
			});
		}
		
		zipOkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.setCancelable(false);
				zipOkBtn.setEnabled(false);
				zipCancelBtn.setEnabled(false);
					try {
							if(action.equalsIgnoreCase("FlashableZips")){
								t = new Thread(nManager.new FlashableZips(mContext  , flashType));
								t.start();
							}
							else{
								t = new Thread(nManager.new FlashableZip(action, mContext , flashType));
								t.start();
							}
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			
		});
		zipCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	


	
	
	
	
	
}
