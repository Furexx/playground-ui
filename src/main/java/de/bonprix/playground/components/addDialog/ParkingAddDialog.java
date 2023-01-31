package de.bonprix.playground.components.addDialog;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.Parking;

public class ParkingAddDialog extends Dialog {

    TextField nameField = new TextField("Name");
    TextField cityField = new TextField("City");
    TextField streetField = new TextField("Street");
    IntegerField zipCode = new IntegerField("Zip code");

    Binder<Parking> binder = new BeanValidationBinder<>(Parking.class);

    public ParkingAddDialog() {


        H2 headline = new H2("Create new parking");


        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");


        VerticalLayout fieldLayout = new VerticalLayout(nameField,
                cityField, streetField, zipCode);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        binder.forField(nameField).asRequired().bind(Parking::getName, Parking::setName);
        binder.forField(cityField).asRequired().bind(Parking::getCity, Parking::setCity);
        binder.forField(streetField).asRequired().bind(Parking::getStreet, Parking::setStreet);
        binder.forField(zipCode).asRequired().bind(Parking::getZipCode, Parking::setZipCode);

        Button cancelButton = new Button("Cancel", e -> {
            this.close();
            nameField.clear();
            cityField.clear();
            streetField.clear();
            zipCode.clear();
        });
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            Parking parking = new Parking();
            try {
                binder.writeBean(parking);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }

            fireEvent(new SaveEvent(this, parking));
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


    public static abstract class ParkingDialogEvent extends ComponentEvent<Dialog> {


        private Parking parking;
        public ParkingDialogEvent(Dialog source, Parking parking) {
            super(source, false);
            this.parking = parking;
        }

        public Parking getParking() {
            return parking;
        }
    }

    public static class SaveEvent extends ParkingDialogEvent {

        public SaveEvent(Dialog source, Parking parking) {
            super(source, parking);
        }
    }

    public static class DeleteEvent extends ParkingDialogEvent {
        DeleteEvent(Dialog source, Parking parking) {
            super(source, parking);
        }

    }

    public static class CloseEvent extends ParkingDialogEvent{
        CloseEvent(Dialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
