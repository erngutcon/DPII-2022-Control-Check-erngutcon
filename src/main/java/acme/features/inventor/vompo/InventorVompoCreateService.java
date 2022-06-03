package acme.features.inventor.vompo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Item;
import acme.entities.Vompo;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractCreateService;
import acme.roles.Inventor;

@Service
public class InventorVompoCreateService implements AbstractCreateService<Inventor, Vompo> {
	
	@Autowired
	protected InventorVompoRepository repository;
	
	@Override
	public boolean authorise(final Request<Vompo> request) {
		assert request != null;
		
		boolean result;
		
		result = request.getPrincipal().hasRole(Inventor.class);
		
		return result;
	}

	@Override
	public void bind(final Request<Vompo> request, final Vompo entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		 
		request.bind(entity, errors, "code", "subject", "explanation", "startPeriod", "finishPeriod", "share", "moreInfo");
	}

	@Override
	public void unbind(final Request<Vompo> request, final Vompo entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "code", "creationMoment", "subject", "explanation", "startPeriod", "finishPeriod", "share", "moreInfo");		
		model.setAttribute("itemId", entity.getItem().getId());
	}
	
	@Override
	public Vompo instantiate(final Request<Vompo> request) {
		assert request != null;
		
		Vompo result;
		Item item;
		int itemId;
		
		result = new Vompo();
		
		// Manage unique code
		String code = "";

		code = this.createCode();
		result.setCode(code);
		
		final Date moment = new Date(System.currentTimeMillis() - 1000);
		result.setCreationMoment(moment);
		
		itemId = request.getModel().getInteger("itemId");
		item = this.repository.findOneItemById(itemId);
		result.setItem(item);
		
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
		if(!errors.hasErrors("budget")) {
			final String upperCaseCurrency = entity.getShare().getCurrency().toUpperCase();
			boolean accepted = false;
			
			// Manage likely currencies
			for (final String acceptedCurrency : this.repository.findConfiguration().getAcceptedCurrencies().toUpperCase().split("[.]")) {
				if (upperCaseCurrency.equals(acceptedCurrency)) {
					accepted = true;
					break;
				}
			}
			errors.state(request, entity.getShare().getAmount() > 0, "share", "inventor.vompo.form.error.negative-budget");
			errors.state(request, accepted, "share", "inventor.vompo.form.error.non-accepted-currency");
		}
	}
			
	@Override
	public void create(final Request<Vompo> request, final Vompo entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.save(entity);			
	}
	
	
	
	
	// Other business methods -------------------------

		public String createCode() {

			// The ticker must be as follow: YYMMDD
			String code = new String();

			// Get creation date
			final Calendar calendar = Calendar.getInstance();
			final String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
			String month = "";
			String day = "";

			if (calendar.get(Calendar.MONTH) + 1 < 10)
				month = "0" + (calendar.get(Calendar.MONTH) + 1);
			else
				month = String.valueOf(calendar.get(Calendar.MONTH) + 1);

			if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
				day = "0" + calendar.get(Calendar.DAY_OF_MONTH);
			else
				day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

			code =  year + month + day ;

			return code;

		}
}