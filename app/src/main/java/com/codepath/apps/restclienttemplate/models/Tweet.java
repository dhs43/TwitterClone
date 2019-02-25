package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {
    public String id;
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public String mediaLink;
    public boolean isRetweet;
    public User original_poster = null;
    public Boolean retweetedByMe = false;
    //public String videoLink;

    public Tweet() {//empty constructor for Parceler
    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();


        if (jsonObject.has("retweeted_status")) {
            tweet.isRetweet = true;
        }else{
            tweet.isRetweet = false;
        }
        tweet.id = jsonObject.getString("id");
        tweet.body = jsonObject.getString("full_text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.retweetedByMe = jsonObject.getBoolean("retweeted");

        //get tweet media if not retweet
        JSONObject entities = jsonObject.getJSONObject("entities");
        if (entities.has("media")) {
            JSONArray mediaArray = entities.getJSONArray("media");
            JSONObject media = mediaArray.getJSONObject(0);
            tweet.mediaLink = media.getString("media_url_https");
        }

        //get tweet media if retweet
        JSONObject retweeted = null;
        if (jsonObject.has("retweeted_status")) {
            retweeted = jsonObject.getJSONObject("retweeted_status");
        }
        if (tweet.isRetweet){
            JSONObject retweet_entities = retweeted.getJSONObject("entities");
            if (retweet_entities.has("media")) {
                JSONArray mediaArray = retweet_entities.getJSONArray("media");
                JSONObject media = mediaArray.getJSONObject(0);
                tweet.mediaLink = media.getString("media_url_https");
            }

            //get info of original poster
            tweet.original_poster = User.fromJson(retweeted.getJSONObject("user"));
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

    public String getId() {
        return id;
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

    public boolean isRetweet() {
        return isRetweet;
    }
}
