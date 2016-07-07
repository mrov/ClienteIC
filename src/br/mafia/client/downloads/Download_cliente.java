package br.mafia.client.downloads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import br.mafia.client.gui.Cliente;
import br.mafia.client.musicas.Musica;

public class Download_cliente extends Thread{
	private int id_download;
	private int byte_init;
	private int pause;
	private Cliente cliente;
	private Musica musica;
	private boolean cancelado;

	public Download_cliente(Cliente cliente, Musica musica){
		this.cancelado = false;
		this.cliente = cliente;
		this.musica = musica;
		this.byte_init = 0;
		pause=0;
		this.id_download = cliente.geraIDdownload(musica);
	}

	public void run() {
		try {
			if (pause == 0 && !Thread.currentThread().isInterrupted()){
				Socket socket = new Socket(this.cliente.getIPservidor(), Integer.parseInt(this.cliente.getPortaServidor()));
				OutputStream saida_t = socket.getOutputStream();
				InputStream entrada_t = socket.getInputStream();
				saida_t.write(49);
				saida_t.write(id_download>>8);
				saida_t.write(id_download & 255); //envia 2 bytes de id_download
				saida_t.write(byte_init >> 24);
				saida_t.write((byte_init & 255) >> 16);
				saida_t.write((byte_init & 255) >> 8);
				saida_t.write(byte_init & 255);
				System.out.println("Download iniciado em " + byte_init + " bytes");
				System.out.println("Vai salvar em: " + this.cliente.getPastaMusicas() + File.separator + this.musica.getPath());
				int bytes_receive=0;
				byte[] buffer = new byte[1024*4];

				OutputStream download;
				if (byte_init == 0) download = new FileOutputStream(this.cliente.getPastaMusicas() + File.separator + this.musica.getPath());  //está começando agora
				else download = new FileOutputStream(this.cliente.getPastaMusicas() + File.separator + this.musica.getPath(), true);           //continuando download

//				int total_bytes=0;
				while (!cancelado && pause == 0 && (bytes_receive = entrada_t.read(buffer)) > 0){ //recebe dados e grava no buffer
					download.write(buffer, 0, bytes_receive); //envia dados do buffer para o arquivo
					byte_init+=bytes_receive;
//					System.out.println("T. Bytes recebidos: " + total_bytes);
					System.out.println(((double)byte_init/(double)this.musica.getTam())*100 + "%");
				}
				if (cancelado){
					System.out.println("Download " + id_download + " cancelado");
					File arquivo = new File(this.cliente.getPastaMusicas() + File.separator + this.musica.getPath());
					arquivo.delete();
				} else if (pause == 0) System.out.println("Download " + id_download + " concluído");
				else {
					System.out.println("Download " + id_download + " pausado em " + byte_init);
				}
				//client.addMusica(id_musica, nome, artista, duracao, path, tam);

				socket.close();
			} else if (pause == 1){
				System.out.println("Download " + id_download + " pausado");

			} else {
				System.out.println("Download " + id_download + " cancelado");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (e instanceof IOException){
				System.out.println("Erro: ");
				e.printStackTrace();
			} else{
				System.out.println("Temos problemas no download: ");
				e.printStackTrace();
			}
		}

	}

	public void pause(){
		this.cliente.anunciaPause(this.id_download);
		this.pause = 1;
	}

	public void despause(){
		this.pause = 0;
	}

	public int getID(){
		return id_download;
	}
	
	public void cancelar() {
		this.cancelado = true;
	}
}