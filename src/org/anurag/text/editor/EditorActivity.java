/**
 * Copyright(c) 2013 DRAWNZER.ORG PROJECTS -> ANURAG

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
package org.anurag.text.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.LinuxShell;
import org.anurag.file.quest.R;
import org.anurag.file.quest.SystemBarTintManager;
import org.anurag.file.quest.SystemBarTintManager.SystemBarConfig;
import org.anurag.file.quest.ThemeOrganizer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("NewApi")
public class EditorActivity extends ActionBarActivity {
	Intent intent;
	Context con;
	EditText main;
	int FILEFORMAT_NL = 1;
	int FILEFORMAT_CR = 2;
	int FILEFORMAT_CRNL = 3;
	int FILEFORMAT;
	boolean OPENING_FILE;
	boolean ERROR_SAVING; 
	String path;
	File f;
	EditText title;
	Point size;
	boolean MODIFIED;
	Dialog dialog;
	BroadcastReceiver rec;
	StringBuffer rea;
	private Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_layout);
	
		toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		
		setSupportActionBar(toolbar);	
		
		LinearLayout linear = (LinearLayout) findViewById(R.id.editor_layout);
		
		if(Constants.COLOR_STYLE == 0){
			//this condition is true then this activity was called explicitly....
			Constants.COLOR_STYLE = getResources().getColor(R.color.orange);
			ThemeOrganizer.BUILD_THEME(Constants.COLOR_STYLE);
		}	
		
		linear.setBackgroundColor(org.anurag.file.quest.Constants.COLOR_STYLE);
		con = getBaseContext();
		size = new Point();
		
		getWindowManager().getDefaultDisplay().getSize(size);
		getSupportActionBar().setTitle("   File Quest Text Editor");
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Constants.COLOR_STYLE));
		getSupportActionBar().setIcon(R.drawable.file_quest_icon);
		
		title = (EditText)findViewById(R.id.textSearch); 
		main = (EditText)findViewById(R.id.note);
		main.setText("");
		dialog = new Dialog(getApplicationContext(), Constants.DIALOG_STYLE);
		dialog.setContentView(R.layout.delete_files);
		//dialog.getWindow().getAttributes().width = size.x*4/5;
		main.setLinksClickable(true);
		intent = getIntent();
		if(intent!=null){
			rec = new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					// TODO Auto-generated method stub
					if(arg1.getAction().equalsIgnoreCase("FQ_EDIT")){
						int r = SaveOnSD(f.getPath());
						if(r ==0)
							Toast.makeText(getBaseContext(), R.string.fsaved,Toast.LENGTH_SHORT).show();
						else if(r == -1)								
								Toast.makeText(getBaseContext(), R.string.xerror, Toast.LENGTH_SHORT).show();
						EditorActivity.this.finish();
					}else if(arg1.getAction().equalsIgnoreCase("FQ_EDIT_EXIT"))
						EditorActivity.this.finish();
				}
			};
			IntentFilter fi = new IntentFilter("FQ_EDIT");
			this.registerReceiver(rec, fi);
			fi = new IntentFilter("FQ_EDIT_EXIT");
			this.registerReceiver(rec, fi);
			MODIFIED = false;
			path = intent.getData().toString();
			Uri uri = intent.getData();
			f = new File(uri.getPath());
			title.setText(f.getName());
			title.setEnabled(false);
			openFile(f.getAbsolutePath());
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		init_system_ui();
	}
	
	
	@Override
	protected void onPause(){
		super.onPause();
		this.unregisterReceiver(rec);
	}
	/**
	 * 
	 * @param path
	 */
	public void openFile(final CharSequence path){
		rea = new StringBuffer();		
		final ProgressBar bar = (ProgressBar)findViewById(R.id.editorBar);
		final TextView message =(TextView)findViewById(R.id.editorMesage);
		new AsyncTask<Void,Void,Void>(){
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				main.setVisibility(View.VISIBLE);
				if(rea != null){
					openFile(path,rea);
					ERROR_SAVING = false;
					bar.setVisibility(View.GONE);
					message.setVisibility(View.GONE);
				}else{
					bar.setVisibility(View.GONE);
					message.setText("Failed to read file");
				}
			}
			
			@Override
			protected Void doInBackground(Void... arg0) {
				OPENING_FILE = false;
				char[] buffer = new char[1024];
				int read = 0;
				
				try{
					File file = new File(path.toString());
					if(!file.canRead()){
						/*
						 * READS THE FILE UNDER PROTECTED DIRECTORY...SYSTEM FILES WITH NO READ PERMISSION
						 */
						BufferedReader re = LinuxShell.execute("cat "+file.getPath());
						while((read = re.read(buffer,0,1000))!=-1)
							rea.append(buffer,0,read);
						re.close();
						openFile(path, rea);
						return null;
					}
					FileReader reader = new FileReader(new File(path.toString()));
					if(file.isFile() && file.length()>0){
						
						
						while((read=reader.read(buffer, 0 , 1000))!=-1)
							rea.append(buffer,0,read);
						reader.close();
					}
				}catch(FileNotFoundException e){
					Toast.makeText(getApplicationContext(), R.string.invalid, Toast.LENGTH_SHORT).show(); 
					//EditorActivity.this.finish();
					rea = null;
				}catch(IOException e){
					Toast.makeText(getBaseContext(), R.string.readerror,Toast.LENGTH_SHORT).show();
					//EditorActivity.this.finish();
					rea = null;
				}catch(Exception e){
					Toast.makeText(getBaseContext(), R.string.xerror, Toast.LENGTH_SHORT).show();
					//EditorActivity.this.finish();
					rea = null;
				}
				return null;
			}
			
		}.execute();
		
	}
	
	/**
	 * 
	 * @param path
	 * @param buffer
	 */
	void openFile(CharSequence path,StringBuffer buffer){
		String newText = buffer.toString();
		
		if(newText.indexOf("\r\n", 0)!=-1){
			FILEFORMAT = FILEFORMAT_CRNL;
			newText = newText.replace("\r","");
		}else if(newText.indexOf("\r",0)!=-1){
			FILEFORMAT = FILEFORMAT_CR;
			newText = newText.replace("\r", "\n");
		}else
			FILEFORMAT = FILEFORMAT_NL; 
		main.requestFocus();
		main.setHorizontallyScrolling(true);
		main.setText(newText);		
		main.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				MODIFIED = true;
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
		//main.setTextScaleX(size);
	}
	
	/**
	 * 
	 * @param path
	 */
	int SaveOnSD(CharSequence path){
		
		try{
			File file = new File(path.toString());
			if((file.exists()&&!file.canWrite())||(!file.exists()&& !file.getParentFile().canRead())){
				//WE DONOT HAVE ENOUGH PERMISSION TO WRITE THIS FILE...
				//DOING WORK AT ROOT 
				try {
					String pa = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/.ftemp.txt";
					SaveOnSD(pa);
					Process p = Runtime.getRuntime().exec("su");
					DataOutputStream os = new DataOutputStream(p.getOutputStream());
					os.writeBytes("mount -o remount,rw /dev/block/mtdblock3 / \n");
			        os.writeBytes("cat "+ pa +" > " + path +" \n");
			        os.writeBytes("umount /");
			        os.writeBytes("exit\n");
			        os.flush();					
			        return 0;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return -1;
				}			
			}
			file = null;//RESETING FILE TO NULL
			FileWriter writer = new FileWriter(path.toString());
			BufferedWriter out = new BufferedWriter(writer);
			if(FILEFORMAT == FILEFORMAT_CR)
				out.write(main.getText().toString().replace("\n","\r"));
			else if(FILEFORMAT == FILEFORMAT_CRNL)
				out.write(main.getText().toString().replace("\n", "\r\n"));
			else
				out.write(main.getText().toString());
			
			out.close();
			
		}catch(Exception e){
			return-1;
		}
		return 0;
	}


	
	@Override
	public boolean onKeyDown(int key,KeyEvent e){
		if(key == KeyEvent.KEYCODE_BACK){
			if(MODIFIED){
				new SaveOnExit(EditorActivity.this,size.x*8/9);
			}else
				EditorActivity.this.finish();
		}
		return false;
	}
	
	/**
	 * restyles the system UI like status bar or navigation bar if present....
	 */
	private void init_system_ui() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		SystemBarTintManager tint = new SystemBarTintManager(EditorActivity.this);
		tint.setStatusBarTintEnabled(true);
		tint.setStatusBarTintColor(Constants.COLOR_STYLE);
		SystemBarConfig conf = tint.getConfig();
		boolean hasNavBar = conf.hasNavigtionBar();
		if(hasNavBar){
			tint.setNavigationBarTintEnabled(true);
			tint.setNavigationBarTintColor(Constants.COLOR_STYLE);
		}
		LinearLayout main = (LinearLayout) findViewById(R.id.editor_layout);
		main.setPadding(0, getStatusBarHeight(), 0, hasNavBar ? getNavigationBarHeight() :0);
	}
	
	/**
	 * 
	 * @return height of status bar....
	 */
	private int getStatusBarHeight(){
		int res = 0;
		int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if(resId > 0)
			res = getResources().getDimensionPixelSize(resId);
		return res;
	}
	
	/**
	 * 
	 * @return the height of navigation bar....
	 */
	private int getNavigationBarHeight(){
		int res = 0;
		int resId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if(resId > 0)
			res = getResources().getDimensionPixelSize(resId);
		return res;
	}
	
	
}
