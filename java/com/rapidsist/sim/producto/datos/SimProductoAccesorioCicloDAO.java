/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

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
 
public class SimProductoAccesorioCicloDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql =  "SELECT  \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_PRODUCTO, \n"+
			"NUM_CICLO, \n"+
			"ID_ACCESORIO \n" +
			"FROM SIM_PRODUCTO_CICLO_ACCESORIO \n" +
			"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_PRODUCTO ='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
			"AND NUM_CICLO ='" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
			"AND ID_ACCESORIO ='" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "' \n";
						
		ejecutaSql();		
				
		if (!rs.next()){
							
			sSql =  "INSERT INTO SIM_PRODUCTO_CICLO_ACCESORIO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"NUM_CICLO, \n" +
				"ID_ACCESORIO) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CARGO_COMISION") + "') \n" ;
					
						
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();	
							
		}
				
		return resultadoCatalogo;
	}
}
