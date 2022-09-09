/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4_g17;

import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.hibernate.*;

public class Lab4_G17 extends Application {

    Scene AddressBookScene ;
    Scene SearchScene ;

    ObservableList<String> obNames,obMobile;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Contact c1=new Contact();
        //Left BorderPane_________________________________________________________________________________


        //Task1
        //Instantiate the obNames observable list
        ObservableList<String>obNames=FXCollections.observableArrayList();
        //Instantiate the obMobile observable list
        ObservableList <String>obMobile=FXCollections.observableArrayList();

        ListView<String> listofNames= new ListView (obNames);
        listofNames.setPrefWidth(150);
        Label Names = new Label("Contact Names: ");



        ListView<String> listofMobile= new ListView (obMobile);
        listofMobile.setPrefWidth(150);
        Label Phones = new Label("Mobile Numbers: ");


        HBox paneForLv = new HBox(20);
        paneForLv.getChildren().addAll(new VBox(Names, new ScrollPane(listofNames)), new VBox(Phones, new ScrollPane(listofMobile)));

        //Center BorderPane_________________________________________________________________________________
        // TextFields and (Add + Delete) buttons

        Label lbFN =new Label("First Name: ");
        TextField FName = new TextField();

        Label lbSN =new Label("Second Name: ");
        TextField SName = new TextField();

        Label lbMNumber =new Label("Mobile Number: ");
        TextField Mobile = new TextField();

        Button AddEntry = new Button("  Add Entry  ");
        Button DeleteEntry = new Button ("Delete Entry");

        GridPane paneForTF = new GridPane();

        paneForTF.setVgap(10);
        paneForTF.setHgap(10);

        paneForTF.add(lbFN,0,0);
        paneForTF.add(FName,1,0);

        paneForTF.add(lbSN,0,1);
        paneForTF.add(SName,1,1);

        paneForTF.add(lbMNumber,0,2);
        paneForTF.add(Mobile,1,2);

        Label lbErr = new Label();
        lbErr.setTextFill(Color.DARKRED);

        VBox paneForEntry = new VBox(paneForTF, AddEntry, DeleteEntry , lbErr);
        paneForEntry.setAlignment(Pos.BOTTOM_RIGHT);
        paneForEntry.setPadding(new Insets(20));
        paneForEntry.setSpacing(10);


        //Bottom BorderPane_________________________________________________________________________________
        //Search button to navigate to the second scene


        Button Search = new Button("Search");
        HBox paneForSearch = new HBox(Search);
        paneForSearch.setAlignment(Pos.BOTTOM_RIGHT);
        paneForSearch.setPadding(new Insets(20,20,20,0));

        //The root Pane for the AddressBookScene _________________________________________________________________________________

        BorderPane Scene1root = new BorderPane();
        Scene1root.setLeft(paneForLv);
        Scene1root.setCenter(paneForEntry);
        Scene1root.setBottom(paneForSearch);

        Scene1root.setPadding(new Insets(20));

        AddressBookScene = new Scene(Scene1root, 685, 330);

        //The root Pane and content for the SearchScene _________________________________________________________________________________

        VBox Scene2root = new VBox();
        Label lbltf = new Label("Enter the contact name: ") ;

        TextField tfSearchContact = new TextField();
        Button SearchContact = new Button("Search");
        Label msg = new Label();
        msg.setTextFill(Color.DARKRED);
        Button Back = new Button("Back");

        HBox  paneDelete = new HBox(lbltf, tfSearchContact, SearchContact);
        paneDelete.setAlignment(Pos.CENTER);
        paneDelete.setSpacing(20);

        Scene2root.getChildren().addAll(paneDelete, msg, Back);
        Scene2root.setAlignment(Pos.CENTER);
        Scene2root.setSpacing(10);
        Scene2root.setPadding(new Insets(20));
        SearchScene = new Scene(Scene2root, 685, 330);

        //Actions ___________________________________________________________________________________________________________
        //retrive Contact list
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        List<Contact> sList = null;
        String queryStr = "from Contact";
        Query query = session1.createQuery(queryStr);
        sList = query.list();
        session1.close();
        System.out.println("Contact list size: "+sList.size());
        for(Contact s: sList)
        { 
            obNames.add(s.getName());
            obMobile.add(s.getMobile());
        }
        //Task2.1
        AddEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String firstn=FName.getText();
                String secondn=SName.getText();
                String mobile=Mobile.getText();
                String fsname=secondn+", "+firstn;
                if(FName.getText().equals("")||SName.getText().equals("")||Mobile.getText().equals(""))
                {
                    lbErr.setText("Enter the contact name and mobile number");
                }
                else if(Mobile.getText().length()!=10)
                {
                    lbErr.setText("The length of Mobile number must be 10 digits");
                }
                else if(obNames.contains(fsname))
                {
                   lbErr.setText("The contact name is already exists");
                }
                else
                {
                obNames.add(SName.getText()+", "+FName.getText());
                obMobile.add(Mobile.getText());
                lbErr.setText("");
                //CREATE OBJ
                c1.setName(SName.getText()+", "+FName.getText());
                c1.setMobile(Mobile.getText());
                //INSERT STUDENT
                Session session = HibernateUtil.getSessionFactory().openSession(); 
                Transaction tx = session.beginTransaction();
                String cId1 = (String) session.save(c1);
                tx.commit();
                session.close();
                System.out.println("inserted Contact: \n"+c1.getName()+"\n"+c1.getMobile());
                }    
            }});
        //Task2.3
        DeleteEntry.setOnAction(e->
        {            
            String str=Mobile.getText();
            
            
            if(obMobile.contains(str))
            {
                int index=obMobile.indexOf(str);
                String name=obNames.get(index);
                lbErr.setText("Contact "+obNames.get(index)+" was Deleted. ");
                //DELETED CONTACT
                Session session = HibernateUtil.getSessionFactory().openSession(); 
                Transaction tx2 = null;
                tx2 = session.beginTransaction();
                session.delete(c1);
                tx2.commit();
                session.close();
                System.out.println("deleted Contact: \n"+c1.getName()+"\n"+c1.getMobile());
    
                obMobile.remove(index);
                obNames.remove(index);
            }
        });

        //Task2.4
        Search.setOnAction(e -> {
            //Set the SearchScene to the primary stage
            primaryStage.setTitle("Search");
            primaryStage.setScene(SearchScene);
        });

        //Task3.1
        SearchContact.setOnAction(e->
        {
                String firstn=FName.getText();
                String secondn=SName.getText();
                String fsname=secondn+", "+firstn;
            //String s=tfSearchContact.getText();
            
            int s=-1;
            String name=tfSearchContact.getText();
            
            if(obNames.isEmpty()&&obMobile.isEmpty())
            {
                msg.setText("The Address Book is Empty");
            }
//            else //if(obNames.contains(fsname)) ||s.contains(firstn)||s.contains(secondn)
//            {
                for (String i : obNames)
                {
                    //String sub=fsname.substring(fsname.length()/2);
                    if(i.contains(name))
                    {
                        int index=obNames.indexOf(i);
                        msg.setText("The phone number is: "+obMobile.get(index));
                        
                if(name.isEmpty()){
                msg.setText("Enter the contact name to search the phone number");
            } break;
                    }

                    if(obNames.contains(name))
                    {
                    s=obNames.indexOf(i);
                    msg.setText("The phone number is: "+obMobile.get(s));
                    }
                    else{msg.setText("The contact not found");}
                }
          
        });

        //Task3.2
        Back.setOnAction(e->
        {
            //Set the AddressBookScene to the primary stage
            primaryStage.setTitle("AddressBook");
            primaryStage.setScene(AddressBookScene);
        });


        /* Task2.2
        The purposes of the following code are:
        -	To retrieve the contact information (first name, last name and mobile numbers) from the ListView to TextFields.
        -	To link each contact name in the ListViwe with its Mobile Number.
            If the user choose any name from the listofNames ListView, the Mobile Number for the selected name will be selected in the listofMobile ListView and vice versa.
         */

        listofNames.getSelectionModel().selectedItemProperty().addListener(e-> {
            listofNames.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            int index = listofNames.getSelectionModel().getSelectedIndex();
            listofMobile.getSelectionModel().select(index);

            String[] FullName = obNames.get(index).split(", ");
            String FirstName = FullName [1];
            String LastName = FullName [0];
            FName.setText(FirstName);
            SName.setText(LastName);
            Mobile.setText(obMobile.get(index));


        });

        listofMobile.getSelectionModel().selectedItemProperty().addListener(e-> {
            listofMobile.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            int index = listofMobile.getSelectionModel().getSelectedIndex();
            listofNames.getSelectionModel().select(index);
            String[] FullName = obNames.get(index).split(", ");
            String FirstName = FullName [1];
            String LastName = FullName [0];
            FName.setText(FirstName);
            SName.setText(LastName);
            Mobile.setText(obMobile.get(index));


        });

        
        primaryStage.setTitle("AddressBook");
        primaryStage.setScene(AddressBookScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

//public class Lab4_G17 extends Application {
//    
//    @Override
//    public void start(Stage primaryStage) {
//        
//    }
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        launch(args);
//    }
//    
//}

//            else 
//            {
//                int index=obNames.indexOf(fsname);
//                String name=obNames.get(index);
//                lbErr.setText("Contact "+obNames.get(index)+" was Deleted. ");
//                obNames.remove(index);
//                obMobile.remove(index);
//            }
