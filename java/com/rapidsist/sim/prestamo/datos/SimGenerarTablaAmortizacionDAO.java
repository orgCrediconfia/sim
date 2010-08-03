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
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para generar las tablas de amortizaciòn.
 */
 
public class SimGenerarTablaAmortizacionDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql =  "SELECT \n"+
			   "CVE_GPO_EMPRESA, \n" +
			   "CVE_EMPRESA, \n" +
			   "ID_PRESTAMO \n"+
			" FROM SIM_TABLA_AMORT_ACCESORIO \n"+
			" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
		
		ejecutaSql();
		if (rs.next()){
			sSql = "DELETE FROM SIM_TABLA_AMORT_ACCESORIO " +
				" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		
		sSql =  "SELECT \n"+
			   "CVE_GPO_EMPRESA, \n" +
			   "CVE_EMPRESA, \n" +
			   "ID_PRESTAMO \n"+
			" FROM SIM_TABLA_AMORTIZACION \n"+
			" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
		
		ejecutaSql();
		if (rs.next()){	
			sSql = "DELETE FROM SIM_TABLA_AMORTIZACION " +
				" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		
		String sTxrespuesta = "";
	
		String sCveGpoEmpresa = (String)registro.getDefCampo("CVE_GPO_EMPRESA");
		String sCveEmpresa = (String)registro.getDefCampo("CVE_EMPRESA");
		String sIdEmpresa = (String)registro.getDefCampo("ID_PRESTAMO");
		
		CallableStatement sto = conn.prepareCall("begin PKG_CREDITO.pGeneraTablaAmortizacion(?,?,?,?); end;");
		
		sto.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
	
		sto.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
		
		sto.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
		
		sto.registerOutParameter(4, java.sql.Types.VARCHAR);
		
		//EJECUTA EL PROCEDIMIENTO ALMACENADO
		sto.execute();
		
		sTxrespuesta  = sto.getString(4);
		
		sto.close();
		
		// SE AGREGA LA RESPUESTA
		System.out.println("sTxrespuesta"+sTxrespuesta);
		registro.addDefCampo("RESPUESTA" ,sTxrespuesta);
		resultadoCatalogo.Resultado = registro;
		
		return resultadoCatalogo;
	}
}