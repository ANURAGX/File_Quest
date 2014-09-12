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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Anurag
 *
 */
public class DBoxUsers extends SQLiteOpenHelper{

	private static String DBNAME = "DBOXUSER.db";
	
	public DBoxUsers(Context ctx) {
		// TODO Auto-generated constructor stub
		super(ctx,DBNAME , null , 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE DBOXUSERS "
				+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "USERNAME TEXT,"
				+ "KEY TEXT,"
				+ "SECRET TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS DBOXUSERS");
		onCreate(db);
	}
	
	/**
	 * THIS FUNCTION IS TO GET TOTAL NUMBER OF DROPBOX USER WHO WERE SUCCESSFULLY AUTHENTICATED
	 * TO THE CURRENT DEVICE...
	 * @return
	 */
	public int getTotalUsers(){
		Cursor curs = this.getReadableDatabase().rawQuery("SELECT * FROM DBOXUSERS", null);
		return curs.getCount();
	}
	
	/**
	 * this function saves the details of dropbox user provided key and secret key.
	 * this avoids repeated authentication of user by password and username... 
	 * 
	 * @param key
	 * @param secret
	 * @param userName
	 */
	public void saveUser(String key , String secret , String userName){
		Log.d("In save User", "In save User");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("KEY", key);
		values.put("SECRET", secret);
		values.put("USERNAME", userName);
		db.insert("DBOXUSERS", null, values);
		db.close();
	}
	
}
