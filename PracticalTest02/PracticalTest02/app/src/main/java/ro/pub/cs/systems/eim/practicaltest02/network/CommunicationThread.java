package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class CommunicationThread extends Thread {

    private Socket socket;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Log.i(Constants.TAG, "Comm Thread run start");

            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);

            String city = br.readLine();
            String information = br.readLine();
            String httpMethod = br.readLine();

            Log.i(Constants.TAG, city);
            Log.i(Constants.TAG, information);
            Log.i(Constants.TAG, httpMethod);

            HttpClient httpClient = new DefaultHttpClient();
            String result = "";
            String pageSourceCode = "";

            if (httpMethod.equals(Constants.GET_METHOD)) {
                Log.i(Constants.TAG, "Page source code: " + pageSourceCode);
                HttpGet httpGet = new HttpGet(Constants.SERVER_HOST
                        + "?" + Constants.QUERY_ATTRIBUTE + "=" + city);
                Log.i(Constants.TAG, Constants.SERVER_HOST
                        + "?" + Constants.QUERY_ATTRIBUTE + "=" + city);
                pageSourceCode = httpClient.execute(httpGet, new BasicResponseHandler());
                Log.i(Constants.TAG, "Page source code: " + pageSourceCode);
            } else if (httpMethod.equals(Constants.POST_METHOD)) {
                Log.i(Constants.TAG, "Page source code: " + pageSourceCode);
                HttpPost httpPost = new HttpPost(Constants.SERVER_HOST);
                List<NameValuePair> httpParams = new ArrayList<>();
                httpParams.add(new BasicNameValuePair(Constants.QUERY_ATTRIBUTE, city));
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(httpParams, HTTP.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                pageSourceCode = httpClient.execute(httpPost, responseHandler);
                Log.i(Constants.TAG, "Page source code: " + pageSourceCode);
            } else {
                Log.e(Constants.TAG, "Http method: " + httpMethod);
            }

            Document document = Jsoup.parse(pageSourceCode);
            Element element = document.child(0);
            Elements elements = element.getElementsByTag(Constants.SCRIPT_TAG);
            for (Element script: elements) {
                String scriptData = script.data();
                if (scriptData.contains(Constants.SEARCH_KEY)) {
                    int position = scriptData.indexOf(Constants.SEARCH_KEY) + Constants.SEARCH_KEY.length();
                    scriptData = scriptData.substring(position);
                    JSONObject content = new JSONObject(scriptData);
                    JSONObject currentObservation = content.getJSONObject(Constants.CURRENT_OBSERVATION);
                    String temperature = currentObservation.getString(Constants.TEMPERATURE);
                    String windSpeed = currentObservation.getString(Constants.WIND_SPEED);
                    String condition = currentObservation.getString(Constants.CONDITION);
                    String pressure = currentObservation.getString(Constants.PRESSURE);
                    String humidity = currentObservation.getString(Constants.HUMIDITY);
                    Log.i(Constants.TAG, humidity);
                    //serverThread.setData(city, weatherForecastInformation);
                    break;
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}