import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;

/**
 * 
 */

/**
 * ECF utilitary that handle ECF files generated by RPA Automation and replaces points in accounts numbers.
 * @author Jaime Albunio
 * @since Jun 28, 2019
 * @version 20190830
 */
public class Corretor {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedWriter logs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("log.txt"), "UTF-8"));
		try {
		
		logs.append(new Date() + " -- Processamento iniciado.");
		logs.newLine();
		
		BufferedReader buffChaves = new BufferedReader(new InputStreamReader(new FileInputStream("contas.txt"), "ISO-8859-1"));
		BufferedReader buffECF = new BufferedReader(new InputStreamReader(new FileInputStream("ECF.txt"), "ISO-8859-1"));
		BufferedWriter buffOutECF = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ECF_Tratado.txt"), "ISO-8859-1"));
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		String line = buffChaves.readLine();
		while (line != null) {
			String novaConta = line.replace(".", "");
			map.put(novaConta, line);
			line = buffChaves.readLine();
		}
		int contador = 0;
		int contadorAlteracao = 0;
		
		line = buffECF.readLine();
		while (line != null) {
			contador++;
		
			if(line.startsWith("|M310") || line.startsWith("|M360")){
				contadorAlteracao++;
				String[] arr = line.split("\\|");
				String novaConta = map.get(arr[2]);
				if(novaConta != null){
					line = line.replace(arr[2], novaConta);
				}
			}
			
			buffOutECF.append(line);
			buffOutECF.newLine();
			line = buffECF.readLine();
		}
		
		buffOutECF.flush();
		buffOutECF.close();
		buffECF.close();
		buffChaves.close();
		logs.append(new Date() + " -- Processamento concluído. "+ contador + " linhas processadas e "+ contadorAlteracao +" linhas alteradas.");
		logs.newLine();

		} catch (Exception e) {
			e.printStackTrace();
			logs.append(new Date() + "  " + e.getCause() + " " + e.getMessage());
			logs.newLine();
			logs.flush();
			logs.close();
		}
	}
}