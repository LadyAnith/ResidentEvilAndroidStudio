package com.example.residentevil;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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
        coloricos();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(i);
                mediaPlayer.stop();mediaPlayer.reset();
            }
        });
    }

    public void animarBoton(){
        AnimatorSet animadorBoton = new AnimatorSet();
        ObjectAnimator animacionMovimiento = ObjectAnimator.ofFloat(boton, "Y", 500);
        animacionMovimiento.setDuration(4000);
        animacionMovimiento.setInterpolator(new BounceInterpolator());

        ObjectAnimator animacionColor = ObjectAnimator.ofObject(boton, "backgroundColor", new ArgbEvaluator(),
                /*Red*/0xFFFF8080, /*Blue*/0xFF8080FF)
                .setDuration(4000);
        animadorBoton.play(animacionMovimiento).with(animacionColor);
        animadorBoton.start();
    }

    public void coloricos(){
        ObjectAnimator.ofObject(boton, "backgroundColor", new ArgbEvaluator(),
                /*Red*/0xFFFF8080, /*Blue*/0xFF8080FF)
                .setDuration(2000);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}