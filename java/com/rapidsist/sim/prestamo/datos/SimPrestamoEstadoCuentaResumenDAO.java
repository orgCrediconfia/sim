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
 * Administra los accesos a la base de datos para la consulta del resumen del estado de cuenta de los créditos.
 */
 
public class SimPrestamoEstadoCuentaResumenDAO extends Conexion2 implements OperacionConsultaTabla  {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
	String sSql1 = "";
	String sSql2 = "";
	String sSql3 = "";
	
	if (parametros.getDefCampo("CONSULTA").equals("RESUMEN")){
		
		sSql =	 "SELECT \n"+	 
				"ID_ORDEN, INITCAP('TOTAL DE '||C.DESC_LARGA) DESCRIPCION, \n"+
         "TO_CHAR(SUM(DECODE(A.CVE_AFECTA,'I',ABS(A.IMP_NETO),0)),'999,999,999.99') AS IMPORTE, \n"+ 
         "TO_CHAR(SUM(DECODE(A.CVE_AFECTA,'I',0,ABS(A.IMP_NETO))),'999,999,999.99') AS PAGADO, \n"+
         "TO_CHAR((SUM(DECODE(A.CVE_AFECTA,'I',ABS(A.IMP_NETO),0)) - SUM(DECODE(A.CVE_AFECTA,'I',0,ABS(A.IMP_NETO)))),'999,999,999.99') AS SALDO \n"+ 
      
      "FROM ( \n"+        
          
   "SELECT  -10 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+  
   "               (T.IMP_CAPITAL_AMORT*-1) AS IMP_NETO, C.CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
   "           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
   "           AND C.CVE_CONCEPTO    = 'CAPITA' \n"+
   "           AND T.IMP_CAPITAL_AMORT > 0 \n"+
              
   "       UNION ALL \n"+ 
      
   "       SELECT  -9 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+  
   "               (T.IMP_INTERES*-1) AS IMP_NETO, C.CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
   "           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
   "           AND C.CVE_CONCEPTO    = 'INTERE' \n"+
   "           AND T.IMP_INTERES > 0 \n"+
      
   "       UNION ALL \n"+ 

   "       SELECT  -8 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
   "               (T.IMP_IVA_INTERES*-1) AS IMP_NETO, C.CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+ 
   "           AND T.FECHA_AMORTIZACION <= (SELECT  F_MEDIO \n"+  
   "                                       FROM    PFIN_PARAMETRO \n"+  
   "                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
   "                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
   "                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
   "           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
   "           AND C.CVE_CONCEPTO    = 'IVAINT' \n"+
   "           AND T.IMP_IVA_INTERES > 0 \n"+

   "       UNION ALL \n"+ 
      
   "       SELECT  -7 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
   "               (T.IMP_INTERES_EXTRA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
   "           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
   "           AND C.CVE_CONCEPTO    = 'INTEXT' \n"+
   "           AND T.IMP_INTERES_EXTRA  > 0 \n"+

   "       UNION ALL \n"+ 

   "       SELECT  -6 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
   "               (T.IMP_IVA_INTERES_EXTRA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
   "           AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+ 
   "                                       FROM    PFIN_PARAMETRO \n"+  
   "                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
   "                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
   "           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
   "           AND C.CVE_CONCEPTO    = 'IVAINTEX' \n"+
   "           AND T.IMP_IVA_INTERES_EXTRA  > 0 \n"+

   "       UNION ALL \n"+
      
   "       SELECT  A.ID_ACCESORIO AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
   "               ROUND(A.IMP_ACCESORIO * -1,2) AS IMP_NETO, \n"+
   "               DECODE(A.ID_ACCESORIO,6,'COMISVID',7,'COMISGM',8,'COMISDEU') AS CVE_CONCEPTO, \n"+ 
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, SIM_TABLA_AMORT_ACCESORIO A, SIM_CAT_ACCESORIO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA       = 'SIM' \n"+
   "           AND T.CVE_EMPRESA           = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO           = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
   "           AND T.CVE_GPO_EMPRESA       = A.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA           = A.CVE_EMPRESA \n"+
   "           AND T.ID_PRESTAMO           = A.ID_PRESTAMO \n"+
   "           AND T.NUM_PAGO_AMORTIZACION = A.NUM_PAGO_AMORTIZACION \n"+ 
   "           AND A.CVE_GPO_EMPRESA       = C.CVE_GPO_EMPRESA \n"+
   "           AND A.CVE_EMPRESA           = C.CVE_EMPRESA \n"+
   "           AND A.ID_ACCESORIO          = C.ID_ACCESORIO \n"+
   "           AND ROUND(A.IMP_ACCESORIO,2)> 0 \n"+
      
   "       UNION ALL \n"+ 
      
   "       SELECT  -5 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, P.ID_TIPO_RECARGO AS ID_ACCESORIO, \n"+ 
   "               ROUND(NVL(P.MONTO_FIJO_PERIODO * -1,0),2) AS IMP_NETO, 'PAGOTARD' AS CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+ 
   "           AND T.FECHA_AMORTIZACION < (SELECT  F_MEDIO \n"+  
   "                                       FROM    PFIN_PARAMETRO \n"+ 
   "                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
   "                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
   "           AND T.B_PAGO_PUNTUAL    = 'F' \n"+
   "           AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+ 
   "           AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
   "           AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
   "           AND P.ID_TIPO_RECARGO  IN (4,5) \n"+
   "           AND ROUND(NVL(P.MONTO_FIJO_PERIODO,0),2) > 0 \n"+ 
          
   "      UNION ALL \n"+ 
      
   "       SELECT  -4 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
   "               (T.IMP_INTERES_MORA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
   "       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
   "           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
   "           AND C.CVE_CONCEPTO    = 'INTMORA' \n"+
   "           AND T.IMP_INTERES_EXTRA  > 0 \n"+

   "       UNION ALL \n"+ 

   "       SELECT  -3 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
   "               (T.IMP_IVA_INTERES_MORA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
   "               'I' AS CVE_AFECTA \n"+
              
   "       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
   "       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
   "           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
   "           AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+ 
   "                                       FROM    PFIN_PARAMETRO \n"+  
   "                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
   "                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
   "                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
   "           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
   "           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
   "           AND C.CVE_CONCEPTO    = 'IVAINTMO' \n"+
   "           AND T.IMP_IVA_INTERES_EXTRA  > 0 \n"+

   "   UNION ALL \n"+ 

   "   SELECT  DECODE(D.CVE_CONCEPTO,  'CAPITA',-10, \n"+ 
   "                                   'INTERE',-9, \n"+
   "                                   'IVAINT',-8, \n"+
   "                                   'INTEXT',-7, \n"+
   "                                   'IVAINTEX',-6, \n"+
   "                                   'INTMORA',-4, \n"+
   "                                   'IVAINTMO',-3, \n"+
   "                                   'PAGOTARD',-5, \n"+
   "                                   'COMISDEU',8, \n"+
   "                                   'COMISGM',7, \n"+
   "                                   'COMISVID',6) AS ID_ORDEN, \n"+  
      
   "           M.CVE_GPO_EMPRESA, M.CVE_EMPRESA, M.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
   "           ROUND(IMP_CONCEPTO*DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),2) AS IMP_NETO, \n"+ 
   "           C.CVE_CONCEPTO, O.CVE_AFECTA_CREDITO AS CVE_AFECTA \n"+
   "   FROM    PFIN_MOVIMIENTO M, PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O, PFIN_CAT_CONCEPTO C \n"+ 
   "   WHERE   M.CVE_GPO_EMPRESA   = 'SIM' \n"+
   "       AND M.CVE_EMPRESA       = 'CREDICONFIA' \n"+ 
   "       AND M.ID_PRESTAMO       = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+ 
   "       AND M.SIT_MOVIMIENTO    <> 'CA' \n"+
   "       AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA \n"+ 
   "       AND M.CVE_EMPRESA       = D.CVE_EMPRESA \n"+
   "       AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO \n"+
   "       AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA \n"+
   "       AND M.CVE_EMPRESA       = O.CVE_EMPRESA \n"+
   "       AND M.CVE_OPERACION     = O.CVE_OPERACION \n"+
   "       AND D.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
   "       AND D.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
   "       AND D.CVE_CONCEPTO      = C.CVE_CONCEPTO  \n"+
   "   ) \n"+
      
   "       A, PFIN_CAT_CONCEPTO C \n"+ 
   "   WHERE   A.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+ 
   "       AND A.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
   "       AND A.CVE_CONCEPTO      = C.CVE_CONCEPTO \n"+
   "   GROUP BY A.ID_ORDEN, INITCAP('TOTAL DE '||C.DESC_LARGA) \n"+ 
   "   ORDER BY ID_ORDEN \n";
	
		System.out.println("resumen de individual***************"+sSql);
		
	}else if (parametros.getDefCampo("CONSULTA").equals("SALDO_TOTAL")){
		
		sSql =	 "SELECT \n"+	 
			        "TO_CHAR(SUM(IMPORTE),'999,999,999.99') IMPORTE_TOTAL, \n"+
			        "TO_CHAR(SUM(PAGADO),'999,999,999.99') PAGO_TOTAL, \n"+
			        "TO_CHAR(SUM (SALDO),'999,999,999.99') SALDO_TOTAL \n"+
			 "FROM ( \n"+
			 
			 "SELECT  ID_ORDEN, INITCAP('TOTAL DE '||C.DESC_LARGA) DESCRIPCION, \n"+ 
			"		                SUM(DECODE(A.CVE_AFECTA,'I',ABS(A.IMP_NETO),0)) AS IMPORTE, \n"+ 
			"		                SUM(DECODE(A.CVE_AFECTA,'I',0,ABS(A.IMP_NETO))) AS PAGADO, \n"+
			"		                (SUM(DECODE(A.CVE_AFECTA,'I',ABS(A.IMP_NETO),0)) - SUM(DECODE(A.CVE_AFECTA,'I',0,ABS(A.IMP_NETO)))) AS SALDO \n"+ 
			"FROM ( \n"+        
			  
			"SELECT  -10 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+  
			"               (T.IMP_CAPITAL_AMORT*-1) AS IMP_NETO, C.CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			"           AND C.CVE_CONCEPTO    = 'CAPITA' \n"+
			"           AND T.IMP_CAPITAL_AMORT > 0 \n"+
			      
			"       UNION ALL \n"+ 
			
			"       SELECT  -9 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+  
			"               (T.IMP_INTERES*-1) AS IMP_NETO, C.CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			"           AND C.CVE_CONCEPTO    = 'INTERE' \n"+
			"           AND T.IMP_INTERES > 0 \n"+
			
			"       UNION ALL \n"+ 
			
			"       SELECT  -8 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
			"               (T.IMP_IVA_INTERES*-1) AS IMP_NETO, C.CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+ 
			"           AND T.FECHA_AMORTIZACION <= (SELECT  F_MEDIO \n"+  
			"                                       FROM    PFIN_PARAMETRO \n"+  
			"                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
			"                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
			"                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
			"           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			"           AND C.CVE_CONCEPTO    = 'IVAINT' \n"+
			"           AND T.IMP_IVA_INTERES > 0 \n"+
			
			"       UNION ALL \n"+ 
			
			"       SELECT  -7 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
			"               (T.IMP_INTERES_EXTRA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			"           AND C.CVE_CONCEPTO    = 'INTEXT' \n"+
			"           AND T.IMP_INTERES_EXTRA  > 0 \n"+
			
			"       UNION ALL \n"+ 
			
			"       SELECT  -6 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
			"               (T.IMP_IVA_INTERES_EXTRA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"           AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+ 
			"                                       FROM    PFIN_PARAMETRO \n"+  
			"                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
			"                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
			"           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			"           AND C.CVE_CONCEPTO    = 'IVAINTEX' \n"+
			"           AND T.IMP_IVA_INTERES_EXTRA  > 0 \n"+
			
			"       UNION ALL \n"+
			
			"       SELECT  A.ID_ACCESORIO AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
			"               ROUND(A.IMP_ACCESORIO * -1,2) AS IMP_NETO, \n"+
			"               DECODE(A.ID_ACCESORIO,6,'COMISVID',7,'COMISGM',8,'COMISDEU') AS CVE_CONCEPTO, \n"+ 
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, SIM_TABLA_AMORT_ACCESORIO A, SIM_CAT_ACCESORIO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA       = 'SIM' \n"+
			"           AND T.CVE_EMPRESA           = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO           = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"           AND T.CVE_GPO_EMPRESA       = A.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA           = A.CVE_EMPRESA \n"+
			"           AND T.ID_PRESTAMO           = A.ID_PRESTAMO \n"+
			"           AND T.NUM_PAGO_AMORTIZACION = A.NUM_PAGO_AMORTIZACION \n"+ 
			"           AND A.CVE_GPO_EMPRESA       = C.CVE_GPO_EMPRESA \n"+
			"           AND A.CVE_EMPRESA           = C.CVE_EMPRESA \n"+
			"           AND A.ID_ACCESORIO          = C.ID_ACCESORIO \n"+
			"           AND ROUND(A.IMP_ACCESORIO,2)> 0 \n"+
			
			"       UNION ALL \n"+ 
			
			"       SELECT  -5 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, P.ID_TIPO_RECARGO AS ID_ACCESORIO, \n"+ 
			"               ROUND(NVL(P.MONTO_FIJO_PERIODO * -1,0),2) AS IMP_NETO, 'PAGOTARD' AS CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+ 
			"           AND T.FECHA_AMORTIZACION < (SELECT  F_MEDIO \n"+  
			"                                       FROM    PFIN_PARAMETRO \n"+ 
			"                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
			"                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
			"           AND T.B_PAGO_PUNTUAL    = 'F' \n"+
			"           AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+ 
			"           AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
			"           AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
			"           AND P.ID_TIPO_RECARGO  IN (4,5) \n"+
			"           AND ROUND(NVL(P.MONTO_FIJO_PERIODO,0),2) > 0 \n"+ 
			  
			"      UNION ALL \n"+ 
			
			"       SELECT  -4 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
			"               (T.IMP_INTERES_MORA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			"       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			"           AND C.CVE_CONCEPTO    = 'INTMORA' \n"+
			"           AND T.IMP_INTERES_EXTRA  > 0 \n"+
			
			"       UNION ALL \n"+ 
			
			"       SELECT  -3 AS ID_ORDEN, T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
			"               (T.IMP_IVA_INTERES_MORA*-1) AS IMP_DESGLOSE, C.CVE_CONCEPTO, \n"+
			"               'I' AS CVE_AFECTA \n"+
			      
			"       FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+ 
			"       WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
			"           AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"           AND T.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"           AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+ 
			"                                       FROM    PFIN_PARAMETRO \n"+  
			"                                       WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+ 
			"                                           AND CVE_EMPRESA     = 'CREDICONFIA' \n"+ 
			"                                           AND CVE_MEDIO       = 'SYSTEM') \n"+
			"           AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"           AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
			"           AND C.CVE_CONCEPTO    = 'IVAINTMO' \n"+
			"           AND T.IMP_IVA_INTERES_EXTRA  > 0 \n"+
			
			"   UNION ALL \n"+ 
			
			"   SELECT  DECODE(D.CVE_CONCEPTO,  'CAPITA',-10, \n"+ 
			"                                   'INTERE',-9, \n"+
			"                                   'IVAINT',-8, \n"+
			"                                   'INTEXT',-7, \n"+
			"                                   'IVAINTEX',-6, \n"+
			"                                   'INTMORA',-4, \n"+
			"                                   'IVAINTMO',-3, \n"+
			"                                   'PAGOTARD',-5, \n"+
			"                                   'COMISDEU',8, \n"+
			"                                   'COMISGM',7, \n"+
			"                                   'COMISVID',6) AS ID_ORDEN, \n"+  
			
			"           M.CVE_GPO_EMPRESA, M.CVE_EMPRESA, M.ID_PRESTAMO, C.ID_ACCESORIO, \n"+ 
			"           ROUND(IMP_CONCEPTO*DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),2) AS IMP_NETO, \n"+ 
			"           C.CVE_CONCEPTO, O.CVE_AFECTA_CREDITO AS CVE_AFECTA \n"+
			"   FROM    PFIN_MOVIMIENTO M, PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O, PFIN_CAT_CONCEPTO C \n"+ 
			"   WHERE   M.CVE_GPO_EMPRESA   = 'SIM' \n"+
			"       AND M.CVE_EMPRESA       = 'CREDICONFIA' \n"+ 
			"       AND M.ID_PRESTAMO       = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+ 
			"       AND M.SIT_MOVIMIENTO    <> 'CA' \n"+
			"       AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA \n"+ 
			"       AND M.CVE_EMPRESA       = D.CVE_EMPRESA \n"+
			"       AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO \n"+
			"       AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA \n"+
			"       AND M.CVE_EMPRESA       = O.CVE_EMPRESA \n"+
			"       AND M.CVE_OPERACION     = O.CVE_OPERACION \n"+
			"       AND D.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
			"       AND D.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
			"       AND D.CVE_CONCEPTO      = C.CVE_CONCEPTO  \n"+
			"   ) \n"+
			
			"       A, PFIN_CAT_CONCEPTO C \n"+ 
			"   WHERE   A.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+ 
			"       AND A.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
			"       AND A.CVE_CONCEPTO      = C.CVE_CONCEPTO \n"+
			"   GROUP BY A.ID_ORDEN, INITCAP('TOTAL DE '||C.DESC_LARGA) \n"+ 
			"   ORDER BY ID_ORDEN) \n";
			
					
		System.out.println("saldo total del resumen de individual"+sSql);
		
	}
		 	
		ejecutaSql();
		return getConsultaLista();
	}
}