/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.comite.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.sim.prestamo.datos.SimGenerarTablaAmortizacionDAO;
import com.rapidsist.sim.prestamo.datos.SimPrestamoFechaDesembolsoDAO;


import java.util.LinkedList;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para comites.
 */
 
public class SimComitePrestamoMontoAutorizadoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("PRESTAMO").equals("Grupo")) {
			sSql =  "SELECT \n"+
					   "M.CVE_GPO_EMPRESA, \n" +
					   "M.CVE_EMPRESA, \n" +
					   "M.ID_PRESTAMO_GRUPO, \n"+
					   "M.ID_PRESTAMO, \n"+
					   "M.ID_INTEGRANTE ID_CLIENTE, \n"+
					   "PER.NOM_COMPLETO, \n"+
					   "M.MONTO_SOLICITADO, \n"+
					   "M.MONTO_AUTORIZADO, \n"+
					   "PG.MONTO_MINIMO, \n"+
					   "PG.MONTO_MAXIMO \n"+
					" FROM SIM_PRESTAMO_GPO_DET M, \n"+
					"		RS_GRAL_PERSONA PER, \n"+
					"       SIM_PRESTAMO_GRUPO PG \n"+
					" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND M.ID_PRESTAMO_GRUPO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					" AND PER.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
					" AND PER.CVE_EMPRESA = M.CVE_EMPRESA \n"+
					" AND PER.ID_PERSONA = M.ID_INTEGRANTE \n"+
					" AND M.ID_ETAPA_PRESTAMO != '18' \n"+
					" AND PG.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
					" AND PG.CVE_EMPRESA = M.CVE_EMPRESA \n"+
					" AND PG.ID_PRESTAMO_GRUPO = M.ID_PRESTAMO_GRUPO \n";
					
			
		}else if (parametros.getDefCampo("PRESTAMO").equals("Individual")) {
			sSql =  "SELECT \n"+
					   "M.CVE_GPO_EMPRESA, \n" +
					   "M.CVE_EMPRESA, \n" +
					   "M.ID_PRESTAMO, \n"+
					   "M.ID_CLIENTE, \n"+
					   "PER.NOM_COMPLETO, \n"+
					   "M.MONTO_SOLICITADO, \n"+
					   "M.MONTO_AUTORIZADO, \n"+
					   "P.MONTO_MINIMO, \n"+
					   "P.MONTO_MAXIMO \n"+
					" FROM SIM_CLIENTE_MONTO M, \n"+
					"		RS_GRAL_PERSONA PER, \n"+
					"       SIM_PRESTAMO P \n"+
					" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND M.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					" AND PER.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
					" AND PER.CVE_EMPRESA = M.CVE_EMPRESA \n"+
					" AND PER.ID_PERSONA = M.ID_CLIENTE \n"+
					" AND P.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
					" AND P.CVE_EMPRESA = M.CVE_EMPRESA \n"+
					" AND P.ID_PRESTAMO = M.ID_PRESTAMO \n";
			
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
		
		if (parametros.getDefCampo("PRESTAMO").equals("Grupo")){
			sSql =  "SELECT DISTINCT \n"+
					   "P.CVE_GPO_EMPRESA, \n" +
					   "P.CVE_EMPRESA, \n" +
					   "PG.ID_PERIODICIDAD_PRODUCTO, \n"+
					   "PG.ID_ETAPA_PRESTAMO, \n"+
					   "PG.MONTO_MAXIMO, \n"+
					   "PG.MONTO_MINIMO, \n"+
					   "PG.DIA_SEMANA_PAGO, \n"+
					   "PG.FECHA_ENTREGA, \n"+
					   "E.B_AUTORIZAR_COMITE \n"+
					" FROM SIM_PRESTAMO_GPO_DET P, \n"+
					" 	   SIM_PRESTAMO_GRUPO PG, \n"+
					"	   SIM_CAT_ETAPA_PRESTAMO E \n"+
					" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND P.ID_PRESTAMO_GRUPO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					" AND PG.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					" AND PG.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					" AND PG.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n"+
					" AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					" AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					" AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n";
				
		}else if (parametros.getDefCampo("PRESTAMO").equals("Individual")){
			sSql =  "SELECT \n"+
					   "P.CVE_GPO_EMPRESA, \n" +
					   "P.CVE_EMPRESA, \n" +
					   "P.ID_PERIODICIDAD_PRODUCTO, \n"+
					   "P.ID_ETAPA_PRESTAMO, \n"+
					   "P.MONTO_MAXIMO, \n"+
					   "P.MONTO_MINIMO, \n"+
					   "P.DIA_SEMANA_PAGO, \n"+
					   "P.FECHA_ENTREGA, \n"+
					   "E.B_AUTORIZAR_COMITE \n"+
					" FROM SIM_PRESTAMO P, \n"+
					"	   SIM_CAT_ETAPA_PRESTAMO E \n"+
					" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND P.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					" AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					" AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					" AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n";
			
		}
			
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
		
		String sFechaEntrega = (String)registro.getDefCampo("FECHA_ENTREGA");
		String sOperaSabado = "";
		String sOperaDomingo = "";
		String sOperaDiaFestivo = "";
		
		boolean bDiaFestivo = false;
		String sDiaEntrega = "";
		int iDiaEntrega = 0;
		int iDiaSemanaPago = 0;
		boolean bBuscaFechaValida = true;
		
		String sFechaDesembolsoValida = ""; 
		String sFechaReal = "";
		String sFechaRealValida = "";
		
		ResultadoCatalogo resultadoCatalogoGenerarTablaAmortizacion = new ResultadoCatalogo();
		SimGenerarTablaAmortizacionDAO simGenerarTablaAmortizacionDAO = new SimGenerarTablaAmortizacionDAO();
		simGenerarTablaAmortizacionDAO.setConexion(this.getConexion());
		

		String[] sMontos = (String[]) registro.getDefCampo("DAO_MONTOS");
		String[] sIdCliente = (String[]) registro.getDefCampo("DAO_CLIENTE");
		String[] sIdPrestamoIndividual = (String[]) registro.getDefCampo("DAO_ID_PRESTAMO_IND");

		String sMontoAutorizado = new String();
		String sCliente = new String();
		String sPrestamoIndividual = new String();

		for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
	
			sMontoAutorizado = sMontos[iNumParametro];
			sCliente = sIdCliente[iNumParametro];
			sPrestamoIndividual = sIdPrestamoIndividual[iNumParametro];
			registro.addDefCampo("MONTO_AUTORIZADO",sMontoAutorizado);
			registro.addDefCampo("ID_CLIENTE",sCliente);
			registro.addDefCampo("ID_PRESTAMO_INDIVIDUAL",sPrestamoIndividual);
		
			if (registro.getDefCampo("PRESTAMO").equals("Grupo")){
				
				sSql = " UPDATE SIM_PRESTAMO_GPO_DET SET "+
						" MONTO_AUTORIZADO = '" + (String)registro.getDefCampo("MONTO_AUTORIZADO") + "' \n" +
						" WHERE ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
						" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
						" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
		
				sSql = "UPDATE SIM_CLIENTE_MONTO SET \n"+
						" MONTO_AUTORIZADO = '" + (String)registro.getDefCampo("MONTO_AUTORIZADO") + "' \n" +
				
						" WHERE ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
						" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
			
				sSql = "SELECT \n"+
						"ID_ETAPA_PRESTAMO \n"+
						"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND B_AUTORIZAR_COMITE = 'V' \n";
				ejecutaSql();
				if (rs.next()){
					registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
				}
			
				sSql = " SELECT \n"+
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
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();
				if(rs3.next()){
		
					//Avanza la etapa.
					//Obtiene las actividades de la etapa que se completa.
					sSql = "SELECT \n"+
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
					
					PreparedStatement ps4 = this.conn.prepareStatement(sSql);
					ps4.execute();
					ResultSet rs4 = ps4.getResultSet();
				
					while (rs4.next()){
						registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs4.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs4.getString("ID_ACTIVIDAD_REQUISITO"));
						registro.addDefCampo("ORDEN_ETAPA",rs4.getString("ORDEN_ETAPA")== null ? "": rs4.getString("ORDEN_ETAPA"));
						registro.addDefCampo("FECHA_REGISTRO",rs4.getString("FECHA_REGISTRO")== null ? "": rs4.getString("FECHA_REGISTRO"));
						//Guarda el historial de la etapa y las actividades
						sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
						PreparedStatement ps5 = this.conn.prepareStatement(sSql);
						ps5.execute();
						ResultSet rs5 = ps5.getResultSet();
						if (rs5.next()){
							registro.addDefCampo("ID_HISTORICO",rs5.getString("ID_HISTORICO"));
					
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
							
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
						}
					}
			
					sSql = " SELECT DISTINCT ID_ETAPA_PRESTAMO \n"+
							" FROM SIM_PRESTAMO_ETAPA \n"+
							" WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
							" ORDEN_ETAPA + 1 \n"+
							" FROM SIM_PRESTAMO_ETAPA \n"+
							" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n"+
							" AND ID_ETAPA_PRESTAMO = ( SELECT \n"+
							" ID_ETAPA_PRESTAMO \n"+
							" FROM SIM_CAT_ETAPA_PRESTAMO \n"+
							" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO = ( \n"+
							" SELECT DISTINCT D.ID_ETAPA_PRESTAMO \n" +
							" FROM SIM_PRESTAMO_GPO_DET D, \n" +
							" SIM_PRESTAMO_ETAPA E \n" +
							" WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							" AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
							" AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
							" AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" +
							" AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
							" AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO \n" +
							" AND E.ORDEN_ETAPA = ( \n" +
							" SELECT MIN(ORDEN_ETAPA) FROM \n" +
							" (SELECT D.ID_ETAPA_PRESTAMO, E.ORDEN_ETAPA \n" +
							" FROM SIM_PRESTAMO_GPO_DET D, \n" +
							" SIM_PRESTAMO_ETAPA E \n" +
							" WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							" AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
							" AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
							" AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" +
							" AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
							" AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO))))) \n"+
							" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n";
					
					PreparedStatement ps6 = this.conn.prepareStatement(sSql);
					ps6.execute();
					ResultSet rs6 = ps6.getResultSet();
			
					if(rs6.next()){
			
						//Pasa a la siguiente etapa.
						registro.addDefCampo("ETAPA_PRESTAMO",rs6.getString("ID_ETAPA_PRESTAMO"));
				
						sSql = " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
						"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" ;
				
						PreparedStatement ps7 = this.conn.prepareStatement(sSql);
						ps7.execute();
						ResultSet rs7 = ps7.getResultSet();
					}
			
					sSql = " SELECT DISTINCT ID_ETAPA_PRESTAMO, \n"+
							" ORDEN_ETAPA \n"+
							" FROM SIM_PRESTAMO_ETAPA \n"+
							" WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
							" ORDEN_ETAPA + 1 \n"+
							" FROM SIM_PRESTAMO_ETAPA \n"+
							" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n"+
							" AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
							" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n";
					
					PreparedStatement ps8 = this.conn.prepareStatement(sSql);
					ps8.execute();
					ResultSet rs8 = ps8.getResultSet();
					if(rs8.next()){
						registro.addDefCampo("ID_ETAPA_PRESTAMO",rs8.getString("ID_ETAPA_PRESTAMO"));
						sSql = " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
								"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" ;
						
						PreparedStatement ps9 = this.conn.prepareStatement(sSql);
						ps9.execute();
						ResultSet rs9 = ps9.getResultSet();
				
						sSql = " UPDATE SIM_PRESTAMO SET \n"+
								"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" ;
						
						PreparedStatement ps10 = this.conn.prepareStatement(sSql);
						ps10.execute();
						ResultSet rs10 = ps10.getResultSet();
				
						sSql = " UPDATE SIM_PRESTAMO_ETAPA SET "+
								" FECHA_REGISTRO =SYSDATE, \n" +
								" ESTATUS ='Registrada', \n" +
								" COMENTARIO ='' \n" +
								" WHERE ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
								" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								" AND ID_ETAPA_PRESTAMO ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
						
						PreparedStatement ps11 = this.conn.prepareStatement(sSql);
						ps11.execute();
						ResultSet rs11 = ps11.getResultSet();
				
				
						sSql = "SELECT \n"+
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
								//Actualiza la bit�cora.
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
				
				
				
						sSql = " UPDATE SIM_PRESTAMO_GPO_DET SET "+
						" COMENTARIO ='' \n"+
						" WHERE ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n"+
						" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						" AND ID_ETAPA_PRESTAMO ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
						" AND ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n";
						PreparedStatement ps15 = this.conn.prepareStatement(sSql);
						ps15.execute();
						ResultSet rs15 = ps15.getResultSet();
			
					}
			
					}
			
			
				}else if (registro.getDefCampo("PRESTAMO").equals("Individual")){
		
					sSql = "UPDATE SIM_CLIENTE_MONTO SET \n"+
							" MONTO_AUTORIZADO = '" + (String)registro.getDefCampo("MONTO_AUTORIZADO") + "' \n" +
							" WHERE ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
							" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
			
					sSql = "SELECT \n"+
							"ID_ETAPA_PRESTAMO \n"+
							"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND B_AUTORIZAR_COMITE = 'V' \n";
					ejecutaSql();
					if (rs.next()){
						registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
					}
			
					sSql = " SELECT \n"+
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
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					if(rs3.next()){
						//Avanza la etapa.
						//Obtiene las actividades de la etapa que se completa.
						sSql = "SELECT \n"+
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
						
						PreparedStatement ps4 = this.conn.prepareStatement(sSql);
						ps4.execute();
						ResultSet rs4 = ps4.getResultSet();
				
						while (rs4.next()){
							registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs4.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs4.getString("ID_ACTIVIDAD_REQUISITO"));
							registro.addDefCampo("ORDEN_ETAPA",rs4.getString("ORDEN_ETAPA")== null ? "": rs4.getString("ORDEN_ETAPA"));
							registro.addDefCampo("FECHA_REGISTRO",rs4.getString("FECHA_REGISTRO")== null ? "": rs4.getString("FECHA_REGISTRO"));
							//Guarda el historial de la etapa y las actividades
							sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
							PreparedStatement ps5 = this.conn.prepareStatement(sSql);
							ps5.execute();
							ResultSet rs5 = ps5.getResultSet();
							if (rs5.next()){
								registro.addDefCampo("ID_HISTORICO",rs5.getString("ID_HISTORICO"));
						
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
						
								if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
								}
							}
						}
				
						sSql = " SELECT DISTINCT ID_ETAPA_PRESTAMO \n"+
								" FROM SIM_PRESTAMO_ETAPA \n"+
								" WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
								" ORDEN_ETAPA + 1 \n"+
								" FROM SIM_PRESTAMO_ETAPA \n"+
								" WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n"+
								" AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
								" AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n";
						
						PreparedStatement ps6 = this.conn.prepareStatement(sSql);
						ps6.execute();
						ResultSet rs6 = ps6.getResultSet();
				
						if(rs6.next()){
				
							//Pasa a la siguiente etapa.
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs6.getString("ID_ETAPA_PRESTAMO"));
					
							sSql = " UPDATE SIM_PRESTAMO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" ;
					
							PreparedStatement ps7 = this.conn.prepareStatement(sSql);
							ps7.execute();
							ResultSet rs7 = ps7.getResultSet();
					
							sSql = " UPDATE SIM_PRESTAMO_ETAPA SET "+
							" FECHA_REGISTRO =SYSDATE, \n" +
							" ESTATUS ='Registrada', \n" +
							" COMENTARIO ='' \n" +
							" WHERE ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n" +
							" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
					
							PreparedStatement ps8 = this.conn.prepareStatement(sSql);
							ps8.execute();
							ResultSet rs8 = ps8.getResultSet();
					
					
							sSql = "SELECT \n"+
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
							"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
					
							PreparedStatement ps9 = this.conn.prepareStatement(sSql);
							ps9.execute();
							ResultSet rs9 = ps9.getResultSet();
					
							while (rs9.next()){
								registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs9.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs9.getString("ID_ACTIVIDAD_REQUISITO"));
								registro.addDefCampo("ID_ETAPA_PRESTAMO",rs9.getString("ID_ETAPA_PRESTAMO")== null ? "": rs9.getString("ID_ETAPA_PRESTAMO"));
								registro.addDefCampo("ORDEN_ETAPA",rs9.getString("ORDEN_ETAPA")== null ? "": rs9.getString("ORDEN_ETAPA"));
								registro.addDefCampo("FECHA_REGISTRO",rs9.getString("FECHA_REGISTRO")== null ? "": rs9.getString("FECHA_REGISTRO"));
								registro.addDefCampo("COMENTARIO",rs9.getString("COMENTARIO")== null ? "": rs9.getString("COMENTARIO"));
								registro.addDefCampo("ESTATUS",rs9.getString("ESTATUS")== null ? "": rs9.getString("ESTATUS"));
						
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
						
								PreparedStatement ps10 = this.conn.prepareStatement(sSql);
								ps10.execute();
								ResultSet rs10 = ps10.getResultSet();
								if (rs10.next()){
									registro.addDefCampo("ID_HISTORICO",rs10.getString("ID_HISTORICO"));
									//Actualiza la bit�cora.
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
									"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
									"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
									"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
									"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
									"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
							
									PreparedStatement ps11 = this.conn.prepareStatement(sSql);
									ps11.execute();
									ResultSet rs11 = ps11.getResultSet();
								}
							}
						}
			
					}
				}
			}
		

		
		sSql =  " SELECT \n" +
				" B_OPERA_SABADO, \n" +
				" B_OPERA_DOMINGO, \n" +
				" B_OPERA_DIA_FESTIVO \n" +
				" FROM PFIN_PARAMETRO \n" ;
		ejecutaSql();
		if (rs.next()){
			sOperaSabado = rs.getString("B_OPERA_SABADO");
			sOperaDomingo = rs.getString("B_OPERA_DOMINGO");
			sOperaDiaFestivo = rs.getString("B_OPERA_DIA_FESTIVO");
		}
		
		while (bBuscaFechaValida){
			
			sSql = " SELECT \n" +
			   " F_DIA_FESTIVO \n" +
			   " FROM PFIN_DIA_FESTIVO \n" +
			   " WHERE F_DIA_FESTIVO      	= TO_DATE('" + sFechaEntrega + "','DD/MM/YYYY') \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_PAIS 			= 'MX' \n";		
			ejecutaSql();
			System.out.println("es dia festivo? "+sSql);
			if (rs.next()){
				bDiaFestivo = true;
			}else {
				bDiaFestivo = false;
			}
			System.out.println("bDiaFestivo"+bDiaFestivo);
			sSql = " SELECT TO_CHAR(TO_DATE('" + sFechaEntrega + "','DD/MM/YYYY'),'D') DIA FROM DUAL \n" ;
			ejecutaSql();
			if (rs.next()){
				sDiaEntrega = rs.getString("DIA");
			}
			System.out.println("sDiaEntrega "+sDiaEntrega);
			
			
			if (sDiaEntrega.equals("6") || sDiaEntrega.equals("7") || bDiaFestivo){
				System.out.println("Se trata de unn domingo sabado o dia festivo");
				if (sOperaDomingo.equals("V") && sDiaEntrega.equals("6")){
					bBuscaFechaValida = false;
				}else if (sOperaSabado.equals("V") && sDiaEntrega.equals("7")){
					bBuscaFechaValida = false;
				}else if (sOperaDiaFestivo.equals("V") && bDiaFestivo){
					bBuscaFechaValida = false;
				}else {
					sSql = " SELECT TO_CHAR(TO_DATE('" + sFechaEntrega + "','DD/MM/YYYY') + 1,'DD/MM/YYYY') FECHA_VALIDA FROM DUAL \n" ;
					ejecutaSql();
					System.out.println("la siguiente fecha a analizar es: "+sSql);
					if (rs.next()){
						sFechaEntrega = rs.getString("FECHA_VALIDA");
					}
				}
			}else {
				System.out.println("Se queda igual");
				sFechaEntrega = sFechaEntrega;
				bBuscaFechaValida = false;
			}
		}
		
		sFechaDesembolsoValida = sFechaEntrega;
		System.out.println("Esta funcion hace lo mismo que la de lalo y creo que es mas simple "+sFechaDesembolsoValida);
		iDiaEntrega = Integer.parseInt(sDiaEntrega);
		//Pregunta si es un crédito catorcena o semanal.
		if (!registro.getDefCampo("DIA_SEMANA_PAGO").equals("ninguno")) {
			if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Lunes")){
				iDiaSemanaPago = 1;
			}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Martes")){
				iDiaSemanaPago = 2;
			}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Miercoles")){
				iDiaSemanaPago = 3;
			}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Jueves")){
				iDiaSemanaPago = 4;
			}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Viernes")){
				iDiaSemanaPago = 5;
			}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Sabado")){
				iDiaSemanaPago = 6;
			}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("Domingo")){
				iDiaSemanaPago = 7;
			}
			
			if (iDiaSemanaPago > iDiaEntrega){
				System.out.println("uno");
				int iFactorSuma = iDiaSemanaPago - iDiaEntrega;
			
				sSql = "SELECT TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') + '"+iFactorSuma+"' FECHA_REAL FROM DUAL \n" ;
				System.out.println("la fecha real es :"+sSql);
				ejecutaSql();
				if (rs.next()){
					sFechaReal = rs.getString("FECHA_REAL");
					registro.addDefCampo("FECHA_REAL",rs.getString("FECHA_REAL"));
				}
			}else if (iDiaSemanaPago < iDiaEntrega){
				System.out.println("dos");
				int iFactorSuma = 7 - iDiaEntrega;
				iFactorSuma = iFactorSuma + iDiaSemanaPago;
			
				sSql = "SELECT TO_DATE('" + (String)registro.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') + '"+iFactorSuma+"' FECHA_REAL FROM DUAL \n" ;
				System.out.println("la fecha real es :"+sSql);
				ejecutaSql();
				if (rs.next()){
					sFechaReal = rs.getString("FECHA_REAL");
					registro.addDefCampo("FECHA_REAL",rs.getString("FECHA_REAL")== null ? "": rs.getString("FECHA_REAL"));
				}
			}else if (iDiaSemanaPago == iDiaEntrega){
				System.out.println("tres");
				registro.addDefCampo("FECHA_REAL","");
			}
			
			//Valida que la fecha real obtenida sea valida, si no es así obtiene una que sí lo sea
			bBuscaFechaValida = true;
			sFechaReal = (String)registro.getDefCampo("FECHA_REAL");
			
			
			while (bBuscaFechaValida){
				
				sSql = " SELECT \n" +
				   " F_DIA_FESTIVO \n" +
				   " FROM PFIN_DIA_FESTIVO \n" +
				   " WHERE F_DIA_FESTIVO      	= TO_TIMESTAMP('" + sFechaReal + "','yyyy-MM-dd HH24:MI:SSXFF') \n" +
				   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				   " AND CVE_PAIS 			= 'MX' \n";		
				ejecutaSql();
				System.out.println("es dia festivo? "+sSql);
				if (rs.next()){
					bDiaFestivo = true;
				}else {
					bDiaFestivo = false;
				}
				System.out.println("bDiaFestivo"+bDiaFestivo);
				sSql = " SELECT TO_CHAR(TO_TIMESTAMP('" + sFechaReal + "','yyyy-MM-dd HH24:MI:SSXFF'),'D') DIA FROM DUAL \n" ;
				ejecutaSql();
				if (rs.next()){
					sDiaEntrega = rs.getString("DIA");
				}
				System.out.println("sDiaEntrega "+sDiaEntrega);
				
				
				if (sDiaEntrega.equals("6") || sDiaEntrega.equals("7") || bDiaFestivo){
					System.out.println("Se trata de unn domingo sabado o dia festivo");
					if (sOperaDomingo.equals("V") && sDiaEntrega.equals("6")){
						bBuscaFechaValida = false;
					}else if (sOperaSabado.equals("V") && sDiaEntrega.equals("7")){
						bBuscaFechaValida = false;
					}else if (sOperaDiaFestivo.equals("V") && bDiaFestivo){
						bBuscaFechaValida = false;
					}else {
						sSql = " SELECT TO_CHAR(TO_TIMESTAMP('" + sFechaReal + "','yyyy-MM-dd HH24:MI:SSXFF') + 1,'DD/MM/YYYY') FECHA_VALIDA FROM DUAL \n" ;
						ejecutaSql();
						System.out.println("la siguiente fecha a analizar es: "+sSql);
						if (rs.next()){
							sFechaReal = rs.getString("FECHA_VALIDA");
						}
					}
				}else {
					System.out.println("Se queda igual");
					sFechaReal = sFechaReal;
					bBuscaFechaValida = false;
				}
			}
			
			sFechaRealValida = sFechaReal;
			
			if (registro.getDefCampo("APLICA").equals("Grupo")){
				sSql = " UPDATE SIM_PRESTAMO_GRUPO SET \n" +
					   " FECHA_ENTREGA     		= TO_DATE('" + sFechaDesembolsoValida + "','DD/MM/YYYY'), \n" +
					   " FECHA_REAL     		= TO_TIMESTAMP('" + sFechaReal + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
					   " DIA_SEMANA_PAGO     		= '" + (String)registro.getDefCampo("DIA_SEMANA_PAGO") + "' \n" +
					   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
					   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				System.out.println("2"+sSql);
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				
				sSql = " SELECT \n" +
					   " ID_PRESTAMO \n" +
					   " FROM SIM_PRESTAMO_GPO_DET \n" +
					   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
					   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				System.out.println("3"+sSql);
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
				
				
				while (rs2.next()){
				
					registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO")== null ? "": rs2.getString("ID_PRESTAMO"));
					
					sSql = " UPDATE SIM_PRESTAMO SET \n" +
						   " FECHA_ENTREGA     		= TO_DATE('" + sFechaDesembolsoValida + "','DD/MM/YYYY'), \n" +
						   " FECHA_REAL     		= TO_TIMESTAMP('" + sFechaReal + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
						   " DIA_SEMANA_PAGO     		= '" + (String)registro.getDefCampo("DIA_SEMANA_PAGO") + "' \n" +
						   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					System.out.println("4"+sSql);
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					
					resultadoCatalogoGenerarTablaAmortizacion = simGenerarTablaAmortizacionDAO.alta(registro);
				}
				
				
				
			}else if (registro.getDefCampo("APLICA").equals("Individual")){
				sSql = " UPDATE SIM_PRESTAMO SET \n" +
					   " FECHA_ENTREGA     		= TO_DATE('" + sFechaDesembolsoValida + "','DD/MM/YYYY'), \n" +
					   " FECHA_REAL     		= TO_TIMESTAMP('" + sFechaReal + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
					   " DIA_SEMANA_PAGO     	= '" + (String)registro.getDefCampo("DIA_SEMANA_PAGO") + "' \n" +
					   " WHERE ID_PRESTAMO     	= '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
					   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				resultadoCatalogoGenerarTablaAmortizacion = simGenerarTablaAmortizacionDAO.alta(registro);
			}
			
		}else if (registro.getDefCampo("DIA_SEMANA_PAGO").equals("ninguno")) {
			if (registro.getDefCampo("APLICA").equals("Grupo")){
				//Ingresa la fecha de desembolso al crédito grupal.
				
				sSql = " UPDATE SIM_PRESTAMO_GRUPO SET \n" +
				   " FECHA_ENTREGA     		= TO_DATE('" + sFechaDesembolsoValida + "','DD/MM/YYYY') \n" +
				   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
				   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				
				PreparedStatement ps1 = this.conn.prepareStatement(sSql);
				ps1.execute();
				ResultSet rs1 = ps1.getResultSet();
				
				sSql = " SELECT \n" +
				   " ID_PRESTAMO \n" +
				   " FROM SIM_PRESTAMO_GPO_DET \n" +
				   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
				   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
				
				
				while (rs2.next()){
					
					registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO")== null ? "": rs2.getString("ID_PRESTAMO"));
					
					sSql = " UPDATE SIM_PRESTAMO SET \n" +
						   " FECHA_ENTREGA     		= TO_DATE('" + sFechaDesembolsoValida + "','DD/MM/YYYY') \n" +
						   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					
					resultadoCatalogoGenerarTablaAmortizacion = simGenerarTablaAmortizacionDAO.alta(registro);
				}
				
				
			}else if (registro.getDefCampo("APLICA").equals("Individual")){
				//Ingresa la fecha de desembolso al crédito individual.
				sSql = " UPDATE SIM_PRESTAMO SET \n" +
				   " FECHA_ENTREGA     		= TO_DATE('" + sFechaDesembolsoValida + "','DD/MM/YYYY') \n" +
				   " WHERE ID_PRESTAMO      = '" + (String)registro.getDefCampo("ID_PRESTAMO_IND_GPO") + "' \n" +
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
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql = " UPDATE SIM_PRESTAMO_GPO_DET SET "+
			   " MONTO_AUTORIZADO     		= '" + (String)registro.getDefCampo("MONTO_AUTORIZADO")  + "' \n" +
			   
			   " WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA EL REGISTRO
		sSql = "DELETE FROM SIM_COMITE" +
				" WHERE ID_COMITE		='" + (String)registro.getDefCampo("ID_COMITE") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}