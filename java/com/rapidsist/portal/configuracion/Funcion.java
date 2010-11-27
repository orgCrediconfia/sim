/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import java.io.Serializable;
import java.util.HashMap;

public class Funcion implements Serializable{

	/**
	 * Nombre de la función.
	 */
	private String nombre;
	
	/**
	 * Clase DAO.
	 */
	private String claseDao;
	
	/**
	 * Clase CON.
	 */
	private String claseCon;
	
	/**
	 * Esquema Catálogo.
	 */
	private String esquemaCatalogo;
	
	/**
	 * Nombres de la clases del reporte.
	 */
	private String nomClaseReporte;
	
	/**
	 * Usuario.
	 */
	private String logUsuario;
	
	/**
	 * Clave de la tabla.
	 */
	private String sCveTabla;
	
	/**
	 * Lista de operaciones.
	 */
	private HashMap listaOperaciones;
	
	/**
	 * Bandera que se enciende si se ratrea el código.
	 */
	private String bRastreaCodigo;
	
	/**
	 * Bandera para la bitácora.
	 */
	private String bBitacora;
	
	/**
	 * Bandera para la paginación.
	 */
	private String bPaginacion;
	
	/**
	 * Bandera de webservice.
	 */
	private String bWebservice;
	
	/**
	 * Bandera que indica si esta bloqueado.
	 */
	private String bBloqueado;
	
	public Funcion() {
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setBRastreaCodigo(String bRastreaCodigo){
		this.bRastreaCodigo = bRastreaCodigo;
	}
	public String getBRastreaCodigo(){
		return this.bRastreaCodigo;
	}
	
	public String getBBitacora(){
		return this.bBitacora;
	}
	public void setBBitacora(String bBitacora){
		this.bBitacora = bBitacora;
	}
	
	public String getBPaginacion(){
		return this.bPaginacion;
	}
	public void setBPaginacion(String bPaginacion){
		this.bPaginacion = bPaginacion;
	}
	
	public String getBWebservice(){
		return this.bWebservice;
	}
	public void setBWebservice(String bWebservice){
		this.bWebservice = bWebservice;
	}
	
	public String getBBloaqueado(){
		return this.bBloqueado;
	}
	public void setBBloaqueado(String bBloqueado){
		this.bBloqueado = bBloqueado;
	}
	
	public void setClaseDao(String claseDao) {
		this.claseDao = claseDao;
	}
	public String getClaseDao() {
		return claseDao;
	}
	public void setClaseCon(String claseCon) {
		this.claseCon = claseCon;
	}
	public String getClaseCon() {
		return claseCon;
	}
	public void setNomClaseReporte(String nomClaseReporte) {
		this.nomClaseReporte = nomClaseReporte;
	}
	public String getNomClaseReporte() {
		return nomClaseReporte;
	}
	public void setLogUsuario(String sLogUsuario){
		this.logUsuario = sLogUsuario;
	}
	public String getLogUsuario(){
		return logUsuario;
	}
	public void setCveTabla(String sCveTabla){
		this.sCveTabla = sCveTabla;
	}
	public String getCveTabla(){
		return sCveTabla;
	}
	public void setListaOperaciones(HashMap listaOperaciones){
		this.listaOperaciones = listaOperaciones;
	}
	public HashMap getListaOperaciones(){
		return listaOperaciones;
	}
}
