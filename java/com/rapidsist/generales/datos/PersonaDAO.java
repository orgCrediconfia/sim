/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de personas.
 */
 
public class PersonaDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		boolean bNomCompleto = false;
		sSql = "SELECT \n"+
				" P.CVE_GPO_EMPRESA, \n"+
				" P.ID_PERSONA, \n"+
				" P.NOM_COMPLETO, \n"+
				" P.RFC \n"+
				" FROM RS_GRAL_PERSONA P \n"+
				" WHERE ";

		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " P.NOM_COMPLETO LIKE'%" + (String) parametros.getDefCampo("NOM_COMPLETO") + "%' \n";
			bNomCompleto = true;
		}
		if (parametros.getDefCampo("RFC") != null) {
			sSql = sSql + (bNomCompleto ? " AND" : "") + " P.RFC = '" + (String) parametros.getDefCampo("RFC") + "' \n";
			bNomCompleto = true;
		}
		if (parametros.getDefCampo("B_PERSONA_FISICA") != null) {
			sSql = sSql + (bNomCompleto ? " AND" : "") + " P.B_PERSONA_FISICA = '" + (String) parametros.getDefCampo("B_PERSONA_FISICA") + "' \n";
			bNomCompleto = true;
		}

		sSql = sSql + (bNomCompleto ? " AND" : "") + " P.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					  " ORDER BY P.NOM_COMPLETO ASC\n";
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
				 " P.ID_PERSONA, \n"+
				 " P.ID_DOMICILIO, \n"+
				 " P.AP_PATERNO, \n"+
				 " P.AP_MATERNO, \n"+
				 " P.NOM_PERSONA, \n" +
				 " P.EMAIL, \n" +
				 " P.NOM_COMPLETO, \n" +
				 " P.RFC, \n" +
				 " P.CURP, \n" +
				 " P.NUM_TEL, \n" +
				 " P.B_PERSONA_FISICA, \n" +
				 " DI.CVE_PAIS, "+
				 " DI.CALLE, \n" +
				 " DI.NUMERO, \n" +
				 " DI.CODIGO_POSTAL, \n" +
				 " DI.ID_REFER_POST, \n" +
				 " DI.CVE_CODIGO_POSTAL, \n" +
				 " DI.NOM_ASENTAMIENTO EX_NOM_ASENTAMIENTO, \n" +
				 " DI.TIPO_ASENTAMIENTO EX_TIPO_ASENTAMIENTO, \n" +
				 " DI.NOM_DELEGACION EX_NOM_DELEGACION, \n" +
				 " DI.NOM_CIUDAD EX_NOM_CIUDAD, \n" +
				 " DI.NOM_ESTADO EX_NOM_ESTADO, \n" +
				 " CP.NOM_ASENTAMIENTO, \n" +
				 " CP.TIPO_ASENTAMIENTO, \n" +
				 " CP.NOM_DELEGACION, \n" +
				 " CP.NOM_CIUDAD, \n" +
				 " CP.NOM_ESTADO \n" +
			" FROM RS_GRAL_PERSONA P, RS_GRAL_CODIGO_POSTAL CP, RS_GRAL_DOMICILIO DI \n" +
			" WHERE P.ID_PERSONA  = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n" +
				" AND P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND P.CVE_GPO_EMPRESA=DI.CVE_GPO_EMPRESA (+) \n" +
				" AND P.ID_DOMICILIO = DI.ID_DOMICILIO (+) \n" +
				" AND DI.CODIGO_POSTAL = CP.CODIGO_POSTAL (+)\n" +
				" AND DI.ID_REFER_POST = CP.ID_REFER_POST (+)\n";
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
		String sNomCompleto = null;
		String sIdDomicilio = "";
		String sIdPersona = "";

		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_GRAL_DOMICILIO.nextval as domicilio FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdDomicilio = rs.getString("domicilio");
		}

		//INSERTA EL REGISTRO DE DOMICILIO PARA PODER OBTENER EL ID_DOMICILIO DEL SEQUENCE
		sSql = "INSERT INTO RS_GRAL_DOMICILIO ( "+
			   "CVE_GPO_EMPRESA, \n" +
			   "ID_DOMICILIO, \n" +
			   "CODIGO_POSTAL, \n" +
			   "ID_REFER_POST, \n" +
			   "CVE_PAIS, \n" +
			   "CALLE, \n" +
			   "NUMERO, \n" +
			   "TX_REFERENCIA, \n" +
			   "CVE_CODIGO_POSTAL, \n" +
			   "NOM_ASENTAMIENTO, \n" +
			   "TIPO_ASENTAMIENTO, \n" +
			   "NOM_DELEGACION, \n" +
			   "NOM_CIUDAD, \n" +
			   "NOM_ESTADO) \n" +
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			" " + sIdDomicilio +", \n" +
			"'" + (String)registro.getDefCampo("CODIGO_POSTAL") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_REFER_POST") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_PAIS") + "', \n" +
			"'" + (String)registro.getDefCampo("CALLE") + "', \n" +
			"'" + (String)registro.getDefCampo("NUMERO") + "', \n" +
			"'" + (String)registro.getDefCampo("TX_REFERENCIA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_CODIGO_POSTAL") + "', \n" +
			"'" + (String)registro.getDefCampo("NOM_ASENTAMIENTO") + "', \n" +
			"'" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO") + "', \n" +
			"'" + (String)registro.getDefCampo("NOM_DELEGACION") + "', \n" +
			"'" + (String)registro.getDefCampo("NOM_CIUDAD") + "', \n" +
			"'" + (String)registro.getDefCampo("NOM_ESTADO") + "')";

		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_GRAL_PERSONA.nextval as ID_PERSONA FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdPersona = rs.getString("ID_PERSONA");
		}

		sSql = "INSERT INTO RS_GRAL_PERSONA ( "+
					"CVE_GPO_EMPRESA, \n" +
					"ID_PERSONA, \n" +
					"ID_DOMICILIO, \n" +
					"AP_PATERNO, \n" +
					"AP_MATERNO, \n" +
					"NOM_PERSONA, \n" +
					"EMAIL, \n" +
					"NOM_COMPLETO, \n" +
					"RFC, \n" +
					"CURP, \n" +
					"NUM_TEL, \n" +
					"B_PERSONA_FISICA) \n" +
				" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					sIdPersona + ", \n" +
					" " + sIdDomicilio +", \n" +
					"'" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
					"'" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_PERSONA") + "', \n" +
					"'" + (String)registro.getDefCampo("EMAIL") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
					"'" + (String)registro.getDefCampo("RFC") + "', \n" +
					"'" + (String)registro.getDefCampo("CURP") + "', \n" +
					"'" + (String)registro.getDefCampo("NUM_TEL") + "', \n" +
					"'" + (String)registro.getDefCampo("B_PERSONA_FISICA") + "')";
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		else{
			resultadoCatalogo.Resultado = new Registro();
			resultadoCatalogo.Resultado.addDefCampo("SEQUENCE", sIdPersona);
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
		String sNomCompleto = null;

		sSql = "UPDATE RS_GRAL_PERSONA SET "+
					"AP_PATERNO ='" + (String)registro.getDefCampo("AP_PATERNO") + "', " +
					"AP_MATERNO ='" + (String)registro.getDefCampo("AP_MATERNO") + "', " +
					"NOM_PERSONA ='" + (String)registro.getDefCampo("NOM_PERSONA") + "', " +
					"EMAIL ='" + (String)registro.getDefCampo("EMAIL") + "', " ;

		if (registro.getDefCampo("B_PERSONA_FISICA").equals("V")){
			if (registro.getDefCampo("NOM_COMPLETO").equals(" ")){
				sNomCompleto = registro.getDefCampo("NOM_PERSONA") + " " + registro.getDefCampo("AP_PATERNO") + " " + registro.getDefCampo("AP_MATERNO");
				registro.addDefCampo("NOM_COMPLETO", sNomCompleto);
				sSql= sSql + "NOM_COMPLETO ='" + (String)registro.getDefCampo("NOM_COMPLETO") + "', " ;
			}
			sSql= sSql + "NOM_COMPLETO ='" + (String)registro.getDefCampo("NOM_COMPLETO") + "', " ;
		}else{
			 sSql= sSql + "NOM_COMPLETO ='" + (String)registro.getDefCampo("NOM_COMPLETO") + "', " ;
		}

		sSql = sSql +"RFC ='" + (String)registro.getDefCampo("RFC") + "', " +
					"CURP = '" + (String)registro.getDefCampo("CURP") + "', " +
					"NUM_TEL = '" + (String)registro.getDefCampo("NUM_TEL") + "', " +
					"B_PERSONA_FISICA = '" + (String)registro.getDefCampo("B_PERSONA_FISICA") + "' " +
				" WHERE ID_PERSONA = " + (String)registro.getDefCampo("ID_PERSONA")+
				" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"'";
		//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		//ACTUALIZA EL DOMICILIO ************
		sSql = " UPDATE RS_GRAL_DOMICILIO SET "+
			     " CODIGO_POSTAL ='" + (String)registro.getDefCampo("CODIGO_POSTAL") + "', \n" +
			     " ID_REFER_POST ='" + (String)registro.getDefCampo("ID_REFER_POST") + "', \n" +
			     " CVE_PAIS ='" + (String)registro.getDefCampo("CVE_PAIS") + "', \n" +
			     " CALLE ='" + (String)registro.getDefCampo("CALLE") + "', \n" +
			     " NUMERO ='" + (String)registro.getDefCampo("NUMERO") + "', \n" +
			     " TX_REFERENCIA ='" + (String)registro.getDefCampo("TX_REFERENCIA") + "', \n" +
			     " CVE_CODIGO_POSTAL ='" + (String)registro.getDefCampo("CVE_CODIGO_POSTAL") + "', \n" +
			     " NOM_ASENTAMIENTO ='" + (String)registro.getDefCampo("NOM_ASENTAMIENTO") + "', \n" +
			     " TIPO_ASENTAMIENTO ='" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO") + "', \n" +
			     " NOM_DELEGACION ='" + (String)registro.getDefCampo("NOM_DELEGACION") + "', \n" +
   			     " NOM_CIUDAD ='" + (String)registro.getDefCampo("NOM_CIUDAD") + "', \n" +
  			     " NOM_ESTADO ='" + (String)registro.getDefCampo("NOM_ESTADO") + "' \n" +
			   " WHERE ID_DOMICILIO = " + (String)registro.getDefCampo("ID_DOMICILIO");

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
		sSql = "SELECT * FROM RS_GRAL_USUARIO \n " +
				 " WHERE ID_PERSONA = " + (String)registro.getDefCampo("ID_PERSONA")+
				 " AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"'";
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
		}
		else{
			sSql = "DELETE FROM RS_GRAL_PERSONA \n " +
					" WHERE ID_PERSONA = " + (String)registro.getDefCampo("ID_PERSONA")+
					" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"'";
			//VERIFICA SI BORRO EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			//BORRA EL DOMICILIO DE LA PERSONA
			sSql = "DELETE FROM RS_GRAL_DOMICILIO \n " +
					" WHERE ID_DOMICILIO = " + (String)registro.getDefCampo("ID_DOMICILIO");
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}

		}
		return resultadoCatalogo;
	}
}