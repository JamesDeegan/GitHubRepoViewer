package rxtest.com.rxtest.viewer;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.schedulers.Timestamped;
import rxtest.com.rxtest.R;
import rxtest.com.rxtest.adapter.AdapterGithubRepo;
import rxtest.com.rxtest.collpasingview.CollapseViewOnScroll;
import rxtest.com.rxtest.datamodels.GitRepo;
import rxtest.com.rxtest.datamodels.GitUser;

public class ProfileActivity extends AppCompatActivity implements MVP_Main.RequiredViewOps {
    @BindView(R.id.userview_username)
    TextView usernameTV;
    @BindView(R.id.userview_profile_pic)
    ImageView profileIV;
    @BindView(R.id.userview_public_repo_count)
    TextView publicRepoCount;
    @BindView(R.id.userview)
    LinearLayout userview;

    private AdapterGithubRepo adapter = new AdapterGithubRepo();
    ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        String username = getIntent().getExtras().getString("username");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.public_repos));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerview_list);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        CollapseViewOnScroll cv = new CollapseViewOnScroll(userview, recyclerView);

        presenter = new ProfilePresenter(this);
        presenter.fetchData(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_git_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_az:
                adapter.sortAlphabetically(true);
                return true;
            case R.id.action_filter_za:
                adapter.sortAlphabetically(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void setRepos(List<GitRepo> items) {
        adapter.setGitHubRepos(items);
    }

    @Override
    public void alertTimeDifference(Timestamped<GitUser> a, Timestamped<List<GitRepo>> b) {
        StringBuilder sb = new StringBuilder();

        if (a.getTimestampMillis() > b.getTimestampMillis()) {
            sb.append("Repo thread finished before User thread by ");
            sb.append(a.getTimestampMillis() - b.getTimestampMillis());
            sb.append(" milliseconds.");
        } else {
            sb.append("User thread finished before Repo thread by ");
            sb.append(b.getTimestampMillis() - a.getTimestampMillis());
            sb.append(" milliseconds.");
        }

        //=======ALERT DIALOG

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        // Add the buttons
        builder.setMessage(sb.toString());
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        //=======END ALERT DIALOG
    }

    @Override
    public void setUserView(GitUser gitUser) {
        Glide.with(ProfileActivity.this).load(gitUser.avatarUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(profileIV) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                profileIV.setImageDrawable(circularBitmapDrawable);
            }
        });

        usernameTV.setText(gitUser.login);

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, gitUser.publicRepos);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                publicRepoCount.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    public void onFetchError(Throwable throwable) {
        Toast.makeText(ProfileActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

}
