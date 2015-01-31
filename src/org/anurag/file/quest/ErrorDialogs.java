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
 *                             anuraxsharma1512@gmail.com
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
		dialog = new Dialog(context, Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.delete_files);
		dialog.setCancelable(true);
		data = action;
		dialog.getWindow().getAttributes().width = width;		
		mContext = context;
		onCreate(context);
	}
	
	protected void onCreate(Context ctx) {
		//params.width = size.x*4/5;
		
		tv = (TextView)dialog.findViewById(R.id.textMessage);
		iView = (ImageView)dialog.findViewById(R.id.popupImage);
		iTv = (TextView)dialog.findViewById(R.id.popupTitle);
		
		if(data.equals("FlashableZips")||data.equalsIgnoreCase("FlashableZip")){
			iView.setBackgroundResource(R.drawable.archive_icon_hd);
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText("  "+mContext.getString(R.string.ziprecovery));
			tv.setText(mContext.getString(R.string.ziprecoverymessage));
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
		}else if(data.equalsIgnoreCase("renameError")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.cantrename);
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rename));
			tv.setText(R.string.delete_frm_ext_sd);
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
		else if(data.equalsIgnoreCase("homeError")){
			b = (Button)dialog.findViewById(R.id.popupCancel);
			b.setVisibility(View.GONE);
			iTv.setText(R.string.nohome);
			iView.setBackgroundResource(R.drawable.task);
			tv.setText(R.string.nohomeerror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText(ctx.getString(R.string.ok));
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
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.task));
			tv.setText(R.string.noapperror);
			b= (Button)dialog.findViewById(R.id.popupOk);
			b.setText(ctx.getString(R.string.ok));
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
