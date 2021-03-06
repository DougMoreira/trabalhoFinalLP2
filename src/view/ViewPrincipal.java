package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import modelo.entidade.Dispositivo;
import modelo.entidade.Historico;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;

import controle.BLLDispositivo;
import controle.BLLHistorico;
import controle.JGitControl;


public class ViewPrincipal extends JFrame {

	// Cada botão tem um actionListener próprio para captar o evento de clique
	// Exceto o botão de commit, os outros estão diretamente relacionados com o banco de dados

	private JPanel contentPane;
	public static void main(String[] args) throws NoFilepatternException, IOException, GitAPIException {
		new ServidorEspera().start();
		ViewPrincipal frame = new ViewPrincipal();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public ViewPrincipal() {
		super("dsiServer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 583, 148);
		setLocationRelativeTo(null);
		contentPane = new JPanel();

		JPanel panelDispositivo = new JPanel();
		panelDispositivo.setBorder(BorderFactory.createTitledBorder("Dispositivo")); // "Fieldset" Dispositivo

		JPanel panelComandos = new JPanel();
		panelComandos.setBorder(BorderFactory.createTitledBorder("Comandos")); // "Fieldset" Comandos

		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(panelDispositivo, BorderLayout.NORTH);
		contentPane.add(panelComandos, BorderLayout.SOUTH);

		setContentPane(contentPane);

		// Botão Adicionar
		JButton btnAdicionarDispositivo = new JButton("Adicionar");
		btnAdicionarDispositivo.setIcon(new ImageIcon("img/circle_black.png"));
		btnAdicionarDispositivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean volta = true;
				JTextField tf = new JTextField();
				JPasswordField pf = new JPasswordField();
				int pass = 0;

				while(volta){

					int okCxxl = JOptionPane.showConfirmDialog(null, tf, "Insira o MAC Adress", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(tf.getText().length() != 17)
						JOptionPane.showMessageDialog(null, "Insira o MAC corretamente. São 17 caracteres contando com dois pontos(:)");
					else
						volta = false;
				}
				volta = true;

				while(volta){
					try{
						// O componente JPasswordField é passado ao JOptionPane para esconder os caracteres digitados
						int okCxl = JOptionPane.showConfirmDialog(null, pf, "Senha (apenas números)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
						System.out.println(String.valueOf(pf.getPassword()));
						if(pf.getPassword().toString().length() == 0);
						else{
							volta = false;
							pass = Integer.parseInt(String.valueOf(pf.getPassword()));
						}
					}
					catch(Exception e){
						volta = true;
						JOptionPane.showMessageDialog(null, "Insira apenas números!");
					}
				}
				// Aqui são passados os valores para o novo dispositivo e o mesmo ganha um ID quando é salvo no banco de dados
				Dispositivo dispositivo = new Dispositivo(tf.getText(), pass);
				dispositivo.setStatus("ATIVO");
				BLLDispositivo bllDispositivo = new BLLDispositivo();
				bllDispositivo.salvar(dispositivo);
				JOptionPane.showMessageDialog(null, "Dispositivo Cadastrado");
			}
		});
		panelDispositivo.setLayout(new GridLayout(0, 4, 0, 0));
		panelDispositivo.add(btnAdicionarDispositivo);

		// Botão Desativar Dispositivos - Após inserida a ID, o dispositivo recebe status de INATIVO
		JButton btnDesativarDispositivo = new JButton("Desativar");
		btnDesativarDispositivo.setIcon(new ImageIcon("img/block_black.png"));
		btnDesativarDispositivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextField tf = new JTextField();
				boolean volta = true;
				int id = 0;

				while(volta){
					try{
						int okCxxl = JOptionPane.showConfirmDialog(null, tf, "Insira a ID ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
						id = Integer.parseInt(String.valueOf(tf.getText()));
						volta = false;
					}
					catch(Exception e){
						volta = true;
						JOptionPane.showMessageDialog(null, "Insira apenas números!");
					}
				}

				BLLDispositivo bllDispositivo = new BLLDispositivo();
				Dispositivo dispositivo = bllDispositivo.buscarPorCodigo(id);
				if(dispositivo == null) {// Se nenhum objeto do tipo Dispositivo for retornado, então não se faz alteração no status
					JOptionPane.showMessageDialog(null, "A ID inserida não existe no banco de dados.");
				} 
				else {
					// Se o dispositivo estiver como ATIVO ele entra no if e é setado como INATIVO...
					if(dispositivo.getStatus().equals("ATIVO")) {
						dispositivo.setStatus("INATIVO");
						bllDispositivo.update(dispositivo);
						JOptionPane.showMessageDialog(null, "Dispositivo Desativado");
					}
					else { // ...caso contrário
						JOptionPane.showMessageDialog(null, "Dispositivo já se encontra desativado.");
					}
				}
			}
		});
		panelDispositivo.add(btnDesativarDispositivo);

		// Botão Reativar Dispositivos - Após inserida a ID, o dispositivo recebe status de ATIVO
		JButton btnReativarDispositivo = new JButton("Reativar");
		btnReativarDispositivo.setIcon(new ImageIcon("img/undo_black.png"));
		btnReativarDispositivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextField tf = new JTextField();
				boolean volta = true;
				int id = 0;

				while(volta){
					try{
						int okCxxl = JOptionPane.showConfirmDialog(null, tf, "Insira a ID ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
						id = Integer.parseInt(String.valueOf(tf.getText()));
						volta = false;
					}
					catch(Exception e){
						volta = true;
						JOptionPane.showMessageDialog(null, "Insira apenas números!");
					}
				}

				BLLDispositivo bllDispositivo = new BLLDispositivo();
				Dispositivo dispositivo = bllDispositivo.buscarPorCodigo(id);
				if(dispositivo == null) {
					JOptionPane.showMessageDialog(null, "A ID inserida não existe no banco de dados.");
				} 
				else {
					if(dispositivo.getStatus().equals("INATIVO")) {
						dispositivo.setStatus("ATIVO");
						bllDispositivo.update(dispositivo);
						JOptionPane.showMessageDialog(null, "Dispositivo Reativado");
					}
					else {
						JOptionPane.showMessageDialog(null, "Dispositivo já se encontra ativado.");
					}
				}
			}
		});
		panelDispositivo.add(btnReativarDispositivo);


		// Botão Listar Dispositivos - Abre uma tabela com informações do dispositivo
		JButton btnListarDispositivos = new JButton("Listar");
		btnListarDispositivos.setIcon(new ImageIcon("img/android_black.png"));
		btnListarDispositivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DefaultTableModel dtm = new DefaultTableModel() {
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};

				dtm.addColumn("Dispositivo");
				dtm.addColumn("MAC Adress");
				dtm.addColumn("Status");

				// Popula a tabela
				BLLDispositivo bllDispositivo = new BLLDispositivo();
				List<Dispositivo> list = bllDispositivo.listar();
				for(int i = 0; i < list.size(); i++){
					dtm.addRow(new String[] {String.valueOf(list.get(i).getCodigo()), list.get(i).getMac(), list.get(i).getStatus()});

				}

				// Cria uma nova janela e exibe a tabela
				JDialog dialog = new JDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setSize(400, 400);
				dialog.setResizable(false);
				dialog.setLocationRelativeTo(null);
				dialog.setTitle("Dispositivos Cadastrados");
				dialog.setModal(false);
				dialog.setVisible(true);

				JTable tabela = new JTable(dtm);
				tabela.setPreferredScrollableViewportSize(new Dimension(400,400));
				tabela.getColumnModel().getColumn(0).setPreferredWidth(70);
				tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
				tabela.getColumnModel().getColumn(2).setPreferredWidth(100); 

				tabela.setVisible(true);

				JScrollPane scroll = new JScrollPane(tabela);

				dialog.getContentPane().add(scroll);
			}
		});
		panelDispositivo.add(btnListarDispositivos);

		// Botão Listar Histórico
		JButton btnListarHistorico = new JButton("Listar Histórico");
		btnListarHistorico.setIcon(new ImageIcon("img/view_list_black.png"));
		btnListarHistorico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BLLHistorico bllHistorico = new BLLHistorico();
				List<Historico> list = bllHistorico.listar();

				if(list.size() > 0){

					DefaultTableModel dtm = new DefaultTableModel() {
						public boolean isCellEditable(int row, int column) {
							return false;
						}
					};

					dtm.addColumn("Dispositivo");
					dtm.addColumn("Comando");
					dtm.addColumn("Data");

					// Popula a tabela
					for(int i = 0; i < list.size(); i++){
						dtm.addRow(new String[] {String.valueOf(list.get(i).getCodigoDispositivo()), list.get(i).getParametro(), String.valueOf(list.get(i).getDataComando().toLocaleString().substring(0, 11))});
					}

					// Cria uma nova janela e exibe a tabela
					JDialog dialog = new JDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setSize(600, 600);
					dialog.setResizable(false);
					dialog.setLocationRelativeTo(null);
					dialog.setTitle("Histórico de Comandos");
					dialog.setModal(false);
					dialog.setVisible(true);

					JTable tabela = new JTable(dtm);
					tabela.getColumnModel().getColumn(0).setPreferredWidth(70);
					tabela.getColumnModel().getColumn(1).setPreferredWidth(400); 
					tabela.getColumnModel().getColumn(2).setPreferredWidth(70);
					JScrollPane scroll = new JScrollPane(tabela);

					tabela.setVisible(true);

					dialog.getContentPane().add(scroll);
				}
				else {
					JOptionPane.showMessageDialog(null, "Não há dados no histórico.");
				}
			}
		});
		panelComandos.setLayout(new GridLayout(0, 3, 0, 0));
		panelComandos.add(btnListarHistorico);

		// Botão Apagar Histórico
		JButton btnApagarHistorico = new JButton("Apagar Histórico");
		btnApagarHistorico.setIcon(new ImageIcon("img/delete_black.png"));
		btnApagarHistorico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BLLHistorico bllHistorico = new BLLHistorico();
				bllHistorico.excluir(new Historico());

				JOptionPane.showMessageDialog(null, "Histórico Apagado!");
			}
		});
		panelComandos.add(btnApagarHistorico);


		// Botão Commitar Logs - GitHub
		JButton btnCommitarLogs = new JButton("Commitar Logs");
		btnCommitarLogs.setIcon(new ImageIcon("img/git.png"));
		btnCommitarLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					commitarLogs();
				} catch (IOException | GitAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panelComandos.add(btnCommitarLogs);

	}

	public void commitarLogs() throws IOException, NoFilepatternException, GitAPIException{
		JGitControl gitControl = new JGitControl();

		//Adiciona os arquivos no repositório
		gitControl.addToRepo();
		//Commita com uma mensagem
		gitControl.commitToRepo("Logs");
		//Push - sobe os arquivos
		gitControl.pushToRepo();

		JOptionPane.showMessageDialog(null, "O commit foi feito com sucesso!\ncommit: " + gitControl.getIdCommit());
	}




}
