package com.cloudnapps.bethany;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.cloudnapps.bethany.widget.NotifyingScrollView;

/**
 * Created by xiaoooyu on 4/15/15.
 */
public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";

    private NotifyingScrollView mScrollView;
    private View mHeader;
    private int mHeaderOriginHeight;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_frag, container, false);

        mScrollView = (NotifyingScrollView) v.findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangedListener(mOnScrollChangedListener);

        mHeader = v.findViewById(R.id.imageView);
        ViewTreeObserver viewTreeObserver = mHeader.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mHeaderOriginHeight = mHeader.getHeight();
                    Log.v(TAG, String.format("orig h:%d", mHeaderOriginHeight));
                }
            });
        }
        return v;
    }

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            Log.v(TAG, String.format("t:%d", t));
            int deltaHeight = Math.min(t, mHeaderOriginHeight);

            mHeader.getLayoutParams().height = mHeaderOriginHeight - deltaHeight;
            mHeader.requestLayout();

//            if(mHeader != null && mActionBar != null && mActionBarBackgroundDrawable != null) {
//                final int headerHeight = mHeader.getHeight() - mActionBar.getHeight();
//                final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
//                final int newAlpha = (int) (ratio * 255);
//                mActionBarBackgroundDrawable.setAlpha(newAlpha);
//            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Detail");
    }
}
