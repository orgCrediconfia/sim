/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.usuarios.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.*;


/**
 * Administra los accesos a la base de datos de todas las regionales de un usuario.
 */
 
public class SimUsuarioRegionalSucursalTodasDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		String sIdSucursal = "";
			
			sSql =  " SELECT DISTINCT \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_REGIONAL, \n"+
				"ID_SUCURSAL \n"+
			" FROM SIM_REGIONAL_SUCURSAL \n"+
			" WHERE CVE_GPO_EMPRESA ='" + registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + registro.getDefCampo("CVE_EMPRESA") + "' \n";
			
			ejecutaSql();
		
		while (rs.next()){
			
			sIdSucursal = rs.getString("ID_SUCURSAL");
			
			sSql =  "INSERT INTO SIM_USUARIO_SUCURSAL ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_USUARIO, \n" +
				"ID_SUCURSAL) \n" +
			        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CLAVE_USUARIO") + "', \n" +
				"'" + sIdSucursal +"') \n" ;
			
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ResultSet rs2 = ps2.getResultSet();
		
			
		}
			
		
		return resultadoCatalogo;
	}
}
