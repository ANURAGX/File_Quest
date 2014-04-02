package org.anurag.file.quest;

import java.io.File;

import java.util.ArrayList;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class EmptyAdapter extends ArrayAdapter<String>{

	
	
	private ArrayList<String> list;
	Context context;
	
	public EmptyAdapter(Context con, int textViewResourceId, ArrayList<String> objects) {
		super(con , textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		list = objects;
		context = con;
	}
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	class Holder{
		
		TextView fName;
		
		
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final String f = list.get(pos);
		Holder h = new Holder();
		if(convertView == null){
			h = new Holder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.empty_adapter, arg2 , false);
			h.fName = (TextView)convertView.findViewById(R.id.empty_text);
			
			
			convertView.setTag(h);
		}
		
		else
			h = (Holder)convertView.getTag();
		
		
		h.fName.setText(f);
		return convertView;
	}
	
	
		
}
