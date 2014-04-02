package org.ultimate.menuItems;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ReadZip {

	public ArrayList<File> list;
	String mSource;
	String mDest;
	File file;
	ZipFile zFile;
	public ReadZip(String source,String destination){
		// TODO Auto-generated constructor stub
		this.mSource = source;
		this.mDest = destination;
		this.file = new File(source);
		this.list = new ArrayList<File>();
	}
	
	public void read(){
		try{
			FileInputStream fis = new FileInputStream(file);
			//InputStreamReader re = new InputStreamReader(in);
			ZipInputStream st = new ZipInputStream(new BufferedInputStream(fis));
			//st.
			
			this.zFile = new ZipFile(file);
			Enumeration<?>e = zFile.entries();
			while(e.hasMoreElements()){
				ZipEntry entry = (ZipEntry) e.nextElement();
				if(entry.isDirectory())
					continue;
				else
					list.add(new File(entry.getName()));
			}
		}catch(IOException e){
			
		}
	}
	
	
}
