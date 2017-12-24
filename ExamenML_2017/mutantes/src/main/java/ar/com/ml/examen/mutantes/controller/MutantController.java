package ar.com.ml.examen.mutantes.controller;

import ar.com.ml.examen.mutantes.dao.MutanteDao;
import ar.com.ml.examen.mutantes.exception.MutanteException;
import ar.com.ml.examen.mutantes.model.Mutante;
import ar.com.ml.examen.mutantes.model.Respuesta;
import ar.com.ml.examen.mutantes.util.Validadores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;

@RestController
public class MutantController {

    @Autowired
    private MutanteDao mutanteDao;


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
            if (retorno.getStatusCode().value() == 200) {
                for (String adnEntrada : entrada) {
                    dnaFormateado += adnEntrada;
                }
                Mutante mutante = new Mutante( dnaFormateado, true );
                mutanteDao.save( mutante );
            } else {
                for (String adnEntrada : entrada) {
                    dnaFormateado += adnEntrada;
                }
                Mutante mutante = new Mutante( dnaFormateado, false );
                mutanteDao.save( mutante );
            }

        return retorno;
    }

    @RequestMapping(value = "/api/v1/stats/", method = RequestMethod.GET, produces = "application/json")
    private @ResponseBody
    String estadisticas() {
        long cantidadDeMutantes = mutanteDao.contadorDeMutantes();
        long cantidadDeNoMutantes = mutanteDao.contadorDeNoMutantes();
        double ratio = cantidadDeMutantes/cantidadDeNoMutantes;
        Respuesta resp = new Respuesta();
        resp.setContadorMutantes( cantidadDeMutantes );
        resp.setContadorNoMutantes( cantidadDeNoMutantes );
        resp.setRatio( ratio );
        return resp.toString();
    }

}
