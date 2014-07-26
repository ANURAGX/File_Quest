/**
 * Copyright(c) 2013 ANURAG 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import org.anurag.compress.ArchiveEntryProperties;
import org.anurag.compress.CreateZip;
import org.anurag.compress.CreateZipApps;
import org.anurag.compress.ExtractZipFile;
import org.anurag.compress.ZipAdapter;
import org.anurag.compress.ZipManager;
import org.anurag.compress.ZipObj;
import org.anurag.gesture.AddGesture;
import org.anurag.gesture.G_Open;
import org.anurag.inherited.sony.ListView3D;
import org.anurag.inherited.sony.SimpleDynamics;
import org.ultimate.menuItems.AppProperties;
import org.ultimate.menuItems.BluetoothChooser;
import org.ultimate.menuItems.DeleteBackups;
import org.ultimate.menuItems.DeleteFlashable;
import org.ultimate.menuItems.FileProperties;
import org.ultimate.menuItems.GetHomeDirectory;
import org.ultimate.menuItems.MultiSendApps;
import org.ultimate.menuItems.MultipleCopyDialog;
import org.ultimate.menuItems.SelectApp;
import org.ultimate.menuItems.SelectedApp;
import org.ultimate.menuItems.SetLaunchDir;
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
import android.graphics.drawable.Drawable;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.abhi.animated.TransitionViewPager;
import com.abhi.animated.TransitionViewPager.TransitionEffect;
import com.viewpagerindicator.TitlePageIndicator;

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
	private static String zipPathSimple;
	private static String zipPathRoot;
	public static boolean ZIP_ROOT;	
	public static boolean ZIP_SIMPLE;
	private static ArrayList<ZipObj> zListSimple;
	private static ArrayList<ZipObj> zListRoot;	
	private static ZipObj zFileRoot;
	private static ZipObj zFileSimple;
	private static ArrayList<ZipObj> zSearch;
	
	
	static int fPos;
	private BroadcastReceiver RECEIVER;
	private static Dialog dialog;
	public static Point size;
	public static Context mContext;
	static int LIST_ANIM;
	static int PAGER_ANIM;
	private static ListView root;
	private static ListView simple;
	private static ListView3D LIST_VIEW_3D;
	private static LinearLayout FILE_GALLEY;

	static ArrayList<File> COPY_FILES;

	private static boolean MULTIPLE_COPY;
	private static boolean MULTIPLE_CUT;
	private static boolean MULTIPLE_COPY_GALLERY;
	private static boolean MULTIPLE_CUT_GALLERY;
	private ProgressBar sd;
	private TextView avail, total;
	private long av, to;
	private String sav, sto;
	public static ArrayList<String> EMPTY;
	private static ArrayList<String> EMPTY_APPS;

	private static boolean MULTI_SELECT_APPS;

	private static boolean ENABLE_ON_LAUNCH;
	private static String INTERNAL_PATH_ONE;
	private static String INTERNAL_PATH_TWO;
	private static String PATH;
	private static int SHOW_APP;
	private static String HOME_DIRECTORY;
	private static int SORT_TYPE;
	private static boolean SHOW_HIDDEN_FOLDERS;
	private static int CURRENT_PREF_ITEM;
	private float TRANSPRA_LEVEL;
	public static SharedPreferences.Editor edit;
	public static SharedPreferences preferences;
	private static boolean SEARCH_FLAG = false;
	private static int CREATE_FLAG = 0;
	private static boolean CREATE_FILE = false;
	private static String CREATE_FLAG_PATH;
	private WindowManager.LayoutParams params;

	private static ArrayList<File> searchList;
	private static boolean RENAME_COMMAND = false;
	private static EditText editBox;
	private static TransitionViewPager mViewPager;
	private static ViewFlipper mFlipperBottom;
	private static TitlePageIndicator indicator;
	private static RootAdapter RootAdapter;
	private static SimpleAdapter nSimple;
	private static RFileManager nRFileManager;
	private static SFileManager nSFManager;
	private static Intent LAUNCH_INTENT;
	private static boolean COPY_COMMAND = false;
	private static boolean CUT_COMMAND = false;
	private static int CURRENT_ITEM = 0;
	/**
	 * Media Panel Variables
	 */
	private static MediaElementAdapter element;
	private static ArrayList<File> mediaFileList;
	private static boolean elementInFocus = false;
	private static int pos = 0;
	private static ArrayList<File> nFiles;
	private static ArrayList<File> sFiles;
	private static AppManager nManager;

	private static ArrayList<ApplicationInfo> nList;
	private static ApplicationInfo info;
	private static AppAdapter nAppAdapter;
	private static ListView APP_LIST_VIEW;
	private static File file;
	private static File file2;
	private static File file0;
	private static boolean mUseBackKey = false;
	private static ViewFlipper mVFlipper;
	private static int LAST_PAGE;
	static boolean LongClick = false;
	static boolean error;

	boolean added = false;
	String id;
	static int rSize = 0;
	static int sSize = 0;
	static int mSize = 0;
	

	@SuppressWarnings("static-access")
	@SuppressLint({ "NewApi", "SdCardPath" })
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
		RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = preferences.getInt("FOLDER_TYPE", 0);
		HOME_DIRECTORY = preferences.getString("HOME_DIRECTORY", null);
		ENABLE_ON_LAUNCH = preferences.getBoolean("ENABLE_ON_LAUNCH", false);
		LIST_ANIM = preferences.getInt("LIST_ANIM", 8);
		PAGER_ANIM = preferences.getInt("PAGER_ANIM", 3);
		edit = preferences.edit();

		try {
			new File("/sdcard/File Quest/").mkdir();
		} catch (Exception e) {

		}

		
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
		
		zipPathRoot = null;
		zipPathSimple = null;
		
		ZIP_SIMPLE = ZIP_ROOT = SEARCH_FLAG = RENAME_COMMAND = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = CREATE_FILE = false;
		fPos = 0;
		params = this.getWindow().getAttributes();
		size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);

		setContentView(R.layout.fargment_ui);

		EMPTY = new ArrayList<String>();
		EMPTY_APPS = new ArrayList<String>();
		// searchList = new ArrayList<File>();
		// mediaFileList = new ArrayList<File>();

		editBox = (EditText) findViewById(R.id.editBox);

		sd = (ProgressBar) findViewById(R.id.space_indicator);
		avail = (TextView) findViewById(R.id.avail);
		total = (TextView) findViewById(R.id.total);

		error = false;
		mContext = FileQuest.this;
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (TransitionViewPager) findViewById(R.id.pager);
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		String[] te = getResources().getStringArray(R.array.effects);		
		mViewPager.setTransitionEffect(TransitionEffect.valueOf(te[PAGER_ANIM]));
		
		indicator.setViewPager(mViewPager);
		mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
		mVFlipper = (ViewFlipper) findViewById(R.id.viewFlipperMenu);
		mFlipperBottom = (ViewFlipper) findViewById(R.id.viewFlipperMenuBottom);
		nSFManager = new SFileManager();
		nRFileManager = new RFileManager();
		if (ENABLE_ON_LAUNCH) {
			if (new File(INTERNAL_PATH_TWO).exists()) {
				if (PATH != INTERNAL_PATH_TWO)
					RFileManager.nStack.push(INTERNAL_PATH_TWO);
			} else {
				edit.putString("INTERNAL_PATH_TWO", PATH);
				edit.commit();
				showToast(getResources().getString(
						R.string.startupfoldernotfound));
			}
			if (new File(INTERNAL_PATH_ONE).exists()) {
				if (PATH != INTERNAL_PATH_ONE)
					SFileManager.nStack.push(INTERNAL_PATH_ONE);
			} else {
				edit.putString("INTERNAL_PATH_ONE", PATH);
				edit.commit();
				showToast(getResources().getString(
						R.string.startupfoldernotfound));
			}
		}

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

		if (CURRENT_PREF_ITEM == 3) {
			mVFlipper.setAnimation(nextAnim());
			mVFlipper.showNext();
			mFlipperBottom.showPrevious();
			mFlipperBottom.setAnimation(prevAnim());
		}

		RFileManager.SORT_TYPE = SFileManager.SORT_TYPE = SORT_TYPE;
		if (SHOW_HIDDEN_FOLDERS) {
			SFileManager.SHOW_HIDDEN_FOLDER = true;
			RFileManager.SHOW_HIDDEN_FOLDER = true;
		}
		nFiles = RFileManager.giveMeFileList();
		sFiles = SFileManager.giveMeFileList();
		nSimple = new SimpleAdapter(getApplicationContext(),
				R.layout.row_list_1, sFiles);
		RootAdapter = new RootAdapter(getApplicationContext(),
				R.layout.row_list_1, nFiles);
		nManager = new AppManager(mContext);
		nManager.SHOW_APP = SHOW_APP;
		nList = nManager.giveMeAppList();
		nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,
				nList);

		for (int i = 0; i < 15; ++i) {
			EMPTY.add("  ");
			EMPTY_APPS.add("  ");
			if (i == 9) {
				EMPTY.add(getString(R.string.nofilesavailable));
				EMPTY_APPS.add(getString(R.string.noappsinstalled));
			}
		}

		
		file = new File("/");
		file2 = new File(PATH);
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int page) {
				// TODO Auto-generated method stub
				mUseBackKey = false;
				CURRENT_ITEM = page;
				Button b = (Button) findViewById(R.id.change);
				TextView t = (TextView) findViewById(R.id.addText);
				if (page == 0) {
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
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		ShowMenu();
		return false;
	}

	/**
	 * FUNCTION RELOADS THE LIST AFTER CERTAIN TASK.....
	 * 
	 * @param ITEM
	 */
	private static void refreshList(final int ITEM) {
		final Handler handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					root.setAdapter(RootAdapter);
					break;

				case 2:
					simple.setAdapter(nSimple);
					break;
				case 3:
					load_FIle_Gallery(fPos);
					break;

				case 4:
					nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
					APP_LIST_VIEW.setAdapter(nAppAdapter);
				}
			}
		};

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (ITEM == 1) {
					sFiles = SFileManager.giveMeFileList();
					handle.sendEmptyMessage(2);
					if (SimpleAdapter.MULTI_SELECT) {
						SimpleAdapter.thumbselection = new boolean[sFiles
								.size()];
						SimpleAdapter.c = 0;
					}
				} else if (ITEM == 2) {
					nFiles = RFileManager.giveMeFileList();
					if (RootAdapter.MULTI_SELECT) {
						RootAdapter.thumbselection = new boolean[nFiles.size()];
						RootAdapter.C = 0;
					}
					handle.sendEmptyMessage(1);
				} else if (ITEM == 0) {
					handle.sendEmptyMessage(3);
				} else if (ITEM == 3)
					handle.sendEmptyMessage(4);
			}
		});
		thread.start();
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
				case 1:
					root.setAdapter(new EmptyAdapter(mContext, R.layout.empty_adapter,EMPTY));
					root.setEnabled(false);
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(ITEM);
					break;

				case 2:

					simple.setAdapter(new EmptyAdapter(mContext, R.layout.empty_adapter,EMPTY));
					simple.setEnabled(false);
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(ITEM);
					break;

				case 3:
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(ITEM);
					break;
				case 4:
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(ITEM);
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
				if (SHOW_HIDDEN_FOLDERS) {
					RFileManager.SHOW_HIDDEN_FOLDER = true;
					SFileManager.SHOW_HIDDEN_FOLDER = true;
				}

				try {// TRY BLOCK IS USED BECAUSE I NOTICED THAT WHEN NEW FOLDER
						// WITH HINDI LANGAUGE IS CREATED THROWS INDEXOUTOFBOUND
						// EXCEPTION
						// I THINK IT IS APPLICABLE TO OTHER LANGUAGES ALSO
					if(!ZIP_ROOT)
						nFiles = RFileManager.giveMeFileList();
					if(!ZIP_SIMPLE)
						sFiles = SFileManager.giveMeFileList();
					if (ITEM == 0)
						load_FIle_Gallery(pos);
				} catch (IndexOutOfBoundsException e) {
					if(!ZIP_ROOT)
						nFiles = RFileManager.giveMeFileList();
					if(!ZIP_SIMPLE)
						sFiles = SFileManager.giveMeFileList();
				}

				if (RootAdapter.MULTI_SELECT) {
					RootAdapter.thumbselection = new boolean[nFiles.size()];
					RootAdapter.MULTI_FILES = new ArrayList<File>();
					RootAdapter.C = 0;
				}
				if (SimpleAdapter.MULTI_SELECT) {
					SimpleAdapter.thumbselection = new boolean[sFiles.size()];
					SimpleAdapter.MULTI_FILES = new ArrayList<File>();
					SimpleAdapter.c = 0;
				}
				rSize = nFiles.size();
				sSize = sFiles.size();
				try {
					mSize = mediaFileList.size();
				} catch (NullPointerException e) {

				}
				mUseBackKey = false;

				if (rSize == 0 && ITEM == 2)
					handle.sendEmptyMessage(1);// LIST IS EMPTY....IN SDCARD
												// PANEL
				else if (sSize == 0 && ITEM == 1)
					handle.sendEmptyMessage(2);// list IS EMPTY...IN "/" PANEL
				else if ((rSize != 0 && ITEM == 2) || (sSize != 0 && ITEM == 1))// NON
																				// OF
																				// THE
																				// LIST
																				// IS
																				// EMPTY....
					handle.sendEmptyMessage(3);
				else if (ITEM == 3) {
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
				Fragment fragment = new SimplePanel();
				mViewPager.setObjectForPosition(fragment, position);
				return fragment;
			case 2:
				Fragment fragment1 = new RootPanel();
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
				if (SFileManager.nStack.size() == 1)
					return "/";
				else if (SFileManager.getCurrentDirectoryName().equals(
						"sdcard0"))
					return getString(R.string.sd);
				return SFileManager.getCurrentDirectoryName();
			case 2:
				curr = RFileManager.getCurrentDirectoryName();
				if (curr.equals("sdcard") || curr.equals("sdcard0")
						|| curr.equalsIgnoreCase("0")
						|| curr.equalsIgnoreCase(PATH))
					return getString(R.string.sd);
				return RFileManager.getCurrentDirectoryName();
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
				if (mediaFileList.size() == 0 && elementInFocus) {
					LIST_VIEW_3D.setAdapter(new EmptyAdapter(mContext,R.layout.empty_adapter, EMPTY));
					LIST_VIEW_3D.setEnabled(false);
				}
				if (elementInFocus) {
					LIST_VIEW_3D.setVisibility(View.VISIBLE);
					FILE_GALLEY.setVisibility(View.GONE);
					// load_FIle_Gallery(fPos, mContext);
					LIST_VIEW_3D.setAdapter(element);
					LIST_VIEW_3D.setDynamics(new SimpleDynamics(0.7f, 0.6f));
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
			View v = inflater.inflate(R.layout.custom_list_view, container,false);
			LIST_VIEW_3D = (ListView3D) v.findViewById(R.id.customListView);
			// LIST_VIEW_3D.setAdapter(adapter);
			LIST_VIEW_3D.setDynamics(new SimpleDynamics(0.7f, 0.6f));
			FILE_GALLEY = (LinearLayout)v.findViewById(R.id.file_gallery_layout);

			LinearLayout music = (LinearLayout) v.findViewById(R.id.music);
			LinearLayout app = (LinearLayout) v.findViewById(R.id.apps);
			LinearLayout docs = (LinearLayout) v.findViewById(R.id.docs);
			LinearLayout photo = (LinearLayout) v.findViewById(R.id.photos);
			LinearLayout vids = (LinearLayout) v.findViewById(R.id.videos);
			LinearLayout zips = (LinearLayout) v.findViewById(R.id.zips);
			LinearLayout misc = (LinearLayout) v.findViewById(R.id.misc);
			LinearLayout zdcard = (LinearLayout) v.findViewById(R.id.sdcard);
			TextView ls = (TextView) v.findViewById(R.id.lsSdcard);
			/**
			 * CHECKING WHETHER EXTERNAL SD IS PRESENT OR NOT....
			 */
			if (StorageHelper.isExternalStorageAvailableAndWriteable())
				ls.setText(R.string.extpresent);
			else
				ls.setText(R.string.extAbsent);
			TextView ls2 = (TextView) v.findViewById(R.id.lsSdcard2);
			ls2.setText(R.string.intpresent);
			Utils util = new Utils(v,mContext);
			util.load();
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
			zdcard.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// setAdapterAgain(2, mContext);
				}
			});

			final Dialog d = new Dialog(mContext,R.style.custom_dialog_theme);
			d.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) d.findViewById(R.id.list);
			AdapterLoaders loaders = new AdapterLoaders(getActivity(), false);
			lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.blue_button));
			d.getWindow().getAttributes().width = size.x*8/9;
			LIST_VIEW_3D.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							if (elementInFocus && !LongClick) {
								if (SEARCH_FLAG)
									file0 = searchList.get(position);
								else
									file0 = mediaFileList.get(position);
								LongClick = true;
								d.show();
							}
							return true;
						}
					});
			lo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					d.dismiss();
					switch (position) {
					case 0:
						// OPEN THE SELECTED FILE
						new OpenFileDialog(mContext, Uri.parse(file0.getAbsolutePath()), size.x*8/9);
						break;
					case 1:
						// CLOUD.....
						Toast.makeText(mContext, R.string.supporttakenBack,	Toast.LENGTH_SHORT).show();
						break;

					case 2:
						// COPY
						RENAME_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
						COPY_COMMAND = true;
						COPY_FILES = new ArrayList<File>();
						COPY_FILES.add(file0);
						break;

					case 3:
						// CUT
						RENAME_COMMAND = COPY_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
						CUT_COMMAND = true;
						COPY_FILES = new ArrayList<File>();
						COPY_FILES.add(file0);
						break;
					case 4:
						// PASTE
						Toast.makeText(mContext, R.string.pasteNotAllowed,Toast.LENGTH_SHORT).show();
						break;
					case 5:
						// ZIP
						ArrayList<File> temp = new ArrayList<File>();
						temp.add(file0);
						new CreateZip(mContext, size.x * 8 / 9, temp);
						break;
					case 6:
							//DELETE
							{
								ArrayList<File> te = new ArrayList<File>();
								te.add(file0);
								new DeleteFiles(mContext, size.x*8/9, te);
							}
							break;
					case 7:
						// RENAME
						COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
						RENAME_COMMAND = true;
						mVFlipper.showPrevious();
						mVFlipper.setAnimation(prevAnim());
						editBox.setText(file0.getName());
						break;

					case 8:
						// SEND
						new BluetoothChooser(mContext, file0.getAbsolutePath(), size.x*8/9, null);
						break;
					case 9:
						new AddGesture(mContext, size.x, size.y*8/9, file0
								.getPath());
						break;
					case 10:
						// PROPERTIES
						new FileProperties(mContext, size.x*8/9, file0);
					}
				}
			});

			lo.setOnItemLongClickListener(null);
			LIST_VIEW_3D.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long id) {
					if (elementInFocus) {
						if (SEARCH_FLAG)
							file0 = searchList.get(position);
						else
							file0 = mediaFileList.get(position);
						if (LongClick) {
							LongClick = false;
						} else
							new OpenFileDialog(mContext, Uri.parse(file0.getAbsolutePath()), size.x*8/9);
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
	public static class SimplePanel extends BaseFragment {
		public SimplePanel(){
			//PASSING THE ANIMATION TYPE FOR THE LIST VIEW.....
			super(LIST_ANIM);
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
			if(ZIP_SIMPLE)
				setListAdapter(new ZipAdapter(zListSimple, mContext));
			else
				setListAdapter(nSimple);
			simple.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						final int position, long id) {
					if (SEARCH_FLAG && searchList.size() > 0) {
						if(ZIP_SIMPLE)
							zFileSimple = zListSimple.get(position);
						else
							file = searchList.get(position);
					} else {
						if(ZIP_SIMPLE)
							zFileSimple = zListSimple.get(position);
						else
							file = sFiles.get(position);
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
					if(ZIP_SIMPLE){
						if(zFileSimple.isFile()){
							//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
							new ExtractZipFile(mContext, zFileSimple, size.x*8/9 , null , file,0);
						}else{
							//DIRECTORY HAS TO BE OPENED....
							zipPathSimple = zFileSimple.getPath();
							SFileManager.nStack.push(zipPathSimple+" -> Zip");
							if(zipPathSimple.startsWith("/"))
								zipPathSimple = zipPathSimple.substring(1, zipPathSimple.length());
							setZipAdapter();
						}	
					}else{
						//HANDLING ORDINARY FILE EXLORING....
						if (file.isFile())
							new OpenFileDialog(mContext, Uri.parse(file.getAbsolutePath()), size.x*8/9);
						else if (file.isDirectory()){
							SFileManager.nStack.push(file.getAbsolutePath());
							setAdapter(1);
						}
					}					
				}
			});

			final Dialog d = new Dialog(mContext, R.style.custom_dialog_theme);
			d.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) d.findViewById(R.id.list);
			AdapterLoaders loaders = new AdapterLoaders(getActivity(), false);
			lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.blue_button));
			d.getWindow().getAttributes().width = size.x*8/9;
			simple.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					spos = position;
					if(SEARCH_FLAG){
						if(ZIP_SIMPLE)
							zFileSimple = zListSimple.get(position);
						else
							file = searchList.get(position);
					} else {
						if(ZIP_SIMPLE)
							zFileSimple = zListSimple.get(position);
						else
							file = sFiles.get(position);
					}
					d.show();
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
									RENAME_COMMAND = false;
						}
						
						/*
						 *ZIP FILE IS OPEN,HANDLE IT HERE... 
						 */
						if(ZIP_SIMPLE){
							if(zFileSimple.isFile()){
								//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
								new ExtractZipFile(mContext, zFileSimple, size.x*8/9 , null , file,0);
							}else{
								//DIRECTORY HAS TO BE OPENED....
								zipPathSimple = zFileSimple.getPath();
								SFileManager.nStack.push(zipPathSimple+" -> Zip");
								if(zipPathSimple.startsWith("/"))
									zipPathSimple = zipPathSimple.substring(1, zipPathSimple.length());
								setZipAdapter();
							}	
						}else{
							//ORDINARY FILE EXPLORING..
							if (file.isFile())
								new OpenFileDialog(mContext, Uri.parse(file.getAbsolutePath()), size.x*8/9);
							else if (file.isDirectory()) {
								SFileManager.nStack.push(file.getAbsolutePath());
								setAdapter(1);
							}
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

						if(ZIP_SIMPLE){
							//CURRENTLY WE ARE INSIDE OF ZIP ARCHIVE...
							//EXTRACT FILES TO USER SPECIFIED PATH....
							new GetHomeDirectory(mContext, size.x*8/9, null);
						}else{
							//ORDINARY FILE OPERATIONS....
							CREATE_FILE = RENAME_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							COPY_COMMAND = true;
							COPY_FILES = new ArrayList<File>();
							COPY_FILES.add(file);
						}
						
						break;

					case 3:
						// CUT
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
							mVFlipper.setAnimation(nextAnim());
						}

						if(ZIP_SIMPLE){
							//WE ARE CURRENTLY INSIDE ZIP ARCHIVE...
							//FILES HAVE TO BE EXTRACTED IN THE CURRENT DIRECTORY
							if(new File(file.getParent()).canRead())
								new ExtractZipFile(mContext, zFileSimple, size.x*8/9, file.getParent(), file, 1);
							else
								//CURRENT DIRECTORY IS UN-WRITABLE...OR WE DONT HAVE WRITE PERMISSION HERE...
								Toast.makeText(mContext, R.string.cannotexthere, Toast.LENGTH_SHORT).show();
						}else{
							//ORDINARY FILE OPERATIONS....
							CREATE_FILE = RENAME_COMMAND = COPY_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							CUT_COMMAND = true;
							COPY_FILES = new ArrayList<File>();
							COPY_FILES.add(file);
						}						
						break;

					case 4:
						// PASTE
						if(ZIP_SIMPLE)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else
							pasteCommand(true);
						break;
					case 5:
						// ZIP
						if(ZIP_SIMPLE)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							if (!file.canRead())
								Toast.makeText(mContext, R.string.serviceUnavaila,Toast.LENGTH_SHORT).show();
							else if (file.canRead()) {
								ArrayList<File> temp = new ArrayList<File>();
								temp.add(file);
								new CreateZip(mContext, size.x*8/9, temp);
							}
						}						
						break;

					case 6:
							
							//DELETE
						if(ZIP_SIMPLE)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							ArrayList<File> te = new ArrayList<File>();
							te.add(file);
							new DeleteFiles(mContext, size.x*8/9, te);
						}
							break;
					case 7:
						// RENAME
						/*
						 * COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG =
						 * MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT
						 * = false; RENAME_COMMAND = true;
						 * mVFlipper.showPrevious();
						 * mVFlipper.setAnimation(prevAnim());
						 * editBox.setText(file.getName());
						 * editBox.setSelected(true);
						 */
						if(ZIP_SIMPLE)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(mContext,"Yet to implement rename command for root files",Toast.LENGTH_SHORT).show();
						break;

					case 8:
						// SEND
						if(ZIP_SIMPLE){
							if(zFileSimple.isFile())
								new ExtractZipFile(mContext, zFileSimple, size.x*8/9, null, file, 2);
							else
								Toast.makeText(mContext, R.string.cannotsendfolder, Toast.LENGTH_SHORT).show();
						}else{
							if (file.isFile())
								new BluetoothChooser(mContext, file.getAbsolutePath(), size.x*8/9, null);
							else
								showToast(getString(R.string.compressandsend));
						}
						
						break;

					case 9:
						//ADD GESTURE...
						if(ZIP_SIMPLE)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else
							new AddGesture(mContext, size.x, size.y*8/9, file.getPath());
						break;
					case 10:
						// PROPERTIES
						if(ZIP_SIMPLE)//A ZIP FILE IS OPENED....
							new ArchiveEntryProperties(mContext, zFileSimple, size.x*8/9);
						else
							new FileProperties(getActivity(), size.x*8/9, file);
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
	public static class RootPanel extends BaseFragment {

		public RootPanel() {
			//PASSING THE ANIMATION TYPE FOR THE LIST VIEW.....
			super(LIST_ANIM);
		}
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			root = getListView();
			
			root.setSelector(R.drawable.blue_button);
			ColorDrawable color = new ColorDrawable(android.R.color.black);
			root.setDivider(color);
			if(ZIP_ROOT){
				setListAdapter(new ZipAdapter(zListRoot,mContext));
			}else
				setListAdapter(RootAdapter);

			dialog = new Dialog(getActivity(), R.style.custom_dialog_theme);
			dialog.setContentView(R.layout.long_click_dialog);
			ListView lo = (ListView) dialog.findViewById(R.id.list);
			AdapterLoaders loaders = new AdapterLoaders(getActivity(), false);
			lo.setAdapter(loaders.getLongClickAdapter());
			lo.setSelector(getResources().getDrawable(R.drawable.blue_button));
			dialog.getWindow().getAttributes().width = size.x*8/9;

			root.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					if (SEARCH_FLAG){
						if(ZIP_ROOT)
							zFileRoot = zSearch.get(arg2);
						else
							file2 = searchList.get(arg2);
					} else {
						if(ZIP_ROOT)
							zFileRoot = zListRoot.get(arg2);
						else
							file2 = nFiles.get(arg2);
					}
					dialog.show();
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
						
						if(ZIP_ROOT){
							if(zFileRoot.isFile()){
								//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
								new ExtractZipFile(mContext, zFileRoot, size.x*8/9 , null , file2,0);
							}else{
								//DIRECTORY HAS TO BE OPENED....
								zipPathRoot = zFileRoot.getPath();
								RFileManager.nStack.push(zipPathRoot+" -> Zip");
								if(zipPathRoot.startsWith("/"))
									zipPathRoot = zipPathRoot.substring(1, zipPathRoot.length());
								setZipAdapter();
							}	
						}else{
							if (file2.isFile()) {
								new OpenFileDialog(mContext, Uri.parse(file2.getAbsolutePath()), size.x*8/9);
							} else if (file2.isDirectory()) {
								RFileManager.nStack.push(file2.getAbsolutePath());
								setAdapter(2);
							}
						}
						break;

					case 1:
						// COPY TO CLOUD
						/**
						 * if root panel is currently on cloud mode,then on
						 * selection from below list,it migrated to root panel
						 * and user there does the pasting work of selected file
						 * from sd card panel..... else a seperate dialog is
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
						
						if(ZIP_ROOT){
							//ZIP CONTENTS HAS TO EXTRACTED AT USER SPECIFIED LOCATION.....
							
							/**
							 * 
							 */
							new GetHomeDirectory(mContext, size.x*8/9, null);
						}else{
							//ORDINARY FILE EXLORING...
							CREATE_FILE = RENAME_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							COPY_COMMAND = true;
							COPY_FILES = new ArrayList<File>();
							COPY_FILES.add(file2);
						}						
						break;

					case 3:
						// CUT
						if (RENAME_COMMAND || CREATE_FILE || SEARCH_FLAG) {
							mVFlipper.showNext();
							mVFlipper.setAnimation(nextAnim());
						}
						
						if(ZIP_ROOT){
							//ZIP FILE IS OPENED....
							//SELECTED FILE HAS TO BE EXTRACTED...
							
							//FILE HAS TO BE EXTRACTED IN CURRENT DIRECTORY....
							new ExtractZipFile(mContext, zFileRoot, size.x*8/9, file2.getParent(), file2 , 1);
						}else{
							//ORDINARY FILE EXPLORING.... 
							CREATE_FILE = RENAME_COMMAND = COPY_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							CUT_COMMAND = true;
							COPY_FILES = new ArrayList<File>();
							COPY_FILES.add(file2);
						}						
						break;
					case 4:
						// PASTE
						if(ZIP_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else
							pasteCommand(true);
						break;
					case 5:
						// ZIP
						if(ZIP_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							ArrayList<File> temp = new ArrayList<File>();
							temp.add(file2);
							new CreateZip(mContext, size.x*8/9, temp);
						}
						
						break;
					case 6:
						
							//DELETE
						if(ZIP_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							ArrayList<File> te = new ArrayList<File>();
							te.add(file2);
							new DeleteFiles(mContext, size.x*8/9, te);
						}
							break;
					case 7:
						// RENAME
						if(ZIP_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else{
							COPY_COMMAND = CUT_COMMAND = SEARCH_FLAG = MULTIPLE_COPY = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT = false;
							RENAME_COMMAND = true;
							mVFlipper.showPrevious();
							mVFlipper.setAnimation(prevAnim());
							editBox.setText(file2.getName());
							editBox.setSelected(true);
						}						
						break;

					case 8:
						// SEND
						if(ZIP_ROOT){
							if(zFileRoot.isFile())
								new ExtractZipFile(mContext, zFileRoot, size.x*8/9, null, file2, 2);
							else
								Toast.makeText(mContext, R.string.cannotsendfolder, Toast.LENGTH_SHORT).show();
						}else{
							if (file2.isFile())
								new BluetoothChooser(mContext, file2.getAbsolutePath(), size.x*8/9  , null);
							else
								showToast(getString(R.string.compressandsend));
						}
						
						break;
					case 9:
						// gesture to the selected file....
						if(ZIP_ROOT)
							Toast.makeText(mContext, R.string.operationnotsupported, Toast.LENGTH_SHORT).show();
						else
							new AddGesture(mContext, size.x, size.y*8/9,file2.getPath());
						break;
					case 10:
						// PROPERTIES
						if(ZIP_ROOT)
							new ArchiveEntryProperties(mContext, zFileRoot, size.x*8/9);
						else
							new FileProperties(mContext, size.x*8/9, file2);
					}
				}
			});
			root.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					// TODO Auto-generated method stub
					if(SEARCH_FLAG){
						if(ZIP_ROOT)
							zFileRoot = zSearch.get(position);
						else
							file2 = searchList.get(position);
					}else{
						if(ZIP_ROOT)
							zFileRoot = zListRoot.get(position);
						else
							file2 = nFiles.get(position);
					}
					
					if (CREATE_FILE || RENAME_COMMAND || SEARCH_FLAG) {
						mVFlipper.setAnimation(nextAnim());
						mVFlipper.showNext();
						CREATE_FILE = RENAME_COMMAND = SEARCH_FLAG = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY = MULTIPLE_CUT = MULTIPLE_COPY_GALLERY = MULTIPLE_CUT_GALLERY = RENAME_COMMAND = false;
						;
					}
					
					if(ZIP_ROOT){
						if(zFileRoot.isFile()){
							//FILES HAS TO BE EXTRACTED THEN USING APPROPRIATE APP MUST BE OPENED...
							new ExtractZipFile(mContext, zFileRoot, size.x*8/9 , null , file2 , 0);
						}else{
							//DIRECTORY HAS TO BE OPENED....
							zipPathRoot = zFileRoot.getPath();
							RFileManager.nStack.push(zipPathRoot+" -> Zip");
							if(zipPathRoot.startsWith("/"))
								zipPathRoot = zipPathRoot.substring(1, zipPathRoot.length());
							setZipAdapter();
						}						
					}else{
						if(file2.isFile())
							new OpenFileDialog(mContext, Uri.parse(file2.getAbsolutePath()), size.x*8/9);
						else if (file2.isDirectory()){
							RFileManager.nStack.push(file2.getAbsolutePath());
							setAdapter(2);
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
			super(LIST_ANIM);
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if (APP_LIST_VIEW != null && nList != null)
				if (nList.size() == 0) {
					APP_LIST_VIEW.setAdapter(new EmptyAdapter(getActivity(),
							R.layout.empty_adapter, EMPTY_APPS));
					APP_LIST_VIEW.setEnabled(false);
				}
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
			if (nList.size() == 0) {
				APP_LIST_VIEW.setAdapter(new EmptyAdapter(getActivity(),
						R.layout.empty_adapter, EMPTY_APPS));
				APP_LIST_VIEW.setEnabled(false);
			}
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
			AdapterLoaders loaders = new AdapterLoaders(getActivity(), true);
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
							PackageManager m = getActivity()
									.getPackageManager();
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
			if (CURRENT_ITEM == 0||ZIP_ROOT||ZIP_SIMPLE) {
				Toast.makeText(mContext, getString(R.string.pastenotallowed), Toast.LENGTH_SHORT).show();
			}else{
				pasteCommand(false);
			}
			break;

		case R.id.bottom_copy:
			if(ZIP_ROOT||ZIP_SIMPLE){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 5,getString(R.string.enablemultiselect));
			break;

		case R.id.bottom_cut:
			if(ZIP_ROOT||ZIP_SIMPLE){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 2,getString(R.string.enablemultiselect));
			break;

		case R.id.bottom_zip:
			if(ZIP_ROOT||ZIP_SIMPLE){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 3,getString(R.string.enablemultiselect));
			break;

		case R.id.bottom_delete:
			if((ZIP_ROOT||ZIP_SIMPLE)){
				Toast.makeText(mContext, getString(R.string.operationnotsupported), Toast.LENGTH_SHORT).show();
			}else
				OPERATION_ON_MULTI_SELECT_FILES(CURRENT_ITEM, 4,getString(R.string.enablemultiselect));
			break;

		case R.id.appSettings:
			ShowMenu();
			break;

		case R.id.bottom_multi: {
			QuickAction action = new QuickAction(mContext);
			ActionItem item = new ActionItem();

			if (element == null || !elementInFocus) {
				item = new ActionItem(-1, getString(R.string.multiforgallery),getResources().getDrawable(R.drawable.ic_launcher_images));
				action.addActionItem(item);
			} else {
				if (MediaElementAdapter.MULTI_SELECT)
					item = new ActionItem(0,getString(R.string.multiforgallery), getResources().getDrawable(R.drawable.ic_launcher_apply));
				else
					item = new ActionItem(0,getString(R.string.multiforgallery), getResources().getDrawable(R.drawable.ic_launcher_images));
				action.addActionItem(item);
			}

			if ((SimpleAdapter.MULTI_SELECT))
				item = new ActionItem(1, getString(R.string.multiforroot),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				item = new ActionItem(1, getString(R.string.multiforroot),getResources().getDrawable(R.drawable.ic_launcher_system));
			action.addActionItem(item);

			if ((RootAdapter.MULTI_SELECT))
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
						if (MediaElementAdapter.MULTI_SELECT) {
							element = new MediaElementAdapter(mContext,R.layout.row_list_1, mediaFileList);
							MediaElementAdapter.thumbselection = new boolean[mediaFileList.size()];
							MediaElementAdapter.MULTI_SELECT = false;
							LIST_VIEW_3D.setAdapter(element);
						} else {
							element = new MediaElementAdapter(mContext,R.layout.row_list_1, mediaFileList);
							MediaElementAdapter.thumbselection = new boolean[mediaFileList.size()];
							MediaElementAdapter.MULTI_SELECT = true;
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
						if (SimpleAdapter.MULTI_SELECT) {
							nSimple = new SimpleAdapter(mContext,R.layout.row_list_1, sFiles);
							SimpleAdapter.thumbselection = new boolean[sFiles.size()];
							SimpleAdapter.MULTI_SELECT = false;
							if(!ZIP_SIMPLE){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								simple.setAdapter(nSimple);
							}	
						} else {
							nSimple = new SimpleAdapter(mContext,R.layout.row_list_1, sFiles);
							SimpleAdapter.thumbselection = new boolean[sFiles.size()];
							SimpleAdapter.MULTI_SELECT = true;
							mViewPager.setCurrentItem(1);
							if(!ZIP_SIMPLE){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								simple.setAdapter(nSimple);
							}								
						}
						break;
					case 2:
						if (RootAdapter.MULTI_SELECT) {
							RootAdapter = new RootAdapter(mContext,R.layout.row_list_1, nFiles);
							RootAdapter.thumbselection = new boolean[nFiles.size()];
							RootAdapter.MULTI_SELECT = false;
							if(!ZIP_ROOT){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								root.setAdapter(RootAdapter);
							}	
						} else {
							RootAdapter = new RootAdapter(mContext,R.layout.row_list_1, nFiles);
							RootAdapter.thumbselection = new boolean[nFiles.size()];
							RootAdapter.MULTI_SELECT = true;
							mViewPager.setCurrentItem(2);
							if(!ZIP_ROOT){
								//MULTI SELECT NOT FUNCTION INSIDE ZIP FILE...
								//MULTI SELECT IS ENABLED,AND ITS EFFECT WILL COME AFTER COMING
								//OUT OF THE ARCHIVE...
								root.setAdapter(RootAdapter);
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
			nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,
					nList);
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
				if(ZIP_ROOT||ZIP_SIMPLE)
					if(SEARCH_FLAG){
						SEARCH_FLAG = false;
						mVFlipper.showNext();
						mVFlipper.setAnimation(nextAnim());
					}else
						zipSearch();
				else{
					if(SEARCH_FLAG){
						SEARCH_FLAG = false;
						mVFlipper.showNext();
						mVFlipper.setAnimation(nextAnim());
					}else
						search();
				}	
				break;

		case R.id.applyBtn:
			// rename the file with given name from editBox
			editBox = new EditText(mContext);
			editBox = (EditText) findViewById(R.id.editBox);
			String name = editBox.getText().toString();
			// String ext = extBox.getText().toString();
			if (name.length() > 0) {
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
								name = SFileManager.getCurrentDirectory() + "/"	+ editBox.getText().toString();
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
						name = getPathWithoutFilename(file0).getPath() + "/"+ name;
						if (file0.renameTo(new File(name))) {
							Toast.makeText(mContext,getString(R.string.renamed)+ new File(name).getName(),Toast.LENGTH_SHORT).show();
						} else {
							/**
							 * THIS INTENT IS FIRED WHEN RENAMING OF FILE FAILS
							 * SHOWING POSSIBLE ERROR
							 */
							new ErrorDialogs(mContext,size.x*8/9,"renameError");
						}
					} else if (CURRENT_ITEM == 1) {
						name = SFileManager.getCurrentDirectory() + "/" + name;
						try {
							/*
							 * yet to implement rename command for root files
							 */
						} catch (Exception e) {
							/**
							 * THIS INTENT IS FIRED WHEN RENAMING OF FILE FAILS
							 * SHOWING POSSIBLE ERROR
							 */
							new ErrorDialogs(mContext, size.x*8/9,"renameError");
						}
					} else if (CURRENT_ITEM == 2) {
						name = RFileManager.getCurrentDirectory() + "/" + name;
						if (file2.renameTo(new File(name))) {
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
					nRFileManager = new RFileManager();
					RFileManager.SHOW_HIDDEN_FOLDER = SHOW_HIDDEN_FOLDERS;
					RFileManager.SORT_TYPE = SORT_TYPE;
					RFileManager.nStack.push(HOME_DIRECTORY);
					nFiles = RFileManager.giveMeFileList();
					RootAdapter = new RootAdapter(mContext,R.layout.row_list_1, nFiles);
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(2);
				} else if (CURRENT_ITEM == 1) {
					nSFManager = new SFileManager();
					SFileManager.SHOW_HIDDEN_FOLDER = SHOW_HIDDEN_FOLDERS;
					SFileManager.SORT_TYPE = SORT_TYPE;
					SFileManager.nStack.push(HOME_DIRECTORY);
					sFiles = SFileManager.giveMeFileList();
					nSimple = new SimpleAdapter(mContext,R.layout.row_list_1, sFiles);
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(1);
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
								SFileManager.nStack.push(HOME_DIRECTORY);
								setAdapter(1);
								break;
							case 9:
								LAST_PAGE = 0;
								RFileManager.nStack.push(HOME_DIRECTORY);
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
				actionJump
						.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
							@Override
							public void onItemClick(QuickAction source,
									int pos, int actionId) {
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
										RFileManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										SFileManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;
								case 1100:
									fJump = new File(PATH + "/DCIM");
									if (!fJump.exists())
										fJump.mkdir();
									if (CURRENT_ITEM == 2)
										RFileManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										SFileManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;
								case 1200:
									fJump = new File(PATH + "/Music");
									if (!fJump.exists())
										fJump.mkdir();
									if (CURRENT_ITEM == 2)
										RFileManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										SFileManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;

								case 1201:
									fJump = new File(PATH + "/Video");
									if (!fJump.exists())
										fJump.mkdir();
									if (CURRENT_ITEM == 2)
										RFileManager.nStack.push(fJump.getAbsolutePath());
									else if (CURRENT_ITEM == 1)
										SFileManager.nStack.push(fJump.getAbsolutePath());
									setAdapter(CURRENT_ITEM);
									break;
								case 1300:
									if (CURRENT_ITEM == 1) {
										new FileProperties(mContext,size.x *8/9, file);
										/*
										 * LAUNCH_INTENT = new
										 * Intent(mContext,
										 * FileProperties.class);
										 * LAUNCH_INTENT.setData
										 * (Uri.parse(nSFManager
										 * .getCurrentDirectory()));
										 * startActivity(LAUNCH_INTENT);
										 */
									} else if (CURRENT_ITEM == 2) {
										new FileProperties(mContext,size.x*8/9, file2);
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
						CREATE_FLAG_PATH = SFileManager.getCurrentDirectory();
					else if (CURRENT_ITEM == 2)
						CREATE_FLAG_PATH = RFileManager.getCurrentDirectory();
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
					default:
						/*
						 * DROPBOX INFO STUFF
						 */
						Toast.makeText(mContext, R.string.supporttakenBack,Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
		});
	}

	/**
	 * OnActivityResult For FileQuest Class
	 */
	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		CURRENT_ITEM = mViewPager.getCurrentItem();
		if (requestCode == 5) {
			if (resultCode == 1) {
				setAdapter(CURRENT_ITEM);
				CUT_COMMAND = COPY_COMMAND = false;
				mVFlipper.showNext();
				mVFlipper.setAnimation(nextAnim());
			}
		} else if (requestCode == 6) {
			if (resultCode == 1) {
				setAdapter(CURRENT_ITEM);
				mVFlipper.showNext();
				mVFlipper.setAnimation(nextAnim());
			}
		}

		else if (requestCode == 400) {
			if (resultCode == RESULT_OK) {
				if (data.getData().toString().equals("/sdcard"))
					HOME_DIRECTORY = PATH;
				else
					HOME_DIRECTORY = data.getData().toString();
				edit.putString("HOME_DIRECTORY", HOME_DIRECTORY);
				edit.commit();
				Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == 2600) {
			if (resultCode == RESULT_OK) {
				if (data.getData().toString().equals("/sdcard"))
					INTERNAL_PATH_ONE = PATH;
				else
					INTERNAL_PATH_ONE = data.getData().toString();
				edit.putString("INTERNAL_PATH_ONE", INTERNAL_PATH_ONE);
				edit.commit();
				Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == 2700) {
			if (resultCode == RESULT_OK) {
				if (data.getData().toString().equals("/sdcard"))
					INTERNAL_PATH_TWO = PATH;
				else
					INTERNAL_PATH_TWO = data.getData().toString();
				edit.putString("INTERNAL_PATH_TWO", INTERNAL_PATH_TWO);
				edit.commit();
				Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CURRENT_ITEM = mViewPager.getCurrentItem();
			if ((SEARCH_FLAG || RENAME_COMMAND || CREATE_FILE)
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
				mViewPager.setAdapter(mSectionsPagerAdapter);
				mViewPager.setCurrentItem(0);
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
						android.os.Process.killProcess(android.os.Process.myPid());
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
						android.os.Process.killProcess(android.os.Process.myPid());
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
			}else if(ZIP_SIMPLE && CURRENT_ITEM==1){
				//POPING OUT THE ZIP PATH.....
				SFileManager.nStack.pop();
				/**
				 * ZIP FILE IS OPENED IN SDCARD_PANEL...
				 * HANDLE IT HERE.....
				 * WHEN ZIP FILE IS CLOSED THEN SET THE ZIP PATH TO NULL.....
				 */
				if(zipPathSimple.equals("/")){
					/*
					 * WE ARE ALREADY IN TOP OF ZIP FILE
					 * NOW QUIT THE ZIP FILE....
					 */
					ZIP_SIMPLE = false;
					zipPathSimple = null;
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(CURRENT_ITEM);
				}else
				
				/*
				 * TRY BLOCK IS USED BECAUSE WHEN USER NAVIGATES TO TOP DIRECTORY IN ZIP FILE
				 * CAUSES STRINGINDEXOUTOFBOUND EXCEPTION....
				 * AND FAILS TO GENERATE THE "/" PATH FOR ZIP FILE...
				 * SO MANUALLY GENERATING THE "/" PATH.... 
				 */
				try{
					zipPathSimple = zipPathSimple.substring(0, zipPathSimple.lastIndexOf("/"));
					setZipAdapter();
				}catch(Exception e){
					zipPathSimple = "/";
					setZipAdapter();
				}
				
			}		
			else if (CURRENT_ITEM == 1&& !SFileManager.getCurrentDirectory().equals("/")) {
				SFileManager.nStack.pop();
				File fi = new File(SFileManager.nStack.peek());
				if (!fi.canRead()) {
					sFiles.clear();
					BufferedReader reader = null; // errReader = null;
					try {
						reader = LinuxShell.execute("IFS='\n';CURDIR='"+LinuxShell.getCmdPath(fi.getAbsolutePath())+ "';for i in `ls $CURDIR`; do if [ -d $CURDIR/$i ]; then echo \"d $CURDIR/$i\";else echo \"f $CURDIR/$i\"; fi; done");
						File f;
						String line;
						while ((line = reader.readLine()) != null) {
							f = new File(line.substring(2));
							sFiles.add(f);
						}
						if (SimpleAdapter.MULTI_SELECT) {
							nSimple = new SimpleAdapter(mContext,R.layout.row_list_1, sFiles);
							SimpleAdapter.MULTI_SELECT = true;
							SimpleAdapter.thumbselection = new boolean[sFiles.size()];
							SimpleAdapter.MULTI_FILES = new ArrayList<File>();
						} else
							nSimple = new SimpleAdapter(mContext,R.layout.row_list_1, sFiles);
						mViewPager.setAdapter(mSectionsPagerAdapter);
						mViewPager.setCurrentItem(1);
					} catch (Exception e) {
						Toast.makeText(mContext, R.string.rootaccessfailed,Toast.LENGTH_SHORT).show();
					}
				} else {
					sFiles = SFileManager.giveMeFileList();
					if (SimpleAdapter.MULTI_SELECT) {
						nSimple = new SimpleAdapter(mContext,R.layout.row_list_1, sFiles);
						SimpleAdapter.MULTI_SELECT = true;
						SimpleAdapter.thumbselection = new boolean[sFiles.size()];
						SimpleAdapter.MULTI_FILES = new ArrayList<File>();
					} else
						nSimple = new SimpleAdapter(mContext,R.layout.row_list_1, sFiles);
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(1);
					file = new File(SFileManager.getCurrentDirectory());
				}

			} else if (CURRENT_ITEM == 1&&SFileManager.getCurrentDirectory().endsWith("/")) {
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
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
				/**
				 * IF CURRENT PREF IS NOT 1 THEN IT DIRECTS VIEW PAGER TO STORED
				 * VALUE
				 */
				else
					mViewPager.setCurrentItem(CURRENT_PREF_ITEM);
			} 
			else if(ZIP_ROOT && CURRENT_ITEM==2){
				//POPING OUT THE ZIP PATH....
				
				RFileManager.nStack.pop();
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
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(CURRENT_ITEM);
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
				
			}else if (CURRENT_ITEM == 2 && (RFileManager.nStack.size() >= 2)) {
				nFiles = RFileManager.getPreviousFileList();
				// RootAdapter = new RootAdapter(getApplicationContext(),
				// R.layout.row_list_1, nFiles);
				RootAdapter.thumbselection = new boolean[nFiles.size()];
				mViewPager.setAdapter(mSectionsPagerAdapter);
				mViewPager.setCurrentItem(2);
				file2 = new File(RFileManager.getCurrentDirectory());
			} else if (CURRENT_ITEM == 2 && RFileManager.nStack.size() < 2) {
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
						android.os.Process.killProcess(android.os.Process.myPid());
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

		case -1:
			// THIS CASE DIAPLAYS AN ACTIVITY SHOWING THE
			// DEVELOPMENT STAGE OF THE APP

			break;

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
			// media.setAdapter(new MediaElementAdapter(mContext,
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

		case 80:
			// SHOWING THE OPTIONS AVAILABLE FOR CHANGING APPREANCE
			QuickAction q = new QuickAction(mContext);
			ActionItem q1 = new ActionItem(100,getString(R.string.adjusttrans), getResources().getDrawable(R.drawable.ic_launcher_appreance));
			q.addActionItem(q1);
			if (CURRENT_ITEM != 3) {
				q1 = new ActionItem(90, getString(R.string.setfoldericn),getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
				q.addActionItem(q1);
			}
			q.setOnActionItemClickListener(this);
			q.show(indicator);
			break;

		case 90:
			// DIRECTED FROM CASE 80
			// DISPLAYS THE OPTIONS AVAILABLE FOR CHANGING FOLDER ICON TO SHOW
			int FOLDER_TYPE = RootAdapter.FOLDER_TYPE;
			QuickAction ac = new QuickAction(mContext);
			ActionItem it;

			if (FOLDER_TYPE == 0)
				it = new ActionItem(2800, getString(R.string.orangefolder),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				it = new ActionItem(2800, getString(R.string.orangefolder),getResources().getDrawable(R.drawable.ic_launcher_orange_folder));
			ac.addActionItem(it);

			if (FOLDER_TYPE == 1)
				it = new ActionItem(2900, getString(R.string.greenfolder),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				it = new ActionItem(2900, getString(R.string.greenfolder),getResources().getDrawable(R.drawable.ic_launcher_green_folder));
			ac.addActionItem(it);

			if (FOLDER_TYPE == 2)
				it = new ActionItem(3000, getString(R.string.yellowfolder),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				it = new ActionItem(3000, getString(R.string.yellowfolder),getResources().getDrawable(R.drawable.ic_launcher_yellow_folder));
			ac.addActionItem(it);

			if (FOLDER_TYPE == 3)
				it = new ActionItem(3100, getString(R.string.violetfolder),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				it = new ActionItem(3100, getString(R.string.violetfolder),getResources().getDrawable(R.drawable.ic_launcher_violet_folder));
			ac.addActionItem(it);

			if (FOLDER_TYPE == 4)
				it = new ActionItem(3101, getString(R.string.redfolder),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				it = new ActionItem(3101, getString(R.string.redfolder),getResources().getDrawable(R.drawable.ic_launcher_red_folder));
			ac.addActionItem(it);

			if (FOLDER_TYPE == 5)
				it = new ActionItem(3102, getString(R.string.brownfolder),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				it = new ActionItem(3102, getString(R.string.brownfolder),getResources().getDrawable(R.drawable.ic_launcher_brown_folder));
			ac.addActionItem(it);

			if (FOLDER_TYPE == 6)
				it = new ActionItem(3103, getString(R.string.bluefolder),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				it = new ActionItem(3103, getString(R.string.bluefolder),getResources().getDrawable(R.drawable.ic_launcher_blue_folder));
			ac.addActionItem(it);

			ac.setOnActionItemClickListener(this);
			ac.show(indicator);
			break;
		case 100:
			// DIRECTED FROM CASE 80
			// SHOWING ALL THE TRANSPARENCY LEVEL AVAILABLE
			QuickAction d = new QuickAction(mContext);
			ActionItem l;
			if (TRANSPRA_LEVEL == 0.6f)
				l = new ActionItem(1700, getString(R.string.opaque60),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				l = new ActionItem(1700, getString(R.string.opaque60),getResources().getDrawable(R.drawable.ic_launcher_appreance));
			d.addActionItem(l);

			if (TRANSPRA_LEVEL == 0.7f)
				l = new ActionItem(1800, getString(R.string.opaque70),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				l = new ActionItem(1800, getString(R.string.opaque70),getResources().getDrawable(R.drawable.ic_launcher_appreance));
			d.addActionItem(l);

			if (TRANSPRA_LEVEL == 0.8f)
				l = new ActionItem(1900, getString(R.string.opaque80),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				l = new ActionItem(1900, getString(R.string.opaque80),getResources().getDrawable(R.drawable.ic_launcher_appreance));
			d.addActionItem(l);

			if (TRANSPRA_LEVEL == 0.9f)
				l = new ActionItem(2000, getString(R.string.opaque90),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				l = new ActionItem(2000, getString(R.string.opaque90),getResources().getDrawable(R.drawable.ic_launcher_appreance));
			d.addActionItem(l);

			if (TRANSPRA_LEVEL == 1.0f)
				l = new ActionItem(2100, getString(R.string.opaque100),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				l = new ActionItem(2100, getString(R.string.opaque100),getResources().getDrawable(R.drawable.ic_launcher_appreance));
			d.addActionItem(l);
			d.setOnActionItemClickListener(this);
			d.show(indicator);
			break;
		case 200:
			// DISPLAYS OPTIONS AVAILABLE IF USER SELECTS FOLDER OPTIONS
			QuickAction a = new QuickAction(mContext);
			ActionItem i = new ActionItem(800,getString(R.string.setstartpanel), getResources().getDrawable(R.drawable.ic_launcher_startup));
			a.addActionItem(i);
			if (mViewPager.getCurrentItem() != 3) {
				i = new ActionItem(900, getString(R.string.setstartdir),getResources().getDrawable(R.drawable.ic_launcher_startup));
				a.addActionItem(i);
			}
			a.setOnActionItemClickListener(this);
			a.show(indicator);
			break;

		case 300:
			QuickAction b = new QuickAction(mContext);
			ActionItem j;
			if (SHOW_HIDDEN_FOLDERS)
				j = new ActionItem(1000, getString(R.string.showhidden),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				j = new ActionItem(1000, getString(R.string.showhidden),getResources().getDrawable(R.drawable.ic_launcher_disabled));
			b.addActionItem(j);
			j = new ActionItem(1100, getString(R.string.sort), getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
			b.addActionItem(j);
			b.setOnActionItemClickListener(this);
			b.show(indicator);
			break;

		case 400:
			// LAUNCHES AN INTERFACE FOR SELECTING A DIRECTORY FOR HOME
			// WITH REQUEST CODE 400
			new GetHomeDirectory(mContext, size.x *8/9, preferences);
			break;
			
		case 420:
			//SHOWS TO SET THE ANIMATION FOR VIEW PAGER AND LIST VIEWS...
			{
				QuickAction act = new QuickAction(mContext);
				ActionItem item = new ActionItem(421,"   "+ getString(R.string.panelanim), getResources().getDrawable(R.drawable.ic_launcher_full));
				act.addActionItem(item);
				
				item = new ActionItem(422,"   "+ getString(R.string.listanim), getResources().getDrawable(R.drawable.ic_launcher_full));
				act.addActionItem(item);
				act.setOnActionItemClickListener(this);
				act.show(indicator);
			}
			break;
			
		case 421:
			//SHOWING THE ANIMATION STYLES FOR VIEW PAGER....
			String[] ls = getResources().getStringArray(R.array.effects);
			{
				Drawable dr = getResources().getDrawable(R.drawable.ic_launcher_full);	
				QuickAction act = new QuickAction(mContext);
				ActionItem item;
				for(int m=0;m<ls.length;++m){
					if(m==PAGER_ANIM)
						item = new ActionItem(423,"   "+ls[m], getResources().getDrawable(R.drawable.ic_launcher_apply));
					else
						item = new ActionItem(423,"   "+ls[m], dr);
					act.addActionItem(item);
				}
				act.setOnActionItemClickListener(new OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos, int actionId) {
						// TODO Auto-generated method stub
						PAGER_ANIM = pos;
						String[] te = getResources().getStringArray(R.array.effects);		
						mViewPager.setTransitionEffect(TransitionEffect.valueOf(te[PAGER_ANIM]));
						edit.putInt("PAGER_ANIM", PAGER_ANIM);
						edit.commit();
						Toast.makeText(mContext, getString(R.string.settingsapplied), Toast.LENGTH_SHORT).show();
					}
				});
				act.show(indicator);
			}
			break;
			
		case 422:
			//ANIMATION STYLES FOR LIST VIEW....
			{
				String[] s = getResources().getStringArray(R.array.listAnims);
				Drawable dra = getResources().getDrawable(R.drawable.ic_launcher_full);
				QuickAction act = new QuickAction(mContext);
				ActionItem item;
				for(int m=0;m<s.length;++m){
					if(m==LIST_ANIM)
						item = new ActionItem(424,"   "+s[m], getResources().getDrawable(R.drawable.ic_launcher_apply));
					else
						item = new ActionItem(424,"   "+s[m], dra);
					act.addActionItem(item);
				}
				act.setOnActionItemClickListener(new OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos, int actionId) {
						// TODO Auto-generated method stub
						LIST_ANIM = pos;
						edit.putInt("LIST_ANIM", LIST_ANIM);
						edit.commit();
						mViewPager.setAdapter(mSectionsPagerAdapter);
						mViewPager.setCurrentItem(CURRENT_ITEM);
						Toast.makeText(mContext, getString(R.string.settingsapplied),Toast.LENGTH_SHORT).show();
					}
				});
				act.show(indicator);
			}
			break;
			
		case 500:
			// RESETS APP SETTINGS TO DEFAULT
			edit.clear();
			edit.putString("INTERNAL_PATH_ONE", INTERNAL_PATH_ONE = PATH);
			edit.putString("INTERNAL_PATH_TWO", INTERNAL_PATH_TWO = PATH);
			edit.putInt("SHOW_APP", SHOW_APP = 1);
			edit.putInt("CURRENT_PREF_ITEM", CURRENT_PREF_ITEM = 2);
			edit.putFloat("TRANSPRA_LEVEL", TRANSPRA_LEVEL = 0.8f);
			edit.putBoolean("SHOW_HIDDEN_FOLDERS", SHOW_HIDDEN_FOLDERS = false);
			edit.putInt("SORT_TYPE", SORT_TYPE = 2);
			edit.putInt("FOLDER_TYPE", FOLDER_TYPE = 3);
			edit.putString("HOME_DIRECTORY", HOME_DIRECTORY = null);
			edit.putBoolean("ENABLE_ON_LAUNCH", ENABLE_ON_LAUNCH = false);
			edit.commit();

			// CLEARING THE DEFAULT SELECTED APPS
			SharedPreferences pr = getSharedPreferences("DEFAULT_APPS",MODE_PRIVATE);
			SharedPreferences.Editor ed = pr.edit();
			ed.clear();
			ed.commit();
			Toast.makeText(mContext, getString(R.string.restored),Toast.LENGTH_SHORT).show();
			break;
		case 501:
			try{
				new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/File Quest/.gesture").delete();
			}catch(Exception e){
				
			}
			Toast.makeText(mContext, mContext.getString(R.string.gesturedatacleared),Toast.LENGTH_SHORT).show();
			break;
			
		case 600:
			CURRENT_ITEM = mViewPager.getCurrentItem();
			setAdapter(CURRENT_ITEM);
			break;
		case 700: {
			Dialog as = new Dialog(mContext, R.style.custom_dialog_theme);
			as.setContentView(R.layout.info_layout);
			as.getWindow().getAttributes().width = size.x*8/9;
			as.setCancelable(true);
			as.show();
		}
			break;
		case 800:
			// DIRECTED FROM CASE 200
			// IT IS FIRST OPTION AVAILABLE UNDER STARTUP
			QuickAction e = new QuickAction(mContext);
			ActionItem m;
			if (CURRENT_PREF_ITEM == 0)
				m = new ActionItem(2200, getString(R.string.filegallery),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				m = new ActionItem(2200, getString(R.string.filegallery),getResources().getDrawable(R.drawable.ic_launcher_startup));
			e.addActionItem(m);

			if (CURRENT_PREF_ITEM == 1)
				m = new ActionItem(2300, getString(R.string.rootpanel),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				m = new ActionItem(2300, getString(R.string.rootpanel),getResources().getDrawable(R.drawable.ic_launcher_startup));
			e.addActionItem(m);
			if (CURRENT_PREF_ITEM == 2)
				m = new ActionItem(2400, getString(R.string.sd), getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				m = new ActionItem(2400, getString(R.string.sd), getResources().getDrawable(R.drawable.ic_launcher_startup));
			e.addActionItem(m);

			if (CURRENT_PREF_ITEM == 3)
				m = new ActionItem(2500, getString(R.string.appstore),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				m = new ActionItem(2500, getString(R.string.appstore),getResources().getDrawable(R.drawable.ic_launcher_startup));
			e.addActionItem(m);
			e.setOnActionItemClickListener(this);
			e.show(indicator);
			break;
		case 900:
			// DIRECTED FROM CASE 200
			// IT IS SECOND OPTION AVAILABLE UNDER STARTUP
			QuickAction f = new QuickAction(mContext);
			ActionItem n = new ActionItem(2600, getString(R.string.rootpanel),getResources().getDrawable(R.drawable.ic_launcher_startup));
			f.addActionItem(n);
			n = new ActionItem(2700, getString(R.string.sd), getResources().getDrawable(R.drawable.ic_launcher_startup));
			f.addActionItem(n);

			if (ENABLE_ON_LAUNCH)
				n = new ActionItem(3600, getString(R.string.enableonlaunch),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				n = new ActionItem(3600, getString(R.string.enableonlaunch),getResources().getDrawable(R.drawable.ic_launcher_disabled));
			f.addActionItem(n);
			f.setOnActionItemClickListener(this);
			f.show(indicator);
			break;

		case 1000:
			// CASE 300 DIRECTED QUICKACTION TO CASE 1000 , FIRST OPTION UNDER
			// FOLDER OPTIONS
			// IF HIDDEN FOLDERS ARE VISIBLE HIDE IT OTHER WISE MAKE THEM
			// VISIBLE
			if (SHOW_HIDDEN_FOLDERS) {
				edit.putBoolean("SHOW_HIDDEN_FOLDERS", false);
				SHOW_HIDDEN_FOLDERS = RFileManager.SHOW_HIDDEN_FOLDER = SFileManager.SHOW_HIDDEN_FOLDER = false;
			} else {
				SHOW_HIDDEN_FOLDERS = RFileManager.SHOW_HIDDEN_FOLDER = SFileManager.SHOW_HIDDEN_FOLDER = true;
				edit.putBoolean("SHOW_HIDDEN_FOLDERS", true);
			}
			edit.commit();
			setAdapter(CURRENT_ITEM);
			Toast.makeText(mContext,getString(R.string.settingsapplied), Toast.LENGTH_SHORT).show();
			break;
		case 1100:
			// DISPLAYS THE AVAILABLE SORTING METHODS AVAILABLE
			QuickAction c = new QuickAction(mContext);
			ActionItem k;
			if (SORT_TYPE == 1)
				k = new ActionItem(1200, getString(R.string.alphafirst),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				k = new ActionItem(1200, getString(R.string.alphafirst),getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
			c.addActionItem(k);

			if (SORT_TYPE == 2)
				k = new ActionItem(1300, getString(R.string.folderfirst),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				k = new ActionItem(1300, getString(R.string.folderfirst),getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
			c.addActionItem(k);

			if (SORT_TYPE == 3)
				k = new ActionItem(1400, getString(R.string.filefirst),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				k = new ActionItem(1400, getString(R.string.filefirst),getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
			c.addActionItem(k);

			if (SORT_TYPE == 4)
				k = new ActionItem(1500, getString(R.string.hidfirst),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				k = new ActionItem(1500, getString(R.string.hidfirst),getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
			c.addActionItem(k);

			if (SORT_TYPE == 5)
				k = new ActionItem(1600, getString(R.string.nonhidfirst),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				k = new ActionItem(1600, getString(R.string.nonhidfirst),getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
			c.addActionItem(k);
			c.setOnActionItemClickListener(this);
			c.show(indicator);
			break;

		case 1200:
			// DIRECTED FROM CASE 1100
			// SETS SORTING IN ALPHABETICAL ORDER
			SORT_TYPE = RFileManager.SORT_TYPE = SFileManager.SORT_TYPE = 1;
			edit.putInt("SORT_TYPE", 1);
			edit.commit();
			Toast.makeText(mContext,getString(R.string.settingsapplied), Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 1300:
			// SETS SORTING IN FOLDER FIRST THEN FILE
			// DIRECTED FROM CASE 1100
			SORT_TYPE = RFileManager.SORT_TYPE = SFileManager.SORT_TYPE = 2;
			edit.putInt("SORT_TYPE", 2);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 1400:
			// SETS DORTING IN FILE FIRST THE FOLDER
			// DIRECTED FROM CASE 1100
			SORT_TYPE = RFileManager.SORT_TYPE = SFileManager.SORT_TYPE = 3;
			edit.putInt("SORT_TYPE", 3);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 1500:
			// SETS SORTING IN SHOW HIDDEN ITEM FIRST
			// DIRECTED FROM CASE 1100
			SORT_TYPE = RFileManager.SORT_TYPE = SFileManager.SORT_TYPE = 4;
			edit.putInt("SORT_TYPE", 4);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 1600:
			// SETS SORTING IN SHOW NON HIDDEN ITEMS FIRST
			// DIRECTED FROM CASE 1100
			SORT_TYPE = RFileManager.SORT_TYPE = SFileManager.SORT_TYPE = 5;
			edit.putInt("SORT_TYPE", 5);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 1700:
			// DIRECTED FROM CASE 100
			// SETS THE TRANSPARENCY TO 60%
			TRANSPRA_LEVEL = 0.6f;
			edit.putFloat("TRANSPRA_LEVEL", 0.6f);
			edit.commit();
			this.getWindow().getAttributes().alpha = TRANSPRA_LEVEL;
			Toast.makeText(mContext, R.string.settingssaved,Toast.LENGTH_SHORT).show();
			break;
		case 1800:
			// DIRECTED FROM CASE 100
			// SETS THE TRANSPARENCY TO 70%
			TRANSPRA_LEVEL = 0.7f;
			edit.putFloat("TRANSPRA_LEVEL", 0.7f);
			edit.commit();
			this.getWindow().getAttributes().alpha = TRANSPRA_LEVEL;
			Toast.makeText(mContext, R.string.settingssaved,Toast.LENGTH_SHORT).show();
			break;

		case 1900:
			// DIRECTED FROM CASE 100
			// SETS THE TRANSPARENCY TO 80%
			TRANSPRA_LEVEL = 0.8f;
			edit.putFloat("TRANSPRA_LEVEL", 0.8f);
			edit.commit();
			this.getWindow().getAttributes().alpha = TRANSPRA_LEVEL;
			Toast.makeText(mContext, R.string.settingssaved,Toast.LENGTH_SHORT).show();
			break;
		case 2000:
			// DIRECTED FROM CASE 100
			// SETS THE TRANSPARENCY TO 90%
			TRANSPRA_LEVEL = 0.9f;
			edit.putFloat("TRANSPRA_LEVEL", 0.9f);
			edit.commit();
			this.getWindow().getAttributes().alpha = TRANSPRA_LEVEL;
			Toast.makeText(mContext, R.string.settingssaved,Toast.LENGTH_SHORT).show();
			break;
		case 2100:
			// DIRECTED FROM CASE 100
			// SETS THE TRANSPARENCY TO 100%
			TRANSPRA_LEVEL = 1.0f;
			edit.putFloat("TRANSPRA_LEVEL", 1.0f);
			edit.commit();
			this.getWindow().getAttributes().alpha = TRANSPRA_LEVEL;
			Toast.makeText(mContext, R.string.settingssaved,Toast.LENGTH_SHORT).show();
			break;

		case 2200:
			// DIRECTED FROM CASE 800
			// SETS THE SETTING TO LOAD ALL FILE PANEL TO LOAD FIRST
			CURRENT_PREF_ITEM = 0;
			edit.putInt("CURRENT_PREF_ITEM", 0);
			edit.commit();
			Toast.makeText(mContext, R.string.settingssaved,Toast.LENGTH_SHORT).show();
			break;
		case 2300:
			// DIRECTED FROM CASE 800
			// SETS THE SETTING TO LOAD SD CARD PANEL-1 TO LOAD FIRST
			CURRENT_PREF_ITEM = 1;
			edit.putInt("CURRENT_PREF_ITEM", 1);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			break;
		case 2400:
			// DIRECTED FROM CASE 800
			// SETS THE SETTING TO LOAD SD CARD PANEL-2 TO LOAD FIRST
			CURRENT_PREF_ITEM = 2;
			edit.putInt("CURRENT_PREF_ITEM", 2);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			break;
		case 2500:
			// DIRECTED FROM CASE 800
			// SETS THE SETTING TO LOAD ALL FILE PANEL TO LOAD FIRST
			// CURRENT_PREF_ITEM = 3;
			edit.putInt("CURRENT_PREF_ITEM", 3);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			break;

		case 2600:
			// LAUNCHES AN ACTIVITY TO SELECT THE DIRECTORY FOR INTERNAL STORAGE
			// 1
			// DIRECTED FROM CASE 900
			new SetLaunchDir(mContext, size.x *8/9, preferences, 1);
			break;
		case 2700:
			// LAUNCHES AN ACTIVITY TO SELECT THE DIRECTORY FOR INTERNAL STORAGE
			// 2
			// DIRECTED FROM CASE 900
			new SetLaunchDir(mContext, size.x *8/9, preferences, 2);
			break;

		case 2800:
			// DIRECTED FROM CAE 90
			// SETS THE FOLDER ICON TO DEFAULT FOLDER ICON
			RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = 0;
			edit.putInt("FOLDER_TYPE", 0);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 2900:
			// DIRECTED FROM CAE 90
			// SETS THE FOLDER ICON TO VIOLET FOLDER ICON
			RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = 1;
			edit.putInt("FOLDER_TYPE", 1);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 3000:
			// DIRECTED FROM CAE 90
			// SETS THE FOLDER ICON TO OXYGEN FOLDER ICON
			RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = 2;
			edit.putInt("FOLDER_TYPE", 2);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 3100:
			// DIRECTED FROM CAE 90
			// SETS THE FOLDER ICON TO YELLOW FOLDER ICON
			RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = 3;
			edit.putInt("FOLDER_TYPE", 3);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 3101:
			// DIRECTED FROM CAE 90
			// SETS THE FOLDER ICON TO YELLOW FOLDER ICON
			RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = 4;
			edit.putInt("FOLDER_TYPE", 4);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;

		case 3102:
			// DIRECTED FROM CAE 90
			// SETS THE FOLDER ICON TO YELLOW FOLDER ICON
			RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = 5;
			edit.putInt("FOLDER_TYPE", 5);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;
		case 3103:
			// DIRECTED FROM CAE 90
			// SETS THE FOLDER ICON TO YELLOW FOLDER ICON
			RootAdapter.FOLDER_TYPE = SimpleAdapter.FOLDER_TYPE = 6;
			edit.putInt("FOLDER_TYPE", 6);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			setAdapter(CURRENT_ITEM);
			break;
		case 3200:
			// DIRECTED FROM CASE 400 ONLY FOR APP PANEL
			// DISPLAYS OPTION FOR SETTING TO SHOW USER OR SYSTEM OR BOTH TYPES
			// OF APP
			QuickAction s = new QuickAction(mContext);
			ActionItem ti;
			if (SHOW_APP == 1)
				ti = new ActionItem(3300, getString(R.string.showdownapp),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				ti = new ActionItem(3300, getString(R.string.showdownapp),getResources().getDrawable(R.drawable.ic_launcher_user));
			
			s.addActionItem(ti);

			if (SHOW_APP == 2)
				ti = new ActionItem(3400, getString(R.string.showsysapp),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				ti = new ActionItem(3400, getString(R.string.showsysapp),getResources().getDrawable(R.drawable.ic_launcher_system));
			s.addActionItem(ti);

			if (SHOW_APP == 3)
				ti = new ActionItem(3500, getString(R.string.showboth),getResources().getDrawable(R.drawable.ic_launcher_apply));
			else
				ti = new ActionItem(3500, getString(R.string.showboth),getResources().getDrawable(R.drawable.ic_launcher_both));
			s.addActionItem(ti);
			s.setOnActionItemClickListener(this);
			s.show(indicator);
			break;
		case 3300:
			// SETS THE SETTING TO SHOW DOWNLOADED APPS ONLY
			// DIRECTED FROM CASE 3200
			edit.putInt("SHOW_APP", 1);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 1;
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;
		case 3400:
			// SETS THE SETTING TO SHOW SYSTEM APPS ONLY
			// DIRECTED FROM CASE 3200
			edit.putInt("SHOW_APP", 2);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 2;
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;

		case 3500:
			// SETS THE SETTING TO SHOW DOWNLOADED AND SYSTEM APPS
			// DIRECTED FROM CASE 3200
			edit.putInt("SHOW_APP", 3);
			edit.commit();
			SHOW_APP = nManager.SHOW_APP = 3;
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
			nList = nManager.giveMeAppList();
			nAppAdapter = new AppAdapter(mContext, R.layout.row_list_1,nList);
			APP_LIST_VIEW.setAdapter(nAppAdapter);
			break;

		case 3600:
			// DIRECTED FROM CASE 900
			// ENABLES OR DISABLES TO SHOW PREFFERED DIRECTORY ON LAUNCH
			if (ENABLE_ON_LAUNCH)
				edit.putBoolean("ENABLE_ON_LAUNCH", ENABLE_ON_LAUNCH = false);
			else
				edit.putBoolean("ENABLE_ON_LAUNCH", ENABLE_ON_LAUNCH = true);
			edit.commit();
			Toast.makeText(mContext, R.string.settingsapplied,Toast.LENGTH_SHORT).show();
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
				String pathwithoutname = filepath.substring(0,
						filepath.length() - filename.length());
				if (pathwithoutname.endsWith("/")) {
					pathwithoutname = pathwithoutname.substring(0,
							pathwithoutname.length() - 1);
				}
				return new File(pathwithoutname);
			}
		}
		return null;
	}

	/**
	 * THIS FUNCTION CONTAINS ALL THE ACTION THAT HAS TO BE PERFORMED WHEN
	 * SEARCH IS IN PROGRESS
	 */
	public void search() {
		SEARCH_FLAG = true;
		searchList = new ArrayList<File>();
		try {
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
			editBox.setHint(R.string.nametofilterout);
			editBox.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
					searchList.clear();
					// TODO Auto-generated method stub
				}
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
					searchList.clear();
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable text) {
					// TODO Auto-generated method stub
					File f = new File(PATH);
					if (CURRENT_ITEM == 2)
						f = new File(RFileManager.getCurrentDirectory());
					else if (CURRENT_ITEM == 1)
						f = new File(SFileManager.getCurrentDirectory());
					final File[] list = f.listFiles();
					final String search = text.toString();
					final String[] fList = f.list();
					new AsyncTask<String[], Void, Void>() {
						@Override
						protected void onPostExecute(Void result) {
							if (SEARCH_FLAG) {
								if (CURRENT_ITEM == 2) {
									root.setAdapter(new RootAdapter(mContext,R.layout.row_list_1, searchList));
								} else if (CURRENT_ITEM == 1) {
									simple.setAdapter(new SimpleAdapter(mContext,R.layout.row_list_1, searchList));
								} else
									LIST_VIEW_3D
											.setAdapter(new MediaElementAdapter(mContext,R.layout.row_list_1,searchList));
							}
						}

						protected void onPreExecute() {
							if (SEARCH_FLAG) {
								if (CURRENT_ITEM == 2)
									root.setAdapter(null);
								else if (CURRENT_ITEM == 1)
									simple.setAdapter(null);
								else
									LIST_VIEW_3D.setAdapter(null);
							}
							super.onPreExecute();
						}

						@Override
						protected Void doInBackground(String[]... arg0) {
							if (SEARCH_FLAG) {
								// SEARCH IS PERFORMED FOR CURRENT ITEM 0
								if (CURRENT_ITEM == 0) {
									int len = mediaFileList.size();
									for (int i = 0; i < len; ++i) {
										File file = mediaFileList.get(i);
										if (file.getName().toLowerCase().contains(search))
											searchList.add(file);
									}
								}
								// SEARCH IS PERFORMED FOR CURRENT ITEM = 1,2
								else
									for (int i = 0; i < fList.length; ++i) {
										if (CURRENT_ITEM == 2) {
											if ((fList[i].toLowerCase()).contains(search.toLowerCase()))
												searchList.add(list[i]);
											
										} else if (CURRENT_ITEM == 1) {
											if ((fList[i].toLowerCase()).contains(search.toLowerCase()))
												searchList.add(list[i]);
										}
									}
							}
							return null;
						}
					}.execute();
				}
			});
		} catch (IllegalStateException e) {

		} catch (RuntimeException e) {

		} catch (Exception e) {

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
	 * THIS FUNCTION SHOWS THE SETTINGS OF THE APPS
	 */
	private void ShowMenu() {
		QuickAction action = new QuickAction(getApplicationContext(), 1);
		ActionItem item = new ActionItem(420, getString(R.string.animation),getResources().getDrawable(R.drawable.ic_launcher_full));
		action.addActionItem(item);
		
		
		item = new ActionItem(80, getString(R.string.appearance),getResources().getDrawable(R.drawable.ic_launcher_appreance));
		action.addActionItem(item);
		
		item = new ActionItem(200, getString(R.string.startup), getResources().getDrawable(R.drawable.ic_launcher_startup));
		action.addActionItem(item);
		if (mViewPager.getCurrentItem() != 3) {
			item = new ActionItem(300, getString(R.string.folderopt),getResources().getDrawable(RootAdapter.FOLDERS[RootAdapter.FOLDER_TYPE]));
			action.addActionItem(item);
			item = new ActionItem(400, getString(R.string.sethomdir),getResources().getDrawable(R.drawable.ic_launcher_droid_home));
			action.addActionItem(item);
		} else if (mViewPager.getCurrentItem() == 3) {
			item = new ActionItem(3200, getString(R.string.apps),getResources().getDrawable(R.drawable.ic_launcher_apk));
			action.addActionItem(item);
		}
		
		
		
		item = new ActionItem(500, getString(R.string.restoretodefault),getResources().getDrawable(R.drawable.ic_launcher_delete));
		action.addActionItem(item);

		item = new ActionItem(501, getString(R.string.cleargesturedata),getResources().getDrawable(R.drawable.ic_launcher_gesture));
		action.addActionItem(item);

		
		
		item = new ActionItem(700, getString(R.string.abtme), getResources().getDrawable(R.drawable.ic_launcher_info));
		action.addActionItem(item);
		action.setAnimStyle(3);
		action.setOnActionItemClickListener(this);
		action.show(indicator);
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
		ImageButton btn = (ImageButton) findViewById(R.id.bottom_multi);
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
		if (ITEM == 0 && MediaElementAdapter.MULTI_SELECT) {
			if (action == 4 && MediaElementAdapter.C != 0) {// DELETE THE
															// MULTIPLE FILES IF
															// ACTIONID = 4
				new DeleteFiles(mContext, size.x*8/9, MediaElementAdapter.MULTI_FILES);
			} else if (action == 5 && MediaElementAdapter.C != 0) {
				MULTIPLE_COPY_GALLERY = true;
				MULTIPLE_COPY = MULTIPLE_CUT = COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = MULTIPLE_CUT_GALLERY = SEARCH_FLAG = false;
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = MediaElementAdapter.MULTI_FILES;
				showToast(MediaElementAdapter.C + " "+ getString(R.string.selectedforcopy));
			} else if (action == 2 && MediaElementAdapter.C != 0) {
				MULTIPLE_CUT_GALLERY = true;
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = MediaElementAdapter.MULTI_FILES;
				MULTIPLE_COPY = MULTIPLE_CUT = COPY_COMMAND = CUT_COMMAND = MULTIPLE_COPY_GALLERY = RENAME_COMMAND = SEARCH_FLAG = false;
				showToast(MediaElementAdapter.C + " "+ getString(R.string.selectedformove));
			} else if (action == 3 && MediaElementAdapter.C != 0) {
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = MediaElementAdapter.MULTI_FILES;
				new CreateZip(mContext, size.x * 8 / 9, COPY_FILES);
			} else
				Toast.makeText(mContext, R.string.firstselectsomefiles,
						Toast.LENGTH_SHORT).show();
		} else if (ITEM == 1 && SimpleAdapter.MULTI_SELECT) {
			if (action == 4 && SimpleAdapter.c != 0) {// DELETE THE MULTIPLE
														// FILES IF ACTIONID = 4
				new DeleteFiles(mContext, size.x*8/9, SimpleAdapter.MULTI_FILES);
			} else if (action == 5 && SimpleAdapter.c > 0) {
				MULTIPLE_COPY = true;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = SimpleAdapter.MULTI_FILES;
				showToast(SimpleAdapter.c + " "+ getString(R.string.selectedforcopy));
			} else if (action == 2 && SimpleAdapter.c > 0) {
				MULTIPLE_CUT = true;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = SimpleAdapter.MULTI_FILES;
				showToast(SimpleAdapter.c + " "+ getString(R.string.selectedformove));
			} else if (action == 3 && SimpleAdapter.c > 0) {
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = SimpleAdapter.MULTI_FILES;
				/*
				 * BUG HAS TO BE FIXED WHILE ZIPPING FILES UNDER ROOT DIRECTORY
				 * FOR NOW SHOWING TOAST MESSAGE SHOWING BCS OF INTERNAL ERROR
				 * COPYING FAILED
				 */
				Toast.makeText(mContext, R.string.serviceUnavaila,Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(mContext, R.string.firstselectsomefiles,Toast.LENGTH_SHORT).show();

		} else if (ITEM == 2 && RootAdapter.MULTI_SELECT) {
			if (action == 4 && RootAdapter.C != 0) {
				// DELETE THE MULTIPLE FILES IF ACTIONID = 4
				new DeleteFiles(mContext, size.x*8/9, RootAdapter.MULTI_FILES);
			} else if (action == 5 && RootAdapter.C != 0) {
				MULTIPLE_COPY = true;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = RootAdapter.MULTI_FILES;
				showToast(RootAdapter.C + " "+ getString(R.string.selectedforcopy));
			} else if (action == 2 && RootAdapter.C != 0) {
				MULTIPLE_CUT = true;
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = RootAdapter.MULTI_FILES;
				COPY_COMMAND = CUT_COMMAND = RENAME_COMMAND = SEARCH_FLAG = false;
				showToast(RootAdapter.C + " "+ getString(R.string.selectedformove));
			} else if (action == 3 && RootAdapter.C != 0) {
				COPY_FILES = new ArrayList<File>();
				COPY_FILES = RootAdapter.MULTI_FILES;
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
						element = new MediaElementAdapter(mContext,R.layout.row_list_1, mediaFileList);
						LIST_VIEW_3D.setAdapter(element);
						LIST_VIEW_3D.setEnabled(true);
						LIST_VIEW_3D.setDynamics(new SimpleDynamics(0.7f, 0.6f));
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
						Toast.makeText(mContext, R.string.empty,
								Toast.LENGTH_SHORT).show();
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
	static void pasteCommand(boolean longC) {
		if ((CUT_COMMAND || COPY_COMMAND)) {
			/**
			 * THIS CONDITION HANDLES THE PASTING OF SINGLE SELECTED FILES ..
			 */
			if (COPY_COMMAND) {
				if (CURRENT_ITEM == 1) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file.getAbsolutePath(), false);
				} else if (CURRENT_ITEM == 2) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file2.getAbsolutePath(), false);
				}
			} else if (CUT_COMMAND) {
				if (CURRENT_ITEM == 1) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file.getAbsolutePath(), true);
				} else if (CURRENT_ITEM == 2) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x * 8 / 9, file2.getAbsolutePath(), true);
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
					dest = file2.getAbsolutePath();
				else
					dest = RFileManager.getCurrentDirectory();
				if (MULTIPLE_COPY)
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, dest, false);
				else if (MULTIPLE_CUT)
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, dest, true);

			} else if (CURRENT_ITEM == 1) {
				if (longC)
					dest = file.getAbsolutePath();
				else
					dest = SFileManager.getCurrentDirectory();
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
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, RFileManager.getCurrentDirectory(),	false);
				} else if (MULTIPLE_CUT_GALLERY) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, RFileManager.getCurrentDirectory(),	true);
				}
			} else if (CURRENT_ITEM == 1) {
				if (MULTIPLE_COPY_GALLERY) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, SFileManager.getCurrentDirectory(),false);
				} else if (MULTIPLE_CUT_GALLERY) {
					new MultipleCopyDialog(mContext, COPY_FILES,size.x*8/9, SFileManager.getCurrentDirectory(),	true);
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
				if (ACTION.equalsIgnoreCase("FQ_BACKUP")|| ACTION.equals(Intent.ACTION_UNINSTALL_PACKAGE))
					refreshList(CURRENT_ITEM);
				else if (ACTION.equalsIgnoreCase("FQ_DELETE")) {
					// setAdapter(CURRENT_ITEM);
					refreshList(CURRENT_ITEM);
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
						if (new File(value).isFile()) {
							mViewPager.setCurrentItem(CURRENT_ITEM);
							if(CURRENT_ITEM==1)
								file = new File(value);
							else if(CURRENT_ITEM ==2)
								file2 = new File(value);
							new OpenFileDialog(mContext, Uri.parse(value),size.x*8/9);
						} else {
							if (CURRENT_ITEM == 1) {
								SFileManager.nStack.push(value);
							} else if (CURRENT_ITEM == 2){
								RFileManager.nStack.push(value);
							}	
							setAdapter(CURRENT_ITEM);
						}
					} else
						Toast.makeText(mContext, R.string.filedoesnotexists,Toast.LENGTH_SHORT).show();

				}else if(ACTION.equalsIgnoreCase("FQ_ZIP_OPEN")){
					if(CURRENT_ITEM==0||CURRENT_ITEM==2){
						ZIP_ROOT = true;
						RFileManager.nStack.push("/ -> Zip");
					}	
					else{
						ZIP_SIMPLE = true;
						SFileManager.nStack.push("/ -> Zip");
					}	
					setZipAdapter();
				}else if(ACTION.equalsIgnoreCase("FQ_EXTRACT_PATH")){
					String path = it.getStringExtra("extract_path");
					if(CURRENT_ITEM==1){
						if(new File(path).canWrite())
							//IF WE HAVE WRITE PERMISSION THEN EXTRACT HERE....
							new ExtractZipFile(mContext, zFileSimple, size.x*8/9, path, file, 1);
						else
							//WE DONT HAVE WRITE PERMISSION..... 
							Toast.makeText(mContext, R.string.cannotexthere, Toast.LENGTH_SHORT).show();
					}else if(CURRENT_ITEM == 2)
						new ExtractZipFile(mContext, zFileRoot, size.x*8/9, path, file2, 1);
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
						}

						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							if(CURRENT_ITEM==2)
								root.setAdapter(null);
						}

						@Override
						protected Void doInBackground(Void... arg0) {
							// TODO Auto-generated method stub
							if(CURRENT_ITEM==2){
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
	 *THIS FUNCTION SETS THE ADAPTER WHEN ZIP FILE IS OPERATED.... 
	 */
	private static void setZipAdapter(){
		final Handler handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==0 && (ZIP_ROOT||ZIP_SIMPLE)){
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(CURRENT_ITEM);
				}else{
					/**
					 * ZIP ARCHIVE IS CORRUPTED OR AN ERROR WAS OCCURED WHILE READING...
					 */
					if(CURRENT_ITEM==1)
						SFileManager.nStack.pop();
					else if(CURRENT_ITEM==2)
						RFileManager.nStack.pop();
					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setCurrentItem(CURRENT_ITEM);
					Toast.makeText(mContext, R.string.zipexception, Toast.LENGTH_SHORT).show();
				}
			}
		};
		Thread thr = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(zipPathRoot==null)
					zipPathRoot = "/";
				
				if(zipPathSimple == null)
					zipPathSimple = "/";
				
				if(CURRENT_ITEM==2||CURRENT_ITEM==0){
					try {
						File fi = file2;
						if(CURRENT_ITEM==0){
							CURRENT_ITEM = 2;
							fi = file0;
						}
						ZipFile zf = new ZipFile(fi);
						zListRoot = new ZipManager(zf, zipPathRoot, mContext).generateList();
						handle.sendEmptyMessage(0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ZIP_ROOT = false;
						handle.sendEmptyMessage(1);
					} 
				}else if(CURRENT_ITEM==1){
					try{
						ZipFile zFile = new ZipFile(file);
						zListSimple = new ZipManager(zFile, zipPathSimple, mContext).generateList();
						handle.sendEmptyMessage(0);
					}catch(IOException e){
						ZIP_SIMPLE = false;
						handle.sendEmptyMessage(1);
					}
				}
			}
		});		
		thr.start();
	}
}
