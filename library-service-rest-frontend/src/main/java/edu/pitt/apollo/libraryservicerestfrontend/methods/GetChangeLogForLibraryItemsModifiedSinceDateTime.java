package edu.pitt.apollo.libraryservicerestfrontend.methods;

import edu.pitt.apollo.exception.SerializationException;
import edu.pitt.apollo.exception.UnsupportedSerializationFormatException;
import edu.pitt.apollo.library_service_types.v3_0_2.GetChangeLogForLibraryItemsModifiedSinceDateTimeMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetChangeLogForLibraryItemsModifiedSinceDateTimeResult;
import edu.pitt.apollo.services_common.v3_0_2.SerializationFormat;
import org.springframework.http.HttpStatus;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jdl50 on 8/13/15.
 */
public class GetChangeLogForLibraryItemsModifiedSinceDateTime extends BaseLibraryServiceAccessorMethod {

    public GetChangeLogForLibraryItemsModifiedSinceDateTime(String username, String password, SerializationFormat serializationFormat) throws UnsupportedSerializationFormatException {
        super(username, password, serializationFormat, GetChangeLogForLibraryItemsModifiedSinceDateTimeResult.class);
    }

    public String getChangeLogForLibraryItemsModifiedSinceDateTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateTime);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            GetChangeLogForLibraryItemsModifiedSinceDateTimeMessage getChangeLogForLibraryItemsModifiedSinceDateTimeMessage = new GetChangeLogForLibraryItemsModifiedSinceDateTimeMessage();
            getChangeLogForLibraryItemsModifiedSinceDateTimeMessage.setAuthentication(authentication);
            getChangeLogForLibraryItemsModifiedSinceDateTimeMessage.setDateTime(xmlGregorianCalendar);
            return getResponseAsString(impl.getChangeLogForLibraryItemsModifiedSinceDateTime(getChangeLogForLibraryItemsModifiedSinceDateTimeMessage));
        } catch (ParseException | DatatypeConfigurationException e) {
            responseBuilder.setStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getClass().getName() + ": " + e.getMessage());
        }
        try {
            return serializer.serializeObject(responseBuilder.getResponse());
        } catch (SerializationException e) {
            return "Error: " + e.getClass().getName() + ": " + e.getMessage();
        }
    }
}
