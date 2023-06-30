import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MeteoServlet extends HttpServlet {

    private final String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=33.44&lon=-94.04&appid=8e83e2097e3966aad80c288d75c049d7";

    
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder responseBody = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                responseBody.append(inputLine);
            }
            in.close();

            // Utiliser Gson pour analyser la chaîne JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseBody.toString(), JsonObject.class);

            // Récupérer la température depuis l'objet "main"
            JsonObject mainObject = jsonObject.getAsJsonObject("main");
            String temp = mainObject.get("temp").getAsDouble();

            // Passer les données à la vue JSP
            request.setAttribute("weatherData", temp);
        }

        connection.disconnect();

        request.getRequestDispatcher("index.jsp").forward(request, response);
    
}
