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


package org.anurag.dialogs;

import org.anurag.file.quest.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 
 * this class shows a dialog when app is updated,
 * shows the new new feature of this update.
 * 
 * @author anurag
 *
 */
public class WhatsNew {

	public WhatsNew(Context ctx) {
		// TODO Auto-generated constructor stub
		LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inf.inflate(R.layout.whats_new, null , false);
		new MaterialDialog.Builder(ctx)
		.customView(view, true)
		.title("What's new")
		.positiveText(R.string.ok)
		.show();
	}
}
