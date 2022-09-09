/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4_g17;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
//import javax.persistence;
/**
 *
 * @author lokamloka
 */
@Entity
@Table(name="Contact")
public class Contact implements java.io.Serializable {
    @Id
    @Column(name="Mobile")
    private String Mobile;
    
    @Column(name="Name")
    private String Name;
       
    public Contact(){
    }
    public Contact(String n ,String m){
        this.Name=n;
        this.Mobile=m;
    }
    public String getName(){
        return Name;
    }
    public void setName(String n){
        this.Name=n;
    }
    
    public String getMobile(){
        return Mobile;
    }
    public void setMobile(String m){
        this.Mobile=m;
    }
}
