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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.io.File;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class FileObj extends File{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4723396612036576710L;
	Drawable icon;
	String type;
	String size;
	public FileObj(File fi) {
		// TODO Auto-generated constructor stub
		super(fi.getAbsolutePath());
		//this.size = size(fi, ctx);
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
	 * @param f
	 * @param mContext
	 * @return
	 */
	private String size(File f,Context mContext){
		if(f.isDirectory()){
			return f.list().length+" "+mContext.getString(R.string.items);
		}
		long size = f.length();
		if(size>Constants.GB)
			return String.format(mContext.getString(R.string.sizegb), (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format(mContext.getString(R.string.sizemb), (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(mContext.getString(R.string.sizekb), (double)size/(1024));
		
		else
			return String.format(mContext.getString(R.string.sizebytes), (double)size);
	}
}
