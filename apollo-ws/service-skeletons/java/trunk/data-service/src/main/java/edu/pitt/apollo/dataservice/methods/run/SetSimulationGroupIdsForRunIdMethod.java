package edu.pitt.apollo.dataservice.methods.run;

import edu.pitt.apollo.Md5UtilsException;
import edu.pitt.apollo.dataservice.methods.database.DatabaseAccessor;
import edu.pitt.apollo.db.ApolloDbUtils;
import edu.pitt.apollo.db.exceptions.ApolloDatabaseException;
import edu.pitt.apollo.exception.DataServiceException;
import edu.pitt.apollo.services_common.v3_0_0.Authentication;
import edu.pitt.apollo.services_common.v3_0_0.MethodCallStatus;
import edu.pitt.apollo.services_common.v3_0_0.MethodCallStatusEnum;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by dcs27 on 5/21/15.
 */
public class SetSimulationGroupIdsForRunIdMethod {

    public static MethodCallStatus setSimulationGroupIdsForRun(List<BigInteger> groupIds, BigInteger runId, Authentication authentication) {
        MethodCallStatus mcs = new MethodCallStatus();
        try(ApolloDbUtils dbUtils = new ApolloDbUtils())
        {
            DatabaseAccessor dbAccessor = new DatabaseAccessor(authentication, dbUtils);
            dbAccessor.addRunIdsToSimulationGroupWithRunId(runId, groupIds, authentication);
        } catch (ApolloDatabaseException e) {
            e.printStackTrace();
            mcs.setStatus(MethodCallStatusEnum.FAILED);
            mcs.setMessage(e.getMessage());
        }catch (DataServiceException e) {
            e.printStackTrace();
            mcs.setStatus(MethodCallStatusEnum.FAILED);
            mcs.setMessage(e.getMessage());
        }
        return mcs;
    }
}