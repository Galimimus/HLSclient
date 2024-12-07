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
        String uri = conn.getUrl()+":"+conn.getPort()+"/"+vid_name+"/240/video.m3u8";
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

    public void findVideo(ActionEvent actionEvent) {
        if ((input_search!=null) && (!input_search.getText().isEmpty())){
            List<String> res = null;
            Video[] videos = conn.getVideos();
            for (int i = 0; i < videos.length; i++) {
                if (Pattern.compile(input_search.getText()).matcher(videos[i].SETTINGS_VIDEO_NAME).find()) {
                    res.add(videos[i].SETTINGS_VIDEO_NAME);
                } else {
                    log.logp(Level.WARNING, "ClientWindowController", "setHost", "Incorrect search.");
                }
            }
            if (res != null) {
                pane_videos.getChildren().clear();
                VBox tmp = new VBox();
                int sps = (int)(pane_videos.getWidth()/4);
                tmp.setPadding(new Insets(sps,sps,sps*2,sps));
                tmp.setSpacing(10);
                displayed_videos = (Video[]) res.toArray();
                for (Video btn_name : displayed_videos) {
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
    }

    public void setHost(ActionEvent actionEvent) {
        if ((input_host!=null) && (!input_host.getText().isEmpty())){
            if (input_host.getText().equals("localhost") || Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$").matcher(input_host.getText()).find()){
                conn.setUrl("http://"+input_host.getText());
            }else{
                log.logp(Level.WARNING, "ClientWindowController", "setHost", "Incorrect host.");
            }
        }
    }

    public void setPort(ActionEvent actionEvent) {
        if ((input_port!=null) && (!input_port.getText().isEmpty())){
            int tmp = Integer.getInteger(input_port.getText());
            if ((tmp > 0) && (tmp < 65535)){
                conn.setPort(tmp);
            } else{
                log.logp(Level.WARNING, "ClientWindowController", "setPort", "Incorrect port.");
            }
        }
    }
}
