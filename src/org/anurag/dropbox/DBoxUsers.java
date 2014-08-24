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

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author Anurag
 *
 */
public class DBoxUsers{

	/**
	 * THIS FUNCTION IS TO GET TOTAL NUMBER OF DROPBOX USER WHO WERE SUCCESSFULLY AUTHENTICATED
	 * TO THE CURRENT DEVICE...
	 * @param ctx
	 * @return
	 */
	public static int getTotalUsers(Context ctx){
		SharedPreferences prefs = ctx.getSharedPreferences("DROPBOX_PREFS", 0);
		return prefs.getInt("TOTAL_USERS", 0);
	}
	
	/**
	 * this function saves the details of dropbox user provided key and secret key.
	 * this avoids repeated authentication of user by password and username... 
	 * 
	 * @param key
	 * @param secret
	 * @param ctx
	 */
	public static void saveUser(String key , String secret , Context ctx){
		SharedPreferences prefs = ctx.getSharedPreferences("DROPBOX_PREFS", 0);
		int total = prefs.getInt("TOTAL_USERS", 0);
		String CURRENT_USER = "USER"+(total+1);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(CURRENT_USER+"_KEY", key);
		edit.putString(CURRENT_USER+"_SECRET", secret);
		edit.putInt("TOTAL_USERS", (total+1));
		edit.commit();
	}
	
}
