/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base para asignar las líneas de fondeo a los préstamos.
 */
 
public class SimPrestamoLineaFondeoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"C.CVE_GPO_EMPRESA,\n"+
				"C.CVE_EMPRESA,\n"+
				"C.APLICA_A,\n"+
				"C.ID_PRESTAMO,\n"+
				"C.CVE_PRESTAMO,\n"+
				"C.ID_PRODUCTO,\n"+
				"C.NOMBRE,\n"+
				"C.NUM_CICLO,\n"+
				"C.FECHA_ENTREGA,\n"+
				"C.ID_ETAPA_PRESTAMO,\n"+
				"NVL(C.MONTO_AUTORIZADO,0) + NVL(C.CARGO_INICIAL,0) IMPORTE_PRESTADO, \n"+
				"C.NUM_LINEA\n"+
				"FROM V_CREDITO C, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND C.APLICA_A != 'INDIVIDUAL_GRUPO' \n"+
				"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+ 
				"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+ 
				"AND E.B_LINEA_FONDEO = 'V' \n"+
				"AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
		        "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
		        "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
		        "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
		
		if (parametros.getDefCampo("FECHA_ENTREGA") != null) {
			sSql = sSql + "AND FECHA_ENTREGA = TO_DATE('" + (String) parametros.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n";
		}
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOMBRE") != null) {
			sSql = sSql + " AND UPPER(NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase() + "%' \n";
		}
		if (parametros.getDefCampo("NUM_LINEA") != null) {
			sSql = sSql + "AND NUM_LINEA = '" + (String) parametros.getDefCampo("NUM_LINEA") + "' \n";
		}
	
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
		
		String sMontoAutorizado = "";
		float fMontoAutorizado = 0;
		String sMontoDisponible = "";
		float fMontoDisponible = 0;
		String sNumLinea = "";
		float fMontoDisponibleFinal = 0;
		
		String sAplica[] = (String[])registro.getDefCampo("DAO_APLICA_A");
		String sPrestamo[] = (String[])registro.getDefCampo("DAO_ID_PRESTAMO");
		String sLinea[] = (String[])registro.getDefCampo("DAO_ID_LINEA");

		String sAplicaA = new String();
		String sIdPrestamo = new String();
		String sIdLinea = new String();
		
		
		for (int iNumParametro = 0; iNumParametro < sLinea.length; iNumParametro++) {
			System.out.println("iNumParametro"+iNumParametro);
			sAplicaA = sAplica[iNumParametro];
			sIdPrestamo = sPrestamo[iNumParametro];
			sIdLinea = sLinea[iNumParametro];
				
			registro.addDefCampo("APLICA_A",sAplicaA);
			registro.addDefCampo("ID_PRESTAMO",sIdPrestamo);
			registro.addDefCampo("NUM_LINEA",sIdLinea);
				
			if (registro.getDefCampo("APLICA_A").equals("GRUPO")){
					
				//Obtiene el monto autorizado del grupo.
				sSql =  "SELECT \n"+
						"NVL(A.MONTO_AUTORIZADO,0) + NVL(B.CARGO_INICIAL,0) IMPORTE_PRESTADO \n"+
						"FROM SIM_PRESTAMO_GRUPO P, \n"+
						"SIM_GRUPO N, \n"+ 
						"(SELECT \n"+ 
						"CVE_GPO_EMPRESA, \n"+
						"CVE_EMPRESA, \n"+
						"ID_PRESTAMO_GRUPO, \n"+
						"SUM(MONTO_AUTORIZADO) MONTO_AUTORIZADO \n"+
						"FROM SIM_PRESTAMO_GPO_DET \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
						"GROUP BY ID_PRESTAMO_GRUPO, \n"+
						"CVE_GPO_EMPRESA, \n"+ 
						"CVE_EMPRESA) A, \n"+
						"(SELECT  \n"+
						"P.CVE_GPO_EMPRESA, \n"+
						"P.CVE_EMPRESA, \n"+
						"P.ID_PRESTAMO_GRUPO, \n"+
						"SUM(C.CARGO_INICIAL) CARGO_INICIAL \n"+
						"FROM SIM_PRESTAMO_GPO_DET P, \n"+ 
						"SIM_PRESTAMO_GPO_CARGO C \n"+
						"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND P.ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
						"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND C.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n"+
						"AND C.ID_FORMA_APLICACION = 1 \n"+
						"GROUP BY  \n"+
						"P.CVE_GPO_EMPRESA, \n"+
						"P.CVE_EMPRESA, \n"+
						"P.ID_PRESTAMO_GRUPO)B \n"+
						"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND P.ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
						"AND N.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						"AND N.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND N.ID_GRUPO = P.ID_GRUPO \n"+
						"AND A.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						"AND A.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND A.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n"+
						"AND B.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						"AND B.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND B.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n";
				
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				if (rs1.next()){
					sMontoAutorizado = rs1.getString("IMPORTE_PRESTADO") == null ? "0" : rs1.getString("IMPORTE_PRESTADO");
				}
				System.out.println("sMontoAutorizado"+sMontoAutorizado);
				System.out.println("NUM_LINEA"+registro.getDefCampo("NUM_LINEA"));
				//Valido que exista un monto autorizado diferente de cero y que el usuario haya llenado el combo de línea de fondeo.
				if (!sMontoAutorizado.equals("0") && !registro.getDefCampo("NUM_LINEA").equals("null")){
					System.out.println("paso 1 grupal");
					fMontoAutorizado = (Float.parseFloat(sMontoAutorizado));
					
					//Obtiene el monto disponible en la línea de fondeo.
					sSql =  "SELECT \n"+
						    " MONTO_DISPONIBLE \n"+
							" FROM SIM_CAT_FONDEADOR_LINEA F \n"+
							" WHERE F.CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND F.CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND F.NUM_LINEA ='" + (String)registro.getDefCampo("NUM_LINEA") + "' \n";
						
					PreparedStatement ps2 = this.conn.prepareStatement(sSql);
					ps2.execute();
					ResultSet rs2 = ps2.getResultSet();
						
					if (rs2.next()){
						sMontoDisponible = rs2.getString("MONTO_DISPONIBLE");
					}	
					fMontoDisponible = (Float.parseFloat(sMontoDisponible));
					
					//Verifica si el monto autorizado del crédito no es mayor al monto disponible de la línea.
					if (fMontoAutorizado <= fMontoDisponible){
							
						fMontoDisponibleFinal = fMontoDisponible - fMontoAutorizado;
						//Actualiza el monto disponible de la línea de fondeo.
						sSql = " UPDATE SIM_CAT_FONDEADOR_LINEA SET \n" +
							   " MONTO_DISPONIBLE     		= '" + fMontoDisponibleFinal  + "' \n" +
							   " WHERE NUM_LINEA      	= '" + (String)registro.getDefCampo("NUM_LINEA") + "' \n" +
							   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
							
						PreparedStatement ps3 = this.conn.prepareStatement(sSql);
						ps3.execute();
						ResultSet rs3 = ps3.getResultSet();
							
						//Asínga el prestamo grupal a la línea de fondeo.
						sSql = " UPDATE SIM_PRESTAMO_GRUPO SET \n" +
							   " NUM_LINEA     		= '" + (String)registro.getDefCampo("NUM_LINEA")  + "' \n" +
							   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
							
						PreparedStatement ps4 = this.conn.prepareStatement(sSql);
						ps4.execute();
						ResultSet rs4 = ps4.getResultSet();
						
						//Obtiene los prestamos individuales del grupo.
						sSql = " SELECT \n" +
							   " ID_PRESTAMO \n" +
							   " FROM SIM_PRESTAMO_GPO_DET \n" +
							   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							   " AND ID_ETAPA_PRESTAMO != '31' \n" +
							   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
						PreparedStatement ps5 = this.conn.prepareStatement(sSql);
						ps5.execute();
						ResultSet rs5 = ps5.getResultSet();
						
						while (rs5.next()){
							registro.addDefCampo("ID_PRESTAMO_INDIVIDUAL",rs5.getString("ID_PRESTAMO")== null ? "": rs5.getString("ID_PRESTAMO"));
							
							//Asigna a la línea de fondeo a cada crédito individual que conforma el crédito grupal.
							sSql = " UPDATE SIM_PRESTAMO SET \n" +
								   " NUM_LINEA     		= '" + (String)registro.getDefCampo("NUM_LINEA")  + "' \n" +
								   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
								   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
								   
							PreparedStatement ps6 = this.conn.prepareStatement(sSql);
							ps6.execute();
							ResultSet rs6 = ps6.getResultSet();
						}

						//Obtiene el identificador de la etapa de asignación de líneas de fondeo.
						sSql =  "SELECT \n"+
								"ID_ETAPA_PRESTAMO \n"+
								"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND B_LINEA_FONDEO = 'V' \n";
						ejecutaSql();
						if (rs.next()){
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
						}
							
						//Verfica si el usuario puede manipular la etapa.
						sSql =  " SELECT \n"+
									"CVE_FUNCION, \n"+
									"CVE_APLICACION, \n"+
									"CVE_PERFIL \n"+
									"FROM RS_CONF_APLICACION_CONFIG \n"+
									"WHERE CVE_FUNCION = (SELECT \n"+
									"CVE_FUNCION \n"+
									"FROM \n"+
									"SIM_CAT_ETAPA_PRESTAMO \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
									") \n"+
									"AND CVE_APLICACION = 'Prestamo' \n"+
									"AND CVE_PERFIL = ( \n"+
									"SELECT \n"+
									"CVE_PERFIL \n"+ 
									"FROM RS_GRAL_USUARIO_PERFIL \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
									"AND CVE_APLICACION = 'Prestamo') \n";
						PreparedStatement ps7 = this.conn.prepareStatement(sSql);
						ps7.execute();
						ResultSet rs7 = ps7.getResultSet();
						if(rs7.next()){
							
							//Obtiene los prestamos individuales del grupo.
							sSql = " SELECT \n" +
								   " ID_PRESTAMO \n" +
								   " FROM SIM_PRESTAMO_GPO_DET \n" +
								   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								   " AND ID_ETAPA_PRESTAMO != '31' \n" +
								   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
							PreparedStatement ps8 = this.conn.prepareStatement(sSql);
							ps8.execute();
							ResultSet rs8 = ps8.getResultSet();
						
							while (rs8.next()){
								registro.addDefCampo("ID_PRESTAMO_INDIVIDUAL",rs8.getString("ID_PRESTAMO")== null ? "": rs8.getString("ID_PRESTAMO"));
								/*
								//Avanza la etapa.
								//Obtiene las actividades de la etapa que se completa por cada prestamo individual.
								sSql =  "SELECT \n"+
										"CVE_GPO_EMPRESA, \n" +
										"CVE_EMPRESA, \n" +
										"ID_ETAPA_PRESTAMO, \n"+
										"ID_ACTIVIDAD_REQUISITO, \n" +
										"FECHA_REGISTRO, \n" +
										"ORDEN_ETAPA \n" +
										"FROM \n" +
										"SIM_PRESTAMO_ETAPA \n" +
										"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
										"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
										"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" ;
							
								PreparedStatement ps9 = this.conn.prepareStatement(sSql);
								ps9.execute();
								ResultSet rs9 = ps9.getResultSet();
				
								while (rs9.next()){
							
									registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs9.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs9.getString("ID_ACTIVIDAD_REQUISITO"));
									registro.addDefCampo("ORDEN_ETAPA",rs9.getString("ORDEN_ETAPA")== null ? "": rs9.getString("ORDEN_ETAPA"));
									registro.addDefCampo("FECHA_REGISTRO",rs9.getString("FECHA_REGISTRO")== null ? "": rs9.getString("FECHA_REGISTRO"));
								
									//Guarda el historial de la etapa y las actividades.
									sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
									PreparedStatement ps10 = this.conn.prepareStatement(sSql);
									ps10.execute();
									ResultSet rs10 = ps10.getResultSet();
									if (rs10.next()){
										registro.addDefCampo("ID_HISTORICO",rs10.getString("ID_HISTORICO"));
									
										sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
												"CVE_GPO_EMPRESA, \n" +
												"CVE_EMPRESA, \n" +
												"ID_HISTORICO, \n" +
												"ID_PRESTAMO, \n" +
												"ID_ACTIVIDAD_REQUISITO, \n" +
												"ID_ETAPA_PRESTAMO, \n"+
												"FECHA_REALIZADA, \n"+
												"FECHA_REGISTRO, \n"+
												"ESTATUS, \n"+
												"CVE_USUARIO, \n"+
												"ORDEN_ETAPA) \n"+
												" VALUES (" +
												"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
												"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
												"SYSDATE, \n"+
												"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
												"'Completada', \n"+
												"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
												"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
									
										PreparedStatement ps11 = this.conn.prepareStatement(sSql);
										ps11.execute();
										ResultSet rs11 = ps11.getResultSet();
									}
								}
							*/
							//Obtiene la siguiente etapa.
							sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO, \n"+
									" ORDEN_ETAPA \n"+
									"	  FROM SIM_PRESTAMO_ETAPA \n"+
									"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
									"	  ORDEN_ETAPA + 1 \n"+
									" 	  FROM SIM_PRESTAMO_ETAPA \n"+
									"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n"+
									"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
									"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n";
						
							PreparedStatement ps14 = this.conn.prepareStatement(sSql);
							ps14.execute();
							ResultSet rs14 = ps14.getResultSet();
							if(rs14.next()){
							registro.addDefCampo("ETAPA_PRESTAMO",rs14.getString("ID_ETAPA_PRESTAMO"));
							
							//Actualiza la etapa del crédito grupal.
							sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
									"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "', \n"+
									" COMENTARIO 		='' \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
									" AND ID_ETAPA_PRESTAMO != '31' \n" +
									"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							
							PreparedStatement ps15 = this.conn.prepareStatement(sSql);
							ps15.execute();
							ResultSet rs15 = ps15.getResultSet();
					
							//Actualiza las etapas de los créditos individuales que pertenecen al grupal.
							sSql =  " UPDATE SIM_PRESTAMO SET \n"+
									"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" ;
							
							PreparedStatement ps16 = this.conn.prepareStatement(sSql);
							ps16.execute();
							ResultSet rs16 = ps16.getResultSet();
					
							//Actualiza la fecha del registro y el estatus de las etapas de los créditos individuales que pertenecen al grupal.
							sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
									" FECHA_REGISTRO 		=SYSDATE, \n" +
									" ESTATUS 	 		='Registrada', \n" +
									" COMENTARIO 	 		='' \n" +
									" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
									" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n";
							
							PreparedStatement ps17 = this.conn.prepareStatement(sSql);
							ps17.execute();
							ResultSet rs17 = ps17.getResultSet();
					/*
							//Obtiene las actividades de la nueva etapa.
							sSql =  "SELECT \n"+
									"CVE_GPO_EMPRESA, \n"+
									"CVE_EMPRESA, \n"+
									"ID_PRESTAMO, \n"+
									"ID_ACTIVIDAD_REQUISITO, \n"+
									"ID_ETAPA_PRESTAMO, \n"+
									"FECHA_REGISTRO, \n"+
									"FECHA_REALIZADA, \n"+
									"COMENTARIO, \n"+
									"ORDEN_ETAPA, \n"+
									"ESTATUS \n" +
									"FROM \n"+
									"SIM_PRESTAMO_ETAPA \n"+
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n"+
									"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n";
						
							PreparedStatement ps18 = this.conn.prepareStatement(sSql);
							ps18.execute();
							ResultSet rs18 = ps18.getResultSet();
								
							while (rs18.next()){
								registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs18.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs18.getString("ID_ACTIVIDAD_REQUISITO"));
								registro.addDefCampo("ETAPA_PRESTAMO",rs18.getString("ID_ETAPA_PRESTAMO")== null ? "": rs18.getString("ID_ETAPA_PRESTAMO"));
								registro.addDefCampo("ORDEN_ETAPA",rs18.getString("ORDEN_ETAPA")== null ? "": rs18.getString("ORDEN_ETAPA"));
								registro.addDefCampo("FECHA_REGISTRO",rs18.getString("FECHA_REGISTRO")== null ? "": rs18.getString("FECHA_REGISTRO"));
								registro.addDefCampo("COMENTARIO",rs18.getString("COMENTARIO")== null ? "": rs18.getString("COMENTARIO"));
								registro.addDefCampo("ESTATUS",rs18.getString("ESTATUS")== null ? "": rs18.getString("ESTATUS"));
									
								//Actualiza el historial.
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
									
								PreparedStatement ps19 = this.conn.prepareStatement(sSql);
								ps19.execute();
								ResultSet rs19 = ps19.getResultSet();	
								if (rs19.next()){
									registro.addDefCampo("ID_HISTORICO",rs19.getString("ID_HISTORICO"));
									//Actualiza la bitácora.
									sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
											"CVE_GPO_EMPRESA, \n" +
											"CVE_EMPRESA, \n" +
											"ID_HISTORICO, \n" +
											"ID_PRESTAMO, \n" +
											"ID_ACTIVIDAD_REQUISITO, \n" +
											"ID_ETAPA_PRESTAMO, \n"+
											"FECHA_REGISTRO, \n"+
											"ORDEN_ETAPA, \n"+
											"COMENTARIO, \n"+
											"ESTATUS) \n" +
											" VALUES (" +
											"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
											"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
											"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
											"'" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "', \n" +
											"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
											"'" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "', \n" +
											"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
											"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
											"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
											"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
										
									PreparedStatement ps20 = this.conn.prepareStatement(sSql);
									ps20.execute();
									ResultSet rs20 = ps20.getResultSet();
								}
							}
					*/
							}
				
							}
							
							sSql =  " SELECT DISTINCT D.ID_ETAPA_PRESTAMO \n" +
							"		  FROM SIM_PRESTAMO_GPO_DET D, \n" +
							"		  SIM_PRESTAMO_ETAPA E \n" +
							"		  WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"		  AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							"		  AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +    
							"		  AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
							"		 AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
							"		 AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
							"		 AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO \n" +
							"		 AND E.ORDEN_ETAPA = ( \n" +
							"		 SELECT MIN(ORDEN_ETAPA) FROM \n" +
							"		 (SELECT D.ID_ETAPA_PRESTAMO, E.ORDEN_ETAPA \n" +
							"		 FROM SIM_PRESTAMO_GPO_DET D, \n" + 
							"		 SIM_PRESTAMO_ETAPA E \n" +
							"		 WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"		 AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							"		 AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +    
							"		 AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
							"		 AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
							"		 AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
							"		 AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO)) \n";
							
						PreparedStatement ps22 = this.conn.prepareStatement(sSql);
						ps22.execute();
						ResultSet rs22 = ps22.getResultSet();
						
						if(rs22.next()){
						
							//Pasa a la siguiente etapa.
							registro.addDefCampo("ETAPA_PRESTAMO",rs22.getString("ID_ETAPA_PRESTAMO"));
						
							sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n"+
								
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							
							PreparedStatement ps23 = this.conn.prepareStatement(sSql);
							ps23.execute();
							ResultSet rs23 = ps23.getResultSet();
						}
							
					}		
						}else if (fMontoAutorizado > fMontoDisponible){
							
							resultadoCatalogo.mensaje.setClave("LINEA_NO_MONTO_DISPONIBLE");
						}
				}else{
					//Mensaje *
				}
				
			}else if (registro.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
		
				//Obtiene el monto autorizado.
				sSql =  "SELECT \n"+
					    "M.MONTO_AUTORIZADO + NVL(C.CARGO_INICIAL,C.PORCENTAJE_MONTO/100*M.MONTO_AUTORIZADO) IMPORTE_PRESTADO \n"+ 
						" FROM SIM_CLIENTE_MONTO M, \n"+
						"		RS_GRAL_PERSONA PER, \n"+
						"		SIM_PRESTAMO P, \n"+
						"		SIM_PRESTAMO_CARGO_COMISION C \n"+
						" WHERE M.CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						" AND M.CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND M.ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
						" AND PER.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
						" AND PER.CVE_EMPRESA = M.CVE_EMPRESA \n"+
						" AND PER.ID_PERSONA = M.ID_CLIENTE \n"+
						" AND P.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
						" AND P.CVE_EMPRESA = M.CVE_EMPRESA \n"+
						" AND P.ID_PRESTAMO = M.ID_PRESTAMO \n"+
						" AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						" AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						" AND C.ID_PRESTAMO = P.ID_PRESTAMO \n"+
						" AND C.ID_FORMA_APLICACION = 1 \n"+
						" AND P.ID_ETAPA_PRESTAMO != '18' \n";
					
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				
				if (rs1.next()){
					sMontoAutorizado = rs1.getString("IMPORTE_PRESTADO") == null ? "0" : rs1.getString("IMPORTE_PRESTADO");
				}
				System.out.println("sMontoAutorizado"+sMontoAutorizado);
				System.out.println("NUM_LINEA"+registro.getDefCampo("NUM_LINEA"));
				//Valido que exista un monto autorizado diferente de cero y que el usuario haya llenado el combo de línea de fondeo.
				if (!sMontoAutorizado.equals("0") && !registro.getDefCampo("NUM_LINEA").equals("null")){
				
					System.out.println("paso 1 individual");
					fMontoAutorizado = (Float.parseFloat(sMontoAutorizado));
						
					//Obtiene el monto disponible en la línea de fondeo.
					sSql =  "SELECT \n"+
						    " MONTO_DISPONIBLE \n"+
							" FROM SIM_CAT_FONDEADOR_LINEA F \n"+
							" WHERE F.CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND F.CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND F.NUM_LINEA ='" + (String)registro.getDefCampo("NUM_LINEA") + "' \n";
						
					PreparedStatement ps2 = this.conn.prepareStatement(sSql);
					ps2.execute();
					ResultSet rs2 = ps2.getResultSet();
						
					if (rs2.next()){
						sMontoDisponible = rs2.getString("MONTO_DISPONIBLE");	
					}
						
					fMontoDisponible = (Float.parseFloat(sMontoDisponible));
						
					if (fMontoAutorizado <= fMontoDisponible){
							
						fMontoDisponibleFinal = fMontoDisponible - fMontoAutorizado;
							
						sSql = " UPDATE SIM_CAT_FONDEADOR_LINEA SET \n" +
							   " MONTO_DISPONIBLE     		= '" + fMontoDisponibleFinal  + "' \n" +
							   " WHERE NUM_LINEA      	= '" + (String)registro.getDefCampo("NUM_LINEA") + "' \n" +
							   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
							
						PreparedStatement ps3 = this.conn.prepareStatement(sSql);
						ps3.execute();
						ResultSet rs3 = ps3.getResultSet();
							
						sSql = " UPDATE SIM_PRESTAMO SET \n" +
							   " NUM_LINEA     		= '" + (String)registro.getDefCampo("NUM_LINEA")  + "' \n" +
							   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
							
						PreparedStatement ps4 = this.conn.prepareStatement(sSql);
						ps4.execute();
						ResultSet rs4 = ps4.getResultSet();
							
						sSql =  "SELECT \n"+
								"ID_ETAPA_PRESTAMO \n"+
								"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND B_LINEA_FONDEO = 'V' \n";
						ejecutaSql();
						if (rs.next()){
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
						}
					
						sSql =  " SELECT \n"+
								"CVE_FUNCION, \n"+
								"CVE_APLICACION, \n"+
								"CVE_PERFIL \n"+
								"FROM RS_CONF_APLICACION_CONFIG \n"+
								"WHERE CVE_FUNCION = (SELECT \n"+
								"CVE_FUNCION \n"+
								"FROM \n"+
								"SIM_CAT_ETAPA_PRESTAMO \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
								") \n"+
								"AND CVE_APLICACION = 'Prestamo' \n"+
								"AND CVE_PERFIL = ( \n"+
								"SELECT \n"+
								"CVE_PERFIL \n"+ 
								"FROM RS_GRAL_USUARIO_PERFIL \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
								"AND CVE_APLICACION = 'Prestamo') \n";
						PreparedStatement ps5 = this.conn.prepareStatement(sSql);
						ps5.execute();
						ResultSet rs5 = ps5.getResultSet();
						if(rs5.next()){
							//Avanza la etapa.
							//Obtiene las actividades de la etapa que se completa.
							sSql =  "SELECT \n"+
									"CVE_GPO_EMPRESA, \n" +
									"CVE_EMPRESA, \n" +
									"ID_ETAPA_PRESTAMO, \n"+
									"ID_ACTIVIDAD_REQUISITO, \n" +
									"FECHA_REGISTRO, \n" +
									"ORDEN_ETAPA \n" +
									"FROM \n" +
									"SIM_PRESTAMO_ETAPA \n" +
									"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
									"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
							
							PreparedStatement ps6 = this.conn.prepareStatement(sSql);
							ps6.execute();
							ResultSet rs6 = ps6.getResultSet();
							
							while (rs6.next()){
								registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs6.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs6.getString("ID_ACTIVIDAD_REQUISITO"));
								registro.addDefCampo("ORDEN_ETAPA",rs6.getString("ORDEN_ETAPA")== null ? "": rs6.getString("ORDEN_ETAPA"));
								registro.addDefCampo("FECHA_REGISTRO",rs6.getString("FECHA_REGISTRO")== null ? "": rs6.getString("FECHA_REGISTRO"));
								//Guarda el historial de la etapa y las actividades
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
								PreparedStatement ps8 = this.conn.prepareStatement(sSql);
								ps8.execute();
								ResultSet rs8 = ps8.getResultSet();
								if (rs8.next()){
									registro.addDefCampo("ID_HISTORICO",rs8.getString("ID_HISTORICO"));
									
									sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
										"CVE_GPO_EMPRESA, \n" +
										"CVE_EMPRESA, \n" +
										"ID_HISTORICO, \n" +
										"ID_PRESTAMO, \n" +
										"ID_ACTIVIDAD_REQUISITO, \n" +
										"ID_ETAPA_PRESTAMO, \n"+
										"FECHA_REALIZADA, \n"+
										"FECHA_REGISTRO, \n"+
										"ESTATUS, \n"+
										"CVE_USUARIO, \n"+
										"ORDEN_ETAPA) \n"+
										" VALUES (" +
										"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
										"SYSDATE, \n"+
										"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
										"'Completada', \n"+
										"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
										"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
									
									if (ejecutaUpdate() == 0){
										resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
									}
								}
							}
							
							sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO \n"+
								"	  FROM SIM_PRESTAMO_ETAPA \n"+
								"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
								"	  ORDEN_ETAPA + 1 \n"+
								" 	  FROM SIM_PRESTAMO_ETAPA \n"+
								"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n";
							PreparedStatement ps9 = this.conn.prepareStatement(sSql);
							ps9.execute();
							ResultSet rs9 = ps9.getResultSet();
							
							if(rs9.next()){
							
								//Pasa a la siguiente etapa.
								registro.addDefCampo("ID_ETAPA_PRESTAMO",rs9.getString("ID_ETAPA_PRESTAMO"));
							
								sSql =  " UPDATE SIM_PRESTAMO SET \n"+
										"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
										"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
										"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
								
								PreparedStatement ps10 = this.conn.prepareStatement(sSql);
								ps10.execute();
								ResultSet rs10 = ps10.getResultSet();
								
								sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
										" FECHA_REGISTRO 		=SYSDATE, \n" +
										" ESTATUS 	 		='Registrada', \n" +
										" COMENTARIO 	 		='' \n" +
										" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
										" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
										" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
										" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
								
								PreparedStatement ps11 = this.conn.prepareStatement(sSql);
								ps11.execute();
								ResultSet rs11 = ps11.getResultSet();
								
								
								sSql =  "SELECT \n"+
										"CVE_GPO_EMPRESA, \n"+
										"CVE_EMPRESA, \n"+
										"ID_PRESTAMO, \n"+
										"ID_ACTIVIDAD_REQUISITO, \n"+
										"ID_ETAPA_PRESTAMO, \n"+
										"FECHA_REGISTRO, \n"+
										"FECHA_REALIZADA, \n"+
										"COMENTARIO, \n"+
										"ORDEN_ETAPA, \n"+
										"ESTATUS \n" +
										"FROM \n"+
										"SIM_PRESTAMO_ETAPA \n"+
										"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
										"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
										"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
							
								PreparedStatement ps12 = this.conn.prepareStatement(sSql);
								ps12.execute();
								ResultSet rs12 = ps12.getResultSet();
									
								while (rs12.next()){
									registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs12.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs12.getString("ID_ACTIVIDAD_REQUISITO"));
									registro.addDefCampo("ID_ETAPA_PRESTAMO",rs12.getString("ID_ETAPA_PRESTAMO")== null ? "": rs12.getString("ID_ETAPA_PRESTAMO"));
									registro.addDefCampo("ORDEN_ETAPA",rs12.getString("ORDEN_ETAPA")== null ? "": rs12.getString("ORDEN_ETAPA"));
									registro.addDefCampo("FECHA_REGISTRO",rs12.getString("FECHA_REGISTRO")== null ? "": rs12.getString("FECHA_REGISTRO"));
									registro.addDefCampo("COMENTARIO",rs12.getString("COMENTARIO")== null ? "": rs12.getString("COMENTARIO"));
									registro.addDefCampo("ESTATUS",rs12.getString("ESTATUS")== null ? "": rs12.getString("ESTATUS"));
										
									sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
										
									PreparedStatement ps13 = this.conn.prepareStatement(sSql);
									ps13.execute();
									ResultSet rs13 = ps13.getResultSet();	
									if (rs13.next()){
										registro.addDefCampo("ID_HISTORICO",rs13.getString("ID_HISTORICO"));
										//Actualiza la bitácora.
										sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
												"CVE_GPO_EMPRESA, \n" +
												"CVE_EMPRESA, \n" +
												"ID_HISTORICO, \n" +
												"ID_PRESTAMO, \n" +
												"ID_ACTIVIDAD_REQUISITO, \n" +
												"ID_ETAPA_PRESTAMO, \n"+
												"FECHA_REGISTRO, \n"+
												"ORDEN_ETAPA, \n"+
												"COMENTARIO, \n"+
												"ESTATUS) \n" +
												" VALUES (" +
												"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
												"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
												"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
												"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
												"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
												"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
												"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
											
										PreparedStatement ps14 = this.conn.prepareStatement(sSql);
										ps14.execute();
										ResultSet rs14 = ps14.getResultSet();
									}
								}
							}
							
						}
					
							
						}else if (fMontoAutorizado > fMontoDisponible){
							
							resultadoCatalogo.mensaje.setClave("LINEA_NO_MONTO_DISPONIBLE");
						}
				}else { 
					//Mensaje *
				}
			}
				
		}//Termina de asignar las líneas de fondeo a los créditos.
		
		return resultadoCatalogo;
	}

}