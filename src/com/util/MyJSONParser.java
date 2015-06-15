package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

@SuppressWarnings("deprecation")
public class MyJSONParser {

	private static InputStream inputstream=null;
	private static JSONObject jsonobject=null;
	private static String json="";
	public MyJSONParser()
	{}
	public JSONObject makeHttpRequest(String URL,String method,List<NameValuePair> params)
	{
		Log.i("http: ", "http check 1");	
try{
	if(method.equals("POST"))
	{
		
		Log.i("http: ", "http check 2");
		DefaultHttpClient httpClient =new DefaultHttpClient();
		HttpPost httpPost= new HttpPost(URL);
		Log.i("http: ", "http check 3");
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		Log.i("http: ", "http check 4");
		HttpResponse httpResponse=httpClient.execute(httpPost);
		Log.i("http: ", "http check 4.5");
		Log.i("http:",""+httpResponse.getStatusLine());
		HttpEntity httpEntity=httpResponse.getEntity();
		Log.i("http: ", "http check 5");
		inputstream=httpEntity.getContent();
		Log.i("http: ", "http check 6");
	}
	else
		if(method.equals("GET"))
		{
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
               // URL += "?" + paramString;
                HttpGet httpGet = new HttpGet(URL);
 
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputstream = httpEntity.getContent();
            }           
}
catch(Exception e)
{
	Log.i("Buffer Error", "Error converting result " + e.toString());	
}

try{
	BufferedReader reader = new BufferedReader(new InputStreamReader(
            inputstream, "iso-8859-1"), 8);
	StringBuilder sb= new StringBuilder();
	String line=null;
	while((line=reader.readLine())!=null)
	{
		System.out.println("====== "+line);
		sb.append(line+"\n");
	}
	inputstream.close();
	json=sb.toString();
}
catch (UnsupportedEncodingException e) {
	// TODO Auto-generated catch block
	Log.e("Buffer Error", "Error converting result " + e.toString());
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	Log.e("Buffer Error", "Error converting result " + e.toString());
	e.printStackTrace();
}
try
{
	jsonobject= new JSONObject(json);
}
catch(JSONException e)
{
	 Log.e("JSON Parser", "Error parsing data " + e.toString());
}
return jsonobject;
	}
}
