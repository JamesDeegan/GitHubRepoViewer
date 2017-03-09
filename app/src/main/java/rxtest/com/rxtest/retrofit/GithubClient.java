package rxtest.com.rxtest.retrofit;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rxtest.com.rxtest.datamodels.GitRepo;
import rxtest.com.rxtest.datamodels.GitUser;

public class GithubClient {
    public static final String BASE_URL = "https://api.github.com";

    private static GithubClient instance;
    private GithubService gitHubService;

    public GithubClient() {
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gitHubService = retrofit.create(GithubService.class);
    }

    public static GithubClient getInstance() {
        if (instance == null) {
            instance = new GithubClient();
        }
        return instance;
    }

    public Observable<GitUser> getUser(@NonNull String userName) {
        //return gitHubService.getUser(userName);

        return Observable.just(new GitUser("James", 666, "https://content-static.upwork.com/uploads/2014/10/02123010/profile-photo_friendly.jpg"));
    }

    public Observable<List<GitRepo>> getRepositories(@NonNull String userName) {
        //return gitHubService.getRepositories(userName);

        List<GitRepo> products = new ArrayList<GitRepo>();
        products.add(new GitRepo("abra", "2015"));
        products.add(new GitRepo("bat", "2015"));
        products.add(new GitRepo("cat", "2015"));
        products.add(new GitRepo("dear", "2015"));
        products.add(new GitRepo("pidgeon", "2015"));
        products.add(new GitRepo("random LIB", "2015"));
        products.add(new GitRepo("facebook", "2015"));
        return Observable.just(products);
    }
}
