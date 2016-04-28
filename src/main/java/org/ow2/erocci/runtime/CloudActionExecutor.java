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
    private static final String OPENSTACK_FLOATING_ID_POOL = "floating_id_pool";
    private static final String OPENSTACK_SECURITY_GROUP = "security_group";
    private static final String OPENSTACK_KEYPAIR = "keypair";
    private static final String OPENSTACK_NETWORK_ID = "network_id";
    private static final String OPENSTACK_ENDPOINT = "endpoint";

    //private final CloudFactory factory = CloudPackage.eINSTANCE.getCloudFactory();

	private CloudActionExecutor() {
        super();
        //ExecutableCloudFactory.init();
        logger.info("default conctructor");
        entityTypeMap = new HashMap<>();
    }
    
    public CloudActionExecutor(Extension extension) {
		super(extension);
        logger.info("extension constructor");
        entityTypeMap = new HashMap<>();
	}

    private Machine_OpenStack setOpenstackMachineAttributes(Entity entity){
        Machine_OpenStack machine = (Machine_OpenStack) entity;
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

	@Override
	public void occiPostCreate(Entity entity) throws ExecuteActionException {
		Machine machine = null;
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
                    logger.info("openstack deployment");
                    ExecutableCloudFactory.init();
                    logger.info("init works");
                    CloudFactory factory = CloudPackage.eINSTANCE.getCloudFactory();
                    logger.info("cloud package works");
                    Machine_OpenStack m = factory.eINSTANCE.createMachine_OpenStack();
                    logger.info("machine openstack created");
                    m.start();
                    logger.info("machine started");
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
		// TODO Auto-generated method stub
		logger.info("pre delete");
	}

	@Override
	public void occiPostUpdate(Entity entity) throws ExecuteActionException {
		// TODO Auto-generated method stub
		logger.info("post update");
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
		// TODO Auto-generated method stub
		logger.info("execute");
	}

	@Override
	public void execute(String actionId, Map<String, String> actionAttributes, Entity entity, String fromMethod)
			throws ExecuteActionException {
		// TODO Auto-generated method stub
		logger.info("execute with attributes");
	}

    
    public static IActionExecutor getInstance() {
        return CloudActionExecutorHolder.INSTANCE;
    }
    
    private static class CloudActionExecutorHolder {
        private final static CloudActionExecutor INSTANCE = new CloudActionExecutor();
    }

}
