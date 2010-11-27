/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de estatus del préstamo.
 */
 
public class SimCatalogoEstatusPrestamoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  " SELECT \n"+
			" 	CVE_GPO_EMPRESA, \n"+
			" 	CVE_EMPRESA, \n"+
			" 	ID_ETAPA_PRESTAMO, \n"+
			" 	NOM_ESTATUS_PRESTAMO, \n"+
			" 	DESCRIPCION, \n"+
			" 	CVE_FUNCION \n"+
			" FROM SIM_CAT_ETAPA_PRESTAMO \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
				
		if (parametros.getDefCampo("ID_ETAPA_PRESTAMO") != null) {
			sSql = sSql + " AND ID_ETAPA_PRESTAMO = '" + (String) parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_ESTATUS_PRESTAMO") != null) {
			sSql = sSql + " AND UPPER(NOM_ESTATUS_PRESTAMO) LIKE'%" + ((String) parametros.getDefCampo("NOM_ESTATUS_PRESTAMO")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY ID_ETAPA_PRESTAMO \n";
				
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  " SELECT \n"+
			" 	CVE_GPO_EMPRESA, \n"+
			" 	CVE_EMPRESA, \n"+
			" 	ID_ETAPA_PRESTAMO, \n"+
			" 	NOM_ESTATUS_PRESTAMO, \n"+
			" 	DESCRIPCION, \n"+
			" 	CVE_FUNCION, \n"+
			" 	B_AUTORIZAR_COMITE, \n"+
			" 	B_DESEMBOLSO, \n"+
			" 	B_LINEA_FONDEO, \n"+
			" 	B_CANCELADO, \n"+
			" 	B_ENTREGADO \n"+
			" FROM SIM_CAT_ETAPA_PRESTAMO \n"+			
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_ETAPA_PRESTAMO = '" + (String)parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		String sNomEtapa = "";
		
		if (registro.getDefCampo("B_AUTORIZAR_COMITE").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_AUTORIZAR_COMITE = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_COMITE_AUTORIZA");
			}else {
				//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
				sSql =  "SELECT REPLACE(INITCAP('" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "'),' ','') NOM_ETAPA FROM DUAL \n";
				ejecutaSql();
				if (rs.next()){
					sNomEtapa = rs.getString("NOM_ETAPA");
				}
				
				sSql =  "INSERT INTO RS_CONF_FUNCION ( \n"+
						"CVE_FUNCION, \n"+
						"NOM_FUNCION, \n"+
						"B_WEBSERVICE, \n"+
						"B_BLOQUEADO, \n"+
						"B_BITACORA, \n"+
						"B_PAGINACION, \n"+
						"B_RASTREA_CODIGO) \n"+
						"VALUES ( \n"+
						"'SimEtapa" + sNomEtapa + "', \n"+
						"'Etapa de " + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F') \n";
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				String sIdEstatusPrestamo = "";
				
				sSql = "SELECT SQ01_SIM_CAT_ESTATUS_PRESTAMO.nextval AS ID_ETAPA_PRESTAMO FROM DUAL";
				
				ejecutaSql();
				if (rs.next()){
					sIdEstatusPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				}
			
				sSql =  "INSERT INTO SIM_CAT_ETAPA_PRESTAMO ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_ETAPA_PRESTAMO, \n"+
						"NOM_ESTATUS_PRESTAMO, \n"+
						"DESCRIPCION, \n"+
						"CVE_FUNCION, \n"+
						"B_AUTORIZAR_COMITE, \n"+
						"B_DESEMBOLSO, \n"+
						"B_LINEA_FONDEO, \n"+
						"B_CANCELADO, \n"+
						"B_ENTREGADO) \n"+
					"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdEstatusPrestamo + ", \n" +
					"'" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
					"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n"+
					"'SimEtapa" + sNomEtapa + "', \n"+
					"'" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE") + "', \n"+
					"'" + (String)registro.getDefCampo("B_DESEMBOLSO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_LINEA_FONDEO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_CANCELADO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_ENTREGADO") + "') \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_DESEMBOLSO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_DESEMBOLSO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_DESEMBOLSO");
			}else {
				//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
				sSql =  "SELECT REPLACE(INITCAP('" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "'),' ','') NOM_ETAPA FROM DUAL \n";
				ejecutaSql();
				if (rs.next()){
					sNomEtapa = rs.getString("NOM_ETAPA");
				}
				
				sSql =  "INSERT INTO RS_CONF_FUNCION ( \n"+
						"CVE_FUNCION, \n"+
						"NOM_FUNCION, \n"+
						"B_WEBSERVICE, \n"+
						"B_BLOQUEADO, \n"+
						"B_BITACORA, \n"+
						"B_PAGINACION, \n"+
						"B_RASTREA_CODIGO) \n"+
						"VALUES ( \n"+
						"'SimEtapa" + sNomEtapa + "', \n"+
						"'Etapa de " + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F') \n";
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				String sIdEstatusPrestamo = "";
				
				sSql = "SELECT SQ01_SIM_CAT_ESTATUS_PRESTAMO.nextval AS ID_ETAPA_PRESTAMO FROM DUAL";
				
				ejecutaSql();
				if (rs.next()){
					sIdEstatusPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				}
			
				sSql =  "INSERT INTO SIM_CAT_ETAPA_PRESTAMO ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_ETAPA_PRESTAMO, \n"+
						"NOM_ESTATUS_PRESTAMO, \n"+
						"DESCRIPCION, \n"+
						"CVE_FUNCION, \n"+
						"B_AUTORIZAR_COMITE, \n"+
						"B_DESEMBOLSO, \n"+
						"B_LINEA_FONDEO, \n"+
						"B_CANCELADO, \n"+
						"B_ENTREGADO) \n"+
					"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdEstatusPrestamo + ", \n" +
					"'" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
					"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n"+
					"'SimEtapa" + sNomEtapa + "', \n"+
					"'" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE") + "', \n"+
					"'" + (String)registro.getDefCampo("B_DESEMBOLSO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_LINEA_FONDEO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_CANCELADO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_ENTREGADO") + "') \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_LINEA_FONDEO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_LINEA_FONDEO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_LINEA_FONDEO");
			}else {
				//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
				sSql =  "SELECT REPLACE(INITCAP('" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "'),' ','') NOM_ETAPA FROM DUAL \n";
				ejecutaSql();
				if (rs.next()){
					sNomEtapa = rs.getString("NOM_ETAPA");
				}
				
				sSql =  "INSERT INTO RS_CONF_FUNCION ( \n"+
						"CVE_FUNCION, \n"+
						"NOM_FUNCION, \n"+
						"B_WEBSERVICE, \n"+
						"B_BLOQUEADO, \n"+
						"B_BITACORA, \n"+
						"B_PAGINACION, \n"+
						"B_RASTREA_CODIGO) \n"+
						"VALUES ( \n"+
						"'SimEtapa" + sNomEtapa + "', \n"+
						"'Etapa de " + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F') \n";
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				String sIdEstatusPrestamo = "";
				
				sSql = "SELECT SQ01_SIM_CAT_ESTATUS_PRESTAMO.nextval AS ID_ETAPA_PRESTAMO FROM DUAL";
				
				ejecutaSql();
				if (rs.next()){
					sIdEstatusPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				}
			
				sSql =  "INSERT INTO SIM_CAT_ETAPA_PRESTAMO ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_ETAPA_PRESTAMO, \n"+
						"NOM_ESTATUS_PRESTAMO, \n"+
						"DESCRIPCION, \n"+
						"CVE_FUNCION, \n"+
						"B_AUTORIZAR_COMITE, \n"+
						"B_DESEMBOLSO, \n"+
						"B_LINEA_FONDEO, \n"+
						"B_CANCELADO, \n"+
						"B_ENTREGADO) \n"+
					"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdEstatusPrestamo + ", \n" +
					"'" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
					"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n"+
					"'SimEtapa" + sNomEtapa + "', \n"+
					"'" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE") + "', \n"+
					"'" + (String)registro.getDefCampo("B_DESEMBOLSO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_LINEA_FONDEO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_CANCELADO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_ENTREGADO") + "') \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_CANCELADO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_CANCELADO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_CANCELADO");
			}else {
				//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
				sSql =  "SELECT REPLACE(INITCAP('" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "'),' ','') NOM_ETAPA FROM DUAL \n";
				ejecutaSql();
				if (rs.next()){
					sNomEtapa = rs.getString("NOM_ETAPA");
				}
				
				sSql =  "INSERT INTO RS_CONF_FUNCION ( \n"+
						"CVE_FUNCION, \n"+
						"NOM_FUNCION, \n"+
						"B_WEBSERVICE, \n"+
						"B_BLOQUEADO, \n"+
						"B_BITACORA, \n"+
						"B_PAGINACION, \n"+
						"B_RASTREA_CODIGO) \n"+
						"VALUES ( \n"+
						"'SimEtapa" + sNomEtapa + "', \n"+
						"'Etapa de " + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F') \n";
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				String sIdEstatusPrestamo = "";
				
				sSql = "SELECT SQ01_SIM_CAT_ESTATUS_PRESTAMO.nextval AS ID_ETAPA_PRESTAMO FROM DUAL";
				
				ejecutaSql();
				if (rs.next()){
					sIdEstatusPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				}
			
				sSql =  "INSERT INTO SIM_CAT_ETAPA_PRESTAMO ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_ETAPA_PRESTAMO, \n"+
						"NOM_ESTATUS_PRESTAMO, \n"+
						"DESCRIPCION, \n"+
						"CVE_FUNCION, \n"+
						"B_AUTORIZAR_COMITE, \n"+
						"B_DESEMBOLSO, \n"+
						"B_LINEA_FONDEO, \n"+
						"B_CANCELADO, \n"+
						"B_ENTREGADO) \n"+
					"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdEstatusPrestamo + ", \n" +
					"'" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
					"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n"+
					"'SimEtapa" + sNomEtapa + "', \n"+
					"'" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE") + "', \n"+
					"'" + (String)registro.getDefCampo("B_DESEMBOLSO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_LINEA_FONDEO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_CANCELADO") + "', \n"+
					"'" + (String)registro.getDefCampo("B_ENTREGADO") + "') \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_ENTREGADO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_ENTREGADO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_ENTREGADO");
			}else {
				//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
				sSql =  "SELECT REPLACE(INITCAP('" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "'),' ','') NOM_ETAPA FROM DUAL \n";
				ejecutaSql();
				if (rs.next()){
					sNomEtapa = rs.getString("NOM_ETAPA");
				}
				
				sSql =  "INSERT INTO RS_CONF_FUNCION ( \n"+
						"CVE_FUNCION, \n"+
						"NOM_FUNCION, \n"+
						"B_WEBSERVICE, \n"+
						"B_BLOQUEADO, \n"+
						"B_BITACORA, \n"+
						"B_PAGINACION, \n"+
						"B_RASTREA_CODIGO) \n"+
						"VALUES ( \n"+
						"'SimEtapa" + sNomEtapa + "', \n"+
						"'Etapa de " + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F', \n"+
						"'F') \n";
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				String sIdEstatusPrestamo = "";
				
				sSql = "SELECT SQ01_SIM_CAT_ESTATUS_PRESTAMO.nextval AS ID_ETAPA_PRESTAMO FROM DUAL";
				
				ejecutaSql();
				if (rs.next()){
					sIdEstatusPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				}
			
				sSql =  "INSERT INTO SIM_CAT_ETAPA_PRESTAMO ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_ETAPA_PRESTAMO, \n"+
							"NOM_ESTATUS_PRESTAMO, \n"+
							"DESCRIPCION, \n"+
							"CVE_FUNCION, \n"+
							"B_AUTORIZAR_COMITE, \n"+
							"B_DESEMBOLSO, \n"+
							"B_LINEA_FONDEO, \n"+
							"B_CANCELADO, \n"+
							"B_ENTREGADO) \n"+
						"VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						sIdEstatusPrestamo + ", \n" +
						"'" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
						"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n"+
						"'SimEtapa" + sNomEtapa + "', \n"+
						"'" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE") + "', \n"+
						"'" + (String)registro.getDefCampo("B_DESEMBOLSO") + "', \n"+
						"'" + (String)registro.getDefCampo("B_LINEA_FONDEO") + "', \n"+
						"'" + (String)registro.getDefCampo("B_CANCELADO") + "', \n"+
						"'" + (String)registro.getDefCampo("B_ENTREGADO") + "') \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_AUTORIZAR_COMITE").equals("") && registro.getDefCampo("B_DESEMBOLSO").equals("") && registro.getDefCampo("B_LINEA_FONDEO").equals("") && registro.getDefCampo("B_CANCELADO").equals("") && registro.getDefCampo("B_ENTREGADO").equals("")) {
			//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
			sSql =  "SELECT REPLACE(INITCAP('" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "'),' ','') NOM_ETAPA FROM DUAL \n";
			ejecutaSql();
			if (rs.next()){
				sNomEtapa = rs.getString("NOM_ETAPA");
			}
			
			sSql =  "INSERT INTO RS_CONF_FUNCION ( \n"+
					"CVE_FUNCION, \n"+
					"NOM_FUNCION, \n"+
					"B_WEBSERVICE, \n"+
					"B_BLOQUEADO, \n"+
					"B_BITACORA, \n"+
					"B_PAGINACION, \n"+
					"B_RASTREA_CODIGO) \n"+
					"VALUES ( \n"+
					"'SimEtapa" + sNomEtapa + "', \n"+
					"'Etapa de " + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
					"'F', \n"+
					"'F', \n"+
					"'F', \n"+
					"'F', \n"+
					"'F') \n";
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			String sIdEstatusPrestamo = "";
			
			sSql = "SELECT SQ01_SIM_CAT_ESTATUS_PRESTAMO.nextval AS ID_ETAPA_PRESTAMO FROM DUAL";
			
			ejecutaSql();
			if (rs.next()){
				sIdEstatusPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
			}
		
			sSql =  "INSERT INTO SIM_CAT_ETAPA_PRESTAMO ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_ETAPA_PRESTAMO, \n"+
					"NOM_ESTATUS_PRESTAMO, \n"+
					"DESCRIPCION, \n"+
					"CVE_FUNCION, \n"+
					"B_AUTORIZAR_COMITE, \n"+
					"B_DESEMBOLSO, \n"+
					"B_LINEA_FONDEO, \n"+
					"B_CANCELADO, \n"+
					"B_ENTREGADO) \n"+
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdEstatusPrestamo + ", \n" +
				"'" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO") + "', \n"+
				"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n"+
				"'SimEtapa" + sNomEtapa + "', \n"+
				"'F', \n"+
				"'F', \n"+
				"'F', \n"+
				"'F', \n"+
				"'F') \n";
			
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		
		
		return resultadoCatalogo;
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		
		String sIdEtapaPrestamo = (String)registro.getDefCampo("ID_ETAPA_PRESTAMO");
		String sEtapaPrestamo = "";
		
		if (registro.getDefCampo("B_AUTORIZAR_COMITE").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_AUTORIZAR_COMITE = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				sEtapaPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				if (sIdEtapaPrestamo.equals(sEtapaPrestamo)){
					sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
							" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
							" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
							" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
							" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
							" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
							" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
							" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
							" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
							" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
				}else {
					resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_COMITE_AUTORIZA");
				}
			}else {
		
				sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					  
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_DESEMBOLSO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_DESEMBOLSO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				sEtapaPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				
				if (sIdEtapaPrestamo.equals(sEtapaPrestamo)){
					sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
				}else {
					resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_DESEMBOLSO");
				}
			}else {
		
				sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "' \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_LINEA_FONDEO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_LINEA_FONDEO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				sEtapaPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				
				if (sIdEtapaPrestamo.equals(sEtapaPrestamo)){
					sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
				}else {
					resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_LINEA_FONDEO");
				}
			}else {
		
				sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_CANCELADO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_CANCELADO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				sEtapaPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				
				if (sIdEtapaPrestamo.equals(sEtapaPrestamo)){
					sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
				}else {
					resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_CANCELADO");
				}
			}else {
		
				sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_ENTREGADO").equals("V")) {
			sSql ="SELECT ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_ENTREGADO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				sEtapaPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
				
				if (sIdEtapaPrestamo.equals(sEtapaPrestamo)){
					sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
				}else {
					resultadoCatalogo.mensaje.setClave("EXISTE_ETAPA_ENTREGADO");
				}
			}else {
		
				sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='" + (String)registro.getDefCampo("B_AUTORIZAR_COMITE")  + "', \n" +
					" B_DESEMBOLSO				='" + (String)registro.getDefCampo("B_DESEMBOLSO")  + "', \n" +
					" B_LINEA_FONDEO			='" + (String)registro.getDefCampo("B_LINEA_FONDEO")  + "', \n" +
					" B_CANCELADO				='" + (String)registro.getDefCampo("B_CANCELADO")  + "', \n" +
					" B_ENTREGADO				='" + (String)registro.getDefCampo("B_ENTREGADO")  + "' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		
		if (registro.getDefCampo("B_AUTORIZAR_COMITE").equals("F") && registro.getDefCampo("B_DESEMBOLSO").equals("F") && registro.getDefCampo("B_LINEA_FONDEO").equals("F") && registro.getDefCampo("B_CANCELADO").equals("F") && registro.getDefCampo("B_ENTREGADO").equals("F")) {
			sSql =  " UPDATE SIM_CAT_ETAPA_PRESTAMO SET "+
					" NOM_ESTATUS_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_ESTATUS_PRESTAMO")  + "', \n" +
					" DESCRIPCION     			='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
					" B_AUTORIZAR_COMITE        ='F', \n" +
					" B_DESEMBOLSO				='F', \n" +
					" B_LINEA_FONDEO			='F', \n" +
					" B_CANCELADO				='F', \n" +
					" B_ENTREGADO				='F' \n" +
					" WHERE ID_ETAPA_PRESTAMO    ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					   
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
		}
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		
		sSql =  " DELETE FROM RS_CONF_APLICACION_FUNCION " +
		 	   	" WHERE CVE_FUNCION  ='" + (String)registro.getDefCampo("CVE_FUNCION") + "' \n" ;
			
		PreparedStatement ps1 = this.conn.prepareStatement(sSql);
		ps1.execute();
		ResultSet rs1 = ps1.getResultSet();
		
		sSql =  " DELETE FROM RS_CONF_APLICACION_CONFIG " +
		 	   	" WHERE CVE_FUNCION  ='" + (String)registro.getDefCampo("CVE_FUNCION") + "' \n" ;
			   	
		//VERIFICA SI DIO DE BAJA EL REGISTRO
		PreparedStatement ps2 = this.conn.prepareStatement(sSql);
		ps2.execute();
		ResultSet rs2 = ps2.getResultSet();
		
		sSql =  " DELETE FROM RS_CONF_FUNCION " +
		 	   	" WHERE CVE_FUNCION  ='" + (String)registro.getDefCampo("CVE_FUNCION") + "' \n" ;
			   
		PreparedStatement ps3 = this.conn.prepareStatement(sSql);
		ps3.execute();
		ResultSet rs3 = ps3.getResultSet();
		
		sSql =  " DELETE FROM SIM_CAT_ETAPA_PRESTAMO " +
	 	   	" WHERE ID_ETAPA_PRESTAMO  ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
		   	" AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		   	" AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		
		PreparedStatement ps4 = this.conn.prepareStatement(sSql);
		ps4.execute();
		ResultSet rs4 = ps4.getResultSet();

		return resultadoCatalogo;
	}

}