package rxtest.com.rxtest.viewer;

import java.util.List;

import rx.schedulers.Timestamped;
import rxtest.com.rxtest.datamodels.GitRepo;
import rxtest.com.rxtest.datamodels.GitUser;

public interface MVP_Main {

    interface RequiredViewOps {
        void setRepos(List<GitRepo> items);

        void alertTimeDifference(Timestamped<GitUser> a, Timestamped<List<GitRepo>> b);

        void setUserView(GitUser gitUser);

        void onFetchError(Throwable throwable);
    }

    interface ProvidedPresenterOps {
        void fetchData(String username);
    }

    interface RequiredPresenterOps {
        void onSuccessfulGitFetch(Timestamped<GitUser> a, Timestamped<List<GitRepo>> b);

        void onUnSuccessfulGitFetch(Throwable throwable);
    }

    interface ProvidedModelOps {
        void fetchData(String username);
    }
}
