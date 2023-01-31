package de.bonprix.playground;

import de.bonprix.dto.Parking;
import de.bonprix.vaadin.mvp.View;

import java.util.List;

public interface ParkingView extends View<ParkingView.Presenter> {

   void updateList();


    interface Presenter extends de.bonprix.vaadin.mvp.Presenter<ParkingView> {
        List<Parking> getAllParkings();


        void createParking(Parking parking);

        void deleteParking(Parking parking);

        void updateParking(Parking parking);

    }
}
