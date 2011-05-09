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
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las actividades o requisitos del préstamo.
 */
 
public class SimPrestamoCargoComisionGrupoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT DISTINCT \n"+
			" 	CC.CVE_GPO_EMPRESA, \n"+
			" 	CC.CVE_EMPRESA, \n"+
			" 	CC.ID_PRESTAMO, \n"+
			" 	CC.ID_CLIENTE, \n"+
			" 	CC.ID_GRUPO, \n"+
			" 	P.NOM_COMPLETO \n"+
			" FROM SIM_PRESTAMO_CARGO_COMISION CC, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE CC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CC.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CC.ID_GRUPO ='" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = CC.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA = CC.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA = CC.ID_CLIENTE \n";
		
		sSql = sSql + " ORDER BY CC.ID_CLIENTE \n";
		
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
			" 	CC.CVE_GPO_EMPRESA, \n"+
			" 	CC.CVE_EMPRESA, \n"+
			" 	CC.ID_CARGO_COMISION, \n"+
			" 	CC.ID_PRESTAMO, \n"+
			" 	CC.ID_CLIENTE, \n"+
			" 	CC.ID_FORMA_APLICACION, \n"+
			" 	CC.CARGO_INICIAL, \n"+
			" 	CC.PORCENTAJE_MONTO, \n"+
			" 	CC.CANTIDAD_FIJA, \n"+
			" 	CC.VALOR, \n"+
			" 	CC.ID_UNIDAD, \n"+
			" 	CC.ID_PERIODICIDAD, \n"+
			" 	A.NOM_ACCESORIO \n"+
			" FROM SIM_PRESTAMO_CARGO_COMISION CC, \n"+
			"      SIM_CAT_ACCESORIO A \n"+
			" WHERE CC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CC.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CC.ID_CLIENTE ='" + (String)parametros.getDefCampo("ID_CLIENTE") + "' \n"+
			" AND A.CVE_GPO_EMPRESA = CC.CVE_GPO_EMPRESA \n" +
			" AND A.CVE_EMPRESA = CC.CVE_EMPRESA \n"+
			" AND A.ID_ACCESORIO = CC.ID_CARGO_COMISION \n";
		
		sSql = sSql + " ORDER BY CC.ID_CARGO_COMISION \n";
			
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
		
		sSql =  " UPDATE SIM_PRESTAMO_CARGO_COMISION SET "+
			" ID_FORMA_APLICACION		='" + (String)registro.getDefCampo("ID_FORMA_APLICACION") + "', \n" +
			" CARGO_INICIAL			='" + (String)registro.getDefCampo("CARGO_INICIAL") + "', \n" +
			" PORCENTAJE_MONTO		='" + (String)registro.getDefCampo("PORCENTAJE_MONTO") + "', \n" +
			" CANTIDAD_FIJA			='" + (String)registro.getDefCampo("CANTIDAD_FIJA") + "', \n" +
			" VALOR				='" + (String)registro.getDefCampo("VALOR") + "', \n" +
			" ID_UNIDAD			='" + (String)registro.getDefCampo("ID_UNIDAD") + "', \n" +
			" ID_PERIODICIDAD		='" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "' \n" +
			" WHERE ID_PRESTAMO  		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			" AND ID_CARGO_COMISION   	='" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n"+
			" AND ID_CLIENTE   		='" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n"+
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
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
		sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
			" FECHA_REGISTRO		=TO_DATE('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','DD/MM/YYYY'), \n" +
			" FECHA_REALIZADA		=TO_DATE('" + (String)registro.getDefCampo("FECHA_REALIZADA") + "','DD/MM/YYYY'), \n" +
			" ESTATUS			='" + (String)registro.getDefCampo("ESTATUS") + "', \n" +
			" COMENTARIO			='" + (String)registro.getDefCampo("COMENTARIO") + "' \n" +
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