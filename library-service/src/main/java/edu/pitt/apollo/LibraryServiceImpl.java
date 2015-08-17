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
import edu.pitt.apollo.interfaces.LibraryServiceInterface;
import edu.pitt.apollo.library_service_types.v3_0_2.AddLibraryItemContainerMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.AddLibraryItemContainerResult;

import java.io.File;
import java.util.Map;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.pitt.apollo.library_service_types.v3_0_2.AddReviewerCommentMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.AddReviewerCommentResult;
import edu.pitt.apollo.library_service_types.v3_0_2.GetChangeLogForLibraryItemsModifiedSinceDateTimeMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetChangeLogForLibraryItemsModifiedSinceDateTimeResult;
import edu.pitt.apollo.library_service_types.v3_0_2.GetCommentsMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetCommentsResult;
import edu.pitt.apollo.library_service_types.v3_0_2.GetLibraryItemContainerMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetLibraryItemContainerResult;
import edu.pitt.apollo.library_service_types.v3_0_2.GetLibraryItemURNsMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetLibraryItemURNsResult;
import edu.pitt.apollo.library_service_types.v3_0_2.GetReleaseVersionMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetReleaseVersionResult;
import edu.pitt.apollo.library_service_types.v3_0_2.GetVersionsMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.GetVersionsResult;
import edu.pitt.apollo.library_service_types.v3_0_2.ModifyGroupOwnershipMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.ModifyGroupOwnershipResult;
import edu.pitt.apollo.library_service_types.v3_0_2.QueryMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.QueryResult;
import edu.pitt.apollo.library_service_types.v3_0_2.SetLibraryItemAsNotReleasedMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.SetLibraryItemAsNotReleasedResult;
import edu.pitt.apollo.library_service_types.v3_0_2.SetReleaseVersionMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.SetReleaseVersionResult;
import edu.pitt.apollo.library_service_types.v3_0_2.UpdateLibraryItemContainerMessage;
import edu.pitt.apollo.library_service_types.v3_0_2.UpdateLibraryItemContainerResult;
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

	static final Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
	public static final String APOLLO_DIR;
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
	public QueryResult query(QueryMessage queryMessage) {
		return QueryLibraryMethod.queryLibrary(libraryDbUtils, readonlyLibraryDbUtils, queryMessage);
	}

	@Override
	public GetLibraryItemContainerResult getLibraryItemContainer(
			GetLibraryItemContainerMessage getLibraryItemContainerMessage) {
		return GetLibraryItemMethod.getLibraryItemMethod(libraryDbUtils, getLibraryItemContainerMessage);
	}

	@Override
	public UpdateLibraryItemContainerResult updateLibraryItemContainer(
			UpdateLibraryItemContainerMessage addOrUpdateLibraryItemContainerMessage) {
		return UpdateLibraryItemMethod.updateLibraryItem(libraryDbUtils, addOrUpdateLibraryItemContainerMessage);
	}

	@Override
	public AddLibraryItemContainerResult addLibraryItemContainer(
			AddLibraryItemContainerMessage addOrUpdateLibraryItemContainerMessage) {
		return AddLibraryItemMethod.addLibraryItem(libraryDbUtils, addOrUpdateLibraryItemContainerMessage);
	}

	@Override
	public GetReleaseVersionResult getLibraryItemReleaseVersion(GetReleaseVersionMessage getLibraryItemReleaseVersionMessage) {
		return GetReleaseVersionMethod.getReleaseVersion(libraryDbUtils, getLibraryItemReleaseVersionMessage);
	}

	@Override
	public GetCommentsResult getCommentsForLibraryItem(GetCommentsMessage getCommentsForLibraryItemMessage) {
		return GetCommentsMethod.getComments(libraryDbUtils, getCommentsForLibraryItemMessage);
	}

	@Override
	public GetVersionsResult getVersionNumbersForLibraryItem(GetVersionsMessage getVersionNumbersForLibraryItemMessage) {
		return GetVersionsMethod.getVersions(libraryDbUtils, getVersionNumbersForLibraryItemMessage);
	}

	@Override
	public ModifyGroupOwnershipResult removeGroupAccessToLibraryItem(ModifyGroupOwnershipMessage removeGroupAccessToLibraryItemMessage) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public GetLibraryItemURNsResult getLibraryItemURNs(GetLibraryItemURNsMessage getLibraryItemURNsMessage) {
		return GetLibraryItemURNsMethod.getLibraryItemURIs(libraryDbUtils, getLibraryItemURNsMessage);
	}

	@Override
	public SetReleaseVersionResult setReleaseVersionForLibraryItem(SetReleaseVersionMessage setReleaseVersionForLibraryItemMessage) {
		return SetReleaseVersionMethod.setReleaseVersion(libraryDbUtils, setReleaseVersionForLibraryItemMessage);
	}

	@Override
	public ModifyGroupOwnershipResult grantGroupAccessToLibraryItem(ModifyGroupOwnershipMessage grantGroupAccessToLibraryItemMessage) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public AddReviewerCommentResult addReviewerCommentToLibraryItem(AddReviewerCommentMessage addReviewerCommentToLibraryItemMessage) {
		return AddReviewerCommentMethod.addReviewerComment(libraryDbUtils, addReviewerCommentToLibraryItemMessage);
	}

	@Override
	public SetLibraryItemAsNotReleasedResult setLibraryItemAsNotReleased(SetLibraryItemAsNotReleasedMessage setLibraryItemAsNotReleasedMessage) {
		return SetLibraryItemAsNotReleasedMethod.setLibraryItemAsNotReleased(libraryDbUtils, setLibraryItemAsNotReleasedMessage);
	}

	@Override
	public GetChangeLogForLibraryItemsModifiedSinceDateTimeResult getChangeLogForLibraryItemsModifiedSinceDateTime(GetChangeLogForLibraryItemsModifiedSinceDateTimeMessage getChangeLogForLibraryItemsModifiedSinceDateTimeMessage) {
		return GetChangeLogForLibraryItemsModifiedSinceDateTimeMethod.getChangeLogForLibraryItemsModifiedSinceDateTime(
				libraryDbUtils, getChangeLogForLibraryItemsModifiedSinceDateTimeMessage);
	}
}
