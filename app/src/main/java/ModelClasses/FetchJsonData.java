package ModelClasses;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An object that fetches data from a link
 */
public class FetchJsonData extends AsyncTask {
    public static final String rootUrl = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";

    /**
     * This is runned in the background whenever a FetchJsonData object is created and execute is called.
     * It reads the json data from the url and returns it as json object
     * @param urlArgument the url to read information from
     * @return the json object
     */
    @Override
    protected Object doInBackground(Object[] urlArgument) {
        String url = urlArgument[0].toString();
        JSONObject jsonObject = new JSONObject();
        try {
            URL encounterUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) encounterUrl.openConnection();
            System.out.println("opened connection");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            System.out.println("set do input");
            InputStream inputStream = httpURLConnection.getInputStream();
            System.out.println("get input");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String jsonText = readAll(bufferedReader);
            jsonObject = new JSONObject(jsonText);
            return jsonObject;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Reads the reader char by char and concatenates to a string
     * @param rd
     * @return
     * @throws IOException
     */
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
