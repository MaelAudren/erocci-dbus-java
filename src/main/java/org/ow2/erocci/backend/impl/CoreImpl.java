/**
 * Copyright (c) 2015-2017 Linagora
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

package org.ow2.erocci.backend.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.freedesktop.DBus;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.occiware.clouddesigner.occi.Configuration;
import org.ow2.erocci.backend.Pair;
import org.ow2.erocci.backend.Quad;
import org.ow2.erocci.backend.Struct1;
import org.ow2.erocci.backend.Struct2;
import org.ow2.erocci.backend.core;
import org.ow2.erocci.model.Entity;
import org.ow2.erocci.model.EntityFactory;
import org.ow2.erocci.model.OcciConstants;

/**
 * Implementation of OCCI core.
 * @author Pierre-Yves Gibello - Linagora
 *
 */
public class CoreImpl implements core, DBus.Properties {

	public static byte NODE_ENTITY = 0;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private String schema;
	
	private Map<String, EntityFactory> factories = new HashMap<String, EntityFactory>();
	private EntityFactory defaultEntityFactory;
	private Map<String, Entity> entities = new HashMap<String, Entity>();
	private Map<String, List<Entity>> categoryIdToEntity = new HashMap<String, List<Entity>>();
	private Map<String, List<Struct2>> currentListRequests = new HashMap<String, List<Struct2>>();

	
	/**
	 * Default constructor
	 */
	public CoreImpl() {
		// Try to pick default schema (ignore error).
		setSchema(this.getClass().getResourceAsStream("/schema.xml"));
	}

	/**
	 * Register a default entity factory, to be used if no more relevant one is found.
	 * @param factory The default factory
	 */
	public void setDefaultEntityFactory(EntityFactory factory) {
		this.defaultEntityFactory = factory;
	}

	/**
	 * Register an OCCI entity factory, by entity category (OCCI kind).
	 * @param kind The entity category name (OCCI kind = scheme#term)
	 * @param factory The entity factory, to create entities of specified category
	 */
	public void addEntityFactory(String kind, EntityFactory factory) {
		factories.put(kind, factory);
	}

	/**
	 * Set OCCI schema
	 * @param in InputStream to read schema from (will be closed at the end of this call)
	 */
	public void setSchema(InputStream in) {
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			Utils.copyStream(in, os);
			this.schema = os.toString("UTF-8");
		} catch(IOException e) {
			Utils.closeQuietly(in);
			Utils.closeQuietly(os);
			schema = null;
		}
	}
	
	@Override
	public <A> A Get(String interfaceName, String property) {
		if("schema".equalsIgnoreCase(property)) {
			return (A)this.schema;
		} else {
			return null;
		}
	}

	@Override
	public Map<String,Variant> GetAll(String interface_name) {
		logger.info("GetAll invoked");
		return null;
	}
	
	@Override
	public <A> void Set(String arg0, String arg1, A arg2) {
		logger.info("set method invoked with arg0 : " + arg0);
		logger.info("set method invoked with arg1 : " + arg1);
		logger.info("set method invoked with arg2 : " + arg2);
		
	}

	@Override
	public boolean isRemote() {
		logger.info("is remote invoked");
		return false;
	}

	@Override
	public void Init(Map<String, Variant> opts) {
		logger.info("Init method invoked");
		
		
	}

	@Override
	public void Terminate() {
		logger.info("Terminate method invoked");
	}

	/**
	 * Record version 1 of a resource (overwrite mode: use Update instead).
	 * If resources already exist with the same id, they will be lost.
	 * @param id Resource id, possibly generated by Erocci
	 * @param kind Category id
	 * @param mixins List of category ids
	 * @param attributes Resource attributes
	 * @param owner
	 * @return resource path relative part.
	 */
	@Override
	public String SaveResource(String id, String kind,
			java.util.List<String> mixins, Map<String, Variant> attributes,
			String owner) {

		logger.info("SaveResource invoked with id=" + id + ", kind=" + kind + ", mixins=" + mixins + ", attributes=" + Utils.convertVariantMap(attributes));

		EntityFactory factory = this.factories.get(kind);
		if(factory == null) {
			logger.fine("No factory found for kind " + kind + ": trying default");
			factory = this.defaultEntityFactory;
		}
		
		if(factory != null) {
		
			Entity entity = factory.newEntity(id, OcciConstants.TYPE_RESOURCE,
					kind, mixins, Utils.convertVariantMap(attributes), owner, 1);

			// 1st version of a resource, with serial no = 1
			entities.put(id, entity);

			entity.occiPostCreate();

			List<Entity> entities = categoryIdToEntity.get(kind);
			if(entities == null) {
				entities = new LinkedList<Entity>();
				categoryIdToEntity.put(kind, entities);
			}
			entities.add(entity);

			return id;
		} else {
			logger.warning("SaveResource: unknown kind, no factory found and no default provided");
			return null;
		}
	}

	/**
	 * Record version 1 of a link (overwrite mode: use Update instead).
	 * If links already exist with the same id, they will be lost.
	 * @param id
	 * @param kind
	 * @param mixins
	 * @param src
	 * @param target
	 * @param attributes
	 * @param owner
	 * @return
	 */
	@Override
	public String SaveLink(String id, String kind,
			java.util.List<String> mixins, String src, String target,
			Map<String, Variant> attributes, String owner) {
		
		logger.info("SaveLink invoked");

		// 1st version of a link, with serial no = 1
		Entity link = new Entity(id, OcciConstants.TYPE_LINK,
				kind, mixins, Utils.convertVariantMap(attributes), owner, 1);
		Entity source = entities.get(src);
		link.setSource(source);
		link.setTarget(entities.get(target));

		source.getLinks().add(link);
		entities.put(id, link);
		
		link.occiPostCreate();

		return id;
	}

	/**
	 * Update an entity
	 * @param id
	 * @param attributes
	 */
	@Override
	public Map<String, Variant> Update(String id,
			Map<String, Variant> attributes) {
		logger.info("Update invoked");
		
		Entity entity = entities.get(id);
		if(entity != null) {
			Map<String, String> attrs = Utils.convertVariantMap(attributes);
			entity.updateAttributes(attrs);
			entity.occiPostUpdate(attrs);
		}
		
		return attributes;
	}

	@Override
	public void SaveMixin(String id, java.util.List<String> entities) {
		logger.info("SaveMixin invoked");
		for(String entityId : entities) {
			Entity entity = this.entities.get(entityId);
			if(! entity.getMixins().contains(id)) entity.getMixins().add(id);
			entity.incrementSerial();
			
			entity.occiMixinAdded(id);
		}
	}

	@Override
	public void UpdateMixin(String id, java.util.List<String> entities) {
		logger.info("UpdateMixin invoked");
		// TODO Why should it be different from SaveMixin ??
		SaveMixin(id, entities);
	}

	@Override
	//TODO unclear why this one returns a list of Struct...
	public java.util.List<Struct1> Find(String id) {
		logger.info("Find invoked with id=" + id);

		List<Struct1> ret = new LinkedList<Struct1>();
		Entity entity = entities.get(id);
		if(entity != null) {
			ret.add(new Struct1(CoreImpl.NODE_ENTITY,
					new Variant<String>(id), entity.getOwner(), new UInt32(entity.getSerial())));
		}

		return ret;
	}

	@Override
	public Quad<String, String, java.util.List<String>, Map<String, Variant>> Load(
			Variant opaque_id) {
		logger.info("Load invoked with opaque_id=" + opaque_id);
		// TODO What is opaque_id ??
		Entity entity = entities.get(opaque_id);
		
		// TODO : Attention ici, le dernier attribut peut poser des problèmes lors du lancement d'un delete resource.
		return new Quad(entity.getId(),
				entity.getKind(), entity.getMixins(), entity.getAttributes());
	}

	@Override
	/**
	 * Get an iterator for a collection: then use Next() to iterate (the List call
	 * initiates an iterator for subsequent Next() calls).
	 * @param id Category id or path relative url part
	 * @param filters
	 * @return Pair, where Variant<String> is the iterator ID for Next() method, and
	 * UInt32 the iterator's serial num (eg. for caching).
	 */
	public Pair<Variant, UInt32> List(String id, Map<String, Variant> filters) {
		logger.info("List invoked with id=" + id + " and filters=" + filters);
		// TODO Auto-generated method stub
		
		String collectionName = "collection" + Utils.getUniqueInt();
		currentListRequests.put(collectionName, listItems(id, filters));

		return new Pair<Variant, UInt32>(
				new Variant<String>(collectionName),
				new UInt32(Utils.getUniqueInt()));
	}

	/**
	 * Retrieve items of a collection.
	 * @param opaque_id The collection ID
	 * @param start the first item index (start with 0)
	 * @param items the number of items (0 for infinite)
	 * @return A list of entities, as Struct2 containing the path relative url part + owner.
	 */
	@Override
	public java.util.List<Struct2> Next(Variant opaque_id, UInt32 start,
			UInt32 items) {
		logger.info("Next invoked with opaque_id=" + opaque_id +", start=" + start + ", items=" + items);
		List<Struct2> fullList = currentListRequests.remove(opaque_id.getValue());
		if(fullList != null) {
			int toIndex = items.intValue();
			if(toIndex <= 0) toIndex = fullList.size();
			return fullList.subList(start.intValue(), toIndex);
		}
		return new LinkedList<Struct2>(); // Empty list
	}

	/**
	 * Supprime la (les) resources du backend (pas du provider).
	 * 
	 * @param id 
	 */
	@Override
	public void Delete(String id) {
		
		logger.info("Delete invoked");

		Entity removed = entities.remove(id);
		
		// Remove all links that point to removed entity
		for (Entity e : removed.getLinkedFrom()) {
			Delete(e.getId()); // Warning recursive call
		}
		
		removed.occiPreDelete();

		// Remove reference to entity by category ID, if any
		List<Entity> entities = categoryIdToEntity.get(removed.getKind());
		entities.remove(removed);
		if(entities.isEmpty()) categoryIdToEntity.remove(removed.getKind());
	}
	
	/**
	 * @return A list of entities, as Struct2 containing the path relative url part + owner.
	 * */
	private List<Struct2> listItems(String id, Map<String, Variant> filters) {
		// TODO add filter support...
		List<Struct2> ret = new LinkedList<Struct2>();
		List<Entity> entities = categoryIdToEntity.get(id);
		if(entities != null) { // id is supposed to be a category ID...
			for(Entity entity : entities) {
				ret.add(new Struct2(entity.getId(), entity.getOwner()));
			}
		} else { // id may be a path relative url part ?
			Entity entity = this.entities.get(id);
			if(entity != null) ret.add(new Struct2(entity.getId(), entity.getOwner()));
		}
		return ret;
	}
}
