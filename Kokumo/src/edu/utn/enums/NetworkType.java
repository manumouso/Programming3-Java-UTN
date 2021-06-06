package edu.utn.enums;

//agregar todos los mensajes, con su numero de error que esten sueltos por el codigo, desp
//consumirlos de aca
public enum NetworkType {
    object(100,"Property instance is null"),ip(101,"IP instance is null"),port(102,"port instance is null");

   private int errorNumber;
   private String errorMessage;

    NetworkType(int errorNumber, String errorMessage) {
        this.errorNumber = errorNumber;
        this.errorMessage = errorMessage;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}