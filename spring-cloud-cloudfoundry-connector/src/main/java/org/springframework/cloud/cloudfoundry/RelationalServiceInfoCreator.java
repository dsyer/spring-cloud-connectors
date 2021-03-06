package org.springframework.cloud.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.cloud.util.UriInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class RelationalServiceInfoCreator<SI extends RelationalServiceInfo> extends CloudFoundryServiceInfoCreator<SI> {

	public RelationalServiceInfoCreator(Tags tags, String uriScheme) {
		super(tags, uriScheme);
	}

	public abstract SI createServiceInfo(String id, String uri);

	public SI createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");

		String id = (String) serviceData.get("name");

		String uri;
		if (credentials.containsKey("uri")) {
			uri = credentials.get("uri").toString();
		} else {
			String host = (String) credentials.get("host");
			int port = Integer.parseInt(credentials.get("port").toString()); // allows the port attribute to be quoted or plain

			String username = (String) credentials.get("user");
			String password = (String) credentials.get("password");

			String database = (String) credentials.get("name");
			
			uri = new UriInfo(getUriScheme(), host, port, username, password, database).toString();
		}
		return createServiceInfo(id, uri);
	}
}
