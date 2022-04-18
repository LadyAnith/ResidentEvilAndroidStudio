package com.example.residentevil;

public class Click {
    //Coordenada x del click
    public int x;
    //Coordenada y del click
    public int y;
    //Indice del pointer
    public int index;

    /**
     * Método constructor
     * @param mIndex
     * @param mX
     * @param mY
     */
    public Click (int mIndex, int mX, int mY){
        index=mIndex;
        x=mX;
        y=mY;
    }
}
