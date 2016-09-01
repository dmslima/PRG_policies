package oraculo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggressivePolling extends Policy{

	HashMap<String, String> osObjectos; //listagem de todos os objectos visto ate agora e o grupo a que pertencem..
	private List<Map<String,Integer>> tabela; //tabela por grupos, para contar occorencias....
	
	public AggressivePolling(int numGrupos) {		
		super(numGrupos);
		// TODO Auto-generated constructor stub
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
		int mudancasGrupo=0;
		
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
		
		String currentObject=null;
		int maxOccorrencias;
		int tabelaVencedora;
		
		for(int a=0; a<hastags.length;a++){
			currentObject=hastags[a];
			tabelaVencedora=getGroupFromObject(currentObject, false);
			maxOccorrencias= tabela.get(tabelaVencedora).get(currentObject);
			
			for(int i=0; i < tabela.size(); i++){
				if(i!=tabelaVencedora){
					temp= tabela.get(i);
					if(temp.containsKey(currentObject)){
						if(temp.get(currentObject)> maxOccorrencias){
							maxOccorrencias= temp.get(currentObject);
							tabelaVencedora=i;
						}
					}
				}
			}
			
			if(getGroupFromObject(currentObject, false) != tabelaVencedora){
				osObjectos.replace(currentObject, getCityByInt(tabelaVencedora));
				mudancasGrupo++;
			}
			
		}
		return mudancasGrupo;
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
}
