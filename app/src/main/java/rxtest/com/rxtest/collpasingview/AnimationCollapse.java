package rxtest.com.rxtest.collpasingview;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnimationCollapse {
    CollapseExpandAnimation anim;

    public void expand(View view) {
        animate(view, true);
    }

    public void collapse(View view) {
        animate(view, false);
    }

    private void animate(View view, boolean expand) {
        anim = new CollapseExpandAnimation(view, expand);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    private class CollapseExpandAnimation extends Animation {
        private final int targetHeight;
        private final View view;
        private final boolean expand;
        private int startHeight;

        private CollapseExpandAnimation(View view, boolean expand) {
            this.view = view;
            this.expand = expand;
            this.startHeight = view.getHeight();

            int currentHeight = view.getHeight();
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            this.targetHeight = view.getMeasuredHeight();

            view.getLayoutParams().height = currentHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight;
            if (expand) {
                newHeight = (int) (startHeight + (interpolatedTime * (targetHeight - startHeight)));
            } else {
                newHeight = (int) (startHeight - (interpolatedTime * startHeight));
            }
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
