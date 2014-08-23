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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Anurag
 *
 */
public class DBoxUsersDB extends SQLiteOpenHelper{

	
	String CREATE = "CREATE TABLE DROPBOX_USER (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "USER_NAME TEXT,KEY_NAME TEXT,SECRET_NAME TEXT)";
	
	public DBoxUsersDB(Context ctx){
		super(ctx,"DROP_BOX_DB",null,3);
	}
	
	public DBoxUsersDB(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @param acctname
	 * @param key
	 * @param secret
	 */
	public void addUser(String acctname,String key,String secret){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("USER_NAME", acctname);
		values.put("KEY_NAME", key);
		values.put("SECRET_KEY", secret);
		db.insert("DROPBOX_USER", null , values);
		db.close();
	}
	
}
