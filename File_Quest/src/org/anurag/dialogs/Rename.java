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
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuestHD;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * this class inflates a dialog and asks for name for new item to be created
 * and creates the item(folder or empty file)....
 * @author anurag
 *
 */
public class Rename {
	
	//view to get name for file to be renamed 
	private EditText getName;
	
	//custom view put in dialog box
	private View view;
	
	/**
	 * 
	 * @param ctx
	 * @param list of files from file gallery
	 * @param keys for list
	 * @param panel is 0 for file gallery
	 */
	public Rename(Context ctx , ConcurrentHashMap<String , Item> list , ConcurrentHashMap<String , String> keys
			, int panel){
		ArrayList<Item> ls = new ArrayList<>();
		int len = list.size();
		for(int i = 0 ; i < len ; ++i)
			if(FileGallery.ITEMS[i] == 1)
				ls.add(list.get(keys.get(""+i)));
		
		new Rename(ctx, ls, panel);
	}
	
	/**
	 * 
	 * @param ctx
	 * @param currentItem
	 * @param panel
	 */
	public Rename(final Context ctx ,final ArrayList<Item> currentItem , final int panel) {
		// TODO Auto-generated constructor stub
		
		LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inf.inflate(R.layout.create_new_item_hd, null , false);
		

		TextView msg = (TextView) view.findViewById(R.id.head);
		getName = (EditText) view.findViewById(R.id.folder_nam);
		
		msg.setText(R.string.enter_name);
		
		//dialog builder....
		new MaterialDialog.Builder(ctx)
		.title(R.string.rename)
		.autoDismiss(false)
		.customView(view, true)
		.positiveText(R.string.rename)
		.negativeText(R.string.dismiss)
		.callback(new ButtonCallback() {
			@Override
			public void onPositive(final MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				if(getName.getText().toString().length() == 0){
					Toast.makeText(ctx, R.string.enter_valid_name, Toast.LENGTH_SHORT).show();
					return;
				}
				
				final int len = currentItem.size();
				if(len == 1){
					//only single item has to be renamed....
					boolean renamed = false;
					File file = currentItem.get(0).getFile();
					String name = getName.getText().toString();
					File newFile = new File(file.getParentFile(), name);
					
					//user tried to rename to a existing folder or file name....
					//so smartly selecting similar name to rename by appending
					//with _ and integer value....
					//like windows or linux....
					if(newFile.exists()){
						name = getSimilarName(file.getParentFile() , name);
					}
					renamed = file.renameTo(new File(file.getParentFile(), name));
					newFile = new File(file.getParentFile(), name);
					
					if(renamed){					
					
						//is user renames the locked or favorite item
						//then after renaming them maintaining their
						//status....
						Item item = currentItem.get(0);
						if(item.isFavItem()){
							Constants.db.deleteFavItem(item.getPath());
							Utils.buildFavItems(item, false);
							Constants.db.insertNodeToFav(newFile.getAbsolutePath());
							
							//Item newItem = new Item(newFile, item.getIcon(), item.getType(),
								//	item.getSize());
							//Utils.buildFavItems(newItem, true);
						}
						
						if(item.isLocked()){
							Constants.db.deleteLockedNode(item.getPath());
							Constants.db.insertNodeToLock(newFile.getAbsolutePath());
						}
						
						hide_keyboard(ctx);
						
						switch(FileQuestHD.getCurrentItem()){
						case 0:
							try{
								FileGallery.resetAdapter();
							}catch(Exception e){}
							break;
							
						case 1:
							RootPanel.notifyDataSetChanged();
							break;
							
						case 2:
							SdCardPanel.notifyDataSetChanged();
							break;
						}
						
						ctx.sendBroadcast(new Intent("FQ_DELETE"));
						
						Toast.makeText(ctx, R.string.itm_renamed, Toast.LENGTH_SHORT).show();
					}	
					else
						Toast.makeText(ctx, R.string.failed_to_rename, Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					
				}else{
					//more than one item has to be renamed...
					
					new AsyncTask<Void , Void , Void>(){
						boolean renamed = false;
						String name = getName.getText().toString();
						
						@Override
						protected void onPostExecute(Void result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							if(renamed){				
								
								Constants.LONG_CLICK[panel] = false;
								
								if(panel == 1){
									RootPanel.notifyDataSetChanged();
								}else if(panel == 2){
									SdCardPanel.notifyDataSetChanged();
								}

								ctx.sendBroadcast(new Intent("FQ_DELETE"));
								Toast.makeText(ctx, R.string.itm_renamed, Toast.LENGTH_SHORT).show();
							}	
							else
								Toast.makeText(ctx, R.string.failed_to_rename, Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							
						}

						@Override
						protected Void doInBackground(Void... arg0) {
							// TODO Auto-generated method stub
							int k = 0;
							
							for(int i = 0 ; i < len ; ++i){
								File file = currentItem.get(i).getFile();
								String renName = name + "_" + k++;
								File newFile = new File(file.getParentFile(), renName);
								
								while(newFile.exists())
									renName = name + "_" + k++;
								
								newFile = new File(file.getParentFile() , renName);
								
								renamed = file.renameTo(newFile);
							
								if(renamed){
									
									//is user renames the locked or favorite item
									//then after renaming them maintaining their
									//status....
									
									Item item = currentItem.get(i);
									if(item.isFavItem()){
										Constants.db.deleteFavItem(item.getPath());
										Utils.buildFavItems(item, false);
										Constants.db.insertNodeToFav(newFile.getAbsolutePath());
										//Utils.rebuildFavList();
									}
									
									if(item.isLocked()){
										Constants.db.deleteLockedNode(item.getPath());
										Utils.buildFavItems(item, false);
										Constants.db.insertNodeToLock(newFile.getAbsolutePath());
									}
								}					
								
							}
							return null;
						}
						
					}.execute();										
				}
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				dialog.dismiss();
			}

			@Override
			public void onNeutral(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNeutral(dialog);
			}			
		}).show();
		
		
		
		
		getName.setHint(R.string.enter_name);
		

		Item itm = currentItem.get(0);
		String str = itm.getName();
		getName.setText(str);
		
		if(!itm.isDirectory() && !itm.getName().startsWith(".") && itm.getName().contains("."))
			getName.setSelection(0, str.lastIndexOf("."));
		else
			getName.setSelection(str.length());
		
	//	btn = (Button) dialog.findViewById(R.id.create);
	
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

	/**
	 * 
	 * @param parentFile
	 * @param name typed user'e name to rename to....
	 * @return
	 */
	private String getSimilarName(File parentFile, String name) {
		// TODO Auto-generated method stub
		int i = 0;
		String retName = name;
		while(new File(parentFile , name).exists()){
			name = retName + "_" + i++;
		}		
		return name;
	}
}	