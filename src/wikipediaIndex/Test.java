package wikipediaIndex;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		ConceptIndex a=new ConceptIndex();
		ConceptSearch b=new ConceptSearch();
		b.searchQuerys("姓名");
	}

}
