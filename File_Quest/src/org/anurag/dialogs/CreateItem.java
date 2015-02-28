/**
 * Copyright(c) 2015 DRAWNZER.ORG PROJECTS -> ANURAG
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

package org.anurag.dialogs;

import java.io.File;
import java.io.IOException;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * this class inflates a dialog and asks for name for new item to be created
 * and creates the item(folder or empty file)....
 * @author anurag
 *
 */
@SuppressLint("InflateParams")
public class CreateItem {
	
	//Dialog builder 
	private Builder dialogBuilder;
	
	//Custom view set in dialog box
	private View view;
	
	//EditText for getting name
	private EditText getName;
	
	/**
	 * 
	 * @param ctx
	 * @param item parent dir
	 * @param panel current opened panel
	 * @param isDir true then create folder
	 */
	public CreateItem( final Context ctx ,final Item item ,final int panel ,final boolean isDir) {
		// TODO Auto-generated constructor stub
		LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = inf.inflate(R.layout.create_new_item_hd, null , false);
		
		TextView msg = (TextView) view.findViewById(R.id.head);
		getName = (EditText) view.findViewById(R.id.folder_nam);
		
		msg.setText(isDir ? R.string.folder_name : R.string.file_name);
		getName.setHint(isDir ? R.string.folder_name : R.string.file_name);
		
		dialogBuilder = new MaterialDialog.Builder(ctx);
		dialogBuilder.callback(new ButtonCallback() {

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				String name = getName.getText().toString();
				
				if(name.length()>0){
					File file = item.getFile();
					file = new File(file, name);
					if(isDir)
						file.mkdirs();
					else
						try {
							file.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					//hiding the keyboard....
					hide_keyboard(ctx);
					
					if(file.exists()){
											
						if(panel == 1){
							RootPanel.notifyDataSetChanged();
						}else if(panel == 2){
							SdCardPanel.notifyDataSetChanged();
						}
						Toast.makeText(ctx, R.string.itm_created, Toast.LENGTH_SHORT).show();
					}	
					else{
						Toast.makeText(ctx, R.string.failed_to_create, Toast.LENGTH_SHORT).show();
					}	
					//dismiss the dialog....
					dialog.dismiss();
				}else{
					Toast.makeText(ctx, R.string.enter_valid_name, Toast.LENGTH_SHORT).show();
				}			
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
			}

			@Override
			public void onNeutral(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNeutral(dialog);
			}
			
		})
		.customView(view , false)
		.autoDismiss(false)
		.content("Message")
		.title(R.string.add_item)
		.positiveText(R.string.create)
		.show();
	}

	/**
	 * this function  hides the keyboard....
	 * @param ctx
	 */
	private void hide_keyboard(Context ctx) {
		// TODO Auto-generated method stub
		InputMethodManager input = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		input.hideSoftInputFromWindow(getName.getWindowToken(), 0);
	}
}
