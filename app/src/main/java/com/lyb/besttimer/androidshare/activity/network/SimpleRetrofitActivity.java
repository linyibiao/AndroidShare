package com.lyb.besttimer.androidshare.activity.network;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lyb.besttimer.androidshare.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimpleRetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_retrofit);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneRXRetrofit();
            }
        });
    }

    private void oneRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create()).build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<List<Repo>> repos = service.listRepos("octocat");
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                Log.e("what", response.body().size() + ";;;");
                for (Repo repo : response.body()) {
                    Log.e("what", repo.getId() + ";");
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

            }
        });
    }

    private void oneRXRetrofit() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(60000, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(60000, TimeUnit.MILLISECONDS);

        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();
                okhttp3.Request.Builder builder = request.newBuilder();

                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.header("Host", request.url().host());

                return chain.proceed(builder.build());
            }
        });

        okHttpClientBuilder.cookieJar(new CookieJar() {

            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        Observable<Response<List<Repo>>> repos = service.listReposByRX("octocat");
        repos.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<List<Repo>>>() {
                    @Override
                    public void accept(Response<List<Repo>> listResponse) throws Exception {
                        Log.e("what", listResponse.headers().toString() + ";;;");
                        Log.e("what", listResponse.body().size() + ";;;");
                        for (Repo repo : listResponse.body()) {
                            Log.e("what", repo.getId() + ";");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

}
