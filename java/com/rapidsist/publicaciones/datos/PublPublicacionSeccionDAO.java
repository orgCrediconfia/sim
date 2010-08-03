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
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.util.Fecha2;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de publicaciones.
 */
public class PublPublicacionSeccionDAO extends Conexion2 implements  OperacionModificacion {

	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
			//SI SE AGREGO CORRECTAMENTE EN LA TABLA ENTONCES SE DEBE DETERMINAR LA RUTA
			//DONDE SE VA A ALMACENAR LA PUBLICACIÓN.
			try{
				
				if(registro.getDefCampo("OPERACION_CATALOGO").equals("MO") ){
			
					sSql =  "SELECT CVE_SECCION, " +
							"SECCION_ANTERIOR " +
							"FROM RS_PUB_PUBLICACION " +
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n" +
							" AND ID_PUBLICACION = '" + (String)registro.getDefCampo("ID_PUBLICACION") + "' \n";
					ejecutaSql();
				
					if (rs.next()){
						String sCveSeccion = rs.getString("CVE_SECCION");	
						registro.addDefCampo("CVE_SECCION",sCveSeccion);
						String sCveSeccionAnterior = rs.getString("SECCION_ANTERIOR");
						registro.addDefCampo("SECCION_ANTERIOR",sCveSeccionAnterior);
					
						//SE INSTANCIA LA CLASE QUE CONSTRUYE LAS CARPETAS
						PubAdministracionCarpetas admoncarpetas = new PubAdministracionCarpetas();
						//OBTIENE LA RUTA DE LA PUBLICACION EN BASE A LA SECCIÓN QUE PERTENECE
						String sRutaPublicacion = admoncarpetas.getRuta(registro, this);
						//ALMACENA EN EL OBJETO resultadoCatalogo.Resultado EL VALOR DE LA RUTA
						resultadoCatalogo.mensaje.setDescripcion(sRutaPublicacion);
					}	
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		return resultadoCatalogo;
	}
}
