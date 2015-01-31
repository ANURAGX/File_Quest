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

package org.anurag.file.quest;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;


/**
 * This class holds the record of files folders that are locked or added to favorite....
 * 
 * @author Anurag
 *
 */

@SuppressLint("SdCardPath")
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
				   "(FILEPATH TEXT PRIMARY KEY , DUP INTEGER);");
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
			
			//ORIGIONAL ITEM....
			values.put("FILEPATH", PATH);
			db.insert("ITEMS", null , values);
			
		//	if(Constants.isExtAvailable)
				if(!PATH.startsWith(Constants.EXT_PATH)){
					//DUPLICATE ITEMS....
					//THESE ITEMS ARE SAME
					//BUT THEY HAVE DIFFERENT PATH TO SAME ITEM....
					//LIKE WE CAN REACH TO SDCARD FROM /MNT/SDCARD OR /SDCARD OR /EMULATED PATHS....
					//SO ADDING THEM ALSO TO THE DB....
					
					String emulatedPath ="";
					String legacyPath = "";
					String mntPath;
					String sdPath;
					String sdcard0;
					String sd;
					String sdcard;
					String basePath = null;			
					
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
						//emulated or legacy paths are available....
						if(PATH.startsWith(Constants.EMULATED_PATH)){
							basePath = PATH.substring(Constants.EMULATED_PATH.length(), PATH.length()); 
						}else if(PATH.startsWith(Constants.LEGACY_PATH)){
							basePath = PATH.substring(Constants.LEGACY_PATH.length(), PATH.length());
						}
					}
					
					//emulated path is not available....
					if(basePath == null){
						if(PATH.startsWith("/sdcard")){
							String str = "/sdcard";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/mnt/sdcard")){
							String str = "/mnt/sdcard";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/storage/sdcard0")){
							String str = "/storage/sdcard0";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/storage/sdcard")){
							String str = "/storage/sdcard";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/storage/sd")){
							String str = "/storage/sd";
							basePath = PATH.substring(str.length(), PATH.length());
						} 
					}
					
					//pushing the item into db....
					if(basePath != null){
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
							emulatedPath = Constants.EMULATED_PATH + basePath;
							legacyPath = Constants.LEGACY_PATH + basePath;
							if(!TextUtils.equals(PATH, emulatedPath))	
								if(new File(emulatedPath).exists()){
									values.put("FILEPATH", emulatedPath);
									db.insert("ITEMS", null , values);
								}
							
							if(!TextUtils.equals(PATH, legacyPath))
								if(new File(legacyPath).exists()){
									values.put("FILEPATH", legacyPath);
									db.insert("ITEMS", null , values);
								}
						}	
						
						mntPath = "/mnt/sdcard" + basePath;
						sdPath = "/sdcard" + basePath;
						sdcard0 = "/storage/sdcard0" + basePath;
						sd = "/storage/sd" + basePath;
						sdcard = "/storage/sdcard" + basePath;
						
						if(!TextUtils.equals(PATH, mntPath))
							if(new File(mntPath).exists()){
								values.put("FILEPATH", mntPath);
								db.insert("ITEMS", null , values);
							}
						
						
						if(!TextUtils.equals(PATH, sdPath))
							if(new File(sdPath).exists()){
								values.put("FILEPATH", sdPath);
								db.insert("ITEMS", null , values);
							}
						
						if(!TextUtils.equals(PATH, sdcard0))
							if(new File(sdcard0).exists()){
								values.put("FILEPATH", sdcard0);
								db.insert("ITEMS", null , values);
							}
												
						if(!TextUtils.equals(PATH, sd))
							if(new File(sd).exists()){
								values.put("FILEPATH", sd);
								db.insert("ITEMS", null , values);
							}
						
						if(!TextUtils.equals(PATH, sdcard))
							if(new File(sdcard).exists()){
								values.put("FILEPATH", sdcard);
								db.insert("ITEMS", null , values);
							}
					}
				}		
		}		
		cursor.close();
	}
	
	/**
	 *
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
			
			//ORIGIONAL FILE....
			values.put("FILEPATH", PATH);
			values.put("DUP", 0);
			db.insert("FAVITEMS", null , values);
			
			//if(Constants.isExtAvailable)
				if(!PATH.startsWith(Constants.EXT_PATH)){
					//DUPLICATE ITEMS....
					//THESE ITEMS ARE SAME
					//BUT THEY HAVE DIFFERENT PATH TO SAME ITEM....
					//LIKE WE CAN REACH TO SDCARD FROM /MNT/SDCARD OR /SDCARD OR /EMULATED PATHS....
					//SO ADDING THEM ALSO TO THE DB....
					
					String emulatedPath ="";
					String legacyPath = "";
					String mntPath;
					String sdPath;
					String sdcard0;
					String sd;
					String sdcard;
					String basePath = null;			
					
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
						//emulated or legacy paths are available....
						if(PATH.startsWith(Constants.EMULATED_PATH)){
							basePath = PATH.substring(Constants.EMULATED_PATH.length(), PATH.length()); 
						}else if(PATH.startsWith(Constants.LEGACY_PATH)){
							basePath = PATH.substring(Constants.LEGACY_PATH.length(), PATH.length());
						}
					}
					
					//emulated path is not available....
					if(basePath == null){
						if(PATH.startsWith("/sdcard")){
							String str = "/sdcard";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/mnt/sdcard")){
							String str = "/mnt/sdcard";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/storage/sdcard0")){
							String str = "/storage/sdcard0";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/storage/sdcard")){
							String str = "/storage/sdcard";
							basePath = PATH.substring(str.length(), PATH.length());
						}else if(PATH.startsWith("/storage/sd")){
							String str = "/storage/sd";
							basePath = PATH.substring(str.length(), PATH.length());
						} 
					}
					
					//pushing the item into db....
					if(basePath != null){
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
							emulatedPath = Constants.EMULATED_PATH + basePath;
							legacyPath = Constants.LEGACY_PATH + basePath;
							if(!TextUtils.equals(PATH, emulatedPath))
								if(new File(emulatedPath).exists()){
									values.put("FILEPATH", emulatedPath);
									values.put("DUP", 1);
									db.insert("FAVITEMS", null , values);
								}
							
							if(!TextUtils.equals(PATH, legacyPath))
								if(new File(legacyPath).exists()){
									values.put("FILEPATH", legacyPath);
									values.put("DUP", 1);
									db.insert("FAVITEMS", null , values);
							}
						}	
						
						mntPath = "/mnt/sdcard" + basePath;
						sdPath = "/sdcard" + basePath;
						sdcard0 = "/storage/sdcard0" + basePath;
						sd = "/storage/sd" + basePath;
						sdcard = "/storage/sdcard" + basePath;
						
						if(!TextUtils.equals(PATH, mntPath))
							if(new File(mntPath).exists()){
								values.put("FILEPATH", mntPath);
								values.put("DUP", 1);
								db.insert("FAVITEMS", null , values);
							}
						
						
						if(!TextUtils.equals(PATH, sdPath))
							if(new File(sdPath).exists()){
								values.put("FILEPATH", sdPath);
								values.put("DUP", 1);
								db.insert("FAVITEMS", null , values);
							}
						
						
						if(!TextUtils.equals(PATH, sdcard0))
							if(new File(sdcard0).exists()){
								values.put("FILEPATH", sdcard0);
								values.put("DUP", 1);
								db.insert("FAVITEMS", null , values);
							}
												
						if(!TextUtils.equals(PATH,sd))
							if(new File(sd).exists()){
								values.put("FILEPATH", sd);
								values.put("DUP", 1);
								db.insert("FAVITEMS", null , values);
							}
												
						if(!TextUtils.equals(PATH, sdcard))
							if(new File(sdcard).exists()){
								values.put("FILEPATH", sdcard);
								values.put("DUP", 1);
								db.insert("FAVITEMS", null , values);
							}
					}
				}			
			db.close();
		}		
		cursor.close();
	}
	
	/**
	 * method to delete the provided locked item from the db...
	 * @param Path
	 * @return
	 */
	public boolean deleteLockedNode(String PATH){
		SQLiteDatabase db = this.getWritableDatabase();
		
		if(!PATH.startsWith(Constants.EXT_PATH)){
			//DUPLICATE ITEMS....
			//THESE ITEMS ARE SAME
			//BUT THEY HAVE DIFFERENT PATH TO SAME ITEM....
			//LIKE WE CAN REACH TO SDCARD FROM /MNT/SDCARD OR /SDCARD OR /EMULATED PATHS....
			//SO ADDING THEM ALSO TO THE DB....
			
			String emulatedPath ="";
			String legacyPath = "";
			String mntPath;
			String sdPath;
			String sdcard0;
			String sd;
			String sdcard;
			String basePath = null;			
			
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
				//emulated or legacy paths are available....
				if(PATH.startsWith(Constants.EMULATED_PATH)){
					basePath = PATH.substring(Constants.EMULATED_PATH.length(), PATH.length()); 
				}else if(PATH.startsWith(Constants.LEGACY_PATH)){
					basePath = PATH.substring(Constants.LEGACY_PATH.length(), PATH.length());
				}
			}
			
			//emulated path is not available....
			if(basePath == null){
				if(PATH.startsWith("/sdcard")){
					String str = "/sdcard";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/mnt/sdcard")){
					String str = "/mnt/sdcard";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/storage/sdcard0")){
					String str = "/storage/sdcard0";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/storage/sdcard")){
					String str = "/storage/sdcard";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/storage/sd")){
					String str = "/storage/sd";
					basePath = PATH.substring(str.length(), PATH.length());
				} 
			}
			
			//pushing the item into db....
			if(basePath != null){
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
					emulatedPath = Constants.EMULATED_PATH + basePath;
					legacyPath = Constants.LEGACY_PATH + basePath;
					db.delete("ITEMS", "FILEPATH = ?",new String[]{emulatedPath});
					db.delete("ITEMS", "FILEPATH = ?",new String[]{legacyPath});
				}					
				mntPath = "/mnt/sdcard" + basePath;
				sdPath = "/sdcard" + basePath;
				sdcard0 = "/storage/sdcard0" + basePath;
				sd = "/storage/sd" + basePath;
				sdcard = "/storage/sdcard" + basePath;
				db.delete("ITEMS", "FILEPATH = ?",new String[]{mntPath});
				db.delete("ITEMS", "FILEPATH = ?",new String[]{sd});
				db.delete("ITEMS", "FILEPATH = ?",new String[]{sdcard});
				db.delete("ITEMS", "FILEPATH = ?",new String[]{sdcard0});
				db.delete("ITEMS", "FILEPATH = ?",new String[]{sdPath});
			}
		}
		db.delete("FAVITEMS", "FILEPATH = ?",new String[]{PATH});
		return true;
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
	public boolean deleteAllFavItem(){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("FAVITEMS", null,null) > 0;
	}
	
	/**
	 * method to delete the fav items entry from db...
	 * @param Path
	 * @return
	 */
	public boolean deleteFavItem(String PATH){
		SQLiteDatabase db = this.getWritableDatabase();
		
		if(!PATH.startsWith(Constants.EXT_PATH)){
			//DUPLICATE ITEMS....
			//THESE ITEMS ARE SAME
			//BUT THEY HAVE DIFFERENT PATH TO SAME ITEM....
			//LIKE WE CAN REACH TO SDCARD FROM /MNT/SDCARD OR /SDCARD OR /EMULATED PATHS....
			//SO ADDING THEM ALSO TO THE DB....
			
			String emulatedPath ="";
			String legacyPath = "";
			String mntPath;
			String sdPath;
			String sdcard0;
			String sd;
			String sdcard;
			String basePath = null;			
			
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
				//emulated or legacy paths are available....
				if(PATH.startsWith(Constants.EMULATED_PATH)){
					basePath = PATH.substring(Constants.EMULATED_PATH.length(), PATH.length()); 
				}else if(PATH.startsWith(Constants.LEGACY_PATH)){
					basePath = PATH.substring(Constants.LEGACY_PATH.length(), PATH.length());
				}
			}
			
			//emulated path is not available....
			if(basePath == null){
				if(PATH.startsWith("/sdcard")){
					String str = "/sdcard";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/mnt/sdcard")){
					String str = "/mnt/sdcard";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/storage/sdcard0")){
					String str = "/storage/sdcard0";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/storage/sdcard")){
					String str = "/storage/sdcard";
					basePath = PATH.substring(str.length(), PATH.length());
				}else if(PATH.startsWith("/storage/sd")){
					String str = "/storage/sd";
					basePath = PATH.substring(str.length(), PATH.length());
				} 
			}
			
			//pushing the item into db....
			if(basePath != null){
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
					emulatedPath = Constants.EMULATED_PATH + basePath;
					legacyPath = Constants.LEGACY_PATH + basePath;
					db.delete("FAVITEMS", "FILEPATH = ?",new String[]{emulatedPath});
					db.delete("FAVITEMS", "FILEPATH = ?",new String[]{legacyPath});
				}					
				mntPath = "/mnt/sdcard" + basePath;
				sdPath = "/sdcard" + basePath;
				sdcard0 = "/storage/sdcard0" + basePath;
				sd = "/storage/sd" + basePath;
				sdcard = "/storage/sdcard" + basePath;
				db.delete("FAVITEMS", "FILEPATH = ?",new String[]{mntPath});
				db.delete("FAVITEMS", "FILEPATH = ?",new String[]{sd});
				db.delete("FAVITEMS", "FILEPATH = ?",new String[]{sdcard});
				db.delete("FAVITEMS", "FILEPATH = ?",new String[]{sdcard0});
				db.delete("FAVITEMS", "FILEPATH = ?",new String[]{sdPath});
			}
		}		
		db.delete("FAVITEMS", "FILEPATH = ?",new String[]{PATH});
		return true;
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
