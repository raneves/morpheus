package br.edu.utfpr.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.edu.utfpr.util.Util;

/**
 * 
 * @author Romulo
 * @since 23/06/2017
 *
 */
public class FramePreferencias extends JDialog {
	private Map<String, String> mapPreferencia; 
	private JTextField txtDiretorio = new JTextField(30);
	private JTextField txtScreenshots = new JTextField(30);
	private JTextField txtGechoDrive = new JTextField(30);
	private JTextField txtJava = new JTextField(30);
	private JTextField txtUrl = new JTextField(35);	
	private JCheckBox btLista = new JCheckBox();
	private JFrame tela;
	
	public FramePreferencias(Map<String, String> mapPreferencia) {
		this.mapPreferencia = mapPreferencia;
		tela = new JFrame();
		tela.setLayout(new BorderLayout());
		
		tela.setTitle("Preferências do Gerador");						
		tela.getContentPane().add(painelPreferencias(), BorderLayout.CENTER);
		tela.getContentPane().add(painelBotoes(), BorderLayout.SOUTH);
		
		
		tela.setResizable(false);
		tela.setVisible(true);
		tela.setSize(670,380);
		tela.setLocationRelativeTo(null);
	}
	
	public JPanel painelPreferencias() {		
		JPanel painelPrincipal = new JPanel(new GridBagLayout());
		painelPrincipal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Dados para geração"));
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/folder_open.png"));
		
		JPanel painelXHTML = new JPanel();
		painelXHTML.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblDiretorio = new JLabel("Diretório das Páginas XHTML");		
		txtDiretorio.setEditable(false);
		txtDiretorio.setEnabled(false);
		txtDiretorio.setText(mapPreferencia.get("DiretorioXHTML"));
		JButton btSelecionar = new JButton(imageIcon);
		btSelecionar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				selecionarDiretorioOuPaginaXhtml();
			}
		});	
		painelXHTML.add(lblDiretorio);
		painelXHTML.add(txtDiretorio);
		painelXHTML.add(btSelecionar);
		
		//segundo passo
		JPanel painelScreenShots = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		JLabel lblSelecioneScrrenshots = new JLabel("Diretório para salvar os screenshots");		
		txtScreenshots.setEditable(false);
		txtScreenshots.setEnabled(false);
		txtScreenshots.setText(mapPreferencia.get("DiretorioScreenShot"));
		JButton btSelecionarScreenshots = new JButton(imageIcon);
		btSelecionarScreenshots.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				selecionarDiretorioImagensScreenShots();
			}
		});
		painelScreenShots.add(lblSelecioneScrrenshots);
		painelScreenShots.add(txtScreenshots);
		painelScreenShots.add(btSelecionarScreenshots);			
				
		//terceiro			
		JPanel painelGechoDriver = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		JLabel lblGechoDrive = new JLabel("Diretório da localização do geckodriver.exe");		
		txtGechoDrive.setEditable(false);
		txtGechoDrive.setEnabled(false);
		txtGechoDrive.setText(mapPreferencia.get("DiretorioGeckodriver"));
		JButton btSelecionarGechoDriver = new JButton(imageIcon);
		btSelecionarGechoDriver.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {				
				selecionarDiretorioWebDriver();
			}
		});		
		painelGechoDriver.add(lblGechoDrive);
		painelGechoDriver.add(txtGechoDrive);
		painelGechoDriver.add(btSelecionarGechoDriver);		
		
		//Java
		JPanel painelJava = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		JLabel lblSelecioneJ = new JLabel("Diretório para gravar os arquivos .java");
		JButton btSelecionarJ = new JButton(imageIcon);				
		txtJava.setEditable(false);
		txtJava.setEnabled(false);		
		txtJava.setText(mapPreferencia.get("DiretorioArquivoJava"));
		btSelecionarJ.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {				
				selecionarDiretorioTesteJava();	
			}
		});
		
		painelJava.add(lblSelecioneJ);
		painelJava.add(txtJava);
		painelJava.add(btSelecionarJ);	
		
		//URL
		JPanel painelURl = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		JLabel lblInformeUrl = new JLabel("Informe a URL");		
		txtUrl.setText(mapPreferencia.get("URL"));
		painelURl.add(lblInformeUrl);
		painelURl.add(txtUrl);	
		
		JPanel pCheck = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lbl = new JLabel("Página Lista");
		pCheck.add(lbl);
		pCheck.add(btLista);
		if (mapPreferencia.get("Lista").equals("true")) {
			btLista.setSelected(true);
		}
		
		GridBagConstraints cons = new GridBagConstraints();		
		cons.insets = new Insets(0, 0, 0, 5);
		cons.gridx = 0;
		cons.gridy = 0;		
		cons.anchor = GridBagConstraints.EAST;		
		painelPrincipal.add(painelXHTML, cons);
		
		cons.gridx = 0;
		cons.gridy = 1;			
		painelPrincipal.add(painelScreenShots, cons);		
		
		cons.gridx = 0;
		cons.gridy = 2;		
		painelPrincipal.add(painelGechoDriver, cons);				
		
		cons.gridx = 0;
		cons.gridy = 3;		
		painelPrincipal.add(painelJava, cons);
		
		cons.gridx = 0;
		cons.gridy = 4;			
		painelPrincipal.add(painelURl, cons);
		
		//cons.gridx = 0;
		//cons.gridy = 5;	
		
		//painelPrincipal.add(pCheck, cons);
		
		return painelPrincipal;
	}	
	private void selecionarDiretorioOuPaginaXhtml() {		
		JFileChooser fc = new JFileChooser();       
        fc.setDialogTitle("Selecionar um diretório contendo páginas XHTML");       
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(this);
        File arquivo = fc.getSelectedFile(); 
        if (arquivo.getPath() != null) {
        	txtDiretorio.setText(arquivo.getPath());
            new Util().alterarPreferencias("DiretorioXHTML", arquivo.getPath());       
            repaint();
        }        
	}	
	private void selecionarDiretorioImagensScreenShots() {		
		JFileChooser fc = new JFileChooser();       
        fc.setDialogTitle("Selecionar um diretório para armazenar os ScreenShots");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(this);
        File arquivo = fc.getSelectedFile();  
        if (arquivo.getPath() != null) {
        	txtScreenshots.setText(arquivo.getAbsolutePath());
        	new Util().alterarPreferencias("DiretorioScreenShot", arquivo.getPath());  
        	repaint();
        }       
	}	
	private void selecionarDiretorioWebDriver() {		
		JFileChooser fc = new JFileChooser();       
        fc.setDialogTitle("Selecionar o arquivo executavel geckodriver");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("Executável (*.exe)", "exe"));
        fc.showOpenDialog(this);
        File arquivo = fc.getSelectedFile();   
        if (arquivo.getPath() != null) {
        	txtGechoDrive.setText(arquivo.getAbsolutePath());
        	new Util().alterarPreferencias("DiretorioGeckodriver", arquivo.getPath());  
        	repaint();
        }          
	}
	private void selecionarDiretorioTesteJava() {		
		JFileChooser fc = new JFileChooser();       
        fc.setDialogTitle("Selecionar um diretório para armazenar os arquivos Test Java");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(this);
        File arquivo = fc.getSelectedFile();        
        if (arquivo.getPath() != null) {        	
        	txtJava.setText(arquivo.getAbsolutePath());
        	new Util().alterarPreferencias("DiretorioArquivoJava", arquivo.getPath());
            repaint();
        }        
	}	
	public JPanel painelBotoes() {
		JPanel pBt = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btSalvar = new JButton("Salvar");
		btSalvar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Util().alterarPreferencias("URL", txtUrl.getText().toString());
				new Util().alterarPreferencias("Lista", btLista.isSelected()+"");
				JOptionPane.showMessageDialog(null, "Salvo com sucesso.");
				tela.setVisible(false);
			}
		});
		pBt.add(btSalvar);		
		return pBt;
	}
}