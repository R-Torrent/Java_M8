package M8_Milestone3.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import M8_Milestone3.domain.Video;

import M8_Milestone3.utilities.BoxField;

public class ReproduirVideo {
	
	private Video video;
	private PlayStatus playS;
	private Timer videoTimer;
	
	private LocalTime actual, durada;
	
	private JPanel panelRV, panelButtons;
	private JLabel image;
	private JButton button1, button2, button3;
	private JFrame frame;
	
	public ReproduirVideo(Video video) {
		this.video = video;
		durada = video.getDurada();
		playS = PlayStatus.STOP;
		actual = LocalTime.of(0,0,0);
		videoTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actual = actual.plusSeconds(1L);
				if(!actual.equals(durada))
					playS.output(image, actual, durada);
				else
					button2.doClick(); // 'Stop' automático
			}
			
		});
		
		// Aquí vendría el código para cargar el vídeo almacenado en 'video.getURL()'.
	}
	
	public void inici() {
		panelRV = new JPanel();
		panelRV.setLayout(new BorderLayout(0, 5));
		
		image = new JLabel(playS.getText(), JLabel.CENTER);
		image.setFont(new Font("Verdana", Font.PLAIN, 20));
		image.setPreferredSize(new Dimension(711, 400));
		image.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 10));
		
		panelRV.add(image, BorderLayout.CENTER);
		
		panelButtons = new JPanel();
		panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
		
		button1 = new BoxField(panelButtons, "Play / Pause");
		button2 = new BoxField(panelButtons, "Stop");
		button3 = new BoxField(panelButtons, "Tancar");
		
		panelRV.add(panelButtons, BorderLayout.PAGE_END);
		
		frame = new JFrame(video.toString().toUpperCase());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().add(panelRV, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		button1.addActionListener(new ActionListener() { // Reproduir/pausar vídeo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(playS == PlayStatus.PLAY) {
					videoTimer.stop();
					playS = PlayStatus.PAUSE;
				}
				else {
					videoTimer.start();
					playS = PlayStatus.PLAY;
				}
				playS.output(image, actual, durada);
			}
			
		});
		
		button2.addActionListener(new ActionListener() { // Aturar vídeo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				videoTimer.stop();
				actual = LocalTime.of(0,0,0);
				playS = PlayStatus.STOP;
				playS.output(image, actual, durada);
			}
			
		});
		
		button3.addActionListener(new ActionListener() { // Tancar finestra
			
			@Override
			public void actionPerformed(ActionEvent e) {
				videoTimer.stop();
				frame.dispose();
			}
			
		});
	}
	
	protected enum PlayStatus {
		
		PLAY("PLAYING...") {
			@Override
			protected String setText(LocalTime c, LocalTime d) {
				return c.format(dtf) + "  " + this.getText() + "  " + d.format(dtf);
		} },
		
		PAUSE("PAUSE") {
			@Override
			protected String setText(LocalTime c, LocalTime d) {
				return c.format(dtf) + "     " + this.getText() + "     " + d.format(dtf);
		} },
		
		STOP("MÒDUL 8 - MILESTONE 3") {
			@Override
			protected String setText(LocalTime c, LocalTime d) {
				return this.getText();
		} };
		
		private static DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_TIME;
		private String text;
		
		PlayStatus(String text) {
			this.text = text;
		}
		
		protected String getText() {
			return text;
		}
		
		protected void output(JLabel image,  LocalTime current, LocalTime duration) {
			image.setText(setText(current, duration));
		}
		
		protected abstract String setText(LocalTime current, LocalTime duration);

	}
	
}
