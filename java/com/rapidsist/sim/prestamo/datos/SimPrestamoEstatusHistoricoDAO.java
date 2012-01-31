/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Administra los accesos a la base de datos para guardar el histórico del estatus del crédito.
 */
 
public class SimPrestamoEstatusHistoricoDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = " SELECT \n"+
			   " H.CVE_GPO_EMPRESA, \n"+
			   " H.CVE_EMPRESA, \n"+
			   " H.ID_PRESTAMO, \n" +
			   " H.ID_ETAPA_PRESTAMO, \n" +
			   " E.NOM_ESTATUS_PRESTAMO, \n" +
			   " H.CVE_USUARIO, \n" +
			   " P.NOM_COMPLETO, \n" +
			   " H.FECHA_REALIZADA \n" +
			   " FROM SIM_PRESTAMO_ETAPA_HISTORICO H, \n"+
			   "      SIM_CAT_ETAPA_PRESTAMO E, \n"+
			   "      RS_GRAL_USUARIO U, \n"+
			   "      RS_GRAL_PERSONA P \n"+
			   " WHERE H.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND H.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND H.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			   " AND E.CVE_GPO_EMPRESA = H.CVE_GPO_EMPRESA \n"+
			   " AND E.CVE_EMPRESA = H.CVE_EMPRESA \n"+
			   " AND E.ID_ETAPA_PRESTAMO = H.ID_ETAPA_PRESTAMO \n"+
			   " AND U.CVE_GPO_EMPRESA = H.CVE_GPO_EMPRESA \n"+
			   " AND U.CVE_EMPRESA = H.CVE_EMPRESA \n"+
			   " AND U.CVE_USUARIO = H.CVE_USUARIO \n"+
		 	   " AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
		       " AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
		       " AND P.ID_PERSONA = U.ID_PERSONA \n";
	
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		/*
		sSql =  " INSERT INTO SIM_PRESTAMO_ESTATUS_HISTORICO \n" +
				" (CVE_GPO_EMPRESA, \n" +
				" CVE_EMPRESA, \n" +
				" ID_PRESTAMO, \n" +
				" ID_ETAPA_PRESTAMO, \n" +
				" CVE_USUARIO, \n" +
				" FECHA) \n" +
				" VALUES \n" +
				"('" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				" '" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				" '" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
				" '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
				" '" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
				" SYSDATE) \n" ;
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
				" FECHA_REGISTRO 		=SYSDATE \n" +
				" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
		
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		*/
		return resultadoCatalogo;
	}
}