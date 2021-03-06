package br.mafia.client.downloads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import br.mafia.client.gui.Cliente;
import br.mafia.client.musicas.Musica;

public class Download_cliente extends Thread{
	private int id_download;
	private int byte_init;
	private int pause;
	private Cliente cliente;
	private Musica musica;
	private boolean cancelado;
	private JanelaDownload guidownload;

	public Download_cliente(Cliente cliente, Musica musica, JanelaDownload guidownload){
		this.guidownload = guidownload;
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
				socket.setSoTimeout(1000);
				OutputStream saida_t = socket.getOutputStream();
				InputStream entrada_t = socket.getInputStream();
				saida_t.write(49);
				saida_t.write(id_download>>8);
				saida_t.write(id_download & 255); //envia 2 bytes de id_download
				saida_t.write((byte_init >> 24) & 255);
				saida_t.write((byte_init >> 16) & 255);
				saida_t.write((byte_init >> 8) & 255);
				saida_t.write(byte_init & 255);
				System.out.println("Download iniciado em " + byte_init + " bytes");
				System.out.println("Vai salvar em: " + this.cliente.getPastaMusicas() + File.separator + this.musica.getPath());
				int bytes_receive=0;
				byte[] buffer = new byte[1024];

				this.guidownload.alertaErro("");

				OutputStream download;
				if (byte_init == 0) download = new FileOutputStream(this.cliente.getPastaMusicas() + File.separator + this.musica.getPath());  //está começando agora
				else download = new FileOutputStream(this.cliente.getPastaMusicas() + File.separator + this.musica.getPath(), true);           //continuando download


				long tamanho_arquivo = this.musica.getTam();
				long tempo_inicial = System.currentTimeMillis();
				long ultimo_tempo_registrado = tempo_inicial;
				long ultima_quantidade_de_bytes_ja_baixados = byte_init;
				//				int total_bytes=0;
				while (!cancelado && pause == 0 && (bytes_receive = entrada_t.read(buffer)) > 0){ //recebe dados e grava no buffer
					download.write(buffer, 0, bytes_receive); //envia dados do buffer para o arquivo
					byte_init+=bytes_receive;
					//					System.out.println("T. Bytes recebidos: " + total_bytes);
					//System.out.println(((double)byte_init/(double)this.musica.getTam())*100 + "%");
					if((System.currentTimeMillis() - ultimo_tempo_registrado) > 200){ //utilizado para atualizar os dados da GUI a cada meio segundo
						long velocidade = (long) ((byte_init-ultima_quantidade_de_bytes_ja_baixados)/((System.currentTimeMillis() - ultimo_tempo_registrado)/1000f));
						this.guidownload.setStatusDownload((int)((float)byte_init/tamanho_arquivo*1000), tempo_restante_em_string((long)((tamanho_arquivo-byte_init)/(float)(velocidade|1))), velocidade_em_string(velocidade));
						ultimo_tempo_registrado = System.currentTimeMillis();
						ultima_quantidade_de_bytes_ja_baixados = byte_init;
					}
				}
				if (cancelado){
					download.close();
					System.out.println("Download " + id_download + " cancelado");
					File arquivo = new File(this.cliente.getPastaMusicas() + File.separator + this.musica.getPath());
					this.guidownload.setStatusDownload((int)((float)byte_init/tamanho_arquivo*1000), "0 s", "0 KB/s");
					arquivo.delete();
				} else if (pause == 0) {
					System.out.println("Download " + id_download + " concluído");
					this.guidownload.setStatusDownload((int)((float)byte_init/tamanho_arquivo*1000), "0 s", "0 KB/s");
					this.cliente.addMusica(this.musica.getId(), this.musica.getNome(), this.musica.getArtista(), this.musica.getDuracao(), this.musica.getPath(), this.musica.getTam());
				}
				else {
					this.guidownload.setStatusDownload((int)((float)byte_init/tamanho_arquivo*1000), "0 s", "0 KB/s");
					System.out.println("Download " + id_download + " pausado em " + byte_init);
				}
				if(byte_init>=tamanho_arquivo){
					this.guidownload.finalizar_download();
					this.guidownload.setStatusDownload(1000, "0 s","0 KB/s");
				}

				socket.close();
			} else if (pause == 1){
				System.out.println("Download " + id_download + " pausado");

			} else {
				System.out.println("Download " + id_download + " cancelado");
			}

		} catch (Exception e) {
			System.out.println("Exceção cabo:");			
			e.printStackTrace();
			// TODO Auto-generated catch block
			try {
//				if (e instanceof UnknownHostException) {
					for(int i = 0; i < 5; i++) {
//						this.guidownload.alertaErro("Erro no download, tentando reconectar em: " + (10 - i) + " segundos");
						this.guidownload.alertaErro("Erro de conexão, tentando reconectar.");
						Thread.sleep(1000);
					}					
//				} else{
//					this.guidownload.alertaErro("O servidor está offline, aguarde um momento.");
//					
//				}
				this.cliente.continuarDownload(this.id_download);

			} catch (InterruptedException e1) {
				System.out.println("Exceção 2");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//			if (e instanceof IOException){
			//				System.out.println("Erro: ");
			//				e.printStackTrace();
			//			} else{
			//				System.out.println("Temos problemas no download: ");
			//				e.printStackTrace();
			//			}
		}

	}
	private String tempo_restante_em_string(long s){
		long mm = s/60;
		s%=60;
		long hh = mm/60;
		mm%=60;
		String retorno = s + "s restantes";
		if(mm > 0)
			retorno = mm + "min e " + retorno;
		if(hh>0)
			retorno = hh + "h e " + retorno;
		return  retorno;
	}
	private String velocidade_em_string(long tam){
		long k = tam/1024;
		tam%=1024;
		long m = k/1024;
		k%=1024;
		long g = m/1024;
		m%=1024;
		if(g>0){
			return g + "," + m + " GB/s";
		}else if(m>0){
			return m + "," + k + " MB/s";
		} else if(k>0){
			return k + "," + tam + " KB/s";
		}
		return  tam + "b/s";
	}

	public void pause(){
		this.cliente.anunciaPause(this.id_download);
		System.out.println("pausando");
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
	
	public void zerarBitInicial(){
		this.byte_init = 0;
	}
}
