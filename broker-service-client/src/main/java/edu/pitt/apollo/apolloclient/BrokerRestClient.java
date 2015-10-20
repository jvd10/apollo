package edu.pitt.apollo.apolloclient;

import edu.pitt.apollo.apolloclient.tutorial.ApolloServiceTypeFactory;
import edu.pitt.apollo.exception.DataServiceException;
import edu.pitt.apollo.restbrokerserviceconnector.RestBrokerServiceConnector;
import edu.pitt.apollo.services_common.v3_1_0.Authentication;
import edu.pitt.apollo.services_common.v3_1_0.InsertRunResult;
import edu.pitt.apollo.simulator_service_types.v3_1_0.RunSimulationMessage;

/**
 * Created by nem41 on 8/17/15.
 */
public class BrokerRestClient {

    public static void main(String[] args) {


        RestBrokerServiceConnector connector = new RestBrokerServiceConnector("http://betaweb.rods.pitt.edu/broker-service-rest-frontend-3.1.0-SNAPSHOT/");

        Authentication authentication = new Authentication();
        authentication.setRequesterId("apollo_demo");
        authentication.setRequesterPassword("apollo_demo");

        try {
            RunSimulationMessage runSimulationMessage = ApolloServiceTypeFactory
                    .getMinimalistRunSimulationMessage(ApolloServiceTypeFactory.SimulatorIdentificationEnum.SEIR);

            runSimulationMessage.getInfectiousDiseaseScenario().getInfections().get(0)
                    .getInfectionAcquisitionsFromInfectedHosts().get(0)
                    .getBasicReproductionNumbers().get(0).setExactValue(1.76);

            InsertRunResult result = connector.insertRun(runSimulationMessage);

            System.out.println(result.getRunId());

        } catch (DataServiceException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

}
