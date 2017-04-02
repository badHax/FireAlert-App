package com.example.keniel.test;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class ForumFragment extends Fragment implements AddDialog.EditNameDialogListener {

    private static final String URL_Data = "http://138.197.79.246/forums";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ForumItems> fitems;
    FloatingActionButton fab;
    View rootview;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       rootview = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fitems = new ArrayList<>();

        loadRecyclerViewData();
        fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog dialog = new AddDialog();
                dialog.setTargetFragment(ForumFragment.this, 0);
                dialog.show(getFragmentManager(),"fragment_add");

            }
        });
        return rootview;

    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_Data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i=0;i< array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                ForumItems fitem = new ForumItems(
                                        o.getString("title"),
                                        o.getString("description"),
                                        o.getString("fire_type"),
                                        o.getString("created_at"),
                                        o.getDouble("lat"),
                                        o.getDouble("long")
                                );
                                fitems.add(fitem);
                            }

                            adapter = new RecyclerAdapter(fitems,getActivity());
                            recyclerView.setAdapter(adapter);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                }}, 3000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQ = Volley.newRequestQueue(getActivity());
        requestQ.add(stringRequest);
    }


    @Override
    public void onFinishEditDialog(String t, String m, String s, Double lat, Double lng) {
        Map<String, String> params = new HashMap();
        params.put("title", t);
        params.put("description", m);
        params.put("fire_type", s);
        params.put("lat",lat.toString());
        params.put("long",lng.toString());
        JSONObject object = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_Data, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rqueue = Volley.newRequestQueue(getActivity());
        rqueue.add(jsonObjectRequest);
    }
}

