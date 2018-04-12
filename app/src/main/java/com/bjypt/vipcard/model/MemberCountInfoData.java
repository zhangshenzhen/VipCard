package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/11/25.
 */

public class MemberCountInfoData {
    private int follower;
    private int threads;
    private int following;
    private int favorites;
    private int isFollow;

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    private UserResponseBean userResponse;

    public UserResponseBean getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponseBean userResponse) {
        this.userResponse = userResponse;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
