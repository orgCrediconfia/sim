/**
 * Sistema de administración de portales.
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
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
				      "VR.CVE_GPO_EMPRESA, \n"+
				      "VR.CVE_EMPRESA, \n"+
				      "VR.ID_PRESTAMO, \n"+
				      "PG.CVE_PRESTAMO_GRUPO CVE_PRESTAMO, \n"+
				      "'GRUPAL' APLICA_A, \n"+
				      "'' NOM_COMPLETO, \n"+
				      "G.NOM_GRUPO, \n"+ 
				      "O.NOM_PRODUCTO, \n"+
				      "PG.NUM_CICLO, \n"+ 
				      "G.ID_SUCURSAL, \n"+
				      "VR.F_APLICACION, \n"+
				      "VR.NUM_PAGO_AMORTIZACION, \n"+ 
				      "VR.F_OPERACION FECHA_OPERACION, \n"+ 
				      "VG.FECHA_AMORTIZACION, \n"+
				      "VR.DESC_MOVIMIENTO DESCRIPCION, \n"+ 
				      "SUM(NVL(ROUND(VR.IMP_PAGO,2),0)) MONTO, \n"+
				      "TO_CHAR(SUM(NVL(ROUND(VR.IMP_PAGO,2),0)),'999,999,999.99') IMPORTE \n"+
				"FROM V_MOV_EDO_CTA_GPO VR, \n"+
				" 		 V_TABLA_AMORTIZACION_GRUPAL VG, \n"+
				"     SIM_PRESTAMO_GRUPO PG, \n"+
				"     SIM_GRUPO G, \n"+
				"     SIM_PRODUCTO O \n"+
				"WHERE VR.CVE_GPO_EMPRESA = 'SIM' \n"+ 
				"AND VR.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
				"AND VR.F_OPERACION <= (SELECT  F_MEDIO  \n"+
				"		                    FROM    PFIN_PARAMETRO \n"+  
				"		                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
				"		                    AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
				"		                    AND CVE_MEDIO       = 'SYSTEM') \n"+
				"AND VR.NUM_PAGO_AMORTIZACION != 0 \n"+
				"AND VG.CVE_GPO_EMPRESA = VR.CVE_GPO_EMPRESA \n"+ 
				"AND VG.CVE_EMPRESA = VR.CVE_EMPRESA \n"+
				"AND VG.ID_PRESTAMO_GRUPO = VR.ID_PRESTAMO \n"+ 
				"AND VG.NUM_PAGO_AMORTIZACION = VR.NUM_PAGO_AMORTIZACION \n"+ 
				"AND PG.CVE_GPO_EMPRESA = VG.CVE_GPO_EMPRESA \n"+
				"AND PG.CVE_EMPRESA = VG.CVE_EMPRESA \n"+
				"AND PG.ID_PRESTAMO_GRUPO = VG.ID_PRESTAMO_GRUPO \n"+
				"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND G.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
				"AND O.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND O.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND O.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
				"AND VR.DESC_MOVIMIENTO = 'Pago prestamo' \n";
		
				if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
					sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
					sSql = sSql + "AND O.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
				}
				if (parametros.getDefCampo("NUM_CICLO") != null) {
					sSql = sSql + "AND PG.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_GRUPO") != null) {
					sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
				}
				
				sSql = sSql + "GROUP BY VR.CVE_GPO_EMPRESA, VR.CVE_EMPRESA, VR.ID_PRESTAMO, PG.CVE_PRESTAMO_GRUPO, 'GRUPAL', '', G.NOM_GRUPO, O.NOM_PRODUCTO, PG.NUM_CICLO, G.ID_SUCURSAL, VR.F_APLICACION, VR.NUM_PAGO_AMORTIZACION, VR.F_OPERACION, VG.FECHA_AMORTIZACION, VR.DESC_MOVIMIENTO \n"+ 
				"UNION ALL \n"+
				"SELECT \n"+
				"      RI.CVE_GPO_EMPRESA, \n"+ 
				"      RI.CVE_EMPRESA, \n"+
				"      RI.ID_PRESTAMO, \n"+
				"      P.CVE_PRESTAMO, \n"+
				"      'INDIVIDUAL' APLICA_A, \n"+
				"      PE.NOM_COMPLETO, \n"+
				"      '' NOM_GRUPO, \n"+
				"      O.NOM_PRODUCTO, \n"+
				"      P.NUM_CICLO, \n"+
				"      P.ID_SUCURSAL, \n"+
				"      RI.F_APLICACION, \n"+
				"      RI.NUM_PAGO_AMORTIZACION, \n"+ 
				"      RI.F_OPERACION FECHA_OPERACION, \n"+ 
				"      VI.FECHA_AMORTIZACION, \n"+
				"      RI.DESC_MOVIMIENTO DESCRIPCION, \n"+ 
				"      NVL(ROUND(RI.IMP_PAGO,2),0) MONTO, \n"+
				"      TO_CHAR(NVL(ROUND(RI.IMP_PAGO,2),0),'999,999,999.99') IMPORTE \n"+
				"FROM V_MOV_EDO_CTA_IND RI, \n"+
				"	   V_TABLA_AMORT_INDIVIDUAL VI, \n"+
				"     SIM_PRESTAMO P, \n"+
				"     RS_GRAL_PERSONA PE, \n"+
				"     SIM_PRODUCTO O \n"+
				"WHERE RI.CVE_GPO_EMPRESA = 'SIM' \n"+ 
				"AND RI.CVE_EMPRESA     = 'CREDICONFIA' \n"+  
				"AND RI.F_OPERACION <= (SELECT  F_MEDIO \n"+
				"					             FROM    PFIN_PARAMETRO \n"+  
				"					             WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
				"					             AND CVE_EMPRESA     = 'CREDICONFIA' \n"+  
				"					             AND CVE_MEDIO       = 'SYSTEM') \n"+
				"AND RI.NUM_PAGO_AMORTIZACION != 0 \n"+					
				"AND VI.CVE_GPO_EMPRESA = RI.CVE_GPO_EMPRESA \n"+ 
				"AND VI.CVE_EMPRESA = RI.CVE_EMPRESA \n"+
				"AND VI.ID_PRESTAMO = RI.ID_PRESTAMO \n"+
				"AND VI.NUM_PAGO_AMORTIZACION = RI.NUM_PAGO_AMORTIZACION \n"+ 
				"AND P.CVE_GPO_EMPRESA = VI.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = VI.CVE_EMPRESA \n"+
				"AND P.ID_PRESTAMO = VI.ID_PRESTAMO \n"+
				"AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
				"AND O.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND O.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND O.ID_PRODUCTO = P.ID_PRODUCTO \n"+
				"AND P.ID_GRUPO IS NULL \n"+
				"AND RI.DESC_MOVIMIENTO = 'Pago prestamo' \n";
				
				 
				if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
					sSql = sSql + "AND P.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
					sSql = sSql + "AND O.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
				}
				if (parametros.getDefCampo("NUM_CICLO") != null) {
					sSql = sSql + "AND P.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_CLIENTE") != null) {
					sSql = sSql + "AND PE.NOM_COMPLETO = '" + (String) parametros.getDefCampo("NOM_CLIENTE") + "' \n";
				}
				
				sSql = sSql + "MINUS \n"+
				"SELECT \n"+
				"CT.CVE_GPO_EMPRESA, \n"+ 
				"				      CT.CVE_EMPRESA, \n"+ 
				"				      CT.ID_PRESTAMO,  \n"+
				"				      PG.CVE_PRESTAMO_GRUPO CVE_PRESTAMO, \n"+ 
				"				      'GRUPAL' APLICA_A, \n"+
				"				      '' NOM_COMPLETO, \n"+
				"				      G.NOM_GRUPO,  \n"+
				"				      O.NOM_PRODUCTO, \n"+
				"				      PG.NUM_CICLO,  \n"+
				"				      CT.ID_SUCURSAL, \n"+
				"				      CT.FECHA_APLICACION F_APLICACION, \n"+ 
				"				      CT.NUM_PAGO_AMORTIZACION, \n"+ 
				"				      CT.FECHA_REGISTRO FECHA_OPERACION, \n"+  
				"				      CT.FECHA_TRANSACCION FECHA_AMORTIZACION, \n"+ 
				"				      'Pago prestamo' DESCRIPCION, \n"+ 
				"				      NVL(ROUND(CT.MONTO,2),0) MONTO, \n"+
				"				      TO_CHAR(NVL(ROUND(CT.MONTO,2),0),'999,999,999.99') IMPORTE \n"+
				"FROM SIM_CAJA_TRANSACCION CT, \n"+
				"SIM_PRESTAMO_GRUPO PG, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRODUCTO O \n"+
				"WHERE CT.CVE_GPO_EMPRESA = 'SIM' \n"+
				"AND CT.CVE_EMPRESA = 'CREDICONFIA' \n"+
				"AND CT.CVE_MOVIMIENTO_CAJA = 'CANPAGO' \n"+
				"AND PG.CVE_GPO_EMPRESA = CT.CVE_GPO_EMPRESA \n"+
				"AND PG.CVE_EMPRESA = CT.CVE_EMPRESA \n"+
				"AND PG.ID_PRESTAMO_GRUPO = CT.ID_PRESTAMO \n"+
				"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
				"AND O.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND O.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND O.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
				"AND G.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n";
				
				if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
					sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
					sSql = sSql + "AND O.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
				}
				if (parametros.getDefCampo("NUM_CICLO") != null) {
					sSql = sSql + "AND PG.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_GRUPO") != null) {
					sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
				}
				
				sSql = sSql + "UNION ALL \n"+
				" SELECT \n"+
				" CT.CVE_GPO_EMPRESA, \n"+ 
				"				      CT.CVE_EMPRESA, \n"+ 
				"				      CT.ID_PRESTAMO,  \n"+
				"				      P.CVE_PRESTAMO, \n"+
				"				      'INDIVIDUAL' APLICA_A, \n"+ 
				"				      PE.NOM_COMPLETO, \n"+
				"				      '' NOM_GRUPO,  \n"+
				"				      O.NOM_PRODUCTO, \n"+ 
				"				      P.NUM_CICLO,  \n"+
				"				      CT.ID_SUCURSAL, \n"+
				"				      CT.FECHA_APLICACION F_APLICACION, \n"+ 
				"				      CT.NUM_PAGO_AMORTIZACION, \n"+
				"				      CT.FECHA_REGISTRO FECHA_OPERACION, \n"+  
				"				      CT.FECHA_TRANSACCION FECHA_AMORTIZACION, \n"+ 
				"				      'Pago prestamo' DESCRIPCION, \n"+ 
				"				      NVL(ROUND(CT.MONTO,2),0) MONTO, \n"+
				"				      TO_CHAR(NVL(ROUND(CT.MONTO,2),0),'999,999,999.99') IMPORTE \n"+
				"FROM SIM_CAJA_TRANSACCION CT, \n"+
				"SIM_PRESTAMO P, \n"+
				"RS_GRAL_PERSONA PE, \n"+
				"SIM_PRODUCTO O \n"+
				"WHERE CT.CVE_GPO_EMPRESA = 'SIM' \n"+
				"AND CT.CVE_EMPRESA = 'CREDICONFIA' \n"+
				"AND CT.CVE_MOVIMIENTO_CAJA = 'CANPAGO' \n"+
				"AND P.CVE_GPO_EMPRESA = CT.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = CT.CVE_EMPRESA \n"+
				"AND P.ID_PRESTAMO = CT.ID_PRESTAMO \n"+
				"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
				"AND O.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND O.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND O.ID_PRODUCTO = P.ID_PRODUCTO \n"+
				"AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n";
				
				if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
					sSql = sSql + "AND P.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
					sSql = sSql + "AND O.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
				}
				if (parametros.getDefCampo("NUM_CICLO") != null) {
					sSql = sSql + "AND P.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
				}
				if (parametros.getDefCampo("NOM_CLIENTE") != null) {
					sSql = sSql + "AND PE.NOM_COMPLETO = '" + (String) parametros.getDefCampo("NOM_CLIENTE") + "' \n";
				}
				
				sSql = sSql + "ORDER BY ID_PRESTAMO, NUM_CICLO, NUM_PAGO_AMORTIZACION \n";
		
		System.out.println("Pagos para ser cancelados"+sSql);
		
		ejecutaSql();
		return getConsultaLista();
	}
	

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdMovimiento = "";
		String sTxrespuesta = "";
		
		if (registro.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
			//Cancela un préstamo indiviudal.
			
			sSql =  "SELECT \n"+
					"      RI.CVE_GPO_EMPRESA, \n"+ 
					"      RI.CVE_EMPRESA, \n"+
					"      RI.ID_PRESTAMO, \n"+
					"      RI.ID_MOVIMIENTO, \n"+
					"      'INDIVIDUAL' APLICA_A, \n"+
					"      PE.NOM_COMPLETO, \n"+
					"      '' NOM_GRUPO, \n"+
					"      O.NOM_PRODUCTO, \n"+
					"      P.NUM_CICLO, \n"+
					"      P.ID_SUCURSAL, \n"+
					"      RI.F_APLICACION, \n"+
					"      RI.NUM_PAGO_AMORTIZACION, \n"+ 
					"      RI.F_OPERACION FECHA_OPERACION, \n"+ 
					"      VI.FECHA_AMORTIZACION, \n"+
					"      RI.DESC_MOVIMIENTO DESCRIPCION, \n"+ 
					"      TO_CHAR(NVL(ROUND(RI.IMP_PAGO,2),0),'999,999,999.99') MONTO \n"+
					"FROM V_MOV_EDO_CTA_IND RI, \n"+
					"	   V_TABLA_AMORT_INDIVIDUAL VI, \n"+
					"     SIM_PRESTAMO P, \n"+
					"     RS_GRAL_PERSONA PE, \n"+
					"     SIM_PRODUCTO O \n"+
					"WHERE RI.CVE_GPO_EMPRESA = 'SIM' \n"+ 
					"AND RI.CVE_EMPRESA     = 'CREDICONFIA' \n"+  
					"AND RI.F_OPERACION <= (SELECT  F_MEDIO \n"+
					"					             FROM    PFIN_PARAMETRO \n"+  
					"					             WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
					"					             AND CVE_EMPRESA     = 'CREDICONFIA' \n"+  
					"					             AND CVE_MEDIO       = 'SYSTEM') \n"+
					"AND RI.NUM_PAGO_AMORTIZACION != 0 \n"+					
					"AND VI.CVE_GPO_EMPRESA = RI.CVE_GPO_EMPRESA \n"+ 
					"AND VI.CVE_EMPRESA = RI.CVE_EMPRESA \n"+
					"AND VI.ID_PRESTAMO = RI.ID_PRESTAMO \n"+
					"AND VI.NUM_PAGO_AMORTIZACION = RI.NUM_PAGO_AMORTIZACION \n"+ 
					"AND P.CVE_GPO_EMPRESA = VI.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = VI.CVE_EMPRESA \n"+
					"AND P.ID_PRESTAMO = VI.ID_PRESTAMO \n"+
					"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
					"AND O.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND O.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND O.ID_PRODUCTO = P.ID_PRODUCTO \n"+
					"AND P.ID_GRUPO IS NULL \n"+
					"AND RI.DESC_MOVIMIENTO = 'Pago prestamo' \n"+
					"AND RI.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND P.NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
					"AND RI.F_APLICACION = '" + (String)registro.getDefCampo("F_APLICACION") + "' \n"+
					"AND RI.NUM_PAGO_AMORTIZACION = '" + (String)registro.getDefCampo("NUM_PAGO_AMORTIZACION") + "' \n"+
					"AND RI.F_OPERACION = '" + (String)registro.getDefCampo("F_OPERACION") + "' \n"+
					"AND VI.FECHA_AMORTIZACION = '" + (String)registro.getDefCampo("FECHA_AMORTIZACION") + "' \n"+
					"AND TO_CHAR(NVL(ROUND(RI.IMP_PAGO,2),0),'999,999,999.99') = '" + (String)registro.getDefCampo("MONTO") + "' \n";
			ejecutaSql();
			if (rs.next()){
				sIdMovimiento = rs.getString("ID_MOVIMIENTO");
			}
			
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaPago(?,?,?,?)); end;");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, sIdMovimiento);
			sto1.registerOutParameter(4, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxrespuesta = sto1.getString(4);
			sto1.close();
			
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
					"NUM_CICLO, \n"+
					"MONTO, \n"+
					"CVE_USUARIO_CAJERO, \n" +
					"FECHA_TRANSACCION, \n" +
					"FECHA_APLICACION, \n" +
					"FECHA_REGISTRO, \n" +
					"FECHA_CANCELACION, \n" +
					"NUM_PAGO_AMORTIZACION) \n" +
			        "VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdTransaccion + ", \n "+
					"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
					"'CANPAGO', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
					"'" + (String)registro.getDefCampo("MONTO") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
					"'" + (String)registro.getDefCampo("FECHA_AMORTIZACION") + "', \n" +
					"'" + (String)registro.getDefCampo("F_APLICACION") + "', \n" +
					"'" + (String)registro.getDefCampo("F_OPERACION") + "', \n" +
					"SYSDATE, \n" +
					"'" + (String)registro.getDefCampo("NUM_PAGO_AMORTIZACION") + "') \n" ;
					
			
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			
		}else if (registro.getDefCampo("APLICA_A").equals("GRUPAL")){
			//Cancela un préstamo grupal.
			
			String sIdPrestamo = "";
		
			sSql =  "SELECT \n"+
					      "VR.CVE_GPO_EMPRESA, \n"+
					      "VR.CVE_EMPRESA, \n"+
					      "VR.ID_PRESTAMO, \n"+
					      "VR.ID_MOVIMIENTO, \n"+
					      "'GRUPAL' APLICA_A, \n"+
					      "'' NOM_COMPLETO, \n"+
					      "G.NOM_GRUPO, \n"+ 
					      "O.NOM_PRODUCTO, \n"+
					      "PG.NUM_CICLO, \n"+ 
					      "G.ID_SUCURSAL, \n"+
					      "VR.F_APLICACION, \n"+
					      "VR.NUM_PAGO_AMORTIZACION, \n"+ 
					      "VR.F_OPERACION FECHA_OPERACION, \n"+ 
					      "VG.FECHA_AMORTIZACION, \n"+
					      "VR.DESC_MOVIMIENTO DESCRIPCION, \n"+ 
					      "TO_CHAR(SUM(NVL(ROUND(VR.IMP_PAGO,2),0)),'999,999,999.99') MONTO \n"+
					"FROM V_MOV_EDO_CTA_GPO VR, \n"+
					" 		 V_TABLA_AMORTIZACION_GRUPAL VG, \n"+
					"     SIM_PRESTAMO_GRUPO PG, \n"+
					"     SIM_GRUPO G, \n"+
					"     SIM_PRODUCTO O \n"+
					"WHERE VR.CVE_GPO_EMPRESA = 'SIM' \n"+ 
					"AND VR.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
					"AND VR.F_OPERACION <= (SELECT  F_MEDIO  \n"+
					"		                    FROM    PFIN_PARAMETRO \n"+  
					"		                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
					"		                    AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
					"		                    AND CVE_MEDIO       = 'SYSTEM') \n"+
					"AND VR.NUM_PAGO_AMORTIZACION != 0 \n"+
					"AND VG.CVE_GPO_EMPRESA = VR.CVE_GPO_EMPRESA \n"+ 
					"AND VG.CVE_EMPRESA = VR.CVE_EMPRESA \n"+
					"AND VG.ID_PRESTAMO_GRUPO = VR.ID_PRESTAMO \n"+ 
					"AND VG.NUM_PAGO_AMORTIZACION = VR.NUM_PAGO_AMORTIZACION \n"+ 
					"AND PG.CVE_GPO_EMPRESA = VG.CVE_GPO_EMPRESA \n"+
					"AND PG.CVE_EMPRESA = VG.CVE_EMPRESA \n"+
					"AND PG.ID_PRESTAMO_GRUPO = VG.ID_PRESTAMO_GRUPO \n"+
					"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
					"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
					"AND O.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
					"AND O.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					"AND O.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
					"AND VR.DESC_MOVIMIENTO = 'Pago prestamo' \n"+
					"AND VR.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND PG.NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
					"AND VR.F_APLICACION = '" + (String)registro.getDefCampo("F_APLICACION") + "' \n"+
					"AND VR.NUM_PAGO_AMORTIZACION = '" + (String)registro.getDefCampo("NUM_PAGO_AMORTIZACION") + "' \n"+
					"AND VR.F_OPERACION = '" + (String)registro.getDefCampo("F_OPERACION") + "' \n"+
					"AND VG.FECHA_AMORTIZACION = '" + (String)registro.getDefCampo("FECHA_AMORTIZACION") + "' \n"+
					"GROUP BY VR.CVE_GPO_EMPRESA, VR.CVE_EMPRESA, VR.ID_PRESTAMO, VR.ID_MOVIMIENTO, 'GRUPAL', '', G.NOM_GRUPO, O.NOM_PRODUCTO, PG.NUM_CICLO, G.ID_SUCURSAL, VR.F_APLICACION, VR.NUM_PAGO_AMORTIZACION, VR.F_OPERACION, VG.FECHA_AMORTIZACION, VR.DESC_MOVIMIENTO \n";
				ejecutaSql();
				while (rs.next()){
					sIdMovimiento = rs.getString("ID_MOVIMIENTO");
				
				
					CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaPago(?,?,?,?)); end;");
					sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
					sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
					sto1.setString(3, sIdMovimiento);
					sto1.registerOutParameter(4, java.sql.Types.VARCHAR);
					
					//EJECUTA EL PROCEDIMIENTO ALMACENADO
					sto1.execute();
					sTxrespuesta = sto1.getString(4);
					sto1.close();
				}
				
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
						"NUM_CICLO, \n"+
						"MONTO, \n"+
						"CVE_USUARIO_CAJERO, \n" +
						"FECHA_TRANSACCION, \n" +
						"FECHA_APLICACION, \n" +
						"FECHA_REGISTRO, \n" +
						"FECHA_CANCELACION, \n" +
						"NUM_PAGO_AMORTIZACION) \n" +
				        "VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						sIdTransaccion + ", \n "+
						"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
						"'CANPAGO', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
						"'" + (String)registro.getDefCampo("MONTO") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
						"'" + (String)registro.getDefCampo("FECHA_AMORTIZACION") + "', \n" +
						"'" + (String)registro.getDefCampo("F_APLICACION") + "', \n" +
						"'" + (String)registro.getDefCampo("F_OPERACION") + "', \n" +
						"SYSDATE, \n" +
						"'" + (String)registro.getDefCampo("NUM_PAGO_AMORTIZACION") + "') \n" ;
				
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			
			
		}
		resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxrespuesta);
		
		return resultadoCatalogo;
	}
}