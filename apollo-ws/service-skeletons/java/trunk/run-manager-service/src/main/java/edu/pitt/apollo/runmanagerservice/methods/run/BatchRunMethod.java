package edu.pitt.apollo.runmanagerservice.methods.run;

import edu.pitt.apollo.JsonUtils;
import edu.pitt.apollo.JsonUtilsException;
import edu.pitt.apollo.apollo_service_types.v3_0_0.RunSimulationsMessage;
import edu.pitt.apollo.exception.DataServiceException;
import edu.pitt.apollo.runmanagerservice.types.RunResultAndSimulationGroupId;
import edu.pitt.apollo.services_common.v3_0_0.Authentication;
import edu.pitt.apollo.services_common.v3_0_0.MethodCallStatus;
import edu.pitt.apollo.services_common.v3_0_0.MethodCallStatusEnum;
import edu.pitt.apollo.services_common.v3_0_0.SoftwareIdentification;
import edu.pitt.apollo.simulator_service_types.v3_0_0.RunSimulationMessage;

import java.math.BigInteger;

/**
 * Created by jdl50 on 5/6/15.
 */
public class BatchRunMethod extends RunMethodForSimulation {


    public BatchRunMethod(BigInteger stagedRunId, Authentication authentication) throws JsonUtilsException, DataServiceException {
        super(stagedRunId, authentication);
    }

    @Override
    protected Object convertRunMessageJson(String jsonForRunMessage) throws JsonUtilsException {
        JsonUtils jsonUtils = new JsonUtils();
        return (RunSimulationMessage) jsonUtils.getObjectFromJson(jsonForRunMessage, RunSimulationsMessage.class);
    }

}