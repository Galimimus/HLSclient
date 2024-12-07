package galimimus.org.hlsclient.models;
import com.google.gson.Gson;
import galimimus.org.hlsclient.HelloApplication;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;



@Getter
@Setter
public class Connection {
    private int port;
    private String url;
    private HttpClient client;
    @Getter
    private static Video[] videos;



    static final Logger log = Logger.getLogger(HelloApplication.class.getName());


    public Connection(String url, int port){
        this.port = port;
        this.url = url;
        client = HttpClient.newHttpClient();

    }

    public void downloadVideos(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+":"+port))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.logp(Level.SEVERE, "Connection", "downloadVideos", e.getMessage());

            throw new RuntimeException(e);
        }
        if (response == null) return;
        if (response.statusCode() == 200) {
            if (response.body() != null) {
                Gson g = new Gson();
                System.out.println(response.body());

                videos = g.fromJson(response.body(), Video[].class);

                for(int i = 0; i < videos.length; i++) {
                    System.out.println(videos[i].SETTINGS_VIDEO_FOLDER);
                }

                log.logp(Level.INFO, "Connection", "downloadVideos", "Status code 200, successfully received body.");
            } else{
                log.logp(Level.INFO, "Connection", "downloadVideos", "Status code 200, empty body received.");
            }
        } else{
            log.logp(Level.INFO, "Connection", "downloadVideos", "Status code " + response.statusCode() + ", error.");
        }
    }
}
