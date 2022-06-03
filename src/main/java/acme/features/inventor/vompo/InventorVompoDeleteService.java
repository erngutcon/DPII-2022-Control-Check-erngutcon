package acme.features.inventor.vompo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Vompo;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Inventor;

@Service
public class InventorVompoDeleteService implements AbstractDeleteService<Inventor, Vompo> {

	@Autowired
	protected InventorVompoRepository repository;
			
	@Override
	public boolean authorise(final Request<Vompo> request) {
		assert request != null;
		
		boolean result;
		int id;
		final Vompo vompo;

		id = request.getModel().getInteger("id");
		vompo = this.repository.findOneVompoById(id);
		result = vompo.getItem().getInventor().getId() == request.getPrincipal().getActiveRoleId();
		return result;
	}
	
	@Override
	public void bind(final Request<Vompo> request, final Vompo entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "code", "creationMoment", "subject", "explanation", "startPeriod", "finishPeriod", "share", "moreInfo");
	}
	
	@Override
	public void unbind(final Request<Vompo> request, final Vompo entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "code", "creationMoment", "subject", "explanation", "startPeriod", "finishPeriod", "share", "moreInfo");
	}
	
	@Override
	public Vompo findOne(final Request<Vompo> request) {
		assert request != null;
		
		Vompo result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneVompoById(id);

		return result;
	}
	
	@Override
	public void validate(final Request<Vompo> request, final Vompo entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}
	
	@Override
	public void delete(final Request<Vompo> request, final Vompo entity) {
		assert request != null;
		assert entity != null;

		this.repository.delete(entity);
	}  
}