package com.example.residentevil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controles {
    private static final String TAG = "PULSADO";
    private Context context;
    private Map<String, BotonControl> botones = new HashMap();

    public Controles(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        BotonControl button;
        int x, y, margen = 20;

        x = margen;
        y = GameActivity.altoPantalla / 10 * 4;
        button = addBoton("left", x, y, R.drawable.flecha_izda);



        x = margen + button.getImagen().getWidth();
        y = button.getCoordenadaY();
        button = addBoton("right", x, y, R.drawable.flecha_dcha);


        x = GameActivity.anchoPantalla - button.getImagen().getWidth();
        y = GameActivity.altoPantalla / 10 * 3;
        button = addBoton("up", x, y, R.drawable.flecha_up);


        x = button.getCoordenadaX();
        y = button.getCoordenadaY() + button.getImagen().getHeight();
        button = addBoton("shoot", x, y, R.drawable.disparo);

    }

    private BotonControl addBoton(String key, int x, int y, int resource) {
        BotonControl boton = new BotonControl(context, x, y, resource);
        botones.put(key, boton);
        return boton;
    }

    public void renderizarBotones(Canvas canvas, Paint paint) {
        paint.setAlpha(200);
        for (BotonControl boton : getBotones().values()) {
            boton.dibujar(canvas, paint);

        }
    }

    //se comprueba si se ha pulsado con el pointer i
    public void comprobarSiEsPulsado(int x, int y, BotonControl button){
        button.getCoordenadaY();
        button.getCoordenadaX();
        int n = button.getImagen().getWidth();

        if(x>button.getCoordenadaX() && x<button.getCoordenadaX()+ button.getImagen().getWidth() &&
                y>button.getCoordenadaY() && y<button.getCoordenadaY()+ button.getImagen().getHeight()){
            button.setPulsado(true);
            boolean b = button.isPulsado();
        }
    }

    public void comprueba_soltado(ArrayList<Click> lista,BotonControl button){
        boolean b = button.isPulsado();
        int n = button.getImagen().getWidth();

        boolean aux=false;
        for(Click c:lista){
            if(c.x>button.getCoordenadaX() && c.x<button.getCoordenadaX()+ button.getImagen().getWidth() && c.y>button.getCoordenadaY() && c.y<button.getCoordenadaY()+ button.getImagen().getHeight()) {
                aux = true;
            }
        }
        if(!aux){
            button.setPulsado(false);
        }
    }


    public Map<String, BotonControl> getBotones() {
        return botones;
    }
}
