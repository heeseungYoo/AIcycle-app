package com.example.project3_test1.PersonFragments;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonalInfoRequest extends StringRequest {

    final static private String URL = "http://192.249.19.252:2080/personalInfo";
    private Map<String, String> parameters;

    public PersonalInfoRequest(String userID, Response.Listener<String> listener) {
        super(Method.GET, URL+"/"+userID, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    public PersonalInfoRequest(String userID, String point, Response.Listener<String> listener) {
        super(Method.PUT, URL +"/"+userID, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("points", point);
        System.out.println(parameters);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
/*
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        System.out.println(headers);
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

 */
}
