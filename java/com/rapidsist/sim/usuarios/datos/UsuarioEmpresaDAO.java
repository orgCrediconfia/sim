/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.usuarios.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para los usuarios del sistema.
 */
 
public class UsuarioEmpresaDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{

		sSql =   " SELECT \n"+
			 " 	U.CVE_GPO_EMPRESA, \n"+
			 " 	U.CVE_EMPRESA, \n"+
			 " 	U.CVE_USUARIO, \n"+
			 " 	U.ID_PERSONA, \n"+
			 " 	U.CVE_PUESTO, \n"+
			 "	U.CVE_PERFIL, \n" +
			 " 	U.LOCALIDAD, \n" +
			 " 	U.NOM_ALIAS, \n" +
			 " 	U.PASSWORD, \n" +
			 " 	U.NUM_NOMINA, \n" +
			 " 	U.FECHA_INGRESO_EMPRESA, \n" +
			 " 	P.AP_PATERNO, \n" +
			 " 	P.AP_MATERNO, \n" +
			 " 	P.NOMBRE_1, \n" +
			 " 	P.NOMBRE_2, \n" +
			 " 	P.NOM_COMPLETO, \n" +
			 "  P.EMAIL, \n" +
			 " 	C.NOM_PUESTO \n" +
			" FROM  RS_GRAL_USUARIO U, \n" +
			"	RS_GRAL_PERSONA P, \n" +
			"	SIM_CAT_PUESTO C \n" +
			" WHERE U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND U.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND P.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA (+)= U.CVE_EMPRESA \n" +
			" AND P.ID_PERSONA (+)= U.ID_PERSONA \n" +
			" AND C.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n" +
			" AND C.CVE_EMPRESA (+)= U.CVE_EMPRESA \n" +
			" AND C.CVE_PUESTO (+)= U.CVE_PUESTO \n" ;
			//" AND P.FECHA_BAJA_LOGICA IS NULL \n" ;
			
		if (parametros.getDefCampo("CLAVE_USUARIO") != null) {
			sSql = sSql + " AND U.CVE_USUARIO = '" + (String) parametros.getDefCampo("CLAVE_USUARIO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
		}
		
		sSql = sSql + " ORDER BY CVE_USUARIO \n";
		
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
		sSql =   " SELECT \n"+
			 " 	U.CVE_GPO_EMPRESA, \n"+
			 " 	U.CVE_EMPRESA, \n"+
			 " 	U.CVE_USUARIO, \n"+
			 " 	U.ID_PERSONA, \n"+
			 " 	U.CVE_PUESTO, \n"+
			 "	U.CVE_PERFIL, \n" +
			 " 	U.LOCALIDAD, \n" +
			 " 	U.NOM_ALIAS, \n" +
			 " 	U.PASSWORD, \n" +
			 " 	U.NUM_NOMINA, \n" +
			 " 	U.FECHA_INGRESO_EMPRESA, \n" +
			 " 	P.AP_PATERNO, \n" +
			 " 	P.AP_MATERNO, \n" +
			 " 	P.NOMBRE_1, \n" +
			 " 	P.NOMBRE_2, \n" +
			 " 	P.NOM_COMPLETO, \n" +
			 "  P.EMAIL, \n" +
			 " 	P.B_VIGENCIA, \n" +
			 " 	P.ID_SUCURSAL, \n" +
			 "  P.FECHA_BAJA_LOGICA, \n" +
			 "	B_REGIONALES \n" +
			" FROM  RS_GRAL_USUARIO U, \n" +
			"	RS_GRAL_PERSONA P \n" +
			" WHERE U.CVE_USUARIO  = '" + (String) parametros.getDefCampo("CVE_USUARIO") + "' \n" +
			" AND U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND U.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND P.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA (+)= U.CVE_EMPRESA \n" +
			" AND P.ID_PERSONA (+)= U.ID_PERSONA \n" ;
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sNomCompleto = "";
		String sIdDomicilio = "";
		String sIdPersona = "";
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_GRAL_PERSONA.nextval as ID_PERSONA FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdPersona = rs.getString("ID_PERSONA");
		}

		sSql =  "INSERT INTO RS_GRAL_PERSONA ( "+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"AP_PATERNO, \n" +
				"AP_MATERNO, \n" +
				"NOMBRE_1, \n" +
				"NOMBRE_2, \n" +
				"NOM_COMPLETO, \n" +
				"EMAIL, \n" +
				"B_VIGENCIA, \n" +
				"ID_SUCURSAL, \n" +
				"B_PERSONA_FISICA) \n" +
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdPersona + ", \n" +
			"'" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
			"'" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
			"'" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
			"'" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
			"'" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
			"'" + (String)registro.getDefCampo("EMAIL") + "', \n" +
			"'V', \n" +
			"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
			"'V') \n" ;
					
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		/*
		sSql =  "INSERT INTO SIM_TIPO_PERSONA ( "+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_PERSONA, \n" +
			"CVE_TIPO_PERSONA) \n" +		
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdPersona + ", \n" +
			"'USUARIO') \n" ;
			
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		*/
		sSql = "INSERT INTO RS_GRAL_USUARIO ( "+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n" +
				"ID_PERSONA, \n" +
				"CVE_PUESTO, \n" +
				"CVE_PERFIL, \n" +
				"LOCALIDAD, \n" +
				"PASSWORD, \n" +
				"CVE_USUARIO_AUTENTIFICACION, \n" +
				"CVE_ROL_APLICACION_WEB, \n" +
				"FECHA_INGRESO, \n" +
				"NUM_NOMINA, \n" +
				"FECHA_INGRESO_EMPRESA, \n" +
				"B_REGIONALES) \n" +
			" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CLAVE_USUARIO") + "', \n" +
				sIdPersona + ", \n" +
				"'" + (String)registro.getDefCampo("CVE_PUESTO") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_PERFIL") + "', \n" +
				"'HOLA', \n" +
				"'" + (String)registro.getDefCampo("PASSWORD") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + (String)registro.getDefCampo("CLAVE_USUARIO") + "', \n" +
				"'AGSA', \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("NUM_NOMINA") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_INGRESO_EMPRESA") + "','DD/MM/YYYY'), \n" +
				"'" + (String)registro.getDefCampo("B_REGIONALES") + "') \n" ;
					
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("USUARIO", (String)registro.getDefCampo("CLAVE_USUARIO"));
		resultadoCatalogo.Resultado.addDefCampo("TODAS_REGIONALES", (String)registro.getDefCampo("B_REGIONALES"));
		resultadoCatalogo.Resultado.addDefCampo("ID_PERSONA", sIdPersona);
			
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
		resultadoCatalogo.Resultado = new Registro();

		//ACTUALIZA EL USUARIO
		sSql =  " UPDATE RS_GRAL_USUARIO SET "+
			" NUM_NOMINA = '" + (String)registro.getDefCampo("NUM_NOMINA") + "', \n" +
			" FECHA_INGRESO_EMPRESA = TO_DATE('" + (String)registro.getDefCampo("FECHA_INGRESO_EMPRESA") + "','DD/MM/YYYY'), \n" +
			" CVE_PUESTO = '" + (String)registro.getDefCampo("CVE_PUESTO") + "', \n" +
			" LOCALIDAD = '" + (String)registro.getDefCampo("LOCALIDAD") + "', \n" +
			" CVE_PERFIL = '" + (String)registro.getDefCampo("CVE_PERFIL") + "', \n" ;
		
			//SI EL PASSWORD ESTA VACIO CONSERVAR EL ACTUAL EN BASE DE DATOS
			
			if (!registro.getDefCampo("PASSWORD").equals("")){
				sSql = sSql + "PASSWORD = '" + (String)registro.getDefCampo("PASSWORD")+"' ,";
			}
			
		sSql= sSql+ " B_REGIONALES = '" + (String)registro.getDefCampo("B_REGIONALES") + "' \n" +
			" WHERE CVE_USUARIO = '" + (String)registro.getDefCampo("CLAVE_USUARIO") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
			
		//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("USUARIO", (String)registro.getDefCampo("CLAVE_USUARIO"));
		resultadoCatalogo.Resultado.addDefCampo("TODAS_REGIONALES", (String)registro.getDefCampo("B_REGIONALES"));
		resultadoCatalogo.Resultado.addDefCampo("ID_PERSONA", (String)registro.getDefCampo("ID_PERSONA"));
		
		//ACTUALIZA LA PERSONA
		sSql =  " UPDATE RS_GRAL_PERSONA SET "+
			" AP_PATERNO = '" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
			" AP_MATERNO = '" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
			" NOMBRE_1 = '" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
			" NOMBRE_2 = '" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
			" NOM_COMPLETO = '" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
			" EMAIL = '" + (String)registro.getDefCampo("EMAIL") + "', \n" +
			" B_VIGENCIA = '" + (String)registro.getDefCampo("B_VIGENCIA") + "' \n" +
			" WHERE ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
			
		//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
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
		sSql = " UPDATE RS_GRAL_PERSONA SET " +
	 	       " FECHA_BAJA_LOGICA    = SYSDATE, \n" +
	 	       " B_VIGENCIA    		  = 'F' \n" +
		       " WHERE ID_PERSONA     ='" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
		       " AND CVE_GPO_EMPRESA  = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		       " AND CVE_EMPRESA      = '" + (String) registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
	//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
		if (ejecutaUpdate() == 0) {
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql = " UPDATE RS_GRAL_USUARIO SET " +
	       " FECHA_BAJA_LOGICA    = SYSDATE \n" +
	       " WHERE ID_PERSONA     ='" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
	       " AND CVE_GPO_EMPRESA  = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
	       " AND CVE_EMPRESA      = '" + (String) registro.getDefCampo("CVE_EMPRESA") + "' \n";

//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
	if (ejecutaUpdate() == 0) {
		resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
	}
		return resultadoCatalogo;
	}
}