package rxtest.com.rxtest.collpasingview;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

public class CollapseViewOnScroll {
    View collapsingView;
    View scrollingView;
    AnimationCollapse animationCollapse;

    public CollapseViewOnScroll(View collapsingView, RecyclerView scrollingView) {
        this.collapsingView = collapsingView;
        this.scrollingView = scrollingView;
        animationCollapse = new AnimationCollapse();

        this.scrollingView.setOnTouchListener(new View.OnTouchListener() {
            float lastY;
            float maxHeight;
            float dragDistance;
            float lastNonZeroDragDistance;

            @Override
            public boolean onTouch(View view, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    collapsingView.clearAnimation();
                    lastY = e.getRawY();
                    lastNonZeroDragDistance = 0;

                    int currentHeight = collapsingView.getHeight();
                    collapsingView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    this.maxHeight = collapsingView.getMeasuredHeight();
                    collapsingView.getLayoutParams().height = currentHeight;
                    collapsingView.requestLayout();
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    dragDistance = e.getRawY() - lastY;
                    if (dragDistance != 0) {
                        lastNonZeroDragDistance = dragDistance;
                    }

                    if (dragDistance > 0 && collapsingView.getHeight() < maxHeight) {//dragging down
                        collapsingView.getLayoutParams().height += dragDistance;
                        if (collapsingView.getLayoutParams().height > maxHeight) {
                            collapsingView.getLayoutParams().height = (int) maxHeight;
                        }
                        collapsingView.requestLayout();
                    } else if (dragDistance < 0 && collapsingView.getHeight() > 0) {
                        collapsingView.getLayoutParams().height += dragDistance;
                        if (collapsingView.getLayoutParams().height < 0) {
                            collapsingView.getLayoutParams().height = 0;
                        }
                        collapsingView.requestLayout();
                    }

                }
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    //view is neither fully infalted or collapsed
                    if (collapsingView.getLayoutParams().height > 0 && collapsingView.getLayoutParams().height < maxHeight) {
                        if (lastNonZeroDragDistance > 0) {
                            animationCollapse.expand(collapsingView);
                        } else {
                            animationCollapse.collapse(collapsingView);
                        }
                    }
                }
                lastY = e.getRawY();
                return false;
            }
        });
    }
}
