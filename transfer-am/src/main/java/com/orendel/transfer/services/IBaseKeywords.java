package com.orendel.transfer.services;

import com.orendel.transfer.domain.Condicional;
import com.orendel.transfer.domain.Status;

public interface IBaseKeywords {
	
	public static final String[] CONDICIONAL = {Condicional.SI.getDescripcion(), Condicional.NO.getDescripcion()};
	
	public static final String[] ESTADO = {Status.ACTIVE.getDescription(), Status.INACTIVE.getDescription()};

}
