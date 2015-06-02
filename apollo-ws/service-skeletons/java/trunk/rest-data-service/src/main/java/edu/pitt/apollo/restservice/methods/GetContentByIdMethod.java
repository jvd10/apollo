/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.apollo.restservice.methods;

import edu.pitt.apollo.DataServiceImpl;
import edu.pitt.apollo.exception.DataServiceException;
import edu.pitt.apollo.exception.SerializationException;
import edu.pitt.apollo.exception.UnsupportedSerializationFormatException;
import edu.pitt.apollo.restservice.utils.ResponseMessageBuilder;
import edu.pitt.apollo.services_common.v3_0_0.Authentication;
import edu.pitt.apollo.services_common.v3_0_0.SerializationFormat;
import edu.pitt.apollo.utilities.Serializer;
import edu.pitt.apollo.utilities.SerializerFactory;
import java.math.BigInteger;
import org.springframework.http.HttpStatus;

/**
 *
 * @author nem41
 */
public class GetContentByIdMethod {

	public static String getContent(String username, String password, BigInteger contentId, SerializationFormat serializationFormat) throws UnsupportedSerializationFormatException, SerializationException {

		DataServiceImpl impl = new DataServiceImpl();

		Authentication authentication = new Authentication();
		authentication.setRequesterId(username);
		authentication.setRequesterPassword(password);

		ResponseMessageBuilder responseBuilder = new ResponseMessageBuilder();

		Serializer serializer = SerializerFactory.getSerializer(serializationFormat, Serializer.APOLLO_NAMESPACE, Serializer.APOLLO_NAMESPACE_TNS_PREFIX);

		try {
			String content = impl.getContentForContentId(contentId, authentication);
			responseBuilder.setStatus(HttpStatus.OK, ResponseMessageBuilder.DEFAULT_SUCCESS_MESSAGE)
					.addContentToBody(content).setIsBodySerialized(false);

		} catch (DataServiceException ex) {
			responseBuilder.setStatus(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

		return serializer.serializeObject(responseBuilder.getResponse());

	}

}
