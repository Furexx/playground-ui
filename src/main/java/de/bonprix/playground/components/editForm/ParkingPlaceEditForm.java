package de.bonprix.playground.components.editForm;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.dto.ParkingZone;

import java.util.List;
import java.util.stream.Collectors;

public class ParkingPlaceEditForm extends FormLayout {
        private ParkingPlace parkingPlace;

        IntegerField numberField = new IntegerField("Number");

        ComboBox<ParkingZone> zone = new ComboBox<>("Parking Zone");

        ComboBox<Car> car = new ComboBox<>("Car");

        Binder<ParkingPlace> binder = new BeanValidationBinder<>(ParkingPlace.class);

        Button edit = new Button("Edit");
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");


        public ParkingPlaceEditForm(List<ParkingZone> parkingZones, List<Car> cars,List<ParkingPlace> places) {
            binder.forField(zone).asRequired().bind(ParkingPlace::getParkingZone,ParkingPlace::setParkingZone);
            binder.forField(car).bind(ParkingPlace::getCar,ParkingPlace::setCar);

            zone.setItems(parkingZones);
            zone.setItemLabelGenerator(ParkingZone::getName);


            List<Car> placeCars = places.stream()
                    .filter(ParkingPlace -> ParkingPlace.getCar() != null)
                    .map(ParkingPlace::getCar)
                    .collect(Collectors.toList());


            cars.removeAll(placeCars);

            car.setItems(cars);
            car.setItemLabelGenerator(Car::getPlateNumber);
            car.setClearButtonVisible(true);


            binder.forField(numberField).asRequired().bind(ParkingPlace::getNumber,ParkingPlace::setNumber);

            add(numberField, zone, car, createButtonsLayout());

        }

        private HorizontalLayout createButtonsLayout() {
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_ERROR);
            cancel.getStyle().set("margin-inline-end", "auto");

            edit.addClickListener(event -> validateAndSave());
            delete.addClickListener(event -> {
                fireEvent(new DeleteEvent(this, parkingPlace));
                UI.getCurrent().getPage().reload();
            });
            cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

            binder.addStatusChangeListener(event -> edit.setEnabled(binder.isValid()));

            return new HorizontalLayout(edit,  cancel, delete);
        }

        public void setParkingPlace(ParkingPlace place) {
            this.parkingPlace = place;
            binder.readBean(place);
        }

        private void validateAndSave() {
            try {
                binder.writeBean(parkingPlace);
                fireEvent(new SaveEvent(this, parkingPlace));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            UI.getCurrent().getPage().reload();
        }


        public static abstract class ParkingPlaceEditFormEvent extends ComponentEvent<ParkingPlaceEditForm> {


            private ParkingPlace place;
            public ParkingPlaceEditFormEvent(ParkingPlaceEditForm source, ParkingPlace place) {
                super(source, false);
                this.place = place;
            }

            public ParkingPlace getParkingPlace() {
                return place;
            }
        }

        public static class SaveEvent extends ParkingPlaceEditFormEvent {

            public SaveEvent(ParkingPlaceEditForm source, ParkingPlace place) {
                super(source, place);
            }
        }

        public static class DeleteEvent extends ParkingPlaceEditFormEvent {
            DeleteEvent(ParkingPlaceEditForm source, ParkingPlace place) {
                super(source, place);
            }
        }

        public static class CloseEvent extends ParkingPlaceEditFormEvent {
            CloseEvent(ParkingPlaceEditForm source) {
                super(source, null);
            }
        }

        public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                      ComponentEventListener<T> listener) {
            return getEventBus().addListener(eventType, listener);
        }
    }


