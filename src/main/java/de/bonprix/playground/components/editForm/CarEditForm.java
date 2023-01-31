package de.bonprix.playground.components.editForm;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;

import java.util.List;

public class CarEditForm extends FormLayout {
    private Car car;

    TextField plateNumberField = new TextField("Plate Number");



    Binder<Car> binder = new BeanValidationBinder<>(Car.class);

    Button edit = new Button("Edit");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");


    public CarEditForm(List<ParkingPlace> parkingPlaces) {


        binder.forField(plateNumberField).asRequired().bind(Car::getPlateNumber, Car::setPlateNumber);

        add(plateNumberField, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        cancel.getStyle().set("margin-inline-end", "auto");

        edit.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> {
            fireEvent(new DeleteEvent(this, car));
            UI.getCurrent().getPage().reload();
        });
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> edit.setEnabled(binder.isValid()));

        return new HorizontalLayout(edit, cancel, delete);
    }

    public void setCar(Car car) {
        this.car = car;
        binder.readBean(car);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(car);
            fireEvent(new SaveEvent(this, car));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        UI.getCurrent().getPage().reload();
    }


    public static abstract class CarEditFormEvent extends ComponentEvent<CarEditForm> {


        private Car car;
        public CarEditFormEvent(CarEditForm source, Car car) {
            super(source, false);
            this.car = car;
        }

        public Car getCar() {
            return car;
        }
    }

    public static class SaveEvent extends CarEditFormEvent {

        public SaveEvent(CarEditForm source, Car car) {
            super(source, car);
        }
    }

    public static class DeleteEvent extends CarEditFormEvent {
        DeleteEvent(CarEditForm source, Car car) {
            super(source, car);
        }
    }

    public static class CloseEvent extends CarEditFormEvent {
        CloseEvent(CarEditForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
