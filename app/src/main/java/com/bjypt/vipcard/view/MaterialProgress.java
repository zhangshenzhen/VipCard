package com.bjypt.vipcard.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * SwipeToRefreshLayout中下拉刷新时的小圈
 * Created by Sahadev on 2015/11/13.
 */
public class MaterialProgress extends FrameLayout {
    private static final int MAX_ALPHA = 255;
    private static final int STARTING_PROGRESS_ALPHA = 76;
    private static final int CIRCLE_DIAMETER = 40;
    private static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0F;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = 0.5F;
    private static final float MAX_PROGRESS_ANGLE = 0.8F;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int ALPHA_ANIMATION_DURATION = 300;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int ANIMATE_TO_START_DURATION = 200;
    private static final int CIRCLE_BG_LIGHT = -328966;
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private View mTarget;
    private boolean mRefreshing;
    private int mTouchSlop;
    private float mTotalDragDistance;
    private float mTotalUnconsumed;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private int[] mParentScrollConsumed;
    private int mMediumAnimationDuration;
    private int mCurrentTargetOffsetTop;
    private boolean mOriginalOffsetCalculated;
    private float mInitialMotionY;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private int mActivePointerId;
    private boolean mScale;
    private boolean mReturningToStart;
    private DecelerateInterpolator mDecelerateInterpolator;
    private static final int[] LAYOUT_ATTRS = new int[]{16842766};
    private CircleImageView mCircleView;
    private int mCircleViewIndex;
    protected int mFrom;
    private float mStartingScale;
    protected int mOriginalOffsetTop;
    private MaterialProgressDrawable mProgress;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mAlphaStartAnimation;
    private Animation mAlphaMaxAnimation;
    private Animation mScaleDownToStartAnimation;
    private float mSpinnerFinalOffset;
    private boolean mNotify;
    private int mCircleWidth;
    private int mCircleHeight;
    private boolean mUsingCustomStart;
    private Animation.AnimationListener mRefreshListener;
    private Animation mAnimateToCorrectPosition;
    private Animation mPeek;
    private Animation mAnimateToStartPosition;

    public MaterialProgress(Context context) {
        this(context, null, 0);
    }

    public MaterialProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mRefreshing = false;
        this.mTotalDragDistance = -1.0F;
        this.mParentScrollConsumed = new int[2];
        this.mOriginalOffsetCalculated = false;
        this.mActivePointerId = -1;
        this.mCircleViewIndex = -1;
        this.mRefreshListener = new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (MaterialProgress.this.mRefreshing) {
                    MaterialProgress.this.mProgress.setAlpha(255);
                    MaterialProgress.this.mProgress.start();
                } else {
                    MaterialProgress.this.mProgress.stop();
                    MaterialProgress.this.mCircleView.setVisibility(View.VISIBLE);
                    MaterialProgress.this.setColorViewAlpha(255);
                    if (MaterialProgress.this.mScale) {
                        MaterialProgress.this.setAnimationProgress(0.0F);
                    } else {
                        MaterialProgress.this.setTargetOffsetTopAndBottom(MaterialProgress.this.mOriginalOffsetTop - MaterialProgress.this.mCurrentTargetOffsetTop, true);
                    }
                }

                MaterialProgress.this.mCurrentTargetOffsetTop = MaterialProgress.this.mCircleView.getTop();
            }
        };
        this.mAnimateToCorrectPosition = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                boolean targetTop = false;
                boolean endTarget = false;
                int endTarget1;
                if (!MaterialProgress.this.mUsingCustomStart) {
                    endTarget1 = (int) (MaterialProgress.this.mSpinnerFinalOffset - (float) Math.abs(MaterialProgress.this.mOriginalOffsetTop));
                } else {
                    endTarget1 = (int) MaterialProgress.this.mSpinnerFinalOffset;
                }

                int targetTop1 = MaterialProgress.this.mFrom + (int) ((float) (endTarget1 - MaterialProgress.this.mFrom) * interpolatedTime);
                int offset = targetTop1 - MaterialProgress.this.mCircleView.getTop();
                MaterialProgress.this.setTargetOffsetTopAndBottom(offset, false);
                MaterialProgress.this.mProgress.setArrowScale(1.0F - interpolatedTime);
            }
        };
        this.mPeek = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                boolean targetTop = false;
                boolean endTarget = false;
                int endTarget1;
                if (!MaterialProgress.this.mUsingCustomStart) {
                    endTarget1 = (int) (MaterialProgress.this.mSpinnerFinalOffset - (float) Math.abs(MaterialProgress.this.mOriginalOffsetTop));
                } else {
                    endTarget1 = (int) MaterialProgress.this.mSpinnerFinalOffset;
                }

                int targetTop1 = MaterialProgress.this.mFrom + (int) ((float) (endTarget1 - MaterialProgress.this.mFrom) * interpolatedTime);
                int offset = targetTop1 - MaterialProgress.this.mCircleView.getTop();
                MaterialProgress.this.setTargetOffsetTopAndBottom(offset, false);
                MaterialProgress.this.mProgress.setArrowScale(1.0F - interpolatedTime);
            }
        };
        this.mAnimateToStartPosition = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                MaterialProgress.this.moveToStart(interpolatedTime);
            }
        };
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //// TODO: 2015/11/11 500 是自己手动修改的
        this.mMediumAnimationDuration = 500;
        this.setWillNotDraw(false);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0F);
        TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        this.setEnabled(a.getBoolean(0, true));
        a.recycle();
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        this.mCircleWidth = (int) (40.0F * metrics.density);
        this.mCircleHeight = (int) (40.0F * metrics.density);
        this.createProgressView();
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        this.mSpinnerFinalOffset = 64.0F * metrics.density;
        this.mTotalDragDistance = this.mSpinnerFinalOffset;
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        this.setNestedScrollingEnabled(true);


        setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        setRefreshing(true);
        this.finishSpinner(mTotalDragDistance + 10);
    }

    private void setColorViewAlpha(int targetAlpha) {
        this.mCircleView.getBackground().setAlpha(targetAlpha);
        this.mProgress.setAlpha(targetAlpha);
    }


    public void setSize(int size) {
        if (size == 0 || size == 1) {
            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
            if (size == 0) {
                this.mCircleHeight = this.mCircleWidth = (int) (56.0F * metrics.density);
            } else {
                this.mCircleHeight = this.mCircleWidth = (int) (40.0F * metrics.density);
            }

            this.mCircleView.setImageDrawable((Drawable) null);
            this.mProgress.updateSizes(size);
            this.mCircleView.setImageDrawable(this.mProgress);
        }
    }

    private void createProgressView() {
        this.mCircleView = new CircleImageView(this.getContext(), -328966, 20.0F);
        this.mProgress = new MaterialProgressDrawable(this.getContext(), this);
        this.mProgress.setBackgroundColor(-328966);
        this.mCircleView.setImageDrawable(this.mProgress);
        this.mCircleView.setVisibility(View.VISIBLE);
        this.mCircleView.setPadding(5, 5, 5, 5);//设置一个内边距
        this.addView(this.mCircleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private boolean isAlphaUsedForScale() {
        return Build.VERSION.SDK_INT < 11;
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && this.mRefreshing != refreshing) {
            this.mRefreshing = refreshing;
            boolean endTarget = false;
            int endTarget1;
            if (!this.mUsingCustomStart) {
                endTarget1 = (int) (this.mSpinnerFinalOffset + (float) this.mOriginalOffsetTop);
            } else {
                endTarget1 = (int) this.mSpinnerFinalOffset;
            }

            this.setTargetOffsetTopAndBottom(endTarget1 - this.mCurrentTargetOffsetTop, true);
            this.mNotify = false;
            this.startScaleUpAnimation(this.mRefreshListener);
        } else {
            this.setRefreshing(refreshing, false);
        }

    }

    private void startScaleUpAnimation(Animation.AnimationListener listener) {
        this.mCircleView.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 11) {
            this.mProgress.setAlpha(255);
        }

        this.mScaleAnimation = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                MaterialProgress.this.setAnimationProgress(interpolatedTime);
            }
        };
        this.mScaleAnimation.setDuration((long) this.mMediumAnimationDuration);
        if (listener != null) {
            this.mCircleView.setAnimationListener(listener);
        }

        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleAnimation);
    }

    private void setAnimationProgress(float progress) {
        if (this.isAlphaUsedForScale()) {
            this.setColorViewAlpha((int) (progress * 255.0F));
        } else {
            ViewCompat.setScaleX(this.mCircleView, progress);
            ViewCompat.setScaleY(this.mCircleView, progress);
        }

    }

    private void setRefreshing(boolean refreshing, boolean notify) {
        if (this.mRefreshing != refreshing) {
            this.mNotify = notify;
            this.ensureTarget();
            this.mRefreshing = refreshing;
            if (this.mRefreshing) {
                this.animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
            } else {
                this.startScaleDownAnimation(this.mRefreshListener);
            }
        }

    }

    private void startScaleDownAnimation(Animation.AnimationListener listener) {
        this.mScaleDownAnimation = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                MaterialProgress.this.setAnimationProgress(1.0F - interpolatedTime);
            }
        };
        this.mScaleDownAnimation.setDuration(150L);
        this.mCircleView.setAnimationListener(listener);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownAnimation);
    }

    private void startProgressAlphaStartAnimation() {
        this.mAlphaStartAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 76);
    }

    private void startProgressAlphaMaxAnimation() {
        this.mAlphaMaxAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 255);
    }

    private Animation startAlphaAnimation(final int startingAlpha, final int endingAlpha) {
        if (this.mScale && this.isAlphaUsedForScale()) {
            return null;
        } else {
            Animation alpha = new Animation() {
                public void applyTransformation(float interpolatedTime, Transformation t) {
                    MaterialProgress.this.mProgress.setAlpha((int) ((float) startingAlpha + (float) (endingAlpha - startingAlpha) * interpolatedTime));
                }
            };
            alpha.setDuration(300L);
            this.mCircleView.setAnimationListener((Animation.AnimationListener) null);
            this.mCircleView.clearAnimation();
            this.mCircleView.startAnimation(alpha);
            return alpha;
        }
    }

    public void setProgressBackgroundColorSchemeResource(@ColorRes int colorRes) {
        this.setProgressBackgroundColorSchemeColor(this.getResources().getColor(colorRes));
    }

    public void setProgressBackgroundColorSchemeColor(@ColorInt int color) {
        this.mCircleView.setBackgroundColor(color);
        this.mProgress.setBackgroundColor(color);
    }

    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        Resources res = this.getResources();
        int[] colorRes = new int[colorResIds.length];

        for (int i = 0; i < colorResIds.length; ++i) {
            colorRes[i] = res.getColor(colorResIds[i]);
        }

        this.setColorSchemeColors(colorRes);
    }

    @ColorInt
    public void setColorSchemeColors(int... colors) {
        this.ensureTarget();
        this.mProgress.setColorSchemeColors(colors);
    }

    public boolean isRefreshing() {
        return this.mRefreshing;
    }

    private void ensureTarget() {
        if (this.mTarget == null) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                if (!child.equals(this.mCircleView)) {
                    this.mTarget = child;
                    break;
                }
            }
        }

    }

    public void setDistanceToTriggerSync(int distance) {
        this.mTotalDragDistance = (float) distance;
    }


    public int getProgressCircleDiameter() {
        return this.mCircleView != null ? this.mCircleView.getMeasuredHeight() : 0;
    }

    public boolean canChildScrollUp() {
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.mTarget, -1);
        } else if (this.mTarget instanceof AbsListView) {
            AbsListView absListView = (AbsListView) this.mTarget;
            return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
        } else {
            return ViewCompat.canScrollVertically(this.mTarget, -1) || this.mTarget.getScrollY() > 0;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.ensureTarget();
        int action = MotionEventCompat.getActionMasked(ev);
        if (this.mReturningToStart && action == 0) {
            this.mReturningToStart = false;
        }

        if (this.isEnabled() && !this.mReturningToStart && !this.canChildScrollUp() && !this.mRefreshing) {
            switch (action) {
                case 0:
                    this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCircleView.getTop(), true);
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    this.mIsBeingDragged = false;
                    float initialDownY = this.getMotionEventY(ev, this.mActivePointerId);
                    if (initialDownY == -1.0F) {
                        return false;
                    }

                    this.mInitialDownY = initialDownY;
                    break;
                case 1:
                case 3:
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    if (this.mActivePointerId == -1) {
                        return false;
                    }

                    float y = this.getMotionEventY(ev, this.mActivePointerId);
                    if (y == -1.0F) {
                        return false;
                    }

                    float yDiff = y - this.mInitialDownY;
                    if (yDiff > (float) this.mTouchSlop && !this.mIsBeingDragged) {
                        this.mInitialMotionY = this.mInitialDownY + (float) this.mTouchSlop;
                        this.mIsBeingDragged = true;
                        this.mProgress.setAlpha(76);
                    }
                case 4:
                case 5:
                default:
                    break;
                case 6:
                    this.onSecondaryPointerUp(ev);
            }

            return this.mIsBeingDragged;
        } else {
            return false;
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        return index < 0 ? -1.0F : MotionEventCompat.getY(ev, index);
    }

    public void requestDisallowInterceptTouchEvent(boolean b) {
        if ((Build.VERSION.SDK_INT >= 21 || !(this.mTarget instanceof AbsListView)) && (this.mTarget == null || ViewCompat.isNestedScrollingEnabled(this.mTarget))) {
            super.requestDisallowInterceptTouchEvent(b);
        }

    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (this.isEnabled() && (nestedScrollAxes & 2) != 0) {
            this.startNestedScroll(nestedScrollAxes & 2);
            return true;
        } else {
            return false;
        }
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        this.mTotalUnconsumed = 0.0F;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && this.mTotalUnconsumed > 0.0F) {
            if ((float) dy > this.mTotalUnconsumed) {
                consumed[1] = dy - (int) this.mTotalUnconsumed;
                this.mTotalUnconsumed = 0.0F;
            } else {
                this.mTotalUnconsumed -= (float) dy;
                consumed[1] = dy;
            }

            this.moveSpinner(this.mTotalUnconsumed);
        }

        int[] parentConsumed = this.mParentScrollConsumed;
        if (this.dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, (int[]) null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }

    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public void onStopNestedScroll(View target) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(target);
        if (this.mTotalUnconsumed > 0.0F) {
            this.finishSpinner(this.mTotalUnconsumed);
            this.mTotalUnconsumed = 0.0F;
        }

        this.stopNestedScroll();
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0) {
            dyUnconsumed = Math.abs(dyUnconsumed);
            this.mTotalUnconsumed += (float) dyUnconsumed;
            this.moveSpinner(this.mTotalUnconsumed);
        }

        this.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dxConsumed, (int[]) null);
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return this.mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private boolean isAnimationRunning(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }

    private void moveSpinner(float overscrollTop) {
        this.mProgress.showArrow(true);
        float originalDragPercent = overscrollTop / this.mTotalDragDistance;
        float dragPercent = Math.min(1.0F, Math.abs(originalDragPercent));
        float adjustedPercent = (float) Math.max((double) dragPercent - 0.4D, 0.0D) * 5.0F / 3.0F;
        float extraOS = Math.abs(overscrollTop) - this.mTotalDragDistance;
        float slingshotDist = this.mUsingCustomStart ? this.mSpinnerFinalOffset - (float) this.mOriginalOffsetTop : this.mSpinnerFinalOffset;
        float tensionSlingshotPercent = Math.max(0.0F, Math.min(extraOS, slingshotDist * 2.0F) / slingshotDist);
        float tensionPercent = (float) ((double) (tensionSlingshotPercent / 4.0F) - Math.pow((double) (tensionSlingshotPercent / 4.0F), 2.0D)) * 2.0F;
        float extraMove = slingshotDist * tensionPercent * 2.0F;
        int targetY = this.mOriginalOffsetTop + (int) (slingshotDist * dragPercent + extraMove);
        if (this.mCircleView.getVisibility() != View.VISIBLE) {
            this.mCircleView.setVisibility(View.VISIBLE);
        }

        if (!this.mScale) {
            ViewCompat.setScaleX(this.mCircleView, 1.0F);
            ViewCompat.setScaleY(this.mCircleView, 1.0F);
        }

        float rotation;
        if (overscrollTop < this.mTotalDragDistance) {
            if (this.mScale) {
                this.setAnimationProgress(overscrollTop / this.mTotalDragDistance);
            }

            if (this.mProgress.getAlpha() > 76 && !this.isAnimationRunning(this.mAlphaStartAnimation)) {
                this.startProgressAlphaStartAnimation();
            }

            rotation = adjustedPercent * 0.8F;
            this.mProgress.setStartEndTrim(0.0F, Math.min(0.8F, rotation));
            this.mProgress.setArrowScale(Math.min(1.0F, adjustedPercent));
        } else if (this.mProgress.getAlpha() < 255 && !this.isAnimationRunning(this.mAlphaMaxAnimation)) {
            this.startProgressAlphaMaxAnimation();
        }

        rotation = (-0.25F + 0.4F * adjustedPercent + tensionPercent * 2.0F) * 0.5F;
        this.mProgress.setProgressRotation(rotation);
        this.setTargetOffsetTopAndBottom(targetY - this.mCurrentTargetOffsetTop, true);
    }

    private void finishSpinner(float overscrollTop) {
        if (overscrollTop > this.mTotalDragDistance) {
            this.setRefreshing(true, true);
        } else {
            this.mRefreshing = false;
            this.mProgress.setStartEndTrim(0.0F, 0.0F);
            Animation.AnimationListener listener = null;
            if (!this.mScale) {
                listener = new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        if (!MaterialProgress.this.mScale) {
                            MaterialProgress.this.startScaleDownAnimation((Animation.AnimationListener) null);
                        }

                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                };
            }

            this.animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, listener);
            this.mProgress.showArrow(false);
        }

    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (this.mReturningToStart && action == 0) {
            this.mReturningToStart = false;
        }

        if (this.isEnabled() && !this.mReturningToStart && !this.canChildScrollUp()) {
            int pointerIndex;
            float y;
            float overscrollTop;
            switch (action) {
                case 0:
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    this.mIsBeingDragged = false;
                    break;
                case 1:
                case 3:
                    if (this.mActivePointerId == -1) {
                        if (action == 1) {
                        }

                        return false;
                    }

                    pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                    y = MotionEventCompat.getY(ev, pointerIndex);
                    overscrollTop = (y - this.mInitialMotionY) * 0.5F;
                    this.mIsBeingDragged = false;
                    this.finishSpinner(overscrollTop);
                    this.mActivePointerId = -1;
                    return false;
                case 2:
                    pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                    if (pointerIndex < 0) {
                        return false;
                    }

                    y = MotionEventCompat.getY(ev, pointerIndex);
                    overscrollTop = (y - this.mInitialMotionY) * 0.5F;
                    if (this.mIsBeingDragged) {
                        if (overscrollTop <= 0.0F) {
                            return false;
                        }
                        this.moveSpinner(overscrollTop);
                    }
                case 4:
                default:
                    break;
                case 5:
                    pointerIndex = MotionEventCompat.getActionIndex(ev);
                    this.mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                    break;
                case 6:
                    this.onSecondaryPointerUp(ev);
            }

            return true;
        } else {
            return false;
        }
    }

    private void animateOffsetToCorrectPosition(int from, Animation.AnimationListener listener) {
        this.mFrom = from;
        this.mAnimateToCorrectPosition.reset();
        this.mAnimateToCorrectPosition.setDuration(200L);
        this.mAnimateToCorrectPosition.setInterpolator(this.mDecelerateInterpolator);
        if (listener != null) {
            this.mCircleView.setAnimationListener(listener);
        }

        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
    }

    private void peek(int from, Animation.AnimationListener listener) {
        this.mFrom = from;
        this.mPeek.reset();
        this.mPeek.setDuration(500L);
        this.mPeek.setInterpolator(this.mDecelerateInterpolator);
        if (listener != null) {
            this.mCircleView.setAnimationListener(listener);
        }

        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mPeek);
    }

    private void animateOffsetToStartPosition(int from, Animation.AnimationListener listener) {
        if (this.mScale) {
            this.startScaleDownReturnToStartAnimation(from, listener);
        } else {
            this.mFrom = from;
            this.mAnimateToStartPosition.reset();
            this.mAnimateToStartPosition.setDuration(200L);
            this.mAnimateToStartPosition.setInterpolator(this.mDecelerateInterpolator);
            if (listener != null) {
                this.mCircleView.setAnimationListener(listener);
            }

            this.mCircleView.clearAnimation();
            this.mCircleView.startAnimation(this.mAnimateToStartPosition);
        }

    }

    private void moveToStart(float interpolatedTime) {
        boolean targetTop = false;
        int targetTop1 = this.mFrom + (int) ((float) (this.mOriginalOffsetTop - this.mFrom) * interpolatedTime);
        int offset = targetTop1 - this.mCircleView.getTop();
        this.setTargetOffsetTopAndBottom(offset, false);
    }

    private void startScaleDownReturnToStartAnimation(int from, Animation.AnimationListener listener) {
        this.mFrom = from;
        if (this.isAlphaUsedForScale()) {
            this.mStartingScale = (float) this.mProgress.getAlpha();
        } else {
            this.mStartingScale = ViewCompat.getScaleX(this.mCircleView);
        }

        this.mScaleDownToStartAnimation = new Animation() {
            public void applyTransformation(float interpolatedTime, Transformation t) {
                float targetScale = MaterialProgress.this.mStartingScale + -MaterialProgress.this.mStartingScale * interpolatedTime;
                MaterialProgress.this.setAnimationProgress(targetScale);
                MaterialProgress.this.moveToStart(interpolatedTime);
            }
        };
        this.mScaleDownToStartAnimation.setDuration(150L);
        if (listener != null) {
            this.mCircleView.setAnimationListener(listener);
        }

        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownToStartAnimation);
    }

    private void setTargetOffsetTopAndBottom(int offset, boolean requiresUpdate) {
        this.mCircleView.bringToFront();
        this.mCircleView.offsetTopAndBottom(offset);
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
        if (requiresUpdate && Build.VERSION.SDK_INT < 11) {
            this.invalidate();
        }

    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }

    }

}
