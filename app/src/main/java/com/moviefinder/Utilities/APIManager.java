package com.moviefinder.Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class APIManager {
    public static RequestCallback.APIRequestCallback onAPICallbackListener;

    public static void setOnAPICallbackListener(RequestCallback.APIRequestCallback onAPICallbackListener) {
        APIManager.onAPICallbackListener = onAPICallbackListener;
    }

////////////////////////////////////////

    /**
     * This method is used to fire JSON Object request.
     *
     * @param context     - Activity reference
     * @param Url         - url of request
     * @param requestType - type of request
     * @param jsonObject  - header parameters
     */
    public static void jsonObjectVolleyRequest(Context context, String Url, String requestType, JSONObject jsonObject) {

        try {
            int reqType = 0;
            if (requestType.trim().equalsIgnoreCase("GET")) {
                reqType = Request.Method.GET;
            } else if (requestType.trim().equalsIgnoreCase("POST"))
                reqType = Request.Method.POST;

            JsonObjectRequest request = new JsonObjectRequest(reqType, Url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        if (onAPICallbackListener != null)
                            onAPICallbackListener.onSuccessJSONResponse(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    onAPICallbackListener.onErrorResponse(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    return params;
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                        volleyError = new VolleyError(new String(volleyError.networkResponse.data));
                    }
                    return volleyError;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

////////////////////////////////////////

    /**
     * This method is used to add the new json array request to queue. (MethodType: GET)
     *
     * @param context     - Activity reference
     * @param Url         - url of request
     */
    public static void jsonArrayVolleyRequest(Context context, String Url) {
        try {

            JsonArrayRequest request = new JsonArrayRequest(Url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (onAPICallbackListener != null)
                        onAPICallbackListener.onSuccessJSONArrayResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    onAPICallbackListener.onErrorResponse(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    return params;
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                        volleyError = new VolleyError(new String(volleyError.networkResponse.data));
                    }
                    return volleyError;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to handle the error response of network request and show appropriate message.
     *
     * @param context      - Current context
     * @param error        - Volley error in response
     * @param errorMessage - default error message
     */
    public static void handleErrorResponseOfRequest(Context context, VolleyError error, String errorMessage, String logTitle) {
        if (context != null && error != null && errorMessage != null && errorMessage.length() > 0 && logTitle != null) {
            try {
                if (error.networkResponse != null) {
                    if (error.networkResponse.data != null) {
                        String responseString = new String(error.networkResponse.data);
                        JSONObject responseJson = new JSONObject(responseString);
                        if (responseJson.optJSONObject("error") != null) {
                            JSONObject errorsJSON = responseJson.getJSONObject("error");
                            if (errorsJSON != null) {
                                showToastMessage(context, errorsJSON.optString("message", ""));
                            } else {
                                showToastMessage(context, errorMessage);
                            }
                        } else {
                            showToastMessage(context, errorMessage);
                        }
                    } else {
                        // Show appropriate error message
                        if (error.getMessage() != null) {
                            showToastMessage(context, error.getMessage());
                        } else {
                            showToastMessage(context, errorMessage);
                        }
                    }
                } else {
                    // Error Network response is null so show appropriate error message
                    if (error.getMessage() != null) {
                        showToastMessage(context, error.getMessage());
                    } else {
                        showToastMessage(context, errorMessage);
                    }
                }
            } catch (JSONException e) {
                Log.e(logTitle, " Exception");
                e.printStackTrace();
                // Show appropriate error message
                if (error.getMessage() != null) {
                    showToastMessage(context, error.getMessage());
                } else {
                    showToastMessage(context, errorMessage);
                }
            } catch (Exception e) {
                Log.e(logTitle, " Exception");
                e.printStackTrace();
                // Show appropriate error message
                showToastMessage(context, errorMessage);
            }
        }
    }

    private static void showToastMessage(Context context, String errorMsg) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

}