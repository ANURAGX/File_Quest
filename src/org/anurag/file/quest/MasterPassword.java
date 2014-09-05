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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author Anurag
 *
 */
public class MasterPassword {

	/**
	 * 
	 * @param ctx
	 * @param width of the dialog window....
	 * @param MODE to specify whether it is for setting master password or verifying password....
	 * @param item to lock or unlock...
	 */
	public MasterPassword(final Context ctx , int width , int MODE , Item item , final SharedPreferences.Editor editor) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, R.style.custom_dialog_theme);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.master_password);
		dialog.getWindow().getAttributes().width = width;
		final EditText pass = (EditText)dialog.findViewById(R.id.password);
		final EditText confirm = (EditText)dialog.findViewById(R.id.confirmpassword);
		
		Button cancel = (Button)dialog.findViewById(R.id.copyCancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		Button set = (Button)dialog.findViewById(R.id.copyOk);
		set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pass.getText().toString().length()<3){
					//password length is not appropriate....
					Toast.makeText(ctx, R.string.minimumpasswordlength, Toast.LENGTH_SHORT).show();
				}else if(pass.getText().toString().equals(confirm.getText().toString())){
					//passwords matched...
					//save the password here....
					editor.putString("MASTER_PASSWORD", pass.toString());
					editor.commit();
					ctx.sendBroadcast(new Intent("FQ_FILE_LOCKED_OR_UNLOCKED"));
					dialog.dismiss();
				}else if(!pass.getText().toString().equals(confirm.getText().toString())){
					//passwords didn't matched... 
					Toast.makeText(ctx, R.string.passworddidnotmatch, Toast.LENGTH_SHORT).show();
				}
			}
		});		
		dialog.show();
	}
}
