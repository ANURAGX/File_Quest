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

package org.anurag.file.quest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This class holds the record of files folders that are locked or added to favorite....
 * 
 * @author Anurag
 *
 */

public class ItemDB extends SQLiteOpenHelper{
	
	static String DBNAME = "ItemDB.db";
	

	public ItemDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super(ctx,DBNAME,null,1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE ITEMS "+
				   "(FILEPATH TEXT PRIMARY KEY);");
		
		db.execSQL("CREATE TABLE FAVITEMS "+
				   "(FILEPATH TEXT PRIMARY KEY);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase DB, int arg1, int arg2) {
		// TODO Auto-generated method stub
		DB.execSQL("DROP TABLE IF EXISTS ITEMS;");
		DB.execSQL("DROP TABLE IF EXISTS FAVITEMS;");
		onCreate(DB);
	}
	
	/**
	 * method to add the item in locked db...
	 * @param PATH
	 * @param locked
	 */
	public void insertNodeToLock(String PATH){
		/**
		 * performing raw query to ensure that we are not making any 
		 * duplicate record of the items.. 
		 */		
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM ITEMS WHERE FILEPATH = ?",
				new String[]{PATH});
		if(cursor.getCount()==0){
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("FILEPATH", PATH);
			//values.put("FAV", fav);
			db.insert("ITEMS", null , values);
		}		
		cursor.close();
	}
	
	/**
	 * method to add the item in fav db...
	 * @param PATH
	 * @param locked
	 */
	public void insertNodeToFav(String PATH){
		/**
		 * performing raw query to ensure that we are not making any 
		 * duplicate record of the items.. 
		 */		
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM FAVITEMS WHERE FILEPATH = ?",
				new String[]{PATH});
		if(cursor.getCount()==0){
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("FILEPATH", PATH);
			db.insert("FAVITEMS", null , values);
			db.close();
		}		
		cursor.close();
	}
	
	/**
	 * method to delete the provided locked item from the db...
	 * @param Path
	 * @return
	 */
	public boolean deleteLockedNode(String Path){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("ITEMS", "FILEPATH = ?", new String[]{Path})>0;
	}
	
	/**
	 * method to delete all locked items from entries...
	 * @return
	 */
	public boolean deleteAllLockedNodes(){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("ITEMS", null , null) > 0;
	}
	
	/**
	 * method to know about the locked status of item...
	 * @param PATH
	 * @return
	 */
	public boolean isLocked(String PATH){
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM ITEMS WHERE FILEPATH = ?",
				new String[]{PATH});
		int count = cursor.getCount();
		cursor.close();
		if(count==0)
			return false;
		return true;
	}
	
	/**
	 * method to delete the all fav items entry from db...
	 * @param Path
	 * @return
	 */
	public boolean deleteAllFavItem(String Path){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("FAVITEMS", null,null) > 0;
	}
	
	/**
	 * method to delete the fav items entry from db...
	 * @param Path
	 * @return
	 */
	public boolean deleteFavItem(String Path){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("FAVITEMS", "FILEPATH = ?",new String[]{Path}) > 0;
	}
	
	/**
	 * method to know about the favorite status of the item...
	 * @param PATH
	 * @return
	 */
	public boolean isFavItem(String PATH){
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM FAVITEMS WHERE FILEPATH = ?",
				new String[]{PATH});
		int count = cursor.getCount();
		cursor.close();
		if(count == 0)
			return false;
		return true;
	}
}
