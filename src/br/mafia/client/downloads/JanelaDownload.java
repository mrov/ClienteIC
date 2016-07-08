package br.mafia.client.downloads;

public interface JanelaDownload {
	public void setStatusDownload(int porcentagem, String tempo_restante, String velocidade);
	public void finalizar_download();
	public void alertaErro(String erro);
}	
