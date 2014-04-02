package org.anurag.archives;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.anurag.file.quest.R;
import org.anurag.file.quest.RootAdapter;
import org.anurag.file.quest.TaskerActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ZipArchive {
	public static ArrayList<ZipEntry> list;
	public static ArrayList<ZipObj> objList;
	Context ctx;
	
	public ZipArchive(File zip,Context c) {
		// TODO Auto-generated constructor stub
		list = new ArrayList<ZipEntry>();
		ctx = c;
		objList = new ArrayList<ZipArchive.ZipObj>();
		try {
			ZipFile file = new ZipFile(zip);
			Enumeration<? extends ZipEntry> entry = file.entries();
			while(entry.hasMoreElements()){
				ZipEntry e = entry.nextElement();
				if(e.isDirectory())
					continue;
				list.add(e);				
			}
			listContents("app/libs");
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	static class Holder{
		ImageView icon;
		TextView fName;
		TextView fType;
		TextView fSize;
		CheckBox box;
	}
	
	public static class ZipAdapter extends ArrayAdapter<ZipObj>{

		Context ctx;
		
		public ZipAdapter(Context context, int resource, List<ZipObj> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
			ctx = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder h = new Holder();
			if(convertView == null){
				h = new Holder();
				LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_1, parent , false);
				h.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
				h.fName = (TextView)convertView.findViewById(R.id.fileName);
				h.fType = (TextView)convertView.findViewById(R.id.fileType);
				h.fSize = (TextView)convertView.findViewById(R.id.fileSize);
				h.box = (CheckBox)convertView.findViewById(R.id.checkbox);
				convertView.setTag(h);
			}			
			else
				h = (Holder)convertView.getTag();
			
			h.fName.setText(objList.get(position).getName());
			h.icon.setImageDrawable(objList.get(position).getIcon());
			
			return convertView;
		}		
	}
	
	/**
	 * 
	 * @author Anurag
	 */
	class ZipObj{
		String eName;
		boolean isDir;
		ZipEntry entry;
		String type;
		String size;
				
		/**
		 * TAKES THE NAME FROM THE ENRTY.....
		 * @param name
		 */
		public ZipObj(ZipEntry fullPath , String name) {
			// TODO Auto-generated constructor stub
			this.entry = fullPath;
			if(!fullPath.equals("/"))
				this.eName = fullPath.getName();
			else
				this.eName = name;
		}
		
		
		/**
		 * 
		 * @return
		 */
		public String getPath(){
			return this.entry.getName(); 
		}
		/**
		 * RETURNS THE ICON FOR THE FILES INSIDE THE ZIP ARCHIVE..... 
		 * @return
		 */
		public Drawable getIcon(){
			if(isDir)
				TaskerActivity.mContext.getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]);
			
			else{
				//IMPLEMENT FOR DIFFERENT FILES....
				
			}
			return null;
		}
		
		public String getName(){
			return this.eName;
		}
		
	}
	
	
	/**
	 * THIS FUNCTION THE EXTENSION OF THE GIVEN FILE
	 * @param f
	 * @return THE EXTENSION OF A FILE
	 */
	public String getExt(String name){
		try{
			return name.substring(name.lastIndexOf("."), name.length());
		}catch(IndexOutOfBoundsException e){
			return "";
		}
	}
	
	/**
	 * 
	 * @param path
	 */
	public void listContents(String path){
		int len = list.size();
		String virt = path;
		if(path.equalsIgnoreCase("/")){
			for(int i = 0 ; i < len ; ++i){
				ZipEntry ent = list.get(i);
				if(!ent.isDirectory()&& !ent.getName().contains("/"))
					objList.add(new ZipObj(ent,ent.getName()));
				else if(ent.isDirectory())
					objList.add(new ZipObj(ent,ent.getName()));
				else
					cmp(ent,"/");
			}
		}else{
			for(int i=0;i<len;++i){
				virt = path;
				ZipEntry ent = list.get(i);
				String eName = ent.getName();
				if(eName.startsWith(path)){
					//objList.add(new ZipObj(ent,path));
					//break;
					if(path.contains("/")){
						
					}
					cmp(ent,path);
				}else if(ent.isDirectory())
					continue;				
			}			
		}
	}
	
	/**
	 * 	
	 * @param entry
	 */
	public void cmp(ZipEntry entry,String name){
		int l = objList.size();
		String cmp = entry.getName();
		//cmp = cmp.substring(name.length(), cmp.indexOf("/"));
		boolean flag = true;
		for(int i=0;i<l;++i){
			if(name.startsWith(objList.get(i).getPath())){
				flag = false;
				break;
			}
		}
		if(flag)
			objList.add(new ZipObj(entry,name));
	}	
}
