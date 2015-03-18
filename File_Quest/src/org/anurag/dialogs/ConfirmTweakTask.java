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

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

/**
 * 
 * this class confirms before deleting the files from file gallery in one shot.
 * 
 * @author anurag
 *
 */
public class ConfirmTweakTask {

	
	/**
	 * 
	 * @param ctx
	 * @param tweaktype
	 * @param type
	 * @param tweakwhat
	 */
	public ConfirmTweakTask(final Context ctx , final int tweaktype , String type , final int tweakwhat) {
		// TODO Auto-generated constructor stub
		
		Builder dialog = new MaterialDialog.Builder(ctx);
		dialog.title(R.string.fq_tweak);
		String msg = null;
		switch(tweaktype){
		case 1:
			msg = String.format(ctx.getString(R.string.clean_tweak), type);
			break;
			
		case 2:
			msg = String.format(ctx.getString(R.string.move_tweak), type);
			break;
			
		case 3:
			msg = String.format(ctx.getString(R.string.zip_tweak), type);
		}
		

		dialog.content(msg);
		dialog.negativeText(R.string.dismiss);
		dialog.positiveText(R.string.ok);
		dialog.callback(new ButtonCallback() {

			@Override
			public void onPositive(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onPositive(dialog);
				ConcurrentHashMap<String , Item> list = new ConcurrentHashMap<>();
				ConcurrentHashMap<String , String> keys = new ConcurrentHashMap<>();
				
				//selecting list of files to be tweaked....
				switch(tweakwhat){
				case 0:
					list = Utils.fav;
					keys = Utils.favKey;
					break;
					
				case 1:
					list = Utils.music;
					keys = Utils.musicKey;					
					break;
					
				case 2:
					list = Utils.apps;
					keys = Utils.appKey;
					break;
					
				case 3:
					list = Utils.img;
					keys = Utils.imgKey;
					break;
					
				case 4:
					list = Utils.vids;
					keys = Utils.videoKey;
					break;
					
				case 5:
					list = Utils.doc;
					keys = Utils.docKey;
					break;
					
				case 6:
					list = Utils.zip;
					keys = Utils.zipKey;
					break;
					
				case 7:
					list = Utils.mis;
					keys = Utils.misKey;
					break;
				}
				
				//providing the list of selected files for operation....
				switch(tweaktype){
				case 1:
					new DeleteFiles(ctx, Constants.size.x*8/9, list, keys);
					break;
					
				case 2:
					new GetMoveLocation(ctx, list, keys);
					break;
					
				case 3:
					ArrayList<Item> itms = new ArrayList<Item>();
					int len = list.size();
					for(int i = 0 ; i<len ; ++i){
						itms.add(list.get(keys.get(""+i)));
					}
					new ZipFiles(ctx, itms);
					break;
				}				
				dialog.dismiss();
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				// TODO Auto-generated method stub
				super.onNegative(dialog);
				dialog.dismiss();
			}			
		});
		
		dialog.show();
	}	
}
