package com.orendel.locator.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.orendel.locator.dao.GenericDAOImpl;
import com.orendel.locator.services.HibernateUtil;


public abstract class AbstractController<X> {
	
	protected static Logger LOGGER = null;
	private Session session;
	private String editorId;
	private GenericDAOImpl<X, Long> dao;

	/**
	 * Inicializa una sesión de hibernate en base al editor indicado
	 * @param id que se asociará a la nueva sesión
	 */
	public AbstractController(String editorId, GenericDAOImpl<X, Long> dao) {
		LOGGER = Logger.getLogger(getClass());
		session = HibernateUtil.getEditorSession(editorId);
		session.setFlushMode(FlushMode.MANUAL);
		this.editorId = editorId;
		this.dao = dao;
		this.dao.setSession(session);
	}

	public AbstractController(GenericDAOImpl<X, Long> dao) {
		LOGGER = Logger.getLogger(getClass());
		session = HibernateUtil.getSessionFactorySQL().getCurrentSession();
//		session.beginTransaction();
		this.dao = dao;
		this.dao.setSession(session);
	}
	
	public AbstractController(GenericDAOImpl<X, Long> dao, boolean isTest) {
		LOGGER = Logger.getLogger(getClass());
		this.dao = dao;
	}

	/*
	public AbstractController() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
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
				HibernateUtil.rollback(getSession().getTransaction());
				HibernateUtil.closeEditorSession(getEditorId());
			}
			HibernateUtil.procesarError(he);
			session = HibernateUtil.getEditorSession(editorId);
			session.setFlushMode(FlushMode.MANUAL);
			dao.setSession(session);
			HibernateUtil.verSesiones();
		}
	}


	public void doDelete(X registro) {
		try {
			dao.doDelete(registro);
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtil.rollback(getSession().getTransaction());
				HibernateUtil.closeEditorSession(getEditorId());
			}
			HibernateUtil.procesarError(he);
			session = HibernateUtil.getEditorSession(editorId);
			session.setFlushMode(FlushMode.MANUAL);
			dao.setSession(session);
			HibernateUtil.verSesiones();
		}
	}


	/**
	 * Finaliza una sesión de hibernate
	 * @param editorId id asociado a la sesión que será finalizada
	 */
	public void finalizarSesion() {
//		System.out.println("Finalizando sesión: " + getEditorId());
		LOGGER.info("Finalizando sesión: " + getEditorId());
		HibernateUtil.closeEditorSession(getEditorId());     // graba en la base de datos
	}

	/**
	 * Retorna el editor que está asociado a este controller
	 * @return id del editor asociado
	 */
	public String getEditorId() {
		return editorId;
	}

	/**
	 * Retorna la sesión que ha sida creada para este editor
	 * @return sesión de hibernate
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
	 * Obtiene un registro en base a su identificador en la base de datos.  Este método se utiliza frecuentemente en
	 * la inicialización de los editores.
	 * @param id identificador del registro en la DB
	 * @return registro de tipo X
	 */
	public X getRegistroById(Long id) {
		X result = null;
		try {
			result = getDAO().findById(id, true);
		} catch (Exception e) {
			if (getSession().isOpen()) {
				HibernateUtil.rollback(getSession().getTransaction());
				if (editorId != null) {
					HibernateUtil.closeEditorSession(getEditorId());
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
		HibernateUtil.verSesiones();
	}

}
