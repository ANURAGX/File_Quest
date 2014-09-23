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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipFile;
import org.anurag.compress.ArchiveEntryProperties;
import org.anurag.compress.CreateZip;
import org.anurag.compress.CreateZipApps;
import org.anurag.compress.ExtractTarFile;
import org.anurag.compress.ExtractZipFile;
import org.anurag.compress.RarAdapter;
import org.anurag.compress.RarFileProperties;
import org.anurag.compress.RarManager;
import org.anurag.compress.RarObj;
import org.anurag.compress.SZipAdapter;
import org.anurag.compress.SZipManager;
import org.anurag.compress.TarAdapter;
import org.anurag.compress.TarFileProperties;
import org.anurag.compress.TarManager;
import org.anurag.compress.TarObj;
import org.anurag.compress.ZipAdapter;
import org.anurag.compress.ZipManager;
import org.anurag.compress.ZipObj;
import org.anurag.dropbox.DBoxAdapter;
import org.anurag.dropbox.DBoxAuth;
import org.anurag.dropbox.DBoxManager;
import org.anurag.dropbox.DBoxUsers;
import org.anurag.gesture.AddGesture;
import org.anurag.gesture.G_Open;
import org.anurag.settings.Settings;
import org.ultimate.menuItems.AppProperties;
import org.ultimate.menuItems.BluetoothChooser;
import org.ultimate.menuItems.DeleteBackups;
import org.ultimate.menuItems.DeleteFlashable;
import org.ultimate.menuItems.FileProperties;
import org.ultimate.menuItems.GetHomeDirectory;
import org.ultimate.menuItems.GetMoveLocation;
import org.ultimate.menuItems.MultiSendApps;
import org.ultimate.menuItems.MultipleCopyDialog;
import org.ultimate.menuItems.SelectApp;
import org.ultimate.menuItems.SelectedApp;
import org.ultimate.quickaction3D.ActionItem;
import org.ultimate.quickaction3D.QuickAction;
import org.ultimate.quickaction3D.QuickAction.OnActionItemClickListener;
import org.ultimate.root.LinuxShell;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.abhi.animated.TransitionViewPager;
import com.abhi.animated.TransitionViewPager.TransitionEffect;
import com.astuetz.PagerSlidingTabStrip;
import com.dropbox.client2.android.AndroidAuthSession;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.rey.slidelayout.SlideLayout;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.twotoasters.jazzylistview.JazzyHelper;


/*
 *	TODO LIST...
 *
 * 	1. DELETE THE ENTRY FROM THE DB WHEN THE LOCKED ITEM IS 
 *     RENAMED AND UPDATE THE DB WITH THE RENAMED ITEM...
 *     
 *     
 */
/**
 * 
 * @author ANURAG
 *
 */


@SuppressLint({ "HandlerLeak", "SdCardPath" })
public class FileQuest extends FragmentActivity implements OnClickListener, QuickAction.OnActionItemClickListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private static SectionsPagerAdapter mSectionsPagerAdapter;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	
	/**
	 * ZIP RELATED VARIABLES....
	 */
	private static String zipPathRoot;
	private static String zipPathSD;
	public static boolean ZIP_SD;	
	public static boolean ZIP_ROOT;
	private static ArrayList<ZipObj> zListRoot;
	private static ArrayList<ZipObj> zListSD;	
	private static ZipObj zFileSD;
	private static ZipObj zFileSimple;
	private static ArrayList<ZipObj> zSearch;
	
	
	/**
	 * RAR RELATED VARIABLES....
	 */
	public static boolean RAR_ROOT;
	public static boolean RAR_SD;
	private static ArrayList<RarObj> rListSD;
	private static String rarPathSD;
	private static String rarPathRoot;
	private static ArrayList<RarObj> rListRoot;
	private static RarObj rarFileSD;
	private static RarObj rFileRoot;
	private static ArrayList<RarObj> rSearch;
	
	/**
	 *TAR RELATED VARIABLES.... 
	 */
	private static ArrayList<TarObj> tListSD;
	public static boolean TAR_SD;
	private static String tarPathSD;
	public static boolean TAR_ROOT;
	private static ArrayList<TarObj> tListRoot;
	private static String tarPathRoot;
	private static TarObj tFileSD;
	private static TarObj tFileRoot;
	private static ArrayList<TarObj> tSearch;
	
	
	/**
	 * 
	 */
	private static SDManager sdManager;
	private static ArrayList<Item> sdItemsList;
	private static RootManager rootManager;
	private static ArrayList<Item> rootItemList;
	private static RootAdapter rootAdapter;
	private static SDAdapter sdAdapter;
	private static ArrayList<Item> tempList;
	
	
	static int fPos;
	private BroadcastReceiver RECEIVER;
	private static Dialog dialog;
	public static Point size;
	public static Context mContext;
	public static int LIST_ANIMATION;
	public static int PAGER_ANIMATION;
	private static ListView root;
	private static ListView simple;
	private static ListView LIST_VIEW_3D;
	private static LinearLayout FILE_GALLEY;
	static ArrayList<Item> COPY_FILES;
	private static boolean MULTIPLE_COPY;
	private static boolean MULTIPLE_CUT;
	private static boolean MULTIPLE_COPY_GALLERY;
	private static boolean MULTIPLE_CUT_GALLERY;
	private ProgressBar sd;
	private TextView avail, total;
	private long av, to;
	private String sav, sto;
	private static boolean MULTI_SELECT_APPS;
	public static boolean ENABLE_ON_LAUNCH;
	private static String INTERNAL_PATH_ONE;
	private static String INTERNAL_PATH_TWO;
	private static String PATH;
	private static int SHOW_APP;
	private static String HOME_DIRECTORY;
	public static int SORT_TYPE;
	public static boolean SHOW_HIDDEN_FOLDERS;
	public static int CURRENT_PREF_ITEM;
	public static float TRANSPRA_LEVEL;
	public static SharedPreferences.Editor edit;
	public static SharedPreferences preferences;
	private static boolean SEARCH_FLAG = false;
	private static int CREATE_FLAG = 0;
	private static boolean CREATE_FILE = false;
	private static String CREATE_FLAG_PATH;
	private WindowManager.LayoutParams params;

	private static ArrayList<Item> searchList;
	private static boolean RENAME_COMMAND = false;
	private static EditText editBox;
	private static TransitionViewPager mViewPager;
	private static ViewFlipper mFlipperBottom;
	private static PagerSlidingTabStrip indicator;
	
	private static Intent LAUNCH_INTENT;
	private static boolean COPY_COMMAND = false;
	private static boolean CUT_COMMAND = false;
	private static int CURRENT_ITEM = 0;
	/**
	 * Media Panel Variables
	 */
	private static FileGalleryAdapter element;
	private static ArrayList<Item> mediaFileList;
	private static boolean elementInFocus = false;
	private static int pos = 0;
	private static AppManager nManager;

	private static ArrayList<ApplicationInfo> nList;
	private static ApplicationInfo info;
	private static AppAdapter nAppAdapter;
	private static ListView APP_LIST_VIEW;
	private static Item file;
	private static Item file2;
	private static Item file0;
	private static boolean mUseBackKey = false;
	private static ViewFlipper mVFlipper;
	private static int LAST_PAGE;
	static boolean error;
	static SlideLayout slidemenu;
	private static Utils loadFileGallery;
	private static View v;
	@SuppressWarnings({ "deprecation" })
	@SuppressLint({ "NewApi"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PATH = INTERNAL_PATH_ONE = INTERNAL_PATH_TWO = Environment.getExternalStorageDirectory().getAbsolutePath();
		preferences = getSharedPreferences("MY_APP_SETTINGS", 0);
		INTERNAL_PATH_ONE = preferences.getString("INTERNAL_PATH_ONE", PATH);
		INTERNAL_PATH_TWO = preferences.getString("INTERNAL_PATH_TWO", PATH);
		SHOW_APP = preferences.getInt("SHOW_APP", 1);
		CURRENT_ITEM = CURRENT_PREF_ITEM = preferences.getInt("CURRENT_PREF_ITEM", 0);
		TRANSPRA_LEVEL = preferences.getFloat("TRANSPRA_LEVEL", 0.9f);
		SHOW_HIDDEN_FOLDERS = preferences.getBoolean("SHOW_HIDDEN_FOLDERS",false);
		SORT_TYPE = preferences.getInt("SORT_TYPE", 2);
		Constants.FOLDER_ICON = preferences.getInt("FOLDER_TYPE", 5);
		HOME_DIRECTORY = preferences.getString("HOME_DIRECTORY", null);
		ENABLE_ON_LAUNCH = preferences.getBoolean("ENABLE_ON_LAUNCH", false);
		LIST_ANIMATION = preferences.getInt("LIST_ANIMATION", 0);
		PAGER_ANIMATION = preferences.getInt("PAGER_ANIMATION", 0);
		edit = preferences.edit();

		try {
			new File("/sdcard/File Quest/").mkdir();
		} catch (Exception e) {

		}
		
		mContext = FileQuest.this;
		Constants.db = new ItemDB(mContext);
		Constants.dboxDB = new DBoxUsers(mContext);		
		
		sdManager = new SDManager(FileQuest.this);
		rootManager = new RootManager(FileQuest.this);
		
		if (ENABLE_ON_LAUNCH) {
			if (new File(INTERNAL_PATH_TWO).exists()) {
				if (PATH != INTERNAL_PATH_TWO)
					SDManager.nStack.push(INTERNAL_PATH_TWO);
			} else {
				edit.putString("INTERNAL_PATH_TWO", PATH);
				edit.commit();
				showToast(getResources().getString(R.string.startupfoldernotfound));
			}
			if (new File(INTERNAL_PATH_ONE).exists()) {
				if (PATH != INTERNAL_PATH_ONE)
					RootManager.nStack.push(INTERNAL_PATH_ONE);
			} else {
				edit.putString("INTERNAL_PATH_ONE", PATH);
				edit.commit();
				showToast(getResources().getString(R.string.startupfoldernotfound));
			}
		}		
		sdItemsList = sdManager.getList();
		rootItemList = rootManager.getList();
		sdAdapter = new SDAdapter(mContext, sdItemsList);
		rootAdapter = new RootAdapter(mContext, rootItemList);
		
		/**
		 * THIS THREAD CATCHES THE UNCAUGHT EXCEPTIONS...
		 * IF APP CRASHES IT RESTARTS THE APP ......
		 */
		
		/**Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				Log.e("Alert","Lets See if it Works !!!" +"paramThread:::" +arg0 +"paramThrowable:::" +arg1);
				startActivity(new Intent(FileQuest.this, FileQuest.class));
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});		
		*/
		
		zipPathSD = null;
		zipPathRoot = null;
		
		TAR_SD = RAR_ROOT = RAR_SD = false;
	
		ZIP_ROOT = ZIP_SD = SEARCH_FLAG = RENAME_COMMAND = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = CREATE_FILE = false;
		fPos = 0;
		params = this.getWindow().getAttributes();
		size = new Point();
		try{
			getWindowManager().getDefaultDisplay().getSize(size);
		}catch(NoSuchMethodError e){
			//OVERCOMING FROM EXCEPTION OCCURING IN ANDROID 2.3.5(API 10)
			size.x = getWindowManager().getDefaultDisplay().getWidth();
			size.y = getWindowManager().getDefaultDisplay().getHeight();
		}
		

		setContentView(R.layout.new_ui);
	//	loadFileGallery = new Utils(null, mContext);
		//loadFileGallery.setDetails();
		

		editBox = (EditText) findViewById(R.id.editBox);

		sd = (ProgressBar) findViewById(R.id.space_indicator);
		avail = (TextView) findViewById(R.id.avail);
		total = (TextView) findViewById(R.id.total);

		error = false;
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (TransitionViewPager) findViewById(R.id.pager);
		indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		String[] te = getResources().getStringArray(R.array.effects);		
		mViewPager.setTransitionEffect(TransitionEffect.valueOf(te[PAGER_ANIMATION]));
		
		indicator.setViewPager(mViewPager);
		mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
		mVFlipper = (ViewFlipper) findViewById(R.id.viewFlipperMenu);
		mFlipperBottom = (ViewFlipper) findViewById(R.id.viewFlipperMenuBottom);
		
		if (CURRENT_PREF_ITEM != 3)
			LAST_PAGE = 2;
		else
			LAST_PAGE = 3;

		if (CURRENT_PREF_ITEM == 0) {
			Button b = (Button) findViewById(R.id.change);
			TextView t = (TextView) findViewById(R.id.addText);
			b.setBackgroundResource(R.drawable.ic_launcher_select_app);
			t.setText(R.string.apps);
			mFlipperBottom.showNext();
			new load().execute();
			LAST_PAGE = 0;
		}
		else if (CURRENT_PREF_ITEM == 3) {
			mVFlipper.setAnimation(nextAnim());
			mVFlipper.showNext();
			mFlipperBottom.showPrevious();
			mFlipperBottom.setAnimation(prevAnim());
		}
		nManager = new AppManager(mContext);
		nManager.SHOW_APP = SHOW_APP;
		nList = nManager.giveMeAppList();
		nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
		
		file = new Item(new File("/"), null, null, null);
		file2 = new Item(new File(PATH),null,null,null);
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int page) {
				// TODO Auto-generated method stub
				mUseBackKey = false;
				CURRENT_ITEM = page;
				Button b = (Button) findViewById(R.id.change);
				TextView t = (TextView) findViewById(R.id.addText);
				if (page == 0){
					if (!elementInFocus) {
						mFlipperBottom.showNext();
						mFlipperBottom.setAnimation(nextAnim());
					}
					b.setBackgroundResource(R.drawable.ic_launcher_select_app);
					t.setText(getString(R.string.apps));
					new load().execute();
					LAST_PAGE = 0;
				} else if (page != 0) {
					b.setBackgroundResource(R.drawable.ic_launcher_add_new);
					t.setText(R.string.New);
				}
				if (page == 1 && LAST_PAGE == 0) {
					LAST_PAGE = 1;
					if (!elementInFocus) {
						mFlipperBottom.showPrevious();
						mFlipperBottom.setAnimation(prevAnim());
					}
				}

				if (page == 2 && LAST_PAGE == 0) {
					if(!elementInFocus){
						mFlipperBottom.showPrevious();
						mFlipperBottom.setAnimation(prevAnim());
					}					
					LAST_PAGE = 2;
				}

				if ((page == 2 || page == 1) && LAST_PAGE == 3) {
					LAST_PAGE = 2;
					mFlipperBottom.showNext();
					mFlipperBottom.setAnimation(nextAnim());
					mVFlipper.setAnimation(prevAnim());
					mVFlipper.showPrevious();
				}
				if (page == 3 && (LAST_PAGE == 2 || LAST_PAGE == 1)) {
					LAST_PAGE = 3;
					mFlipperBottom.showPrevious();
					mFlipperBottom.setAnimation(prevAnim());
					if (RENAME_COMMAND || SEARCH_FLAG || CREATE_FILE) {
						mVFlipper.setAnimation(prevAnim());
						mVFlipper.showPrevious();
						RENAME_COMMAND = SEARCH_FLAG = CREATE_FILE = false;
					} else {
						mVFlipper.setAnimation(nextAnim());
						mVFlipper.showNext();
					}
				}

				if (page == 3 && LAST_PAGE == 0) {
					LAST_PAGE = 3;
					if (elementInFocus) {
						mVFlipper.showNext();
						mVFlipper.setAnimation(nextAnim());
						mFlipperBottom.showPrevious();
						mFlipperBottom.setAnimation(prevAnim());
					}
				}

				if (RENAME_COMMAND || SEARCH_FLAG || CREATE_FILE) {
					RENAME_COMMAND = SEARCH_FLAG = CREATE_FILE = false;
					mVFlipper.showNext();
					mVFlipper.setAnimation(nextAnim());
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		//LOADS THE SD CARD STATUS...
		new load().execute();
		
		/**
		 * CHECKS WHETHER APP IS UPDATED OR NOT IF UPDATED THEN DISPLAYS THE NEW
		 * ADDED FEATURES
		 */
		if (!preferences.getString("APP_VERSION", "0.0.0").equalsIgnoreCase(
				getString(R.string.version))) {
			edit.putString("APP_VERSION", getString(R.string.version));
			edit.commit();
			new WhatsNew(mContext, size.x * 8 / 9, size.y * 8 / 9);
		}
        initLeftMenu();
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void onPostResume() {
		params = this.getWindow().getAttributes();
		params.alpha = TRANSPRA_LEVEL;
		super.onPostResume();
	}

	@Override
	protected void onResumeFragments() {
		params = this.getWindow().getAttributes();
		params.alpha = TRANSPRA_LEVEL;
		super.onResumeFragments();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(RECEIVER);
	}

	@Override
	protected void onStart() {
		params = this.getWindow().getAttributes();
		params.alpha = TRANSPRA_LEVEL;
		super.onStart();
		// REGISTER_RECEIVER();
	}

	@Override
	protected void onResume() {
		params = this.getWindow().getAttributes();
		params.alpha = 0.85f;
		super.onResume();
		REGISTER_RECEIVER();
		
		/**
		 * There was dropbox authentication request by user....
		 */
		if(DBoxAuth.AUTH){
			AndroidAuthSession session = DBoxAuth.mApi.getSession();
			if(session.authenticationSuccessful()){
				DBoxAuth.storeAuth(session.getOAuth2AccessToken(),mContext);
				session.finishAuthentication();
				Toast.makeText(mContext, "Authenticated", Toast.LENGTH_SHORT).show();
				if(CURRENT_ITEM==2)
					DBoxManager.DBOX_SD=true;
				else if(CURRENT_ITEM==1)
					DBoxManager.DBOX_ROOT = true;
				DBoxManager.setDropBoxAdapter(CURRENT_ITEM, mContext);
			}else
				Toast.makeText(mContext, "Failed to authenticate", Toast.LENGTH_SHORT).show();
			DBoxAuth.AUTH = false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//ShowMenu();
		if(!slidemenu.isLeftMenuOpen())
			slidemenu.openLeftMenu(true);
		return false;
	}

	/**
	 * THIS FUNCTION SETS THE ADPATER FOR ALL THE PANELS,AFTER CERTAIN
	 * OPERATIONS...
	 */
	private static void setAdapter(final int ITEM) {
		final Handler handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				
				case 3:
					CURRENT_ITEM=ITEM;
					resetPager();
					break;
				case 4:
					CURRENT_ITEM=ITEM;
					resetPager();
					APP_LIST_VIEW.setAdapter(nAppAdapter);
					APP_LIST_VIEW.setSelection(pos);
					break;
				case 5:
					load_FIle_Gallery(fPos);
					break;
				}
				super.handleMessage(msg);
			}
		};

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {// TRY BLOCK IS USED BECAUSE I NOTICED THAT WHEN NEW FOLDER
						// WITH HINDI LANGAUGE IS CREATED THROWS INDEXOUTOFBOUND
						// EXCEPTION
						// I THINK IT IS APPLICABLE TO OTHER LANGUAGES ALSO
					if(!ZIP_SD&&!RAR_SD&&!TAR_SD)
						sdItemsList = sdManager.getList();
					if(!ZIP_ROOT&&!RAR_ROOT&&!TAR_ROOT)
						rootItemList = rootManager.getList();
					//if (ITEM == 0)
						//load_FIle_Gallery(pos);
				} catch (IndexOutOfBoundsException e) {
					if(!ZIP_SD&&!RAR_SD&&!TAR_SD)
						sdItemsList = sdManager.getList();
					if(!ZIP_ROOT&&!RAR_ROOT&&!TAR_ROOT)
						rootItemList = rootManager.getList();
				}

				if (SDAdapter.MULTI_SELECT) {
					SDAdapter.thumbselection = new boolean[sdItemsList.size()];
					SDAdapter.MULTI_FILES = new ArrayList<Item>();
					SDAdapter.C = 0;
				}
				if (RootAdapter.MULTI_SELECT) {
					RootAdapter.thumbselection = new boolean[rootItemList.size()];
					RootAdapter.MULTI_FILES = new ArrayList<Item>();
					RootAdapter.C = 0;
				}
				mUseBackKey = false;
				handle.sendEmptyMessage(3);
				if (ITEM == 3) {
					if (MULTI_SELECT_APPS) {
						nAppAdapter = new AppAdapter(mContext,R.layout.row_list_1, nList);
						nAppAdapter.MULTI_SELECT = true;
						handle.sendEmptyMessage(4);
					} else if (!MULTI_SELECT_APPS) {
						nAppAdapter = new AppAdapter(mContext,R.layout.row_list_1, nList);
						nAppAdapter.MULTI_SELECT = false;
						handle.sendEmptyMessage(4);
					}
				} else if (ITEM == 0 && elementInFocus)
					handle.sendEmptyMessage(5);
			}
		});
		thread.start();
	}

	
	/**
	 * The above same method,bute here task is achieved via asynctask... 
	 * @param ITEM
	 */
	private static void setAdapter2(final int ITEM) {
		
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				CURRENT_ITEM = ITEM;
				if(CURRENT_ITEM != 0){
					resetPager();
				}else if(CURRENT_ITEM == 0)
					load_FIle_Gallery(fPos);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {// TRY BLOCK IS USED BECAUSE I NOTICED THAT WHEN NEW FOLDER
					// WITH HINDI LANGAUGE IS CREATED THROWS INDEXOUTOFBOUND
					// EXCEPTION
					// I THINK IT IS APPLICABLE TO OTHER LANGUAGES ALSO
					if(!ZIP_SD&&!RAR_SD&&!TAR_SD)
						sdItemsList = sdManager.getList();
					if(!ZIP_ROOT&&!RAR_ROOT&&!TAR_ROOT)
						rootItemList = rootManager.getList();
					//if (ITEM == 0)
						//load_FIle_Gallery(pos);
				}catch (IndexOutOfBoundsException e) {
					if(!ZIP_SD&&!RAR_SD&&!TAR_SD)
						sdItemsList = sdManager.getList();
					if(!ZIP_ROOT&&!RAR_ROOT&&!TAR_ROOT)
						rootItemList = rootManager.getList();
				}
				
				if(SDAdapter.MULTI_SELECT) {
					SDAdapter.thumbselection = new boolean[sdItemsList.size()];
					SDAdapter.MULTI_FILES = new ArrayList<Item>();
					SDAdapter.C = 0;
				}
				if(RootAdapter.MULTI_SELECT) {
					RootAdapter.thumbselection = new boolean[rootItemList.size()];
					RootAdapter.MULTI_FILES = new ArrayList<Item>();
					RootAdapter.C = 0;
				}
				mUseBackKey = false;
				if(ITEM == 3) {
					if (MULTI_SELECT_APPS) {
						nAppAdapter = new AppAdapter(mContext,R.layout.row_list_1, nList);
						nAppAdapter.MULTI_SELECT = true;
					} else if (!MULTI_SELECT_APPS) {
						nAppAdapter = new AppAdapter(mContext,R.layout.row_list_1, nList);
						nAppAdapter.MULTI_SELECT = false;
						
					}
				} 
				return null;
			}			
		}.execute();
	}
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				Fragment fragment0 = new MediaPanel();
				mViewPager.setObjectForPosition(fragment0, position);
				return fragment0;
			case 1:
				Fragment fragment = new RootPanel();
				mViewPager.setObjectForPosition(fragment, position);
				return fragment;
			case 2:
				Fragment fragment1 = new SDPanel();
				mViewPager.setObjectForPosition(fragment1, position);
				return fragment1;
			case 3:
				Fragment appFragment = new AppPanel();
				mViewPager.setObjectForPosition(appFragment, position);
				return appFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String curr;
			switch (position) {
			case 0:
				return getString(R.string.filegallery);
			case 1:
				if (RootManager.nStack.size() == 1)
					return "/";
				else if (RootManager.getCurrentDirectoryName().equals(
						"sdcard0"))
					return getString(R.string.sd);
				return RootManager.getCurrentDirectoryName();
			case 2:
				curr = SDManager.getCurrentDirectoryName();
				if (curr.equals("sdcard") || curr.equals("sdcard0")
						|| curr.equalsIgnoreCase("0")
						|| curr.equalsIgnoreCase(PATH))
					return getString(R.string.sd);
				return SDManager.getCurrentDirectoryName();
			case 3:
				return getString(R.string.appstore);
			}
			return null;
		}			
	}

	/**
	 * 
	 * @author anurag
	 *
	 */
	public static class MediaPanel extends Fragment {
		public MediaPanel(){}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if (LIST_VIEW_3D != null && mediaFileList != null) {
				if (elementInFocus) {
					LIST_VIEW_3D.setVisibility(View.VISIBLE);
					FILE_GALLEY.setVisibility(View.GONE);
					LIST_VIEW_3D.setAdapter(element);
				} else if (!elementInFocus) {
					LIST_VIEW_3D.setVisibility(View.GONE);
					FILE_GALLEY.setVisibility(View.VISIBLE);
				}
			}
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				final ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			// mContext = getActivity();
			inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.custom_list_view, container,false);
			LIST_VIEW_3D = (ListView) v.findViewById(R.id.customListView);
			LIST_VIEW_3D.setSelector(R.drawable.blue_button);
			FILE_GALLEY = (LinearLayout)v.findViewById(R.id.file_gallery_layout);

			JazzyHelper helper = new JazzyHelper(mContext, null);
			helper.setTransitionEffect(LIST_ANIMATION);
			LIST_VIEW_3D.setOnScrollListener(helper);
			
			LinearLayout music = (LinearLayout) v.findViewById(R.id.music);
			LinearLayout app = (LinearLayout) v.findViewById(R.id.apps);
			LinearLayout docs = (LinearLayout) v.findViewById(R.id.docs);
			LinearLayout photo = (LinearLayout) v.findViewById(R.id.photos);
			LinearLayout vids = (LinearLayout) v.findViewById(R.id.videos);
			LinearLayout zips = (LinearLayout) v.findViewById(R.id.zips);
			LinearLayout misc = (LinearLayout) v.findViewById(R.id.misc);
		
			loadFileGallery = new Utils(v, mContext);
			loadFileGallery.load();
			//loadFileGallery.setView(v);
			Constants.UPDATE_FILEGALLERY = true;
			//loadFileGallery.load();
			/*
			 * WHEN MUSIC BUTTON IS CLICKED
			 */
			music.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 0));
				}
			});

			app.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						load_FIle_Gallery((fPos = 1));
					} catch (InflateException e) {

					}
				}
			});

			docs.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 2));
				}
			});

			photo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 3));
				}
			});

			vids.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 4));
				}
			});

			zips.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 5));
				}
			});

			misc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 6));
				}
			});
		

			final Dialog d = new Dialog(mContext,R.style.custom_dialog_theme);
			d.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) d.findViewById(R.id.list);
			AdapterLoaders loaders = new AdapterLoaders(getActivity(), false,0);
			lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.blue_button));
			d.getWindow().getAttributes().width = size.x*8/9;
			LIST_VIEW_3D.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,View arg1, int position, long arg3) {
							if (elementInFocus) {
								if (SEARCH_FLAG)
									file0 = searchList.get(position);
								else
									file0 = mediaFileList.get(position);
								d.show();
								fPos = position;
							}
							return true;
						}
					});
			lo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
					// TODO Auto-generated method stub
					d.dismiss();
					switch (position) {
					case 0:
						// OPEN THE SELECTED FILE
						
						if(!file0.isLocked())//file is not locked...
							new OpenFileDialog(mContext, Uri.parse(file0.getPath()), size.x*8/9);
						else//file is locked....
							new MasterPassword(mContext, size.x*8/9, file0, preferences,Constants.MODES.OPEN);
						break;
					case 1:
						// CLOUD.....
						Toast.makeText(mContext, R.string.supporttakenBack,	Toast.LENGTH_SHORT).show();
						break;

					case 2:
						// COPY
						RENAME_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
						COPY_COMMAND = true;
						COPY_FILES = new ArrayList<Item>();
						COPY_FILES.add(file0);
						break;

					case 3:
						// CUT
						RENAME_COMMAND = COPY_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
						CUT_COMMAND = true;
						COPY_FILES = new ArrayList<Item>();
						COPY_FILES.add(file0);
						break;
					case 4:
						// PASTE
						Toast.makeText(mContext, R.string.pasteNotAllowed,Toast.LENGTH_SHORT).show();
						break;
					case 5:
						// ZIP
						if(!file0.isLocked()){//item is not locked....
							ArrayList<Item> temp = new ArrayList<Item>();
							temp.add(file0);
							new CreateZip(mContext, size.x * 8 / 9,temp);
						}else//item is locked....
							new MasterPassword(mContext, size.x*8/9, file0, preferences, Constants.MODES.ARCHIVE);
						break;
					case 6:
							//DELETE
							if(!file0.isLocked()){//item is not locked...
								ArrayList<Item> te = new ArrayList<Item>();
								te.add(file0);
								new DeleteFiles(mContext, size.x*8/9, te,null);
							}else//item is locked...
								new MasterPassword(mContext, size.x*8/9, file0, preferences, Constants.MODES.DELETE);
							break;
					case 7:
						// RENAME
						if(file0.isLocked())
							new MasterPassword(mContext, size.x*8/9, file0, preferences, Constants.MODES.RENAME);
						else{
							COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							RENAME_COMMAND = true;
							mVFlipper.showPrevious();
							mVFlipper.setAnimation(prevAnim());
							editBox.setText(file0.getName());
						}
						break;

					case 8:
						// SEND
						if(file0.isLocked())//item is locked....
							new MasterPassword(mContext, size.x*8/9, file0, preferences, Constants.MODES.SEND);
						else//item is not locked...
							new BluetoothChooser(mContext, file0.getPath(), size.x*8/9, null);
						break;
					case 9:
						new AddGesture(mContext, size.x, size.y*8/9, file0.getPath());
						break;
					case 10:
						// PROPERTIES
						new FileProperties(mContext, size.x*8/9, file0.getFile());
					}
				}
			});

			lo.setOnItemLongClickListener(null);
			LIST_VIEW_3D.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,final int position, long id) {
					if (elementInFocus) {
						if (SEARCH_FLAG)
							file0 = searchList.get(position);
						else
							file0 = mediaFileList.get(position);
						
						if(!file0.isLocked())//selected item is not locked...
							new OpenFileDialog(mContext, Uri.parse(file0.getPath()), size.x*8/9);
						else//item is locked...
							new MasterPassword(mContext, size.x*8/9, file0, preferences, Constants.MODES.OPEN);
					}
				}
			});
			return v;
		}
	}

	/**
	 * 
	 * @author Anurag
	 *
	 */
	public static class RootPanel extends BaseFragment {
		public RootPanel(){
			//PASSING THE ANIMATION TYPE FOR THE LIST VIEW.....
			super(LIST_ANIMATION);
		}
		int spos;
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// mContext = getActivity();
			simple = getListView();
			simple.setSelector(R.drawable.blue_button);
			ColorDrawable color = new ColorDrawable(android.R.color.black);
			simple.setDivider(color);
			if(ZIP_ROOT)
				setListAdapter(new ZipAdapter(zListRoot, mContext));
			else if(RAR_ROOT)
				setListAdapter(new RarAdapter(mContext, rListRoot));
			else if(TAR_ROOT)
				setListAdapter(new TarAdapter(mContext, tListRoot));
			else{
				setListAdapter(rootAdapter);
			}	
			simple.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						final int position, long id) {
					if (SEARCH_FLAG) {
						if(ZIP_ROOT)
							zFileSimple = zSearch.get(position);
						else if(RAR_ROOT)
							rFileRoot = rSearch.get(position);
						else if(TAR_ROOT)
							tFileRoot = tSearch.get(position);
						else
							file = searchList.get(position);
					} else {
						if(ZIP_ROOT)
							zFileSimple = zListRoot.get(position);
						else if(RAR_ROOT)
							rFileRoot = rListRoot.get(position);
						else if(TAR_ROOT)
							tFileRoot = tListRoot.get(position);
						else
							file = rootItemList.get(position);
					}
					if (CREATE_FILE || RENAME_COMMAND || SEARCH_FLAG) {
						mVFlipper.setAnimation(nextAnim());
						mVFlipper.showNext();
						CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = RENAME_COMMAND = false;
						;
					}

					/*
					 *ZIP FILE IS OPEN,HANDLE IT HERE... 
					 */
					if(ZIP_ROOT){
						if(zFileSimple.isFile()){
							//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
							new ExtractZipFile(mContext, zFileSimple, size.x*8/9 , null , file.getFile(),0);
						}else{
							//DIRECTORY HAS TO BE OPENED....
							zipPathRoot = zFileSimple.getPath();
							RootManager.nStack.push(zipPathRoot+" -> Zip");
							if(zipPathRoot.startsWith("/"))
								zipPathRoot = zipPathRoot.substring(1, zipPathRoot.length());
							setZipAdapter();
						}	
					}else if(RAR_ROOT){//handling rar file.....
						if(rFileRoot.isFile()){
							//EXTRACT TO CACHE DIR AND THEN OPEN IT....
							//new ExtractRarFile(mContext, rFileRoot, size.x*8/9, null, file.getFile(), 0);
							Toast.makeText(mContext, R.string.cantextractfromrar, Toast.LENGTH_SHORT).show();
						}else{
							rarPathRoot = rFileRoot.getPath();
							if(rarPathRoot.startsWith("\\"))
								rarPathRoot = rarPathRoot.substring(0,rarPathRoot.length());
							RootManager.nStack.push(rFileRoot.getFileName()+" -> Rar");
							setRarAdapter();
						}
						
					}else if(TAR_ROOT){//HANDLING TAR FILE....
						if(tFileRoot.isFile()){
							//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
							new ExtractTarFile(mContext, tFileRoot, size.x*8/9, null, file.getFile(), 0);	
						}else{
							tarPathRoot = tFileRoot.getPath();
							RootManager.nStack.push(tFileRoot.getName()+" -> Tar");
							setTarAdapter();
						}
					}else{
						//HANDLING ORDINARY FILE EXLORING....
						if(!file.isLocked()){
							//file is not locked,open it as usual....
							if (!file.isDirectory())
								new OpenFileDialog(mContext, Uri.parse(file.getPath()), size.x*8/9);
							else if (file.isDirectory()){
								RootManager.nStack.push(file.getPath());
								setAdapter(1);
							}
						}else
							new MasterPassword(mContext, size.x*8/9, file, preferences,Constants.MODES.OPEN);
					}					
				}
			});

			final Dialog d = new Dialog(mContext, R.style.custom_dialog_theme);
			d.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) d.findViewById(R.id.list);
			AdapterLoaders loaders = new AdapterLoaders(getActivity(), false , 1);
			lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.blue_button));
			d.getWindow().getAttributes().width = size.x*8/9;
			simple.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long id) {
					spos = position;
					if(SEARCH_FLAG){
						if(ZIP_ROOT)
							zFileSimple = zListRoot.get(position);
						else if(RAR_ROOT)
							rFileRoot = rSearch.get(position);
						else if(TAR_ROOT)
							tFileRoot = tSearch.get(position);
						else
							file = searchList.get(position);
					} else {
						if(ZIP_ROOT)
							zFileSimple = zListRoot.get(position);
						else if(RAR_ROOT)
							rFileRoot = rListRoot.get(position);
						else if(TAR_ROOT)
							tFileRoot = tListRoot.get(position);
						else
							file = rootItemList.get(position);
					}
					d.show();
					fPos = position;
					return true;
				}
			});

			lo.setOnItemLongClickListener(null);
			lo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					// TODO Auto-generated method stub
					d.dismiss();
					switch (position) {
					case 0:
						// OPEN
						if (CREATE_FILE || RENAME_COMMAND || SEARCH_FLAG) {
							mVFlipper.setAnimation(nextAnim());
							mVFlipper.showNext();
							CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = 
									MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = 
									false;
						}
						
						/*
						 *ZIP FILE IS OPEN,HANDLE IT HERE... 
						 */
						if(ZIP_ROOT){
							if(zFileSimple.isFile()){
								//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
								new ExtractZipFile(mContext, zFileSimple, size.x*8/9 , null , file.getFile(),0);
							}else{
								//DIRECTORY HAS TO BE OPENED....
								zipPathRoot = zFileSimple.getPath();
								RootManager.nStack.push(zipPathRoot+" -> Zip");
								if(zipPathRoot.startsWith("/"))
									zipPathRoot = zipPathRoot.substring(1, zipPathRoot.length());
								setZipAdapter();
							}	
						}else if(RAR_ROOT){//handling rar file.....
							if(rFileRoot.isFile()){
								//extract file to cache dir and the open it....
								//new ExtractRarFile(mContext, rFileRoot, size.x*8/9, null, file.getFile(), 0);
								Toast.makeText(mContext, R.string.cantextractfromrar, Toast.LENGTH_SHORT).show();
							}else{
								rarPathRoot = rFileRoot.getPath();
								if(rarPathRoot.startsWith("\\"))
									rarPathRoot = rarPathRoot.substring(0,rarPathRoot.length());
								RootManager.nStack.push(rFileRoot.getFileName()+" -> Rar");
								setRarAdapter();
							}
							
						}else if(TAR_ROOT){//HANDLING TAR FILE....
							if(tFileRoot.isFile()){
								//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
								new ExtractTarFile(mContext, tFileRoot, size.x*8/9, null, file.getFile(), 0);	
							}else{
								tarPathRoot = tFileRoot.getPath();
								RootManager.nStack.push(tFileRoot.getName()+" -> Tar");
								setTarAdapter();
							}
						}else{
							//ORDINARY FILE EXPLORING..
							//HANDLING ORDINARY FILE EXLORING....
							if(!file.isLocked()){
								//file is not locked,open it as usual....
								if (!file.isDirectory())
									new OpenFileDialog(mContext, Uri.parse(file.getPath()), size.x*8/9);
								else if (file.isDirectory()){
									RootManager.nStack.push(file.getPath());
									setAdapter(1);
								}
							}else
								new MasterPassword(mContext, size.x*8/9, file, preferences,Constants.MODES.OPEN);
						}
						break;

					case 1:
						// COPY TO CLOUD
						Toast.makeText(mContext, R.string.supporttakenBack,Toast.LENGTH_SHORT).show();
						break;

					case 2:
						// COPY
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
							mVFlipper.setAnimation(nextAnim());
						}

						if(ZIP_ROOT||RAR_ROOT||TAR_ROOT){
							//CURRENTLY WE ARE INSIDE OF ZIP ARCHIVE...
							//EXTRACT FILES TO USER SPECIFIED PATH....
							new GetHomeDirectory(mContext, size.x*8/9, null);
						}else{
							//ORDINARY FILE OPERATIONS....
							CREATE_FILE = RENAME_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							COPY_COMMAND = true;
							COPY_FILES = new ArrayList<Item>();
							COPY_FILES.add(file);
						}
						
						break;

					case 3:
						// CUT
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
							mVFlipper.setAnimation(nextAnim());
						}

						if(ZIP_ROOT){
							//WE ARE CURRENTLY INSIDE ZIP ARCHIVE...
							//FILES HAVE TO BE EXTRACTED IN THE CURRENT DIRECTORY
							if(new File(file.getParent()).canRead())
								new ExtractZipFile(mContext, zFileSimple, size.x*8/9, file.getParent(), file.getFile(), 1);
							else
								//CURRENT DIRECTORY IS UN-WRITABLE...OR WE DONT HAVE WRITE PERMISSION HERE...
								Toast.makeText(mContext, R.string.cannotexthere, Toast.LENGTH_SHORT).show();
						}else{
							//ORDINARY FILE OPERATIONS....
							CREATE_FILE = RENAME_COMMAND = COPY_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							CUT_COMMAND = true;
							COPY_FILES = new ArrayList<Item>();
							COPY_FILES.add(file);
						}						
						break;

					case 4:
						// PASTE
						if(ZIP_ROOT||RAR_ROOT||TAR_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else if(file.isDirectory()){
							if(!file.isLocked()){
								try{
									//checking whether the selected item is locked or not....
									int l = COPY_FILES.size();
									boolean flag = false;
									for(int i=0;i<l;++i){
										if(COPY_FILES.get(i)!=null)
											if(COPY_FILES.get(i).isLocked()){
												flag = true;
												break;
											}
									}
									if(flag)
										new MasterPassword(mContext, size.x*8/9, file, preferences,Constants.MODES.COPY);
									else
										pasteCommand(true);
								}catch(NullPointerException e){
									
								}
							}	
							else 
								new MasterPassword(mContext, size.x*8/9, file, preferences, Constants.MODES.PASTEINTO);
						}	
						break;
					case 5:
						// ZIP
						if(ZIP_ROOT||RAR_ROOT||TAR_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							if (!file.canRead())
								Toast.makeText(mContext, R.string.serviceUnavaila,Toast.LENGTH_SHORT).show();
							else if (file.canRead()) {
								if(!file.isLocked()){
									ArrayList<Item> temp = new ArrayList<Item>();
									temp.add(file);
									new CreateZip(mContext, size.x*8/9, temp);
								}else
									new MasterPassword(mContext, size.x*8/9, file, preferences, Constants.MODES.ARCHIVE);
							}
						}						
						break;

					case 6:
							//DELETE
							if(ZIP_ROOT||RAR_ROOT||TAR_ROOT)
								Toast.makeText(mContext, R.string.operationnotsupported,
										Toast.LENGTH_SHORT).show();
							else{
								if(!file.isLocked())
									deleteMethod(file);
								else new MasterPassword(mContext, size.x*8/9,file, 
										preferences, Constants.MODES.DELETE);
							}
							break;
					case 7:
						// RENAME
						if(ZIP_ROOT||RAR_ROOT||TAR_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							if(!file.isLocked()){
								COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
								RENAME_COMMAND = true;
								mVFlipper.showPrevious();
								mVFlipper.setAnimation(prevAnim());
								editBox.setText(file.getName());
								editBox.setSelected(true);
							}
							else
								new MasterPassword(mContext, size.x*8/9, file, preferences, Constants.MODES.RENAME);
						}	
						break;

					case 8:
						// SEND
						if(ZIP_ROOT){
							if(zFileSimple.isFile())
								new ExtractZipFile(mContext, zFileSimple, size.x*8/9, null, file.getFile(), 2);
							else
								Toast.makeText(mContext, R.string.cannotsendfolder, Toast.LENGTH_SHORT).show();
						}else if(RAR_ROOT){
							
						}else if(TAR_ROOT){
							if(tFileRoot.isFile())//the mode is 2 to share it via bluetooth....
								new ExtractTarFile(mContext, tFileRoot, size.x*8/9, null, file.getFile(), 2);
							else
								Toast.makeText(mContext, R.string.cannotsendfolder, Toast.LENGTH_SHORT).show();
						}
						else{
							if(!file.isDirectory()){
								if(!file.isLocked())
									new BluetoothChooser(mContext, file.getPath(), size.x*8/9, null);
								else 
									new MasterPassword(mContext, size.x*8/9, file, preferences, Constants.MODES.SEND);
							}	
							else
								showToast(getString(R.string.compressandsend));
						}
						
						break;

					case 9:
						//ADD GESTURE...
						if(ZIP_ROOT||RAR_ROOT||TAR_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else
							new AddGesture(mContext, size.x, size.y*8/9, file.getPath());
						break;
					case 10:
						// PROPERTIES
						if(ZIP_ROOT)//A ZIP FILE IS OPENED....
							new ArchiveEntryProperties(mContext, zFileSimple, size.x*8/9);
						else if(RAR_ROOT)
							new RarFileProperties(mContext, rFileRoot, size.x*8/9);
						else if(TAR_ROOT)
							new TarFileProperties(mContext, tFileRoot, size.x*8/9);
						else
							new FileProperties(getActivity(), size.x*8/9, file.getFile());
					}
				}
			});
		}
	}

	/**
	 * 
	 * @author Anurag
	 *
	 */
	public static class SDPanel extends BaseFragment {

		public SDPanel() {
			//PASSING THE ANIMATION TYPE FOR THE LIST VIEW.....
			super(LIST_ANIMATION);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			root = getListView();			
			root.setSelector(R.drawable.blue_button);
			ColorDrawable color = new ColorDrawable(android.R.color.black);
			
			root.setDivider(color);
			if(DBoxManager.DBOX_ROOT)
				setListAdapter(new DBoxAdapter(mContext, DBoxManager.dListRoot));
			else if(ZIP_SD){
				setListAdapter(new ZipAdapter(zListSD,mContext));
			}else if(RAR_SD)
				setListAdapter(new RarAdapter(mContext, rListSD));
			else if(TAR_SD)
				setListAdapter(new TarAdapter(mContext, tListSD));
			else{	
				//sdItemsList = sdManager.getList();
				setListAdapter(sdAdapter);
			}	
			dialog = new Dialog(getActivity(), R.style.custom_dialog_theme);
			dialog.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) dialog.findViewById(R.id.list);
			AdapterLoaders loaders = new AdapterLoaders(mContext, false , 2);
			lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.blue_button));
			dialog.getWindow().getAttributes().width = size.x*8/9;
			root.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					// TODO Auto-generated method stub
					if (SEARCH_FLAG){
						if(ZIP_SD)
							zFileSD = zSearch.get(arg2);
						else if(RAR_SD)
							rarFileSD = rSearch.get(arg2);
						else if(TAR_SD)
							tFileSD = tSearch.get(arg2);
						else
							file2 = searchList.get(arg2);
					} else {
						if(ZIP_SD)
							zFileSD = zListSD.get(arg2);
						else if(RAR_SD)
							rarFileSD = rListSD.get(arg2);
						else if(TAR_SD)
							tFileSD = tListSD.get(arg2);
						else
							file2 = sdItemsList.get(arg2);
					}
					dialog.show();
					fPos = arg2;
					return true;
				}
			});

			lo.setOnItemLongClickListener(null);
			lo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					switch (position) {
					case 0:
							if (CREATE_FILE || RENAME_COMMAND || SEARCH_FLAG) {
								mVFlipper.setAnimation(nextAnim());
								mVFlipper.showNext();
								CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = RENAME_COMMAND = false;
							}
						
							if(ZIP_SD){//ZIP FILE HANDLING...
								if(zFileSD.isFile()){
									//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
									new ExtractZipFile(mContext, zFileSD, size.x*8/9 , null , file2.getFile(),0);
								}else{
									//DIRECTORY HAS TO BE OPENED....
									zipPathSD = zFileSD.getPath();
									SDManager.nStack.push(zipPathSD+" -> Zip");
									if(zipPathSD.startsWith("/"))
										zipPathSD = zipPathSD.substring(1, zipPathSD.length());
									setZipAdapter();
								}	
							}else if(RAR_SD){//RAR FILE HANDLING...
								if(rarFileSD.isFile()){//EXTRATC FILES TO CACHE DIR AND THEN OPEN IT...
									//new ExtractRarFile(mContext, rarFileSD, size.x*8/9, null, file2.getFile(), 0);
									Toast.makeText(mContext, R.string.cantextractfromrar,Toast.LENGTH_SHORT).show();
								}else{
									//LISTING THE DIRECTORIES....
									rarPathSD = rarFileSD.getPath();
									if(rarPathSD.startsWith("\\"))
										rarPathSD = rarPathSD.substring(0,rarPathSD.length());
									SDManager.nStack.push(rarFileSD.getFileName()+" -> Rar");
									setRarAdapter();
								}
							}else if(TAR_SD){//HANDLING TAR FILE....
								if(tFileSD.isFile()){
									//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
									new ExtractTarFile(mContext, tFileSD, size.x*8/9, null, file2.getFile(), 0);
								}else{
									tarPathSD = tFileSD.getPath();
									SDManager.nStack.push(tFileSD.getName()+" -> Tar");
									setTarAdapter();
								}
							}else{//ORDINARY FILE HANDLING....
								if(!file2.isLocked()){
									//this item is unlocked,no need to verify for password....
									if (!file2.isDirectory()) {
										new OpenFileDialog(mContext, Uri.parse(file2.getPath()), size.x*8/9);
									} else if (file2.isDirectory()) {
										SDManager.nStack.push(file2.getPath());
										setAdapter(2);
										//resetPager();
									}
								}else{
									new MasterPassword(mContext, size.x*8/9, file2, preferences,Constants.MODES.OPEN);
								}	
							}
						break;

					case 1:
						// COPY TO CLOUD
						/**
						 * if root panel is currently on cloud mode,then on
						 * selection from below list,it migrated to root panel
						 * and user there does the pasting work of selected file
						 * from sd card panel..... else a separate dialog is
						 * fired listing the account and asking user to select a
						 * directory to paste...
						 */
						Toast.makeText(mContext, R.string.supporttakenBack,Toast.LENGTH_SHORT).show();
						break;

					case 2:
						// COPY
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
							mVFlipper.setAnimation(nextAnim());
						}
						
						if(ZIP_SD||RAR_SD||TAR_SD){
							//ZIP CONTENTS HAS TO EXTRACTED AT USER SPECIFIED LOCATION.....
							
							/**
							 * 
							 */
							new GetHomeDirectory(mContext, size.x*8/9, null);
						}else{
							//ORDINARY FILE EXLORING...
							CREATE_FILE = RENAME_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							COPY_COMMAND = true;
							COPY_FILES = new ArrayList<Item>();
							COPY_FILES.add(file2);
						}						
						break;

					case 3:
						// CUT
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
							mVFlipper.setAnimation(nextAnim());
						}
						
						if(ZIP_SD){
							//ZIP FILE IS OPENED....
							//SELECTED FILE HAS TO BE EXTRACTED...
							
							//FILE HAS TO BE EXTRACTED IN CURRENT DIRECTORY....
							new ExtractZipFile(mContext, zFileSD, size.x*8/9, file2.getParent(), file2.getFile() , 1);
						}else{
							//ORDINARY FILE EXPLORING.... 
							CREATE_FILE = RENAME_COMMAND = COPY_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							CUT_COMMAND = true;
							COPY_FILES = new ArrayList<Item>();
							COPY_FILES.add(file2);
						}						
						break;
					case 4:
							// PASTE
							if(ZIP_SD||RAR_SD||TAR_SD)
								Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
							else if(file2.isDirectory()){
								if(!file2.isLocked()){//checking the locked status of current folder..
									boolean flag = false;
									try{
										int l = COPY_FILES.size();
										
										//checking whether the copy list contains any locked items or not.
										//if it contains,then password verification...
										for(int i=0;i<l;++i){
											if(COPY_FILES.get(i)!=null)
													if(COPY_FILES.get(i).isLocked()){
														flag = true;
														break;
													}
										}
									}catch(NullPointerException e){
										
									}
									
									if(flag)
										new MasterPassword(mContext, size.x*8/9, file2, preferences, Constants.MODES.COPY);
									else
										pasteCommand(true);
								}	
								else 
									new MasterPassword(mContext, size.x*8/9, file2, preferences, Constants.MODES.PASTEINTO);
							}	
							break;
					case 5:
							// ZIP
							if(ZIP_SD||RAR_SD||TAR_SD)
								Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
							else{
								if(!file2.isLocked()){
									ArrayList<Item> temp = new ArrayList<Item>();
									temp.add(file2);
									new CreateZip(mContext, size.x*8/9, temp);
								}else
									new MasterPassword(mContext, size.x*8/9, file2, preferences, Constants.MODES.ARCHIVE);
							}
						
							break;
					case 6:
						
							//DELETE
							if(ZIP_SD||RAR_SD||TAR_SD)
								Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
							else{
								if(!file2.isLocked()){
									deleteMethod(file2);
								}else
									new MasterPassword(mContext, size.x*8/9, file2, preferences,
											Constants.MODES.DELETE);
							}
							break;
					case 7:
							// RENAME
							if(ZIP_SD||RAR_SD||TAR_SD)
								Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
							else{
								if(!file2.isLocked()){
									COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
									RENAME_COMMAND = true;
									mVFlipper.showPrevious();
									mVFlipper.setAnimation(prevAnim());
									editBox.setText(file2.getName());
									editBox.setSelected(true);
								}else{
									new MasterPassword(mContext, size.x*8/9, file2, preferences, Constants.MODES.RENAME);
								}
							}						
							break;

					case 8:
						// SEND
						if(ZIP_SD){
							if(zFileSD.isFile())
								new ExtractZipFile(mContext, zFileSD, size.x*8/9, null, file2.getFile(), 2);
							else
								Toast.makeText(mContext, R.string.cannotsendfolder, Toast.LENGTH_SHORT).show();
						}else if(RAR_SD){
							
						}else if(TAR_SD){
							if(tFileSD.isFile()){
								//mode is 2 share the file ...
								new ExtractTarFile(mContext, tFileSD, size.x*8/9, null, file2.getFile(), 2);
							}else
								Toast.makeText(mContext, R.string.cannotsendfolder, Toast.LENGTH_SHORT).show();
						}
						else{
							if(!file2.isDirectory()){
								if(!file2.isLocked())
									new BluetoothChooser(mContext, file2.getPath(), size.x*8/9  , null);
								else 
									new MasterPassword(mContext, size.x*8/9, file2, preferences, Constants.MODES.SEND);
							}	
							else
								showToast(getString(R.string.compressandsend));
						}						
						break;
					case 9:
						// gesture to the selected file....
						if(ZIP_SD||RAR_SD||TAR_SD)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else
							new AddGesture(mContext, size.x, size.y*8/9,file2.getPath());
						break;
					case 10:
						// PROPERTIES
						if(ZIP_SD)
							new ArchiveEntryProperties(mContext, zFileSD, size.x*8/9);
						else if(RAR_SD)
							new RarFileProperties(mContext, rarFileSD, size.x*8/9);
						else if(TAR_SD)
							new TarFileProperties(mContext, tFileSD, size.x*8/9);
						else
							new FileProperties(mContext, size.x*8/9, file2.getFile());
					}
				}			
			});
			
			root.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
					// TODO Auto-generated method stub
					if(SEARCH_FLAG){
						if(ZIP_SD)
							zFileSD = zSearch.get(position);
						else if(RAR_SD)
							rarFileSD = rSearch.get(position);
						else if(TAR_SD)
							tFileSD = tSearch.get(position);
						else
							file2 = searchList.get(position);
					}else{
						if(ZIP_SD)
							zFileSD = zListSD.get(position);
						else if(RAR_SD)
							rarFileSD = rListSD.get(position);
						else if(TAR_SD)
							tFileSD = tListSD.get(position);
						else
							file2 = sdItemsList.get(position);
					}
					
					if (CREATE_FILE || RENAME_COMMAND || SEARCH_FLAG) {
						mVFlipper.setAnimation(nextAnim());
						mVFlipper.showNext();
						CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = RENAME_COMMAND = false;
						;
					}
					
					if(ZIP_SD){//zip file handling...
						if(zFileSD.isFile()){
							//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
							new ExtractZipFile(mContext, zFileSD, size.x*8/9 , null , file2.getFile() , 0);
						}else{
							//DIRECTORY HAS TO BE OPENED....
							zipPathSD = zFileSD.getPath();
							SDManager.nStack.push(zipPathSD+" -> Zip");
							if(zipPathSD.startsWith("/"))
								zipPathSD = zipPathSD.substring(1, zipPathSD.length());
							setZipAdapter();
						}						
					}else if(RAR_SD){//rar file handling....
						if(rarFileSD.isFile()){//EXTRACT THE FILE TO CACHE DIRECTORY AND THEN OPEN IT WITH
							//APPROPRIATE APP.....
							//new ExtractRarFile(mContext, rarFileSD, size.x*8/9, null, file2.getFile(), 0);
							Toast.makeText(mContext, R.string.cantextractfromrar,Toast.LENGTH_SHORT).show();
						}else{
							rarPathSD = rarFileSD.getPath();
							if(rarPathSD.startsWith("\\"))
								rarPathSD = rarPathSD.substring(0,rarPathSD.length());
							SDManager.nStack.push(rarFileSD.getFileName()+" -> Rar");
							setRarAdapter();
						}
					}else if(TAR_SD){//HANDLING TAR FILE....
						if(tFileSD.isFile()){
							//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
							new ExtractTarFile(mContext, tFileSD, size.x*8/9, null, file2.getFile(), 0);
						}else{
							tarPathSD = tFileSD.getPath();
							SDManager.nStack.push(tFileSD.getName()+" -> Tar");
							setTarAdapter();
						}
					}else{//ordinary file handling...
						if(!file2.isLocked()){
							//this item is unlocked,no need to verify for password....
							if (!file2.isDirectory()) {
								new OpenFileDialog(mContext, Uri.parse(file2.getPath()), size.x*8/9);
							} else if (file2.isDirectory()) {
								SDManager.nStack.push(file2.getPath());
								setAdapter(2);
								//resetPager();
							}
						}else{
							new MasterPassword(mContext, size.x*8/9, file2, preferences,Constants.MODES.OPEN);
						}
					}
				}
			});
		}
	}

	/**
	 * 
	 * @author Anurag
	 *
	 */
	public static class AppPanel extends BaseFragment {
		public AppPanel(){
			//PASSING THE ANIMATION TYPE FOR THE LIST VIEW.....
			super(LIST_ANIMATION);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// mContext = getActivity();
			APP_LIST_VIEW = getListView();
			APP_LIST_VIEW.setSelector(R.drawable.blue_button);
			ColorDrawable color = new ColorDrawable(android.R.color.black);
			APP_LIST_VIEW.setDivider(color);
			setListAdapter(nAppAdapter);
			APP_LIST_VIEW.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					info = nList.get(position);
					pos = position;
					ArrayList<ApplicationInfo> temp = new ArrayList<ApplicationInfo>();
					temp.add(info);
					new AppBackup(mContext, size.x*8/9, temp);
				}
			});
			final Dialog dia = new Dialog(getActivity(),
					R.style.custom_dialog_theme);
			dia.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) dia.findViewById(R.id.list);
			AdapterLoaders loaders = new AdapterLoaders(mContext, true , 3);
			lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.blue_button));
			dia.getWindow().getAttributes().width = size.x*8/9;
			APP_LIST_VIEW
					.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int position, long id) {
							pos = position; // Taking pos to get the index value
											// for launching an app from nList
							info = nList.get(position);
							dia.show();
							return true;
						}
					});
			lo.setOnItemLongClickListener(null);
			lo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					// TODO Auto-generated method stub
					dia.dismiss();
					switch (position) {
					case 0:
						// AN APP HAS TO BE LAUNCHED
						LAUNCH_INTENT = new Intent();
						LAUNCH_INTENT = getActivity().getPackageManager()
								.getLaunchIntentForPackage(info.packageName);
						try {
							startActivity(LAUNCH_INTENT);
							/**
							 * TRY BLOCK HAS BEEN USED IF AN APP LIKE UNLOCKER
							 * APP DONOT HAVE ACTIVITY TO SHOW ,THEN IT THROWS
							 * RUNTIME ERROR
							 */
						} catch (RuntimeException e) {
							Toast.makeText(mContext, R.string.noactivity,Toast.LENGTH_SHORT).show();
						}
						break;

					case 1:
						// UNINSTALL APP
						LAUNCH_INTENT = new Intent();
						LAUNCH_INTENT.setAction(Intent.ACTION_DELETE);
						LAUNCH_INTENT.setData(Uri.parse("package:"+info.packageName));
						startActivity(LAUNCH_INTENT);
						break;
					case 2:
						// LAUNCH DIALOG TO TAKE BACKUP
						ArrayList<ApplicationInfo> temp = new ArrayList<ApplicationInfo>();
						temp.add(info);
						new AppBackup(mContext, size.x*8/9, temp);
						break;
					case 3:
						// DELETE BACKUP IF EXISTS
						if (nManager.backupExists(info) == 0) {
							Toast.makeText(getActivity(),R.string.nobackuptodelete,Toast.LENGTH_SHORT).show();
						} else {
							PackageInfo i = null;
							PackageManager m = mContext.getPackageManager();
							try {
								i = m.getPackageInfo(info.packageName, 0);
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							File del = new File(PATH + "/File Quest/AppBackup/"+info.loadLabel(m) + "-v" + i.versionName+".apk");
							if (del.delete()) {
								Toast.makeText(mContext,R.string.backupdeleted,Toast.LENGTH_SHORT).show();
								nList = nManager.giveMeAppList();
								nAppAdapter = new AppAdapter(mContext,R.layout.row_list_1, nList);
								if (MULTI_SELECT_APPS)
									nAppAdapter.MULTI_SELECT = true;
								APP_LIST_VIEW.setAdapter(nAppAdapter);
								APP_LIST_VIEW.setSelection(pos);
							} else
								Toast.makeText(mContext,R.string.failtodeletebackup,Toast.LENGTH_SHORT).show();
						}
						break;
					case 4:
						// CREATE FLASHABLE ZIP
						ArrayList<ApplicationInfo> t = new ArrayList<ApplicationInfo>();
						t.add(info);
						new CreateZipApps(mContext, size.x*8/9, t);
						break;
					case 5:
						// SEND APPS
						new BluetoothChooser(mContext, info.sourceDir,size.x*8/9, null);
						break;
					case 6:
						// APP PROPERTIES
						new AppProperties(mContext, size.x*8/9,	info.packageName);
					}
				}
			});
		}

	}

	@Override
	public void onClick(final View v) {
		CURRENT_ITEM = mViewPager.getCurrentItem();
		switch (v.getId()) {

		case R.id.g_open:
			new G_Open(mContext, size.x, size.y);
			break;

		case R.id.bottom_Quit:
			FileQuest.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			break;

		case R.id.bottom_paste:
			if (CURRENT_ITEM == 0||ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT) {
				Toast.makeText(mContext, getString(R.string.pastenotallowed), Toast.LENGTH_SHORT).show();
			}else{
				pasteCommand(false);
			}
			break;

		case R.id.bottom_copy:
			if(ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 5,getString(R.string.enablemultiselect));
			break;

		case R.id.bottom_cut:
			if(ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 2,getString(R.string.enablemultiselect));
			break;

		case R.id.bottom_zip:
			if(ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 3,getString(R.string.enablemultiselect));
			break;

		case R.id.bottom_delete:
			if((ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT)){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 4,getString(R.string.enablemultiselect));
			break;

		case R.id.appSettings:
			startActivityForResult(new Intent(mContext , Settings.class), 100);
			break;

		case R.id.bottom_multi: {
			QuickAction action = new QuickAction(mContext);
			ActionItem item = new ActionItem();

			if (element == null || !elementInFocus) {
				item = new ActionItem(-1, getString(R.string.multiforgallery),getResources().getDrawable(R.drawable.ic_launcher_images));
				action.addActionItem(item);
			} else {
				if (FileGalleryAdapter.MULTI_SELECT)
					item = new ActionItem(0,getString(R.string.multiforgallery), getResources().getDrawable(R.drawable.ic_launcher_apply));
				else
					item = new ActionItem(0,getString(R.string.multiforgallery), getResources().getDrawable(R.drawable.ic_launcher_images));
				action.addActionItem(item);
			}

			if ((RootAdapter.MULTI_SELECT))
				item = new ActionItem(1, getString(R.string.multiforroot),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				item = new ActionItem(1, getString(R.string.multiforroot),getResources().getDrawable(R.drawable.ic_launcher_system));
			action.addActionItem(item);

			if ((SDAdapter.MULTI_SELECT))
				item = new ActionItem(2, getString(R.string.multiforsd),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				item = new ActionItem(2, getString(R.string.multiforsd),getResources().getDrawable(R.drawable.ic_launcher_sdcard));
			action.addActionItem(item);

			if (MULTI_SELECT_APPS)
				item = new ActionItem(3, getString(R.string.multiforapp),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				item = new ActionItem(3, getString(R.string.multiforapp),getResources().getDrawable(R.drawable.ic_launcher_apk));
			action.addActionItem(item);
			action.show(v);
			action.setOnActionItemClickListener(new OnActionItemClickListener() {
				@Override
				public void onItemClick(QuickAction source, int pos,
						int actionId) {
					// TODO Auto-generated method stub
					switch (actionId) {

					case -1: {
						QuickAction ac = new QuickAction(mContext);
						ActionItem it = new ActionItem(0,getString(R.string.selectcategoryfromgallery));
						ac.addActionItem(it);
						ac.show(v);
					}
						break;
					case 0:
						if (FileGalleryAdapter.MULTI_SELECT) {
							element = new FileGalleryAdapter(mContext , mediaFileList);
							FileGalleryAdapter.thumbselection = new boolean[mediaFileList.size()];
							FileGalleryAdapter.MULTI_SELECT = false;
							LIST_VIEW_3D.setAdapter(element);
						} else {
							element = new FileGalleryAdapter(mContext , mediaFileList);
							FileGalleryAdapter.thumbselection = new boolean[mediaFileList.size()];
							FileGalleryAdapter.MULTI_SELECT = true;
							LIST_VIEW_3D.setAdapter(element);
							if (CURRENT_ITEM == 3) {
								mFlipperBottom.showNext();
								mFlipperBottom.setAnimation(prevAnim());
								mVFlipper.showPrevious();
								mVFlipper.setAnimation(prevAnim());
							}
							mViewPager.setCurrentItem(0);
						}
						break;

					case 1:
						if (RootAdapter.MULTI_SELECT) {
							rootAdapter = new RootAdapter(mContext,rootItemList);
							RootAdapter.thumbselection = new boolean[rootItemList.size()];
							RootAdapter.MULTI_SELECT = false;
							if(!ZIP_ROOT&&!RAR_ROOT&&!TAR_ROOT){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								simple.setAdapter(rootAdapter);
							}	
						} else {
							rootAdapter = new RootAdapter(mContext, rootItemList);
							RootAdapter.thumbselection = new boolean[rootItemList.size()];
							RootAdapter.MULTI_SELECT = true;
							if(!ZIP_ROOT&&!RAR_ROOT&&!TAR_ROOT){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								resetPager();
							}								
						}
						break;
					case 2:
						if (SDAdapter.MULTI_SELECT) {
							sdAdapter = new SDAdapter(mContext,sdItemsList);
							SDAdapter.thumbselection = new boolean[sdItemsList.size()];
							SDAdapter.MULTI_SELECT = false;
							if(!ZIP_SD&&!RAR_SD&&!TAR_SD){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								root.setAdapter(sdAdapter);
							}	
						} else {
							sdAdapter = new SDAdapter(mContext,sdItemsList);
							SDAdapter.thumbselection = new boolean[sdItemsList.size()];
							SDAdapter.MULTI_SELECT = true;
							if(!ZIP_SD&&!RAR_SD&&!TAR_SD){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								//root.setAdapter(sdAdapter);
								resetPager();
							}	
						}
						break;

					case 3:
						if (MULTI_SELECT_APPS) {
							MULTI_SELECT_APPS = nAppAdapter.MULTI_SELECT = false;
							APP_LIST_VIEW.setAdapter(nAppAdapter);
							APP_LIST_VIEW.setSelection(pos);
						} else {
							MULTI_SELECT_APPS = nAppAdapter.MULTI_SELECT = true;
							mViewPager.setCurrentItem(3);
							APP_LIST_VIEW.setAdapter(nAppAdapter);
							APP_LIST_VIEW.setSelection(pos);
						}
						break;
						}
					}
				});
			}
			break;

		case R.id.bottom_multi_send_app:
			if (MULTI_SELECT_APPS) {
				if (nAppAdapter.C > 0) {
					try {
						new MultiSendApps(mContext, size,nAppAdapter.MULTI_APPS);
					} catch (Exception e) {
						Toast.makeText(mContext, R.string.unabletosend,Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mContext, R.string.someapps,Toast.LENGTH_SHORT).show();
				}
			} else {
				QuickAction ac = new QuickAction(mContext);
				ActionItem it = new ActionItem(0, getResources().getString((R.string.enablefirstforappstore)));
				ac.addActionItem(it);
				ac.show(v);
			}
			break;
		case R.id.bottom_multi_select_backup:
			if (MULTI_SELECT_APPS) {
				if (nAppAdapter.C > 0) {
					new AppBackup(mContext, size.x*8/9,nAppAdapter.MULTI_APPS);
				} else {
					Toast.makeText(mContext, R.string.someapps,Toast.LENGTH_SHORT).show();
				}
			} else {
				QuickAction ac = new QuickAction(mContext);
				ActionItem it = new ActionItem(0, getResources().getString(R.string.enablefirstforappstore));
				ac.addActionItem(it);
				ac.show(v);
			}
			break;

		case R.id.bottom_user_apps:
			// SETS THE SETTING TO SHOW DOWNLOADED APPS ONLY
			edit.putInt("SHOW_APP", 1);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 1;
			Toast.makeText(mContext,getString(R.string.showinguserapps), Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;
		case R.id.bottom_system_apps:
			// SETS THE SETTING TO SHOW SYSTEM APPS ONLY
			edit.putInt("SHOW_APP", 2);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 2;
			Toast.makeText(mContext,getString(R.string.showingsystemapps), Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;

		case R.id.bottom_both_apps:
			// SETS THE SETTING TO SHOW DOWNLOADED AND SYSTEM APPS
			edit.putInt("SHOW_APP", 3);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 3;
			Toast.makeText(mContext,getString(R.string.showinguserandsystemapps),Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;

		case R.id.searchBtn:
				if(ZIP_SD||ZIP_ROOT){
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND....
						SEARCH_FLAG = false;
						mVFlipper.showNext();
						mVFlipper.setAnimation(nextAnim());
					}else//SEARCH INSIDE ZIP ARCHIVE...
						zipSearch();
				}else if(RAR_SD||RAR_ROOT){
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND....
						SEARCH_FLAG = false;
						mVFlipper.showNext();
						mVFlipper.setAnimation(nextAnim());
					}else//SEARCH INSIDE RAR ARCHIVE....
						rarSearch();
				}else if(TAR_SD||TAR_ROOT){
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND...
						SEARCH_FLAG = false;
						mVFlipper.showNext();
						mVFlipper.setAnimation(nextAnim());
					}else//SEARCH INSIDE TAR ARCHIVE....
						tarSearch();
				}
				else{
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND....
						SEARCH_FLAG = false;
						mVFlipper.showNext();
						mVFlipper.setAnimation(nextAnim());
					}else//ORDINARY SEARCH....
						search();
				}	
				break;

		case R.id.applyBtn:
			// rename the file with given name from editBox
			editBox = new EditText(mContext);
			editBox = (EditText) findViewById(R.id.editBox);
			String name = editBox.getText().toString();
			// String ext = extBox.getText().toString();
			if (name.length()>0){
				if (CREATE_FILE) {
					// GETS THE PATH WHERE FOLDER OR FILE HAS TO BE CREATED

					// CREATE HIDDEN FOLDER
					if (CREATE_FLAG == 1) {
						if (name.startsWith("."))
							name = CREATE_FLAG_PATH + "/" + name;
						else if (!name.startsWith("."))
							name = CREATE_FLAG_PATH + "/." + name;
						if (CURRENT_ITEM == 1) {
							/**
							 * TRY TO CREATE FOLDER WITH ROOT PREVILLEGES...
							 */
							LinuxShell.execute("mkdir " + name + "\n");
							if (new File(name).exists()) {
								Toast.makeText(mContext,getString(R.string.foldercreated)+ name, Toast.LENGTH_SHORT).show();
								setAdapter(CURRENT_ITEM);
							} else
								Toast.makeText(mContext,getString(R.string.foldernotcreated),Toast.LENGTH_SHORT).show();
						} else if (CURRENT_ITEM == 2) {
							if (new File(name).mkdir()) {
								Toast.makeText(mContext,getString(R.string.foldercreated)+ name, Toast.LENGTH_LONG).show();
								setAdapter(CURRENT_ITEM);
							} else if (!new File(name).mkdir())
								Toast.makeText(mContext,getString(R.string.foldernotcreated),Toast.LENGTH_LONG).show();
						}
						RENAME_COMMAND = CREATE_FILE = SEARCH_FLAG = CUT_COMMAND = COPY_COMMAND = false;
					}
					// CREATE A SIMPLE FOLDER
					else if (CREATE_FLAG == 2) {
						name = CREATE_FLAG_PATH + "/" + name;
						if (CURRENT_ITEM == 1) {
							/**
							 * TRY TO CREATE FOLDER WITH ROOT PREVILLEGES...
							 */
							LinuxShell.execute("mkdir " + name + "\n");
							if (new File(name).exists()) {
								Toast.makeText(mContext,getString(R.string.foldercreated)+ name, Toast.LENGTH_SHORT).show();
								setAdapter(CURRENT_ITEM);
							} else
								Toast.makeText(mContext,getString(R.string.foldernotcreated),Toast.LENGTH_SHORT).show();
						} else if (CURRENT_ITEM == 2) {
							if (new File(name).mkdir()) {
								Toast.makeText(mContext,getString(R.string.foldercreated)+ name, Toast.LENGTH_LONG).show();
								setAdapter(CURRENT_ITEM);
							} else if (!new File(name).mkdir())
								Toast.makeText(mContext,getString(R.string.foldernotcreated),Toast.LENGTH_LONG).show();
						}
						RENAME_COMMAND = CREATE_FILE = SEARCH_FLAG = CUT_COMMAND = COPY_COMMAND = false;
					}
					// CREATE AN EMPTY FILE
					else if (CREATE_FLAG == 3) {
						try {

							if (CURRENT_ITEM == 1) {
								name = RootManager.getCurrentDirectory() + "/"	+ editBox.getText().toString();
								File f = new File(name);
								if (f.exists()) {
									Toast.makeText(mContext,getString(R.string.fileexists),Toast.LENGTH_SHORT).show();
								} else if (!f.exists()) {
									LinuxShell.execute("cat > " + name);
									setAdapter(CURRENT_ITEM);
								}
							} else if (CURRENT_ITEM == 2) {
								if (new File(name).createNewFile()) {
									Toast.makeText(mContext,getString(R.string.foldercreated)+ name, Toast.LENGTH_LONG).show();
									setAdapter(CURRENT_ITEM);
								}
							}
						} catch (IOException e) {
							Toast.makeText(mContext,getString(R.string.foldernotcreated),Toast.LENGTH_LONG).show();
						}
					}
					// AFTER CREATING FILES OR FOLDER AGAIN FLIPPER IS SET TO
					// MAIN MENU
					mVFlipper.setAnimation(nextAnim());
					mVFlipper.showNext();
				}
				// THIS FLIPPER IS SET FOR RENAMING
				else if (RENAME_COMMAND) {
					if (CURRENT_ITEM == 0) {
						name = getPathWithoutFilename(file0.getFile()).getPath() + "/"+ name;
						if (file0.getFile().renameTo(new File(name))) {
							Toast.makeText(mContext,getString(R.string.renamed)+ new File(name).getName(),Toast.LENGTH_SHORT).show();
						} else {
							/**
							 * THIS INTENT IS FIRED WHEN RENAMING OF FILE FAILS
							 * SHOWING POSSIBLE ERROR
							 */
							new ErrorDialogs(mContext,size.x*8/9,"renameError");
						}
					} else if (CURRENT_ITEM == 1) {
						name = RootManager.getCurrentDirectory() + "/" + name;
						try {
							/*
							 * yet to implement rename command for root files
							 */
							
							if (file.getFile().renameTo(new File(name))) {
								Toast.makeText(mContext,getString(R.string.renamed)+ new File(name).getName(),Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							/**
							 * THIS INTENT IS FIRED WHEN RENAMING OF FILE FAILS
							 * SHOWING POSSIBLE ERROR
							 */
							new ErrorDialogs(mContext, size.x*8/9,"renameError");
						}
					} else if (CURRENT_ITEM == 2) {
						name = SDManager.getCurrentDirectory() + "/" + name;
						if (file2.getFile().renameTo(new File(name))) {
							Toast.makeText(mContext,getString(R.string.renamed)	+ new File(name).getName(),Toast.LENGTH_SHORT).show();
						} else {
							/**
							 * THIS INTENT IS FIRED WHEN RENAMING OF FILE FAILS
							 * SHOWING POSSIBLE ERROR
							 */
							new ErrorDialogs(mContext, size.x*8/9,"renameError");
						}
					}
					// AFTER RENAMING THE FOLDER OR FILES THE FLIPPER IS SET
					// AGAIN TO MAIN MENU
					mVFlipper.setAnimation(nextAnim());
					mVFlipper.showNext();
				}
				setAdapter(CURRENT_ITEM);
				RENAME_COMMAND = CREATE_FILE = SEARCH_FLAG = CUT_COMMAND = COPY_COMMAND = false;
			} else if (name.length() == 0)
				Toast.makeText(mContext, R.string.entervalidname,Toast.LENGTH_SHORT).show();
			break;

		case R.id.homeDirBtn:
			HOME_DIRECTORY = preferences.getString("HOME_DIRECTORY", null);
			if (HOME_DIRECTORY == null) {
				new GetHomeDirectory(mContext, size.x*8/9, preferences);
			} else if (new File(HOME_DIRECTORY).exists()) {
				if (CURRENT_ITEM == 2) {
					//checking whether home is locked or not....
					if(!new Item(new File(HOME_DIRECTORY), null, null, null).isLocked()){
						String hPath = HOME_DIRECTORY;
						boolean locked = false;
						
						//checking whether home's parent folder is locked or not....
						while(!hPath.equalsIgnoreCase(Constants.PATH)){
							hPath = hPath.substring(0, hPath.lastIndexOf("/"));
							if(new Item(new File(hPath), null, null, null).isLocked()){
								locked = true;
								break;
							}
						}
						
						if(!locked){
							SDManager.nStack.push(HOME_DIRECTORY);
							setAdapter(2);
						}else	
							new MasterPassword(mContext, size.x*8/9, new Item(new File(hPath), null, null, null)
							,preferences, Constants.MODES.HOME);
					}	
					else
						new MasterPassword(mContext, size.x*8/9, new Item(new File(HOME_DIRECTORY), null, null, null)
							,preferences, Constants.MODES.HOME);
				} else if (CURRENT_ITEM == 1) {
					if(!new Item(new File(HOME_DIRECTORY), null, null, null).isLocked()){
						String hPath = HOME_DIRECTORY;
						boolean locked = false;
						
						//checking whether home's parent folder is locked or not....
						while(!hPath.equalsIgnoreCase(Constants.PATH)){
							hPath = hPath.substring(0, hPath.lastIndexOf("/"));
							if(new Item(new File(hPath), null, null, null).isLocked()){
								locked = true;
								break;
							}
						}
						
						if(!locked){
							RootManager.nStack.push(HOME_DIRECTORY);
							setAdapter(1);
						}else
							new MasterPassword(mContext, size.x*8/9, new Item(new File(hPath), null, null, null)
								,preferences, Constants.MODES.HOME);
							
					}	
					else
						new MasterPassword(mContext, size.x*8/9, new Item(new File(HOME_DIRECTORY), null, null, null),
								preferences, Constants.MODES.HOME);
				} else if (CURRENT_ITEM == 0) {
					QuickAction d = new QuickAction(mContext);
					ActionItem df = new ActionItem(8, "/", getResources().getDrawable(R.drawable.ic_launcher_droid_home));
					d.addActionItem(df);
					df = new ActionItem(9, getString(R.string.sd),getResources().getDrawable(R.drawable.ic_launcher_droid_home));
					d.addActionItem(df);
					d.show(v);
					d.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
						@Override
						public void onItemClick(QuickAction source, int pos,
								int actionId) {
							// TODO Auto-generated method stub
							switch (actionId) {
							case 8:
								RootManager.nStack.push(HOME_DIRECTORY);
								setAdapter(1);
								break;
							case 9:
								LAST_PAGE = 0;
								SDManager.nStack.push(HOME_DIRECTORY);
								setAdapter(2);
								break;
							}
						}
					});

				}
			} else {
				HOME_DIRECTORY = PATH;
				edit.putString("HOME_DIRECTORY", PATH);
				edit.commit();
				new ErrorDialogs(mContext, size.x*8/9, "homeError");
			}

			break;

		case R.id.jumpToBtn:

			if (CURRENT_ITEM == 0) {
				// IF CURRENT ITEM == 0
				// DISPLAYS LIST THAT IS APPLICABLE TO ONLY ALL FILE PANEL

				QuickAction ac = new QuickAction(mContext);
				ActionItem i = new ActionItem(8, getString(R.string.jmpmusic),getResources().getDrawable(R.drawable.ic_launcher_music));
				ac.addActionItem(i);

				i = new ActionItem(9, getString(R.string.jmpapk),getResources().getDrawable(R.drawable.ic_launcher_apk));
				ac.addActionItem(i);

				i = new ActionItem(10, getString(R.string.jmpdocs),getResources().getDrawable(R.drawable.ic_launcher_ppt));
				ac.addActionItem(i);

				i = new ActionItem(11, getString(R.string.jmpimg),getResources().getDrawable(R.drawable.ic_launcher_images));
				ac.addActionItem(i);

				i = new ActionItem(12, getString(R.string.jmpvids),getResources().getDrawable(R.drawable.ic_launcher_video));
				ac.addActionItem(i);

				i = new ActionItem(13, getString(R.string.jmpzip),getResources().getDrawable(R.drawable.ic_launcher_zip_it));
				ac.addActionItem(i);

				i = new ActionItem(14, getString(R.string.jmpmisc),getResources().getDrawable(R.drawable.ic_launcher_rar));
				ac.addActionItem(i);
				ac.setOnActionItemClickListener(this);
				ac.show(v);

			} else {
				// IF CURRENT ITMEM !=0
				// This option allows user to directly go to specified directory
				// from any directory
				final QuickAction actionJump = new QuickAction(
						mContext, 1);
				ActionItem paste = new ActionItem(900,getString(R.string.pastehere), getResources().getDrawable(R.drawable.ic_launcher_paste));
				ActionItem download = new ActionItem(1000,getString(R.string.jmpdown), getResources().getDrawable(R.drawable.ic_launcher_downloads));
				ActionItem camera = new ActionItem(1100,getString(R.string.jmpcam), getResources().getDrawable(R.drawable.ic_launcher_camera));
				ActionItem songs = new ActionItem(1200,getString(R.string.jmpmusfo), getResources().getDrawable(R.drawable.ic_launcher_music_folder));
				ActionItem vid = new ActionItem(1201,getString(R.string.jmpvidfo), getResources().getDrawable(R.drawable.ic_launcher_video));
				ActionItem pro = new ActionItem(1300, getString(R.string.prop),getResources().getDrawable(R.drawable.ic_launcher_stats));
				ActionItem apps = new ActionItem(1400,getString(R.string.selecteddefaultapp), getResources().getDrawable(R.drawable.ic_launcher_select_app));
				actionJump.addActionItem(paste);
				actionJump.addActionItem(download);
				actionJump.addActionItem(camera);
				actionJump.addActionItem(songs);
				actionJump.addActionItem(vid);
				actionJump.addActionItem(pro);
				actionJump.addActionItem(apps);
				actionJump.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
							@Override
							public void onItemClick(QuickAction source,int pos, int actionId) {
								// TODO Auto-generated method stub
								File fJump = null;
								switch (actionId) {
								case 900:
									pasteCommand(false);
									break;
								case 1000:
									fJump = new File(PATH + "/Download");
									if (!fJump.exists())
										fJump.mkdir();
									if (CURRENT_ITEM == 2)
										SDManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										RootManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;
								case 1100:
									fJump = new File(PATH + "/DCIM");
									if (!fJump.exists())
										fJump.mkdir();
									if (CURRENT_ITEM == 2)
										SDManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										RootManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;
								case 1200:
									fJump = new File(PATH + "/Music");
									if (!fJump.exists())
										fJump.mkdir();
									if (CURRENT_ITEM == 2)
										SDManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										RootManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;

								case 1201:
									fJump = new File(PATH + "/Video");
									if (!fJump.exists())
										fJump.mkdir();
									if (CURRENT_ITEM == 2)
										SDManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										RootManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;
								case 1300:
									//getting the current properties for opened path.....
									if (CURRENT_ITEM == 1){
										if(ZIP_ROOT && zFileSimple !=null)
											new ArchiveEntryProperties(mContext, zFileSimple, size.x*8/9);
										else if(TAR_ROOT && tFileRoot !=null)
											new TarFileProperties(mContext, tFileRoot, size.x*8/9);
										else if(RAR_ROOT && rFileRoot !=null)
											new RarFileProperties(mContext, rFileRoot, size.x*8/9);
										else
											new FileProperties(mContext,size.x *8/9, file.getFile());
									} else if (CURRENT_ITEM == 2) {
										if(ZIP_SD && zFileSD != null)
											new ArchiveEntryProperties(mContext, zFileSD, size.x*8/9);
										else if(TAR_SD && tFileSD!=null)
											new TarFileProperties(mContext, tFileSD, size.x*8/9);
										else if(RAR_SD && rarFileSD !=null)
											new RarFileProperties(mContext, rarFileSD, size.x*8/9);
										else
											new FileProperties(mContext,size.x*8/9, file2.getFile());
									}
									break;
								case 1400:
									showDefaultApps(v);
								}
							}
						});
				actionJump.show(v);
			}
			break;

		case R.id.addBtn:

			if (CURRENT_ITEM == 0) {
				showDefaultApps(v);
			} else {
				NEW_OPTIONS(v);
			}

			break;

		case R.id.backupAll:
			QuickAction as = new QuickAction(mContext);
			ActionItem o = new ActionItem(100,getString(R.string.backupuserapp), getResources().getDrawable(R.drawable.ic_launcher_user));
			as.addActionItem(o);
			o = new ActionItem(200, getString(R.string.backupsystemapp),getResources().getDrawable(R.drawable.ic_launcher_system));
			as.addActionItem(o);
			o = new ActionItem(300, getString(R.string.backupboth),getResources().getDrawable(R.drawable.ic_launcher_both));
			as.addActionItem(o);
			as.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
				@Override
				public void onItemClick(QuickAction source, int pos, int Id) {
					// TODO Auto-generated method stub
					// ArrayList<ApplicationInfo> li;
					switch (Id) {
					case 100:
						new AppBackup(mContext, size.x*8/9, nList);
						break;
					case 200:
						new AppBackup(mContext, size.x*8/9, nManager.getSysApps());
						break;
					case 300:
						new AppBackup(mContext, size.x*8/9, nManager.getAllApps());
					}
				}
			});
			as.show(v);
			break;

		case R.id.cleanBackups:
			// THIS BUTTON DISPLAYS TWO OPTIONS -1.TO DELETED THE BACKUPS
			// 2. DELETE THE FLASHABLE ZIPS CREATED
			QuickAction c = new QuickAction(mContext);
			ActionItem i = new ActionItem(100,getString(R.string.deleteearlierbackup), getResources().getDrawable(R.drawable.ic_launcher_backupall));
			c.addActionItem(i);
			i = new ActionItem(200, getString(R.string.deletezipbackup),getResources().getDrawable(R.drawable.ic_launcher_zip_it));
			c.addActionItem(i);
			c.show(v);
			c.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
				@Override
				public void onItemClick(QuickAction source, int pos,
						int actionId) {
					// TODO Auto-generated method stub
					switch (actionId) {
					case 100:
						new DeleteBackups(mContext, size.x *8/9);
						break;
					case 200:
						new DeleteFlashable(mContext, size.x*8/9);
					}
				}
			});
			break;

		case R.id.zipItBtn:
			if (CURRENT_ITEM == 3)
				new ErrorDialogs(mContext, size.x*8/9, "FlashableZips");
			break;
		}
	}

	

	/**
	 * THIS FUNCTION SHOWS THE BOX GINIG THE OPTIONS TO ADD NEW FOLDER AND CLOUD
	 * STUFFS
	 * 
	 * @param v
	 */
	private void NEW_OPTIONS(View v) {
		// TODO Auto-generated method stub
		final QuickAction action = new QuickAction(mContext, 1);
		ActionItem hiddenItem = new ActionItem(-10000,getString(R.string.dropbox), getResources().getDrawable(R.drawable.ic_launcher_drop_box));
		action.addActionItem(hiddenItem);
		hiddenItem = new ActionItem(-9000, getString(R.string.googledrive),getResources().getDrawable(R.drawable.ic_launcher_google_drive));
		action.addActionItem(hiddenItem);

		hiddenItem = new ActionItem(-7000, getString(R.string.skydrive),getResources().getDrawable(R.drawable.ic_launcher_sky_drive));
		action.addActionItem(hiddenItem);

		hiddenItem = new ActionItem(500,getString(R.string.createhiddenfolder), getResources().getDrawable(R.drawable.ic_launcher_add_new));
		action.addActionItem(hiddenItem);
		ActionItem folderItem = new ActionItem(600,getString(R.string.createfolder), getResources().getDrawable(R.drawable.ic_launcher_add_new));
		action.addActionItem(folderItem);
		ActionItem fileItem = new ActionItem(700,getString(R.string.createfile), getResources().getDrawable(R.drawable.ic_launcher_new_file));
		action.addActionItem(fileItem);
		action.show(v);
		action.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				// TODO Auto-generated method stub
				if (actionId >= 500) {
					CREATE_FLAG_PATH = null;
					if (CURRENT_ITEM == 1)
						CREATE_FLAG_PATH = RootManager.getCurrentDirectory();
					else if (CURRENT_ITEM == 2)
						CREATE_FLAG_PATH = SDManager.getCurrentDirectory();
					CREATE_FILE = true;
					editBox.setText(null);
					switch (actionId) {
					case 500:

						editBox.setHint(getString(R.string.enterfoldername));
						CREATE_FLAG = 1;
						LinearLayout l = (LinearLayout) findViewById(R.id.applyBtn);
						l.setVisibility(View.VISIBLE);
						editBox.setTextColor(Color.WHITE);
						// if file is to created then edittext for extension of
						// file is also displayed
						// then mViewFlipper is rotated to editText for getting
						// text
						mVFlipper.setAnimation(nextAnim());
						mVFlipper.showNext();
						mVFlipper.showNext();
						break;
					case 600:

						editBox.setHint(getString(R.string.enterfoldername));
						CREATE_FLAG = 2;
						LinearLayout l2 = (LinearLayout) findViewById(R.id.applyBtn);
						l2.setVisibility(View.VISIBLE);
						editBox.setTextColor(Color.WHITE);
						// if file is to created then edittext for extension of
						// file is also displayed
						// then mViewFlipper is rotated to editText for getting
						// text
						mVFlipper.setAnimation(nextAnim());
						mVFlipper.showNext();
						mVFlipper.showNext();
						break;
					case 700:
						editBox.setHint(getString(R.string.enterfilename));
						CREATE_FLAG = 3;
						LinearLayout l3 = (LinearLayout) findViewById(R.id.applyBtn);
						l3.setVisibility(View.VISIBLE);
						editBox.setTextColor(Color.WHITE);
						// if file is to created then edittext for extension of
						// file is also displayed
						// then mViewFlipper is rotated to editText for getting
						// text
						mVFlipper.setAnimation(nextAnim());
						mVFlipper.showNext();
						mVFlipper.showNext();
					}

				} else {
					/**
					 * CLOUD STORAGE OPTIONS
					 */
					switch (actionId) {
						case -10000:
									/*
								 	* DROPBOX STUFF
								 	*/
									DBoxAuth.DoAuth(FileQuest.this);
									break;
									
						default:
							
									Toast.makeText(mContext, ""+Constants.dboxDB.getTotalUsers(),
											Toast.LENGTH_SHORT).show();
									//Toast.makeText(mContext, R.string.supporttakenBack,Toast.LENGTH_SHORT).show();
									break;
					}
				}
			}
		});
	}

	/**
	 * OnActivityResult For FileQuest Class
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 100){
			if(Settings.settingsChanged || Settings.pagerAnimSettingsChanged){
				//some change in settings encountered....
				if(Settings.pagerAnimSettingsChanged){
					String[] te = getResources().getStringArray(R.array.effects);		
					mViewPager.setTransitionEffect(TransitionEffect.valueOf(te[PAGER_ANIMATION]));
				}
				setAdapter(CURRENT_ITEM);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CURRENT_ITEM = mViewPager.getCurrentItem();
			
			if(slidemenu.isLeftMenuOpen())
				slidemenu.closeLeftMenu(true);
			else if ((SEARCH_FLAG || RENAME_COMMAND || CREATE_FILE)
					&& (CURRENT_ITEM == 1 || CURRENT_ITEM == 2 || CURRENT_ITEM == 0)) {
				setAdapter(CURRENT_ITEM);
				mVFlipper.setAnimation(nextAnim());
				mVFlipper.showNext();
				if (CURRENT_ITEM == 0) {
					mFlipperBottom.showNext();
					mFlipperBottom.setAnimation(nextAnim());
				}
				SEARCH_FLAG = RENAME_COMMAND = COPY_COMMAND = CUT_COMMAND = CREATE_FILE = false;
				if (elementInFocus) {
					// LIST_VIEW_3D.setAdapter(adapter);
					elementInFocus = false;
					LIST_VIEW_3D.setVisibility(View.GONE);
					FILE_GALLEY.setVisibility(View.VISIBLE);
				}
			} else if (CURRENT_ITEM == 0 && elementInFocus) {
				elementInFocus = false;
				resetPager();
				mFlipperBottom.showNext();
				mFlipperBottom.setAnimation(nextAnim());
			} else if (CURRENT_ITEM == 0 && !elementInFocus) {
				/**
				 * CHECKS WHETHER THE CURRENT PREF IS 0 IF IT IS FOUND 0 THEN IT
				 * TAKES ACTION TO EXIT ON CURRENT ITEM=0
				 */
				if (CURRENT_PREF_ITEM == 0) {
					if (!mUseBackKey) {
						Toast.makeText(mContext,R.string.pressbackagain, Toast.LENGTH_SHORT).show();
						mUseBackKey = true;
					} else if (mUseBackKey) {
						mUseBackKey = false;
						FileQuest.this.finish();
					//	android.os.Process.killProcess(android.os.Process.myPid());
					}

				}
				/**
				 * IF CURRENT PREF IS NOT 0 THEN IT DIRECTS VIEW PAGER TO STORED
				 * VALUE AND SETS THE BOTTOM MENU TO MULTIPLE OPTIONS INSTEAD OF
				 * SHOWING SD CARD SPACE
				 */
				else {
					LAST_PAGE = CURRENT_PREF_ITEM;
					mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
					mFlipperBottom.showPrevious();
					mFlipperBottom.setAnimation(prevAnim());
				}
			} else if (CURRENT_ITEM == 3) {
				/**
				 * CHECKS WHETHER THE CURRENT PREF IS 3 IF IT IS FOUND 3 THEN IT
				 * TAKES ACTION TO EXIT ON CURRENT ITEM = 3
				 */
				if (CURRENT_PREF_ITEM == 3) {
					if (!mUseBackKey) {
						Toast.makeText(mContext,R.string.pressbackagain, Toast.LENGTH_SHORT).show();
						mUseBackKey = true;

					} else if (mUseBackKey) {
						mUseBackKey = false;
						FileQuest.this.finish();
						//android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
				/**
				 * IF CURRENT PREF IS NOT 3 THEN IT DIRECTS VIEW PAGER TO STORED
				 * VALUE
				 */
				else {
					if (CURRENT_PREF_ITEM == 0) {
						mVFlipper.showPrevious();
						mVFlipper.setAnimation(prevAnim());
						if (elementInFocus) {
							mFlipperBottom.showNext();
							mFlipperBottom.setAnimation(nextAnim());
						} else if (!elementInFocus) {
							mFlipperBottom.showNext();
							mFlipperBottom.setAnimation(nextAnim());
						}
					}
					mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
				}
			}else if(ZIP_ROOT && CURRENT_ITEM==1){
				//POPING OUT THE ZIP PATH.....
				RootManager.nStack.pop();
				/**
				 * ZIP FILE IS OPENED IN SDCARD_PANEL...
				 * HANDLE IT HERE.....
				 * WHEN ZIP FILE IS CLOSED THEN SET THE ZIP PATH TO NULL.....
				 */
				if(zipPathRoot.equals("/")){
					/*
					 * WE ARE ALREADY IN TOP OF ZIP FILE
					 * NOW QUIT THE ZIP FILE....
					 */
					ZIP_ROOT = false;
					zipPathRoot = null;
					CURRENT_ITEM=1;
					resetPager();
				}else
				
				/*
				 * TRY BLOCK IS USED BECAUSE WHEN USER NAVIGATES TO TOP DIRECTORY IN ZIP FILE
				 * CAUSES STRINGINDEXOUTOFBOUND EXCEPTION....
				 * AND FAILS TO GENERATE THE "/" PATH FOR ZIP FILE...
				 * SO MANUALLY GENERATING THE "/" PATH.... 
				 */
				try{
					zipPathRoot = zipPathRoot.substring(0, zipPathRoot.lastIndexOf("/"));
					setZipAdapter();
				}catch(Exception e){
					zipPathRoot = "/";
					setZipAdapter();
				}
				
			}else if(RAR_ROOT&&CURRENT_ITEM==1){//RAR FILE HANDLING ON BACK KEY PRESS...
				RootManager.nStack.pop();
				if(rarPathRoot.equalsIgnoreCase("/")){
					RAR_ROOT = false;
					rarPathRoot = null;
					CURRENT_ITEM=1;
					resetPager();
				}else{
					try{//no need about the exception...
						rarPathRoot = rarPathRoot.substring(0, rarPathRoot.lastIndexOf("\\"));
						setRarAdapter();
					}catch(Exception e){
						rarPathRoot = "/";
						setRarAdapter();
					}
				}
			}else if(TAR_ROOT&&CURRENT_ITEM==1){//TAR FILE HANDLING ON BACK KEY PRESS...
				RootManager.nStack.pop();
				if(tarPathRoot.equals("/")){
					TAR_ROOT = false;
					tarPathRoot = null;
					CURRENT_ITEM=1;
					resetPager();
				}else{
					try{
						tarPathRoot = tarPathRoot.substring(0, tarPathRoot.lastIndexOf("/"));
						setTarAdapter();
					}catch(Exception e){
						tarPathRoot = "/";
						setTarAdapter();
					}
				}
			}
			else if (CURRENT_ITEM == 1&& !RootManager.getCurrentDirectory().equals("/")) {
				RootManager.nStack.pop();
				rootItemList = rootManager.getList();
				CURRENT_ITEM=1;
				resetPager();
				file = new Item(new File(RootManager.getCurrentDirectory()),null, null, null);
			} else if (CURRENT_ITEM == 1&&RootManager.getCurrentDirectory().endsWith("/")) {
				/**
				 * CHECKS WHETHER THE CURRENT PREF IS 1 IF IT IS FOUND 3 THEN IT
				 * TAKES ACTION TO EXIT ON CURRENT ITEM = 1
				 */
				if (CURRENT_PREF_ITEM == 1) {
					if (!mUseBackKey) {
						Toast.makeText(mContext,R.string.pressbackagain, Toast.LENGTH_SHORT).show();
						mUseBackKey = true;
					} else if (mUseBackKey) {
						mUseBackKey = false;
						FileQuest.this.finish();
						//android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
				/**
				 * IF CURRENT PREF IS NOT 1 THEN IT DIRECTS VIEW PAGER TO STORED
				 * VALUE
				 */
				else
					mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
			} 
			else if(ZIP_SD && CURRENT_ITEM==2){
				//POPING OUT THE ZIP PATH....
				
				SDManager.nStack.pop();
				/**
				 * ZIP FILE IS OPENED IN SDCARD_PANEL...
				 * HANDLE IT HERE.....
				 * WHEN ZIP FILE IS CLOSED THEN SET THE ZIP PATH TO NULL.....
				 */
				if(zipPathSD.equals("/")){
					/*
					 * WE ARE ALREADY IN TOP OF ZIP FILE
					 * NOW QUIT THE ZIP FILE....
					 */
					ZIP_SD = false;
					zipPathSD = null;
					CURRENT_ITEM=2;
					resetPager();
				}else
				
				/*
				 * TRY BLOCK IS USED BECAUSE WHEN USER NAVIGATES TO TOP DIRECTORY IN ZIP FILE
				 * CAUSES STRINGINDEXOUTOFBOUND EXCEPTION....
				 * AND FAILS TO GENERATE THE "/" PATH FOR ZIP FILE...
				 * SO MANUALLY GENERATING THE "/" PATH.... 
				 */
				try{
					zipPathSD = zipPathSD.substring(0, zipPathSD.lastIndexOf("/"));
					setZipAdapter();
				}catch(Exception e){
					zipPathSD = "/";
					setZipAdapter();
				}
				
			}else if(RAR_SD&&CURRENT_ITEM==2){
				//BACK KEY HANDLING FOR RAR FILES....
				SDManager.nStack.pop();
				if(rarPathSD.equalsIgnoreCase("/")){
					RAR_SD = false;
					rarPathSD = null;
					CURRENT_ITEM=2;
					resetPager();
				}else{
					try{//NO NEED TO WORRY ABOUT THE EXCEPTION....
						rarPathSD = rarPathSD.substring(0, rarPathSD.lastIndexOf("\\"));
						setRarAdapter();
					}catch(Exception e){
						rarPathSD = "/";
						setRarAdapter();
					}
				}
			}else if(TAR_SD&&CURRENT_ITEM==2){//TAR FILE HANDLING ON BACK KEY PRESS...
				SDManager.nStack.pop();
				if(tarPathSD.equals("/")){
					TAR_SD = false;
					tarPathSD = null;
					CURRENT_ITEM=2;
					resetPager();
				}else{
					try{
						tarPathSD = tarPathSD.substring(0, tarPathSD.lastIndexOf("/"));
						setTarAdapter();
					}catch(Exception e){
						tarPathSD = "/";
						setTarAdapter();
					}
				}
			}else if (CURRENT_ITEM == 2 && (SDManager.nStack.size() >= 2)) {
				SDManager.nStack.pop();
				sdItemsList = sdManager.getPreviousList();
				if(SDAdapter.MULTI_SELECT)
					SDAdapter.thumbselection = new boolean[sdItemsList.size()];
				CURRENT_ITEM = 2;
				resetPager();
				file2 = new Item(new File(SDManager.getCurrentDirectory()), null, null, null);
			} else if (CURRENT_ITEM == 2 && SDManager.nStack.size() < 2) {
				/**
				 * CHECKS WHETHER THE CURRENT PREF IS 2 IF IT IS FOUND 3 THEN IT
				 * TAKES ACTION TO EXIT ON CURRENT ITEM = 2
				 */
				if (CURRENT_PREF_ITEM == 2) {
					if (!mUseBackKey) {
						Toast.makeText(mContext,R.string.pressbackagain, Toast.LENGTH_SHORT).show();
						mUseBackKey = true;
					} else if (mUseBackKey) {
						mUseBackKey = false;
						FileQuest.this.finish();
						//android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
				/**
				 * IF CURRENT PREF IS NOT 2 THEN IT DIRECTS VIEW PAGER TO STORED
				 * VALUE
				 */
				else
					mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
			}

		}
		return false;
	}

	/**
	 * Creates the animation set for next ViewFlippers when loaded.
	 * 
	 * @return a customized animation for mViewFlippers
	 */
	private static Animation nextAnim() {
		Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				+1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		anim.setDuration(50);
		anim.setInterpolator(new AccelerateInterpolator());
		return anim;
	}

	/**
	 * Creates the animation set for previous ViewFlippers when loaded.
	 * 
	 * @return a customized animation for mViewFlippers
	 */
	private static Animation prevAnim() {
		Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				-1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		anim.setDuration(50);
		anim.setInterpolator(new AccelerateInterpolator());
		return anim;
	}

	/**
	 * 
	 */
	@Override
	public void onItemClick(QuickAction source, int pos, int actionId) {
		CURRENT_ITEM = mViewPager.getCurrentItem();
		switch (actionId) {
		case 1:
			// IF CURRENT ITEM IS 0 AND CATEGORIES ARE DIAPLAYED
			// THEN USER SELECTS FILTER BUTTON,IT SHOWS 7 OPTION
			// FIRST OPTION IS CASE 1
			// SEARCH_FLAG = true;
			load_FIle_Gallery(0);
			search();
			mVFlipper.showNext();
			mVFlipper.setAnimation(nextAnim());
			break;
		case 2:
			// IF CURRENT ITEM IS 0 AND CATEGORIES ARE DIAPLAYED
			// THEN USER SELECTS FILTER BUTTON,IT SHOWS 7 OPTION
			// SECOND OPTION IS CASE 2
			// SEARCH_FLAG = true;
			load_FIle_Gallery(1);
			search();
			mVFlipper.showNext();
			mVFlipper.setAnimation(nextAnim());
			break;
		case 3:
			// IF CURRENT ITEM IS 0 AND CATEGORIES ARE DIAPLAYED
			// THEN USER SELECTS FILTER BUTTON,IT SHOWS 7 OPTION
			// THIRT OPTION IS CASE 3
			// SEARCH_FLAG = true;
			load_FIle_Gallery(2);
			search();
			mVFlipper.showNext();
			mVFlipper.setAnimation(nextAnim());
			break;

		case 4:
			// IF CURRENT ITEM IS 0 AND CATEGORIES ARE DIAPLAYED
			// THEN USER SELECTS FILTER BUTTON,IT SHOWS 7 OPTION
			// Fourth OPTION IS CASE 4
			// SEARCH_FLAG = true;
			load_FIle_Gallery(3);
			search();
			mVFlipper.showNext();
			mVFlipper.setAnimation(nextAnim());
			break;

		case 5:
			// IF CURRENT ITEM IS 0 AND CATEGORIES ARE DIAPLAYED
			// THEN USER SELECTS FILTER BUTTON,IT SHOWS 7 OPTION
			// FIFTH OPTION IS CASE 5
			// SEARCH_FLAG = true;
			load_FIle_Gallery(4);
			search();
			mVFlipper.showNext();
			mVFlipper.setAnimation(nextAnim());
			break;

		case 6:
			// IF CURRENT ITEM IS 0 AND CATEGORIES ARE DIAPLAYED
			// THEN USER SELECTS FILTER BUTTON,IT SHOWS 7 OPTION
			// SIXTH OPTION IS CASE 6
			// SEARCH_FLAG = true;
			load_FIle_Gallery(5);
			search();
			mVFlipper.showNext();
			mVFlipper.setAnimation(nextAnim());
			break;

		case 7:
			// IF CURRENT ITEM IS 0 AND CATEGORIES ARE DIAPLAYED
			// THEN USER SELECTS FILTER BUTTON,IT SHOWS 7 OPTION
			// SEVENTH OPTION IS CASE 7
			// SEARCH_FLAG = true;
			load_FIle_Gallery(6);
			search();
			mVFlipper.showNext();
			mVFlipper.setAnimation(nextAnim());
			break;

		case 8:

			// IF CURRENT ITEM IS 0 AND USER SELECTS JUMP TO BUTTON
			// THEN SEVEN LOCATIONS ARE SHOWN
			// FIRST LOCATION IS CASE 8
			load_FIle_Gallery(0);
			break;

		case 9:
			// IF CURRENT ITEM IS 0 AND USER SELECTS JUMP TO BUTTON
			// THEN SEVEN LOCATIONS ARE SHOWN
			// SECOND LOCATION IS CASE 9
			// loadMediaList(pos=1);
			// elementInFocus = true;
			// media.setAdapter(new FileGalleryAdapter(mContext,
			// R.layout.row_list_1, mediaFileList));
			load_FIle_Gallery(1);
			break;

		case 10:
			// IF CURRENT ITEM IS 0 AND USER SELECTS JUMP TO BUTTON
			// THEN SEVEN LOCATIONS ARE SHOWN
			// THIRD LOCATION IS CASE 10
			load_FIle_Gallery(2);
			break;

		case 11:
			// IF CURRENT ITEM IS 0 AND USER SELECTS JUMP TO BUTTON
			// THEN SEVEN LOCATIONS ARE SHOWN
			// FOURTH LOCATION IS CASE 11
			load_FIle_Gallery(3);
			break;

		case 12:
			// IF CURRENT ITEM IS 0 AND USER SELECTS JUMP TO BUTTON
			// THEN SEVEN LOCATIONS ARE SHOWN
			// FIFTH LOCATION IS CASE 12
			load_FIle_Gallery(4);
			break;

		case 13:
			// IF CURRENT ITEM IS 0 AND USER SELECTS JUMP TO BUTTON
			// THEN SEVEN LOCATIONS ARE SHOWN
			// SIXTH LOCATION IS CASE 13
			load_FIle_Gallery(5);
			break;

		case 14:
			// IF CURRENT ITEM IS 0 AND USER SELECTS JUMP TO BUTTON
			// THEN SEVEN LOCATIONS ARE SHOWN
			// SEVENTH LOCATION IS CASE 14
			load_FIle_Gallery(6);
			break;		
		}
	}

	/**
	 * Returns the path only (without file name).
	 * 
	 * @param file
	 * @return
	 */
	public static File getPathWithoutFilename(File file) {
		if (file != null) {
			if (file.isDirectory()) {
				// no file to be split off. Return everything
				return file;
			} else {
				String filename = file.getName();
				String filepath = file.getAbsolutePath();

				// Construct path without file name.
				String pathwithoutname = filepath.substring(0,filepath.length() - filename.length());
				if (pathwithoutname.endsWith("/")) {
					pathwithoutname = pathwithoutname.substring(0,pathwithoutname.length() - 1);
				}
				return new File(pathwithoutname);
			}
		}
		return null;
	}

	
	
	/**
	 * THIS FUNCTION IS TO PERFORM ORDINARY FILE SEARCH.
	 * THIS FUNCTION PERFORMS SEARCH IN 2 PANELS... 
	 */
	private void search(){
		// TODO Auto-generated method stub
				try{
					if(searchList == null)
						searchList = new ArrayList<Item>();
					else
						searchList.clear();
					LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
					a.setVisibility(View.GONE);
					// Search Flipper is loaded
					mVFlipper.setAnimation(nextAnim());
					mVFlipper.showNext();
					mVFlipper.showNext();
					SEARCH_FLAG = true;
					// PREVIOUS COMMANDS ARE OVERWRITTEN
					COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = CREATE_FILE = false;
					editBox.setTextColor(Color.WHITE);
					editBox.setText(null);
					editBox.setHint(R.string.nametofilterout);
					editBox.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
							// TODO Auto-generated method stub
							searchList.clear();
						}
						
						@Override
						public void afterTextChanged(final Editable ed) {
							// TODO Auto-generated method stub
							new AsyncTask<Void, Void, Void>() {
								
								@Override
								protected void onPostExecute(Void result) {
									// TODO Auto-generated method stub
									
									try{
										if(CURRENT_ITEM == 2)
											root.setAdapter(new SDAdapter(mContext,searchList));
										else if(CURRENT_ITEM == 1)
											simple.setAdapter(new RootAdapter(mContext,searchList));
										else if(CURRENT_ITEM == 0)
											LIST_VIEW_3D.setAdapter(new FileGalleryAdapter(mContext, searchList));
									}catch(Exception e){
										
									}
								}

								@Override
								protected void onPreExecute() {
									// TODO Auto-generated method stub
									try{
										if(CURRENT_ITEM==2)
											root.setAdapter(null);
										else if(CURRENT_ITEM==1)
											simple.setAdapter(null);
										else if(CURRENT_ITEM == 0)
											LIST_VIEW_3D.setAdapter(null);
									}catch(Exception e){
										
									}
								}

								@Override
								protected Void doInBackground(Void... arg0) {
									// TODO Auto-generated method stub
									if(CURRENT_ITEM == 2){
										String text = ed.toString().toLowerCase();
										int len = sdItemsList.size();
										for(int i=0;i<len;++i){
											if(sdItemsList.get(i).getName().toLowerCase().contains(text))
												searchList.add(sdItemsList.get(i));
										}
									}else if(CURRENT_ITEM == 1){
										String text = ed.toString().toLowerCase();
										int len = rootItemList.size();
										for(int i=0;i<len;++i){
											if(rootItemList.get(i).getName().toLowerCase().contains(text))
												searchList.add(rootItemList.get(i));
										}
									}else if(CURRENT_ITEM == 0){
										String text = ed.toString().toLowerCase();
										int len = mediaFileList.size();
										for(int i=0;i<len;++i){
											try{
												if(mediaFileList.get(i).getName().toLowerCase().contains(text))
													searchList.add(mediaFileList.get(i));
											}catch(Exception e){
												
											}
										}
									}
									return null;
								}
							}.execute();
						}
					});
				}catch(Exception e){
					
				}
	}
	
	

	/**
	 * THIS FUNCTION IS USED AT TWO PLACE IN THIS FILE THIS FUNCTION DISPLAYS
	 * THE QUICKACTION VIEW SHOWING THE DEFAULT APPS SET BY USER FOR CRETAIN
	 * FILE TYPES
	 */
	public void showDefaultApps(View v) {
		/**
		 * 
		 */
		PackageInfo in;
		PackageManager man = getPackageManager();
		QuickAction q = new QuickAction(mContext);
		final SharedPreferences p = getSharedPreferences("DEFAULT_APPS",MODE_PRIVATE);
		final SharedPreferences.Editor ed = p.edit();
		final String MUSIC = p.getString("MUSIC", null);
		ActionItem it = new ActionItem();
		try {
			in = man.getPackageInfo(MUSIC, 0);
			it.setTitle(getString(R.string.music) + " - "+ in.applicationInfo.loadLabel(man));
			it.setIcon(in.applicationInfo.loadIcon(man));
			it.setActionId(500);
		} catch (NameNotFoundException e) {
			it = new ActionItem(600, getString(R.string.nomusicapp),getResources().getDrawable(R.drawable.ic_launcher_music));
			ed.putString("MUSIC", null);
			ed.commit();
		}
		q.addActionItem(it);

		it = new ActionItem();
		final String IMAGE = p.getString("IMAGE", null);
		try {
			in = man.getPackageInfo(IMAGE, 0);
			it.setTitle(getString(R.string.image) + " - "+ in.applicationInfo.loadLabel(man));
			it.setIcon(in.applicationInfo.loadIcon(man));
			it.setActionId(700);
		} catch (NameNotFoundException e) {
			ed.putString("IMAGE", null);
			ed.commit();
			it = new ActionItem(800, getString(R.string.noimgapp),getResources().getDrawable(R.drawable.ic_launcher_images));

		}
		q.addActionItem(it);

		it = new ActionItem();
		final String VIDEO = p.getString("VIDEO", null);
		try {
			in = man.getPackageInfo(VIDEO, 0);
			it.setTitle(getString(R.string.vids) + " - "+ in.applicationInfo.loadLabel(man));
			it.setIcon(in.applicationInfo.loadIcon(man));
			it.setActionId(900);
		} catch (NameNotFoundException e) {
			ed.putString("VIDEO", null);
			ed.commit();
			it = new ActionItem(1000, getString(R.string.novidapp),getResources().getDrawable(R.drawable.ic_launcher_video));

		}
		q.addActionItem(it);

		it = new ActionItem();
		final String ZIP = p.getString("ZIP", null);
		try {
			in = man.getPackageInfo(ZIP, 0);
			it.setTitle(getString(R.string.zip) + " - "	+ in.applicationInfo.loadLabel(man));
			it.setIcon(in.applicationInfo.loadIcon(man));
			it.setActionId(1100);
		} catch (NameNotFoundException e) {
			ed.putString("ZIP", null);
			ed.commit();
			it = new ActionItem(1200, getString(R.string.nozipapp),getResources().getDrawable(R.drawable.ic_launcher_zip_it));

		}
		q.addActionItem(it);

		it = new ActionItem();
		final String PDF = p.getString("PDF", null);
		try {
			in = man.getPackageInfo(PDF, 0);
			it.setTitle(getString(R.string.pdf) + " - "	+ in.applicationInfo.loadLabel(man));
			it.setIcon(in.applicationInfo.loadIcon(man));
			it.setActionId(1300);
		} catch (NameNotFoundException e) {
			ed.putString("PDF", null);
			ed.commit();
			it = new ActionItem(1400, getString(R.string.nopdfapp),getResources().getDrawable(R.drawable.ic_launcher_adobe));

		}
		q.addActionItem(it);

		it = new ActionItem();
		final String TEXT = p.getString("TEXT", null);
		try {
			in = man.getPackageInfo(TEXT, 0);
			it.setTitle(getString(R.string.docs) + " - "+in.applicationInfo.loadLabel(man));
			it.setIcon(in.applicationInfo.loadIcon(man));
			it.setActionId(1500);
		} catch (NameNotFoundException e) {
			ed.putString("TEXT", null);
			ed.commit();
			it = new ActionItem(1600, getString(R.string.nodocapp),getResources().getDrawable(R.drawable.ic_launcher_text));

		}
		q.addActionItem(it);

		it = new ActionItem();
		final String RAR = p.getString("RAR", null);
		try {
			in = man.getPackageInfo(RAR, 0);
			it.setTitle(getString(R.string.rar) + " - "	+ in.applicationInfo.loadLabel(man));
			it.setIcon(in.applicationInfo.loadIcon(man));
			it.setActionId(1700);
		} catch (NameNotFoundException e) {
			ed.putString("RAR", null);
			ed.commit();
			it = new ActionItem(1800, getString(R.string.norarapp),getResources().getDrawable(R.drawable.ic_launcher_rar));

		}
		q.addActionItem(it);
		it = new ActionItem(1900, getString(R.string.cleardefaults),getResources().getDrawable(R.drawable.ic_launcher_delete));
		q.addActionItem(it);
		q.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				// TODO Auto-generated method stub
				switch (actionId) {
				case 500:
					/**
					 * MUSIC APP HAS BEEN SET TO DEFAULT THEN DISPLAY ITS INFO
					 * AND GIVE OPTION TO CLEAR DEFAULT
					 */
					new SelectedApp(mContext, size.x*8/9, MODE_PRIVATE,	MUSIC, "MUSIC");
					break;
				case 600:
					new SelectApp(mContext, size.x*8/9, "MUSIC",MODE_PRIVATE);
					break;
				case 700:
					new SelectedApp(mContext, size.x*8/9, MODE_PRIVATE,	IMAGE, "IMAGE");
					break;
				case 800:
					new SelectApp(mContext, size.x*8/9, "IMAGE",MODE_PRIVATE);
					break;
				case 900:
					new SelectedApp(mContext, size.x*8/9, MODE_PRIVATE,	VIDEO, "VIDEO");
					break;
				case 1000:
					new SelectApp(mContext, size.x*8/9, "VIDEO",MODE_PRIVATE);
					break;
				case 1100:
					new SelectedApp(mContext, size.x*8/9, MODE_PRIVATE,	ZIP, "ZIP");
					break;
				case 1200:
					new SelectApp(mContext, size.x*8/9, "ZIP", MODE_PRIVATE);
					break;
				case 1300:
					new SelectedApp(mContext, size.x*8/9, MODE_PRIVATE,	PDF, "PDF");
					break;
				case 1400:
					new SelectApp(mContext, size.x*8/9, "PDF", MODE_PRIVATE);
					break;
				case 1500:
					new SelectedApp(mContext, size.x*8/9, MODE_PRIVATE,	TEXT, "TEXT");
					break;
				case 1600:
					new SelectApp(mContext, size.x*8/9, "TEXT",MODE_PRIVATE);
					break;
				case 1700:
					new SelectedApp(mContext, size.x*8/9, MODE_PRIVATE,RAR, "RAR");
					break;
				case 1800:
					new SelectApp(mContext, size.x*8/9, "RAR", MODE_PRIVATE);
					break;
				case 1900:
					SharedPreferences.Editor ed = p.edit();
					ed.clear();
					ed.commit();
					Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		q.show(v);
	}

	/**
	 * THIS FUNCTION RESETS THE HORIZONTAL SCROLL VIEW TO START AND DISPLAYS THE
	 * APPROPRIATE MESSAGE WHEN MULTI SELECT IS DISABLED
	 * 
	 * @param str
	 */
	public void MultiModeDisabled(String str) {
		HorizontalScrollView v = (HorizontalScrollView) findViewById(R.id.hscrollView);
		if (CURRENT_ITEM == 3)
			v = (HorizontalScrollView) findViewById(R.id.hscrollView2);
		LinearLayout btn = (LinearLayout ) findViewById(R.id.bottom_multi);
		QuickAction action = new QuickAction(mContext);
		ActionItem item = new ActionItem(1, str);
		action.addActionItem(item);
		v.scrollTo(0, 0);
		action.show(btn);
	}

	/**
	 * 
	 * @param str
	 */
	public static void showToast(String str) {
		Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @param ITEM
	 * @param action
	 * @param str
	 */
	public void OPERATION_ON_MULTI_SELECT_FILES(int ITEM, int action, String str) {
		if (ITEM == 0 && FileGalleryAdapter.MULTI_SELECT) {
			if (action == 4 && FileGalleryAdapter.C != 0) {// DELETE THE
															// MULTIPLE FILES IF
															// ACTIONID = 4
				new DeleteFiles(mContext, size.x*8/9, FileGalleryAdapter.MULTI_FILES,null);
			} else if (action == 5 && FileGalleryAdapter.C != 0) {
				MULTIPLE_COPY_GALLERY = true;
				MULTIPLE_COPY = MULTIPLE_CUT = COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = MULTIPLE_CUT_GALLERY = SEARCH_FLAG = false;
				COPY_FILES = FileGalleryAdapter.MULTI_FILES;
				showToast(FileGalleryAdapter.C + " "+ getString(R.string.selectedforcopy));
			} else if (action == 2 && FileGalleryAdapter.C != 0) {
				MULTIPLE_CUT_GALLERY = true;
				COPY_FILES = FileGalleryAdapter.MULTI_FILES;
				MULTIPLE_COPY = MULTIPLE_CUT = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY_GALLERY = RENAME_COMMAND = SEARCH_FLAG = false;
				showToast(FileGalleryAdapter.C + " "+ getString(R.string.selectedformove));
			} else if (action == 3 && FileGalleryAdapter.C != 0) {
				COPY_FILES = FileGalleryAdapter.MULTI_FILES;
				new CreateZip(mContext, size.x * 8 / 9, COPY_FILES);
			} else
				Toast.makeText(mContext, R.string.firstselectsomefiles,
						Toast.LENGTH_SHORT).show();
		} else if (ITEM == 1 && RootAdapter.MULTI_SELECT) {
			if (action == 4 && RootAdapter.C != 0) {// DELETE THE MULTIPLE
														// FILES IF ACTIONID = 4
				new DeleteFiles(mContext, size.x*8/9, RootAdapter.MULTI_FILES,null);
			} else if (action == 5 && RootAdapter.C > 0) {
				MULTIPLE_COPY = true;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = RootAdapter.MULTI_FILES;
				showToast(RootAdapter.C + " "+ getString(R.string.selectedforcopy));
			} else if (action == 2 && RootAdapter.C > 0) {
				MULTIPLE_CUT = true;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = RootAdapter.MULTI_FILES;
				showToast(RootAdapter.C + " "+ getString(R.string.selectedformove));
			} else if (action == 3 && RootAdapter.C > 0) {
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = RootAdapter.MULTI_FILES;
				/*
				 * BUG HAS TO BE FIXED WHILE ZIPPING FILES UNDER ROOT DIRECTORY
				 * FOR NOW SHOWING TOAST MESSAGE SHOWING BCS OF INTERNAL ERROR
				 * COPYING FAILED
				 */
				Toast.makeText(mContext, R.string.serviceUnavaila,Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(mContext, R.string.firstselectsomefiles,Toast.LENGTH_SHORT).show();

		} else if (ITEM == 2 && SDAdapter.MULTI_SELECT) {
			if (action == 4 && SDAdapter.C != 0) {
				// DELETE THE MULTIPLE FILES IF ACTIONID = 4
				new DeleteFiles(mContext, size.x*8/9, SDAdapter.MULTI_FILES,null);
			} else if (action == 5 && SDAdapter.C != 0) {
				MULTIPLE_COPY = true;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = SDAdapter.MULTI_FILES;
				showToast(SDAdapter.C + " "+ getString(R.string.selectedforcopy));
			} else if (action == 2 && SDAdapter.C != 0) {
				MULTIPLE_CUT = true;
				COPY_FILES = SDAdapter.MULTI_FILES;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				showToast(SDAdapter.C + " "+ getString(R.string.selectedformove));
			} else if (action == 3 && SDAdapter.C != 0) {
				COPY_FILES = SDAdapter.MULTI_FILES;
				MULTIPLE_COPY = MULTIPLE_CUT = COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				new CreateZip(mContext, size.x * 8 / 9, COPY_FILES);
			} else
				Toast.makeText(mContext, R.string.firstselectsomefiles,Toast.LENGTH_SHORT).show();
		} else
			MultiModeDisabled(str);
	}

	/**
	 * FUNCTION MAKE 3d LIST VIEW VISIBLE OR GONE AS PER REQUIREMENT
	 * 
	 * @param mode
	 * @param con
	 */
	public static void load_FIle_Gallery(final int mode) {
		final Dialog pDialog = new Dialog(mContext, R.style.custom_dialog_theme);
		final Handler handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					try {
						pDialog.setContentView(R.layout.p_dialog);
						pDialog.setCancelable(false);
						pDialog.getWindow().getAttributes().width = size.x*8/9;
						WebView web = (WebView)pDialog.findViewById(R.id.p_Web_View);
						web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
						web.setEnabled(false);
						pDialog.show();
					} catch (InflateException e) {
						error = true;
						Toast.makeText(mContext, R.string.errorinloadingfiles,Toast.LENGTH_SHORT).show();
					}
					break;
				case 1:
					if (pDialog != null)
						if (pDialog.isShowing())
							pDialog.dismiss();

					if (mediaFileList.size() > 0) {
						FILE_GALLEY.setVisibility(View.GONE);
						LIST_VIEW_3D.setVisibility(View.VISIBLE);
						element = new FileGalleryAdapter(mContext, mediaFileList);
						LIST_VIEW_3D.setAdapter(element);
						LIST_VIEW_3D.setEnabled(true);
						//LIST_VIEW_3D.setDynamics(new SimpleDynamics(0.7f, 0.6f));
						if (!elementInFocus) {
							mFlipperBottom.showPrevious();
							mFlipperBottom.setAnimation(prevAnim());
							elementInFocus = true;
						}
						if (SEARCH_FLAG) {
							mVFlipper.showPrevious();
							mVFlipper.setAnimation(nextAnim());
						}
					} else {
						LIST_VIEW_3D.setVisibility(View.GONE);
						FILE_GALLEY.setVisibility(View.VISIBLE);
						Toast.makeText(mContext, R.string.empty,Toast.LENGTH_SHORT).show();
						if (elementInFocus) {
							mFlipperBottom.showNext();
							mFlipperBottom.setAnimation(nextAnim());
						}
						elementInFocus = false;
					}
					break;
				}
			}
		};
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handle.sendEmptyMessage(0);
				while (!Utils.loaded) {
					// STOPPING HERE WHILE FILES ARE BEING LOADED IN
					// BACKGROUND....
				}
				if (mode == 0)
					mediaFileList = Utils.music;
				else if (mode == 1)
					mediaFileList = Utils.apps;
				else if (mode == 2)
					mediaFileList = Utils.doc;
				else if (mode == 3)
					mediaFileList = Utils.img;
				else if (mode == 4)
					mediaFileList = Utils.vids;
				else if (mode == 5)
					mediaFileList = Utils.zip;
				else if (mode == 6)
					mediaFileList = Utils.mis;
				handle.sendEmptyMessage(1);
			}
		});
		thread.start();
	}

	/**
	 * THIS CLASS LOADS THE SDCARD INFO AND REFRESHES THE UI OF BOTTOM AND
	 * DISPLAYSTHE CURRENT SD CARD STATUS
	 * 
	 * @author ANURAG
	 *
	 */
	class load {
		Handler handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					sd.setMax((int) to);
					sd.setProgress((int) (to - av));
					avail.setText(sav);
					total.setText(sto);
					break;
				}
			}
		};

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				av = new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getFreeSpace();
				to = new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getTotalSpace();
				if (av > Constants.GB)
					sav = String.format(getString(R.string.availgb),(double) av / (Constants.GB));

				else if (av > Constants.MB)
					sav = String.format(getString(R.string.availmb),(double) av / (Constants.MB));

				else if (av > 1024)
					sav = String.format(getString(R.string.availkb),(double) av / (1024));
				else
					sav = String.format(getString(R.string.availbytes),	(double) av);

				if (to > Constants.GB)
					sto = String.format(getString(R.string.totgb), (double) to/ (Constants.GB));

				else if (to > Constants.MB)
					sto = String.format(getString(R.string.totmb), (double) to/ (Constants.MB));

				else if (to > 1024)
					sto = String.format(getString(R.string.totkb),(double) to / (1024));
				else
					sto = String.format(getString(R.string.totbytes),(double) to);
				
				/**
				 * DECREASING THE VALUE OF LONG SO THAT IT FITS IN INTEGER RANGE...
				 */
				if(to>Constants.GB){
					while(to>Constants.GB){
						to = to/10 ;
						av=av/10;
					}	
				}
				handle.sendEmptyMessage(1);
			}
		});

		public void execute() {
			thread.start();
		}
	}

	/**
	 * THIS FUNCTION HANDLES THE PASTING OF FILES ANYWHERE IN ANY MODE...... IF
	 * FILE HAS TO BE CUT THE LAST ARGUMENT IN MULTIPLECOPYDIALOG IS TRUE longC
	 * is used when a folder is longclicked and then try to paste in that folder
	 * 
	 * @param longC
	 */
	private static void pasteCommand(boolean longC) {
		if ((CUT_COMMAND || COPY_COMMAND)) {
			/**
			 * THIS CONDITION HANDLES THE PASTING OF SINGLE SELECTED FILES ..
			 */
			if (COPY_COMMAND) {
				if (CURRENT_ITEM == 1) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file.getFile().getAbsolutePath(), false);
				} else if (CURRENT_ITEM == 2) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file2.getFile().getAbsolutePath(), false);
				}
			} else if (CUT_COMMAND) {
				if (CURRENT_ITEM == 1) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file.getFile().getAbsolutePath(), true);
				} else if (CURRENT_ITEM == 2) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file2.getFile().getAbsolutePath(), true);
				}
			}
			COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_CUT = CREATE_FILE = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = false;
		} else if (MULTIPLE_COPY || MULTIPLE_CUT) {
			/*
			 * THIS CONDITION HANDLES PASTING OF FILES FOR ROOT AND SD CARD
			 * PANEL THIS DOESNOT HANDLE FOR FILES PASTING FROM FILE GALLERY
			 */
			String dest;
			if (CURRENT_ITEM == 2) {
				if (longC)
					dest = file2.getFile().getAbsolutePath();
				else
					dest = SDManager.getCurrentDirectory();
				if (MULTIPLE_COPY)
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, dest, false);
				else if (MULTIPLE_CUT)
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, dest, true);

			} else if (CURRENT_ITEM == 1) {
				if (longC)
					dest = file.getFile().getAbsolutePath();
				else
					dest = RootManager.getCurrentDirectory();
				if (MULTIPLE_COPY)
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, dest, false);
				else if (MULTIPLE_CUT)
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, dest, true);

			}
			COPY_COMMAND = CUT_COMMAND = CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = false;
		} else if (MULTIPLE_COPY_GALLERY || MULTIPLE_CUT_GALLERY) {
			/*
			 * THIS CONDITION HANDLES PASTING OF FILES FROM GALLERY
			 */
			if (CURRENT_ITEM == 2) {
				if (MULTIPLE_COPY_GALLERY) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, SDManager.getCurrentDirectory(),	false);
				} else if (MULTIPLE_CUT_GALLERY) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, SDManager.getCurrentDirectory(),	true);
				}
			} else if (CURRENT_ITEM == 1) {
				if (MULTIPLE_COPY_GALLERY) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, RootManager.getCurrentDirectory(),false);
				} else if (MULTIPLE_CUT_GALLERY) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, RootManager.getCurrentDirectory(),	true);
				}
			}
			COPY_COMMAND = CUT_COMMAND = CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = false;
		} else {
			Toast.makeText(mContext, R.string.selectFiles, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * THIS FUNCTION REGISTERS THE RECEIVER FOR CUSTOM ACTION USED IN THE APP...
	 */
	private void REGISTER_RECEIVER() {
		// TODO Auto-generated method stub
		RECEIVER = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent it) {
				// TODO Auto-generated method stub
				String ACTION = it.getAction();
				if (ACTION.equalsIgnoreCase("FQ_BACKUP")|| ACTION.equals(Intent.ACTION_UNINSTALL_PACKAGE)){
					setAdapter(CURRENT_ITEM);
				}else if (ACTION.equalsIgnoreCase("FQ_DELETE")) {
					// setAdapter(CURRENT_ITEM);
					if(CURRENT_ITEM == 1){
						if(!RootAdapter.MULTI_SELECT){
							//single item has to removed....
							rootItemList.remove(fPos);
							simple.setAdapter(rootAdapter);
						}else{
							//multi select option was enabled and delete operation
							//was performed...
							int l = RootAdapter.MULTI_FILES.size();
							for(int i = 0 ; i < l ;++i){
								if(RootAdapter.MULTI_FILES.get(i)!=null){
									//SDAdapter.MULTI_FILES.remove(i);
									rootItemList.remove(i); 
								}
							}
							RootAdapter.thumbselection = new boolean[rootItemList.size()];
							simple.setAdapter(rootAdapter);
						}
					}else if(CURRENT_ITEM == 2){
						if(!SDAdapter.MULTI_SELECT){
							//single item has to removed....
							sdItemsList.remove(fPos);
							root.setAdapter(sdAdapter);
						}else{
							//multi select option was enabled and delete operation
							//was performed...
							int l = SDAdapter.MULTI_FILES.size();
							for(int i = 0 ; i < l ;++i){
								if(SDAdapter.MULTI_FILES.get(i)!=null){
									//SDAdapter.MULTI_FILES.remove(i);
									sdItemsList.remove(i); 
								}
							}
							SDAdapter.thumbselection = new boolean[sdItemsList.size()];
							root.setAdapter(sdAdapter);
						}
					}
					else if(CURRENT_ITEM == 0){
						if(!FileGalleryAdapter.MULTI_SELECT){
							//deleting single item from UI...
							mediaFileList.remove(fPos);
							LIST_VIEW_3D.setAdapter(element);
						}else{
							//multi select option was enabled and delete operation
							//was performed...
							int l = FileGalleryAdapter.MULTI_FILES.size();
							for(int i = 0 ; i < l ;++i){
								if(FileGalleryAdapter.MULTI_FILES.get(i)!=null){
									//SDAdapter.MULTI_FILES.remove(i);
									mediaFileList.remove(i); 
								}
							}
							FileGalleryAdapter.thumbselection = new boolean[mediaFileList.size()];
							LIST_VIEW_3D.setAdapter(element);
						}
					}
				} else if (ACTION.equalsIgnoreCase("FQ_FLASHZIP")) {
					// FLASHABLE ZIP DIALOG IS FIRED FROM HERE
					 new CreateZipApps(mContext, size.x*8/9, nList);
				} else if (ACTION.equalsIgnoreCase("FQ_DROPBOX_STARTLINK")) {
					// LINK A USER....
					Toast.makeText(mContext, R.string.linkanaccount,Toast.LENGTH_SHORT).show();
					NEW_OPTIONS(indicator);
				} else if (ACTION.equalsIgnoreCase("FQ_G_OPEN")) {
					Toast.makeText(mContext, R.string.openingfile,Toast.LENGTH_SHORT).show();
					if (CURRENT_ITEM == 0 || CURRENT_ITEM == 3)
						CURRENT_ITEM = 2;
					String value = it.getStringExtra("gesture_path");
					if (new File(value).exists()) {
						
						Item tempItem = new Item(new File(value), null, null, null);						
						if (new File(value).isFile()) {
							mViewPager.setCurrentItem(CURRENT_ITEM);
							if(!tempItem.isLocked())
								new OpenFileDialog(mContext, Uri.parse(value),size.x*8/9);
							else
								new MasterPassword(mContext, size.x*8/9, tempItem,preferences, Constants.MODES.G_OPEN);
						} else {
							if (CURRENT_ITEM == 1) {
								if(!tempItem.isLocked()){
									RootManager.nStack.push(value);
									setAdapter(CURRENT_ITEM);
								}else
									new MasterPassword(mContext, size.x*8/9, tempItem, preferences, Constants.MODES.G_OPEN);
								
							} else if (CURRENT_ITEM == 2){
								if(!tempItem.isLocked()){
									SDManager.nStack.push(value);
									setAdapter(2);
								}else
									new MasterPassword(mContext, size.x*8/9, tempItem, preferences, Constants.MODES.G_OPEN);
							}	
							
						}
					} else
						Toast.makeText(mContext, R.string.filedoesnotexists,Toast.LENGTH_SHORT).show();

				}else if(ACTION.equalsIgnoreCase("FQ_ZIP_OPEN")){
					//CHECKING WHETHER ANY ARCHIVE IS ALREADY OPENED OR NOT...
					//IF OPENED THEN CLOSING THE ARCHIVE AND OPENING THE NEW ARCHIVE REQUEST....
					cleanStack(it.getStringExtra("open_path"));
					
					if(CURRENT_ITEM==0||CURRENT_ITEM==2){
						ZIP_SD = true;
						SDManager.nStack.push("/ -> Zip");
					}	
					else{
						ZIP_ROOT = true;
						RootManager.nStack.push("/ -> Zip");
					}	
					setZipAdapter();
				}else if(ACTION.equalsIgnoreCase("FQ_EXTRACT_PATH")){
					String path = it.getStringExtra("extract_path");
					extractTo(path);
				}else if(ACTION.equalsIgnoreCase("FQ_RAR_OPEN")){
					//CHECKING WHETHER ANY ARCHIVE IS ALREADY OPENED OR NOT...
					//IF OPENED THEN CLOSING THE ARCHIVE AND OPENING THE NEW ARCHIVE REQUEST....
					cleanStack(it.getStringExtra("open_path"));
					
					if(CURRENT_ITEM==0||CURRENT_ITEM==2){
						RAR_SD = true;
						SDManager.nStack.push("/ -> Rar");
					}else{
						RAR_ROOT = true;
						RootManager.nStack.push("/ -> Rar");
					}
					setRarAdapter();
				}else if(ACTION.equalsIgnoreCase("FQ_TAR_OPEN")){
					//CHECKING WHETHER ANY ARCHIVE IS ALREADY OPENED OR NOT...
					//IF OPENED THEN CLOSING THE ARCHIVE AND OPENING THE NEW ARCHIVE REQUEST....
					cleanStack(it.getStringExtra("open_path"));
					
					if(CURRENT_ITEM==0||CURRENT_ITEM==2){
						TAR_SD = true;
						SDManager.nStack.push("/ -> Tar");
					}else if(CURRENT_ITEM==1){
						TAR_ROOT = true;
						RootManager.nStack.push("/ -> Tar");
					}
					setTarAdapter();
				}else if(ACTION.equalsIgnoreCase("FQ_7Z_OPEN")){
					try {
						root.setAdapter(new SZipAdapter(mContext, new SZipManager(file2.getFile(), "/", mContext).generateList()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(ACTION.equals("FQ_DROPBOX_OPEN_FOLDER")){
					//action to open drop box location in panel.....
					if(CURRENT_ITEM == 2){
						root.setAdapter(new DBoxAdapter(mContext, DBoxManager.dListSimple));
					}
				}else if(ACTION.equalsIgnoreCase("FQ_MOVE_LOCATION")){
					new MultipleCopyDialog(mContext, tempList, size.x*8/9, it.getStringExtra("move_location"), true);
				}else if(ACTION.equalsIgnoreCase("FQ_FILE_LOCKED_OR_UNLOCKED")){
					/**
					 * item locking and unlocking task is done here....
					 */
					if(it.getStringExtra("password_verified").equals("verified")){
						//THE FILE OPENING AND FILE LISTING OPERATION HAS TO BE
						//DONE AFTER PASSWORD IS VERIFIED....
						//USER HAS SELECTED TO OPEN A LOCKED ITEM,
						//AFTER PASSORD VERIFICATION,SERVE USER'S REQUEST HERE...
						
						
						if(Constants.activeMode == Constants.MODES.UNLOCK_ALL){
							if(Constants.db.deleteAllLockedNodes()){
								setAdapter(CURRENT_ITEM);
								Toast.makeText(mContext, R.string.lockeditemsunlocked, Toast.LENGTH_SHORT).show();
							}	
							else
								Toast.makeText(mContext, R.string.unabletounlock, Toast.LENGTH_SHORT).show();
						}						
						else if(CURRENT_ITEM==2){
							
							//opening task here...
							if(Constants.activeMode==Constants.MODES.OPEN){
								if (!file2.isDirectory()) {
									new OpenFileDialog(mContext, Uri.parse(file2.getPath()), size.x*8/9);
								} else if (file2.isDirectory()) {
									SDManager.nStack.push(file2.getPath());
									setAdapter(2);
								}
							}
							//delete the provided item after password verification...
							else if(Constants.activeMode == Constants.MODES.DELETE){
								Constants.db.deleteLockedNode(file2.getPath());
								deleteMethod(file2);
							}
							//open the locked file or folder after password verification...
							else if(Constants.activeMode == Constants.MODES.G_OPEN){
								File f = new File(it.getStringExtra("g_open_path"));
								if(f.isFile())
									new OpenFileDialog(mContext, Uri.parse(f.getPath()), size.x*8/9);
								else{
									SDManager.nStack.push(f.getPath());
									setAdapter(2);
								}
							}
							//sharing the locked item after password verification...
							else if(Constants.activeMode == Constants.MODES.SEND){
								new BluetoothChooser(mContext, file2.getPath(), size.x*8/9, null);
							}
							
							//archiving the locked item here....
							else if(Constants.activeMode == Constants.MODES.ARCHIVE){
								ArrayList<Item> temp = new ArrayList<Item>();
								temp.add(file2);
								new CreateZip(mContext, size.x*8/9, temp);
							}
							
							//renaming the locked item here...
							else if(Constants.activeMode == Constants.MODES.RENAME){
								COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
								RENAME_COMMAND = true;
								mVFlipper.showPrevious();
								mVFlipper.setAnimation(prevAnim());
								editBox.setText(file2.getName());
								editBox.setSelected(true);
							}
							
							//pasting the file into locked item...
							else if(Constants.activeMode == Constants.MODES.PASTEINTO) {
								pasteCommand(true);
							}
							
							else if(Constants.activeMode == Constants.MODES.COPY)
								pasteCommand(true);
							
							//opening the home folder....
							else if(Constants.activeMode == Constants.MODES.HOME){
								SDManager.nStack.push(HOME_DIRECTORY);
								setAdapter(2);
							}
						}else if(CURRENT_ITEM==1){
							
							//opening task here...
							if(Constants.activeMode == Constants.MODES.OPEN){
								if(!file.isDirectory()){
									new OpenFileDialog(mContext, Uri.parse(file.getPath()), size.x*8/9);
								}else if(file.isDirectory()){
									RootManager.nStack.push(file.getPath());
									setAdapter(1);
								}
							}
							//delete the provided item after password verification...
							else if(Constants.activeMode == Constants.MODES.DELETE){
								Constants.db.deleteLockedNode(file.getPath());
								deleteMethod(file);
							}
							//open the locked file or folder after password verification...
							else if(Constants.activeMode == Constants.MODES.G_OPEN){
								File f = new File(it.getStringExtra("g_open_path"));
								if(f.isFile())
									new OpenFileDialog(mContext, Uri.parse(f.getPath()), size.x*8/9);
								else{
									RootManager.nStack.push(f.getPath());
									setAdapter(1);
								}
							}
							//sharing the locked item after password verification...
							else if(Constants.activeMode == Constants.MODES.SEND)
								new BluetoothChooser(mContext, file.getPath(), size.x*8/9, null);
							
							//archiving the locked item here....
							else if(Constants.activeMode == Constants.MODES.ARCHIVE){
								ArrayList<Item> temp = new ArrayList<Item>();
								temp.add(file);
								new CreateZip(mContext, size.x*8/9, temp);
							}
							
							//renaming the locked item here...
							else if(Constants.activeMode == Constants.MODES.RENAME){
								COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
								RENAME_COMMAND = true;
								mVFlipper.showPrevious();
								mVFlipper.setAnimation(prevAnim());
								editBox.setText(file.getName());
								editBox.setSelected(true);
							}
							
							//pasting the file into locked item...
							else if(Constants.activeMode == Constants.MODES.PASTEINTO){
								pasteCommand(true);
							}
							
							else if(Constants.activeMode == Constants.MODES.COPY)
								pasteCommand(true);
							
							
							//opening the home folder..
							else if(Constants.activeMode == Constants.MODES.HOME){
								RootManager.nStack.push(HOME_DIRECTORY);
								setAdapter(1);
							}
						}else if(CURRENT_ITEM == 0){
							//handling the locked items post request here for file gallery...
							
							//opening the locked item after passwd verification...
							if(Constants.activeMode == Constants.MODES.OPEN)
								new OpenFileDialog(mContext, Uri.parse(file0.getPath()), size.x*8/9);
							
							//archiving the locked item after passwd verivication....
							else if(Constants.activeMode == Constants.MODES.ARCHIVE){
								ArrayList<Item> temp = new ArrayList<Item>();
								temp.add(file0);
								new CreateZip(mContext, size.x*8/9, temp);
							}
							
							//delete the provided item after password verification...
							else if(Constants.activeMode == Constants.MODES.DELETE){
								Constants.db.deleteLockedNode(file0.getPath());
								deleteMethod(file0);
							}
							
							//sharing the locked item after password verification...
							else if(Constants.activeMode == Constants.MODES.SEND)
								new BluetoothChooser(mContext, file0.getPath(), size.x*8/9, null);
							
							//renaming the locked item here...
							else if(Constants.activeMode == Constants.MODES.RENAME){
								COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
								RENAME_COMMAND = true;
								mVFlipper.showPrevious();
								mVFlipper.setAnimation(prevAnim());
								editBox.setText(file0.getName());
								editBox.setSelected(true);
							}
							
						}
					}else{
						//THE UI HAS TO BE CHANGED BASED ON THE LOCKING AND
						//UNLOCKING EVENT...
						int id = Constants.lock.getId();
						if(CURRENT_ITEM==2){
							if(!sdItemsList.get(id).isLocked()){
								//file is not locked ....
								//lock the file...
								
								//this condition is true when user has not up the password and tried to lock the item...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_locked));
								Constants.db.insertNodeToLock(sdItemsList.get(id).getFile().getAbsolutePath(), 1);
								sdItemsList.get(id).setLockStatus(true);
								Toast.makeText(mContext, R.string.itemlocked, Toast.LENGTH_SHORT).show();
							}else if(sdItemsList.get(id).isLocked()){
								//after password verification was successful,unlock the file...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_unlocked));
								Constants.db.deleteLockedNode(sdItemsList.get(id).getFile().getPath());
								sdItemsList.get(id).setLockStatus(false);
								Toast.makeText(mContext, R.string.itemunlocked, Toast.LENGTH_SHORT).show();
							}
						}else if(CURRENT_ITEM==1){
							if(!rootItemList.get(id).isLocked()){
								//file is not locked ....
								//lock the file...
								
								//this condition is true when user has not up the password and tried to lock the item...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_locked));
								Constants.db.insertNodeToLock(rootItemList.get(id).getFile().getAbsolutePath(), 1);
								rootItemList.get(id).setLockStatus(true);
								Toast.makeText(mContext, R.string.itemlocked, Toast.LENGTH_SHORT).show();
							}else if(rootItemList.get(id).isLocked()){
								//after password verification was successful,unlock the file...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_unlocked));
								Constants.db.deleteLockedNode(rootItemList.get(id).getFile().getPath());
								rootItemList.get(id).setLockStatus(false);
								Toast.makeText(mContext, R.string.itemunlocked, Toast.LENGTH_SHORT).show();
							}
						}else if(CURRENT_ITEM == 0){
							if(!mediaFileList.get(id).isLocked()){
								//file is not locked...
								//lock the file...
								
								//this condition is true when user has not up the password and tried to lock the item...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_locked));
								Constants.db.insertNodeToLock(mediaFileList.get(id).getFile().getAbsolutePath(), 1);
								mediaFileList.get(id).setLockStatus(true);
								Toast.makeText(mContext, R.string.itemlocked, Toast.LENGTH_SHORT).show();
							}else if(mediaFileList.get(id).isLocked()){
								//after password verification was successful,unlock the file...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_unlocked));
								Constants.db.deleteLockedNode(mediaFileList.get(id).getFile().getPath());
								mediaFileList.get(id).setLockStatus(false);
								Toast.makeText(mContext, R.string.itemunlocked, Toast.LENGTH_SHORT).show();
							}
						}
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter("FQ_BACKUP");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_DELETE");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_FLASHZIP");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter(Intent.ACTION_UNINSTALL_PACKAGE);
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_G_OPEN");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_ZIP_OPEN");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_EXTRACT_PATH");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_RAR_OPEN");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_TAR_OPEN");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_7Z_OPEN");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_DROPBOX_OPEN_FOLDER");
		this.registerReceiver(RECEIVER, filter);
		filter = new IntentFilter("FQ_MOVE_LOCATION");
		this.registerReceiver(RECEIVER, filter);
		
		filter = new IntentFilter("FQ_FILE_LOCKED_OR_UNLOCKED");
		this.registerReceiver(RECEIVER, filter);
	}
	
	
	/**
	 * PERFORMS SEARCH INSIDE OF ZIP ARCHIVE FILE...
	 */
	private void zipSearch() {
		// TODO Auto-generated method stub
		zSearch = new ArrayList<ZipObj>();
		try{
			LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
			a.setVisibility(View.GONE);
			// Search Flipper is loaded
			mVFlipper.setAnimation(nextAnim());
			mVFlipper.showNext();
			mVFlipper.showNext();
			SEARCH_FLAG = true;
			// PREVIOUS COMMANDS ARE OVERWRITTEN
			COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = CREATE_FILE = false;
			editBox.setTextColor(Color.WHITE);
			editBox.setText(null);
			editBox.setHint(R.string.nametofilterout);
			editBox.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
					// TODO Auto-generated method stub
					zSearch.clear();
				}
				
				@Override
				public void afterTextChanged(final Editable ed) {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void>() {
						
						@Override
						protected void onPostExecute(Void result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							if(CURRENT_ITEM==2)
								root.setAdapter(new ZipAdapter(zSearch, mContext));
							else if(CURRENT_ITEM==1)
								simple.setAdapter(new ZipAdapter(zSearch, mContext));
						}

						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							if(CURRENT_ITEM==2)
								root.setAdapter(null);
							else if(CURRENT_ITEM==1)
								simple.setAdapter(null);
						}

						@Override
						protected Void doInBackground(Void... arg0) {
							// TODO Auto-generated method stub
							if(CURRENT_ITEM==2){
								String text = ed.toString().toLowerCase();
								int len = zListSD.size();
								for(int i=0;i<len;++i){
									if(zListSD.get(i).getName().toLowerCase().contains(text))
										zSearch.add(zListSD.get(i));
								}
							}else if(CURRENT_ITEM==1){
								String text = ed.toString().toLowerCase();
								int len = zListRoot.size();
								for(int i=0;i<len;++i){
									if(zListRoot.get(i).getName().toLowerCase().contains(text))
										zSearch.add(zListRoot.get(i));
								}
							}
							return null;
						}
					}.execute();
				}
			});
		}catch(Exception e){
			
		}
	}
	
	/**
	 * PERFORMS SEARCH INSIDE OF TAR ARCHIVE FILE...
	 */
	private void tarSearch() {
		// TODO Auto-generated method stub
		tSearch = new ArrayList<TarObj>();
		try{
			LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
			a.setVisibility(View.GONE);
			// Search Flipper is loaded
			mVFlipper.setAnimation(nextAnim());
			mVFlipper.showNext();
			mVFlipper.showNext();
			SEARCH_FLAG = true;
			// PREVIOUS COMMANDS ARE OVERWRITTEN
			COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = CREATE_FILE = false;
			editBox.setTextColor(Color.WHITE);
			editBox.setText(null);
			editBox.setHint(R.string.nametofilterout);
			editBox.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
					// TODO Auto-generated method stub
					tSearch.clear();
				}
				
				@Override
				public void afterTextChanged(final Editable ed) {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void>() {
						
						@Override
						protected void onPostExecute(Void result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							if(CURRENT_ITEM==2)
								root.setAdapter(new TarAdapter(mContext,tSearch));
							else if(CURRENT_ITEM==1)
								simple.setAdapter(new TarAdapter(mContext,tSearch));
						}

						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							if(CURRENT_ITEM==2)
								root.setAdapter(null);
							else if(CURRENT_ITEM==1)
								simple.setAdapter(null);
						}

						@Override
						protected Void doInBackground(Void... arg0) {
							// TODO Auto-generated method stub
							if(CURRENT_ITEM==2){
								String text = ed.toString().toLowerCase();
								int len = tListSD.size();
								for(int i=0;i<len;++i){
									if(tListSD.get(i).getName().toLowerCase().contains(text))
										tSearch.add(tListSD.get(i));
								}
							}else if(CURRENT_ITEM==1){
								String text = ed.toString().toLowerCase();
								int len = tListRoot.size();
								for(int i=0;i<len;++i){
									if(tListRoot.get(i).getName().toLowerCase().contains(text))
										tSearch.add(tListRoot.get(i));
								}
							}
							return null;
						}
					}.execute();
				}
			});
		}catch(Exception e){
			
		}
	}
	
	
	/**
	 * PERFORMS SEARCH INSIDE OF RAR ARCHIVE FILE...
	 */
	private void rarSearch() {
		// TODO Auto-generated method stub
		rSearch = new ArrayList<RarObj>();
		try{
			LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
			a.setVisibility(View.GONE);
			// Search Flipper is loaded
			mVFlipper.setAnimation(nextAnim());
			mVFlipper.showNext();
			mVFlipper.showNext();
			SEARCH_FLAG = true;
			// PREVIOUS COMMANDS ARE OVERWRITTEN
			COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = CREATE_FILE = false;
			editBox.setTextColor(Color.WHITE);
			editBox.setText(null);
			editBox.setHint(R.string.nametofilterout);
			editBox.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
					// TODO Auto-generated method stub
					rSearch.clear();
				}
				
				@Override
				public void afterTextChanged(final Editable ed) {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, Void>() {
						
						@Override
						protected void onPostExecute(Void result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							if(CURRENT_ITEM==2)
								root.setAdapter(new RarAdapter(mContext,rSearch));
							else if(CURRENT_ITEM==1)
								simple.setAdapter(new RarAdapter(mContext, rSearch));
						}

						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							if(CURRENT_ITEM==2)
								root.setAdapter(null);
							else if(CURRENT_ITEM ==1)
								root.setAdapter(null);
						}

						@Override
						protected Void doInBackground(Void... arg0) {
							// TODO Auto-generated method stub
							if(CURRENT_ITEM==2){
								String text = ed.toString().toLowerCase();
								int len = rListSD.size();
								for(int i=0;i<len;++i){
									if(rListSD.get(i).getFileName().toLowerCase().contains(text))
										rSearch.add(rListSD.get(i));
								}
							}else if(CURRENT_ITEM==1){
								String text = ed.toString().toLowerCase();
								int len = rListRoot.size();
								for(int i=0;i<len;++i){
									if(rListRoot.get(i).getFileName().toLowerCase().contains(text))
										rSearch.add(rListRoot.get(i));
								}
							}
							return null;
						}
					}.execute();
				}
			});
		}catch(Exception e){
			
		}
	}
	
	
	/**
	 *THIS FUNCTION SETS THE ADAPTER WHEN ZIP FILE IS OPERATED.... 
	 */
	private static void setZipAdapter(){
		final Dialog progDial = new Dialog(mContext, R.style.custom_dialog_theme);
		progDial.setContentView(R.layout.p_dialog);
		progDial.setCancelable(false);
		progDial.getWindow().getAttributes().width = size.x*8/9;
		WebView web = (WebView)progDial.findViewById(R.id.p_Web_View);
		web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		web.setEnabled(false);
		progDial.show();
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==0 && (ZIP_SD||ZIP_ROOT)){
					resetPager();
					if(progDial.isShowing())
						progDial.dismiss();
				}else{
					/**
					 * ZIP ARCHIVE IS CORRUPTED OR AN ERROR WAS OCCURED WHILE READING...
					 */
					if(CURRENT_ITEM==1)
						RootManager.nStack.pop();
					else if(CURRENT_ITEM==2)
						SDManager.nStack.pop();
					resetPager();
					Toast.makeText(mContext, R.string.zipexception, Toast.LENGTH_SHORT).show();
				}
			}
		};
		Thread thr = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(zipPathSD==null)
					zipPathSD = "/";
				
				if(zipPathRoot == null)
					zipPathRoot = "/";
				
				if(CURRENT_ITEM==2||CURRENT_ITEM==0){
					try {
						File fi = file2.getFile();
						if(CURRENT_ITEM==0){
							CURRENT_ITEM = 2;
							fi = file0.getFile();
						}
						ZipFile zf = new ZipFile(fi);
						zListSD = new ZipManager(zf, zipPathSD, mContext).generateList();
						handle.sendEmptyMessage(0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ZIP_SD = false;
						handle.sendEmptyMessage(1);
					} 
				}else if(CURRENT_ITEM==1){
					try{
						ZipFile zFile = new ZipFile(file.getFile());
						zListRoot = new ZipManager(zFile, zipPathRoot, mContext).generateList();
						handle.sendEmptyMessage(0);
					}catch(IOException e){
						ZIP_ROOT = false;
						handle.sendEmptyMessage(1);
					}
				}
			}
		});		
		thr.start();
	}
	
	/**
	 * THIS FUNCTION PREPARED THE LIST OF FILES INSIDE OF RAR ARCHIVE
	 * AT THE PROVIDED PATH....
	 */
	private static void setRarAdapter(){
		final Dialog progDial = new Dialog(mContext, R.style.custom_dialog_theme);
		progDial.setContentView(R.layout.p_dialog);
		progDial.setCancelable(false);
		progDial.getWindow().getAttributes().width = size.x*8/9;
		WebView web = (WebView)progDial.findViewById(R.id.p_Web_View);
		web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		web.setEnabled(false);
		progDial.show();
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==0 && (RAR_SD||RAR_ROOT)){
					resetPager();
					if(progDial.isShowing())
						progDial.dismiss();
				}else{
					/**
					 * RAR ARCHIVE IS CORRUPTED OR AN ERROR WAS OCCURED WHILE READING...
					 */
					if(CURRENT_ITEM==1)
						RootManager.nStack.pop();
					else if(CURRENT_ITEM==2)
						SDManager.nStack.pop();
					resetPager();
					Toast.makeText(mContext, R.string.zipexception, Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		Thread thr = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(rarPathSD==null)
					rarPathSD = "/";
				
				if(rarPathRoot == null)
					rarPathRoot = "/";
				
				if(CURRENT_ITEM==2||CURRENT_ITEM==0){
					try {
						File fi = file2.getFile();
						if(CURRENT_ITEM==0){
							CURRENT_ITEM = 2;
							fi = file0.getFile();
						}
						Archive zf = new Archive(fi);
						rListSD = new RarManager(zf, rarPathSD, mContext).generateList();
						handle.sendEmptyMessage(0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						RAR_SD = false;
						handle.sendEmptyMessage(1);
					}catch(RarException e){
						RAR_SD = false;
						handle.sendEmptyMessage(1);
					}
				}else if(CURRENT_ITEM==1){
					try{
						Archive zfile = new Archive(file.getFile());
						rListRoot = new RarManager(zfile, rarPathRoot, mContext).generateList();
						handle.sendEmptyMessage(0);
					}catch(IOException e){
						RAR_ROOT = false;
						handle.sendEmptyMessage(1);
					}catch(RarException e){
						RAR_SD = false;
						handle.sendEmptyMessage(1);
					}
				}
			}
		});		
		thr.start();
	}
	
	
	/**
	 * THIS FUNCTION PREPARED THE LIST OF FILES INSIDE OF RAR ARCHIVE
	 * AT THE PROVIDED PATH....
	 */
	private static void setTarAdapter(){
		final Dialog progDial = new Dialog(mContext, R.style.custom_dialog_theme);
		progDial.setContentView(R.layout.p_dialog);
		progDial.getWindow().getAttributes().width = size.x*8/9;
		WebView web = (WebView)progDial.findViewById(R.id.p_Web_View);
		web.loadUrl("file:///android_asset/Progress_Bar_HTML/index.html");
		web.setEnabled(false);
		progDial.show();
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==0 && (TAR_SD||TAR_ROOT)){
					resetPager();
					if(progDial.isShowing())
						progDial.dismiss();
				}else{
					/**
					 * RAR ARCHIVE IS CORRUPTED OR AN ERROR WAS OCCURED WHILE READING...
					 */
					if(CURRENT_ITEM==1)
						RootManager.nStack.pop();
					else if(CURRENT_ITEM==2)
						SDManager.nStack.pop();
					resetPager();
					Toast.makeText(mContext, R.string.zipexception, Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		Thread thr = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(tarPathSD==null)
					tarPathSD = "/";
				
				if(tarPathRoot == null)
					tarPathRoot = "/";
				
				if(CURRENT_ITEM==2||CURRENT_ITEM==0){
					try {
						File fi = file2.getFile();
						if(CURRENT_ITEM==0){
							CURRENT_ITEM = 2;
							fi = file0.getFile();
						}
						tListSD = new TarManager(fi, tarPathSD, mContext).generateList();
						if(progDial.isShowing())
							handle.sendEmptyMessage(0);
						else{
							TAR_SD = false;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						TAR_SD = false;
						handle.sendEmptyMessage(1);
					}
				}else if(CURRENT_ITEM==1){
					try{
						tListRoot = new TarManager(file.getFile(), tarPathRoot, mContext).generateList();
						if(progDial.isShowing())
							handle.sendEmptyMessage(0);
						else{
							TAR_ROOT = false;
						}
					}catch(IOException e){
						TAR_ROOT = false;
						handle.sendEmptyMessage(1);
					}
				}
			}
		});		
		thr.start();
	}
	
	/**
	 * THIS FUNCTION CLOSES THE CURRENTLY OPENED ARCHIVE TO OPEN 
	 * THE NEW ARCHIVE IN THE SAME PANEL...
	 * @param p
	 */
	private void cleanStack(String p){
		if(CURRENT_ITEM==1&&(RAR_ROOT||ZIP_ROOT||TAR_ROOT)){
			while(RootManager.nStack.peek().contains("->"))
				RootManager.nStack.pop();
			RAR_ROOT = TAR_ROOT= ZIP_ROOT = false;
			file = new Item(new File(p), null, null, null);
		}else if(CURRENT_ITEM==2&&(RAR_SD||TAR_SD||ZIP_SD)){
			while(SDManager.nStack.peek().contains("->"))
				SDManager.nStack.pop();
			RAR_SD = ZIP_SD = TAR_SD = false;
			file2 = new Item(new File(p), null, null, null);
		}
	}
	
	/**
	 * 
	 * @param p	
	 */
	private void extractTo(String p){
		if(ZIP_SD||ZIP_ROOT){//EXTRACTING FILE FROM ZIP ARCHIVE...
			if(CURRENT_ITEM==1){
				if(new File(p).canWrite())
					//IF WE HAVE WRITE PERMISSION THEN EXTRACT HERE....
					new ExtractZipFile(mContext, zFileSimple, size.x*8/9, p, file.getFile(), 1);
				else
					//WE DONT HAVE WRITE PERMISSION..... 
					Toast.makeText(mContext, R.string.cannotexthere, Toast.LENGTH_SHORT).show();
			}else if(CURRENT_ITEM == 2)
				new ExtractZipFile(mContext, zFileSD, size.x*8/9, p, file2.getFile(), 1);
		}else if(RAR_SD||RAR_ROOT){//EXTRACTING FILE FROM RAR ARCHIVE...
			
		}else if(TAR_SD||TAR_ROOT){//EXTRACTING FILES FROM TAR ARCHIVE...
			if(CURRENT_ITEM==1){
				if(new File(p).canWrite())
					//IF WE HAVE WRITE PERMISSION THEN EXTRACT HERE....
					new ExtractTarFile(mContext, tFileRoot, size.x*8/9, p, file.getFile(), 1);
				else
					//WE DONT HAVE WRITE PERMISSION..... 
					Toast.makeText(mContext, R.string.cannotexthere, Toast.LENGTH_SHORT).show();
			}else if(CURRENT_ITEM == 2)
				new ExtractTarFile(mContext, tFileSD, size.x*8/9, p, file2.getFile(), 1);
		}
	}		
	
	/**
	 * THIS METHOD INITIALIZES THE SLIDE MENU IN ONCREATE METHOD....
	 */
	private void initLeftMenu(){
		slidemenu = (SlideLayout)findViewById(R.id.slide_left_menu);
		
		ActionSlideExpandableListView lsView = (ActionSlideExpandableListView)findViewById(R.id.actionSlideExpandableListView1);
		
		String[] values = getResources().getStringArray(R.array.slideList);
		lsView.setAdapter(new ExpandableAdapter(this,R.layout.expandable_row_list,
				R.id.expandable_toggle_button,values));
		
		/**
		 * click listener for single item inside slide menu list...
		 */
		lsView.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
			@Override
			public void onClick(View itemView, View clickedView, int position) {
				// TODO Auto-generated method stub
				ArrayList<Item> itemList = null;
				if(position==1)
					itemList = (Utils.music);
				else if(position==2)
					itemList = (Utils.apps);
				else if(position==3)
					itemList = (Utils.doc);
				else if(position==4)
					itemList = (Utils.img);
				else if(position==5)
					itemList = (Utils.vids);
				else if(position==6)
					itemList = (Utils.zip);
				else if(position==7)
					itemList = (Utils.mis);
				
				/**
				 * switching to different actions of buttons in expanded list....
				 */
				switch(clickedView.getId()){
					case R.id.button_delete:
							new DeleteFiles(mContext, size.x*8/9, itemList, getResources().getString(R.string.confirmdeletion));
							break;
							
					case R.id.button_move_all:
							tempList = itemList;
							new GetMoveLocation(mContext, size.x*8/9);
							break;
					case R.id.button_zip_all:
							new CreateZip(mContext, size.x*8/9, itemList);
							break;
				}
			}
		}, R.id.button_delete,R.id.button_zip_all,R.id.button_move_all);
	}
	
	
	
	
	/**
	 * 
	 */
	private static void resetPager(){
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(CURRENT_ITEM);
		indicator.setViewPager(mViewPager);
	}
	
	private static void deleteMethod(Item file2) {
		// TODO Auto-generated method stub
		ArrayList<Item> te = new ArrayList<Item>();
		te.add(file2);
		new DeleteFiles(mContext, size.x*8/9, te,null);
		
	}
	
	/**
	 * 
	 */
	private static void setRootAdapter(){
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				simple.setAdapter(rootAdapter);
			}
		};
		Thread thr = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rootItemList = rootManager.getList();
				if(RootAdapter.MULTI_SELECT)	
					RootAdapter.thumbselection = new boolean[rootItemList.size()];
				handle.sendEmptyMessage(0);
			}
		});
		thr.start();
	}
	
}