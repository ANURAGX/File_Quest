/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * anurag.dev1512@gmail.com
 *
 */
package org.anurag.file.quest;

import java.io.File;
import java.util.ArrayList;

public class MediaManager {
	
	private static ArrayList<File> zList;
	private static ArrayList<File> sList;
	private static ArrayList<File> iList;
	private static ArrayList<File> dList;
	private static ArrayList<File> vList;
	private static ArrayList<File> aList;
	private static ArrayList<File> cList;
	
	public MediaManager(){
		zList = new ArrayList<File>();
		sList = new ArrayList<File>();
		iList = new ArrayList<File>();
		dList = new ArrayList<File>();
		vList = new ArrayList<File>();
		aList = new ArrayList<File>();
		cList = new ArrayList<File>();
	}
	
	
	public void clearList(){
		zList.clear();
		sList.clear();
		iList.clear();
		dList.clear();
		vList.clear();
		aList.clear();
		cList.clear();
	}
	
	/**
	 * Function To return All the Zipped Files Present In Phone
	 * @param file
	 * @return
	 */
	public static ArrayList<File> getZipFiles(File file){
		if(file.listFiles() !=null){
			for(File f:file.listFiles()){
				String na = f.getName();
				if(f.isDirectory())
					getZipFiles(f);
				else if(na.endsWith(".tar") || na.endsWith(".TAR") || na.endsWith(".rar") 
						|| na.endsWith("RAR") || na.endsWith(".7z") || na.endsWith(".7Z")||
						na.endsWith(".zip") || na.endsWith(".ZIP"))
					zList.add(f);
			}	
		}
		return zList;
	}
	
	/**
	 * Function To return All the Other Compressed Files Present In Phone
	 * @param file
	 * @return
	 */
	public static ArrayList<File> getCompressedFiles(File file){
		if(file.listFiles() !=null){
			for(File f:file.listFiles()){
				String na = f.getName();
				if(f.isDirectory())
					getCompressedFiles(f);
				else if(na.endsWith(".tar") || na.endsWith(".TAR") || na.endsWith(".rar") 
						|| na.endsWith("RAR") || na.endsWith(".7z") || na.endsWith(".7Z")||
						na.endsWith(".zip") || na.endsWith(".ZIP")||na.endsWith(".apk") || na.endsWith(".APK")||
						na.endsWith(".mp3") || na.endsWith(".MP3") || na.endsWith(".amr") || na.endsWith(".AMR")
						|| na.endsWith(".ogg") || na.endsWith(".OGG")||na.endsWith(".pdf") || na.endsWith(".PDF") || na.endsWith(".doc") || na.endsWith(".DOC")
						|| na.endsWith(".DOCX") || na.endsWith(".docx") || na.endsWith(".ppt") ||
						na.endsWith(".txt")||na.endsWith(".TXT")||na.endsWith(".PPT")||na.endsWith(".png") || na.endsWith(".PNG") || na.endsWith(".jpg")
						|| na.endsWith(".JPG") || na.endsWith(".gif") || na.endsWith(".GIF")
						|| na.endsWith(".JPEG") || na.endsWith(".jpeg") || na.endsWith(".bmp") || na.endsWith(".BMP")||na.endsWith(".mp4") || na.endsWith(".MP4") || na.endsWith(".avi") || na.endsWith(".AVI")
						|| na.endsWith(".FLV") || na.endsWith(".flv") || na.endsWith(".3GP") || na.endsWith(".3gp"))
				{//DO NOTTHING
					
				}
				else
					cList.add(f);
			}	
		}
		return cList;
	}
	
	/**
	 * Function To return All the Apk Files Present In Phone
	 * @param file
	 * @return
	 */
	public static ArrayList<File> getApkFiles(File file){
		if(file.listFiles() !=null){
			for(File f:file.listFiles())
			{	String na = f.getName();
				if(f.isDirectory())
					getApkFiles(f);
				else if(na.endsWith(".apk") || na.endsWith(".APK"))
					aList.add(f);	
		}}
		return aList;
	}
	/**
	 * Function To return All the Audio Files Present In Phone
	 * @param file
	 * @return
	 */
	public static ArrayList<File> getAudioFiles(File file){
		if(file.listFiles() !=null){
			for(File f:file.listFiles()){
				String na = f.getName();
				if(f.isDirectory())
					getAudioFiles(f);
				else if(na.endsWith(".mp3") || na.endsWith(".MP3") || na.endsWith(".amr") || na.endsWith(".AMR")
						|| na.endsWith(".ogg") || na.endsWith(".OGG"))
					sList.add(f);
			}
		}
		return sList;
	}
	
	/**
	 * Function To return All the Document Files Present In Phone
	 * @param file
	 * @return
	 */
	public static ArrayList<File> getDocumentFiles(File file){
		if(file.listFiles() !=null){
			for(File f:file.listFiles()){
				String na = f.getName();
				if(f.isDirectory())
					getDocumentFiles(f);
				else if(na.endsWith(".pdf") || na.endsWith(".PDF") || na.endsWith(".doc") || na.endsWith(".DOC")
						|| na.endsWith(".DOCX") || na.endsWith(".docx") || na.endsWith(".ppt") ||
						na.endsWith(".txt")||na.endsWith(".TXT")||na.endsWith(".PPT"))
					dList.add(f);
			}	
		}
		return dList;
	}
	
	/**
	 * Function To return All the Document Files Present In Phone
	 * @param file
	 * @return
	 */
	public static ArrayList<File> getImageFiles(File file){
		if(file.listFiles() !=null){
			for(File f:file.listFiles()){
				String na = f.getName();
				if(f.isDirectory())
					getImageFiles(f);
				else if(na.endsWith(".png") || na.endsWith(".PNG") || na.endsWith(".jpg")
						|| na.endsWith(".JPG") || na.endsWith(".gif") || na.endsWith(".GIF")
						|| na.endsWith(".JPEG") || na.endsWith(".jpeg") || na.endsWith(".bmp") || na.endsWith(".BMP"))
					iList.add(f);
			}	
		}
		return iList;
	}
	
	/**
	 * Function To return All the Document Files Present In Phone
	 * @param file
	 * @return
	 */
	public static ArrayList<File> getVideoFiles(File file){
		if(file.listFiles() !=null){
			for(File f:file.listFiles()){
				String na = f.getName();
				if(f.isDirectory())
					getVideoFiles(f);
				else if(na.endsWith(".mp4") || na.endsWith(".MP4") || na.endsWith(".avi") || na.endsWith(".AVI")
						|| na.endsWith(".FLV") || na.endsWith(".flv") || na.endsWith(".3GP") || na.endsWith(".3gp"))
					vList.add(f);
			}	
		}
		return vList;
	}
	

}
