package wikipediaIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.lucene.util.CollectionUtil;


public class ConceptExtract {
	public void extract(){
		String dataDir="wikipedia//dataPure";
        File[] files=(new File(dataDir)).listFiles();
        String titleRegex="^<doc id=\"\\d*\" url=\".*\" title=\".*\">$";
        int count=0;
        for(int i=0; i<files.length; i++){
        	try {
   		     FileInputStream fis = new FileInputStream(files[i]);
   		     InputStreamReader isr = new InputStreamReader(fis,"utf-8");//决定以何种编码格式读文件
   		     BufferedReader br = new BufferedReader(isr);
   		     String line = null;
   		     while ((line = br.readLine()) != null) {
   		    	 if(line.matches(titleRegex)){
   		    		 count++;
   		    		 System.out.println(line);
   		    		 String concept=br.readLine();
   		    		 String content="";
   		    		 while((line=br.readLine()).equals("</doc>")!=true){
   		    			 if(line.length()!=0) content+=line.trim();
   		    		 }
//   		    		 writeAppend(new WikiConcept(concept, content).toString()+"\r\n","wikipedia//out//concept");
   		    		writeAppend(concept+"\r\n","wikipedia//out//conceptOfWikipedia");
   		    	 }
   		        }		        
   		    } catch (Exception e) {
   		        e.printStackTrace();
   		    }			
        }
        System.out.println(count);//843330
	}

	public static void main(String[] args) {
		ConceptExtract a=new ConceptExtract();
		a.extract();
	}
	
	public void writeAppend(String content,String filePath){
		try {
		        FileOutputStream fos = new FileOutputStream(filePath,true);//追加方式
		        OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");//指明写文件的编码格式
		        osw.write(content);
		        osw.flush();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	}

}
