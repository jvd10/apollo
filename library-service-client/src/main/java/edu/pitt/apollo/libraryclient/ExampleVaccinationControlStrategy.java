package edu.pitt.apollo.libraryclient;

import edu.pitt.apollo.types.v3_0_2.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * Author: Nick Millett
 * Email: nick.millett@gmail.com
 * Date: Dec 9, 2014
 * Time: 5:17:04 PM
 * Class: ExampleVaccinationControlStrategy
 */
public class ExampleVaccinationControlStrategy {

	public static IndividualTreatmentControlStrategy getStrategy(XMLGregorianCalendar startDate) {
		ApolloPathogenCode strain = new ApolloPathogenCode();
		strain.setNcbiTaxonId("114727");

		Vaccination vacc = new Vaccination();
		vacc.setDescription("H1N1 Vaccine");
		vacc.setNumDosesInTreatmentCourse(new BigInteger("1"));
		vacc.setSpeciesOfTreatedOrganism("9606");
		vacc.setPathogen(strain);
		vacc.setVaccineId("Influenza A (H1N1) 2009 Monovalent Vaccine");

		FixedDuration durationOfTreatmentCourse = new FixedDuration();
		durationOfTreatmentCourse.setUnitOfTime(UnitOfTimeEnum.DAY);
		durationOfTreatmentCourse.setValue(1);
		vacc.setDurationOfTreatmentCourse(durationOfTreatmentCourse);

//        Vaccine vaccine = new Vaccine();
//        vaccine.setDescription("Influenza A (H1N1) 2009 Monovalent Vaccine");
//        vaccine.setVaccineIdentifier("Influenza A (H1N1) 2009 Monovalent Vaccine");
//        vaccine.getValence().add(new BigInteger("1"));
//        vacc.setVaccine(vaccine);
		VaccinationEfficacyForSimulatorConfiguration vesc = new VaccinationEfficacyForSimulatorConfiguration();

		vesc.setStrainIdentifier(strain);
		vesc.setHostIdentifier("9606");
		vesc.setForTreatmentPreventableOutcome(TreatmentPreventableOutcomeEnum.INFECTION);
//        vesc.setVaccineIdentifier("Influenza A (H1N1) 2009 Monovalent Vaccine");
		vesc.setAverageVaccinationEfficacy(0.7);
//        vesc.setDescription("The vaccination efficacy for the Influenza A (H1N1) 2009 Monovalent Vaccine");
//        vesc.setTreatment(vacc2);

		vacc.getVaccinationEfficacies().add(vesc);

		IndividualTreatmentControlStrategy vcm = new IndividualTreatmentControlStrategy();
		vcm.setIndividualTreatment(vacc);
		ProbabilisticParameter compliance = new ProbabilisticParameter();
		compliance.setProbability(0.5);
		vcm.setCompliance(compliance);

		ControlStrategyTargetPopulationsAndPrioritization targetPopulationsAndPrioritization = new ControlStrategyTargetPopulationsAndPrioritization();
		targetPopulationsAndPrioritization.setControlStrategyNamedPrioritizationScheme(NamedPrioritizationSchemeEnum.ACIP);

		vcm.setTargetPopulationsAndPrioritizations(targetPopulationsAndPrioritization);

		FixedDuration stopTime = new FixedDuration();
		stopTime.setUnitOfTime(UnitOfTimeEnum.DAY);
		stopTime.setValue(127);

		TemporalTriggerDefinition stopTrigger = new TemporalTriggerDefinition();
		stopTrigger.setTimeScale(TimeScaleEnum.SIMULATOR_TIME_SCALE);
		stopTrigger.setTimeSinceTimeScaleZero(stopTime);
		vcm.getControlStrategyStopTime().add(stopTrigger);

		FixedDuration responseDelay = new FixedDuration();
		responseDelay.setUnitOfTime(UnitOfTimeEnum.DAY);
		responseDelay.setValue(0d);

		vcm.setControlStrategyResponseDelay(responseDelay);

		FixedDuration standDownDelay = new FixedDuration();
		standDownDelay.setUnitOfTime(UnitOfTimeEnum.DAY);
		standDownDelay.setValue(0d);

		vcm.setControlStrategyStandDownDelay(standDownDelay);

		FixedDuration startTime = new FixedDuration();
		startTime.setUnitOfTime(UnitOfTimeEnum.DAY);
		startTime.setValue(0);

		TemporalTriggerDefinition trigger = new TemporalTriggerDefinition();
		trigger.setTimeScale(TimeScaleEnum.SIMULATOR_TIME_SCALE);
		trigger.setTimeSinceTimeScaleZero(startTime);
		vcm.getControlStrategyStartTime().add(trigger);

		vcm.setDescription("The vaccination control strategy used by Allegheny County to mitigate the spread of H1N1 for the 2009 Influenza season.");
		vcm.setPathogen(strain);

        LogisticalSystem logisticalSystem = new LogisticalSystem();
        logisticalSystem.setProduct("Influenza A (H1N1) 2009 Monovalent Vaccine");
        LogisticalSystemNode outputNode = new LogisticalSystemNode();
        Schedule outputSchedule = new Schedule();
        outputNode.setOutputSchedule(outputSchedule);
		outputSchedule.setUnitOfMeasure(UnitOfMeasureEnum.INDIVIDUAL_TREATMENTS);

        Calendar outputCal = startDate.toGregorianCalendar();
        outputCal.add(Calendar.DATE, 28);

        LogisticalSystemNode capacityNode = new LogisticalSystemNode();
        Schedule capacitySchedule = new Schedule();
        capacitySchedule.setUnitOfMeasure(UnitOfMeasureEnum.INDIVIDUAL_TREATMENTS);
        capacityNode.setCapacitySchedule(capacitySchedule);

        try {
            for (int i = 28; i < 84; i++) {
                ScheduleElement element = new ScheduleElement();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTimeInMillis(outputCal.getTimeInMillis());
                element.setDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
                element.setQuantity(new BigInteger("3500"));
                outputCal.add(Calendar.DATE, 1);
                outputSchedule.getScheduleElements().add(element);
            }

            for (int i = 84; i < 115; i++) {
                ScheduleElement element = new ScheduleElement();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTimeInMillis(outputCal.getTimeInMillis());
                element.setDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
                element.setQuantity(new BigInteger("10000"));
                outputCal.add(Calendar.DATE, 1);
                outputSchedule.getScheduleElements().add(element);
            }

            for (int i = 115; i < 127; i++) {
                ScheduleElement element = new ScheduleElement();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTimeInMillis(outputCal.getTimeInMillis());
                element.setDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
                element.setQuantity(new BigInteger("3500"));
                outputCal.add(Calendar.DATE, 1);
                outputSchedule.getScheduleElements().add(element);
            }

            Calendar capacityCal = startDate.toGregorianCalendar();
            capacityCal.add(Calendar.DATE, 28);

            for (int i = 28; i < 127; i++) {
                ScheduleElement element = new ScheduleElement();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTimeInMillis(outputCal.getTimeInMillis());
                element.setDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
                element.setQuantity(new BigInteger("5000"));
                capacityCal.add(Calendar.DATE, 1);
                capacitySchedule.getScheduleElements().add(element);
            }
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException("DatatypeConfigurationException: " + ex.getMessage());
        }

        outputNode.getChildren().add(capacityNode);
        logisticalSystem.getLogisticalSystemNodes().add(outputNode);

        vcm.getLogisticalSystems().add(logisticalSystem);

        return vcm;
	}

}
