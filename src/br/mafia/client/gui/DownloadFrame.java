package br.mafia.client.gui;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import br.mafia.client.downloads.JanelaDownload;
import br.mafia.client.musicas.Musica;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DownloadFrame implements JanelaDownload {

	private JFrame frame;
	private Musica musica;
	private Cliente cliente;
	private JLabel lblNewLabel;
	private JProgressBar progressBar;
	private int id_download;
	private boolean pausado;
	private JButton btnPausar;

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
		
		btnPausar = new JButton("Pausar");
		btnPausar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pausadespausa();
			}
		});
		btnPausar.setBounds(69, 199, 117, 25);
		frame.getContentPane().add(btnPausar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(203, 199, 117, 25);
		frame.getContentPane().add(btnCancelar);
		this.id_download = this.cliente.iniciarDownload(musica, this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		this.progressBar = new JProgressBar();
		this.progressBar.setBounds(68, 111, 322, 54);
		frame.getContentPane().add(this.progressBar);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(35, 25, 389, 77);
		frame.getContentPane().add(lblNewLabel);
	}
	
	public void setStatusDownload(int porcentagem) {
		this.progressBar.setValue(porcentagem);
	}
	
	public void pausadespausa() {
		if(this.pausado == true) {
			this.btnPausar.setText("Pause");
			this.pausado = false;
			this.cliente.continuarDownload(this.id_download);
		} else {
			this.btnPausar.setText("Continuar");
			this.pausado = true;
			this.cliente.pausarDownload(this.id_download);
		}
	}
}
