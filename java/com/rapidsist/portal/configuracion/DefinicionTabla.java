/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.HashMap;

public class DefinicionTabla implements Serializable {
	
	/**
	 * Nombre de la Tabla.
	 */
	private String sNomTabla;
	
	/**
	 * Descripción de la Tabla.
	 */
	private String sDescTabla;

	/**
	 * Descripción de la Tabla.
	 */
	private String sBajaLogica;
	
	/**
	 * Lista de campos de la llave primaria.
	 */
	private LinkedList listaCamposLlavePrimaria;
	
	/**
	 * Lista de campos de la tabla.
	 */
	private LinkedList listaCamposTabla;
	
	/**
	 * Lista de tablas dependientes.
	 */
	private LinkedList listaTablasDependientes;
	
	/**
	 * Lista de tablas bitacora.
	 */
	private LinkedList listaTablasBitacora;
	
	/**
	 * Lista de filtros.
	 */
	private HashMap listaFiltros;
	
	/**
	 * 
	 */
	private HashMap mapaCamposTabla;

	public void setNomTabla(String sNomTabla){
		this.sNomTabla = sNomTabla;
	}
	public String getNomTabla(){
		return sNomTabla;
	}

	public void setDescTabla(String sDescTabla){
		this.sDescTabla = sDescTabla;
	}
	public String getDescTabla(){
		return sDescTabla;
	}

	public void setBajaLogica(String sBajaLogica){
		this.sBajaLogica = sBajaLogica;
	}
	public String getBajaLogica(){
		return sBajaLogica;
	}
	
	public void setCamposLlavePrimaria(LinkedList camposLlavePrimaria){
		this.listaCamposLlavePrimaria = camposLlavePrimaria;
	}
	public LinkedList getCamposLlavePrimaria(){
		return listaCamposLlavePrimaria;
	}

	public void setCamposTabla(LinkedList camposTabla){
		this.listaCamposTabla = camposTabla;
	}
	public LinkedList getCamposTabla(){
		return listaCamposTabla;
	}

	public void setTablasDependientes(LinkedList tablasDependientes){
		this.listaTablasDependientes = tablasDependientes;
	}
	public LinkedList getTablasDependientes(){
		return listaTablasDependientes;
	}

	public void setTablasBitacora(LinkedList tablasBitacora){
		this.listaTablasBitacora = tablasBitacora;
	}
	public LinkedList getTablasBitacora(){
		return listaTablasBitacora;
	}

	public void setFiltros(HashMap filtros){
		this.listaFiltros = filtros;
	}
	public HashMap getFiltros(){
		return listaFiltros;
	}

	public void setMapaCamposTabla(HashMap mapaCamposTabla){
		this.mapaCamposTabla = mapaCamposTabla;
	}
	public HashMap getMapaCamposTabla(){
		return mapaCamposTabla;
	}

}