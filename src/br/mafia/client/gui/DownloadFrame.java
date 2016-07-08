package br.mafia.client.gui;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import br.mafia.client.downloads.JanelaDownload;
import br.mafia.client.musicas.Musica;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private JLabel lb_tempo_restante_s;
	private JLabel lb_velo_download_kbps;

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
		btnPausar.setBounds(69, 268, 117, 25);
		frame.getContentPane().add(btnPausar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(235, 268, 117, 25);
		frame.getContentPane().add(btnCancelar);
		
		JLabel lb_velocidade_download = new JLabel("Velocidade Download:");
		lb_velocidade_download.setBounds(69, 237, 158, 15);
		frame.getContentPane().add(lb_velocidade_download);
		
		lb_velo_download_kbps = new JLabel("0 KB/s");
		lb_velo_download_kbps.setBounds(267, 237, 70, 15);
		frame.getContentPane().add(lb_velo_download_kbps);
		
		JLabel lb_tempo_restante = new JLabel("Tempo Restante: ");
		lb_tempo_restante.setBounds(69, 213, 191, 15);
		frame.getContentPane().add(lb_tempo_restante);
		
		lb_tempo_restante_s = new JLabel("0 s");
		lb_tempo_restante_s.setBounds(267, 213, 70, 15);
		frame.getContentPane().add(lb_tempo_restante_s);
		this.id_download = this.cliente.iniciarDownload(musica, this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 431, 347);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		this.progressBar = new JProgressBar();
		this.progressBar.setBounds(46, 89, 322, 54);
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(1000);
		frame.getContentPane().add(this.progressBar);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(36, 12, 389, 77);
		frame.getContentPane().add(lblNewLabel);
	}
	public void finalizar_download(){
		JOptionPane.showMessageDialog(this.frame, "Download finalizado com sucesso!", "Download", JOptionPane.INFORMATION_MESSAGE);
		btnPausar.setEnabled(false);
	}
	
	public void setStatusDownload(int porcentagem, String tempo_restante, String velocidade) {
		this.progressBar.setValue(porcentagem);
		this.lb_tempo_restante_s.setText(tempo_restante);
		this.lb_velo_download_kbps.setText(velocidade);
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
