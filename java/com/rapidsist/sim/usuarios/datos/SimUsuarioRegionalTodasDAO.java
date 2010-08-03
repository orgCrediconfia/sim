/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.usuarios.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.*;
//import com.rapidsist.sim.usuarios.datos.SimUsuarioRegionalSucursalTodasDAO;


/**
 * Administra los accesos a la base de datos de todas las regionales de un usuario.
 */
 
public class SimUsuarioRegionalTodasDAO extends Conexion2 implements OperacionAlta, OperacionModificacion  {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		ResultadoCatalogo resultadoCatalogoRegionalSucursal = new  ResultadoCatalogo();
		SimUsuarioRegionalSucursalTodasDAO RegionalSucursalDAO = new SimUsuarioRegionalSucursalTodasDAO();
		
		String sIdRegional = "";
		String sClaveUsuario = (String)registro.getDefCampo("CLAVE_USUARIO");
		
		sSql =  " SELECT \n"+
				" CVE_GPO_EMPRESA, \n"+
				" CVE_EMPRESA, \n"+
				" ID_REGIONAL \n"+
			" FROM SIM_CAT_REGIONAL \n"+
			" WHERE CVE_GPO_EMPRESA ='" + registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + registro.getDefCampo("CVE_EMPRESA") + "' \n";
		ejecutaSql();
		
		while (rs.next()){
			
			sIdRegional = rs.getString("ID_REGIONAL");
			
			sSql =  "INSERT INTO SIM_USUARIO_REGIONAL ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"CVE_USUARIO, \n"+
					"ID_REGIONAL) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CLAVE_USUARIO") + "', \n" +
					"'" + sIdRegional +"') \n" ;
			
			PreparedStatement ps1 = this.conn.prepareStatement(sSql);
			ps1.execute();
			ResultSet rs1 = ps1.getResultSet();		
			
		}
		
		RegionalSucursalDAO.setConexion(this.getConexion());
		resultadoCatalogoRegionalSucursal = RegionalSucursalDAO.alta(registro);
		
		
		RegionalSucursalDAO.cierraConexion();
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
		ResultadoCatalogo resultadoCatalogoRegionalSucursal = new  ResultadoCatalogo();
		SimUsuarioRegionalSucursalTodasDAO RegionalSucursalDAO = new SimUsuarioRegionalSucursalTodasDAO();
		
		if (registro.getDefCampo("REGIONALES").equals("SI")){
			
			sSql =  "SELECT \n"+
			 	"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n"+
				"ID_REGIONAL \n"+
			"FROM \n"+
				"SIM_USUARIO_REGIONAL \n"+
			" WHERE CVE_USUARIO ='" + registro.getDefCampo("CLAVE_USUARIO") + "' \n";
			ejecutaSql();
			
			while (rs.next()){
				
				sSql = "DELETE FROM SIM_USUARIO_REGIONAL" +
				       " WHERE ID_REGIONAL		='" + rs.getString("ID_REGIONAL") + "' \n"+
				       " AND CVE_USUARIO		='" + (String)registro.getDefCampo("CLAVE_USUARIO") + "' \n"+
				       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ps1.close();
			}	
			
			sSql =  "SELECT \n"+
			 	"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n"+
				"ID_SUCURSAL \n"+
			"FROM \n"+
				"SIM_USUARIO_SUCURSAL \n"+
			" WHERE CVE_USUARIO ='" + registro.getDefCampo("CLAVE_USUARIO") + "' \n";
			ejecutaSql();
			
			while (rs.next()){
				
				sSql = "DELETE FROM SIM_USUARIO_SUCURSAL" +
				       " WHERE ID_SUCURSAL		='" + rs.getString("ID_SUCURSAL") + "' \n"+
				       " AND CVE_USUARIO		='" + (String)registro.getDefCampo("CLAVE_USUARIO") + "' \n"+
				       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ps2.close();
			}
			
			String sIdRegional = "";
			String sClaveUsuario = (String)registro.getDefCampo("CLAVE_USUARIO");
			
			sSql =  " SELECT \n"+
					" CVE_GPO_EMPRESA, \n"+
					" CVE_EMPRESA, \n"+
					" ID_REGIONAL \n"+
				" FROM SIM_CAT_REGIONAL \n"+
				" WHERE CVE_GPO_EMPRESA ='" + registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND CVE_EMPRESA = '" + registro.getDefCampo("CVE_EMPRESA") + "' \n";
			ejecutaSql();
			
			while (rs.next()){
				
				sIdRegional = rs.getString("ID_REGIONAL");
				
				sSql =  "INSERT INTO SIM_USUARIO_REGIONAL ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"CVE_USUARIO, \n"+
						"ID_REGIONAL) \n"+
					"VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CLAVE_USUARIO") + "', \n" +
						"'" + sIdRegional +"') \n" ;
				
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();		
				
			}
			
			RegionalSucursalDAO.setConexion(this.getConexion());
			resultadoCatalogoRegionalSucursal = RegionalSucursalDAO.alta(registro);
			
				
				
			
			
			RegionalSucursalDAO.cierraConexion();
			
			
			
		}else if (registro.getDefCampo("REGIONALES").equals("NO")){
			
			sSql =  "SELECT \n"+
			 	"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n"+
				"ID_REGIONAL \n"+
			"FROM \n"+
				"SIM_USUARIO_REGIONAL \n"+
			" WHERE CVE_USUARIO ='" + registro.getDefCampo("CLAVE_USUARIO") + "' \n";
			ejecutaSql();
			
			while (rs.next()){
				
				sSql = "DELETE FROM SIM_USUARIO_REGIONAL" +
				       " WHERE ID_REGIONAL		='" + rs.getString("ID_REGIONAL") + "' \n"+
				       " AND CVE_USUARIO		='" + (String)registro.getDefCampo("CLAVE_USUARIO") + "' \n"+
				       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ps1.close();
			}	
			
			sSql =  "SELECT \n"+
			 	"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n"+
				"ID_SUCURSAL \n"+
			"FROM \n"+
				"SIM_USUARIO_SUCURSAL \n"+
			" WHERE CVE_USUARIO ='" + registro.getDefCampo("CLAVE_USUARIO") + "' \n";
			ejecutaSql();
			
			while (rs.next()){
				
				sSql = "DELETE FROM SIM_USUARIO_SUCURSAL" +
				       " WHERE ID_SUCURSAL		='" + rs.getString("ID_SUCURSAL") + "' \n"+
				       " AND CVE_USUARIO		='" + (String)registro.getDefCampo("CLAVE_USUARIO") + "' \n"+
				       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ps2.close();
			}	
		}
		return resultadoCatalogo;
	}
}
