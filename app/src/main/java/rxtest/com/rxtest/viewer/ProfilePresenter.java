package rxtest.com.rxtest.viewer;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.schedulers.Timestamped;
import rxtest.com.rxtest.datamodels.GitRepo;
import rxtest.com.rxtest.datamodels.GitUser;

public class ProfilePresenter implements MVP_Main.ProvidedPresenterOps, MVP_Main.RequiredPresenterOps{
    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Main.RequiredViewOps> mView;
    private ProfileModel mModel;

    public ProfilePresenter(MVP_Main.RequiredViewOps view) {
        mView = new WeakReference<>(view);
        mModel = new ProfileModel(this);
    }

    private MVP_Main.RequiredViewOps getView() throws NullPointerException {
        if (mView != null)
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    @Override
    public void fetchData(String username) {
        mModel.fetchData(username);
    }

    @Override
    public void onSuccessfulGitFetch(Timestamped<GitUser> a, Timestamped<List<GitRepo>> b) {
        GitUser gitUser = a.getValue();
        List<GitRepo> gitRepoList = b.getValue();
        getView().alertTimeDifference(a, b);
        getView().setUserView(gitUser);
        getView().setRepos(gitRepoList);
    }

    @Override
    public void onUnSuccessfulGitFetch(Throwable throwable){
        getView().onFetchError(throwable);
    }
}
