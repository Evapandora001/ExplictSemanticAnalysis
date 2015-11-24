package wikipediaIndex;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKQueryParser;

public class ConceptSearch {
	
	public LinkedList<WikiConcept> searchQuerys(String input){
		LinkedList<WikiConcept> res=new LinkedList<WikiConcept>();
		IndexSearcher indexSearcher=null;
		Directory directory;
		try {
				directory = FSDirectory.open(new File("wikipedia//ConceptIndex"));
	            //创建索引 前缀搜索器,且只读
	            IndexReader indexReader = IndexReader.open(directory,true);
	            indexSearcher = new IndexSearcher(indexReader);
	            
	            Query query = IKQueryParser.parse("content", input);
	            
	            //返回前number条记录
//	            TopDocs topDocs = indexSearcher.search(query, 100,new Sort(new SortField("score", SortField.INT,true)));
	            TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
	            //信息展示
	            int totalCount = topDocs.totalHits;
	            System.out.println("共检索出 "+totalCount+" 条记录");
	            

	            
	            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
	            
	            for(ScoreDoc scDoc : scoreDocs){
	                Document  document = indexSearcher.doc(scDoc.doc);
	                String concept= document.get("concept");
	                String content= document.get("content");
	                res.add(new WikiConcept(concept, content));
//	                System.out.println(concept+"\r\n"+content+"\r\n");
	                 new ConceptExtract().writeAppend(concept+"\r\n"+content+"\r\n","wikipedia\\out\\"+input);
	           }
	        } catch (IOException er) {
				er.printStackTrace();
				System.err.println("索引文件打开失败");
			}catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	            try {
	                indexSearcher.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		  return res;
	}

}
