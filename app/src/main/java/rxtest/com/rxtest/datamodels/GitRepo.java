package rxtest.com.rxtest.datamodels;

public class GitRepo {
    private String name;
    private String createdAt;

    public GitRepo (String name, String createdAt){
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
