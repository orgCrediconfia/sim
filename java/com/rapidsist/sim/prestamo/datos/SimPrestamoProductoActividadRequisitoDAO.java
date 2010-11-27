/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para el catálogo de producto.
 */
 
public class SimPrestamoProductoActividadRequisitoDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		/*sSql =  "SELECT \n"+
					"CVE_GPO_EMPRESA, \n"+
					"CVE_EMPRESA, \n"+
					"ID_PRODUCTO, \n"+
					"ID_ACTIVIDAD_REQUISITO, \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM \n"+
					"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
					
				ejecutaSql();
				while (rs.next()){
					registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs.getString("ID_ACTIVIDAD_REQUISITO"));
					registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO")== null ? "": rs.getString("ID_ETAPA_PRESTAMO"));
					
					sSql = "INSERT INTO SIM_PRESTAMO_ETAPA ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PRESTAMO, \n" +
						"ID_ACTIVIDAD_REQUISITO, \n" +
						"ID_ETAPA_PRESTAMO) \n" +
						" VALUES (" +
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n" ;
					
					PreparedStatement ps12 = this.conn.prepareStatement(sSql);
					ps12.execute();
					ResultSet rs12 = ps12.getResultSet();	
				}
				*/
				
		String sIdActividad = "";
		String sIdEstatus = "";
		
		sSql =  "SELECT \n"+
			"CVE_GPO_EMPRESA, \n"+
			"CVE_EMPRESA, \n"+
			"ID_PRODUCTO, \n"+
			"ID_ACTIVIDAD_REQUISITO, \n"+
			"ID_ETAPA_PRESTAMO \n"+
			"FROM \n"+
			"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
			
					
		ejecutaSql();		
							
		while (rs.next()){
						
			sIdActividad = rs.getString("ID_ACTIVIDAD_REQUISITO");
			sIdEstatus = rs.getString("ID_ETAPA_PRESTAMO");
							
			sSql =  "INSERT INTO SIM_PRESTAMO_ETAPA ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRESTAMO, \n" +
				"ID_ACTIVIDAD_REQUISITO, \n" +
				"ID_ETAPA_PRESTAMO) \n" +
				" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
				"'" + sIdActividad + "', \n"+
				"'" + sIdEstatus + "') \n";
									
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ResultSet rs2 = ps2.getResultSet();		
			
		}
				
		return resultadoCatalogo;
	}

}