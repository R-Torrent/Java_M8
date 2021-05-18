/** Back-end Java, IT Academy
*** M8 - Milestone 3
*** Roger Torrent */

package M8_Milestone3.api;

public class Main {
	
/*
 * PATRONES DE DISE�O IMPLEMENTADOS:
 * 
 * "singleton (creational)" en las clases 'persistence.RepositoryXXX_DAO'
 * 
 * "prototype (creational)" en la clase 'application.Controller'
 * 
 * "DAO - Database Access Object" para las clases 'RepositoryXXX_DAO' en el paquete 'M8_Milestone3.persistence' 
 *     NOTA: Puede considerarse que el patr�n "DAO", familia de m�todos intercambiables seleccionados din�micamente,
 *     es tambi�n una implementaci�n del dise�o "strategy (behavioral)". Ejerce la clase 'Controller' de "context"
 *     del patr�n.
 */
	
	public static void main(String[] args) {			
		new MainMenu().start();
	}
	
}
