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

package org.anurag.dropbox;

import java.util.ArrayList;


import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuest;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author Anurag....
 * 
 * this class uploads the selected files to the user's dropbox
 * account....
 *
 */
public class DBoxUploader {
	
	private Dialog dialog;
	private ArrayList<Item> list_to_upload;
	private ProgressBar progress;
	
	
	
	private Handler handle = new Handler(){
		
	};
	
	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	});
	
	public DBoxUploader(Context context , ArrayList<Item> list) {
		// TODO Auto-generated constructor stub
		list_to_upload = list;
		
		dialog = new Dialog(context , Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.copy_dialog);
		dialog.getWindow().getAttributes().width = FileQuest.size.x*8/9;
		
		
		TextView tv = (TextView)dialog.findViewById(R.id.header);
		tv.setText(context.getString(R.string.uploading));
		
		dialog.show();
		
	}
}
