package primrim;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import primrim.datamodel.TodoData;
import primrim.datamodel.TodoItem;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    public TodoItem processResults() {
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadlineVale = deadlinePicker.getValue();

        TodoItem newItem = new TodoItem(shortDescription, details, deadlineVale);
        TodoData.getInstance().addTodoItem(newItem);
        return newItem;
    }


}
