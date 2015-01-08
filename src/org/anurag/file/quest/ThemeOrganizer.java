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

public class ThemeOrganizer {
	
	public static void BUILD_THEME(int clr){
		switch(clr){
		case 0xFFC74B46:
			Constants.DIALOG_STYLE = R.style.Dialog_Red;
			Constants.SELECTOR_STYLE = R.drawable.list_selector_red_hd;
			break;
		
		case 0xFF53AB3A:
			Constants.DIALOG_STYLE = R.style.Dialog_Green;
			Constants.SELECTOR_STYLE = R.drawable.list_selector_green_hd;
			break;
			
		case 0xFF666666:
			Constants.DIALOG_STYLE = R.style.Dialog_Grey;
			Constants.SELECTOR_STYLE = R.drawable.list_selector_grey_hd;
			break;	
			
		case 0xFFFF5D3D:
			Constants.DIALOG_STYLE = R.style.Dialog_Orange;
			Constants.SELECTOR_STYLE = R.drawable.list_selector_orange_hd;
			break;	
			
		case 0xFF3F9FE0:
			Constants.DIALOG_STYLE = R.style.Dialog_Blue;
			Constants.SELECTOR_STYLE = R.drawable.list_selector_blue_hd;
			break;	
			
		case 0xFF5161BC:
			Constants.DIALOG_STYLE = R.style.Dialog_Violet;
			Constants.SELECTOR_STYLE = R.drawable.list_selector_violet_hd;
			break;
		}
	}

}
