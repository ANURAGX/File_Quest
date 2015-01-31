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

package org.anurag.compress;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author ANURAG
 *
 */
public class TarObj {
	
	TarArchiveEntry ent;
	String name;
	String path;
	boolean isFile;
	Context ctx;
	Drawable icon;
	String size;
	String type;
	public TarObj(TarArchiveEntry entry , String fname , String pname,Context ct) {
		// TODO Auto-generated constructor stub
		this.ent = entry;
		this.name = fname;
		this.path = pname;
		this.ctx = ct;
		this.isFile = checkForFile();
		this.size = size(entry.getSize(), ct);
		this.type = getType(this.name, ctx);
	}

	/**
	 * 
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * 
	 * @return
	 */
	public Drawable getIcon(){
		return this.icon;
	}
	/**
	 * 
	 * @return
	 */
	public String getPath(){
		if(path.equalsIgnoreCase(""))
			return this.name;
		return this.path+"/"+this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	public TarArchiveEntry getEntry(){
		return this.ent;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEntryName(){
		return this.ent.getName();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFile(){
		return this.isFile;
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
	 * @return
	 */
	private boolean checkForFile(){
		String str = ent.getName().substring(path.length(), ent.getName().length());
		if(str.startsWith("/"))
			str = str.substring(1, str.length());
		if(str.contains("/"))
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param size
	 * @param mContext
	 * @return
	 */
	private String size(long size , Context mContext){
		if(size>Constants.GB)
			return String.format(mContext.getString(R.string.sizegb), (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format(mContext.getString(R.string.sizemb), (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(mContext.getString(R.string.sizekb), (double)size/(1024));
		
		else
			return String.format(mContext.getString(R.string.sizebytes), (double)size);
	}
	/**
	 * 
	 * @param Name
	 * @return
	 */
	private String getType(String Name , Context ctx){
		
		/*
		 * ITS DIRECTORY CHECK......
		 */
		if(!isFile()){
			this.icon = ctx.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]);
			return ctx.getString(R.string.directory);
		}	
		
		/*
		 * NOW FILE CHECK FOR THEIR TYPES....
		 */
		
		if(Name.endsWith("jpg")||Name.endsWith(".JPG")|| Name.endsWith(".png") || Name.endsWith(".PNG") || Name.endsWith(".gif") || Name.endsWith(".GIF")
				|| Name.endsWith(".JPEG") || Name.endsWith(".jpeg") ||Name.endsWith(".bmp") ||Name.endsWith(".BMP")){
			this.icon = Constants.IMAGE;
			return ctx.getString(R.string.image);
		}	
		else if(Name.endsWith(".zip") || Name.endsWith(".ZIP")){
			this.icon = Constants.ARCHIVE;
			return ctx.getString(R.string.zip);
		}	
		else if( Name.endsWith("mhtml")||Name.endsWith(".MHTML")||  Name.endsWith(".HTM") || Name.endsWith(".htm") 
				||Name.endsWith(".html") || Name.endsWith(".HTML")){
			this.icon = Constants.WEB;
			return ctx.getString(R.string.web);
		}			
		else if(Name.endsWith(".tar") || Name.endsWith(".TAR") || Name.endsWith(".rar") 
				|| Name.endsWith("RAR") || Name.endsWith(".7z") || Name.endsWith(".7Z")||
				Name.endsWith(".GZ") || Name.endsWith(".gz")){
			this.icon = Constants.ARCHIVE;
			return ctx.getString(R.string.compr);
		}	
		else if(Name.endsWith(".apk") || Name.endsWith(".APK")){
			this.icon = Constants.APP;
			return ctx.getString(R.string.application);
		}	
		else if(Name.endsWith(".mp3") || Name.endsWith(".MP3") || Name.endsWith(".amr") || Name.endsWith(".AMR")
				|| Name.endsWith(".ogg") || Name.endsWith(".OGG")||Name.endsWith(".m4a")||Name.endsWith(".M4A")){
			this.icon = Constants.MUSIC;
			return ctx.getString(R.string.music);
		}	
		else if(Name.endsWith(".doc") ||Name.endsWith(".DOC")
				|| Name.endsWith(".DOCX") || Name.endsWith(".docx") || Name.endsWith(".ppt") || Name.endsWith(".PPT")){
			this.icon = Constants.DOCS;
			return ctx.getString(R.string.document);
		}	
		else if(Name.endsWith(".txt") || Name.endsWith(".TXT") || Name.endsWith(".inf") || Name.endsWith(".INF")){
			this.icon = Constants.DOCS;
			return ctx.getString(R.string.text);
		}	
		else if(Name.endsWith(".mp4") || Name.endsWith(".MP4") || Name.endsWith(".avi") ||Name.endsWith(".AVI")
				|| Name.endsWith(".FLV") || Name.endsWith(".flv") || Name.endsWith(".3GP") || Name.endsWith(".3gp")
				||Name.endsWith(".mkv") || Name.endsWith(".MKV")){
			this.icon = Constants.VIDEO;
			return ctx.getString(R.string.vids);		
		}	
		else if(Name.endsWith(".default")||Name.endsWith(".prop")||Name.endsWith(".rc")||Name.endsWith(".sh")||Name.endsWith("init")){
			this.icon = Constants.SCRIPT;
			return ctx.getString(R.string.script);
		}	
		this.icon = Constants.UNKNOWN;
		return ctx.getString(R.string.unknown);
	}
	
	
}
