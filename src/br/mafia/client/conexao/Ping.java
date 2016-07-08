package br.mafia.client.conexao;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import br.mafia.client.gui.Cliente;

public class Ping extends Thread {
	private Socket socket;
	private OutputStream saida;
	private Cliente cliente;
	
	public Ping(Socket socket, Cliente cliente) {
		try {
			this.socket = socket;
			this.cliente = cliente;
			this.saida = this.socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(!this.socket.isClosed()) {
			try {
				this.saida.write(80);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				cliente.reconectar();
				e.printStackTrace();
			}
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
