package org.anurag.file.quest;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

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
	File file; 	
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
		
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
					case 1:
							//Displaying File counts of each type
							TextView count = (TextView)v.findViewById(R.id.mFiles);
							count.setText(music.size() + " Items");
							
							count = (TextView)v.findViewById(R.id.aFiles);
							count.setText(apps.size() + " Items");
							
							count = (TextView)v.findViewById(R.id.dFile);
							count.setText(doc.size() + " Items");
							
							count = (TextView)v.findViewById(R.id.pFiles);
							count.setText(img.size() + " Items");
							
							count = (TextView)v.findViewById(R.id.vFiles);
							count.setText(vids.size() + " Items");
							
							count = (TextView)v.findViewById(R.id.zFiles);
							count.setText(zip.size() + " Items");
							
							count = (TextView)v.findViewById(R.id.misFiles);
							count.setText(mis.size() + " Items");
							loaded = true;
							break;
				
					case 2:
							//DISLPAYS MUSIC SIZE..
							TextView mSize= (TextView)v.findViewById(R.id.mSize);
							mSize.setText((CharSequence) msg.obj);
							break;
							
					case 3:
							//DISPLAYS APPS SIZE...
							TextView aSize= (TextView)v.findViewById(R.id.aSize);
							aSize.setText((CharSequence) msg.obj);
							
							break;
							
					case 4:
						
							//DSIPLAYS DOCS SIZE...
							TextView dSize= (TextView)v.findViewById(R.id.dSize);
							dSize.setText((CharSequence) msg.obj);
							break;
					case 5:
							//displays IMAGE SIZE..
							TextView iSize= (TextView)v.findViewById(R.id.pSize);
							iSize.setText((CharSequence) msg.obj);
							break;
							
					case 6:
							//displays video size...
							TextView vSize= (TextView)v.findViewById(R.id.vSize);
							vSize.setText((CharSequence) msg.obj);
							break;
							
							
					case 7:
						
							//displays archive size...
							TextView zSize= (TextView)v.findViewById(R.id.zSize);
							zSize.setText((CharSequence) msg.obj);
							break;
							
					case 8:
							//displays miscellaneous size...
							TextView mmSize= (TextView)v.findViewById(R.id.misSize);
							mmSize.setText((CharSequence) msg.obj);
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
				
				Message msg = new Message();
				msg.what = 2;
				msg.obj = size(music);
				handle.sendMessage(msg);
				
				msg = new Message();
				msg.what = 3;
				msg.obj = size(apps);
				handle.sendMessage(msg);
				
				msg = new Message();
				msg.what = 4;
				msg.obj = size(doc);
				handle.sendMessage(msg);
				
				msg = new Message();
				msg.what = 5;
				msg.obj = size(img);
				handle.sendMessage(msg);
				
				msg = new Message();
				msg.what = 6;
				msg.obj = size(vids);
				handle.sendMessage(msg);
				
				msg = new Message();
				msg.what = 7;
				msg.obj = size(zip);
				handle.sendMessage(msg);
				
				msg = new Message();
				msg.what = 8;
				msg.obj = size(mis);
				handle.sendMessage(msg);
			}
		});
		thread.start();
	}
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public String size(ArrayList<File> list){
		int l = list.size();
		long size = 0;
		for(int i = 0 ; i<l;++i)
			size += list.get(i).length(); 
		
		if(size>1024*1024*1024)
			return String.format("%.2f GB", (double)size/(1024*1024*1024));
		
		else if(size > 1024*1024)
			return String.format("%.2f MB", (double)size/(1024*1024));
		
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
			if(SimpleAdapter.getFileType(file)==null)
				mis.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("song"))
				music.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("image"))
				img.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("video"))
				vids.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("zip"))
				zip.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("compressed"))
				zip.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("apk"))
				apps.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("document"))
				doc.add(file);
			else if(SimpleAdapter.getFileType(file).equalsIgnoreCase("text"))
				doc.add(file);
		}else if(file.isDirectory()){
			for(File f:file.listFiles())
				makelist(f);
		}
	}
}
