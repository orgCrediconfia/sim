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
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de Documentaci¿ón Envio Sedena Detalle.
 */
 
public class SimUsuarioRegionalDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envï¿½an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
			"CVE_GPO_EMPRESA, \n"+
			"CVE_EMPRESA, \n"+
			"ID_REGIONAL, \n"+ 
			"NOM_REGIONAL \n"+
			"FROM SIM_CAT_REGIONAL \n"+
			"WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"MINUS \n"+
			"SELECT \n"+
			"R.CVE_GPO_EMPRESA, \n"+
			"R.CVE_EMPRESA, \n"+
			"R.ID_REGIONAL, \n"+ 
			"CR.NOM_REGIONAL \n"+
			"FROM SIM_USUARIO_REGIONAL R, \n"+
			"     SIM_CAT_REGIONAL CR \n"+
			"WHERE R.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND R.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND R.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
			"AND CR.CVE_GPO_EMPRESA = R.CVE_GPO_EMPRESA \n"+
			"AND CR.CVE_EMPRESA = R.CVE_EMPRESA \n"+
			"AND CR.ID_REGIONAL = R.ID_REGIONAL \n";
			
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
				"UR.CVE_GPO_EMPRESA, \n"+
				"UR.CVE_EMPRESA, \n"+
				"UR.CVE_USUARIO, \n"+	
				"UR.ID_REGIONAL, \n"+ 
				"CR.NOM_REGIONAL \n"+
		    	"FROM  SIM_USUARIO_REGIONAL UR, \n"+
			"      RS_GRAL_USUARIO GU, \n"+
			"      SIM_CAT_REGIONAL CR \n"+
			" WHERE UR.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND UR.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND UR.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
			" AND UR.ID_REGIONAL = '" + (String)parametros.getDefCampo("ID_REGIONAL") + "' \n"+
			" AND GU.CVE_GPO_EMPRESA = UR.CVE_GPO_EMPRESA \n"+
			" AND GU.CVE_EMPRESA = UR.CVE_EMPRESA  \n"+
			" AND GU.CVE_USUARIO =  UR.CVE_USUARIO \n"+	
			" AND UR.CVE_GPO_EMPRESA = CR.CVE_GPO_EMPRESA \n"+
			" AND UR.CVE_EMPRESA = CR.CVE_EMPRESA  \n"+
			" AND UR.ID_REGIONAL =  CR.ID_REGIONAL \n";
		
				 
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
	
		String sIdSucursal = "";
	
		sSql =  "INSERT INTO SIM_USUARIO_REGIONAL ( \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"CVE_USUARIO, \n" +
			"ID_REGIONAL) \n" +
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_REGIONAL") + "') \n" ;

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  "SELECT  \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_REGIONAL, \n"+
				"ID_SUCURSAL \n"+
			"FROM SIM_REGIONAL_SUCURSAL \n" +
			"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_REGIONAL ='" + (String)registro.getDefCampo("ID_REGIONAL") + "' \n";
		
		ejecutaSql();		
		while (rs.next()){
			
			sIdSucursal = rs.getString("ID_SUCURSAL");
			
			sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n"+
				"ID_SUCURSAL \n"+
			"FROM \n"+
				"SIM_USUARIO_SUCURSAL \n"+
			"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CVE_USUARIO ='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
			"AND ID_SUCURSAL ='" + sIdSucursal +"' \n";
			
			PreparedStatement ps1 = this.conn.prepareStatement(sSql);
			ps1.execute();
			ResultSet rs1 = ps1.getResultSet();
			
			if (!rs1.next()){
				
				sSql =  "INSERT INTO SIM_USUARIO_SUCURSAL ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n" +
				"ID_SUCURSAL) \n" +
			        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
				"'" + sIdSucursal +"') \n" ;
	
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ps2.close();
			}
			rs1.close();
			ps1.close();
		}
		
		return resultadoCatalogo;
	}
	

	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		String sIdSucursal = "";
		
		//BORRA LA FUNCION
		sSql =  "DELETE FROM SIM_USUARIO_REGIONAL " +
			" WHERE ID_REGIONAL			='" + (String)registro.getDefCampo("ID_REGIONAL") + "' \n" +
			" AND CVE_USUARIO			='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
			" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
			
			sSql =  "SELECT \n"+
			 	"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_SUCURSAL, \n"+
				"ID_REGIONAL \n"+
				"FROM \n"+
					"SIM_REGIONAL_SUCURSAL \n"+
				" WHERE ID_REGIONAL ='" + (String)registro.getDefCampo("ID_REGIONAL") + "' \n" ;
			ejecutaSql();
			
			while (rs.next()){
				
				sIdSucursal = rs.getString("ID_SUCURSAL");
			
				/*
				sSql =  "SELECT \n"+
				 	"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_SUCURSAL, \n"+
					"CVE_USUARIO \n"+
					"FROM \n"+
						"SIM_USUARIO_SUCURSAL \n"+
					" WHERE CVE_USUARIO ='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
					" AND ID_SUCURSAL ='" + sIdSucursal + "' \n";
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ps1.close();
				
				
				*/
					sSql = "DELETE FROM SIM_USUARIO_SUCURSAL" +
					       " WHERE ID_SUCURSAL		='" + sIdSucursal + "' \n"+
					       " AND CVE_USUARIO		='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
					       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					PreparedStatement ps2 = this.conn.prepareStatement(sSql);
					ps2.execute();
					ps2.close();
				
			}
		

		return resultadoCatalogo;
	}
}