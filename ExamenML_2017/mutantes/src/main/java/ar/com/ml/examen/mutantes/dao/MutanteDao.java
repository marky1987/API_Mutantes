package ar.com.ml.examen.mutantes.dao;

import ar.com.ml.examen.mutantes.model.Mutante;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Se mapea metodos propios para la devolucion de resultados especificos consultando a la
 * Base de Datos para
 * luego informar en el controlador usando el servicio /stats/
 */
public interface MutanteDao extends CrudRepository<Mutante, Integer> {

    @Query("SELECT COUNT (m) FROM Mutante m where m.mutante = 1")
    Long contadorDeMutantes();

    @Query("SELECT COUNT (m) FROM Mutante m where m.mutante = 0")
    Long contadorDeNoMutantes();

}

