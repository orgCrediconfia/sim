/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
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
 
public class SimCajaDesembolsoIndividualTransaccionDAO extends Conexion2 implements OperacionConsultaTabla {
	
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
				"T.CVE_GPO_EMPRESA, \n"+
				"T.CVE_EMPRESA, \n"+
				"T.ID_MOVIMIENTO_OPERACION, \n"+
				"T.ID_CLIENTE, \n"+
				"T.NUM_CICLO, \n"+ 
				"T.ID_PRESTAMO, \n"+
				"B.NOM_COMPLETO, \n"+
				"TO_CHAR(-T.MONTO,'999,999,999.99') MONTO_PRESTADO, \n"+
				"CANTIDADES_LETRAS(MONTO) MONTO_AUTORIZADO_LETRAS, \n"+
				"NVL(P.FECHA_ENTREGA,P.FECHA_REAL) FECHA, \n"+
				"T.ID_SUCURSAL, \n"+
				"A.NOM_SUCURSAL, \n"+
				"T.ID_CAJA, \n"+
				"D.CALLE||' '||D.NUMERO_INT||' '||'COL.'||' '||D.NOM_ASENTAMIENTO||' '||'C.P.'||' '||D.CODIGO_POSTAL||','||' '||D.NOM_DELEGACION||','||' '||D.NOM_ESTADO DOMICILIO \n"+
				"FROM SIM_CAJA_TRANSACCION T, \n"+
				"SIM_PRESTAMO P, \n"+
				"SIM_SUCURSAL_CAJA S, \n"+
				"RS_GRAL_PERSONA B, \n"+
				"SIM_CAT_SUCURSAL A, \n"+
				"RS_GRAL_DOMICILIO D, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				"WHERE T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND T.CVE_MOVIMIENTO_CAJA = 'DESIND' \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND S.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA = T.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL = T.ID_SUCURSAL \n"+
				"AND S.ID_SUCURSAL||'-'||S.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA_SUCURSAL") + "' \n"+
				"AND P.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND P.ID_PRESTAMO (+)= T.ID_PRESTAMO \n"+
				"AND B.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND B.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND B.ID_PERSONA (+)= T.ID_CLIENTE \n"+
				"AND A.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
				"AND A.CVE_EMPRESA = T.CVE_EMPRESA \n"+
				"AND A.ID_SUCURSAL = T.ID_SUCURSAL \n"+
				"AND D.CVE_GPO_EMPRESA (+)= A.CVE_GPO_EMPRESA \n"+
				"AND D.CVE_EMPRESA (+)= A.CVE_EMPRESA \n"+
				"AND D.ID_DOMICILIO (+)= A.ID_DIRECCION \n"+
				"AND US.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
	            "AND US.CVE_EMPRESA = T.CVE_EMPRESA \n"+
	            "AND US.ID_SUCURSAL = T.ID_SUCURSAL \n"+
	            "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + " AND CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(B.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
		}
		
		ejecutaSql();
		return getConsultaLista();
	}
}