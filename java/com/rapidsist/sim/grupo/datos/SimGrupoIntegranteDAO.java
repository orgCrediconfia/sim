/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.grupo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos de los integrantes del grupo
 */
 
public class SimGrupoIntegranteDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("CONSULTA").equals("PARA_ALTA")) {
			//Consulta los posibles clientes para conformar el grupo.
			sSql =   " SELECT \n"+
					 " TP.CVE_GPO_EMPRESA, \n"+
					 " TP.CVE_EMPRESA, \n"+
					 " TP.ID_PERSONA, \n"+
					 " P.NOM_COMPLETO, \n"+
					 " P.CVE_ASESOR_CREDITO, \n"+
					 " NA.NOM_COMPLETO NOM_ASESOR_CREDITO, \n"+
					 " DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
					 " TN.ID_TIPO_NEGOCIO, \n"+
					 " TN.NOM_TIPO_NEGOCIO \n"+
					 " FROM SIM_TIPO_PERSONA TP, \n"+
					 "      RS_GRAL_PERSONA P, \n"+
					 "      SIM_CLIENTE_NEGOCIO N, \n"+
					 "      SIM_CAT_TIPO_NEGOCIO TN, \n"+
					 "      RS_GRAL_USUARIO U, \n"+
					 "      RS_GRAL_PERSONA NA \n"+
					 " WHERE TP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					 " AND TP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
					 " AND TP.CVE_TIPO_PERSONA ='CLIENTE' \n" +
					 " AND P.CVE_GPO_EMPRESA = TP.CVE_GPO_EMPRESA \n" +
					 " AND P.CVE_EMPRESA = TP.CVE_EMPRESA \n" +
					 " AND P.ID_PERSONA = TP.ID_PERSONA \n" +
					 " AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n" +
					 " AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
					 " AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
					 " AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
					 " AND N.B_PRINCIPAL (+)= 'V' \n" +
					 " AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
					 " AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
					 " AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
					 " AND P.LISTA_NEGRA = 'F' \n" +
					 " AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
					 " AND U.CVE_EMPRESA = P.CVE_EMPRESA \n" +
					 " AND U.CVE_USUARIO = P.CVE_ASESOR_CREDITO \n" +
					 " AND NA.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
					 " AND NA.CVE_EMPRESA = U.CVE_EMPRESA \n"+
					 " AND NA.ID_PERSONA = U.ID_PERSONA \n";
			
					if (parametros.getDefCampo("ID_PERSONA") != null) {
						sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
					}
					if (parametros.getDefCampo("NOM_COMPLETO") != null) {
						sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
					}
			
					sSql = sSql + " MINUS \n" +
					 " SELECT \n"+
					 " GI.CVE_GPO_EMPRESA, \n"+
					 " GI.CVE_EMPRESA, \n"+
					 " GI.ID_INTEGRANTE ID_PERSONA, \n"+
					 " P.NOM_COMPLETO, \n"+
					 " P.CVE_ASESOR_CREDITO, \n"+
					 " NA.NOM_COMPLETO NOM_ASESOR_CREDITO, \n"+
					 " DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
					 " TN.ID_TIPO_NEGOCIO, \n"+
					 " TN.NOM_TIPO_NEGOCIO \n"+
					 " FROM SIM_GRUPO_INTEGRANTE GI, \n"+
					 "      RS_GRAL_PERSONA P, \n"+
					 "      SIM_CLIENTE_NEGOCIO N, \n" +
					 "      SIM_CAT_TIPO_NEGOCIO TN, \n" +
					 "      RS_GRAL_USUARIO U, \n"+
					 "      RS_GRAL_PERSONA NA \n"+
					 " WHERE "+
					 " GI.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					 " AND GI.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					 " AND GI.ID_GRUPO='" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
					 " AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
					 " AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
					 " AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
					 " AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n" +
					 " AND GI.FECHA_BAJA_LOGICA IS NULL \n"+
					 " AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
					 " AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
					 " AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
					 " AND N.B_PRINCIPAL (+)= 'V' \n" +
					 " AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
					 " AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
					 " AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
					 " AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
					 " AND U.CVE_EMPRESA = P.CVE_EMPRESA \n" +
					 " AND U.CVE_USUARIO = P.CVE_ASESOR_CREDITO \n" +
					 " AND NA.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
					 " AND NA.CVE_EMPRESA = U.CVE_EMPRESA \n"+
					 " AND NA.ID_PERSONA = U.ID_PERSONA \n";
			
					if (parametros.getDefCampo("ID_PERSONA") != null) {
						sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
					}
					if (parametros.getDefCampo("NOM_COMPLETO") != null) {
						sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
					}
					/*
					sSql = sSql + " MINUS \n" +
					 " SELECT \n"+
					 " GI.CVE_GPO_EMPRESA, \n"+
					 " GI.CVE_EMPRESA, \n"+
					 " GI.ID_INTEGRANTE ID_PERSONA, \n"+
					 " P.NOM_COMPLETO, \n"+
					 " P.CVE_ASESOR_CREDITO, \n"+
					 " NA.NOM_COMPLETO NOM_ASESOR_CREDITO, \n"+
					 " DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
					 " TN.ID_TIPO_NEGOCIO, \n"+
					 " TN.NOM_TIPO_NEGOCIO \n"+
					 " FROM SIM_GRUPO_INTEGRANTE GI, \n"+
					 "      RS_GRAL_PERSONA P, \n"+
				     	 " SIM_CLIENTE_NEGOCIO N, \n" +
				     	 " SIM_CAT_TIPO_NEGOCIO TN, \n" +
				     	 " RS_GRAL_USUARIO U, \n"+
					 "     RS_GRAL_PERSONA NA \n"+
					 " WHERE "+
					 " GI.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					 " AND GI.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					 " AND GI.ID_GRUPO='" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
					 " AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
					 " AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
					 " AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
					 " AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n" +
					 " AND GI.FECHA_BAJA_LOGICA IS NOT NULL \n"+
					 " AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
					 " AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
					 " AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
					 " AND N.B_PRINCIPAL (+)= 'V' \n" +
					 " AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
					 " AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
					 " AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
					 " AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
					 " AND U.CVE_EMPRESA = P.CVE_EMPRESA \n" +
					 " AND U.CVE_USUARIO = P.CVE_ASESOR_CREDITO \n" +
					 " AND NA.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
					 " AND NA.CVE_EMPRESA = U.CVE_EMPRESA \n"+
					 " AND NA.ID_PERSONA = U.ID_PERSONA \n";
				
				if (parametros.getDefCampo("ID_PERSONA") != null) {
					sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
				}
				if (parametros.getDefCampo("NOM_COMPLETO") != null) {
					sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
				}
				 */
				 sSql = sSql + "ORDER BY ID_PERSONA \n";
				 
				 System.out.println("usuarios para alta"+sSql);
		}else if (parametros.getDefCampo("CONSULTA").equals("ASIGNADOS")) {
			//Consulta los integrantes asignados al grupo.
			sSql =	" SELECT \n"+
					" GI.CVE_GPO_EMPRESA, \n"+
					" GI.CVE_EMPRESA, \n"+
					" GI.ID_INTEGRANTE ID_PERSONA, \n"+
					" GI.FECHA_ALTA, \n"+
					" P.NOM_COMPLETO, \n"+
					" P.CVE_ASESOR_CREDITO, \n"+
					" NA.NOM_COMPLETO NOM_ASESOR_CREDITO, \n"+
					" DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
					" TN.ID_TIPO_NEGOCIO, \n"+
					" TN.NOM_TIPO_NEGOCIO \n"+
					" FROM SIM_GRUPO_INTEGRANTE GI, \n"+
					"      RS_GRAL_PERSONA P, \n"+
					"      SIM_CLIENTE_NEGOCIO N, \n" +
					"      SIM_CAT_TIPO_NEGOCIO TN, \n" +
					"      RS_GRAL_USUARIO U, \n"+
					"      RS_GRAL_PERSONA NA \n"+
					" WHERE "+
					" GI.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND GI.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND GI.ID_GRUPO='" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
					" AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
					" AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
					" AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
					" AND GI.FECHA_BAJA_LOGICA IS NULL \n"+
					" AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
					" AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
					" AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
					" AND N.B_PRINCIPAL (+)= 'V' \n" +
					" AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
					" AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
					" AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
					" AND U.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
					" AND U.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
					" AND U.CVE_USUARIO (+)= P.CVE_ASESOR_CREDITO \n" +
					" AND NA.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n"+
					" AND NA.CVE_EMPRESA (+)= U.CVE_EMPRESA \n"+
					" AND NA.ID_PERSONA (+)= U.ID_PERSONA \n"+
					" ORDER BY ID_PERSONA \n";
			
		}else if (parametros.getDefCampo("CONSULTA").equals("BAJA")) {
			//Consulta los integrantes que fueron dados de baja en el grupo.
			sSql =	" SELECT \n"+
					" GI.CVE_GPO_EMPRESA, \n"+
					" GI.CVE_EMPRESA, \n"+
					" GI.ID_INTEGRANTE ID_PERSONA, \n"+
					" GI.FECHA_ALTA, \n"+
					" GI.FECHA_BAJA_LOGICA, \n"+
					" P.NOM_COMPLETO, \n"+
					" DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
					" TN.ID_TIPO_NEGOCIO, \n"+
					" TN.NOM_TIPO_NEGOCIO \n"+
				    " FROM SIM_GRUPO_INTEGRANTE GI, \n"+
				    "      RS_GRAL_PERSONA P, \n"+
				    "     SIM_CLIENTE_NEGOCIO N, \n" +
				    "     SIM_CAT_TIPO_NEGOCIO TN \n" +
				    " WHERE "+
				    " GI.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				    " AND GI.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				    " AND GI.ID_GRUPO='" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
				    " AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
				    " AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
				    " AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
				    " AND GI.FECHA_BAJA_LOGICA IS NOT NULL \n"+
				    " AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
				    " AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
				    " AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
				    " AND N.B_PRINCIPAL (+)= 'V' \n" +
				    " AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
				    " AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
				    " AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
				    " ORDER BY ID_PERSONA \n";
				
		}
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		//Inserta el integrante al grupo
		//Verifica si ya ha sido parte del grupo
		sSql = "SELECT \n" +
			   " CVE_GPO_EMPRESA, \n" +
			   " CVE_EMPRESA, \n" +
			   " ID_GRUPO, \n"+
			   " ID_INTEGRANTE, \n"+
			   " FECHA_ALTA, \n"+
			   " FECHA_BAJA_LOGICA \n"+
			   "FROM \n"+
			   " SIM_GRUPO_INTEGRANTE \n"+
			   " WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
			   " AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
			   " AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" ;
		ejecutaSql();
		if (rs.next()){
			//Borra la fecha de baja logica.
			sSql = " UPDATE SIM_GRUPO_INTEGRANTE SET"+
		       " FECHA_BAJA_LOGICA 	= '', \n" +
		       " FECHA_ALTA			= SYSDATE \n" +
		       " WHERE ID_GRUPO		='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
		       " AND ID_INTEGRANTE	='" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n"+
		       " AND CVE_EMPRESA	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
		       " AND CVE_GPO_EMPRESA	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		}else {
			//Da de alta el integrante al grupo.
			sSql = " INSERT INTO SIM_GRUPO_INTEGRANTE ( \n"+
				   " CVE_GPO_EMPRESA, \n" +
				   " CVE_EMPRESA, \n" +
				   " ID_GRUPO, \n"+
				   " ID_INTEGRANTE, \n"+
				   " FECHA_ALTA, \n"+
				   " FECHA_BAJA_LOGICA) \n"+
				   " VALUES ( \n"+
				   " '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				   " '" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				   " '" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				   " '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
				   " SYSDATE, \n" +
				   " '') \n" ;
		}
			  	
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
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
		
		//Borrar el integrante del grupo	
			sSql = " UPDATE SIM_GRUPO_INTEGRANTE SET"+
			       " FECHA_BAJA_LOGICA 	= SYSDATE \n" +
			       " WHERE ID_GRUPO		='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
			       " AND ID_INTEGRANTE	='" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n"+
			       " AND CVE_EMPRESA	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			       " AND CVE_GPO_EMPRESA	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}