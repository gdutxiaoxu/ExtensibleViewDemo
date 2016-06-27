package com.xujun.administrator.collapseviewdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xujun.administrator.collapseviewdemo.R;

import static com.xujun.administrator.collapseviewdemo.uitls.ViewUtils.createCircleShape;

/**
 * @ explain:
 * <p/>
 * 注意：若使用setContent(View view)这个方法，
 * 强制layoutParams must be RelativeLayout.LayoutParams，防止在某些情况下出现错误
 * @ author：xujun on 2016/6/27 11:21
 * @ email：gdutxiaoxu@163.com
 */
public class CollapseView extends LinearLayout {
    private long duration = 350;
    private Context mContext;
    private TextView mNumberTextView;
    private TextView mTitleTextView;
    private RelativeLayout mContentRelativeLayout;
    private RelativeLayout mTitleRelativeLayout;
    private ImageView mArrowImageView;
    int parentWidthMeasureSpec;
    int parentHeightMeasureSpec;
    private String TAG = "xujun";

    public CollapseView(Context context) {
        this(context, null);
    }

    public CollapseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.collapse_layout, this);
        initView();
    }

    private void initView() {
        mNumberTextView = (TextView) findViewById(R.id.numberTextView);
        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mTitleRelativeLayout = (RelativeLayout) findViewById(R.id.titleRelativeLayout);
        mContentRelativeLayout = (RelativeLayout) findViewById(R.id.contentRelativeLayout);
        mArrowImageView = (ImageView) findViewById(R.id.arrowImageView);
        mTitleRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateArrow();
            }
        });
        mNumberTextView.setBackgroundResource(R.drawable.circle);
        Drawable circleShape = createCircleShape(Color.BLACK);
        mNumberTextView.setBackgroundDrawable(circleShape);
        collapse(mContentRelativeLayout);
    }

    public void setNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            mNumberTextView.setText(number);
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleTextView.setText(title);
        }
    }

    public void setContent(int resID) {
        View view = LayoutInflater.from(mContext).inflate(resID, null);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout
                        .LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        mContentRelativeLayout.addView(view);
    }

    /**
     * 若使用这个方法，强制layoutParams must be RelativeLayout.LayoutParams，防止在某些情况下出现错误
     *
     * @param view
     */
    public void setContent(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (layoutParams == null) {
            layoutParams = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }

        if (!(layoutParams instanceof RelativeLayout.LayoutParams)) {
            throw new IllegalStateException("layoutParams must be RelativeLayout.LayoutParams ");
        }

        view.setLayoutParams(layoutParams);
        mContentRelativeLayout.addView(view);
    }

    public void rotateArrow() {
        int degree = 0;
        if (mArrowImageView.getTag() == null || mArrowImageView.getTag().equals(true)) {
            mArrowImageView.setTag(false);
            degree = -180;
            expand(mContentRelativeLayout);
        } else {
            degree = 0;
            mArrowImageView.setTag(true);
            collapse(mContentRelativeLayout);
        }
        mArrowImageView.animate().setDuration(duration).rotation(degree);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidthMeasureSpec = widthMeasureSpec;
        parentHeightMeasureSpec = heightMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    // 展开
    private void expand(final View view) {

        view.setVisibility(View.VISIBLE);
        int childWidthMode = getMode(parentWidthMeasureSpec);
        int childHeightMode = getMode(parentHeightMeasureSpec);
        view.measure(childWidthMode, childHeightMode);
        final int measuredWidth = view.getMeasuredWidth();
        final int measuredHeight = view.getMeasuredHeight();

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float precent = animation.getAnimatedFraction();
                int width = (int) (measuredWidth * precent);
                setWidth(view, width);
                // 动画执行结束的时候，同时移除监听器
                if (precent == 1) {
                    valueAnimator.removeAllUpdateListeners();
                }
            }
        });
        valueAnimator.start();

    }

    private int getMode(int parentMeasureSpec) {

        if (parentMeasureSpec == MeasureSpec.EXACTLY) {
            return MeasureSpec.AT_MOST;
        } else if (parentMeasureSpec == MeasureSpec.AT_MOST) {
            return MeasureSpec.AT_MOST;
        } else {
            return parentMeasureSpec;
        }
    }

    private void setWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
        view.requestLayout();
    }

    // 折叠
    private void collapse(final View view) {
        final int measuredHeight = view.getMeasuredHeight();
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(duration);
        final int viewMeasuredWidth = view.getMeasuredWidth();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float precent = animation.getAnimatedFraction();
                Log.i(TAG, "onAnimationUpdate: precent" + precent);
                int width = (int) (viewMeasuredWidth - viewMeasuredWidth * precent);
                setWidth(view, width);
//                动画执行结束的时候，设置View为View.GONE，同时移除监听器
                if (precent == 1) {
                    view.setVisibility(View.GONE);
                    valueAnimator.removeAllUpdateListeners();
                }
            }
        });
        valueAnimator.start();
    }
}
