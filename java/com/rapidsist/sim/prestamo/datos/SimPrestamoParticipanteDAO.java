/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos de los roles de las personas.
 */
 
public class SimPrestamoParticipanteDAO extends Conexion2 implements OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
			sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO, \n"+
				"P.CVE_TIPO_PERSONA, \n"+
				"C.NOM_TIPO_PERSONA, \n"+
				"P.ID_PERSONA, \n"+
				"G.NOM_COMPLETO \n"+
				"FROM SIM_PRESTAMO_PARTICIPANTE P, \n"+
				"     SIM_CAT_TIPO_PERSONA C, \n"+
				"     RS_GRAL_PERSONA G \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND C.CVE_TIPO_PERSONA = P.CVE_TIPO_PERSONA \n"+
				"AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND G.ID_PERSONA (+)= P.ID_PERSONA \n";
				
		ejecutaSql();
		return getConsultaLista();
	}

	
	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO, \n"+
				"P.CVE_TIPO_PERSONA, \n"+
				"C.NOM_TIPO_PERSONA, \n"+
				"P.ID_PERSONA \n"+
				"FROM SIM_PRESTAMO_PARTICIPANTE P, \n"+
				"     SIM_CAT_TIPO_PERSONA C \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND P.CVE_TIPO_PERSONA = '" + (String)parametros.getDefCampo("CVE_TIPO_PERSONA") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND C.CVE_TIPO_PERSONA = P.CVE_TIPO_PERSONA \n";
				
				
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}


	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = " UPDATE SIM_PRESTAMO_PARTICIPANTE SET "+
			   " ID_PERSONA    	 = '" + (String)registro.getDefCampo("ID_PERSONA")  + "' \n" +
			   " WHERE ID_PRESTAMO    	 = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			   " AND CVE_TIPO_PERSONA   	 = '" + (String)registro.getDefCampo("CVE_TIPO_PERSONA") + "' \n"+
			   " AND CVE_EMPRESA   	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}