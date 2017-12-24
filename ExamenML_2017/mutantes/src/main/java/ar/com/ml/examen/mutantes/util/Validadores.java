package ar.com.ml.examen.mutantes.util;

import ar.com.ml.examen.mutantes.exception.MutanteException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validadores {

    private static final Logger LOG = LogManager.getLogger( Validadores.class );

    /**
     * Metodo que retorna la respuesta del proceso isMutant
     *
     * @param dna
     * @return ResponseEntity
     * @throws MutanteException
     */
    public ResponseEntity respuesta( String[] dna) throws MutanteException {
        ResponseEntity response;
        String[] matrizFormateada;

        matrizFormateada = convertir( dna );

        if (isMutant( matrizFormateada )) {
            response = new ResponseEntity( HttpStatus.OK );
        } else {
            response = new ResponseEntity( HttpStatus.FORBIDDEN );
        }

        return response;
    }

    /**
     * Se procesa el ADN ingresado por parametros, el resultado sera positivo (true) siempre y cuando haya mas de 1 coincidencia
     * de anera oblicua, horizontal o vertical
     *
     * @param dna
     * @return ResponseEntity
     * @throws MutanteException
     */
    private boolean isMutant(String[] dna) throws MutanteException {
        int coincidencia;
        boolean respuesta;
        Validadores validadores = new Validadores();
        if (validadores.validarMatriz( dna )) {
            coincidencia = validadores.init( dna );
            LOG.info( "Cantidad de coincidencias encontradas: " + coincidencia );
            if (coincidencia < 2) {
                respuesta = false;
            } else {
                respuesta = true;
            }
        } else {
            throw new MutanteException( "[ERROR DEFINICION MATRIZ]", "La Matriz ingresada es incorrecta" );
        }

        return respuesta;
    }

    /**
     * Convertimos siempre el array que se ingresa por parametros a Mayuscula, en caso que se ingrese minusculas se formatea,
     * caso contrario seguira igual
     *
     * @param args
     * @return String[]
     */
    private String[] convertir(String[] args) {
        String[] matrizFormateada = new String[args.length];
        try {
            for (int i = 0; i < args.length; i++) {
                matrizFormateada[i] = args[i].toUpperCase();
            }
        } catch (Exception e) {
            LOG.error( e.getMessage(), e.getCause() );
        }
        return matrizFormateada;
    }

    private int init(String[] dna) {
        LOG.info( "[INICIO] - Comienza el proceso de seleccion de mutantes" );
        int coincidencias = 0;
        LOG.info( "Coincidencias:" );
        try {

            // Recorre la matriz de izquierda a dererecha
            if (reccorreMatrizHorizontal( dna )) {
                coincidencias++;
            }

            // Recorre la matriz de arriba hacia abajo
            if (recorreMatrizVertical( dna )) {
                coincidencias++;
            }

            //recorre la diagonal principal
            if (recorrerMatrizDiagonalPrincipal( dna )) {
                coincidencias++;
            }

            // Recorre la diagonal secundaria,superior e inferior realizando la inversa de la matriz pasada por parametro
            //En el caso de encontrar coincidencias se autoincrementa dentro del metodo y devuelve un valor para luego
            // ir autoincrementando a la variable coincidencias.

            coincidencias += recorrerMatrizDiagonalSecundariaEInversa( dna );

            //Recorre la matriz obteniendo la diagonal superior para saber si hay coincidencias
            if (recorrerMatrizDiagonalSuperior( dna )) {
                coincidencias++;
            }

            //recorre la matriz obteniendo la diagonal inferior para saber si hay coincidencias
            if (recorrerMatrizDiagonalInferior( dna )) {
                coincidencias++;
            }

            LOG.info( "[FIN] - Proceso de seleccion de mutantes" );
        } catch (MutanteException e) {
            LOG.error( e.getCodigoError() + " " + e.getMessage() + ", " + e.getCause() );
        }
        return coincidencias;
    }

    /**
     * Recorriendo la diagonal principal arriba hacia abajo para controlar si hay coincidencias
     *
     * @param dnaA
     * @return boolean
     */
    private boolean recorrerMatrizDiagonalPrincipal(String[] dnaA) throws MutanteException {
        int tope = dnaA.length;
        String[] matrizDestino = new String[tope];

        try {
            for (int i = 0; i < dnaA.length; i++) {
                matrizDestino[i] = String.valueOf( dnaA[i].charAt( i ) );
            }
        } catch (Exception e) {
            LOG.error( e.getMessage(), e.getCause() );
        }

        return reccorreMatrizHorizontal( matrizDestino );
    }

    /**
     * Recorrido le la diagonal Superior
     *
     * @param dna
     * @return boolean
     * @throws MutanteException
     */
    private boolean recorrerMatrizDiagonalSuperior(String[] dna) throws MutanteException {

        int contadorDiagonalSuperior = 0;
        String tamanioVector = "";
        String[] diagonalSuperior = new String[dna.length];

        try {
            //primera parte diagonal superior
            for (int i = 0; i < dna.length; i++) {
                for (int j = 0; j <= i; j++) {
                    tamanioVector += dna[i - j].charAt( j );
                }
                if (tamanioVector.length() >= 4 && tamanioVector.length() < dna.length) {
                    diagonalSuperior[contadorDiagonalSuperior] = tamanioVector;
                    contadorDiagonalSuperior++;
                    tamanioVector = "";
                } else {
                    tamanioVector = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] arrayDiagonalSuperior = new String[contadorDiagonalSuperior];
        for (int i = 0; i < contadorDiagonalSuperior; i++) {
            arrayDiagonalSuperior[i] = diagonalSuperior[i];
        }
        return reccorreMatrizHorizontal( arrayDiagonalSuperior );
    }

    /**
     * recorrido de la diagonal inferior
     *
     * @param dna
     * @return boolean
     * @throws MutanteException
     */
    private boolean recorrerMatrizDiagonalInferior(String[] dna) throws MutanteException {

        int contadorDiagonalInferior = 0;
        String tamanioVectorB = "";
        String[] diagonalInferior = new String[dna.length];

        try {
            //segunda parte diagonal inferior
            for (int i = 0; i < dna.length; i++) {
                for (int j = 0; j < dna.length - i - 1; j++) {
                    tamanioVectorB += dna[dna.length - j - 1].charAt( j + i + 1 );
                }
                if (tamanioVectorB.length() >= 4) {
                    diagonalInferior[contadorDiagonalInferior] = tamanioVectorB;
                    contadorDiagonalInferior++;
                    tamanioVectorB = "";
                } else {
                    tamanioVectorB = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] arrayDiagonalInferior = new String[contadorDiagonalInferior];
        for (int i = 0; i < contadorDiagonalInferior; i++) {
            arrayDiagonalInferior[i] = diagonalInferior[i];
        }
        return reccorreMatrizHorizontal( arrayDiagonalInferior );
    }

    /**
     * Se recorre la matriz Horizontalmente tratando de encontrar los requisitos de los mutantes
     *
     * @param dna
     * @return boolean
     */
    private boolean reccorreMatrizHorizontal(String[] dna) throws MutanteException {

        /*
         * Se declaran las variables de trabajo
         */

        int contadorDeIgualdad = 1;
        int cantidadDeIgualdad = 0;
        String variableAux = "";
        String coincidencia = "";

        // Recorremos la matriz
        try {
            for (int i = 0; i < dna.length; i++) {
                for (int m = 0; m < dna[i].length(); m++) {
                    /*
                     * Al entrar por primera vez al bucle guardamos el valor del caracter en una variable auxiliar
                     * para luego hacer las validaciones pertinentes.
                     */
                    if (variableAux.isEmpty()) {
                        variableAux = String.valueOf( dna[i].charAt( m ) );
                    } else {
                        /*
                         * Se realiza la validacion preguntando si el caracter de la cadena de adn coincide con el registrado con anterioridad
                         * caso positivo se ira acumulando en una variable contadora hasta llegar al pedido de coincidencias
                         * que es 4.
                         */
                        if (String.valueOf( dna[i].charAt( m ) ).equals( variableAux )) {
                            coincidencia += dna[i].charAt( m );
                            variableAux = String.valueOf( dna[i].charAt( m ) );
                            contadorDeIgualdad++;
                            if (contadorDeIgualdad == 4) {
                                LOG.info( variableAux + coincidencia );
                                cantidadDeIgualdad++;
                            }
                            if (cantidadDeIgualdad >= 1) {
                                return true;
                            }
                        } else {
                            variableAux = String.valueOf( dna[i].charAt( m ) );
                            contadorDeIgualdad = 1;
                            coincidencia = "";
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error( e.getMessage(), e.getCause() );
        }

        return false;
    }

    /**
     * Recorre la Matriz de manera Vertical.
     *
     * @param dna
     * @return boolean
     */
    private boolean recorreMatrizVertical(String[] dna) throws MutanteException {

        //declaramos las variables de trabajo

        int tope = dna.length;
        String caracteres = "";
        String[] matrizDestino = new String[tope];
        try {
            for (int i = 0; i < tope; i++) {
                int topeB = dna[i].length();
                for (int j = 0; j < topeB && i < topeB; j++) {
                    caracteres += dna[j].charAt( i );
                }
                matrizDestino[i] = "";
                matrizDestino[i] += caracteres;
                caracteres = "";
            }
        } catch (Exception e) {
            LOG.error( e.getMessage(), e.getCause() );
        }
        return reccorreMatrizHorizontal( matrizDestino );
    }

    /**
     * Se invierte la matriz, de manera espejo para recorrer luego a traves de la diagonal secundaria
     * y corroborar si hay coincidencias.
     *
     * @param dna
     * @return int
     */
    private int recorrerMatrizDiagonalSecundariaEInversa(String[] dna) throws MutanteException {
        int tope = dna.length;
        String caracteres = "";
        String[] matrizDestino = new String[tope];
        int contador = 0;

        try {
            for (int i = 0; i < dna.length; i++) {
                for (int j = dna[0].length() - 1; j >= 0; j--) {
                    caracteres += dna[i].charAt( j );
                }
                matrizDestino[i] = "";
                matrizDestino[i] += caracteres;
                caracteres = "";
            }

            if (recorrerMatrizDiagonalSuperior( matrizDestino )) {
                contador++;
            }
            if (recorrerMatrizDiagonalInferior( matrizDestino )) {
                contador++;
            }
            if (recorrerMatrizDiagonalPrincipal( matrizDestino )) {
                contador++;
            }
        } catch (Exception e) {
            LOG.error( e.getMessage(), e.getCause() );
        }

        return contador;
    }

    /**
     * Proceso que valida que la matriz ingresada sea cuadrada si es asi verifica que solamente
     * contenga las letras "A,T,C,G", caso contrario se indica al usuario el tipo
     * de matriz que informa y si contiene alguna letra que no es la de la lista lo informa
     *
     * @param arg
     * @return boolean
     */
    private boolean validarMatriz(String[] arg) throws MutanteException {
        boolean respuesta = false;
        int tamanioMatriz = arg.length;
        for (int i = 0; i < tamanioMatriz; i++) {
            int tamanioElementoMatriz = arg[i].length();
            for (int j = 0; j < tamanioElementoMatriz; j++) {
                if (tamanioElementoMatriz != tamanioMatriz) {
                    LOG.error( "La matriz no es cuadrada, la misma es de: " + tamanioMatriz + " x " + tamanioElementoMatriz + "." );
                    throw new MutanteException( "Error Formato Matriz. La matriz no es cuadrada, la misma es de: " + tamanioMatriz + " x " + tamanioElementoMatriz + ".", null );
                }
                Pattern pat = Pattern.compile( "[ATCG]+" );
                Matcher mat = pat.matcher( String.valueOf( arg[i].charAt( j ) ) );
                if (mat.matches()) {
                    respuesta = true;
                } else {
                    LOG.error( "Contiene caracteres Invalidos: " + String.valueOf( arg[i].charAt( j ) ) + ", letras esperadas: " +
                            "A,T,C,G" );
                    throw new MutanteException( "Contiene caracteres Invalidos: " + String.valueOf( arg[i].charAt( j ) ) + " letras esperadas: " +
                            "A,T,C,G", null );
                }
            }
        }
        return respuesta;
    }

}
