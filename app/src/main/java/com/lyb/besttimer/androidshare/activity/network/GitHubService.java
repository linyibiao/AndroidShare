package com.lyb.besttimer.androidshare.activity.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/19.
 */
public interface GitHubService {
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);
    @GET("users/{user}/repos")
    Observable<List<Repo>> listReposByRX(@Path("user") String user);
}
