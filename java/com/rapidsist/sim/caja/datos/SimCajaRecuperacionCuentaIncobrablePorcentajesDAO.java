/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para hacer el corte de Caja.
 */
 
public class SimCajaRecuperacionCuentaIncobrablePorcentajesDAO extends Conexion2 implements OperacionConsultaRegistro {
	
	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
		"A.INCOBRABLE_TOTAL, \n"+
		"B.INCOBRABLE_CAPITAL, \n"+
		"C.INCOBRABLE_INTERES, \n"+
		"D.INCOBRABLE_RECARGO, \n"+
		"E.INCOBRABLE_ACCESORIOS, \n"+
		"TRUNC(B.INCOBRABLE_CAPITAL * 100 / A.INCOBRABLE_TOTAL,4) || '%' PORC_CAPITAL, \n"+
		"TRUNC(C.INCOBRABLE_INTERES * 100 / A.INCOBRABLE_TOTAL,4) || '%' PORC_INTERES, \n"+
		"TRUNC(D.INCOBRABLE_RECARGO * 100 / A.INCOBRABLE_TOTAL,4) || '%' PORC_RECARGO, \n"+
		"TRUNC(E.INCOBRABLE_ACCESORIOS * 100/ A.INCOBRABLE_TOTAL,4) || '%' PORC_ACCESORIOS \n"+

		"FROM \n"+
		" (SELECT \n"+
		"			SUM(IMP_CONCEPTO) INCOBRABLE_TOTAL FROM ( \n"+ 
		"			SELECT  \n"+
		"			CVE_GPO_EMPRESA, \n"+ 
		"			CVE_EMPRESA,  \n"+
		"			ID_PRESTAMO,  \n"+
		"			F_APLICACION, \n"+
		"			F_OPERACION FECHA_OPERACION, \n"+ 
		"			DESC_MOVIMIENTO DESCRIPCION, \n"+
		"			IMP_PAGO, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+ 
		"        	ID_MOVIMIENTO, \n"+
		"         	IMP_CONCEPTO, \n"+
		"         	SUM(IMP_CONCEPTO) OVER ( \n"+ 
		"         	ORDER BY F_APLICACION, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+
		"         	ID_MOVIMIENTO, \n"+
		"         	DESC_MOVIMIENTO) AS IMP_SALDO \n";
		
		if (parametros.getDefCampo("APLICA_A").equals("GRUPO")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_GPO \n";
		}else if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_IND \n";
		}
		
		sSql = sSql + "WHERE CVE_GPO_EMPRESA = 'SIM' AND \n"+ 
		"         	CVE_EMPRESA     = 'CREDICONFIA'  AND \n"+
		"         	ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
		"            AND F_OPERACION <= (SELECT  F_MEDIO \n"+  
		"			                    FROM    PFIN_PARAMETRO \n"+  
		"			                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
		"			                    AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
		"			                    AND CVE_MEDIO       = 'SYSTEM') \n"+
		"        AND DESC_MOVIMIENTO LIKE '%Incobrable%' \n"+
		"			))A, \n"+
		      
		"       (SELECT  \n"+
		"			SUM(IMP_CONCEPTO) INCOBRABLE_CAPITAL FROM ( \n"+ 
		"			SELECT \n"+
		"			CVE_GPO_EMPRESA, \n"+ 
		"			CVE_EMPRESA,  \n"+
		"			ID_PRESTAMO, \n"+
		"			F_APLICACION, \n"+
		"			F_OPERACION FECHA_OPERACION, \n"+ 
		"			DESC_MOVIMIENTO DESCRIPCION, \n"+
		"			IMP_PAGO, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+ 
		"         	ID_MOVIMIENTO, \n"+
		"         	IMP_CONCEPTO, \n"+
		"         	SUM(IMP_CONCEPTO) OVER ( \n"+ 
		"         	ORDER BY F_APLICACION, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+
		"         	ID_MOVIMIENTO, \n"+
		"         	DESC_MOVIMIENTO) AS IMP_SALDO \n";
		
		if (parametros.getDefCampo("APLICA_A").equals("GRUPO")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_GPO \n";
		}else if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_IND \n";
		}
		
	
		sSql = sSql + "WHERE CVE_GPO_EMPRESA = 'SIM' AND \n"+ 
		"         	CVE_EMPRESA     = 'CREDICONFIA'  AND \n"+
		"         	ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
		"            AND F_OPERACION <= (SELECT  F_MEDIO \n"+  
		"			                    FROM    PFIN_PARAMETRO \n"+  
		"			                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
		"			                    AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
		"			                    AND CVE_MEDIO       = 'SYSTEM') \n"+
		"        AND DESC_MOVIMIENTO LIKE '%Incobrable Capital%' \n"+
		"			))B, \n"+
		     
		"       (SELECT \n"+
		"			SUM(IMP_CONCEPTO) INCOBRABLE_INTERES FROM ( \n"+ 
		"			SELECT \n"+
		"			CVE_GPO_EMPRESA, \n"+ 
		"			CVE_EMPRESA, \n"+
		"			ID_PRESTAMO, \n"+
		"			F_APLICACION, \n"+
		"			F_OPERACION FECHA_OPERACION, \n"+ 
		"			DESC_MOVIMIENTO DESCRIPCION, \n"+
		"			IMP_PAGO, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+ 
		"         	ID_MOVIMIENTO, \n"+
		"         	IMP_CONCEPTO, \n"+
		"         	SUM(IMP_CONCEPTO) OVER ( \n"+ 
		"         	ORDER BY F_APLICACION, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+
		"         	ID_MOVIMIENTO, \n"+
		"         	DESC_MOVIMIENTO) AS IMP_SALDO \n";
		
		if (parametros.getDefCampo("APLICA_A").equals("GRUPO")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_GPO \n";
		}else if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_IND \n";
		}
		
	
		sSql = sSql + "WHERE CVE_GPO_EMPRESA = 'SIM' AND \n"+ 
		"         	CVE_EMPRESA     = 'CREDICONFIA'  AND \n"+
		"         	ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
		"            AND F_OPERACION <= (SELECT  F_MEDIO \n"+  
		"			                    FROM    PFIN_PARAMETRO \n"+  
		"			                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
		"			                    AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
		"			                    AND CVE_MEDIO       = 'SYSTEM') \n"+
		"        AND DESC_MOVIMIENTO LIKE '%Incobrable Interés%' \n"+
		"			))C, \n"+
		      
		"       (SELECT \n"+
		"			SUM(IMP_CONCEPTO) INCOBRABLE_RECARGO FROM ( \n"+ 
		"			SELECT \n"+
		"			CVE_GPO_EMPRESA, \n"+ 
		"			CVE_EMPRESA, \n"+
		"			ID_PRESTAMO, \n"+
		"			F_APLICACION, \n"+
		"			F_OPERACION FECHA_OPERACION, \n"+ 
		"			DESC_MOVIMIENTO DESCRIPCION, \n"+
		"			IMP_PAGO, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+ 
		"         	ID_MOVIMIENTO, \n"+
		"         	IMP_CONCEPTO, \n"+
		"         	SUM(IMP_CONCEPTO) OVER ( \n"+ 
		"         	ORDER BY F_APLICACION, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+
		"         	ID_MOVIMIENTO, \n"+
		"         	DESC_MOVIMIENTO) AS IMP_SALDO \n";
		
		if (parametros.getDefCampo("APLICA_A").equals("GRUPO")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_GPO \n";
		}else if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_IND \n";
		}
		
		sSql = sSql + "WHERE CVE_GPO_EMPRESA = 'SIM' AND \n"+ 
		"         	CVE_EMPRESA     = 'CREDICONFIA'  AND \n"+
		"         	ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
		"            AND F_OPERACION <= (SELECT  F_MEDIO \n"+  
		"			                    FROM    PFIN_PARAMETRO \n"+  
		"			                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
		"			                    AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
		"			                    AND CVE_MEDIO       = 'SYSTEM') \n"+
		"        AND DESC_MOVIMIENTO LIKE '%Incobrable Pago Tardío%' \n"+
		"			))D, \n"+
		      
		" (SELECT \n"+
		"			SUM(IMP_CONCEPTO) INCOBRABLE_ACCESORIOS FROM ( \n"+ 
		"			SELECT \n"+
		"			CVE_GPO_EMPRESA, \n"+ 
		"			CVE_EMPRESA, \n"+
		"			ID_PRESTAMO, \n"+
		"			F_APLICACION, \n"+
		"			F_OPERACION FECHA_OPERACION, \n"+
		"			DESC_MOVIMIENTO DESCRIPCION, \n"+
		"			IMP_PAGO, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+ 
		"         	ID_MOVIMIENTO, \n"+
		"         	IMP_CONCEPTO, \n"+
		"         	SUM(IMP_CONCEPTO) OVER ( \n"+ 
		"         	ORDER BY F_APLICACION, \n"+
		"         	NUM_PAGO_AMORTIZACION, \n"+
		"         	ID_MOVIMIENTO, \n"+
		"         	DESC_MOVIMIENTO) AS IMP_SALDO \n";
		
		if (parametros.getDefCampo("APLICA_A").equals("GRUPO")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_GPO \n";
		}else if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")) {
			sSql = sSql + "FROM V_MOV_EDO_CTA_IND \n";
		}
		
		sSql = sSql + "WHERE CVE_GPO_EMPRESA = 'SIM' AND \n"+ 
		"         	CVE_EMPRESA     = 'CREDICONFIA'  AND \n"+
		"         	ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
		"            AND F_OPERACION <= (SELECT  F_MEDIO \n"+  
		"			                    FROM    PFIN_PARAMETRO \n"+  
		"			                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
		"			                    AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
		"			                    AND CVE_MEDIO       = 'SYSTEM') \n"+
		"        AND (DESC_MOVIMIENTO LIKE '%Incobrable Administracion Crediticia%' OR DESC_MOVIMIENTO LIKE '%Incobrable Seguro Deudor%') \n"+
		"			))E \n";		
		
		System.out.println("pocentajes"+sSql);
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}