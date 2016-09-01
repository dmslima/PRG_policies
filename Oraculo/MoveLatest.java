package oraculo;

import java.util.HashMap;
import java.util.Set;

/**
 * Um objecto muda sempre para o ultimo sitio onde foi usado.
 */

public class MoveLatest extends Policy {
	HashMap<String, String> osObjectos; 

	public MoveLatest(int numGrupos) {
		super(numGrupos);

		osObjectos= new HashMap<String, String>();
	}
	
	public int addObjectsToTable(String[] hastags, String cidade) {
		int mudancasGrupo=0;
		for(int i=0; i<hastags.length;i++){
			if(osObjectos.containsKey(hastags[i]) && !cidade.equals(osObjectos.get(hastags[i]))){
			//	System.out.println(hastags[i]);
				mudancasGrupo++;
			}
			osObjectos.put(hastags[i], cidade);
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
	
	public void printAll() {
		Set<String> chaves= osObjectos.keySet();
		
		for(String cenas: chaves)
			System.out.println(cenas + " grupo: " + osObjectos.get(cenas));
	}
}
