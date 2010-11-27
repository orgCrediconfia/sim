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
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra de los adeudos del Cliente.
 */
 
public class SimClienteGarantiaDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"G.CVE_GPO_EMPRESA, \n" +
				"G.CVE_EMPRESA, \n" +
				"G.ID_PERSONA, \n" +
				"G.ID_GARANTIA, \n" +
				"G.ID_TIPO_GARANTIA, \n" +
				"G.NUMERO_FACTURA_ESCRITURA, \n" +
				"G.FECHA_FACTURA_ESCRITURA, \n" +
				"G.VALOR_COMERCIAL, \n" +
				"G.VALOR_A_GARANTIZAR, \n" +
				"G.DESCRIPCION, \n" +
				"G.ID_GARANTE_DEPOSITARIO, \n" +
				"P.NOM_COMPLETO NOM_GARANTE, \n" +
				"G.PORC_CUBRE_GARANTIA \n" +
			" FROM SIM_CLIENTE_GARANTIA G, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE G.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND G.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA = G.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA = G.ID_GARANTE_DEPOSITARIO \n";
			
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
		
		sSql =  "SELECT \n"+
				"G.CVE_GPO_EMPRESA, \n" +
				"G.CVE_EMPRESA, \n" +
				"G.ID_PERSONA, \n" +
				"G.ID_GARANTIA, \n" +
				"G.ID_TIPO_GARANTIA, \n" +
				"C.NOM_TIPO_GARANTIA, \n" +
				"G.NUMERO_FACTURA_ESCRITURA, \n" +
				"G.FECHA_FACTURA_ESCRITURA, \n" +
				"G.VALOR_COMERCIAL, \n" +
				"G.VALOR_A_GARANTIZAR, \n" +
				"G.DESCRIPCION, \n" +
				"G.ID_GARANTE_DEPOSITARIO, \n" +
				"P.NOM_COMPLETO NOM_GARANTE, \n" +
				"G.PORC_CUBRE_GARANTIA \n" +
			"FROM SIM_CLIENTE_GARANTIA G, \n"+
			"     RS_GRAL_PERSONA P, \n"+
			"     SIM_CAT_TIPO_GARANTIA C \n"+
			" WHERE G.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND G.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND G.ID_GARANTIA = '" + (String)parametros.getDefCampo("ID_GARANTIA") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA = G.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA = G.ID_GARANTE_DEPOSITARIO \n"+
			" AND C.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
			" AND C.CVE_EMPRESA = G.CVE_EMPRESA \n"+
			" AND C.ID_TIPO_GARANTIA = G.ID_TIPO_GARANTIA \n";
			
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
		
		String sIdGarantia = "";
		
		//OBTENEMOS EL SEQUENCE	   
		sSql = "SELECT SQ01_SIM_CLIENTE_GARANTIA.nextval as ID_GARANTIA FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdGarantia = rs.getString("ID_GARANTIA");
		}
			
		sSql =  "INSERT INTO SIM_CLIENTE_GARANTIA ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"ID_GARANTIA, \n" +
				"ID_TIPO_GARANTIA, \n" +
				"NUMERO_FACTURA_ESCRITURA, \n" +
				"FECHA_FACTURA_ESCRITURA, \n" +
				"VALOR_COMERCIAL, \n" +
				"VALOR_A_GARANTIZAR, \n" +
				"DESCRIPCION, \n" +
				"ID_GARANTE_DEPOSITARIO, \n" +
				"PORC_CUBRE_GARANTIA) \n" +
		          "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				sIdGarantia + ", \n "+
				"'" + (String)registro.getDefCampo("ID_TIPO_GARANTIA") + "', \n" +
				"'" + (String)registro.getDefCampo("NUMERO_FACTURA_ESCRITURA") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_FACTURA_ESCRITURA") + "','DD/MM/YYYY'), \n" +
				"'" + (String)registro.getDefCampo("VALOR_COMERCIAL") + "', \n" +
				"'" + (String)registro.getDefCampo("VALOR_A_GARANTIZAR") + "', \n" +
				"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GARANTE_DEPOSITARIO") + "', \n" +
				"'" + (String)registro.getDefCampo("PORC_CUBRE_GARANTIA") + "') \n" ;
				
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
		sSql = " UPDATE SIM_CLIENTE_GARANTIA SET "+
			   " DESCRIPCION    		='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
			   " ID_TIPO_GARANTIA    	='" + (String)registro.getDefCampo("ID_TIPO_GARANTIA")  + "', \n" +
			   " NUMERO_FACTURA_ESCRITURA   ='" + (String)registro.getDefCampo("NUMERO_FACTURA_ESCRITURA")  + "', \n" +
			   " VALOR_COMERCIAL    	='" + (String)registro.getDefCampo("VALOR_COMERCIAL")  + "', \n" +
			   " VALOR_A_GARANTIZAR    	='" + (String)registro.getDefCampo("VALOR_A_GARANTIZAR")  + "', \n" +
			   " FECHA_FACTURA_ESCRITURA    = TO_DATE('" + (String)registro.getDefCampo("FECHA_FACTURA_ESCRITURA")  + "','DD/MM/YYYY'), \n" +
			   " ID_GARANTE_DEPOSITARIO    	='" + (String)registro.getDefCampo("ID_GARANTE_DEPOSITARIO")  + "', \n" +
			   " PORC_CUBRE_GARANTIA    	='" + (String)registro.getDefCampo("PORC_CUBRE_GARANTIA")  + "' \n" +
			   " WHERE ID_GARANTIA  	='" + (String)registro.getDefCampo("ID_GARANTIA") + "' \n" +
			   " AND ID_PERSONA 		='" + (String)registro.getDefCampo("ID_PERSONA") + "'\n"+
			   " AND CVE_GPO_EMPRESA 	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			   " AND CVE_EMPRESA 		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		sSql =  " DELETE FROM SIM_CLIENTE_GARANTIA " +
		 	 " WHERE ID_GARANTIA  ='" + (String)registro.getDefCampo("ID_GARANTIA") + "' \n" +
			   " AND ID_PERSONA ='" + (String)registro.getDefCampo("ID_PERSONA") + "'\n"+
			   " AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			   " AND CVE_EMPRESA 	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}