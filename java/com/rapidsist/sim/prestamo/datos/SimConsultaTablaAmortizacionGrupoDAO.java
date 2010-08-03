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
 
public class SimConsultaTablaAmortizacionGrupoDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		String sVista = "";
		String sNumPrestamo = "";
		int iNumPrestamo = 0;
		int iCont = 0;
		int iAccesorio = 0;
		int iAccesorioCU1= 0;
		int iAccesorioCU2= 0;
		int iPago = 0;
		int iTabla = 0;
		int iRelacion = 0;
		
		sVista =  "SELECT \n"+
				  "CVE_GPO_EMPRESA, \n"+
				  "CVE_EMPRESA, \n"+
				  "NUM_PAGO_AMORTIZACION, \n"+
				  "FECHA_AMORTIZACION, \n"+
				  "TO_CHAR(TASA_INTERES,'999,999,999.9999') TASA_INTERES, \n";
				 
		//Obtiene todos los accesorio para mostrarlos en la tabla de amortización.
		sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
	           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
		       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
		       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
			   "AND    ID_FORMA_APLICACION != '1' \n" +
			   "ORDER BY ID_CARGO_COMISION \n";
		PreparedStatement ps1 = this.conn.prepareStatement(sSql);
		ps1.execute();
		ResultSet rs1 = ps1.getResultSet();
		while (rs1.next()){
			sVista = sVista + " TO_CHAR(SUM(IMP_ACCESORIO_" + iAccesorio + "),'999,999,999.9999') ACCESORIO_" + iAccesorio + ",  \n";
			iAccesorio++;
		}		
				
		sVista = sVista + "TO_CHAR(SUM(PAGO_TOTAL),'999,999,999.9999') PAGO_TOTAL, \n"+
						  "TO_CHAR(SUM(IMP_SALDO_INICIAL),'999,999,999.9999') IMP_SALDO_INICIAL, \n"+
						  "TO_CHAR(SUM(NVL(IMP_INTERES,0))+ SUM(NVL(IMP_IVA_INTERES,0)) + SUM(NVL(IMP_INTERES_EXTRA,0))+ SUM(NVL(IMP_IVA_INTERES_EXTRA,0)),'999,999,999.9999') INTERES, \n"+
						  "TO_CHAR(SUM(IMP_CAPITAL_AMORT),'999,999,999.9999') IMP_CAPITAL_AMORT, \n"+
						  "TO_CHAR(SUM(IMP_PAGO),'999,999,999.9999') IMP_PAGO, \n"+
						  "TO_CHAR(SUM(IMP_SALDO_FINAL),'999,999,999.9999') IMP_SALDO_FINAL \n"+
						  //"TO_CHAR(SUM(IMP_IVA_INTERES),'999,999,999.9999') IMP_IVA_INTERES, \n"+
						  //"TO_CHAR(SUM(IMP_IVA_INTERES_EXTRA),'999,999,999.9999') IMP_IVA_INTERES_EXTRA \n"+
						  
						  "FROM \n"+
						  "( \n";
		
		sSql =  "SELECT \n"+
				"COUNT (P.ID_PRESTAMO) NUM_PRESTAMO \n"+
				"FROM SIM_PRESTAMO_GPO_DET P \n"+
				"WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			    "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			    "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
		PreparedStatement ps2 = this.conn.prepareStatement(sSql);
		ps2.execute();
		ResultSet rs2 = ps2.getResultSet();
		if (rs2.next()){
			sNumPrestamo = rs2.getString("NUM_PRESTAMO");
			iNumPrestamo = (Integer.parseInt(sNumPrestamo));
		}
				
		sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO, \n"+
				"P.ID_PRESTAMO_GRUPO \n"+
				"FROM SIM_PRESTAMO_GPO_DET P \n"+
				"WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			    "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			    "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
		PreparedStatement ps3 = this.conn.prepareStatement(sSql);
		ps3.execute();
		ResultSet rs3 = ps3.getResultSet();
		while (rs3.next()){
			parametros.addDefCampo("ID_PRESTAMO",rs3.getString("ID_PRESTAMO"));
			
			if (iCont == 0){
			
				sVista = sVista + "SELECT \n"+
									"T.CVE_GPO_EMPRESA, \n"+
									"T.CVE_EMPRESA, \n"+
									"T.NUM_PAGO_AMORTIZACION, \n"+
									"T.FECHA_AMORTIZACION, \n"+
									"T.TASA_INTERES, \n";
				
				sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
			           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
				       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
				       "AND    ID_FORMA_APLICACION != '1' \n" +
				       "ORDER BY ID_CARGO_COMISION \n";
				PreparedStatement ps4 = this.conn.prepareStatement(sSql);
				ps4.execute();
				ResultSet rs4 = ps4.getResultSet();
				while (rs4.next()){
					sVista = sVista + " A" + iAccesorioCU1 + ".IMP_ACCESORIO IMP_ACCESORIO_" + iAccesorioCU1 + ",  \n";
					iAccesorioCU1++;
				}
				
				
				
				sVista = sVista + " ( \n";
				
				sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
			           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
				       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
				       "AND    ID_FORMA_APLICACION != '1' \n" +
				       "ORDER BY ID_CARGO_COMISION \n";
				PreparedStatement ps5 = this.conn.prepareStatement(sSql);
				ps5.execute();
				ResultSet rs5 = ps5.getResultSet();
				while (rs5.next()){
					sVista = sVista + "A" + iPago + ".IMP_ACCESORIO + \n";
					iPago++;
				}
				
				sVista = sVista + " NVL(T.IMP_INTERES,0) + NVL(T.IMP_IVA_INTERES,0) + NVL(T.IMP_INTERES_EXTRA,0) + NVL(T.IMP_IVA_INTERES_EXTRA,0) + NVL(T.IMP_CAPITAL_AMORT,0)) PAGO_TOTAL, \n";
				
				
				
				sVista = sVista + "T.IMP_SALDO_INICIAL, \n"+
								  "T.IMP_INTERES, \n"+
								  "T.IMP_INTERES_EXTRA, \n"+
								  "T.IMP_CAPITAL_AMORT, \n"+
								  "T.IMP_PAGO, \n"+
								  "T.IMP_SALDO_FINAL, \n"+
								  "T.IMP_IVA_INTERES, \n"+
								  "T.IMP_IVA_INTERES_EXTRA \n"+
								  "FROM \n";
				
				sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
			           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
				       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
				       "AND    ID_FORMA_APLICACION != '1' \n" +
					   "ORDER BY ID_CARGO_COMISION \n";
				PreparedStatement ps6 = this.conn.prepareStatement(sSql);
				ps6.execute();
				ResultSet rs6 = ps6.getResultSet();
				while (rs6.next()){
					sVista = sVista + " SIM_TABLA_AMORT_ACCESORIO A" + iTabla + ",  \n";
					iTabla++;
				}
			
				sVista = sVista + "SIM_TABLA_AMORTIZACION T \n"+
								  "WHERE  1=1 \n"+
								  "AND    T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								  "AND    T.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
								  "AND    T.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
						
				sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
		           "FROM SIM_PRESTAMO_GPO_CARGO \n" +
			       "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			       "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			       "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
			       "AND    ID_FORMA_APLICACION != '1' \n" +
			       "ORDER BY ID_CARGO_COMISION \n";
				PreparedStatement ps7 = this.conn.prepareStatement(sSql);
				ps7.execute();
				ResultSet rs7 = ps7.getResultSet();
				while (rs7.next()){
						sVista = sVista + "AND	A" + iRelacion + ".CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
										  "AND	A" + iRelacion + ".CVE_EMPRESA = T.CVE_EMPRESA \n"+
										  "AND	A" + iRelacion + ".ID_PRESTAMO = T.ID_PRESTAMO \n"+
										  "AND	A" + iRelacion + ".ID_ACCESORIO = '" + rs7.getString("ID_ACCESORIO") + "' \n"+
										  "AND  A" + iRelacion + ".NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION   \n";
						iRelacion++;
				}
			
				iCont++;
			}else if (iCont > 0){
				iAccesorio = 0;
				iAccesorioCU1= 0;
				iAccesorioCU2= 0;
				iPago = 0;
				iTabla = 0;
				iRelacion = 0;
				
				sVista = sVista +   "UNION ALL \n"+
								    "SELECT \n"+
									"T.CVE_GPO_EMPRESA, \n"+
									"T.CVE_EMPRESA, \n"+
									"T.NUM_PAGO_AMORTIZACION, \n"+
									"T.FECHA_AMORTIZACION, \n"+
									"T.TASA_INTERES, \n";
					
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
					   "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					   "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					   "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					   "AND    ID_FORMA_APLICACION != '1' \n" ;
					PreparedStatement ps8 = this.conn.prepareStatement(sSql);
					ps8.execute();
					ResultSet rs8 = ps8.getResultSet();
					while (rs8.next()){
					sVista = sVista + " A" + iAccesorioCU1 + ".IMP_ACCESORIO IMP_ACCESORIO_" + iAccesorioCU1 + ",  \n";
					iAccesorioCU1++;
					}
					
					
					
					sVista = sVista + " ( \n";
					
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
					   "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					   "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					   "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					   "AND    ID_FORMA_APLICACION != '1' \n" +
					   "ORDER BY ID_CARGO_COMISION \n";
					PreparedStatement ps9 = this.conn.prepareStatement(sSql);
					ps9.execute();
					ResultSet rs9 = ps9.getResultSet();
					while (rs9.next()){
					sVista = sVista + "A" + iPago + ".IMP_ACCESORIO + \n";
					iPago++;
					}
					
					sVista = sVista + " NVL(T.IMP_INTERES,0) + NVL(T.IMP_IVA_INTERES,0) + NVL(T.IMP_INTERES_EXTRA,0) + NVL(T.IMP_IVA_INTERES_EXTRA,0) + NVL(T.IMP_CAPITAL_AMORT,0)) PAGO_TOTAL, \n";
					
					
					
					sVista = sVista + "T.IMP_SALDO_INICIAL, \n"+
								  "T.IMP_INTERES, \n"+
								  "T.IMP_INTERES_EXTRA, \n"+
								  "T.IMP_CAPITAL_AMORT, \n"+
								  "T.IMP_PAGO, \n"+
								  "T.IMP_SALDO_FINAL, \n"+
								  "T.IMP_IVA_INTERES, \n"+
								  "T.IMP_IVA_INTERES_EXTRA \n"+
								  "FROM \n";
					
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
					   "FROM SIM_PRESTAMO_GPO_CARGO \n" +
					   "WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   "AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					   "AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					   "AND    ID_FORMA_APLICACION != '1' \n" +
					   "ORDER BY ID_CARGO_COMISION \n";
					PreparedStatement ps10 = this.conn.prepareStatement(sSql);
					ps10.execute();
					ResultSet rs10 = ps10.getResultSet();
					while (rs10.next()){
					sVista = sVista + " SIM_TABLA_AMORT_ACCESORIO A" + iTabla + ",  \n";
					iTabla++;
					}
					
					sVista = sVista + "SIM_TABLA_AMORTIZACION T \n"+
								  "WHERE  1=1 \n"+
								  "AND    T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								  "AND    T.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
								  "AND    T.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
						
					sSql = "SELECT DISTINCT ID_CARGO_COMISION ID_ACCESORIO \n" +
			           		"FROM SIM_PRESTAMO_GPO_CARGO \n" +
			           		"WHERE  CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			           		"AND    CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			           		"AND    ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
			           		"AND    ID_FORMA_APLICACION != '1' \n" +
			           		"ORDER BY ID_CARGO_COMISION \n";
					PreparedStatement ps11 = this.conn.prepareStatement(sSql);
					ps11.execute();
					ResultSet rs11 = ps11.getResultSet();
					while (rs11.next()){
							sVista = sVista + "AND	A" + iRelacion + ".CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
												"AND	A" + iRelacion + ".CVE_EMPRESA = T.CVE_EMPRESA \n"+
												"AND	A" + iRelacion + ".ID_PRESTAMO = T.ID_PRESTAMO \n"+
												"AND	A" + iRelacion + ".ID_ACCESORIO = '" + rs11.getString("ID_ACCESORIO") + "' \n"+
												"AND  A" + iRelacion + ".NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION   \n";
							iRelacion++;
					}

				iCont++;
			}
		}
		sVista = sVista + ") \n"+
							"GROUP BY \n"+
							"CVE_GPO_EMPRESA, \n"+
							"CVE_EMPRESA, \n"+
							"NUM_PAGO_AMORTIZACION, \n"+
							"FECHA_AMORTIZACION, \n"+
							"TASA_INTERES \n"+
							"ORDER BY NUM_PAGO_AMORTIZACION \n";
			
		sSql = sVista;
			
		ejecutaSql();
		return getConsultaLista();
	}
}