package de.bonprix.playground.components.addDialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.dto.ParkingZone;

import java.util.List;

public class ParkingPlaceAddDialog extends Dialog {
    IntegerField numberField = new IntegerField("Number");

    ComboBox<ParkingZone> parkingZone = new ComboBox<>("Parking Zone");

    Binder<ParkingPlace> binder = new BeanValidationBinder<>(ParkingPlace.class);

    public ParkingPlaceAddDialog(List<ParkingZone> zones){
        binder.bindInstanceFields(this);

        parkingZone.setItems(zones);
        parkingZone.setItemLabelGenerator(ParkingZone::getName);

        H2 headline = new H2("Create new parking place");


        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");


        VerticalLayout fieldLayout = new VerticalLayout(numberField, parkingZone);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        binder.forField(numberField).asRequired().bind(ParkingPlace::getNumber,ParkingPlace::setNumber);


        Button cancelButton = new Button("Cancel", e -> {
            this.close();
            numberField.clear();
            parkingZone.clear();
        });
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            ParkingPlace place = new ParkingPlace();
            try {
                binder.writeBean(place);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }

            fireEvent(new ParkingPlaceAddDialog.SaveEvent(this, place));
            this.close();
            UI.getCurrent().getPage().reload();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                saveButton);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        add(dialogLayout);

        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));
    }

    public static abstract class ParkingPlaceDialogEvent extends ComponentEvent<Dialog> {


        private ParkingPlace place;
        public ParkingPlaceDialogEvent(Dialog source, ParkingPlace place) {
            super(source, false);
            this.place = place;
        }

        public ParkingPlace getPlace() {
            return place;
        }
    }

    public static class SaveEvent extends ParkingPlaceAddDialog.ParkingPlaceDialogEvent {

        public SaveEvent(Dialog source, ParkingPlace place) {
            super(source, place);
        }
    }

    public static class CloseEvent extends ParkingPlaceAddDialog.ParkingPlaceDialogEvent {
        CloseEvent(Dialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}