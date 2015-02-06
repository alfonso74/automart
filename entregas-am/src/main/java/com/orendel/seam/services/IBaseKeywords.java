package com.orendel.seam.services;

import com.orendel.seam.domain.Condicional;
import com.orendel.seam.domain.Status;

public interface IBaseKeywords {
	
	public static final String[] CONDICIONAL = {Condicional.SI.getDescripcion(), Condicional.NO.getDescripcion()};
	
	public static final String[] ESTADO = {Status.ACTIVE.getDescription(), Status.INACTIVE.getDescription()};

}
