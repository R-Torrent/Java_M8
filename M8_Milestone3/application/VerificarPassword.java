package M8_Milestone3.application;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import M8_Milestone3.utilities.HintTextField;

public class VerificarPassword {
	
	private int password;
	private String nomUsuari;
	
	private JPanel panelVP;
	private Box box;
	private HintTextField sequencia;
	private int userOp;
	
	// No cal verificar les dues entrades; res es trenca encara que estiguin buides 
	public VerificarPassword(Integer password, String nomUsuari) {
		this.password = password.intValue();
		this.nomUsuari = nomUsuari;
	}
	
	public boolean inici() throws Exception {
		panelVP = new JPanel();
		panelVP.setLayout(new BoxLayout(panelVP, BoxLayout.Y_AXIS));
		
		panelVP.add(Box.createHorizontalStrut(250));
		
		box = Box.createVerticalBox();
		
		box.add(new JLabel(nomUsuari));
		sequencia = new HintTextField("< introdueixi la seva contrasenya >");
		box.add(sequencia);
		
		panelVP.add(box);
		
	    userOp = JOptionPane.showConfirmDialog(null, panelVP, "VERIFICACIÓ DE LA CONTRASENYA", JOptionPane.OK_CANCEL_OPTION); 
		
	    if(userOp == JOptionPane.OK_OPTION)
	    	if(sequencia.getText().hashCode() == password)
	    		return true;
	    	else throw new Exception("Incorrect password!");
		
		return false;
	}

}
