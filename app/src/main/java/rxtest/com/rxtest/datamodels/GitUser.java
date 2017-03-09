package rxtest.com.rxtest.datamodels;

public class GitUser {
    public String login;
    public int publicRepos;
    public String avatarUrl;

    public GitUser(String login, Integer publicRepos, String avatarUrl) {
        this.login = login;
        this.publicRepos = publicRepos;
        this.avatarUrl = avatarUrl;
    }
}
