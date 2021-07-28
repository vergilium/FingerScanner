import Abstract.IDriver;
import com.google.gson.JsonObject;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

public class ReadTemplate extends TimerTask {
    private final IDriver driver = new ZKTDriver();

    @Override
    public void run() {
        List<Byte> template = new ArrayList<>(2048);
        driver.ScanTemplate(template);

        //driver.IdentifyFree();
        if(template.size() > 0){
            byte[] b1 = new byte[template.size()];

            for (int i = 0; i < template.size(); i++)
            {
                b1[i] = template.get(i);
            }

            System.out.println(Arrays.toString(template.toArray()));

            URL url = null;
            try {
                url = new URL("http://localhost:8080/finger/match");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                String str = "{\n" +
                        "\n" +
                        "\t\"filial_id\": 1,\n" +
                        "\t\"template\":" +
                        Arrays.toString(template.toArray()) +
                        "}";

                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = str.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


//            FingerprintTemplate _template = new FingerprintTemplate(b1);
//            double score = new FingerprintMatcher(_template)
//                    .match(_template);
//            if(score > 40){
//                System.out.println("======================================\n===============SUCCESS================\n======================================\n");
//            }

        }
    }
}
