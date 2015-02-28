/**
 * Copyright(c) 2015 DRAWNZER.ORG PROJECTS -> ANURAG
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * this class is more like FileUtils class but it operates on apps....
 * 
 * this class contains sorting functions ....
 * @author anurag
 *
 */
public class AppUtils {
	
	private PackageManager mgr;
	
	public AppUtils(PackageManager manager) {
		// TODO Auto-generated constructor stub
		this.mgr = manager;
	}
	
	/**
	 * this function sorts the list of files in a-z order
	 * keeping folders at first
	 */
	public ArrayList<ApplicationInfo> a_zSort(ArrayList<ApplicationInfo> items){
		Comparator<ApplicationInfo> compare = new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
				// TODO Auto-generated method stub
				return lhs.loadLabel(mgr).toString().compareToIgnoreCase(rhs.loadLabel(mgr).toString());
			}
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in z-a order
	 * keeping folders at first
	 */
	public ArrayList<ApplicationInfo> z_aSort(ArrayList<ApplicationInfo> items){
		Comparator<ApplicationInfo> compare = new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
				// TODO Auto-generated method stub
				return -lhs.loadLabel(mgr).toString().compareToIgnoreCase(rhs.loadLabel(mgr).toString());
			}
		};
		Collections.sort(items, compare);		
		return items;
	}

	/**
	 * this function sorts the list of files in small to big size order
	 * keeping folders at first
	 */
	public ArrayList<ApplicationInfo> smallSize_Sort(ArrayList<ApplicationInfo> items){
		Comparator<ApplicationInfo> compare = new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
				// TODO Auto-generated method stub
				long lhslen = new File(lhs.sourceDir).length();
				long rhsLen = new File(rhs.sourceDir).length();
				if(lhslen < rhsLen)
					return -1;
				else if (lhslen > rhsLen)
					return 1;
				else 
					return 0;
				}
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in big to small size order
	 * keeping folders at first
	 */
	public ArrayList<ApplicationInfo> bigSize_Sort(ArrayList<ApplicationInfo> items){
		Comparator<ApplicationInfo> compare = new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
				// TODO Auto-generated method stub
				long lhslen = new File(lhs.sourceDir).length();
				long rhsLen = new File(rhs.sourceDir).length();
				if(lhslen < rhsLen)
					return 1;
				else if (lhslen > rhsLen)
					return -1;
				else 
					return 0;
			}
			
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in small to big size order
	 * keeping folders at first
	 */
	public ArrayList<ApplicationInfo> newDate_Sort(ArrayList<ApplicationInfo> items){
		Comparator<ApplicationInfo> compare = new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
				// TODO Auto-generated method stub
				long lhslen = new File(lhs.sourceDir).lastModified();
				long rhsLen = new File(rhs.sourceDir).lastModified();
				Date lhsD = new Date(lhslen);
				Date rhsD = new Date(rhsLen);
				return -lhsD.compareTo(rhsD);
			}			
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in small to big size order
	 * keeping folders at first
	 */
	public ArrayList<ApplicationInfo> oldDate_Sort(ArrayList<ApplicationInfo> items){
		Comparator<ApplicationInfo> compare = new Comparator<ApplicationInfo>() {
			@Override
			public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
				// TODO Auto-generated method stub
				long lhslen = new File(lhs.sourceDir).lastModified();
				long rhsLen = new File(rhs.sourceDir).lastModified();
				Date lhsD = new Date(lhslen);
				Date rhsD = new Date(rhsLen);
				return lhsD.compareTo(rhsD);
			}			
		};
		Collections.sort(items, compare);		
		return items;
	}
}
