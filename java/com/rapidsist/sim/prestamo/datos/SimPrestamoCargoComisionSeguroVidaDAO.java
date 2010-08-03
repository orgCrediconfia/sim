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
 * Administra los accesos a la base de datos para las actividades o requisitos del préstamo.
 */
 
public class SimPrestamoCargoComisionSeguroVidaDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT \n"+
			" 	B.CVE_GPO_EMPRESA, \n"+
			" 	B.CVE_EMPRESA, \n"+
			" 	B.ID_BENEFICIARIO, \n"+
			" 	B.ID_CARGO_COMISION, \n"+
			" 	B.ID_PRESTAMO, \n"+
			" 	B.AP_PATERNO, \n"+
			" 	B.AP_MATERNO, \n"+
			" 	B.NOMBRE_1, \n"+
			" 	B.NOMBRE_2, \n"+
			" 	B.NOM_COMPLETO, \n"+
			" 	B.PORCENTAJE, \n"+
			" 	B.FECHA_NACIMIENTO, \n"+
			" 	B.ID_PARENTESCO, \n"+
			" 	C.NOM_PARENTESCO \n"+
			" FROM SIM_BENEFICIARIO B, \n"+
			"      SIM_CAT_PARENTESCO C \n"+
			" WHERE B.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND B.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND B.ID_CARGO_COMISION ='" + (String)parametros.getDefCampo("ID_CARGO_COMISION") + "' \n"+
			" AND B.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND C.CVE_GPO_EMPRESA = B.CVE_GPO_EMPRESA \n"+
			" AND C.CVE_EMPRESA = B.CVE_EMPRESA \n"+
			" AND C.ID_PARENTESCO = B.ID_PARENTESCO \n";
			
		sSql = sSql + " ORDER BY ID_BENEFICIARIO \n";
		
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
			" 	PAR.CVE_GPO_EMPRESA, \n"+
			" 	PAR.CVE_EMPRESA, \n"+
			" 	PAR.ID_ACTIVIDAD_REQUISITO, \n"+
			" 	PAR.ID_PRESTAMO, \n"+
			" 	PAR.FECHA_REGISTRO, \n"+
			" 	PAR.FECHA_REALIZADA, \n"+
			" 	PAR.ESTATUS, \n"+
			" 	PAR.COMENTARIO, \n"+
			" 	C.NOM_ACTIVIDAD_REQUISITO, \n"+
			" 	PR.ID_ETAPA_PRESTAMO \n"+
			" FROM SIM_PRESTAMO_ETAPA PAR, \n"+
			"      SIM_CAT_ACTIVIDAD_REQUISITO C, \n"+
			"      SIM_PRODUCTO_ETAPA_PRESTAMO PR \n"+
			" WHERE PAR.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND PAR.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PAR.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND C.CVE_GPO_EMPRESA = PAR.CVE_GPO_EMPRESA \n" +
			" AND C.CVE_EMPRESA = PAR.CVE_EMPRESA \n"+
			" AND C.ID_ACTIVIDAD_REQUISITO = PAR.ID_ACTIVIDAD_REQUISITO \n"+
			" AND PR.CVE_GPO_EMPRESA = PAR.CVE_GPO_EMPRESA \n" +
			" AND PR.CVE_EMPRESA = PAR.CVE_EMPRESA \n"+
			" AND PR.ID_ACTIVIDAD_REQUISITO = PAR.ID_ACTIVIDAD_REQUISITO \n";
			//" AND PR.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n";
			
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
		String sIdEscolaridad = "";
		
		//SE OBTIENE EL SEQUENCE
					   
		sSql = "SELECT SQ01_SIM_CAT_ESCOLARIDAD.nextval as ID_ESCOLARIDAD FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdEscolaridad = rs.getString("ID_ESCOLARIDAD");
		}
		
			sSql =  "INSERT INTO SIM_CAT_ESCOLARIDAD ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_ESCOLARIDAD, \n" +
					"NOM_ESCOLARIDAD) \n" +
			        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdEscolaridad + ", \n "+
				"'" + (String)registro.getDefCampo("NOM_ESCOLARIDAD") + "') \n" ;

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