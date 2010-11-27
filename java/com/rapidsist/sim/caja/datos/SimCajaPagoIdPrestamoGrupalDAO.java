/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para la Distribución automática del pago grupal.
 */
 
public class SimCajaPagoIdPrestamoGrupalDAO extends Conexion2 implements OperacionConsultaRegistro  {
	

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
				"PG.ID_PRESTAMO_GRUPO \n"+
				"FROM \n"+
				"SIM_PRESTAMO_GRUPO PG, \n"+   
				"SIM_CAT_ETAPA_PRESTAMO E \n"+
				"WHERE PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		        "AND PG.CVE_EMPRESA       = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
		        "AND PG.ID_GRUPO          = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
		        "AND PG.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND PG.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' \n"+
		        "AND PG.ID_ETAPA_PRESTAMO != '18' \n"+
		        "AND E.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
				"AND E.CVE_EMPRESA = PG.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO = PG.ID_ETAPA_PRESTAMO \n"+ 
				"AND E.B_CANCELADO != 'V' \n"; 
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}