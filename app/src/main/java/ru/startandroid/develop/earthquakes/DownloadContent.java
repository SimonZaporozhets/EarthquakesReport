package ru.startandroid.develop.earthquakes;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class DownloadContent extends AsyncTask<String, Void, ArrayList<Earthquake>> {


    @Override
    protected ArrayList<Earthquake> doInBackground(String... urls) {
        // Create URL object
        URL url = createUrl(urls[0]);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Earthquake> earthquakes = extractFeaturesFromJson(jsonResponse);

        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
        return earthquakes;
    }


    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("URL error", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("http error", "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private ArrayList<Earthquake> extractFeaturesFromJson(String earthquakeJSON) {

        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {

            JSONObject object = new JSONObject(earthquakeJSON);

            JSONArray jsonArray = object.getJSONArray("features");

            // If there are results in the features array
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject feature = jsonArray.getJSONObject(i);

                    JSONObject properties = feature.getJSONObject("properties");

                    long dateLong = properties.getLong("time");

                    double mag = properties.getDouble("mag");
                    String place = properties.getString("place");

                    String url = properties.getString("url");

                    if (place.contains("of")) {
                        String[] splitPlace = place.split(" of ");
                        earthquakes.add(new Earthquake(mag, splitPlace[0] + " of ", splitPlace[1], dateLong, url));
                    } else {
                        earthquakes.add(new Earthquake(mag, "Near the", place, dateLong, url));
                    }
                }

                return earthquakes;

            }
        } catch (JSONException e) {
            Log.e("JSON error", "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }

}