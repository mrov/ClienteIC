package br.mafia.client.downloads;

import java.util.ArrayList;

import br.mafia.client.gui.Cliente;
import br.mafia.client.musicas.Musica;

public class DownloadsController {
	private Cliente cliente;
	private ArrayList<Download_cliente> downloads;
	
	public DownloadsController(Cliente cliente) {
		this.cliente = cliente;
		this.downloads = new ArrayList();
	}
	
	public void iniciarDownload(Musica musica) {
		Download_cliente download = new Download_cliente(this.cliente, musica);
		this.downloads.add(download);
		new Thread(download).start();
	}
}
