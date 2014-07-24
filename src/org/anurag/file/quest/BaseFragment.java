package org.anurag.file.quest;

import com.twotoasters.jazzylistview.JazzyHelper;

import android.support.v4.app.ListFragment;
import android.widget.ListView;


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
