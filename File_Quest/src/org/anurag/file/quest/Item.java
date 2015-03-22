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
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.github.junrar.rarfile.FileHeader;

/**
 * 
 * @author anurag
 *
 */
public class Item {
	
	//Ordinary file handling....
	private File file;
	private String name;
	private String path;
	private String parent;
	private Drawable icon;
	private String size;
	private String type;
	private boolean isLocked;
	private boolean isFav;
	private boolean isDir;
	/**
	 * 
	 * @param fi
	 * @param img
	 * @param typ
	 * @param si
	 */
	public Item(File fi , Drawable img , String typ , String si) {
		// TODO Auto-generated constructor stub
		this.file = fi;
		this.parent = file.getParent();
		this.path = file.getPath();
		this.name = file.getName();
		this.icon = img;
		this.type = typ;
		this.size = si;
		this.isLocked = Constants.db.isLocked(this.path);
		this.isFav = Constants.db.isFavItem(this.path);
		this.isDir = file.isDirectory();
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
	public String getPath(){
		return this.path;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getParent(){
		return this.parent;
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
		this.size = size(zSize);
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
	public boolean canRead(){
		return file.canRead();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canWrite(){
		return file.canWrite();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canExecute(){
		return file.canExecute();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDirectory(){
		return (isDir);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean exists(){
		try{
			return this.file.exists();
			
		}catch(NullPointerException e){
			
		}
		return true;
	}
	/**
	 * 
	 * @return
	 */
	public File getFile(){
		return this.file;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isLocked(){
		return this.isLocked;
	}
	
	/**
	 * 
	 * @param lockstatus
	 */
	public void setLockStatus(boolean lockstatus){
		this.isLocked = lockstatus;
	}
	
	/**
	 * 
	 * @param favStatus
	 */
	public void setFavStatus(boolean favStatus){
		this.isFav = favStatus;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFavItem(){
		return this.isFav;
	}
	
	//---------------------------------------------------//
	//---------------------------------------------------//
	//Zip file handling Related stuff(inside zip archive)

	private String z_path;
	private String z_name;
	private String z_entry;
	private ZipEntry z;
	
	/**
	 * 
	 * @param zPath
	 * @param zName
	 * @param zEntry
	 * @param zSize
	 * @param ctx
	 * @param entry
	 */
	public Item(String zPath,String zName,String zEntry,long zSize,Context ctx , ZipEntry entry) {
		// TODO Auto-generated constructor stub
		
		this.z_path = zPath;
		
		if(!z_path.equalsIgnoreCase(""))
			this.path = this.z_path + "/" + zName;
		else
			this.path = zName;
		
		this.name = this.z_name = zName;
		this.z_entry = zEntry;
		this.isDir = !z_checkForFile();
		this.z = entry;		
		FileType t = new FileType(new File(zPath), ctx);
		if(isDir){
			this.type = t.getFolderString();
			this.icon = Constants.FOLDER_IMAGE;
			this.size = "";
		}else{
			this.type = t.getType();
			this.icon = t.getIcon();
			this.size = size(zSize);
		}
	}
	
	private String size(long size){
		if(size>Constants.GB)
			return String.format(Constants.GB_STR, (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format(Constants.MB_STR, (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(Constants.KB_STR, (double)size/(1024));
		
		else
			return String.format(Constants.BYT_STR, (double)size);
	}	
	

	/**
	 * 
	 * @return
	 */
	public String z_getName(){
		return this.z_name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String z_getPath(){
		String str = this.z_path + "/"+this.z_name;
		return str;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEntry(){
		return this.z_entry;
	}
	
		
	/**
	 * 
	 * @return
	 */
	public ZipEntry getZipEntry(){
		return this.z;
	}
	
	
	/**
	 * 
	 * @return 
	 */
	private boolean z_checkForFile(){
		String str = z_entry.substring(z_path.length(), z_entry.length());
		if(str.startsWith("/"))
			str = str.substring(1, str.length());
		if(str.contains("/"))
			return false;
		return true;
	}
	

	//---------------------------------------------------//
	//---------------------------------------------------//
	//RAR file handling Related stuff(inside rar archive)
	

	private String headername;

	private FileHeader fh;
	
	/**
	 * 
	 * @param header
	 * @param Name
	 * @param Path
	 * @param ctx
	 */
	public Item(FileHeader header, String Name , String Path , Context ctx) {
		// TODO Auto-generated constructor stub
		if(header.isUnicode())
			this.headername = header.getFileNameW();
		else
			this.headername = header.getFileNameString();
		this.name = Name;
		this.path = Path;
		this.fh = header;
		this.isDir = !r_checkForFile();

		FileType t = new FileType(new File(path), ctx);
		if(isDir){
			this.type = t.getFolderString();
			this.icon = Constants.FOLDER_IMAGE;
			this.size = "";
		}else{
			this.type = t.getType();
			this.icon = t.getIcon();
			this.size = size(fh.getFullPackSize());
		}		
	}
	
	/**
	 * 
	 * @return
	 */
	public FileHeader r_getFileHeader(){
		return this.fh;
	}
	
	/**
	 * 
	 * @return
	 */
	public String r_getFileHeaderName(){
		return this.headername;
	}
	
	/**
	 * 
	 * @return true if file....
	 */
	private boolean r_checkForFile(){
		String str = headername.substring(path.length(), headername.length());
		if(str.startsWith("\\"))
			str = str.substring(1, str.length());
		if(str.contains("\\"))
			return false;
		return true;
	}
	
	

	//---------------------------------------------------//
	//---------------------------------------------------//
	//TAR file handling Related stuff(inside tar archive)
			
	private TarArchiveEntry ent;
	/**
	 * 
	 * @param entry
	 * @param fname
	 * @param pname
	 * @param ct
	 */
	public Item(TarArchiveEntry entry , String fname , String pname,Context ct) {
		// TODO Auto-generated constructor stub
		this.ent = entry;
		this.name = fname;
		this.path = pname;
		
		this.isDir = !t_checkForFile();

		FileType t = new FileType(new File(path), ct);
		if(isDir){
			this.type = t.getFolderString();
			this.icon = Constants.FOLDER_IMAGE;
			this.size = "";
		}else{
			this.type = t.getType();
			this.icon = t.getIcon();
			this.size = size(fh.getFullPackSize());
		}	
		
	}
	
	/**
	 * 
	 * @return true if it is file
	 */
	private boolean t_checkForFile(){
		String str = ent.getName().substring(path.length(), ent.getName().length());
		if(str.startsWith("/"))
			str = str.substring(1, str.length());
		if(str.contains("/"))
			return false;
		return true;
	}

	
}
