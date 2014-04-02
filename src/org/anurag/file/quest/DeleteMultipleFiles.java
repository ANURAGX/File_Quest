/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * anurag.dev1512@gmail.com
 *
 */
package org.anurag.file.quest;

import java.io.File;
import java.util.ArrayList;
import org.ultimate.root.LinuxShell;

import com.stericson.RootTools.RootTools;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteMultipleFiles{

	private ArrayList<File> list;
	private TextView popupTitle;
	private TextView popupMessage;
	private Button btn1;
	private Button btn2;
	WebView view;
	String ACTION;
	Context mContext;
	Dialog dialog;
	
	public DeleteMultipleFiles(Context context,int width,ArrayList<File> ob,String root) {
		// TODO Auto-generated constructor stub
		mContext = context;
		ACTION = root;
		list = ob;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.delete_multiple_files);
		dialog.getWindow().getAttributes().width = width;
		dialog.setCancelable(true);
		onCreate();
	}
	
	
	protected void onCreate() {
		//params.width = size.x*4/5;		
		btn1 = (Button)dialog.findViewById(R.id.popupOk);
		btn2 = (Button)dialog.findViewById(R.id.popupCancel);
		popupTitle = (TextView)dialog.findViewById(R.id.popupTitle3);
		view = (WebView)dialog.findViewById(R.id.delete_multi_Web_View);
		view.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		view.setEnabled(false);
		
		popupMessage = (TextView)dialog.findViewById(R.id.textMessage);
		if(ACTION.equals("root")){
			popupTitle.setText(R.string.condelete);
			popupMessage.setText(R.string.deleteSystem);
		}else{
			popupTitle.setText(R.string.condelete);
			popupMessage.setText(R.string.deleteSimple);
		}
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.setCancelable(false);
				new Delete().execute();
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	class Delete extends AsyncTask<Void, Void, Void>{
		int i , l = list.size();		
		File file;
		public Delete(){
			
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mContext.sendBroadcast(new Intent("FQ_DELETE"));
			Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			view.setVisibility(View.VISIBLE);
			popupMessage.setText("Please wait while Deleting Folders and files");
			btn1.setVisibility(View.GONE);
			btn2.setVisibility(View.GONE);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for(i = 0 ; i < l ; ++i){
				file = (File) list.get(i);
				try{
					if(file!=null){
						if(ACTION.equals("root")){
							RootTools.deleteFileOrDirectory("'"+file.getAbsolutePath()+"'",
									false);
						}
						else
							deleteTarget(file);
					}	
				}catch(Exception e){
					
				}
			}
			return null;
		}
		
	}

	/**
	 * Function To Delete The Given File And Returns Message To Handler
	 * If Deletion is successful returns 0 else returns -1
	 * @param path
	 * @return
	 */
	public void deleteTarget(File file) {
		File target = file;
		if(target.exists() && target.isFile() && target.canWrite())
			target.delete();
		
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				target.delete();
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);
					if(temp_f.isDirectory())
						deleteTarget(temp_f);
					else if(temp_f.isFile())
						temp_f.delete();
				}
			}
			if(target.exists())
				if(target.delete()){}
		}
	}
	
	
}
