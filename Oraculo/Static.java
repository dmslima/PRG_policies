package oraculo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 1� sitio onde um objecto aparece, fica afectado a esse grupo, e nunca muda.
 */

public class Static extends Policy{
	HashMap<String, String> osObjectos; //listagem de todos os objectos visto ate agora e o grupo a que pertencem..
	
	HashMap<String, Integer> ocorrencias;

	public Static(int numGrupos) {
		super(numGrupos);

		osObjectos= new HashMap<String, String>();
		ocorrencias = new HashMap<String, Integer>();
	}
	
	public int addObjectsToTable(String[] hastags, String cidade) {
		for(int i=0; i<hastags.length;i++){
			if(!osObjectos.containsKey(hastags[i])){
				osObjectos.put(hastags[i], cidade);
			}
			if(!ocorrencias.containsKey(hastags[i])){
				ocorrencias.put(hastags[i], 1);
			}
			else{
				ocorrencias.replace(hastags[i], ocorrencias.get(hastags[i])+1);
			}
		}
		return 0;
	}
	
	public int getGroupFromObject(String objecto, boolean ehCidade) {
		int posicao = -1;
		String cidade= null;
		if(ehCidade)
			cidade = objecto;
		else
			cidade =osObjectos.get(objecto);

		if(cidade.equals("Lisboa"))
			posicao=0;
		else if(cidade.equals("Porto"))
			posicao=1;
		else if(cidade.equals("Braga"))
			posicao=2;
		else if(cidade.equals("Faro"))
			posicao=3;
	
		return posicao;
	}
	
	public void printAll() {
		int[] grupos = new int[4];
		int i;
		
	/*	Set<String> chaves= osObjectos.keySet();
		
		for(String cenas: chaves){
			i = getGroupFromObject(cenas,false);
			grupos[i] +=1;
		}
		
		System.out.println("Num de # em Lisboa: " + grupos[0]);
		System.out.println("Num de # em Porto: " + grupos[1]);
		System.out.println("Num de # em Braga: " + grupos[2]);
		System.out.println("Num de # em Faro: " + grupos[3]);
		*/
		
	}
	
	public void tenFavorite(){
		int maxOcorrencias;
		String currentObject;
	
		Set<String> chaves;
		
		for(int i=0; i<10; i++){
			maxOcorrencias=-1;
			currentObject=null;
			
			chaves= ocorrencias.keySet();
			for(String cenas: chaves){
				if(ocorrencias.get(cenas)> maxOcorrencias){
					maxOcorrencias= ocorrencias.get(cenas);
					currentObject= cenas;
				}
			}
			System.out.println("Objecto " + i + " " + currentObject + "--> " + maxOcorrencias);
			ocorrencias.remove(currentObject);
		}
	}

}
