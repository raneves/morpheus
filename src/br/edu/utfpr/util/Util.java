package br.edu.utfpr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * 
 * @author Romulo
 * @since 22/09/2017
 */
public class Util {
	private File arquivoConfiguracao;
	private BufferedReader bufferedReader;
	
	/** Metodo responsavel por retornar um BufferedReader com os dados do arquivo(preferencias.txt) */
	public BufferedReader getBufferDadosDoArquivo() {
		arquivoConfiguracao = new File("C:\\morpheus\\preferencias.txt");
		try {
			if (!arquivoConfiguracao.exists()) {
				arquivoConfiguracao.createNewFile();
			}	
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoConfiguracao), "ISO-8859-1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedReader;
	}
	
	public void alterarPreferencias(String chave, String valor) {	
		BufferedReader bufferLocal = getBufferDadosDoArquivo();
		Map<String, String> mapPreferencia = new HashMap<>();
		if (!(bufferLocal == null)) {
			try {				
				bufferLocal.readLine();
				while (bufferLocal.ready()) {
					String linha = bufferLocal.readLine();					
					if (chave.equals(linha.substring(0, linha.indexOf(":")))){
						mapPreferencia.put(chave, valor+";\r\n");
					} else {
						mapPreferencia.put(linha.substring(0, linha.indexOf(":")), linha.substring(linha.indexOf(":")+1,linha.indexOf(";"))+";\r\n");					
					}			
				}				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo preferencias.","Erro.", JOptionPane.ERROR_MESSAGE);	
				e.printStackTrace();
			}
		}		
		
		File arquivo = new File("preferencias.txt");
		try {
			bufferLocal.readLine();
			StringBuilder parametros = new StringBuilder();
			parametros.append("\r\n");
			for (Map.Entry<String, String> map : mapPreferencia.entrySet()) {
				String chaveMap = map.getKey();
				String valueMap = map.getValue();
				parametros.append(chaveMap+":"+valueMap);
			}
			Writer fileWriter = new OutputStreamWriter(new FileOutputStream(arquivo), "ISO-8859-1");
			fileWriter.write(parametros.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro gravar o arquivo preferencias.","Erro.", JOptionPane.ERROR_MESSAGE);	
			e.printStackTrace();
		}
	}
}
