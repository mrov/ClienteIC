package br.mafia.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.mafia.client.musicas.JanelaPlayer;
import br.mafia.client.musicas.Musica;
import jaco.mp3.player.MP3Player;

public class LogadoFrame implements JanelaPlayer {

	private JFrame frame;
	private Cliente cliente;
	private LoginFrame login;
	private JTextField textField;
	private DefaultTableModel model;
	private DefaultTableModel model_2;
	private JComboBox comboBox;
	private JTable table;
	private JTable table_2;
	private int musicaselecionada;
	private int linhaselecionada;
	private int reproduzindo;
	private JButton btnBaixar;
	private ArrayList<Musica> musicas;
	private MP3Player mp;
	private JButton btnBack;
	private JButton btnPlay;
	private JButton btnPause;
	private JButton btnStop;
	private JButton btnFoward;
	private JButton[] botoes = {btnBack,btnPause,btnStop,btnFoward};

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public LogadoFrame(Cliente cliente, LoginFrame login) {
		this.cliente = cliente;
		this.login = login;
		this.musicaselecionada = -1;
		this.linhaselecionada = -1;
		initialize();
		this.mp = new MP3Player();
		this.frame.setVisible(true);
		this.loadMusicas();
		JButton[] botoes = {btnBack,btnPause,btnStop,btnFoward};
		this.btnBack.setEnabled(false);
		this.btnPlay.setEnabled(true);
		this.btnPause.setEnabled(false);
		this.btnStop.setEnabled(false);
		this.btnFoward.setEnabled(false);
		this.cliente.setJanelaPlayer(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(27, 63, 696, 525);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Procurar músicas", null, panel, null);
		panel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(29, 34, 305, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		String[] buscaspossiveis = {"Nome", "Artista"};
		comboBox = new JComboBox(buscaspossiveis);
		comboBox.setBounds(346, 31, 158, 24);
		panel.add(comboBox);
		
		JButton btnProcurar = new JButton("Procurar");
		btnProcurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procurar();
			}
		});
		btnProcurar.setBounds(516, 31, 146, 25);
		panel.add(btnProcurar);
		
		this.model = new DefaultTableModel() {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		this.model.addColumn("Id"); 
		this.model.addColumn("Nome"); 
		this.model.addColumn("Artista");
		this.model.addColumn("Duração"); 
		this.model.addColumn("Tamanho");
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(29, 81, 633, 356);
		panel.add(panel_2);
		
		table = new JTable(this.model);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		
		
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = table.rowAtPoint(evt.getPoint());
		        int col = table.columnAtPoint(evt.getPoint());
		        if (evt.getClickCount() == 1) {
		        	if (row >= 0 && col >= 0) {
		        		selecionamusica(String.valueOf(model.getValueAt(row, 0)));
		        	}
		        }
		    }
		});
		
		
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(20);
		table.getColumnModel().getColumn(3).setPreferredWidth(20);
		table.getColumnModel().getColumn(4).setPreferredWidth(20);
		table.removeColumn(table.getColumnModel().getColumn(0));
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane(table);
		panel_2.add(scrollPane);
		
		btnBaixar = new JButton("Baixar");
		btnBaixar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				baixarselecionada();
			}
		});
		btnBaixar.setEnabled(false);
		btnBaixar.setBounds(545, 449, 117, 25);
		panel.add(btnBaixar);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Minhas músicas", null, panel_1, null);
		panel_1.setLayout(null);
		
		this.btnBack = new JButton("");
		btnBack.setIcon(new ImageIcon(LogadoFrame.class.getResource("/Frames/backm.png")));
		this.btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				skip_b();
			}
		});
		this.btnBack.setBounds(219, 36, 45, 45);
		panel_1.add(this.btnBack);
		
		this.btnPlay = new JButton("");
		btnPlay.setIcon(new ImageIcon(LogadoFrame.class.getResource("/Frames/playm.png")));
		this.btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tocarMusica();
			}
		});
		this.btnPlay.setBounds(315, 36, 45, 45);
		panel_1.add(this.btnPlay);
		
		this.btnPause = new JButton("");
		btnPause.setIcon(new ImageIcon(LogadoFrame.class.getResource("/Frames/pausem.png")));
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});
		this.btnPause.setBounds(363, 36, 45, 45);
		panel_1.add(this.btnPause);
		
		this.btnStop = new JButton("");
		btnStop.setIcon(new ImageIcon(LogadoFrame.class.getResource("/Frames/stopm.png")));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		this.btnStop.setBounds(267, 36, 45, 45);
		panel_1.add(this.btnStop);
		
		this.btnFoward = new JButton("");
		btnFoward.setIcon(new ImageIcon(LogadoFrame.class.getResource("/Frames/fowardm.png")));
		this.btnFoward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				skip_f();
			}
		});
		this.btnFoward.setBounds(411, 36, 45, 45);
		panel_1.add(this.btnFoward);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(29, 87, 638, 356);
		panel_1.add(panel_3);
		
		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectar();
			}
		});
		btnDesconectar.setBounds(582, 26, 141, 25);
		frame.getContentPane().add(btnDesconectar);
		
		// segunda tabela
		
		this.model_2 = new DefaultTableModel() {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		this.model_2.addColumn("Path"); 
		this.model_2.addColumn("Nome"); 
		this.model_2.addColumn("Artista");
		this.model_2.addColumn("Duração"); 
		
		table_2 = new JTable(this.model_2);
		table_2.setFillsViewportHeight(true);
		table_2.setAutoCreateRowSorter(true);
		
		
		table_2.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = table_2.rowAtPoint(evt.getPoint());
		        int col = table_2.columnAtPoint(evt.getPoint());
		        if (evt.getClickCount() == 1) {
		        	if (row >= 0 && col >= 0) {
		        		selecionaLinha(row);
		        	}
		        } 
		        if(evt.getClickCount() == 2) {
		        	if (row >= 0 && col >= 0) {
		        		selecionaLinha(row);
		        		tocarMusica();
		        	}
		        }
		    }
		});
		
		
		table_2.getColumnModel().getColumn(0).setPreferredWidth(20);
		table_2.getColumnModel().getColumn(1).setPreferredWidth(250);
		table_2.getColumnModel().getColumn(2).setPreferredWidth(20);
		table_2.getColumnModel().getColumn(3).setPreferredWidth(20);
		table_2.removeColumn(table_2.getColumnModel().getColumn(0));
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane(table_2);
		panel_3.add(scrollPane_2);
	}
	
	public void desconectar() {
		this.cliente.logout();
		this.frame.setVisible(false);
		this.login.v();
	}
	
	public void procurar() {
		this.btnBaixar.setEnabled(false);
		int linhastabela = this.table.getRowCount();
		for (int i = linhastabela - 1; i >= 0; i--) {
		    this.model.removeRow(i);
		}
		
		String busca = this.textField.getText();
		String mod = this.comboBox.getSelectedItem().toString();
		ArrayList<Musica> musicas;
		if(mod.equals("Nome")) {
			this.musicas = musicas = this.cliente.procurarMusicaNome(busca);
		} else {
			this.musicas = musicas = this.cliente.procurarMusicaArtista(busca);
		}
		Musica atual;
		int minutos, segundos;
		for(int i = 0; i < musicas.size(); i++) {
			atual = musicas.get(i);
			minutos = atual.getDuracao() / 60;
			segundos = atual.getDuracao() % 60;
			this.model.addRow(new Object[]{String.valueOf(atual.getId()), atual.getNome(), atual.getArtista(), minutos + ":" + segundos, this.tamstring(atual.getTam())});
		}
	}
	
	private String tamstring(long tam) {
		int kb = (int)(tam / 1024);
		int mb = kb / 1024;
		int gb = mb / 1024;
		String r = "";
		if(gb > 0) r = gb + " GB";
		else if(mb > 0) r = mb + " MB";
		else if(kb > 0) r = kb + " KB";
		else r = tam + " Bytes";
		return r;
	}
	
	public void selecionamusica(String id) {
		int idint = Integer.parseInt(id);
		this.musicaselecionada = idint;
		this.btnBaixar.setEnabled(true);
	}
	
	public void baixarselecionada() {
		if(this.cliente.getMusica(this.musicaselecionada) == null) {
			new DownloadFrame(this.getMusica(this.musicaselecionada), this.cliente);
		} else {
			JOptionPane.showMessageDialog(frame, "Você já possui esta música em sua biblioteca", "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private Musica getMusica(int id) {
		Musica musica = null;
		Musica atual = null;
		for(int i = 0; i < this.musicas.size(); i++) {
			atual = this.musicas.get(i);
			if(atual.getId() == id) {
				musica = atual;
			}
		}
		return musica;
	}
	
	//repositório cliente
	
	public void loadMusicas() {
		ArrayList<Musica> musicas = this.cliente.getMusicasDisponiveis();
		for(int i = 0; i < musicas.size(); i++) {
			addMusica(musicas.get(i));
		}
	}
	
	public void addMusica(Musica musica) {
		int min = musica.getDuracao() / 60;
		int sec = musica.getDuracao() % 60;
		this.model_2.addRow(new Object[]{musica.getPath(), musica.getNome(), musica.getArtista(), min + ":" + sec});
	}
	
	public void selecionaLinha(int linha) {
		this.linhaselecionada = linha;
		this.btnBack.setEnabled(true);
		this.btnPlay.setEnabled(false);
		this.btnPause.setEnabled(true);
		this.btnStop.setEnabled(true);
		this.btnFoward.setEnabled(true);
	}
	
	public void tocarMusica() {
		if(mp.isStopped()){
			btnPlay.setEnabled(false);
			btnStop.setEnabled(true);
			btnPause.setEnabled(true);
			btnBack.setEnabled(true);
			btnFoward.setEnabled(true);
			linhaselecionada = 0;
			String path = String.valueOf(model_2.getValueAt(0, 0));
			this.mp = new MP3Player(new File(this.cliente.getPastaMusicas() + File.separator + path));
			this.reproduzindo = this.linhaselecionada;
			int linhastabela = this.table_2.getRowCount();
			for(int i = 0; i < linhastabela; i++) {
				this.mp.addToPlayList(new File(this.cliente.getPastaMusicas() + File.separator + String.valueOf(model_2.getValueAt(i, 0))));
    	}
			this.mp.play();
		}
    	else if(mp.isPaused()){
    		mp.play();
    		btnPause.setEnabled(true);
    		btnPlay.setEnabled(false);
    	}
    	else if(!mp.isStopped() || !mp.isPaused()){
    		mp.stop();
			String path = String.valueOf(model_2.getValueAt(linhaselecionada, 0));
			this.mp = new MP3Player(new File(this.cliente.getPastaMusicas() + File.separator + path));
			this.reproduzindo = this.linhaselecionada;
			int linhastabela = this.table_2.getRowCount();
			for(int i = 0; i < linhastabela; i++) {
				this.mp.addToPlayList(new File(this.cliente.getPastaMusicas() + File.separator + String.valueOf(model_2.getValueAt(i, 0))));
			}
			mp.play();
		}
	}
	
	public void skip_b() {
		int linhastabela = this.table_2.getRowCount();
		if(linhastabela > 0) {
			this.linhaselecionada--;
			if(this.linhaselecionada == -1) this.linhaselecionada = linhastabela - 1;
			table_2.setRowSelectionInterval(this.linhaselecionada, this.linhaselecionada);
			this.tocarMusica();
		}
	}
	
	
	public void skip_f() {
		int linhastabela = this.table_2.getRowCount();
		if(linhastabela > 0) {
			this.linhaselecionada = (this.linhaselecionada + 1) % linhastabela;
			table_2.setRowSelectionInterval(this.linhaselecionada, this.linhaselecionada);
			this.tocarMusica();
		}
	}
	
	public void stop() {
		this.mp.stop();
		btnStop.setEnabled(false);
		btnPlay.setEnabled(true);
		btnPause.setEnabled(false);
		btnBack.setEnabled(false);
		btnFoward.setEnabled(false);
	}
	
	public void pause() {
		this.mp.pause();
		btnPause.setEnabled(false);
		btnPlay.setEnabled(true);
	}
}
