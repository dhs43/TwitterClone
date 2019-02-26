package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 *
 * This is the object responsible for communicating with a REST API.
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes:
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 *
 */
public class TwitterClient extends OAuthBaseClient {
    public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "vsBPRuLfWm2r6snd2modFuOSa";       // Change this
    public static final String REST_CONSUMER_SECRET = "qqN2gVXPJNCGcE3a2383mc9q2yAcy1IDpDGlOC4yxdubt5l8hm"; // Change this

    // Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    // See https://developer.chrome.com/multidevice/android/intents
    public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

    public TwitterClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }

    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", "1");
        params.put("tweet_mode", "extended");
        client.get(apiUrl, params, handler);
    }

    public void getOlderTweets(Long lastTweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("max_id", lastTweetId);
        params.put("tweet_mode", "extended");
        client.get(apiUrl, params, handler);
    }

    public void composeTweet(String tweetContent, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("status", tweetContent);
        params.put("tweet_mode", "extended");
        client.post(apiUrl, params, handler);
    }

    public void retweetTweet(Tweet tweet, JsonHttpResponseHandler handler) {
        if (tweet.retweetedByMe) {
            String initialUrl = "statuses/unretweet/%s.json";
            String apiUrl = getApiUrl(String.format(initialUrl, tweet.getId()));
            client.post(apiUrl, handler);
            tweet.retweetedByMe = false;
        } else {
            String initialUrl = "statuses/retweet/%s.json";
            String apiUrl = getApiUrl(String.format(initialUrl, tweet.getId()));
            client.post(apiUrl, handler);
            tweet.retweetedByMe = true;
        }
    }

    public void favoriteTweet(Tweet tweet, JsonHttpResponseHandler handler) {
        if (tweet.isFavorited) {
            String apiUrl = getApiUrl("favorites/destroy.json");
            RequestParams params = new RequestParams();
            params.put("id", tweet.getId());
            client.post(apiUrl, params, handler);
            tweet.isFavorited = false;
        } else {
            String apiUrl = getApiUrl("favorites/create.json");
            RequestParams params = new RequestParams();
            params.put("id", tweet.getId());
            client.post(apiUrl, params, handler);
            tweet.isFavorited = true;
        }
    }
}
