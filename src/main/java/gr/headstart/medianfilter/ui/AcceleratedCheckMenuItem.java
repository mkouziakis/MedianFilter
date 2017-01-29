package gr.headstart.medianfilter.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckMenuItem;

/**
 * Extended CheckMenuItem class, supports keyboard shortcuts for
 * enabling/disabling menu
 *
 * @author KouziaMi
 */
public class AcceleratedCheckMenuItem extends CheckMenuItem {

    boolean checkStatus;

    public AcceleratedCheckMenuItem() {
        super();
        addDisableListener();
    }

    public AcceleratedCheckMenuItem(String string) {
        super(string);
        addDisableListener();
    }

    private void addDisableListener() {
        disableProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (!t1) {
                    setCheckStatus(false);
                }
            }
        });
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
        if (selectedProperty().get() != checkStatus) {
            selectedProperty().set(checkStatus);
        }
    }

    public boolean toggleCheckStatus() {
        checkStatus = !checkStatus;
        if (selectedProperty().get() != checkStatus) {
            selectedProperty().set(checkStatus);
        }
        return checkStatus;
    }
}
