package snooze.ninja;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
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

    abstract public void onResponse(HttpResponse httpResponse);


    private class HttpAsyncTask extends android.os.AsyncTask {
        private HttpPost httpPost;
        private HttpResponse response;

        private HttpAsyncTask(HttpPost httpPost) {
            this.httpPost = httpPost;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                response = new DefaultHttpClient().execute(httpPost);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.d("Post JSON response", response.getStatusLine().getReasonPhrase());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            onResponse(response);
        }
    }

}
