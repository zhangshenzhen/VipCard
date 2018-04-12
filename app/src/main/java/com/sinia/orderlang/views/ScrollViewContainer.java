 package com.sinia.orderlang.views;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * ��������ScrollView������
 * ������������http://dwtedx.com
 * 
 * @author chenjing
 * 
 */
public class ScrollViewContainer extends RelativeLayout {

	/**
	 * �Զ��ϻ�
	 */
	public static final int AUTO_UP = 0;
	/**
	 * �Զ��»�
	 */
	public static final int AUTO_DOWN = 1;
	/**
	 * �������
	 */
	public static final int DONE = 2;
	/**
	 * �����ٶ�
	 */
	public static final float SPEED = 6.5f;

	private boolean isMeasured = false;

	/**
	 * ���ڼ����ֻ������ٶ�
	 */
	private VelocityTracker vt;

	private int mViewHeight;
	private int mViewWidth;

	private View topView;
	private View bottomView;

	private boolean canPullDown;
	private boolean canPullUp;
	private int state = DONE;

	/**
	 * ��¼��ǰչʾ�����ĸ�view��0��topView��1��bottomView
	 */
	private int mCurrentViewIndex = 0;
	/**
	 * �ֻ������룬����ǿ��Ʋ��ֵ���Ҫ����
	 */
	private float mMoveLen;
	private MyTimer mTimer;
	private float mLastY;
	/**
	 * ���ڿ����Ƿ�䶯���ֵ���һ��������mEvents==0ʱ���ֿ�����ק�ˣ�mEvents==-1ʱ����������Ҫ�����ĵ�һ��move�¼���
	 * �����ȥ������϶����Ĺؼ�
	 */
	private int mEvents;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (mMoveLen != 0) {
				if (state == AUTO_UP) {
					mMoveLen -= SPEED;
					if (mMoveLen <= -mViewHeight) {
						mMoveLen = -mViewHeight;
						state = DONE;
						mCurrentViewIndex = 1;
					}
				} else if (state == AUTO_DOWN) {
					mMoveLen += SPEED;
					if (mMoveLen >= 0) {
						mMoveLen = 0;
						state = DONE;
						mCurrentViewIndex = 0;
					}
				} else {
					mTimer.cancel();
				}
			}
			requestLayout();
		}

	};

	public ScrollViewContainer(Context context) {
		super(context);
		init();
	}

	public ScrollViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScrollViewContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mTimer = new MyTimer(handler);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if (vt == null)
				vt = VelocityTracker.obtain();
			else
				vt.clear();
			mLastY = ev.getY();
			vt.addMovement(ev);
			mEvents = 0;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// ��һֻ��ָ���»�̧��ʱ������Ҫ�����ĵ�һ���¼�move����ֹ�����ק��bug
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			vt.addMovement(ev);
			if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0) {
				mMoveLen += (ev.getY() - mLastY);
				// ��ֹ����Խ��
				if (mMoveLen > 0) {
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				} else if (mMoveLen < -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;

				}
				if (mMoveLen < -8) {
					// ��ֹ�¼���ͻ
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0) {
				mMoveLen += (ev.getY() - mLastY);
				// ��ֹ����Խ��
				if (mMoveLen < -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;
				} else if (mMoveLen > 0) {
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				}
				if (mMoveLen > 8 - mViewHeight) {
					// ��ֹ�¼���ͻ
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else
				mEvents++;
			mLastY = ev.getY();
			requestLayout();
			break;
		case MotionEvent.ACTION_UP:
			mLastY = ev.getY();
			vt.addMovement(ev);
			vt.computeCurrentVelocity(700);
			// ��ȡY������ٶ�
			float mYV = vt.getYVelocity();
			if (mMoveLen == 0 || mMoveLen == -mViewHeight)
				break;
			if (Math.abs(mYV) < 500) {
				// �ٶ�С��һ��ֵ��ʱ������ֹ�ͷţ���ʱ������View�����ƶ�ȡ���ڻ����ľ���
				if (mMoveLen <= -mViewHeight / 2) {
					state = AUTO_UP;
				} else if (mMoveLen > -mViewHeight / 2) {
					state = AUTO_DOWN;
				}
			} else {
				// ̧����ָʱ�ٶȷ����������View�����ƶ�
				if (mYV < 0)
					state = AUTO_UP;
				else
					state = AUTO_DOWN;
			}
			mTimer.schedule(2);
			try {
				vt.recycle();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		}
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		topView.layout(0, (int) mMoveLen, mViewWidth,
				topView.getMeasuredHeight() + (int) mMoveLen);
		bottomView.layout(0, topView.getMeasuredHeight() + (int) mMoveLen,
				mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen
						+ bottomView.getMeasuredHeight());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isMeasured) {
			isMeasured = true;

			mViewHeight = getMeasuredHeight();
			mViewWidth = getMeasuredWidth();

			topView = getChildAt(0);
			bottomView = getChildAt(1);

			bottomView.setOnTouchListener(bottomViewTouchListener);
			topView.setOnTouchListener(topViewTouchListener);
		}
	}

	private OnTouchListener topViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ScrollView sv = (ScrollView) v;
			if (sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv
					.getMeasuredHeight()) && mCurrentViewIndex == 0)
				canPullUp = true;
			else
				canPullUp = false;
			return false;
		}
	};
	private OnTouchListener bottomViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ScrollView sv = (ScrollView) v;
			if (sv.getScrollY() == 0 && mCurrentViewIndex == 1)
				canPullDown = true;
			else
				canPullDown = false;
			return false;
		}
	};

	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}

		}
	}

}
