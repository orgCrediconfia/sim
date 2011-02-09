/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para la paginación del los préstamos grupales.
 */
 
public class SimPrestamoGrupalPaginacionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {

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
				" AND ROWNUM <= '"+ (String) parametros.getDefCampo("SUPERIOR") +"' \n";  
				
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
			
			sSql = sSql + " MINUS \n"+
							"SELECT \n"+
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
						" AND ROWNUM <= '"+ (String) parametros.getDefCampo("INFERIOR") +"' \n";  
						
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
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		sSql =	"SELECT TRUNC (HOJAS) PAGINAS FROM ( \n"+
				"SELECT PAGINACION / 100 AS HOJAS FROM ( \n"+
				"SELECT COUNT(*) PAGINACION FROM \n"+
				"( \n"+  
				"SELECT \n"+
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
			"FROM SIM_PRESTAMO_GRUPO P, \n"+
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
			"AND P.ID_GRUPO IS NOT NULL \n";
			
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
			
		sSql = sSql +	" ))) \n";
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}