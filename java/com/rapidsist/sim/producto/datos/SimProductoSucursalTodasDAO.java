/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.*;


/**
 * Administra los accesos a la base de datos de todas sucursales que tiene acceso al producto.
 */
 
public class SimProductoSucursalTodasDAO extends Conexion2 implements OperacionAlta, OperacionModificacion  {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		
		sSql =  " SELECT \n"+
				" CVE_GPO_EMPRESA, \n"+
				" CVE_EMPRESA, \n"+
				" ID_SUCURSAL \n"+
			" FROM SIM_USUARIO_SUCURSAL \n"+
			" WHERE CVE_GPO_EMPRESA ='" + registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_USUARIO = '" + registro.getDefCampo("CVE_USUARIO") + "' \n";
		ejecutaSql();
		
		while (rs.next()){
			sSql =  "INSERT INTO SIM_PRODUCTO_SUCURSAL ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_SUCURSAL, \n"+
					"ID_PRODUCTO) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + rs.getString("ID_SUCURSAL") + "',  \n"+
					"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "') \n" ;
					
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			PreparedStatement ps1 = this.conn.prepareStatement(sSql);
			ps1.execute();
			ResultSet rs1 = ps1.getResultSet();	
		
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
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		if (registro.getDefCampo("SUCURSALES").equals("SI")){
			
			sSql =  "SELECT \n"+
					 	"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_SUCURSAL, \n"+
						"ID_PRODUCTO \n"+
					"FROM \n"+
						"SIM_PRODUCTO_SUCURSAL \n"+
					" WHERE ID_PRODUCTO ='" + registro.getDefCampo("ID_PRODUCTO") + "' \n";
			ejecutaSql();
			
			if (!rs.next()){
				
				sSql =  " SELECT \n"+
						" CVE_GPO_EMPRESA, \n"+
						" CVE_EMPRESA, \n"+
						" ID_SUCURSAL \n"+
					" FROM SIM_USUARIO_SUCURSAL \n"+
					" WHERE CVE_GPO_EMPRESA ='" + registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND CVE_EMPRESA = '" + registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_USUARIO = '" + registro.getDefCampo("CVE_USUARIO") + "' \n";
				ejecutaSql();
				
				while (rs.next()){
					sSql =  "INSERT INTO SIM_PRODUCTO_SUCURSAL ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_SUCURSAL, \n"+
							"ID_PRODUCTO) \n"+
						"VALUES ( \n"+
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + rs.getString("ID_SUCURSAL") + "',  \n"+
							"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "') \n" ;
							
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();	
				}
			}else {
				System.out.println("existen sucursales que deberan de ser validadas");
				sSql =  "SELECT \n"+
						 	"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_SUCURSAL, \n"+
							"ID_PRODUCTO \n"+
						"FROM \n"+
							"SIM_PRODUCTO_SUCURSAL \n"+
						" WHERE ID_PRODUCTO ='" + registro.getDefCampo("ID_PRODUCTO") + "' \n";
				ejecutaSql();
				while (rs.next()){
					
					sSql =  " SELECT \n"+
							" CVE_GPO_EMPRESA, \n"+
							" CVE_EMPRESA, \n"+
							" ID_SUCURSAL \n"+
						" FROM SIM_USUARIO_SUCURSAL \n"+
						" WHERE CVE_GPO_EMPRESA ='" + registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						" AND CVE_EMPRESA = '" + registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND ID_SUCURSAL = '" + rs.getString("ID_SUCURSAL") + "' \n"+
						" AND CVE_USUARIO = '" + registro.getDefCampo("CVE_USUARIO") + "' \n";
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					
					if (!rs1.next()){
						sSql = "DELETE FROM SIM_PRODUCTO_SUCURSAL" +
					       " WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
					       " AND ID_SUCURSAL 			='" + rs.getString("ID_SUCURSAL") + "' \n"+	
					       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						PreparedStatement ps2 = this.conn.prepareStatement(sSql);
						ps2.execute();
						ResultSet rs2 = ps2.getResultSet();
					
					}
					
				}
				
				
				sSql =  " SELECT \n"+
						" CVE_GPO_EMPRESA, \n"+
						" CVE_EMPRESA, \n"+
						" ID_SUCURSAL \n"+
					" FROM SIM_USUARIO_SUCURSAL \n"+
					" WHERE CVE_GPO_EMPRESA ='" + registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND CVE_EMPRESA = '" + registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_USUARIO = '" + registro.getDefCampo("CVE_USUARIO") + "' \n";
				ejecutaSql();
				
				while (rs.next()){
					sSql =  "SELECT \n"+
						 	"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_SUCURSAL, \n"+
							"ID_PRODUCTO \n"+
						"FROM \n"+
							"SIM_PRODUCTO_SUCURSAL \n"+
						" WHERE ID_PRODUCTO ='" + registro.getDefCampo("ID_PRODUCTO") + "' \n"+
						" AND ID_SUCURSAL = '" + rs.getString("ID_SUCURSAL") + "' \n";
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					
					if (!rs3.next()){
						sSql =  "INSERT INTO SIM_PRODUCTO_SUCURSAL ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_SUCURSAL, \n"+
								"ID_PRODUCTO) \n"+
							"VALUES ( \n"+
								"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
								"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
								"'" + rs.getString("ID_SUCURSAL") + "',  \n"+
								"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "') \n" ;
						PreparedStatement ps4 = this.conn.prepareStatement(sSql);
						ps4.execute();
						ResultSet rs4 = ps4.getResultSet();
					}	
					
				}
						
			}
			
		}else if (registro.getDefCampo("SUCURSALES").equals("NO")){
			
			sSql =  "SELECT \n"+
			 	"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_SUCURSAL, \n"+
				"ID_PRODUCTO \n"+
			"FROM \n"+
				"SIM_PRODUCTO_SUCURSAL \n"+
			" WHERE ID_PRODUCTO ='" + registro.getDefCampo("ID_PRODUCTO") + "' \n";
			ejecutaSql();
			
			if (rs.next()){
				
				sSql = "DELETE FROM SIM_PRODUCTO_SUCURSAL" +
				       " WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
				       " AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				       " AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}	
		}
		return resultadoCatalogo;
	}
}
