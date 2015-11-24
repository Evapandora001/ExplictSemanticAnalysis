package segmentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

public class IKAnalyzerTest {
public static void main(String[] args) {
String str = "到青岛，一个比印象更好的城市。到了才想起火车票忘给钱了";
IKAnalysis(str);
(new IKAnalyzerTest()).IKSegment(str);
}

public static String IKAnalysis(String str) {
StringBuffer sb = new StringBuffer();
try {
// InputStream in = new FileInputStream(str);//
byte[] bt = str.getBytes();// str
InputStream ip = new ByteArrayInputStream(bt);
Reader read = new InputStreamReader(ip);
IKSegmentation iks = new IKSegmentation(read, true);
Lexeme t;
while ((t = iks.next()) != null) {
sb.append(t.getLexemeText() + "\t");

}
sb.delete(sb.length() - 1, sb.length());
} catch (IOException e) {
e.printStackTrace();
}
System.out.println(sb.toString());
return sb.toString();

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
