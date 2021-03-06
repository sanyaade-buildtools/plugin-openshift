/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.openshift.shared.dto;

import org.eclipse.che.dto.shared.DTO;

@DTO
public interface DeploymentStrategy {
    RecreateDeploymentStrategyParams getRecreateParams();

    void setRecreateParams(RecreateDeploymentStrategyParams recreateParams);

    DeploymentStrategy withRecreateParams(RecreateDeploymentStrategyParams recreateParams);

    RollingDeploymentStrategyParams getRollingParams();

    void setRollingParams(RollingDeploymentStrategyParams rollingParams);

    DeploymentStrategy withRollingParams(RollingDeploymentStrategyParams rollingParams);

    CustomDeploymentStrategyParams getCustomParams();

    void setCustomParams(CustomDeploymentStrategyParams customParams);

    DeploymentStrategy withCustomParams(CustomDeploymentStrategyParams customParams);

    ResourceRequirements getResources();

    void setResources(ResourceRequirements resources);

    DeploymentStrategy withResources(ResourceRequirements resources);

    String getType();

    void setType(String type);

    DeploymentStrategy withType(String type);

}
