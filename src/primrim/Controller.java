package primrim;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import primrim.datamodel.TodoData;
import primrim.datamodel.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private List<TodoItem> todoItems;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;



    public void initialize(){
      /*  TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthdat card for Jonh", LocalDate.of(2020, Month.MARCH, 25));
        TodoItem item2 = new TodoItem("Doctors Appointment", "See Dr. Smith at 123 Man Street. Bring paperwork", LocalDate.of(2020, Month.APRIL, 25));
        TodoItem item3 = new TodoItem("Finish design proposal for client", "I promised Mike I'd email website mockups", LocalDate.of(2020, Month.MAY, 24));
        TodoItem item4 = new TodoItem("Pickup Doug at the train station", "Doug's arriving on March 23 on the 5:00 train", LocalDate.of(2020, Month.JUNE, 25));
        TodoItem item5 = new TodoItem("Paaaaick up dry cleaning", "The clothes should be rady by", LocalDate.of(2020, Month.APRIL, 25));

        todoItems = new ArrayList<TodoItem>();
        todoItems.add(item1);
        todoItems.add(item2);
        todoItems.add(item3);
        todoItems.add(item4);
        todoItems.add(item5);

        TodoData.getInstance().setTodoItems(todoItems);*/


       todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
           @Override
           public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if(newValue != null) {
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("eee ,d MMMM, yyyy,");      // DATE FORMAT
                    deadlineLabel.setText(df.format(item.getDeadline()));
                }
           }
       });

        todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // select only SINGLE item at one time (MULTIPLE for select more than one)
        todoListView.getSelectionModel().selectFirst();


    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        try{
            Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));
            dialog.getDialogPane().setContent(root);
        } catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

    }


    @FXML
    public void handleClickListView(){

        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadlineLabel.setText(item.getDeadline().toString());
        //System.out.println("The selected item is " + item);
        /*StringBuilder sb = new StringBuilder(item.getDetails()); // Making string builder and addind details
        sb.append("\n\n\n\n");
        sb.append("Due: ");
        sb.append(item.getDeadline().toString()); // Adding deadline to stringBuilder
       itemDetailsTextArea.setText(sb.toString()) ;*/ // SB -> String, Show it on in window
    }


}
