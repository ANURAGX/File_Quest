package org.anurag.text.editor;

import java.io.BufferedWriter;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.anurag.file.quest.R;
import org.ultimate.root.LinuxShell;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class EditorActivity extends Activity {
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
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_layout);
	
		con = getBaseContext();
		size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		
		title = (EditText)findViewById(R.id.textSearch); 
		main = (EditText)findViewById(R.id.note);
		main.setText("");
		dialog = new Dialog(getApplicationContext(), R.style.custom_dialog_theme);
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
	protected void onPause(){
		super.onPause();
		this.unregisterReceiver(rec);
	}
	/**
	 * 
	 * @param path
	 */
	public void openFile(final CharSequence path){
		final StringBuffer rea = new StringBuffer();		
		final ProgressBar bar = (ProgressBar)findViewById(R.id.editorBar);
		final TextView message =(TextView)findViewById(R.id.editorMesage);
		new AsyncTask<Void,Void,Void>(){
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				main.setVisibility(View.VISIBLE);
				openFile(path,rea);
				ERROR_SAVING = false;
				bar.setVisibility(View.GONE);
				message.setVisibility(View.GONE);
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
						 * READS THE FILE UNDER NON READ DIRECTORY...SYSTEM FILES WITH NO READ PERMISSION
						 */
						BufferedReader re = LinuxShell.execute("cat "+file.getPath());
						while((read = re.read(buffer,0,1000))!=-1)
							rea.append(buffer,0,read);
						re.close();
						openFile(path, rea);
						return null;
					}
					FileReader reader = new FileReader(new File(path.toString()));
					if(reader == null)
						throw(new FileNotFoundException());
					if(file.isDirectory())
						throw(new IOException());
					if(file.isFile() && file.length()>0){
						
						
						while((read=reader.read(buffer, 0 , 1000))!=-1)
							rea.append(buffer,0,read);
						reader.close();
					}
				}catch(FileNotFoundException e){
					Toast.makeText(getApplicationContext(), R.string.invalid, Toast.LENGTH_SHORT).show(); 
					EditorActivity.this.finish();
				}catch(IOException e){
					Toast.makeText(getBaseContext(), R.string.readerror,Toast.LENGTH_SHORT).show();
					EditorActivity.this.finish();
				}catch(Exception e){
					Toast.makeText(getBaseContext(), R.string.xerror, Toast.LENGTH_SHORT).show();
					EditorActivity.this.finish();
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
				new SaveOnExit(EditorActivity.this,size.x*4/5);
			}else
				EditorActivity.this.finish();
		}
		return false;
	}
	
	
	
	
}
