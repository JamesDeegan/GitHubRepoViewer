package rxtest.com.rxtest.retrofit;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rxtest.com.rxtest.datamodels.GitRepo;
import rxtest.com.rxtest.datamodels.GitUser;

public interface GithubService {
    @GET("users/{user}")
    Observable<GitUser> getUser(@Path("user") String userName);

    @GET("users/{user}/repos")
    Observable<List<GitRepo>> getRepositories(@Path("user") String userName);
}
