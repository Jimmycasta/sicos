package com.sicos.utilities;

import java.time.LocalDate;
import java.time.Month;

public class Fecha {

    public static String getMes(LocalDate fecha) {

        String mes = null;
        Month mesIngles = fecha.getMonth();

        switch (mesIngles) {
            case JANUARY:
                mes = "Enero";
                break;
            case FEBRUARY:
                mes = "Febrero";
                break;
            case MARCH:
                mes = "Marzo";
                break;
            case APRIL:
                mes = "Abril";
                break;
            case MAY:
                mes = "Mayo";
                break;
            case JUNE:
                mes = "Junio";
                break;
            case JULY:
                mes = "Julio";
                break;
            case AUGUST:
                mes = "Agosto";
                break;
            case SEPTEMBER:
                mes = "Septiembre";
                break;
            case OCTOBER:
                mes = "Octubre";
                break;
            case NOVEMBER:
                mes = "Noviembre";
                break;
            case DECEMBER:
                mes = "Diciembre";
                break;
        }
        return mes;
    }
}
