package acme.features.inventor.vompo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Vompo;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Inventor;

@Service
public class InventorVompoUpdateService implements AbstractUpdateService<Inventor, Vompo> {
	
	@Autowired
	protected InventorVompoRepository repository;
	
	@Override
	public boolean authorise(final Request<Vompo> request) {
		assert request != null;
		
		boolean result;
		Vompo chimpum;
		int id;
		
		id = request.getModel().getInteger("id");
		chimpum = this.repository.findOneVompoById(id);
		result = chimpum.getItem().getInventor().getId() == request.getPrincipal().getActiveRoleId();
		return result;
	}
	
	@Override
	public void bind(final Request<Vompo> request, final Vompo entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "code", "creationMoment", "subject", "explanation", "share", "moreInfo");
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
		
		if (!errors.hasErrors("startPeriod")) {
			Calendar calendar;

			calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, 1);
			errors.state(request, entity.getStartPeriod().after(calendar.getTime()), "startPeriod", "inventor.vompo.form.error.too-close-start-period");
		}
		if (!errors.hasErrors("finishPeriod")) {
			Calendar calendar;
			Date finish;
			calendar = new GregorianCalendar();
			calendar.setTime(entity.getStartPeriod());
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			finish = calendar.getTime();
			errors.state(request, entity.getFinishPeriod().after(finish), "finishPeriod", "inventor.vompo.form.error.one-week");
		}
		if(!errors.hasErrors("share")) {
			final String upperCaseCurrency = entity.getShare().getCurrency().toUpperCase();
			boolean accepted = false;
			
			// Manage likely currencies
			for (final String acceptedCurrency : this.repository.findConfiguration().getAcceptedCurrencies().toUpperCase().split("[.]")) {
				if (upperCaseCurrency.equals(acceptedCurrency)) {
					accepted = true;
					break;
				}
			}
			errors.state(request, entity.getShare().getAmount() > 0, "share", "inventor.vompo.form.error.negative-share");
			errors.state(request, accepted, "share", "inventor.vompo.form.error.non-accepted-currency");
		}
	}
	
	@Override
	public void update(final Request<Vompo> request, final Vompo entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}
}