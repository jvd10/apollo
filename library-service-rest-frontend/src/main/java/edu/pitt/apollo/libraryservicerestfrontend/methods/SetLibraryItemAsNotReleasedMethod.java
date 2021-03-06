package edu.pitt.apollo.libraryservicerestfrontend.methods;

import edu.pitt.apollo.exception.UnsupportedSerializationFormatException;
import edu.pitt.apollo.library_service_types.v3_0_2.SetLibraryItemAsNotReleasedMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.SetLibraryItemAsNotReleasedResult;
import edu.pitt.apollo.services_common.v3_0_2.SerializationFormat;

/**
 * Created by jdl50 on 8/12/15.
 */
public class SetLibraryItemAsNotReleasedMethod extends BaseLibraryServiceAccessorMethod {
    public SetLibraryItemAsNotReleasedMethod(String username, String password, SerializationFormat serializationFormat) throws UnsupportedSerializationFormatException {
        super(username, password, serializationFormat, SetLibraryItemAsNotReleasedResult.class);
    }

    public String hideLibraryItem(int urn) {
        SetLibraryItemAsNotReleasedMessage setLibraryItemAsNotReleasedMessage = new SetLibraryItemAsNotReleasedMessage();
        setLibraryItemAsNotReleasedMessage.setAuthentication(authentication);
        setLibraryItemAsNotReleasedMessage.setUrn(urn);
        return getResponseAsString(impl.setLibraryItemAsNotReleased(setLibraryItemAsNotReleasedMessage));
    }
}
