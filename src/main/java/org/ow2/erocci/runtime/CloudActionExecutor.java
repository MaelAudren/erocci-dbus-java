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
import java.util.logging.Logger;

import org.ow2.erocci.model.exception.ExecuteActionException;
import org.occiware.clouddesigner.occi.AttributeState;
import org.occiware.clouddesigner.occi.Entity;
import org.occiware.clouddesigner.occi.Extension;
import org.occiware.clouddesigner.occi.cloud.CloudFactory;
import org.occiware.clouddesigner.occi.cloud.CloudPackage;
import org.occiware.clouddesigner.occi.cloud.Machine;
import org.occiware.clouddesigner.occi.cloud.Machine_OpenStack;
import org.occiware.clouddesigner.occi.cloud.connector.ExecutableCloudFactory;
import org.occiware.clouddesigner.occi.infrastructure.RestartMethod;
import org.occiware.clouddesigner.occi.infrastructure.StopMethod;
import org.occiware.clouddesigner.occi.infrastructure.SuspendMethod;

public class CloudActionExecutor extends AbstractActionExecutor implements IActionExecutor {

	private Logger logger = Logger.getLogger("Cloud executor");

    public final Map<String, Integer> entityTypeMap;

    private static final String EC2_TERM = "machine_Amazon_EC2";
    private static final String CLOUD_SIGMA_TERM = "machine_CloudSigma";
    private static final String GOGRID_TERM = "machine_Gogrid";
    private static final String HP_HELION_TERM = "machine_Hp_Helion";
    private static final String RACKSPACE_TERM = "machine_RackSpace";
    private static final String PROFITBRICKS_TERM = "machine_ProfitBricks";
    private static final String GCE_TERM = "machine_GCE";
    private static final String OPENSTACK_TERM = "machine_OpenStack";

    private static final String OPENSTACK_NAME = "name";
    private static final String OPENSTACK_FLAVOR_ID = "flavor_id";
    private static final String OPENSTACK_PROVIDER = "provider";
    private static final String OPENSTACK_TENANT = "tenant";
    private static final String OPENSTACK_USERNAME = "username";
    private static final String OPENSTACK_PASSWORD = "password";
    private static final String OPENSTACK_FLOATING_ID_POOL = "floating_ip_pool";
    private static final String OPENSTACK_SECURITY_GROUP = "security_group";
    private static final String OPENSTACK_KEYPAIR = "keypair";
    private static final String OPENSTACK_NETWORK_ID = "network_id";
    private static final String OPENSTACK_ENDPOINT = "endpoint";

    private static final String START = "http://schemas.ogf.org/occi/infrastructure/compute/action#start";
    private static final String STOP = "http://schemas.ogf.org/occi/infrastructure/compute/action#stop";
    private static final String RESTART = "http://schemas.ogf.org/occi/infrastructure/compute/action#restart";
    private static final String SUSPEND = "http://schemas.ogf.org/occi/infrastructure/compute/action#suspend";

    private Map<String,Machine> instances;

	private CloudActionExecutor() {
        super();
        instances = new HashMap<>();
        //ExecutableCloudFactory.init();
        logger.info("default conctructor");
        entityTypeMap = new HashMap<>();
    }
    
    public CloudActionExecutor(Extension extension) {
		super(extension);
        instances = new HashMap<>();
        logger.info("extension constructor");
        entityTypeMap = new HashMap<>();
	}

	@Override
	public void occiPostCreate(Entity entity) throws ExecuteActionException {

        logger.info("post create");
        ExecutableCloudFactory.init();
        logger.info("init works");
        CloudFactory factory = CloudPackage.eINSTANCE.getCloudFactory();
        logger.info("cloud package works");
        switch (entity.getKind().getTerm()) {
            case EC2_TERM:
                logger.info("EC2 is not implemented yet");
				break;
            case CLOUD_SIGMA_TERM:
                logger.info("sigma is not implemented yet");
                break;
            case GOGRID_TERM:
                logger.info("gogrid is not implemented yet");
                break;
            case HP_HELION_TERM:
                logger.info("helion is not implemented yet");
                break;
            case RACKSPACE_TERM:
                logger.info("rackspace is not implemented yet");
                break;
            case PROFITBRICKS_TERM:
                logger.info("profitbricks is not implemented yet");
                break;
            case GCE_TERM:
                logger.info("gce is not implemented yet");
                break;
            case OPENSTACK_TERM:
                try{
                    Machine_OpenStack machine = factory.eINSTANCE.createMachine_OpenStack();
                    logger.info("machine openstack created");
                    machine = setOpenstackMachineAttributes(machine,entity);
                    instances.put(entity.getId(),machine);
                    logger.info("machine attributes set");
                }catch(Exception e){
                    logger.info("EXCEPTION");
                    logger.info(e.toString());
                }
                break;
            default:
                logger.info("no matching term found");
        }
	}

	@Override
	public void occiPreDelete(Entity entity) throws ExecuteActionException {
		logger.info("pre delete");
        if(instances.keySet().contains(entity.getId())){
            instances.remove(entity.getId());
        }
        else{
            logger.info("The instance"+entity.getId()+"was not found");
        }
	}

	@Override
	public void occiPostUpdate(Entity entity) throws ExecuteActionException {
		logger.info("post update");
        if(instances.keySet().contains(entity.getId())){
            try{
                switch (entity.getKind().getTerm()) {
                    case EC2_TERM:
                        logger.info("EC2 is not implemented yet");
                        break;
                    case CLOUD_SIGMA_TERM:
                        logger.info("sigma is not implemented yet");
                        break;
                    case GOGRID_TERM:
                        logger.info("gogrid is not implemented yet");
                        break;
                    case HP_HELION_TERM:
                        logger.info("helion is not implemented yet");
                        break;
                    case RACKSPACE_TERM:
                        logger.info("rackspace is not implemented yet");
                        break;
                    case PROFITBRICKS_TERM:
                        logger.info("profitbricks is not implemented yet");
                        break;
                    case GCE_TERM:
                        logger.info("gce is not implemented yet");
                        break;
                    case OPENSTACK_TERM:
                            Machine_OpenStack machine = (Machine_OpenStack) instances.get(entity.getId());
                            Machine updated_machine = setOpenstackMachineAttributes(machine,entity);
                            instances.put(entity.getId(),updated_machine);
                            logger.info("machine attributes updated");
                        break;
                    default:
                        logger.info("no matching term found");
                }
            }catch(Exception e){
                logger.info("EXCEPTION");
                logger.info(e.toString());
            }
        }
        else{
            logger.info("The instance"+entity.getId()+"was not found");
        }
	}

	@Override
	public void occiMixinAdded(String mixinId) throws ExecuteActionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void occiMixinDeleted(String mixinId) throws ExecuteActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(String actionId, Entity entity, String fromMethod) throws ExecuteActionException {
        execute(actionId,new HashMap<String,String>(),entity,fromMethod);
	}

    /**
     *  Execute the action on a machine
     * @param actionId is the action to execute
     * @param actionAttributes is the attributes which describes the action
     * @param entity
     * @param fromMethod
     * @throws ExecuteActionException
     */
	@Override
	public void execute(String actionId, Map<String, String> actionAttributes, Entity entity, String fromMethod)
			throws ExecuteActionException {

        Machine machine = instances.get(entity.getId());
        logger.info("execute with attributes");

        logger.info("displaying of the action attributes");
        for(String s : actionAttributes.keySet()){
            logger.info("actionAttributes key"+s);
            logger.info("actionAttributes value"+actionAttributes.get(s));
        }
        logger.info("end of the actionattributes");

        String method = null;
        if(actionAttributes.keySet().contains("method")){
            method = actionAttributes.get(method);
        }

        try {

            switch (actionId) {
                case START:
                    logger.info("actionId = " + actionId);
                    try {
                        machine.start();
                        logger.info("machine started");
                    } catch (Exception e) {
                        logger.info("EXCEPTION");
                        logger.info(e.toString());
                    }
                    break;
                case STOP:
                    logger.info(STOP + "in development");
                    if (!(method == null || method.equals("undefined"))) {
                        machine.stop(getStopMethod(method));
                    }
                    break;
                case RESTART:
                    logger.info(RESTART + "in development");
                    if (!(method == null || method.equals("undefined"))) {
                        machine.restart(getRestartMethod(method));
                    }
                    break;
                case SUSPEND:
                    logger.info(SUSPEND + "in development");
                    if (!(method == null || method.equals("undefined"))) {
                        machine.suspend(getSuspendMethod(method));
                    }
                    break;
            }
        }catch(Exception e){
            logger.info("ACTION ON MACHINE TRHOW EXCEPTION :" + e.getMessage());
        }
	}


    /**
     * Give the right object according to method
     * @param method is the string from the user request
     * @return the object matching with method
     */
    private StopMethod getStopMethod(String method){
        if (method.equals(StopMethod.ACPIOFF.getLiteral())){
            return StopMethod.ACPIOFF;
        }else if(method.equals(StopMethod.GRACEFUL.getLiteral())){
            return StopMethod.GRACEFUL;
        }else if(method.equals(StopMethod.POWEROFF.getLiteral())){
            return  StopMethod.POWEROFF;
        }else{
            logger.info("unknown method for stop the machine, defautl behaviour set : graceful stop");
            return StopMethod.GRACEFUL;
        }
    }


    /**
     * Give the right object according to method
     * @param method is the string from the user request
     * @return the object matching with method
     */
    private RestartMethod getRestartMethod(String method){
        if (method.equals(RestartMethod.COLD.getLiteral())){
            return RestartMethod.COLD;
        }else if(method.equals(RestartMethod.GRACEFUL.getLiteral())){
            return RestartMethod.GRACEFUL;
        }else if(method.equals(RestartMethod.WARM.getLiteral())){
            return RestartMethod.WARM;
        }else{
            logger.info("unknown method for restart the machine, defautl behaviour set : graceful restart");
            return RestartMethod.GRACEFUL;
        }
    }

    /**
     * Give the right object according to method
     * @param method is the string from the user request
     * @return the object matching with method
     */
    private SuspendMethod getSuspendMethod(String method){
        if (method.equals(SuspendMethod.HIBERNATE.getLiteral())){
            return SuspendMethod.HIBERNATE;
        }else if(method.equals(SuspendMethod.SUSPEND.getLiteral())){
            return SuspendMethod.SUSPEND;
        }else{
            logger.info("unknown method for suspend the machine, defautl behaviour set : graceful suspend");
            return SuspendMethod.SUSPEND;
        }
    }

    /**
     *  Set the machine attributes accroding to the entity content
     * @param machine is the machine that will have his attributes set
     * @param entity is the entity that contains the data from the request
     * @return the machine with the set attributes
     */
    private Machine_OpenStack setOpenstackMachineAttributes(Machine_OpenStack machine, Entity entity){

        for(AttributeState attribute : entity.getAttributes() ){
            switch (attribute.getName()){
                case OPENSTACK_NAME :
                    machine.setName(attribute.getValue());
                    break;
                case OPENSTACK_FLAVOR_ID :
                    machine.setFlavor_id(attribute.getValue());
                    break;
                case OPENSTACK_PROVIDER :
                    machine.setProvider(attribute.getValue());
                    break;
                case OPENSTACK_TENANT :
                    machine.setTenant(attribute.getValue());
                    break;
                case OPENSTACK_USERNAME:
                    machine.setUsername(attribute.getValue());
                    break;
                case OPENSTACK_PASSWORD:
                    machine.setPassword(attribute.getValue());
                    break;
                case OPENSTACK_FLOATING_ID_POOL :
                    machine.setFloating_ip_pool(attribute.getValue());
                    break;
                case OPENSTACK_SECURITY_GROUP :
                    machine.setSecurity_group(attribute.getValue());
                    break;
                case OPENSTACK_NETWORK_ID :
                    machine.setSecurity_group(attribute.getValue());
                    break;
                case OPENSTACK_KEYPAIR :
                    machine.setKeypair(attribute.getValue());
                    break;
                case OPENSTACK_ENDPOINT :
                    machine.setEndpoint(attribute.getValue());
                    break;
            }
        }
        return machine;
    }


    public static IActionExecutor getInstance() {
        return CloudActionExecutorHolder.INSTANCE;
    }
    
    private static class CloudActionExecutorHolder {
        private final static CloudActionExecutor INSTANCE = new CloudActionExecutor();
    }

}
