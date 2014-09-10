/**
 * Copyright(c) 2014 DRAWNZER.ORG PROJECTS -> ANURAG
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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Anurag
 *
 */
@SuppressLint("CutPasteId")
public class MasterPassword {

	/**
	 * 
	 * @param ctx
	 * @param width
	 * @param item
	 * @param prefs
	 * @param MODE
	 */
	public MasterPassword(final Context ctx , int width , final Item item , final SharedPreferences prefs ,final Constants.MODES MODE) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, R.style.custom_dialog_theme);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.master_password);
		dialog.getWindow().getAttributes().width = width;
		final EditText pass = (EditText)dialog.findViewById(R.id.password);
		final EditText confirm = (EditText)dialog.findViewById(R.id.confirmpassword);
		final EditText newpass = (EditText)dialog.findViewById(R.id.newpassword);
		final String password = prefs.getString("MASTER_PASSWORD", null);
		
		
		Button cancel = (Button)dialog.findViewById(R.id.copyCancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		Button set = (Button)dialog.findViewById(R.id.copyOk);
		
		//MODE=1 means password has to be reset...
		if(MODE== Constants.MODES.RESET){
			newpass.setVisibility(View.VISIBLE);
			TextView co = (TextView)dialog.findViewById(R.id.newpasswdtext);
			co.setVisibility(View.VISIBLE);
			co = (TextView)dialog.findViewById(R.id.header);
			co.setText(ctx.getString(R.string.resetmasterpassword));
		}
		
		///MODE!=1 means password confirmation...
		if(password != null && MODE!=Constants.MODES.RESET){
			confirm.setVisibility(View.GONE);
			TextView con = (TextView)dialog.findViewById(R.id.currentFile);
			con.setVisibility(View.GONE);
			con = (TextView)dialog.findViewById(R.id.header);
			con.setText(ctx.getString(R.string.entermaster));
			set.setText(ctx.getString(R.string.ok));
		}	
		
		set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//getting the reference about task for which password
				//was verified...
				Constants.activeMode = MODE;
				
				if(password == null||MODE==Constants.MODES.RESET){
					if(pass.getText().toString().length()<3){
						//password length is not appropriate....
						Toast.makeText(ctx, R.string.minimumpasswordlength, Toast.LENGTH_SHORT).show();
					}else if(pass.getText().toString().equals(confirm.getText().toString())){
						//passwords matched...
						//save the password here....
						SharedPreferences.Editor  editor = prefs.edit();
						editor.putString("MASTER_PASSWORD", pass.getText().toString());
						editor.commit();
						if(MODE==Constants.MODES.DEFAULT){
							//main activity has to notified after verification...
							Intent intent = new Intent("FQ_FILE_LOCKED_OR_UNLOCKED");
							intent.putExtra("password_verified", "no_need");
							ctx.sendBroadcast(intent);
							dialog.dismiss();
						}else if(MODE==Constants.MODES.RESET)
							//it is only to change or set password...
							Toast.makeText(ctx, R.string.passwdreset, Toast.LENGTH_SHORT).show();
					}else if(!pass.getText().toString().equals(confirm.getText().toString())){
						//passwords didn't matched... 
						Toast.makeText(ctx, R.string.passworddidnotmatch, Toast.LENGTH_SHORT).show();
					}
				}else if(password != null){
					if(pass.getText().toString().length()<3){
						//password length is not appropriate....
						Toast.makeText(ctx, R.string.minimumpasswordlength, Toast.LENGTH_SHORT).show();
					}else if(pass.getText().toString().equals(password)){
						Intent intent = new Intent("FQ_FILE_LOCKED_OR_UNLOCKED");
						try{
							//in one case item will always be null...
							if(item.isLocked())
								intent.putExtra("password_verified", "verified");
							else
								intent.putExtra("password_verified", "no_need");
						}catch(NullPointerException e){
							intent.putExtra("password_verified", "no_need");
						}
						ctx.sendBroadcast(intent);
						dialog.dismiss();
					}else if(!pass.getText().toString().equals(confirm.getText().toString())){
						//passwords didn't matched... 
						Toast.makeText(ctx, R.string.passworddidnotmatch, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});		
		dialog.show();
	}
}
