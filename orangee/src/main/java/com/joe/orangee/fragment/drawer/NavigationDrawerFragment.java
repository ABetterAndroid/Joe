package com.joe.orangee.fragment.drawer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.joe.orangee.R;
import com.joe.orangee.activity.my.MyCommentActivity;
import com.joe.orangee.activity.my.MyMentionActivity;
import com.joe.orangee.activity.person.PersonPageActivity;
import com.joe.orangee.activity.search.SearchActivity;
import com.joe.orangee.activity.settings.SettingsActivity;
import com.joe.orangee.activity.weibo.NearbyMapWeiboActivity;
import com.joe.orangee.activity.weibo.NearbyWeiboMapActivity;
import com.joe.orangee.activity.weibo.WeiboCollectionActivity;
import com.joe.orangee.listener.OrangeeImageLoadingListener;
import com.joe.orangee.model.User;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.PersonDownloader;
import com.joe.orangee.util.PreferencesKeeper;
import com.joe.orangee.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NavigationDrawerFragment extends Fragment {

	private User person;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private ImageView ivAvatar;
	private TextView tvName;
	private TextView tvFollow;
	private TextView tvFollower;
	private TextView tvStatus;
	private Activity activity;

	public NavigationDrawerFragment(Activity activity) {
		this.activity=activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDrawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		Utils.setTopPadding(getActivity(), mDrawerListView);
		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						selectItem(position);
					}
				});
		mDrawerListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.left_drawer_list)));
		View headerView=View.inflate(getActivity(), R.layout.drawer_left_header, null);
		ivAvatar = (ImageView) headerView.findViewById(R.id.user_avatar);
		tvName = (TextView) headerView.findViewById(R.id.user_name);
		tvFollow = (TextView) headerView.findViewById(R.id.user_follow);
		tvFollower = (TextView) headerView.findViewById(R.id.user_follower);
		tvStatus = (TextView) headerView.findViewById(R.id.user_status);
		
		fillLeftDrawer();
		mDrawerListView.addHeaderView(headerView);
		
		return mDrawerListView;
	}

	private void fillLeftDrawer() {
		if (PreferencesKeeper.readUserInfo(getActivity())!=null) {
			fillViews(PreferencesKeeper.readUserInfo(getActivity()));
			return;
		}
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				person = new PersonDownloader(getActivity()).getPersonInfo(null, PreferencesKeeper.readAccessToken(getActivity()).getUid());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (person!=null) {
					fillViews(person);
					PreferencesKeeper.writeUserInfo(getActivity(), person);
				}
				super.onPostExecute(result);
			}

		}.execute();
		
	}
	private void fillViews(User person) {
		OrangeeImageLoadingListener.LoadingListener mListener= new OrangeeImageLoadingListener.LoadingListener();
		ImageLoader.getInstance().displayImage(person.getAvatar(), ivAvatar, Utils.getRoundedPicDisplayImageOptions(), mListener);
		tvName.setText(person.getName());
		Resources res=getActivity().getResources();
		tvFollow.setText(res.getString(R.string.person_follow)+""+person.getFriends_count());
		tvFollower.setText(res.getString(R.string.person_follower)+""+person.getFollowers_count()+"");
		tvStatus.setText(res.getString(R.string.person_status)+""+person.getStatuses_count()+"");
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {
//		mDrawerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerToggle = new ActionBarDrawerToggle(activity,
		mDrawerLayout,
		toolbar,
		R.string.navigation_drawer_open,
		R.string.navigation_drawer_close
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}
		};

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(final int position) {
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(Gravity.START);
		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				switch (position) {
				case 0:
					if (PreferencesKeeper.readUserInfo(getActivity())!=null) {
						Intent intent=new Intent(getActivity(), PersonPageActivity.class);
						intent.putExtra("User", PreferencesKeeper.readUserInfo(getActivity()));
						intent.setExtrasClassLoader(WeiboStatus.class.getClassLoader());
						getActivity().startActivity(intent);
					}
					
					break;
				case 1:
					startActivity(new Intent(getActivity(), WeiboCollectionActivity.class));
					break;
				case 2:
//					startActivity(new Intent(getActivity(), NearbyWeiboActivity.class));
					startActivity(new Intent(getActivity(), NearbyMapWeiboActivity.class));
					break;
				case 3:
					startActivity(new Intent(getActivity(), MyCommentActivity.class));
					break;
				case 4:
					startActivity(new Intent(getActivity(), MyMentionActivity.class));
					break;
				case 5:
                    startActivity(new Intent(getActivity(), SearchActivity.class));
					break;
				case 6:
					startActivity(new Intent(getActivity(), SettingsActivity.class));
					break;
				default:
					break;
				}
			}
		}, 200);
	}

/*	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}*/

	/*@Override
	public void onDetach() {
		super.onDetach();
//		mCallbacks = null;
	}*/

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}*/

}