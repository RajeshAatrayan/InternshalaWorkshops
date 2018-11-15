package com.ibrickedlabs.internshala.Frags;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ibrickedlabs.internshala.R;
import com.ibrickedlabs.internshala.WorkshopsData.WorkshopAdapter;
import com.ibrickedlabs.internshala.WorkshopsData.WorkshopContract;
import com.ibrickedlabs.internshala.WorkshopsData.WorkshopDbHelper;

public class WorkshopsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int WORKSHOP_LOADER = 101;
    //ListView
    ListView mWorkshopListView;
    //Adapter
    WorkshopAdapter mAdapter;
    // WorkshopDbHelper reference variable
    WorkshopDbHelper mWorkshopDbHelper;

    //Context reference variable
    Context context;

    //Workshop items
    String workshopNames[] = {
            "Android", "Artificial Intelligence", "BigData Bootcamp", "BlockChain Technology", "Cyber Security", "CloudComputing", "Datascience Bootcamp", "Iot", "Machine Learning", "SAP Bootcamp", "Web Development"
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mWorkshopDbHelper = new WorkshopDbHelper(context);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        //Getting the root reference from the layout
        View view = inflater.inflate(R.layout.workshops_layout, container, false);
        mWorkshopListView = (ListView) view.findViewById(R.id.workshopListView);
        mAdapter = new WorkshopAdapter(context, null);
        mWorkshopListView.setAdapter(mAdapter);
        mWorkshopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("POSITION", String.valueOf(id));
                bundle.putString("SELECTEDWORKSHOPNAME", workshopNames[position]);
                Fragment fragment = new WorkshopDetails();
                fragment.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFragmentLayout, fragment).addToBackStack("AddWorkshopDetails").setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_left).commit();

            }
        });
        //Kicking off the loader
        getLoaderManager().initLoader(WORKSHOP_LOADER, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //define the string projection first
        String projection[] = {
                WorkshopContract.WorkshopEntry._ID,
                WorkshopContract.WorkshopEntry.WORKSHOP_NAME,
                WorkshopContract.WorkshopEntry.WORKSHOP_DETAILS
        };
        // This loader will execute the ContentProvider's query method on a background thread
        System.out.println("the old" + WorkshopContract.WorkshopEntry.WORKSHOP_CONTENT_URI);
        return new CursorLoader(context,//Context
                WorkshopContract.WorkshopEntry.WORKSHOP_CONTENT_URI,
                projection,
                null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //It will load the data of the adapter since we passed empty args into workshopadapter
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //resetting the recent cursor
        mAdapter.swapCursor(null);
    }

}
