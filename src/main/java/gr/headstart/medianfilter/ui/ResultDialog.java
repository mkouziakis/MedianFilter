package gr.headstart.medianfilter.ui;

import java.util.List;

import gr.headstart.medianfilter.InputDataHolder;
import gr.headstart.medianfilter.data.Result;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Custom dialog for presenting calculation results in a text area We have used
 * a text area, so the user can copy calculation results
 *
 * @author KouziaMi
 */
public class ResultDialog {

    /**
     * Invoke to show results dialog
     *
     * @param owner
     * @param results
     */
    public static void showResults(Window owner, List<Result> results) {
        final Stage stage = new Stage();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Average Deviation/Euclidean Distance");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);

        // prepare UI
        final TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        StringBuilder resultsBuilder = new StringBuilder();

        // prepare results text
        for (Result result : results) {
            resultsBuilder.append(result.getCalculationType().getTitle())
                    .append("(sw:")
                    .append(InputDataHolder.getInstance().getSlidingWindow())
                    .append(")")
                    .append("\t")
                    .append(result.getResult())
                    .append("\n");
        }
        resultArea.setText(resultsBuilder.toString());
        root.setCenter(resultArea);

        // copy button initialization
        Button copyToClipboardButton = new Button("Copy to Clipboard");
        copyToClipboardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(resultArea.getText());
                    clipboard.setContent(content);
                } catch (Exception e) {
                    //Do nothing here, not really importnt overall..
                }
            }
        });

        // close button initialization
        Button closeButton = new Button("Close");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.close();
            }
        });

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.getChildren().addAll(copyToClipboardButton, closeButton);

        root.setBottom(hbox);

        //show
        stage.show();
    }
}
