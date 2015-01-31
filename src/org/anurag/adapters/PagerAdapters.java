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

package org.anurag.adapters;
import org.anurag.fragments.AppStore;
import org.anurag.fragments.FileGallery;
import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapters extends FragmentStatePagerAdapter{

	private static String TITLES[] = {"FILE GALLERY" , "/" , "SDCARD" , "APP STORE"}; 
	
	public PagerAdapters(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 0:
			Fragment frag1 = new FileGallery();
			//FileQuestHD.pager.setObjectForPosition(frag1, arg0);
			return frag1;
		
		case 1:
			Fragment frag2 = new RootPanel();
			//FileQuestHD.pager.setObjectForPosition(frag2, arg0);
			return frag2;
			
		case 2:
			Fragment frag3 = new SdCardPanel();
			//FileQuestHD.pager.setObjectForPosition(frag3, arg0);
			return frag3;
			
		case 3:
			Fragment frag4 = new AppStore();
			//FileQuestHD.pager.setObjectForPosition(frag4, arg0);
			return frag4;
			
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return TITLES[position];
	}
	
	/**
	 * 
	 * @param position
	 * @param title
	 */
	public static void setTitles(int position , String title){
		TITLES[position] = title;
	}	
}
