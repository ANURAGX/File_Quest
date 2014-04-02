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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class ErrorDialogs {
	private TextView tv;
	private TextView iTv;
	private ImageView iView;
	private String data;
	private Button b;
	
	Context mContext;
	Dialog dialog;
	
	public ErrorDialogs(Context context,int width,String action) {
		// TODO Auto-generated constructor stub
		dialog = new Dialog(context, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.popup_dialog);
		dialog.setCancelable(true);
		data = action;
		dialog.getWindow().getAttributes().width = width;		
		mContext = context;
		onCreate();
	}
	
	protected void onCreate() {
		//params.width = size.x*4/5;
		
		tv = (TextView)dialog.findViewById(R.id.textMessage);
		iView = (ImageView)dialog.findViewById(R.id.popupImage);
		iTv = (TextView)dialog.findViewById(R.id.popupTitle);
		
		if(data.equals("FlashableZips")||data.equalsIgnoreCase("FlashableZip")){
			iTv.setText("Flashable Zip");
			iView.setBackgroundResource(R.drawable.ic_launcher_zip_it);
			tv.setText(R.string.flashable);
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText("   Recovery Zip");
			tv.setText("This Option will give a zip file having all the user apps bundled such that " +
					"it can be flashed in recover mode to make it user or system apps ");
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mContext.sendBroadcast(new Intent("FQ_FLASHZIP"));
					dialog.dismiss();
				}
			});
		}else if(data.equals("CopyToNextPanel")){
			iTv.setText("Copy");
			iView.setBackgroundResource(R.drawable.ic_launcher_file_task);
			tv.setText(R.string.copynextpanel);
			
		}else if(data.equals("CutFile")){
			iTv.setText("Cut");
			iView.setBackgroundResource(R.drawable.ic_launcher_file_task);
			tv.setText(R.string.removenextpanel);
		}else if(data.equalsIgnoreCase("homeError")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.nohome);
			iView.setBackgroundResource(R.drawable.ic_launcher_droid_home);
			tv.setText(R.string.nohomeerror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}else if(data.equalsIgnoreCase("renameError")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.cantrename);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_rename));
			tv.setText(R.string.renameerror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}else if(data.contains("Null")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.cantcopy);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_file_task));
			tv.setText(R.string.copyerror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}else if(data.contains("NotFound")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.noApp);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_file_task));
			tv.setText(R.string.noapperror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}
		
		else if(data.contains("unsupportedScreenSize")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.usize);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_file_task));
			tv.setText(R.string.usizeerror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//setResult(RESULT_OK);
					dialog.dismiss();
				}
			});
		}
		
		else if(data.contains("Security")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.sec);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_file_task));
			tv.setText(R.string.secerror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//setResult(RESULT_OK);
					dialog.dismiss();
				}
			});
		}else if(data.contains("cloud")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.clod);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_sugar_sync));
			tv.setText(R.string.cloderror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//setResult(RESULT_OK);
					dialog.dismiss();
				}
			});
		}
		
		else{
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.cantcopy);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_file_task));
			tv.setText(R.string.cantcopyerror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText("Ok");
			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}
		dialog.show();
	}

}
