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

package org.ultimate.menuItems;

import java.io.File;
import java.sql.Date;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

public class FileProperties{
	private File file;
	private long size;
	private TextView dev;
	private TextView info;
	private TextView name;
	private TextView copy;
	private TextView ver;
	private TextView si;
	private TextView siLen;
	private TextView pack;
	private TextView pro;
	private ImageView vi;
	Context mContext;
	Dialog dialog;
	
	
	/**
	 * TODO
	 * FIX THE FILE SIZE WHEN LOADING PROTECTED FILES.... 
	 */
	
	
	/**
	 * 
	 * @param context
	 * @param width
	 * @param f
	 */
	
	public FileProperties(Context context,int width,File f) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dialog = new Dialog(mContext, R.style.custom_dialog_theme);
		dialog.setContentView(R.layout.info_layout);
		dialog.getWindow().getAttributes().width = width;
		file = f;
		onCreate();
	}
	
	void onCreate() {
		// TODO Auto-generated method stub
		
		size = 0;
		
		dev = (TextView)dialog.findViewById(R.id.developer);
		copy = (TextView)dialog.findViewById(R.id.copyright);
		info = (TextView)dialog.findViewById(R.id.infoName);
		name = (TextView)dialog.findViewById(R.id.name);
		vi = (ImageView)dialog.findViewById(R.id.infoIcon);
		vi.setImageDrawable(mContext.getResources().getDrawable((R.drawable.ic_launcher_stats)));
		info.setText(R.string.properties);
		ver = (TextView)dialog.findViewById(R.id.version);
		ver.setText(R.string.type);
		ver = (TextView)dialog.findViewById(R.id.versionCode);
		si = (TextView)dialog.findViewById(R.id.size);
		siLen = (TextView)dialog.findViewById(R.id.sizeLenth); 
		pro = (TextView)dialog.findViewById(R.id.process);
		pro.setText(R.string.modified);
		pro = (TextView)dialog.findViewById(R.id.proName);
		pack = (TextView)dialog.findViewById(R.id.packageT);
		pack.setText(R.string.internalstorage);
		pack = (TextView)dialog.findViewById(R.id.pName);
		
		
		try{
			{
				Date mod = new Date(file.lastModified());
				pro.setText("    "+mContext.getString(R.string.modon)+" " + mod);
				String availSize;
				String totalSize = null;
				// TOTAL PHONE STORAGE IN MB
				long total = new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getTotalSpace();
				if(total>=Constants.GB)
					totalSize = String.format(mContext.getString(R.string.sizegb), (double)total/(Constants.GB));
				else if(total>=Constants.MB)
					totalSize = String.format(mContext.getString(R.string.sizemb), (double)total/(Constants.MB));
				
				// AVAILABLE STORAGE ON PHONE
				long avail = new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getFreeSpace();
				if(avail>=Constants.GB)
					availSize = String.format(mContext.getString(R.string.sizegb), (double)avail/(Constants.GB));
				else if(avail>=Constants.MB)
					availSize = String.format(mContext.getString(R.string.sizemb), (double)avail/(Constants.MB));
				else
					availSize = String.format(mContext.getString(R.string.sizekb), (double)avail/(1024));
				
				pack.setText("     "+availSize+ mContext.getString(R.string.free)+"/"+totalSize + " "+mContext.getString(R.string.total));
				
				if(file.isDirectory()){
					dev.setText(mContext.getString(R.string.folder));
					copy.setText("    "+mContext.getString(R.string.foldername)+" " + file.getName());
					name.setText("    "+mContext.getString(R.string.folderpath)+" " + file.getAbsolutePath());
					if(file.getName().startsWith("."))
						ver.setText("    "+mContext.getString(R.string.hiddentype));
					else 
						ver.setText("    "+mContext.getString(R.string.nonhiddentype));
					si.setText(mContext.getString(R.string.foldersize));
					if(file.canRead())
						getFileSize(file);
					else
						try{
							size = RootTools.getSpace(file.getAbsolutePath());
						}catch(Exception e){
							size = 0;
						}						
					if(size >= Constants.GB)
						siLen.setText(String.format("    "+mContext.getString(R.string.foldersizegb), (double)size/(Constants.GB)));
					else if(size>=Constants.MB)
						siLen.setText(String.format("    "+mContext.getString(R.string.foldersizemb), (double)size/(Constants.MB)));
					else if(size>=1024)
						siLen.setText(String.format("    "+mContext.getString(R.string.foldersizekb), (double)size/(1024)));
					else
						siLen.setText(String.format("    "+mContext.getString(R.string.foldersizebytes), (double)size/(1)));
					
				}else if(file.isFile()){
					dev.setText(mContext.getString(R.string.file));
					copy.setText("    "+mContext.getString(R.string.filename)+" " + file.getName());
					name.setText("    "+mContext.getString(R.string.filepath)+" " + file.getAbsolutePath());
					getFileType(file);
					si.setText(mContext.getString(R.string.filesize));
					if(file.canRead())
						getFileSize(file);
					else //INVOKING ROOT ACCESS TO FIND PROVIDED FILE SIZE....
						try{
							size = RootTools.getSpace(file.getAbsolutePath());
						}catch(Exception e){
							size = 0;
						}
					if(size >= Constants.GB)
						siLen.setText(String.format("    "+mContext.getString(R.string.filesizegb), (double)size/(Constants.GB)));
					else if(size>=Constants.MB)
						siLen.setText(String.format("    "+mContext.getString(R.string.filesizemb), (double)size/(Constants.MB)));
					else if(size>=1024)
						siLen.setText(String.format("    "+mContext.getString(R.string.filesizekb), (double)size/(1024)));
					else
						siLen.setText(String.format("    "+mContext.getString(R.string.filesizebytes), (double)size/(1)));
				}
				
				
			}
			dialog.show();
		}catch(RuntimeException e){
			Toast.makeText(mContext, R.string.failedToRetrieve, Toast.LENGTH_SHORT).show();
		}
		
	}
	/**
	 * 
	 * @param f
	 * @return
	 */
	public void getFileType(File f){
		if(f.getName().endsWith(".zip") || f.getName().endsWith(".ZIP"))
			ver.setText("    "+mContext.getString(R.string.zip));
		else if(f.getName().endsWith(".tar") || f.getName().endsWith(".TAR") || f.getName().endsWith(".rar") 
				|| f.getName().endsWith("RAR") || f.getName().endsWith(".7z") || f.getName().endsWith(".7Z"))
			ver.setText("    "+mContext.getString(R.string.compr));
		else if(f.getName().endsWith(".apk") || f.getName().endsWith(".APK"))
			ver.setText("    "+mContext.getString(R.string.application));
		else if(f.getName().endsWith(".mp3") || f.getName().endsWith(".MP3") || f.getName().endsWith(".amr") || f.getName().endsWith(".AMR")
				|| f.getName().endsWith(".ogg") || f.getName().endsWith(".OGG")||f.getName().endsWith(".m4a")||f.getName().endsWith(".M4A"))
			ver.setText("    "+mContext.getString(R.string.music));
		else if(f.getName().endsWith(".doc") || f.getName().endsWith(".DOC")
				|| f.getName().endsWith(".DOCX") || f.getName().endsWith(".docx") || f.getName().endsWith(".ppt") || f.getName().endsWith(".PPT"))
			ver.setText("    "+mContext.getString(R.string.document));
		else if( f.getName().endsWith("jpg")||f.getName().endsWith(".JPG")||  f.getName().endsWith(".png") || f.getName().endsWith(".PNG") || f.getName().endsWith(".gif") || f.getName().endsWith(".GIF")
				|| f.getName().endsWith(".JPEG") || f.getName().endsWith(".jpeg") || f.getName().endsWith(".bmp") || f.getName().endsWith(".BMP"))
			ver.setText("    "+mContext.getString(R.string.image));
		else if(f.getName().endsWith(".mp4") || f.getName().endsWith(".MP4") || f.getName().endsWith(".avi") || f.getName().endsWith(".AVI")
				|| f.getName().endsWith(".FLV") || f.getName().endsWith(".flv") || f.getName().endsWith(".3GP") || f.getName().endsWith(".3gp"))
			ver.setText("    "+mContext.getString(R.string.vids));	
		else if(f.getName().endsWith(".txt") || f.getName().endsWith(".TXT"))
			ver.setText("    "+mContext.getString(R.string.text));
		else if(f.getName().endsWith(".pdf") || f.getName().endsWith(".PDF"))
			ver.setText("    "+mContext.getString(R.string.pdf));
		else
			ver.setText("    "+mContext.getString(R.string.unknown)); 
	}
		/**
		 * 
		 * @param file
		 */
		public void getFileSize(File file){
			if(file.isFile())
				size = file.length();
			else if(file.isDirectory() && file.listFiles().length !=0){
				File[] a = file.listFiles();
				for(int i = 0 ; i<a.length ; ++i){
					if(a[i].isFile())
						size = size + a[i].length();
					else
						getFileSize(a[i]);
				}
			}
		}
	
}
