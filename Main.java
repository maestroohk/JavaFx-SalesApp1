package sales;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage window;
    
    @Override
    public void start(Stage stage) {
        Image img = new Image(getClass().getResourceAsStream("cart.png"));
        Main.window = stage;
        Main.window.getIcons().add(img);
        Main.window.setResizable(false);
        Main.window.setTitle("Sales");
        Main.window.setScene(new Login().login());
        Main.window.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
