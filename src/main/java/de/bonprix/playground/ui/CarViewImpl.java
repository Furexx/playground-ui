package de.bonprix.playground.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.playground.CarView;
import de.bonprix.playground.components.addDialog.CarAddDialog;
import de.bonprix.playground.components.editForm.CarEditForm;
import de.bonprix.vaadin.components.menu.ModuleView;
import de.bonprix.vaadin.mvp.BaseView;

import java.util.List;

@ModuleView(
        name = "Car", position = 5, route = "Car"
)
public class CarViewImpl extends BaseView<CarView.Presenter> implements CarView {
    CarAddDialog addDialog;

    CarEditForm editForm;
    Button button = new Button("Add new", new Icon(VaadinIcon.PLUS_CIRCLE), e -> addDialog.open());

    Grid<Car> grid = new Grid<>(Car.class,false);

    @Override
    protected void init() {
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        configureGrid();

        addDialog = new CarAddDialog();
        addDialog.addListener(CarAddDialog.SaveEvent.class, this::saveCar);

        editForm = new CarEditForm(getParkingPlaces());
        editForm.setWidth("25em");
        editForm.addListener(CarEditForm.SaveEvent.class, this::updateCar);
        editForm.addListener(CarEditForm.DeleteEvent.class, this::deleteCar);
        editForm.addListener(CarEditForm.CloseEvent.class, event -> closeEditor());

        FlexLayout flexLayout = new FlexLayout(grid, editForm);

        flexLayout.setFlexGrow(2, grid);
        flexLayout.setFlexGrow(1, editForm);
        flexLayout.setFlexShrink(0, editForm);
        flexLayout.setSizeFull();

        add(button,flexLayout);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event -> editCar(event.getValue()));

    }

    private List<ParkingPlace> getParkingPlaces() {
     return  getPresenter().getParkingPlaces();
    }


    @Override
    public void updateList() {
        grid.setItems(getPresenter().getAllCars());
    }

    private void configureGrid(){
        grid.setSizeFull();
        grid.addColumn(Car::getId).setHeader("ID").setSortable(true);
        grid.addColumn(Car::getPlateNumber).setHeader("Plate Number").setSortable(true);
        grid.addColumn(Car -> {
            for (ParkingPlace place : getParkingPlaces()) {
                if (place.getCar() != null) {
                    if (place.getCar().getId() == Car.getId()){
                        return place.getNumber();
                    }
                }
            }
            return "Car is not parked";
        }).setHeader("Parking place").setSortable(true);

    }



    private void saveCar(CarAddDialog.SaveEvent event){
        getPresenter().createCar(event.getCar());
        updateList();
    }
    private void updateCar(CarEditForm.SaveEvent event) {
        getPresenter().updateCar(event.getCar());
        updateList();
    }


    private void deleteCar(CarEditForm.DeleteEvent event) {
        try {
            getPresenter().deleteCar(event.getCar());
        }
        catch (Exception e) {
            Notification error = new Notification("Please first remove the car from the place",2000, Notification.Position.MIDDLE);
            error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            error.open();
        }

        updateList();
    }

    private void editCar(Car car) {

        if (car == null) {
            closeEditor();
        } else {
            editForm.setCar(car);
            editForm.setVisible(true);
            addClassName("editing");
        }
    }


    private void closeEditor() {
        editForm.setCar(null);
        editForm.setVisible(false);
        removeClassName("editing");
    }



}
