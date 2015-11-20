package wikipediaIndex;

public class WikiConcept {
	String concept;
	String content;
	/**
	 * @param concept	wikiPedia 词条
	 * @param content	wikiPedia 词条内容，解释
	 */
	public WikiConcept(String concept,String content){
		this.concept=concept;
		this.content=content;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
