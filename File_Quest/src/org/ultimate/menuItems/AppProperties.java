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
 *                             anuraxsharma1512@gmail.com
 *
 */

package org.ultimate.menuItems;

import java.io.File;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AppProperties {
    private PackageInfo info;
    private ImageView image;
    private TextView infoName;
    Context mContext;
    Dialog dialog;
    String packageName;
    
    public AppProperties(Context context,int width,String data) {
		// TODO Auto-generated constructor stub
    	mContext = context;
    	packageName = data;
    	dialog = new Dialog(mContext, Constants.DIALOG_STYLE);
    	dialog.setContentView(R.layout.info_layout);
    	dialog.getWindow().getAttributes().width = width;
    	onCreate();
    }
    
    
	protected void onCreate() {
		// TODO Auto-generated method stub
		
		//params.width = p.x*5/6;
		
		
		try {
			info = mContext.getPackageManager().getPackageInfo(packageName , 0);
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		image = (ImageView) dialog.findViewById(R.id.infoIcon);
		infoName = (TextView)dialog.findViewById(R.id.infoName);
		
		
		
		PackageManager pack = mContext.getPackageManager();
		image.setImageDrawable(pack.getApplicationIcon(info.applicationInfo));
		infoName.setText(pack.getApplicationLabel(info.applicationInfo));
		
		infoName = (TextView)dialog.findViewById(R.id.developer);
		infoName.setText(mContext.getString(R.string.applicationname));
    
		infoName = (TextView)dialog.findViewById(R.id.copyright);
		infoName.setVisibility(View.GONE);
		
		infoName = (TextView)dialog.findViewById(R.id.name);
		infoName.setText("    " + pack.getApplicationLabel(info.applicationInfo));
		
		infoName = (TextView)dialog.findViewById(R.id.versionCode);
		try {
			PackageInfo pi = pack.getPackageInfo(info.packageName, 0);
			infoName.setText("    "+pi.versionName);
		} catch (NameNotFoundException e) {
			infoName.setText(mContext.getString(R.string.noappversion));
			e.printStackTrace();
		}
		infoName = (TextView)dialog.findViewById(R.id.size);
		infoName.setText(mContext.getString(R.string.appsize));
		
		infoName = (TextView)dialog.findViewById(R.id.sizeLenth);
		File f = new File(info.applicationInfo.sourceDir);
		long len = f.length();
		if(len>Constants.GB){
			len = len/(Constants.GB);
			infoName.setText(String.format("    "+mContext.getString(R.string.sizegb), (double)len));
		}else if(len>Constants.MB){
			len = len/(Constants.MB);
			infoName.setText(String.format("    "+mContext.getString(R.string.sizemb), (double)len));
		}else if(len>1024){
			len = len/(1024);
			infoName.setText(String.format("    "+mContext.getString(R.string.sizekb),(double)len));
		}else{
			infoName.setText(String.format("    "+mContext.getString(R.string.sizebytes),(double)len));
		}
		
		infoName = (TextView)dialog.findViewById(R.id.packageT);
		infoName.setText(mContext.getString(R.string.apppackage));
		
		infoName = (TextView)dialog.findViewById(R.id.pName);
		infoName.setText("    " + info.packageName);
		
		infoName = (TextView)dialog.findViewById(R.id.process);
		infoName.setText(mContext.getString(R.string.appprocess));
		
		infoName = (TextView)dialog.findViewById(R.id.proName);
		infoName.setText("    " + info.applicationInfo.processName);
		
		dialog.show();
	}
	

}
