/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */


package com.rapidsist.publicaciones.datos;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.publicaciones.datos.PublicacionDAO;
import com.rapidsist.publicaciones.datos.ConstruyeSeccion;
import com.rapidsist.publicaciones.datos.ConstruyeSeccion2;
import com.rapidsist.publicaciones.datos.PubAdministracionCarpetas;
import javax.ejb.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.sql.SQLException;
import javax.naming.*;


/**
 * Contiene las operaciones realizadas sobre la infraestructura de portales
 *
 * @ejb.bean
 *     name="PublicacionSL"
 *     type="Stateless"
 *     jndi-name="PublicacionSL"
 *     local-jndi-name="PublicacionSLLocal"
 * @ejb.transaction type="Required"
 * @ejb.resource-ref
 *		res-ref-name="jdbc/PortalDs"
 *		res-type="javax.sql.DataSource"
 *		res-auth="Container"
 *		jndi-name="PortalDs"
 * @weblogic.resource-description
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="PortalDs"
 * @jrun.resource-ref
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="PortalDs"
 * @jboss.resource-ref
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="java:/PortalDs"
 * @jonas.resource
 *		res-ref-name="jdbc/PortalDs"
 *		jndi-name="PortalDs"
 * @oc4j.bean
 * 		data-source="jdbc/PortalDs"
 */
public class PublicacionSLBean implements SessionBean {

	Context	context=null;

	/**
	 * Contexto del EJB
	 */
	SessionContext sessionContext;
	Conexion2 conexionBd = null;
	PublicacionDAO publicacionDatos = new PublicacionDAO();

	private PublicacionDAO publicacionDao;

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 * @throws CreateException
	 */
	public void ejbCreate() throws CreateException {
		try {
			context = new InitialContext();
		}
		catch (NamingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 */
	public void ejbRemove(){
	}

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 */
	public void ejbActivate(){
	}

	/**
	 * Este método es requerido por la especificación J2EE, pero no tiene
	 * implementado ningún código.
	 */
	public void ejbPassivate(){
	}

	/**
	 * Inicializa el contexto de la sesion para el EJB.
	 * @param sessionContext Contexto de la sesion que provee información al EJB sobre
	 * el contenedor.
	 */
	public void setSessionContext(SessionContext sessionContext){
		this.sessionContext = sessionContext;
	}

	/**
	 * Obtiene todos los registros de un catálogo.
	 * @param parametros Filtro de búsqueda.
	 * @return Lista de registros.
	 * @ejb.interface-method
	 */
	public LinkedList getRegistros(Registro parametros) {
		LinkedList lista = null;
		//SE INSTANCIA LA CLASE DAO
		PublicacionDAO publicacionDAO = new PublicacionDAO();
		try {
			//ABRE CONEXION A BASE DE DATOS
			publicacionDAO.abreConexion("java:comp/env/jdbc/PortalDs");
			//OBTIENE LOS PERMISOS DE ACCESO A UNA FUNCION
			lista = publicacionDAO.getRegistros(parametros);
				//VERIFICA SI LA CONSULTA TRAE REGISTROS
				if (lista != null){
					// AGREGA A CADA REGISTRO ENCONTRADO EN LA CONSULTA UN CAMPO
					// QUE PERMITE CAMBIAR EL COLOR EN LA TABLA DONDE SE DESPLIEGA
					// EL RESULTADO
					Iterator listaRegistros = lista.iterator();
					boolean bRenglonColor = false;
					while (listaRegistros.hasNext()){
						String sRenglonColor = "";
						if (bRenglonColor){
							sRenglonColor= "class='RenglonTablaColor'";
							bRenglonColor = false;
						}
						else{
							sRenglonColor= "";
							bRenglonColor = true;
						}
						Registro registro = (Registro)listaRegistros.next();
						registro.addDefCampo("RenglonTablaColor", sRenglonColor);
					}//WHILE
				} // LISTA != NULL
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en PublicacionSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( publicacionDAO.isConectado()){
				publicacionDAO.cierraConexion();
			}
		}
		return lista;
	}

	/**
	 * Obtiene las propiedades de un usuario asociado al portal, como son
	 * su perfil de publicación, su nivel de acceso y su nombre de usuario
	 * @param parametros Filtro de búsqueda.
	 * @return resultado Registro con el resultado dela busqueda.
	 * @ejb.interface-method
	 */
	public Registro getPropiedadesUsuarioPublicaciones(Registro parametros) {
		Registro resultado = null;
		//SE INSTANCIA LA CLASE DAO
		PublicacionDAO publusuarioDAO = new PublicacionDAO();
		try {
			//ABRE CONEXION A BASE DE DATOS
			publusuarioDAO.abreConexion("java:comp/env/jdbc/PortalDs");
			//OBTIENE LOS PERMISOS DE ACCESO A UNA FUNCION
			resultado = publusuarioDAO.getPropiedadesUsuarioPublicaciones(parametros);
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en PublicacionSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( publusuarioDAO.isConectado()){
				publusuarioDAO.cierraConexion();
			}
		}
		return resultado;
	}

	/**
	 * Obtiene un registros de una publicación y verifica los permisos necesarios
	 * para poder presentarla
	 * @param parametros Filtro de búsqueda.
	 * @return resultado Registro con el resultado dela busqueda.
	 * @ejb.interface-method
	 */
	public Registro getPublicacion(Registro parametros) {
		
		Registro resultado = null;
		try {
			//ABRE CONEXION A BASE DE DATOS
			publicacionDatos.abreConexion("java:comp/env/jdbc/PortalDs");
			//SI SE ABRE LA PUBLICACION O SE DA DE BAJA SE VA AL METODO QUE INCREMENTA EL CONTADOR
			String sContador = "";
			sContador = (String)parametros.getDefCampo("CONTADOR");
			if (sContador == null){
				boolean bIncrementaContador = publicacionDatos.IncrementaContadorPublicacion(parametros);
			}
			
			//OBTIENE LOS PERMISOS DE ACCESO A UNA FUNCION
			resultado = publicacionDatos.getPublicacion(parametros);
			if (resultado!=null){
				PubAdministracionCarpetas administracionCarpetas = new PubAdministracionCarpetas();
				String sRutaSeccion = administracionCarpetas.getRutaSeccion(resultado, publicacionDatos);
				resultado.addDefCampo("RUTA_SECCION", sRutaSeccion);
			}
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en PublicacionSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( publicacionDatos.isConectado()){
				publicacionDatos.cierraConexion();
			}
		}
		return resultado;
	}

	/**
	 * Obtiene las secciones y las publicaciones que puede ver el usuario
	 * @param parametros Conjunto de datos que son necesarios: sCveGpoEmpresa,  sCvePortal, sNomAplicacionWeb
	 * @return sMenu Cadena con código html que la jsp puede interpretar con ayuda de una hoja de estilos y
	 * de javascript.
	 * @ejb.interface-method
	 */
	public String getMenuPublicaciones(Registro parametros){
		String sMenu = "";
		publicacionDao = new PublicacionDAO();
		try{
			//BUSCA EL OBJETO GRUPO EN EL JNDI
			sMenu = (String) context.lookup("Publicaciones_GrupoEmpresa_" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "Portal_" + (String) parametros.getDefCampo("CVE_PORTAL") + "PerfilPub_" + (String) parametros.getDefCampo("CVE_PERFIL_PUB"));
		}
		//VERIFICA SI NO ENCONTRO EL MENU LO CREA
		catch(NameNotFoundException e){
			try {
				//ABRE CONEXION A BASE DE DATOS
				publicacionDao.abreConexion("java:comp/env/jdbc/PortalDs");
				ConstruyeSeccion2 construyeSeccion2 = new ConstruyeSeccion2();
				//ASIGNA EL MENU CONSTRUIDO A LA APLICACION
				sMenu = construyeSeccion2.getSeccion(publicacionDao, parametros);
				try {
					context = new InitialContext();
					//ALMACENA LA CONFIGURACION DEL MENU EN EL JNDI
					context.rebind("Publicaciones_GrupoEmpresa_" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "Portal_" + (String)parametros.getDefCampo("CVE_PORTAL") + "PerfilPub_" +  (String)parametros.getDefCampo("CVE_PERFIL_PUB"),  sMenu);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( publicacionDao.isConectado()){
				publicacionDao.cierraConexion();
			}
		}
		return sMenu;
	}

	/**
	 * Borra una objeto del JNDI
	 * @param sCveGpoEmpresa Clave del grupo de empresas
	 * @param sCvePortal Clave del portal
	 * @param sCveUsuario Clave del usuario
	 * @return bExito Valor lógico con Verdadero si el objeto fue borrado y Falso si
	 * el objeto no se pudo borrar
	 * @ejb.interface-method
	 */
	public boolean BorraObjetoJNDI(String sCveGpoEmpresa, String sCvePortal, String sCveUsuario) {
		boolean bExito = false;
		PublPerfilDAO perfilDao = new PublPerfilDAO();
		try {
			Registro parametros =  new Registro();
			parametros.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
			parametros.addDefCampo("CVE_PORTAL", sCvePortal);
			parametros.addDefCampo("CVE_USUARIO", sCveUsuario);
			String sCvePerfilPub = (String)((Registro)(this.getPropiedadesUsuarioPublicaciones(parametros))).getDefCampo("CVE_PERFIL_PUB");
			//ABRE CONEXION A BASE DE DATOS
			perfilDao.abreConexion("java:comp/env/jdbc/PortalDs");
			//SE BUSCAN TODOS LOS PERFILES PARA UN DETERMINADO GRUPO DE EMPRESAS Y PORTAL
			LinkedList listaPerfiles = perfilDao.getRegistros(parametros);
			Iterator ilistaPerfiles = listaPerfiles.iterator();
			//SE BORRAN TODOS LOS MENUS DEL JDNI PERTENECIENTES A TODOS LOS PERFILES
			while (ilistaPerfiles.hasNext()){
				Registro registroPerfil = new Registro();
				registroPerfil = (Registro)ilistaPerfiles.next();
				try {				
					context.unbind("Publicaciones_GrupoEmpresa_" + sCveGpoEmpresa+ "Portal_" + sCvePortal + "PerfilPub_" + (String)registroPerfil.getDefCampo("CVE_PERFIL_PUB"));
				}
				catch (Exception e){
					//ESTE CATCH ES PARA ATRAPAR EL ERRO EN CASO DE NO ENCONTRAR EN EL JNDI LO QUE SE BUSCA
				}
			}
			//BORRA EL STRING DEL MENU PARA CUANDO EL PERFIL ES NULO, O EN OTRAS PALABRAS
			//CUANDO SE TRATA DE UN VISITANTE O DE ALGUIEN QUE AUN NO SE FIRMA
			try {
				context.unbind("Publicaciones_GrupoEmpresa_" + sCveGpoEmpresa+ "Portal_" + sCvePortal + "PerfilPub_null");
			}
			catch (Exception e){
				//ESTE CATCH ES PARA ATRAPAR EL ERRO EN CASO DE NO ENCONTRAR EN EL JNDI LO QUE SE BUSCA
			}
			bExito = true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if ( perfilDao.isConectado()){
				perfilDao.cierraConexion();
			}
		}
		return bExito;
	}

	/**
	 * Obtiene las publicaciones que puede ver un usuario
	 * @param parametros Conjunto de datos que son necesarios: sCveGpoEmpresa,  sCvePortal, sNomAplicacionWeb
	 * @return lista Lista con las publicaciones de la sección deseada
	 * de javascript.
	 * @throws RemoteException
	 * @ejb.interface-method
	 */
	public LinkedList getPublicaciones(Registro parametros) {
		LinkedList lista = null;
		boolean bErrorGenerado = false;
		//SE INSTANCIA LA CLASE DAO
		PublicacionDAO publicacionDAO = new PublicacionDAO();
		try {
			//ABRE CONEXION A BASE DE DATOS
			publicacionDAO.abreConexion("java:comp/env/jdbc/PortalDs");
			//OBTIENE LOS PERMISOS DE ACCESO A UNA FUNCION
			lista = publicacionDAO.getPublicaciones(parametros);
				//VERIFICA SI LA CONSULTA TRAE REGISTROS
				if (lista != null){
					// AGREGA A CADA REGISTRO ENCONTRADO EN LA CONSULTA UN CAMPO
					// QUE PERMITE CAMBIAR EL COLOR EN LA TABLA DONDE SE DESPLIEGA
					// EL RESULTADO
					Iterator listaRegistros = lista.iterator();
					boolean bRenglonColor = false;
					while (listaRegistros.hasNext()){
						String sRenglonColor = "";
						if (bRenglonColor){
							sRenglonColor= "class='RenglonTablaColor'";
							bRenglonColor = false;
						}
						else{
							sRenglonColor= "";
							bRenglonColor = true;
						}
						Registro registro = (Registro)listaRegistros.next();
						registro.addDefCampo("RenglonTablaColor", sRenglonColor);
					}//WHILE
				} // LISTA != NULL
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en PublicacionSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( publicacionDAO.isConectado()){
				publicacionDAO.cierraConexion();
			}
		}
		return lista;
	}

	/**
	 * Obtiene la historia de la sección seleccionada
	 * @param parametros Valores que permiten realizar la consulta
	 * @return sRuta Cadena con la ruta de la sección
	 * @ejb.interface-method
	 */
public String getHistoriaSeccion(Registro parametros)  {
		StringBuffer sHistoriaSeccion = null;
		//SE INSTANCIA LA CLASE DAO
		PublicacionDAO publicacionDao = new PublicacionDAO();
		try {
			//ABRE CONEXION A BASE DE DATOS
			publicacionDao.abreConexion("java:comp/env/jdbc/PortalDs");

			//OBTIENE LOS PERMISOS DE ACCESO A UNA FUNCION
			ConstruyeSeccion construyeSeccion = new ConstruyeSeccion();
			//ESTE PARAMETRO INDICA QUE SOLO VA A DETERMINA LA RUTA DE UNA SECCION
			parametros.addDefCampo("DETERMINA_RUTA_SECCION", "SI");
			//ASIGNA EL MENU CONSTRUIDO A LA APLICACION
			LinkedList lista = construyeSeccion.getSeccion(publicacionDao, parametros);
			//VERIFICA SI LA CONSULTA TRAE REGISTROS
			if (lista != null){
				//AGREGA A CADA REGISTRO ENCONTRADO EN LA CONSULTA UN CAMPO
				//QUE PERMITE CAMBIAR EL COLOR EN LA TABLA DONDE SE DESPLIEGA
				//EL RESULTADO
				Iterator listaRegistros = lista.iterator();
				Registro registro = null;
				sHistoriaSeccion = new StringBuffer();
				while (listaRegistros.hasNext()){
					registro = (Registro)listaRegistros.next();
					sHistoriaSeccion.append( ((String)registro.getDefCampo("NOM_SECCION_SIN_IDENTAR")).trim()+" > ");
				}//WHILE
			} // LISTA != NULL

			//SI LA CADENA QUE DETERMINA LA RUTA DE LA SECCIÓN NO ESTA VACIA SE DICE QUE
			//SE PUDO CONSTRUIR LA CADENA Y SE LE QUITA EL ÚLTIMO CARACTER ">"
			if (sHistoriaSeccion != null){
				sHistoriaSeccion.delete(sHistoriaSeccion.length()-2, sHistoriaSeccion.length());
			}
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en PublicacionSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( publicacionDao.isConectado()){
				publicacionDao.cierraConexion();
			}
		}
		return sHistoriaSeccion.toString();
	}
}