package com.fastaccess.ui.modules.pinned;

import android.support.annotation.NonNull;
import android.view.View;

import com.fastaccess.data.dao.NameParser;
import com.fastaccess.data.dao.model.AbstractPinnedRepos;
import com.fastaccess.data.dao.model.PinnedRepos;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.repos.RepoPagerView;

import java.util.ArrayList;

/**
 * Created by Kosh on 25 Mar 2017, 8:00 PM
 */

public class PinnedReposPresenter extends BasePresenter<PinnedReposMvp.View> implements PinnedReposMvp.Presenter {
    private ArrayList<PinnedRepos> pinnedRepos = new ArrayList<>();

    @Override protected void onAttachView(@NonNull PinnedReposMvp.View view) {
        super.onAttachView(view);
        if (pinnedRepos.isEmpty()) {
            onReload();
        }
    }

    @NonNull @Override public ArrayList<PinnedRepos> getPinnedRepos() {
        return pinnedRepos;
    }

    @Override public void onReload() {
        manageSubscription(AbstractPinnedRepos.getMyPinnedRepos()
                .subscribe(repos -> {
                    if (repos != null) {
                        pinnedRepos.clear();
                        pinnedRepos.addAll(repos);
                    }
                    sendToView(PinnedReposMvp.View::onNotifyAdapter);
                }));
    }

    @Override public void onItemClick(int position, View v, PinnedRepos item) {
        RepoPagerView.startRepoPager(v.getContext(), new NameParser(item.getPinnedRepo().getHtmlUrl()));
    }

    @Override public void onItemLongClick(int position, View v, PinnedRepos item) {
        if (getView() != null) {
            getView().onDeletePinnedRepo(item.getId(), position);
        }
    }
}
