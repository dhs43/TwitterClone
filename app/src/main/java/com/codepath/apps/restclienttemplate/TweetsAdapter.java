package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> tweets;

    //pass in context and list of tweets

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //for each row shown, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, viewGroup, false);
        return new ViewHolder(view);
    }

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //clean items from list
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }
    //add a list of items
    public void addTweets(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    //define the ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvRetweeted;
        public ImageView ivProfileImage;
        public TextView tvName;
        public TextView tvScreenName;
        public TextView tvCreatedAt;
        public TextView tvBody;
        public ImageView ivMedia;
        public ImageView ivRetweeted;
        public ConstraintLayout retweetContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRetweeted = itemView.findViewById(R.id.tvRetweeted);
            ivRetweeted = itemView.findViewById(R.id.ivMedia);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            retweetContainer = itemView.findViewById(R.id.retweetContainer);
            retweetContainer.setVisibility(View.GONE);
        }

        public void bind(final Tweet tweet){
            if (tweet.isRetweet) {
                tvRetweeted.setText(tweet.getUser().getScreenName() + " Retweeted");
                retweetContainer.setVisibility(View.VISIBLE);

                //show original poster info
                tvName.setText(tweet.original_poster.getName());
                tvScreenName.setText("@" + tweet.original_poster.getScreenName());
                Log.d("screnName ", tweet.original_poster.getScreenName());
                GlideApp.with(context)
                        .load(tweet.original_poster.profileImageUrl)
                        .transform(new RoundedCorners(100))
                        .error(R.drawable.error)
                        .into(ivProfileImage);
            }else{
                retweetContainer.setVisibility(View.GONE);
            }

            tvBody.setText(tweet.getBody());
            if (tweet.isRetweet == false) {
                tvName.setText(tweet.user.getName());
                tvScreenName.setText("@" + tweet.user.getScreenName());
                GlideApp.with(context)
                        .load(tweet.user.profileImageUrl)
                        .transform(new RoundedCorners(100))
                        .error(R.drawable.error)
                        .into(ivProfileImage);
            }
            tvCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));
            if (tweet.getMediaLink() != null) {
                GlideApp.with(context)
                        .load(tweet.getMediaLink())
                        .centerCrop()
                        .fitCenter()
                        .transform(new RoundedCorners(30))
                        .error(R.drawable.error)
                        .into(ivMedia);
                ivMedia.setVisibility(View.VISIBLE);
            }else{
                GlideApp.with(context).clear(ivMedia);
                ivMedia.setVisibility(View.GONE);
            }
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
