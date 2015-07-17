package com.xiaoooyu.bethany.sample;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 */
public class BlurFrag01 extends Fragment {

    private ImageView image;
    private TextView text;
    private TextView statusText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.blur_01_frag, container, false);

        image = (ImageView) view.findViewById(R.id.picture);
        text = (TextView) view.findViewById(R.id.text);
        statusText = addStatusText((ViewGroup) view.findViewById(R.id.controls));

//        applyBlur();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyBlur();


            }
        });

        return view;
    }

    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();

                blur(bmp, getView());

                image.setVisibility(View.GONE);

                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(final Bitmap bkg, final View view) {


        AsyncTask asyncTask = new AsyncTask() {
            Bitmap mBlurBmp;
            long startMs = System.currentTimeMillis();

            @Override
            protected Object doInBackground(Object[] params) {
                startMs = System.currentTimeMillis();

                float scaleFactor = 1 / 6.0f;
                float radius = 2;

                int overlayWidth = (int) Math.ceil(view.getMeasuredWidth() * scaleFactor);
                int overlayHeight = (int) Math.ceil(view.getMeasuredHeight() * scaleFactor);

                mBlurBmp = Bitmap.createBitmap(overlayWidth, overlayHeight,
                        Bitmap.Config.ARGB_8888);

                Bitmap bkg = BitmapFactory.decodeResource(getResources(), R.drawable.my_bg_04);
                Canvas canvas = new Canvas(mBlurBmp);
                canvas.translate(- view.getLeft() * scaleFactor, - view.getTop() * scaleFactor);
                canvas.scale( scaleFactor, scaleFactor);
                Paint paint = new Paint();
                paint.setFlags(Paint.FILTER_BITMAP_FLAG);
                canvas.drawBitmap(bkg, 0, 0, paint);

                RenderScript rs = RenderScript.create(getActivity());
                Allocation overlayAlloc = Allocation.createFromBitmap(rs, mBlurBmp);
                ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());

                blur.setInput(overlayAlloc);
                blur.setRadius(radius);
                blur.forEach(overlayAlloc);
                overlayAlloc.copyTo(mBlurBmp);

                rs.destroy();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                view.setBackground(new BitmapDrawable(
                        getResources(), mBlurBmp));

                statusText.setText(System.currentTimeMillis() - startMs + "ms");

                getView().invalidate();
            }
        };
        asyncTask.execute();
    }

    private TextView addStatusText(ViewGroup container) {
        TextView result = new TextView(getActivity());
        result.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setTextColor(0xFFFFFFFF);
        container.addView(result);
        return result;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public String toString() {
        return "RenderScript";
    }
}
