package com.moviefinder.Utilities;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestCallback {
    public interface APIRequestCallback {
        void onSuccessResponse(String response);
        void onSuccessJSONResponse(JSONObject response);
        void onSuccessJSONArrayResponse(JSONArray response);
        void onErrorResponse(VolleyError error);
        void statusCode(int statusCode);
    }

}

