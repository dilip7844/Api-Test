package com.example.myapplication

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ApiClient {
    companion object{
        val BASE_URL="https://jsonplaceholder.typicode.com"
          var requestQueue:RequestQueue?=null
    }

    fun getRequestQueue(context: Context): RequestQueue {
        if(requestQueue==null)
            requestQueue=Volley.newRequestQueue(context)
        return requestQueue!!;
    }

    public fun makeCall(context: Context,url:String,listener: OnApiResponse?){
        val stringRequest = StringRequest(Request.Method.GET,url,object : Response.Listener<String>{
            override fun onResponse(response: String?) {
                response?.let {
                    listener?.onResponse(true,response)
                    return
                 }
                listener?.onResponse(false,"")

            }

        },Response.ErrorListener {
            Log.d("TAG", "makeCall: "+it.message)
            listener?.onResponse(false,"")
        })
        getRequestQueue(context).add(stringRequest)
    }

    interface OnApiResponse{
        fun onResponse(isSuccessfull : Boolean ,root :String)

    }
}