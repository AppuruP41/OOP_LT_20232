package oop.ict.project.gui;

import javafx.scene.paint.Color;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_BLUEPeer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static javafx.scene.paint.Color.BLUE;

public class ChatBox {

    @FXML
    private TextArea msgArea;

    @FXML
    private Button sendBtn;

    @FXML
    private Button recBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private TextArea contentPane;

    public void initialize() {
        msgArea.setEditable(true);
    }

    public void sendPressed(ActionEvent e) {
        String message = msgArea.getText();
        Label messageLabel = new Label("You: " + message + "\n\n");
        gptModel myAI = new gptModel();

        contentPane.appendText("You: " + message + "\n");
        contentPane.appendText("***\n");
        msgArea.clear();
        String myPrompt = myAI.createFullPrompt(message);
        System.out.println(myAI.chatGPT(myPrompt));
        String answer = myAI.chatGPT(myPrompt);

        contentPane.appendText("AI Assistant: " + answer + "\n\n");

    }

    public void recPressed(ActionEvent e) throws InterruptedException, IOException {
        AudioFormat format = new AudioFormat(16000, 8, 2, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        try (TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info)) {
            targetLine.open(format);
            targetLine.start();

            Thread stopper = new Thread(new Runnable() {
                public void run() {
                    AudioInputStream audioStream = new AudioInputStream(targetLine);
                    File wavFile = new File("oop/ict/project/record/recorded.wav");
                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            stopper.start();

            Thread.sleep(5000);
            targetLine.stop();
            targetLine.close();
            System.out.println("Done");
        } catch (LineUnavailableException | InterruptedException ex) {
            ex.printStackTrace();
        }


            String pythonScriptPath = "oop/ict/project/record/record.py";

            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath);
            Process process = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String ret = in.readLine();
            System.out.println("Python script output: " + ret);
            msgArea.appendText(ret);

        }


    public void clearPressed(ActionEvent e) {
        msgArea.setText("");
        contentPane.clear();
    }

}
