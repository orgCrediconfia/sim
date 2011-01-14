/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;

import java.util.HashMap;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.rapidsist.prestamo.Grupo;

/**
 * Administra los accesos a la base de datos para dar de alta el grupo al que se le va a 
 * otorgar el pr�stamo grupal, validando el m�nimo de porcentajes fundadores del grupo,
 * as� como que se cumplan los par�metros globales del grupo (M�ximo de integrantes,
 * m�nimo de integrantes, m�ximo de integrantes en riesgo, m�ximo de negocios ambulates y
 * m�ximo de negocios por cat�logos).
 */
 
public class SimPrestamoGrupalDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
					"P.ID_PRESTAMO_GRUPO, \n"+
					"P.CVE_PRESTAMO_GRUPO, \n"+
					//"P.FECHA_SOLICITUD, \n"+
					//"P.FECHA_ENTREGA, \n"+
					"P.ID_GRUPO, \n"+
					"G.NOM_GRUPO, \n"+
					"P.ID_PRODUCTO, \n"+
					"P.NUM_CICLO, \n"+
					"P.ID_ETAPA_PRESTAMO, \n"+
					"E.NOM_ESTATUS_PRESTAMO \n"+
				"FROM \n"+
					"(SELECT * FROM SIM_PRESTAMO_GRUPO ORDER BY ID_PRESTAMO_GRUPO) P, \n"+
					"SIM_GRUPO G, \n" +
					"SIM_CAT_ETAPA_PRESTAMO E, \n" +
					"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRODUCTO IS NOT NULL \n"+
				"AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
				"AND G.ID_GRUPO (+)= P.ID_GRUPO \n"+
				"AND E.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO (+)= P.ID_ETAPA_PRESTAMO \n"+
				"AND US.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
                "AND US.CVE_EMPRESA = G.CVE_EMPRESA \n"+
                "AND US.ID_SUCURSAL = G.ID_SUCURSAL \n"+
                "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				"AND P.ID_GRUPO IS NOT NULL \n"+
				"AND ROWNUM <= 100 \n";
				
			if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
				sSql = sSql + "AND P.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
			}
			if (parametros.getDefCampo("ID_PRODUCTO") != null) {
				sSql = sSql + "AND P.ID_PRODUCTO = '" + (String) parametros.getDefCampo("ID_PRODUCTO") + "' \n";
			}
			if (parametros.getDefCampo("NUM_CICLO") != null) {
				sSql = sSql + "AND P.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
			}
			if (parametros.getDefCampo("FECHA_SOLICITUD") != null) {
				sSql = sSql + "AND P.FECHA_SOLICITUD >= TO_DATE('" + (String) parametros.getDefCampo("FECHA_SOLICITUD") + "','DD/MM/YYYY') \n";
			}
			if (parametros.getDefCampo("FECHA_ENTREGA") != null) {
				sSql = sSql + "AND P.FECHA_ENTREGA <= TO_DATE('" + (String) parametros.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n";
			}
			if (parametros.getDefCampo("NOM_GRUPO") != null) {
				sSql = sSql + "AND UPPER(G.NOM_GRUPO) LIKE '%" + ((String) parametros.getDefCampo("NOM_GRUPO")).toUpperCase() + "%' \n";
			}
		
			if (parametros.getDefCampo("ID_ETAPA_PRESTAMO") != null) {
				sSql = sSql + "AND P.ID_ETAPA_PRESTAMO = '" + (String) parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
			}
			
			sSql = sSql + "ORDER BY P.CVE_PRESTAMO_GRUPO \n";
			
			System.out.println("GERarDO 1"+sSql);
			
		ejecutaSql();
		return getConsultaLista();
		
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		sSql = " SELECT \n"+
				"PG.CVE_GPO_EMPRESA, \n"+
				"PG.CVE_EMPRESA, \n"+
				"PG.ID_PRESTAMO_GRUPO, \n"+
				"PG.CVE_PRESTAMO_GRUPO, \n"+
				"PG.ID_GRUPO, \n"+
				"G.ID_SUCURSAL, \n"+
				"S.TASA_IVA, \n"+
				"PG.ID_PRODUCTO, \n"+ 
				"PG.NUM_CICLO, \n"+
				"PG.ID_ETAPA_PRESTAMO, \n"+
				"PG.PLAZO, \n"+
				"PG.FECHA_ENTREGA, \n"+
				"PG.FECHA_REAL, \n"+
				"PG.ID_PERIODICIDAD_PRODUCTO ID_PERIODICIDAD, \n"+
				"PG.ID_PERIODICIDAD_PRODUCTO, \n"+
				"G.NOM_GRUPO, \n"+
				"PG.TIPO_TASA, \n"+
				"E.B_ENTREGADO, \n"+
				"E.B_CANCELADO, \n"+
				
				"DECODE(PG.ID_PERIODICIDAD_TASA,NULL,(SELECT ID_PERIODICIDAD \n" +
				"		FROM SIM_CAT_TASA_REFERENCIA \n" +
				"		WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA \n" +
				"									FROM SIM_PRESTAMO_GRUPO \n" +
				"									WHERE ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "')),PG.ID_PERIODICIDAD_TASA) ID_PERIODICIDAD_TASA, \n" +
				"DECODE(PG.VALOR_TASA,NULL,(SELECT VALOR  \n" +
				"		FROM SIM_CAT_TASA_REFER_DETALLE \n" +
				"		WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA \n" +
				"									FROM SIM_PRESTAMO_GRUPO \n" +
				"									WHERE ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "') \n" +
				"		AND FECHA_PUBLICACION = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA \n" +
				"								FROM (SELECT FECHA_PUBLICACION \n" +
				"								FROM SIM_CAT_TASA_REFER_DETALLE \n" +
				"								WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA \n" +
										"									FROM SIM_PRESTAMO_GRUPO \n" +
										"									WHERE ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "') \n" +
				"				AND FECHA_PUBLICACION < SYSDATE))),PG.VALOR_TASA) VALOR_TASA, \n" +
				
				
				"PG.ID_TASA_REFERENCIA, \n"+
				"PG.CVE_METODO, \n"+
				"PG.DIA_SEMANA_PAGO, \n"+
				"PG.MONTO_MAXIMO, \n"+
				"PG.ID_TIPO_RECARGO, \n"+
				"PG.TIPO_TASA_RECARGO, \n"+
				"PG.TASA_RECARGO, \n"+
				"PG.ID_PERIODICIDAD_TASA_RECARGO, \n"+
				"PG.ID_TASA_REFERENCIA_RECARGO, \n"+
				"PG.FACTOR_TASA_RECARGO, \n"+
				"PG.MONTO_FIJO_PERIODO \n"+
				"FROM \n"+
				"SIM_PRESTAMO_GRUPO PG,  \n"+
				"SIM_CAT_PERIODICIDAD P, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_CAT_SUCURSAL S, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E \n" +
				"WHERE \n"+
				"PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND PG.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND PG.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
				"AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND P.ID_PERIODICIDAD = PG.ID_PERIODICIDAD_PRODUCTO \n"+
				"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
				"AND S.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA = G.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL = G.ID_SUCURSAL \n"+
				"AND E.CVE_GPO_EMPRESA (+)= PG.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA (+)= PG.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO (+)= PG.ID_ETAPA_PRESTAMO \n";
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
			String sNumIntegrantes = "";
			String sMinIntegrantes = "";
			String sMaxIntegrantes = "";
			String sMaximoRiego = "";
			String sMaximoAmbulante = "";
			String sMaximoCatalogo = "";
			String sIdIntegrantes = "";
			int iMaxRiesgoProp = 0;
			int iMaxAmbulanteProp = 0;
			int iMaxCatalogoProp = 0;
			String sCreditosSimultaneos = "";
			String sDeudaMinima = "";
			int iDeudaMinima = 0;
			int iDeuda = 0;
			String sSaldo = "";
			float fSaldo = 0;
			String sMinIntFundadores = "";
			float fMinIntFundadores = 0;
			float fIntegrante = 0;
			int iIntegrante = 0;
			String sNumCiclo = "";
			String sNumNegociosPrincipales = "";
			int iNumNegociosPrincipales = 0;
			boolean bMinFundadores = false;
			boolean bErrorGrupoFundador = false;
			
			/*EJEMPLO COMENTADO PARA VALIDAR EN SCRIPTS GROOVY
			Grupo grupo = new Grupo(conn);
			
			HashMap resultadoValidaGrupo = new HashMap();
			
			resultadoValidaGrupo = (HashMap)grupo.altaPrestamoGrupal(
					(String)registro.getDefCampo("CVE_GPO_EMPRESA"),
					(String)registro.getDefCampo("CVE_EMPRESA"),
					1,
					5419);
			
			Integer resultado = (Integer)resultadoValidaGrupo.get("clave");
			String  descripcion = (String)resultadoValidaGrupo.get("descripcion");
			System.out.println ("*************resultado: "+resultado);
			System.out.println ("*************descripcion: "+descripcion);			
			*/
			
			//Verificamos si existen los fundadores del grupo
			sSql = "SELECT \n" + 
				   "ID_GRUPO, \n" + 
				   "ID_INTEGRANTE \n" +
				   "FROM \n" +
				   "SIM_GRUPO_FUNDADOR \n" +
				   "WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
				   "AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
			System.out.println("1.2"+sSql);
			ejecutaSql();
			if (rs.next()){
				//Preguntamos por el par�metro % m�nimo de fundadores del grupo.
				sSql = "SELECT B * (SELECT \n" +  
					   	"PORC_INT_FUNDADORES \n" + 
					   	"FROM SIM_PARAMETRO_GLOBAL \n" +  
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" + 
					    "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
						") MIN_INT_FUNDADORES \n" +  
						"FROM ( \n" +  
						"SELECT A / 100 B \n" +  
						"FROM( \n" +  
						"SELECT COUNT(*) A \n" +  
						"FROM ( \n" +  
					 "SELECT \n" +  
					   "ID_GRUPO, \n" +  
					   "ID_INTEGRANTE \n" + 
					   "FROM \n" + 
					   "SIM_GRUPO_FUNDADOR \n" + 
					   "WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" + 
					   "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					   "AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
					   "))) \n" ;
				System.out.println("1.3"+sSql);
				ejecutaSql();
				if (rs.next()){
					sMinIntFundadores = rs.getString("MIN_INT_FUNDADORES");
					fMinIntFundadores = (Float.parseFloat(sMinIntFundadores));
				
				}
				System.out.println("1.4");
				//Obtenemos los integrantes que actualmente integran el grupo
				sSql = " SELECT \n" + 
					  " ID_INTEGRANTE \n" +
					  "	FROM SIM_GRUPO_INTEGRANTE  \n" +
					  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					  "	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
					  " AND FECHA_BAJA_LOGICA IS NULL \n";
				System.out.println("1.5"+sSql);
				ejecutaSql();
				while (rs.next()){
			
					registro.addDefCampo("ID_INTEGRANTE",rs.getString("ID_INTEGRANTE")== null ? "": rs.getString("ID_INTEGRANTE"));
					
					sSql = "SELECT \n"+
							"ID_GRUPO, \n" + 
							"ID_INTEGRANTE \n" +
							"FROM SIM_GRUPO_FUNDADOR \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						    "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
							"AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n" ;
					System.out.println("1.6"+sSql);
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					if (rs1.next()){
						fIntegrante++;
					}
				}
				System.out.println("1.7"+sSql);
				if (fIntegrante < fMinIntFundadores){
					bMinFundadores = false;
				}else {
					bMinFundadores = true;
				}
				
			}else {
				System.out.println("1.8");
				sSql = "SELECT \n" + 
				   "ID_GRUPO \n" + 
				   "FROM \n" + 
				   "SIM_PRESTAMO_EXCEPCION_CICLO \n" +
				   "WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
				   "AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
				ejecutaSql();
				if (rs.next()){
					System.out.println("1.9"+sSql);
					//Error esto no es posible, est�n no se dieron los integrantes fundadores en la migraci�n.
					bErrorGrupoFundador = true;
				}else {
			
					sSql = " SELECT \n" + 
					  " ID_INTEGRANTE \n" +
					  "	FROM SIM_GRUPO_INTEGRANTE  \n" +
					  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					  "	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
					  " AND FECHA_BAJA_LOGICA IS NULL \n";
				ejecutaSql();
				while (rs.next()){
					System.out.println("1.10"+sSql);
					registro.addDefCampo("ID_INTEGRANTE",rs.getString("ID_INTEGRANTE")== null ? "": rs.getString("ID_INTEGRANTE"));
					
					sSql =  "INSERT INTO SIM_GRUPO_FUNDADOR ( \n"+
								"CVE_GPO_EMPRESA, \n" +
								"CVE_EMPRESA, \n" +
								"ID_GRUPO, \n" +
								"ID_INTEGRANTE) \n" +
						        "VALUES ( \n"+
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "') \n" ;
					System.out.println("1.11"+sSql);
					
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					System.out.println("1.12");
					ps1.execute();
					System.out.println("1.13");
					ResultSet rs1 = ps1.getResultSet();
					System.out.println("1.14");
				}
				System.out.println("1.15");
				bMinFundadores = true;
				System.out.println("1.16");
				}
				System.out.println("1.17");
			}
		
			System.out.println("2");
			
			
			sSql ="SELECT COUNT(*) NUM_INTEGRANTES FROM ( \n" + 
				  " SELECT \n" + 
				  " ID_INTEGRANTE \n" +
				  "	FROM SIM_GRUPO_INTEGRANTE  \n" +
				  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
				  "	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
				  " AND FECHA_BAJA_LOGICA IS NULL ) \n";
			ejecutaSql();
			if (rs.next()){
				sNumIntegrantes = rs.getString("NUM_INTEGRANTES");
				iIntegrante = (Integer.parseInt(sNumIntegrantes));
			}
			
			
			
			//Verificamos que todos los integrantes del grupo tengan un negocio principal definido.
			
			sSql =  "SELECT COUNT(*) NUM_NEGOCIOS_PRINCIPALES \n" +
					"FROM ( \n" +
					"SELECT \n" +
					"ID_INTEGRANTE GI, \n" +
					"ID_TIPO_NEGOCIO CN \n" +
					"FROM SIM_GRUPO_INTEGRANTE GI, \n" +
					"SIM_CLIENTE_NEGOCIO CN \n" +
					"WHERE GI.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					"AND GI.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					"AND GI.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
					"AND GI.FECHA_BAJA_LOGICA IS NULL \n" +
					"AND CN.CVE_GPO_EMPRESA = GI.CVE_GPO_EMPRESA \n" +
					"AND CN.CVE_EMPRESA = GI.CVE_EMPRESA \n" +
					"AND CN.ID_PERSONA = GI.ID_INTEGRANTE \n" +
					"AND CN.B_PRINCIPAL = 'V' \n" +
					") \n" ;
			ejecutaSql();
			if (rs.next()){
				sNumNegociosPrincipales = rs.getString("NUM_NEGOCIOS_PRINCIPALES");
				iNumNegociosPrincipales = (Integer.parseInt(sNumNegociosPrincipales));
			}
			
			if (iNumNegociosPrincipales == iIntegrante){
			
			//	*************************
				if (!bMinFundadores){
			
					if (bErrorGrupoFundador){
						resultadoCatalogo.mensaje.setClave("NO_EXISTE_GPO_FUNDADOR");
					}else {
						resultadoCatalogo.mensaje.setClave("NO_MINIMO_FUNDADORES");
					}
				}else if (bMinFundadores) {
				
					//Preguntamos por el par�metro Cr�dito Simult�neos.
					
					sSql = " SELECT \n" + 
						  " CREDITOS_SIMULTANEOS, \n" +
						  " IMP_DEUDA_MINIMA \n" +
						  "	FROM SIM_PARAMETRO_GLOBAL  \n" +
						  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
					ejecutaSql();
					if (rs.next()){
						sCreditosSimultaneos = rs.getString("CREDITOS_SIMULTANEOS");
						sDeudaMinima = rs.getString("IMP_DEUDA_MINIMA");
						iDeudaMinima = (Integer.parseInt(sDeudaMinima));
					}
					System.out.println("3");
					if (sCreditosSimultaneos.equals("V")){
					
						//Cuenta los integrantes del grupo.
						sSql =  "SELECT COUNT(*) NUM_INTEGRANTES \n" +
							"FROM SIM_GRUPO_INTEGRANTE  \n"+
							"WHERE \n"+
							"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
							"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
							"AND FECHA_BAJA_LOGICA IS NULL \n" ;
						ejecutaSql();
						if (rs.next()){
							sNumIntegrantes = rs.getString("NUM_INTEGRANTES");
						}
						
						//Obtiene de los par�metros globales del grupo el m�nimo de integrantes que debe tener un grupo.
						sSql =  "SELECT \n"+
							"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
							"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
						ejecutaSql();
						
						if (rs.next()){
							sMinIntegrantes = rs.getString("MINIMO_INTEGRANTES");
						}
						
						//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que debe tener un grupo.
						sSql =  "SELECT \n"+
							"	MAX(NUM_INTEGRANTE) MAXIMO_INTEGRANTES \n"+
							"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
						ejecutaSql();
						if (rs.next()){
							sMaxIntegrantes = rs.getString("MAXIMO_INTEGRANTES");
						}
						System.out.println("4");
						int iNumIntegrantes =Integer.parseInt(sNumIntegrantes.trim());
						int iMinIntegrantes =Integer.parseInt(sMinIntegrantes.trim());
						int iMaxIntegrantes =Integer.parseInt(sMaxIntegrantes.trim());
						
						//Compara si el n�mero de candidatos al grupo se encuentra dentro de los par�metros globales del grupo.
						if (iNumIntegrantes >= iMinIntegrantes){
						
							if (iNumIntegrantes <= iMaxIntegrantes){
								
								//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden estar en riesgo.
								sSql =  "SELECT \n"+
									"	MAXIMO_RIESGO \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoRiego = rs.getString("MAXIMO_RIESGO");
								}
								
								int iMaximoRiego =Integer.parseInt(sMaximoRiego.trim());
								
								//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden tener negocios ambulantes.
								sSql =  "SELECT \n"+
									"	MAX_AMBULANTE \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								System.out.println("5");	
								if (rs.next()){
									sMaximoAmbulante = rs.getString("MAX_AMBULANTE");
								}
								
								int iMaximoAmbulante =Integer.parseInt(sMaximoAmbulante.trim());
								
								//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden tener negocios de venta por cat�logo.
								sSql =  "SELECT \n"+
									"	MAX_CATALOGO \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoCatalogo = rs.getString("MAX_CATALOGO");
								}
								
								int iMaximoCatalogo =Integer.parseInt(sMaximoCatalogo.trim());
								
								//Obtiene los candidatos propuestos a formar el grupo.
								sSql =  "SELECT ID_INTEGRANTE \n" +
									"FROM SIM_GRUPO_INTEGRANTE  \n"+
									"WHERE \n"+
									"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
									"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
									"AND FECHA_BAJA_LOGICA IS NULL \n" ;
								ejecutaSql();			
									
								while (rs.next()){
									
									sIdIntegrantes = rs.getString("ID_INTEGRANTE");
									//Busca si el tipo de negocio del candidato es ambulante o de venta por cat�logo.
									sSql =  "SELECT NOM_NEGOCIO \n" +
										"FROM SIM_CLIENTE_NEGOCIO  \n"+
										"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
										"AND B_PRINCIPAL = 'V' \n" +
										"AND (ID_TIPO_NEGOCIO = '3' OR ID_TIPO_NEGOCIO = '4') \n" +
										"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
									
									PreparedStatement ps1 = this.conn.prepareStatement(sSql);
									ps1.execute();
									ResultSet rs1 = ps1.getResultSet();
						
									if (rs1.next()){
										iMaxRiesgoProp ++;
									}
									rs1.close();
									ps1.close();	
								}
								System.out.println("6");
								//Comprueba si el n�mero de candidatos en riesgo se encuentra dentro de los par�metros globales del grupo.
								if (iMaxRiesgoProp <= iMaximoRiego){
									
									//Obtiene los candidatos propuestos a formar el grupo.
									sSql =  "SELECT ID_INTEGRANTE \n" +
										"FROM SIM_GRUPO_INTEGRANTE  \n"+
										"WHERE \n"+
										"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
										"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
										"AND FECHA_BAJA_LOGICA IS NULL \n" ;
									ejecutaSql();			
									
									
									while (rs.next()){
										
										sIdIntegrantes = rs.getString("ID_INTEGRANTE");
										//Busca si el tipo de negocio del candidato es ambulante.
										sSql =  "SELECT NOM_NEGOCIO \n" +
											"FROM SIM_CLIENTE_NEGOCIO  \n"+
											"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND B_PRINCIPAL = 'V' \n" +
											"AND ID_TIPO_NEGOCIO = '3' \n" +
											"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
										
										PreparedStatement ps1 = this.conn.prepareStatement(sSql);
										ps1.execute();
										ResultSet rs1 = ps1.getResultSet();
							
										if (rs1.next()){
											iMaxAmbulanteProp ++;
										}
										rs1.close();
										ps1.close();
									}	
										
									//Comprueba si el n�mero de integrantes con negocios tipo ambulantes es el permitido.
									if (iMaxAmbulanteProp <= iMaximoAmbulante){
										
										//Obtiene los candidatos propuestos a formar el grupo.
										sSql =  "SELECT ID_INTEGRANTE \n" +
											"FROM SIM_GRUPO_INTEGRANTE  \n"+
											"WHERE \n"+
											"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
											"AND FECHA_BAJA_LOGICA IS NULL \n" ;
										ejecutaSql();			
										
										
										while (rs.next()){
											
											sIdIntegrantes = rs.getString("ID_INTEGRANTE");
											//Busca si el tipo de negocio del candidato es venta por cat�logo.
											sSql =  "SELECT NOM_NEGOCIO \n" +
												"FROM SIM_CLIENTE_NEGOCIO  \n"+
												"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"AND B_PRINCIPAL = 'V' \n" +
												"AND ID_TIPO_NEGOCIO = '4' \n" +
												"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
											
											PreparedStatement ps1 = this.conn.prepareStatement(sSql);
											ps1.execute();
											ResultSet rs1 = ps1.getResultSet();
								
											if (rs1.next()){
												iMaxCatalogoProp ++;
											}
											rs1.close();
											ps1.close();
										}	
										System.out.println("7");
										//Comprueba si el n�mero de integrantes con negocios tipo venta por cat�logo es el permitido.
										if (iMaxCatalogoProp <= iMaximoCatalogo){
											
										
											
											
											//Verificamos si el producto tiene actividades o requisitos definidos.
											sSql =  "SELECT DISTINCT \n"+
												"ID_ETAPA_PRESTAMO \n"+
												"FROM \n"+
												"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
												"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
												"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
											
											ejecutaSql();
											
											if (rs.next()){
												
												
												
												
												
												//Verificamos si ya se le asigno el producto.
													sSql =  "SELECT \n"+
														"PG.ID_GRUPO, \n"+
														"PG.ID_PRODUCTO \n"+
														"FROM SIM_PRESTAMO_GRUPO PG, \n"+
														"     SIM_CAT_ETAPA_PRESTAMO E \n"+
														"WHERE PG.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
														"AND PG.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
														"AND PG.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
														"AND PG.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n"+
														"AND E.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
														"AND E.CVE_EMPRESA = PG.CVE_EMPRESA \n"+ 
														"AND E.ID_ETAPA_PRESTAMO = PG.ID_ETAPA_PRESTAMO \n"+ 
														"AND E.B_CANCELADO != 'V' \n"; 
														
													
													ejecutaSql();
													
													if (rs.next()){
														
														//Obtiene el pr�ximo ciclo que le corresponde.
														sSql =  "SELECT \n" +
														"ID_PRODUCTO, \n"+
														"NUM_CICLO \n"+
														"FROM SIM_PRODUCTO_CICLO \n"+
														"WHERE \n"+
														"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
														"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
														"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
														"AND NUM_CICLO = (SELECT \n"+
																"MAX(PG.NUM_CICLO)+1 NUM_CICLO_PROX \n"+
																"FROM \n"+
																"SIM_PRESTAMO_GRUPO PG, \n"+
																"SIM_CAT_ETAPA_PRESTAMO E \n"+
																"WHERE PG.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																"AND PG.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																"AND PG.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
																"AND PG.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n"+
																"AND E.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
																"AND E.CVE_EMPRESA = PG.CVE_EMPRESA \n"+ 
																"AND E.ID_ETAPA_PRESTAMO = PG.ID_ETAPA_PRESTAMO \n"+ 
																"AND E.B_CANCELADO != 'V') \n";
																
													
														ejecutaSql();
														if (rs.next()){
															sNumCiclo = rs.getString("NUM_CICLO");
															
															sSql = "Todavia no se da nungun alta";
															resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
															resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",sNumCiclo);
														}else{
															//Como no tiene otro ciclo definido se le asigna el �ltimo.
															sSql =  "SELECT \n" +
																	"MAX (NUM_CICLO) NUM_CICLO \n"+
																	"FROM ( \n"+
																	"SELECT \n" +
																	"ID_PRODUCTO, \n"+
																	"NUM_CICLO \n"+
																	"FROM SIM_PRODUCTO_CICLO \n"+
																	"WHERE \n"+
																	"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
																	"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
																	"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
																	") \n";
																	
															ejecutaSql();
															if (rs.next()){
																registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																
																sSql = "Todavia no se da nungun alta";
																
																resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
															}
														}
														
													}else{
														
														System.out.println("8");
														
														//Busca si hay una excepci�n.
														sSql =  "SELECT \n"+
																"P.CVE_GPO_EMPRESA, \n"+
																"P.CVE_EMPRESA, \n"+
																"P.ID_PRODUCTO, \n"+
																"P.NUM_CICLO, \n"+
																"P.ID_GRUPO \n"+
																"FROM \n"+
																"SIM_PRESTAMO_EXCEPCION_CICLO P \n"+
																"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																"AND P.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
																"AND P.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n";
																
															ejecutaSql();
															if (rs.next()){
																registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																
																sSql = "SELECT DISTINCT \n"+
																		"PC.CVE_GPO_EMPRESA, \n"+
																		"PC.CVE_EMPRESA, \n"+
																		"PC.ID_PRODUCTO, \n"+
																		"P.NOM_PRODUCTO, \n"+
																		"P.APLICA_A, \n"+
																		"P.ID_PERIODICIDAD, \n"+
																		"P.CVE_METODO, \n"+
																		"PC.NUM_CICLO, \n"+
																		"PC.ID_FORMA_DISTRIBUCION, \n"+
																		"PC.PLAZO, \n"+
																		"PC.TIPO_TASA, \n"+
																		"PC.VALOR_TASA, \n"+
																		"PC.ID_PERIODICIDAD_TASA, \n"+
																		"PC.ID_TASA_REFERENCIA, \n"+
																		"PC.MONTO_MAXIMO, \n"+
																		"P.MONTO_MINIMO, \n"+
																		"PC.PORC_FLUJO_CAJA, \n"+
																		"P.FECHA_INICIO_ACTIVACION, \n"+
																		"P.FECHA_FIN_ACTIVACION \n"+
																		"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
																		"SIM_PRODUCTO P, \n"+
																		"SIM_PRODUCTO_SUCURSAL PS \n"+
																		"WHERE PC.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																		"AND PC.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																		"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
																		"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
																		"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
																		"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
																		"AND PC.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+	
																		"AND PC.NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' + '1' \n"+
																		"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
																		"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
																		"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
																		"AND P.APLICA_A = 'Grupo' \n";
																ejecutaSql();
																if (rs.next()){
																	registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																	
																	sSql = "Todavia no se da nungun alta";
																	resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
																	resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																}else{
																	//Como no tiene otro ciclo definido se le asigna el ultimo.
																	sSql =  "SELECT \n"+
																	"P.CVE_GPO_EMPRESA, \n"+
																	"P.CVE_EMPRESA, \n"+
																	"P.ID_PRODUCTO, \n"+
																	"P.NUM_CICLO, \n"+
																	"P.ID_GRUPO \n"+
																	"FROM \n"+
																	"SIM_PRESTAMO_EXCEPCION_CICLO P \n"+
																	"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																	"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																	"AND P.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
																	"AND P.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n";
																	
																			
																	ejecutaSql();
																	if (rs.next()){
																		registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																		
																		sSql = "Todavia no se da nungun alta";
																		resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																		resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																	}
																}
														
															}else {
														
														
																System.out.println("9");
														//Se le asigna el producto-ciclo 1.
														//Ingresa el cr�dito grupal.
														//String sIdPrestamoGrupo = "";
														/*
														//OBTENEMOS EL SEQUENCE
														sSql = "SELECT SQ01_SIM_PRESTAMO_GRUPO.nextval as ID_PRESTAMO_GRUPO FROM DUAL";
														ejecutaSql();
														
														if (rs.next()){
															sIdPrestamoGrupo = rs.getString("ID_PRESTAMO_GRUPO");
														}
														*/
														sSql = "Todavia no se da nungun alta";
														resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
														resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO","1");
															}
														/*
														sSql =  "INSERT INTO SIM_PRESTAMO_GRUPO ( "+
															"CVE_GPO_EMPRESA, \n" +
															"CVE_EMPRESA, \n" +
															"ID_PRESTAMO_GRUPO, \n" +
															"ID_GRUPO, \n" +
															"ID_PRODUCTO, \n" +
															"NUM_CICLO) \n" +
															" VALUES (" +
															"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
															"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
															" " + sIdPrestamoGrupo +", \n" +
															"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
															"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
															"'1') \n" ;
																
														System.out.println("*ALTA EN LA TABLA PADRE cilco 1*"+sSql);
														
														if (ejecutaUpdate() == 0){
															resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
														}
														*/
													}
											
												
											}else {
												//El producto no tiene actividades o requisitos definidos.
												resultadoCatalogo.mensaje.setClave("PRODUCTO_SIN_ACT_REQ");
											}
										
										}else{
											//El n�mero de integrantes con negocios tipo venta por cat�logo no es el permitido.
											System.out.println("**NO CUMPLE** con los tipo de negocio por catalogo permitido");
											resultadoCatalogo.mensaje.setClave("INTEGRANTES_CATALOGO_ERROR");
										}
											
									}else{
										//El n�mero de integrantes con negocios tipo ambulantes no es el permitido.
										System.out.println("**NO CUMPLE** con los tipo de negocio ambulante permitido");
										resultadoCatalogo.mensaje.setClave("INTEGRANTES_AMBULANTE_ERROR");
									}
									
								} else {
									//El n�mero de candidatos en riesgo no encuentra dentro de los par�metros globales del grupo.
									System.out.println("**NO CUMPLE** con los integrantes en riesgo permitidos");
									resultadoCatalogo.mensaje.setClave("INTEGRANTES_MAX_RIESGO_ERROR");
								}
							}else {
								//El n�mero de integrantes para formar el grupo no se encuentra dentro de los par�metros globales del grupo.
								System.out.println("***NO*** Cumple con los integrantes maximos");
								resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
							}
						}else {
							//El n�mero de integrantes para formar el grupo no se encuentra dentro de los par�metros globales del grupo.
							System.out.println("***NO*** Cumple con los integrantes m�nimos");
							resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
						}
						
						
					}else{
						
						sSql =  "SELECT \n"+
								"ID_INTEGRANTE \n"+
								"FROM SIM_GRUPO_INTEGRANTE \n"+
								"WHERE CVE_GPO_EMPRESA='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								"AND CVE_EMPRESA='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_GRUPO='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n"+
								"AND FECHA_BAJA_LOGICA IS NULL \n" ;
						
						PreparedStatement ps1 = this.conn.prepareStatement(sSql);
						ps1.execute();
						ResultSet rs1 = ps1.getResultSet();
						
						while (rs1.next()){
						
							registro.addDefCampo("ID_PERSONA",rs1.getString("ID_INTEGRANTE")== null ? "": rs1.getString("ID_INTEGRANTE"));
							System.out.println("10");
							//Busca todos los creditos del cliente.
							sSql =  "SELECT \n" +
									"P.ID_PRESTAMO \n" +
									"FROM SIM_PRESTAMO P, \n" +
									"SIM_CAT_ETAPA_PRESTAMO E \n" +
									"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
									"AND P.ID_CLIENTE = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
									"AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
									"AND E.CVE_EMPRESA = P.CVE_EMPRESA \n" +
									"AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n" +
									"AND E.B_ENTREGADO = 'V' \n" ;
							System.out.println("Busca todos los creditos del cliente"+sSql);
							
							PreparedStatement ps2 = this.conn.prepareStatement(sSql);
							ps2.execute();
							ResultSet rs2 = ps2.getResultSet();
							
							while (rs2.next()){
								System.out.println("No debe de entrar aqui");
								registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO")== null ? "": rs2.getString("ID_PRESTAMO"));
								
								sSql = "    SELECT  ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, DESCRIPCION, NULL AS IMPORTE, SUM(IMP_DESGLOSE) IMP_DESGLOSE \n"+
							    "    FROM    ( \n"+
							        
							    "    SELECT  9999999999999999 AS ID_ORDEN_TIPO, 1 AS ID_ORDEN, ID_PRESTAMO, (SELECT  F_MEDIO  \n"+
							    "                          FROM    PFIN_PARAMETRO \n"+
							    "                          WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                              AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                              AND CVE_MEDIO       = 'SYSTEM') AS FECHA_OPERACION, \n"+
							    "            'SALDO A LA FECHA' AS DESCRIPCION, NULL AS IMPORTE, ROUND(SUM(IMP_NETO),2) AS IMP_DESGLOSE, CVE_CONCEPTO \n"+
							    "    FROM (         \n"+
							            
							    "        SELECT  1 AS ID_ORDEN_TIPO, 7 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "                ROUND(T.IMP_CAPITAL_AMORT * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
							    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
							    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "            AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+
							    "                                        FROM    PFIN_PARAMETRO  \n"+
							    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
							    "            AND C.CVE_CONCEPTO    = 'CAPITA' \n"+
							    "            AND ROUND(T.IMP_CAPITAL_AMORT,2) > 0 \n"+
							                
							    "        UNION ALL \n"+
							        
							    "        SELECT  1 AS ID_ORDEN_TIPO, 2 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "                ROUND(T.IMP_INTERES*-1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
							    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
							    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
							    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "            AND T.FECHA_AMORTIZACION <= (SELECT  F_MEDIO \n"+
							    "                                        FROM    PFIN_PARAMETRO \n"+
							    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
							    "            AND C.CVE_CONCEPTO    = 'INTERE' \n"+
							    "            AND ROUND(T.IMP_INTERES,2) > 0 \n"+
							        
							    "        UNION ALL \n"+
							
							    "        SELECT  1 AS ID_ORDEN_TIPO, 2 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "                ROUND(NVL(T.IMP_IVA_INTERES,0) * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION,  \n"+
							    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
							    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
							    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "            AND T.FECHA_AMORTIZACION <= (SELECT  F_MEDIO \n"+
							    "                                        FROM    PFIN_PARAMETRO \n"+
							    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
							    "            AND C.CVE_CONCEPTO    = 'IVAINT' \n"+
							    "            AND ROUND(NVL(T.IMP_IVA_INTERES,0),2) > 0 \n"+
							
							    "        UNION ALL \n"+
							        
							    "        SELECT  1 AS ID_ORDEN_TIPO, 3 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "                ROUND(T.IMP_INTERES_EXTRA * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
							    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
							    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
							    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "            AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+
							    "                                        FROM    PFIN_PARAMETRO  \n"+
							    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
							    "            AND C.CVE_CONCEPTO    = 'INTEXT' \n"+
							    "            AND ROUND(T.IMP_INTERES_EXTRA,2) > 0 \n"+
							
							    "        UNION ALL \n"+
							
							            //Muestra el importe de IVA de inter�s extra a una fecha 
							    "        SELECT  1 AS ID_ORDEN_TIPO, 5 AS ID_ORDEN, ID_PRESTAMO, FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "                ROUND(NVL(T.IMP_IVA_INTERES_EXTRA,0) * -1,2) AS IMP_DESGLOSE, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
							    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
							    "        FROM    SIM_TABLA_AMORTIZACION T, PFIN_CAT_CONCEPTO C \n"+
							    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "            AND T.FECHA_AMORTIZACION <=(SELECT  F_MEDIO \n"+
							    "                                        FROM    PFIN_PARAMETRO \n"+
							    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "            AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA     = C.CVE_EMPRESA \n"+
							    "            AND C.CVE_CONCEPTO    = 'IVAINTEX' \n"+
							    "            AND ROUND(NVL(T.IMP_IVA_INTERES_EXTRA,0),2) > 0 \n"+
							
							    "        UNION ALL \n"+
							
							            //Obtiene los recargos por pago tard�o en caso de que apliquen para el pr�stamo
							    "        SELECT  1 AS ID_ORDEN_TIPO, 6 AS ID_ORDEN, P.ID_PRESTAMO, T.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "                ROUND(NVL(PC.MONTO_FIJO_PERIODO * -1,0),2) AS IMP_DESGLOSE, INITCAP(C.DESC_LARGA) AS DESCRIPCION, \n"+
							    "                C.CVE_CONCEPTO, 'I' CVE_AFECTA \n"+
							    "        FROM    SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P, SIM_PRODUCTO_CICLO PC, PFIN_CAT_CONCEPTO C \n"+
							    "        WHERE   T.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "            AND T.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "            AND T.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "            AND T.FECHA_AMORTIZACION < (SELECT  F_MEDIO \n"+
							    "                                        FROM    PFIN_PARAMETRO \n"+
							    "                                        WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "            AND T.B_PAGO_PUNTUAL    = 'F' \n"+
							    "            AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
							    "            AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
							    "            AND P.CVE_GPO_EMPRESA   = PC.CVE_GPO_EMPRESA \n"+
							    "            AND P.CVE_EMPRESA       = PC.CVE_EMPRESA \n"+
							    "            AND P.ID_PRODUCTO       = PC.ID_PRODUCTO \n"+
							    "            AND P.NUM_CICLO         = PC.NUM_CICLO \n"+
							    "            AND T.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
							    "            AND 'PAGOTARD'          = C.CVE_CONCEPTO \n"+
							    "            AND PC.ID_TIPO_RECARGO  IN (4,5) \n"+
							    "            AND ROUND(NVL(PC.MONTO_FIJO_PERIODO,0),2) > 0 \n"+
							        
							    "        UNION ALL \n"+
							        
							    "        SELECT  1 AS ID_ORDEN_TIPO, A.ID_ACCESORIO AS ID_ORDEN, T.ID_PRESTAMO, T.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "                ROUND(A.IMP_ACCESORIO * -1,2) AS IMP_NETO, INITCAP(C.NOM_ACCESORIO) AS DESCRIPCION, \n"+
							    "                DECODE(A.ID_ACCESORIO,6,'COMISVID',7,'COMISGM',8,'COMISDEU') AS CVE_CONCEPTO, \n"+
							    "                'I' CVE_AFECTA \n"+
							    "        FROM    SIM_TABLA_AMORTIZACION T, SIM_TABLA_AMORT_ACCESORIO A, SIM_CAT_ACCESORIO C \n"+
							    "        WHERE   T.CVE_GPO_EMPRESA       = 'SIM' \n"+
							    "            AND T.CVE_EMPRESA           = 'CREDICONFIA' \n"+
							    "            AND T.ID_PRESTAMO           = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "            AND T.FECHA_AMORTIZACION    <= (SELECT  F_MEDIO \n"+
							    "                                            FROM    PFIN_PARAMETRO \n"+
							    "                                            WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                                 AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                                 AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "            AND T.CVE_GPO_EMPRESA       = A.CVE_GPO_EMPRESA \n"+
							    "            AND T.CVE_EMPRESA           = A.CVE_EMPRESA \n"+
							    "            AND T.ID_PRESTAMO           = A.ID_PRESTAMO \n"+
							    "            AND T.NUM_PAGO_AMORTIZACION = A.NUM_PAGO_AMORTIZACION \n"+
							    "            AND A.CVE_GPO_EMPRESA       = C.CVE_GPO_EMPRESA \n"+
							    "            AND A.CVE_EMPRESA           = C.CVE_EMPRESA \n"+
							    "            AND A.ID_ACCESORIO          = C.ID_ACCESORIO \n"+
							    "            AND ROUND(A.IMP_ACCESORIO,2)> 0 \n"+
							
							    "    UNION ALL \n"+
							        
							
							
							    "SELECT  ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, IMP_NETO, \n"+
							    "        DESCRIPCION, CVE_CONCEPTO, CVE_AFECTA \n"+
							    "FROM    (    \n"+
							
							    //Muestra el importe de Intereses Moratorios a la fecha 
							    "SELECT  1 AS ID_ORDEN_TIPO, 7 AS ID_ORDEN, A.ID_PRESTAMO, A.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "        ROUND(SUM(A.IMP_INT_MORATORIO) * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO, \n"+
							    "        'I' AS CVE_AFECTA \n"+
							    "FROM    (    \n"+
							                
							    "    SELECT  P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO, F.F_LIQUIDACION, T.FECHA_AMORTIZACION, \n"+
							    "            T.NUM_PAGO_AMORTIZACION, T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION AS IMP_CAPITAL, \n"+
							                
							    "            CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
							    "                0 \n"+
							    "            ELSE \n"+
							    "                CASE WHEN \n"+
							    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    ELSE \n"+
							    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    END > 0 THEN \n"+
							                        
							    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    ELSE \n"+
							    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    END \n"+
							    "                ELSE  \n"+
							    "                    0 \n"+
							    "                END \n"+
							    "            END AS IMP_CAPITAL_DEBE, M.IMP_MOVTOS,  \n"+
							                
							    "            PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) TASA_MORATORIA, \n"+
							                
							    "            CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
							    "                0 \n"+
							    "            ELSE \n"+
							    "                CASE WHEN \n"+
							    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    ELSE \n"+
							    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    END > 0 THEN \n"+
							                        
							    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    ELSE \n"+
							    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    END \n"+
							    "                ELSE  \n"+
							    "                    0 \n"+
							    "                END \n"+
							    "            END * PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) AS IMP_INT_MORATORIO, \n"+
							    "            P.ID_PRODUCTO, P.NUM_CICLO \n"+
							                
							                
							                
							    "    FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P, SIM_TABLA_AMORTIZACION T2, \n"+
							        
							    "            (   SELECT  F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION, SUM( \n"+
							    "                        NVL(D.IMP_CONCEPTO *  \n"+
							    "                            DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),0) \n"+
							    "                        )  \n"+
							    "                        IMP_MOVTOS \n"+
							    "                FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, PFIN_MOVIMIENTO M, \n"+
							    "                        PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O \n"+
							    "                WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
							    "                    AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
							    "                    AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
							    "                    AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
							    "                    AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
							    "                    AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "                    AND T.NUM_PAGO_AMORTIZACION   = 1 \n"+
							    "                    AND F.F_INFORMACION     BETWEEN T.FECHA_AMORTIZACION -1 \n"+
							    "                                            AND (SELECT  F_MEDIO \n"+
							    "                                                 FROM    PFIN_PARAMETRO \n"+
							    "                                                 WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                                     AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                                     AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "                    AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA(+) \n"+
							    "                    AND F.CVE_EMPRESA       = M.CVE_EMPRESA(+) \n"+
							    "                    AND " + (String)registro.getDefCampo("ID_PRESTAMO") + "                 = M.ID_PRESTAMO(+) \n"+
							    "                    AND F.F_INFORMACION     >= M.F_LIQUIDACION(+) \n"+
							    "                    AND M.SIT_MOVIMIENTO(+) <> 'CA' \n"+
							    "                    AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA(+) \n"+
							    "                    AND M.CVE_EMPRESA       = D.CVE_EMPRESA(+) \n"+
							    "                    AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO(+) \n"+
							    "                    AND M.CVE_OPERACION     = D.CVE_OPERACION(+) \n"+
							    "                    AND D.CVE_CONCEPTO(+)      = 'CAPITA' \n"+
							    "                    AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA(+) \n"+
							    "                    AND M.CVE_EMPRESA       = O.CVE_EMPRESA(+) \n"+
							    "                    AND M.CVE_OPERACION     = O.CVE_OPERACION(+) \n"+
							    "                GROUP BY F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION ) M \n"+
							
							    "    WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
							    "        AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
							    "        AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
							    "        AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
							    "        AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
							    "        AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							            
							    "        AND T.CVE_GPO_EMPRESA       = T2.CVE_GPO_EMPRESA(+) \n"+
							    "        AND T.CVE_EMPRESA           = T2.CVE_EMPRESA(+) \n"+
							    "        AND T.ID_PRESTAMO           = T2.ID_PRESTAMO(+) \n"+
							    "        AND T.NUM_PAGO_AMORTIZACION - 1 = T2.NUM_PAGO_AMORTIZACION(+) \n"+
							
							    "        AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+
							    "        AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
							    "        AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
							    "        AND (SELECT  MAX(P.NUM_PAGO_AMORTIZACION) \n"+
							    //, P.FECHA_AMORTIZACION
							    "             FROM    SIM_TABLA_AMORTIZACION P \n"+
							    "             WHERE   P.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                 AND P.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                 AND P.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "                 AND P.FECHA_AMORTIZACION <= F.F_INFORMACION) = T.NUM_PAGO_AMORTIZACION \n"+
							    "        AND F.F_INFORMACION     BETWEEN (SELECT MIN(FECHA_AMORTIZACION) \n"+
							    "                                         FROM   SIM_TABLA_AMORTIZACION \n"+
							    "                                         WHERE  CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "') \n"+
							    "                                AND (SELECT  F_MEDIO  \n"+
							    "                                     FROM    PFIN_PARAMETRO  \n"+
							    "                                     WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                         AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                         AND CVE_MEDIO       = 'SYSTEM') \n"+
							                                             
							    "        AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA \n"+
							    "        AND F.CVE_EMPRESA       = M.CVE_EMPRESA \n"+
							    "        AND F.F_INFORMACION     = M.F_LIQUIDACION + 1 \n"+
							            
							            
							    "        )   A, SIM_PRODUCTO_CICLO PC, PFIN_CAT_CONCEPTO C \n"+
							
							        
							    "    WHERE   A.CVE_GPO_EMPRESA   = PC.CVE_GPO_EMPRESA \n"+
							    "        AND A.CVE_EMPRESA       = PC.CVE_EMPRESA \n"+
							    "        AND A.ID_PRODUCTO       = PC.ID_PRODUCTO \n"+
							    "        AND A.NUM_CICLO         = PC.NUM_CICLO \n"+
							    "        AND A.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
							    "        AND A.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
							    "        AND C.CVE_CONCEPTO      = 'INTMORA' \n"+
							    "    GROUP BY A.ID_PRESTAMO, A.FECHA_AMORTIZACION, INITCAP(C.DESC_LARGA), C.CVE_CONCEPTO \n"+
							    "    HAVING ROUND(SUM(A.IMP_INT_MORATORIO),2) > 0 \n"+
							    ") \n"+
							
							    "UNION ALL \n"+
							
							    "SELECT  ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, IMP_NETO, \n"+
							    "        DESCRIPCION, CVE_CONCEPTO, CVE_AFECTA \n"+
							    "FROM    (    \n"+
							
							    //Muestra el importe de IVA de Intereses Moratorios a la fecha 
							    "SELECT  1 AS ID_ORDEN_TIPO, 8 AS ID_ORDEN, A.ID_PRESTAMO, A.FECHA_AMORTIZACION AS FECHA_OPERACION, \n"+
							    "        ROUND(SUM(A.IMP_IVA_INT_MORATORIO) * -1,2) AS IMP_NETO, INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO, \n"+
							    "        'I' AS CVE_AFECTA \n"+
							    "FROM    (    \n"+
							                
							    "    SELECT  P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO, F.F_LIQUIDACION, T.FECHA_AMORTIZACION, \n"+
							    "            T.NUM_PAGO_AMORTIZACION, T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION AS IMP_CAPITAL, \n"+
							                
							    "            CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
							    "                0 \n"+
							    "            ELSE \n"+
							    "                CASE WHEN \n"+
							    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    ELSE \n"+
							    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    END > 0 THEN \n"+
							                        
							    "                     CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                         T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                     ELSE \n"+
							    "                         T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                     END \n"+
							    "                 ELSE  \n"+
							    "                     0 \n"+
							    "                 END \n"+
							    "             END AS IMP_CAPITAL_DEBE, M.IMP_MOVTOS, \n"+
							                
							    "             PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) TASA_MORATORIA, \n"+
							                 
							    "             CASE WHEN F.F_LIQUIDACION = T.FECHA_AMORTIZACION AND T.NUM_PAGO_AMORTIZACION = 1 THEN \n"+
							    "                 0 \n"+
							    "             ELSE \n"+
							    "                 CASE WHEN  \n"+
							    "                     CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                         T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                     ELSE \n"+
							    "                         T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                     END > 0 THEN \n"+
							    
							    "                    CASE WHEN F.F_LIQUIDACION > T.FECHA_AMORTIZACION THEN \n"+
							    "                        T.IMP_CAPITAL_AMORT * T.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    ELSE \n"+
							    "                        T2.IMP_CAPITAL_AMORT * T2.NUM_PAGO_AMORTIZACION - M.IMP_MOVTOS \n"+
							    "                    END \n"+
							    "                ELSE  \n"+
							    "                    0 \n"+
							    "                END \n"+
							    "            END * PKG_CREDITO.dametasamoratoriadiaria(P.CVE_GPO_EMPRESA,P.CVE_EMPRESA,P.ID_PRESTAMO) *  (TASA_IVA -1 ) AS IMP_IVA_INT_MORATORIO, \n"+
							    "            P.ID_PRODUCTO, P.NUM_CICLO \n"+
							                
							    "    FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, SIM_PRESTAMO P, SIM_CAT_SUCURSAL S, SIM_TABLA_AMORTIZACION T2, \n"+
							        
							    "            (   SELECT  F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION, SUM( \n"+
							    "                        NVL(D.IMP_CONCEPTO * \n"+
							    "                            DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),0) \n"+
							    "                        ) \n"+
							    "                        IMP_MOVTOS, MAX(NUM_PAGO_AMORTIZACION) MAX_NUM_PAGO \n"+
							    "                FROM    PFIN_DIA_LIQUIDACION F, SIM_TABLA_AMORTIZACION T, PFIN_MOVIMIENTO M, \n"+
							    "                        PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O \n"+
							    "                WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
							    "                    AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
							    "                    AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
							    "                    AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
							    "                    AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
							    "                    AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "                    AND T.NUM_PAGO_AMORTIZACION   = 1 \n"+
							    "                    AND F.F_INFORMACION     BETWEEN T.FECHA_AMORTIZACION -1 \n"+
							    "                                            AND (SELECT  F_MEDIO  \n"+
							    "                                                 FROM    PFIN_PARAMETRO \n"+
							    "                                                 WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                                     AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                                     AND CVE_MEDIO       = 'SYSTEM') \n"+
							    "                    AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA(+) \n"+
							    "                    AND F.CVE_EMPRESA       = M.CVE_EMPRESA(+) \n"+
							    "                    AND " + (String)registro.getDefCampo("ID_PRESTAMO") + "    = M.ID_PRESTAMO(+) \n"+
							    "                    AND F.F_INFORMACION     >= M.F_LIQUIDACION(+) \n"+
							    "                    AND M.SIT_MOVIMIENTO(+) <> 'CA' \n"+
							    "                    AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA(+) \n"+
							    "                    AND M.CVE_EMPRESA       = D.CVE_EMPRESA(+) \n"+
							    "                    AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO(+) \n"+
							    "                    AND M.CVE_OPERACION     = D.CVE_OPERACION(+) \n"+
							    "                    AND D.CVE_CONCEPTO(+)      = 'CAPITA' \n"+
							    "                    AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA(+) \n"+
							    "                    AND M.CVE_EMPRESA       = O.CVE_EMPRESA(+) \n"+
							    "                    AND M.CVE_OPERACION     = O.CVE_OPERACION(+) \n"+
							    "                GROUP BY F.CVE_GPO_EMPRESA, F.CVE_EMPRESA, F.F_LIQUIDACION ) M \n"+
							
							    "    WHERE   F.CVE_GPO_EMPRESA   = 'SIM' \n"+
							    "        AND F.CVE_EMPRESA       = 'CREDICONFIA' \n"+
							    "        AND F.CVE_LIQUIDACION   = 'NATUR' \n"+
							    "        AND F.CVE_GPO_EMPRESA   = T.CVE_GPO_EMPRESA \n"+
							    "        AND F.CVE_EMPRESA       = T.CVE_EMPRESA \n"+
							    "        AND T.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							            
							    "        AND T.CVE_GPO_EMPRESA       = T2.CVE_GPO_EMPRESA(+) \n"+
							    "        AND T.CVE_EMPRESA           = T2.CVE_EMPRESA(+) \n"+
							    "        AND T.ID_PRESTAMO           = T2.ID_PRESTAMO(+) \n"+
							    "        AND T.NUM_PAGO_AMORTIZACION - 1 = T2.NUM_PAGO_AMORTIZACION(+) \n"+
							
							    "        AND T.CVE_GPO_EMPRESA   = P.CVE_GPO_EMPRESA \n"+
							    "        AND T.CVE_EMPRESA       = P.CVE_EMPRESA \n"+
							    "        AND T.ID_PRESTAMO       = P.ID_PRESTAMO \n"+
							    "        AND P.CVE_GPO_EMPRESA   = S.CVE_GPO_EMPRESA \n"+
							    "        AND P.CVE_EMPRESA       = S.CVE_EMPRESA \n"+
							    "        AND P.ID_SUCURSAL       = S.ID_SUCURSAL \n"+
							    "        AND (SELECT  MAX(P.NUM_PAGO_AMORTIZACION) \n"+
							    //, P.FECHA_AMORTIZACION
							    "             FROM    SIM_TABLA_AMORTIZACION P \n"+
							    "             WHERE   P.CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                 AND P.CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                 AND P.ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "                 AND P.FECHA_AMORTIZACION <= F.F_INFORMACION) = T.NUM_PAGO_AMORTIZACION \n"+
							    "        AND F.F_INFORMACION     BETWEEN (SELECT MIN(FECHA_AMORTIZACION) \n"+
							    "                                         FROM   SIM_TABLA_AMORTIZACION \n"+
							    "                                         WHERE  CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                            AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                            AND ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "') \n"+
							    "                                AND (SELECT  F_MEDIO  \n"+
							    "                                     FROM    PFIN_PARAMETRO \n"+
							    "                                     WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
							    "                                         AND CVE_EMPRESA     = 'CREDICONFIA' \n"+
							    "                                         AND CVE_MEDIO       = 'SYSTEM') \n"+
							                                             
							    "        AND F.CVE_GPO_EMPRESA   = M.CVE_GPO_EMPRESA \n"+
							    "        AND F.CVE_EMPRESA       = M.CVE_EMPRESA \n"+
							    "        AND F.F_INFORMACION     = M.F_LIQUIDACION +1)   A, SIM_PRODUCTO_CICLO PC, PFIN_CAT_CONCEPTO C \n"+
							
							        
							    "    WHERE   A.CVE_GPO_EMPRESA   = PC.CVE_GPO_EMPRESA \n"+
							    "        AND A.CVE_EMPRESA       = PC.CVE_EMPRESA \n"+
							    "        AND A.ID_PRODUCTO       = PC.ID_PRODUCTO \n"+
							    "        AND A.NUM_CICLO         = PC.NUM_CICLO \n"+
							    "        AND A.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
							    "        AND A.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
							    "        AND C.CVE_CONCEPTO      = 'IVAINTMO' \n"+
							    "    GROUP BY A.ID_PRESTAMO, A.FECHA_AMORTIZACION, INITCAP(C.DESC_LARGA), C.CVE_CONCEPTO \n"+
							    "    HAVING ROUND(SUM(A.IMP_IVA_INT_MORATORIO),2) > 0 \n"+
							    ") \n"+
							
							    "    UNION ALL \n"+
							        
							    "    SELECT  3 AS ID_ORDEN_TIPO, M.ID_MOVIMIENTO AS ID_ORDEN, M.ID_PRESTAMO, M.F_LIQUIDACION AS FECHA_OPERACION, \n"+
							    "            ROUND(D.IMP_CONCEPTO * DECODE(O.CVE_AFECTA_CREDITO,'I',-1,'D',1),2) AS IMP_NETO,  \n"+
							    "            INITCAP(C.DESC_LARGA) AS DESCRIPCION, C.CVE_CONCEPTO,  \n"+
							    "            O.CVE_AFECTA_CREDITO AS CVE_AFECTA \n"+
							    "    FROM    PFIN_MOVIMIENTO M, PFIN_MOVIMIENTO_DET D, PFIN_CAT_OPERACION O, PFIN_CAT_CONCEPTO C \n"+
							    "    WHERE   M.CVE_GPO_EMPRESA   = 'SIM' \n"+
							    "        AND M.CVE_EMPRESA       = 'CREDICONFIA' \n"+
							    "        AND M.ID_PRESTAMO       = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							    "        AND M.SIT_MOVIMIENTO    <> 'CA' \n"+
							    "        AND M.CVE_GPO_EMPRESA   = D.CVE_GPO_EMPRESA \n"+
							    "        AND M.CVE_EMPRESA       = D.CVE_EMPRESA \n"+
							    "        AND M.ID_MOVIMIENTO     = D.ID_MOVIMIENTO \n"+
							    "        AND M.CVE_OPERACION     = D.CVE_OPERACION \n"+
							    "        AND M.CVE_GPO_EMPRESA   = O.CVE_GPO_EMPRESA \n"+
							    "        AND M.CVE_EMPRESA       = O.CVE_EMPRESA \n"+
							    "        AND M.CVE_OPERACION     = O.CVE_OPERACION \n"+
							    "        AND D.CVE_GPO_EMPRESA   = C.CVE_GPO_EMPRESA \n"+
							    "        AND D.CVE_EMPRESA       = C.CVE_EMPRESA \n"+
							    "        AND D.CVE_CONCEPTO      = C.CVE_CONCEPTO  \n"+
							    "        AND ROUND(D.IMP_CONCEPTO,2) > 0 \n"+
							    "        )\n"+
							    "        GROUP BY ID_PRESTAMO, CVE_CONCEPTO \n"+
							    "    )GROUP BY ID_ORDEN_TIPO, ID_ORDEN, ID_PRESTAMO, FECHA_OPERACION, DESCRIPCION \n"+
							         
							    "    ORDER BY FECHA_OPERACION, ID_ORDEN_TIPO, ID_ORDEN \n";
								
								PreparedStatement ps3 = this.conn.prepareStatement(sSql);
								ps3.execute();
								ResultSet rs3 = ps3.getResultSet();
								
								if (rs3.next()){
									sSaldo = rs3.getString("IMP_DESGLOSE");
									fSaldo = (Float.parseFloat(sSaldo));
									
								}
								
								fSaldo = fSaldo < 0 ? -fSaldo : fSaldo;
								
								if (fSaldo >= iDeudaMinima){
									iDeuda++;
								}
							}
						}
						if (iDeuda != 0){
							resultadoCatalogo.mensaje.setClave("PRESTAMO_VIGENTE_GRUPO");
						}else {
							//Cuenta los integrantes del grupo.
							sSql =  "SELECT COUNT(*) NUM_INTEGRANTES \n" +
								"FROM SIM_GRUPO_INTEGRANTE  \n"+
								"WHERE \n"+
								"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
								"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
								"AND FECHA_BAJA_LOGICA IS NULL \n" ;
							PreparedStatement ps4 = this.conn.prepareStatement(sSql);
							ps4.execute();
							ResultSet rs4 = ps4.getResultSet();
							if (rs4.next()){
								sNumIntegrantes = rs4.getString("NUM_INTEGRANTES");
							}
							
							//Obtiene de los par�metros globales del grupo el m�nimo de integrantes que debe tener un grupo.
							sSql =  "SELECT \n"+
								"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
								"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
							PreparedStatement ps5 = this.conn.prepareStatement(sSql);
							ps5.execute();
							ResultSet rs5 = ps5.getResultSet();
							
							if (rs5.next()){
								sMinIntegrantes = rs5.getString("MINIMO_INTEGRANTES");
							}
							
							//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que debe tener un grupo.
							sSql =  "SELECT \n"+
								"	MAX(NUM_INTEGRANTE) MAXIMO_INTEGRANTES \n"+
								"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
							PreparedStatement ps6 = this.conn.prepareStatement(sSql);
							ps6.execute();
							ResultSet rs6 = ps6.getResultSet();
							if (rs6.next()){
								sMaxIntegrantes = rs6.getString("MAXIMO_INTEGRANTES");
							}
							
							int iNumIntegrantes =Integer.parseInt(sNumIntegrantes.trim());
							int iMinIntegrantes =Integer.parseInt(sMinIntegrantes.trim());
							int iMaxIntegrantes =Integer.parseInt(sMaxIntegrantes.trim());
							
							//Compara si el n�mero de candidatos al grupo se encuentra dentro de los par�metros globales del grupo.
							if (iNumIntegrantes >= iMinIntegrantes){
								System.out.println("11");
								if (iNumIntegrantes <= iMaxIntegrantes){
									
									//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden estar en riesgo.
									sSql =  "SELECT \n"+
										"	MAXIMO_RIESGO \n"+
										"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
										"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
									PreparedStatement ps7 = this.conn.prepareStatement(sSql);
									ps7.execute();
									ResultSet rs7 = ps7.getResultSet();
									
									if (rs7.next()){
										sMaximoRiego = rs7.getString("MAXIMO_RIESGO");
									}
									
									int iMaximoRiego =Integer.parseInt(sMaximoRiego.trim());
									
									//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden tener negocios ambulantes.
									sSql =  "SELECT \n"+
										"	MAX_AMBULANTE \n"+
										"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
										"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
									PreparedStatement ps8 = this.conn.prepareStatement(sSql);
									ps8.execute();
									ResultSet rs8 = ps8.getResultSet();
									
									if (rs8.next()){
										sMaximoAmbulante = rs8.getString("MAX_AMBULANTE");
									}
									
									int iMaximoAmbulante =Integer.parseInt(sMaximoAmbulante.trim());
									
									//Obtiene de los par�metros globales del grupo el m�ximo de integrantes que pueden tener negocios de venta por cat�logo.
									sSql =  "SELECT \n"+
										"	MAX_CATALOGO \n"+
										"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
										"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
									PreparedStatement ps9 = this.conn.prepareStatement(sSql);
									ps9.execute();
									ResultSet rs9 = ps9.getResultSet();
									
									if (rs9.next()){
										sMaximoCatalogo = rs9.getString("MAX_CATALOGO");
									}
									
									int iMaximoCatalogo =Integer.parseInt(sMaximoCatalogo.trim());
									
									//Obtiene los candidatos propuestos a formar el grupo.
									sSql =  "SELECT ID_INTEGRANTE \n" +
										"FROM SIM_GRUPO_INTEGRANTE  \n"+
										"WHERE \n"+
										"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
										"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
										"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
									PreparedStatement ps10 = this.conn.prepareStatement(sSql);
									ps10.execute();
									ResultSet rs10 = ps10.getResultSet();		
										
									while (rs10.next()){
										
										sIdIntegrantes = rs10.getString("ID_INTEGRANTE");
										//Busca si el tipo de negocio del candidato es ambulante o de venta por cat�logo.
										sSql =  "SELECT NOM_NEGOCIO \n" +
											"FROM SIM_CLIENTE_NEGOCIO  \n"+
											"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND B_PRINCIPAL = 'V' \n" +
											"AND (ID_TIPO_NEGOCIO = '3' OR ID_TIPO_NEGOCIO = '4') \n" +
											"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
										
										PreparedStatement ps11 = this.conn.prepareStatement(sSql);
										ps11.execute();
										ResultSet rs11 = ps11.getResultSet();
							
										if (rs11.next()){
											iMaxRiesgoProp ++;
										}
										rs11.close();
										ps11.close();	
									}
									
									//Comprueba si el n�mero de candidatos en riesgo se encuentra dentro de los par�metros globales del grupo.
									if (iMaxRiesgoProp <= iMaximoRiego){
										
										//Obtiene los candidatos propuestos a formar el grupo.
										sSql =  "SELECT ID_INTEGRANTE \n" +
											"FROM SIM_GRUPO_INTEGRANTE  \n"+
											"WHERE \n"+
											"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
											"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
											"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
										ejecutaSql();			
										
										
										while (rs.next()){
											
											sIdIntegrantes = rs.getString("ID_INTEGRANTE");
											//Busca si el tipo de negocio del candidato es ambulante.
											sSql =  "SELECT NOM_NEGOCIO \n" +
												"FROM SIM_CLIENTE_NEGOCIO  \n"+
												"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"AND B_PRINCIPAL = 'V' \n" +
												"AND ID_TIPO_NEGOCIO = '3' \n" +
												"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
											
											PreparedStatement ps12 = this.conn.prepareStatement(sSql);
											ps12.execute();
											ResultSet rs12 = ps12.getResultSet();
								
											if (rs12.next()){
												iMaxAmbulanteProp ++;
											}
											rs12.close();
											ps12.close();
										}	
										System.out.println("12");
										//Comprueba si el n�mero de integrantes con negocios tipo ambulantes es el permitido.
										if (iMaxAmbulanteProp <= iMaximoAmbulante){
											
											//Obtiene los candidatos propuestos a formar el grupo.
											sSql =  "SELECT ID_INTEGRANTE \n" +
												"FROM SIM_GRUPO_INTEGRANTE  \n"+
												"WHERE \n"+
												"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
												"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
											PreparedStatement ps13 = this.conn.prepareStatement(sSql);
											ps13.execute();
											ResultSet rs13 = ps13.getResultSet();		
											
											
											while (rs13.next()){
												
												sIdIntegrantes = rs13.getString("ID_INTEGRANTE");
												//Busca si el tipo de negocio del candidato es venta por cat�logo.
												sSql =  "SELECT NOM_NEGOCIO \n" +
													"FROM SIM_CLIENTE_NEGOCIO  \n"+
													"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
													"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
													"AND B_PRINCIPAL = 'V' \n" +
													"AND ID_TIPO_NEGOCIO = '4' \n" +
													"AND ID_PERSONA = '" + sIdIntegrantes + "' \n";
												
												PreparedStatement ps14 = this.conn.prepareStatement(sSql);
												ps14.execute();
												ResultSet rs14 = ps14.getResultSet();
									
												if (rs14.next()){
													iMaxCatalogoProp ++;
												}
												rs14.close();
												ps14.close();
											}	
											
											//Comprueba si el n�mero de integrantes con negocios tipo venta por cat�logo es el permitido.
											if (iMaxCatalogoProp <= iMaximoCatalogo){
												
												//Verificamos si el producto tiene actividades o requisitos definidos.
												sSql =  "SELECT DISTINCT \n"+
													"ID_ETAPA_PRESTAMO \n"+
													"FROM \n"+
													"SIM_PRODUCTO_ETAPA_PRESTAMO \n"+
													"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
													"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
													"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n";
												
												ejecutaSql();
												
												if (rs.next()){
													
													//Verificamos si ya se le asigno el producto.
														sSql =  "SELECT \n"+
															"ID_GRUPO, \n"+
															"ID_PRODUCTO \n"+
															"FROM SIM_PRESTAMO_GRUPO \n"+
															"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
															"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
															"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n";
														
														ejecutaSql();
														
														if (rs.next()){
															//Obtiene el pr�ximo ciclo que le corresponde.
															sSql =  "SELECT \n" +
															"ID_PRODUCTO, \n"+
															"NUM_CICLO \n"+
															"FROM SIM_PRODUCTO_CICLO \n"+
															"WHERE \n"+
															"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
															"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
															"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
															"AND NUM_CICLO = (SELECT \n"+
																	"MAX(PG.NUM_CICLO)+1 NUM_CICLO_PROX \n"+
																	"FROM \n"+
																	"SIM_PRESTAMO_GRUPO PG, \n"+
																	"SIM_CAT_ETAPA_PRESTAMO E \n"+
																	"WHERE PG.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																	"AND PG.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																	"AND PG.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
																	"AND PG.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n"+
																	"AND E.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
																	"AND E.CVE_EMPRESA = PG.CVE_EMPRESA \n"+ 
																	"AND E.ID_ETAPA_PRESTAMO = PG.ID_ETAPA_PRESTAMO \n"+ 
																	"AND E.B_CANCELADO != 'V') \n";
																	
															ejecutaSql();
															if (rs.next()){
																sNumCiclo = rs.getString("NUM_CICLO");
																
																sSql = "Todavia no se da nungun alta";
																resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
																resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",sNumCiclo);
															}else{
																//Como no tiene otro ciclo definido se le asigna el ultimo.
																sSql =  "SELECT \n" +
																		"MAX (NUM_CICLO) NUM_CICLO \n"+
																		"FROM ( \n"+
																		"SELECT \n" +
																		"ID_PRODUCTO, \n"+
																		"NUM_CICLO \n"+
																		"FROM SIM_PRODUCTO_CICLO \n"+
																		"WHERE \n"+
																		"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
																		"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
																		"AND ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
																		") \n";
																	
																ejecutaSql();
																if (rs.next()){
																	registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																	
																	sSql = "Todavia no se da nungun alta";
																	resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																	resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																}
																
															}
															
														}else{
															
															//Busca si hay una excepci�n.
															sSql =  "SELECT \n"+
																	"P.CVE_GPO_EMPRESA, \n"+
																	"P.CVE_EMPRESA, \n"+
																	"P.ID_PRODUCTO, \n"+
																	"P.NUM_CICLO, \n"+
																	"P.ID_GRUPO \n"+
																	"FROM \n"+
																	"SIM_PRESTAMO_EXCEPCION_CICLO P \n"+
																	"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																	"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																	"AND P.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
																	"AND P.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n";
																	
																ejecutaSql();
																if (rs.next()){
																	registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																	
																	sSql = "SELECT DISTINCT \n"+
																			"PC.CVE_GPO_EMPRESA, \n"+
																			"PC.CVE_EMPRESA, \n"+
																			"PC.ID_PRODUCTO, \n"+
																			"P.NOM_PRODUCTO, \n"+
																			"P.APLICA_A, \n"+
																			"P.ID_PERIODICIDAD, \n"+
																			"P.CVE_METODO, \n"+
																			"PC.NUM_CICLO, \n"+
																			"PC.ID_FORMA_DISTRIBUCION, \n"+
																			"PC.PLAZO, \n"+
																			"PC.TIPO_TASA, \n"+
																			"PC.VALOR_TASA, \n"+
																			"PC.ID_PERIODICIDAD_TASA, \n"+
																			"PC.ID_TASA_REFERENCIA, \n"+
																			"PC.MONTO_MAXIMO, \n"+
																			"P.MONTO_MINIMO, \n"+
																			"PC.PORC_FLUJO_CAJA, \n"+
																			"P.FECHA_INICIO_ACTIVACION, \n"+
																			"P.FECHA_FIN_ACTIVACION \n"+
																			"FROM SIM_PRODUCTO_CICLO PC, \n"+ 
																			"SIM_PRODUCTO P, \n"+
																			"SIM_PRODUCTO_SUCURSAL PS \n"+
																			"WHERE PC.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																			"AND PC.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																			"AND P.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
																			"AND P.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
																			"AND P.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
																			"AND SYSDATE BETWEEN P.FECHA_INICIO_ACTIVACION AND P.FECHA_FIN_ACTIVACION+1 \n"+
																			"AND PC.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+	
																			"AND PC.NUM_CICLO = '" + (String)registro.getDefCampo("NUM_CICLO") + "' + '1' \n"+
																			"AND PS.CVE_GPO_EMPRESA = PC.CVE_GPO_EMPRESA \n"+
																			"AND PS.CVE_EMPRESA = PC.CVE_EMPRESA \n"+
																			"AND PS.ID_PRODUCTO = PC.ID_PRODUCTO \n"+
																			"AND P.APLICA_A = 'Grupo' \n";
																	ejecutaSql();
																	if (rs.next()){
																		registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																		
																		sSql = "Todavia no se da nungun alta";
																		resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
																		resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																	}else{
																		//Como no tiene otro ciclo definido se le asigna el ultimo.
																		sSql =  "SELECT \n"+
																		"P.CVE_GPO_EMPRESA, \n"+
																		"P.CVE_EMPRESA, \n"+
																		"P.ID_PRODUCTO, \n"+
																		"P.NUM_CICLO, \n"+
																		"P.ID_GRUPO \n"+
																		"FROM \n"+
																		"SIM_PRESTAMO_EXCEPCION_CICLO P \n"+
																		"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
																		"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
																		"AND P.ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
																		"AND P.ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n";
																		
																				
																		ejecutaSql();
																		if (rs.next()){
																			registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																			
																			sSql = "Todavia no se da nungun alta";
																			resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																			resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																		}
																	}
																	System.out.println("13");
																}else {
															//Se le asigna el producto-ciclo 1.
															//Ingresa el cr�dito grupal.
															//String sIdPrestamoGrupo = "";
															/*
															//OBTENEMOS EL SEQUENCE
															sSql = "SELECT SQ01_SIM_PRESTAMO_GRUPO.nextval as ID_PRESTAMO_GRUPO FROM DUAL";
															ejecutaSql();
															
															if (rs.next()){
																sIdPrestamoGrupo = rs.getString("ID_PRESTAMO_GRUPO");
															}
															*/
															sSql = "Todavia no se da nungun alta";
															resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
															resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO","1");
														}
															/*
															sSql =  "INSERT INTO SIM_PRESTAMO_GRUPO ( "+
																"CVE_GPO_EMPRESA, \n" +
																"CVE_EMPRESA, \n" +
																"ID_PRESTAMO_GRUPO, \n" +
																"ID_GRUPO, \n" +
																"ID_PRODUCTO, \n" +
																"NUM_CICLO) \n" +
																" VALUES (" +
																"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
																"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
																" " + sIdPrestamoGrupo +", \n" +
																"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
																"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
																"'1') \n" ;
																	
															System.out.println("*ALTA EN LA TABLA PADRE cilco 1*"+sSql);
															
															if (ejecutaUpdate() == 0){
																resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
															}
															*/
														}
												
													
												}else {
													//El producto no tiene actividades o requisitos definidos.
													resultadoCatalogo.mensaje.setClave("PRODUCTO_SIN_ACT_REQ");
												}
											
												
											}else{
												//El n�mero de integrantes con negocios tipo venta por cat�logo no es el permitido.
												System.out.println("**NO CUMPLE** con los tipo de negocio por catalogo permitido");
												resultadoCatalogo.mensaje.setClave("INTEGRANTES_CATALOGO_ERROR");
											}
												
										}else{
											//El n�mero de integrantes con negocios tipo ambulantes no es el permitido.
											System.out.println("**NO CUMPLE** con los tipo de negocio ambulante permitido");
											resultadoCatalogo.mensaje.setClave("INTEGRANTES_AMBULANTE_ERROR");
										}
										
									} else {
										//El n�mero de candidatos en riesgo no encuentra dentro de los par�metros globales del grupo.
										System.out.println("**NO CUMPLE** con los integrantes en riesgo permitidos");
										resultadoCatalogo.mensaje.setClave("INTEGRANTES_MAX_RIESGO_ERROR");
									}
								}else {
									//El n�mero de integrantes para formar el grupo no se encuentra dentro de los par�metros globales del grupo.
									System.out.println("***NO*** Cumple con los integrantes maximos");
									resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
								}
							}else {
								//El n�mero de integrantes para formar el grupo no se encuentra dentro de los par�metros globales del grupo.
								System.out.println("***NO*** Cumple con los integrantes m�nimos");
								resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
							}
							
							
						}
					}
				}//El porcentaje de fundadores del grupo es el permitido.
			}else {
				resultadoCatalogo.mensaje.setClave("INTEGRANTES_NO_NEGOCIO");
			}
		return resultadoCatalogo;
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET "+
				" PLAZO 		='" + (String)registro.getDefCampo("PLAZO") + "', \n" +
				" ID_PERIODICIDAD_PRODUCTO		='" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "', \n" +
				" VALOR_TASA 		='" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
				" ID_PERIODICIDAD_TASA		='" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
				" ID_TASA_REFERENCIA 	= '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA")  + "', \n" +
				" MONTO_MAXIMO 		='" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
				" ID_TIPO_RECARGO 		= '" + (String)registro.getDefCampo("ID_TIPO_RECARGO")  + "', \n" +
				" TIPO_TASA_RECARGO 		= '" + (String)registro.getDefCampo("TIPO_TASA_RECARGO")  + "', \n" +
				" ID_PERIODICIDAD_TASA_RECARGO 	= '" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO")  + "', \n" +
				" TASA_RECARGO 			= '" + (String)registro.getDefCampo("TASA_RECARGO")  + "', \n" +
				" ID_TASA_REFERENCIA_RECARGO 	= '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO")  + "', \n" +  
			    " FACTOR_TASA_RECARGO 		= '" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO")  + "', \n" +
				" MONTO_FIJO_PERIODO 		= '" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO")  + "' \n" +
				" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  " SELECT \n"+
				" ID_PRESTAMO \n"+
				" FROM \n"+
				" SIM_PRESTAMO_GPO_DET \n"+
				" WHERE ID_PRESTAMO_GRUPO 	='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
				" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		PreparedStatement ps1 = this.conn.prepareStatement(sSql);
		ps1.execute();
		ResultSet rs1 = ps1.getResultSet();
		while (rs1.next()){
			registro.addDefCampo("ID_PRESTAMO",rs1.getString("ID_PRESTAMO"));
			
			sSql =  " UPDATE SIM_PRESTAMO SET "+
					" PLAZO 		='" + (String)registro.getDefCampo("PLAZO") + "', \n" +
					" ID_PERIODICIDAD_PRODUCTO		='" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "', \n" +
					" VALOR_TASA 		='" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
					" ID_PERIODICIDAD_TASA		='" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
					" ID_TASA_REFERENCIA 	= '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA")  + "', \n" +
					" MONTO_MAXIMO 		='" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
					" ID_TIPO_RECARGO 		= '" + (String)registro.getDefCampo("ID_TIPO_RECARGO")  + "', \n" +
					" TIPO_TASA_RECARGO 		= '" + (String)registro.getDefCampo("TIPO_TASA_RECARGO")  + "', \n" +
					" ID_PERIODICIDAD_TASA_RECARGO 	= '" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO")  + "', \n" +
					" TASA_RECARGO 			= '" + (String)registro.getDefCampo("TASA_RECARGO")  + "', \n" +
					" ID_TASA_REFERENCIA_RECARGO 	= '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO")  + "', \n" +  
				    " FACTOR_TASA_RECARGO 		= '" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO")  + "', \n" +
					" MONTO_FIJO_PERIODO 		= '" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO")  + "' \n" +
					" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ResultSet rs2 = ps2.getResultSet();
		}
		
		return resultadoCatalogo;
	}
}