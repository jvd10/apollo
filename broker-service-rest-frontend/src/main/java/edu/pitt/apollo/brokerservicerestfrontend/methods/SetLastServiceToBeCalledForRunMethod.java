/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.apollo.brokerservicerestfrontend.methods;

import edu.pitt.apollo.exception.DataServiceException;
import edu.pitt.apollo.exception.SerializationException;
import edu.pitt.apollo.exception.UnsupportedSerializationFormatException;
import edu.pitt.apollo.brokerservicerestfrontend.utils.ResponseMessageBuilder;
import edu.pitt.apollo.types.v3_1_0.ApolloSoftwareTypeEnum;;
import edu.pitt.apollo.services_common.v3_1_0.SerializationFormat;
import edu.pitt.apollo.types.v3_1_0.SoftwareIdentification;
import java.math.BigInteger;
import org.springframework.http.HttpStatus;

/**
 *
 * @author nem41
 */
public class SetLastServiceToBeCalledForRunMethod extends BaseBrokerServiceAccessorMethod {

	public SetLastServiceToBeCalledForRunMethod(String username, String password, SerializationFormat serializationFormat) throws UnsupportedSerializationFormatException {
		super(username, password, serializationFormat);
	}

	public String setLastServiceToBeCalledForRunMethod(BigInteger runId, String softwareName, String softwareVersion,
			String softwareDeveloper, ApolloSoftwareTypeEnum softwareType) throws UnsupportedSerializationFormatException, SerializationException {

		SoftwareIdentification softwareId = new SoftwareIdentification();
		softwareId.setSoftwareDeveloper(softwareDeveloper);
		softwareId.setSoftwareName(softwareName);
		softwareId.setSoftwareType(softwareType);
		softwareId.setSoftwareVersion(softwareVersion);

		try {

			impl.updateLastServiceToBeCalledForRun(runId, softwareId, authentication);

			responseBuilder.setStatus(HttpStatus.OK, ResponseMessageBuilder.DEFAULT_SUCCESS_MESSAGE);
		} catch (DataServiceException ex) {
			responseBuilder.setStatus(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

		return serializer.serializeObject(responseBuilder.getResponse());
	}

}
