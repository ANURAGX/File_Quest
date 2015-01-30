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
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.file.quest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.anurag.file.quest.Item;

/**
 * this class contains sorting functions ....
 * @author anurag
 *
 */
public class FileUtils {
	
	/**
	 * this function sorts the list of files in a-z order
	 * keeping folders at first
	 */
	public ArrayList<Item> a_zSort(ArrayList<Item> items){
		Comparator<Item> compare = new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				// TODO Auto-generated method stub
				boolean lhsIsFolder = lhs.isDirectory();
				boolean rhsIsFolder = rhs.isDirectory();
				if(lhsIsFolder == rhsIsFolder)
					return lhs.getName().compareTo(rhs.getName());
				if(rhsIsFolder)
					return 1;
				return -1;
			}
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in z-a order
	 * keeping folders at first
	 */
	public ArrayList<Item> z_aSort(ArrayList<Item> items){
		Comparator<Item> compare = new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				// TODO Auto-generated method stub
				boolean lhsIsFolder = lhs.isDirectory();
				boolean rhsIsFolder = rhs.isDirectory();
				if(lhsIsFolder == rhsIsFolder)
					return -lhs.getName().compareTo(rhs.getName());
				if(rhsIsFolder)
					return 1;
				return -1;
			}
		};
		Collections.sort(items, compare);		
		return items;
	}

	/**
	 * this function sorts the list of files in small to big size order
	 * keeping folders at first
	 */
	public ArrayList<Item> smallSize_Sort(ArrayList<Item> items){
		Comparator<Item> compare = new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				// TODO Auto-generated method stub
				boolean lhsIsFolder = lhs.isDirectory();
				boolean rhsIsFolder = rhs.isDirectory();
				if(lhsIsFolder == rhsIsFolder){
					if(lhsIsFolder)
						return lhs.getName().compareTo(rhs.getName());
					else{
						long lhslen = lhs.getFile().length();
						long rhsLen = rhs.getFile().length();
						
						if(lhslen < rhsLen)
							return -1;
						else if (lhslen > rhsLen)
							return 1;
						else 
							return 0;
					}
				}if(rhsIsFolder)
					return 1;
				return -1;
			}
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in big to small size order
	 * keeping folders at first
	 */
	public ArrayList<Item> bigSize_Sort(ArrayList<Item> items){
		Comparator<Item> compare = new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				// TODO Auto-generated method stub
				boolean lhsIsFolder = lhs.isDirectory();
				boolean rhsIsFolder = rhs.isDirectory();
				if(lhsIsFolder == rhsIsFolder){
					if(lhsIsFolder)
						return lhs.getName().compareTo(rhs.getName());
					else{
						long lhslen = lhs.getFile().length();
						long rhsLen = rhs.getFile().length();
						
						if(lhslen < rhsLen)
							return 1;
						else if (lhslen > rhsLen)
							return -1;
						else 
							return 0;
					}
				}if(rhsIsFolder)
					return 1;
				return -1;
			}
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in small to big size order
	 * keeping folders at first
	 */
	public ArrayList<Item> newDate_Sort(ArrayList<Item> items){
		Comparator<Item> compare = new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				// TODO Auto-generated method stub
				boolean lhsIsFolder = lhs.isDirectory();
				boolean rhsIsFolder = rhs.isDirectory();
				if(lhsIsFolder == rhsIsFolder){
					if(lhsIsFolder)
						return lhs.getName().compareTo(rhs.getName());
					else{
						long lhslen = lhs.getFile().lastModified();
						long rhsLen = rhs.getFile().lastModified();
					
						Date lhsD = new Date(lhslen);
						Date rhsD = new Date(rhsLen);
						return -lhsD.compareTo(rhsD);
					}
				}if(rhsIsFolder)
					return 1;
				return -1;
			}
		};
		Collections.sort(items, compare);		
		return items;
	}
	
	/**
	 * this function sorts the list of files in small to big size order
	 * keeping folders at first
	 */
	public ArrayList<Item> oldDate_Sort(ArrayList<Item> items){
		Comparator<Item> compare = new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				// TODO Auto-generated method stub
				boolean lhsIsFolder = lhs.isDirectory();
				boolean rhsIsFolder = rhs.isDirectory();
				if(lhsIsFolder == rhsIsFolder){
					if(lhsIsFolder)
						return lhs.getName().compareTo(rhs.getName());
					else{
						long lhslen = lhs.getFile().lastModified();
						long rhsLen = rhs.getFile().lastModified();
					
						Date lhsD = new Date(lhslen);
						Date rhsD = new Date(rhsLen);
						return lhsD.compareTo(rhsD);
					}
				}if(rhsIsFolder)
					return 1;
				return -1;
			}
		};
		Collections.sort(items, compare);		
		return items;
	}
}
