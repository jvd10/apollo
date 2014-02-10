package edu.pitt.apollo.service.apolloservice.v2_0;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.8
 * 2014-02-07T13:39:19.852-05:00
 * Generated source version: 2.7.8
 * 
 */
@WebService(targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", name = "ApolloServiceEI")
@XmlSeeAlso({ObjectFactory.class, edu.pitt.apollo.types.v2_0.ObjectFactory.class})
public interface ApolloServiceEI {

    @WebResult(name = "methodCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "unRegisterService", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.UnRegisterService")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/unRegisterService")
    @ResponseWrapper(localName = "unRegisterServiceResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.UnRegisterServiceResponse")
    public edu.pitt.apollo.types.v2_0.MethodCallStatus unRegisterService(
        @WebParam(name = "serviceRegistrationRecord", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.ServiceRegistrationRecord serviceRegistrationRecord
    );

    @WebResult(name = "syntheticPopulationGenerationResult", targetNamespace = "")
    @RequestWrapper(localName = "runSyntheticPopulationGeneration", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunSyntheticPopulationGeneration")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/runSyntheticPopulationGeneration")
    @ResponseWrapper(localName = "runSyntheticPopulationGenerationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunSyntheticPopulationGenerationResponse")
    public edu.pitt.apollo.types.v2_0.SyntheticPopulationGenerationResult runSyntheticPopulationGeneration(
        @WebParam(name = "runSyntheticPopulationGenerationMessage", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.RunSyntheticPopulationGenerationMessage runSyntheticPopulationGenerationMessage
    );

    @WebResult(name = "runSimulationsResult", targetNamespace = "")
    @RequestWrapper(localName = "runSimulations", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunSimulations")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/runSimulations")
    @ResponseWrapper(localName = "runSimulationsResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunSimulationsResponse")
    public edu.pitt.apollo.types.v2_0.RunSimulationsResult runSimulations(
        @WebParam(name = "runSimulationsMessage", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.RunSimulationsMessage runSimulationsMessage
    );

    @WebResult(name = "methodCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "registerService", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RegisterService")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/registerService")
    @ResponseWrapper(localName = "registerServiceResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RegisterServiceResponse")
    public edu.pitt.apollo.types.v2_0.MethodCallStatus registerService(
        @WebParam(name = "serviceRegistrationRecord", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.ServiceRegistrationRecord serviceRegistrationRecord
    );

    @WebResult(name = "getLibraryItemsResult", targetNamespace = "")
    @RequestWrapper(localName = "getUuidsForLibraryItemsGivenType", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetUuidsForLibraryItemsGivenType")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getUuidsForLibraryItemsGivenType")
    @ResponseWrapper(localName = "getUuidsForLibraryItemsGivenTypeResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetUuidsForLibraryItemsGivenTypeResponse")
    public edu.pitt.apollo.types.v2_0.GetLibraryItemUuidsResult getUuidsForLibraryItemsGivenType(
        @WebParam(name = "type", targetNamespace = "")
        java.lang.String type
    );

    @WebResult(name = "getLocationsSupportedBySimulatorResult", targetNamespace = "")
    @RequestWrapper(localName = "getScenarioLocationCodesSupportedBySimulator", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetScenarioLocationCodesSupportedBySimulator")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getScenarioLocationCodesSupportedBySimulator")
    @ResponseWrapper(localName = "getScenarioLocationCodesSupportedBySimulatorResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetScenarioLocationCodesSupportedBySimulatorResponse")
    public edu.pitt.apollo.types.v2_0.GetScenarioLocationCodesSupportedBySimulatorResult getScenarioLocationCodesSupportedBySimulator(
        @WebParam(name = "simulatorIdentification", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.SoftwareIdentification simulatorIdentification
    );

    @WebResult(name = "addLibraryItemResult", targetNamespace = "")
    @RequestWrapper(localName = "addLibraryItem", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.AddLibraryItem")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/addLibraryItem")
    @ResponseWrapper(localName = "addLibraryItemResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.AddLibraryItemResponse")
    public edu.pitt.apollo.types.v2_0.AddLibraryItemResult addLibraryItem(
        @WebParam(name = "authentication", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.Authentication authentication,
        @WebParam(name = "apolloIndexableItem", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.ApolloIndexableItem apolloIndexableItem,
        @WebParam(name = "itemDescription", targetNamespace = "")
        java.lang.String itemDescription,
        @WebParam(name = "itemSource", targetNamespace = "")
        java.lang.String itemSource,
        @WebParam(name = "itemType", targetNamespace = "")
        java.lang.String itemType,
        @WebParam(name = "itemIndexingLabels", targetNamespace = "")
        java.util.List<java.lang.String> itemIndexingLabels
    );

    @WebResult(name = "runId", targetNamespace = "")
    @RequestWrapper(localName = "runSimulation", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunSimulation")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/runSimulation")
    @ResponseWrapper(localName = "runSimulationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunSimulationResponse")
    public java.lang.String runSimulation(
        @WebParam(name = "runSimulationMessage", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.RunSimulationMessage runSimulationMessage
    );

    @WebResult(name = "getConfigurationFileForSimulationResult", targetNamespace = "")
    @RequestWrapper(localName = "getConfigurationFileForSimulation", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetConfigurationFileForSimulation")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getConfigurationFileForSimulation")
    @ResponseWrapper(localName = "getConfigurationFileForSimulationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetConfigurationFileForSimulationResponse")
    public edu.pitt.apollo.types.v2_0.GetConfigurationFileForSimulationResult getConfigurationFileForSimulation(
        @WebParam(name = "runAndSoftwareIdentification", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.RunAndSoftwareIdentification runAndSoftwareIdentification
    );

    @WebResult(name = "runStatus", targetNamespace = "")
    @RequestWrapper(localName = "getRunStatus", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetRunStatus")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getRunStatus")
    @ResponseWrapper(localName = "getRunStatusResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetRunStatusResponse")
    public edu.pitt.apollo.types.v2_0.MethodCallStatus getRunStatus(
        @WebParam(name = "runAndSoftwareIdentification", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.RunAndSoftwareIdentification runAndSoftwareIdentification
    );

    @WebResult(name = "getLibraryItemResult", targetNamespace = "")
    @RequestWrapper(localName = "getLibraryItem", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetLibraryItem")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getLibraryItem")
    @ResponseWrapper(localName = "getLibraryItemResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetLibraryItemResponse")
    public edu.pitt.apollo.types.v2_0.GetLibraryItemResult getLibraryItem(
        @WebParam(name = "uuid", targetNamespace = "")
        java.lang.String uuid
    );

    @WebResult(name = "serviceRecords", targetNamespace = "")
    @RequestWrapper(localName = "getRegisteredServices", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetRegisteredServices")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getRegisteredServices")
    @ResponseWrapper(localName = "getRegisteredServicesResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetRegisteredServicesResponse")
    public java.util.List<edu.pitt.apollo.types.v2_0.ServiceRecord> getRegisteredServices();

    @WebResult(name = "getPopulationAndEnvironmentCensusResult", targetNamespace = "")
    @RequestWrapper(localName = "getPopulationAndEnvironmentCensus", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetPopulationAndEnvironmentCensus")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getPopulationAndEnvironmentCensus")
    @ResponseWrapper(localName = "getPopulationAndEnvironmentCensusResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetPopulationAndEnvironmentCensusResponse")
    public edu.pitt.apollo.types.v2_0.GetPopulationAndEnvironmentCensusResult getPopulationAndEnvironmentCensus(
        @WebParam(name = "simulatorIdentification", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.SoftwareIdentification simulatorIdentification,
        @WebParam(name = "location", targetNamespace = "")
        java.lang.String location
    );

    @WebResult(name = "visualizationResult", targetNamespace = "")
    @RequestWrapper(localName = "runVisualization", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunVisualization")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/runVisualization")
    @ResponseWrapper(localName = "runVisualizationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RunVisualizationResponse")
    public edu.pitt.apollo.types.v2_0.VisualizerResult runVisualization(
        @WebParam(name = "runVisualizationMessage", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.RunVisualizationMessage runVisualizationMessage
    );

    @WebResult(name = "methodCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "removeLibraryItem", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RemoveLibraryItem")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/removeLibraryItem")
    @ResponseWrapper(localName = "removeLibraryItemResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.RemoveLibraryItemResponse")
    public edu.pitt.apollo.types.v2_0.MethodCallStatus removeLibraryItem(
        @WebParam(name = "authentication", targetNamespace = "")
        edu.pitt.apollo.types.v2_0.Authentication authentication,
        @WebParam(name = "uuid", targetNamespace = "")
        java.lang.String uuid
    );

    @WebResult(name = "getLibraryItemsResult", targetNamespace = "")
    @RequestWrapper(localName = "getUuidsForLibraryItemsCreatedSinceDateTime", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetUuidsForLibraryItemsCreatedSinceDateTime")
    @WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0/getUuidsForLibraryItemsCreatedSinceDateTime")
    @ResponseWrapper(localName = "getUuidsForLibraryItemsCreatedSinceDateTimeResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0/", className = "edu.pitt.apollo.service.apolloservice.v2_0.GetUuidsForLibraryItemsCreatedSinceDateTimeResponse")
    public edu.pitt.apollo.types.v2_0.GetLibraryItemUuidsResult getUuidsForLibraryItemsCreatedSinceDateTime(
        @WebParam(name = "creationDateTime", targetNamespace = "")
        javax.xml.datatype.XMLGregorianCalendar creationDateTime
    );
}
