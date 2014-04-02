package org.ultimate.menuItems;

import java.io.File;


import java.util.ArrayList;

import org.anurag.file.quest.R;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MultiZip{

	ZipFile zip;
	ArrayList<File> list;
	ProgressBar bar;
	String DEST;
	CheckBox normal;
	CheckBox max;
	TextView level;
	Button st,ca;
	boolean running;
	TextView name;
	TextView lo;
	TextView toFiles;
	Context mContext;
	Dialog dialog;
	
	
	public MultiZip(Context context,int width,ArrayList<File> fList,long totalFiles,String locat,String dest) {
		// TODO Auto-generated constructor stub
		mContext = context;
		list = fList;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.zip_file_dialog);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().width = width;
		toFiles = (TextView)dialog.findViewById(R.id.zipNoOfFiles);
		toFiles.setText("TOTAL FILES : "+ totalFiles);
		lo = (TextView)dialog.findViewById(R.id.zipFileLocation); 
		lo.setText("LOCATION : "+locat);
		DEST = dest+"/Archive.zip";
		onCreate();
	}
	
 
	protected void onCreate(){
		
		//params.width = p.x*7/9;
				
		
		level = (TextView)dialog.findViewById(R.id.zipFormat); 
		normal = (CheckBox)dialog.findViewById(R.id.zipChioce);
		max = (CheckBox)dialog.findViewById(R.id.tarChioce); 
		bar = (ProgressBar)dialog.findViewById(R.id.zipProgressBar);
		
		st = (Button)dialog.findViewById(R.id.zipOkButton);
		ca = (Button)dialog.findViewById(R.id.zipCalcelButton);
		running = false;
		name = (TextView)dialog.findViewById(R.id.zipFileName);
		
		name.setText("ARCHIVE NAME : Archive.zip");
		
		normal.setText("Normal");
		max.setText("Max");
		level.setText("COMPRESSION");
		normal.setChecked(true);
		max.setChecked(false);
		
		
		normal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				normal.setChecked(true);
				max.setChecked(false);
			}
		});
		
		max.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				normal.setChecked(false);
				max.setChecked(true);
			}
		});
		st.setText("Start");		
		st.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				normal.setEnabled(false);
				max.setEnabled(false);
				ca.setEnabled(false);
				st.setEnabled(false);
				running = true;
				dialog.setCancelable(false);
				new ZipFiles().execute();				
			}
		});
		ca.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private class ZipFiles extends AsyncTask<Void, String, Void>{
			@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(mContext, R.string.zipCompleted, Toast.LENGTH_SHORT).show();
			mContext.sendBroadcast(new Intent("FQ_DELETE"));
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			lo.setText(values[0]);
			toFiles.setText(values[1]);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			File file;
			int j = 0 , c = 0;
			int l = list.size();
			for(int i = 0 ; i <l ; ++i){
				file = (File) list.get(i);
				if(file!=null)
					c++;
			}
			if(new File(DEST).exists())
				new File(DEST).delete();
			ZipParameters para = new ZipParameters();
			if(normal.isChecked())
				para.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			else if(max.isChecked())
				para.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_MAXIMUM);
			try{
				zip = new ZipFile(DEST);
				for(int i = 0 ; i<l;++i){
					file = (File)list.get(i);
					if(file!=null){
						j++;
						String[] res = {"Zipping : " + file.getName(),j-1 + " of " + c + " Zipped"};
						publishProgress(res);
						if(file.isDirectory())
							zip.addFolder(file, para);
						else if(file.isFile())
							zip.addFile(file, para);
					}
				}
			}catch(Exception e){
				
			}
			return null;
		}
	}
		
}
