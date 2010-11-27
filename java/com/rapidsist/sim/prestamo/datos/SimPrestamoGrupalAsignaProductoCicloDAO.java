/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para dar de alta el grupo al que se le va a 
 * otorgar el préstamo grupal, validando el mínimo de porcentajes fundadores del grupo,
 * así como que se cumplan los parámetros globales del grupo (Máximo de integrantes,
 * mínimo de integrantes, máximo de integrantes en riesgo, máximo de negocios ambulates y
 * máximo de negocios por catálogos).
 */
 
public class SimPrestamoGrupalAsignaProductoCicloDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}

}