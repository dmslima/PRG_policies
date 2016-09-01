package oraculo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/*
 * Nodes pertencem aos grupos que forem convenientes (nÃ£o interessa agora, a
 * definir mais tarde). Oraculo tenta meter os objectos que mais frequentemente
 * partilham transacÃ§Ãµes no mesmo grupo.
 */

public class PoliticaB extends Policy{
	
	private class Par{
		String objecto;
		int occorencias;
		
		public Par(String obj){
			objecto=obj;
			occorencias=1;
		}
		
		public String getObjecto(){
			return objecto;
		}
		
		public int getOccorencia(){
			return occorencias;
		}
		
		public void addOccorencia(){
			occorencias++;
		}
		
	}

	HashMap<String, String> osObjectos;
	HashMap<String, LinkedList<Par>> repeticoes;	
	String primeiro= null;
	boolean ehprimeio= true;
	
	public PoliticaB(int numGrupo){
		super(numGrupo);
		osObjectos= new HashMap<String, String>();
		repeticoes= new HashMap<String, LinkedList<Par>>();
		
	}
	
	public void addObjectsToTable(LinkedList<String> lista){
		String[] objEgrupo1;
		String[] objEgrupo2;
		
		for(String s: lista){
			objEgrupo1= s.split("-");
			if(ehprimeio){
				primeiro= objEgrupo1[0];
				ehprimeio=false;
			}
			if(!osObjectos.containsKey(objEgrupo1[0]))
					osObjectos.put(objEgrupo1[0], objEgrupo1[1]);
		}
	
		int a=0;
		int b=1;
		LinkedList<Par> temp=null;
		int c;
		String aux;
		boolean encontrou;
		
		while(a < lista.size()-1){
			objEgrupo1= lista.get(a).split("-");
		//	System.out.println("a=" + a + " " +objEgrupo1[0]+"-->");

			if(!repeticoes.containsKey(objEgrupo1[0]))
				repeticoes.put(objEgrupo1[0], new LinkedList<Par>());
			b=a+1;
			while(b< lista.size()){
				objEgrupo2= lista.get(b).split("-");
			//	System.out.print(objEgrupo2[0] + " ");
				temp= repeticoes.get(objEgrupo1[0]);
				c=0;
				
				encontrou=false;
				while(c < temp.size()){
					aux= temp.get(c).getObjecto();
					if(aux.equals(objEgrupo2[0])){
						temp.get(c).addOccorencia();
						encontrou=true;
						c= temp.size();
					}
					c++;
				}
				if(!encontrou)
					temp.add(new Par(objEgrupo2[0]));			
				b++;
			}
		/*	System.out.println("temp size: " + temp.size());
			System.out.println();*/
			a++;
		}
		
	}
	
	public String getGroupFromObject(String objecto){
		return osObjectos.get(objecto);
	}
	
	public int analyse() {
		int mudancasGrupo=0;
		
		int numTotalRepeticoes=0;
		int quantosConjuntos=0;
		
		Set<String> chaves= osObjectos.keySet();
		LinkedList<Par> temp;
		
		for(String s: chaves){
			temp= repeticoes.get(s);
			if(temp!=null){
				for(Par e: temp){
					numTotalRepeticoes += e.getOccorencia();
				}
				quantosConjuntos += temp.size();
			}
		}
				
		double media= (double) numTotalRepeticoes / quantosConjuntos;
		media = media*2;
		
		String grupoDoObjecto1= null;
		String grupoDoObjecto2= null;
				
		for(String s: chaves){
			temp= repeticoes.get(s);
			if(temp!=null){
				for(Par e: temp){
					if(e.getOccorencia()>media){
						grupoDoObjecto1= osObjectos.get(s);
						grupoDoObjecto2= osObjectos.get(e.getObjecto());
						if(!grupoDoObjecto1.equals(grupoDoObjecto2)){
							/*System.out.println(s + " grupo: " + osObjectos.get(s));
							System.out.println(e.getObjecto() + " grupo: " + osObjectos.get(e.getObjecto()));
							System.out.println("Repeticoes: " +  e.getOccorencia());*/
							osObjectos.replace(e.getObjecto(), grupoDoObjecto1);
							mudancasGrupo++;
							/*System.out.println("#############");
							System.out.println(s + " grupo: " + osObjectos.get(s));
							System.out.println(e.getObjecto() + " grupo: " + osObjectos.get(e.getObjecto()));
							System.out.println(" ");*/
						}
						//option: remove 2º objecto das repeticoes!!!!
					}
				}
			}
		}
		
		//2Âº ciclo para ver aquelas 2x > Ã  superiores e ir ao "osObjectos" mudar o grupo!!
		return mudancasGrupo;
	}
	
	
	public void printAll(){		
		/*System.out.println("Os objectos size: " + osObjectos.size());
		System.out.println("O primeiro objecto dos repeticoes foi: " + primeiro);
		LinkedList<Par>  aux= repeticoes.get(primeiro);
		System.out.println("E tem este numero de 'irmaos' " + aux.size());
		for(Par e: aux)
			System.out.println(e.getObjecto() + " repetiu " + e.getOccorencia());*/
		
		int maxRepeticoes= -1;
		String chave= null;
		String valor = null;
		Set<String> chabes= osObjectos.keySet();
		LinkedList<Par> temp;
		
		for(String s: chabes){
		//	System.out.print("!" + s + ": ");
			temp= repeticoes.get(s);
			if(temp!=null){
				for(Par e: temp){
		//			System.out.print(" " + e.getObjecto()+ "-->" + e.getOccorencia() +"; ");
					if(e.getOccorencia()>maxRepeticoes){
						maxRepeticoes= e.getOccorencia();
						chave= s;
						valor= e.getObjecto();
					}
				}
			}
		//	System.out.println();
		}
		System.out.println("Primeiro: " + chave);
		System.out.println("Segundo: " + valor);
		System.out.println(maxRepeticoes);
	}
}