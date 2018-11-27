package com.community.jboss.leadmanagement.main.about;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.community.jboss.leadmanagement.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.community.jboss.leadmanagement.SettingsActivity.PREF_DARK_THEME;

public class ContributorsFragment extends Fragment {

    @BindView(R.id.rv_contributors)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_contributors)
    RelativeLayout relativeLayout;

    private ContributorsAdapter mAdapter;
    private List<Contributor> mContributors;
    private ProgressBar mProgressBar;

    public ContributorsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        View view = inflater.inflate(R.layout.fragment_contributors, container, false);
        ButterKnife.bind(this, view);

        if (useDarkTheme)
            relativeLayout.setBackgroundColor(Color.parseColor("#303030"));

        initView();
        initRV();
        initRetrofit();
        return view;
    }

    private void initView() {
        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(mProgressBar, params);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void initRV() {
        mContributors = new ArrayList<>();
        mAdapter = new ContributorsAdapter(mContributors, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Service service = retrofit.create(Service.class);
        service.getContributors().enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Contributor>> call, @NonNull Response<List<Contributor>> response) {
                mContributors.addAll(response.body());
                mAdapter.updateContributors(mContributors);
                recyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<Contributor>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error downloading repositories list!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
