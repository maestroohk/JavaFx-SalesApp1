package sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManageSales {

    Connection conn = DbConnector.establishConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;

    TextField product, price, qty;
    Button store;
    Label message, summation, h, l;
    double total = 0.0, higest = 0.0, lowest = 0.0;

    public Scene manageSales() {
        BorderPane root = new BorderPane();
        Scene manageSales = new Scene(root, 800, 600);
        manageSales.getStylesheets().add(getClass().getResource("ManageSales.css").toExternalForm());
        root.setPadding(new Insets(10, 10, 10, 10));

        BorderPane.setMargin(generateSalesTable(), new Insets(10, 10, 10, 10));

        root.setTop(topSection());
        root.setLeft(generateSalesTable());
        root.setRight(leftSide());

        return manageSales;
    }

    private HBox topSection() {
        HBox topSection = new HBox(220);
        Label title = new Label("Larsen's sales recording system");
        title.setId("title_TopSection");
        HBox.setMargin(title, new Insets(10, 10, 10, 200));

        Button signOut = new Button("Logout");
        signOut.setOnAction(e -> Main.window.setScene(new Login().login()));
        signOut.setId("signOut");
        signOut.setPrefWidth(120);
        signOut.setPrefHeight(15);
        
        topSection.getChildren().addAll(title, signOut);

        return topSection;
    }

    private VBox generateSalesTable() {
        VBox generateSalesTable = new VBox();
        TableView<Product> table = new TableView<>();
        final ObservableList<Product> data = FXCollections.observableArrayList();

        TableColumn col1 = new TableColumn("Id");
        col1.setMinWidth(100);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn col2 = new TableColumn("Product");
        col2.setMinWidth(120);
        col2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn col3 = new TableColumn("Price");
        col3.setMinWidth(100);
        col3.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn col4 = new TableColumn("Quantity");
        col4.setMinWidth(100);
        col4.setCellValueFactory(new PropertyValueFactory<>("qty"));

        TableColumn col5 = new TableColumn("Amount");
        col5.setMinWidth(100);
        col5.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.getColumns().addAll(col1, col2, col3, col4, col5);

        generateSalesTable.getChildren().addAll(table);

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                data.clear();
            } 
//            else 
//                if (e.getClickCount() == 3) {
//                table.getSelectionModel().selectedItemProperty().addListener((obs, olv, nvl) -> {
//                    Product product = (Product)table.getSelectionModel().getSelectedItem();
//                    if (table.getSelectionModel().getSelectedItem() != null) {
//                        String qry = "delete from product where id = ?";
//                        try {
//                            pst = conn.prepareStatement(qry);
//                            pst.setInt(1, product.getId());
//                            pst.executeUpdate();
//                            System.out.println("Delete complete!");
//                            pst.close();
//                        } catch (Exception x) {
//
//                        }
//                    }
//                });
//            } 
                else {
                fetchSum();
                data.clear();
                try {
                    String qry = "select * from product";
                    pst = conn.prepareStatement(qry);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        data.add(new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("qty"),
                                rs.getDouble("amount")
                        ));
                        table.setItems(data);
                        table.setEditable(true);
                    }
                    pst.close();
                    rs.close();

                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        });

        return generateSalesTable;
    }

    private VBox leftSide() {
        VBox leftSide = new VBox();

        leftSide.getChildren().addAll(addNewProduct(), computations(), backgroundPic());

        return leftSide;
    }

    private VBox addNewProduct() {
        VBox addNewProduct = new VBox(10);
        addNewProduct.setPadding(new Insets(10, 10, 10, 10));

        Label title = new Label("Add New Product");
        title.setId("tile_AddnewProduct");
        title.setOnMouseClicked(e -> clearInsertFields());

        product = new TextField();
        product.setId("product");
        product.setPromptText("Product");
        product.setOnKeyPressed(e -> message.setText(""));

        price = new TextField();
        price.setId("price");
        price.setPromptText("Price");

        qty = new TextField();
        qty.setId("qty");
        qty.setPromptText("Units sold");
        qty.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                storeIntoDb();
            }
        });

        store = new Button("Store");
        store.setId("store");
        store.setOnAction(x -> storeIntoDb());

        message = new Label();

        addNewProduct.getChildren().addAll(title, product, price, qty, store, message);

        return addNewProduct;
    }

    private VBox computations() {
        VBox computations = new VBox();
        computations.setPadding(new Insets(10, 10, 10, 10));
        computations.setId("computationsContainer");
        summation = new Label();
        summation.setId("summation");

        HBox comps = new HBox(15);

        Label title = new Label("Total sum of goods sold");
        title.setId("computations");
        title.setOnMouseClicked(e -> {
            summation.setText("Total sales: " + fetchSum());

        });
        Image img = new Image(getClass().getResourceAsStream("help.png"));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(25);
        imgView.setFitWidth(25);
        imgView.setOnMouseClicked(e -> summation.setText(""));

        comps.getChildren().addAll(title, imgView);

        computations.getChildren().addAll(comps, summation);
        return computations;
    }

    private double fetchSum() {
        String retrieve = "select sum(amount)from product";

        try {
            pst = conn.prepareStatement(retrieve);
            rs = pst.executeQuery();
            rs.next();
            String sum = rs.getString(1);
//            System.out.println(sum);
            total = Double.parseDouble(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    private VBox backgroundPic(){
        VBox backgroundPic = new VBox();
        Image img = new Image(getClass().getResourceAsStream("cart3.png"));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(100);
        imgView.setFitWidth(100);
        backgroundPic.setPadding(new Insets(45,0,0,0));
        
        Image img2 = new Image(getClass().getResourceAsStream("vendor.png"));
        ImageView imgView2 = new ImageView(img2);
        imgView2.setFitHeight(100);
        imgView2.setFitWidth(100);
        backgroundPic.setPadding(new Insets(45,0,0,0));
        
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(imgView, imgView2);
        
        backgroundPic.getChildren().addAll(hBox);
        return backgroundPic;
    }

    private void storeIntoDb() {
        if (product.getText().isEmpty() || price.getText().isEmpty() || qty.getText().isEmpty()) {
            message.setText("All fields must be filled");
            message.setStyle("-fx-text-fill:red");
        } else {
            try {
                String qry = "INSERT INTO product (name, price, qty, amount) VALUES (?, ?, ?, ?)";
                pst = conn.prepareStatement(qry);
                pst.setString(1, product.getText());
                pst.setDouble(2, Double.parseDouble(price.getText()));
                pst.setInt(3, Integer.parseInt(qty.getText()));
                pst.setDouble(4, (Double.parseDouble(price.getText()) * (double) Integer.parseInt(qty.getText())));
                message.setText("Product Added Successfully");
                message.setStyle("-fx-text-fill:green");
                pst.execute();
                pst.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void clearInsertFields() {
        product.clear();
        price.clear();
        qty.clear();
    }
}
