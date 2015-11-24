package wikipediaBasedESA;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import wikipediaIndex.ConceptSearch;

public class TextVectorization {
	ConceptSearch wordConceptIndex=new ConceptSearch();
	
	public HashMap<String,Double> wikiVector(String input){
		HashMap<String,Double> in=new HashMap<String,Double>();
		String seg=IKSegment(input);
		double max=0;//用于value归一化
		for(String word:seg.split("\t")){//constuct HashMap-in
			if(in.containsKey(word)){
				Double value=in.get(word);
				value=value+1;
				if(value>max) max=value;
				in.put(word, value);
			}else{
				in.put(word, 1.0);
			}
		}
		System.out.println(in.values().toString());
		
		HashMap<String,Double> conceptOfIn=new HashMap<String,Double>();
		for (String key:in.keySet()) {
//			in.put(key,in.get(key)/max);//normalization
			HashMap<String,Double> ele=getConceptOfWord(key,in.get(key)/max);
			merger(ele,conceptOfIn);
		}
		System.out.println(conceptOfIn.toString());
	}

	public void merger(HashMap<String, Double> ele, HashMap<String, Double> conceptOfIn) {
		
	}

	public HashMap<String, Double> getConceptOfWord(String key, double  wright) {
		
		return null;
	}

	public static void main(String[] args) {
		TextVectorization a=new TextVectorization();
		String text="到青岛，一个比印象更好的城市。到了才想起火车票忘给钱了";
	}
	public String IKSegment(String str){
		StringBuffer sb = new StringBuffer();
		try {
			byte[] bt = str.getBytes();// str
			InputStream ip = new ByteArrayInputStream(bt);
			Reader read = new InputStreamReader(ip);
			IKSegmentation iks = new IKSegmentation(read, true);
			Lexeme t;
			while ((t = iks.next()) != null) {
				if(t.getLexemeText().length()<=1) continue;//去除 单个字
				sb.append(t.getLexemeText().trim()+"\t");
				}
			sb.delete(sb.length()-1,sb.length());
			} catch (IOException e) {
				e.printStackTrace();
				}
		System.out.println(sb.toString());
		return sb.toString();
		}

}
