/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.ide.ext.openshift.shared.dto;

import java.util.List;
import org.eclipse.che.dto.shared.DTO;

@DTO
public interface DockerBuildStrategy {
  boolean getForcePull();

  void setForcePull(boolean forcePull);

  DockerBuildStrategy withForcePull(boolean forcePull);

  boolean getNoCache();

  void setNoCache(boolean noCache);

  DockerBuildStrategy withNoCache(boolean noCache);

  ObjectReference getFrom();

  void setFrom(ObjectReference from);

  DockerBuildStrategy withFrom(ObjectReference from);

  List<EnvVar> getEnv();

  void setEnv(List<EnvVar> env);

  DockerBuildStrategy withEnv(List<EnvVar> env);

  LocalObjectReference getPullSecret();

  void setPullSecret(LocalObjectReference pullSecret);

  DockerBuildStrategy withPullSecret(LocalObjectReference pullSecret);
}
