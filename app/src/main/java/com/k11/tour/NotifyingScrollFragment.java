package com.k11.tour;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.k11.tour.widget.NotifyingScrollView;

/**
 * Created by xiaoyu on 4/8/15.
 */
public class NotifyingScrollFragment extends Fragment {

    private Drawable mActionBarBackgroundDrawable;
    private ActionBar mActionBar;
    private View mHeader;
    private NotifyingScrollView mNotifyingScrollView;

    public void setActionBarBackgroundDrawable(Drawable actionBarBackgroundDrawable) {
        this.mActionBarBackgroundDrawable = actionBarBackgroundDrawable;
    }

    public void setActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notifying_scroll_frag, container, false);

        mHeader = rootView.findViewById(R.id.image_header);

        mNotifyingScrollView = ((NotifyingScrollView) rootView.findViewById(R.id.scroll_view));
        mNotifyingScrollView.setOnScrollChangedListener(mOnScrollChangedListener);

        return rootView;
    }

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            if(mHeader != null && mActionBar != null && mActionBarBackgroundDrawable != null) {
                final int headerHeight = mHeader.getHeight() - mActionBar.getHeight();
                final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
                final int newAlpha = (int) (ratio * 255);
                mActionBarBackgroundDrawable.setAlpha(newAlpha);
            }
        }
    };
}
