package org.anurag.file.quest;

import java.io.File;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Utils {
	View v;
	public static boolean loaded;
	public static ArrayList<File> music;
	public static ArrayList<File> apps;
	public static ArrayList<File> vids;
	public static ArrayList<File> doc;
	public static ArrayList<File> zip;
	public static ArrayList<File> mis;
	public static ArrayList<File> img;
	
	long musicsize=0;
	long apksize=0;
	long vidsize=0;
	long docsize=0;
	long zipsize=0;
	long missize=0;
	long imgsize=0;
	int size = 0;
	File file; 	
	String message;
	Handler handle;
	TextView count;
	public Utils(View view) {
		// TODO Auto-generated constructor stub
		v = view;
		loaded = false;
		file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		music = new ArrayList<File>();
		apps = new ArrayList<File>();
		vids = new ArrayList<File>();
		doc = new ArrayList<File>();
		zip = new ArrayList<File>();
		mis = new ArrayList<File>();
		img = new ArrayList<File>();
	}
	
	public void load(){
		
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 1:
							//Displaying File counts of each type
							loaded = true;
							break;
				
					case 2:
							//DISLPAYS MUSIC SIZE..
							TextView mSize= (TextView)v.findViewById(R.id.mSize);
							mSize.setText(message);
							
							count = (TextView)v.findViewById(R.id.mFiles);
							count.setText(music.size() + " Items");
							break;
							
					case 3:
							//DISPLAYS APPS SIZE...
							TextView aSize= (TextView)v.findViewById(R.id.aSize);
							aSize.setText(message);
							
							
							count = (TextView)v.findViewById(R.id.aFiles);
							count.setText(apps.size() + " Items");
							break;
							
					case 4:
						
							//DSIPLAYS DOCS SIZE...
							TextView dSize= (TextView)v.findViewById(R.id.dSize);
							dSize.setText(message);
							
							count = (TextView)v.findViewById(R.id.dFile);
							count.setText(doc.size() + " Items");
							break;
					case 5:
							//displays IMAGE SIZE..
							TextView iSize= (TextView)v.findViewById(R.id.pSize);
							iSize.setText(message);
							
							count = (TextView)v.findViewById(R.id.pFiles);
							count.setText(img.size() + " Items");
							break;
							
					case 6:
							//displays video size...
							TextView vSize= (TextView)v.findViewById(R.id.vSize);
							vSize.setText(message);
							
							
							count = (TextView)v.findViewById(R.id.vFiles);
							count.setText(vids.size() + " Items");
							break;
							
							
					case 7:
						
							//displays archive size...
							TextView zSize= (TextView)v.findViewById(R.id.zSize);
							zSize.setText(message);
							
							count = (TextView)v.findViewById(R.id.zFiles);
							count.setText(zip.size() + " Items");
							break;
							
					case 8:
							//displays miscellaneous size...
							TextView mmSize= (TextView)v.findViewById(R.id.misSize);
							mmSize.setText(message);
							
							count = (TextView)v.findViewById(R.id.misFiles);
							count.setText(mis.size() + " Items");
							break;
				}
			}
		};		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loaded = false;
				makelist(file);
				handle.sendEmptyMessage(1);				
			}
		});
		thread.start();
	}
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public String size(long size){
		if(size>Constants.GB)
			return String.format("%.2f GB", (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format("%.2f MB", (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format("%.2f KB", (double)size/(1024));
		
		else
			return String.format("%.2f Bytes", (double)size);
	}
	
	/**
	 * 
	 * @param file
	 */
	void makelist(File file){
		if(file.isFile()){
			if(SimpleAdapter.getFileType(file)==null){
				mis.add(file);
				missize+=file.length();
				message = size(missize);
				size = mis.size();
				handle.sendEmptyMessage(8);
			}	
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("song")){
				music.add(file);
				musicsize+=file.length();
				message = size(musicsize);
				size = music.size();
				handle.sendEmptyMessage(2);
			}	
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("image")){
				img.add(file);
				imgsize+=file.length();
				message = size(imgsize);
				size = img.size();
				handle.sendEmptyMessage(5);
			}	
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("video")){
				vids.add(file);
				vidsize+=file.length();
				message = size(vidsize);
				size = vids.size();
				handle.sendEmptyMessage(6);
			}	
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("zip")||SimpleAdapter.getFileType(file).equalsIgnoreCase("compressed")){
				zip.add(file);
				zipsize+=file.length();
				message = size(zipsize);
				size = zip.size();
				handle.sendEmptyMessage(7);
			}else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("apk")){
				apps.add(file);
				apksize+=file.length();
				message = size(apksize);
				size = apps.size();
				handle.sendEmptyMessage(3);
			}	
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("document")||
					SimpleAdapter.getFileType(file).equalsIgnoreCase("text")){
				doc.add(file);
				docsize+=file.length();
				message = size(docsize);
				size = doc.size();
				handle.sendEmptyMessage(4);
			}	
			
		}else if(file.isDirectory()){
			for(File f:file.listFiles())
				makelist(f);
		}
	}
}
