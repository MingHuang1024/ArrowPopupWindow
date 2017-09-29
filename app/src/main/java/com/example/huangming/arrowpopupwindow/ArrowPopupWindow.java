package com.example.huangming.arrowpopupwindow;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * 带箭头的选择框
 *
 * 使用场合：点击某个按钮，弹出此选择框，箭头指向被点击按钮，并从框中选择数据
 *
 * 使用方法：请阅读同目录下的readme文件
 *
 * @author Huangming
 * @date 2016/6/4
 * @modified [describe][editor][date]
 */

public class ArrowPopupWindow {
    private PopupWindow mWindow;
    private Drawable mBackground = null;
    private WindowManager mWindowManager;

    /** 弹出框根容器 */
    private View mRootView;
    private ImageView mArrowUp;
    private ImageView mArrowDown;
    private LayoutInflater mInflater;
    private ViewGroup mTrack;
    private ScrollView mScroller;
    private OnPopuItemClickListener mItemClickListener;

    private int mInsertPos;
    // private int rootWidth = 0;

    public ArrowPopupWindow(Context context) {
        mWindow = new PopupWindow(context);
        mWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mWindow.dismiss();

                    return true;
                }

                return false;
            }
        });

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setRootViewId(R.layout.arrow_popup_vertical);
    }

    private void setContentView(View root) {
        mRootView = root;
        mWindow.setContentView(root);
    }

    private void dismiss() {
        mWindow.dismiss();
    }


    private void setRootViewId(int id) {
        mRootView = mInflater.inflate(id, null);
        mTrack = mRootView.findViewById(R.id.tracks);

        mArrowDown = mRootView.findViewById(R.id.arrow_down);
        mArrowUp =  mRootView.findViewById(R.id.arrow_up);

        mScroller = mRootView.findViewById(R.id.scroller);

        mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        setContentView(mRootView);
    }

    /**
     * 设置回调监听器
     *
     * @param
     * @return
     * @author Huangming
     * @date 2016/6/4
     * @modified [describe][editor][date]
     */
    public void setOnPopuItemClickListener(OnPopuItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * 添加数据项
     *
     * @param
     * @return
     * @author Huangming
     * @date 2016/6/4
     * @modified [describe][editor][date]
     */
    public void addPopuItem(final PopuItem action) {
        String title = action.title;
        Drawable icon = action.icon;

        View container;

        container = mInflater.inflate(R.layout.arrow_action_item_vertical, null);

        ImageView img = container.findViewById(R.id.iv_icon);
        TextView text = container.findViewById(R.id.tv_title);
        TextView divider = container.findViewById(R.id.divider);
        LinearLayout ll = container.findViewById(R.id.layout);

        if (mInsertPos == 0) {
            divider.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
        }

        if (icon != null) {
            img.setImageDrawable(icon);
        } else {
            img.setVisibility(View.GONE);
        }

        if (title != null) {
            text.setText(title);
        } else {
            text.setVisibility(View.GONE);
        }

        ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(action);
                }
                dismiss();
            }
        });

        container.setFocusable(true);
        container.setClickable(true);

        mTrack.addView(container, mInsertPos);

        mInsertPos++;
    }

    /**
     * 显示前准备
     *
     * @param
     * @return
     * @author Huangming
     * @date 2016/6/4
     * @modified [describe][editor][date]
     */
    private void preShow() {
        if (mRootView == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }
        if (mBackground == null) {
            mWindow.setBackgroundDrawable(new BitmapDrawable());
        } else {
            mWindow.setBackgroundDrawable(mBackground);
        }

        mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setTouchable(true);
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);

        mWindow.setContentView(mRootView);
    }

    /**
     * 显示弹出框
     *
     * @param
     * @return
     * @author Huangming
     * @date 2016/6/4
     * @modified [describe][editor][date]
     */
    public void show(View anchor) {
        preShow();

        int xOffset;  // X轴偏移量
        int yOffset; // y轴偏移量
        int arrowOffset; // 箭头的偏移量

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

        mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int rootWidth = mRootView.getMeasuredWidth();//弹出框的宽度
        if (rootWidth > 0.8 * screenWidth) {
            rootWidth = (int) (0.8 * screenWidth);
            LayoutParams l = mScroller.getLayoutParams();
            l.width = rootWidth;
            // 重新测量一下弹出框的尺寸, 因为执行上一句后弹出框内的内容可能会换行
            mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        int rootHeight = mRootView.getMeasuredHeight(); //弹出框的高度

        //计算X轴偏移量
        if ((anchorRect.centerX() + rootWidth / 2.0) > screenWidth) {
            xOffset = anchorRect.centerX() - rootWidth / 2 - (rootWidth / 2 - (screenWidth - anchorRect.centerX())) - 6;
        } else if (anchorRect.centerX() - rootWidth / 2.0 < 0) {
            xOffset = 6;
        } else {
            xOffset = anchorRect.centerX() - (rootWidth / 2);
        }
        arrowOffset = anchorRect.centerX() - xOffset;

        int dyTop = anchorRect.top; //屏幕顶部到被点击按钮的上边缘的距离
        int dyBottom = screenHeight - anchorRect.bottom; //被点击按钮下边缘到屏幕底部的距离

        boolean onTop = (dyTop > dyBottom); //弹出框是否在被点击按钮的上方显示

        //计算Y轴偏移量
        if (onTop) {
            // 弹出框在被点按钮上方
            if (rootHeight > 0.7 * dyTop) {
                // 被点按钮上方容纳不下弹出框（高度不够），此时要重设弹出框的高度
                LayoutParams l = mScroller.getLayoutParams();
                l.height = (int) (0.7 * dyTop);
                // 重新测量一下弹出框的高度，因为重新给mScroller的高度赋值了
                mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                rootHeight = mRootView.getMeasuredHeight();
                yOffset = anchorRect.top - rootHeight ;
            } else {
                yOffset = anchorRect.top - rootHeight ;
            }
        } else {
            // 弹出框在被点按钮下方
            yOffset = anchorRect.bottom;

            if (rootHeight > 0.7 * dyBottom) {
                // 被点按钮下方容纳不下弹出框（高度不够），此时要重设弹出框的高度
                LayoutParams l = mScroller.getLayoutParams();
                l.height = (int) (0.7 * dyBottom);
            }
        }

        showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowOffset);

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xOffset, yOffset);
    }

    /**
     * 显示箭头
     *
     * @param
     * @return
     * @author Huangming
     * @date 2016/6/4
     * @modified [describe][editor][date]
     */
    private void showArrow(int whichArrow, int arrowOffset) {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

        final int arrowWidth = mArrowUp.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();

        param.leftMargin = arrowOffset - arrowWidth / 2;

        hideArrow.setVisibility(View.INVISIBLE);
    }

    /**
     * 回调监听器
     *
     * @param
     * @author Huangming
     * @return
     * @date 2016/6/4
     * @modified [describe][editor][date]
     */
    public interface OnPopuItemClickListener {
        /**
         * 待选择项点击事件
         *
         * @param item 被点击的item
         * @return
         * @author Huangming
         * @date 2016/6/4
         * @modified [describe][editor][date]
         */
        void onItemClick(PopuItem item);
    }

    /**
     * 选择框数据项
     *
     * @author Huangming
     * @date 2016/6/4
     * @modified [describe][editor][date]
     */
    public static class PopuItem {
        /** 要显示的图标，不需要图标可以传null */
        public Drawable icon;

        /** 要显示的内容 */
        public String title;

        /** 业务数据 */
        public Object value;

        public PopuItem(String title, Drawable icon, Object value) {
            this.title = title;
            this.icon = icon;
            this.value = value;
        }
    }
}