/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionAlta; 
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
 
public class SimPrestamoProductoCicloModificacionDAO extends Conexion2 implements OperacionAlta {
	
	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql =  " UPDATE SIM_PRESTAMO SET "+
			" ID_PERIODICIDAD_PRODUCTO	='" + (String)registro.getDefCampo("ID_PERIODICIDAD_PRODUCTO") + "', \n" +
			" CVE_METODO			='" + (String)registro.getDefCampo("CVE_METODO") + "', \n" +
			" ID_FORMA_DISTRIBUCION		='" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
			" PLAZO				='" + (String)registro.getDefCampo("PLAZO") + "', \n" +
			" TIPO_TASA			='" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
			" VALOR_TASA			='" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
			" ID_PERIODICIDAD_TASA		='" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
			" ID_TASA_REFERENCIA		='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
			" WHERE ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		
		return resultadoCatalogo;
	}
}