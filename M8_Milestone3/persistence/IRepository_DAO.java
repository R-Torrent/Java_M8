package M8_Milestone3.persistence;

import java.util.Set;

import M8_Milestone3.domain.Usuari;
import M8_Milestone3.domain.UsuariExtended;
import M8_Milestone3.domain.Video;

public interface IRepository_DAO {
	
/*
 * Implementa el patrón de diseño "DAO - Database Access Object". Los servicios de la aplicación se atienden vía esta
 * interfaz. La clase concreta de DAO se implementa al instanciar el 'Controller' para la BBDD.
 * 
 * El controlador ya se encarga de comprobar que 'password', 'usuari' y 'video' != null
 * 
 * Las clases 'RepositoryXXX_DAO' se instancian mediante un "singleton" con llamadas 'RepositoryXXX_DAO.getInstance()'.
 */ 
	
	boolean addUsuari(UsuariExtended usuari); // 'true' si exitoso
	
	Set<Usuari> findUsuaris(String cognomParcial); // Si 'cognomParcial' == null o isBlank() devuelve el conjunto completo
	
	void removeUsuari(Usuari usuari);
		
	int size();
	
	boolean addVideo(Usuari usuari, Video video);  // 'true' si exitoso
	
	void removeVideo(Usuari usuari, Video video);

}
