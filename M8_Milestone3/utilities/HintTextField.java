package M8_Milestone3.utilities;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener {

	static final long serialVersionUID = 1L;
	
	private final String hint;
	private boolean showingHint;

	public HintTextField(final String hint) {
		super(hint);
	    this.hint = hint;
	    showingHint = true;
	    super.addFocusListener(this);
	}
	
	public void reset() {
		super.setText(hint);
		showingHint = true; 
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(this.getText().isEmpty()) {
			super.setText("");
			showingHint = false;
	    }
	}
	  
	@Override
	public void focusLost(FocusEvent e) {
		if(this.getText().isEmpty())
			reset();
	}

	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}
	  
}
