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
package edu.pitt.apollo.examples;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
import edu.pitt.apollo.types.v2_0_1.PopulationInfectionAndImmunityCensus;
import edu.pitt.apollo.types.v2_0_1.PopulationInfectionAndImmunityCensusData;
import edu.pitt.apollo.types.v2_0_1.PopulationInfectionAndImmunityCensusDataCell;
import edu.pitt.apollo.types.v2_0_1.RunSimulationMessage;
import edu.pitt.apollo.types.v2_0_1.SimulatorTimeSpecification;
import edu.pitt.apollo.types.v2_0_1.SoftwareIdentification;
import edu.pitt.apollo.types.v2_0_1.UnitOfTimeEnum;

public class TutorialChapter2_ExampleConfig {

	public static Authentication getAuthentication() {
		Authentication authentication = new Authentication();
		authentication.setRequesterId("TutorialUser");
		authentication.setRequesterPassword("TutorialPassword");
		return authentication;
	}

	public static SoftwareIdentification getSoftwareIdentificationForSimulator() {
		
//		SoftwareIdentification softwareId = new SoftwareIdentification();
//		softwareId.setSoftwareDeveloper("UPitt,PSC,CMU");
//		softwareId.setSoftwareName("FRED");
//		softwareId.setSoftwareVersion("2.0.1_i");
//		softwareId.setSoftwareType(ApolloSoftwareTypeEnum.SIMULATOR);
		SoftwareIdentification softwareId = new SoftwareIdentification();
		softwareId.setSoftwareDeveloper("UPitt");
		softwareId.setSoftwareName("SEIR");
		softwareId.setSoftwareVersion("1.0");
		softwareId.setSoftwareType(ApolloSoftwareTypeEnum.SIMULATOR);
		return softwareId;
	}

	protected SimulatorTimeSpecification getSimulatorTimeSpecification() {
		SimulatorTimeSpecification timeSpec = new SimulatorTimeSpecification();
		timeSpec.setRunLength(new BigInteger("100"));
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
		infectionAcquisition.setFromInfectiousHost(new InfectionAcquisitionFromInfectiousHost());
		infectionAcquisition.getFromInfectiousHost().setBasicReproductionNumber(1.3);
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

}