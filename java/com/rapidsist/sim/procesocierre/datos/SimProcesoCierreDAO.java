/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.procesocierre.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el proceso de cierre.
 */
 
public class SimProcesoCierreDAO extends Conexion2 implements OperacionAlta, OperacionConsultaRegistro {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
		sSql = " SELECT \n"+
			   " F_MEDIO \n" +
			   " FROM PFIN_PARAMETRO \n"+
			   " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
			 
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
		
		String sTxrespuestaRecorreFecha = "";
		String sTxrespuestaActualiza = "";
		String sFMedio = "";
		
		CallableStatement sto = conn.prepareCall("begin dbms_output.put_line(PKG_PROCESOS.RecorreFecha(?,?,?)); end;");
		
		sto.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
		
		sto.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
		
		sto.registerOutParameter(3, java.sql.Types.VARCHAR);
		
		
		//EJECUTA EL PROCEDIMIENTO ALMACENADO
		sto.execute();
		sTxrespuestaRecorreFecha  = sto.getString(3);
		sto.close();
		
		sSql =  "SELECT TO_CHAR(TO_DATE(F_MEDIO,'DD-MM-YYYY'),'DD-MON-YYYY') AS F_MEDIO \n"+
				"FROM PFIN_PARAMETRO \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND CVE_MEDIO = 'SYSTEM' \n";
		ejecutaSql();
			
		if (rs.next()){
			sFMedio = rs.getString("F_MEDIO");
			
		}
		
		CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fActualizaInformacionCredito(?,?,?,?,?)); end;");
		
		sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
		sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
		sto1.setString(3, "0");
		sto1.setString(4, sFMedio);
		sto1.registerOutParameter(5, java.sql.Types.VARCHAR);
		
		
		//EJECUTA EL PROCEDIMIENTO ALMACENADO
		sto1.execute();
		sTxrespuestaActualiza = sto1.getString(5);
		sto1.close();
		
		System.out.println("sTxrespuestaActualiza"+sTxrespuestaActualiza);
		
		
		return resultadoCatalogo;
	}
}