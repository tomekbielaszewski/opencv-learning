package pl.grizwold.opencvtest.controllers;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainSceneController {
    private final static int CAMERA_ID = 0;

    @FXML
    private Button toggleCammeraButton;
    @FXML
    private ImageView currentFrame;

    private VideoCapture capture = new VideoCapture();
    private ScheduledExecutorService timer;

    private boolean cameraActive = false;

    @FXML
    protected void startCamera(ActionEvent event) {
        if (!cameraActive) {
            capture.open(CAMERA_ID);

            if (capture.isOpened()) {
                cameraActive = true;

                Runnable frameGrabber = () -> {
                    Mat frame = grabFrame(capture);
                    Image image = convertToImage(frame);
                    updateImageView(currentFrame, image);
                };

                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                toggleCammeraButton.setText("Stop camera");
            }
        } else {
            cameraActive = false;
            toggleCammeraButton.setText("Start camera");
            stopCapturing();
        }
    }

    @SneakyThrows
    private void stopCapturing() {
        if(timer != null && !timer.isShutdown()) {
            timer.shutdown();
            timer.awaitTermination(33, TimeUnit.MILLISECONDS);
        }

        if(capture.isOpened()) {
            capture.release();
        }
    }

    private Mat grabFrame(VideoCapture capture) {
        Mat frame = new Mat();

        if (capture.isOpened()) {
            capture.read(frame);

            if (!frame.empty()) {
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
            }
        }
        return frame;
    }

    private Image convertToImage(Mat frame) {
        BufferedImage image;
        int width = frame.width();
        int height = frame.height();
        int channels = frame.channels();

        byte[] sourcePixelData = new byte[width * height * channels];
        frame.get(0, 0, sourcePixelData);

        if(frame.channels() > 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }

        final byte[] targetPixelData = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixelData, 0, targetPixelData, 0, sourcePixelData.length);

        return SwingFXUtils.toFXImage(image, null);
    }

    private void updateImageView(ImageView imageView, Image image) {
        ObjectProperty<Image> imageProperty = imageView.imageProperty();
        Platform.runLater(() -> {
            imageProperty.setValue(image);
        });
    }
}
