package br.edu.utfpr.gerador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;


/**
 * 
 * @author Romulo
 * @since 24/07/2017
 */
public class GeradorClassSelenium {

	private Element elementForm = null;	
	private StringBuilder sbSelenium = new StringBuilder();	
	private String nomeDoTeste;
	private String diretorioScreenShots;	
	private String diretorioJava;
	private String url;	
	private boolean isPaginaLista;
	private List<File> fileList = new ArrayList<>();

	/**
	 * Gerar Teste Selenium com valores aleatorios
	 * @param caminhoPagina
	 */
	public void gerarSelenium(Map<String, String> mapPreferencia) {	
		this.diretorioScreenShots = mapPreferencia.get("DiretorioScreenShot");			
		this.diretorioJava = mapPreferencia.get("DiretorioArquivoJava");
		this.url = mapPreferencia.get("URL");
		
		if (mapPreferencia.get("Lista").equals("true")) {
			isPaginaLista = true;
		}

		//if (url.contains("?id") || isPaginaLista) {
		//	gerarTestSelenium(null, url.substring(url.lastIndexOf("/")+1, url.indexOf("xhtml")-1));
		//	gerarArquivo();
		//} else {
			//File fileSelecionado = new File(mapPreferencia.get("DiretorioXHTML"));
			fileList.clear();
			List<File> l = listarArquivos(new File(mapPreferencia.get("DiretorioXHTML")));
			for (File arquivo : l) {
				if (arquivo.getAbsolutePath().endsWith(".xhtml")) {					
					System.out.println("Gerando o arquivo....:"+arquivo.getName());					
					gerarTestSelenium(arquivo.getAbsolutePath(), arquivo.getName().substring(0, arquivo.getName().indexOf(".")));
					gerarArquivo();
				}	
			}
			//for (File f : fileSelecionado.listFiles()) {
				//if (f.getAbsolutePath().endsWith(".xhtml")) {
				//	gerarTestSelenium(f.getAbsolutePath(), f.getName().substring(0, f.getName().indexOf(".")));
				//	gerarArquivo();
				//}				
			//}
		//}					
	}
	
	private void gerarArquivo() {
		File arquivo = new File(diretorioJava+File.separator+"Teste"+nomeDoTeste.substring(0, 1).toUpperCase()+nomeDoTeste.substring(1)+".java");
		
		try {
			Writer fileWriter = new OutputStreamWriter(new FileOutputStream(arquivo),
					"ISO-8859-1");
			fileWriter.write(sbSelenium.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ao final
		sbSelenium = new StringBuilder();
	}

	private void gerarTestSelenium(String caminhoPagina, String nomeDoTeste) {
		//caminho da pagina.....
		//C:\Users\Romulo\OneDrive\Documentos\crm_mdt\paginas\index.xhtml		
		this.nomeDoTeste = nomeDoTeste;
		SAXReader reader = new SAXReader(new DOMDocumentFactory());			
		Document document = null;
		try {
			//capturar screenshot antes e depois
			//takescreenshot - selenium
			
			//caminho = this.getClass().getResource("/index.xhtml").toURI().getPath();
			//if (!url.contains("?id") && !isPaginaLista) {
				document = reader.read(caminhoPagina);
			//}			
			//gerar dados da declaracao
			gerarDadosDeclaracao();
			if (!nomeDoTeste.contains("index")) {
				gerarLogar();
			}
			//if (url.contains("?id") || isPaginaLista) {
			//	sbSelenium.append(gerarTestePaginaComParametro());
			//} else {
				//gerar testes com strings aleatorias
				String sAleatoria = gerarTestesStringAleatoria(document.getRootElement());			
				sbSelenium.append(sAleatoria.toString());
				//gerar testes com strings vazia
				String sVazia = gerarTestesStringVazia(document.getRootElement());
				sbSelenium.append(sVazia.toString());
				//gerar testes com numeros inteiros aletorios
				String sInteirosAleatorios = gerarTestesInteirosAleatorios(document.getRootElement());
				sbSelenium.append(sInteirosAleatorios.toString());	
			//}				
			
			//capturar screenshot antes e depois
			//takescreenshot - selenium			
			capturarScreenShot();			
			//ao final fechar todos
			sbSelenium.append("}\r\n");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possível ler Página xhtml especificada");			
		}			
	}

	private void capturarScreenShot() {
		sbSelenium.append("\tpublic static void capturarScreenShot(String caminho) {\r\n");
		sbSelenium.append("\t\ttry {\r\n");
		sbSelenium.append("\t\t\tTakesScreenshot scrShot =((TakesScreenshot)driver);\r\n");	            
	    sbSelenium.append("\t\t\tFile SrcFile=scrShot.getScreenshotAs(OutputType.FILE);\r\n");	            
	    sbSelenium.append("\t\t\tFile DestFile=new File(caminho);\r\n");	            
	    sbSelenium.append("\t\t\tFileUtils.copyFile(SrcFile, DestFile);\r\n");
	    sbSelenium.append("\t\t} catch (Exception e) {\r\n");
	    sbSelenium.append("\t\t\te.printStackTrace();\r\n");
		sbSelenium.append("\t\t}\r\n");		
		sbSelenium.append("\t}\r\n");		
	}

	private void gerarDadosDeclaracao() {
		sbSelenium.append("package test.java;\r\n\r\n");	
		
		sbSelenium.append("import static org.junit.Assert.*;\r\n");
		sbSelenium.append("import java.io.File;\r\n");
		sbSelenium.append("import org.junit.AfterClass;\r\n");
		sbSelenium.append("import org.junit.BeforeClass;\r\n");
		sbSelenium.append("import org.junit.Test;\r\n");
		sbSelenium.append("import org.openqa.selenium.By;\r\n");
		sbSelenium.append("import org.openqa.selenium.OutputType;\r\n");
	    sbSelenium.append("import org.openqa.selenium.TakesScreenshot;\r\n");
	    sbSelenium.append("import org.openqa.selenium.WebDriver;\r\n");
	    sbSelenium.append("import org.openqa.selenium.WebElement;\r\n");
	    sbSelenium.append("import org.openqa.selenium.firefox.FirefoxDriver;\r\n");
	    sbSelenium.append("import org.apache.commons.io.FileUtils;\r\n\r\n");
		
		sbSelenium.append("public class Teste"+nomeDoTeste.substring(0, 1).toUpperCase()+nomeDoTeste.substring(1)+" {\r\n");
		sbSelenium.append("\t// Declarando um objeto do tipo WebDriver, utilizado pelo Selenium WebDriver.\r\n");
		sbSelenium.append("\tprivate static WebDriver driver;\r\n\r\n");
		
		sbSelenium.append("\t@BeforeClass\r\n");
		sbSelenium.append("\tpublic static void setUpTest(){\r\n");
		sbSelenium.append("\t\tSystem.setProperty(\"webdriver.gecko.driver\", \"D:\\\\Romulo\\\\mestrado UTFPR\\\\geckodriver\\\\geckodriver.exe\");\r\n");	
		
		//TODO
		//sbSelenium.append("\t\tSystem.setProperty(\"webdriver.gecko.driver\", \""+diretorioWebDriver.replace(System.getProperty("file.separator"), "\\")+"\");\r\n");			
		sbSelenium.append("\t\tdriver = new FirefoxDriver();\r\n");
		
		if (!nomeDoTeste.contains("index")) {
			sbSelenium.append("\t\tlogar();\r\n");			
		} else {
			sbSelenium.append("\t\tdriver.get(\""+url+nomeDoTeste+".xhtml\");\r\n");	
			//TODO
			//sbSelenium.append("\t\t driver.get(\"http://localhost:8080/helpDesk_2/index.xhtml\");\r\n");	
			sbSelenium.append("\t\tcapturarScreenShot(\""+diretorioScreenShots+File.separator+nomeDoTeste+"_antes.png\");\r\n");	
		}		
		sbSelenium.append("\t}\r\n\r\n");
		
		sbSelenium.append("\t@AfterClass\r\n");
		sbSelenium.append("\tpublic static void tearDownTest(){\r\n");
		sbSelenium.append("\t\tdriver.quit();\r\n");
		sbSelenium.append("\t}\r\n\r\n");
	}
	
	private void gerarLogar() {
		sbSelenium.append("\tpublic static void logar() {\r\n");
		sbSelenium.append("\t\tdriver.get(\"http://localhost:8080/helpDesk/index.xhtml\");\r\n");
		sbSelenium.append("\t\tWebElement element = driver.findElement(By.name(\"Login\"));\r\n");		
		sbSelenium.append("\t\telement.sendKeys(\"admin\");\r\n");	
		sbSelenium.append("\t\telement = driver.findElement(By.name(\"Senha\"));\r\n");			
		sbSelenium.append("\t\telement.sendKeys(\"a\");\r\n");		
		sbSelenium.append("\t\tdriver.findElement(By.id(\"btEntrar\")).click();\r\n");	
		sbSelenium.append("\t}\r\n");		
	}
	
	//public String gerarTestePaginaComParametro() {				
	//	StringBuilder sb = new StringBuilder();		
	//	try {	
	//		//testar a pagina com strings aleatorias
	//		sb.append("\t @Test\r\n");				
	//		sb.append("\t public void "+nomeDoTeste+"() {\r\n");				
	//		sb.append("\t\t driver.get(\""+url+"\");\r\n");						
				
	//		sb.append("\t\t try {\r\n");
	//		sb.append("\t\t\t Thread.sleep(3000);\r\n");
	//		sb.append("\t\t} catch (InterruptedException e) {\r\n");			
	//		sb.append("\t\t\t e.printStackTrace();\r\n");
	//		sb.append("\t\t }\r\n");
	//		sb.append("\t\t capturarScreenShot(\""+diretorioScreenShots+File.separator+nomeDoTeste+"_telaComParametro.png\");\r\n");
			//sb.append("\t\tcapturarScreenShot(\"D:\\capturarStringAleatoria.png\");\r\n");	
	//		sb.append("\t }\r\n");					
	//	} catch (Exception e) {			
	//		e.printStackTrace();
	//	}
	//	return sb.toString();       
   // }

	public String gerarTestesStringAleatoria(Element rootElement) {
		obterElementForm(rootElement);		
		StringBuilder sb = new StringBuilder();		
		try {	
			//testar a pagina com strings aleatorias
			sb.append("\t @Test\r\n");				
			sb.append("\t public void "+nomeDoTeste+"() {\r\n");
			//sb.append("\t\t driver.get(\"http://localhost:8080/helpDesk_2/"+nomeDoTeste+".xhtml\");\r\n");	
			sb.append("\t\t driver.get(\""+url+"/"+nomeDoTeste+".xhtml\");\r\n");
			sb.append("\t\t WebElement element = null;\r\n");	
			
			if (elementForm != null) {
				sb.append(gerar(elementForm, true, false, false).toString());
			}	
			sb.append("\t\t try {\r\n");
			sb.append("\t\t\t Thread.sleep(3000);\r\n");
			sb.append("\t\t} catch (InterruptedException e) {\r\n");			
			sb.append("\t\t\t e.printStackTrace();\r\n");
			sb.append("\t\t }\r\n");
			sb.append("\t\t capturarScreenShot(\""+diretorioScreenShots+File.separator+nomeDoTeste+"_string_aleatoria.png\");\r\n");
			//sb.append("\t\tcapturarScreenShot(\"D:\\capturarStringAleatoria.png\");\r\n");	
			sb.append("\t }\r\n");					
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return sb.toString();       
    }
	
	private String gerarTestesInteirosAleatorios(Element rootElement) {
		obterElementForm(rootElement);		
		StringBuilder sb = new StringBuilder();		
		try {	
			//testar a pagina com strings aleatorias
			sb.append("\t @Test\r\n");				
			sb.append("\t public void "+nomeDoTeste+"InteiroAleatorio() {\r\n");
			//sb.append("\t\t driver.get(\"http://localhost:8080/helpDesk_2/"+nomeDoTeste+".xhtml\");\r\n");
			sb.append("\t\t driver.get(\""+url+nomeDoTeste+".xhtml\");\r\n");
			sb.append("\t\t WebElement element = null;\r\n");		
			
			if (elementForm != null) {
				sb.append(gerar(elementForm, false, false, true).toString());
			}	
			sb.append("\t\t try {\r\n");
			sb.append("\t\t\t Thread.sleep(3000);\r\n");
			sb.append("\t\t} catch (InterruptedException e) {\r\n");			
			sb.append("\t\t\t e.printStackTrace();\r\n");
			sb.append("\t\t }\r\n");
			sb.append("\t\t capturarScreenShot(\""+diretorioScreenShots+File.separator+nomeDoTeste+"_inteiro_aleatorio.png\");\r\n");			
			sb.append("\t }\r\n");				
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return sb.toString();       
    }
	
	private String gerarTestesStringVazia(Element rootElement) {
		obterElementForm(rootElement);		
		StringBuilder sb = new StringBuilder();		
		try {	
			//testar a pagina com strings aleatorias
			sb.append("\t @Test\r\n");				
			sb.append("\t public void "+nomeDoTeste+"StringVazio() {\r\n");
			//sb.append("\t\t driver.get(\"http://localhost:8080/helpDesk_2/"+nomeDoTeste+".xhtml\");\r\n");
			sb.append("\t\t driver.get(\""+url+nomeDoTeste+".xhtml\");\r\n");
			sb.append("\t\t WebElement element = null;\r\n");		
			
			if (elementForm != null) {
				sb.append(gerar(elementForm, false, true, false).toString());
			}	
			sb.append("\t\t try {\r\n");
			sb.append("\t\t\t Thread.sleep(3000);\r\n");
			sb.append("\t\t} catch (InterruptedException e) {\r\n");			
			sb.append("\t\t\t e.printStackTrace();\r\n");
			sb.append("\t\t }\r\n");
			sb.append("\t\t capturarScreenShot(\""+diretorioScreenShots+File.separator+nomeDoTeste+"_string_vazia.png\");\r\n");
			//sb.append("\t\tcapturarScreenShot(\"D:\\capturarStringVazia.png\");\r\n");	
			sb.append("\t }\r\n");				
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return sb.toString();       
    }	
	
	
	@SuppressWarnings("rawtypes")
	public String gerarSeleniumInteiroAleatorio(Element element) {		
		StringBuilder codigo = new StringBuilder();
		for (Iterator it = element.elementIterator(); it.hasNext(); ) {
			Element elemento = (Element) it.next();	
			String id = elemento.attributeValue("id");					
			if (elemento.getName().equals("inputText") || elemento.getName().equals("password")) {					
				codigo.append("\t\t element = driver.findElement(By.id(\""+id+"\"));\r\n");				
				int maxLength= Integer.parseInt(elemento.attribute("maxlength").getValue());				
				codigo.append("\t\t element.sendKeys(\""+obterNumeroAleatorio(maxLength)+"\");\r\n");					
			} else if (elemento.getName().contains("Button")) {
				codigo.append("\t\t driver.findElement(By.id(\""+id+"\")).click();\r\n");			   
			} else {
				codigo.append(gerarSeleniumInteiroAleatorio(elemento));				
			}			
		}	
		return codigo.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public String gerarSeleniumTextoAleatorio(Element element) {		
		StringBuilder cod = new StringBuilder();
		for (Iterator it = element.elementIterator(); it.hasNext(); ) {
			Element el = (Element) it.next();	
			String id = el.attributeValue("id");					
			if (el.getName().equals("inputText")) {					
				cod.append("element = driver.findElement(By.id(\""+id+"\"));");				
				int length= Integer.parseInt(el.attribute("maxlength").getValue());				
				cod.append("element.sendKeys(\""+obterNumeroAleatorio(length)+"\");");					
			} else if (el.getName().contains("Button")) {
				cod.append("driver.findElement(By.id(\""+id+"\")).click();");			   
			} else {
				cod.append(gerarSeleniumInteiroAleatorio(el));				
			}			
		}	
		return cod.toString();
	}
	
	
	
	/**
	 * 
	 * @param element -  o element a ser gerado
	 * @param gerarTesteStringAleatorio - true se for para gerar string aleatoria
	 * @param gerarTesteStringVazio - true se for para gerar string vazia
	 * @param gerarTesteInteiroAleatorio - true se for para gerar metodo com numeros inteiros aleatorios
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String gerar(Element element, boolean gerarTesteStringAleatorio, boolean gerarTesteStringVazio, boolean gerarTesteInteiroAleatorio) {		
		StringBuilder ss = new StringBuilder();
		for (Iterator it = element.elementIterator(); it.hasNext(); ) {
			Element elemento = (Element) it.next();				
			String id = elemento.attributeValue("id");
			//inputText, password, inputTextarea,ckEditor,inputSecret,editor,inputSecret			
			if (elemento.getName().equals("inputText") || elemento.getName().equals("password") || elemento.getName().equals("inputTextarea") 
					|| elemento.getName().equals("ckEditor") || elemento.getName().equals("inputSecret") || elemento.getName().equals("textarea") || 
					elemento.getName().equals("editor")) {					
				//se tiver um redered nao inserir texto
				if (elemento.attribute("rendered") != null) {					
					continue;
				} 
				if (elemento.getName().equals("editor")) {
					ss.append("\t\t element = driver.findElement(By.id(\""+id+"_input\"));\r\n");
				} else {
					ss.append("\t\t element = driver.findElement(By.id(\""+id+"\"));\r\n");
				}
				
				if (gerarTesteStringAleatorio) {
					if (elemento.attribute("onkeypress") != null) {
						ss.append("\t\t element.sendKeys(\""+obterNumeroAleatorio(2)+"\");\r\n");//valores para o teste...........
					} else {
						try {
							int maxLength = 0;
							if (elemento.getName().equals("ckEditor") || elemento.getName().equals("inputTextarea")) {
								maxLength = 50;
							} else {
								maxLength = Integer.parseInt(elemento.attribute("maxlength").getValue());
							}														
							ss.append("\t\t element.sendKeys(\""+obterStringAleatoria(maxLength)+"\");\r\n");//valores para o teste...........
						} catch (Exception e) {
							System.out.println("Verifique a propriedade maxlength no arquivo");
							//JOptionPane.showMessageDialog(null, elemento.getName()+" - ID: "+elemento.attribute("id").getValue()+" - não informado a propriedade maxlength", "Erro", JOptionPane.ERROR_MESSAGE);
						}
					}									
				} else if (gerarTesteStringVazio) {											
					ss.append("\t\t element.sendKeys(\"\");\r\n");//valores para o teste...........
				} else if (gerarTesteInteiroAleatorio) {	
					int maxLength = 0;
					if (elemento.getName().equals("ckEditor") || elemento.getName().equals("inputTextarea")) {
						maxLength = 50;
					} else {
						try {
							maxLength= Integer.parseInt(elemento.attribute("maxlength").getValue());						
						} catch (Exception e) {							
							System.out.println("Verifique a propriedade maxlength do arquivo: -Não foi informado a propriedade maxlength");
							//JOptionPane.showMessageDialog(null, elemento.getName()+" - ID: "+elemento.attribute("id").getValue()+" - não informado a propriedade maxlength", "Erro", JOptionPane.ERROR_MESSAGE);
							continue;
						}
					}
					ss.append("\t\t element.sendKeys(\""+obterNumeroAleatorio(maxLength)+"\");\r\n");//valores para o teste...........
				}						
			// inputMask
			} else if (elemento.getName().equals("inputMask")) {
				//TODO se o campo for CPF, CNPJ, CEP,INCRA, CEI,TELEFONE, DDD......Como gerar?????????????????
				ss.append("\t\t element = driver.findElement(By.id(\""+id+"\"));\r\n");
				if (gerarTesteStringAleatorio) {											
					ss.append("\t\t element.sendKeys(\"add\");\r\n");//valores para o teste...........
				} else if (gerarTesteStringVazio) {													
					ss.append("\t\t element.sendKeys(\"\");\r\n");//valores para o teste...........
				} else if (gerarTesteInteiroAleatorio) {
					String mask = (elemento.attribute("mask").getValue());							
					ss.append("\t\t element.sendKeys(\""+mask+"\");\r\n");//valores para o teste...........					
				}
			// Button, Radio	
			} else if (elemento.getName().contains("Button") || elemento.getName().contains("radio")) {
				ss.append("\t\t driver.findElement(By.id(\""+id+"\")).click();\r\n");				
		    // Link
			} else if (elemento.getName().contains("Link")) {
				//ss.append("\t\t driver.findElement(By.linkText(\""+id+"\")).click();\r\n");							
			//CheckBox, qualquer tipo de checkBox
			} else if (elemento.getName().contains("Checkbox")) {
				ss.append("\t\t element = driver.findElement(By.id(\""+id+"\"));\r\n");
				ss.append("\t\t element.click();\r\n");					
			} else if (elemento.getName().contains("calendar")) {
				ss.append("\t\t element = driver.findElement(By.id(\""+id+"_input\"));\r\n");
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				if (gerarTesteStringAleatorio) {
					String dataHoje = formato.format(new Date());
					ss.append("\t\t element.sendKeys(\""+dataHoje+"\");\r\n");//valores para o teste...........
				} else if (gerarTesteStringVazio) {													
					ss.append("\t\t element.sendKeys(\"\");\r\n");//valores para o teste...........
				} else if (gerarTesteInteiroAleatorio) {												
					ss.append("\t\t element.sendKeys(\"111111\");\r\n");//valores para o teste...........					
				}
			} else {
				ss.append(gerar(elemento, gerarTesteStringAleatorio, gerarTesteStringVazio, gerarTesteInteiroAleatorio).toString());				
			}			
		}	
		return ss.toString();
	}	
	
	@SuppressWarnings("rawtypes")
	private Element obterElementForm(Element element) {		
		for (Iterator it = element.elementIterator(); it.hasNext(); ) {
			Element elemento = (Element) it.next();				
			if (elemento.getName().equals("form")) {
				//TODO todo FORM tem que TER um ID, e ser preprendID false
				//TODO e se a página tiver + de 1 form...????
				if (elemento.attribute("id") != null) {
					elementForm = elemento;
					break;
				}			
			} else {				
				obterElementForm(elemento);//chama recursivo ate achar a tag form		
			}
		}
		return null;
	}
	
	private long obterNumeroAleatorio(int tamanho) {
		//instância um objeto da classe Random usando o construtor padrão
		Random gerador = new Random();			    
		//imprime sequência de 10 números inteiros aleatórios
		String numAleatorio = "";
		if (tamanho > 10) {//nao gerar com numeros inteiros com + de 10 digitos
			tamanho = 10;
		}
		for (int i = 0; i < tamanho; i++) {
			numAleatorio += gerador.nextInt(9)+"";			
		}
		long retorno = 0;
		try {
			retorno = Long.parseLong(numAleatorio);
		}catch (Exception e) {
			e.printStackTrace();
			retorno = 0;
		}
		return retorno;
	}
	
	private String obterStringAleatoria(int tamanhoString) {
		int limitEsquerda = 97; // letra 'a'
		int limitDireita = 122; // letra 'z'		
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(tamanhoString);
		for (int i = 0; i < tamanhoString; i++) {
			int valorRandom = limitEsquerda + (int)(random.nextFloat() * (limitDireita - limitEsquerda + 1));
		    buffer.append((char) valorRandom);
		}		 
		return  buffer.toString();
	}

	public StringBuilder getSbSelenium() {
		return sbSelenium;
	}	
	
	private List<File> listarArquivos(File source) {	   
	    File[] list = source.listFiles();
	    for (File fl : list) {
	        if (!fl.isDirectory()) {
	        	if (fl.getName() != null && fl.getName().contains("xhtml")) {
	        		fileList.add(fl);
	        	}
	        }else{
	            listarArquivos(fl);
	        }
	    }
	    return fileList;
	}
	
	@SuppressWarnings("rawtypes")
	public String gerarTextoAleatorio(Element element) {		
		StringBuilder cod = new StringBuilder();
		for (Iterator it = element.elementIterator(); it.hasNext(); ) {
			Element el = (Element) it.next();	
			String id = el.attributeValue("id");					
			if (el.getName().equals("inputText")) {					
				cod.append("element = driver.findElement(By.id(\""+id+"\"));");				
				int length= Integer.parseInt(el.attribute("maxlength").getValue());				
				cod.append("element.sendKeys(\""+obterNumeroAleatorio(length)+"\");");					
			} else if (el.getName().contains("Button")) {
				cod.append("driver.findElement(By.id(\""+id+"\")).click();");			   
			} else {
				cod.append(gerarSeleniumInteiroAleatorio(el));				
			}			
		}	
		return cod.toString();
	}
}