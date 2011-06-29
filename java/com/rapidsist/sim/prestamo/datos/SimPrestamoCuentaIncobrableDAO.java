/**
 * Sistema de administraciï¿½n de portales.
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
 * Administra los accesos a la base para la liquidación de un préstamo por ser cuenta incobrable.
 */
 
public class SimPrestamoCuentaIncobrableDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sFecha = "";
		String sTxRespuesta = "";

		sSql = "SELECT TO_CHAR(TO_DATE('"+(String)registro.getDefCampo("FECHA")+"','DD-MM-YYYY'),'DD-MON-YY') AS F_APLICA FROM DUAL \n";
        
		ejecutaSql();
		if (rs.next()){
			sFecha = rs.getString("F_APLICA");
		}
		
		if (registro.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
		
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fRegistraMovtoCuentaIncobrable(?,?,?,?,?,?)); end;");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
			sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
			sto1.setString(5, sFecha);
			sto1.registerOutParameter(6, java.sql.Types.VARCHAR);
			
			System.out.println("CVE_GPO_EMPRESA:"+(String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			System.out.println("CVE_EMPRESA:"+(String)registro.getDefCampo("CVE_EMPRESA"));
			System.out.println("ID_PRESTAMO:"+(String)registro.getDefCampo("ID_PRESTAMO"));
			System.out.println("CVE_USUARIO:"+(String)registro.getDefCampo("CVE_USUARIO"));
			System.out.println("FECHA:"+sFecha);
		
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxRespuesta = sto1.getString(6);
			sto1.close();
			
			resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxRespuesta);
			
			System.out.println("sTxRespuesta:"+sTxRespuesta);
			
		}else if (registro.getDefCampo("APLICA_A").equals("GRUPO")){
			
			registro.addDefCampo("ID_PRESTAMO_GRUPO",registro.getDefCampo("ID_PRESTAMO"));
			
			sSql = "SELECT \n"+
			"ID_PRESTAMO \n"+
			"FROM \n"+
			"SIM_PRESTAMO_GPO_DET \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
			ejecutaSql();
			while (rs.next()){
				
				registro.addDefCampo("ID_PRESTAMO", rs.getString("ID_PRESTAMO"));
			
		
				CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fRegistraMovtoCuentaIncobrable(?,?,?,?,?,?)); end;");
				sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
				sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
				sto1.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
				sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
				sto1.setString(5, sFecha);
				sto1.registerOutParameter(6, java.sql.Types.VARCHAR);
				
				System.out.println("CVE_GPO_EMPRESA:"+(String)registro.getDefCampo("CVE_GPO_EMPRESA"));
				System.out.println("CVE_EMPRESA:"+(String)registro.getDefCampo("CVE_EMPRESA"));
				System.out.println("ID_PRESTAMO:"+(String)registro.getDefCampo("ID_PRESTAMO"));
				System.out.println("CVE_USUARIO:"+(String)registro.getDefCampo("CVE_USUARIO"));
				System.out.println("FECHA:"+sFecha);
			
				//EJECUTA EL PROCEDIMIENTO ALMACENADO
				sto1.execute();
				sTxRespuesta = sto1.getString(6);
				sto1.close();
				
				resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxRespuesta);
				
				System.out.println("sTxRespuesta:"+sTxRespuesta);
				
				if (sTxRespuesta == null){
					//Acutaliza el estado de cuenta del prestamo en la tabla SIM_PRESTAMO_GPO_DET
					
					sSql = " UPDATE SIM_PRESTAMO_GPO_DET SET "+
							" ID_ETAPA_PRESTAMO ='9' \n" +
							" WHERE ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							" AND ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
							" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					
				}
				
			}
			//Acutaliza el estado de cuenta del prestamo en la tabla SIM_PRESTAMO_GRUPO
			
			sSql = " UPDATE SIM_PRESTAMO_GRUPO SET "+
					" ID_ETAPA_PRESTAMO ='9' \n" +
					" WHERE ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
					" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
		}
		return resultadoCatalogo;
	}

}