##################################################
# file: SimulatorService_v2_0_2_server.py
#
# skeleton generated by "ZSI.generate.wsdl2dispatch.ServiceModuleWriter"
#      /usr/local/bin/wsdl2py --complexType --lazy simulatorservice202.wsdl
#
##################################################

from ZSI.schema import GED, GTD
from ZSI.TCcompound import ComplexType, Struct
from SimulatorService_v2_0_2_types import *
from ZSI.ServiceContainer import ServiceSOAPBinding

# Messages  
runSimulationRequest = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "runSimulation").pyclass

runSimulationResponse = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "runSimulationResponse").pyclass

getScenarioLocationCodesSupportedBySimulatorRequest = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "getScenarioLocationCodesSupportedBySimulator").pyclass

getScenarioLocationCodesSupportedBySimulatorResponse = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "getScenarioLocationCodesSupportedBySimulatorResponse").pyclass

runSimulationsRequest = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "runSimulations").pyclass

runSimulationsResponse = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "runSimulationsResponse").pyclass

getPopulationAndEnvironmentCensusRequest = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "getPopulationAndEnvironmentCensus").pyclass

getPopulationAndEnvironmentCensusResponse = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "getPopulationAndEnvironmentCensusResponse").pyclass

terminateRunRequest = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "terminateRun").pyclass

terminateRunResponse = GED("http://service.apollo.pitt.edu/simulatorservice/v2_0_2/", "terminateRunResponse").pyclass


# Service Skeletons
class SimulatorService_v2_0_2(ServiceSOAPBinding):
    soapAction = {}
    root = {}

    def __init__(self, post='/simulatorservice202/services/simulatorservice', **kw):
        ServiceSOAPBinding.__init__(self, post)

    def soap_runSimulation(self, ps, **kw):
        request = ps.Parse(runSimulationRequest.typecode)
        return request,runSimulationResponse()

    soapAction['http://service.apollo.pitt.edu/simulatorservice/v2_0_2/runSimulation'] = 'soap_runSimulation'
    root[(runSimulationRequest.typecode.nspname,runSimulationRequest.typecode.pname)] = 'soap_runSimulation'

    def soap_getScenarioLocationCodesSupportedBySimulator(self, ps, **kw):
        request = ps.Parse(getScenarioLocationCodesSupportedBySimulatorRequest.typecode)
        return request,getScenarioLocationCodesSupportedBySimulatorResponse()

    soapAction['http://service.apollo.pitt.edu/simulatorservice/v2_0_2/getScenarioLocationCodesSupportedBySimulator'] = 'soap_getScenarioLocationCodesSupportedBySimulator'
    root[(getScenarioLocationCodesSupportedBySimulatorRequest.typecode.nspname,getScenarioLocationCodesSupportedBySimulatorRequest.typecode.pname)] = 'soap_getScenarioLocationCodesSupportedBySimulator'

    def soap_runSimulations(self, ps, **kw):
        request = ps.Parse(runSimulationsRequest.typecode)
        return request,runSimulationsResponse()

    soapAction['http://service.apollo.pitt.edu/simulatorservice/v2_0_2/runSimulations'] = 'soap_runSimulations'
    root[(runSimulationsRequest.typecode.nspname,runSimulationsRequest.typecode.pname)] = 'soap_runSimulations'

    def soap_getPopulationAndEnvironmentCensus(self, ps, **kw):
        request = ps.Parse(getPopulationAndEnvironmentCensusRequest.typecode)
        return request,getPopulationAndEnvironmentCensusResponse()

    soapAction['http://service.apollo.pitt.edu/simulatorservice/v2_0_2/getPopulationAndEnvironmentCensus'] = 'soap_getPopulationAndEnvironmentCensus'
    root[(getPopulationAndEnvironmentCensusRequest.typecode.nspname,getPopulationAndEnvironmentCensusRequest.typecode.pname)] = 'soap_getPopulationAndEnvironmentCensus'

    def soap_terminateRun(self, ps, **kw):
        request = ps.Parse(terminateRunRequest.typecode)
        return request,terminateRunResponse()

    soapAction['http://service.apollo.pitt.edu/simulatorservice/v2_0_2/terminateRun'] = 'soap_terminateRun'
    root[(terminateRunRequest.typecode.nspname,terminateRunRequest.typecode.pname)] = 'soap_terminateRun'

