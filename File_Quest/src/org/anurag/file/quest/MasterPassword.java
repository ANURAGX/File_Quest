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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.anurag.file.quest;

import org.anurag.dialogs.Rename;
import org.anurag.dialogs.ZipFiles;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
	public MasterPassword(final Context ctx , int width , final Item item ,  SharedPreferences prefs2 ,final Constants.MODES MODE) {
		// TODO Auto-generated constructor stub
		final Dialog dialog = new Dialog(ctx, Constants.DIALOG_STYLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.master_password);
		dialog.getWindow().getAttributes().width = width;
		final EditText pass = (EditText)dialog.findViewById(R.id.password);
		final EditText confirm = (EditText)dialog.findViewById(R.id.confirmpassword);
		final EditText newpass = (EditText)dialog.findViewById(R.id.newpassword);
		
		SharedPreferences prefs = prefs2;
		if(prefs == null)
			prefs = ctx.getSharedPreferences("SETTINGS", 0);

		final SharedPreferences.Editor  editor = prefs.edit();
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
		if(MODE == Constants.MODES.RESET && password != null){
			TextView msg = (TextView)dialog.findViewById(R.id.Message);
			msg.setText(ctx.getResources().getString(R.string.changepasswd));
			newpass.setVisibility(View.VISIBLE);
			TextView co = (TextView)dialog.findViewById(R.id.newpasswdtext);
			co.setVisibility(View.VISIBLE);
			co = (TextView)dialog.findViewById(R.id.header);
			co.setText(ctx.getString(R.string.resetmasterpassword));
		}
		
		
		
		if(password==null){
			TextView msg = (TextView)dialog.findViewById(R.id.Message);
			if(MODE == Constants.MODES.DEFAULT)
				msg.setText(ctx.getResources().getString(R.string.setpasswd));
		}
		///MODE!=1 means password confirmation...
		else if(password != null && MODE!=Constants.MODES.RESET){
			
			TextView msg = (TextView)dialog.findViewById(R.id.Message);
			if(MODE == Constants.MODES.ARCHIVE)
				msg.setText((R.string.arcpasswd));
			else if(MODE == Constants.MODES.COPY)
				msg.setText((R.string.copypasswd));
			else if(MODE == Constants.MODES.DELETE)
				msg.setText((R.string.delpasswd));
			else if(MODE == Constants.MODES.G_OPEN)
				msg.setText((R.string.openpasswd));
			else if(MODE == Constants.MODES.OPEN)
				msg.setText((R.string.openpasswd));
			else if(MODE == Constants.MODES.PASTEINTO)
				msg.setText((R.string.pasteppasswd));
			else if(MODE == Constants.MODES.RENAME)
				msg.setText((R.string.renamepasswd));
			else if(MODE == Constants.MODES.SEND)
				msg.setText((R.string.sendpasswd));
			else if(MODE == Constants.MODES.UNLOCK_ALL)
				msg.setText((R.string.unlockpasswd));
			else if(MODE == Constants.MODES.DISABLE_NEXT_RESTART)
				msg.setText((R.string.dis_next_restart));
			else if(MODE == Constants.MODES.HOME)
				msg.setText((R.string.homepasswd));
			else if(MODE == Constants.MODES.DEFAULT)
				msg.setText((R.string.unlockpasswd));
			confirm.setVisibility(View.GONE);
			TextView con = (TextView)dialog.findViewById(R.id.currentFile);
			con.setVisibility(View.GONE);
			con = (TextView)dialog.findViewById(R.id.header);
			con.setText(ctx.getString(R.string.entermaster));
			set.setText(ctx.getString(R.string.ok));
		}	
		
		Constants.activeMode = MODE;
		set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//getting the reference about task for which password
				//was verified...
							
				if(password == null){
					if(pass.getText().toString().length()<3){
						//password length is not appropriate....
						Toast.makeText(ctx, R.string.minimumpasswordlength, Toast.LENGTH_SHORT).show();
					}else if(pass.getText().toString().equals(confirm.getText().toString())){
						//passwords matched...
						//save the password here....
						//editor = prefs.edit();
						editor.putString("MASTER_PASSWORD", pass.getText().toString());
						editor.commit();
						
						if(MODE == Constants.MODES.RESET){
							dialog.dismiss();
							Toast.makeText(ctx, R.string.passwdreset, Toast.LENGTH_SHORT).show();
						}	
						if(MODE==Constants.MODES.DEFAULT){
							//main activity has to notified after verification...
							Intent intent = new Intent("FQ_FILE_LOCKED_OR_UNLOCKED");
							intent.putExtra("password_verified", "no_need");
							ctx.sendBroadcast(intent);
							Toast.makeText(ctx, R.string.passwdreset, Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
							
					}else if(!pass.getText().toString().equals(confirm.getText().toString())){
						//passwords didn't matched... 
						Toast.makeText(ctx, R.string.passworddidnotmatch, Toast.LENGTH_SHORT).show();
					}
				}
				//reseting the password....
				else if(MODE == Constants.MODES.RESET){
					if(!pass.getText().toString().equals(password)){
						//wrong old password....
						Toast.makeText(ctx, R.string.wrong_old_passws, Toast.LENGTH_SHORT).show();
					}else if(newpass.getText().toString().length()<3){
						//password length is short....
						Toast.makeText(ctx, R.string.minimumpasswordlength, Toast.LENGTH_SHORT).show();
					}else if(newpass.getText().toString().equals(confirm.getText().toString())){
						//passwords matched...
						//save the password here....
						//editor = prefs.edit();
						editor.putString("MASTER_PASSWORD", newpass.getText().toString());
						editor.commit();
						Toast.makeText(ctx, R.string.passwdreset, Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}else{
						//passwords didn't match....
						Toast.makeText(ctx, R.string.passworddidnotmatch, Toast.LENGTH_SHORT).show();
					}
				}
				
				else if(password != null){
					if(pass.getText().toString().equals(password)){
						Intent intent = new Intent("FQ_FILE_LOCKED_OR_UNLOCKED");
						try{
							//in some cases item will always be null...
														
							if(MODE == Constants.MODES.COPY)
								intent.putExtra("password_verified", "verified");
							else if(item.isLocked())
								intent.putExtra("password_verified", "verified");
							else
								intent.putExtra("password_verified", "no_need");
						}catch(NullPointerException e){
							intent.putExtra("password_verified", "no_need");
							if(Constants.activeMode == Constants.MODES.UNLOCK_ALL || 
									Constants.activeMode == Constants.MODES.DISABLE_NEXT_RESTART)
								intent.putExtra("password_verified", "verified");
						}
						
						//It is specific to G_open (gesture open for locked files)
						if(Constants.activeMode == Constants.MODES.G_OPEN){
							FileQuestHD.open_gesture_recognized_item(item.getPath(), FileQuestHD.getCurrentItem());
						}
						
						//AFTER PASSWORD IS VERIFIED RENAMING THE LIST OF FILES....
						else if(Constants.activeMode == Constants.MODES.RENAME){
							switch(FileQuestHD.getCurrentItem()){
							
							case 0:
								new Rename(Constants.ctx, FileGallery.get_selected_items(), 0);
								break;
								
							case 1:
								new Rename(Constants.ctx, RootPanel.get_selected_items() , 1);
								break;
								
							case 2:
								new Rename(Constants.ctx, SdCardPanel.get_selected_items(), 2);
								break;
							}
						}
						
						//opening the locked item after password verification....
						else if(Constants.activeMode == Constants.MODES.OPEN){
							switch(FileQuestHD.getCurrentItem()){
							case 0:
								FileGallery.open_locked_item();
								break;
								
							case 1:
								RootPanel.open_locked_item();
								break;
								
							case 2:
								SdCardPanel.open_locked_item();
							}
						}
						
						//zipping the locked items....
						else if(Constants.activeMode == Constants.MODES.ARCHIVE){
							switch(FileQuestHD.getCurrentItem()){
							
							case 0:
								new ZipFiles(Constants.ctx, FileGallery.get_selected_items());
								break;
								
							case 1:
								new ZipFiles(Constants.ctx, RootPanel.get_selected_items());
								break;
								
							case 2:
								new ZipFiles(Constants.ctx, SdCardPanel.get_selected_items());
								break;
							}
						}
						
						//removing the locked item from the db....
						else if(Constants.activeMode == Constants.MODES.DEFAULT){
							Constants.lock.setImageDrawable(Constants.UNLOCK_IMG);
							item.setLockStatus(false);
							Constants.db.deleteLockedNode(item.getPath());
							Toast.makeText(ctx, R.string.item_unlocked, Toast.LENGTH_SHORT).show();
						}
						else{
							ctx.sendBroadcast(intent);
						}
						hide_keyboard(ctx , pass);
						
						dialog.dismiss();
					}else if(!pass.getText().toString().equals(confirm.getText().toString())){
						//passwords didn't matched... 
						Toast.makeText(ctx, R.string.passworddidnotmatch, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});		
		
		dialog.show();
		if(Constants.disable_lock)
			lock_disabled(ctx, item, dialog, MODE);
	}
	
	/**
	 * 
	 * @param ctx
	 * @param item
	 * @param dialog
	 * @param MODE
	 */
	private void lock_disabled(Context ctx , Item item ,Dialog dialog , Constants.MODES MODE){
		Intent intent = new Intent("FQ_FILE_LOCKED_OR_UNLOCKED");
		try{
			//in some cases item will always be null...
										
			if(MODE == Constants.MODES.COPY)
				intent.putExtra("password_verified", "verified");
			else if(item.isLocked())
				intent.putExtra("password_verified", "verified");
			else
				intent.putExtra("password_verified", "no_need");
		}catch(NullPointerException e){
			intent.putExtra("password_verified", "no_need");
			if(Constants.activeMode == Constants.MODES.UNLOCK_ALL || 
					Constants.activeMode == Constants.MODES.DISABLE_NEXT_RESTART)
				intent.putExtra("password_verified", "verified");
		}
		
		//It is specific to G_open (gesture open for locked files)
		if(Constants.activeMode == Constants.MODES.G_OPEN)
			intent.putExtra("g_open_path", item.getPath());
		ctx.sendBroadcast(intent);
		dialog.dismiss();
	}
	
	/**
	 * this function  hides the keyboard....
	 * @param ctx
	 */
	private void hide_keyboard(Context ctx , EditText getName) {
		// TODO Auto-generated method stub
		InputMethodManager input = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		input.hideSoftInputFromWindow(getName.getWindowToken(), 0);
	}
}
