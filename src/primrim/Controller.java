package primrim;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import primrim.datamodel.TodoData;
import primrim.datamodel.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    @FXML
    private ContextMenu listContextMenu;




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

      listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        listContextMenu.getItems().addAll(deleteMenuItem);

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

       todoListView.setItems(TodoData.getInstance().getTodoItems());
        //todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // select only SINGLE item at one time (MULTIPLE for select more than one)
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>(){

                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if(item.getDeadline().isBefore(LocalDate.now().plusDays(1))){
                                setTextFill(Color.RED);
                            } else if(item.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BROWN);
                            }
                        }
                    }
                };
                    cell.emptyProperty().addListener(
                            (obs, wasEmpty, isNowEmpty) -> {
                                if(isNowEmpty){
                                    cell.setContextMenu(null);
                                }else{
                                    cell.setContextMenu(listContextMenu);
                                }
                            }
                    );

                    return cell;

            }
        });




    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new Todo Item");
        dialog.setHeaderText("Use this dialog to create a new todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try{
            //Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));
            //dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK ){
            DialogController controller = fxmlLoader.getController();
            TodoItem newItem =  controller.processResults();
            // todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
            todoListView.getSelectionModel().select(newItem);
            // System.out.println("Ok pressed");
        } /*else {
            System.out.println("Cancel pressed");
        }*/


    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if(selectedItem !=null) {
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            }
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


    public void deleteItem(TodoItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Black out.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK  )){
            TodoData.getInstance().deleteTodoItem(item);
        }

    }

}
