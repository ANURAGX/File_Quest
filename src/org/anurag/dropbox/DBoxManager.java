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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.dropbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.FileQuest;
import org.anurag.file.quest.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;


/**
 * this is manager class for listing the files from user's dropbox
 * account from the provided path....
 * after loading it notifies the main activity class
 * to list them in list view....
 * @author Anurag
 *
 */
public class DBoxManager {

	public static ArrayList<DBoxObj> dListRoot;
	public static ArrayList<DBoxObj> dListSimple;
	public static ArrayList<DBoxObj> dSearch;
	public static boolean DBOX_ROOT = false;
	public static boolean DBOX_SD = false;
    public static String rootPath = "/";
    public static String simplePath = "/";
    public static DBoxObj sd;
    public static DBoxObj root;
    
    /**
     * 
     * @return
     */
    public static ArrayList<DBoxObj> generateListForRoot(Context ctx){
    	DropboxAPI<?> api = DBoxAuth.mApi_1;
    	try {
    		com.dropbox.client2.DropboxAPI.Entry list = api.metadata(rootPath, 1000, "", true, null);
			if(list.isDir){
				ArrayList<DBoxObj> mainList = new ArrayList<DBoxObj>();
				List<com.dropbox.client2.DropboxAPI.Entry> entries = list.contents;
				for(Entry ent:entries)
					mainList.add(new DBoxObj(ent,ctx));
				return mainList;
			}
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<DBoxObj>();
		}
    	return new ArrayList<DBoxObj>();
    }
    
    /**
     * 
     * @return
     */
    public static ArrayList<DBoxObj> generateListForSimple(Context ctx){
    	DropboxAPI<?> api = DBoxAuth.mApi_1;
    	try {
    		com.dropbox.client2.DropboxAPI.Entry list = api.metadata(simplePath, 1000, "", true, null);
			if(list.isDir){
				ArrayList<DBoxObj> mainList = new ArrayList<DBoxObj>();
				List<com.dropbox.client2.DropboxAPI.Entry> entries = list.contents;
				for(Entry ent:entries)
					mainList.add(new DBoxObj(ent,ctx));
				return mainList;
			}
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<DBoxObj>();
		}
    	return new ArrayList<DBoxObj>();
    }
    
    public static void setDropBoxAdapter(final int ITEM ,final Context ctx){
    	final Dialog dialog = new Dialog(ctx , Constants.DIALOG_STYLE);
    	dialog.setCancelable(false);
    	dialog.setContentView(R.layout.p_dialog);
    	dialog.getWindow().getAttributes().width = FileQuest.size.x*8/9;
    	WebView prog = (WebView)dialog.findViewById(R.id.p_Web_View);
    	prog.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		prog.setEnabled(false);
    	final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				
					case 0:
							try{
								dialog.show();
							}catch(Exception e){
								
							}
							break;
							
					case 1:
							try{
								ctx.sendBroadcast(new Intent("FQ_DROPBOX_OPEN_FOLDER"));
								dialog.dismiss();								
							}catch(Exception e){
								
							}
							break;
 				}
			}
    		
    	};
    	
    	Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				//sending initial message to open loading dialog...
				handle.sendEmptyMessage(0);
				if(ITEM==2)
					dListSimple = generateListForSimple(ctx);
				else if(ITEM==1)
					dListRoot = generateListForRoot(ctx);
				sort(ITEM);
				handle.sendEmptyMessage(1);
					
			}
		});
    	thread.start();
    }    
    
    /**
	 * SORTING THE FILES AS PER ALPHABETICAL ORDER...
	 * FIRST FOLDER AND THEN FILES.....
	 */
	private static void sort(int item){
		Comparator<DBoxObj> comp = new Comparator<DBoxObj>() {
			@Override
			public int compare(DBoxObj a, DBoxObj b) {
				// TODO Auto-generated method stub
				boolean aisfolder =a.isDir();
				boolean bisfolder = b.isDir();
				if(aisfolder==bisfolder)
					return a.getName().compareToIgnoreCase(b.getName());
				else if(bisfolder)
					return 1;
				return -1;
			}
		};		
		if(item==2)
			Collections.sort(dListSimple, comp);
		else
			Collections.sort(dListRoot, comp);
	}    
}
