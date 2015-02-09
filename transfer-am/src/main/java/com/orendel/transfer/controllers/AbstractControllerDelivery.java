package com.orendel.transfer.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.orendel.transfer.dao.GenericDAOImpl;
import com.orendel.transfer.services.HibernateUtilDelivery;


public abstract class AbstractControllerDelivery<X> {
	
	protected static Logger LOGGER = null;
	private Session session;
	private String editorId;
	private GenericDAOImpl<X, Long> dao;

	/**
	 * Inicializa una sesión de hibernate en base al editor indicado
	 * @param id que se asociará a la nueva sesión
	 */
	public AbstractControllerDelivery(String editorId, GenericDAOImpl<X, Long> dao) {
		LOGGER = Logger.getLogger(getClass());
		session = HibernateUtilDelivery.getEditorSession(editorId);
		session.setFlushMode(FlushMode.MANUAL);
		this.editorId = editorId;
		this.dao = dao;
		this.dao.setSession(session);
	}

	public AbstractControllerDelivery(GenericDAOImpl<X, Long> dao) {
		LOGGER = Logger.getLogger(getClass());
		session = HibernateUtilDelivery.getSessionFactorySQL().getCurrentSession();
//		session.beginTransaction();
		this.dao = dao;
		this.dao.setSession(session);
	}

	/*
	public AbstractController() {
		session = HibernateUtilDelivery.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		this.dao = new GenericDAOImpl<X, Long>() {
		};
		this.dao.setSession(session);
	}
	 */

	/**
	 * Persiste un registro en la base de datos
	 * @param registro registro a ser guardado, instancia tipo X
	 */
	public void doSave(X registro) {
		try {
			dao.doSave(registro);
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtilDelivery.rollback(getSession().getTransaction());
				HibernateUtilDelivery.closeEditorSession(getEditorId());
			}
			HibernateUtilDelivery.procesarError(he);
			session = HibernateUtilDelivery.getEditorSession(editorId);
			session.setFlushMode(FlushMode.MANUAL);
			dao.setSession(session);
			HibernateUtilDelivery.verSesiones();
		}
	}


	public void doDelete(X registro) {
		try {
			dao.doDelete(registro);
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtilDelivery.rollback(getSession().getTransaction());
				HibernateUtilDelivery.closeEditorSession(getEditorId());
			}
			HibernateUtilDelivery.procesarError(he);
			session = HibernateUtilDelivery.getEditorSession(editorId);
			session.setFlushMode(FlushMode.MANUAL);
			dao.setSession(session);
			HibernateUtilDelivery.verSesiones();
		}
	}


	/**
	 * Finaliza una sesi�n de hibernate
	 * @param editorId id asociado a la sesi�n que ser� finalizada
	 */
	public void finalizarSesion() {
//		System.out.println("Finalizando sesi�n: " + getEditorId());
		LOGGER.info("Finalizando sesi�n: " + getEditorId());
		HibernateUtilDelivery.closeEditorSession(getEditorId());     // graba en la base de datos
	}

	/**
	 * Retorna el editor que est� asociado a este controller
	 * @return id del editor asociado
	 */
	public String getEditorId() {
		return editorId;
	}

	/**
	 * Retorna la sesi�n que ha sida creada para este editor
	 * @return sesi�n de hibernate
	 */
	public Session getSession() {
		return session;
	}

	public GenericDAOImpl<X, Long> getDAO() {
		return dao;
	}

	/**
	 * Obtiene un listado de todos los registros en la base de datos de tipo especificado.
	 * @return
	 */
	public List<X> getListado() {
		return getDAO().findAll();
	}


	/**
	 * Obtiene un registro en base a su identificador en la base de datos.  Este m�todo se utiliza frecuentemente en
	 * la inicializaci�n de los editores.
	 * @param id identificador del registro en la DB
	 * @return registro de tipo X
	 */
	public X getRegistroById(Long id) {
		X result = null;
		try {
			result = getDAO().findById(id, true);
		} catch (Exception e) {
			if (getSession().isOpen()) {
				HibernateUtilDelivery.rollback(getSession().getTransaction());
				if (editorId != null) {
					HibernateUtilDelivery.closeEditorSession(getEditorId());
				}
			}
			e.printStackTrace();
		}
		
		return result;
	}


	/**
	 * Permite ver las sesiones activas de Hibernate
	 */
	public void verSesiones() {
		HibernateUtilDelivery.verSesiones();
	}

}
