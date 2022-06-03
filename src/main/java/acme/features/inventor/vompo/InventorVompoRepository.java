package acme.features.inventor.vompo;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Configuration;
import acme.entities.Item;
import acme.entities.Vompo;
import acme.framework.entities.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Inventor;

@Repository
public interface InventorVompoRepository extends AbstractRepository {

	@Query("SELECT userAccount FROM UserAccount userAccount WHERE userAccount.id = :id")
	UserAccount findOneUserAccountById(int id);
	
	@Query("SELECT inventor FROM Inventor inventor WHERE inventor.id = :id")
	Inventor findOneInventorById(int id);
	
	@Query("SELECT inventor FROM Inventor inventor WHERE inventor.userAccount.id = :id")
	Inventor findOneInventorByAccountId(int id);
	
	@Query("SELECT v FROM Vompo v WHERE v.id = :id")
	Vompo findOneVompoById(int id);
	
	@Query("SELECT i FROM Item i WHERE i.id = :id")
	Item findOneItemById(int id);
	
	@Query("SELECT v FROM Vompo v")
	Collection<Vompo> findAllVompos();
	
	@Query("SELECT v FROM Vompo v where v.item.inventor.id = :id")
	Collection<Vompo> findAllVomposByInventorId(int id);
	
	@Query("select configuration from Configuration configuration")
	Configuration findConfiguration();
}