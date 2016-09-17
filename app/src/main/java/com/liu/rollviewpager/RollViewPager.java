package com.liu.rollviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 循环滚动切换图片(支持带标题,不带标题传null即可),自带适配器 支持显示本地res图片和网络图片，指定uri的图片
 * OnPagerClickCallback onPagerClickCallback 处理page被点击的回调接口, 被用户手动滑动时，暂停滚动，增强用户友好性
 *
 * @author dance
 *
 */
public class RollViewPager extends ViewPager {
	private Context context;
	private int currentItem;
	private ArrayList<String> uriList;// 图片地址
	private ArrayList<View> dots;// 点的位置不固定，所以需要让调用者传入
	private TextView title;// 用于显示每个图片的标题
	private String[] titles;
	private int dot_focus_resId;// 获取焦点的图片id
	private int dot_normal_resId;// 未获取焦点的图片id
	private OnPagerClickCallback onPagerClickCallback;
	MyOnTouchListener myOnTouchListener;
	ViewPagerTask viewPagerTask;
	private int max_count;

	private long start = 0;

	/**
	 * 是否循环
	 */
	private boolean isCycle=false;

	public class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					start = System.currentTimeMillis();
					handler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_MOVE:
					handler.removeCallbacks(viewPagerTask);
					break;
				case MotionEvent.ACTION_CANCEL:
					startRoll();
					break;
				case MotionEvent.ACTION_UP:
					long duration = System.currentTimeMillis() - start;
					if (duration <= 400) {
						if(onPagerClickCallback!=null)onPagerClickCallback.onPagerClick(currentItem);
					}
					startRoll();
					break;
			}
			return true;
		}
	}

	public boolean isCycle() {
		return isCycle;
	}

	public void setCycle(boolean cycle) {
		isCycle = cycle;
	}

	public class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			if (currentItem+1==max_count) {
				currentItem = (currentItem + 1)% uriList.size();
			}else
			{
				currentItem=currentItem+1;
			}
			handler.obtainMessage().sendToTarget();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			RollViewPager.this.setCurrentItem(currentItem);
			startRoll();
		}
	};

	/**
	 * 循环滚动切换图片，被触摸时，暂停滚动，增强用户友好性 ，支持带标题,设置标题用setTitle，
	 * 支持显示本地res图片和网络图片，指定uri的图片
	 *
	 * @param context
	 * @param dots
	 *            图片的点的集合，之所以不自动生成，是因为点的位置和大小不确定，所以由调用者传入
	 * @param onPagerClickCallback
	 *            页面点击回调
	 * @param dot_focus_resId
	 *            获取焦点的图片id
	 * @param dot_normal_resId
	 *            未获取焦点的图片id
	 */
	public RollViewPager(Context context, ArrayList<View> dots,
						 int dot_focus_resId, int dot_normal_resId,
						 OnPagerClickCallback onPagerClickCallback) {
		super(context);
		this.context = context;
		this.dots = dots;
		this.dot_focus_resId = dot_focus_resId;
		this.dot_normal_resId = dot_normal_resId;
		this.onPagerClickCallback = onPagerClickCallback;
		if (isCycle){
			this.max_count=dots.size()*20;
			this.currentItem=max_count/2;
		}else {
			this.max_count=dots.size();
			this.currentItem=max_count/2;
		}

		viewPagerTask = new ViewPagerTask();
		myOnTouchListener = new MyOnTouchListener();
	}

	/**
	 * 设置网络图片的url集合，也可以是本地图片的uri
	 * 图片uriList集合，可以是网络地址，如：http://www.ssss.cn/3.jpg，也可以是本地的uri,如：
	 * assest://image/3.jpg，uriList和resImageIds只需传入一个
	 *
	 * @param uriList
	 */
	public void setUriList(ArrayList<String> uriList) {
		this.uriList = uriList;

	}


	/**
	 * 标题相关
	 *
	 * @param title
	 *            用于显示标题的TextView
	 * @param titles
	 *            标题数组
	 */
	public void setTitle(TextView title, String[] titles) {
		this.title = title;
		this.titles = titles;
		if (title != null && titles != null && titles.length > 0)
			title.setText(titles[0]);// 默认显示第一张的标题
	}

	private boolean hasSetAdapter = false;
	private final int SWITCH_DURATION = 3500;
	/**
	 * 开始滚动
	 */
	public void startRoll() {
		if (!hasSetAdapter) {
			hasSetAdapter = true;
			this.setOnPageChangeListener(new MyOnPageChangeListener());
			this.setAdapter(new ViewPagerAdapter());
		}
		RollViewPager.this.setCurrentItem(currentItem);

		if (isCycle){
			/**
			 * 是否自动滚动
			 */
			handler.postDelayed(viewPagerTask, SWITCH_DURATION);
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {
		int oldPosition = 0;

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			if (title != null)
				title.setText(titles[position%titles.length]);
			if (dots != null && dots.size() > 0) {
				dots.get(position%dots.size()).setBackgroundResource(dot_focus_resId);
				dots.get(oldPosition%dots.size()).setBackgroundResource(dot_normal_resId);
			}
			oldPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			Log.i("onPageScrolled",arg0+";"+arg1+";"+arg2);
		}
	}

	/**
	 * 自带设配器,需要回调类来处理page的点击事件
	 *
	 * @author dance
	 *
	 */
	class ViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return max_count;
//			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			View view = View.inflate(context, R.layout.viewpager_item, null);
			((ViewPager) container).addView(view);
			view.setOnTouchListener(myOnTouchListener);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);

			Picasso.with(context).load(uriList.get(position%uriList.size()))
					.config(Bitmap.Config.RGB_565)
					.placeholder(R.mipmap.ic_launcher)
					.error(R.mipmap.ic_launcher)
//					.memoryPolicy()
//					.networkPolicy()
					.into(imageView);
			return view;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// 将ImageView从ViewPager移除
			((ViewPager) arg0).removeView((View) arg2);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}

	/**
	 * 处理page点击的回调接口
	 *
	 * @author dance
	 *
	 */
	interface OnPagerClickCallback {
		public abstract void onPagerClick(int position);
	}
}
