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


package org.anurag.file.quest;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 
 * this class is to build icons,size and type for the given file....
 * 
 * @author anurag
 *
 */
public class FileType {

	
	private Drawable draw;
	private String type;
	private String size;
	
	//strings for different files types
	private String flder;
	private String zip,rar,tar,z7;
	private String music;
	private String apk;
	private String sh;
	private String pdf;
	private String web;
	private String vids;
	private String text;
	private String img;
	private String docs;
	private String unk;
	
	
	public FileType(File f , Context ctx) {
		// TODO Auto-generated constructor stub
	
		zip = ctx.getString(R.string.zip);
		z7 = ctx.getString(R.string.zip7);
		rar = ctx.getString(R.string.rar);
		tar = ctx.getString(R.string.tar);
		flder = ctx.getString(R.string.directory);
		apk = ctx.getString(R.string.application);
		music = ctx.getString(R.string.music);
		sh = ctx.getString(R.string.script);
		pdf = ctx.getString(R.string.pdf);
		web = ctx.getString(R.string.web);
		vids = ctx.getString(R.string.vids);
		img = ctx.getString(R.string.image);
		text = ctx.getString(R.string.text);
		docs = ctx.getString(R.string.docs);
		unk = ctx.getString(R.string.unknown);
		
		draw = buildIcon(f);
		if(draw == null){
			draw = ctx.getResources().getDrawable(R.drawable.sdcard);
		}		
		
		size = getSize(f, ctx);
	}
	
	/**
	 * 
	 * @return the icon for the file
	 */
	public Drawable getIcon(){
		return this.draw;
	}
	
	/**
	 * 
	 * @return the type of file
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSize(){
		return this.size;
	}
	
	/**
	 * 
	 * @return icon for the file....
	 */
	private Drawable buildIcon(File f){
		if(f.isDirectory()){
			type = flder;
			if(Constants.isExtAvailable)
				if(f.getAbsolutePath().equalsIgnoreCase(Constants.EXT_PATH))
					return null;
			return Constants.FOLDER_IMAGE;
		}
		
		String name = f.getName().toLowerCase(Locale.ENGLISH);
		
		if(name.endsWith(".zip")){
			
			type = zip;
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".7z")){
			
			type = z7;
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".rar")){
			
			type = rar;
			return Constants.ARCHIVE;
			
		}else if(name.endsWith(".tar")||name.endsWith(".tar.gz")||name.endsWith(".tar.bz2")){
			
			type = tar;
			return Constants.ARCHIVE;
			
		}
		else if(name.endsWith(".mp3")||name.endsWith(".ogg")||name.endsWith(".m4a")||name.endsWith(".wav")
				||name.endsWith(".amr")){
			
			type = music;
			return Constants.MUSIC;
			
		}
		else if(name.endsWith(".apk")){
			
			type = apk;
			return Constants.APP;
			
		}else if(name.endsWith(".sh")||name.endsWith(".prop")||name.endsWith("init")
				||name.endsWith(".default")||name.endsWith(".rc")){
			
			type = sh;
			return Constants.SCRIPT;
			
		}else if(name.endsWith(".pdf")){
			
			type = pdf;
			return Constants.PDF;
			
		}else if(name.endsWith(".htm")||name.endsWith(".html")||name.endsWith(".mhtml")){
			
			type = web;
			return Constants.WEB;
			
		}else if(name.endsWith(".flv")||name.endsWith(".mp4")||name.endsWith(".3gp")||name.endsWith(".avi")
				||name.endsWith(".mkv")){
			
			type = vids;
			return Constants.VIDEO;
			
		}	
		else if(name.endsWith(".bmp")||name.endsWith(".gif")||name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".png")){
			
			type = img;
			return Constants.IMAGE;
			
		}else if(name.endsWith(".txt")||name.endsWith(".log")||name.endsWith(".ini")){
			
			type = text;
			return Constants.DOCS;
			
		}
		else if(name.endsWith(".doc")||name.endsWith(".ppt")||name.endsWith(".docx")||name.endsWith(".DOC")
				||name.endsWith(".pptx")||name.endsWith(".csv")){
			
			type = docs;
			return Constants.DOCS;
			
		}
		else{
			
			type = unk;
			return Constants.UNKNOWN;			
		}		
	}
	
	/**
	 * 
	 * 
	 * @return the folder string
	 */
	public String getFolderString(){
		return flder;
	}
	
	/**
	 * 
	 * @param f
	 * @param ctx
	 * @return
	 */
	private String getSize(File f , Context ctx){
		if(f.isDirectory()){
			if(!f.canRead())
				return ctx.getString(R.string.rootd);
			try{
				return f.list().length+" "+ctx.getString(R.string.items);
			}catch(NullPointerException e){
				return 0+" "+ctx.getString(R.string.items);
			}
		}	
		long size = f.length();
		if(size>Constants.GB)
			return String.format(Constants.GB_STR , (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format(Constants.MB_STR, (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(Constants.KB_STR, (double)size/(1024));
		
		else
			return String.format(Constants.BYT_STR , (double)size);
	}
	
}
