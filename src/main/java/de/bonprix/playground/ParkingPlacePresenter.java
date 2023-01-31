package de.bonprix.playground;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.dto.ParkingZone;
import de.bonprix.service.car.CarService;
import de.bonprix.service.car.filter.CarFilter;
import de.bonprix.service.parkingplace.ParkingPlaceService;
import de.bonprix.service.parkingplace.filter.ParkingPlaceFilter;
import de.bonprix.service.parkingzone.ParkingZoneService;
import de.bonprix.service.parkingzone.filter.ParkingZoneFilter;
import de.bonprix.vaadin.mvp.BasePresenter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@SpringComponent
@VaadinSessionScope
public class ParkingPlacePresenter extends BasePresenter<ParkingPlaceView> implements ParkingPlaceView.Presenter {

    @Resource
    private ParkingPlaceService parkingPlaceService;

    @Resource
    private ParkingZoneService parkingZoneService;

    @Resource
    private CarService carService;

    private ParkingPlaceFilter parkingPlaceFilter = new ParkingPlaceFilter();
    private ParkingZoneFilter parkingZoneFilter = new ParkingZoneFilter();
    private CarFilter carFilter = new CarFilter();

    @Override
    public void afterViewInit(){
        getView().updateList();
    }

    @PostConstruct
    public void init(){

    }

    @Override
    public List<ParkingPlace> getAllParkingPlaces() {

        return parkingPlaceService.findAll(parkingPlaceFilter);
    }
    @Override
    public List<ParkingZone> getParkingZones() {
        return parkingZoneService.findAll(parkingZoneFilter);
    }

    @Override
    public List<Car> getCars() {
        return carService.findAll(new CarFilter());
    }

    @Override
    public void createPlace(ParkingPlace place) {

        parkingPlaceService.create(place);
    }

    @Override
    public void deleteParkingPlace(ParkingPlace place) {
        parkingPlaceService.deleteById(place.getId());
    }

    @Override
    public void updateParkingPlace(ParkingPlace place) {
        parkingPlaceService.update(place);
    }


}
