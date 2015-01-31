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


import org.anurag.fragments.RootPanel;
import org.anurag.fragments.SdCardPanel;

import android.content.Context;


/**
 * this class builds the theme as per the color scheme at start 
 * of the app as well as during the runtime....
 * @author Anurag
 *
 */
public class ThemeOrganizer {
	
	public static void BUILD_THEME(int clr){
		switch(clr){
		case 0xFFC74B46:
			Constants.DIALOG_STYLE = R.style.Dialog_Red;
			//Constants.SELECTOR_STYLE = R.drawable.list_selector_red_hd;
			Constants.FOLDER_ICON = 1;
			break;
		
		case 0xFF18ca5b:
			Constants.DIALOG_STYLE = R.style.Dialog_Green;
			//Constants.SELECTOR_STYLE = R.drawable.list_selector_green_hd;
			Constants.FOLDER_ICON = 3;
			break;
			
		case 0xFF222222:
			Constants.DIALOG_STYLE = R.style.Dialog_Grey;
			//Constants.SELECTOR_STYLE = R.drawable.list_selector_grey_hd;
			Constants.FOLDER_ICON = 0;
			break;	
			
		case 0xFFFF5D3D:
			Constants.DIALOG_STYLE = R.style.Dialog_Orange;
			//Constants.SELECTOR_STYLE = R.drawable.list_selector_orange_hd;
			Constants.FOLDER_ICON = 2;
			break;	
			
		case 0xFF3F9FE0:
			Constants.DIALOG_STYLE = R.style.Dialog_Blue;
			//Constants.SELECTOR_STYLE = R.drawable.list_selector_blue_hd;
			Constants.FOLDER_ICON = 4;
			break;	
			
		case 0xFF5161BC:
			Constants.DIALOG_STYLE = R.style.Dialog_Violet;
			//Constants.SELECTOR_STYLE = R.drawable.list_selector_violet_hd;
			Constants.FOLDER_ICON = 5;
			break;
		}		
	}

	/**
	 * builds the folder icon image when theme changes or created first....
	 * @param fileQuestHD
	 */
	public static void BUILD_FOLDER_ICON(Context fileQuestHD) {
		// TODO Auto-generated method stub
		Constants.FOLDER_IMAGE = fileQuestHD.getResources().getDrawable(Constants.FOLDERS[Constants.FOLDER_ICON]);
	}

	/**
	 *
	 * after theme creation its time to fpply folder icon change to 
	 * all the applicable list views....
	 *
	 * @param first is current panel opened and load first for the current panel....
	 */
	public static void APPLY_FOLDER_THEME(int first){
		if(first == 2){
			if(!Constants.LONG_CLICK[2])
				SdCardPanel.notifyDataSetChanged();
			
			if(!Constants.LONG_CLICK[1])
				RootPanel.notifyDataSetChanged();
			return;
		}
		if(!Constants.LONG_CLICK[1])
			RootPanel.notifyDataSetChanged();
		if(!Constants.LONG_CLICK[2])
			SdCardPanel.notifyDataSetChanged();		
	}

	/*
	public static void UPDATE_LIST_SELECTORS(){
		//updating the list selector as per new theme....
		RootPanel.setListSelector();
		SdCardPanel.setListSelector();
		AppStore.setListSelector();
		FileGallery.setListSelector();
	}
	*/
}
