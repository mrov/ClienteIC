package br.mafia.client.gui;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import br.mafia.client.musicas.Musica;
import javax.swing.JLabel;

public class DownloadFrame {

	private JFrame frame;
	private Musica musica;
	private Cliente cliente;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public DownloadFrame(Musica musica, Cliente cliente) {
		this.musica = musica;
		this.cliente = cliente;
		initialize();
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.lblNewLabel.setText("Baixando " + this.musica.getPath() + " em " + this.cliente.getPastaMusicas());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(68, 111, 322, 54);
		frame.getContentPane().add(progressBar);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(35, 25, 389, 77);
		frame.getContentPane().add(lblNewLabel);
	}
}
