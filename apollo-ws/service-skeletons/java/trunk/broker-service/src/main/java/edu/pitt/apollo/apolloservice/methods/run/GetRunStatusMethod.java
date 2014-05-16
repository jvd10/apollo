package edu.pitt.apollo.apolloservice.methods.run;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import edu.pitt.apollo.apolloservice.database.ApolloDbUtilsContainer;
import edu.pitt.apollo.apolloservice.error.ApolloServiceErrorHandler;
import edu.pitt.apollo.db.ApolloDatabaseException;
import edu.pitt.apollo.db.ApolloDatabaseKeyNotFoundException;
import edu.pitt.apollo.db.ApolloDbUtils;
import edu.pitt.apollo.service.simulatorservice.v2_0_1.SimulatorServiceEI;
import edu.pitt.apollo.service.simulatorservice.v2_0_1.SimulatorServiceV201;
import edu.pitt.apollo.service.translatorservice.v2_0_1.TranslatorServiceEI;
import edu.pitt.apollo.service.translatorservice.v2_0_1.TranslatorServiceV201;
import edu.pitt.apollo.service.visualizerservice.v2_0_1.VisualizerServiceEI;
import edu.pitt.apollo.service.visualizerservice.v2_0_1.VisualizerServiceV201;
import edu.pitt.apollo.types.v2_0_1.ApolloSoftwareTypeEnum;
import edu.pitt.apollo.types.v2_0_1.MethodCallStatus;
import edu.pitt.apollo.types.v2_0_1.MethodCallStatusEnum;
import edu.pitt.apollo.types.v2_0_1.SoftwareIdentification;

/**
 *
 * Author: Nick Millett
 * Email: nick.millett@gmail.com
 * Date: May 9, 2014
 * Time: 1:29:19 PM
 * Class: GetRunStatusMethod
 * IDE: NetBeans 6.9.1
 */
public class GetRunStatusMethod {

    private static MethodCallStatus getErrorMethodCallStatus(String message) {
        MethodCallStatus status = new MethodCallStatus();
        status.setStatus(MethodCallStatusEnum.FAILED);
        status.setMessage(message);
        return status;
    }

    public static MethodCallStatus getRunStatus(String runIdentification) {

        ApolloDbUtils dbUtils = ApolloDbUtilsContainer.getApolloDbUtils();
        // first check the apollo errors file
        long runIdAsLong = Long.parseLong(runIdentification);
        if (runIdAsLong == -1) {
            return getErrorMethodCallStatus("Unable to write error file on server (disk full?).");
        }

        {
            // long runIdAsLong =
            // Long.parseLong(runAndSoftwareIdentification.getRunId());
            if (ApolloServiceErrorHandler.checkErrorFileExists(runIdAsLong)) {

                MethodCallStatus status = new MethodCallStatus();
                status.setStatus(MethodCallStatusEnum.FAILED);
                status.setMessage(ApolloServiceErrorHandler.readErrorFromErrorFile(runIdAsLong));
                return status;
            }
        }

        int runId = Integer.parseInt(runIdentification);
        // get the last called software

        SoftwareIdentification softwareId;
        try {
            softwareId = dbUtils.getLastServiceToBeCalledForRun(runId);
        } catch (ApolloDatabaseException ex) {
            String message = ex.getMessage();
            return getErrorMethodCallStatus(message);
        }

        URL url;
        try {
            url = new URL(dbUtils.getUrlForSoftwareIdentification(softwareId));
        } catch (SQLException ex) {
            String message = "SQLException attempting to get URL for software identification for runId "
                    + runId + ": " + ex.getMessage();
            return getErrorMethodCallStatus(message);
        } catch (ApolloDatabaseKeyNotFoundException ex) {
            String message = "Apollo database key not found attempting to get URL for software identification for runId "
                    + runId + ": " + ex.getMessage();
            return getErrorMethodCallStatus(message);
        } catch (ClassNotFoundException ex) {
            String message = "ClassNotFoundException attempting to get URL for software identification for runId "
                    + runId + ": " + ex.getMessage();
            return getErrorMethodCallStatus(message);
        } catch (MalformedURLException ex) {
            String message = "MalformedURLException attempting to get URL for software identification for runId "
                    + runId + ": " + ex.getMessage();
            return getErrorMethodCallStatus(message);
        }

        MethodCallStatus status;
        // get the webservice WSDL URL for supplied
        if (softwareId.getSoftwareType() == ApolloSoftwareTypeEnum.SIMULATOR) {
            SimulatorServiceEI port = new SimulatorServiceV201(url).getSimulatorServiceEndpoint();
            status = port.getRunStatus(runIdentification);
            if (status.getStatus().equals(MethodCallStatusEnum.UNKNOWN_RUNID)) {
                status.setStatus(MethodCallStatusEnum.CALLED_SIMULATOR);
                status.setMessage("The run was submitted to the simulator.");
            }
        } else if (softwareId.getSoftwareType() == ApolloSoftwareTypeEnum.VISUALIZER) {

            VisualizerServiceEI port = new VisualizerServiceV201(url).getVisualizerServiceEndpoint();
            status = port.getRunStatus(runIdentification);
            if (status.getStatus().equals(MethodCallStatusEnum.UNKNOWN_RUNID)) {
                status.setStatus(MethodCallStatusEnum.CALLED_VISUALIZER);
                status.setMessage("The run was submitted to the visualizer.");
            }
        } else if (softwareId.getSoftwareType() == ApolloSoftwareTypeEnum.TRANSLATOR) {
            TranslatorServiceEI port = new TranslatorServiceV201(url).getTranslatorServiceEndpoint();
            status = port.getRunStatus(runIdentification);

            if (status.getStatus().equals(MethodCallStatusEnum.UNKNOWN_RUNID)) {
                status.setStatus(MethodCallStatusEnum.CALLED_TRANSLATOR);
                status.setMessage("The run was submitted to the translator.");
            }
        } else {
            return getErrorMethodCallStatus("Unrecognized software type");
        }

        return status;
    }
}
