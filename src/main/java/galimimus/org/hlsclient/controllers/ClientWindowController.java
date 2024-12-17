package galimimus.org.hlsclient.controllers;

import galimimus.org.hlsclient.HelloApplication;
import galimimus.org.hlsclient.models.Connection;

import galimimus.org.hlsclient.models.Video;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class ClientWindowController {
    static final Logger log = Logger.getLogger(HelloApplication.class.getName());

    @FXML
    public AnchorPane pane_videos;
    @FXML
    public Button btn_get_vids;
    @FXML
    private AnchorPane pane_main;
    @FXML
    private TextField input_port;
    @FXML
    private TextField input_host;
    @FXML
    private TextField input_search;

    Video[] displayed_videos;
    Connection conn;

    public void downloadVideo(Connection conn, String vid_name){
        this.conn = conn;
        String uri = conn.getUrl()+":"+conn.getPort()+"/"+vid_name+"/video.m3u8";
        VideoPlayerController videoPlayerController = new VideoPlayerController();
        videoPlayerController.initializePlayer(uri, pane_main);
    }

    @FXML
    public void fillVideosButtons(ActionEvent actionEvent) {
        pane_videos.getChildren().clear();
        VBox tmp = new VBox();
        int sps = (int)(pane_videos.getWidth()/4);
        tmp.setPadding(new Insets(sps,sps,sps*2,sps));
        tmp.setSpacing(10);

        Connection conn = HelloApplication.getUserConnectionObject();
        Video[] videos = Connection.getVideos();
        displayed_videos = videos;
        for (Video btn_name : videos) {
            Button btn = new Button(btn_name.SETTINGS_VIDEO_NAME);
            btn.setOnAction(btnActionEvent -> downloadVideo(conn, btn_name.SETTINGS_VIDEO_FOLDER));
            btn.setId(btn_name.SETTINGS_VIDEO_FOLDER);
            btn.setMinWidth(pane_videos.getMinWidth());
            btn.setPrefWidth((int)(pane_videos.getWidth()/2));
            btn.setMaxWidth(pane_videos.getMaxWidth());
            tmp.getChildren().add(btn);
        }
        pane_videos.getChildren().add(tmp);

    }

}
