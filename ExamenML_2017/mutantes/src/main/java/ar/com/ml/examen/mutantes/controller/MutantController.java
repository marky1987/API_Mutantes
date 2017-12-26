package ar.com.ml.examen.mutantes.controller;

import ar.com.ml.examen.mutantes.dao.MutanteDao;
import ar.com.ml.examen.mutantes.exception.MutanteException;
import ar.com.ml.examen.mutantes.model.Mutante;
import ar.com.ml.examen.mutantes.model.Respuesta;
import ar.com.ml.examen.mutantes.util.Validadores;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;

@RestController
public class MutantController {

    private static final int OK = 200;
    private static final int FORBIDDEN = 403;
    private static final Logger LOG = LogManager.getLogger( MutantController.class );

    @Autowired
    private MutanteDao mutanteDao;


    /**
     * Retorna 200 Ok si isMutant devuelve true (Que significa que se inserto en la base y es mutante),
     * o 403 Forbidden si isMutant devuelve false (Que significa que se inserto en la base y no es mutante)
     *
     * @param dna
     * @return ResponseEntity
     * @throws MutanteException
     */
    @RequestMapping(value = "/api/v1/mutant/", method = RequestMethod.POST, consumes = "application/json")
    private ResponseEntity verificarMutante(@RequestBody Map<String, Object> dna) throws MutanteException {
        Validadores validadores = new Validadores();
        String[] entrada = new String[0];
        ResponseEntity retorno;
        String valor;
        Iterator it = dna.keySet().iterator();
        while (it.hasNext()) {
            valor = it.next().toString();
            String[] aux = dna.get( valor ).toString().replaceAll( "\\[", "" ).replaceAll( "\\]", "" ).replace( " ", "" ).split( "," );
            entrada = aux;
        }
        retorno = validadores.respuesta( entrada );
        String dnaFormateado = "";
        if (retorno.getStatusCode().value() == OK) {
            for (String adnEntrada : entrada) {
                dnaFormateado += adnEntrada;
            }
            Mutante mutante = new Mutante( dnaFormateado, true );
            mutanteDao.save( mutante );
        } else if (retorno.getStatusCode().value() == FORBIDDEN) {
            for (String adnEntrada : entrada) {
                dnaFormateado += adnEntrada;
            }
            Mutante mutante = new Mutante( dnaFormateado, false );
            mutanteDao.save( mutante );
        } else
            retorno = new ResponseEntity( '{' + "\"Error\": \"La matriz no es cuadrada o contiene caracteres invalidos\"" + '}', HttpStatus.BAD_REQUEST );

        return retorno;
    }

    /**
     * Retorna las estadisticas de la cantidad de Mutantes y no Mutantes insertados en la base, y en relacion a ellos
     * se saca una proporcion de los mismos
     *
     * @return
     */
    @RequestMapping(value = "/api/v1/stats/", method = RequestMethod.GET, produces = "application/json")
    private @ResponseBody
    String estadisticas() {
        long cantidadDeMutantes = mutanteDao.contadorDeMutantes();
        long cantidadDeNoMutantes = mutanteDao.contadorDeNoMutantes();
        double ratio = cantidadDeMutantes / cantidadDeNoMutantes;
        Respuesta resp = new Respuesta();
        resp.setContadorMutantes( cantidadDeMutantes );
        resp.setContadorNoMutantes( cantidadDeNoMutantes );
        resp.setRatio( ratio );
        return resp.toString();
    }

}
