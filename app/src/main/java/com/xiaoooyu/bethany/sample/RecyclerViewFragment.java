package com.xiaoooyu.bethany.sample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * Fragment presents the main scenes list
 * which also
 */
public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final int DATASET_COUNT = 20;
    private String[] mDataset;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter<?> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);

        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewFragmentAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("List");
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }

    private void navigateToDetail() {
        Fragment fragment = new DetailFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }

    private class RecyclerViewFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        private String[] mDataset;
        private RecyclerViewFragmentAdapter(String[] dataset) {
            mDataset = dataset;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new view.
            RecyclerView.ViewHolder viewHolder;
            if(viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_header, parent, false);
                viewHolder = new ViewHolderHeader(v);
            } else {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_item, parent, false);
                viewHolder = new ViewHolderItem(v);
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof ViewHolderItem) {
//                ((ViewHolderItem) holder).setText(mDataset[position]);
                ((ViewHolderItem) holder).init();
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length + 1;
        }

        @Override
        public int getItemViewType(int position) {
            int returnType = TYPE_ITEM;

            if(position == 0) {
                returnType = TYPE_HEADER;
            }

            return returnType;
        }
    }



    private class ViewHolderItem extends RecyclerView.ViewHolder{

        private final TextView mTextView;
        private final TextView mTranscriptToggle;
        private final TextView mTranscriptTextView;

        public ViewHolderItem(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToDetail();
                }
            });
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mTranscriptTextView = (TextView) itemView.findViewById(R.id.transcript);

            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mTranscriptTextView.measure(widthMeasureSpec, heightMeasureSpec);

            mTranscriptToggle = (TextView) itemView.findViewById(R.id.transcript_toggle);
            mTranscriptToggle.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(mTranscriptToggle.getText().equals(getResources().getString(R.string.view_transcript))){
                        expandTranscript(0);
                    } else {
                        hideTranscript();
                    }
                }
            });
        }

        public void init(){
            mTranscriptTextView.setVisibility(View.GONE);
            mTranscriptToggle.setText(R.string.view_transcript);
        }

        public void setText(String text) {
            mTextView.setText(text);
        }

        private void hideTranscript() {
            // set text to view transcript
            mTranscriptToggle.setText(R.string.view_transcript);

            final View v = mTranscriptTextView;
            final int initialHeight = v.getHeight();

//            Log.d(TAG, String.format("-H:%d", initialHeight));
            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if(interpolatedTime == 1){
                        v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        v.setVisibility(View.GONE);
                    }else{
                        v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            long duringMillis = (long)(initialHeight / v.getContext().getResources().getDisplayMetrics().density);
            a.setDuration(duringMillis);
            v.startAnimation(a);
        }

        private void expandTranscript(final int initialHeight) {
            // set text to hide transcript
            mTranscriptToggle.setText(R.string.hide_transcript);

            final View v = mTranscriptTextView;
            v.getLayoutParams().height = 0;
            v.setVisibility(View.VISIBLE);
            final ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
            if(viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);

                        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                                v.getWidth(), View.MeasureSpec.EXACTLY);
                        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                                ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED);

                        v.measure(widthMeasureSpec, heightMeasureSpec);

                        final int targetHeight = v.getMeasuredHeight();
//                        Log.d(TAG, String.format("+H:%d", targetHeight));

                        Animation a = new Animation() {
                            @Override
                            protected void applyTransformation(float interpolatedTime, Transformation t) {
                                v.getLayoutParams().height = interpolatedTime == 1
                                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                                        : (int)(targetHeight * interpolatedTime);
                                v.requestLayout();
                            }

                            @Override
                            public boolean willChangeBounds() {
                                return true;
                            }
                        };
                        // 1dp/ms
                        long duringMillis = (long)(targetHeight /
                                v.getContext().getResources().getDisplayMetrics().density);
                        a.setDuration(duringMillis);
                        v.startAnimation(a);
                    }
                });
            }
        }
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder{

        public ViewHolderHeader(View itemView) {
            super(itemView);
        }
    }
    // inner class END
}
