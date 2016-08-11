package sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Test {

    Connection conn = DbConnector.establishConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;
    double total = 0.0;

    public Scene test() {
        BorderPane root = new BorderPane();
        Scene test = new Scene(root, 400, 600);

        String retrieve = "select sum(amount) from product";

        try {
            pst = conn.prepareStatement(retrieve);
            rs = pst.executeQuery();
            rs.next();
            String sum = rs.getString(1);
            System.out.println(sum);
            total = Double.parseDouble(sum);
        } catch (Exception e) {
        }

        return test;
    }
}
