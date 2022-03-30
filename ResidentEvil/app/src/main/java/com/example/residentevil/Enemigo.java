package com.example.residentevil;

import android.content.Context;

public class Enemigo extends RecursoInterfazUsuario{
    public final int NEMESIS=0; //enemigo que sigue a la nave
    public final int ZOMBIE=1; //enemigo que se mueve aleatoriamente
    public final float VELOCIDAD_NEMESIS=5;
    public final float VELOCIDAD_ZOMBIE=2;
    public float velocidad;

    public int tipo_enemigo;

    private Juego juego;



    public Enemigo(Context context, int x, int y, int recurso) {
        super(context, x, y, recurso);
    }
}
