/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para la entrega de prestamos individuales.
 */
 
public class SimCajaDesembolsoIndividualDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionConsultaRegistro {
	
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
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"C.ID_PRESTAMO, \n"+
				"C.CVE_PRESTAMO, \n"+
				"C.CVE_NOMBRE ID_CLIENTE, \n"+
				"C.NOMBRE NOM_COMPLETO, \n"+
				"C.ID_PRODUCTO, \n"+
				"C.NOM_PRODUCTO, \n"+
				"C.NUM_CICLO, \n"+
				"TO_CHAR(C.MONTO_AUTORIZADO,'999,999,999.99') MONTO_PRESTADO \n"+
				"FROM V_CREDITO C, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND C.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND C.APLICA_A = 'INDIVIDUAL' \n"+
				"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+ 
				"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+ 
				"AND E.B_DESEMBOLSO = 'V' \n"+
				"AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
	            "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
	            "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
	            "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + " AND CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(C.NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
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
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+ 
				"P.ID_PRESTAMO, \n"+
				"P.ID_CLIENTE, \n"+
				"P.ID_PRODUCTO, \n"+
				"P.NUM_CICLO, \n"+
				"P.B_ENTREGADO, \n"+
				"M.MONTO_AUTORIZADO, \n"+
				"PE.NOM_COMPLETO \n"+
				"FROM SIM_PRESTAMO P, \n"+
				"SIM_CLIENTE_MONTO M, \n"+
				"RS_GRAL_PERSONA PE \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND P.ID_ETAPA_PRESTAMO = '11' \n"+
				"AND P.B_ENTREGADO != 'V' \n"+
				"AND M.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND M.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND M.ID_PRESTAMO = P.ID_PRESTAMO \n"+
				"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PE.ID_PERSONA = P.ID_CLIENTE \n";
		
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
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdMovimientoOperacion = "";
		int iIdMovimientoOperacion = 0;
		String sMontoCaja = "";
		float fMontoCaja = 0;
		String sMonto = "";
		float fMonto = 0;
		
		sSql =  "SELECT \n" +
				"SUM(MONTO) MONTO_CAJA \n" +
				"FROM \n" +
				"SIM_CAJA_TRANSACCION \n" +
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_CAJA = '" + (String)registro.getDefCampo("ID_CAJA") + "' \n"+
				"AND ID_SUCURSAL = '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n";
		ejecutaSql();
		if (rs.next()){
			sMontoCaja = rs.getString("MONTO_CAJA");
			if (sMontoCaja == null){
				sMontoCaja = "0";
			}
			fMontoCaja = Float.parseFloat(sMontoCaja.trim());
		}
		
		sSql =  "SELECT \n"+
				"C.MONTO_AUTORIZADO MONTO_PRESTADO \n"+
				"FROM V_CREDITO C \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND C.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND C.ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND C.APLICA_A = 'INDIVIDUAL' \n"+
				"AND C.ID_ETAPA_PRESTAMO != '18' \n";
		ejecutaSql();
		if (rs.next()){
			sMonto = rs.getString("MONTO_PRESTADO");
			fMonto = Float.parseFloat(sMonto.trim());
		}
		
		if (fMontoCaja < fMonto){
			//La caja no tiene suficientes fondos para hacer el retiro.
			resultadoCatalogo.Resultado.addDefCampo("ID_MOVIMIENTO_OPERACION", "NO_FONDOS");
			System.out.println("no tiene fondos");
		}else {
			
			sSql = "SELECT SQ01_SIM_CAJA_TRANSACCION.nextval as ID_TRANSACCION FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				registro.addDefCampo("ID_TRANSACCION", rs.getString("ID_TRANSACCION"));
			}
		
			sSql =  "SELECT \n" +
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRESTAMO, \n" +
					"ID_PRODUCTO, \n" +
					"NUM_CICLO, \n" +
					"ID_CLIENTE \n" +
					"FROM \n" +
					"SIM_PRESTAMO \n" +
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
			ejecutaSql();
			if (rs.next()){
				registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO")== null ? "": rs.getString("ID_PRESTAMO"));
				registro.addDefCampo("ID_PRODUCTO",rs.getString("ID_PRODUCTO")== null ? "": rs.getString("ID_PRODUCTO"));
				registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
				registro.addDefCampo("ID_CLIENTE",rs.getString("ID_CLIENTE")== null ? "": rs.getString("ID_CLIENTE"));
			}
			
			//OBTENEMOS EL SEQUENCE
			sSql =  "SELECT \n" +
					"CVE_GPO_EMPRESA, \n" +
					"MAX(ID_MOVIMIENTO_OPERACION) ID_MOVIMIENTO_OPERACION \n" +
					"FROM \n" +
					"SIM_CAJA_TRANSACCION \n" +
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
					"GROUP BY CVE_GPO_EMPRESA \n";
			ejecutaSql();
			if (rs.next()){
				sIdMovimientoOperacion = rs.getString("ID_MOVIMIENTO_OPERACION");
				iIdMovimientoOperacion=Integer.parseInt(sIdMovimientoOperacion.trim());
				iIdMovimientoOperacion ++;
				sIdMovimientoOperacion= String.valueOf(iIdMovimientoOperacion);
			}else {
				sIdMovimientoOperacion = "1";
			}
			
			sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_MOVIMIENTO_OPERACION, \n" +
					"ID_TRANSACCION, \n" +
					"CVE_MOVIMIENTO_CAJA, \n" +
					"ID_PRESTAMO, \n" +
					"ID_PRODUCTO, \n" +
					"NUM_CICLO, \n" +
					"ID_CLIENTE, \n" +
					"ID_SUCURSAL, \n" +
					"ID_CAJA, \n" +
					"MONTO, \n" +
					"FECHA_TRANSACCION, \n" +
					"CVE_USUARIO_CAJERO) \n" +
			        "VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdMovimientoOperacion + ", \n "+
					"'" + (String)registro.getDefCampo("ID_TRANSACCION") + "', \n" +
					"'DESIND', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
					"-" + sMonto + ", \n" +
					"SYSDATE, \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "') \n" ;
			
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			resultadoCatalogo.Resultado.addDefCampo("ID_MOVIMIENTO_OPERACION", sIdMovimientoOperacion);
		
			//
			sSql =  "SELECT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND B_DESEMBOLSO = 'V' \n";
			ejecutaSql();
			if (rs.next()){
				registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
			}
	
			
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
				"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
				"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
				
				PreparedStatement ps9 = this.conn.prepareStatement(sSql);
				ps9.execute();
				ResultSet rs9 = ps9.getResultSet();
			
				if(rs9.next()){
				
					//Pasa a la siguiente etapa.
					registro.addDefCampo("ID_ETAPA_PRESTAMO",rs9.getString("ID_ETAPA_PRESTAMO"));
				
					sSql =  " UPDATE SIM_PRESTAMO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n"+
							"B_ENTREGADO = 'V' \n"+
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
		return resultadoCatalogo;
	}
}