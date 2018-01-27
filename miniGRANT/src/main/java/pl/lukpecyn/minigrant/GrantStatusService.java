package pl.lukpecyn.minigrant;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GrantStatusService {

	private static final Logger logger = LoggerFactory.getLogger(GrantStatusService.class);
	
	List<GrantStatus> grantStatusList;
	
	public GrantStatusService() {
		grantStatusList = new ArrayList<GrantStatus>();

		grantStatusList.add(new GrantStatus(0,"Projekt"));
		grantStatusList.add(new GrantStatus(10,"Realizacja"));
		grantStatusList.add(new GrantStatus(20,"Zakończony"));
		grantStatusList.add(new GrantStatus(30,"Rozliczony"));
		grantStatusList.add(new GrantStatus(99,"Zarchiwizowany"));
	}
	public List<GrantStatus> getGrantStatusList() {
				return grantStatusList;
	}
	
	public GrantStatus getGrantStatus(Integer i) {		
		
		for (GrantStatus gs : grantStatusList) {
			logger.info(gs.getId() + " - " + gs.getName());
			if (gs.getId()==i) {
				logger.info(gs.getId() + " = " + i);
				return gs;
			}
		}
		return null;
	}
}
