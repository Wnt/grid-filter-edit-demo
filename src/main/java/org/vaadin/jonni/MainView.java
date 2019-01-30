package org.vaadin.jonni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;

import com.github.javafaker.Faker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
// @PWA(name = "Project Base for Vaadin Flow", shortName = "Project Base")
public class MainView extends VerticalLayout {
	Faker f = new Faker(new Random(42));

	public MainView() {
		Grid<Dto> grid = new Grid<>(Dto.class);
		ListDataProvider<Dto> dataProvider = new ListDataProvider<Dto>(getDummyData(40));
		
		grid.setDataProvider(dataProvider);

		Editor<Dto> editor = grid.getEditor();
		editor.setBuffered(true);

		addEmailFilterRow(grid);

		addEmailEditField(grid);
		addNameEditField(grid);

		addEditButtonsColumn(grid);

		add(grid);
		grid.setSizeFull();
		setSizeFull();
	}

	private void addEmailFilterRow(Grid<Dto> grid) {
		ListDataProvider<Dto> dataProvider = (ListDataProvider<Dto>) grid.getDataProvider();
		
		HeaderRow filterHeaderRow = grid.appendHeaderRow();
		TextField emailFilterField = new TextField();
		emailFilterField.setPlaceholder("filter by email");
		emailFilterField.setWidth("100%");
		emailFilterField.setValueChangeMode(ValueChangeMode.EAGER);
		emailFilterField.addValueChangeListener(change -> {
			dataProvider.setFilter(dto -> {
				for (String email : dto.getEmails()) {
					if (email.contains(emailFilterField.getValue())) {
						return true;
					}
				}
				return false;
			});
		});
		filterHeaderRow.getCell(grid.getColumnByKey("emails")).setComponent(emailFilterField);
	}

	private void addEditButtonsColumn(Grid<Dto> grid) {
		Column<Dto> editorColumn = grid.addComponentColumn(item -> {
			Button edit = new Button("Edit");
			edit.addClassName("edit");
			edit.addClickListener(e -> grid.getEditor().editItem(item));
			return edit;
		}).setHeader("Tools");

		Button save = new Button("Save", e -> grid.getEditor().save());
		save.addClassName("save");

		Button cancel = new Button("Cancel", e -> grid.getEditor().cancel());
		cancel.addClassName("cancel");

		HorizontalLayout buttons = new HorizontalLayout(save, cancel);
		editorColumn.setEditorComponent(buttons);
	}

	private void addNameEditField(Grid<Dto> grid) {
		Binder<Dto> binder = grid.getEditor().getBinder();
		TextField nameField = new TextField();
		nameField.setWidth("100%");
		binder.forField(nameField).bind(Dto::getName, Dto::setName);
		grid.getColumnByKey("name").setEditorComponent(nameField);
	}

	private void addEmailEditField(Grid<Dto> grid) {
		Binder<Dto> binder = grid.getEditor().getBinder();

		TextField emailsField = new TextField();
		emailsField.setWidth("100%");
		binder.forField(emailsField).bind(dto -> {
			return dto.getEmails().stream().collect(Collectors.joining(","));
		}, (dto, value) -> {
			String[] emails = value.split(",");
			dto.setEmails(Arrays.asList(emails));
		});
		grid.getColumnByKey("emails").setEditorComponent(emailsField);
	}

	private Collection<Dto> getDummyData(int count) {
		ArrayList<Dto> list = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Double low = f.number().randomDouble(2, 0, 120);
			Double high = f.number().randomDouble(2, low.intValue(), 120);
			double mean = f.number().randomDouble(2, low.intValue(), high.intValue());
			List<String> emails = Arrays.asList(f.internet().emailAddress(), f.internet().emailAddress(),
					f.internet().emailAddress());
			list.add(new Dto(f.commerce().productName(), low, mean, high, emails));
		}
		return list;
	}
}
