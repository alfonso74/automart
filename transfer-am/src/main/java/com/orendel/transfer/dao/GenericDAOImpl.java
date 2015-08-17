package com.orendel.transfer.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.orendel.transfer.config.Datasource;
import com.orendel.transfer.config.MultiDbSessionManager;


public class GenericDAOImpl<T, ID extends Serializable> 
	implements IGenericDAO<T, ID> {
	
	private Class<T> persistentClass;
	private Session session;
	protected Datasource dataSource = Datasource.COUNTERPOINT;
	
	@SuppressWarnings("unchecked")
	public GenericDAOImpl() {
		persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public void setSession(Session s) {
		session = s;
	}
	
	protected Session getSession() {
		if (session == null || !session.isOpen()) {
			session = MultiDbSessionManager.getSessionForDatasource(dataSource);
		}
		return session;
	}
	
	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	/**
	 * Con el ... es opcional poner el argumento, nice!!
	 * @param criterion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		getSession().beginTransaction();
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		List<T> listado = crit.list();
		getSession().getTransaction().commit();
		return listado;
	}
	
	/**
	 * Retorna resultados ordenados de acuerdo al campo y al orden indicado
	 * @param field Campo usado para ordenar
	 * @param ascending Dirección de ordenamiento (true: ascendente)
	 * @param criterion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByOrderedCriteria(String field, boolean ascending, Criterion... criterion) {
		getSession().beginTransaction();
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		if (ascending) {
			crit.addOrder(Order.asc(field));
		} else {
			crit.addOrder(Order.desc(field));
		}
		List<T> listado = crit.list();
		getSession().getTransaction().commit();
		return listado;
	}
	
	
	public List<T> findByField(String nombreCampo, Long valor) {
		//Criterion criterion = Restrictions.like(nombreCampo, valor);
		Criterion criterion = Restrictions.eq(nombreCampo, valor);
		List<T> resultados = findByCriteria(criterion); 
		return resultados;
	}
	
	public List<T> findByField(String nombreCampo, boolean valor) {
		Criterion criterion = Restrictions.eq(nombreCampo, valor);
		List<T> resultados = findByCriteria(criterion); 
		return resultados;
	}
	
	public List<T> findByField(String nombreCampo, String valor) {
		Criterion criterion = Restrictions.eq(nombreCampo, valor);
		List<T> resultados = findByCriteria(criterion); 
		return resultados;
	}
	
	public List<T> findByField(String nombreCampo, Date valor) {
		Criterion criterion = Restrictions.eq(nombreCampo, valor);
		List<T> resultados = findByCriteria(criterion); 
		return resultados;
	}
	
	
// **************** Métodos heredados de la interface *****************
	

	public List<T> findAll() {
		return findByCriteria();
	}
	
	/**
	 * Retorna una lista ordenada con los registros encontrados
	 * @param field Campo a utilizar para el ordenamiento
	 * @param ascending realiza un ordenamiento ascendente (true), si es false es descendente.
	 * @return Lista ordenada de acuerdo al campo ascending
	 */
	public List<T> findAllOrdered(String field, boolean ascending) {
		return findByOrderedCriteria(field, ascending);
	}
	
	@SuppressWarnings("unchecked")
	public T findById(ID id, boolean lock) {
		T entity;
		getSession().beginTransaction();
		if (lock) {
			entity = (T) getSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
		} else {
			entity = (T) getSession().load(getPersistentClass(), id);
		}
		getSession().getTransaction().commit();
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public T findById2(ID id, boolean lock) {
		T entity;
		getSession().beginTransaction();
		if (lock) {
			entity = (T) getSession().get(getPersistentClass(), id, LockOptions.UPGRADE);
		} else {
			entity = (T) getSession().get(getPersistentClass(), id);
		}
		getSession().getTransaction().commit();
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		getSession().beginTransaction();
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		List<T> listado = crit.list();
		getSession().getTransaction().commit();
		return listado;
	}
	
	/**
	 * Ejecuta un query sencillo de Hibernate y retorna un List con objetos del mismo
	 * tipo de donde se hace el llamado.
	 * @param queryHQL Query en texto:  "select * from rcp.assets.model.Caso"
	 * @return List con el resultado
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> executeHQL(String queryHQL) {
		getSession().beginTransaction();
		Query query = getSession().createQuery(queryHQL);
		List<T> listado = query.list();
		getSession().getTransaction().commit();
		return listado;
	}
	
	/**
	 * Ejecuta un Query de hibernate.  El query ya debe tener los parámetros asignados y estar
	 * listo para la ejecución.
	 * @param query Query de hibernate (org.hibernate.Query)
	 * @return List con el resultado, si no encuentra nada el resultado incluye 1 elemento null (verificar con .get(0))
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> executeQuery(Query query) {
		getSession().beginTransaction();
		List<T> listado = query.list();
		getSession().getTransaction().commit();
		return listado;
	}

	public T makePersistent(T entity) {
		//getSession().beginTransaction();
		getSession().saveOrUpdate(entity);
		//getSession().getTransaction().commit();
		return entity;
	}

	public void makeTransient(T entity) {
		getSession().delete(entity);
	}
	
	/**
	 * Inicia una transacción, llama a makePersistent(entity), flush() y commit()
	 * @param entity Entidad que se quiere guardar/actualizar en la base de datos
	 */
	public void doSave(T entity) {
		getSession().beginTransaction();
		makePersistent(entity);
		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	public void doDelete(T entity) {
		getSession().beginTransaction();
		makeTransient(entity);
		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	
	public void flush() {
		getSession().flush();
	}
	
	public void clear() {
		getSession().clear();
	}

}
