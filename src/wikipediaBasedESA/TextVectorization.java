package wikipediaBasedESA;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import wikipediaIndex.ConceptSearch;
import wikipediaIndex.WikiConcept;

public class TextVectorization {
	ConceptSearch wordConceptIndex=new ConceptSearch();
	
	public Double semantic(String a,String b){
		HashMap<String,Double> aVector=wikiVector(a);
		HashMap<String,Double> bVector=wikiVector(b);
		Double res=0.0,aMod=0.0,bMod=0.0;
		Iterator<HashMap.Entry<String,Double>> it=aVector.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry<String,Double> entry=it.next();
			aMod+=entry.getValue()*entry.getValue();
			if(bVector.containsKey(entry.getKey())){
				res+=entry.getValue()*bVector.get(entry.getKey());
			}
		}
		 it=bVector.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry<String,Double> entry=it.next();
			bMod+=entry.getValue()*entry.getValue();
		}
		res=res/Math.sqrt(aMod*bMod);
		return res;
	}
	
	public HashMap<String,Double> wikiVector(String input){
		HashMap<String,Double> in=new HashMap<String,Double>();
		String seg=IKSegment(input);
		double max=1;//用于value归一化
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
		System.out.println("Input="+in.values().toString());
		System.out.println("max="+max);
		
		HashMap<String,Double> conceptOfIn=new HashMap<String,Double>();
		for (String key:in.keySet()) {
//			in.put(key,in.get(key)/max);//normalization
			HashMap<String,Double> ele=getConceptOfWord(key,in.get(key)/max);
			merger(ele,conceptOfIn);
		}
		System.out.println(input+":\r\n"+conceptOfIn.toString());
		return conceptOfIn;
	}
//Mergr ele into conceptOfIn
	public void merger(HashMap<String, Double> ele, HashMap<String, Double> conceptOfIn) {
		Iterator<HashMap.Entry<String,Double>> it=ele.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry<String,Double> entry=it.next();
			if(conceptOfIn.containsKey(entry.getKey())){
				conceptOfIn.put(entry.getKey(),entry.getValue()+conceptOfIn.get(entry.getKey()));
			}else{
				conceptOfIn.put(entry.getKey(),entry.getValue());
			}
		}
	}

	public HashMap<String, Double> getConceptOfWord(String key, double  weight) {
		HashMap<String,Double> res=new HashMap<String,Double>();
		LinkedList<WikiConcept> concepts=wordConceptIndex.searchQuerys(key);//get concepts indexed by key  get top 1000concepts
		Iterator<WikiConcept> it=concepts.iterator();
		double max=1;//normalization
		while(it.hasNext()){
			WikiConcept a=it.next();
			String concept=a.getConcept();
			Double value=getKeysNum(a.getContent(),key);
			if(value>max)
				max=value;
			if(res.containsKey(concept)) 
				System.err.println("Error----concept repition");
			res.put(concept, value);
		}
		System.out.println("Word="+key+"\tmax="+max+"\r\n"+res.toString());
		//java.util.ConcurrentModificationException   iterator 迭代时 不能通过res.remove(key)移除元素 ，否则出现左侧异常
		Iterator<HashMap.Entry<String,Double>> itMap=res.entrySet().iterator();
		while(itMap.hasNext()){
			HashMap.Entry<String,Double> entry=itMap.next();
			double newValue=entry.getValue()*weight/max;//normalization
			if(newValue==0)
				itMap.remove();
			else	
				res.put(entry.getKey(),newValue);
		}
		return res;
	}

	public Double getKeysNum(String content, String key) {
		double count=0;
		int index=0;
		while((index=content.toLowerCase().indexOf(key))!=-1){
			count++;
			content=content.substring(index+key.length());
//			System.out.println(content);
		}
		if(count==0) System.err.println(key+"#"+content);
		return count;
	}

	public static void main(String[] args) {
		TextVectorization a=new TextVectorization();
		String text="LSTM";
		a.wikiVector(text);
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
