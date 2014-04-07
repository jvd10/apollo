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
package edu.pitt.apollo.apolloclient;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import edu.pitt.apollo.service.apolloservice.v2_0_1.ApolloServiceEI;
import edu.pitt.apollo.service.apolloservice.v2_0_1.ApolloServiceV201;
import edu.pitt.apollo.types.v2_0_1.ApolloPathogenCode;
import edu.pitt.apollo.types.v2_0_1.ApolloSoftwareTypeEnum;
import edu.pitt.apollo.types.v2_0_1.Authentication;
import edu.pitt.apollo.types.v2_0_1.FixedDuration;
import edu.pitt.apollo.types.v2_0_1.Infection;
import edu.pitt.apollo.types.v2_0_1.InfectionAcquisition;
import edu.pitt.apollo.types.v2_0_1.InfectionAcquisitionFromInfectiousHost;
import edu.pitt.apollo.types.v2_0_1.InfectionStateEnum;
import edu.pitt.apollo.types.v2_0_1.InfectiousDisease;
import edu.pitt.apollo.types.v2_0_1.InfectiousDiseaseScenario;
import edu.pitt.apollo.types.v2_0_1.Location;
import edu.pitt.apollo.types.v2_0_1.LocationDefinition;
import edu.pitt.apollo.types.v2_0_1.MethodCallStatus;
import edu.pitt.apollo.types.v2_0_1.MethodCallStatusEnum;
import edu.pitt.apollo.types.v2_0_1.PopulationInfectionAndImmunityCensus;
import edu.pitt.apollo.types.v2_0_1.PopulationInfectionAndImmunityCensusData;
import edu.pitt.apollo.types.v2_0_1.PopulationInfectionAndImmunityCensusDataCell;
import edu.pitt.apollo.types.v2_0_1.RunAndSoftwareIdentification;
import edu.pitt.apollo.types.v2_0_1.RunSimulationMessage;
import edu.pitt.apollo.types.v2_0_1.RunVisualizationMessage;
import edu.pitt.apollo.types.v2_0_1.SimulatorTimeSpecification;
import edu.pitt.apollo.types.v2_0_1.SoftwareIdentification;
import edu.pitt.apollo.types.v2_0_1.UnitOfTimeEnum;
import edu.pitt.apollo.types.v2_0_1.UrlOutputResource;
import edu.pitt.apollo.types.v2_0_1.VisualizerResult;


public class TutorialChapter2_BasicRunSimulationExample {

	public static final String WSDL_LOC = "http://localhost:8080/apolloservice2.0.1/services/apolloservice?wsdl";

	private ApolloServiceEI port;

	private static final QName SERVICE_NAME = new QName("http://service.apollo.pitt.edu/apolloservice/v2_0_1/",
			"ApolloService_v2.0.1");

	protected TutorialChapter2_BasicRunSimulationExample(boolean connectToService) throws MalformedURLException {
		if (connectToService) {
			ApolloServiceV201 ss = new ApolloServiceV201(new URL(WSDL_LOC), SERVICE_NAME);
			port = ss.getApolloServiceEndpoint();
		}
	}

	public ApolloServiceEI getPort() {
		return port;
	}

	public SoftwareIdentification getSoftwareIdentificationForSimulator() {
		SoftwareIdentification softwareId = new SoftwareIdentification();
		softwareId.setSoftwareDeveloper("UPitt");
		softwareId.setSoftwareName("SEIR");
		softwareId.setSoftwareVersion("1.0");
		softwareId.setSoftwareType(ApolloSoftwareTypeEnum.SIMULATOR);
		return softwareId;
	}

	protected SoftwareIdentification getSoftwareIdentifiationForTimeSeriesVisualizer() {
		SoftwareIdentification softwareId = new SoftwareIdentification();
		softwareId.setSoftwareName("Time Series Visualizer");
		softwareId.setSoftwareType(ApolloSoftwareTypeEnum.VISUALIZER);
		softwareId.setSoftwareVersion("1.0");
		softwareId.setSoftwareDeveloper("UPitt");
		return softwareId;
	}

	protected SoftwareIdentification getSoftwareIdentifiationForGaia() {
		SoftwareIdentification softwareId = new SoftwareIdentification();
		softwareId.setSoftwareName("GAIA");
		softwareId.setSoftwareType(ApolloSoftwareTypeEnum.VISUALIZER);
		softwareId.setSoftwareVersion("1.0");
		softwareId.setSoftwareDeveloper("PSC");
		return softwareId;
	}

	protected Authentication getAuthentication() {
		Authentication authentication = new Authentication();
		authentication.setRequesterId("TutorialUser");
		authentication.setRequesterPassword("TutorialPassword");
		return authentication;
	}

	protected SimulatorTimeSpecification getSimulatorTimeSpecification() {
		SimulatorTimeSpecification timeSpec = new SimulatorTimeSpecification();
		timeSpec.setRunLength(new BigInteger("90"));
		timeSpec.setUnitOfTimeForSimulatorTimeStep(UnitOfTimeEnum.DAY);
		timeSpec.setNumberOfUnitsOfTimeInOneSimulatorTimeStep(1.0);
		return timeSpec;
	}

	protected PopulationInfectionAndImmunityCensus getPopulationInfectionAndImmunityCensus() {
		PopulationInfectionAndImmunityCensus census = new PopulationInfectionAndImmunityCensus();
		census.setDescription("Population of Allegheny County, Pennsylvania");

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2009);
		calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		XMLGregorianCalendar censusDate = null;
		try {
			censusDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			System.out.println("Error!  Unable to set date, error was:" + e.getMessage());
			System.exit(-1);
		}
		census.setReferenceDate(censusDate);

		Location location = new Location();
		location.setApolloLocationCode("42003");
		census.setLocation(location);
		census.setPopulationSpecies("9606"); // homo sapiens

		ApolloPathogenCode pathId = new ApolloPathogenCode();
		pathId.setCladeName("H1N1");
		pathId.setNcbiTaxonId("114727"); // Influenza A subtype H1N1
		census.setPathogen(pathId);

		PopulationInfectionAndImmunityCensusData data = new PopulationInfectionAndImmunityCensusData();
		data.setLocation(location);
		PopulationInfectionAndImmunityCensusDataCell susceptibleCell = new PopulationInfectionAndImmunityCensusDataCell();
		susceptibleCell.setInfectionState(InfectionStateEnum.SUSCEPTIBLE);
		susceptibleCell.setFractionInInfectionState(0.94);
		PopulationInfectionAndImmunityCensusDataCell exposedCell = new PopulationInfectionAndImmunityCensusDataCell();
		exposedCell.setInfectionState(InfectionStateEnum.EXPOSED);
		exposedCell.setFractionInInfectionState(0.0);
		PopulationInfectionAndImmunityCensusDataCell infectiousCell = new PopulationInfectionAndImmunityCensusDataCell();
		infectiousCell.setInfectionState(InfectionStateEnum.INFECTIOUS);
		infectiousCell.setFractionInInfectionState(0.01);
		PopulationInfectionAndImmunityCensusDataCell recoveredCell = new PopulationInfectionAndImmunityCensusDataCell();
		recoveredCell.setInfectionState(InfectionStateEnum.RECOVERED);
		recoveredCell.setFractionInInfectionState(0.05);

		data.getCensusDataCells().add(susceptibleCell);
		data.getCensusDataCells().add(exposedCell);
		data.getCensusDataCells().add(infectiousCell);
		data.getCensusDataCells().add(recoveredCell);

		census.setCensusData(data);
		return census;
	}

	protected InfectiousDisease getInfectiousDisease() {
		InfectiousDisease disease = new InfectiousDisease();
		disease.setDiseaseId("H1N1");
		disease.setSpeciesWithDisease("9606"); // homo sapiens

		ApolloPathogenCode pathId = new ApolloPathogenCode();
		pathId.setCladeName("H1N1");
		pathId.setNcbiTaxonId("114727"); // Influenza A subtype H1N1

		disease.setCausalPathogen(pathId);
		return disease;
	}

	protected Infection getInfection() {
		Infection infection = new Infection();

		ApolloPathogenCode pathId = new ApolloPathogenCode();
		pathId.setCladeName("H1N1");
		pathId.setNcbiTaxonId("114727"); // Influenza A subtype H1N1
		infection.setPathogenTaxonId(pathId);

		infection.setHostTaxonId("9606"); // homo sapiens

		FixedDuration infectiousPeriod = new FixedDuration();
		infectiousPeriod.setUnitOfTime(UnitOfTimeEnum.DAY);
		infectiousPeriod.setValue(6.0);
		infection.setInfectiousPeriodDuration(infectiousPeriod);

		FixedDuration latentPeriod = new FixedDuration();
		latentPeriod.setUnitOfTime(UnitOfTimeEnum.DAY);
		latentPeriod.setValue(2.0);
		infection.setLatentPeriodDuration(latentPeriod);

		infection.getInfectionAcquisition().add(getInfectionAcquisition());

		return infection;
	}

	private InfectionAcquisition getInfectionAcquisition() {
		InfectionAcquisition infectionAcquisition = new InfectionAcquisition();

		ApolloPathogenCode pathId = new ApolloPathogenCode();
		pathId.setCladeName("H1N1");
		pathId.setNcbiTaxonId("114727"); // Influenza A subtype H1N1
		infectionAcquisition.setPathogenTaxonId(pathId);
		infectionAcquisition.setSusceptibleHostTaxonId("9606"); // homo sapiens
                InfectionAcquisitionFromInfectiousHost iafih = new InfectionAcquisitionFromInfectiousHost();
                iafih.setInfectiousHostTaxonId("9606");
                iafih.setBasicReproductionNumber(1.3);
		infectionAcquisition.setFromInfectiousHost(iafih);
		return infectionAcquisition;
	}

	protected InfectiousDiseaseScenario getInfectiousDiseaseScenario() {
		InfectiousDiseaseScenario scenario = new InfectiousDiseaseScenario();

		LocationDefinition definition = new LocationDefinition();
		definition.setDescription("Allegheny County, Pennsylvania");
		// set the scenario location to Allegheny County
		Location location = new Location();
		location.setApolloLocationCode("42003");
		scenario.setLocation(location);

		// set the scenario date to 2009/09/01
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2009);
		calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// translate from Java style Calendar to an XML compatible calendar
		XMLGregorianCalendar scenarioDate = null;
		try {
			scenarioDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			System.out.println("Error!  Unable to set date, error was:" + e.getMessage());
			System.exit(-1);
		}

		scenario.setScenarioDate(scenarioDate);
		scenario.getInfections().add(getInfection());
		scenario.getDiseases().add(getInfectiousDisease());
		scenario.getPopulationInfectionAndImmunityCensuses().add(getPopulationInfectionAndImmunityCensus());

		return scenario;
	}

	public RunSimulationMessage getRunSimulationMessage() {
		RunSimulationMessage message = new RunSimulationMessage();
		message.setInfectiousDiseaseScenario(getInfectiousDiseaseScenario());
		message.setAuthentication(getAuthentication());
		message.setSimulatorIdentification(getSoftwareIdentificationForSimulator());
		message.setSimulatorTimeSpecification(getSimulatorTimeSpecification());
		return message;
	}

	protected MethodCallStatus checkStatusOfWebServiceCall(RunAndSoftwareIdentification runAndSoftwareId) {
		// give the simulator a chance to launch the simulation
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// this is acceptable
		}
		while (true) {
			MethodCallStatus status = port.getRunStatus(runAndSoftwareId);

			switch (status.getStatus()) {

			case AUTHENTICATION_FAILURE:
			case UNAUTHORIZED:
				System.out.println("No authorization for this run! Error message is:" + status.getMessage());
				return status;
			case COMPLETED:
				System.out.println("Completed!");
				return status;
			case FAILED:
				System.out.println("Run Failed! Error message is:" + status.getMessage());
				return status;
			case RUNNING:
			case MOVING:
			case QUEUED:
			case HELD:
			case EXITING:
			case WAITING:
				System.out.println("The " + runAndSoftwareId.getSoftwareId().getSoftwareName() + " run is active ("
						+ status.getStatus().toString() + "). The status message is: " + status.getMessage());
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	protected void getResourcesFromVisualizer(String simulatorRunId, SoftwareIdentification visualizerSoftwareIdentification) {
		System.out.println("Visualizing runId: " + simulatorRunId + " using the "
				+ visualizerSoftwareIdentification.getSoftwareName() + " visualizer...");

		RunVisualizationMessage runVisualizationMessage = new RunVisualizationMessage();

		runVisualizationMessage.setVisualizerIdentification(visualizerSoftwareIdentification);

		Authentication auth = new Authentication();
		auth.setRequesterId("TutorialUser");
		auth.setRequesterPassword("TutorialPassword");
		runVisualizationMessage.setAuthentication(auth);

		VisualizerResult visualizerResult = port.runVisualization(runVisualizationMessage);

		String visualizationRunId = visualizerResult.getRunId();

		RunAndSoftwareIdentification visualizationRunAndSoftwareId = new RunAndSoftwareIdentification();
		visualizationRunAndSoftwareId.setRunId(visualizationRunId);
		visualizationRunAndSoftwareId.setSoftwareId(visualizerSoftwareIdentification);

		if (checkStatusOfWebServiceCall(visualizationRunAndSoftwareId).getStatus() == MethodCallStatusEnum.COMPLETED) {
			System.out.println("The following resources were returned from the "
					+ visualizerSoftwareIdentification.getSoftwareName() + " visualizer:");
			for (UrlOutputResource r : visualizerResult.getVisualizerOutputResource()) {
				System.out.println("\t" + r.getURL());
			}
		}
	}

	protected RunAndSoftwareIdentification runSimulation(RunSimulationMessage runSimulationMessage) {
		BigInteger simulationRunId = port.runSimulation(runSimulationMessage);
		System.out.println("The simulator returned a runId of " + simulationRunId);

		RunAndSoftwareIdentification runAndSoftwareId = new RunAndSoftwareIdentification();
		runAndSoftwareId.setSoftwareId(getSoftwareIdentificationForSimulator());
		runAndSoftwareId.setRunId(simulationRunId.toString());

		MethodCallStatus status = checkStatusOfWebServiceCall(runAndSoftwareId);
		if (status.getStatus() == MethodCallStatusEnum.COMPLETED) {
			return runAndSoftwareId;
		} else {
			System.exit(-1);
			return null;
		}
	}

	protected void displayResults(RunAndSoftwareIdentification simulatorRunAndSoftwareId) {
//		getResourcesFromVisualizer(simulatorRunAndSoftwareId.getRunId(), getSoftwareIdentifiationForTimeSeriesVisualizer());
//		getResourcesFromVisualizer(simulatorRunAndSoftwareId.getRunId(), getSoftwareIdentifiationForGaia());
	}

	protected RunAndSoftwareIdentification runSimulationAndDisplayResults(RunSimulationMessage runSimulationMessage) {
		RunAndSoftwareIdentification simulatorRunAndSoftwareId = runSimulation(runSimulationMessage);
		displayResults(simulatorRunAndSoftwareId);
		return simulatorRunAndSoftwareId;
	}

	public static void main(String args[]) throws java.lang.Exception {
		TutorialChapter2_BasicRunSimulationExample example = new TutorialChapter2_BasicRunSimulationExample(true);
		RunSimulationMessage runSimulationMessage = example.getRunSimulationMessage();
		example.runSimulationAndDisplayResults(runSimulationMessage);
	}

}