package de.bonprix.playground;

import de.bonprix.dto.Parking;
import de.bonprix.dto.ParkingZone;
import de.bonprix.vaadin.mvp.View;

import java.util.List;

public interface ParkingZoneView extends View<ParkingZoneView.Presenter> {

    void updateList();

    interface Presenter extends de.bonprix.vaadin.mvp.Presenter<ParkingZoneView>{
        List<ParkingZone> getAllParkingzones();

        List<Parking> getAllParkings();
        void createZone(ParkingZone zone);

        void deleteParkingZone(ParkingZone zone);

        void updateParkingZone(ParkingZone zone);
    }

}
