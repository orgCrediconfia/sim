/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;
import java.io.Serializable;

public class Mensaje implements Serializable{
	
	/**
	 * Clave del mensaje.
	 */
	private String clave;
	
	/**
	 * Descripción.
	 */
	private String descripcion;
	
	/**
	 * Tipo de mensaje.
	 */
	private String tipo;
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTipo() {
		return tipo;
	}
}