/**
 * Sistema de administraciï¿½n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Administra los accesos a la base para la cancelación de cuenta incobrable de un préstamo.
 */
 
public class SimPrestamoCancelarCuentaIncobrableDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
				"V.CVE_GPO_EMPRESA, \n"+
				"V.CVE_EMPRESA, \n"+
				"V.ID_PRESTAMO, \n"+
				"V.CVE_PRESTAMO, \n"+ 
				"V.APLICA_A, \n"+ 
				"V.NOMBRE, \n"+
				"V.NOM_PRODUCTO, \n"+
				"V.NUM_CICLO, \n"+
				"V.ID_ETAPA_PRESTAMO, \n"+
				"E.NOM_ESTATUS_PRESTAMO \n"+
				"FROM V_CREDITO V, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US  \n"+
				"WHERE V.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND V.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND V.ID_ETAPA_PRESTAMO = '9' \n"+
				"AND E.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND E.ID_ETAPA_PRESTAMO = V.ID_ETAPA_PRESTAMO \n"+
				"AND US.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND US.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND US.ID_SUCURSAL = V.ID_SUCURSAL \n"+
				"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				"AND V.APLICA_A != 'INDIVIDUAL_GRUPO' \n";
				
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + " AND V.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(V.NOMBRE) LIKE'%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY V.CVE_PRESTAMO \n";
			
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sTxRespuesta = "";
	
		if(registro.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
			
			sSql = "SELECT \n"+
				   "ID_MOVIMIENTO \n"+
				   "FROM \n"+
				   "PFIN_MOVIMIENTO \n"+
				   "WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				   "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				   "AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
				   "AND CVE_OPERACION = 'CTAINCOB' \n";
			ejecutaSql();
			
			if (rs.next()){
				registro.addDefCampo("ID_MOVIMIENTO", rs.getString("ID_MOVIMIENTO"));
			}
			
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaIncobrable(?,?,?,?,?)); end;");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, (String)registro.getDefCampo("ID_MOVIMIENTO"));
			sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
			sto1.registerOutParameter(5, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxRespuesta = sto1.getString(5);
			sto1.close();
			
			resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxRespuesta);
			
			System.out.println("sTxRespuesta:"+sTxRespuesta);
			
			if (sTxRespuesta == null){
				sSql = " UPDATE SIM_PRESTAMO SET "+
						" ID_ETAPA_PRESTAMO ='7' \n" +
						" WHERE ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
			}
			
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
			
			if (rs.next()){
				registro.addDefCampo("ID_PRESTAMO", rs.getString("ID_PRESTAMO"));
			}
			
			sSql = "SELECT \n"+
				   "ID_MOVIMIENTO \n"+
				   "FROM \n"+
				   "PFIN_MOVIMIENTO \n"+
				   "WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				   "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				   "AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			       "AND CVE_OPERACION = 'CTAINCOB' \n";
			ejecutaSql();
			
			if (rs.next()){
				registro.addDefCampo("ID_MOVIMIENTO", rs.getString("ID_MOVIMIENTO"));
			}
			
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaIncobrable(?,?,?,?,?)); end;");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, (String)registro.getDefCampo("ID_MOVIMIENTO"));
			sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
			sto1.registerOutParameter(5, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxRespuesta = sto1.getString(5);
			sto1.close();
			
			resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxRespuesta);
			
			System.out.println("sTxRespuesta:"+sTxRespuesta);
			
			if (sTxRespuesta == null){
				
				//Acutaliza el estado de cuenta del prestamo en la tabla SIM_PRESTAMO_GPO_DET
				sSql = " UPDATE SIM_PRESTAMO_GPO_DET SET "+
						" ID_ETAPA_PRESTAMO ='7' \n" +
						" WHERE ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
						" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				
				sSql = " UPDATE SIM_PRESTAMO_GRUPO SET "+
						" ID_ETAPA_PRESTAMO ='7' \n" +
						" WHERE ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
						" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}

		return resultadoCatalogo;
	}

}