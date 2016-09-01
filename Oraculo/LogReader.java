package oraculo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class LogReader {

	public static void main(String[] args) {
		
		int transaccoesFechadas = 0, numTransaccoes = 0, numTransaccoesAvaliadas=0;
		boolean transFechada;
		int currentGroup =-1;
		int numMudancasGrupo=0;
		
		Policy policy = null;
		
		boolean minhaPolitica= false;
		boolean politicaNaoMuda= true;
		boolean mudaSempre= false;
		boolean novaPolitica = false;
		
		double mediaGruposPtransaccao;
		double variancias;
		double desvPadGruposPtransaccao;
		int numHashtags=0;
		int maxHashtags=0;
		
		int[] grupos = new int[4];
		
		int percentLogRead=0;
		
		if(minhaPolitica)
			policy= new ConservativePolling(4);
		
		if(novaPolitica)
			policy= new AggressivePolling(4);
		
		if(politicaNaoMuda)
			policy= new Static(0);
		
		if(mudaSempre)
			policy = new MoveLatest(0);
		
		try (BufferedReader br = new BufferedReader(new FileReader("teste"))){
			
			int numGruposDaTransaccao = 0;
			LinkedList<Integer> listaNumGruposPorTransaccao= new LinkedList<Integer>();
			
			String sCurrentLine;
			String[] linhaPartida;
			String cidadeOrigem =null;
			String[] hashtags;
			
			LinkedList<String> listaObjectos;
			LinkedList<Integer> listaDegrupos;
			
			int tweetsSoC1Hastag=0;

			while ((sCurrentLine = br.readLine()) != null) {
				numTransaccoes++;
				numTransaccoesAvaliadas++;
				
				linhaPartida=sCurrentLine.split(": ");
				cidadeOrigem= linhaPartida[0];
			//	System.out.print("Cidade origem " + cidadeOrigem);
				listaObjectos= new LinkedList<String>();
				hashtags= linhaPartida[1].split(" ");
				
				//if(hashtags.length > maxHashtags)
				//	maxHashtags = hashtags.length;
				
				if(hashtags.length == 4)
					tweetsSoC1Hastag++;
				
				numHashtags+= hashtags.length;
				
				if(minhaPolitica || politicaNaoMuda || mudaSempre || novaPolitica)
					numMudancasGrupo += policy.addObjectsToTable(hashtags, cidadeOrigem);		
				
				if(minhaPolitica)
					numMudancasGrupo += policy.analyse();
				
				listaDegrupos= new LinkedList<Integer>();
		//		objEgrupo= listaObjectos.getFirst().split("-");
				
				if(minhaPolitica || politicaNaoMuda || mudaSempre || novaPolitica){
					currentGroup= policy.getGroupFromObject(cidadeOrigem, true);
					grupos[currentGroup] +=1;
				}
			//	else{
			//		currentGroup= Integer.parseInt(objEgrupo[1]);
			//	}
								
				listaDegrupos.add(currentGroup);
				int a=0;
				transFechada= true;
				int aux =-1;
				
				while(a < hashtags.length){
					//objEgrupo= listaObjectos.get(a).split("-");
					if(minhaPolitica || politicaNaoMuda || mudaSempre || novaPolitica){
						aux= policy.getGroupFromObject(hashtags[a],false);
					}
				//	else{
				//		aux= Integer.parseInt(objEgrupo[1]);
				//	} 
					if(aux != currentGroup){
						
						transFechada= false;
						//System.out.print(" " + hashtags[a] +  " group " + aux);
						if(!listaDegrupos.contains(aux)){
						//	System.out.print("aux " +  aux);
							listaDegrupos.add(aux);
					//		System.out.println("-->" + sCurrentLine);
						}
					}
					a++;
				}
				
				numGruposDaTransaccao += listaDegrupos.size();
				listaNumGruposPorTransaccao.add(listaDegrupos.size());
				
				if(transFechada)
					transaccoesFechadas++;
				else if(numTransaccoes >= 5308)
					System.out.println(linhaPartida[0] + ": " + linhaPartida[1]);
				
				// (numTransaccoes == 1024 || numTransaccoes == 2048 || numTransaccoes == 3072)
				//if(numTransaccoes == 590 || numTransaccoes == 1180 || numTransaccoes == 1769 || numTransaccoes == 2359 || numTransaccoes == 2949 || numTransaccoes == 3539 || numTransaccoes == 4128 || numTransaccoes == 4718 || numTransaccoes == 5308){
				if(numTransaccoes == 5308){
			//		if(minhaPolitica)
			//			numMudancasGrupo += policy.analyse();
					
					percentLogRead +=10;
					System.out.println(percentLogRead + "% tweets lidos");
					
					mediaGruposPtransaccao = (double) numGruposDaTransaccao/numTransaccoesAvaliadas;
					variancias=0.0;
					desvPadGruposPtransaccao=0.0;
					
					for(Integer b: listaNumGruposPorTransaccao)
						variancias += (mediaGruposPtransaccao-b)*(mediaGruposPtransaccao-b);
					variancias = variancias/listaNumGruposPorTransaccao.size();
					desvPadGruposPtransaccao = Math.sqrt(variancias);
					
					System.out.println("Nº de mudanças de grupo: " + numMudancasGrupo);
					System.out.println("Nº transacções avaliadas: " + numTransaccoesAvaliadas);
					System.out.println("Nº medio de grupos envolvidos p/ transaccao: " + mediaGruposPtransaccao + " Desvio Padrão de grupos envolvidos p/ transaccao: " + desvPadGruposPtransaccao);
					System.out.println("Nº transaccoes fechadas: " + transaccoesFechadas) ;
					System.out.println("Proporção de transacções fechadas num grupo: " + ((double) transaccoesFechadas/numTransaccoesAvaliadas*100) + "%");
					System.out.println("");
				
					numMudancasGrupo=0;
					numTransaccoesAvaliadas=0;
					numGruposDaTransaccao=0;
					transaccoesFechadas = 0;
					listaNumGruposPorTransaccao = new LinkedList<Integer>();
				}
			}
			
			mediaGruposPtransaccao = (double) numGruposDaTransaccao/numTransaccoesAvaliadas;
			variancias=0.0;
			desvPadGruposPtransaccao=0.0;
			
			for(Integer a: listaNumGruposPorTransaccao)
				variancias += (mediaGruposPtransaccao-a)*(mediaGruposPtransaccao-a);
			variancias = variancias/listaNumGruposPorTransaccao.size();
			desvPadGruposPtransaccao = Math.sqrt(variancias);
			
			System.out.println("100% tweets lidos");
			System.out.println("Nº transacções avaliadas: " + numTransaccoesAvaliadas);
			System.out.println("Nº de mudanças de grupo: " + numMudancasGrupo);
			System.out.println("Nº medio de grupos envolvidos p/ transaccao: " + mediaGruposPtransaccao + " Desvio Padrão de grupos envolvidos p/ transaccao: " + desvPadGruposPtransaccao);
			System.out.println("Nº transaccoes fechadas: " + transaccoesFechadas) ;
			System.out.println("Proporção de transacções fechadas num grupo: " + ((double) transaccoesFechadas/numTransaccoesAvaliadas*100) + "%");
		
		
	//		policy.printAll();
			
	//		if(politicaNaoMuda)
		//		policy.tenFavorite();
		/*	System.out.println("Num de tweets em Lisboa: " + grupos[0]);
			System.out.println("Num de tweets em Porto: " + grupos[1]);
			System.out.println("Num de tweets em Braga: " + grupos[2]);
			System.out.println("NNum de tweets em Faro: " + grupos[3]);*/
			
			//System.out.println("Num medio de hashtags por tweet: " + (double) numHashtags/numTransaccoes);
	//		System.out.println("Num de tweets só com X hashtag: " + (double) tweetsSoC1Hastag / numTransaccoes);
			
			//System.out.println(maxHashtags);

		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

}