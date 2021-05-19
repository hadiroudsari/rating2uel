package com.example.rating2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.example.rating2.BattleActivity;
import com.example.rating2.Fdialog;

/**
 * TODO: document your custom view class.
 */
public class MyBattleView extends View {

    float bx = 0f;
    float by = 0f;
    float by2 = 0f;
    float vx = 2f;
    float vy = -3000f;
    float vy2 = 3000f;
    float radious = 15f;
    static boolean StartFromDown;
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
        invalidateStatus = false;
    }


    public void shotUp() {
        System.out.println("in shot to up----------------------------------");
        this.current = System.currentTimeMillis();
        StartFromDown = true;
        invalidateStatus = true;


    }

    public void shotDown() {
        System.out.println("in shot to up----------------------------------");
        this.current = System.currentTimeMillis();
        StartFromDown = false;
        invalidateStatus = true;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        this.current = System.currentTimeMillis();
        System.out.println("inside draw inalidateStatus:" + invalidateStatus);
        invalidate();
        if (!invalidateStatus) {
            System.out.println("__no___" + this.getWidth() + " -----" + getHeight());
            this.bx = getWidth() / 2;
            this.by = getHeight()-300;
            this.by2 = 300f;
        }
        if (invalidateStatus && StartFromDown) {
            System.out.println("__yes___" + this.bx + " -----" + this.by);
            long now = System.currentTimeMillis();
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            long dt = now - current;
            current = now;
            by = by + vy * dt / 1000;
            canvas.drawCircle(bx, by, radious, paint);
            if (by < 300f){
                invalidateStatus=false;
                openDialog("you win the duel");
            }
        } else if (invalidateStatus && !StartFromDown) {
            System.out.println("__yes___" + this.bx + " -----" + this.by);
            long now = System.currentTimeMillis();
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            long dt = now - current;
            current = now;
            by2 = by2 + vy2 * dt / 1000;
            canvas.drawCircle(bx, by2, radious, paint);
            if(by2>2000f){
                invalidateStatus=false;
                openDialog("you lost the duel");
            }
        }
    }

    public void openDialog(String message) {
        System.out.println("in dialog");
        Fdialog fdialog=new Fdialog();
        Bundle bundle = new Bundle();
        bundle.putString("TEXT",message);
        fdialog.setArguments(bundle);
        fdialog.show(((BattleActivity)getContext()).getSupportFragmentManager(), "your state");
    }
}
