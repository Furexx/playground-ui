package de.bonprix.playground.components.editForm;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.Parking;

public class ParkingEditForm extends FormLayout {
    private Parking parking;

    TextField nameField = new TextField("Name");
    TextField cityField = new TextField("City");
    TextField streetField = new TextField("Street");
    IntegerField zipCode = new IntegerField("Zip code");

    Binder<Parking> binder = new BeanValidationBinder<>(Parking.class);

    Button edit = new Button("Edit");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    public ParkingEditForm() {
        addClassName("parking-form");
        binder.bindInstanceFields(this);

        binder.forField(nameField).asRequired().bind(Parking::getName, Parking::setName);
        binder.forField(cityField).asRequired().bind(Parking::getCity, Parking::setCity);
        binder.forField(streetField).asRequired().bind(Parking::getStreet, Parking::setStreet);
        binder.forField(zipCode).asRequired().bind(Parking::getZipCode, Parking::setZipCode);

        add(nameField, cityField, streetField, zipCode, createButtonsLayout());

    }

    private HorizontalLayout createButtonsLayout() {
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        cancel.getStyle().set("margin-inline-end", "auto");


        edit.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, parking)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> edit.setEnabled(binder.isValid()));

        return new HorizontalLayout(edit, cancel, delete);
    }

    public void setParking(Parking parking) {
        this.parking = parking;
        binder.readBean(parking);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(parking);
            fireEvent(new SaveEvent(this, parking));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class ParkingFormEvent extends ComponentEvent<ParkingEditForm> {
        private Parking parking;

        protected ParkingFormEvent(ParkingEditForm source, Parking parking) {
            super(source, false);
            this.parking = parking;
        }

        public Parking getParking() {
            return parking;
        }
    }
        public static class SaveEvent extends ParkingFormEvent {
            SaveEvent(ParkingEditForm source, Parking parking) {
                super(source, parking);
            }
        }

        public static class DeleteEvent extends ParkingFormEvent {
            DeleteEvent(ParkingEditForm source, Parking parking) {
                super(source, parking);
            }
        }

        public static class CloseEvent extends ParkingFormEvent {
            CloseEvent(ParkingEditForm source) {
                super(source, null);
            }
        }

        public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                      ComponentEventListener<T> listener) {
            return getEventBus().addListener(eventType, listener);
        }

}

