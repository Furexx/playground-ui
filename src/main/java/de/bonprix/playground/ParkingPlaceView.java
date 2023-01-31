package de.bonprix.playground;

import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.dto.ParkingZone;
import de.bonprix.vaadin.mvp.View;

import java.util.List;

public interface ParkingPlaceView extends View<ParkingPlaceView.Presenter> {

        void updateList();
    interface Presenter extends de.bonprix.vaadin.mvp.Presenter<ParkingPlaceView>{
        List<ParkingPlace> getAllParkingPlaces();

        List<ParkingZone> getParkingZones();
        List<Car> getCars();

        void createPlace(ParkingPlace place);

        void deleteParkingPlace(ParkingPlace place);

        void updateParkingPlace(ParkingPlace place);
    }
}
