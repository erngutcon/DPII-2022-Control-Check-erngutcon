package acme.features.inventor.vompo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Vompo;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;

@Service
public class InventorVompoShowService implements AbstractShowService<Inventor, Vompo> {

	@Autowired
	protected InventorVompoRepository repository;

	@Override
	public boolean authorise(final Request<Vompo> request) {
		assert request != null;

		boolean result;
		int id;
		Vompo vompo;
		
		id = request.getModel().getInteger("id");
		vompo = this.repository.findOneVompoById(id);
		result = vompo.getItem().getInventor().getId() == request.getPrincipal().getActiveRoleId();

		return result;
	}

	@Override
	public Vompo findOne(final Request<Vompo> request) {
		assert request != null;

		int id;
		Vompo result;
		
		id = request.getModel().getInteger("id");
		result = this.repository.findOneVompoById(id);

		return result;
	}
	
	@Override
	public void unbind(final Request<Vompo> request, final Vompo entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "code", "creationMoment", "subject", "explanation", "startPeriod", "finishPeriod", "share", "moreInfo");
	}
}