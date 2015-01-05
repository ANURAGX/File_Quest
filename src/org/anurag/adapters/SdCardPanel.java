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

import org.anurag.file.quest.Constants;
import org.anurag.file.quest.Item;
import org.anurag.file.quest.OpenFileDialog;
import org.anurag.file.quest.R;
import org.anurag.file.quest.SDAdapter;
import org.anurag.file.quest.SDManager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class SdCardPanel extends Fragment{
	
	private ListView list;
	private ArrayList<Item> adapter_list;
	private LoadList load;
	private SDManager manager;
	
	public SdCardPanel() {
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
		list = (ListView)view.findViewById(R.id.list_view_hd);		
		list.setSelector(R.drawable.list_selector_hd);
		if(load == null){
			load = new LoadList();
			load.execute();
		}	
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Item item = adapter_list.get(position);
				if(item.isDirectory()){
					//selecting a folder....
					manager.pushPath(item.getPath());
					load = new LoadList();
					load.execute();
				}else{
					//selecting a file....
					new OpenFileDialog(getActivity(), Uri.parse(item.getPath())
							, Constants.size.x*8/9);
				}
			}
		});
	}
	
	
	
	/**
	 * 
	 * @author anurag
	 *
	 */
	private class LoadList extends AsyncTask<Void , Void , Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			list.setAdapter(new SDAdapter(getActivity(), adapter_list));
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(manager == null)
				manager = new SDManager(getActivity());
			adapter_list = manager.getList();
			return null;
		}		
	}	
}
