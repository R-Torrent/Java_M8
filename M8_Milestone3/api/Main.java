/** Back-end Java, IT Academy
*** M8 - Milestone 3
*** Roger Torrent */

package M8_Milestone3.api;

public class Main {
	
/*
 * PATRONES DE DISEÑO IMPLEMENTADOS:
 * 
 * "singleton (creational)" en las clases 'persistence.RepositoryXXX_DAO'
 * 
 * "prototype (creational)" en la clase 'application.Controller'
 * 
 * "DAO - Database Access Object" para las clases 'RepositoryXXX_DAO' en el paquete 'M8_Milestone3.persistence' 
 *     NOTA: Puede considerarse que el patrón "DAO", familia de métodos intercambiables seleccionados dinámicamente,
 *     es también una implementación del diseño "strategy (behavioral)". Ejerce la clase 'Controller' de "context"
 *     del patrón.
 */
	
	public static void main(String[] args) {			
		new MainMenu().start();
	}
	
}
