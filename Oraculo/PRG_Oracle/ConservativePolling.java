package oraculo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Cada nÃ³ pertence a 1 grupo, conceito de transacÃ§Ã£o nÃ£o interessa, oraculo tenta meter
 * objectos mais usados pelo(s) nÃ³(s) no grupo do nÃ³.
 */

public class ConservativePolling extends Policy {
	
	HashMap<String, String> osObjectos; //listagem de todos os objectos visto ate agora e o grupo a que pertencem..
	private List<Map<String,Integer>> tabela; //tabela por grupos, para contar occorencias....

	public ConservativePolling(int numGrupos) {
		super(numGrupos);
		
		osObjectos= new HashMap<String, String>();
		
		tabela = new ArrayList<Map<String,Integer>>(numGrupos);
		Map<String,Integer> element;
		for(int i=0; i < numGrupos; i++){
			element= new HashMap<String, Integer>();
			tabela.add(i, element);
		}
	}

	@Override
	public int addObjectsToTable(String[] hastags, String cidade) {
		int posicao =-1;
		
		if(cidade.equals("Lisboa"))
			posicao=0;
		else if(cidade.equals("Porto"))
			posicao=1;
		else if(cidade.equals("Braga"))
			posicao=2;
		else if(cidade.equals("Faro"))
			posicao=3;
		
		Map<String,Integer> temp= tabela.get(posicao);
		
		for(int i=0; i<hastags.length;i++){
			if(!osObjectos.containsKey(hastags[i])){
				osObjectos.put(hastags[i], cidade);
			}
			if(!temp.containsKey(hastags[i])){
				temp.put(hastags[i],1);
			}
			else{
				temp.replace(hastags[i], temp.get(hastags[i])+1);
			}
		}
		return 0;
	}
			
	@Override
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

	@Override
	public int analyse() {
		int mudancasGrupo=0;
		Map<String,Integer> temp;
		int numTotalRepeticoes=0;
		int quantosConjuntos=0;
		
		for(int i=0; i < tabela.size(); i++){
			temp= tabela.get(i);
			
			for (Entry<String, Integer> entry : temp.entrySet()) {
				numTotalRepeticoes += entry.getValue();
			}
			quantosConjuntos += temp.size();                       
		}
		
		double media= (double) numTotalRepeticoes / quantosConjuntos;
		media = media*1.5;
		//System.out.println("Dobro da media " + media);
		int posicaoVencedora, maxOccorrencias;
		int b;
		String currentObject;
		Map<String,Integer> temp2;
		
		for(int i=0; i < tabela.size(); i++){
			
			temp= tabela.get(i);
			
			for (Entry<String, Integer> entry : temp.entrySet()) {
				if(entry.getValue()> media){
					currentObject = entry.getKey();
					maxOccorrencias= entry.getValue();
					
					posicaoVencedora=i;
					b=i+1;
					
				/*	System.out.println("Inicio--->");
					System.out.println(currentObject + "-" + osObjectos.get(currentObject));
					
					System.out.print("Na tabela " + i);
					System.out.println(" o objecto " + currentObject + " tem estas repeticoes " + maxOccorrencias);*/
					if(b<tabela.size()){
						while(b<tabela.size()){
							temp2= tabela.get(b);
							if(temp2.containsKey(currentObject)){
								if(temp2.get(currentObject)>maxOccorrencias){
									maxOccorrencias= temp2.get(currentObject);
									posicaoVencedora=b;
								}
							}
							b++;
						}
					}
					
					String cidadeVencedora = getCityByInt(posicaoVencedora);
					
					if(!cidadeVencedora.equals(osObjectos.get(currentObject))){
					//	System.out.print("Na tabela " + i);
					//	System.out.println(" o objecto " + currentObject + " tem estas repeticoes " + maxOccorrencias);
					//	System.out.println("Mas o " +  currentObject + " est� em " + osObjectos.get(currentObject));
						osObjectos.replace(currentObject, cidadeVencedora);
						mudancasGrupo++;
						
						
					}
					//System.out.println("Final--->");
					//System.out.println(currentObject + "-" + osObjectos.get(currentObject));
				}
			}                 
		}
		/*for(int i=0; i < tabela.size(); i++){
			temp= tabela.get(i);
			temp.clear();
		}*/
		
		return mudancasGrupo;
	}
	
	public String getCityByInt(int posicao){
		if(posicao == 0)
			return "Lisboa";
		else if(posicao == 1)
			return "Porto";
		else if(posicao == 2)
			return "Braga";
		else if(posicao == 3)
			return "Faro";
		
		return null;		
	}
	
	@Override
	public void printAll() {
		Set<String> chaves= osObjectos.keySet();
		
		for(String cenas: chaves)
			System.out.println(cenas + " grupo: " + osObjectos.get(cenas));
		
		Map<String,Integer> temp;
		String chave;
		Integer valor;
		//int ix=0;
		
	/*	System.out.println("################################");
		
		for(int i=0; i < tabela.size(); i++){
			System.out.println("Tabela: " + i);
			temp= tabela.get(i);
			
			for (Entry<String, Integer> entry : temp.entrySet()) {
				  chave = entry.getKey();
				  valor = entry.getValue();
				  
				  System.out.print("O objecto " + chave);
				  System.out.print(" do grupo " +  osObjectos.get(chave));
				  System.out.println(" Tem estas ocorrencias: " + valor);
				  
				//  ix++;
				  
				//  if(ix >10)
				//	  break;
				}
			System.out.println("                                 ");
		}*/
		
	}
	
	
	
	/*
	
	public void resetOraculo(){
		Map<ObjectMetadata,Par> temp= null;
		
		for(int i=0; i < tabela.size(); i++){
			temp= tabela.get(i);
			
			for (Map.Entry<ObjectMetadata,Par> entry : temp.entrySet()) {
				entry.getValue().setOccorrencia(0);
			}
		}
	}
	
	//FAZER PRINT FINAL DESTE ORACULO NO INICIO E NO FIM DO BENCHMARK!!!
	
	public void printFinal(){
		Map<ObjectMetadata,Par> temp= null;
		System.out.println("********* PRINT FINAL ORACULO *************");
		ObjectMetadata chave= null;
		Par par= null;
		for(int i=0; i < tabela.size(); i++){
			System.out.println("Tabela: " + i);
			temp= tabela.get(i);
			
			for (Map.Entry<ObjectMetadata,Par> entry : temp.entrySet()) {
				  chave = entry.getKey();
				  par = entry.getValue();
				  
				  System.out.print("EstÃ¡ no grupo: " + ((PartialReplicationOID) chave).getPartialGroup().getId());
				  System.out.print(" O objecto Ã©: "+ par.getObject().toString() + ", da clase " + par.getObject().getClass());
				  System.out.println(" E as ocorrencias sao: " + par.getOccorencia());
				}
			System.out.println("                                 ");
		}
		
		analisa();
	}
	
	public void analisa(){
	/*	preciso de ver a mÃ©dia de occorencias por indice da tabela
		dps ver aqueles cujas ocorrencias sejam 1.5x > Ã  mÃ©dia
		ver se esses objectos estÃ£o ao no nosso groupo tabela.indice == partialreplication metadata.getgroup.getid
		se sim, stable = true
		else compara com o num de vezes usado no grupo de partida.*/
	/*	int[] medias= new int[tabela.size()];
		
		Map<ObjectMetadata,Par> temp= null;
		
		for(int i=0; i < tabela.size(); i++){
			temp= tabela.get(i);
			
			for (Map.Entry<ObjectMetadata,Par> entry : temp.entrySet()) {
				medias[i] += entry.getValue().getOccorencia();
			}
			
			if(temp.entrySet().size() > 0){
				medias[i]= medias[i] / temp.entrySet().size();
				
				System.out.println("A tabela " + i + " tem esta media " + medias[i]);
			}
			for (Map.Entry<ObjectMetadata,Par> entry : temp.entrySet()) {
				if(entry.getValue().getOccorencia() > medias[i] * 2){
					System.out.print("RED FLAG: ");
					System.out.print("EstÃ¡ no grupo: " + ((PartialReplicationOID) entry.getKey()).getPartialGroup().getId());
					System.out.print(" O objecto Ã©: "+ entry.getValue().getObject().toString() + ", da clase " + entry.getValue().getObject().getClass());
					System.out.println(" E as ocorrencias sao: " + entry.getValue().getOccorencia());
				}
			}
			
			//TENTAR BOTAR AGORA O VACATION A BOMBAR!!!
		}
		//NESTE MOMENTO SEI A MEDIA DE OCORRENCIA DENTRO DUMA TABELA, E AS RED FLAGS DENTRO DESSA TABELA!!!
		

	 */

}