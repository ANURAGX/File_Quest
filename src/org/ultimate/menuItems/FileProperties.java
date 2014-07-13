
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
package org.ultimate.menuItems;

import java.io.File;
import java.sql.Date;
import org.anurag.file.quest.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
		vi.setBackgroundDrawable(mContext.getResources().getDrawable((R.drawable.ic_launcher_stats)));
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
			if(file.canRead()){
				Date mod = new Date(file.lastModified());
				pro.setText("    Modified On : " + mod);
				String availSize;
				String totalSize = null;
				StatFs env = new StatFs(Environment.getExternalStorageDirectory().getPath());
				// TOTAL PHONE STORAGE IN MB
				long total = new File("/sdcard/").getTotalSpace();
				if(total>=1024*1024*1024)
					totalSize = String.format("%.2f GB", (double)total/(1024*1024*1024));
				else if(total>=1024*1024)
					totalSize = String.format("%.2f MB", (double)total/(1024*1024));
				
				// AVAILABLE STORAGE ON PHONE
				long avail = new File("/sdcard").getFreeSpace();
				if(avail>=1024*1024*1024)
					availSize = String.format("%.2f GB", (double)avail/(1024*1024*1024));
				else if(avail>=1024*1024)
					availSize = String.format("%.2f MB", (double)avail/(1024*1024));
				else
					availSize = String.format("%.2f KB", (double)avail/(1024));
				
				pack.setText("    "+availSize+" Free/"+totalSize + " Total");
				
				if(file.isDirectory()){
					dev.setText("Folder");
					copy.setText("    Folder Name : " + file.getName());
					name.setText("    Folder Path : " + file.getAbsolutePath());
					if(file.getName().startsWith("."))
						ver.setText("    Hidden Type");
					else 
						ver.setText("    Non Hidden Type");
					si.setText("Folder Size");
					getFileSize(file);
					if(size >= 1024*1024*1024)
						siLen.setText(String.format("    Folder Size : %.2f GB", (double)size/(1024*1024*1024)));
					else if(size>=1024*1024)
						siLen.setText(String.format("    Folder Size : %.2f MB", (double)size/(1024*1024)));
					else if(size>=1024)
						siLen.setText(String.format("    Folder Size : %.2f KB", (double)size/(1024)));
					else
						siLen.setText("    Folder Size : " + size + " Byte");
					
				}else if(file.isFile()){
					dev.setText("File");
					copy.setText("    File Name : " + file.getName());
					name.setText("    File Path : " + file.getAbsolutePath());
					getFileType(file);
					si.setText("File Size");
					getFileSize(file);
					if(size >= 1024*1024*1024)
						siLen.setText(String.format("    File Size : %.2f GB", (double)size/(1024*1024*1024)));
					else if(size>=1024*1024)
						siLen.setText(String.format("    File Size : %.2f MB", (double)size/(1024*1024)));
					else if(size>=1024)
						siLen.setText(String.format("    File Size : %.2f KB", (double)size/(1024)));
					else
						siLen.setText("    File Size : " + size + " Byte");
				}
				
				
			}else{
				
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
			ver.setText("    Zip File");
		else if(f.getName().endsWith(".tar") || f.getName().endsWith(".TAR") || f.getName().endsWith(".rar") 
				|| f.getName().endsWith("RAR") || f.getName().endsWith(".7z") || f.getName().endsWith(".7Z"))
			ver.setText("    Compressed File");
		else if(f.getName().endsWith(".apk") || f.getName().endsWith(".APK"))
			ver.setText("    Apk File");
		else if(f.getName().endsWith(".mp3") || f.getName().endsWith(".MP3") || f.getName().endsWith(".amr") || f.getName().endsWith(".AMR")
				|| f.getName().endsWith(".ogg") || f.getName().endsWith(".OGG")||f.getName().endsWith(".m4a")||f.getName().endsWith(".M4A"))
			ver.setText("    Audio File");
		else if(f.getName().endsWith(".doc") || f.getName().endsWith(".DOC")
				|| f.getName().endsWith(".DOCX") || f.getName().endsWith(".docx") || f.getName().endsWith(".ppt") || f.getName().endsWith(".PPT"))
			ver.setText("    Document File");
		else if( f.getName().endsWith("jpg")||f.getName().endsWith(".JPG")||  f.getName().endsWith(".png") || f.getName().endsWith(".PNG") || f.getName().endsWith(".gif") || f.getName().endsWith(".GIF")
				|| f.getName().endsWith(".JPEG") || f.getName().endsWith(".jpeg") || f.getName().endsWith(".bmp") || f.getName().endsWith(".BMP"))
			ver.setText("    Image File");
		else if(f.getName().endsWith(".mp4") || f.getName().endsWith(".MP4") || f.getName().endsWith(".avi") || f.getName().endsWith(".AVI")
				|| f.getName().endsWith(".FLV") || f.getName().endsWith(".flv") || f.getName().endsWith(".3GP") || f.getName().endsWith(".3gp"))
			ver.setText("    Video File");	
		else if(f.getName().endsWith(".txt") || f.getName().endsWith(".TXT"))
			ver.setText("    Text File");
		else if(f.getName().endsWith(".pdf") || f.getName().endsWith(".PDF"))
			ver.setText("    Pdf File");
		else
			ver.setText("    Unknown"); 
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
