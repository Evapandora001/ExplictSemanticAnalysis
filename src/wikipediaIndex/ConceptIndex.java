package wikipediaIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class ConceptIndex {
	 /**
     * 带参数构造,参数用来指定索引文件目录
     * @param indexFilePath
     */
    public ConceptIndex(String indexFilePath){
        try {
        	createIndex(indexFilePath);
        } catch (Exception e) {
        	System.out.println("Concept索引失败");
			e.printStackTrace();
		}
    }
	/**
     * 默认构造,使用系统默认的路径作为索引
     */
    public ConceptIndex(){
        this("wikipedia//ConceptIndex");
    }
    
    /**
     * 创建索引
     */
    public void createIndex(String indexFilePath)throws Exception{
    	Directory directory=FSDirectory.open(new File(indexFilePath));
    	long start = System.currentTimeMillis();
    	int numIndexed=0;
    	String dataDir="wikipedia//dataPure";
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        indexWriter.deleteAll();
        File[] files=(new File(dataDir)).listFiles();
        for(int i=0; i<files.length; i++){
        	try {
   		     FileInputStream fis = new FileInputStream(files[i]);
   		     InputStreamReader isr = new InputStreamReader(fis,"utf-8");//决定以何种编码格式读文件
   		     BufferedReader br = new BufferedReader(isr);
   		     String line = null;
   		     while ((line = br.readLine()) != null) {
   		    	 String temp[]=line.split("#",2);
   	            Document document = addDocument(new WikiConcept(temp[0],temp[1]));
   	            indexWriter.addDocument(document);
   	            numIndexed++;
   		        }		        
   		    } catch (Exception e) {
   		        e.printStackTrace();
   		    }			
        }
        long end = System.currentTimeMillis();
        System.out.println("Indexing " + numIndexed + " files took "+ (end - start) + " milliseconds");
        indexWriter.close();
    }
	/**
     * 
     * Description：
     * @param filePath 路径
     * @param content 内容
     * @return
     */
    public Document addDocument(WikiConcept wiki){
        Document doc = new Document();
        //Field.Index.NO 表示不索引         
        //Field.Index.ANALYZED 表示分词且索引         
        //Field.Index.NOT_ANALYZED 表示不分词且索引
        doc.add(new Field("concept",wiki.getConcept(),Field.Store.YES,Field.Index.NOT_ANALYZED));
        doc.add(new Field("content",wiki.getContent(),Field.Store.YES,Field.Index.ANALYZED));
        return doc;
    }


}
