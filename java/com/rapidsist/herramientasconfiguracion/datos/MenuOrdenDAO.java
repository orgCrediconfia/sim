/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.*;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el proceso de ordenamiento
 * de menús.
 */
public class MenuOrdenDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = new LinkedList();

		sSql = "SELECT MEN.ID_APLICACION, MEN.ID_OPCION_PADRE, "+
					"APL.NOM_APLICACION, FUN.NOM_FUNCION "+
				" FROM "+
					" MENU MEN,"+
					" APLICACION APL, "+
					" FUNCION FUN "+
				" WHERE MEN.ID_OPCION ="+(String)parametros.getDefCampo("ID_OPCION")+" "+
					" AND MEN.ID_APLICACION = APL.ID_APLICACION "+
					" AND MEN.ID_FUNCION = FUN.ID_FUNCION"+
					" AND MEN.SITUACION = 'AC' ";
		ejecutaSql();

		Registro registro= new Registro();
		if (rs.next()){
			// ASIGNA ESTOS VALORES AL REGISTRO CUANDO ENCUENTRA REGISTROS
			registro.addDefCampo("ID_APLICACION",rs.getString("ID_APLICACION"));
			registro.addDefCampo("ID_OPCION_PADRE",rs.getString("ID_OPCION_PADRE"));
			registro.addDefCampo("NOM_APLICACION",rs.getString("NOM_APLICACION"));
			registro.addDefCampo("NOM_FUNCION",rs.getString("NOM_FUNCION"));
		}
		else{
			// ASIGNA ESTOS VALORES AL REGISTRO CUANDO NO ENCUENTRA REGISTROS
			registro.addDefCampo("ID_APLICACION","");
			registro.addDefCampo("ID_OPCION_PADRE",null);
			registro.addDefCampo("NOM_APLICACION","");
			registro.addDefCampo("NOM_FUNCION",(String)registro.getDefCampo("NOM_FUNCION"));
		}
		if (((String)registro.getDefCampo("ID_OPCION_PADRE"))== null){
			// EJECUTA ESTE QRY CUANDO NO HAY REGISTROS PADRE
			sSql = "SELECT "+
						"MEN.ID_OPCION, "+
						"MEN.ID_OPCION_PADRE, "+
						"MEN.ID_FUNCION, "+
						"MEN.ID_APLICACION,"+
						"MEN.NOM_OPCION, "+
						"MEN.B_BLOQUEADO, "+
						"MEN.NUM_POSICION, "+
						"MEN.FOL_BITACORA, "+
						"MEN.SITUACION, "+
						"APL.NOM_APLICACION, "+
						"FUN.NOM_FUNCION "+
					" FROM MENU MEN, "+
						" APLICACION APL, "+
						" FUNCION FUN "+
					" WHERE MEN.ID_APLICACION = APL.ID_APLICACION "+
							" AND MEN.ID_FUNCION = FUN.ID_FUNCION "+
							" AND MEN.ID_APLICACION = "+(String)registro.getDefCampo("ID_APLICACION")+" "+
							" AND MEN.ID_OPCION_PADRE IS NULL "+
					" ORDER BY MEN.NUM_POSICION";
		}
		else {
			// CUANDO EXISTEN OPCIONES PADRE, ES DECIR, CUANDO HAY SUBOPCIONES
			// SE EJECUTA ESTE QRY.
			sSql = "SELECT "+
						"MEN.ID_OPCION, "+
						"MEN.ID_OPCION_PADRE, "+
						"MEN.ID_FUNCION, "+
						"MEN.ID_APLICACION,"+
						"MEN.NOM_OPCION, "+
						"MEN.B_BLOQUEADO, "+
						"MEN.NUM_POSICION, "+
						"MEN.FOL_BITACORA, "+
						"MEN.SITUACION, "+
						"APL.NOM_APLICACION, "+
						"FUN.NOM_FUNCION "+
					" FROM MENU MEN, "+
						" APLICACION APL, "+
						" FUNCION FUN "+
					" WHERE MEN.ID_APLICACION = APL.ID_APLICACION "+
							" AND MEN.ID_FUNCION = FUN.ID_FUNCION "+
							" AND MEN.ID_APLICACION = "+(String)registro.getDefCampo("ID_APLICACION")+" "+
							" AND MEN.ID_OPCION_PADRE = "+(String)registro.getDefCampo("ID_OPCION_PADRE")+" "+
					" ORDER BY MEN.NUM_POSICION";
		}
		ejecutaSql();
		while (rs.next()){
			registro = new Registro();
			registro.addDefCampo("ID_OPCION",rs.getString("ID_OPCION"));
			registro.addDefCampo("ID_OPCION_PADRE",rs.getString("ID_OPCION_PADRE"));
			registro.addDefCampo("ID_FUNCION",rs.getString("ID_FUNCION"));
			registro.addDefCampo("ID_APLICACION",rs.getString("ID_APLICACION"));
			registro.addDefCampo("NOM_OPCION",rs.getString("NOM_OPCION"));
			registro.addDefCampo("B_BLOQUEADO",rs.getString("B_BLOQUEADO"));
			registro.addDefCampo("NUM_POSICION",rs.getString("NUM_POSICION"));
			registro.addDefCampo("FOL_BITACORA",rs.getString("FOL_BITACORA"));
			registro.addDefCampo("SITUACION",rs.getString("SITUACION"));
			registro.addDefCampo("NOM_APLICACION",rs.getString("NOM_APLICACION"));
			registro.addDefCampo("NOM_FUNCION",rs.getString("NOM_FUNCION"));
			lista.add(registro);
		}
		return lista;
	}

	/**
	 * Obtiene un registro en base a la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{

		sSql = "SELECT MEN.ID_APLICACION, MEN.ID_OPCION_PADRE, "+
					"APL.NOM_APLICACION, FUN.NOM_FUNCION "+
				" FROM "+
					" MENU MEN,"+
					" APLICACION APL, "+
					" FUNCION FUN "+
				" WHERE MEN.ID_OPCION ="+(String)parametros.getDefCampo("ID_OPCION")+" "+
					" AND MEN.ID_APLICACION = APL.ID_APLICACION "+
					" AND MEN.ID_FUNCION = FUN.ID_FUNCION "+
					" AND MEN.SITUACION = 'AC' ";
		ejecutaSql();
		if (rs.next()){
			// ASIGNA ESTOS VALORES AL REGISTRO CUANDO ENCUENTRA REGISTROS
			parametros.addDefCampo("ID_APLICACION",rs.getString("ID_APLICACION"));
			parametros.addDefCampo("ID_OPCION_PADRE",rs.getString("ID_OPCION_PADRE"));
			parametros.addDefCampo("NOM_APLICACION",rs.getString("NOM_APLICACION"));
			parametros.addDefCampo("NOM_FUNCION",rs.getString("NOM_FUNCION"));
		}
		else{
			// ASIGNA ESTOS VALORES AL REGISTRO CUANDO NO ENCUENTRA REGISTROS
			parametros.addDefCampo("ID_APLICACION","");
			parametros.addDefCampo("ID_OPCION_PADRE","");
			parametros.addDefCampo("NOM_APLICACION","No hay, pues estas son las opciones principales de las que deriban las subopciones.");
			parametros.addDefCampo("NOM_FUNCION","Estas son las aplicaciones");
		}
		return parametros;
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		return null;
	}

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();

		sSql = "UPDATE MENU SET "+
					"NUM_POSICION ='" + (String)registro.getDefCampo("NUM_POSICION")+ "' "+
				" WHERE ID_OPCION ='" + (String)registro.getDefCampo("ID_OPCION")+"'";

		//VERIFICA SI NO SE MODIFICO EL REGISTRO
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
		return null;
	}
}