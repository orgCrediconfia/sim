/**
 * Sistema de administraci�n de portales.
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
 * Administra los accesos a la base de datos para la consulta de los saldos de un pr�stamo para la pantalla de movimientos extraordinarios.
 */
 
public class SimPrestamoMovimientoExtraordinarioSaldosDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base al filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
			int iAccesorio = 0;
			int iPago = 0;
			int iTabla = 0;
			int iRelacion = 0;
			int iAccesorioTotal = 0;
			int iAccesorioSaldoTotal = 0;
			int iPagoTotal = 0;
			int iTablaTotal = 0;
			int iRelacionTotal = 0;
			String sVista = "";
			
			
			if (parametros.getDefCampo("CONSULTA").equals("SALDOS")){
			
				if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
				
					sVista = " SELECT \n"+
							" 	P.CVE_GPO_EMPRESA, \n"+
							" 	P.CVE_EMPRESA, \n"+
							" 	P.ID_PRESTAMO, \n"+
							"	P.ID_CLIENTE ID_PERSONA, \n"+
							"	PE.NOM_COMPLETO, \n"+
							"TO_CHAR(M.MONTO_AUTORIZADO,'999,999,999.9999') MONTO_AUTORIZADO, \n"+
							"TO_CHAR(SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0)) + SUM(NVL(T.IMP_EXT_CAPITAL_AMORT,0)),'999,999,999.9999') CAPITAL_SALDO, \n"+
							"TO_CHAR(SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - (SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_INTERES,0)) + SUM(NVL(T.IMP_EXT_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_INTERES_MORA,0)),'999,999,999.9999') INTERES_SALDO, \n"+
							"TO_CHAR(SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - (SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_IVA_INTERES,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_MORA,0)),'999,999,999.9999') IVA_SALDO, \n"+
							"TO_CHAR(SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_EXT_PAGO_TARDIO,0)),'999,999,999.9999') RECARGOS_SALDO, \n";
							
					//Obtiene todos los accesorio para mostrarlos en la tabla de amortizaci�n.
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				    	   "ORDER BY ID_ACCESORIO \n";
					ejecutaSql();
					while (rs.next()){
						
						sVista = sVista + "TO_CHAR(SUM(NVL(A" + iAccesorio + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iAccesorio + ".IMP_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_IVA_ACCESORIO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_IVA_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_EXT_ACCESORIO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_EXT_IVA_ACCESORIO,0)),'999,999,999.9999') ACCESORIO_" + iAccesorio + "_SALDO, \n";
						
						iAccesorio++;
					}
					
					sVista = sVista + "TO_CHAR( \n";
					
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				    	   "ORDER BY ID_ACCESORIO \n";
					ejecutaSql();
					while (rs.next()){
						
						sVista = sVista + "SUM(NVL(A" + iPago + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iPago + ".IMP_ACCESORIO_PAGADO,0)) + \n";
						iPago++;
					}
					
					
					sVista = sVista + "SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0)) + SUM(NVL(T.IMP_EXT_CAPITAL_AMORT,0)) + SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - (SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_INTERES,0)) + SUM(NVL(T.IMP_EXT_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_INTERES_MORA,0)) + SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - (SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_IVA_INTERES,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_MORA,0)) + SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_EXT_PAGO_TARDIO,0)),'999,999,999.9999') TOTAL \n";
					
					sVista = sVista + "FROM SIM_PRESTAMO P, \n"+
							"     RS_GRAL_PERSONA PE, \n"+
							"     SIM_CLIENTE_MONTO M, \n";
					
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				    	   "ORDER BY ID_ACCESORIO \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + " SIM_TABLA_AMORT_ACCESORIO A" + iTabla + ",  \n";
						iTabla++;
					}
					
					
					sVista = sVista + "SIM_TABLA_AMORTIZACION T \n"+
							"WHERE  P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						    "AND    P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						    "AND    P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
						    "AND    PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						    "AND    PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						    "AND    PE.ID_PERSONA = P.ID_CLIENTE \n"+
						    "AND    M.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						    "AND    M.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						    "AND    M.ID_PRESTAMO = P.ID_PRESTAMO \n"+
						    "AND    M.ID_CLIENTE = P.ID_CLIENTE \n"+
						    "AND    T.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						    "AND    T.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						    "AND    T.ID_PRESTAMO = P.ID_PRESTAMO \n";
					
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
					
					sVista = sVista +  "GROUP BY P.CVE_GPO_EMPRESA, \n"+
						    "P.CVE_EMPRESA, \n"+
						    "P.ID_PRESTAMO, \n"+
						    "P.ID_CLIENTE, \n"+
						    "PE.NOM_COMPLETO, \n"+
						    "M.MONTO_AUTORIZADO \n"+
							"ORDER BY NOM_COMPLETO \n";
					
				}else if (parametros.getDefCampo("APLICA_A").equals("GRUPO")){
					
					
					sVista = " SELECT \n"+
							 	"PG.CVE_GPO_EMPRESA, \n"+
							 	"PG.CVE_EMPRESA, \n"+
							 	"PG.ID_PRESTAMO_GRUPO, \n"+ 
							 	"PG.ID_PRESTAMO, \n"+
								"PG.ID_INTEGRANTE ID_PERSONA, \n"+ 
								"PE.NOM_COMPLETO, \n"+
								"TO_CHAR(PG.MONTO_AUTORIZADO,'999,999,999.9999') MONTO_AUTORIZADO, \n"+
								"TO_CHAR(SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0)) + SUM(NVL(T.IMP_EXT_CAPITAL_AMORT,0)),'999,999,999.9999') CAPITAL_SALDO, \n"+
								"TO_CHAR(SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - (SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_INTERES,0)) + SUM(NVL(T.IMP_EXT_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_INTERES_MORA,0)),'999,999,999.9999') INTERES_SALDO, \n"+
								"TO_CHAR(SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - (SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_IVA_INTERES,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_MORA,0)) ,'999,999,999.9999') IVA_SALDO, \n"+
								"TO_CHAR(SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_EXT_PAGO_TARDIO,0)),'999,999,999.9999') RECARGOS_SALDO, \n";
								
					//Obtiene todos los accesorio para mostrarlos en la tabla de amortizaci�n.
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
				           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
						   "AND    ID_FORMA_APLICACION != '1' \n" +
						   "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + "TO_CHAR(SUM(NVL(A" + iAccesorio + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iAccesorio + ".IMP_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_IVA_ACCESORIO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_IVA_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_EXT_ACCESORIO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_EXT_IVA_ACCESORIO,0)),'999,999,999.9999') ACCESORIO_" + iAccesorio + "_SALDO, \n";
						iAccesorio++;
					}
					
					sVista = sVista + "TO_CHAR( \n";
					
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
				           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					       "AND    ID_FORMA_APLICACION != '1' \n" +
					       "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					
					while (rs.next()){
						sVista = sVista + "SUM(NVL(A" + iPago + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iPago + ".IMP_ACCESORIO_PAGADO,0)) + \n";
						iPago++;
					}
					
					sVista = sVista + "SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0)) + SUM(NVL(T.IMP_EXT_CAPITAL_AMORT,0)) + SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - (SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_INTERES,0)) + SUM(NVL(T.IMP_EXT_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_INTERES_MORA,0)) + SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - (SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_IVA_INTERES,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_MORA,0)) + SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_EXT_PAGO_TARDIO,0)),'999,999,999.9999') TOTAL \n";
					
					sVista = sVista + "FROM SIM_PRESTAMO_GPO_DET PG, \n"+
				    				  "		RS_GRAL_PERSONA PE, \n";
					
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
				           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					       "AND    ID_FORMA_APLICACION != '1' \n" +
						   "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + " SIM_TABLA_AMORT_ACCESORIO A" + iTabla + ",  \n";
						iTabla++;
					}
					
					
					
					sVista = sVista + "SIM_TABLA_AMORTIZACION T \n"+
										"WHERE  PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									    "AND    PG.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
									    "AND    PG.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
									    "AND    PE.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
									    "AND    PE.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
									    "AND    PE.ID_PERSONA = PG.ID_INTEGRANTE \n"+
									    "AND    T.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
									    "AND    T.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
									    "AND    T.ID_PRESTAMO = PG.ID_PRESTAMO \n";
									    
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
					       "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					       "AND    ID_FORMA_APLICACION != '1' \n" +
					       "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					while (rs.next()){
							sVista = sVista + "AND	A" + iRelacion + ".CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
											  "AND	A" + iRelacion + ".CVE_EMPRESA = T.CVE_EMPRESA \n"+
											  "AND	A" + iRelacion + ".ID_PRESTAMO = T.ID_PRESTAMO \n"+
											  "AND	A" + iRelacion + ".ID_ACCESORIO = '" + rs.getString("ID_ACCESORIO") + "' \n"+
											  "AND  A" + iRelacion + ".NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION   \n";
							iRelacion++;
					}
									    
					sVista = sVista + "GROUP BY PG.CVE_GPO_EMPRESA, \n"+
									  "PG.CVE_EMPRESA, \n"+
									  "PG.ID_PRESTAMO_GRUPO, \n"+ 
									  "PG.ID_PRESTAMO, \n"+
									  "PG.ID_INTEGRANTE, \n"+
									  "PE.NOM_COMPLETO, \n"+
									  "PG.MONTO_AUTORIZADO \n"+
									  "ORDER BY NOM_COMPLETO \n";
				}
			
			}else if (parametros.getDefCampo("CONSULTA").equals("SALDOS_TOTALES")){
				if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
					
					sVista = " SELECT \n"+
							"TO_CHAR(SUM(MONTO_AUTORIZADO),'999,999,999.9999') MONTO_AUTORIZADO_TOTAL, \n"+
							"TO_CHAR(SUM(CAPITAL_SALDO),'999,999,999.9999') CAPITAL_SALDO_TOTAL, \n"+
							"TO_CHAR(SUM(INTERES_SALDO),'999,999,999.9999') INTERES_SALDO_TOTAL, \n"+
							"TO_CHAR(SUM(IVA_SALDO),'999,999,999.9999') IVA_SALDO_TOTAL, \n"+
							"TO_CHAR(SUM(RECARGOS_SALDO),'999,999,999.9999') RECARGOS_SALDO_TOTAL, \n";
							
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				    	   "ORDER BY ID_ACCESORIO \n";
					
					ejecutaSql();
					while (rs.next()){
						
						sVista = sVista + "TO_CHAR(SUM(NVL(ACCESORIO_" + iAccesorioSaldoTotal + "_SALDO,0)),'999,999,999.9999') ACCESORIO_" + iAccesorioSaldoTotal + "_SALDO_TOTAL, \n";
						
						iAccesorioSaldoTotal++;
					}
		
					
					sVista = sVista + "TO_CHAR(SUM(NVL(TOTAL,0)),'999,999,999.9999') SALDOS_TOTALES \n"+
							"FROM ( \n"+
							" SELECT \n"+
							" 	P.CVE_GPO_EMPRESA, \n"+
							" 	P.CVE_EMPRESA, \n"+
							" 	P.ID_PRESTAMO, \n"+
							"	P.ID_CLIENTE ID_PERSONA, \n"+
							"	PE.NOM_COMPLETO, \n"+
							"M.MONTO_AUTORIZADO, \n"+
							"SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0)) + SUM(NVL(T.IMP_EXT_CAPITAL_AMORT,0)) CAPITAL_SALDO, \n"+
							"SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - (SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_INTERES,0)) + SUM(NVL(T.IMP_EXT_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_INTERES_MORA,0)) INTERES_SALDO, \n"+
							"SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - (SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_IVA_INTERES,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_MORA,0)) IVA_SALDO, \n"+
							"SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_EXT_PAGO_TARDIO,0)) RECARGOS_SALDO, \n";
							
					//Obtiene todos los accesorio para mostrarlos en la tabla de amortizaci�n.
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				    	   "ORDER BY ID_ACCESORIO \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + "SUM(NVL(A" + iAccesorioTotal + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iAccesorioTotal + ".IMP_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorioTotal + ".IMP_IVA_ACCESORIO,0)) + SUM(NVL(A" + iAccesorioTotal + ".IMP_IVA_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorioTotal + ".IMP_EXT_ACCESORIO,0)) + SUM(NVL(A" + iAccesorioTotal + ".IMP_EXT_IVA_ACCESORIO,0)) ACCESORIO_" + iAccesorioTotal + "_SALDO, \n";
						
						iAccesorioTotal++;
					}
					
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				    	   "ORDER BY ID_ACCESORIO \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + "SUM(NVL(A" + iPagoTotal + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iPagoTotal + ".IMP_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iPagoTotal + ".IMP_IVA_ACCESORIO,0)) + SUM(NVL(A" + iPagoTotal + ".IMP_IVA_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iPagoTotal + ".IMP_EXT_ACCESORIO,0)) + SUM(NVL(A" + iPagoTotal + ".IMP_EXT_IVA_ACCESORIO,0)) + \n";
						
						iPagoTotal++;
					}
					
					
					sVista = sVista + "SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0))  + SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) TOTAL \n";
					
					sVista = sVista + "FROM SIM_PRESTAMO P, \n"+
							"     RS_GRAL_PERSONA PE, \n"+
							"     SIM_CLIENTE_MONTO M, \n";
					
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				    	   "ORDER BY ID_ACCESORIO \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + " SIM_TABLA_AMORT_ACCESORIO A" + iTablaTotal + ",  \n";
						iTablaTotal++;
					}
					
					
					sVista = sVista + "SIM_TABLA_AMORTIZACION T \n"+
							"WHERE  P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						    "AND    P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						    "AND    P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
						    "AND    PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						    "AND    PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						    "AND    PE.ID_PERSONA = P.ID_CLIENTE \n"+
						    "AND    M.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						    "AND    M.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						    "AND    M.ID_PRESTAMO = P.ID_PRESTAMO \n"+
						    "AND    M.ID_CLIENTE = P.ID_CLIENTE \n"+
						    "AND    T.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						    "AND    T.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						    "AND    T.ID_PRESTAMO = P.ID_PRESTAMO \n";
					
					sSql = "SELECT DISTINCT ID_ACCESORIO \n" +
				 		       "FROM SIM_TABLA_AMORT_ACCESORIO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					       "ORDER BY ID_ACCESORIO \n";
					ejecutaSql();
					while (rs.next()){
							sVista = sVista + "AND	A" + iRelacionTotal + ".CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
											  "AND	A" + iRelacionTotal + ".CVE_EMPRESA = T.CVE_EMPRESA \n"+
											  "AND	A" + iRelacionTotal + ".ID_PRESTAMO = T.ID_PRESTAMO \n"+
											  "AND	A" + iRelacionTotal + ".ID_ACCESORIO = '" + rs.getString("ID_ACCESORIO") + "' \n"+
											  "AND  A" + iRelacionTotal + ".NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION   \n";
							iRelacionTotal++;
					}
					
					sVista = sVista +  "GROUP BY P.CVE_GPO_EMPRESA, \n"+
						    "P.CVE_EMPRESA, \n"+
						    "P.ID_PRESTAMO, \n"+
						    "P.ID_CLIENTE, \n"+
						    "PE.NOM_COMPLETO, \n"+
						    "M.MONTO_AUTORIZADO) \n";
					
				}else if (parametros.getDefCampo("APLICA_A").equals("GRUPO")){
					
					sVista = " SELECT \n"+
						
							"TO_CHAR(SUM(MONTO_AUTORIZADO),'999,999,999.9999') MONTO_AUTORIZADO_TOTAL, \n"+
							"TO_CHAR(SUM(CAPITAL_SALDO),'999,999,999.9999') CAPITAL_SALDO_TOTAL, \n"+
							"TO_CHAR(SUM(INTERES_SALDO),'999,999,999.9999') INTERES_SALDO_TOTAL, \n"+
							"TO_CHAR(SUM(IVA_SALDO),'999,999,999.9999') IVA_SALDO_TOTAL, \n"+
							"TO_CHAR(SUM(RECARGOS_SALDO),'999,999,999.9999') RECARGOS_SALDO_TOTAL, \n";
				
					//Obtiene todos los accesorio para mostrarlos en la tabla de amortizaci�n.
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
				           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
						   "AND    ID_FORMA_APLICACION != '1' \n" +
						   "ORDER BY ID_CARGO_COMISION \n";
					
					ejecutaSql();
					while (rs.next()){
						
						sVista = sVista + "TO_CHAR(SUM(NVL(ACCESORIO_" + iAccesorioSaldoTotal + "_SALDO,0)),'999,999,999.9999') ACCESORIO_" + iAccesorioSaldoTotal + "_SALDO_TOTAL, \n";
						iAccesorioSaldoTotal++;
					}
		
					sVista = sVista + "TO_CHAR(SUM(NVL(TOTAL,0)),'999,999,999.9999') SALDOS_TOTALES \n"+	
							"FROM ( \n"+
								" SELECT \n"+
							 	"PG.CVE_GPO_EMPRESA, \n"+
							 	"PG.CVE_EMPRESA, \n"+
							 	"PG.ID_PRESTAMO_GRUPO, \n"+ 
							 	"PG.ID_PRESTAMO, \n"+
								"PG.ID_INTEGRANTE ID_PERSONA, \n"+ 
								"PE.NOM_COMPLETO, \n"+
								"PG.MONTO_AUTORIZADO, \n"+
								"SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0)) + SUM(NVL(T.IMP_EXT_CAPITAL_AMORT,0)) CAPITAL_SALDO, \n"+
								"SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - (SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_INTERES,0)) + SUM(NVL(T.IMP_EXT_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_INTERES_MORA,0)) INTERES_SALDO, \n"+
								"SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - (SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0))) + SUM(NVL(T.IMP_EXT_IVA_INTERES,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_EXT_IVA_INTERES_MORA,0)) IVA_SALDO, \n"+
								"SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_EXT_PAGO_TARDIO,0)) RECARGOS_SALDO, \n";
								
					//Obtiene todos los accesorio para mostrarlos en la tabla de amortizaci�n.
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
				           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
						   "AND    ID_FORMA_APLICACION != '1' \n" +
						   "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + "SUM(NVL(A" + iAccesorio + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iAccesorio + ".IMP_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_IVA_ACCESORIO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_IVA_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_EXT_ACCESORIO,0)) + SUM(NVL(A" + iAccesorio + ".IMP_EXT_IVA_ACCESORIO,0)) ACCESORIO_" + iAccesorio + "_SALDO, \n";
						
						iAccesorio++;
					}
					
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
				           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					       "AND    ID_FORMA_APLICACION != '1' \n" +
					       "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					
					while (rs.next()){
						
						sVista = sVista + "SUM(NVL(A" + iPago + ".IMP_ACCESORIO,0)) - SUM(NVL(A" + iPago + ".IMP_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iPago + ".IMP_IVA_ACCESORIO,0)) + SUM(NVL(A" + iPago + ".IMP_IVA_ACCESORIO_PAGADO,0)) + SUM(NVL(A" + iPago + ".IMP_EXT_ACCESORIO,0)) + SUM(NVL(A" + iPago + ".IMP_EXT_IVA_ACCESORIO,0)) + \n";
					
						iPago++;
					}
					
					sVista = sVista + "SUM(NVL(T.IMP_CAPITAL_AMORT,0)) - SUM(NVL(T.IMP_CAPITAL_AMORT_PAGADO,0))  + SUM(NVL(T.IMP_INTERES,0)) + SUM(NVL(T.IMP_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_INTERES_MORA,0)) - SUM(NVL(T.IMP_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA,0)) - SUM(NVL(T.IMP_IVA_INTERES_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_EXTRA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_PAGO_TARDIO,0)) - SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) TOTAL \n";
					
					sVista = sVista + "FROM SIM_PRESTAMO_GPO_DET PG, \n"+
				    				  "		RS_GRAL_PERSONA PE, \n";
					
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
				           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					       "AND    ID_FORMA_APLICACION != '1' \n" +
						   "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					while (rs.next()){
						sVista = sVista + " SIM_TABLA_AMORT_ACCESORIO A" + iTabla + ",  \n";
						iTabla++;
					}
					
					
					
					sVista = sVista + "SIM_TABLA_AMORTIZACION T \n"+
										"WHERE  PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									    "AND    PG.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
									    "AND    PG.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
									    "AND    PE.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
									    "AND    PE.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
									    "AND    PE.ID_PERSONA = PG.ID_INTEGRANTE \n"+
									    "AND    T.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
									    "AND    T.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
									    "AND    T.ID_PRESTAMO = PG.ID_PRESTAMO \n";
									    
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
					       "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					       "AND    ID_FORMA_APLICACION != '1' \n" +
					       "ORDER BY ID_CARGO_COMISION \n";
					ejecutaSql();
					while (rs.next()){
							sVista = sVista + "AND	A" + iRelacion + ".CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
											  "AND	A" + iRelacion + ".CVE_EMPRESA = T.CVE_EMPRESA \n"+
											  "AND	A" + iRelacion + ".ID_PRESTAMO = T.ID_PRESTAMO \n"+
											  "AND	A" + iRelacion + ".ID_ACCESORIO = '" + rs.getString("ID_ACCESORIO") + "' \n"+
											  "AND  A" + iRelacion + ".NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION   \n";
							iRelacion++;
					}
									    
					sVista = sVista + "GROUP BY PG.CVE_GPO_EMPRESA, \n"+
									  "PG.CVE_EMPRESA, \n"+
									  "PG.ID_PRESTAMO_GRUPO, \n"+ 
									  "PG.ID_PRESTAMO, \n"+
									  "PG.ID_INTEGRANTE, \n"+
									  "PE.NOM_COMPLETO, \n"+
									  "PG.MONTO_AUTORIZADO) \n";
				
				}
				
			}
			
			sSql = sVista;
			
			System.out.println("saldos"+sSql);
			
		ejecutaSql();
		return getConsultaLista();
	}
}