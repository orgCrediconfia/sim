/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para mostrar movimientos de Caja.
 */
 
public class SimCajaCancelacionPagoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
				"M.ID_PRESTAMO, \n"+
				"P.CVE_PRESTAMO, \n"+
				"'INDIVIDUAL' APLICA_A, \n"+
				"PE.NOM_COMPLETO, \n"+
				"'' NOM_GRUPO, \n"+
				"P.ID_PRODUCTO, \n"+
				"PRO.NOM_PRODUCTO, \n"+
				"P.NUM_CICLO, \n"+
				"M.F_APLICACION, \n"+
				"M.ID_GRUPO, \n"+
				"SUM(NVL(ROUND(M.IMP_NETO,2),0)) MONTO, \n"+ 
				"TO_CHAR(SUM(NVL(ROUND(M.IMP_NETO,2),0)),'999,999,999.99') IMPORTE \n"+
				"FROM \n"+
				"PFIN_MOVIMIENTO M, \n"+
				"SIM_PRESTAMO P, \n"+
				"RS_GRAL_PERSONA PE, \n"+
				"SIM_PRODUCTO PRO \n"+ 
				"WHERE M.CVE_GPO_EMPRESA = 'SIM' \n"+
				"AND M.CVE_EMPRESA = 'CREDICONFIA' \n"+
				"AND M.CVE_OPERACION = 'CRPAGOPRES' \n"+
				"AND P.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+ 
				"AND P.CVE_EMPRESA = M.CVE_EMPRESA \n"+
				"AND P.ID_PRESTAMO = M.ID_PRESTAMO \n"+
				"AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND P.ID_ETAPA_PRESTAMO != '8' \n"+
				"AND P.ID_GRUPO IS NULL \n"+
				"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
				"AND PRO.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PRO.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PRO.ID_PRODUCTO = P.ID_PRODUCTO \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND P.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
			sSql = sSql + "AND PRO.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND P.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_CLIENTE") != null) {
			sSql = sSql + "AND PE.NOM_COMPLETO LIKE '%" + (String) parametros.getDefCampo("NOM_CLIENTE") + "%' \n";
		}
		
		sSql = sSql + "GROUP BY M.ID_PRESTAMO, P.CVE_PRESTAMO, 'INDIVIDUAL',  PE.NOM_COMPLETO, '', P.ID_PRODUCTO, PRO.NOM_PRODUCTO, P.NUM_CICLO, M.F_APLICACION, M.ID_GRUPO \n";
		
		sSql = sSql + "MINUS \n"+
					"SELECT \n"+
					"C.ID_PRESTAMO, \n"+
					"P.CVE_PRESTAMO, \n"+
					"'INDIVIDUAL' APLICA_A, \n"+
					"PE.NOM_COMPLETO, \n"+
					"'' NOM_GRUPO, \n"+
					"P.ID_PRODUCTO, \n"+
					"PRO.NOM_PRODUCTO, \n"+
					"P.NUM_CICLO, \n"+
					"C.FECHA_APLICACION F_APLICACION, \n"+
					"ID_TRANSACCION_GRUPO ID_GRUPO, \n"+
					"NVL(ROUND(C.MONTO,2),0) MONTO,  \n"+
					"TO_CHAR(NVL(ROUND(C.MONTO,2),0),'999,999,999.99') IMPORTE \n"+
					"FROM \n"+
					"SIM_CAJA_TRANSACCION C, \n"+
					"SIM_PRESTAMO P, \n"+
					"RS_GRAL_PERSONA PE, \n"+
					"SIM_PRODUCTO PRO \n"+
					"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
					"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
					"AND C.CVE_MOVIMIENTO_CAJA = 'CANPAGO' \n"+
					"AND C.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND P.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = C.CVE_EMPRESA \n"+
					"AND P.ID_PRESTAMO = C.ID_PRESTAMO \n"+
					"AND P.ID_GRUPO IS NULL \n"+
					"AND P.ID_ETAPA_PRESTAMO != '8' \n"+
					"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
					"AND PRO.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND PRO.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND PRO.ID_PRODUCTO = P.ID_PRODUCTO \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND P.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
			sSql = sSql + "AND PRO.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND P.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_CLIENTE") != null) {
			sSql = sSql + "AND PE.NOM_COMPLETO LIKE '%" + (String) parametros.getDefCampo("NOM_CLIENTE") + "%' \n";
		}
		
		sSql = sSql + "UNION ALL \n"+
						"SELECT \n"+
						"GD.ID_PRESTAMO_GRUPO, \n"+ 
						"PG.CVE_PRESTAMO_GRUPO, \n"+
						"'GRUPAL' APLICA_A, \n"+
						"'' NOM_COMPLETO, \n"+
						"G.NOM_GRUPO, \n"+
						"PG.ID_PRODUCTO, \n"+
						"PRO.NOM_PRODUCTO, \n"+
						"PG.NUM_CICLO, \n"+
						"M.F_APLICACION, \n"+
						"M.ID_GRUPO, \n"+
					    "SUM(NVL(ROUND(M.IMP_NETO,2),0)) MONTO, \n"+
					    "TO_CHAR(SUM(NVL(ROUND(M.IMP_NETO,2),0)),'999,999,999.99') IMPORTE \n"+
						"FROM \n"+
						"SIM_PRESTAMO_GPO_DET GD, \n"+ 
						"SIM_PRESTAMO_GRUPO PG, \n"+
						"SIM_GRUPO G, \n"+
						"SIM_PRESTAMO P, \n"+
						"SIM_PRODUCTO PRO, \n"+
						"PFIN_MOVIMIENTO M \n"+
						"WHERE GD.CVE_GPO_EMPRESA = 'SIM' \n"+ 
						"AND GD.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND PG.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
						"AND PG.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
						"AND PG.ID_PRESTAMO_GRUPO = GD.ID_PRESTAMO_GRUPO \n"+  
						"AND PG.ID_ETAPA_PRESTAMO != '8' \n"+
						"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
						"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
						"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
						"AND G.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
						"AND P.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
						"AND P.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
						"AND P.ID_PRESTAMO = GD.ID_PRESTAMO \n"+
						"AND PRO.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
						"AND PRO.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND PRO.ID_PRODUCTO = P.ID_PRODUCTO \n"+
						"AND M.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
						"AND M.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
						"AND M.ID_PRESTAMO = GD.ID_PRESTAMO \n"+
						"AND M.CVE_OPERACION = 'CRPAGOPRES' \n";
						
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
			sSql = sSql + "AND PRO.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND PG.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
		}

		sSql = sSql + "GROUP BY GD.ID_PRESTAMO_GRUPO, PG.CVE_PRESTAMO_GRUPO, 'GRUPAL', '', G.NOM_GRUPO, PG.ID_PRODUCTO, PRO.NOM_PRODUCTO, PG.NUM_CICLO, M.F_APLICACION, M.ID_GRUPO \n";
		 
		sSql = sSql + "MINUS \n"+
					"SELECT \n"+
					"C.ID_PRESTAMO, \n"+ 
					"PG.CVE_PRESTAMO_GRUPO, \n"+
					"'GRUPAL' APLICA_A, \n"+
					"'' NOM_COMPLETO, \n"+
					"G.NOM_GRUPO, \n"+
					"PG.ID_PRODUCTO, \n"+
					"PRO.NOM_PRODUCTO, \n"+
					"PG.NUM_CICLO, \n"+
					"C.FECHA_APLICACION, \n"+
					"ID_TRANSACCION_GRUPO ID_GRUPO, \n"+
					"NVL(ROUND(-C.MONTO,2),0) MONTO,  \n"+
					"TO_CHAR(NVL(ROUND(-C.MONTO,2),0),'999,999,999.99') IMPORTE \n"+
					"FROM \n"+
					"SIM_CAJA_TRANSACCION C, \n"+
					"SIM_PRESTAMO_GRUPO PG, \n"+
					"SIM_GRUPO G, \n"+
					"SIM_PRODUCTO PRO \n"+
					"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+ 
					"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
					"AND C.CVE_MOVIMIENTO_CAJA = 'CANPAGO' \n"+
					"AND PG.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+  
					"AND PG.CVE_EMPRESA = C.CVE_EMPRESA \n"+
					"AND PG.ID_PRESTAMO_GRUPO = C.ID_PRESTAMO \n"+  
					"AND PG.ID_ETAPA_PRESTAMO != '8' \n"+
					"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
					"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
					"AND G.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND PRO.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
					"AND PRO.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					"AND PRO.ID_PRODUCTO = PG.ID_PRODUCTO \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
			sSql = sSql + "AND PRO.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND PG.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
		}
		
		ejecutaSql();
		return getConsultaLista();
	}
	

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdMovimiento = "";
		String sTxrespuesta = "";
		
		if (registro.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
			//Cancela un pr�stamo indiviudal.
			
			sSql = "SELECT \n"+
					"M.ID_PRESTAMO, \n"+
					"M.ID_MOVIMIENTO, \n"+
					"P.CVE_PRESTAMO, \n"+
					"'INDIVIDUAL' APLICA_A, \n"+
					"PE.ID_PERSONA, \n"+
					"PE.NOM_COMPLETO, \n"+
					"'' NOM_GRUPO, \n"+
					"P.ID_PRODUCTO, \n"+
					"PRO.NOM_PRODUCTO, \n"+
					"P.NUM_CICLO, \n"+
					"M.F_APLICACION, \n"+
					"M.ID_GRUPO, \n"+
					"SUM(M.IMP_NETO) IMPORTE \n"+
					"FROM \n"+
					"PFIN_MOVIMIENTO M, \n"+
					"SIM_PRESTAMO P, \n"+
					"RS_GRAL_PERSONA PE, \n"+
					"SIM_PRODUCTO PRO \n"+ 
					"WHERE M.CVE_GPO_EMPRESA = 'SIM' \n"+
					"AND M.CVE_EMPRESA = 'CREDICONFIA' \n"+
					"AND M.CVE_OPERACION = 'CRPAGOPRES' \n"+
					"AND P.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+ 
					"AND P.CVE_EMPRESA = M.CVE_EMPRESA \n"+
					"AND P.ID_PRESTAMO = M.ID_PRESTAMO \n"+
					"AND P.ID_GRUPO IS NULL \n"+
					"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
					"AND PRO.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND PRO.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND PRO.ID_PRODUCTO = P.ID_PRODUCTO \n"+
					"AND M.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND M.ID_GRUPO = '" + (String)registro.getDefCampo("ID_TRANSACCION_GRUPO") + "' \n"+
					"GROUP BY M.ID_PRESTAMO, M.ID_MOVIMIENTO, P.CVE_PRESTAMO, 'INDIVIDUAL', PE.ID_PERSONA, PE.NOM_COMPLETO, '', P.ID_PRODUCTO, PRO.NOM_PRODUCTO, P.NUM_CICLO, M.F_APLICACION, M.ID_GRUPO \n";
					
			ejecutaSql();
			if (rs.next()){
				sIdMovimiento = rs.getString("ID_MOVIMIENTO");
				registro.addDefCampo("ID_CLIENTE",rs.getString("ID_PERSONA"));
			}
			
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaPago(?,?,?,?,?)); end;");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, sIdMovimiento);
			sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
			sto1.registerOutParameter(5, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxrespuesta = sto1.getString(5);
			sto1.close();
			
			if (sTxrespuesta.equals("Movimiento aplicado con exito")){
			
				String sIdTransaccion = "";
				int iIdTransaccion = 0;
				
				//OBTENEMOS EL SEQUENCE
				sSql =  "SELECT \n" +
						"CVE_GPO_EMPRESA, \n" +
						"MAX(ID_TRANSACCION) ID_TRANSACCION \n" +
						"FROM \n" +
						"SIM_CAJA_TRANSACCION \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
						"GROUP BY CVE_GPO_EMPRESA \n";
				ejecutaSql();
					
				if (rs.next()){
					
					sIdTransaccion = rs.getString("ID_TRANSACCION");
					iIdTransaccion=Integer.parseInt(sIdTransaccion.trim());
					iIdTransaccion ++;
					sIdTransaccion= String.valueOf(iIdTransaccion);
				}else {
				
					sIdTransaccion = "1";
				}
				
				sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_TRANSACCION, \n" +
						"ID_SUCURSAL, \n" +
						"ID_CAJA, \n" +
						"CVE_MOVIMIENTO_CAJA, \n" +
						"ID_PRESTAMO, \n"+
						"ID_CLIENTE, \n"+
						"NUM_CICLO, \n"+
						"MONTO, \n"+
						"CVE_USUARIO_CAJERO, \n" +
						"FECHA_APLICACION, \n" +
						"FECHA_TRANSACCION, \n" +
						"ID_TRANSACCION_GRUPO, \n" +
						"FECHA_CANCELACION) \n" +
				        "VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						sIdTransaccion + ", \n "+
						"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
						"'CANPAGO', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
						"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
						"-'" + (String)registro.getDefCampo("MONTO") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
						"'" + (String)registro.getDefCampo("F_APLICACION") + "', \n" +
						"'" + (String)registro.getDefCampo("F_APLICACION") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_TRANSACCION_GRUPO") + "', \n" +
						"SYSDATE) \n" ;
				
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
			
		}else if (registro.getDefCampo("APLICA_A").equals("GRUPAL")){
			//Cancela un pr�stamo grupal.
			String fAplicacion = "";

			sSql = "SELECT TO_CHAR(TO_DATE('"+(String)registro.getDefCampo("F_APLICACION")+"','DD-MM-YYYY'),'DD-MON-YY') AS F_APLICA FROM DUAL \n";
	        
			ejecutaSql();
			if (rs.next()){
				fAplicacion = rs.getString("F_APLICA");
			}

			
			sSql = "SELECT \n"+
					"GD.ID_PRESTAMO_GRUPO, \n"+ 
					"M.ID_MOVIMIENTO, \n"+
					"PG.CVE_PRESTAMO_GRUPO, \n"+
					"'GRUPAL' APLICA_A, \n"+
					"G.ID_GRUPO, \n"+
					"'' NOM_COMPLETO, \n"+
					"G.NOM_GRUPO, \n"+
					"PG.ID_PRODUCTO, \n"+
					"PRO.NOM_PRODUCTO, \n"+
					"PG.NUM_CICLO, \n"+
					"M.F_APLICACION, \n"+
					"M.ID_GRUPO, \n"+
				    "SUM(NVL(ROUND(M.IMP_NETO,2),0)) MONTO, \n"+
				    "TO_CHAR(SUM(NVL(ROUND(M.IMP_NETO,2),0)),'999,999,999.99') IMPORTE \n"+
					"FROM \n"+
					"SIM_PRESTAMO_GPO_DET GD, \n"+ 
					"SIM_PRESTAMO_GRUPO PG, \n"+
					"SIM_GRUPO G, \n"+
					"SIM_PRESTAMO P, \n"+
					"SIM_PRODUCTO PRO, \n"+
					"PFIN_MOVIMIENTO M \n"+
					"WHERE GD.CVE_GPO_EMPRESA = 'SIM' \n"+ 
					"AND GD.CVE_EMPRESA = 'CREDICONFIA' \n"+
					"AND PG.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
					"AND PG.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
					"AND PG.ID_PRESTAMO_GRUPO = GD.ID_PRESTAMO_GRUPO \n"+  
					"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
					"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
					"AND P.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
					"AND P.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
					"AND P.ID_PRESTAMO = GD.ID_PRESTAMO \n"+
					"AND PRO.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
					"AND PRO.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND PRO.ID_PRODUCTO = P.ID_PRODUCTO \n"+
					"AND M.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
					"AND M.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
					"AND M.ID_PRESTAMO = GD.ID_PRESTAMO \n"+
					"AND M.CVE_OPERACION = 'CRPAGOPRES' \n"+
					"AND PG.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND M.F_APLICACION = '" + fAplicacion + "' \n"+
					"GROUP BY GD.ID_PRESTAMO_GRUPO, M.ID_MOVIMIENTO, PG.CVE_PRESTAMO_GRUPO, 'GRUPAL', G.ID_GRUPO, '', G.NOM_GRUPO, PG.ID_PRODUCTO, PRO.NOM_PRODUCTO, PG.NUM_CICLO, M.F_APLICACION, M.ID_GRUPO \n";
			 
				ejecutaSql();
				if (rs.next()){
					sIdMovimiento = rs.getString("ID_MOVIMIENTO");
					registro.addDefCampo("ID_GRUPO",rs.getString("ID_GRUPO"));
				}	
					
				CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaPago(?,?,?,?,?)); end;");
				sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
				sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
				sto1.setString(3, sIdMovimiento);
				sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
				sto1.registerOutParameter(5, java.sql.Types.VARCHAR);
					
				//EJECUTA EL PROCEDIMIENTO ALMACENADO
				sto1.execute();
				sTxrespuesta = sto1.getString(5);
				sto1.close();
				
				if (sTxrespuesta.equals("Movimiento aplicado con exito")){
					
					String sIdTransaccion = "";
					int iIdTransaccion = 0;
					
					//OBTENEMOS EL SEQUENCE
					sSql =  "SELECT \n" +
							"CVE_GPO_EMPRESA, \n" +
							"MAX(ID_TRANSACCION) ID_TRANSACCION \n" +
							"FROM \n" +
							"SIM_CAJA_TRANSACCION \n" +
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
							"GROUP BY CVE_GPO_EMPRESA \n";
					ejecutaSql();
						
					if (rs.next()){
						
						sIdTransaccion = rs.getString("ID_TRANSACCION");
						iIdTransaccion=Integer.parseInt(sIdTransaccion.trim());
						iIdTransaccion ++;
						sIdTransaccion= String.valueOf(iIdTransaccion);
					}else {
					
						sIdTransaccion = "1";
					}
					
					sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_TRANSACCION, \n" +
							"ID_SUCURSAL, \n" +
							"ID_CAJA, \n" +
							"CVE_MOVIMIENTO_CAJA, \n" +
							"ID_PRESTAMO, \n"+
							"ID_GRUPO, \n"+
							"NUM_CICLO, \n"+
							"MONTO, \n"+
							"CVE_USUARIO_CAJERO, \n" +
							"FECHA_APLICACION, \n" +
							"FECHA_TRANSACCION, \n" +
							"ID_TRANSACCION_GRUPO, \n" +
							"FECHA_CANCELACION) \n" +
					        "VALUES ( \n"+
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							sIdTransaccion + ", \n "+
							"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
							"'CANPAGO', \n" +
							"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
							"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
							"-'" + (String)registro.getDefCampo("MONTO") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
							"'" + (String)registro.getDefCampo("F_APLICACION") + "', \n" +
							"'" + (String)registro.getDefCampo("F_APLICACION") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_TRANSACCION_GRUPO") + "', \n" +
							"SYSDATE) \n" ;
					
					//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
				}
			
		}
		resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxrespuesta);
		
		return resultadoCatalogo;
	}
}