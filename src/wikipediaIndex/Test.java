package wikipediaIndex;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		ConceptIndex a=new ConceptIndex();
		WikiConceptSearch b=new WikiConceptSearch();
		b.searchQuerys("信息");
	}

}
