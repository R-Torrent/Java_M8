package M8_Milestone3.api;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import M8_Milestone3.application.Controller;

import M8_Milestone3.utilities.BoxField;

public class UserMenu {
	
	private Controller m8;
	
	private JPanel panel;
	private JButton button1, button2, button3, button4, button5;
	private JFrame frame;
	
	public UserMenu(Controller m8) {
		this.m8 = m8;	
		Controller.activeUsers.add(this);
	}
	
	public void start() { // Menú secundario - vídeos
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(Box.createHorizontalStrut(250));
		
		button1 = new BoxField(panel, "Reproduir vídeo");
		button2 = new BoxField(panel, "Afegir vídeo");
		button3 = new BoxField(panel, "Mostrar vídeo");		
		button4 = new BoxField(panel, "Esborrar vídeo");
		button5 = new BoxField(panel, "Tancar");
		
		frame = new JFrame(m8.contU.usuari.toString().toUpperCase() + " " + m8.labelBBDD);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		button1.addActionListener(new ActionListener() { // Reproduir vídeo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contV.playVideo();
			}
			
		});
		
		button2.addActionListener(new ActionListener() { // Afegir vídeo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contV.createVideo();
			}
			
		});
		
		button3.addActionListener(new ActionListener() { // Mostrar vídeo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contV.printVideo();
			}
			
		});
		
		button4.addActionListener(new ActionListener() { // Esborrar vídeo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				m8.contV.deleteVideo();
			}
			
		});
		
		button5.addActionListener(new ActionListener() { // Tancar finestra
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				Controller.activeUsers.remove(UserMenu.this);
			}
			
		});
	}
	
	public Controller getController() {
		return m8;
	}
	
	public void kill() { // Permite cerrar la ventana desde el controlador
		frame.dispose();
	}
	
}
