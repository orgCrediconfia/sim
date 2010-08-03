/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base para dar de alta las fechas en que se desembolsarán los préstamos.
 */
 
public class SimPrestamoFechaDesembolsoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		
			sSql =  "SELECT DISTINCT \n"+ 
					"G.ID_PRESTAMO_GRUPO ID_PRESTAMO, \n"+  
					"G.CVE_PRESTAMO_GRUPO CVE_PRESTAMO, \n"+ 
					"G.FECHA_ENTREGA, \n"+ 
					"G.DIA_SEMANA_PAGO, \n"+ 
					"G.ID_PERIODICIDAD_PRODUCTO, \n"+ 
					"GPO.NOM_GRUPO NOMBRE, \n"+ 
					"'GRUPO'  \n"+ 
					"FROM \n"+ 
					"SIM_PRESTAMO_GRUPO G, \n"+  
					"SIM_GRUPO GPO,  \n"+
					"SIM_CAT_ETAPA_PRESTAMO E \n"+
					"WHERE G.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+ 
					"AND E.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+ 
					"AND E.CVE_EMPRESA = G.CVE_EMPRESA \n"+ 
					"AND E.ID_ETAPA_PRESTAMO = G.ID_ETAPA_PRESTAMO \n"+ 
					"AND E.B_AUTORIZAR_COMITE = 'V' \n"+ 
					"AND GPO.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+  
					"AND GPO.CVE_EMPRESA = G.CVE_EMPRESA  \n"+ 
					"AND GPO.ID_GRUPO = G.ID_GRUPO \n";
			
			if (parametros.getDefCampo("FECHA_ENTREGA") != null) {
				sSql = sSql + "AND G.FECHA_ENTREGA = TO_DATE('" + (String) parametros.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n";
			}
			if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
				sSql = sSql + "AND G.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
			}
			if (parametros.getDefCampo("NOMBRE") != null) {
				sSql = sSql + "AND UPPER(GPO.NOM_GRUPO) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase() + "%' \n";
			}
			
			sSql = sSql + "UNION \n"+ 
		            "SELECT \n"+ 
					"P.ID_PRESTAMO, \n"+ 
					"P.CVE_PRESTAMO, \n"+ 
					"P.FECHA_ENTREGA, \n"+ 
					"P.DIA_SEMANA_PAGO, \n"+ 
					"P.ID_PERIODICIDAD_PRODUCTO, \n"+ 
					"PER.NOM_COMPLETO NOMBRE, \n"+ 
					"'INDIVIDUAL'  \n"+ 
					"FROM \n"+ 
		            "SIM_COMITE_PRESTAMO G, \n"+ 
					"SIM_PRESTAMO P, \n"+ 
					"RS_GRAL_PERSONA PER, \n"+
					"SIM_CAT_ETAPA_PRESTAMO E \n"+
					"WHERE G.CVE_GPO_EMPRESA=  '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+ 
					"AND G.CVE_EMPRESA =  '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
		            "AND G.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+   
					"AND G.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
					"AND G.ID_PRESTAMO = P.ID_PRESTAMO \n"+ 
		            "AND G.ID_PRESTAMO_GRUPO IS NULL \n"+ 
		            "AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
					"AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
					"AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n"+ 
					"AND E.B_AUTORIZAR_COMITE = 'V' \n"+ 
		            "AND PER.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+   
					"AND PER.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
					"AND PER.ID_PERSONA = P.ID_CLIENTE \n";
                                
			if (parametros.getDefCampo("FECHA_ENTREGA") != null) {
				sSql = sSql + "AND P.FECHA_ENTREGA = TO_DATE('" + (String) parametros.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n";
			}
			if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
				sSql = sSql + "AND P.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
			}
			if (parametros.getDefCampo("NOMBRE") != null) {
				sSql = sSql + " AND UPPER(PER.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase() + "%' \n";
			}
		
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
			   "P.CVE_GPO_EMPRESA, \n" +
			   "P.CVE_EMPRESA, \n" +
			   "P.ID_PRESTAMO \n"+
			" FROM SIM_PRESTAMO_GPO_DET P \n"+
			" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND P.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		ResultadoCatalogo resultadoCatalogoGenerarTablaAmortizacion = new ResultadoCatalogo();
		SimGenerarTablaAmortizacionDAO simGenerarTablaAmortizacionDAO = new SimGenerarTablaAmortizacionDAO();
		simGenerarTablaAmortizacionDAO.setConexion(this.getConexion());
		int iNumFecha = 0;
		int iNumDia = 0;
		String sFechaEntrega = "";
		
		sFechaEntrega = (String)registro.getDefCampo("FECHA_ENTREGA");
		
		if (registro.getDefCampo("APLICA").equals("Grupo")){
			
			//Para créditos semanales y catorcenales que den un día de pago.
			if (!registro.getDefCampo("DIA_SEMANA_PAGO").equals("null") && !registro.getDefCampo("DIA_SEMANA_PAGO").equals("")) {
				
				sSql = " SELECT \n" +
				   " F_DIA_FESTIVO \n" +
				   " FROM PFIN_DIA_FESTIVO \n" +
				   " WHERE F_DIA_FESTIVO      	= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n" +
				   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				   " AND CVE_PAIS 			= 'MX' \n";		
				ejecutaSql();
				
				//Si la fecha de desembolso resultará se un día festivo te pide que ingreses otra fecha.
				if (rs.next()){
					
					resultadoCatalogo.mensaje.setClave("FD_DIA_FESTIVO");
				}else {
					
					//Obtiene el día de la fecha de desembolso.
					sSql = " SELECT TO_CHAR(TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'),'day') DIA FROM DUAL \n" ;
					ejecutaSql();
					if (rs.next()){
						
						String sFecha = rs.getString("DIA");
						
						if (sFecha.equals("sunday   ")){
							iNumFecha = 1;
						}else if (sFecha.equals("monday   ")){
							iNumFecha = 2;
						}else if (sFecha.equals("tuesday  ")){
							iNumFecha = 3;
						}else if (sFecha.equals("wednesday")){
							iNumFecha = 4;
						}else if (sFecha.equals("thursday ")){
							iNumFecha = 5;
						}else if (sFecha.equals("friday   ")){
							iNumFecha = 6;
						}else if (sFecha.equals("saturday ")){
							iNumFecha = 7;
						}
					}
					
						if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Domingo")){
							iNumDia = 1;
						}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Lunes")){
							iNumDia = 2;
						}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Martes")){
							iNumDia = 3;
						}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Miercoles")){
							iNumDia = 4;
						}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Jueves")){
							iNumDia = 5;
						}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Viernes")){
							iNumDia = 6;
						}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Sabado")){
							iNumDia = 7;
						}
						
					if (iNumDia > iNumFecha){
						
						int iFactorSuma = iNumDia - iNumFecha;
						
						sSql = "SELECT TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') + '"+iFactorSuma+"' FECHA_REAL FROM DUAL \n" ;
						ejecutaSql();
						if (rs.next()){
							String sFechaReal = rs.getString("FECHA_REAL");
							
							registro.addDefCampo("FECHA_REAL",rs.getString("FECHA_REAL"));
						}
					}else if (iNumDia < iNumFecha){
						
						int iFactorSuma = 7 - iNumFecha;
						iFactorSuma = iFactorSuma + iNumDia;
					
						sSql = "SELECT TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') + '"+iFactorSuma+"' FECHA_REAL FROM DUAL \n" ;
						ejecutaSql();
						
						if (rs.next()){
							String sFechaReal = rs.getString("FECHA_REAL");
							
							registro.addDefCampo("FECHA_REAL",rs.getString("FECHA_REAL")== null ? "": rs.getString("FECHA_REAL"));
						}
					}else if (iNumDia == iNumFecha){
						
						registro.addDefCampo("FECHA_REAL","");
					}
					
					sSql = " SELECT \n" +
						   " F_DIA_FESTIVO \n" +
						   " FROM PFIN_DIA_FESTIVO \n" +
						   " WHERE F_DIA_FESTIVO      	= TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REAL") + "','yyyy-MM-dd HH24:MI:SSXFF') \n" +
						   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						   " AND CVE_PAIS 			= 'MX' \n";		
						ejecutaSql();
						if (rs.next()){
							
							resultadoCatalogo.mensaje.setClave("FR_DIA_FESTIVO");
						}else {
					
							sSql = " UPDATE SIM_PRESTAMO_GRUPO SET \n" +
								   " FECHA_ENTREGA     		= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'), \n" +
								   " FECHA_REAL     		= TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REAL") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
								   " DIA_SEMANA_PAGO     		= '" + (String)registro.getDefCampo("DIA_SEMANA_PAGO") + "' \n" +
								   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
							
							PreparedStatement ps1 = this.conn.prepareStatement(sSql);
							ps1.execute();
							ResultSet rs1 = ps1.getResultSet();
							
							sSql = " SELECT \n" +
								   " ID_PRESTAMO \n" +
								   " FROM SIM_PRESTAMO_GPO_DET \n" +
								   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
							
							PreparedStatement ps2 = this.conn.prepareStatement(sSql);
							ps2.execute();
							ResultSet rs2 = ps2.getResultSet();
							
							
							while (rs2.next()){
							
								registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO")== null ? "": rs2.getString("ID_PRESTAMO"));
								
								sSql = " UPDATE SIM_PRESTAMO SET \n" +
									   " FECHA_ENTREGA     		= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'), \n" +
									   " FECHA_REAL     		= TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REAL") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
									   " DIA_SEMANA_PAGO     		= '" + (String)registro.getDefCampo("DIA_SEMANA_PAGO") + "' \n" +
									   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
									   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
								
								PreparedStatement ps3 = this.conn.prepareStatement(sSql);
								ps3.execute();
								ResultSet rs3 = ps3.getResultSet();
								
								resultadoCatalogoGenerarTablaAmortizacion = simGenerarTablaAmortizacionDAO.alta(registro);	
							}
					
					
						}
					
			
				}
			
			
		}//Para créditos semanales y catorcenales.
			
		else {
			
			sSql = " SELECT \n" +
			   " F_DIA_FESTIVO \n" +
			   " FROM PFIN_DIA_FESTIVO \n" +
			   " WHERE F_DIA_FESTIVO      	= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_PAIS 			= 'MX' \n";		
			ejecutaSql();
			if (rs.next()){
				resultadoCatalogo.mensaje.setClave("FD_DIA_FESTIVO");
			}else {
			
			
				sSql = " UPDATE SIM_PRESTAMO_GRUPO SET \n" +
				   " FECHA_ENTREGA     		= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n" +
				   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				
				sSql = " SELECT \n" +
					   " ID_PRESTAMO \n" +
					   " FROM SIM_PRESTAMO_GPO_DET \n" +
					   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
				
				
				while (rs2.next()){
					
					registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO")== null ? "": rs2.getString("ID_PRESTAMO"));
					
					sSql = " UPDATE SIM_PRESTAMO SET \n" +
						   " FECHA_ENTREGA     		= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n" +
						   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					
				}
				
				sSql = " SELECT \n" +
					   " ID_PRESTAMO \n" +
					   " FROM SIM_PRESTAMO_GPO_DET \n" +
					   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				
				PreparedStatement ps4 = this.conn.prepareStatement(sSql);
				ps4.execute();
				ResultSet rs4 = ps4.getResultSet();
				
				while (rs4.next()){
					
					registro.addDefCampo("ID_PRESTAMO",rs4.getString("ID_PRESTAMO")== null ? "": rs4.getString("ID_PRESTAMO"));
					
					resultadoCatalogoGenerarTablaAmortizacion = simGenerarTablaAmortizacionDAO.alta(registro);
				}
					
			}
		}
			
			
		}else if (registro.getDefCampo("APLICA").equals("Individual")){
			
			if (!registro.getDefCampo("DIA_SEMANA_PAGO").equals("ninguno")) {
				
				sSql = " SELECT TO_CHAR(TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'),'day') DIA FROM DUAL \n" ;
				ejecutaSql();
				if (rs.next()){
					String sFecha = rs.getString("DIA");
					System.out.println("DIA"+sFecha+"es");
					if (sFecha.equals("sunday   ")){
						iNumFecha = 1;
					}else if (sFecha.equals("monday   ")){
						iNumFecha = 2;
					}else if (sFecha.equals("tuesday  ")){
						iNumFecha = 3;
					}else if (sFecha.equals("wednesday")){
						iNumFecha = 4;
					}else if (sFecha.equals("thursday ")){
						iNumFecha = 5;
					}else if (sFecha.equals("friday   ")){
						iNumFecha = 6;
					}else if (sFecha.equals("saturday ")){
						iNumFecha = 7;
					}
					System.out.println("iNumFecha"+iNumFecha);
				}
				
				System.out.println("DIA_SEMANA_PAGO"+registro.getDefCampo("DIA_SEMANA_PAGO")+"es");
				if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Domingo")){
					iNumDia = 1;
				}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Lunes")){
					iNumDia = 2;
				}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Martes")){
					iNumDia = 3;
				}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Miercoles")){
					iNumDia = 4;
				}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Jueves")){
					iNumDia = 5;
				}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Viernes")){
					iNumDia = 6;
				}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Sabado")){
					iNumDia = 7;
				}
				System.out.println("iNumDia "+iNumDia);
				
				if (iNumDia > iNumFecha){
					
					int iFactorSuma = iNumDia - iNumFecha;
					
					sSql = "SELECT TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') + '"+iFactorSuma+"' FECHA_REAL FROM DUAL \n" ;
					ejecutaSql();
					if (rs.next()){
						String sFechaReal = rs.getString("FECHA_REAL");
						
						registro.addDefCampo("FECHA_REAL",rs.getString("FECHA_REAL"));
					}
				}else if (iNumDia < iNumFecha){
					
					int iFactorSuma = 7 - iNumFecha;
					iFactorSuma = iFactorSuma + iNumDia;
					
					sSql = "SELECT TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') + '"+iFactorSuma+"' FECHA_REAL FROM DUAL \n" ;
					ejecutaSql();
					
					if (rs.next()){
						String sFechaReal = rs.getString("FECHA_REAL");
						
						registro.addDefCampo("FECHA_REAL",rs.getString("FECHA_REAL")== null ? "": rs.getString("FECHA_REAL"));
					}
				}else if (iNumDia == iNumFecha){
					
					registro.addDefCampo("FECHA_REAL","");
				}
				
				
				sSql = " UPDATE SIM_PRESTAMO SET \n" +
					   " FECHA_ENTREGA     		= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY'), \n" +
					   " FECHA_REAL     		= TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REAL") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
					   " DIA_SEMANA_PAGO     	= '" + (String)registro.getDefCampo("DIA_SEMANA_PAGO") + "' \n" +
					   " WHERE ID_PRESTAMO     	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				System.out.println("Fecha de entrega en SIM_PRESTAMO**************"+sSql);
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
					
				resultadoCatalogoGenerarTablaAmortizacion = simGenerarTablaAmortizacionDAO.alta(registro);	
				
			}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("ninguno")) {
				System.out.println("SE TRATA DE UN CREDITO QUE NO ES SEMANAL NI CATORCENAL, POR LO QUE SU FECHA DE ENTREGA ES LA FECHA DE DESEMBOLSO");
				sSql = " UPDATE SIM_PRESTAMO SET \n" +
				   " FECHA_ENTREGA     		= TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n" +
				   " WHERE ID_PRESTAMO      = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
					
				resultadoCatalogoGenerarTablaAmortizacion = simGenerarTablaAmortizacionDAO.alta(registro);	
				
			}	
		}
		
		simGenerarTablaAmortizacionDAO.cierraConexion();
		
		return resultadoCatalogo;
	}
}