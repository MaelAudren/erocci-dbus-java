/**
 * Copyright (c) 2015-2017 Inria - Linagora
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ow2.erocci.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.occiware.clouddesigner.occi.Action;
import org.occiware.clouddesigner.occi.Configuration;
import org.occiware.clouddesigner.occi.Entity;
import org.occiware.clouddesigner.occi.Extension;
import org.occiware.clouddesigner.occi.docker.Container;
import org.occiware.clouddesigner.occi.docker.Machine;
import org.occiware.clouddesigner.occi.docker.connector.ExecutableDockerModel;
import org.occiware.clouddesigner.occi.infrastructure.RestartMethod;
import org.occiware.clouddesigner.occi.infrastructure.StopMethod;
import org.ow2.erocci.model.ConfigurationManager;
import org.ow2.erocci.model.exception.ExecuteActionException;

/**
 * This executor is specialized to DockerConnector.
 *
 * @author Christophe Gourdin - Inria
 *
 */
public class DockerActionExecutor extends AbstractActionExecutor implements IActionExecutor {

    
    
    private Logger logger = Logger.getLogger(this.getClass().getName()); 
    
    public static final Integer CONTAINER_TYPE = 1;
    public static final Integer CONTAINS_TYPE = 2; // a link.
    public static final Integer VOLUMES_FROM_TYPE = 3; // a link.
    public static final Integer EC2_TYPE = 4;
    public static final Integer MACHINE_TYPE = 5;
    public static final Integer DIGITAL_OCEAN_TYPE = 6;
    public static final Integer GOOGLE_COMPUTE_TYPE = 7;
    public static final Integer AZURE_TYPE = 8;
    public static final Integer HYPER_V_TYPE = 9;
    public static final Integer OPENSTACK_TYPE = 10;
    public static final Integer RACKSPACE_TYPE = 11;
    public static final Integer VIRTUALBOX_TYPE = 12;
    public static final Integer VMWARE_FUSION_TYPE = 13;
    public static final Integer VMWARE_CLOUD_AIR_TYPE = 14;
    public static final Integer VMWARE_VSPHERE_TYPE = 15;

    public static final String CONTAINER_NAME = "Container";
    public static final String CONTAINS_NAME = "Contains"; // a link.
    public static final String VOLUMES_FROM_NAME = "Volumesfrom"; // a link.
    public static final String EC2_NAME = "Machine_Amazon_EC2";
    public static final String MACHINE_NAME = "Machine";
    public static final String DIGITAL_OCEAN_NAME = "Machine_Digital_Ocean";
    public static final String GOOGLE_COMPUTE_NAME = "Machine_Google_Compute_Engine";
    public static final String AZURE_NAME = "Machine_Microsoft_Azure";
    public static final String HYPER_V_NAME = "Machine_Microsoft_Hyper_V";
    public static final String OPENSTACK_NAME = "Machine_OpenStack";
    public static final String RACKSPACE_NAME = "Machine_Rackspace";
    public static final String VIRTUALBOX_NAME = "Machine_VirtualBox";
    public static final String VMWARE_FUSION_NAME = "Machine_VMware_Fusion";
    public static final String VMWARE_CLOUD_AIR_NAME = "Machine_VMware_vCloud_Air";
    public static final String VMWARE_VSPHERE_NAME = "Machine_VMware_vSphere";

    public final Map<String, Integer> entityTypeMap;

    public DockerActionExecutor() {
        entityTypeMap = new HashMap<String, Integer>();
        entityTypeMap.put(CONTAINER_NAME, CONTAINER_TYPE);
        entityTypeMap.put(CONTAINS_NAME, CONTAINS_TYPE);
        entityTypeMap.put(VOLUMES_FROM_NAME, VOLUMES_FROM_TYPE);
        entityTypeMap.put(EC2_NAME, EC2_TYPE);
        entityTypeMap.put(MACHINE_NAME, MACHINE_TYPE);
        entityTypeMap.put(DIGITAL_OCEAN_NAME, DIGITAL_OCEAN_TYPE);
        entityTypeMap.put(GOOGLE_COMPUTE_NAME, GOOGLE_COMPUTE_TYPE);
        entityTypeMap.put(AZURE_NAME, AZURE_TYPE);
        entityTypeMap.put(HYPER_V_NAME, HYPER_V_TYPE);
        entityTypeMap.put(OPENSTACK_NAME, OPENSTACK_TYPE);
        entityTypeMap.put(RACKSPACE_NAME, RACKSPACE_TYPE);
        entityTypeMap.put(VIRTUALBOX_NAME, VIRTUALBOX_TYPE);
        entityTypeMap.put(VMWARE_FUSION_NAME, VMWARE_FUSION_TYPE);
        entityTypeMap.put(VMWARE_CLOUD_AIR_NAME, VMWARE_CLOUD_AIR_TYPE);
        entityTypeMap.put(VMWARE_VSPHERE_NAME, VMWARE_VSPHERE_TYPE);
    }

    public DockerActionExecutor(Extension extension) {
        super(extension);
        entityTypeMap = new HashMap<String, Integer>();
        entityTypeMap.put(CONTAINER_NAME, CONTAINER_TYPE);
        entityTypeMap.put(CONTAINS_NAME, CONTAINS_TYPE);
        entityTypeMap.put(VOLUMES_FROM_NAME, VOLUMES_FROM_TYPE);
        entityTypeMap.put(EC2_NAME, EC2_TYPE);
        entityTypeMap.put(MACHINE_NAME, MACHINE_TYPE);
        entityTypeMap.put(DIGITAL_OCEAN_NAME, DIGITAL_OCEAN_TYPE);
        entityTypeMap.put(GOOGLE_COMPUTE_NAME, GOOGLE_COMPUTE_TYPE);
        entityTypeMap.put(AZURE_NAME, AZURE_TYPE);
        entityTypeMap.put(HYPER_V_NAME, HYPER_V_TYPE);
        entityTypeMap.put(OPENSTACK_NAME, OPENSTACK_TYPE);
        entityTypeMap.put(RACKSPACE_NAME, RACKSPACE_TYPE);
        entityTypeMap.put(VIRTUALBOX_NAME, VIRTUALBOX_TYPE);
        entityTypeMap.put(VMWARE_FUSION_NAME, VMWARE_FUSION_TYPE);
        entityTypeMap.put(VMWARE_CLOUD_AIR_NAME, VMWARE_CLOUD_AIR_TYPE);
        entityTypeMap.put(VMWARE_VSPHERE_NAME, VMWARE_VSPHERE_TYPE);
    }

    @Override
    public void occiPostCreate(Entity entity) throws ExecuteActionException {
        if (entity instanceof Container) {
            execute("pullImage", entity, FROM_CREATE);
        } else {
            logger.info("No actions to execute on SaveResource() postCreate().");
        }
        

    }

    @Override
    public void occiPreDelete(Entity entity) throws ExecuteActionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void occiPostUpdate(Entity entity) throws ExecuteActionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void occiMixinAdded(Entity entity, String mixinId) throws ExecuteActionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void occiMixinDeleted(Entity entity, String mixinId) throws ExecuteActionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(String actionId, Entity entity, String fromMethod) throws ExecuteActionException {
        execute(actionId, new HashMap<>(), entity, fromMethod);

    }

    /**
     * Execute a docker action.
     *
     * @param actionId
     * @param actionAttributes
     * @param entity
     * @param fromMethod
     * @throws org.ow2.erocci.model.exception.ExecuteActionException
     */
    @Override
    public void execute(String actionId, Map<String, String> actionAttributes, Entity entity, String fromMethod)
            throws ExecuteActionException {

        if (fromMethod.equals(FROM_ACTION)) {
            // Called from ActionImpl interface DBUS Object.
            if (actionId == null) {
                throw new ExecuteActionException("You must provide an action kind for entity : " + entity.getId());
            }
        }

        if (entity == null) {
            throw new ExecuteActionException("You must provide an entity to execute this action : " + actionId);
        }

        // Get the concrete entity object.
        String className = entity.getClass().getSimpleName();
        Integer entityType = entityTypeMap.get(className);
        if (entityType == null) {
            throw new ExecuteActionException(
                    "Entity type : " + className + " is not supported by this backend for now");
        }

        // Find which method to execute, following the final type of entity object.
        switch (fromMethod) {
            case FROM_CREATE:
                if (actionId.equals("pullImage")) {
                    // Pull the image of a container.
                    // TODO : Pullimage if container has machine ?;
                }
                break;

            case FROM_UPDATE:
                // TODO: Command execute when updating attributes.

                break;
            case FROM_DELETE:
                // TODO : Delete a container or a machines with all his containers.
                switch (className) {
                    case CONTAINER_NAME:
                        // Delete the container on his machine.
                        // A refactorer, invoquer synchronize (cf clouddesigner docker.design).
                        
                        // destroyContainer((Container) entity);
                        break;
                    case CONTAINS_NAME:
                        // remove linked container.
                        // Contains contains = (Contains) entity;
                        // destroyContainer((Container) contains.getTarget());

                        break;
                    case DIGITAL_OCEAN_NAME:
                    case GOOGLE_COMPUTE_NAME:
                    case EC2_NAME:
                    case HYPER_V_NAME:
                    case MACHINE_NAME:
                    case OPENSTACK_NAME:
                    case RACKSPACE_NAME:
                    case VIRTUALBOX_NAME:
                    case VMWARE_CLOUD_AIR_NAME:
                    case VMWARE_FUSION_NAME:
                    case VMWARE_VSPHERE_NAME:
                        
                        // Remove the dockerMachine and all his containers if any.
                        // A refactorer avec synchronize cf clouddesigner
                        // entity.state = ComputeStatus.destroy ?
                        // destroyMachine((Machine) entity);
                        break;

                }

                break;
            case FROM_USER_MIXIN_ADDED:

                break;
            case FROM_USER_MIXIN_DELETED:

                break;
            case FROM_ACTION:
                // Ref: Clouddesigner ==> package docker.design.services.DockerServices
                
                // Check if we have special actions like "synchronize" or "import".
                if (actionId.contains("import")) {

                    Set<String> owners = ConfigurationManager.getAllOwner();
                    for (String owner : owners) {
                        this.importModel(ConfigurationManager.getConfigurationForOwner(owner));
                    }
                    return;
                }
                if (actionId.contains("synchronize")) {
                    this.synchronize(entity);
                    return;
                }
                if (actionId.contains("startAll")) {
                    this.startAll(entity);
                    return;
                }
                
                // Other cases.
                // Get the action term from kind object.
                Action actionKind = ConfigurationManager.getActionKindFromExtension(extension, actionId);
                if (actionKind == null) {

                    throw new ExecuteActionException(
                            "Action : " + actionId + " doesnt exist on extension : " + extension.getName());
                }
                switch (actionKind.getTerm()) {
                    case "start":
                        this.start(entity);
                        break;
                    case "stop":
                        this.stop(entity);
                        break;
                    case "pause":
                        this.pause(entity);
                        break;
                    case "unpause":
                        this.unpause(entity);
                        break;
                    case "kill":
                        this.kill(entity);
                        break;
                    case "create":
                        this.create(entity);
                        break;
                    default:
                        throw new ExecuteActionException("the action : " + actionId + " is not supported at this time, please report it on our github.");
                }

                break;

        }

    }

    /**
     * Start a machine or a container.
     *
     * @param eo
     */
    private void start(final EObject eo) throws ExecuteActionException {
        try {
            if (eo instanceof Machine) {
                if ((eo instanceof Machine)) {
                    Machine machine = ((Machine) eo);
                    final ExecutableDockerModel machineExec = new ExecutableDockerModel(machine);
                    machineExec.start();

                } else if ((eo instanceof Container)) {
                    Container container = ((Container) eo);
                    final ExecutableDockerModel containerExec = new ExecutableDockerModel(container);
                    containerExec.container.start();
                }
            }
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }

    /**
     * Pause a container.
     *
     * @param eo
     * @throws ExecuteActionException
     */
    private void pause(final EObject eo) throws ExecuteActionException {
        try {
            if (eo instanceof Container) {
                Container container = ((Container) eo);
                final ExecutableDockerModel containerExec = new ExecutableDockerModel(container);
                containerExec.container.pause();
            }
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }
    
    /**
     * Unpause a container.
     * @param eo
     * @throws ExecuteActionException 
     */
    private void unpause(final EObject eo) throws ExecuteActionException {
        try {
            if (eo instanceof Container) {
                Container container = ((Container) eo);
                final ExecutableDockerModel containerExec = new ExecutableDockerModel(container);
                containerExec.container.unpause();
            }
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }
    /**
     * Create a container.
     * @param eo
     * @throws ExecuteActionException 
     */
    private void create(final EObject eo) throws ExecuteActionException {
        try {
            if (eo instanceof Container) {
                Container container = ((Container) eo);
                final ExecutableDockerModel containerExec = new ExecutableDockerModel(container);
                containerExec.container.create();
            }
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }
    /**
     * Kill a container.
     * @param eo
     * @throws ExecuteActionException 
     */
    private void kill(final EObject eo) throws ExecuteActionException {
        try {
            if (eo instanceof Container) {
                Container container = ((Container) eo);
                final ExecutableDockerModel containerExec = new ExecutableDockerModel(container);
                containerExec.container.kill("KILL");
            }
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }
    

    /**
     * Start all, this applies to all children links. Like a machine with all
     * his containers, with good orders.
     *
     * @param eo
     * @throws ExecuteActionException
     */
    private void startAll(final EObject eo) throws ExecuteActionException {
        if ((eo instanceof Machine)) {
            Machine machine = ((Machine) eo);
            final ExecutableDockerModel main = new ExecutableDockerModel(machine);
            main.restart();
        } else if ((eo instanceof Container)) {
            Container container = ((Container) eo);
            final ExecutableDockerModel main_1 = new ExecutableDockerModel(container);
            main_1.container.restart(RestartMethod.GRACEFUL);
        }
    }

    /**
     * Stop a machine or a container.
     *
     * @param eo
     * @throws ExecuteActionException
     */
    private void stop(final EObject eo) throws ExecuteActionException {
        if ((eo instanceof Machine)) {
            Machine machine = ((Machine) eo);
            final ExecutableDockerModel main = new ExecutableDockerModel(machine);
            main.stop();
        } else if ((eo instanceof Container)) {
            Container container = ((Container) eo);
            final ExecutableDockerModel main_1 = new ExecutableDockerModel(container);
            main_1.container.stop(StopMethod.GRACEFUL);
        }
    }

    /**
     * Restart a container or a machine.
     *
     * @param eo
     * @throws ExecuteActionException
     */
    private void restart(final EObject eo) throws ExecuteActionException {
        try {
            if ((eo instanceof Machine)) {
                Machine machine = ((Machine) eo);
                final ExecutableDockerModel main = new ExecutableDockerModel(machine);
                main.startAll();
            }
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }

    /**
     * Import the configuration model from real.
     *
     * @param conf
     * @throws ExecuteActionException
     */
    private void importModel(final Configuration conf) throws ExecuteActionException {
        try {
            final ExecutableDockerModel main = new ExecutableDockerModel(conf);
            main.importModel();
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }

    /**
     * Synchronize the model with real.
     *
     * @param eo
     * @throws ExecuteActionException
     */
    private void synchronize(final EObject eo) throws ExecuteActionException {
        try {
            if ((eo instanceof Machine)) {
                Machine machine = ((Machine) eo);
                final ExecutableDockerModel main = new ExecutableDockerModel(machine);
                main.synchronize();
            }
        } catch (Exception ex) {
            throw new ExecuteActionException(ex);
        }
    }

    public static IActionExecutor getInstance() {
        return DockerActionExecutorHolder.INSTANCE;
    }

    private static class DockerActionExecutorHolder {

        private final static DockerActionExecutor INSTANCE = new DockerActionExecutor();
    }

}
