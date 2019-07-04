package br.edu.utfpr;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import br.edu.utfpr.gerador.GeradorClassSelenium;
import br.edu.utfpr.util.Util;
import br.edu.utfpr.view.FramePreferencias;

/**
 * 
 * @author Romulo
 * @since 22/08/2017
 */
public class FramePrincipal extends JFrame {
	private JMenuBar menuBar = new JMenuBar();		
	private GeradorClassSelenium geradorValoresAleatorios = new GeradorClassSelenium();			
	private Map<String, String> mapPreferencia = new HashMap<>();
	
	
	public FramePrincipal() {
		JFrame tela = new JFrame();
		tela.setLayout(new BorderLayout());
		
		tela.setTitle("Morpheus");
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		montarMenu();		
		
		lerPreferencias();	
		
		tela.getContentPane().add(menuBar, BorderLayout.NORTH);
		tela.getContentPane().add(getPainelCentral(), BorderLayout.CENTER);
		
		tela.setVisible(true);
		tela.setSize(440,450);
		tela.setLocationRelativeTo(null);	
	}	
	private void lerPreferencias() {
		BufferedReader bufferLocal = new Util().getBufferDadosDoArquivo();		
		if (!(bufferLocal == null)) {
			try {				
				bufferLocal.readLine();
				while (bufferLocal.ready()) {
					String linha = bufferLocal.readLine();
					mapPreferencia.put(linha.substring(0, linha.indexOf(":")), linha.substring(linha.indexOf(":")+1,linha.indexOf(";")));					
				}				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo preferencias.","Erro.", JOptionPane.ERROR_MESSAGE);	
				e.printStackTrace();
			}
		}
	}

	private JPanel getPainelCentral() {		
		JPanel pAcao = new JPanel(new BorderLayout());
		ImageIcon imageFundo = new ImageIcon(getClass().getResource("/principal.png"));
		JLabel lbl = new JLabel(imageFundo);
		
		JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btGerar = new JButton("Gerar");
		btGerar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				lerPreferencias();//ler novamente pois pode ter sido alterado.....
				geradorValoresAleatorios.gerarSelenium(mapPreferencia);	
				JOptionPane.showMessageDialog(null, "Geração realizada com sucesso.");
			}
		});
		JButton btSair = new JButton("Sair");
		btSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
		});
		
		pBotoes.add(btGerar);
		pBotoes.add(btSair);
		
		pAcao.add(lbl, BorderLayout.CENTER);
		pAcao.add(pBotoes, BorderLayout.SOUTH);
		return pAcao;
	}

	private void montarMenu() {
		JMenu menuArquivo = new JMenu("Arquivo");
		menuArquivo.setMargin(new Insets(2, 8, 2, 8));
		menuArquivo.setBackground(SystemColor.control);		
		
		JMenuItem menuSair = new JMenuItem("Sair");	
		JMenuItem menuPreferencia = new JMenuItem("Preferência");
		
		menuArquivo.add(menuSair);
		menuArquivo.add(menuPreferencia);
		
		menuBar.setFont(new Font("Calibri", Font.PLAIN, 12));
		menuBar.setBorder(null);
		menuBar.setBackground(SystemColor.control);
		
		menuBar.add(menuArquivo);
		
		menuSair.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuPreferencia.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FramePreferencias(mapPreferencia);				
			}
		});
		setJMenuBar(menuBar);
	}		
	
	public void visualizarTestSelenium(GeradorClassSelenium gerador) {
		JFrame tela = new JFrame();		
		tela.setTitle("Arquivo Test Selenium");
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		JTextArea lblTestSelenium = new JTextArea("");		
		lblTestSelenium.setText(gerador.getSbSelenium().toString());
		
		JScrollPane js = new JScrollPane(lblTestSelenium);
		
		JLabel lblTitulo = new JLabel("Arquivo Selenium.........");
		
		tela.getContentPane().add(lblTitulo, BorderLayout.NORTH);
		tela.getContentPane().add(js, BorderLayout.CENTER);
		
		tela.setVisible(true);
		tela.setSize(700, 500);
		//tela.pack();
		tela.setLocationRelativeTo(null);	
	}
	
	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {		
			e.printStackTrace();
		} 
		new FramePrincipal();
	}
}