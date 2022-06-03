package acme.features.inventor.vompo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Vompo;
import acme.framework.controllers.AbstractController;
import acme.roles.Inventor;

@Controller
public class InventorVompoController extends AbstractController<Inventor, Vompo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorVompoListService listService;

	@Autowired
	protected InventorVompoShowService showService;
	
	@Autowired
	protected InventorVompoCreateService createService;
	
	@Autowired
	protected InventorVompoUpdateService updateService;
	
	@Autowired
	protected InventorVompoDeleteService deleteService;
	
	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addCommand("list", this.listService);
		super.addCommand("show", this.showService);
		super.addCommand("create", this.createService);
		super.addCommand("update", this.updateService);
		super.addCommand("delete", this.deleteService);
	}
}