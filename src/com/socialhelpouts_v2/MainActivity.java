package com.socialhelpouts_v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.util.MyJSONParser;

public class MainActivity extends ListActivity {
private ProgressDialog dialog;
MyJSONParser jParser= new MyJSONParser();
//ArrayList<HashMap<String, ArrayList<String>>> lst;
ArrayList<HashMap<String,String>> lst;
private static String url_all_jobs ="http://devtest.socialhelpouts.com/jobsJson/q?page=1";
private static final String TAG_JOBS = "jobs";
private static final String TAG_ID = "id";
private static final String TAG_COMPANY = "company";
private static final String TAG_NAME = "name";
private static final String TAG_COMMENT = "comment";
JSONArray jobsarray=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_jobs);
		
		lst= new ArrayList<>();
		jobsarray= new JSONArray();
		new LoadAllJobs().execute();
		//ListView lv = getListView();
		//lv.setAdapter(getListAdapter());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	class LoadAllJobs extends AsyncTask<String, String, String>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog=new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loading Jobs. Please wait...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> args = new ArrayList<NameValuePair>();
            // getting JSON string from URL
		    Log.i("All Jobs: ", "check 0");
            JSONObject json = jParser.makeHttpRequest(url_all_jobs, "GET", args);
 
            // Check your log cat for JSON reponse
            Log.i("All Jobs: ", "check 1");
 
            try {
                // Checking for SUCCESS TAG
                    // Jobs found
                    // Getting Array of Jobs
                    jobsarray = json.getJSONArray(TAG_JOBS);
                    Log.i("All Jobs: ", "check 2--->" +jobsarray.length());
                    ArrayList<String> jobslist=new ArrayList<>();
                    // looping through All Jobs
                    for (int i = 0; i < jobsarray.length(); i++) {
                        JSONObject c = jobsarray.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String company=c.getString(TAG_COMPANY);
                        String comment=c.getString(TAG_COMMENT);
                        Log.i("All Jobs: ", "check 3");
                       // jobslist.add(id);
//                        jobslist.add(company);
//                        jobslist.add(name);
//                        jobslist.add(comment);
                        // creating new HashMap
//                        HashMap<String, ArrayList<String>> map = new HashMap<>();
                        HashMap<String, String> map = new HashMap<>();
                        
                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_COMPANY, company);
                        map.put(TAG_NAME, name);
                        map.put(TAG_COMMENT, comment);
                        Log.i("All Jobs: ", "check 4");
                        
                        // adding HashList to ArrayList
                        lst.add(map);
                } 
                    // no Jobs found
                    // Launch Add New product Activity
                   
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("All Jobs: ", "errooror");
                
            }
			return null;
		}
		@Override
		protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all Jobs
			dialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                	 Log.i("post: ", "cpost check 1");
                    ListAdapter adapter = new SimpleAdapter(
                            MainActivity.this, lst,
                            R.layout.fragment_main, new String[] {TAG_ID,
                                   TAG_COMPANY, TAG_NAME,TAG_COMMENT},
                            new int[] { R.id.job_id, R.id.company_name,R.id.name,R.id.comment });
                    // updating listview

               	 Log.i("post: ", "cpost check 2");
                    setListAdapter(adapter);

               	 Log.i("post: ", "cpost check 3");
                }
            });

        }
		
	}
}
