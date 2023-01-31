package de.bonprix.playground.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import de.bonprix.dto.Parking;
import de.bonprix.dto.ParkingZone;
import de.bonprix.playground.ParkingZoneView;
import de.bonprix.playground.components.addDialog.ParkingZoneAddDialog;
import de.bonprix.playground.components.editForm.ParkingZoneEditForm;
import de.bonprix.vaadin.components.menu.ModuleView;
import de.bonprix.vaadin.mvp.BaseView;

import java.util.List;

@ModuleView(name = "ParkingZone",
        route = "ParkingZone",
        position = 3)
public class ParkingZoneViewImpl extends BaseView<ParkingZoneView.Presenter> implements ParkingZoneView {

    Grid<ParkingZone> grid = new Grid<>(ParkingZone.class,false);

    ParkingZoneAddDialog addDialog;

    ParkingZoneEditForm editForm;
    Button button = new Button("Add new", new Icon(VaadinIcon.PLUS_CIRCLE), e -> addDialog.open());
    @Override
    protected void init() {
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addDialog = new ParkingZoneAddDialog(getParkings());
        addDialog.addListener(ParkingZoneAddDialog.SaveEvent.class,this::saveZone);

        configureGrid();

        editForm = new ParkingZoneEditForm(getParkings());
        editForm.setWidth("25em");
        editForm.addListener(ParkingZoneEditForm.SaveEvent.class, this::updateParkingZone);
        editForm.addListener(ParkingZoneEditForm.DeleteEvent.class, this::deleteParkingZone);
        editForm.addListener(ParkingZoneEditForm.CloseEvent.class, event -> closeEditor());

        FlexLayout flexLayout = new FlexLayout(grid, editForm);

        flexLayout.setFlexGrow(2, grid);
        flexLayout.setFlexGrow(1, editForm);
        flexLayout.setFlexShrink(0, editForm);
        flexLayout.setSizeFull();

        add(button,flexLayout);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event -> editParkingZone(event.getValue()));
    }

    @Override
    public void updateList() {
        grid.setItems(getPresenter().getAllParkingzones());
    }


    public void saveZone(ParkingZoneAddDialog.SaveEvent event) {
        getPresenter().createZone(event.getZone());
        updateList();
    }



    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(ParkingZone::getId).setHeader("ID").setSortable(true);
        grid.addColumn(ParkingZone::getName).setHeader("Name").setSortable(true);
        grid.addColumn(parkingZone -> parkingZone.getParking().getName()).setHeader("Parking").setSortable(true);
        grid.getColumns().forEach(parkingZoneColumn -> parkingZoneColumn.setAutoWidth(true));
    }

    public List<Parking> getParkings(){
        return getPresenter().getAllParkings();
    }


    private void updateParkingZone(ParkingZoneEditForm.SaveEvent event) {
        getPresenter().updateParkingZone(event.getParkingZone());
        updateList();
    }


    private void deleteParkingZone(ParkingZoneEditForm.DeleteEvent event) {
        getPresenter().deleteParkingZone(event.getParkingZone());
        updateList();
    }

    private void editParkingZone(ParkingZone zone) {
        if (zone == null) {
            closeEditor();
        } else {
            editForm.setParkingZone(zone);
            editForm.setVisible(true);
            addClassName("editing");
        }
    }


    private void closeEditor() {
        editForm.setParkingZone(null);
        editForm.setVisible(false);
        removeClassName("editing");
    }
}
