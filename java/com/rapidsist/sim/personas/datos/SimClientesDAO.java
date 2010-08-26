/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.datos;

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
 * Administra los accesos a la base de datos para los clientes.
 */
 
public class SimClientesDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT DISTINCT \n"+
			"P.ID_PERSONA, \n"+
			"P.NOM_COMPLETO, \n"+
			"P.ID_SUCURSAL, \n"+
			"S.NOM_SUCURSAL \n"+
			"FROM RS_GRAL_PERSONA P, \n"+
			"      SIM_TIPO_PERSONA TP, \n"+
			"      SIM_CAT_SUCURSAL S, \n"+
			"      SIM_USUARIO_ACCESO_SUCURSAL US \n"+
			"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND TP.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND TP.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND TP.ID_PERSONA = P.ID_PERSONA \n"+
			"AND P.FECHA_BAJA_LOGICA IS NULL \n"+
			"AND TP.CVE_TIPO_PERSONA != 'EXCONYUGE' \n"+
			"AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND S.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND S.ID_SUCURSAL = P.ID_SUCURSAL \n"+
			"AND US.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND US.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND US.ID_SUCURSAL = P.ID_SUCURSAL \n"+
			"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
				
		if (!parametros.getDefCampo("CVE_TIPO_PERSONA").equals("")) {
			sSql = sSql + "AND TP.CVE_TIPO_PERSONA = '" + (String) parametros.getDefCampo("CVE_TIPO_PERSONA") + "' \n";
		}
		
		if (parametros.getDefCampo("ID_PERSONA") != null) {
			sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
		}
		if (parametros.getDefCampo("RFC") != null) {
			sSql = sSql + " AND P.RFC LIKE '%" + (String) parametros.getDefCampo("RFC") + "%' \n";
		}
	
		sSql = sSql + " ORDER BY P.ID_PERSONA \n";
		
		System.out.println("consulta de personas"+sSql);
	
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
		sSql = "SELECT \n"+
				 " P.CVE_GPO_EMPRESA, \n"+
				 " P.CVE_EMPRESA, \n"+
				 " P.ID_PERSONA, \n"+
				 " P.AP_PATERNO, \n"+
				 " P.AP_MATERNO, \n"+
				 " P.NOMBRE_1, \n" +
				 " P.NOMBRE_2, \n" +
				 " P.NOM_COMPLETO, \n" +
				 " P.NOMBRE_ALTERNO, \n" +
				 " P.SEXO, \n" +
				 " P.ESTADO_CIVIL, \n" +
				 " P.REGIMEN_MARITAL, \n" +
				 " P.FECHA_NACIMIENTO, \n" +
				 " P.IDENTIFICACION_OFICIAL, \n" +
				 " P.NUM_IDENTIFICACION_OFICIAL, \n" +
				 " P.RFC, \n" +
				 " P.CURP, \n" +
				 " P.NUM_DEPENDIENTES_ECONOMICOS, \n" +
				 " P.ID_ESCOLARIDAD, \n" +	
				 " P.LISTA_NEGRA, \n" +
				 " P.ID_EX_CONYUGE, \n"+
				 " P.CVE_ASESOR_CREDITO, \n" +
				 " P.ID_SUCURSAL, \n" +
				 " S.NOM_SUCURSAL, \n" +
				 " NA.NOM_COMPLETO NOMBRE_ASESOR \n"+
			" FROM RS_GRAL_PERSONA P, \n" +
			"      RS_GRAL_USUARIO UA, \n"+
			"      RS_GRAL_PERSONA NA, \n"+
			"      SIM_CAT_SUCURSAL S \n"+
			" WHERE P.ID_PERSONA  = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n" +
			" AND P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND UA.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
			" AND UA.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
			" AND UA.CVE_USUARIO (+)= P.CVE_ASESOR_CREDITO \n"+
			" AND NA.CVE_GPO_EMPRESA (+)= UA.CVE_GPO_EMPRESA \n"+
			" AND NA.CVE_EMPRESA (+)= UA.CVE_EMPRESA \n"+
			" AND NA.ID_PERSONA (+)= UA.ID_PERSONA \n"+
			" AND S.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
			" AND S.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
			" AND S.ID_SUCURSAL (+)= P.ID_SUCURSAL \n";
			
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
	
		String sIdPersona = "";
		String sNomCompleto = "";
		String sNombre = "";
		
		sNomCompleto = (String)registro.getDefCampo("NOM_COMPLETO");
		
		sNombre = sNomCompleto.replace("  "," ");
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_GRAL_PERSONA.nextval as ID_PERSONA FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdPersona = rs.getString("ID_PERSONA");
		}

		sSql = "INSERT INTO RS_GRAL_PERSONA ( "+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"AP_PATERNO, \n" +
				"AP_MATERNO, \n" +
				"NOMBRE_1, \n" +
				"NOMBRE_2, \n" +
				"NOM_COMPLETO, \n" +
				"NOMBRE_ALTERNO, \n" +
				"SEXO, \n" +
				"ESTADO_CIVIL, \n" +
				"REGIMEN_MARITAL, \n" +
				"FECHA_NACIMIENTO, \n" +
				"IDENTIFICACION_OFICIAL, \n" +
				"NUM_IDENTIFICACION_OFICIAL, \n" +
				"RFC, \n" +
				"CURP, \n" +
				"NUM_DEPENDIENTES_ECONOMICOS, \n" +
				"ID_ESCOLARIDAD, \n" +
				"LISTA_NEGRA, \n" +
				"B_PERSONA_FISICA, \n" +
				"CVE_ASESOR_CREDITO, \n" +
				"ID_SUCURSAL) \n" +
			" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdPersona + ", \n" +
				"'" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
				"'" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
				"'" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
				"'" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
				"'"+ sNombre +"', \n" +
				"'" + (String)registro.getDefCampo("NOMBRE_ALTERNO") + "', \n" +
				"'" + (String)registro.getDefCampo("SEXO") + "', \n" +
				"'" + (String)registro.getDefCampo("ESTADO_CIVIL") + "', \n" +
				"'" + (String)registro.getDefCampo("REGIMEN_MARITAL") + "', \n" +
				" TO_DATE('" + registro.getDefCampo("FECHA_NACIMIENTO") + "','DD/MM/YYYY'), \n" +
				"'" + (String)registro.getDefCampo("IDENTIFICACION_OFICIAL") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_IDENTIFICACION_OFICIAL") + "', \n" +
				"'" + (String)registro.getDefCampo("RFC") + "', \n" +
				"'" + (String)registro.getDefCampo("CURP") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_DEPENDIENTES_ECONOMICOS") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_ESCOLARIDAD") + "', \n" +
				"'F', \n" +					
				"'V', \n" +
				"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "') \n" ;
			
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_PERSONA", sIdPersona);
		
		sSql =  "INSERT INTO SIM_TIPO_PERSONA ( "+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_PERSONA, \n" +
			"CVE_TIPO_PERSONA) \n" +		
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdPersona + ", \n" +
			"'PERSONA') \n" ;
			
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
		
		sSql =  "UPDATE RS_GRAL_PERSONA SET "+
				"AP_PATERNO = '" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
				"AP_MATERNO = '" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
				"NOMBRE_1 = '" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
				"NOMBRE_2 = '" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
				"NOM_COMPLETO = '" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
				"NOMBRE_ALTERNO = '" + (String)registro.getDefCampo("NOMBRE_ALTERNO") + "', \n" +
				"SEXO = '" + (String)registro.getDefCampo("SEXO") + "', \n" +
				"ESTADO_CIVIL = '" + (String)registro.getDefCampo("ESTADO_CIVIL") + "', \n" +
				"REGIMEN_MARITAL = '" + (String)registro.getDefCampo("REGIMEN_MARITAL") + "', \n" +
				"FECHA_NACIMIENTO = TO_DATE('" + (String)registro.getDefCampo("FECHA_NACIMIENTO")  + "','DD/MM/YYYY'), \n" +
				"IDENTIFICACION_OFICIAL	= '" + (String)registro.getDefCampo("IDENTIFICACION_OFICIAL") + "', \n" +
				"NUM_IDENTIFICACION_OFICIAL = '" + (String)registro.getDefCampo("NUM_IDENTIFICACION_OFICIAL") + "', \n" +
				"RFC = '" + (String)registro.getDefCampo("RFC") + "', \n" +
				"CURP = '" + (String)registro.getDefCampo("CURP") + "', \n" +
				"NUM_DEPENDIENTES_ECONOMICOS = '" + (String)registro.getDefCampo("NUM_DEPENDIENTES_ECONOMICOS") + "', \n" +
				"ID_ESCOLARIDAD	= '" + (String)registro.getDefCampo("ID_ESCOLARIDAD") + "', \n" +
				"LISTA_NEGRA = '" + (String)registro.getDefCampo("LISTA_NEGRA") + "', \n" +
				"B_PERSONA_FISICA = 'V', \n" +
				"CVE_ASESOR_CREDITO = '" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
				"ID_SUCURSAL = '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n" +
			"WHERE ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA")+ "' \n" +
			"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
		
		System.out.println("modifica sucursal-asesor");
			
		//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		if (registro.getDefCampo("MODIFICACION_CONYUGE").equals("SI")) {
			sSql =  "UPDATE RS_GRAL_PERSONA SET "+
					"AP_PATERNO = '" + (String)registro.getDefCampo("AP_PATERNO_EX_CONYUGE") + "', \n" +
					"AP_MATERNO = '" + (String)registro.getDefCampo("AP_MATERNO_EX_CONYUGE") + "', \n" +
					"NOMBRE_1 = '" + (String)registro.getDefCampo("NOMBRE_1_EX_CONYUGE") + "', \n" +
					"NOMBRE_2 = '" + (String)registro.getDefCampo("NOMBRE_2_EX_CONYUGE") + "', \n" +
					"NOM_COMPLETO = '" + (String)registro.getDefCampo("NOM_COMPLETO_EX_CONYUGE") + "', \n" +
					"SEXO = '" + (String)registro.getDefCampo("SEXO_EX_CONYUGE") + "', \n" +
					"FECHA_NACIMIENTO = TO_DATE('" + (String)registro.getDefCampo("FECHA_NACIMIENTO_EX_CONYUGE")  + "','DD/MM/YYYY'), \n" +
					"IDENTIFICACION_OFICIAL	= '" + (String)registro.getDefCampo("IDENTIFICACION_OFICIAL_EX_CONYUGE") + "', \n" +
					"NUM_IDENTIFICACION_OFICIAL = '" + (String)registro.getDefCampo("NUM_IDENTIFICACION_OFICIAL_EX_CONYUGE") + "' \n" +
				"WHERE ID_PERSONA = '" + (String)registro.getDefCampo("ID_EX_CONYUGE")+ "' \n" +
				"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n" +
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA")+"' \n" ;
				
			//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
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
		sSql =  " UPDATE RS_GRAL_PERSONA SET " +
		   	" FECHA_BAJA_LOGICA     = SYSDATE \n" +
		   	" WHERE ID_PERSONA 	='" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
		   	" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		   	" AND CVE_EMPRESA       ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}