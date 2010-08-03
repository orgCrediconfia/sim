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
import com.rapidsist.sim.grupo.datos.SimGrupoIntegranteDAO;

/**
 * Administra los accesos a la base de datos de los candidatos al grupo
 */
 
public class SimGrupoCandidatoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionBaja  {

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
			sSql =   "SELECT \n"+
				 "TP.CVE_GPO_EMPRESA, \n"+
				 "TP.CVE_EMPRESA, \n"+
				 "TP.ID_PERSONA, \n"+
				 "P.NOM_COMPLETO, \n"+
				 "DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n"+
				 "TN.ID_TIPO_NEGOCIO, \n"+
				 "TN.NOM_TIPO_NEGOCIO \n"+
				 "FROM SIM_TIPO_PERSONA TP, \n"+
				 "     RS_GRAL_PERSONA P, \n"+
				 "     SIM_CLIENTE_NEGOCIO N, \n"+
				 "     SIM_CAT_TIPO_NEGOCIO TN \n"+
				 "WHERE TP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				 "AND TP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				 "AND TP.CVE_TIPO_PERSONA ='CLIENTE' \n" +
				 "AND P.CVE_GPO_EMPRESA = TP.CVE_GPO_EMPRESA \n" +
				 "AND P.CVE_EMPRESA = TP.CVE_EMPRESA \n" +
				 "AND P.ID_PERSONA = TP.ID_PERSONA \n" +
				 "AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
				 "AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
				 "AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
				 "AND N.B_PRINCIPAL (+)= 'V' \n" +
				 "AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
				 "AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
				 "AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
				 "MINUS \n" +
				 "SELECT \n" +
				 "GI.CVE_GPO_EMPRESA, \n" +
				 "GI.CVE_EMPRESA, \n" +
				 "GI.ID_INTEGRANTE ID_PERSONA, \n" +
				 "P.NOM_COMPLETO, \n" +
				 "DECODE(N.B_PRINCIPAL,'V','Principal','F','Secundario') B_PRINCIPAL, \n" +
				 "TN.ID_TIPO_NEGOCIO, \n"+
				 "TN.NOM_TIPO_NEGOCIO \n" +
				 "FROM SIM_GRUPO_INTEGRANTE GI, \n" +
				 "     RS_GRAL_PERSONA P, \n" +
				 "     SIM_CLIENTE_NEGOCIO N, \n" +
				 "     SIM_CAT_TIPO_NEGOCIO TN \n" +
				 "WHERE  GI.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				 "AND GI.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				 "AND GI.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n" +
				 "AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n" +
				 "AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n" +
				 "AND P.ID_PERSONA = GI.ID_INTEGRANTE \n" +
				 "AND GI.FECHA_BAJA_LOGICA IS NULL \n" +
				 "AND N.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n" +
				 "AND N.CVE_EMPRESA (+)= P.CVE_EMPRESA \n" +
				 "AND N.ID_PERSONA (+)= P.ID_PERSONA \n" +
				 "AND N.B_PRINCIPAL (+)= 'V' \n" +
				 "AND TN.CVE_GPO_EMPRESA (+)= N.CVE_GPO_EMPRESA \n" +
				 "AND TN.CVE_EMPRESA (+)= N.CVE_EMPRESA \n" +
				 "AND TN.ID_TIPO_NEGOCIO (+)= N.ID_TIPO_NEGOCIO \n" +
				 "ORDER BY ID_PERSONA \n";
			
		}else if (parametros.getDefCampo("CONSULTA").equals("ASIGNADOS")) {
			sSql =	"SELECT \n"+
					" GI.CVE_GPO_EMPRESA, \n"+
					" GI.CVE_EMPRESA, \n"+
					" GI.ID_INTEGRANTE ID_PERSONA, \n"+
					" P.NOM_COMPLETO \n"+
				" FROM SIM_GRUPO_INTEGRANTE GI, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE "+
				" GI.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND GI.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND GI.ID_GRUPO='" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
				" AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = GI.ID_INTEGRANTE \n"+
				" AND GI.FECHA_BAJA_LOGICA IS NULL \n"+
				" ORDER BY ID_PERSONA \n";
				
				/*
				"MINUS \n"+
				
				"SELECT \n"+
					" G.CVE_GPO_EMPRESA, \n"+
					" G.CVE_EMPRESA, \n"+
					" G.ID_COORDINADOR ID_PERSONA, \n"+
					" P.NOM_COMPLETO \n"+
				" FROM SIM_GRUPO G, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE G.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND G.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND G.ID_GRUPO='" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
				" AND P.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = G.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = G.ID_COORDINADOR \n";
				*/
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
		
		sSql =  "INSERT INTO SIM_GRUPO_INTEGRANTE ( \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_GRUPO, \n"+
			"ID_INTEGRANTE) \n"+
			"VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
					
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
		//BORRA EL REGISTRO
			
			if (registro.getDefCampo("BAJA").equals("Fisica")){
				sSql = " DELETE FROM SIM_GRUPO_INTEGRANTE "+
				       " WHERE ID_GRUPO		='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
				       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			}
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}