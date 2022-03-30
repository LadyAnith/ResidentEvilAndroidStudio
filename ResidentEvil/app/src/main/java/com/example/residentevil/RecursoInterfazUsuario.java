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

    public RecursoInterfazUsuario(Context context, int x, int y, int recurso) {
        super(context);
        init(x, y, recurso);
    }

    public void init(int x, int y, int recurso) {
        coordenadaX = x;
        coordenadaY = y;
        recurso = recurso;
        imagen = BitmapFactory.decodeResource(getResources(), recurso);
        imagenEscalada = escalarImagenPantalla(imagen);
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

    public void dibujar(Canvas c, Paint p) {
        c.drawBitmap(imagen, coordenadaX, coordenadaY, p);
    }

    public Bitmap escalarImagenPantalla(Bitmap imagenOrigen) {
        return Bitmap.createScaledBitmap(imagenOrigen, GameActivity.altoPantalla, GameActivity.anchoPantalla, true);
    }

}
