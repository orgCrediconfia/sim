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
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base para la liquidación de préstamos por defunción.
 */
 
public class SimPrestamoLiquidacionDefuncionDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sTxRespuesta = "";
		CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fRegistraMovtoDefuncion(?,?,?,?)); end;");
		sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
		sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
		sto1.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
		sto1.registerOutParameter(4, java.sql.Types.VARCHAR);
		
		//EJECUTA EL PROCEDIMIENTO ALMACENADO
		sto1.execute();
		sTxRespuesta = sto1.getString(4);
		sto1.close();
		
		resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxRespuesta);
		
		return resultadoCatalogo;
	}

}