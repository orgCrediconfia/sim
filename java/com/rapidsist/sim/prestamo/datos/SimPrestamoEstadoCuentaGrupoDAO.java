/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para la consulta del estado de cuenta de los créditos.
 */
 
public class SimPrestamoEstadoCuentaGrupoDAO extends Conexion2 implements OperacionConsultaTabla  {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		String sConsulta = "";
		String sNumIntegrantes = "";
		int iNumIntegrantes = 0;
		
		if (parametros.getDefCampo("CONSULTA").equals("REGISTRO")){	
			sSql =  "SELECT \n"+
				"P.ID_PRESTAMO, \n"+
				"P.CVE_METODO, \n"+
				"P.ID_CLIENTE, \n"+
				"P.ID_GRUPO, \n"+
				"PE.NOM_COMPLETO, \n"+
				"P.ID_GRUPO, \n"+
				"S.NOM_SUCURSAL, \n"+
				"S.TASA_IVA, \n"+
				"PP.NOM_PERIODICIDAD PERIODICIDAD_PRODUCTO, \n"+
				"P.PLAZO, \n"+
				"P.VALOR_TASA, \n"+
				"PT.NOM_PERIODICIDAD PERIODICIDAD_TASA, \n"+
				"M.MONTO_AUTORIZADO, \n"+
				"TA.FECHA_AMORTIZACION \n"+
				"FROM \n"+
				"SIM_PRESTAMO P, \n"+
				"SIM_CLIENTE_MONTO M, \n"+
				"SIM_TABLA_AMORTIZACION TA, \n"+
				"SIM_CAT_SUCURSAL S, \n"+
				"SIM_CAT_PERIODICIDAD PP, \n"+
				"SIM_CAT_PERIODICIDAD PT, \n"+
				"RS_GRAL_PERSONA PE \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
				"AND P.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND P.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' \n"+
				"AND M.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND M.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND M.ID_PRESTAMO = P.ID_PRESTAMO \n"+
				"AND M.ID_CLIENTE = P.ID_CLIENTE \n"+
				"AND TA.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND TA.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND TA.ID_PRESTAMO (+)= P.ID_PRESTAMO \n"+
				"AND TA.NUM_PAGO_AMORTIZACION (+)= '1' \n"+
				"AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL = P.ID_SUCURSAL \n"+
				"AND PP.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PP.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PP.ID_PERIODICIDAD = P.ID_PERIODICIDAD_PRODUCTO \n"+
				"AND PT.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PT.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PT.ID_PERIODICIDAD = P.ID_PERIODICIDAD_TASA \n"+
				"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
				"ORDER BY P.ID_PRESTAMO \n";
			
		}else if (parametros.getDefCampo("CONSULTA").equals("MOVIMIENTOS")){	
			
			sSql =  "SELECT \n"+
					"CVE_GPO_EMPRESA, \n"+
					"CVE_EMPRESA, \n"+
					"ID_PRESTAMO, \n"+
					"F_APLICACION, \n"+
					"NUM_PAGO_AMORTIZACION, \n"+
					"F_OPERACION FECHA_OPERACION, \n"+
					"DESC_MOVIMIENTO DESCRIPCION, \n"+
					"TO_CHAR(SUM(NVL(ROUND(IMP_PAGO,2),0)),'999,999,999.99') IMPORTE, \n"+
		         	"TO_CHAR(SUM(NVL(ROUND(IMP_CONCEPTO,2),0)),'999,999,999.99') IMP_DESGLOSE, \n"+
		         	"SUM(NVL(ROUND(IMP_CONCEPTO,2),0)) \n"+
		    		"FROM V_MOV_EDO_CTA_GPO \n"+
		   			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' AND \n"+
		         	"CVE_EMPRESA     = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "'  AND \n"+
		         	"ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
		            "AND F_OPERACION <= (SELECT  F_MEDIO \n"+ 
					"                    FROM    PFIN_PARAMETRO \n"+ 
					"                    WHERE   CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"                    AND CVE_EMPRESA     = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+ 
					"                    AND CVE_MEDIO       = 'SYSTEM') \n"+
					"AND NUM_PAGO_AMORTIZACION != 0 \n"+
					//"AND DESC_MOVIMIENTO != ' ' \n"+
					"GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, ID_PRESTAMO, F_APLICACION, NUM_PAGO_AMORTIZACION, F_OPERACION, DESC_MOVIMIENTO \n"+
					"ORDER BY F_APLICACION, FECHA_OPERACION, NUM_PAGO_AMORTIZACION, SUM(NVL(ROUND(IMP_CONCEPTO,2),0)) \n";
			
		}else if (parametros.getDefCampo("CONSULTA").equals("SALDO_FECHA")){	
		 
			sSql =  "SELECT \n"+
			"TO_CHAR(SUM(IMP_CONCEPTO),'999,999,999.99') IMP_DESGLOSE FROM ( \n"+
			"SELECT \n"+
			"CVE_GPO_EMPRESA, \n"+
			"CVE_EMPRESA, \n"+
			"ID_PRESTAMO, \n"+
			"F_APLICACION, \n"+
			"F_OPERACION FECHA_OPERACION, \n"+
			"DESC_MOVIMIENTO DESCRIPCION, \n"+
			"IMP_PAGO, \n"+
         	"NUM_PAGO_AMORTIZACION, \n"+
         	"ID_MOVIMIENTO, \n"+
         	"IMP_CONCEPTO, \n"+
         	"SUM(IMP_CONCEPTO) OVER ( \n"+
         	"ORDER BY F_APLICACION, \n"+
         	"NUM_PAGO_AMORTIZACION, \n"+
         	"ID_MOVIMIENTO, \n"+
         	"DESC_MOVIMIENTO) AS IMP_SALDO \n"+
    		"FROM V_MOV_EDO_CTA_GPO \n"+
   			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' AND \n"+
         	"CVE_EMPRESA     = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "'  AND \n"+
         	"ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
            "AND F_OPERACION <= (SELECT  F_MEDIO \n"+ 
			"                    FROM    PFIN_PARAMETRO \n"+ 
			"                    WHERE   CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"                    AND CVE_EMPRESA     = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+ 
			"                    AND CVE_MEDIO       = 'SYSTEM') \n"+
			") \n";
			
		}
		ejecutaSql();
		return getConsultaLista();
	}
}