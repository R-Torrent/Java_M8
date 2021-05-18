package M8_Milestone3.domain;

import java.time.LocalDate;

// Versión ampliada de 'Usuari' que incorpora la contraseña previamente codificada con el método '.hashCode()'
public class UsuariExtended extends Usuari {
	
	protected Integer password;
	
	// Contructor habitual para los registros nuevos
	public UsuariExtended(String nom, String cognom, int password) throws Exception {
		super(nom, cognom, LocalDate.now());
		
		this.password = Integer.valueOf(password);
	}
	
	// Constructor para crear objetos en memoria desde una fuente externa (XML, etc.)
	public UsuariExtended(String nom, String cognom, int password, int registryY, int registryM, int registryD ) throws Exception {
		super(nom, cognom, LocalDate.of(registryY, registryM, registryD));
		
		this.password = Integer.valueOf(password);
	}
	
	public Integer getHashCode() {
		return password;
	}

}
