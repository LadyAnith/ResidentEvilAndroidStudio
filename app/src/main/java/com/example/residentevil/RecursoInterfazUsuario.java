package com.example.residentevil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

abstract class RecursoInterfazUsuario extends View {
    protected int coordenadaX, coordenadaY;
    protected Bitmap imagen;
    protected Bitmap imagenEscalada;
    protected int recurso;

    public static final int DIRECCION_ABAJO = 0;
    public static final int DIRECCION_IZQUIERDA = 1;
    public static final int DIRECCION_DERECHA = 2;
    public static final int DIRECCION_ARRIBA = 3;

    protected int direccion = DIRECCION_IZQUIERDA;

    protected Colision colision;

    public RecursoInterfazUsuario(Context context, int x, int y, int recurso) {
        super(context);
        init(x, y, recurso);
    }

    public RecursoInterfazUsuario(Context context, int x, int y) {
        super(context);
        init(x, y, null);
    }


    public void init(int x, int y, Integer recurso) {
        this.coordenadaX = x;
        this.coordenadaY = y;
        this.colision = new Colision(0,0,0,0);

        if (recurso != null){
            this.recurso = recurso;
            this.imagen = BitmapFactory.decodeResource(getResources(), recurso);
            this.imagenEscalada = escalarImagenPantalla(imagen);
        }
    }

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Bitmap getImagenEscalada() {
        return imagenEscalada;
    }

    public void setImagenEscalada(Bitmap imagenEscalada) {
        this.imagenEscalada = imagenEscalada;
    }

    public int getRecurso() {
        return recurso;
    }

    public void setRecurso(int recurso) {
        this.recurso = recurso;
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public void dibujar(Canvas c, Paint p) {
        c.drawBitmap(imagen, coordenadaX, coordenadaY, p);
    }

    public Bitmap escalarImagenPantalla(Bitmap imagenOrigen) {
        return Bitmap.createScaledBitmap(imagenOrigen, GameActivity.altoPantalla, GameActivity.anchoPantalla, true);
    }

    public Colision getColision() {
        return colision;
    }

    public void setColision(Colision colision) {
        this.colision = colision;
    }
}
