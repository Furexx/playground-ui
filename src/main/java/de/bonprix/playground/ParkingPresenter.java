package de.bonprix.playground;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import de.bonprix.dto.Parking;
import de.bonprix.service.parking.ParkingService;
import de.bonprix.service.parking.filter.ParkingFilter;
import de.bonprix.vaadin.mvp.BasePresenter;

import javax.annotation.Resource;
import java.util.List;

@SpringComponent
@VaadinSessionScope
public class ParkingPresenter extends BasePresenter<ParkingView> implements ParkingView.Presenter {

    @Resource
    private ParkingService parkingService;


    private ParkingFilter parkingFilter = new ParkingFilter();


    @Override
    public void afterViewInit() {
       getView().updateList();
    }


    @Override
    public List<Parking> getAllParkings() {
        return parkingService.findAll(parkingFilter);
    }


    @Override
    public void createParking(Parking parking) {
        parkingService.create(parking);
    }
    @Override
    public void deleteParking(Parking parking) {
        parkingService.deleteById(parking.getId());
    }
    @Override
    public void updateParking(Parking parking) {
        parkingService.update(parking);
    }
}
