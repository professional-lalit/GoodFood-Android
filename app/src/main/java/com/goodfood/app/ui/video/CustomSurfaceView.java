package com.goodfood.app.ui.video;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CustomSurfaceView extends SurfaceView implements Runnable {

    private Thread thread;
    volatile boolean running;


    public CustomSurfaceView(Context context) {
        super(context);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*holder = this.getHolder();// Access to holder
        holder.addCallback(this);
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);*/
    }


    @Override
    public void run() {
        while (running) {
            draw();
        }
    }

    private void draw() {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.argb(255, 255, 0, 0));
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        running = false;

        while (running) {
            if (!getHolder().getSurface().isValid())
                continue;

            Canvas c = getHolder().lockCanvas();
            c.drawARGB(0, 0, 0, 0);
            Paint redPaint = new Paint();
            redPaint.setColor(Color.RED);
            c.drawCircle(100, 100, 30, redPaint);
            getHolder().unlockCanvasAndPost(c);
        }
    }
}