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

package org.anurag.adapters;

import java.util.ArrayList;

import org.anurag.file.quest.AppAdapter;
import org.anurag.file.quest.AppBackup;
import org.anurag.file.quest.AppManager;
import org.anurag.file.quest.Constants;
import org.anurag.file.quest.R;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class AppStore extends Fragment{
	
	private ListView ls;
	private ArrayList<ApplicationInfo> apps;
	private LoadApps load;
	private AppManager manager;
	public AppStore() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.list_view_hd, container , false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		ls = (ListView) view.findViewById(R.id.list_view_hd);
		ls.setSelector(Constants.SELECTOR_STYLE);
		if(load == null){
			load = new LoadApps();
			load.execute();
		}			
		
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ArrayList<ApplicationInfo> infos = new ArrayList<ApplicationInfo>();
				infos.add(apps.get(arg2));
				new AppBackup(getActivity(), Constants.size.x*8/9, infos);
			}
		});
	}
	
	/**
	 * 
	 * @author anurag
	 *
	 */
	private class LoadApps extends AsyncTask<Void, Void , Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ls.setAdapter(new AppAdapter(getActivity(), R.layout.row_list_2, apps));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(manager == null)
				manager = new AppManager(getActivity());
			apps = manager.get_downloaded_apps();
			return null;
		}
		
	}
	
}
