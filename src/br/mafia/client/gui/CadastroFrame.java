package br.mafia.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.ConnectException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.mafia.client.conexao.UsuarioJaCadastradoException;

public class CadastroFrame {

	private JFrame frame;
	private Cliente cliente;
	private LoginFrame login;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnCadastrar;
	private boolean fix_bug;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public CadastroFrame(Cliente cliente, LoginFrame login) {
		this.cliente = cliente;
		this.login = login;
		initialize();
		this.frame.setVisible(true);
		fix_bug = false;
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(68, 76, 70, 15);
		frame.getContentPane().add(lblLogin);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(68, 103, 70, 15);
		frame.getContentPane().add(lblSenha);

		textField = new JTextField();
		textField.setBounds(135, 74, 160, 19);
		frame.getContentPane().add(textField);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!fix_bug){
						cadastrar();
						fix_bug = true;
					} else fix_bug = false;
				}
			}
		});
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(134, 101, 160, 19);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!fix_bug){
						cadastrar();
						fix_bug = true;
					} else fix_bug = false;
				}
			}
		});
		frame.getContentPane().add(passwordField);

		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrar();
			}
		});
		btnCadastrar.setBounds(177, 159, 117, 25);
		frame.getContentPane().add(btnCadastrar);
	}

	public void cadastrar() {
		try {
			this.cliente.cadastrar(this.textField.getText(), this.passwordField.getText());
			JOptionPane.showMessageDialog(frame, "Cadastrado com sucesso!");
			this.login.setText(this.textField.getText(), this.passwordField.getText());
			this.frame.setVisible(false);
		} catch (UsuarioJaCadastradoException e) {
			JOptionPane.showMessageDialog(frame, "Usuário já cadastraro", "Erro", JOptionPane.ERROR_MESSAGE);
		}catch (Exception e) {
			if(e instanceof ConnectException){
				JOptionPane.showMessageDialog(frame, "Verifique a sua conexão com a internet", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
