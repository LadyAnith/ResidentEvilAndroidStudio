package com.example.residentevil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;

public class Fondo extends RecursoInterfazUsuario {

    Display display;
    Rect screen;

    public Fondo(Context context) {
        super(context, 0, 0, R.drawable.fondo2);
        init();

    }

    private void init() {
        int ancho, alto;
        Point size = new Point();
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        ancho = size.x;
        alto = size.y;
        screen = new Rect(0, 0, ancho, alto);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(imagenEscalada, null, screen, null);
    }

    @Override
    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public Rect getScreen() {
        return screen;
    }

    public void setScreen(Rect screen) {
        this.screen = screen;
    }
}
