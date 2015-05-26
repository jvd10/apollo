package edu.pitt.apollo.runmanagerservice.methods.run.dataserviceaccessors;

import edu.pitt.apollo.Md5UtilsException;

import java.math.BigInteger;

/**
 * Created by jdl50 on 5/21/15.
 */
public class DataServiceAccessorForRunningDataServiceRequests extends DataServiceAccessor {

    @Override
    public BigInteger getCachedRunIdFromDatabaseOrNull() throws DataserviceException, Md5UtilsException {
        return null;
    }

    @Override
    public String getRunMessageAssociatedWithRunIdAsJsonOrNull(BigInteger runId) throws DataserviceException {
        return null;
    }

    @Override
    public BigInteger[] insertRunIntoDatabase(BigInteger memberOfSimulationGroupIdOrNull) throws DataserviceException, Md5UtilsException {
        return new BigInteger[0];
    }
}