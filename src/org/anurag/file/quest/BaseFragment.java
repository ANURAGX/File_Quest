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



import android.support.v4.app.ListFragment;
import android.widget.ListView;

import com.extra.libs.JazzyHelper;


/**
 * THIS CLASS ADDS THE JAZZY LIST VIEW ANIMATION TO THE DEFAULT 
 * LIST VIEW OF LISTFRAGMENT CLASS...
 * @author anurag
 *
 */
public class BaseFragment extends ListFragment{

	int effect;
	public BaseFragment(int animEffect) {
		// TODO Auto-generated constructor stub
		effect = animEffect;
	}
	
	@Override
	public ListView getListView() {
		// TODO Auto-generated method stub
		ListView lv = super.getListView(); 
		JazzyHelper help = new JazzyHelper(getActivity(), null);
		help.setTransitionEffect(effect);
		lv.setOnScrollListener(help);
		return lv;
	}
}
