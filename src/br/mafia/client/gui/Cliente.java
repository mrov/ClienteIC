package br.mafia.client.gui;

import java.util.ArrayList;

import br.mafia.client.conexao.ConexaoRoot;
import br.mafia.client.conexao.FalhaLoginException;
import br.mafia.client.conexao.UsuarioJaCadastradoException;
import br.mafia.client.downloads.DownloadsController;
import br.mafia.client.downloads.JanelaDownload;
import br.mafia.client.musicas.JanelaPlayer;
import br.mafia.client.musicas.Musica;
import br.mafia.client.musicas.MusicasController;
import br.mafia.client.util.Config;

public class Cliente {
	private Config conf;
	private MusicasController musicas;
	private ConexaoRoot root;
	private DownloadsController downloads;
	
	public Cliente() {
		this.conf = new Config("mafia.conf");
		this.musicas = new MusicasController(this.conf.getArquivoMusicas());
		this.root = new ConexaoRoot(this);
		this.downloads = new DownloadsController(this);
	}
	
	// { <ROOT>
	
	public void cadastrar(String usuario, String senha) throws UsuarioJaCadastradoException {
		this.root.cadastro(usuario, senha);
	}
	
	public void login(String usuario, String senha) throws FalhaLoginException {
		this.root.login(usuario, senha);
	}
	
	public void logout() {
		this.root.logout();
	}
	
	public ArrayList<Musica> procurarMusicaNome(String busca) {
		return this.root.procuraMusica(busca, 32);
	}
	
	public ArrayList<Musica> procurarMusicaArtista(String busca) {
		return this.root.procuraMusica(busca, 33);
	}
	
	public int geraIDdownload(Musica musica) {
		return this.root.geraIDdownload(musica.getId());
	}
	
	public void anunciaPause(int id) {
		this.root.pauseDownload(id);
	}
	
	public void anunciaCancelamento(int id) {
		this.root.cancelarDownload(id);
	}
	
	// } </ROOT>
	
	// { <MUSICAS>
	
	public ArrayList<Musica> getMusicasDisponiveis() {
		return this.musicas.getMusicasBaixadas();
	}
	
	public void addMusica(int id, String nome, String artista, int duracao, String path, long tam) {
		this.musicas.addMusica(id, nome, artista, duracao, path, tam);
	}
	
	public Musica getMusica(int id) {
		return this.musicas.getMusica(id);
	}
	
	public void setJanelaPlayer(JanelaPlayer janela) {
		this.musicas.setJanelaPlayer(janela);
	}
	
	// } </MUSICAS>
	
	// { <DOWNLOADS>
	
	public int iniciarDownload(Musica musica, JanelaDownload guidownload) {
		return this.downloads.iniciarDownload(musica, guidownload);
	}
	
	public void zerarBitInicial(int id){
		this.downloads.zerarBitInicial(id);
	}
	
	public void pausarDownload(int id) {
		this.downloads.pauseDownload(id);
	}
	
	public void cancelarDownload(int id) {
		this.downloads.cancelarDownload(id);
	}
	
	public void continuarDownload(int id) {
		this.downloads.continuarDownload(id);
	}
	
	// } </DOWNLOADS>
	
	// { <CONFIG>
	
	public String getPastaMusicas() {
		return this.conf.getPastaMusicas();
	}
	
	public String getArquivoMuscas() {
		return this.conf.getArquivoMusicas();
	}
	
	public String getIPservidor() {
		return this.conf.getIPservidor();
	}
	
	public String getPortaServidor() {
		return this.conf.getPortaServidor();
	}
	
	public void setIPservidor(String ip) {
		this.conf.setIPservidor(ip);
	}
	
	public void setPortaServidor(String porta) {
		this.conf.setPortaServidor(porta);
	}
	
	public void SalvarConfig() {
		this.conf.salvaConfig("mafia.conf");
	}
	
	// } </CONFIG>
}
