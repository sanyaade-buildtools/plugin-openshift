/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.openshift.client.project;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.project.ProjectServiceClient;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.api.workspace.shared.dto.ProjectConfigDto;
import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.ext.openshift.client.OpenshiftLocalizationConstant;
import org.eclipse.che.ide.ext.openshift.shared.OpenshiftProjectTypeConstants;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.eclipse.che.ide.ext.openshift.shared.OpenshiftProjectTypeConstants.OPENSHIFT_PROJECT_TYPE_ID;
import static org.eclipse.che.ide.workspace.perspectives.project.ProjectPerspective.PROJECT_PERSPECTIVE_ID;
import static org.eclipse.che.ide.api.notification.StatusNotification.DisplayMode.EMERGE_MODE;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.FAIL;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.SUCCESS;

/**
 * Action for unlink Che project from OpenShift.
 *
 * @author Sergii Leschenko
 */
@Singleton
public class UnlinkProjectAction extends AbstractPerspectiveAction {

    private final AppContext           appContext;
    private final ProjectServiceClient projectServiceClient;
    private final NotificationManager  notificationManager;
    private final DtoFactory           dtoFactory;
    private final OpenshiftLocalizationConstant locale;

    @Inject
    public UnlinkProjectAction(AppContext appContext,
                               ProjectServiceClient projectServiceClient,
                               NotificationManager notificationManager,
                               DtoFactory dtoFactory,
                               OpenshiftLocalizationConstant locale) {
        super(Collections.singletonList(PROJECT_PERSPECTIVE_ID), locale.unlinkProjectActionTitle(), null, null, null);
        this.appContext = appContext;
        this.projectServiceClient = projectServiceClient;
        this.notificationManager = notificationManager;
        this.dtoFactory = dtoFactory;
        this.locale = locale;
    }

    @Override
    public void updateInPerspective(@NotNull ActionEvent event) {
        final Resource resource = appContext.getResource();
        if (resource != null) {
            final Optional<Project> relatedProject = resource.getRelatedProject();
            event.getPresentation().setVisible(relatedProject.isPresent());
            event.getPresentation().setEnabled(relatedProject.isPresent()
                                               && relatedProject.get().getMixins().contains(OPENSHIFT_PROJECT_TYPE_ID));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Resource resource = appContext.getResource();
        if (resource != null && resource.getRelatedProject().isPresent()) {
            final Project relatedProject = resource.getRelatedProject().get();
            List<String> mixins = relatedProject.getMixins();
            if (mixins.contains(OpenshiftProjectTypeConstants.OPENSHIFT_PROJECT_TYPE_ID)) {
                mixins.remove(OpenshiftProjectTypeConstants.OPENSHIFT_PROJECT_TYPE_ID);
                Map<String, List<String>> attributes = relatedProject.getAttributes();
                attributes.remove(OpenshiftProjectTypeConstants.OPENSHIFT_APPLICATION_VARIABLE_NAME);
                attributes.remove(OpenshiftProjectTypeConstants.OPENSHIFT_NAMESPACE_VARIABLE_NAME);
                final ProjectConfigDto dto = dtoFactory.createDto(ProjectConfigDto.class)
                                                       .withPath(relatedProject.getPath())
                                                       .withDescription(relatedProject.getDescription())
                                                       .withType(relatedProject.getType())
                                                       .withMixins(mixins)
                                                       .withAttributes(attributes);
                projectServiceClient.updateProject(dto)
                                    .then(new Operation<ProjectConfigDto>() {
                                        @Override
                                        public void apply(ProjectConfigDto result) throws OperationException {
                                            //appContext.getCurrentProject().setRootProject(result);
                                            notificationManager.notify(locale.unlinkProjectSuccessful(result.getName()),
                                                                       SUCCESS,
                                                                       EMERGE_MODE);
                                        }
                                    })
                                    .catchError(new Operation<PromiseError>() {
                                        @Override
                                        public void apply(PromiseError promiseError) throws OperationException {
                                            notificationManager.notify(locale.unlinkProjectFailed() + " " + promiseError.getMessage(),
                                                                       FAIL,
                                                                       EMERGE_MODE);
                                        }
                                    });
            }
        }
    }
}
