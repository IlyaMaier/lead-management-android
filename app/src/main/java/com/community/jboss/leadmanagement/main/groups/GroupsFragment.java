package com.community.jboss.leadmanagement.main.groups;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.data.entities.Groups;
import com.community.jboss.leadmanagement.main.MainActivity;
import com.community.jboss.leadmanagement.main.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsFragment extends MainFragment implements GroupsAdapter.AdapterListener, SearchView.OnQueryTextListener {

    @BindView(R.id.rv_groups)
    RecyclerView recyclerView;

    private GroupsAdapter mAdapter;
    private GroupsFragmentViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_groups, container, false);
        setHasOptionsMenu(true);

        final MainActivity activity = (MainActivity) getActivity();
        if (activity != null) activity.initFab();

        ButterKnife.bind(this, view);

        mViewModel = ViewModelProviders.of(this).get(GroupsFragmentViewModel.class);
        initRV();

        return view;
    }

    void initRV() {
        mAdapter = new GroupsAdapter(this, getActivity());
        mViewModel.getGroups().observe(this, groups -> {
            mAdapter.replaceData(groups);
            mAdapter.notifyDataSetChanged();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem importItem = menu.findItem(R.id.action_import);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        importItem.setVisible(false);
        searchItem.setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryRefinementEnabled(false);
        searchView.setOnQueryTextListener(this);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (item == searchMenuItem) {
                    mAdapter.getFilter().filter(searchView.getQuery());
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (item == searchMenuItem) {
                    mAdapter.getFilter().filter("");
                }
                return true;
            }
        });
    }


    @Override
    public int getTitle() {
        return R.string.title_groups;
    }

    @Override
    public void onGroupsDeleted(Groups groups) {
        mViewModel.deleteGroup(groups);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s);
        return true;
    }

}
