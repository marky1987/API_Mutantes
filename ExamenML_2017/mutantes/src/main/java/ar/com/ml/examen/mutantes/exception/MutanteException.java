package ar.com.ml.examen.mutantes.exception;

public class MutanteException extends Exception implements Comparable<MutanteException>{

    private Exception e;
    protected String mensaje;
    protected String codigoError;


    public MutanteException(Exception e, String mensaje){
        super(mensaje,e);
        this.e = e;
        this.mensaje=mensaje;
    }
    public MutanteException(Exception e, String mensaje, String codigoError){
        super(mensaje,e);
        this.mensaje=mensaje;
        this.codigoError=codigoError;
    }
    public MutanteException( String mensaje, String codigoError){
        super(mensaje);
        this.mensaje=mensaje;
        this.codigoError=codigoError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    @Override
    public int compareTo(MutanteException e) {
        return 0;
    }
}
