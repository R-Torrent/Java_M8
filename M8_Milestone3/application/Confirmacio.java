package M8_Milestone3.application;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Confirmacio {
	
	public Confirmacio() {}
	
	public boolean inici(String missatge) {
		return JOptionPane.showConfirmDialog(null, new JLabel(missatge),
				"ATENCIÓ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION;
	}

}
