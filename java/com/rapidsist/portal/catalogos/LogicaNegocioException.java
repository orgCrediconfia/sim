/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.catalogos;

public class LogicaNegocioException extends Exception{
	private int iDetalle;
	
	public LogicaNegocioException(){
		iDetalle = -1;
	}

	public LogicaNegocioException(int iDetalle){
		this.iDetalle = iDetalle;
	}
}
