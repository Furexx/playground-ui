package de.bonprix.playground.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import de.bonprix.dto.Parking;
import de.bonprix.playground.ParkingView;
import de.bonprix.playground.components.addDialog.ParkingAddDialog;
import de.bonprix.playground.components.editForm.ParkingEditForm;
import de.bonprix.vaadin.components.gridheaderfilter.GridHeaderFilterExtension;
import de.bonprix.vaadin.components.menu.ModuleView;
import de.bonprix.vaadin.data.filter.AdvancedStringFilter;
import de.bonprix.vaadin.mvp.BaseView;

@ModuleView(name = "Parking",
        route = "Parking",
        position = 2)
public class ParkingViewImpl extends BaseView<ParkingView.Presenter> implements ParkingView {

    private static final long serialVersionUID = 1L;

    Grid<Parking> grid = new Grid<>(Parking.class,false);

    GridContextMenu<Parking> gridContextMenu = grid.addContextMenu();

    ParkingAddDialog addDialog = new ParkingAddDialog();

    ParkingEditForm editForm;

    Button button = new Button("Add new", new Icon(VaadinIcon.PLUS_CIRCLE), e -> addDialog.open());


    @Override
    protected void init() {
        addClassName("list-view");
        setSizeFull();
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addDialog.addListener(ParkingAddDialog.SaveEvent.class, this::saveParking);

        editForm = new ParkingEditForm();
        editForm.setWidth("25em");
        editForm.addListener(ParkingEditForm.SaveEvent.class, this::updateParking);
        editForm.addListener(ParkingEditForm.DeleteEvent.class, this::deleteParking);
        editForm.addListener(ParkingEditForm.CloseEvent.class, event -> closeEditor());

        configureGrid();

        FlexLayout flexLayout = new FlexLayout(grid, editForm);

        flexLayout.setFlexGrow(2, grid);
        flexLayout.setFlexGrow(1, editForm);
        flexLayout.setFlexShrink(0, editForm);
        flexLayout.setSizeFull();

        add(button, flexLayout);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event -> editParking(event.getValue()));
    }
    private void configureGrid() {
        grid.addClassNames("parking-grid");
        grid.setSizeFull();
        Grid.Column<Parking> idColumn = grid.addColumn(Parking::getId).setHeader("ID").setSortable(true);
        Grid.Column<Parking> nameColumn = grid.addColumn(Parking::getName).setHeader("Name").setSortable(true);
        Grid.Column<Parking> cityColumn = grid.addColumn(Parking::getCity).setHeader("City").setSortable(true);
        Grid.Column<Parking> streetColumn = grid.addColumn(Parking::getStreet).setHeader("Street").setSortable(true);
        Grid.Column<Parking> zipCodeColumn = grid.addColumn(Parking::getZipCode).setHeader("Zip Code").setSortable(true);
        grid.getColumns().forEach(parkingColumn -> parkingColumn.setAutoWidth(true));

        GridHeaderFilterExtension<Parking> headerFilterExtension = new GridHeaderFilterExtension<>(grid);
        headerFilterExtension.addLongFilter(idColumn, Parking::getId);
        headerFilterExtension.addStringFilter(nameColumn,Parking::getName, new AdvancedStringFilter(true));
        headerFilterExtension.addStringFilter(cityColumn, Parking::getCity, new AdvancedStringFilter(true));
        headerFilterExtension.addStringFilter(streetColumn, Parking::getStreet, new AdvancedStringFilter(true));
        headerFilterExtension.addIntegerFilter(zipCodeColumn, Parking::getZipCode);

    }


    @Override
    public void updateList() {
        grid.setItems(getPresenter().getAllParkings());
    }

    private void saveParking(ParkingAddDialog.SaveEvent event) {
        getPresenter().createParking(event.getParking());
        updateList();
    }

    private void updateParking(ParkingEditForm.SaveEvent event) {
        getPresenter().updateParking(event.getParking());
        updateList();
    }

    private void deleteParking(ParkingEditForm.DeleteEvent event) {
        try {
            getPresenter().deleteParking(event.getParking());
        }
        catch (Exception e) {
            Notification error = new Notification("Please first remove the parking zone from the parking",2000, Notification.Position.MIDDLE);
            error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            error.open();
        }

        updateList();
    }

    private void editParking(Parking parking) {
        if (parking == null) {
            closeEditor();
        } else {
            editForm.setParking(parking);
            editForm.setVisible(true);
            addClassName("editing");
        }
    }


    private void closeEditor() {
        editForm.setParking(null);
        editForm.setVisible(false);
        removeClassName("editing");
    }
}
