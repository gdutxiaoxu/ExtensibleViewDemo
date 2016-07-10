# 自定义View常用例子二（点击展开隐藏控件，九宫格图片控件）

**今天博客的主要内容是两个常见的自定义控件，第一个是我们经常看到的点击隐藏点击查看控件，第二个控件是仿微信朋友圈的九宫格图片控件，相对上一篇的流布式布局来说，这篇博客更容易，只不过涉及更多的知识点而已**


[**转载请注明原博客地址：**]()


## 一.废话不多说了，先来看一下效果图
1. **图一效果,点击隐藏，展开** 


 ![](http://7xvjnq.com1.z0.glb.clouddn.com/16-6-27/77762822.jpg)

----------

2. **图二效果，类似于朋友圈九宫格图片**


 ![](http://7xvjnq.com1.z0.glb.clouddn.com/16-6-27/98347681.jpg)

----------

[**图二源码下载地址**](https://github.com/gdutxiaoxu/NineGridView.git)

[**图一源码下载地址：**](https://github.com/gdutxiaoxu/CustomViewDemo2.git)

## 图一源码

```

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

```
## 思路解析
1. 如图所示，图一一四个部分组成，数字，标题，箭头，图片，点击标题所在的那一行，图片回相应地隐藏或者显示。
2. 数字，标题，箭头都在同一个相对布局里面，图片在单独的一个相对布局中，总体由    LinearLayout构成，布局文件如下

```

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	              android:layout_width="match_parent"
	              android:layout_height="wrap_content"
	              android:background="#ffffff"
	              android:orientation="vertical">
	    <RelativeLayout
	        android:id="@+id/titleRelativeLayout"
	        android:padding="30px"
	        android:layout_width="match_parent"
	        android:layout_height="170px"
	        android:clickable="true">
	
	        <TextView
	            android:id="@+id/numberTextView"
	            android:layout_width="70px"
	            android:layout_height="70px"
	            android:gravity="center"
	            android:layout_centerVertical="true"
	
	            android:clickable="false"
	            android:text="1"
	            android:textStyle="bold"
	            android:textColor="#EBEFEC"
	            android:textSize="35px" />
	
	        <TextView
	            android:id="@+id/titleTextView"
	            android:layout_width="match_parent"
	            android:layout_height="wrap_content"
	            android:layout_centerVertical="true"
	            android:layout_toRightOf="@id/numberTextView"
	            android:layout_marginLeft="30px"
	            android:clickable="false"
	            android:textColor="#1d953f"
	            android:textSize="46px" />
	
	
	        <ImageView
	            android:id="@+id/arrowImageView"
	            android:layout_width="48px"
	            android:layout_height="27px"
	            android:layout_alignParentRight="true"
	            android:layout_centerVertical="true"
	            android:background="@mipmap/arrow_down"
	            android:clickable="false"
	            android:scaleType="fitCenter" />
	    </RelativeLayout>
	
	    <View
	        android:layout_width="match_parent"
	        android:layout_height="2px"
	        android:layout_below="@id/titleRelativeLayout"
	        android:background="#E7E7EF"
	        android:clickable="false"
	        />
	
	    <RelativeLayout
	        android:id="@+id/contentRelativeLayout"
	        android:visibility="gone"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content">
	    </RelativeLayout>
	
	</LinearLayout>
```

把contentRelativeLayout的visibility设置为android:visibility="gone"，是因为一开始不用加载这个相对局部，这样它不会占用位置

3. 在代码中初始化布局，并给 mTitleRelativeLayout设置点击事件。


---

	 mTitleRelativeLayout.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                rotateArrow();
		            }
		        });
我们来看 rotateArrow()里面我们做了什么，其实就是根据相应的动画执行箭头旋转的动作和更改
contentRelativeLayout的高度
核心代码如下：

---
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

我们在来看一下在expand我们做了什么，其实就是给contentRelativeLayout执行一个动画，在动画的执行过程中不断改变contentRelativeLayout的高度，注意在执行动画之前，我们需要小调用view.measure(childWidthMode, childHeightMode);方法，这样我们可能获取到高度

---

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
反之，隐藏也是执行一个动画，不断改变高度，只不过高度 是越来越小，直至为0为止

###图一的源码分析到此为止，[源码下载地址：](https://github.com/gdutxiaoxu/CustomViewDemo2.git)https://github.com/gdutxiaoxu/CustomViewDemo2.git
