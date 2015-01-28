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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * this class inflates a dialog and asks for name for new item to be created
 * and creates the item(folder or empty file)....
 * @author anurag
 *
 */
public class Rename {
	
	private Dialog dialog;
	private Button btn;
	private EditText getName;
	
	
	/**
	 * 
	 * @param ctx
	 * @param list
	 * @param keys
	 * @param panel
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
		dialog = new Dialog(ctx, Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.create_new_item_hd);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().width = Constants.size.x*8/9;
		dialog.show();
		
		getName = (EditText) dialog.findViewById(R.id.folder_nam);
		TextView head = (TextView) dialog.findViewById(R.id.head);
		TextView header = (TextView) dialog.findViewById(R.id.header);
		ImageView img = (ImageView) dialog.findViewById(R.id.headerImage);
		
		img.setBackgroundResource(R.drawable.rename);
		header.setText(R.string.rename);
		head.setText(R.string.enter_name);
		getName.setHint(R.string.enter_name);
		

		Item itm = currentItem.get(0);
		String str = itm.getName();
		getName.setText(str);
		
		if(!itm.isDirectory() && !itm.getName().startsWith(".") && itm.getName().contains("."))
			getName.setSelection(0, str.lastIndexOf("."));
		else
			getName.setSelection(str.length());
		
		btn = (Button) dialog.findViewById(R.id.create);
		btn.setText(R.string.rename);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
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
						
						RootPanel.ITEMS = null;
						SdCardPanel.ITEMS = null;
						FileGallery.ITEMS = null;
						RootPanel.counter = 0;
						SdCardPanel.counter = 0;
						FileGallery.counter = 0;
						
						Constants.LONG_CLICK[panel] = false;
						
						if(panel == 1){
							RootPanel.notifyDataSetChanged();
						}else if(panel == 2){
							SdCardPanel.notifyDataSetChanged();
						}else if(panel == 0)
							FileGallery.resetAdapter();
						
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
								
								RootPanel.ITEMS = null;
								SdCardPanel.ITEMS = null;
								FileGallery.ITEMS = null;
								RootPanel.counter = 0;
								SdCardPanel.counter = 0;
								FileGallery.counter = 0;
								
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
		});		
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