package it.gabriel.cdir_calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    GridPane grid = new GridPane();
    Text scenetitle = new Text("cdir calculator");
    Label ip = new Label("Ip: ");
    TextField ipTextField = new TextField();
    final Text ipTargetDec = new Text();
    final Text ipTargetBin = new Text();
    Label address = new Label("Address: ");
    Label netMask = new Label("NetMask:");
    TextField netMaskTextField = new TextField();
    final Text netMaskTargetDec = new Text();
    final Text netMaskTargetBin = new Text();
    Scene scene = new Scene(grid, 640, 480);
    Button btn = new Button("Calculate Range");
    HBox hbBtn = new HBox(10);
    Label networkId = new Label("Network Id:");
    final Text networkTargetDec = new Text();
    final Text networkTargetBin = new Text();
    Label broadcast = new Label("Broadcast:");
    final Text broadcastTargetDec = new Text();
    final Text broadcastTargetBin = new Text();

    @Override
    public void start(Stage stage) throws IOException {
        settingProps(stage);

    }

    public void settingProps(Stage stage){
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));

        scenetitle.setFont(Font.font("Constantia", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        grid.add(ip, 0,1);
        ipTextField.setPromptText("192.168.0.1");
        grid.add(ipTextField, 1, 1);

        ipTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            ipTargetDec.setFill(Color.DARKGREEN);
            ipTargetDec.setText(newValue);
            ipTargetBin.setFill(Color.DARKCYAN);
            ipTargetBin.setText(parsingIpToBinary(newValue));
        });

        grid.add(address,0,2);
        grid.add(ipTargetDec, 1, 2);
        grid.add(ipTargetBin, 1, 3);

        grid.add(netMask, 2, 1);
        netMaskTextField.setPromptText("23");
        grid.add(netMaskTextField, 3, 1);

        netMaskTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || newValue.equals("")){
                newValue = "0";
            }
            netMaskTargetBin.setFill(Color.DARKCYAN);
            netMaskTargetBin.setText(parsingNetmaskToBinary(Integer.parseInt(newValue)));
            netMaskTargetDec.setFill(Color.DARKGREEN);
            netMaskTargetDec.setText(parsingBinaryToIp(netMaskTargetBin.getText()));
        });

        grid.add(netMaskTargetDec, 3, 2);
        grid.add(netMaskTargetBin, 3, 3);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 3, 4);

        grid.add(networkId, 0,5);
        grid.add(networkTargetDec, 1, 5);
        grid.add(networkTargetBin, 3, 5);

        grid.add(broadcast, 0,6);
        grid.add(broadcastTargetDec, 1, 6);
        grid.add(broadcastTargetBin, 3, 6);

        btn.setOnAction(e -> {
            networkTargetBin.setFill(Color.FIREBRICK);
            networkTargetBin.setText(networkIdBinaryCalculation(ipTargetBin.getText(),
                                                                netMaskTargetBin.getText()));
            networkTargetDec.setFill(Color.FIREBRICK);
            networkTargetDec.setText(parsingBinaryToIp(networkTargetBin.getText()));

            broadcastTargetBin.setFill(Color.FIREBRICK);
            broadcastTargetBin.setText(broadcastBinaryCalculation(ipTargetBin.getText(),
                    netMaskTargetBin.getText()));
            broadcastTargetDec.setFill(Color.FIREBRICK);
            broadcastTargetDec.setText(parsingBinaryToIp(broadcastTargetBin.getText()));

        });
    }

    public String parsingIpToBinary(String decimalIp) {
        String[] arrOfStrings = decimalIp.split("\\.").clone();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < arrOfStrings.length; i++) {
            StringBuilder binaryPart = new StringBuilder();
            if(arrOfStrings[i].equals("") || arrOfStrings[i] == null){
                binaryPart.append("0");
            } else {
                binaryPart.append(Integer.
                        toBinaryString(
                                Integer.parseInt(arrOfStrings[i])));
            }
            if (binaryPart.length() < 8) {
                for (int j = 8 - binaryPart.length(); j > 0; j--) {
                    binaryPart.insert(0, "0");
                }
            }
            result.append(binaryPart);
            if (arrOfStrings.length > 1 && i < arrOfStrings.length - 1) {
                result.append(".");
            }
        }
        return result.toString();
    }

    public String parsingNetmaskToBinary(int decimalNetmask) {
        StringBuilder result = new StringBuilder();
        for (int i = 1, j = decimalNetmask; i < 33; i++, j--) {
            if (j > 0) {
                result.append("1");
            } else {
                result.append("0");
            }
            if (i % 8 == 0 && i < 30) {
                result.append(".");
            }
        }
        return result.toString();
    }

    private String parsingBinaryToIp(String binaryIp) {
      String[] arrOfBinaries = binaryIp.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String binPart : arrOfBinaries){
            result.append(Integer.parseInt(binPart, 2)).append(".");
        }
        return result.toString();
    }

    private String networkIdBinaryCalculation( String binaryAddressIp, String binaryNMIp) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binaryAddressIp.length(); i++) {
            if (binaryAddressIp.charAt(i) != '.') {
                result.append(
                        Character.toChars(binaryAddressIp.charAt(i) & binaryNMIp.charAt(i))
                );
            } else {
                result.append('.');
            }
        }
        return result.toString();
    }

    private String broadcastBinaryCalculation(String binaryAddressIp, String binaryNMIp) {
        StringBuilder result = new StringBuilder();
        String inverseBinaryNetmask = inverseBinaryCalc(binaryNMIp);
        for (int i = 0; i < binaryAddressIp.length(); i++) {
            if (binaryAddressIp.charAt(i) != '.') {
                result.append(
                        Character.toChars(binaryAddressIp.charAt(i) | inverseBinaryNetmask.charAt(i))
                );
            } else {
                result.append('.');
            }
        }

        return result.toString();
    }
    private String inverseBinaryCalc(String binary) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) != '.') {
                if (binary.charAt(i) == '0') {
                    result.append('1');
                } else if (binary.charAt(i) == '1') {
                    result.append('0');
                }

            } else {
                result.append('.');
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        launch();
    }
}