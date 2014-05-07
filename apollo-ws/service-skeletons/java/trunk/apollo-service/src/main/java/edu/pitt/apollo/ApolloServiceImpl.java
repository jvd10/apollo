/* Copyright 2012 University of Pittsburgh
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.pitt.apollo;

import edu.pitt.apollo.db.ApolloDatabaseException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import edu.pitt.apollo.db.ApolloDatabaseKeyNotFoundException;
import edu.pitt.apollo.db.ApolloDatabaseRecordNotInsertedException;
import edu.pitt.apollo.db.ApolloDbUtils;
import edu.pitt.apollo.service.apolloservice.v2_0_1.ApolloServiceEI;
import edu.pitt.apollo.service.libraryservice.v2_0_1.LibraryServiceEI;
import edu.pitt.apollo.service.libraryservice.v2_0_1.LibraryServiceV201;
import edu.pitt.apollo.service.simulatorservice.v2_0_1.SimulatorServiceEI;
import edu.pitt.apollo.service.simulatorservice.v2_0_1.SimulatorServiceV201;
import edu.pitt.apollo.service.translatorservice.v2_0_1.TranslatorServiceEI;
import edu.pitt.apollo.service.translatorservice.v2_0_1.TranslatorServiceV201;
import edu.pitt.apollo.service.visualizerservice.v2_0_1.VisualizerServiceEI;
import edu.pitt.apollo.service.visualizerservice.v2_0_1.VisualizerServiceV201;
import edu.pitt.apollo.types.v2_0_1.AddLibraryItemResult;
import edu.pitt.apollo.types.v2_0_1.ApolloIndexableItem;
import edu.pitt.apollo.types.v2_0_1.ApolloSoftwareTypeEnum;
import edu.pitt.apollo.types.v2_0_1.Authentication;
import edu.pitt.apollo.types.v2_0_1.GetConfigurationFileForSimulationResult;
import edu.pitt.apollo.types.v2_0_1.GetLibraryItemResult;
import edu.pitt.apollo.types.v2_0_1.GetLibraryItemUuidsResult;
import edu.pitt.apollo.types.v2_0_1.GetPopulationAndEnvironmentCensusResult;
import edu.pitt.apollo.types.v2_0_1.GetScenarioLocationCodesSupportedBySimulatorResult;
import edu.pitt.apollo.types.v2_0_1.GetVisualizerOutputResourcesResult;
import edu.pitt.apollo.types.v2_0_1.Location;
import edu.pitt.apollo.types.v2_0_1.MethodCallStatus;
import edu.pitt.apollo.types.v2_0_1.MethodCallStatusEnum;
import edu.pitt.apollo.types.v2_0_1.PopulationAndEnvironmentCensus;
import edu.pitt.apollo.types.v2_0_1.RunAndSoftwareIdentification;
import edu.pitt.apollo.types.v2_0_1.RunSimulationMessage;
import edu.pitt.apollo.types.v2_0_1.RunSimulationsMessage;
import edu.pitt.apollo.types.v2_0_1.RunSimulationsResult;
import edu.pitt.apollo.types.v2_0_1.RunSyntheticPopulationGenerationMessage;
import edu.pitt.apollo.types.v2_0_1.RunVisualizationMessage;
import edu.pitt.apollo.types.v2_0_1.RunVisualizationResult;
import edu.pitt.apollo.types.v2_0_1.ServiceRecord;
import edu.pitt.apollo.types.v2_0_1.ServiceRegistrationRecord;
import edu.pitt.apollo.types.v2_0_1.SoftwareIdentification;
import edu.pitt.apollo.types.v2_0_1.SyntheticPopulationGenerationResult;
import edu.pitt.apollo.types.v2_0_1.UrlOutputResource;

@WebService(targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", portName = "ApolloServiceEndpoint", serviceName = "ApolloService_v2.0.1", endpointInterface = "edu.pitt.apollo.service.apolloservice.v2_0_1.ApolloServiceEI")
class ApolloServiceImpl implements ApolloServiceEI {

	private static final int END_USER_APPLICATION_SOURCE_ID = 0;
	private static final String FATAL_ERROR_CODE = "-1";
	private static final String DATABASE_PROPERTIES_FILENAME = "database.properties";
	private static final String REGISTRY_FILENAME = "registered_services.xml";
	private static final String ERROR_FILENAME = "run_errors.txt";
	private static final String RUN_ERROR_PREFIX = "ApolloServiceError";
	private static final String ERROR_FILE_DIR = "errors";
	public static final String APOLLO_WORKDIR_ENVIRONMENT_VARIABLE = "APOLLO_201_WORK_DIR";
	// private static final String DB4O_FILENAME = "db4o_db_20";
	private static final String CENSUS_DATA_FILENAME = "fred_census_data.csv";
	private static String APOLLO_DIR = "";
	private static ApolloDbUtils dbUtils = null;
	private static ServiceRegistrationRecord translatorServiceRecord;
	// private static ObjectContainer db4o;
	private static List<PopulationAndEnvironmentCensus> censusData;

	static String getRegistryFilename() {
		return APOLLO_DIR + REGISTRY_FILENAME;
	}

	static String getErrorFilename() {
		return APOLLO_DIR + ERROR_FILENAME;
	}

	static String getRunErrorPrefix() {
		return RUN_ERROR_PREFIX;
	}

	public static String getDatabasePropertiesFilename() {
		return APOLLO_DIR + DATABASE_PROPERTIES_FILENAME;
	}

	public File getErrorFile(long runId) {
		return new File(APOLLO_DIR + ERROR_FILE_DIR + File.separator + "error-"
				+ Long.valueOf(runId) + ".txt");
	}

	public long getErrorRunId() {
		return -System.currentTimeMillis();
	}

	public static ServiceRegistrationRecord getTranslatorServiceRegistrationRecord() {
		return translatorServiceRecord;
	}

	private LibraryServiceEI getLibraryServicePort() {
		URL libraryServiceURL;
		try {
			libraryServiceURL = new URL(
					"http://localhost:8080/libraryservice2.0.1/services/libraryservice?wsdl");

			return new LibraryServiceV201(libraryServiceURL)
					.getLibraryServiceEndpoint();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static MethodCallStatus getErrorMethodCallStatus(String message) {
		MethodCallStatus status = new MethodCallStatus();
		status.setStatus(MethodCallStatusEnum.FAILED);
		status.setMessage(message);
		return status;
	}

	protected VisualizerServiceEI getVisualizerServicePort(URL url) {
		return new VisualizerServiceV201(url).getVisualizerServiceEndpoint();
	}

	protected SimulatorServiceEI getSimulatorServicePort(URL url) {
		return new SimulatorServiceV201(url).getSimulatorServiceEndpoint();
	}

	protected TranslatorServiceEI getTranslatorServicePort(URL url) {
		return new TranslatorServiceV201(url).getTranslatorServiceEndpoint();
	}

	@Override
	@WebResult(name = "methodCallStatus", targetNamespace = "")
	@RequestWrapper(localName = "unRegisterService", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.UnRegisterService")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/unRegisterService")
	@ResponseWrapper(localName = "unRegisterServiceResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.UnRegisterServiceResponse")
	public MethodCallStatus unRegisterService(
			@WebParam(name = "serviceRegistrationRecord", targetNamespace = "") ServiceRegistrationRecord serviceRegistrationRecord) {
		MethodCallStatus result = new MethodCallStatus();

		List<ServiceRegistrationRecord> records;
		try {
			// get the entire list of current service registration records
			records = RegistrationUtils.getServiceRegistrationRecords();

			for (ServiceRegistrationRecord record : records) {
				// for each record currently in the registry, see if we can find
				// a record with a ServiceIdentification that is equal to one
				// that the user is trying to unregister
				if (RegistrationUtils.softwareIdentificationEqual(
						record.getSoftwareIdentification(),
						serviceRegistrationRecord.getSoftwareIdentification())) {
					// found the service the user wants to unregister, now check
					// that the username and password supplied with this request
					// match the username and password sent with the
					// registration request
					if (RegistrationUtils.authenticationEqual(
							record.getAuthentication(),
							serviceRegistrationRecord.getAuthentication())) {
						try {
							// the username/password match, so remove the record
							// from the registry
							RegistrationUtils
									.removeServiceRegistrationRecord(serviceRegistrationRecord);
						} catch (IOException e) {
							// there was en error removing the record, report
							// this error to the caller
							result.setMessage("Error Unregistering Service: "
									+ e.getMessage());
							result.setStatus(MethodCallStatusEnum.FAILED);
							return result;
						}
						// removal succeeded
						result.setMessage("unregistration Successful!");
						result.setStatus(MethodCallStatusEnum.COMPLETED);
						return result;
					} else {
						// username/passwords do not match
						result.setMessage("Error Unregistering Service: Username/Password does not match orignial ServiceRegistrationRecord!");
						result.setStatus(MethodCallStatusEnum.FAILED);
						return result;
					}
				}
			}
			// couldn't find matching ServiceRecords
			result.setMessage("Error Unregistering Service: Service not registered at this registry.");
			result.setStatus(MethodCallStatusEnum.FAILED);
			return result;
		} catch (IOException e) {
			result.setMessage("Error Unregistering Service: Error reading registry!");
			result.setStatus(MethodCallStatusEnum.FAILED);
			return result;
		}

	}

	@Override
	@WebResult(name = "syntheticPopulationGenerationResult", targetNamespace = "")
	@RequestWrapper(localName = "runSyntheticPopulationGeneration", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunSyntheticPopulationGeneration")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/runSyntheticPopulationGeneration")
	@ResponseWrapper(localName = "runSyntheticPopulationGenerationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunSyntheticPopulationGenerationResponse")
	public SyntheticPopulationGenerationResult runSyntheticPopulationGeneration(
			@WebParam(name = "runSyntheticPopulationGenerationMessage", targetNamespace = "") RunSyntheticPopulationGenerationMessage runSyntheticPopulationGenerationMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebResult(name = "runSimulationsResult", targetNamespace = "")
	@RequestWrapper(localName = "runSimulations", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunSimulations")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/runSimulations")
	@ResponseWrapper(localName = "runSimulationsResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunSimulationsResponse")
	public RunSimulationsResult runSimulations(
			@WebParam(name = "runSimulationsMessage", targetNamespace = "") RunSimulationsMessage runSimulationsMessage) {
		RunSimulationsResult bsr = new RunSimulationsResult();

		String runId;
		try {
			URL url = null;
			try {
				// get the webservice WSDL URL for supplied
				// SimulatorIdentification
				url = RegistrationUtils
						.getUrlForSoftwareIdentification(runSimulationsMessage
								.getSoftwareIdentification());
				if (url == null) {
					runId = RunUtils.getErrorRunId();
					RunUtils.reportError(runId, "Service not registered.");
					bsr.setRunId(runId);
					return bsr;
				}
			} catch (IOException e) {
				runId = RunUtils.getErrorRunId();
				RunUtils.reportError(runId, "Error reading registry.");
				bsr.setRunId(runId);
				return bsr;
			}
			// run the simulator

			SimulatorServiceEI port = getSimulatorServicePort(url);

			// disable chunking for ZSI
			Client client = ClientProxy.getClient(port);
			HTTPConduit http = (HTTPConduit) client.getConduit();
			HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
			httpClientPolicy.setConnectionTimeout(36000);
			httpClientPolicy.setAllowChunking(false);
			http.setClient(httpClientPolicy);

			return port.runSimulations(runSimulationsMessage);
		} catch (Exception e) {
			runId = RunUtils.getErrorRunId();
			RunUtils.reportError(runId,
					"Problem with SimulatorService:" + e.getMessage());
			bsr.setRunId(runId);
			return bsr;
		}

	}

	@Override
	@WebResult(name = "methodCallStatus", targetNamespace = "")
	@RequestWrapper(localName = "registerService", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RegisterService")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/registerService")
	@ResponseWrapper(localName = "registerServiceResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RegisterServiceResponse")
	public MethodCallStatus registerService(
			@WebParam(name = "serviceRegistrationRecord", targetNamespace = "") ServiceRegistrationRecord serviceRegistrationRecord) {
		MethodCallStatus result = new MethodCallStatus();

		List<ServiceRegistrationRecord> records;
		try {

			records = RegistrationUtils.getServiceRegistrationRecords();

			for (ServiceRegistrationRecord record : records) {
				if (RegistrationUtils.softwareIdentificationEqual(
						record.getSoftwareIdentification(),
						serviceRegistrationRecord.getSoftwareIdentification())) {
					result.setMessage("Service is already registered.  Please unRegisterService to make changes to the existing ServiceRecord.");
					result.setStatus(MethodCallStatusEnum.FAILED);
					return result;
				}

				if (RegistrationUtils.serviceUrlEqual(record,
						serviceRegistrationRecord)) {
					result.setMessage("URL is already registered.");
					result.setStatus(MethodCallStatusEnum.FAILED);
					return result;
				}
			}

			// if we are here, it looks like a valid registration
			try {
				RegistrationUtils
						.addServiceRegistrationRecord(serviceRegistrationRecord);
				result.setMessage("Service Registration Successful!");
				result.setStatus(MethodCallStatusEnum.COMPLETED);
			} catch (IOException e) {
				result.setMessage("Error registering service: "
						+ e.getMessage());
				result.setStatus(MethodCallStatusEnum.FAILED);
			}

		} catch (IOException e) {
			result.setMessage("Error reading registry!");
			result.setStatus(MethodCallStatusEnum.FAILED);
		}
		return result;
	}

	@Override
	@WebResult(name = "getLibraryItemsResult", targetNamespace = "")
	@RequestWrapper(localName = "getUuidsForLibraryItemsGivenType", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetUuidsForLibraryItemsGivenType")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getUuidsForLibraryItemsGivenType")
	@ResponseWrapper(localName = "getUuidsForLibraryItemsGivenTypeResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetUuidsForLibraryItemsGivenTypeResponse")
	public GetLibraryItemUuidsResult getUuidsForLibraryItemsGivenType(
			@WebParam(name = "type", targetNamespace = "") String type) {
		return getLibraryServicePort().getUuidsForLibraryItemsGivenType(type);
	}

	@Override
	@WebResult(name = "addLibraryItemResult", targetNamespace = "")
	@RequestWrapper(localName = "addLibraryItem", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.AddLibraryItem")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/addLibraryItem")
	@ResponseWrapper(localName = "addLibraryItemResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.AddLibraryItemResponse")
	public AddLibraryItemResult addLibraryItem(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "apolloIndexableItem", targetNamespace = "") ApolloIndexableItem apolloIndexableItem,
			@WebParam(name = "itemDescription", targetNamespace = "") String itemDescription,
			@WebParam(name = "itemSource", targetNamespace = "") String itemSource,
			@WebParam(name = "itemType", targetNamespace = "") String itemType,
			@WebParam(name = "itemIndexingLabels", targetNamespace = "") List<String> itemIndexingLabels) {
		// TODO Auto-generated method stub
		return getLibraryServicePort().addLibraryItem(authentication,
				apolloIndexableItem, itemDescription, itemSource, itemType,
				itemIndexingLabels);
	}

	@Override
	@WebResult(name = "getConfigurationFileForSimulationResult", targetNamespace = "")
	@RequestWrapper(localName = "getConfigurationFileForSimulation", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetConfigurationFileForSimulation")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getConfigurationFileForSimulation")
	@ResponseWrapper(localName = "getConfigurationFileForSimulationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetConfigurationFileForSimulationResponse")
	public GetConfigurationFileForSimulationResult getConfigurationFileForSimulation(
			@WebParam(name = "runAndSoftwareIdentification", targetNamespace = "") RunAndSoftwareIdentification runAndSoftwareIdentification) {

		GetConfigurationFileForSimulationResult result = new GetConfigurationFileForSimulationResult();
		MethodCallStatus status = new MethodCallStatus();
		result.setMethodCallStatus(status);

		int runId = Integer.parseInt(runAndSoftwareIdentification.getRunId());
		Map<String, ByteArrayOutputStream> map;
		try {
			map = dbUtils
					.getDataContentForSoftware(
							runId,
							dbUtils.getSoftwareIdentificationKey(getTranslatorServiceRegistrationRecord()
									.getSoftwareIdentification()),
							dbUtils.getSoftwareIdentificationKey(runAndSoftwareIdentification
									.getSoftwareId()));
		} catch (ApolloDatabaseException ex) {
			status.setStatus(MethodCallStatusEnum.FAILED);
			status.setMessage(ex.getMessage());
			return result;
		}

		for (String label : map.keySet()) {
			ByteArrayOutputStream stream = map.get(label);
			if (label.equals("config.txt")) {
				String content = stream.toString();
				result.setConfigurationFile(content);
			} else if (label.equals("verbose.html")) {
				String content = stream.toString();
				result.setConfigurationFileInHtmlFormat(content);
			}
		}

		status.setStatus(MethodCallStatusEnum.COMPLETED);

		return result;

		// GetConfigurationFileForSimulationResult result = new
		// GetConfigurationFileForSimulationResult();
		// MethodCallStatus status = new MethodCallStatus();
		// result.setMethodCallStatus(status);
		//
		// String simName =
		// runAndSoftwareIdentification.getSoftwareId().getSoftwareName();
		// try {
		// ServiceRecord serviceRecord =
		// RegistrationUtils.getServiceRecordForSoftwareId(runAndSoftwareIdentification.getSoftwareId());
		//
		// URL url = new URL(serviceRecord.getUrl());
		//
		// // get the webservice WSDL URL for supplied
		// // SimulatorIdentification
		// if (serviceRecord.getSoftwareIdentification().getSoftwareType() ==
		// ApolloSoftwareTypeEnum.SIMULATOR) {
		//
		// SimulatorServiceEI port = getSimulatorServicePort(url);
		// return
		// port.getConfigurationFileForSimulation(runAndSoftwareIdentification.getRunId());
		// } else {
		// status.setMessage("getConfigurationFile() not implemented for this service type.");
		// status.setStatus(MethodCallStatusEnum.FAILED);
		// }
		// } catch (IOException e) {
		// status.setMessage("General error occurred on server.");
		// status.setStatus(MethodCallStatusEnum.FAILED);
		//
		// }
		// return result;
		// return null;

	}

	@Override
	@WebResult(name = "runStatus", targetNamespace = "")
	@RequestWrapper(localName = "getRunStatus", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetRunStatus")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getRunStatus")
	@ResponseWrapper(localName = "getRunStatusResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetRunStatusResponse")
	public MethodCallStatus getRunStatus(
			@WebParam(name = "runAndSoftwareIdentification", targetNamespace = "") RunAndSoftwareIdentification runAndSoftwareIdentification) {

		// first check the apollo errors file
		long runIdAsLong = Long.parseLong(runAndSoftwareIdentification
				.getRunId());
		if (runIdAsLong == -1) {
			return getErrorMethodCallStatus("Unable to write error file on server (disk full?).");
		}

		{
			// long runIdAsLong =
			// Long.parseLong(runAndSoftwareIdentification.getRunId());
			if (ErrorUtils.checkFileExists(getErrorFile(runIdAsLong))) {

				MethodCallStatus status = new MethodCallStatus();
				status.setStatus(MethodCallStatusEnum.FAILED);
				status.setMessage(ErrorUtils
						.readErrorFromFile(getErrorFile(runIdAsLong)));
				return status;
			}
		}

		int runId = Integer.parseInt(runAndSoftwareIdentification.getRunId());
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
			SimulatorServiceEI port = getSimulatorServicePort(url);
			status = port.getRunStatus(runAndSoftwareIdentification.getRunId());
			if (status.getStatus().equals(MethodCallStatusEnum.UNKNOWN_RUNID)) {
				status.setStatus(MethodCallStatusEnum.CALLED_SIMULATOR);
				status.setMessage("The run was submitted to the simulator.");
			}
		} else if (softwareId.getSoftwareType() == ApolloSoftwareTypeEnum.VISUALIZER) {

			VisualizerServiceEI port = getVisualizerServicePort(url);
			status = port.getRunStatus(runAndSoftwareIdentification.getRunId());
			if (status.getStatus().equals(MethodCallStatusEnum.UNKNOWN_RUNID)) {
				status.setStatus(MethodCallStatusEnum.CALLED_VISUALIZER);
				status.setMessage("The run was submitted to the visualizer.");
			}
		} else if (softwareId.getSoftwareType() == ApolloSoftwareTypeEnum.TRANSLATOR) {
			TranslatorServiceEI port = getTranslatorServicePort(url);
			status = port.getRunStatus(runAndSoftwareIdentification);

			if (status.getStatus().equals(MethodCallStatusEnum.UNKNOWN_RUNID)) {
				status.setStatus(MethodCallStatusEnum.CALLED_TRANSLATOR);
				status.setMessage("The run was submitted to the translator.");
			}
		} else {
			return getErrorMethodCallStatus("Unrecognized software type");
		}

		return status;
	}

	@Override
	@WebResult(name = "getLibraryItemResult", targetNamespace = "")
	@RequestWrapper(localName = "getLibraryItem", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetLibraryItem")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getLibraryItem")
	@ResponseWrapper(localName = "getLibraryItemResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetLibraryItemResponse")
	public GetLibraryItemResult getLibraryItem(
			@WebParam(name = "uuid", targetNamespace = "") String uuid) {
		return getLibraryServicePort().getLibraryItem(uuid);
	}

	@Override
	@WebResult(name = "serviceRecords", targetNamespace = "")
	@RequestWrapper(localName = "getRegisteredServices", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetRegisteredServices")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getRegisteredServices")
	@ResponseWrapper(localName = "getRegisteredServicesResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetRegisteredServicesResponse")
	public List<ServiceRecord> getRegisteredServices() {

		try {
			return new ArrayList(dbUtils.getRegisteredSoftware().values());
		} catch (SQLException ex) {
			System.out
					.println("SQLException attempting to get registered services: "
							+ ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out
					.println("ClassNotFoundException attempting to get registered services: "
							+ ex.getMessage());
		} catch (Exception ex) {
			System.out
					.println("Exception attempting to get registered services: "
							+ ex.getMessage());
		}

		return null;
	}

	@Override
	@WebResult(name = "getPopulationAndEnvironmentCensusResult", targetNamespace = "")
	@RequestWrapper(localName = "getPopulationAndEnvironmentCensus", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetPopulationAndEnvironmentCensus")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getPopulationAndEnvironmentCensus")
	@ResponseWrapper(localName = "getPopulationAndEnvironmentCensusResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetPopulationAndEnvironmentCensusResponse")
	public GetPopulationAndEnvironmentCensusResult getPopulationAndEnvironmentCensus(
			@WebParam(name = "simulatorIdentification", targetNamespace = "") SoftwareIdentification simulatorIdentification,
			@WebParam(name = "location", targetNamespace = "") String location) {
		GetPopulationAndEnvironmentCensusResult res = new GetPopulationAndEnvironmentCensusResult();
		MethodCallStatus status = new MethodCallStatus();
		status.setStatus(MethodCallStatusEnum.COMPLETED);
		status.setMessage("Success!");
		res.setPopulationAndEnvironmentCensus(getPopulationAndEnvironmentCensusGivenINCITS(location));
		return res;
	}

	@Override
	@WebResult(name = "visualizationResult", targetNamespace = "")
	@RequestWrapper(localName = "runVisualization", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunVisualization")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/runVisualization")
	@ResponseWrapper(localName = "runVisualizationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunVisualizationResponse")
	public RunVisualizationResult runVisualization(
			@WebParam(name = "runVisualizationMessage", targetNamespace = "") RunVisualizationMessage runVisualizationMessage) {

		RunVisualizationResult result = new RunVisualizationResult();
		try {
			BigInteger runId = getCachedRunIdFromDatabaseOrNull(runVisualizationMessage);
			try {
				if (runId != null) {
					if (runId.longValue() < 0) {
						result.setVisualizationRunId(runId.toString());
						return result;
					}
					int runIdInt = runId.intValue();
					if (isRunFailed(runIdInt)) {
						dbUtils.removeRunData(runIdInt);
					} else {
						result.setVisualizationRunId(Integer.toString(runIdInt));
						return result;
					}
				}

				runId = insertRunIntoDatabase(runVisualizationMessage);

				new ApolloRunVisualizationThread(runId.intValue(),
						runVisualizationMessage, dbUtils, this).start();

				result.setVisualizationRunId(runId.toString());
				return result;
			} catch (ApolloDatabaseException ex) {
				runId = (runId == null) ? new BigInteger(
						Long.toString(getErrorRunId())) : runId;
				ErrorUtils.writeErrorToFile(ex.getMessage(),
						getErrorFile(runId.longValue()));
				result.setVisualizationRunId(runId.toString());
				return result;
			}
		} catch (IOException e) {
			System.err.println("IOException writing error file: "
					+ e.getMessage());
			result.setVisualizationRunId(FATAL_ERROR_CODE);
			return result;
		}
	}

	@Override
	@WebResult(name = "methodCallStatus", targetNamespace = "")
	@RequestWrapper(localName = "removeLibraryItem", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RemoveLibraryItem")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/removeLibraryItem")
	@ResponseWrapper(localName = "removeLibraryItemResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RemoveLibraryItemResponse")
	public MethodCallStatus removeLibraryItem(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "uuid", targetNamespace = "") String uuid) {
		return getLibraryServicePort().removeLibraryItem(authentication, uuid);
	}

	@Override
	@WebResult(name = "getLibraryItemsResult", targetNamespace = "")
	@RequestWrapper(localName = "getUuidsForLibraryItemsCreatedSinceDateTime", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetUuidsForLibraryItemsCreatedSinceDateTime")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getUuidsForLibraryItemsCreatedSinceDateTime")
	@ResponseWrapper(localName = "getUuidsForLibraryItemsCreatedSinceDateTimeResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetUuidsForLibraryItemsCreatedSinceDateTimeResponse")
	public GetLibraryItemUuidsResult getUuidsForLibraryItemsCreatedSinceDateTime(
			@WebParam(name = "creationDateTime", targetNamespace = "") XMLGregorianCalendar creationDateTime) {
		return getLibraryServicePort()
				.getUuidsForLibraryItemsCreatedSinceDateTime(creationDateTime);
	}

	@Override
	@WebResult(name = "getLocationsSupportedBySimulatorResult", targetNamespace = "")
	@RequestWrapper(localName = "getScenarioLocationCodesSupportedBySimulator", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetScenarioLocationCodesSupportedBySimulator")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/getScenarioLocationCodesSupportedBySimulator")
	@ResponseWrapper(localName = "getScenarioLocationCodesSupportedBySimulatorResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetScenarioLocationCodesSupportedBySimulatorResponse")
	public GetScenarioLocationCodesSupportedBySimulatorResult getScenarioLocationCodesSupportedBySimulator(
			@WebParam(name = "simulatorIdentification", targetNamespace = "") SoftwareIdentification simulatorIdentification) {
		GetScenarioLocationCodesSupportedBySimulatorResult res = new GetScenarioLocationCodesSupportedBySimulatorResult();
		for (PopulationAndEnvironmentCensus c : censusData) {
			res.getLocationCodes().add(c.getLocation().getApolloLocationCode());
			for (PopulationAndEnvironmentCensus cc : c.getSubLocationCensuses()) {
				res.getLocationCodes().add(
						cc.getLocation().getApolloLocationCode());
			}
		}
		MethodCallStatus status = new MethodCallStatus();
		status.setStatus(MethodCallStatusEnum.COMPLETED);
		status.setMessage("Returned " + res.getLocationCodes().size()
				+ " items.");
		res.setMethodCallStatus(status);
		return res;

	}

	private static PopulationAndEnvironmentCensus getPopulationAndEnvironmentCensusGivenINCITS(
			String INCITS) {

		for (PopulationAndEnvironmentCensus c : censusData) {
			if (INCITS.length() == 2) { // state
				if (c.getLocation().getApolloLocationCode()
						.equalsIgnoreCase(INCITS)) {
					return c;
				}
			} else { // county
				if (c.getLocation().getApolloLocationCode().substring(0, 2)
						.equalsIgnoreCase(INCITS.substring(0, 2))) {
					// down the rabbit hole
					for (PopulationAndEnvironmentCensus cc : c
							.getSubLocationCensuses()) {
						if (cc.getLocation().getApolloLocationCode()
								.equalsIgnoreCase(INCITS)) {
							return cc;
						}
					}
				}
			}

		}

		return null;
	}

	public static void readCensusData() throws FileNotFoundException,
			DatatypeConfigurationException {
		File f = new File(APOLLO_DIR + "/" + CENSUS_DATA_FILENAME);
		censusData = new ArrayList<PopulationAndEnvironmentCensus>();

		GregorianCalendar cal = new GregorianCalendar();
		XMLGregorianCalendar date = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(cal);
		date.setMonth(1);
		date.setDay(1);
		date.setYear(2009);

		Scanner s = new Scanner(f);
		boolean onHeader = true;
		while (s.hasNextLine()) {
			if (onHeader) {
				onHeader = false;
				s.nextLine();
				continue;

			}
			String[] cols = s.nextLine().split("\t");

			PopulationAndEnvironmentCensus c = new PopulationAndEnvironmentCensus();
			c.setReferenceDate(date);
			c.setDescription(cols[1]);
			Location l = new Location();
			l.setApolloLocationCode(cols[0]);
			c.setLocation(l);
			c.setNameOfAdministativeUnit(cols[1]);
			c.setNumberOfPeople(new BigInteger(cols[2]));
			c.setNumberOfSchools(new BigInteger(cols[3]));
			c.setNumberOfWorkplaces(new BigInteger(cols[4]));

			if (cols[0].length() == 2) { // state
				censusData.add(c);
			} else { // county
				PopulationAndEnvironmentCensus stateRoot = getPopulationAndEnvironmentCensusGivenINCITS(cols[0]
						.substring(0, 2));
				stateRoot.getSubLocationCensuses().add(c);
			}
		}
		s.close();
	}

	// public static void main(String[] args) {
	// PopulationAndEnvironmentCensus c =
	// getPopulationAndEnvironmentCensusGivenINCITS("42");
	// c = getPopulationAndEnvironmentCensusGivenINCITS("42003");
	// }
	static {
		Map<String, String> env = System.getenv();
		APOLLO_DIR = env.get(APOLLO_WORKDIR_ENVIRONMENT_VARIABLE);
		if (APOLLO_DIR != null) {
			if (!APOLLO_DIR.endsWith(File.separator)) {
				APOLLO_DIR += File.separator;
			}
			System.out.println(APOLLO_WORKDIR_ENVIRONMENT_VARIABLE + " is now:"
					+ APOLLO_DIR);
		} else {
			System.out.println(APOLLO_WORKDIR_ENVIRONMENT_VARIABLE
					+ " environment variable not found!");
			APOLLO_DIR = "";
		}

		try {
			dbUtils = new ApolloDbUtils(new File(
					getDatabasePropertiesFilename()));
		} catch (IOException e1) {
			System.out.println("Error in ApolloServiceImpl initializer: "
					+ e1.getMessage());
		}

		// EmbeddedConfiguration configuration =
		// Db4oEmbedded.newConfiguration();
		// configuration.file().generateUUIDs(ConfigScope.GLOBALLY);
		// db4o = Db4oEmbedded.openFile(configuration, APOLLO_DIR + "/"
		// + DB4O_FILENAME);

		try {
			try {
				readCensusData();
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Loading translator software identification");
		try {
			Map<Integer, ServiceRegistrationRecord> softwareIdMap = dbUtils
					.getRegisteredSoftware();
			for (Integer id : softwareIdMap.keySet()) {
				SoftwareIdentification softwareId = softwareIdMap.get(id)
						.getSoftwareIdentification();
				if (softwareId.getSoftwareName().toLowerCase()
						.equals("translator")) {
					translatorServiceRecord = softwareIdMap.get(id);
					break;
				}

			}

		} catch (ClassNotFoundException ex) {
			System.out
					.println("ClassNotFoundException attempting to load the translator service record: "
							+ ex.getMessage());
		} catch (SQLException ex) {
			System.out
					.println("SQLException attempting to load the translator service record: "
							+ ex.getMessage());
		} catch (Exception ex) {
			System.out
					.println("Exception attempting to load the translator service record: "
							+ ex.getMessage());
		}

		// try {
		// ApolloRunSimulationThread.loadTranslatorSoftwareIdentification();
		// } catch (ApolloRunSimulationException ex) {
		// throw new
		// RuntimeException("ApolloRunSimulationException attempting to load the translator service record: "
		// + ex.getMessage());
		// } catch (Exception ex) {
		// throw new
		// RuntimeException("Exception attempting to load the translator service record: "
		// + ex.getMessage());
		// }
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		// db4o.close();
	}

	private boolean isRunFailed(int runId) throws ApolloDatabaseException {
		RunAndSoftwareIdentification rasid = new RunAndSoftwareIdentification();
		rasid.setRunId(Integer.toString(runId));

		// If this call results in an exception, there is a bad database
		// configuration for that run (TODO: WE NEED TO DEAL WITH THIS)
		rasid.setSoftwareId(dbUtils.getLastServiceToBeCalledForRun(runId));

		MethodCallStatus status = getRunStatus(rasid);
		MethodCallStatusEnum statusEnum = status.getStatus();

		if (statusEnum.equals(MethodCallStatusEnum.FAILED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * School is in!  Big Methods become classes!  CMU endorsed!
	 * @param targetRunSimulationMessage
	 * @return
	 * @throws IOException
	 */
	private BigInteger getCachedRunIdFromDatabaseOrNull(
			RunSimulationMessage targetRunSimulationMessage) throws IOException {
		//make a new class per 
		List<Integer> runIds;
		try {
			runIds = dbUtils
					.getSimulationRunIdsAssociatedWithRunSimulationMessageHash(targetRunSimulationMessage);
		} catch (ApolloDatabaseException ex) {
			long errorId = getErrorRunId();
			ErrorUtils.writeErrorToFile(ex.getMessage(), getErrorFile(errorId));
			return new BigInteger(Long.toString(errorId));
		}

		
		if (runIds.size() > 0) { 
			//new function: do the thing that all this does (i.e. hasMatchingRunSimulationMessage
			String targetRunSimulationMessageAsJson = dbUtils
					.getJSONString(targetRunSimulationMessage);
			for (int runIdAssociatedWithRunSimulationMessageHash : runIds) {
				if (hasMatchingRunSImulationMessage(targetRunSimulationMessage, runIdAssociatedWithRunSimulationMessageHash) {
					return new BigIntegr(Integer.toString(runIdAssociatedWithRunSimulationMessageHash));
				}
				try {
					Map<String, ByteArrayOutputStream> currentRunSimulationMessageAsJsonMap = dbUtils
							.getDataContentForSoftware(
									runIdAssociatedWithRunSimulationMessageHash,
									END_USER_APPLICATION_SOURCE_ID,
									dbUtils.getSoftwareIdentificationKey(translatorServiceRecord
											.getSoftwareIdentification()));
					String currentRunSimulationMessageAsJson = null;
					for (String label : currentRunSimulationMessageAsJsonMap
							.keySet()) {
						if (label.equals("run_simulation_message.json")) {
							currentRunSimulationMessageAsJson = currentRunSimulationMessageAsJsonMap
									.get(label).toString();
							break;
						}
					}

					if (currentRunSimulationMessageAsJson == null) {
						throw new ApolloDatabaseException(
								"There was no run_simulation_message.json content for run ID "
										+ runIdAssociatedWithRunSimulationMessageHash);
					}

					if (targetRunSimulationMessageAsJson
							.equals(currentRunSimulationMessageAsJson)) {
						return new BigInteger(
								Integer.toString(runIdAssociatedWithRunSimulationMessageHash));
					}

				} catch (ApolloDatabaseException ex) {
					long errorId = getErrorRunId();
					ErrorUtils
							.writeErrorToFile(
									ex.getMessage(),
									getErrorFile(runIdAssociatedWithRunSimulationMessageHash));
					return new BigInteger(Long.toString(errorId));
				}
			}

			return null;

		} else {
			return null;
		}
	}

	private BigInteger getCachedRunIdFromDatabaseOrNull(
			RunVisualizationMessage targetRunVisualizationMessage)
			throws IOException {

		List<Integer> runIds;
		try {
			runIds = dbUtils
					.getVisualizationRunIdsAssociatedWithRunVisualizationMessageHash(targetRunVisualizationMessage);
		} catch (ApolloDatabaseException ex) {
			long errorId = getErrorRunId();
			ErrorUtils.writeErrorToFile(ex.getMessage(), getErrorFile(errorId));
			return new BigInteger(Long.toString(errorId));
		}

		if (runIds.size() > 0) {
			String targetRunVisualizationMessageAsJson = dbUtils
					.getJSONString(targetRunVisualizationMessage);
			for (int runIdAssociatedWithRunVisualizationMessageHash : runIds) {
				try {
					Map<String, ByteArrayOutputStream> currentRunVisualizationMessageAsJsonMap = dbUtils
							.getDataContentForSoftware(
									runIdAssociatedWithRunVisualizationMessageHash,
									0,
									dbUtils.getSoftwareIdentificationKey(targetRunVisualizationMessage
											.getVisualizerIdentification()));
					String currentRunVisualizationMessageAsJson = null;
					for (String label : currentRunVisualizationMessageAsJsonMap
							.keySet()) {
						if (label.equals("run_visualization_message.json")) {
							currentRunVisualizationMessageAsJson = currentRunVisualizationMessageAsJsonMap
									.get(label).toString();
							break;
						}
					}

					if (currentRunVisualizationMessageAsJson == null) {
						throw new ApolloDatabaseException(
								"There was no run_visualization_message.json content for run ID "
										+ runIdAssociatedWithRunVisualizationMessageHash);
					}

					if (targetRunVisualizationMessageAsJson
							.equals(currentRunVisualizationMessageAsJson)) {
						return new BigInteger(
								Integer.toString(runIdAssociatedWithRunVisualizationMessageHash));
					}

				} catch (ApolloDatabaseException ex) {
					long errorId = getErrorRunId();
					ErrorUtils
							.writeErrorToFile(
									ex.getMessage(),
									getErrorFile(runIdAssociatedWithRunVisualizationMessageHash));
					return new BigInteger(Long.toString(errorId));
				}
			}

			return null;

		} else {
			return null;
		}
	}

	private BigInteger insertRunIntoDatabase(
			RunSimulationMessage runSimulationMessage)
			throws ApolloDatabaseException {
		int md5CollisionId = dbUtils
				.getHighestMD5CollisionIdForRun(runSimulationMessage) + 1;
		BigInteger runId = new BigInteger(Integer.toString(dbUtils
				.addSimulationRun(runSimulationMessage, md5CollisionId,
						translatorServiceRecord.getSoftwareIdentification())));
		return runId;
	}

	private BigInteger insertRunIntoDatabase(
			RunVisualizationMessage runVisualizationMessage)
			throws ApolloDatabaseException {
		int md5CollisionId = dbUtils
				.getHighestMD5CollisionIdForRun(runVisualizationMessage) + 1;
		BigInteger runId = new BigInteger(Integer.toString(dbUtils
				.addVisualizationRun(runVisualizationMessage, md5CollisionId)));
		return runId;
	}
	
	private boolean isErrorRunId(BigInteger runId) {
		return runId.longValue() < 0;
	}

	@Override
	@WebResult(name = "simulationRunId", targetNamespace = "")
	@RequestWrapper(localName = "runSimulation", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunSimulation")
	@WebMethod(action = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/runSimulation")
	@ResponseWrapper(localName = "runSimulationResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.RunSimulationResponse")
	public BigInteger runSimulation(
			@WebParam(name = "runSimulationMessage", targetNamespace = "") RunSimulationMessage runSimulationMessage) {

		try {
			BigInteger runId = getCachedRunIdFromDatabaseOrNull(runSimulationMessage);
			try {
				if (runId != null) {
					if (!isErrorRunId(runId)) {
						return runId;
					}
					int runIdInt = runId.intValue();
					if (isRunFailed(runIdInt)) {
						dbUtils.removeRunData(runIdInt);
					} else {
						return runId;
					}
				}

				runId = insertRunIntoDatabase(runSimulationMessage);

				new ApolloRunSimulationThread(runId.intValue(),
						runSimulationMessage, dbUtils, this).start();

				return runId;
			} catch (ApolloDatabaseException ex) {
				runId = (runId == null) ? new BigInteger(
						Long.toString(getErrorRunId())) : runId;
				ErrorUtils.writeErrorToFile(ex.getMessage(),
						getErrorFile(runId.longValue()));
				return runId;
			}
		} catch (IOException e) {
			System.err.println("IOException writing error file: "
					+ e.getMessage());
			return new BigInteger(FATAL_ERROR_CODE);
		}
	}

	@Override
	@WebResult(name = "getVisualizerOutputResourcesResult", targetNamespace = "")
	@RequestWrapper(localName = "getVisualizerOutputResources", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetVisualizerOutputResources")
	@WebMethod
	@ResponseWrapper(localName = "getVisualizerOutputResourcesResponse", targetNamespace = "http://service.apollo.pitt.edu/apolloservice/v2_0_1/", className = "edu.pitt.apollo.service.apolloservice.v2_0_1.GetVisualizerOutputResourcesResponse")
	public GetVisualizerOutputResourcesResult getVisualizerOutputResources(
			@WebParam(name = "runId", targetNamespace = "") RunAndSoftwareIdentification runId) {
		ApolloDbUtils dbUtils;

		GetVisualizerOutputResourcesResult result = new GetVisualizerOutputResourcesResult();
		MethodCallStatus status = new MethodCallStatus();
		result.setMethodCallStatus(status);
		try {
			dbUtils = new ApolloDbUtils(new File(
					getDatabasePropertiesFilename()));
		} catch (IOException e) {
			status.setMessage("IOException creating ApolloDbUtils for run ID "
					+ runId.getRunId() + ": " + e.getMessage());
			status.setStatus(MethodCallStatusEnum.FAILED);
			return result;
		}
		try {
			Map<String, ByteArrayOutputStream> map = dbUtils
					.getDataContentForSoftware(Integer.parseInt(runId
							.getRunId()),
							dbUtils.getSoftwareIdentificationKey(runId
									.getSoftwareId()), 0);

			if (map.isEmpty()) {
				status.setStatus(MethodCallStatusEnum.FAILED);
				status.setMessage("There were no visualizer resources available for run "
						+ runId);
				return result;
			}

			for (String label : map.keySet()) {
				UrlOutputResource resource = new UrlOutputResource();
				resource.setDescription(label);
				resource.setURL(map.get(label).toString());
				result.getUrlOutputResources().add(resource);
			}

			status.setStatus(MethodCallStatusEnum.COMPLETED);
			status.setMessage("The resources are available");
			return result;

		} catch (ApolloDatabaseException ex) {
			status.setMessage(ex.getMessage());
			status.setStatus(MethodCallStatusEnum.FAILED);
			return result;
		}
	}
}
