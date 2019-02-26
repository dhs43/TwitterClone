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
    public Boolean isFavorited = false;
    public String videoLink;

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
        tweet.isFavorited = jsonObject.getBoolean("favorited");

        //get tweet media if not retweet
        JSONObject entities = jsonObject.getJSONObject("entities");
        if (entities.has("media")) {
            JSONArray mediaArray = entities.getJSONArray("media");
            JSONObject media = mediaArray.getJSONObject(0);
            tweet.mediaLink = media.getString("media_url_https");

            if (jsonObject.has("extended_entities")) {
                JSONObject extended_entities = jsonObject.getJSONObject("extended_entities");
                JSONArray videoMediaArray = extended_entities.getJSONArray("media");
                media = videoMediaArray.getJSONObject(0);
                if (media.has("video_info")) {
                    JSONObject video_info = media.getJSONObject("video_info");
                    JSONArray variantsArray = video_info.getJSONArray("variants");
                    JSONObject variants = variantsArray.getJSONObject(0);
                    tweet.videoLink = variants.getString("url");
                }
            }
        }


        JSONObject retweeted = null;
        if (jsonObject.has("retweeted_status")) {
            retweeted = jsonObject.getJSONObject("retweeted_status");
        }
        //get tweet media if retweet
        if (tweet.isRetweet){
            JSONObject retweet_entities = retweeted.getJSONObject("entities");
            if (retweet_entities.has("media")) {
                JSONArray mediaArray = retweet_entities.getJSONArray("media");
                JSONObject media = mediaArray.getJSONObject(0);
                tweet.mediaLink = media.getString("media_url_https");

                JSONObject retweetedInfo = jsonObject.getJSONObject("retweeted_status");
                if (retweetedInfo.has("extended_entities")) {
                    JSONObject extended_entities = retweetedInfo.getJSONObject("extended_entities");
                    JSONArray videoMediaArray = extended_entities.getJSONArray("media");
                    media = videoMediaArray.getJSONObject(0);
                    if (media.has("video_info")) {
                        JSONObject video_info = media.getJSONObject("video_info");
                        JSONArray variantsArray = video_info.getJSONArray("variants");
                        JSONObject variants = variantsArray.getJSONObject(0);
                        tweet.videoLink = variants.getString("url");
                    }
                }
            }

            //get info of original poster
            tweet.original_poster = User.fromJson(retweeted.getJSONObject("user"));
        }
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
