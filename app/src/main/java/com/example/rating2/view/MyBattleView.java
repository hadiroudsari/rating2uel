package com.example.rating2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import com.example.rating2.Profile;
import com.example.rating2.Profile;
import com.example.rating2.R;

/**
 * TODO: document your custom view class.
 */
public class MyBattleView extends View {

    float bx = 500f;
    float by = 500f;
    float vx = 2f;
    float vy = 3f;
    float radious = 30f;
    long current = System.currentTimeMillis();
    volatile static boolean invalidateStatus;

    public MyBattleView(Context context) {
        super(context);
        init(null);
    }

    public MyBattleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyBattleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MyBattleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(@Nullable AttributeSet set) {
        System.out.println("inside initttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
        invalidateStatus=false;
    }

    public void shotUp() {
        System.out.println("in shot to up----------------------------------");
        this.current = System.currentTimeMillis();
        invalidateStatus=true;
//        postInvalidate();
      }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("inside draw inalidateStatus:"+invalidateStatus);
        invalidate();
        if(invalidateStatus){
            System.out.println("_____");
            long now = System.currentTimeMillis();
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            long dt = now - current;
            current = now;
            by = by + vy * dt / 1000;
            canvas.drawCircle(bx, by, radious, paint);

        }
       // canvas.drawCircle(bx, by, radious, paint);
//        invalidate();

    }
}
