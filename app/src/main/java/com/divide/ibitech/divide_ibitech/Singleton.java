package com.divide.ibitech.divide_ibitech;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {
    private static Singleton mInstance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private Singleton(Context context){
        ctx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Singleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new Singleton(context);
        }
        return mInstance;
    }

    public <T>void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }
}
