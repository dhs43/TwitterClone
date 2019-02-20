package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public String mediaLink;
    public static Boolean isRetweet = false;
    //public String videoLink;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();

        if (jsonObject.has("retweeted_status")) {
            jsonObject = jsonObject.getJSONObject("retweeted_status");
            isRetweet = true;
        }else{
            isRetweet = false;
        }

        tweet.body = jsonObject.getString("full_text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        //get tweet media
        JSONObject entities = jsonObject.getJSONObject("entities");
        if (entities.has("media")) {
            JSONArray mediaArray = entities.getJSONArray("media");
            JSONObject media = mediaArray.getJSONObject(0);
            tweet.mediaLink = media.getString("media_url_https");
        }
            /*
            if (media.has("video_info")) {
                JSONObject video_info = media.getJSONObject("video_info");
                JSONObject variants = video_info.getJSONObject("variants");
                tweet.videoLink = variants.getString("url");
            }
            */

        return tweet;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getMediaLink() {
        return mediaLink;
    }
}
