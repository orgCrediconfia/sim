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
 
public class SimSeguroVidaBeneficiarioDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
			" 	PCC.CVE_GPO_EMPRESA, \n"+
			" 	PCC.CVE_EMPRESA, \n"+
			" 	PCC.ID_CARGO_COMISION, \n"+
			" 	PCC.ID_PRESTAMO, \n"+
			" 	C.NOM_CARGO_COMISION \n"+
			" FROM SIM_PRESTAMO_CARGO_COMISION PCC, \n"+
			"      SIM_CAT_CARGO_COMISION C \n"+
			" WHERE PCC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND PCC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PCC.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND C.CVE_GPO_EMPRESA = PCC.CVE_GPO_EMPRESA \n" +
			" AND C.CVE_EMPRESA = PCC.CVE_EMPRESA \n"+
			" AND C.ID_CARGO_COMISION = PCC.ID_CARGO_COMISION \n";
		
		sSql = sSql + " ORDER BY ID_CARGO_COMISION \n";
		
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
			" 	ID_BENEFICIARIO, \n"+
			" 	ID_CARGO_COMISION, \n"+
			" 	ID_PRESTAMO, \n"+
			" 	AP_PATERNO, \n"+
			" 	AP_MATERNO, \n"+
			" 	NOMBRE_1, \n"+
			" 	NOMBRE_2, \n"+
			" 	NOM_COMPLETO, \n"+
			" 	PORCENTAJE, \n"+
			" 	FECHA_NACIMIENTO, \n"+
			" 	ID_PARENTESCO \n"+
			" FROM SIM_BENEFICIARIO \n"+
			" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_BENEFICIARIO = '" + (String)parametros.getDefCampo("ID_BENEFICIARIO") + "' \n";
			
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
		String sIdBeneficiario = "";
		
		//SE OBTIENE EL SEQUENCE
					   
		sSql = "SELECT SQ01_SIM_BENEFICIARIO.nextval as ID_BENEFICIARIO FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdBeneficiario = rs.getString("ID_BENEFICIARIO");
		}
		
			sSql =  "INSERT INTO SIM_BENEFICIARIO ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_BENEFICIARIO, \n" +
					"ID_CARGO_COMISION, \n" +
					"ID_PRESTAMO, \n" +
					"AP_PATERNO, \n" +
					"AP_MATERNO, \n" +
					"NOMBRE_1, \n" +
					"NOMBRE_2, \n" +
					"NOM_COMPLETO, \n" +
					"PORCENTAJE, \n" +
					"FECHA_NACIMIENTO, \n" +
					"ID_PARENTESCO) \n" +
			        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdBeneficiario + ", \n "+
				"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
				"'" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
				"'" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
				"'" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
				"'" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
				"'" + (String)registro.getDefCampo("PORCENTAJE") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_NACIMIENTO") + "','DD/MM/YYYY'), \n" +
				"'" + (String)registro.getDefCampo("ID_PARENTESCO") + "') \n" ;

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
		sSql =  " UPDATE SIM_BENEFICIARIO SET "+
			" AP_PATERNO			='" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
			" AP_MATERNO			='" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
			" NOMBRE_1			='" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
			" NOMBRE_2			='" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
			" NOM_COMPLETO			='" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
			" PORCENTAJE			='" + (String)registro.getDefCampo("PORCENTAJE") + "', \n" +
			" FECHA_NACIMIENTO		=TO_DATE('" + (String)registro.getDefCampo("FECHA_NACIMIENTO") + "','DD/MM/YYYY'), \n" +
			" ID_PARENTESCO			='" + (String)registro.getDefCampo("ID_PARENTESCO") + "' \n" +
			" WHERE ID_BENEFICIARIO  	='" + (String)registro.getDefCampo("ID_BENEFICIARIO") + "' \n" +
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
		sSql =  " DELETE FROM SIM_BENEFICIARIO " +
			" WHERE ID_BENEFICIARIO  ='" + (String)registro.getDefCampo("ID_BENEFICIARIO") + "' \n" +
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}