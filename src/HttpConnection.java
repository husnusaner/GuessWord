import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpConnection {
	
	URL url;
	String token = "";
	
	public HttpConnection(String urlConnection) throws MalformedURLException{
		url = new URL(urlConnection);
		try {
			URLConnection con = url.openConnection();
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			token = br.readLine();
			br.close();
			//System.out.println("Done");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getRespond(){
		return token;
	}

}



