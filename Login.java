/**
*Lema Larsen Osoa
*
*/
package sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Login {
    Connection conn = DbConnector.establishConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    TextField username;
    PasswordField password;
    Button signIn;
    Label message, title;

    public Scene login(){
        VBox root = new VBox(25);
        Scene login = new Scene(root, 230, 300);
        login.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
        root.setPadding(new Insets(10,10,10,10));
        
        HBox topSection = new HBox(25);
        title = new Label("Sales Assessment App");
        title.setId("title");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("Windsong.ttf"), 18));
        
        Image img = new Image(getClass().getResourceAsStream("sales.png"));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(100);
        imgView.setFitWidth(100);
        
        topSection.setPadding(new Insets(0, 0, 0, 0));
        topSection.getChildren().addAll(title);
        
        username = new TextField();
        username.setId("username");
        username.setPromptText("Username");
        username.setMaxWidth(200);
        
        password = new PasswordField();
        password.setId("password");
        password.setMaxWidth(200);
        password.setPromptText("Password");
        password.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.ENTER){
                validateLogin();
            }
        });
        
        HBox lowerSection = new HBox(25);
        signIn = new Button("Login");
        signIn.setId("login");
        signIn.setOnAction( e -> validateLogin());
        
        lowerSection.getChildren().addAll(signIn, imgView);
        
        message = new Label();
        
        root.getChildren().addAll(topSection, username, password,lowerSection, message);
        
        return login;
    }
   
    public void validateLogin() {
            try {
                String qry = "SELECT * FROM user WHERE username = ? AND password = ?";
                pst = conn.prepareStatement(qry);
                pst.setString(1, username.getText());
                pst.setString(2, password.getText());

                rs = pst.executeQuery();
                if (rs.next()) {
                    Main.window.setScene(new ManageSales().manageSales());
                    message.setText("Login successful");
                } else {
                    message.setText("Login failed");
                    clearLoginFields();
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    public void clearLoginFields() {
            username.clear();
            password.clear();
        }
}
