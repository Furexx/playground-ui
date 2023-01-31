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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.Parking;
import de.bonprix.dto.ParkingZone;

import java.util.List;

public class ParkingZoneAddDialog extends Dialog {
    TextField nameField = new TextField("Name");

    ComboBox<Parking> parking = new ComboBox<>("Parking");

    Binder<ParkingZone> binder = new BeanValidationBinder<>(ParkingZone.class);

    public ParkingZoneAddDialog(List<Parking> parkings){
        binder.bindInstanceFields(this);

        parking.setItems(parkings);
        parking.setItemLabelGenerator(Parking::getName);
        H2 headline = new H2("Create new parking zone");


        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");


        VerticalLayout fieldLayout = new VerticalLayout(nameField, parking);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        binder.forField(nameField).asRequired().bind(ParkingZone::getName,ParkingZone::setName);


        Button cancelButton = new Button("Cancel", e -> {
            close();
            nameField.clear();
            parking.clear();
        });
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            ParkingZone zone = new ParkingZone();
            try {
                binder.writeBean(zone);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }

            fireEvent(new ParkingZoneAddDialog.SaveEvent(this, zone));
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

    public static abstract class ParkingZoneDialogEvent extends ComponentEvent<Dialog> {


        private ParkingZone zone;
        public ParkingZoneDialogEvent(Dialog source, ParkingZone zone) {
            super(source, false);
            this.zone = zone;
        }

        public ParkingZone getZone() {
            return zone;
        }
    }

    public static class SaveEvent extends ParkingZoneAddDialog.ParkingZoneDialogEvent {

        public SaveEvent(Dialog source, ParkingZone zone) {
            super(source, zone);
        }
    }

    public static class CloseEvent extends ParkingZoneAddDialog.ParkingZoneDialogEvent {
        CloseEvent(Dialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}


