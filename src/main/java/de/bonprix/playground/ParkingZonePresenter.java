package de.bonprix.playground;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import de.bonprix.dto.Parking;
import de.bonprix.dto.ParkingZone;
import de.bonprix.service.parking.ParkingService;
import de.bonprix.service.parking.filter.ParkingFilter;
import de.bonprix.service.parkingzone.ParkingZoneService;
import de.bonprix.service.parkingzone.filter.ParkingZoneFilter;
import de.bonprix.vaadin.mvp.BasePresenter;

import javax.annotation.Resource;
import java.util.List;

@SpringComponent
@VaadinSessionScope
public class ParkingZonePresenter extends BasePresenter<ParkingZoneView> implements ParkingZoneView.Presenter {
    @Resource
    private ParkingZoneService parkingZoneService;

    @Resource
    private ParkingService parkingService;


    private ParkingZoneFilter parkingZoneFilter = new ParkingZoneFilter();

    private ParkingFilter parkingFilter = new ParkingFilter();

    @Override
    public void afterViewInit() {
        getView().updateList();
    }


    @Override
    public List<ParkingZone> getAllParkingzones() {
        return parkingZoneService.findAll(parkingZoneFilter);
    }

    @Override
    public List<Parking> getAllParkings() {
        return parkingService.findAll(parkingFilter);
    }

    @Override
    public void createZone(ParkingZone zone) {
        parkingZoneService.create(zone);
    }

    @Override
    public void deleteParkingZone(ParkingZone zone) {
        parkingZoneService.deleteById(zone.getId());
    }

    @Override
    public void updateParkingZone(ParkingZone zone) {
        parkingZoneService.update(zone);
    }


}
