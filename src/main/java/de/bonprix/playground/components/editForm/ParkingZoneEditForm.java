package de.bonprix.playground.components.editForm;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.Parking;
import de.bonprix.dto.ParkingZone;

import java.util.List;

public class ParkingZoneEditForm extends FormLayout {
    private ParkingZone parkingZone;

    TextField nameField = new TextField("Name");

    ComboBox<Parking> parking = new ComboBox<>("Parking");

    Binder<ParkingZone> binder = new BeanValidationBinder<>(ParkingZone.class);

    Button edit = new Button("Edit");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");


    public ParkingZoneEditForm(List<Parking> parkings) {
        binder.bindInstanceFields(this);

        parking.setItems(parkings);
        parking.setItemLabelGenerator(Parking::getName);


        binder.forField(nameField).asRequired().bind(ParkingZone::getName,ParkingZone::setName);

        add(nameField,parking,createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        cancel.getStyle().set("margin-inline-end", "auto");

        edit.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, parkingZone)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> edit.setEnabled(binder.isValid()));

        return new HorizontalLayout(edit, cancel, delete);
    }

    public void setParkingZone(ParkingZone zone) {
        this.parkingZone = zone;
        binder.readBean(zone);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(parkingZone);
            fireEvent(new SaveEvent(this, parkingZone));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class ParkingZoneEditFormEvent extends ComponentEvent<ParkingZoneEditForm> {


        private ParkingZone zone;
        public ParkingZoneEditFormEvent(ParkingZoneEditForm source, ParkingZone zone) {
            super(source, false);
            this.zone = zone;
        }

        public ParkingZone getParkingZone() {
            return zone;
        }
    }

    public static class SaveEvent extends ParkingZoneEditFormEvent {

        public SaveEvent(ParkingZoneEditForm source, ParkingZone zone) {
            super(source, zone);
        }
    }

    public static class DeleteEvent extends ParkingZoneEditForm.ParkingZoneEditFormEvent {
        DeleteEvent(ParkingZoneEditForm source, ParkingZone zone) {
            super(source, zone);
        }
    }

    public static class CloseEvent extends ParkingZoneEditFormEvent {
        CloseEvent(ParkingZoneEditForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

