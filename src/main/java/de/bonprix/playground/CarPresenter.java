package de.bonprix.playground;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.service.car.CarService;
import de.bonprix.service.car.filter.CarFilter;
import de.bonprix.service.parkingplace.ParkingPlaceService;
import de.bonprix.service.parkingplace.filter.ParkingPlaceFilter;
import de.bonprix.vaadin.mvp.BasePresenter;

import javax.annotation.Resource;
import java.util.List;

@SpringComponent
@VaadinSessionScope
public class CarPresenter extends BasePresenter<CarView> implements CarView.Presenter {

    @Resource
    private CarService carService;

    @Resource
    private ParkingPlaceService parkingPlaceService;

    private CarFilter carFilter = new CarFilter();
    private ParkingPlaceFilter parkingPlaceFilter = new ParkingPlaceFilter();

    @Override
    public void afterViewInit() {
        getView().updateList();
    }


    @Override
    public List<Car> getAllCars() {
        return carService.findAll(carFilter);
    }

    @Override
    public void createCar(Car car) {
        carService.create(car);
    }

    @Override
    public void updateCar(Car car) {
        carService.update(car);
    }

    @Override
    public void deleteCar(Car car) {
            carService.deleteById(car.getId());
    }

    @Override
    public List<ParkingPlace> getParkingPlaces() {
        return parkingPlaceService.findAll(parkingPlaceFilter);
    }

}
