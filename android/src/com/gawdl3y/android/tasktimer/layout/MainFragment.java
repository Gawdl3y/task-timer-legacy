package com.gawdl3y.android.tasktimer.layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.gawdl3y.android.tasktimer.R;
import com.gawdl3y.android.tasktimer.adapters.TaskListFragmentAdapter;
import com.gawdl3y.android.tasktimer.pojos.Group;
import com.gawdl3y.android.tasktimer.util.Log;

import java.util.ArrayList;

/**
 * The main fragment for Task Timer; contains a TaskListFragmentAdapter
 * @author Schuyler Cebulskie
 */
public class MainFragment extends SherlockFragment {
    private static final String TAG = "MainFragment";

    // Data
    private ArrayList<Group> groups;

    // Stuff
    private ViewPager pager;

    /**
     * The fragment is being created
     * @param savedInstanceState The saved instance state
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            // Load from saved instance
            groups = savedInstanceState.getParcelableArrayList("groups");
        } else if(getArguments() != null) {
            // Load from arguments
            groups = getArguments().getParcelableArrayList("groups");
        }
    }

    /**
     * The view for the fragment is being created
     * @param inflater           The LayoutInflater to use
     * @param container          The container
     * @param savedInstanceState The saved instance state
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create the view and pager
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);

        // Set the adapter of the pager
        new SetAdapterTask().execute();

        Log.v(TAG, "View created");
        return view;
    }

    /**
     * The instance of the fragment is being saved
     * @param savedInstanceState The saved instance state
     * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("groups", groups);
    }

    /**
     * Sets the adapter of the ViewPager
     * @author Schuyler Cebulskie
     */
    private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
        /**
         * Does absolutely nothing
         * @param params Useless
         * @return Nothing
         */
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        /**
         * Sets the adapter of the ViewPager
         * @param result Nothing
         */
        @Override
        protected void onPostExecute(Void result) {
            pager.setAdapter(new TaskListFragmentAdapter(getFragmentManager(), groups));
            Log.v(TAG, "Set ViewPager adapter");
        }
    }


    /**
     * Gets the ViewPager
     * @return The ViewPager
     */
    public ViewPager getPager() {
        return pager;
    }

    /**
     * Gets the adapter of the ViewPager
     * @return The adapter of the ViewPager
     */
    public TaskListFragmentAdapter getAdapter() {
        return (TaskListFragmentAdapter) pager.getAdapter();
    }


    /**
     * Creates a new instance of MainFragment
     * @param groups The groups
     * @return A new instance of MainFragment
     */
    public static MainFragment newInstance(ArrayList<Group> groups) {
        // Create the arguments for the fragment
        Bundle args = new Bundle();
        args.putParcelableArrayList("groups", groups);

        // Create the fragment
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
