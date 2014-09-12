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

import org.anurag.file.quest.Constants;

import android.content.Context;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;


public class DBoxAuth {
	
	public static DropboxAPI<AndroidAuthSession> mApi;
	public static boolean AUTH = false;
	
	/**
	 * 
	 * @param ctx
	 */
	public static void DoAuth(Context ctx){
		if(DropBoxConstant.appKey.startsWith("YOUR_KEY")||DropBoxConstant.appSecretKey.startsWith("YOUR_KEY")){
			Toast.makeText(ctx, "Provide your own app key in DropboxConstant file", Toast.LENGTH_SHORT).show();
			return;
		}
		AUTH = true;
		AndroidAuthSession session = buildAuthSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		mApi.getSession().startOAuth2Authentication(ctx);
	}
	
	static private AndroidAuthSession buildAuthSession(){
		AppKeyPair pair = new AppKeyPair(DropBoxConstant.appKey, DropBoxConstant.appSecretKey);
		AndroidAuthSession session = new AndroidAuthSession(pair);		
		return session;
	}
	
	public static void storeAuth(String session ,Context ctx){
		//String auth2Token = session.getOAuth2AccessToken();
		if(session!=null){
			String name;
			try {
				name = mApi.accountInfo().displayName;
			} catch (DropboxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				name = "USER";
			}
			Constants.dboxDB.saveUser("oauth2:", session , name);
		}
	}
}
