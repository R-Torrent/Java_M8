package M8_Milestone3.application;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import M8_Milestone3.domain.Video;

import M8_Milestone3.utilities.BoxField;

public class MostrarVideo implements ActionListener {
	
	private Controller m8;
	private Video video;
	
	private JPanel panelMV;
	private JTextArea textA;
	private JScrollPane scrollPanel;
	private JButton button;
	private JFrame frame;
	
	// No cal verificar les dues entrades; mai seran buides
	public MostrarVideo(Controller m8, Video video) {
		this.m8 = m8;
		this.video = video;
		
		video.addActionListener(this); // Permite que el 'UploadStatus' se pueda actualizar
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { // Actualización en directo
		textA.setText(video.showVideo());
	}
	
	public void inici() {
		panelMV = new JPanel();
		panelMV.setLayout(new BoxLayout(panelMV, BoxLayout.Y_AXIS));
				
		textA = new JTextArea(video.showVideo());
		textA.setEditable(false);
		textA.setFocusable(false);
		
		scrollPanel = new JScrollPane(textA);
		panelMV.add(scrollPanel);
		
		button = new BoxField(panelMV, "Tancar");	
		
		frame = new JFrame(m8.contU.usuari.toString().toUpperCase() + m8.labelBBDD);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().add(panelMV, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		button.addActionListener(new ActionListener() { // Tancar finestra
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
			
		});
	}
	
}
