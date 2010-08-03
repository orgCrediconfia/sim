/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.publicaciones.datos.PubAdministracionCarpetas;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de secciones.
 */
public class PublSeccionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		boolean bNomSeccion = false;
		LinkedList lista = null;
		String sFiltro = (String)parametros.getDefCampo("Filtro");
	   ConstruyeSeccion construyeSeccion = new ConstruyeSeccion();
	   Conexion2 conexion2 = new Conexion2();
	   conexion2.setConexion( getConexion() );
	   LinkedList listaSeccion = construyeSeccion.getSeccion(conexion2 , parametros);
	   lista = listaSeccion;
	   return listaSeccion;
	}

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = " SELECT \n"+
			   " PS.CVE_GPO_EMPRESA, \n"+
			   " PS.CVE_PORTAL, \n"+
			   " PS.CVE_SECCION, \n"+
			   " PS.CVE_SECCION_PADRE, \n"+
			   " PS.CVE_GPO_RESP, \n"+
			   " PS.NOM_SECCION, \n"+
			   " PS.B_BLOQUEADO, \n"+
			   " PS.NUM_ORDEN, \n"+
			   " GR.CVE_GPO_RESP, \n"+
			   " GR.DESC_GPO_RESP \n"+
			   " FROM \n"+
			   " RS_PUB_SECCION PS, RS_PUB_CONFIG_GPO_RESP GR \n"+
			   " WHERE PS.CVE_PORTAL ='" + (String)parametros.getDefCampo("CVE_PORTAL") + "' \n"+
			   " AND PS.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND PS.CVE_SECCION = '" + (String)parametros.getDefCampo("CVE_SECCION") + "' \n"+
			   " AND PS.CVE_GPO_RESP = GR.CVE_GPO_RESP \n"+
			   " AND  PS.CVE_GPO_EMPRESA = GR.CVE_GPO_EMPRESA \n";

		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		//OBTIENE EL REGISTRO DE LA SECCION
		Registro registroBusqueda = new Registro();
		registroBusqueda = getRegistro(registro);
		//VERIFICA SI LA SECCION EXISTE
		if (registroBusqueda == null){
			//LA SECCION NO EXISTE Y PUEDA DARSE DE ALTA
		
			boolean bCrearCarpeta = true;
			//LOGICA DE ORDENAMIENTO
			long lOrdenamiento = 0;
			String sSeccionPadre = (String)registro.getDefCampo("CVE_SECCION_PADRE");
			//SI LA SECCIÓN DEL PADRE ES NULO LA SENTANCIA SE FORMA COMO "IS NULL"
			if (sSeccionPadre.equals("")){
				sSeccionPadre = " IS NULL ";
			} else {
				//SI LA SECCIÓN DEL PADRE ES DIFERENTE DE NULO SE PONE LA SENTANCIA S
				sSeccionPadre = " = '"+(String)registro.getDefCampo("CVE_SECCION_PADRE")+"' ";
			}
			sSql = " SELECT MAX(NUM_ORDEN) \n" +
				" FROM RS_PUB_SECCION  \n" +
				" WHERE CVE_SECCION_PADRE " + sSeccionPadre +" \n" +
				" AND CVE_GPO_EMPRESA       = '" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND CVE_PORTAL       = '" + (String) registro.getDefCampo("CVE_PORTAL") + "' \n";
			ejecutaSql();
			while(rs.next()){
				lOrdenamiento = rs.getLong(1);
				lOrdenamiento++;
			}
	
			sSql = "INSERT INTO RS_PUB_SECCION ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_PORTAL, \n" +
						"CVE_SECCION, \n" +
						"CVE_SECCION_PADRE, \n" +
						"CVE_GPO_RESP, \n" +
						"NOM_SECCION, \n" +
						"B_BLOQUEADO, \n" +
						"NUM_ORDEN ) \n"+
					"VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_PORTAL") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_SECCION")  + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_SECCION_PADRE")  + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_GPO_RESP")  + "', \n" +
						"'" + (String)registro.getDefCampo("NOM_SECCION")  + "', \n" +
						"'" + (String)registro.getDefCampo("B_BLOQUEADO")  + "', \n" +
						""  + lOrdenamiento+ ") \n";
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
	
			try{
				PubAdministracionCarpetas admoncarpetas = new PubAdministracionCarpetas();
				bCrearCarpeta = admoncarpetas.CrearSeccion(registro, this);
			}catch(Exception e){
				e.printStackTrace();
			}
			if (!bCrearCarpeta){
				sSql = "NO SE PUDO CREAR LA CARPETA";
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setTipo("Error");
					resultadoCatalogo.mensaje.setClave("PUBLICACION_NO_SE_CREO_CARPETA");
					resultadoCatalogo.mensaje.setDescripcion("No se pudo crear la carpeta");
				}
			}
		}
		else{
			resultadoCatalogo.mensaje.setClave("PUBLICACION_SECCION_EXISTE");
		}//VERIFICA SI LA SECCION EXISTE
			
		return resultadoCatalogo;
	}

	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = " UPDATE RS_PUB_SECCION SET "+
			   " CVE_SECCION_PADRE      ='" + (String)registro.getDefCampo("CVE_SECCION_PADRE")      + "', \n" +
			   " CVE_GPO_RESP           ='" + (String)registro.getDefCampo("CVE_GPO_RESP")  + "', \n" +
			   " NOM_SECCION            ='" + (String)registro.getDefCampo("NOM_SECCION")   + "', \n" +
			   " B_BLOQUEADO            ='" + (String)registro.getDefCampo("B_BLOQUEADO")   + "' \n" +
			   " WHERE CVE_SECCION      ='" + (String)registro.getDefCampo("CVE_SECCION")       + "' \n" +
			   " AND CVE_GPO_EMPRESA    ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_PORTAL        ='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
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
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();

		LinkedList listaRuta = new LinkedList();
		PubAdministracionCarpetas admoncarpetas = new PubAdministracionCarpetas();
		try {
			//SE DETERMINA LA LISTA DE DIRECTORIOS PARA OBTENER EL DIRECTORIO QUE SE DESEA BORRAR
			//EL PORQUE SE INDICA ANTES DE QUE SE BORRE ES PRECISAMENTE PORQUE CUANDO SE BORRA
			//DE LA BASE NO HAY FORMA DE OBTENER LA RUTA COMPLETA QUE SE DEBE BORRAR EN DISCO DURO
			listaRuta = admoncarpetas.ConstruyeListaRuta(registro, this);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		//VERIFICA SI TIENE HIJOS
		sSql = "SELECT * FROM RS_PUB_SECCION \n"+
			   " WHERE CVE_SECCION_PADRE ='" + (String)registro.getDefCampo("CVE_SECCION") + "' \n" +
			   " AND CVE_GPO_EMPRESA     ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_PORTAL         ='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
		}else{

			//BORRA EL REGISTRO
			sSql = "DELETE FROM RS_PUB_SECCION " +
				" WHERE CVE_SECCION='" + (String) registro.getDefCampo("CVE_SECCION") + "' \n" +
				" AND CVE_GPO_EMPRESA='" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND CVE_PORTAL='" + (String) registro.getDefCampo("CVE_PORTAL") + "' \n";

			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0) {
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			} else {
				//SI SE PUDO BORRA EL REGISTRO ENTONCES TAMBIEN SE BORRA LA CARPETA QUE TIENE ASOCIADA
				String sMensajeError = "";
				LinkedList listaErrores = new LinkedList();
				//SE ENVIA EN EL OBJETO listaRuta CADA UNA DE LAS CARPETAS QUE FORMAN LA RUTA EN UNA LISTA
				registro.addDefCampo("LISTA", listaRuta);
				try {
					listaErrores = admoncarpetas.BorrarSeccion(registro);

					if (listaErrores.size() > 0) {
						Iterator ilistaErrores = listaErrores.iterator();
						while (ilistaErrores.hasNext()) {
							sMensajeError += "Error: " + (String) ilistaErrores.next() + "\n";
						}
						sSql = "No se pudieron borrar todas las carpetas, probablemente esten en uso...";
						if (ejecutaUpdate() == 0) {
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
					}
				}
				catch (Exception e){
					System.out.println("Se generó un al tratar de borrar una seccion");
					e.printStackTrace();
				}
			}

		}
		return resultadoCatalogo;
	}
}