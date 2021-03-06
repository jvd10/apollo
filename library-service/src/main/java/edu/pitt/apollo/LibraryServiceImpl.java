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

import edu.pitt.apollo.db.LibraryDbUtils;
import edu.pitt.apollo.db.LibraryReadOnlyDbUtils;
import edu.pitt.apollo.db.exceptions.ApolloDatabaseException;
import edu.pitt.apollo.exception.LibraryServiceException;
import edu.pitt.apollo.interfaces.LibraryServiceInterface;
import edu.pitt.apollo.library_service_types.v3_0_2.*;

import java.io.File;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import edu.pitt.apollo.service.libraryservice.v3_0_2.SetLibraryItemAsNotReleased;
import edu.pitt.apollo.services_common.v3_0_2.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.pitt.apollo.libraryservice.methods.AddLibraryItemMethod;
import edu.pitt.apollo.libraryservice.methods.AddReviewerCommentMethod;
import edu.pitt.apollo.libraryservice.methods.GetChangeLogForLibraryItemsModifiedSinceDateTimeMethod;
import edu.pitt.apollo.libraryservice.methods.GetCommentsMethod;
import edu.pitt.apollo.libraryservice.methods.GetLibraryItemMethod;
import edu.pitt.apollo.libraryservice.methods.GetLibraryItemURNsMethod;
import edu.pitt.apollo.libraryservice.methods.GetReleaseVersionMethod;
import edu.pitt.apollo.libraryservice.methods.GetVersionsMethod;
import edu.pitt.apollo.libraryservice.methods.QueryLibraryMethod;
import edu.pitt.apollo.libraryservice.methods.SetLibraryItemAsNotReleasedMethod;
import edu.pitt.apollo.libraryservice.methods.SetReleaseVersionMethod;
import edu.pitt.apollo.libraryservice.methods.UpdateLibraryItemMethod;
import edu.pitt.apollo.service.libraryservice.v3_0_2.LibraryServiceEI;


@WebService(targetNamespace = "http://service.apollo.pitt.edu/libraryservice/v3_0_2/", portName = "LibraryServiceEndpoint", serviceName = "LibraryService_v3.0.2", endpointInterface = "edu.pitt.apollo.service.libraryservice.v3_0_2.LibraryServiceEI")
public class LibraryServiceImpl implements LibraryServiceEI, LibraryServiceInterface {

    public static final String APOLLO_DIR;
    static final Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
    private static final LibraryDbUtils libraryDbUtils;
    private static final LibraryDbUtils readonlyLibraryDbUtils;

    static {
        Map<String, String> env = System.getenv();
        String apolloDir = env.get(GlobalConstants.APOLLO_WORKDIR_ENVIRONMENT_VARIABLE);
        if (apolloDir != null) {
            if (!apolloDir.endsWith(File.separator)) {
                apolloDir += File.separator;
            }
            logger.info(GlobalConstants.APOLLO_WORKDIR_ENVIRONMENT_VARIABLE + " is now:" + apolloDir);
        } else {
            logger.error(GlobalConstants.APOLLO_WORKDIR_ENVIRONMENT_VARIABLE + "environment variable not found!");
        }

        APOLLO_DIR = apolloDir;
        try {
            libraryDbUtils = new LibraryDbUtils();
            readonlyLibraryDbUtils = new LibraryReadOnlyDbUtils();
        } catch (ApolloDatabaseException ex) {
            throw new ExceptionInInitializerError("ApolloDatabaseException initializing LibraryDbUtils: " + ex.getMessage());
        }
    }

    @Override
    public GetLibraryItemContainerResult getLibraryItem(int urn, Integer version, Authentication authentication) throws LibraryServiceException {
        return GetLibraryItemMethod.getLibraryItemMethod(libraryDbUtils, urn, version, authentication);
    }

    @Override
    public UpdateLibraryItemContainerResult reviseLibraryItem(int urn, LibraryItemContainer libraryItemContainer, String comment, Authentication authentication) throws LibraryServiceException {
        return UpdateLibraryItemMethod.updateLibraryItem(libraryDbUtils, urn, libraryItemContainer, comment, authentication);
    }

    @Override
    public AddLibraryItemContainerResult addLibraryItem(LibraryItemContainer libraryItemContainer, String comment, Authentication authentication) throws LibraryServiceException {
        return AddLibraryItemMethod.addLibraryItem(libraryDbUtils, libraryItemContainer, comment, authentication);
    }

    @Override
    public GetCommentsResult getCommentsForLibraryItem(int urn, int version, Authentication authentication) throws LibraryServiceException {
        return GetCommentsMethod.getComments(libraryDbUtils, urn, version, authentication);
    }

    @Override
    public GetRevisionsResult getAllRevisionsOfLibraryItem(int urn, Authentication authentication) throws LibraryServiceException {
        return GetVersionsMethod.getVersions(libraryDbUtils, urn, authentication);
    }

    @Override
    public ModifyGroupOwnershipResult removeGroupAccessToLibraryItem(int urn, String group, Authentication authentication) throws LibraryServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GetLibraryItemURNsResult getLibraryItemURNs(String itemType, Authentication authentication) throws LibraryServiceException {
        return GetLibraryItemURNsMethod.getLibraryItemURIs(libraryDbUtils, itemType, authentication);
    }

    @Override
    public SetReleaseVersionResult approveRevisionOfLibraryItem(int urn, int version, String comment, Authentication authentication) throws LibraryServiceException {
        return SetReleaseVersionMethod.setReleaseVersion(libraryDbUtils, urn, version, comment, authentication);
    }

    @Override
    public ModifyGroupOwnershipResult grantGroupAccessToLibraryItem(int urn, String group, Authentication authentication) throws LibraryServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AddReviewerCommentResult addReviewerCommentToLibraryItem(int urn, int version, String comment, Authentication authentication) throws LibraryServiceException {
       return AddReviewerCommentMethod.addReviewerComment(libraryDbUtils, urn, version, comment, authentication);
    }

    @Override
    public SetLibraryItemAsNotReleasedResult hideLibraryItem(int urn, Authentication authentication) throws LibraryServiceException {
        return SetLibraryItemAsNotReleasedMethod.setLibraryItemAsNotReleased(libraryDbUtils, urn, authentication);
    }

    @Override
    public QueryResult query(String query, Authentication authentication) {
        return QueryLibraryMethod.queryLibrary(libraryDbUtils, readonlyLibraryDbUtils, query, authentication);
    }

    @Override
    public GetChangeLogForLibraryItemsModifiedSinceDateTimeResult getChangeLogForLibraryItemsModifiedSinceDateTime(XMLGregorianCalendar dateTime, Authentication authentication) throws LibraryServiceException {
       return GetChangeLogForLibraryItemsModifiedSinceDateTimeMethod.getChangeLogForLibraryItemsModifiedSinceDateTime(libraryDbUtils, dateTime, authentication);
    }

    @Override
    public UpdateLibraryItemContainerResult updateLibraryItemContainer(
            UpdateLibraryItemContainerMessage addOrUpdateLibraryItemContainerMessage) {
        return UpdateLibraryItemMethod.updateLibraryItem(libraryDbUtils, addOrUpdateLibraryItemContainerMessage.getUrn(), addOrUpdateLibraryItemContainerMessage.getLibraryItemContainer(), addOrUpdateLibraryItemContainerMessage.getComment(), addOrUpdateLibraryItemContainerMessage.getAuthentication());
    }

    @Override
    public GetRevisionsResult getVersionNumbersForLibraryItem(GetVersionsMessage getVersionNumbersForLibraryItemMessage) {
        return GetVersionsMethod.getVersions(libraryDbUtils, getVersionNumbersForLibraryItemMessage.getUrn(), getVersionNumbersForLibraryItemMessage.getAuthentication());
    }

    @Override
    public GetReleaseVersionResult getLibraryItemReleaseVersion(GetReleaseVersionMessage getLibraryItemReleaseVersionMessage) {
        return GetReleaseVersionMethod.getReleaseVersion(libraryDbUtils, getLibraryItemReleaseVersionMessage.getUrn(), getLibraryItemReleaseVersionMessage.getAuthentication());
    }

    @Override
    public AddLibraryItemContainerResult addLibraryItemContainer(
            AddLibraryItemContainerMessage addOrUpdateLibraryItemContainerMessage) {
        return AddLibraryItemMethod.addLibraryItem(libraryDbUtils, addOrUpdateLibraryItemContainerMessage.getLibraryItemContainer(), addOrUpdateLibraryItemContainerMessage.getComment(), addOrUpdateLibraryItemContainerMessage.getAuthentication());
    }

    @Override
    public ModifyGroupOwnershipResult removeGroupAccessToLibraryItem(ModifyGroupOwnershipMessage removeGroupAccessToLibraryItemMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GetLibraryItemContainerResult getLibraryItemContainer(
            GetLibraryItemContainerMessage getLibraryItemContainerMessage) {
        return GetLibraryItemMethod.getLibraryItemMethod(libraryDbUtils, getLibraryItemContainerMessage.getUrn(), getLibraryItemContainerMessage.getVersion(), getLibraryItemContainerMessage.getAuthentication());
    }

    @Override
    public QueryResult query(QueryMessage queryMessage) {
        return QueryLibraryMethod.queryLibrary(libraryDbUtils, readonlyLibraryDbUtils, queryMessage.getQuery(), queryMessage.getAuthentication());
    }

    @Override
    public GetChangeLogForLibraryItemsModifiedSinceDateTimeResult getChangeLogForLibraryItemsModifiedSinceDateTime(GetChangeLogForLibraryItemsModifiedSinceDateTimeMessage getChangeLogForLibraryItemsModifiedSinceDateTimeMessage) {
        return GetChangeLogForLibraryItemsModifiedSinceDateTimeMethod.getChangeLogForLibraryItemsModifiedSinceDateTime(
                libraryDbUtils, getChangeLogForLibraryItemsModifiedSinceDateTimeMessage.getDateTime(), getChangeLogForLibraryItemsModifiedSinceDateTimeMessage.getAuthentication());
    }

    @Override
    public AddReviewerCommentResult addReviewerCommentToLibraryItem(AddReviewerCommentMessage addReviewerCommentToLibraryItemMessage) {
        return AddReviewerCommentMethod.addReviewerComment(libraryDbUtils, addReviewerCommentToLibraryItemMessage.getUrn(), addReviewerCommentToLibraryItemMessage.getVersion(), addReviewerCommentToLibraryItemMessage.getComment(), addReviewerCommentToLibraryItemMessage.getAuthentication());
    }

    @Override
    public SetLibraryItemAsNotReleasedResult setLibraryItemAsNotReleased(SetLibraryItemAsNotReleasedMessage setLibraryItemAsNotReleasedMessage) {
        return SetLibraryItemAsNotReleasedMethod.setLibraryItemAsNotReleased(libraryDbUtils, setLibraryItemAsNotReleasedMessage.getUrn(), setLibraryItemAsNotReleasedMessage.getAuthentication());
    }

    @Override
    public GetCommentsResult getCommentsForLibraryItem(GetCommentsMessage getCommentsForLibraryItemMessage) {
        return GetCommentsMethod.getComments(libraryDbUtils, getCommentsForLibraryItemMessage.getUrn(), getCommentsForLibraryItemMessage.getVersion(), getCommentsForLibraryItemMessage.getAuthentication());
    }

    @Override
    public ModifyGroupOwnershipResult grantGroupAccessToLibraryItem(ModifyGroupOwnershipMessage grantGroupAccessToLibraryItemMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SetReleaseVersionResult setReleaseVersionForLibraryItem(SetReleaseVersionMessage setReleaseVersionForLibraryItemMessage) {
        return SetReleaseVersionMethod.setReleaseVersion(libraryDbUtils, setReleaseVersionForLibraryItemMessage.getUrn(), setReleaseVersionForLibraryItemMessage.getVersion(), setReleaseVersionForLibraryItemMessage.getComment(), setReleaseVersionForLibraryItemMessage.getAuthentication());
    }

    @Override
    public GetLibraryItemURNsResult getLibraryItemURNs(GetLibraryItemURNsMessage getLibraryItemURNsMessage) {
        return GetLibraryItemURNsMethod.getLibraryItemURIs(libraryDbUtils, getLibraryItemURNsMessage.getItemType(), getLibraryItemURNsMessage.getAuthentication());
    }
}
