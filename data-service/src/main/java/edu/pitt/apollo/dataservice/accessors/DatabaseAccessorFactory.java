package edu.pitt.apollo.dataservice.accessors;


import edu.pitt.apollo.apollo_service_types.v3_1_0.RunSimulationsMessage;
import edu.pitt.apollo.data_service_types.v3_1_0.DataRetrievalRequestMessage;
import edu.pitt.apollo.db.exceptions.ApolloDatabaseException;
import edu.pitt.apollo.exception.UnrecognizedMessageTypeException;
import edu.pitt.apollo.services_common.v3_1_0.Authentication;
import edu.pitt.apollo.services_common.v3_1_0.ContentDataTypeEnum;
import edu.pitt.apollo.services_common.v3_1_0.RunMessage;
import edu.pitt.apollo.simulator_service_types.v3_1_0.RunSimulationMessage;
import edu.pitt.apollo.visualizer_service_types.v3_1_0.RunVisualizationMessage;

public class DatabaseAccessorFactory {

    public static DatabaseAccessor getDatabaseAccessor(Authentication authentication) throws UnrecognizedMessageTypeException, ApolloDatabaseException {
        return new DatabaseAccessor(authentication);
    }

    public static DatabaseAccessor getDatabaseAccessor(RunMessage message) throws UnrecognizedMessageTypeException, ApolloDatabaseException {
        if (message instanceof RunSimulationMessage | message instanceof RunVisualizationMessage
                | message instanceof RunSimulationsMessage) {
            return new DatabaseAccessorForRunningJobs(
                    message.getAuthentication(), message.getSoftwareIdentification());
        } else if (message instanceof DataRetrievalRequestMessage) {
            Authentication authentication = message.getAuthentication();
            return new DatabaseAccessorForRunningDataService((DataRetrievalRequestMessage) message, authentication);
        } else {
            throw new UnrecognizedMessageTypeException(
                    "Unrecognized message type in DatabaseAccessorFactory");
        }

	}

}