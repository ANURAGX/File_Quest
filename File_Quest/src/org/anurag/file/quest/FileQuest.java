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
 *                            anuraxsharma1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipFile;

import org.anurag.adapters.AppAdapter;
import org.anurag.adapters.FileGalleryAdapter;
import org.anurag.adapters.RootAdapter;
import org.anurag.adapters.SDAdapter;
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
import org.anurag.dialogs.BluetoothChooser;
import org.anurag.dialogs.DeleteFiles;
import org.anurag.dropbox.DBoxAdapter;
import org.anurag.dropbox.DBoxAuth;
import org.anurag.dropbox.DBoxManager;
import org.anurag.dropbox.DBoxUsers;
import org.ultimate.menuItems.AppProperties;
import org.ultimate.menuItems.GetHomeDirectory;
import org.ultimate.menuItems.MultipleCopyDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.dropbox.client2.android.AndroidAuthSession;
import com.extra.libs.JazzyHelper;
import com.extra.libs.PagerSlidingTabStrip;
import com.extra.libs.TransitionViewPager;
import com.extra.libs.TransitionViewPager.TransitionEffect;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;



/**
 * 
 * @author ANURAG
 *
 */

@SuppressLint({ "HandlerLeak", "SdCardPath" })
public class FileQuest extends FragmentActivity implements OnClickListener{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private static SectionsPagerAdapter mSectionsPagerAdapter;
	private static String pageTitleRoot;
	private static String pageTitleSD;
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
	 * variables related to sdcard panel....
	 */
	private static SDManager sdManager;
	private static ArrayList<Item> sdItemsList;
	private static RootManager rootManager;
	private static ArrayList<Item> rootItemList;
	private static RootAdapter rootAdapter;
	private static SDAdapter sdAdapter;
	
	
	private static int fPos;

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
	public static float TRANSPARENCY_LEVEL;
	public static SharedPreferences.Editor edit;
	public static SharedPreferences preferences;
	private static boolean SEARCH_FLAG = false;
	private static int CREATE_FLAG = 0;
	private static boolean CREATE_FILE = false;
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
	public static FileGalleryAdapter element;
	public static boolean elementInFocus = false;
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
	//static SlideLayout slidemenu;
	private static View v;
	
	private boolean delete_from_slider_menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PATH = INTERNAL_PATH_ONE = INTERNAL_PATH_TWO = Environment.getExternalStorageDirectory().getAbsolutePath();
		preferences = getSharedPreferences("MY_APP_SETTINGS", 0);
		INTERNAL_PATH_ONE = preferences.getString("INTERNAL_PATH_ONE", PATH);
		INTERNAL_PATH_TWO = preferences.getString("INTERNAL_PATH_TWO", PATH);
		SHOW_APP = preferences.getInt("SHOW_APP", 1);
		CURRENT_ITEM = CURRENT_PREF_ITEM = preferences.getInt("CURRENT_PREF_ITEM", 0);
		TRANSPARENCY_LEVEL = preferences.getFloat("TRANSPARENCY_LEVEL", 1.0f);
		SHOW_HIDDEN_FOLDERS = preferences.getBoolean("SHOW_HIDDEN_FOLDERS",false);
		SORT_TYPE = preferences.getInt("SORT_TYPE", 2);
		Constants.FOLDER_ICON = preferences.getInt("FOLDER_TYPE", 0);
		HOME_DIRECTORY = preferences.getString("HOME_DIRECTORY", null);
		ENABLE_ON_LAUNCH = preferences.getBoolean("ENABLE_ON_LAUNCH", false);
		LIST_ANIMATION = preferences.getInt("LIST_ANIMATION", 4);
		PAGER_ANIMATION = preferences.getInt("PAGER_ANIMATION", 3);
		
		//thumbnail settings....
		Constants.SHOW_APP_THUMB = preferences.getBoolean("SHOW_APP_THUMB", true);
		Constants.SHOW_IMAGE_THUMB = preferences.getBoolean("SHOW_IMAGE_THUMB", true);
		Constants.SHOW_MUSIC_THUMB = preferences.getBoolean("SHOW_MUSIC_THUMB", true);
		
		
		
		edit = preferences.edit();

		//initializing the external,internal,emulated,legacy paths....
		new StorageUtils();
		
		try {
			new File("/sdcard/File Quest/").mkdir();
		} catch (Exception e) {

		}
		
		delete_from_slider_menu = false;
		mContext = FileQuest.this;
		Constants.db = new ItemDB(mContext);
		Constants.dboxDB = new DBoxUsers(mContext);		
		
		Utils.setContext(null , mContext);
		Utils.load();
		
		sdManager = new SDManager(FileQuest.this);
		rootManager = new RootManager(FileQuest.this);
		
		if (ENABLE_ON_LAUNCH) {
			if (new File(INTERNAL_PATH_TWO).exists()) {
			} else {
				edit.putString("INTERNAL_PATH_TWO", PATH);
				edit.commit();
				showToast(getResources().getString(R.string.startupfoldernotfound));
			}
			if (new File(INTERNAL_PATH_ONE).exists()) {
				
			} else {
				edit.putString("INTERNAL_PATH_ONE", PATH);
				edit.commit();
				showToast(getResources().getString(R.string.startupfoldernotfound));
			}
		}		
		//mediaFileList = new ArrayList<Item>();
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
		

		//setContentView(R.layout.new_ui);
	
		//editBox = (EditText) findViewById(R.id.editBox);
	
		//error = false;
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		//mViewPager = (TransitionViewPager) findViewById(R.id.pager);
		//indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		//setting view pager limit to hold fragments off screen....
		mViewPager.setOffscreenPageLimit(4);
		
		String[] te = getResources().getStringArray(R.array.effects);		
		mViewPager.setTransitionEffect(TransitionEffect.valueOf(te[PAGER_ANIMATION]));
		
		indicator.setViewPager(mViewPager);
		mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
		//mVFlipper = (ViewFlipper) findViewById(R.id.viewFlipperMenu);
		// = (ViewFlipper) findViewById(R.id.viewFlipperMenuBottom);
		
		if (CURRENT_PREF_ITEM != 3)
			LAST_PAGE = 2;
		else
			LAST_PAGE = 3;

		if (CURRENT_PREF_ITEM == 0) {
			/*Button b = (Button) findViewById(R.id.change);
			TextView t = (TextView) findViewById(R.id.addText);
			b.setBackgroundResource(R.drawable.ic_launcher_select_app);
			t.setText(R.string.apps);
			mFlipperBottom.showNext();
			new load().execute();
			LAST_PAGE = 0;*/
		}
		else if (CURRENT_PREF_ITEM == 3) {
			mVFlipper.showNext();
			mFlipperBottom.showPrevious();
		}
		nManager = new AppManager(mContext);
		nManager.SHOW_APP = SHOW_APP;
		nList = nManager.giveMeAppList();
		//nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
		
		file = new Item(new File("/"), null, null, null);
		file2 = new Item(new File(PATH),null,null,null);
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int page) {
				// TODO Auto-generated method stub
				mUseBackKey = false;
				CURRENT_ITEM = page;
				//Button b = (Button) findViewById(R.id.change);
				//TextView t = (TextView) findViewById(R.id.addText);
				if (page == 0){
					if (!elementInFocus) {
						mFlipperBottom.showNext();
					}
				//	b.setBackgroundResource(R.drawable.ic_launcher_select_app);
					//t.setText(getString(R.string.apps));
					LAST_PAGE = 0;
					
					//delete or copy operation was performed....
					//updating file gallery....
					if(Utils.update_Needed){
						new load().execute();
						Utils.update_Needed = false;
						if(elementInFocus)
							element.notifyDataSetChanged();
						Utils.updateUI();
					}else if(Utils.fav_Update_Needed){
						//an item was added or removed to favorite list
						//so updating ui....
						Utils.fav_Update_Needed = false;
						Utils.update_fav();
						if(element != null)
							element.notifyDataSetChanged();
					}
					
				} else if (page != 0) {
					//b.setBackgroundResource(R.drawable.ic_launcher_add_new);
					//t.setText(R.string.New);
				}
				if (page == 1 && LAST_PAGE == 0) {
					LAST_PAGE = 1;
					if (!elementInFocus) {
						mFlipperBottom.showPrevious();
					}
				}

				if (page == 2 && LAST_PAGE == 0) {
					if(!elementInFocus){
						mFlipperBottom.showPrevious();
					}					
					LAST_PAGE = 2;
				}

				if ((page == 2 || page == 1) && LAST_PAGE == 3) {
					LAST_PAGE = 2;
					mFlipperBottom.showNext();
					mVFlipper.showPrevious();
				}
				if (page == 3 && (LAST_PAGE == 2 || LAST_PAGE == 1)) {
					LAST_PAGE = 3;
					mFlipperBottom.showPrevious();
					if (RENAME_COMMAND || SEARCH_FLAG || CREATE_FILE) {
						mVFlipper.showPrevious();
						RENAME_COMMAND = SEARCH_FLAG = CREATE_FILE = false;
					} else {
						mVFlipper.showNext();
					}
				}

				if (page == 3 && LAST_PAGE == 0) {
					LAST_PAGE = 3;
					if (elementInFocus) {
						mVFlipper.showNext();
						mFlipperBottom.showPrevious();
					}
				}

				if (RENAME_COMMAND || SEARCH_FLAG || CREATE_FILE) {
					RENAME_COMMAND = SEARCH_FLAG = CREATE_FILE = false;
					mVFlipper.showNext();
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
        
		super.onCreate(savedInstanceState);
			
	}

	@Override
	protected void onPostResume() {
		params = this.getWindow().getAttributes();
		params.alpha = TRANSPARENCY_LEVEL;
		super.onPostResume();
	}

	@Override
	protected void onResumeFragments() {
		params = this.getWindow().getAttributes();
		params.alpha = TRANSPARENCY_LEVEL;
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
		params.alpha = TRANSPARENCY_LEVEL;
		super.onStart();
		// REGISTER_RECEIVER();
		initLeftMenu();
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
			AndroidAuthSession session = DBoxAuth.mApi_1.getSession();
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
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		Utils.loaded = true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Utils.loaded = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//ShowMenu();
		
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
						if(CURRENT_ITEM == 2)
							root.setAdapter(sdAdapter);
						else if(CURRENT_ITEM == 1)
							simple.setAdapter(rootAdapter);
						indicator.notifyDataSetChanged();
						break;
					case 4:
						load_FIle_Gallery(fPos);
						break;
					}
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

				
				
				mUseBackKey = false;
				handle.sendEmptyMessage(3);
				if (ITEM == 0 && elementInFocus)
					handle.sendEmptyMessage(5);
			}
		});
		thread.start();
	}
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		String[] titles = mContext.getResources().getStringArray(R.array.slideList);
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
			switch (position) {
				case 0:
					if(!elementInFocus)
						return getString(R.string.filegallery);
					return titles[fPos];
				case 1:
					
					return pageTitleRoot;
				case 2:
					//pageTitleSD = SDManager.getCurrentDirectoryName();
					return pageTitleSD;
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
			if (LIST_VIEW_3D != null) {
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
			LIST_VIEW_3D.setSelector(R.drawable.button_click);
			FILE_GALLEY = (LinearLayout)v.findViewById(R.id.file_gallery_layout);

			JazzyHelper helper = new JazzyHelper(mContext, null);
			helper.setTransitionEffect(LIST_ANIMATION);
			LIST_VIEW_3D.setOnScrollListener(helper);
			
			LinearLayout fav = (LinearLayout) v.findViewById(R.id.fav);
			LinearLayout music = (LinearLayout) v.findViewById(R.id.music);
			LinearLayout app = (LinearLayout) v.findViewById(R.id.apps);
			LinearLayout docs = (LinearLayout) v.findViewById(R.id.docs);
			LinearLayout photo = (LinearLayout) v.findViewById(R.id.photos);
			LinearLayout vids = (LinearLayout) v.findViewById(R.id.videos);
			LinearLayout zips = (LinearLayout) v.findViewById(R.id.zips);
			LinearLayout misc = (LinearLayout) v.findViewById(R.id.misc);
		
			
			Utils.setView(v);
			
			//update the favorite tile separately.... 
			Utils.update_fav();
			//update the ui....
			Utils.updateUI();
			
			/*
			 * WHEN FAVORITE BUTTON IS CLICKED
			 */
			fav.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 0));
				}
			});

			/*
			 * WHEN music BUTTON IS CLICKED
			 */
			music.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 1));
				}
			});

			app.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						load_FIle_Gallery((fPos = 2));
					} catch (InflateException e) {

					}
				}
			});

			docs.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 5));
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
					load_FIle_Gallery((fPos = 6));
				}
			});

			misc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					load_FIle_Gallery((fPos = 7));
				}
			});
		

			final Dialog d = new Dialog(mContext,Constants.DIALOG_STYLE);
			d.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) d.findViewById(R.id.list);
			//AdapterLoaders loaders = new AdapterLoaders(getActivity(), false,0);
			//lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.button_click));
			d.getWindow().getAttributes().width = size.x*8/9;
			LIST_VIEW_3D.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,View arg1, int position, long arg3) {
							if (elementInFocus) {
								if (SEARCH_FLAG)
									file0 = (Item) LIST_VIEW_3D.getAdapter().getItem(position);
								else
									file0 = getItemFromCategory(fPos, position);
								
								//checks existsnce for file....
								if(file0.exists()){
									d.show();
								}
								else
									Toast.makeText(mContext, R.string.filedoesnotexists, Toast.LENGTH_SHORT).show();
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
						
						if(!file0.isLocked()){//file is not locked...
							if(!file0.isDirectory())
								new OpenFileDialog(mContext, Uri.parse(file0.getPath()), size.x*8/9);
							else{
								//a folder was requested to open from favorite tile...
								//SDManager.nStack.push(file0.getPath());
								setAdapter(2);
								mViewPager.setCurrentItem(2);
							}
						}	
						else//file is locked....
							new MasterPassword(mContext, size.x*8/9, file0, preferences,Constants.MODES.OPEN);
						break;
					case 1:
						// copy to CLOUD.....
				//		new CopyToCloud(mContext, file0);
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
						//new AddGesture(mContext, size.x, size.y*8/9, file0.getPath());
						break;
					case 10:
						// PROPERTIES
						Intent fprp = new Intent(mContext, org.anurag.file.quest.FileProperties.class);
						fprp.putExtra("path", file0.getPath());
						startActivity(fprp);
					}
				}
			});

			lo.setOnItemLongClickListener(null);
			LIST_VIEW_3D.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,final int position, long id) {
					if (elementInFocus) {
						if (SEARCH_FLAG)
							file0 = (Item) LIST_VIEW_3D.getAdapter().getItem(position);
						else
							file0 = getItemFromCategory(fPos, position);
						
						if(file0.exists()){
							if(!file0.isLocked()){//selected item is not locked...
								if(!file0.isDirectory())
									new OpenFileDialog(mContext, Uri.parse(file0.getPath()), size.x*8/9);
								else{
									//folder is tried to open from favorite tile...
									//SDManager.nStack.push(file0.getPath());
									setAdapter(2);
									mViewPager.setCurrentItem(2);
								}
							}else//item is locked...
								new MasterPassword(mContext, size.x*8/9, file0, preferences, Constants.MODES.OPEN);
						}else
							Toast.makeText(mContext, R.string.filedoesnotexists, Toast.LENGTH_SHORT).show();
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
			simple.setSelector(R.drawable.button_click);
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
						mVFlipper.showNext();
						CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = RENAME_COMMAND = false;
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
							///RootManager.nStack.push(zipPathRoot+" -> Zip");
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
							//RootManager.nStack.push(rFileRoot.getFileName()+" -> Rar");
							setRarAdapter();
						}
						
					}else if(TAR_ROOT){//HANDLING TAR FILE....
						if(tFileRoot.isFile()){
							//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
							new ExtractTarFile(mContext, tFileRoot, size.x*8/9, null, file.getFile(), 0);	
						}else{
							tarPathRoot = tFileRoot.getPath();
							//.nStack.push(tFileRoot.getName()+" -> Tar");
							setTarAdapter();
						}
					}else{
						if(file.exists()){
							//HANDLING ORDINARY FILE EXLORING....
							if(!file.isLocked()){
								//file is not locked,open it as usual....
								if (!file.isDirectory())
									new OpenFileDialog(mContext, Uri.parse(file.getPath()), size.x*8/9);
								else if (file.isDirectory()){
									//RootManager.nStack.push(file.getPath());
									setAdapter(1);
								}
							}else
								new MasterPassword(mContext, size.x*8/9, file, preferences,Constants.MODES.OPEN);
						}else
							Toast.makeText(mContext, R.string.filedoesnotexists, Toast.LENGTH_SHORT).show();
					}					
				}
			});

			final Dialog d = new Dialog(mContext, Constants.DIALOG_STYLE);
			d.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) d.findViewById(R.id.list);
			//AdapterLoaders loaders = new AdapterLoaders(getActivity(), false , 1);
			//lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.button_click));
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
					if(file.exists()){
						d.show();
					}else
						Toast.makeText(mContext, R.string.filedoesnotexists, Toast.LENGTH_SHORT).show();
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
								//RootManager.nStack.push(zipPathRoot+" -> Zip");
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
								//RootManager.nStack.push(rFileRoot.getFileName()+" -> Rar");
								setRarAdapter();
							}
							
						}else if(TAR_ROOT){//HANDLING TAR FILE....
							if(tFileRoot.isFile()){
								//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
								new ExtractTarFile(mContext, tFileRoot, size.x*8/9, null, file.getFile(), 0);	
							}else{
								tarPathRoot = tFileRoot.getPath();
								//RootManager.nStack.push(tFileRoot.getName()+" -> Tar");
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
									//RootManager.nStack.push(file.getPath());
									setAdapter(1);
								}
							}else
								new MasterPassword(mContext, size.x*8/9, file, preferences,Constants.MODES.OPEN);
						}
						break;

					case 1:
						// COPY TO CLOUD
				//		new CopyToCloud(mContext, file);
						break;

					case 2:
						// COPY
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
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
						if(ZIP_SD||RAR_SD||TAR_SD)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							if(Constants.isExtAvailable && Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
								if(file.getPath().startsWith(Constants.EXT_PATH)){
									//tried to rename the file from external sd.....
									//new ErrorDialogs(mContext, size.x*8/9, "renameError");
								}else{
									if(!file.isLocked()){
										COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
										RENAME_COMMAND = true;
										mVFlipper.showPrevious();
										editBox.setText(file.getName());
										editBox.setSelected(true);
									}else{
										new MasterPassword(mContext, size.x*8/9, file, preferences, Constants.MODES.RENAME);
									}
								}
							}else{		
								if(!file.isLocked()){
									COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
									RENAME_COMMAND = true;
									mVFlipper.showPrevious();
									editBox.setText(file.getName());
									editBox.setSelected(true);
								}else{
									new MasterPassword(mContext, size.x*8/9, file, preferences, Constants.MODES.RENAME);
								}
							}
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
						//else
							//new AddGesture(mContext, size.x, size.y*8/9, file.getPath());
						break;
					case 10:
						// PROPERTIES
						if(ZIP_ROOT)//A ZIP FILE IS OPENED....
							new ArchiveEntryProperties(mContext, zFileSimple, size.x*8/9);
						else if(RAR_ROOT)
							new RarFileProperties(mContext, rFileRoot, size.x*8/9);
						else if(TAR_ROOT)
							new TarFileProperties(mContext, tFileRoot, size.x*8/9);
						else{
							Intent fprp = new Intent(mContext, org.anurag.file.quest.FileProperties.class);
							fprp.putExtra("path", file.getPath());
							startActivity(fprp);
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
	public static class SDPanel extends BaseFragment {

		public SDPanel() {
			//PASSING THE ANIMATION TYPE FOR THE LIST VIEW.....
			super(LIST_ANIMATION);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			root = getListView();			
			root.setSelector(R.drawable.button_click);
			ColorDrawable color = new ColorDrawable(android.R.color.black);
			
			root.setDivider(color);
			if(DBoxManager.DBOX_SD)
				setListAdapter(new DBoxAdapter(mContext, DBoxManager.dListSimple));
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
			dialog = new Dialog(getActivity(), Constants.DIALOG_STYLE);
			dialog.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) dialog.findViewById(R.id.list);
			//AdapterLoaders loaders = new AdapterLoaders(mContext, false , 2);
			//lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.button_click));
			dialog.getWindow().getAttributes().width = size.x*8/9;
			root.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					// TODO Auto-generated method stub
					if (SEARCH_FLAG){
						if(DBoxManager.DBOX_SD)
							DBoxManager.sd = DBoxManager.dSearch.get(arg2);
						else if(ZIP_SD)
							zFileSD = zSearch.get(arg2);
						else if(RAR_SD)
							rarFileSD = rSearch.get(arg2);
						else if(TAR_SD)
							tFileSD = tSearch.get(arg2);
						else
							file2 = searchList.get(arg2);
					} else {
						if(DBoxManager.DBOX_SD)
							DBoxManager.sd = DBoxManager.dListSimple.get(arg2);
						else if(ZIP_SD)
							zFileSD = zListSD.get(arg2);
						else if(RAR_SD)
							rarFileSD = rListSD.get(arg2);
						else if(TAR_SD)
							tFileSD = tListSD.get(arg2);
						else
							file2 = sdItemsList.get(arg2);
					}
					if(file2.exists()){
						dialog.show();
					}
					else
						Toast.makeText(mContext, R.string.filedoesnotexists, Toast.LENGTH_SHORT).show();
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
								mVFlipper.showNext();
								CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = RENAME_COMMAND = false;
							}
						
							if(DBoxManager.DBOX_SD){
								if(DBoxManager.sd.isDir()){
									//open the folder.....
									DBoxManager.simplePath = DBoxManager.sd.getPath();
									DBoxManager.setDropBoxAdapter(2, mContext);
								}else{
									//download the file....
								}
							}							
							else if(ZIP_SD){//ZIP FILE HANDLING...
								if(zFileSD.isFile()){
									//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
									new ExtractZipFile(mContext, zFileSD, size.x*8/9 , null , file2.getFile(),0);
								}else{
									//DIRECTORY HAS TO BE OPENED....
									zipPathSD = zFileSD.getPath();
									//SDManager.nStack.push(zipPathSD+" -> Zip");
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
									//SDManager.nStack.push(rarFileSD.getFileName()+" -> Rar");
									setRarAdapter();
								}
							}else if(TAR_SD){//HANDLING TAR FILE....
								if(tFileSD.isFile()){
									//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
									new ExtractTarFile(mContext, tFileSD, size.x*8/9, null, file2.getFile(), 0);
								}else{
									tarPathSD = tFileSD.getPath();
									//SDManager.nStack.push(tFileSD.getName()+" -> Tar");
									setTarAdapter();
								}
							}else{//ORDINARY FILE HANDLING....
								if(!file2.isLocked()){
									//this item is unlocked,no need to verify for password....
									if (!file2.isDirectory()) {
										new OpenFileDialog(mContext, Uri.parse(file2.getPath()), size.x*8/9);
									} else if (file2.isDirectory()) {
										//SDManager.nStack.push(file2.getPath());
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
					//	new CopyToCloud(mContext, file2);
						break;

					case 2:
						// COPY
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
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
								if(Constants.isExtAvailable && Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
									if(file2.getPath().startsWith(Constants.EXT_PATH)){
										//tried to rename the file from external sd.....
									//	new ErrorDialogs(mContext, size.x*8/9, "renameError");
									}else{
										if(!file2.isLocked()){
											COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
											RENAME_COMMAND = true;
											mVFlipper.showPrevious();
											editBox.setText(file2.getName());
											editBox.setSelected(true);
										}else{
											new MasterPassword(mContext, size.x*8/9, file2, preferences, Constants.MODES.RENAME);
										}
									}
								}else{		
									if(!file2.isLocked()){
										COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
										RENAME_COMMAND = true;
										mVFlipper.showPrevious();
										editBox.setText(file2.getName());
										editBox.setSelected(true);
									}else{
										new MasterPassword(mContext, size.x*8/9, file2, preferences, Constants.MODES.RENAME);
									}
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
						//else
						//	new AddGesture(mContext, size.x, size.y*8/9,file2.getPath());
						break;
					case 10:
						// PROPERTIES
						if(ZIP_SD)
							new ArchiveEntryProperties(mContext, zFileSD, size.x*8/9);
						else if(RAR_SD)
							new RarFileProperties(mContext, rarFileSD, size.x*8/9);
						else if(TAR_SD)
							new TarFileProperties(mContext, tFileSD, size.x*8/9);
						else{
							//new FileProperties(mContext, size.x*8/9, file2.getFile());
							Intent fprp = new Intent(mContext, org.anurag.file.quest.FileProperties.class);
							fprp.putExtra("path", file2.getPath());
							startActivity(fprp);
						}	
					}
				}			
			});
			
			root.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
					// TODO Auto-generated method stub
					if(SEARCH_FLAG){
						if(DBoxManager.DBOX_SD)
							DBoxManager.sd = DBoxManager.dSearch.get(position);
						else if(ZIP_SD)
							zFileSD = zSearch.get(position);
						else if(RAR_SD)
							rarFileSD = rSearch.get(position);
						else if(TAR_SD)
							tFileSD = tSearch.get(position);
						else
							file2 = searchList.get(position);
					}else{
						if(DBoxManager.DBOX_SD)
							DBoxManager.sd = DBoxManager.dListSimple.get(position);
						else if(ZIP_SD)
							zFileSD = zListSD.get(position);
						else if(RAR_SD)
							rarFileSD = rListSD.get(position);
						else if(TAR_SD)
							tFileSD = tListSD.get(position);
						else
							file2 = sdItemsList.get(position);
					}
					
					if (CREATE_FILE || RENAME_COMMAND || SEARCH_FLAG) {
						mVFlipper.showNext();
						CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = RENAME_COMMAND = false;
					}
					
					if(DBoxManager.DBOX_SD){
						if(DBoxManager.sd.isDir()){
							DBoxManager.simplePath = DBoxManager.sd.getPath();
							DBoxManager.setDropBoxAdapter(2, mContext);
						}else{
							//downloading the file....
							Toast.makeText(mContext, "Downloading file", Toast.LENGTH_LONG).show();
						}
					}
					
					else if(ZIP_SD){//zip file handling...
						if(zFileSD.isFile()){
							//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
							new ExtractZipFile(mContext, zFileSD, size.x*8/9 , null , file2.getFile() , 0);
						}else{
							//DIRECTORY HAS TO BE OPENED....
							zipPathSD = zFileSD.getPath();
							//SDManager.nStack.push(zipPathSD+" -> Zip");
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
							//.nStack.push(rarFileSD.getFileName()+" -> Rar");
							setRarAdapter();
						}
					}else if(TAR_SD){//HANDLING TAR FILE....
						if(tFileSD.isFile()){
							//EXTRACT THE TAR FILE AND OPEN IT USING APPROPRIATE APP....
							new ExtractTarFile(mContext, tFileSD, size.x*8/9, null, file2.getFile(), 0);
						}else{
							tarPathSD = tFileSD.getPath();
							//SDManager.nStack.push(tFileSD.getName()+" -> Tar");
							setTarAdapter();
						}
					}else{//ordinary file handling...
						if(file2.exists()){
							if(!file2.isLocked()){
								//this item is unlocked,no need to verify for password....
								if (!file2.isDirectory()) {
									new OpenFileDialog(mContext, Uri.parse(file2.getPath()), size.x*8/9);
								} else if (file2.isDirectory()) {
									//SDManager.nStack.push(file2.getPath());
									setAdapter(2);
									//resetPager();
								}
							}else{
								new MasterPassword(mContext, size.x*8/9, file2, preferences,Constants.MODES.OPEN);
							}
						}else
							Toast.makeText(mContext, R.string.filedoesnotexists, Toast.LENGTH_SHORT).show();
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
			APP_LIST_VIEW.setSelector(R.drawable.button_click);
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
					Constants.DIALOG_STYLE);
			dia.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) dia.findViewById(R.id.list);
			//AdapterLoaders loaders = new AdapterLoaders(mContext, true , 3);
			//lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.button_click));
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
								//nAppAdapter = new AppAdapter(mContext,R.layout.row_list_1, nList);
								if (MULTI_SELECT_APPS){
									
								}
									//nAppAdapter.MULTI_SELECT = true;
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
		/*switch (v.getId()) {
			
		//options under development....may be available in next update.... 	
		case 1:
		//case R.id.cld_drp:
		//case R.id.cld_gogledrv:
		//case R.id.cld_mdfire:
		//case R.id.cld_sugar:	
		//case R.id.cld_skydrv:
	//	case R.id.cld_copy:	
			Toast.makeText(mContext, R.string.coming_soon, Toast.LENGTH_SHORT).show();
			break;
		
					
		//case R.id.g_open:
			new G_Open(mContext, size.x, size.y);
			break;

		//case R.id.bottom_Quit:
			FileQuest.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			break;

		//case R.id.bottom_paste:
			if (CURRENT_ITEM == 0||ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT) {
				Toast.makeText(mContext, getString(R.string.pastenotallowed), Toast.LENGTH_SHORT).show();
			}else{
				pasteCommand(false);
			}
			break;

	//	case R.id.bottom_copy:
			if(ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 5,getString(R.string.enablemultiselect));
			break;

		//case R.id.bottom_cut:
			if(ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 2,getString(R.string.enablemultiselect));
			break;

		//case R.id.bottom_zip:
			if(ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 3,getString(R.string.enablemultiselect));
			break;

		//case R.id.bottom_delete:
			if((ZIP_SD||ZIP_ROOT||RAR_SD||RAR_ROOT)){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 4,getString(R.string.enablemultiselect));
			break;

		//case R.id.appSettings:
			startActivityForResult(new Intent(mContext , Settings.class), 100);
			break;

		//case R.id.bottom_multi: 

			break;

		//case R.id.bottom_multi_send_app:
			if (MULTI_SELECT_APPS) {
				
			} else {			}
			break;
		//case R.id.bottom_multi_select_backup:
			if (MULTI_SELECT_APPS) {
				
			} else {
				
			}
			break;

		//case R.id.bottom_user_apps:
			// SETS THE SETTING TO SHOW DOWNLOADED APPS ONLY
			edit.putInt("SHOW_APP", 1);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 1;
			Toast.makeText(mContext,getString(R.string.showinguserapps), Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
		//	nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;
		//case R.id.bottom_system_apps:
			// SETS THE SETTING TO SHOW SYSTEM APPS ONLY
			edit.putInt("SHOW_APP", 2);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 2;
			Toast.makeText(mContext,getString(R.string.showingsystemapps), Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			//nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;

		//case R.id.bottom_both_apps:
			// SETS THE SETTING TO SHOW DOWNLOADED AND SYSTEM APPS
			edit.putInt("SHOW_APP", 3);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 3;
			Toast.makeText(mContext,getString(R.string.showinguserandsystemapps),Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			//nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;

		//case R.id.searchBtn:
				if(ZIP_SD||ZIP_ROOT){
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND....
						SEARCH_FLAG = false;
						mVFlipper.showNext();
					}else//SEARCH INSIDE ZIP ARCHIVE...
						zipSearch();
				}else if(RAR_SD||RAR_ROOT){
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND....
						SEARCH_FLAG = false;
						mVFlipper.showNext();
					}else//SEARCH INSIDE RAR ARCHIVE....
						rarSearch();
				}else if(TAR_SD||TAR_ROOT){
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND...
						SEARCH_FLAG = false;
						mVFlipper.showNext();
					}else//SEARCH INSIDE TAR ARCHIVE....
						tarSearch();
				}
				else{
					if(SEARCH_FLAG){//DISABLING SEARCH COMMAND....
						SEARCH_FLAG = false;
						mVFlipper.showNext();
					}else//ORDINARY SEARCH....
						search();
				}	
				break;

	//	case R.id.applyBtn:
			// rename the file with given name from editBox
			editBox = new EditText(mContext);
		//	editBox = (EditText) findViewById(R.id.editBox);
			String name = editBox.getText().toString();
			// String ext = extBox.getText().toString();
			if (name.length()>0){
				if (CREATE_FILE) {
					// CREATE A SIMPLE FOLDER,if create flag == 2
					//create a hidden folder if create flag == 1
					if (CREATE_FLAG == 2 || CREATE_FLAG == 1) {
						if(CREATE_FLAG == 1){
							//create hidden folder.....
							if(name.startsWith("."))
								name = name.substring(1, name.length());
							name = "/." + name;
						}	
						else//create simple folder.....
							name = "/" + name;
						if (CURRENT_ITEM == 1) {							
							try{
								/**
								 * TODO create folders that needs root permisionns
								 * to create....
								 
								if(!RootManager.getCurrentDirectory().equals("/"))
									name = RootManager.getCurrentDirectory() + name;
								boolean exists = new File(name).exists();
								if(exists)
									Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
								else{
									boolean created = new File(name).mkdirs();
									if(created){
										Toast.makeText(mContext, R.string.item_created, Toast.LENGTH_SHORT).show();
										rootItemList = rootManager.getList();
										rootAdapter.notifyDataSetChanged();
									}else if(!created)
										Toast.makeText(mContext, R.string.creation_failed, Toast.LENGTH_SHORT).show();
								}
							}catch(IllegalStateException e){
								Toast.makeText(mContext, R.string.creation_failed, Toast.LENGTH_SHORT).show();
							}
						} else if (CURRENT_ITEM == 2) {
							name = SDManager.getCurrentDirectory() + name;
							if(new File(name).exists())
								Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
							else{
								boolean created = new File(name).mkdirs();
								if(created){
									Toast.makeText(mContext, R.string.item_created, Toast.LENGTH_SHORT).show();
									sdItemsList = sdManager.getList();
									sdAdapter.notifyDataSetChanged();
								}else if(!created)
									Toast.makeText(mContext, R.string.creation_failed, Toast.LENGTH_SHORT).show();
							}
						}
						RENAME_COMMAND = CREATE_FILE = SEARCH_FLAG = CUT_COMMAND = COPY_COMMAND = false;
					}
					// CREATE AN EMPTY FILE
					else if (CREATE_FLAG == 3) {
						name = editBox.getText().toString();
						if(CURRENT_ITEM == 1){
							
							if(!RootManager.getCurrentDirectory().equals("/"))
								name = RootManager.getCurrentDirectory() + "/" + name;
							else
								name = "/" + name;
							File chFile = new File(RootManager.getCurrentDirectory());
							if(new File(name).exists())//file exists....
								Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
							else{
								if(!(chFile.canWrite())){
									/*try{
										//needs root permission....
										LinuxShell.execute("cat > " + name);
										setAdapter(CURRENT_ITEM);
									}catch(Exception e){
										Toast.makeText(mContext,getString(R.string.creation_failed),Toast.LENGTH_LONG).show();
									}
									Toast.makeText(mContext,getString(R.string.creation_failed),Toast.LENGTH_LONG).show();
								}else{
									chFile = new File(name);
									try {
										chFile.createNewFile();
										Toast.makeText(mContext,getString(R.string.item_created), Toast.LENGTH_LONG).show();
										
										//adding item to unknown file list....
										Item it = new Item(chFile, Constants.UNKNOWN, Utils.misType, "");
										Utils.mis.put(it.getPath(), it);
										Utils.misKey.put(""+Utils.misCounter++, it.getPath());
										Utils.update_Needed = true;
									} catch (IOException e) {
										// TODO Auto-generated catch block
										Toast.makeText(mContext,getString(R.string.creation_failed),Toast.LENGTH_LONG).show();
									}
								}
							}
						}else if(CURRENT_ITEM == 2){
							name = SDManager.getCurrentDirectory() + "/" + name;
							File chFile = new File(SDManager.getCurrentDirectory());
							if(new File(name).exists())
								//file exists....
								Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
							else{
								chFile = new File(name);
								try {
									chFile.createNewFile();
									Toast.makeText(mContext,getString(R.string.item_created), Toast.LENGTH_LONG).show();
									
									//adding item to unknown file list....
									Item it = new Item(chFile, Constants.UNKNOWN, Utils.misType, "");
									Utils.mis.put(it.getPath(), it);
									Utils.misKey.put(""+Utils.misCounter++, it.getPath());
									Utils.update_Needed = true;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									Toast.makeText(mContext,getString(R.string.creation_failed),Toast.LENGTH_LONG).show();
								}
							}
						}
					}
					// AFTER CREATING FILES OR FOLDER AGAIN FLIPPER IS SET TO
					// MAIN MENU
					mVFlipper.showNext();
				}
				
				// THIS FLIPPER IS SET FOR RENAMING
				else if (RENAME_COMMAND) {
					if (CURRENT_ITEM == 0) {
						name = getPathWithoutFilename(file0.getFile()).getPath() + "/"+ name;
						//file with same name already exists....
						if(new File(name).exists())
							Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
						
						//try to rename the file.....
						else
							try{
								boolean locked = false;
								boolean fav = false;
								//after renaming file ,if it was locked again locking it....
								if(file0.isLocked()){
									locked = true;
									Constants.db.deleteLockedNode(file0.getPath());
									Constants.db.insertNodeToLock(name);
								}
								
								//after renaming file,if it was a favorite item again
								//adding it to favorite item....
								if(file0.isFavItem()){
									fav = true;
									Constants.db.deleteFavItem(file0.getPath());
									Constants.db.insertNodeToFav(name);
								}
								
								file0.getFile().renameTo(new File(name));
								
								if(fav)
									file0.setFavStatus(true);
								
								if(locked)
									file0.setLockStatus(true);
								
								Toast.makeText(mContext,getString(R.string.renamed)	+ " " + new File(name).getName(),Toast.LENGTH_SHORT).show();
							
							}catch(Exception e){
								Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
							}
						
					} else if (CURRENT_ITEM == 1) {
						name = RootManager.getCurrentDirectory() + "/" + name;
						try {
							/*
							 * yet to implement rename command for root files
							 
							
							//file with same name already exists....
							if(new File(name).exists())
								Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
							
							//try to rename the file.....
							else if(file.canRead() && file.canWrite()){
									boolean locked = false;
									boolean fav = false;
									//after renaming file ,if it was locked again locking it....
									if(file.isLocked()){
										locked = true;
										Constants.db.deleteLockedNode(file.getPath());
										Constants.db.insertNodeToLock(name);
									}
									
									//after renaming file,if it was a favorite item again
									//adding it to favorite item....
									if(file.isFavItem()){
										fav = true;
										Constants.db.deleteFavItem(file.getPath());
										Constants.db.insertNodeToFav(name);
									}
									
									file.getFile().renameTo(new File(name));
									
									if(fav)
										file.setFavStatus(true);
									
									if(locked)
										file.setLockStatus(true);
									Toast.makeText(mContext,getString(R.string.renamed)	+ " " + new File(name).getName(),Toast.LENGTH_SHORT).show();
								}
							//failed to get enough permissions to rename the file.....
							else
									Toast.makeText(mContext, R.string.faile_to_rename, Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							//tried to rename a file that requires root permission.....
							Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
						}
					} else if (CURRENT_ITEM == 2) {
						name = SDManager.getCurrentDirectory() + "/" + name;
						
						//file with same name already exists....
						if(new File(name).exists())
							Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
						
						//try to rename the file.....
						else
							try{
								boolean locked = false;
								boolean fav = false;
								
								file2.getFile().renameTo(new File(name));
								
								//after renaming file ,if it was locked again locking it....
								if(file2.isLocked()){
									locked = true;
									Constants.db.deleteLockedNode(file2.getPath());
									Constants.db.insertNodeToLock(name);
								}
								
								//after renaming file,if it was a favorite item again
								//adding it to favorite item....
								if(file2.isFavItem()){
									fav = true;
									Constants.db.deleteFavItem(file2.getPath());
									Constants.db.insertNodeToFav(name);
								}
								if(fav)
									file2.setFavStatus(true);
								
								if(locked)
									file2.setLockStatus(true);
								Toast.makeText(mContext,getString(R.string.renamed)	+ " " + new File(name).getName(),Toast.LENGTH_SHORT).show();
							}catch(Exception e){
								Toast.makeText(mContext, R.string.fileexists, Toast.LENGTH_SHORT).show();
							}						
					}
					// AFTER RENAMING THE FOLDER OR FILES THE FLIPPER IS SET
					// AGAIN TO MAIN MENU
					mVFlipper.showNext();
				}
				setAdapter(CURRENT_ITEM);
				RENAME_COMMAND = CREATE_FILE = SEARCH_FLAG = CUT_COMMAND = COPY_COMMAND = false;
			} else if (name.length() == 0)
				Toast.makeText(mContext, R.string.entervalidname,Toast.LENGTH_SHORT).show();
			break;

	//	case R.id.homeDirBtn:
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
					
				}
			} else {
				HOME_DIRECTORY = PATH;
				edit.putString("HOME_DIRECTORY", PATH);
				edit.commit();
				new ErrorDialogs(mContext, size.x*8/9, "homeError");
			}

			break;

		//case R.id.jumpToBtn:

			if (CURRENT_ITEM == 0) {
				// IF CURRENT ITEM == 0
				// DISPLAYS LIST THAT IS APPLICABLE TO ONLY ALL FILE PANEL

				
			} else {
				// IF CURRENT ITMEM !=0
				// This option allows user to directly go to specified directory
				// from any directory
							}
			break;

		//case R.id.addBtn:

			if (CURRENT_ITEM == 0) {
				//showDefaultApps(v);
			} else {
			//	NEW_OPTIONS(v);
			}

			break;

	//	case R.id.backupAll:
						break;

		//case R.id.cleanBackups:
			// THIS BUTTON DISPLAYS TWO OPTIONS -1.TO DELETED THE BACKUPS
			// 2. DELETE THE FLASHABLE ZIPS CREATED
						break;

		case R.id.zipItBtn:
			if (CURRENT_ITEM == 3)
				new ErrorDialogs(mContext, size.x*8/9, "FlashableZips");
			break;
		}*/
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
			
			
			if ((SEARCH_FLAG || RENAME_COMMAND || CREATE_FILE)
					&& (CURRENT_ITEM == 1 || CURRENT_ITEM == 2 || CURRENT_ITEM == 0)) {
				setAdapter(CURRENT_ITEM);
				mVFlipper.showNext();
				SEARCH_FLAG = RENAME_COMMAND = COPY_COMMAND = CUT_COMMAND = CREATE_FILE = false;
				/*if (elementInFocus) {
					// LIST_VIEW_3D.setAdapter(adapter);
					elementInFocus = false;
					LIST_VIEW_3D.setVisibility(View.GONE);
					FILE_GALLEY.setVisibility(View.VISIBLE);
					indicator.notifyDataSetChanged();
				}*/
			} else if (CURRENT_ITEM == 0 && elementInFocus) {
				elementInFocus = false;
				//resetPager();
				LIST_VIEW_3D.setVisibility(View.GONE);
				FILE_GALLEY.setVisibility(View.VISIBLE);
				mFlipperBottom.showNext();
				//update ui after setting file gallery view....
				if(!Utils.loaded)
					Utils.updateUI();
				Utils.update_fav();
				indicator.notifyDataSetChanged();
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
						if (elementInFocus) {
							mFlipperBottom.showNext();
						} else if (!elementInFocus) {
							mFlipperBottom.showNext();
						}
					}
					mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
				}
			}else if(ZIP_ROOT && CURRENT_ITEM==1){
				//POPING OUT THE ZIP PATH.....
				//RootManager.nStack.pop();
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
				//RootManager.nStack.pop();
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
				//RootManager.nStack.pop();
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
			else if (CURRENT_ITEM == 1) {
				//RootManager.nStack.pop();
				setAdapter(1);
				//file = new Item(new File(RootManager.getCurrentDirectory()),null, null, null);
			} else if (CURRENT_ITEM == 1) {
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
				
				//SDManager.nStack.pop();
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
				//SDManager.nStack.pop();
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
				//SDManager.nStack.pop();
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
			}else if (CURRENT_ITEM == 2  ) {
				//.nStack.pop();
				setAdapter(2);
				//file2 = new Item(new File(SDManager.getCurrentDirectory()), null, null, null);
			} else if (CURRENT_ITEM == 2 ) {
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
					
					final ConcurrentHashMap<String , Item> list = new ConcurrentHashMap<String , Item>();
					final ConcurrentHashMap<String , String> key = new ConcurrentHashMap<String , String>();
					final ConcurrentHashMap<String , Item> lsTouse = getCategoryList(fPos);
					final ConcurrentHashMap<String , String> keysTouse = getKeys(fPos);
					
					if(searchList == null)
						searchList = new ArrayList<Item>();
					else
						searchList.clear();
				//	LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
				//	a.setVisibility(View.GONE);
					// Search Flipper is loaded
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
							if(CURRENT_ITEM == 0){
								list.clear();
								key.clear();
							}else
								searchList.clear();
						}
						
						@Override
						public void afterTextChanged(final Editable ed) {
							// TODO Auto-generated method stub
							new AsyncTask<Void, Void, Void>() {
								int counter = 0;
								@Override
								protected void onPostExecute(Void result) {
									// TODO Auto-generated method stub
									
									try{
										if(CURRENT_ITEM == 2)
											root.setAdapter(new SDAdapter(mContext,searchList));
										else if(CURRENT_ITEM == 1)
											simple.setAdapter(new RootAdapter(mContext,searchList));
										else if(CURRENT_ITEM == 0){
											//LIST_VIEW_3D.setAdapter(new FileGalleryAdapter(mContext, list, key));
										}	
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
										int len = lsTouse.size();
										for(int i=0;i<len;++i){
											try{
												Item itm = lsTouse.get(keysTouse.get(""+i));
												if(itm.getName().toLowerCase().contains(text)){
													list.put(itm.getPath(), itm);
													key.put(""+counter++, itm.getPath());
												}	
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
		{
				Toast.makeText(mContext, R.string.firstselectsomefiles,Toast.LENGTH_SHORT).show();

		} 
			//MultiModeDisabled(str);
	}

	/**
	 * FUNCTION MAKE 3d LIST VIEW VISIBLE OR GONE AS PER REQUIREMENT
	 * 
	 * @param mode
	 * @param con
	 */
	public static void load_FIle_Gallery(final int mode) {
		if(!elementInFocus)
			elementInFocus = true;
		else{
			mFlipperBottom.showNext();
		}
		try{
			Utils.pause();
			if(mode == 0)
				element = new FileGalleryAdapter(mContext, Utils.fav , getKeys(mode));
			else if (mode == 1)
				element = new FileGalleryAdapter(mContext, Utils.music , getKeys(mode));
			else if (mode == 2)
				element = new FileGalleryAdapter(mContext, Utils.apps , getKeys(mode));
			else if (mode == 5)
				element = new FileGalleryAdapter(mContext, Utils.doc , getKeys(mode));
			else if (mode == 3)
				element = new FileGalleryAdapter(mContext, Utils.img , getKeys(mode));
			else if (mode == 4)
				element = new FileGalleryAdapter(mContext, Utils.vids , getKeys(mode));
			else if (mode == 6)
				element = new FileGalleryAdapter(mContext, Utils.zip , getKeys(mode));
			else if (mode == 7)
				element = new FileGalleryAdapter(mContext, Utils.mis , getKeys(mode));
			LIST_VIEW_3D.setAdapter(element);
		}catch(Exception e){
			elementInFocus = false;
		}
		if(elementInFocus){
			FILE_GALLEY.setVisibility(View.GONE);
			LIST_VIEW_3D.setVisibility(View.VISIBLE);
			mFlipperBottom.showPrevious();
			indicator.notifyDataSetChanged();
		}
	}
	
	/**
	 * Function to get exact item from the selected 
	 * category of array list in file gallery...
	 * @param mode category selected....
	 * @param pos which position from list view....
	 * @return
	 */
	private static Item getItemFromCategory(int mode , int pos){
		if (mode == 1)
			return Utils.music.get(Utils.musicKey.get(""+pos));
		else if (mode == 2)
			return Utils.apps.get(Utils.appKey.get(""+pos));
		else if (mode == 5)
			return Utils.doc.get(Utils.docKey.get(""+pos));
		else if (mode == 3)
			return Utils.img.get(Utils.imgKey.get(""+pos));
		else if (mode == 4)
			return Utils.vids.get(Utils.videoKey.get(""+pos));
		else if (mode == 6)
			return Utils.zip.get(Utils.zipKey.get(""+pos));
		else if (mode == 7)
			return Utils.mis.get(Utils.misKey.get(""+pos));
		else 
			return Utils.fav.get(Utils.favKey.get(""+pos));
	}
	
	/**
	 * Function get array list of currently selected files category
	 * in file gallery....
	 * @param mode category number....
	 * @return list of files in that category....
	 */
	private static ConcurrentHashMap<String , Item> getCategoryList(int mode){
		if (mode == 1)
			return Utils.music;
		else if (mode == 2)
			return Utils.apps;
		else if (mode == 5)
			return Utils.doc;
		else if (mode == 3)
			return Utils.img;
		else if (mode == 4)
			return Utils.vids;
		else if (mode == 6)
			return Utils.zip;
		else if (mode == 7)
			return Utils.mis;
		else 
			return Utils.fav;
	}
	
	/**
	 * Function get keys for the list of files selected
	 * @param mode
	 * @return
	 */
	private static ConcurrentHashMap<String , String> getKeys(int mode){
		if(mode == 0)
			return Utils.favKey;
		
		if(mode == 1)
			return Utils.musicKey;
		
		if(mode == 2)
			return Utils.appKey;
		
		if(mode == 3)
			return Utils.imgKey;
		
		if(mode == 4)
			return Utils.videoKey;
		
		if(mode == 5)
			return Utils.docKey;
		
		if(mode == 6)
			return Utils.zipKey;
		
		return Utils.misKey;
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
				
				

			} else if (CURRENT_ITEM == 1) {
				if (longC)
					dest = file.getFile().getAbsolutePath();
				
				

			}
			COPY_COMMAND = CUT_COMMAND = CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = false;
		} else if (MULTIPLE_COPY_GALLERY || MULTIPLE_CUT_GALLERY) {
			/*
			 * THIS CONDITION HANDLES PASTING OF FILES FROM GALLERY
			 */
			if (CURRENT_ITEM == 2) {
				if (MULTIPLE_COPY_GALLERY) {
					//new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, SDManager.getCurrentDirectory(),	false);
				} else if (MULTIPLE_CUT_GALLERY) {
					//new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, SDManager.getCurrentDirectory(),	true);
				}
			} else if (CURRENT_ITEM == 1) {
				if (MULTIPLE_COPY_GALLERY) {
				//	new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, RootManager.getCurrentDirectory(),false);
				} else if (MULTIPLE_CUT_GALLERY) {
					//new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, RootManager.getCurrentDirectory(),	true);
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
					//nAppAdapter = new AppAdapter(mContext,R.layout.row_list_1, nList);
					if (MULTI_SELECT_APPS) {
						//nAppAdapter.MULTI_SELECT = true;
					}else if (!MULTI_SELECT_APPS) {
						//nAppAdapter.MULTI_SELECT = false;
					}
					APP_LIST_VIEW.setAdapter(nAppAdapter); 
					APP_LIST_VIEW.setSelection(pos);
					
					//notifying the file gallery to update....
					Utils.update_Needed = true;
				}else if (ACTION.equalsIgnoreCase("FQ_DELETE")) {
					if(delete_from_slider_menu){
						//file deletion was performed from left slide menu...
						//all files were deleted from a category....
						if(CURRENT_ITEM == 0){
							if(elementInFocus)
								element.notifyDataSetChanged();
							Utils.updateUI();
						}	
						else
							Utils.update_Needed = true;
						setAdapter(CURRENT_ITEM);
					}
					else if(CURRENT_ITEM == 1){
						setAdapter(1);
						//file gallery has to be updated after delete operation....
						Utils.update_Needed = true;
					}else if(CURRENT_ITEM == 2){
						setAdapter(2);					
						//file gallery has to be updated after delete operation....
						Utils.update_Needed = true;
					}
					else if(CURRENT_ITEM == 0){
						
							
						Utils.updateUI();						
					}					
				} else if (ACTION.equalsIgnoreCase("FQ_FLASHZIP")) {
					// FLASHABLE ZIP DIALOG IS FIRED FROM HERE
					 new CreateZipApps(mContext, size.x*8/9, nList);
				} else if (ACTION.equalsIgnoreCase("FQ_DROPBOX_STARTLINK")) {
					// LINK A USER....
					Toast.makeText(mContext, R.string.linkanaccount,Toast.LENGTH_SHORT).show();
				//	NEW_OPTIONS(indicator);
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
									//RootManager.nStack.push(value);
									setAdapter(CURRENT_ITEM);
									mViewPager.setCurrentItem(1);
								}else
									new MasterPassword(mContext, size.x*8/9, tempItem, preferences, Constants.MODES.G_OPEN);
								
							} else if (CURRENT_ITEM == 2){
								if(!tempItem.isLocked()){
									//SDManager.nStack.push(value);
									setAdapter(2);
									mViewPager.setCurrentItem(2, true);
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
						//SDManager.nStack.push("/ -> Zip");
					}	
					else{
						ZIP_ROOT = true;
						//RootManager.nStack.push("/ -> Zip");
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
						//SDManager.nStack.push("/ -> Rar");
					}else{
						RAR_ROOT = true;
						//RootManager.nStack.push("/ -> Rar");
					}
					setRarAdapter();
				}else if(ACTION.equalsIgnoreCase("FQ_TAR_OPEN")){
					//CHECKING WHETHER ANY ARCHIVE IS ALREADY OPENED OR NOT...
					//IF OPENED THEN CLOSING THE ARCHIVE AND OPENING THE NEW ARCHIVE REQUEST....
					cleanStack(it.getStringExtra("open_path"));
					
					if(CURRENT_ITEM==0||CURRENT_ITEM==2){
						TAR_SD = true;
						//SDManager.nStack.push("/ -> Tar");
					}else if(CURRENT_ITEM==1){
						TAR_ROOT = true;
						//RootManager.nStack.push("/ -> Tar");
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
					cleanStack(null);
					if(CURRENT_ITEM == 1){
						simple.setAdapter(new DBoxAdapter(mContext, DBoxManager.dListRoot));
					}
					else if(CURRENT_ITEM == 2){
						root.setAdapter(new DBoxAdapter(mContext, DBoxManager.dListSimple));
					}
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
						}else if(Constants.activeMode == Constants.MODES.DISABLE_NEXT_RESTART){
							//disabling the lock after verifying the password.... 
							Constants.disable_lock = true;
							Toast.makeText(mContext, R.string.lock_disabled, Toast.LENGTH_SHORT).show();
						}
						else if(CURRENT_ITEM==2){
							
							//opening task here...
							if(Constants.activeMode==Constants.MODES.OPEN 
									|| Constants.activeMode == Constants.MODES.G_OPEN) {
								if (!file2.isDirectory()) {
									new OpenFileDialog(mContext, Uri.parse(file2.getPath()), size.x*8/9);
								} else if (file2.isDirectory()) {
									//SDManager.nStack.push(file2.getPath());
									
									//using separate handler and thread only because
									//setAdapter() function was causing app to force close....
									final Handler handle = new Handler(){
										@Override
										public void handleMessage(Message msg) {
											// TODO Auto-generated method stub
											super.handleMessage(msg);
											mViewPager.setAdapter(mSectionsPagerAdapter);
											mViewPager.setCurrentItem(2);
											indicator.setViewPager(mViewPager);
										}
									};
									
									Thread thr = new Thread(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											sdItemsList = sdManager.getList();
											handle.sendEmptyMessage(0);
										}
									});
									thr.start();
								}
							}
							//delete the provided item after password verification...
							else if(Constants.activeMode == Constants.MODES.DELETE){
								Constants.db.deleteLockedNode(file2.getPath());
								deleteMethod(file2);
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
								//SDManager.nStack.push(HOME_DIRECTORY);
								setAdapter(2);
							}
						}else if(CURRENT_ITEM==1){
							
							//opening task here...
							if(Constants.activeMode == Constants.MODES.OPEN){
								if(!file.isDirectory()){
									new OpenFileDialog(mContext, Uri.parse(file.getPath()), size.x*8/9);
								}else if(file.isDirectory()){
								//	RootManager.nStack.push(file.getPath());
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
									//RootManager.nStack.push(f.getPath());
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
							//	RootManager.nStack.push(HOME_DIRECTORY);
								setAdapter(1);
							}
						}else if(CURRENT_ITEM == 0){
							//handling the locked items post request here for file gallery...
							
							//opening the locked item after passwd verification...
							if(Constants.activeMode == Constants.MODES.OPEN){
								if(!file0.isDirectory())
									new OpenFileDialog(mContext, Uri.parse(file0.getPath()), size.x*8/9);
								else{
									//a folder was requested to open from favorite tile...
									//SDManager.nStack.push(file0.getPath());
									setAdapter(2);
								}
							}
							
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
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.lock_icon_hd));
								Constants.db.insertNodeToLock(sdItemsList.get(id).getFile().getAbsolutePath());
								sdItemsList.get(id).setLockStatus(true);
								Toast.makeText(mContext, R.string.itemlocked, Toast.LENGTH_SHORT).show();
							}else if(sdItemsList.get(id).isLocked()){
								//after password verification was successful,unlock the file...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_icon_hd));
								Constants.db.deleteLockedNode(sdItemsList.get(id).getFile().getPath());
								sdItemsList.get(id).setLockStatus(false);
								Toast.makeText(mContext, R.string.itemunlocked, Toast.LENGTH_SHORT).show();
							}
						}else if(CURRENT_ITEM==1){
							if(!rootItemList.get(id).isLocked()){
								//file is not locked ....
								//lock the file...
								
								//this condition is true when user has not up the password and tried to lock the item...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.lock_icon_hd));
								Constants.db.insertNodeToLock(rootItemList.get(id).getFile().getAbsolutePath());
								rootItemList.get(id).setLockStatus(true);
								Toast.makeText(mContext, R.string.itemlocked, Toast.LENGTH_SHORT).show();
							}else if(rootItemList.get(id).isLocked()){
								//after password verification was successful,unlock the file...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_icon_hd));
								Constants.db.deleteLockedNode(rootItemList.get(id).getFile().getPath());
								rootItemList.get(id).setLockStatus(false);
								Toast.makeText(mContext, R.string.itemunlocked, Toast.LENGTH_SHORT).show();
							}
						}else if(CURRENT_ITEM == 0){
							if(!getCategoryList(fPos).get(Constants.lockID).isLocked()){
								//file is not locked...
								//lock the file...
								
								//this condition is true when user has not up the password and tried to lock the item...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.lock_icon_hd));
								Constants.db.insertNodeToLock(getCategoryList(fPos).get(Constants.lockID).getFile().getAbsolutePath());
								getCategoryList(fPos).get(Constants.lockID).setLockStatus(true);
								Toast.makeText(mContext, R.string.itemlocked, Toast.LENGTH_SHORT).show();
							}else if(getCategoryList(fPos).get(Constants.lockID).isLocked()){
								//after password verification was successful,unlock the file...
								Constants.lock.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_icon_hd));
								Constants.db.deleteLockedNode(getCategoryList(fPos).get(Constants.lockID).getFile().getPath());
								getCategoryList(fPos).get(Constants.lockID).setLockStatus(false);
								Toast.makeText(mContext, R.string.itemunlocked, Toast.LENGTH_SHORT).show();
							}
						}
					}
				}else if(ACTION.equalsIgnoreCase("FQ_COPY")){
					if(CURRENT_ITEM == 0){
						if(elementInFocus)
							element.notifyDataSetChanged();
						Utils.updateUI();
					}else{
						setAdapter(CURRENT_ITEM);
						//after copying or moving files updating the file gallery....
						Utils.update_Needed = true;
					}					
				}else if(ACTION.equalsIgnoreCase("FQ_ARCHIVE_CREATED")){
					if(CURRENT_ITEM == 0){
						if(elementInFocus)
							element.notifyDataSetChanged();
						Utils.updateUI();
					}
					else{
						setAdapter(CURRENT_ITEM);
						//after creating zipped file updating the file gallery....
						Utils.update_Needed = true;
					}					
				}
			}
		};
				
		IntentFilter filter = new IntentFilter();
		filter.addAction("FQ_BACKUP");
		filter.addAction("FQ_DELETE");
		filter.addAction("FQ_FLASHZIP");
		filter.addAction("FQ_G_OPEN");
		filter.addAction("FQ_ZIP_OPEN");
		filter.addAction("FQ_EXTRACT_PATH");
		filter.addAction("FQ_RAR_OPEN");
		filter.addAction("FQ_TAR_OPEN");
		filter.addAction("FQ_7Z_OPEN");
		filter.addAction(Intent.ACTION_UNINSTALL_PACKAGE);
		filter.addAction("FQ_DROPBOX_OPEN_FOLDER");
		filter.addAction("FQ_COPY");
		filter.addAction("FQ_FILE_LOCKED_OR_UNLOCKED");
		filter.addAction("FQ_ARCHIVE_CREATED");
		this.registerReceiver(RECEIVER, filter);
	}
	
	
	/**
	 * PERFORMS SEARCH INSIDE OF ZIP ARCHIVE FILE...
	 */
	private void zipSearch() {
		// TODO Auto-generated method stub
		zSearch = new ArrayList<ZipObj>();
		try{
			//LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
		//	a.setVisibility(View.GONE);
			// Search Flipper is loaded
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
			//LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
			//a.setVisibility(View.GONE);
			// Search Flipper is loaded
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
		//	LinearLayout a = (LinearLayout) findViewById(R.id.applyBtn);
			//a.setVisibility(View.GONE);
			// Search Flipper is loaded
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
		final Dialog progDial = new Dialog(mContext, Constants.DIALOG_STYLE);
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
					//if(CURRENT_ITEM==1)
				//		RootManager.nStack.pop();
					//else if(CURRENT_ITEM==2)
						//SDManager.nStack.pop();
					resetPager();
					if(progDial.isShowing())
						progDial.dismiss();
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
		final Dialog progDial = new Dialog(mContext, Constants.DIALOG_STYLE);
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
					//if(CURRENT_ITEM==1)
				//		RootManager.nStack.pop();
				//else if(CURRENT_ITEM==2)
						//SDManager.nStack.pop();
					resetPager();
					if(progDial.isShowing())
						progDial.dismiss();
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
		final Dialog progDial = new Dialog(mContext, Constants.DIALOG_STYLE);
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
				//	if(CURRENT_ITEM==1)
					//	RootManager.nStack.pop();
					//else if(CURRENT_ITEM==2)
						//SDManager.nStack.pop();
					resetPager();
					if(progDial.isShowing())
						progDial.dismiss();
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
			//while(RootManager.nStack.peek().contains("->"))
			//	RootManager.nStack.pop();
			RAR_ROOT = TAR_ROOT= ZIP_ROOT = false;
			if(p != null)
				file = new Item(new File(p), null, null, null);
			//else
			//	file = new Item(new File(RootManager.nStack.peek()), null, null, null);
		}else if(CURRENT_ITEM==2&&(RAR_SD||TAR_SD||ZIP_SD)){
			//while(SDManager.nStack.peek().contains("->"))
		//.nStack.pop();
		//	RAR_SD = ZIP_SD = TAR_SD = false;
			if(p != null)
				file2 = new Item(new File(p), null, null, null);
			else{}
				//file2 = new Item(new File(SDManager.nStack.peek()), null, null, null);
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
		
		//setting the device name here....
		TextView devId = (TextView)findViewById(R.id.dev_id);
		String dev = Build.MODEL;
		String man = Build.MANUFACTURER;
		if(dev.length() == 0 || dev == null)
			dev = getString(R.string.unknown);
		else{
			if(!dev.contains(man))
				dev = man + " " + dev;
			char a = dev.charAt(0);
			if(!Character.isUpperCase(a)){
				dev = Character.toUpperCase(a) + dev.substring(1);
			}
		}		
		devId.setText(dev);
				/*
		final ActionSlideExpandableListView lsView = (ActionSlideExpandableListView)findViewById(R.id.expandable_list);
				
				String[] values = getResources().getStringArray(R.array.slideList);
				lsView.setAdapter(new ExpandableAdapter(this,R.layout.expandable_list_item,
						R.id.expandable_toggle_button,values));
				
				/**
				 * click listener for single item inside slide menu list...
				 
				lsView.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
					@Override
					public void onClick(View itemView, View clickedView, int position) {
						// TODO Auto-generated method stub
						ConcurrentHashMap<String , Item> itemList = null;
						ConcurrentHashMap<String , String> keys = null;
						keys = null;
						if(position == 0)
							itemList = Utils.fav;
						else if(position==1){
							itemList = (Utils.music);
							keys = Utils.musicKey;
						}	
						else if(position==2){
							itemList = (Utils.apps);
							keys = Utils.appKey;
						}	
						else if(position==5){
							itemList = (Utils.doc);
							keys = Utils.docKey;
						}	
						else if(position==3){
							itemList = (Utils.img);
							keys = Utils.imgKey;
						}	
						else if(position==4){
							itemList = (Utils.vids);
							keys = Utils.videoKey;
						}	
						else if(position==6){
							itemList = (Utils.zip);
							keys = Utils.zipKey;
						}	
						else if(position==7){
							itemList = (Utils.mis);
							keys = Utils.misKey;
						}	
												
						//tells that slide menu was used to delete files from panel
						delete_from_slider_menu = true;
						
						/**
						 * switching to different actions of buttons in expanded list....
						 
						switch(clickedView.getId()){
							case R.id.button_delete:
									if(position == 0){
										//only deleting favorite items from DB....
										if(Constants.db.deleteAllFavItem()){
											Toast.makeText(mContext, R.string.allfavdeleted, Toast.LENGTH_SHORT).show();
											Utils.notifyFavFileDelete(fPos);
										}	
										break;
									}	
									new DeleteFiles(mContext, size.x*8/9, itemList,keys, getResources().getString(R.string.confirmdeletion));
									break;
									
							case R.id.button_move_all:
									//tempList = itemList;
									new GetMoveLocation(mContext, size.x*8/9 , itemList , keys);
									break;
							case R.id.button_zip_all:
									new ZipGallery(mContext, size.x*8/9, itemList, keys , position);
									break;
						}
					}
				}, R.id.button_delete,R.id.button_zip_all,R.id.button_move_all);
				
			//expand or collapse file gallery sub items....	
			LinearLayout fg = (LinearLayout)findViewById(R.id.fg);
			fg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(lsView.getVisibility() == View.GONE){
						expandListView(lsView);
						lsView.setVisibility(View.VISIBLE);
					}	
					else
						lsView.setVisibility(View.GONE);
				}
			});		*/				
	}
	
	/**
	 * hack to expand the list view inside the scroll view....
	 * @param ls
	 */
	private void expandListView(ListView myListView){
		ListAdapter myListAdapter = myListView.getAdapter();
	    if (myListAdapter == null) {
	        // do nothing return null
	        return;
	    }
	    // set listAdapter in loop for getting final size
	    int totalHeight = 0;
	    for (int size = 0; size < myListAdapter.getCount(); size++) {
	        View listItem = myListAdapter.getView(size, null, myListView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }
	    // setting listview item in adapter
	    ViewGroup.LayoutParams params = myListView.getLayoutParams();
	    params.height = totalHeight
	            + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
	    myListView.setLayoutParams(params);
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
	 * this function checks for update for File Quest
	 * and makes a notification to download link
	 * in playstore.... 
	 */
	private void update_checker() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, R.string.checking_update, Toast.LENGTH_SHORT).show();
		
		final Handler hand = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 1://update available....
					{
						NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
						mBuilder.setSmallIcon(R.drawable.file_quest_icon);																					
						mBuilder.setContentTitle(getString(R.string.app_name));
						mBuilder.setContentText(getString(R.string.update_avail));
						
						mBuilder.setTicker(getString(R.string.update_avail));
						
						Toast.makeText(mContext, R.string.update_avail, Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("market://details?id=org.anurag.file.quest"));
						
						
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
											
						PendingIntent pendint = PendingIntent.getActivity(mContext, 900, intent, 0);
						mBuilder.setContentIntent(pendint);
				
						mBuilder.setAutoCancel(true);
						
						NotificationManager notimgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						notimgr.notify(1, mBuilder.build());
					}
					break;
				case 2://no connectivity....
					Toast.makeText(mContext, R.string.nointernet, Toast.LENGTH_SHORT).show();
					break;
				case 3:
					//failed to check for update....
					Toast.makeText(mContext, R.string.failed_to_check_for_update, Toast.LENGTH_SHORT).show();
				}
			}			
		};
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo info = cm.getActiveNetworkInfo();
					if(!info.isConnected()){
						hand.sendEmptyMessage(2);
						return;
					}	
					Scanner scan = new Scanner(new URL("https://www.dropbox.com/s/x1gp7a6ozdvg81g/FQ_UPDATE.txt?dl=1").openStream());
					String update = scan.next();
					if(!update.equalsIgnoreCase(getString(R.string.version)))
						hand.sendEmptyMessage(1);
					scan.close();
				}catch(Exception e){
					hand.sendEmptyMessage(3);
				}
			}
		});
		th.start();
	}	
}