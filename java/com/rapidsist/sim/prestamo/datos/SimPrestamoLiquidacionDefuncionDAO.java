/**
 * Sistema de administraci�n de portales.
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
 * Administra los accesos a la base para la liquidaci�n de pr�stamos por defunci�n.
 */
 
public class SimPrestamoLiquidacionDefuncionDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sFMedio = "";
		
		sSql = "SELECT TO_CHAR(F_MEDIO,'DD-MON-YY') AS F_MEDIO FROM PFIN_PARAMETRO \n";
		ejecutaSql();
		if (rs.next()){
			sFMedio = rs.getString("F_MEDIO");
		}
		
		String sTxRespuesta = "";
		CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fRegistraMovtoDefuncion(?,?,?,?,?,?)); end;");
		sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
		sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
		sto1.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
		sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
		sto1.setString(5, sFMedio);
		sto1.registerOutParameter(6, java.sql.Types.VARCHAR);
	
		//EJECUTA EL PROCEDIMIENTO ALMACENADO
		sto1.execute();
		sTxRespuesta = sto1.getString(6);
		sto1.close();
		
		resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxRespuesta);
		
		//CODIGO COMENTADO YA QUE EL PL ACTUALIZA LA ETAPA DEL PRESTAMO
		/*
		if (sTxRespuesta == null){
			sSql =  " UPDATE SIM_PRESTAMO SET "+
					" ID_ETAPA_PRESTAMO		='8' \n" +
					" WHERE ID_PRESTAMO  	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					" AND CVE_EMPRESA   	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				sSql =  "SELECT \n"+
						"CVE_GPO_EMPRESA, \n"+
						"CVE_EMPRESA, \n"+
						"ID_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_GPO_DET  \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO = '" + (String) registro.getDefCampo("ID_PRESTAMO") + "' \n";	
				ejecutaSql();
				if (rs.next()){
					sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
							" ID_ETAPA_PRESTAMO		='8' \n" +
							" WHERE ID_PRESTAMO  	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							" AND CVE_EMPRESA   	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						//VERIFICA SI DIO DE ALTA EL REGISTRO
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
				}
		}
		*/
		return resultadoCatalogo;
	}

}