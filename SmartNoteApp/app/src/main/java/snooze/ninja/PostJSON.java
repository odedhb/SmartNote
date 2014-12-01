package snooze.ninja;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by oded on 11/9/14.
 */
abstract public class PostJSON {

    private String postUrl;
    private JSONObject jsonObject;

    PostJSON(String postUrl, JSONObject jsonObject) throws UnsupportedEncodingException {
        this.postUrl = postUrl;
        this.jsonObject = jsonObject;

        run();
    }

    private void run() throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(postUrl);

        String stringJson = jsonObject.toString();
        StringEntity se = new StringEntity(stringJson);
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPost.setEntity(se);

        new HttpAsyncTask(httpPost).execute();
    }

    abstract public void onResponse(HttpResponse httpResponse, JSONObject jsonResponse);


    private class HttpAsyncTask extends android.os.AsyncTask {
        private HttpPost httpPost;
        private HttpResponse response;
        private JSONObject jsonResponse;

        private HttpAsyncTask(HttpPost httpPost) {
            this.httpPost = httpPost;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                response = new DefaultHttpClient().execute(httpPost);

                Log.d("Post JSON response", response.getStatusLine().getReasonPhrase());

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }
                JSONTokener tokener = new JSONTokener(builder.toString());
                jsonResponse = new JSONObject(tokener);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            onResponse(response, jsonResponse);
        }
    }

}
