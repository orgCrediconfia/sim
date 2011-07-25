/**
 * Sistema de administraciï¿½n de portales.
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

import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.rapidsist.prestamo.Grupo;

/**
 * Administra los accesos a la base de datos para dar de alta el grupo al que se le va a 
 * otorgar el prï¿½stamo grupal, validando el mï¿½nimo de porcentajes fundadores del grupo,
 * asï¿½ como que se cumplan los parï¿½metros globales del grupo (Mï¿½ximo de integrantes,
 * mï¿½nimo de integrantes, mï¿½ximo de integrantes en riesgo, mï¿½ximo de negocios ambulates y
 * mï¿½ximo de negocios por catï¿½logos).
 */
 
public class SimPrestamoGrupalDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de bï¿½squeda.
	 * @param parametros Parï¿½metros que se le envï¿½an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
					"P.ID_PRESTAMO_GRUPO, \n"+
					"P.CVE_PRESTAMO_GRUPO, \n"+
					"P.FECHA_ENTREGA, \n"+
					"P.FECHA_REAL, \n"+
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
			
		ejecutaSql();
		return getConsultaLista();
		
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parï¿½metros que se le envï¿½an a la consulta para obtener el registro
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
				"E.B_AUTORIZAR_COMITE, \n"+
				"E.B_LINEA_FONDEO, \n"+
				"E.B_DESEMBOLSO, \n"+
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
				"DECODE(PG.ID_TIPO_RECARGO,3,'Interés moratorio',4,'Monto fijo por periodo',5,'Interés moratorio y Monto fijo por periodo') TIPO_RECARGO, \n"+
				"DECODE(PG.TIPO_TASA_RECARGO,1,'Fija independiente',2,'Dependiente de la tasa de credito') TIPO_TASA_DE_RECARGO, \n"+
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
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
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
			String sSaldoTotal = "";
			float fSaldoTotal = 0;
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
			System.out.println("a.Verificamos si existen los fundadores del grupo");
			ejecutaSql();
			if (rs.next()){
				//Preguntamos por el parï¿½metro % mï¿½nimo de fundadores del grupo.
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
				System.out.println("b*********************.Preguntamos por el parï¿½metro % mï¿½nimo de fundadores del grupo.");
				ejecutaSql();
				if (rs.next()){
					sMinIntFundadores = rs.getString("MIN_INT_FUNDADORES");
					fMinIntFundadores = (Float.parseFloat(sMinIntFundadores));
				
				}
				
				//Obtenemos los integrantes que actualmente integran el grupo
				sSql = " SELECT \n" + 
					  " ID_INTEGRANTE \n" +
					  "	FROM SIM_GRUPO_INTEGRANTE  \n" +
					  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
					  "	AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
					  " AND FECHA_BAJA_LOGICA IS NULL \n";
				
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
					
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					ps1.execute();
					ResultSet rs1 = ps1.getResultSet();
					if (rs1.next()){
						fIntegrante++;
					}
				}
				
				if (fIntegrante < fMinIntFundadores){
					bMinFundadores = false;
				}else {
					bMinFundadores = true;
				}
				
			}else {
				
				sSql = "SELECT \n" + 
				   "ID_GRUPO \n" + 
				   "FROM \n" + 
				   "SIM_PRESTAMO_EXCEPCION_CICLO \n" +
				   "WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   "AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
				   "AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
				ejecutaSql();
				if (rs.next()){
				
					//Error esto no es posible, estï¿½n no se dieron los integrantes fundadores en la migraciï¿½n.
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
					
					
					PreparedStatement ps1 = this.conn.prepareStatement(sSql);
					
					ps1.execute();
					
					ResultSet rs1 = ps1.getResultSet();
					
				}
				
				bMinFundadores = true;
				
				}
				
			}
		
			
			
			
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
					"AND CN.CVE_CLASE IS NOT NULL \n" +
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
				
					//Preguntamos por el parï¿½metro Crï¿½dito Simultï¿½neos.
					
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
						
						//Obtiene de los parï¿½metros globales del grupo el mï¿½nimo de integrantes que debe tener un grupo.
						sSql =  "SELECT \n"+
							"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
							"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
						ejecutaSql();
						
						if (rs.next()){
							sMinIntegrantes = rs.getString("MINIMO_INTEGRANTES");
						}
						
						//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que debe tener un grupo.
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
						
						//Compara si el nï¿½mero de candidatos al grupo se encuentra dentro de los parï¿½metros globales del grupo.
						if (iNumIntegrantes >= iMinIntegrantes){
						
							if (iNumIntegrantes <= iMaxIntegrantes){
								
								//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que pueden estar en riesgo.
								sSql =  "SELECT \n"+
									"	MAXIMO_RIESGO \n"+
									"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
									"WHERE NUM_INTEGRANTE = '"+iNumIntegrantes+"' \n";
								ejecutaSql();
								
								if (rs.next()){
									sMaximoRiego = rs.getString("MAXIMO_RIESGO");
								}
								
								int iMaximoRiego =Integer.parseInt(sMaximoRiego.trim());
								
								//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que pueden tener negocios ambulantes.
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
								
								//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que pueden tener negocios de venta por catï¿½logo.
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
									//Busca si el tipo de negocio del candidato es ambulante o de venta por catï¿½logo.
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
							
								//Comprueba si el nï¿½mero de candidatos en riesgo se encuentra dentro de los parï¿½metros globales del grupo.
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
										
									//Comprueba si el nï¿½mero de integrantes con negocios tipo ambulantes es el permitido.
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
											//Busca si el tipo de negocio del candidato es venta por catï¿½logo.
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
										
										//Comprueba si el nï¿½mero de integrantes con negocios tipo venta por catï¿½logo es el permitido.
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
														
														//Obtiene el prï¿½ximo ciclo que le corresponde.
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
															
															sSql = "Todavia no se da ningún alta";
															resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
															resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",sNumCiclo);
														}else{
															//Como no tiene otro ciclo definido se le asigna el ï¿½ltimo.
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
																
																sSql = "Todavia no se da ningún alta";
																
																resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
															}
														}
														
													}else{
														
														//Busca si hay una excepciï¿½n.
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
																	
																	sSql = "Todavia no se da ningún alta";
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
																		
																		sSql = "Todavia no se da ningún alta";
																		resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																		resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																	}
																}
														
															}else {
														
														//Se le asigna el producto-ciclo 1.
														//Ingresa el crï¿½dito grupal.
														
														sSql = "Todavia no se da ningún alta";
														resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
														resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO","1");
															}
														
													}
											
												
											}else {
												//El producto no tiene actividades o requisitos definidos.
												resultadoCatalogo.mensaje.setClave("PRODUCTO_SIN_ACT_REQ");
											}
										
										}else{
											//El nï¿½mero de integrantes con negocios tipo venta por catï¿½logo no es el permitido.
											System.out.println("**NO CUMPLE** con los tipo de negocio por catalogo permitido");
											resultadoCatalogo.mensaje.setClave("INTEGRANTES_CATALOGO_ERROR");
										}
											
									}else{
										//El nï¿½mero de integrantes con negocios tipo ambulantes no es el permitido.
										System.out.println("**NO CUMPLE** con los tipo de negocio ambulante permitido");
										resultadoCatalogo.mensaje.setClave("INTEGRANTES_AMBULANTE_ERROR");
									}
									
								} else {
									//El nï¿½mero de candidatos en riesgo no encuentra dentro de los parï¿½metros globales del grupo.
									System.out.println("**NO CUMPLE** con los integrantes en riesgo permitidos");
									resultadoCatalogo.mensaje.setClave("INTEGRANTES_MAX_RIESGO_ERROR");
								}
							}else {
								//El nï¿½mero de integrantes para formar el grupo no se encuentra dentro de los parï¿½metros globales del grupo.
								System.out.println("***NO*** Cumple con los integrantes maximos");
								resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
							}
						}else {
							//El nï¿½mero de integrantes para formar el grupo no se encuentra dentro de los parï¿½metros globales del grupo.
							System.out.println("***NO*** Cumple con los integrantes mï¿½nimos");
							resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
						}
						
						
					}else{
						//No acepta créditos simultáneos.
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
						
							//Busca todos los creditos del cliente.
							sSql =  "SELECT \n" +
									"P.ID_PRESTAMO \n" +
									"FROM SIM_PRESTAMO P \n" +
									"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
									"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
									"AND P.ID_CLIENTE = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" ;
							PreparedStatement ps2 = this.conn.prepareStatement(sSql);
							ps2.execute();
							ResultSet rs2 = ps2.getResultSet();
							
							while (rs2.next()){
							
								registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO")== null ? "": rs2.getString("ID_PRESTAMO"));
								
								sSql= "SELECT V.CVE_GPO_EMPRESA, \n" +
								   "V.CVE_EMPRESA, \n" +
								   "V.Id_Prestamo, \n" +
								   "DECODE(SUM(V.IMP_SALDO_HOY),null,'NADA',SUM(V.IMP_SALDO_HOY)) IMP_SALDO_HOY \n" + 
								   "From V_SIM_PRESTAMO_RES_EDO_CTA V, \n" +
								   "SIM_PRESTAMO P \n" +
								   "WHERE V.DESC_MOVIMIENTO IN ('Pago Tardío','Pago Pago Tardío','Seguro Deudor','Pago Seguro Deudor','Capital','Pago Capital','Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra') \n" + 
						           "AND V.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						           "AND P.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n" +
					               "AND P.CVE_EMPRESA = V.CVE_EMPRESA \n" +
					               "AND P.ID_PRESTAMO = V.ID_PRESTAMO \n" + 
					               "AND P.ID_ETAPA_PRESTAMO != '16' \n" +
								   "GROUP BY V.CVE_GPO_EMPRESA, V.CVE_EMPRESA, V.Id_Prestamo \n" ;
								PreparedStatement ps3 = this.conn.prepareStatement(sSql);
								ps3.execute();
								ResultSet rs3 = ps3.getResultSet();
								
								if (rs3.next()){
									sSaldo = rs3.getString("IMP_SALDO_HOY");
									fSaldo = (Float.parseFloat(sSaldo));
								}
								
								if (fSaldo >= 0){
									
									//Si no tiene saldo a la fecha consulta el saldo total.
									
									sSql =	"SELECT \n"+
											"A.CVE_GPO_EMPRESA, \n"+
											"A.CVE_EMPRESA, \n"+
											"A.Id_Prestamo, \n"+
											"DECODE(SUM(IMP_NETO),null,'NADA',SUM(IMP_NETO)) SALDO_TOTAL \n" + 
											"FROM  \n"+
											"V_SIM_TABLA_AMORT_CONCEPTO A, \n"+
											"PFIN_CAT_CONCEPTO B, \n"+
											"SIM_PRESTAMO P \n"+
											"WHERE A.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
											"AND A.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
											"AND A.ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
											"AND B.CVE_GPO_EMPRESA   = A.CVE_GPO_EMPRESA \n"+
											"AND B.CVE_EMPRESA       = A.CVE_EMPRESA \n"+
											"AND B.CVE_CONCEPTO      = A.CVE_CONCEPTO \n"+
											"AND A.IMP_ORIGINAL     <> 0 \n"+
											"AND P.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
								            "AND P.CVE_EMPRESA = A.CVE_EMPRESA \n"+
								            "AND P.ID_PRESTAMO = A.ID_PRESTAMO \n"+
								            "AND P.ID_ETAPA_PRESTAMO != '16' \n"+
								            "GROUP BY A.CVE_GPO_EMPRESA, A.CVE_EMPRESA, A.Id_Prestamo \n";
									
									PreparedStatement ps15 = this.conn.prepareStatement(sSql);
									ps15.execute();
									ResultSet rs15 = ps15.getResultSet();
									
									if (rs15.next()){
										sSaldoTotal = rs15.getString("SALDO_TOTAL");
										fSaldoTotal = (Float.parseFloat(sSaldoTotal));
										fSaldo = fSaldoTotal;
									}else{
										sSql =	"SELECT \n"+
												"CVE_GPO_EMPRESA, \n"+
												"CVE_EMPRESA, \n"+
												"Id_Prestamo, \n"+
												"FECHA_ENTREGA \n"+
												"FROM SIM_PRESTAMO \n"+
												"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
												"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
												"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
												"AND FECHA_ENTREGA IS NULL \n"+
												"AND ID_ETAPA_PRESTAMO != '16' \n";
										PreparedStatement ps16 = this.conn.prepareStatement(sSql);
										ps16.execute();
										ResultSet rs16 = ps16.getResultSet();
										
										if (rs16.next()){
											iDeuda++;
										}
									}
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
							
							//Obtiene de los parï¿½metros globales del grupo el mï¿½nimo de integrantes que debe tener un grupo.
							sSql =  "SELECT \n"+
								"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
								"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
							PreparedStatement ps5 = this.conn.prepareStatement(sSql);
							ps5.execute();
							ResultSet rs5 = ps5.getResultSet();
							
							if (rs5.next()){
								sMinIntegrantes = rs5.getString("MINIMO_INTEGRANTES");
							}
							
							//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que debe tener un grupo.
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
							
							//Compara si el nï¿½mero de candidatos al grupo se encuentra dentro de los parï¿½metros globales del grupo.
							if (iNumIntegrantes >= iMinIntegrantes){
								
								if (iNumIntegrantes <= iMaxIntegrantes){
									
									//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que pueden estar en riesgo.
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
									
									//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que pueden tener negocios ambulantes.
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
									
									//Obtiene de los parï¿½metros globales del grupo el mï¿½ximo de integrantes que pueden tener negocios de venta por catï¿½logo.
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
										//Busca si el tipo de negocio del candidato es ambulante o de venta por catï¿½logo.
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
									
									//Comprueba si el nï¿½mero de candidatos en riesgo se encuentra dentro de los parï¿½metros globales del grupo.
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
										
										//Comprueba si el nï¿½mero de integrantes con negocios tipo ambulantes es el permitido.
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
												//Busca si el tipo de negocio del candidato es venta por catï¿½logo.
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
											
											//Comprueba si el nï¿½mero de integrantes con negocios tipo venta por catï¿½logo es el permitido.
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
															"AND E.B_CANCELADO != 'V' \n";
													System.out.println("Verificamos si ya se le asigno el producto"+sSql);
													ejecutaSql();
														
													if (rs.next()){
														//Obtiene el prï¿½ximo ciclo que le corresponde.
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
															System.out.println("Obtiene el prï¿½ximo ciclo que le corresponde"+sSql);
															ejecutaSql();
															if (rs.next()){
																sNumCiclo = rs.getString("NUM_CICLO");
																
																sSql = "Todavia no se da ningún alta";
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
																System.out.println("****Como no tiene otro ciclo definido se le asigna el ultimo"+sSql);
																ejecutaSql();
																if (rs.next()){
																	registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																	
																	sSql = "Todavia no se da ningún alta";
																	resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																	resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																}
																
															}
															
														}else{
															
															//Busca si hay una excepciï¿½n.
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
															System.out.println("Busca si hay una excepciï¿½n."+sSql);
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
																		
																		sSql = "Todavia no se da ningún alta";
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
																		System.out.println("Como no tiene otro ciclo definido se le asigna el ultimo."+sSql);
																				
																		ejecutaSql();
																		if (rs.next()){
																			registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
																			
																			sSql = "Todavia no se da ningún alta";
																			resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","F");
																			resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
																		}
																	}
																	
																}else {
															//Se le asigna el producto-ciclo 1.
															//Ingresa el crï¿½dito grupal.
															
															sSql = "Todavia no se da ningún alta";
															resultadoCatalogo.Resultado.addDefCampo("B_EXISTE_CICLO_SIG","V");
															resultadoCatalogo.Resultado.addDefCampo("NUM_CICLO","1");
														}
															
														}
												
													
												}else {
													//El producto no tiene actividades o requisitos definidos.
													resultadoCatalogo.mensaje.setClave("PRODUCTO_SIN_ACT_REQ");
												}
											
												
											}else{
												//El nï¿½mero de integrantes con negocios tipo venta por catï¿½logo no es el permitido.
												System.out.println("**NO CUMPLE** con los tipo de negocio por catalogo permitido");
												resultadoCatalogo.mensaje.setClave("INTEGRANTES_CATALOGO_ERROR");
											}
												
										}else{
											//El nï¿½mero de integrantes con negocios tipo ambulantes no es el permitido.
											System.out.println("**NO CUMPLE** con los tipo de negocio ambulante permitido");
											resultadoCatalogo.mensaje.setClave("INTEGRANTES_AMBULANTE_ERROR");
										}
										
									} else {
										//El nï¿½mero de candidatos en riesgo no encuentra dentro de los parï¿½metros globales del grupo.
										System.out.println("**NO CUMPLE** con los integrantes en riesgo permitidos");
										resultadoCatalogo.mensaje.setClave("INTEGRANTES_MAX_RIESGO_ERROR");
									}
								}else {
									//El nï¿½mero de integrantes para formar el grupo no se encuentra dentro de los parï¿½metros globales del grupo.
									System.out.println("***NO*** Cumple con los integrantes maximos");
									resultadoCatalogo.mensaje.setClave("INTEGRANTES_GRUPO_ERROR");
								}
							}else {
								//El nï¿½mero de integrantes para formar el grupo no se encuentra dentro de los parï¿½metros globales del grupo.
								System.out.println("***NO*** Cumple con los integrantes mï¿½nimos");
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
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
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