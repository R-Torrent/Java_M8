package M8_Milestone3.utilities;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BoxField extends JButton {
	
	private static final long serialVersionUID = 1L;

	public BoxField(JPanel panel, String label) {
		super(label);
		panel.add(Box.createVerticalStrut(10));
		final Box box = Box.createHorizontalBox();
		box.add(this);
		panel.add(box);
		panel.add(Box.createVerticalStrut(10));
	}
	
}