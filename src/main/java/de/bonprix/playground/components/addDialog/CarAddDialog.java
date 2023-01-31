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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.bonprix.dto.Car;

public class CarAddDialog extends Dialog {

    TextField plateNumber = new TextField("Plate Number");

    Binder<Car> binder = new BeanValidationBinder<>(Car.class);

    public CarAddDialog(){

        H2 headline = new H2("Create new car");


        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");


        VerticalLayout fieldLayout = new VerticalLayout(plateNumber);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        binder.forField(plateNumber).asRequired().bind(Car::getPlateNumber, Car::setPlateNumber);


        Button cancelButton = new Button("Cancel", e -> {
            this.close();
           plateNumber.clear();
        })  ;
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            Car car = new Car();
            try {
                binder.writeBean(car);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }

            fireEvent(new CarAddDialog.SaveEvent(this, car));
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

    public static abstract class CarDialogEvent extends ComponentEvent<Dialog> {


        private Car car;
        public CarDialogEvent(Dialog source, Car car) {
            super(source, false);
            this.car = car;
        }

        public Car getCar() {
            return car;
        }
    }

    public static class SaveEvent extends CarAddDialog.CarDialogEvent {

        public SaveEvent(Dialog source, Car car) {
            super(source, car);
        }
    }

    public static class CloseEvent extends CarAddDialog.CarDialogEvent {
        CloseEvent(Dialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

