package org.anurag.file.quest;

import java.util.ArrayList;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterLoaders {

	static String list[] = {"DropBox","Box","Google Drive","SkyDrive"};
	Context mContext;
	boolean status; 
	public AdapterLoaders(Context context,boolean st){
		mContext = context;
		status =st;
	}	
	
	public static Adapter getCloudAdapter(Context con){
		return new Adapter(con, R.layout.row_list_2, list);
	}
	/**
	 * Adapter for CLOUD SERVICES LIST....
	 * @author anurag
	 *
	 */
	private static class ItemHolder{
		ImageView Icon;
		TextView Name;
	}
	
	public static class Adapter extends ArrayAdapter<String>{
		public Adapter(Context context, int textViewResourceId,	String[] objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			list = objects;
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ItemHolder item = new ItemHolder();
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				item.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				item.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(item);
			}else
				item = (ItemHolder)convertView.getTag();
				
			if(position == 0){
					item.Icon.setBackgroundResource(R.drawable.ic_launcher_drop_box);
					item.Name.setText("DropBox");
			}
			else if(position == 1){
				item.Icon.setBackgroundResource(R.drawable.ic_launcher_google_drive);
				item.Name.setText("Google Drive");
			}else if(position == 2){
				item.Icon.setBackgroundResource(R.drawable.ic_launcher_sky_drive);
				item.Name.setText("Sky drive");
			}	
			return convertView;
		}
	}
	
	
	
	
	
	
	
	
	public LongClickAdapter getLongClickAdapter(){
		ArrayList<String> l = new ArrayList<String>();
		l.add("");
		l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");l.add("");
		ArrayList<String> list = new ArrayList<String>();
		list.add("");list.add("");list.add("");list.add("");list.add("");list.add("");list.add("");
		if(status)
			return new LongClickAdapter(mContext, R.layout.row_list_2, list);
		else
			return new LongClickAdapter(mContext, R.layout.row_list_2,l);
	}
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	private static class LongClickHolder{
		ImageView Icon;
		TextView Name;
	}
	/**
	 * 
	 * @author anurag
	 *
	 */
	private class LongClickAdapter extends ArrayAdapter<String>{
		public LongClickAdapter(Context context, int textViewResourceId ,ArrayList<String> objects) {
			super(context, textViewResourceId, objects);
			
    	}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LongClickHolder ho; 
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_list_2, parent , false);
				ho = new LongClickHolder();
				ho.Icon = (ImageView)convertView.findViewById(R.id.iconImage2);
				ho.Name = (TextView)convertView.findViewById(R.id.directoryName2);
				convertView.setTag(ho);
			}else
				ho = (LongClickHolder)convertView.getTag();
			
			if(status){
				if(position == 0){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_open);
					ho.Name.setText("Launch App");
				}else if(position == 1){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_uninstall);
					ho.Name.setText("Uninstall");
				}else if(position == 2){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_backupall);
					ho.Name.setText("Take Backup");
				}else if(position == 3){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_delete);
					ho.Name.setText("Delete Earlier Backups");
				}else if(position == 4){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_zip_it);
					ho.Name.setText("Create Flashable Zip");
				}else if(position == 5){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_share);
					ho.Name.setText("Share App");
				}else if(position == 6){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_stats);
					ho.Name.setText("App Properties");
				}
			}
			else{
				if(position == 0){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_open);
					ho.Name.setText("Open");
				}else if(position == 2){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_copy);
					ho.Name.setText("Copy");
				}else if(position == 3){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_cut);
					ho.Name.setText("Cut");
				}else if(position == 5){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_zip_it);
					ho.Name.setText("Create Zip");
				}else if(position == 6){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_delete);
					ho.Name.setText("Delete");
				}else if(position == 7){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_rename);
					ho.Name.setText("Rename");
				}else if(position == 8){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_share);
					ho.Name.setText("Share");
				}else if(position == 9){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_stats);
					ho.Name.setText("Properties");
				}else if(position == 4){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_paste);
					ho.Name.setText("Paste");
				}else if(position == 1){
					ho.Icon.setBackgroundResource(R.drawable.ic_launcher_drop_box);
					ho.Name.setText("Copy to cloud");
				}
			}
			return convertView;
		}
	}
	
}
