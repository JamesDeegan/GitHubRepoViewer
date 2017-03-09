package rxtest.com.rxtest.viewer;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxtest.com.rxtest.datamodels.GitRepo;
import rxtest.com.rxtest.datamodels.GitUser;
import rxtest.com.rxtest.retrofit.GithubClient;

public class ProfileModel implements MVP_Main.ProvidedModelOps {
    private MVP_Main.RequiredPresenterOps mPresenter;

    public ProfileModel(MVP_Main.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
    }

    public void fetchData(String username) {
        Observable<GitUser> observableUser = GithubClient.getInstance().getUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<List<GitRepo>> observableRepos = GithubClient.getInstance().getRepositories(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable.zip(observableUser.timestamp(), observableRepos.timestamp(), (a, b) -> {
            mPresenter.onSuccessfulGitFetch(a, b);
            return null;
        })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mPresenter.onUnSuccessfulGitFetch(throwable);
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
    }
}
