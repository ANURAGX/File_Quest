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

package org.anurag.text.editor;

import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SaveOnExit{

	Context mContext;
	Dialog dialog;
	public SaveOnExit(Context con,int w) {
		// TODO Auto-generated constructor stub
		mContext = con;		
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.delete_files);
		dialog.getWindow().getAttributes().width = w;
		onCreate();
	}
	
	void onCreate() {
		ImageView v = (ImageView)dialog.findViewById(R.id.popupImage);
		v.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_save));
		TextView popupTitle = (TextView)dialog.findViewById(R.id.popupTitle);
		popupTitle.setText(R.string.saveOnExit);
		TextView popupMessage = (TextView)dialog.findViewById(R.id.textMessage);
		popupMessage.setText(R.string.saveMesage);
		Button btn1 = (Button)dialog.findViewById(R.id.popupOk);
		Button btn2 = (Button)dialog.findViewById(R.id.popupCancel);
		
		btn1.setText(R.string.yes);
		btn2.setText(R.string.no);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mContext.sendBroadcast(new Intent("FQ_EDIT"));
				dialog.dismiss();
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				mContext.sendBroadcast(new Intent("FQ_EDIT_EXIT"));
				dialog.dismiss();
			}
		});
	}
}
