package es.pulimento.wifi.ui.utils.github;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

public class GithubApi {

	// Constants...
	private static String BASEURL = "https://api.github.com/repos/pulWifi/pulWifi/";

	// Variables...
	private Boolean mAuthed;
	private String mToken;

	public GithubApi() {
		mAuthed = false;
	}

	public GithubApi(String token) {
		mAuthed = true;
		mToken = token;
	}

	public Download getLastDownload() {
		try {
			URL url = new URL(BASEURL+"downloads");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = in.readLine()) != null)
				sb.append(s);
			in.close();
			return new Download((new JSONArray(sb.toString())).getJSONObject(0));
		} catch (MalformedURLException e) {
			// Never going to happen...
		} catch (IOException e) {
			// No internet...
		} catch (JSONException e) {
			// Error in JSON...
		}
		
		return null;
	}

	/**
	 * TODO Still needing an authentication system.
	 * @param i Issue to post in Github.
	 */
	public void reportIssue(Issue i) {
		if(!mAuthed)
			return;

		HttpPost httpPost = new HttpPost(BASEURL+"issues");
		httpPost.setHeader("Authorization", "token "+mToken);
		httpPost.setHeader("Content-Type", "application/json");
		try {
			httpPost.setEntity(new StringEntity(i.toJSONString(), "UTF-8"));
			HttpResponse response = (new DefaultHttpClient()).execute(httpPost);
			//Log.v("ASD", EntityUtils.toString(response.getEntity()));
		} catch (UnsupportedEncodingException e) {
			// TODO Review
		} catch (ClientProtocolException e) {
			// TODO Review
		} catch (IOException e) {
			// TODO Review
		}
	}
}
