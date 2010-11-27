/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para los montos por cliente del préstamo.
 */
 
public class SimPrestamoGrupalMontoEtapaDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla, OperacionConsultaRegistro, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		if (parametros.getDefCampo("PANTALLA").equals("ALTA")) {
			sSql =  "SELECT \n"+
					"GI.CVE_GPO_EMPRESA, \n" +
					"GI.CVE_EMPRESA, \n" +
					"GI.ID_GRUPO, \n" +
					"GI.ID_INTEGRANTE, \n" +
					"P.NOM_COMPLETO, \n" +
					"N.ID_TIPO_NEGOCIO, \n" +
					"A.NOM_TIPO_NEGOCIO, \n" +
					"N.CVE_CLASE, \n" +
					"C.NOM_CLASE NOM_GIRO \n" +
					"FROM \n" +
					"SIM_GRUPO_INTEGRANTE GI, \n" +
					"RS_GRAL_PERSONA P, \n" +
					"SIM_CLIENTE_NEGOCIO N, \n" +
					"SIM_CAT_TIPO_NEGOCIO A, \n" +
					"SIM_CAT_CLASE C \n" +
					"WHERE GI.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"AND GI.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
					"AND GI.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n" +
					"AND P.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n" +
					"AND P.CVE_EMPRESA = GI.CVE_EMPRESA \n" + 
					"AND P.ID_PERSONA = GI.ID_INTEGRANTE \n" +
					"AND N.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n" +
					"AND N.CVE_EMPRESA = GI.CVE_EMPRESA \n" +
					"AND N.ID_PERSONA = GI.ID_INTEGRANTE \n" +
					"AND N.B_PRINCIPAL = 'V' \n" +
					"AND A.CVE_GPO_EMPRESA = N.CVE_GPO_EMPRESA \n" +
					"AND A.CVE_EMPRESA = N.CVE_EMPRESA \n" +
					"AND A.ID_TIPO_NEGOCIO = N.ID_TIPO_NEGOCIO \n" +
					"AND C.CVE_GPO_EMPRESA = N.CVE_GPO_EMPRESA \n" +
					"AND C.CVE_EMPRESA = N.CVE_EMPRESA \n" +
					"AND C.CVE_CLASE = N.CVE_CLASE \n" +
					"AND GI.FECHA_BAJA_LOGICA IS NULL \n" ;
			
			
		}else if (parametros.getDefCampo("PANTALLA").equals("MODIFICACION")) {
			sSql =  "SELECT \n"+
					"GD.CVE_GPO_EMPRESA, \n" +
					"GD.CVE_EMPRESA, \n" +
					"GD.ID_PRESTAMO_GRUPO, \n" +
					"GD.ID_PRESTAMO, \n" +
					"PR.CVE_PRESTAMO, \n" +
					"GD.ID_INTEGRANTE, \n" +
					"GD.MONTO_SOLICITADO, \n" +
					"GD.MONTO_AUTORIZADO, \n" +
					"GD.ID_ETAPA_PRESTAMO, \n" +
					"GD.ID_PRESTAMO, \n" +
					"GD.COMENTARIO, \n" +
					"E.NOM_ESTATUS_PRESTAMO, \n" +
					"P.NOM_COMPLETO, \n" +
					"N.ID_TIPO_NEGOCIO, \n" +
					"A.NOM_TIPO_NEGOCIO, \n" +
					"N.CVE_CLASE, \n" +
					"C.NOM_CLASE NOM_GIRO \n" +
					"FROM \n" +
					"SIM_PRESTAMO_GPO_DET GD, \n" +
					"RS_GRAL_PERSONA P, \n" +
					"SIM_CLIENTE_NEGOCIO N, \n" +
					"SIM_CAT_TIPO_NEGOCIO A, \n" +
					"SIM_CAT_CLASE C, \n" +
					"SIM_CAT_ETAPA_PRESTAMO E, \n" +
					"SIM_PRESTAMO PR \n" +
					"WHERE GD.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"AND GD.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
					"AND GD.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
					"AND P.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n" +
					"AND P.CVE_EMPRESA = GD.CVE_EMPRESA \n" + 
					"AND P.ID_PERSONA = GD.ID_INTEGRANTE \n" +
					"AND N.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n" +
					"AND N.CVE_EMPRESA = GD.CVE_EMPRESA \n" +
					"AND N.ID_PERSONA = GD.ID_INTEGRANTE \n" +
					"AND N.B_PRINCIPAL = 'V' \n" +
					"AND A.CVE_GPO_EMPRESA = N.CVE_GPO_EMPRESA \n" +
					"AND A.CVE_EMPRESA = N.CVE_EMPRESA \n" +
					"AND A.ID_TIPO_NEGOCIO = N.ID_TIPO_NEGOCIO \n" +
					"AND C.CVE_GPO_EMPRESA = N.CVE_GPO_EMPRESA \n" +
					"AND C.CVE_EMPRESA = N.CVE_EMPRESA \n" +
					"AND C.CVE_CLASE = N.CVE_CLASE \n" +
					"AND E.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n" +
					"AND E.CVE_EMPRESA = GD.CVE_EMPRESA \n" +
					"AND E.ID_ETAPA_PRESTAMO = GD.ID_ETAPA_PRESTAMO \n" +
					"AND PR.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n" +
					"AND PR.CVE_EMPRESA = GD.CVE_EMPRESA \n" +
					"AND PR.ID_PRESTAMO = GD.ID_PRESTAMO \n" +
					"AND GD.ID_ETAPA_PRESTAMO != '18' \n" ;
			
		}
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
			" 	M.CVE_GPO_EMPRESA, \n"+
			" 	M.CVE_EMPRESA, \n"+
			" 	M.ID_PRESTAMO, \n"+
			" 	M.ID_CLIENTE, \n"+
			" 	M.MONTO_SOLICITADO, \n"+
			" 	M.MONTO_AUTORIZADO, \n"+
			" 	P.NOM_COMPLETO \n"+
			" FROM SIM_CLIENTE_MONTO M, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND M.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA = M.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA = M.ID_CLIENTE \n";
			
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
		
		sSql =  "INSERT INTO SIM_PRESTAMO_GPO_DET ( \n" +
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRESTAMO_GRUPO, \n" +
				"ID_INTEGRANTE, \n" +
				"MONTO_SOLICITADO \n" +
				")VALUES ( \n" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO_SOLICITADO") + "') \n" ;

		
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
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
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql =  " UPDATE SIM_CLIENTE_MONTO SET "+
			" MONTO_SOLICITADO		='" + (String)registro.getDefCampo("MONTO_SOLICITADO") + "' \n" +
			" WHERE ID_PRESTAMO  		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			" AND ID_CLIENTE   		='" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n";

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
		//BORRA LA FUNCION
		sSql =  " DELETE FROM SIM_PRESTAMO_ETAPA " +
			" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			" AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}