/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para obtenr el identificador del pr�stamo en base a su clave.
 */
 
public class SimGarantiasPrestamoDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
				  "CG.DESCRIPCION, \n"+
				  "TO_CHAR(CG.VALOR_COMERCIAL, '999,999,999.99')AS VALOR_COMERCIAL, \n"+
				  "CG.NUMERO_FACTURA_ESCRITURA, \n"+
				  "CG.FECHA_FACTURA_ESCRITURA \n"+
			    "FROM SIM_CLIENTE_GARANTIA CG, \n"+
			      "SIM_PRESTAMO_GARANTIA PG \n"+
		        "WHERE CG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				  "AND CG.CVE_EMPRESA     = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				  "AND CG.ID_GARANTIA     = PG.ID_GARANTIA \n"+
				  "AND PG.ID_PRESTAMO     = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";

		
		System.out.println("Muestra consulta: " + sSql);
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	

}