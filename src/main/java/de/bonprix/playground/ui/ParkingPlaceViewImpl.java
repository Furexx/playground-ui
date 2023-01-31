package de.bonprix.playground.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.bonprix.dto.Car;
import de.bonprix.dto.ParkingPlace;
import de.bonprix.dto.ParkingZone;
import de.bonprix.playground.ParkingPlaceView;
import de.bonprix.playground.components.addDialog.ParkingPlaceAddDialog;
import de.bonprix.playground.components.editForm.ParkingPlaceEditForm;
import de.bonprix.vaadin.components.menu.ModuleView;
import de.bonprix.vaadin.mvp.BaseView;

import java.util.List;

@ModuleView(
        name = "ParkingPlace",  route = "ParkingPlace", position = 4
)
public class ParkingPlaceViewImpl extends BaseView<ParkingPlaceView.Presenter> implements ParkingPlaceView {

    Grid<ParkingPlace> grid = new Grid<>(ParkingPlace.class, false);

    ParkingPlaceAddDialog addDialog;
    ParkingPlaceEditForm editForm;

    Button button = new Button("Add new", new Icon(VaadinIcon.PLUS_CIRCLE), e -> addDialog.open());


    @Override
    protected void init() {
        HorizontalLayout buttons = new HorizontalLayout(button);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addDialog = new ParkingPlaceAddDialog(getParkingZones()/*,getCars()*/);
        addDialog.addListener(ParkingPlaceAddDialog.SaveEvent.class,this::savePlace);



        configureGrid();

        editForm = new ParkingPlaceEditForm(getParkingZones(),getCars(),getPresenter().getAllParkingPlaces());
        editForm.setWidth("25em");
        editForm.addListener(ParkingPlaceEditForm.SaveEvent.class, this::updateParkingPlace);
        editForm.addListener(ParkingPlaceEditForm.DeleteEvent.class, this::deleteParkingPlace);
        editForm.addListener(ParkingPlaceEditForm.CloseEvent.class, event -> closeEditor());



        FlexLayout flexLayout = new FlexLayout(grid, editForm);

        flexLayout.setFlexGrow(2, grid);
        flexLayout.setFlexGrow(1, editForm);
        flexLayout.setFlexShrink(0, editForm);
        flexLayout.setSizeFull();

        add(buttons,flexLayout);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event -> editParkingPlace(event.getValue()));

    }

    private void configureGrid(){
        grid.setSizeFull();
        grid.addColumn(ParkingPlace::getId).setHeader("ID").setSortable(true);
        grid.addColumn(ParkingPlace::getNumber).setHeader("Number").setSortable(true);
        grid.addColumn(ParkingPlace -> ParkingPlace.getParkingZone().getName()).setHeader("Parking Zone").setSortable(true);
        grid.addColumn(parkingPlace -> {
            if (parkingPlace.getCar() == null) {
                return "Empty";
            } else {
                return parkingPlace.getCar().getPlateNumber();
            }

        } ).setHeader("Car").setSortable(true);
    }




    @Override
    public void updateList() {
        grid.setItems(getPresenter().getAllParkingPlaces());
    }

    public void savePlace(ParkingPlaceAddDialog.SaveEvent event){
        getPresenter().createPlace(event.getPlace());
        updateList();
    }
    public List<ParkingZone> getParkingZones() {
        return getPresenter().getParkingZones();
    }

    public List<Car> getCars() {
        return getPresenter().getCars();
    }

    private void updateParkingPlace(ParkingPlaceEditForm.SaveEvent event) {
        getPresenter().updateParkingPlace(event.getParkingPlace());
        updateList();
    }


    private void deleteParkingPlace(ParkingPlaceEditForm.DeleteEvent event) {
        getPresenter().deleteParkingPlace(event.getParkingPlace());
        updateList();
    }

    private void editParkingPlace(ParkingPlace place) {
        updateList();
        if (place == null) {
            closeEditor();
        } else {
            editForm.setParkingPlace(place);
            editForm.setVisible(true);
            addClassName("editing");
        }
    }


    private void closeEditor() {
        editForm.setParkingPlace(null);
        editForm.setVisible(false);
        removeClassName("editing");
    }
}
