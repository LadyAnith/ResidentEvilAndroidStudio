package com.example.residentevil;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button boton;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciaMusicaIntro();
        //Botón que nos llevará a la aplicación principal
        boton = (Button) findViewById(R.id.button);
        animarBoton();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Con el evento onClick, llamo a la actividad del juego
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(i);
                //Se detiene la música
                mediaPlayer.stop();mediaPlayer.reset();
            }
        });
    }

    /**
     * Método encargado de animar el botón de inicio
     */
    public void animarBoton(){
        AnimatorSet animadorBoton = new AnimatorSet();
        //Movimientos del botón hacia abajo
        ObjectAnimator animacionMovimiento = ObjectAnimator.ofFloat(boton, "Y", 500);
        //Duración de la animación
        animacionMovimiento.setDuration(4000);
        //El botón rebota al llegar a un punto
        animacionMovimiento.setInterpolator(new BounceInterpolator());

        //Animación que hacer que el botón cambie de color
        ObjectAnimator animacionColor = ObjectAnimator.ofObject(boton, "backgroundColor", new ArgbEvaluator(),
                /*Red*/0xFFFF8080, /*Blue*/0xFF8080FF)
                .setDuration(4000);
        //Al ser dos animaciones las que quiero llevar a cabo, he de llamarlas con el .play y después añadiendo el .with
        animadorBoton.play(animacionMovimiento).with(animacionColor);
        //Comienza la aplicación
        animadorBoton.start();
    }

    /**
     * Método encargado de iniciar la música al comenzar el juego
     */
    public void iniciaMusicaIntro(){
        mediaPlayer = MediaPlayer.create(this, R.raw.freefromfear);
        mediaPlayer.setVolume(0.5f,0.5f);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer.start();
    }

    /**
     * Método que detiene la música del juego
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}