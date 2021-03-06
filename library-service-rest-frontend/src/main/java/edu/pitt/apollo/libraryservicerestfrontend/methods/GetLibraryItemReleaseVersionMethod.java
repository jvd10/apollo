package edu.pitt.apollo.libraryservicerestfrontend.methods;

import edu.pitt.apollo.exception.SerializationException;
import edu.pitt.apollo.exception.UnsupportedSerializationFormatException;
import edu.pitt.apollo.library_service_types.v3_0_2.GetReleaseVersionMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetReleaseVersionResult;
import edu.pitt.apollo.service.libraryservice.v3_0_2.GetLibraryItemReleaseVersion;
import edu.pitt.apollo.services_common.v3_0_2.ObjectSerializationInformation;
import edu.pitt.apollo.services_common.v3_0_2.SerializationFormat;
import edu.pitt.apollo.utilities.Serializer;
import edu.pitt.apollo.utils.ResponseMessageBuilder;
import org.springframework.http.HttpStatus;

/**
 * Created by jdl50 on 8/12/15.
 */
public class GetLibraryItemReleaseVersionMethod extends BaseLibraryServiceAccessorMethod {
    public GetLibraryItemReleaseVersionMethod(String username, String password, SerializationFormat serializationFormat) throws UnsupportedSerializationFormatException {
        super(username, password, serializationFormat, GetReleaseVersionResult.class);
    }

    public String getLibraryItemReleaseVersion(int urn) {

        GetReleaseVersionMessage getReleaseVersionMessage = new GetReleaseVersionMessage();
        getReleaseVersionMessage.setAuthentication(authentication);
        getReleaseVersionMessage.setUrn(urn);

        Object result = impl.getLibraryItemReleaseVersion(getReleaseVersionMessage);
        return getResponseAsString(result);

    }
}
