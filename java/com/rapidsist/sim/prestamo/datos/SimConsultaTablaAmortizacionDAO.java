/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para la consulta de la tabla de amortización.
 */
 
public class SimConsultaTablaAmortizacionDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		if (parametros.getDefCampo("CONSULTA").equals("PRESTAMOS")) {
			
			if (parametros.getDefCampo("CVE_PRESTAMO").equals("")) {
				if (parametros.getDefCampo("NOMBRE").equals("")) {
				
				sSql = "SELECT \n"+
					   "C.CVE_PRESTAMO, \n"+
					   "C.ID_PRODUCTO, \n"+
					   "C.NOM_PRODUCTO, \n"+
					   "C.NOMBRE, \n"+
					   "C.NUM_CICLO \n"+
					   "FROM V_CREDITO C, \n"+
					   "SIM_CAT_ETAPA_PRESTAMO E, \n"+
					   "SIM_USUARIO_ACCESO_SUCURSAL US \n"+
					   "WHERE  C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       	   "AND C.APLICA_A != 'INDIVIDUAL_GRUPO' \n"+
					   "AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
					   "AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+
					   "AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+
					   "AND E.B_CANCELADO = 'F' \n"+
					   "AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			           "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
			           "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
			           "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
					   "ORDER BY CVE_PRESTAMO \n";
					
				}else if (!parametros.getDefCampo("NOMBRE").equals("")) {
					sSql = "SELECT \n"+
					   "C.CVE_PRESTAMO, \n"+
					   "C.ID_PRODUCTO, \n"+
					   "C.NOM_PRODUCTO, \n"+
					   "C.NOMBRE, \n"+
					   "C.NUM_CICLO \n"+
					   "FROM V_CREDITO C, \n"+
					   "SIM_CAT_ETAPA_PRESTAMO E, \n"+
					   "SIM_USUARIO_ACCESO_SUCURSAL US \n"+
					   "WHERE  C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					   "AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
					   "AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+
					   "AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+
					   "AND E.B_CANCELADO = 'F' \n"+
					   "AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			           "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
			           "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
			           "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
					  
					if (!parametros.getDefCampo("NOMBRE").equals("")) {
						sSql = sSql + " AND UPPER(C.NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase()  + "%' \n";
					}
					
					sSql = sSql + "ORDER BY CVE_PRESTAMO \n";
				}
			}else  {
				if (!parametros.getDefCampo("NOMBRE").equals("")) {
				
					sSql = "SELECT \n"+
					   "C.CVE_PRESTAMO, \n"+
					   "C.ID_PRODUCTO, \n"+
					   "C.NOM_PRODUCTO, \n"+
					   "C.NOMBRE, \n"+
					   "C.NUM_CICLO \n"+
					   "FROM V_CREDITO C, \n"+
					   "SIM_CAT_ETAPA_PRESTAMO E, \n"+
					   "SIM_USUARIO_ACCESO_SUCURSAL US \n"+
					   "WHERE  C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					   "AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
					   "AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+
					   "AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+
					   "AND E.B_CANCELADO = 'F' \n"+
					   "AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			           "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
			           "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
			           "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
				
				if (!parametros.getDefCampo("CVE_PRESTAMO").equals("")) {
					sSql = sSql + " AND C.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
				}
				
				if (!parametros.getDefCampo("NOMBRE").equals("")) {
					sSql = sSql + " AND UPPER(C.NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase()  + "%' \n";
				}
				
				sSql = sSql + "ORDER BY CVE_PRESTAMO \n";
			}else if(parametros.getDefCampo("NOMBRE").equals("")) {
				
				sSql = "SELECT \n"+
				   "C.CVE_PRESTAMO, \n"+
				   "C.ID_PRODUCTO, \n"+
				   "C.NOM_PRODUCTO, \n"+
				   "C.NOMBRE, \n"+
				   "C.NUM_CICLO \n"+
				   "FROM V_CREDITO C, \n"+
				   "SIM_CAT_ETAPA_PRESTAMO E, \n"+
				   "SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				   "WHERE  C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			       "AND    C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				   "AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				   "AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				   "AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+
				   "AND E.B_CANCELADO = 'F' \n"+
				   "AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
		           "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
		           "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
		           "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
			
				if (!parametros.getDefCampo("CVE_PRESTAMO").equals("")) {
					sSql = sSql + " AND C.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
				}
			
				sSql = sSql + "ORDER BY CVE_PRESTAMO \n";
					
				}
			}
		}
		if (parametros.getDefCampo("CONSULTA").equals("TABLA")) {
			int iAccesorio = 0;
			int iPago = 0;
			int iTabla = 0;
			int iRelacion = 0;
			String sVista = "";
			
				sVista = " SELECT \n"+
				" 	T.CVE_GPO_EMPRESA, \n"+
				" 	T.CVE_EMPRESA, \n"+
				" 	T.ID_PRESTAMO, \n"+
				" 	T.NUM_PAGO_AMORTIZACION, \n"+
				" 	T.FECHA_AMORTIZACION, \n"+
				"	TO_CHAR(T.IMP_SALDO_INICIAL,'999,999,999.9999') IMP_SALDO_INICIAL, \n"+
				"	TO_CHAR(T.TASA_INTERES,'999,999,999.9999') TASA_INTERES, \n"+
				"	TO_CHAR(NVL(T.IMP_INTERES,0) + NVL(T.IMP_IVA_INTERES,0) + NVL(T.IMP_INTERES_EXTRA,0) + NVL(T.IMP_IVA_INTERES_EXTRA,0),'999,999,999.9999') INTERES, \n"+
				" 	TO_CHAR(T.IMP_CAPITAL_AMORT,'999,999,999.9999') IMP_CAPITAL_AMORT, \n"+
				"	TO_CHAR(T.IMP_PAGO,'999,999,999.9999') IMP_PAGO, \n";
				
				//Obtiene todos los accesorio para mostrarlos en la tabla de amortización.
				sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
	            		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
				       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
		           	   "ORDER BY ID_ACCESORIO \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + " TO_CHAR(A" + iAccesorio + ".IMP_ACCESORIO,'999,999,999.9999') ACCESORIO_" + iAccesorio + ",  \n";
					iAccesorio++;
				}
				
				sVista = sVista + " ( \n";
				
				sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
	            		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
				       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				       "ORDER BY ID_ACCESORIO \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + "A" + iPago + ".IMP_ACCESORIO + \n";
					iPago++;
				}
				
				sVista = sVista + " NVL(T.IMP_CAPITAL_AMORT,0) + NVL(T.IMP_INTERES,0) + \n"+
									"NVL(T.IMP_IVA_INTERES,0) + NVL(T.IMP_INTERES_EXTRA,0) + \n"+
									"NVL(T.IMP_IVA_INTERES_EXTRA,0)) PAGO_TOTAL, \n";
				
				
				
				sVista = sVista + " 	TO_CHAR(T.IMP_SALDO_FINAL,'999,999,999.9999') IMP_SALDO_FINAL \n"+
						  "FROM 	\n";	
				
				sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
	            		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
				       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				       "ORDER BY ID_ACCESORIO \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + " SIM_TABLA_AMORT_ACCESORIO A" + iTabla + ",  \n";
					iTabla++;
				}
				
				sVista = sVista + " SIM_TABLA_AMORTIZACION T \n"+
				
						  " WHERE  1=1 \n"+
						  " AND    T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						  " AND    T.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						  " AND    T.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
				
				sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
	            		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
				       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				       "ORDER BY ID_ACCESORIO \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + "AND	A" + iRelacion + ".CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
							  "AND	A" + iRelacion + ".CVE_EMPRESA = T.CVE_EMPRESA \n"+
							  "AND	A" + iRelacion + ".ID_PRESTAMO = T.ID_PRESTAMO \n"+
							  "AND	A" + iRelacion + ".ID_ACCESORIO = '" + rs.getString("ID_ACCESORIO") + "' \n"+
							  "AND  A" + iRelacion + ".NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION   \n";
					iRelacion++;
				}
				
				sVista = sVista + " ORDER BY NUM_PAGO_AMORTIZACION \n";
				
				sSql = sVista;
		}
		ejecutaSql();
		return getConsultaLista();
	}
}