package com.cloudnapps.bethany;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * Created by xiaoyu on 4/8/15.
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

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
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
            mTranscriptToggle = (TextView) itemView.findViewById(R.id.transcript_toggle);
            mTranscriptToggle.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(mTranscriptToggle.getText().equals(getResources().getString(R.string.view_transcript))){
                        expandTranscript();
                    } else {
                        hideTranscript();
                    }
                }
            });
        }

        public void init(){
//            mTextView.setText("");
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
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if(interpolatedTime == 1){
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
            a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);


        }

        private void expandTranscript() {
            // set text to hide transcript
            mTranscriptToggle.setText(R.string.hide_transcript);

            final View v = mTranscriptTextView;
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();

            v.getLayoutParams().height = 0;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation()
            {
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
            a.setDuration((int)((targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 8);
            v.startAnimation(a);


        }
    }

    private void navigateToDetail() {
        Fragment fragment = new DetailFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder{

        public ViewHolderHeader(View itemView) {
            super(itemView);
        }
    }
}
