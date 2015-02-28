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

import java.util.zip.ZipEntry;


import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;


/**
 * THIS CLASS SHOWS THE PROPERTIES OF A FILE SELECTED FROM INSIDE OF AN ARCHIVE......
 * 
 */
public class RarFileProperties {
	
	public RarFileProperties(Context ctx , RarObj file , int width) {
		// TODO Auto-generated constructor stub
		Dialog dialog = new Dialog(ctx, Constants.DIALOG_STYLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.info_layout);
		dialog.getWindow().getAttributes().width = width;
		TextView text = (TextView)dialog.findViewById(R.id.infoName);
		text.setText(ctx.getString(R.string.properties));
		
		
		text = (TextView)dialog.findViewById(R.id.developer);
		if(!file.isFile()){
			text.setText("Folder");
			text = (TextView)dialog.findViewById(R.id.copyright);
			text.setText("   Folder Name :-"+file.getFileName());
			text = (TextView)dialog.findViewById(R.id.name);
			String path = file.getPath();
			if(!path.startsWith("/"))
				path="/"+path;
			path = path.substring(0, path.lastIndexOf("/"));
			if(path.length()==0)
				path = "/";
			text.setText("   Folder Path :-"+path);
			
			text = (TextView)dialog.findViewById(R.id.size);
			text.setText("Folder Size");
			
			text = (TextView)dialog.findViewById(R.id.sizeLenth);
			text.setText("   ?");
		}	
		else{
			text.setText(ctx.getString(R.string.file));
			text = (TextView)dialog.findViewById(R.id.copyright);
			text.setText("   "+ctx.getString(R.string.filename)+" "+file.getFileName());
			text = (TextView)dialog.findViewById(R.id.name);
			String path = file.getPath();
			if(!path.startsWith("/"))
				path="/"+path;
			path = path.substring(0, path.lastIndexOf("/"));
			if(path.length()==0)
				path = "/";
			text.setText("   "+ctx.getString(R.string.filepath)+" "+path);
			text = (TextView)dialog.findViewById(R.id.size);
			text.setText(ctx.getString(R.string.filesize));
			
			text = (TextView)dialog.findViewById(R.id.sizeLenth);
			text.setText("   "+file.getSize());
		}
		
		
		text = (TextView)dialog.findViewById(R.id.version);
		text.setText(ctx.getString(R.string.type));
		text = (TextView)dialog.findViewById(R.id.versionCode );
		text.setText("   "+file.getFileType());
		
		
		text = (TextView)dialog.findViewById(R.id.packageT);
		text.setText(ctx.getString(R.string.compmethod));
		
		text = (TextView)dialog.findViewById(R.id.pName);
		int method = 0;//file.getFileHeader().ge
		if(method == ZipEntry.DEFLATED)
			text.setText("   "+ctx.getString(R.string.deflated));
		else
			text.setText("   "+ctx.getString(R.string.stored));
		
		text = (TextView)dialog.findViewById(R.id.process);
		text.setText(ctx.getString(R.string.modified));
		
		text = (TextView)dialog.findViewById(R.id.proName);
		text.setText("   "+ctx.getString(R.string.modon)+ " "+ (file.getFileHeader().getArcTime()));
		
		dialog.show();
	}

}
