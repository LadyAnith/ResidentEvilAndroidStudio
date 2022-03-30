package com.example.residentevil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    Juego j;
    public static int anchoPantalla = 0;
    public static int altoPantalla = 0;
    Fondo fondo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        calculaTamañoPantalla();
        j = new Juego(this);
        setContentView(j);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();


    }

    // This snippet hides the system bars.
    private void hideSystemUI() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            //A partir de kitkat
            j.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            //cuando se presiona volumen, por ej, se cambia la visibilidad, hay que volver
            //a ocultar
            j.setOnSystemUiVisibilityChangeListener(new
                                                            View.OnSystemUiVisibilityChangeListener() {
                                                                @Override
                                                                public void onSystemUiVisibilityChange(int visibility) {
                                                                    hideSystemUI();
                                                                }
                                                            });
        }
    }

    public void calculaTamañoPantalla() {
        if (Build.VERSION.SDK_INT > 13) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            anchoPantalla = size.x;
            altoPantalla = size.y;
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            anchoPantalla = display.getWidth(); // deprecated
            altoPantalla = display.getHeight(); // deprecated
        }
    }
}