package de.bonprix.playground;

import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.vaadin.mvp.View;

import java.util.List;

public interface CarView extends View<CarView.Presenter> {

    void updateList();

    interface Presenter extends de.bonprix.vaadin.mvp.Presenter<CarView>{

        List<Car> getAllCars();

        void createCar(Car car);

        void updateCar(Car car);

        void deleteCar(Car car);

        List<ParkingPlace> getParkingPlaces();
    }


}

