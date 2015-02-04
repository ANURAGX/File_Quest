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

import java.util.concurrent.ConcurrentHashMap;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;
import org.anurag.file.quest.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author anurag
 *
 */
public class ConfirmTweakTask {

	private Dialog dialog;
	private String msg;
	private TextView message;
	private Button btn;
	public ConfirmTweakTask(final Context ctx , final int tweaktype , String type , final int tweakwhat) {
		// TODO Auto-generated constructor stub
		dialog = new Dialog(ctx, Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.confirm_tweak_hd);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().width = Constants.size.x*8/9;
		
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
		
		message = (TextView) dialog.findViewById(R.id.message);
		message.setText(msg);
		
		btn = (Button) dialog.findViewById(R.id.ok);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
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
					
					break;
					
				case 3:
				}
				
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
	
}
