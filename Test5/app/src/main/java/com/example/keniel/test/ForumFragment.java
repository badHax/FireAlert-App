package com.example.keniel.test;


import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment implements AddDialog.EditNameDialogListener{

    private List<ForumItems> items = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver3;


    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    private Double x,y;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       rootview = inflater.inflate(R.layout.fragment_forum, container, false);

        items.add(new ForumItems("g","g","ii","f",-45.2,034.0));

        engine();
        addClicker();
        if (broadcastReceiver3 == null){
            broadcastReceiver3 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle bundle = intent.getExtras();
                    setX(bundle.getDouble("lat"));
                    setY(bundle.getDouble("lng"));
                }
            };
        }

        return rootview;

    }
    private void engine(){
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        Log.i("ffdfd","gfgththhh");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"http://138.197.79.246/forums",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("forumitems");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject temp = jsonArray.getJSONObject(i);
                                Log.i("here","heerer");
                                String title = temp.getString("title");
                                String content = temp.getString("description");
                                String date = temp.getString("created_at");
                                String lat = temp.getString("lat");
                                String lng = temp.getString("long");
                                String type = temp.getString("type");
                                items.add(new ForumItems(title,content,type,date,Double.parseDouble(lat),Double.parseDouble(lng)));
                                System.out.println(items);
                                Log.i("g",items.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error",error.toString());
            }

        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        rQueue.add(request);


        ArrayAdapter<ForumItems> adapter = new customAdapter();

        ListView listview = (ListView) rootview.findViewById(R.id.list_view);

        listview.setAdapter(adapter);



    }


    @Override
    public void onFinishEditDialog(final String t, final String m, final String s) {

        String url = "http://localhost:8081/test";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", t);
                params.put("message", m);
                params.put("fire_type", s);
                params.put("latitude",getX().toString());
                params.put("longitude",getY().toString());
                params.put("date","23/11/1995");
                return  params;
            }
        };
    }

    private void addClicker(){
        ListView listview = (ListView) rootview.findViewById(R.id.list_view);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumItems forumitem = items.get(position);
                Bundle data = new Bundle();
                data.putString("title",forumitem.getTitle());
                data.putDouble("latitude", forumitem.getLat());
                data.putDouble("longitude",forumitem.getLng());
                FireLocateFragment fragment = new FireLocateFragment();
                fragment.setArguments(data);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.forumfragment, fragment).commit();


            }
        });
    }

    public void addMethod(View v){
        AddDialog dialog = new AddDialog();
        dialog.setTargetFragment(ForumFragment.this,300);
        dialog.show(getFragmentManager(), "addDialog");
    }




    private class customAdapter extends ArrayAdapter<ForumItems>{

        public customAdapter() {
            super(getActivity(), R.layout.item, items);
        }

        @Override
        public View getView(int position, View convertview , @NonNull ViewGroup parent){

            if (convertview == null){
                Log.i("error","errrrroor");
                convertview= getActivity().getLayoutInflater().inflate(R.layout.item,parent,false);

            }

            ForumItems forumitem = items.get(position);


            TextView textView = (TextView) convertview.findViewById(R.id.content);

            textView.setText(forumitem.getContent());

            return convertview;
        }
    }

}

