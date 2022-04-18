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
        //Pantalla en posición horizontal
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();


    }

    /**
     * Método encargado de ocultar los elementos de la interfaz
     */
    private void hideSystemUI() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            j.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            j.setOnSystemUiVisibilityChangeListener(new
                                                            View.OnSystemUiVisibilityChangeListener() {
                                                                @Override
                                                                public void onSystemUiVisibilityChange(int visibility) {
                                                                    hideSystemUI();
                                                                }
                                                            });
        }
    }

    /**
     * Método encargado de calcular el alto y ancho de la pantalla del dispositivo
     */
    public void calculaTamañoPantalla() {
        if (Build.VERSION.SDK_INT > 13) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            anchoPantalla = size.x;
            altoPantalla = size.y;
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            anchoPantalla = display.getWidth();
            altoPantalla = display.getHeight();
        }
    }
}