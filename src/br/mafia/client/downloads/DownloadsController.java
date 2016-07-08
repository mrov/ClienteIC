package br.mafia.client.downloads;

import java.util.ArrayList;

import br.mafia.client.gui.Cliente;
import br.mafia.client.musicas.Musica;

public class DownloadsController {
	private Cliente cliente;
	private ArrayList<Download_cliente> downloads;
	
	public DownloadsController(Cliente cliente) {
		this.cliente = cliente;
		this.downloads = new ArrayList<Download_cliente>();
	}
	
	public int iniciarDownload(Musica musica, JanelaDownload guidownload) {
		Download_cliente download = new Download_cliente(this.cliente, musica, guidownload);
		this.downloads.add(download);
		new Thread(download).start();
		return download.getID();
	}
	
	public void pauseDownload(int id) {
		this.getDownload(id).pause();
	}
	
	public void continuarDownload(int id) {
		this.getDownload(id).despause();
		new Thread(this.getDownload(id)).start();
	}
	
	public void zerarBitInicial(int id){
		this.getDownload(id).zerarBitInicial();
	}
	
	public void cancelarDownload(int id) {
		this.getDownload(id).cancelar();
	}
	
	private Download_cliente getDownload(int id) { System.out.println("id " + id);
		Download_cliente d = null;
		for(int i = 0; i < this.downloads.size(); i++) {
			if(this.downloads.get(i).getID() == id) {
				d = this.downloads.get(i);
			}
		}
		return d;
	}
}
