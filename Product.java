/**
*Lema Larsen Osoa
*
*/
package sales;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty qty;
    private final SimpleDoubleProperty amount;

    public Product(int id, String pname, Double price, int units, double amount) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(pname);
        this.price = new SimpleDoubleProperty(price);
        this.qty = new SimpleIntegerProperty(units);
        this.amount = new SimpleDoubleProperty(amount);
    }

//    getter methods
    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public Double getPrice() {
        return price.get();
    }

    public int getQty() {
        return qty.get();
    }
    
    public Double getAmount() {
        this.amount.bind(this.price.multiply(this.qty));
        return amount.get();
    }
    

//    Setter methods
    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(double pPrice) {
        this.price.set(pPrice);
    }
    
    public void setQty(int units) {
        this.qty.set(units);
    }
    
    public void setAmount(double amount) {
        this.amount.set(amount);
    }    

    
//    properties
    public SimpleIntegerProperty idProperty(){
        return id;
    }
    
    public SimpleStringProperty nameProperty(){
       return name;
    }
    
    public SimpleDoubleProperty priceProperty(){
        return price;
    }
    
    public SimpleIntegerProperty qtyProperty(){
        return qty;
    }
    
    public SimpleDoubleProperty amountProperty(){
        return amount;
    }
 
}
