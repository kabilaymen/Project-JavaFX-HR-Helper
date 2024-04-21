package com.projet.utilities;

import javafx.scene.control.TableCell;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import java.time.LocalDate;

public class DatePickerTableCell<S, T> extends TableCell<S, LocalDate> {
    private final DatePicker datePicker;
    private final StringConverter<LocalDate> converter;

    public DatePickerTableCell(StringConverter<LocalDate> converter) {
        this.converter = converter;
        this.datePicker = new DatePicker();

        this.datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(converter.fromString(datePicker.getEditor().getText()));
            }
        });

        this.setGraphic(datePicker);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(true);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            datePicker.setValue(item);
            setGraphic(datePicker);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        datePicker.setDisable(false);
        setText(null);
        setGraphic(datePicker);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(converter.toString(getItem()));
        setGraphic(null);
    }

    @Override
    public void commitEdit(LocalDate item) {
        super.commitEdit(item);
        setGraphic(null);
    }
}
