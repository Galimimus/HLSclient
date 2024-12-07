package galimimus.org.hlsclient;

import com.google.gson.Gson;
import galimimus.org.hlsclient.models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.LogManager;

import lombok.Getter;

import static galimimus.org.hlsclient.controllers.VideoPlayerController.stopPlayer;
import static galimimus.org.hlsclient.helpers.Helper.readFromResourceStream;

public class HelloApplication extends Application {
    @Getter
    private static Connection UserConnectionObject;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("client-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("HLS client");
        stage.setScene(scene);
        stage.show();
        Gson g = new Gson();

        Server settings_server = g.fromJson(readFromResourceStream(Paths.get("settings_connection.json")), Server.class);

        try {
            LogManager.getLogManager().readConfiguration(
                    HelloApplication.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e);
        }
        UserConnectionObject = new Connection("http://"+settings_server.SETTINGS_HOST, settings_server.SETTINGS_PORT);

        UserConnectionObject.downloadVideos();
        // TODO: обработать ошибку, что сервер не запущен
        Video[] videos = Connection.getVideos();
        if (videos == null) System.err.println("Could not setup connection to " + "http://"+settings_server.SETTINGS_HOST + ":" + settings_server.SETTINGS_PORT);

    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception{
        super.stop();
        stopPlayer();
    }
}