package galimimus.org.hlsclient.controllers;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoPlayerController {

    private static MediaPlayerFactory mediaPlayerFactory;
    private static EmbeddedMediaPlayer embeddedMediaPlayer;
    private ImageView videoImageView;

    public void initializePlayer(String mediaPath, AnchorPane pane_main) {

        VBox vbox = new VBox();

        videoImageView = new ImageView();
        videoImageView.setPreserveRatio(true);
        videoImageView.setFitWidth(pane_main.getWidth());
        videoImageView.setFitHeight(pane_main.getHeight());

        vbox.getChildren().add(videoImageView);
        HBox hbox = new HBox();
        Button pause = new Button("Pause");
        pause.setOnAction(btnActionEvent -> pausePlayer());
        hbox.getChildren().add(pause);
        Button resume = new Button("Resume");
        resume.setOnAction(btnActionEvent -> resumePlayer());
        hbox.getChildren().add(resume);
        vbox.getChildren().add(hbox);
        pane_main.getChildren().add(vbox);

        mediaPlayerFactory = new MediaPlayerFactory();
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();

        embeddedMediaPlayer.videoSurface().set(new ImageViewVideoSurface(videoImageView));

        embeddedMediaPlayer.media().play(mediaPath);

        pane_main.widthProperty().addListener((obs, oldVal, newVal) ->
                videoImageView.setFitWidth(newVal.doubleValue())
        );
        pane_main.heightProperty().addListener((obs, oldVal, newVal) ->
                videoImageView.setFitHeight(newVal.doubleValue())
        );
    }

    public void pausePlayer(){
        if (embeddedMediaPlayer != null) {
            embeddedMediaPlayer.controls().pause();
        }
    }

    public void resumePlayer(){
        if (embeddedMediaPlayer != null) {
            embeddedMediaPlayer.controls().start();
        }
    }

    // Метод для остановки плеера и очистки ресурсов
    public static void stopPlayer() {
        if (embeddedMediaPlayer != null) {
            embeddedMediaPlayer.controls().stop();
            embeddedMediaPlayer.release();
        }
        if (mediaPlayerFactory != null) {
            mediaPlayerFactory.release();
        }
    }
}
