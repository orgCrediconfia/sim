/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para los préstamos.
 */
 
public class SimPrestamoConsultaBitacoraActividadEstatusDAO extends Conexion2 implements OperacionConsultaTabla {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		int iNumPrestamos = 0;
		String sNumPrestamos = "";
		
		if (parametros.getDefCampo("PRESTAMO").equals("CREDITO")){
			sSql = "SELECT \n"+
				   "C.CVE_PRESTAMO, \n"+
				   "C.ID_PRESTAMO, \n"+
				   "C.APLICA_A, \n"+
				   "C.ID_PRODUCTO, \n"+
				   "C.NOMBRE, \n"+
				   "C.NUM_CICLO \n"+
				   "FROM V_CREDITO C, \n"+
				   "     SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				   "WHERE  C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			       "AND    C.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			       "AND C.APLICA_A != 'INDIVIDUAL_GRUPO' \n"+
			       "AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
	               "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
	               "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
	               "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
			
			if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
				
				sSql = sSql + " AND C.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
			}
			
			if (parametros.getDefCampo("NOMBRE") != null) {
				
				sSql = sSql + " AND UPPER(C.NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase()  + "%' \n";
			}
			
			sSql = sSql + "ORDER BY C.ID_PRESTAMO \n";
			System.out.println("consulta"+sSql);
			
		}else if (parametros.getDefCampo("PRESTAMO").equals("INDIVIDUAL")){
	
		sSql =  "SELECT \n"+
			"H.CVE_GPO_EMPRESA, \n" +
			"H.CVE_EMPRESA, \n" +
			"H.ID_HISTORICO, \n" +
			"H.ID_PRESTAMO, \n" +
			"H.ID_ACTIVIDAD_REQUISITO, \n" +
			"A.NOM_ACTIVIDAD_REQUISITO, \n" +
			"H.ID_ETAPA_PRESTAMO, \n" +
			"E.NOM_ESTATUS_PRESTAMO, \n" +
			"TO_CHAR(H.FECHA_REGISTRO,'DD/MM/YYYY HH24:MI:SS') FECHA_REGISTRO, \n" +
			"TO_CHAR(H.FECHA_REALIZADA,'DD/MM/YYYY HH24:MI:SS') FECHA_REALIZADA, \n" +
			"H.ESTATUS, \n" +
			"H.COMENTARIO, \n" +
			"H.CVE_USUARIO, \n" +
			"P.NOM_COMPLETO NOM_USUARIO, \n" +
			"H.ORDEN_ETAPA \n" +
			"FROM SIM_PRESTAMO_ETAPA_HISTORICO H, \n" +
			"SIM_CAT_ACTIVIDAD_REQUISITO A, \n" +
			"SIM_CAT_ETAPA_PRESTAMO E, \n" +
			"RS_GRAL_USUARIO U, \n" +
			"RS_GRAL_PERSONA P \n" +
			"WHERE H.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND H.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND H.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"AND A.CVE_GPO_EMPRESA = H.CVE_GPO_EMPRESA \n"+
			"AND A.CVE_EMPRESA = H.CVE_EMPRESA \n"+
			"AND A.ID_ACTIVIDAD_REQUISITO = H.ID_ACTIVIDAD_REQUISITO \n"+
			"AND E.CVE_GPO_EMPRESA = H.CVE_GPO_EMPRESA \n"+
			"AND E.CVE_EMPRESA = H.CVE_EMPRESA \n"+
			"AND E.ID_ETAPA_PRESTAMO = H.ID_ETAPA_PRESTAMO \n"+
			"AND U.CVE_GPO_EMPRESA (+)= H.CVE_GPO_EMPRESA \n"+
			"AND U.CVE_EMPRESA (+)= H.CVE_EMPRESA \n"+
			"AND U.CVE_USUARIO (+)= H.CVE_USUARIO \n"+
			"AND P.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA (+)= U.CVE_EMPRESA \n"+
			"AND P.ID_PERSONA (+)= U.ID_PERSONA \n"+
			"AND FECHA_REGISTRO IS NOT NULL \n"+
			"ORDER BY H.ORDEN_ETAPA, H.ID_HISTORICO \n";
		
		}else if (parametros.getDefCampo("PRESTAMO").equals("GRUPO")){
			
			sSql =  "SELECT \n"+
					"COUNT(*) NUM_PRESTAMOS \n" +
					"FROM SIM_PRESTAMO_GPO_DET \n" +
					"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
			ejecutaSql();
			if (rs.next()){
				sNumPrestamos = rs.getString("NUM_PRESTAMOS");
				iNumPrestamos = Integer.parseInt(sNumPrestamos.trim());
			}
			
			
			
			sSql =  "SELECT \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRESTAMO_GRUPO, \n" +
					"ID_PRESTAMO \n" +
					"FROM SIM_PRESTAMO_GPO_DET \n" +
					"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
			ejecutaSql();
			while (rs.next()){
				parametros.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO")== null ? "": rs.getString("ID_PRESTAMO"));
				
				
				sSql =  "SELECT \n"+
				"H.CVE_GPO_EMPRESA, \n" +
				"H.CVE_EMPRESA, \n" +
				"H.ID_HISTORICO, \n" +
				"H.ID_PRESTAMO, \n" +
				"H.ID_ACTIVIDAD_REQUISITO, \n" +
				"A.NOM_ACTIVIDAD_REQUISITO, \n" +
				"H.ID_ETAPA_PRESTAMO, \n" +
				"E.NOM_ESTATUS_PRESTAMO, \n" +
				"TO_CHAR(H.FECHA_REGISTRO,'DD/MM/YYYY HH24:MI:SS') FECHA_REGISTRO, \n" +
				"TO_CHAR(H.FECHA_REALIZADA,'DD/MM/YYYY HH24:MI:SS') FECHA_REALIZADA, \n" +
				"H.ESTATUS, \n" +
				"H.COMENTARIO, \n" +
				"H.CVE_USUARIO, \n" +
				"P.NOM_COMPLETO NOM_USUARIO, \n" +
				"H.ORDEN_ETAPA \n" +
				"FROM SIM_PRESTAMO_ETAPA_HISTORICO H, \n" +
				"SIM_CAT_ACTIVIDAD_REQUISITO A, \n" +
				"SIM_CAT_ETAPA_PRESTAMO E, \n" +
				"RS_GRAL_USUARIO U, \n" +
				"RS_GRAL_PERSONA P \n" +
				"WHERE H.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND H.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND H.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND A.CVE_GPO_EMPRESA = H.CVE_GPO_EMPRESA \n"+
				"AND A.CVE_EMPRESA = H.CVE_EMPRESA \n"+
				"AND A.ID_ACTIVIDAD_REQUISITO = H.ID_ACTIVIDAD_REQUISITO \n"+
				"AND E.CVE_GPO_EMPRESA = H.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA = H.CVE_EMPRESA \n"+
				"AND E.ID_ETAPA_PRESTAMO = H.ID_ETAPA_PRESTAMO \n"+
				"AND U.CVE_GPO_EMPRESA (+)= H.CVE_GPO_EMPRESA \n"+
				"AND U.CVE_EMPRESA (+)= H.CVE_EMPRESA \n"+
				"AND U.CVE_USUARIO (+)= H.CVE_USUARIO \n"+
				"AND P.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA (+)= U.CVE_EMPRESA \n"+
				"AND P.ID_PERSONA (+)= U.ID_PERSONA \n"+
				"AND FECHA_REGISTRO IS NOT NULL \n"+
				"ORDER BY H.ORDEN_ETAPA, H.ID_HISTORICO \n";
				
				if (iNumPrestamos > 1){
					sSql = sSql + "UNION ALL \n";
					iNumPrestamos--;
				}
				
				
			}
		}
		
		ejecutaSql();
		return getConsultaLista();
	}
}