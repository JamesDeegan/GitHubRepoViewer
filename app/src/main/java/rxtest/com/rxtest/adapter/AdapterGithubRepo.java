package rxtest.com.rxtest.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rxtest.com.rxtest.R;
import rxtest.com.rxtest.datamodels.GitRepo;

public class AdapterGithubRepo extends RecyclerView.Adapter<AdapterGithubRepo.RepoViewHolder> {
    private List<GitRepo> mRepos = new ArrayList<>();

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repo_card, viewGroup, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder repoViewHolder, int i) {
        repoViewHolder.bindRepo(i, mRepos.get(i));
    }

    @Override
    public int getItemCount() {
        return mRepos.size();
    }

    public void setGitHubRepos(@Nullable List<GitRepo> repos) {
        if (repos == null) {
            return;
        }
        mRepos.clear();
        mRepos.addAll(repos);
        notifyDataSetChanged();
    }

    public void sortAlphabetically(boolean sortAlphabetically) {
        Collections.sort(mRepos, new Comparator<GitRepo>() {
            @Override
            public int compare(GitRepo item1, GitRepo item2) {
                if (sortAlphabetically) {
                    return item1.getName().toLowerCase().compareTo(item2.getName().toLowerCase());
                } else {
                    return item2.getName().toLowerCase().compareTo(item1.getName().toLowerCase());
                }
            }
        });
        notifyDataSetChanged();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.repo_card_index)
        TextView tv_index;
        @BindView(R.id.repo_card_name)
        TextView tv_name;
        @BindView(R.id.repo_card_create_at)
        TextView tv_created_at;

        public RepoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindRepo(int index, GitRepo repo) {
            tv_index.setText(String.valueOf(index + 1));
            tv_name.setText(repo.getName());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                Date d = sdf.parse(repo.getCreatedAt());
                String formattedTime = output.format(d);
                tv_created_at.setText(formattedTime);
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }
    }

}