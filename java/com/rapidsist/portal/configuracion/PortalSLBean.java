/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */
 
package com.rapidsist.portal.configuracion;

import javax.ejb.*;
import javax.naming.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.sql.SQLException;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import org.apache.log4j.PropertyConfigurator;

/**
 * Contiene las operaciones realizadas sobre la infraestructura de portales
 *
 * @ejb.bean
 *     name="PortalSL"
 *     type="Stateless"
 *     jndi-name="PortalSL"
 *     local-jndi-name="PortalSLLocal"
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
public class PortalSLBean implements SessionBean {
	/**
	 * Contexto del EJB
	 */
	Context	context=null;
	SessionContext sessionContext;
	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("MensajesInfraestructura");

	private ConfiguracionPortalDAO configuracionPortalDao;

	/**
	 * @throws CreateExcepction 
	 */
	public void ejbCreate() throws CreateException {
		try {
			context = new InitialContext();
			//VERIFICA SI EL LOGGER NO ESTA CONFIGURADO
			if (logger.getLevel() == null){
				//CONFIGURA EL LOGGER
				PropertyConfigurator.configure("PortalLog.properties");
			}

		}
		catch (NamingException ex) {
			ex.printStackTrace();
		}
	}
	public void ejbRemove() {
		/**@todo Complete this method*/
	}
	public void ejbActivate() {
		/**@todo Complete this method*/
	}
	public void ejbPassivate() {
		/**@todo Complete this method*/
	}
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	/**
	 * Verifica si el usuario tiene acceso a la función.
	 * @param sCveAplicacion Clave de la aplicación.
	 * @param sCvePerfil Clave del perfil.
	 * @param sCveFuncion Clave de la función.
	 * @return Los permisos asociados a una función (alta, baja, cambio, consulta)
	 * @ejb.interface-method
	 */
	public Permisos validaAccesoFuncion(String sCveAplicacion, String sCvePerfil, String sCveFuncion){
		Permisos permiso = null;
		
		ConfiguracionPortalDAO configuracionDAO = new ConfiguracionPortalDAO();

		try{
			//ABRE CONEXION A BASE DE DATOS
			configuracionDAO.abreConexion("java:comp/env/jdbc/PortalDs");
			
			//OBTIENE LOS PERMISOS DE ACCESO A UNA FUNCION
			Registro registroPermiso = configuracionDAO.getPermisos(sCveAplicacion, sCvePerfil, sCveFuncion);

			if (registroPermiso != null){
				permiso = new Permisos();
				permiso.bAlta = ((String)registroPermiso.getDefCampo("B_ALTA")).equals("V") ? true : false;
				permiso.bBaja = ((String)registroPermiso.getDefCampo("B_BAJA")).equals("V") ? true : false;
				permiso.bModificacion = ((String)registroPermiso.getDefCampo("B_MODIF")).equals("V") ? true : false;
				permiso.bConsulta = ((String)registroPermiso.getDefCampo("B_CONSULTA")).equals("V") ? true : false;
				permiso.bBitacora = ((String)registroPermiso.getDefCampo("B_BITACORA")).equals("V") ? true : false;
			} // registroPermiso != null
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en ValidaAccesoFuncion en PortalSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( configuracionDAO.isConectado()){
				configuracionDAO.cierraConexion();
			}
		}
		return permiso;
	}

	/**
	 * Obtiene la información relacionada a una pagina
	 * @param sCvePagina Clave de la página
	 * @return los datos asociados a la página
	 * @ejb.interface-method
	 */
	 public com.rapidsist.portal.configuracion.Pagina getPagina(String sCvePagina){

		com.rapidsist.portal.configuracion.Pagina pagina = null;
		ConfiguracionPortalDAO configuracionDAO = new ConfiguracionPortalDAO();
		try{

			try {
				//BUSCA EL OBJETO CON LA CONFIGURACION DEL PORTAL
				pagina = (com.rapidsist.portal.configuracion.Pagina) context.lookup("Pagina_" + sCvePagina);
				System.out.println("ESTA LA PAGINA EN EL JNDI");
			}
			catch (javax.naming.NamingException e) {
				System.out.println("* NO ESTA LA PAGINA EN EL JNDI");

				//ABRE CONEXION A BASE DE DATOS
				configuracionDAO.abreConexion("java:comp/env/jdbc/PortalDs");
				
				//VERFICA SI LA PAGINA NO ESTA BLOQUEADA
				Registro paginaDatos = configuracionDAO.getPagina(sCvePagina);
				if (paginaDatos != null) {
					pagina = new Pagina();
					pagina.setCveFuncion((String) paginaDatos.getDefCampo("CVE_FUNCION"));
					pagina.setNomPagina((String) paginaDatos.getDefCampo("NOM_PAGINA"));
					pagina.setBitacoraPagina((String) paginaDatos.getDefCampo("B_BITACORA_VISITA"));
					try {
						//ALMACENA LA PAGINA EN EL JNDI
						context.rebind("Pagina_" + sCvePagina, pagina);
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( configuracionDAO != null){
				if (configuracionDAO.isConectado()) {
					configuracionDAO.cierraConexion();
				}
			}
		}
		return pagina;
	}
	
	/**
	 * Obtiene la información relacionada a una función.
	 * @param sIdFuncion Clave de la función.
	 * @return Los datos asociados a una función.
	 * @ejb.interface-method	 
	 */
	public com.rapidsist.portal.configuracion.Funcion getFuncion(String sIdFuncion){
		OperacionesComunes operacionesComunes = new OperacionesComunes();
		return operacionesComunes.getFuncion(sIdFuncion);
	}

	/**
	 * Obtiene el menú asociado a un grupo - aplicación
	 * @param sCveAplicacion Clave de la aplicación.
	 * @param sCvePerfil Clave del perfil.
	 * @param sNomAplicacionWeb Nombre de la aplicación web.
	 * @return Menú del grupo aplicación.
	 * @ejb.interface-method
	 */
	public String getMenuAplicacion(String sCveAplicacion, String sCvePerfil, String sNomAplicacionWeb){
		
		String sMenu ="";

		try{
			//BUSCA EL OBJETO GRUPO EN EL JNDI
			sMenu = (String)context.lookup("Menu_" + sCveAplicacion + sCvePerfil);
		}
		//VERIFICA SI NO ENCONTRO EL MENU
		catch(javax.naming.NamingException e){
			try {
				configuracionPortalDao = new ConfiguracionPortalDAO();
				configuracionPortalDao.abreConexion("java:comp/env/jdbc/PortalDs");

				ConstruyeMenu2 construyeMenu = new ConstruyeMenu2();

				//ASIGNA EL MENU CONSTRUIDO A LA APLICACION
				sMenu = construyeMenu.getMenu(configuracionPortalDao, sCveAplicacion, sCvePerfil, sNomAplicacionWeb);

				try {
					//ALMACENA EL MENU EN EL JNDI
					context.rebind("Menu_" + sCveAplicacion + sCvePerfil, sMenu);
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
			if ( configuracionPortalDao != null){
				if (configuracionPortalDao.isConectado()) {
					configuracionPortalDao.cierraConexion();
				}
			}
		}
		return sMenu;
	}
	
	/**
	 * Obtiene el menú asociado a un grupo - aplicación
	 * @param sCveAplicacion Clave de la aplicación.
	 * @param sCvePerfil Clave del perfil.
	 * @param sNomAplicacionWeb Nombre de la aplicación web.
	 * @return Menú del grupo aplicación.
	 * @ejb.interface-method
	 */
	public String getMenuAplicacionHorizontal(String sCveAplicacion, String sCvePerfil, String sNomAplicacionWeb){
		String sMenu ="";

		try{
			//BUSCA EL OBJETO GRUPO EN EL JNDI
			sMenu = (String)context.lookup("MenuHorizontal_" + sCveAplicacion + sCvePerfil);
		}
		//VERIFICA SI NO ENCONTRO EL MENU
		catch(javax.naming.NamingException e){
			try {
				configuracionPortalDao = new ConfiguracionPortalDAO();
				configuracionPortalDao.abreConexion("java:comp/env/jdbc/PortalDs");

				ConstruyeMenu2 construyeMenuHorizontal = new ConstruyeMenu2();

				//ASIGNA EL MENU CONSTRUIDO A LA APLICACION
				sMenu = construyeMenuHorizontal.getMenuHorizontal(configuracionPortalDao, sCveAplicacion, sCvePerfil, sNomAplicacionWeb);

				try {
					//ALMACENA EL MENU EN EL JNDI
					context.rebind("MenuHorizontal_" + sCveAplicacion + sCvePerfil, sMenu);
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
			if ( configuracionPortalDao != null){
				if (configuracionPortalDao.isConectado()) {
					configuracionPortalDao.cierraConexion();
				}
			}
		}
		return sMenu;
	}

	/**
	 * Obtiene el mensaje.
	 * @param sClaveMensaje
	 * @ejb.interface-method
	 */
	public Mensaje getMensaje(String sClaveMensaje) {
		OperacionesComunes operacionesComunes = new OperacionesComunes();
		return operacionesComunes.getMensaje(sClaveMensaje);
	}


	/**
	 * Almacena los eventos del usuario en la base de datos
	 * @param sCveGpoEmpresa Clave del grupo de empresa.
	 * @param sCveUsuario Clave del usuario. 
	 * @param sCvePagina Clave de la página.
	 * @ejb.interface-method
	 */
	public void almacenaPagina(String sCveGpoEmpresa, String sCveUsuario, String sCvePagina) {

		ConfiguracionPortalDAO configuracionDAO = new ConfiguracionPortalDAO();
		com.rapidsist.portal.configuracion.Pagina pagina = null;
		try{
			//ABRE CONEXION A BASE DE DATOS
			configuracionDAO.abreConexion("java:comp/env/jdbc/PortalDs");

			try {
				//BUSCA EL OBJETO CON LA CONFIGURACION DEL PORTAL
				pagina = (com.rapidsist.portal.configuracion.Pagina) context.lookup("Pagina_" + sCvePagina);
			}
			catch (javax.naming.NamingException e) {

				//VERFICA SI LA PAGINA NO ESTA BLOQUEADA
				Registro paginaDatos = null;
				paginaDatos = configuracionDAO.getPagina(sCvePagina);
				if (paginaDatos != null) {
					pagina = new Pagina();
					pagina.setCveFuncion( (String) paginaDatos.getDefCampo("CVE_FUNCION"));
					pagina.setNomPagina( (String) paginaDatos.getDefCampo("NOM_PAGINA"));
					pagina.setBitacoraPagina( (String) paginaDatos.getDefCampo("B_BITACORA_VISITA"));
					try {
						//ALMACENA LA PAGINA EN EL JNDI
						context.rebind("Pagina_" + sCvePagina, pagina);
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			String sBitacoraPagina = "";
			sBitacoraPagina = pagina.getBitacoraPagina();
			if (sBitacoraPagina.equals("V")){
				try {
					ResultadoCatalogo resultadoCatalogo = configuracionDAO.alta(sCveGpoEmpresa, sCveUsuario, sCvePagina);
				}
				catch (SQLException ex) {
					System.out.println("Se genero una SQLException en getMensaje en PortalSLBean: " + ex.getMessage());
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
		//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
		if (configuracionDAO.isConectado()) {
			configuracionDAO.cierraConexion();
		}
	}
}

	/**
	 * Obtiene la configuración para un usuario.
	 * @param sCvePortalDefault Clave del portal default.
	 * @param sCveUsuario Clave del usuario.
	 * @param sNomAplicacionWeb Clave del usuario.
	 * @return Configuración del usuario.
	 * @ejb.interface-method
	 */
	public Usuario getConfiguracionUsuario(String sCvePortalDefault, String sCveUsuario, String sNomAplicacionWeb){
		
		Usuario usuario = null;
		configuracionPortalDao = new ConfiguracionPortalDAO();
		try {			
			logger.debug("Portal: " + sCvePortalDefault);
			logger.debug("Nombre aplicacin web: " + sNomAplicacionWeb);
			logger.debug("Usuario: " + sCveUsuario);

			System.out.println("*inicia metodo getConfiguracionUsuario. PortalSLBean");

			System.out.println("Portal. sCveUsuario es: "+sCvePortalDefault+" PortalSLBean");
			System.out.println("Nombre aplicacion web. sNomAplicacionWeb es: "+sNomAplicacionWeb+" PortalSLBean");
			System.out.println("Usuario. sCveUsuario es: "+sCveUsuario+" PortalSLBean");

			//VERIFICA SI LA CLAVE DEL USUARIO ESTA VACIA
			if (sCveUsuario.equals("")){
				System.out.println("LA CLAVE DEL USUARIO ESTA VACIA. PortalSLBean");
				try{
					//BUSCA EL OBJETO CON LA CONFIGURACION DEL PORTAL Y EL USUARIO EN EL JNDI
					usuario = (Usuario)context.lookup("PortalDefaultUsuario_" + sCvePortalDefault);
					System.out.println("Encontro el objeto preconfigurado en el JNDI, el cual se llama: PortalDefaultUsuario_" + sCvePortalDefault);
					logger.debug("Encontro el objeto preconfigurado en el JNDI, el cual se llama: PortalDefaultUsuario_" + sCvePortalDefault);
					System.out.println("*PASO 1*");
				}
				//VERIFICA SI NO ENCONTRO LA CONFIGURACION DEL PORTAL POR DEFAULT
				catch(javax.naming.NamingException e){
					System.out.println("*PASO 2*");
					logger.debug("No encontró el portal preconfigurado en el JNDI, se procede a configurarlo");
					System.out.println("*PASO 3*");
					//ABRE CONEXION A BASE DE DATOS
					configuracionPortalDao.abreConexion("java:comp/env/jdbc/PortalDs");
					System.out.println("llama al metodo getPortalDefault. PortalSLBean");
					//OBTIENE LA CONFIGURACION POR DEFAULT PARA EL PORTAL
					logger.debug("Se obtiene la configuración para el portal: " + sCvePortalDefault);
					PortalDefault portalDefault = getPortalDefault(sCvePortalDefault);
					
					System.out.println("**Termina metodo getPortalDefault. PortalSLBean");
					//VERIFICA SI ENCONTRO LA CONFIGURACION DEFAULT DEL PORTAL
					if (portalDefault != null) {
						System.out.println("ENCONTRO LA CONFIGURACION DEFAULT DEL PORTAL");
						logger.debug("Se encontró en el portal: " + sCvePortalDefault + ", se procede a preconfigurar el usuario");
						usuario = new Usuario();
						usuario.bValidado = false;
						usuario.sAplicacionActual = portalDefault.sCveAplicacion;
						usuario.sCveGpoEmpresa = portalDefault.sCveGpoEmpresa;
						usuario.sNombreVentanaBrowser = portalDefault.sNomVentana;
						usuario.sUrlEncabezado = portalDefault.sUrlEncabezado;
						usuario.sUrlHojaEstilo = portalDefault.sUrlEstilo;
						usuario.sUrlPiePagina = portalDefault.sUrlPiePagina;
						usuario.sUrlMenuAplicacion = portalDefault.sUrlMenuAplicacion;
						usuario.sUrlContenidoInicio = portalDefault.sUrlContenidoInicio;
						usuario.sUrlContenidoFin = portalDefault.sUrlContenidoFin;
						usuario.sUrlDirectorioDefault = portalDefault.sUrlDirectorioDefault;
						usuario.sNomAplicacionWeb = sNomAplicacionWeb;
						usuario.sCvePortal = portalDefault.sCvePortal;
						usuario.sCvePortalDefault = portalDefault.sCvePortalDefault;
						usuario.sCvePerfilActual = portalDefault.sCvePerfil;
						usuario.sTipoLetraEmpresa = portalDefault.sTipoLetraEmpresa;
						usuario.sTipoLetraPortal = portalDefault.sTipoLetraPortal;
						usuario.sTipoMenu = portalDefault.sTipoMenu;
						
						//DATOS DEL USUARIO DEFAULT
						usuario.sCveUsuario = portalDefault.sCveUsuario;
						usuario.sIdPersona = portalDefault.sIdPersona;
						usuario.sCveEmpresa = portalDefault.sCveEmpresa;
						usuario.sNomAlias = portalDefault.sNomAlias;
						usuario.sLocalidad = "";
						usuario.sNomCompleto = portalDefault.sNomCompleto;
						usuario.sTipoMenu = portalDefault.sTipoMenu;

						//OBTIENE LA CONFIGURACION DE LAS APLICACIONES ASIGNADAS AL USUARIO
						configuracionAplicacionesUsuario(usuario, portalDefault.sCveAplicacion);
						try {
							//ALMACENA LA CONFIGURACION DEL PORTAL PARA EL USUARIO POR DEFAULT EN EL JNDI
							context.rebind("PortalDefaultUsuario_" + sCvePortalDefault, usuario);
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					else{
						System.out.println("NO ENCONTRO LA CONFIGURACION DEFAULT DEL PORTAL");
						logger.debug("No se encontró en el portal: " + sCvePortalDefault + ", termina la ejecución del método: getConfiguracionUsuario()");
					}//VERIFICA SI ENCONTRO LA CONFIGURACION DEFAULT DEL PORTAL
				}//CATCH NameNotFoundException
			}
			else{
				System.out.println("LA CLAVE DEL USUARIO **NO** ESTA VACIA. PortalSLBean");
				//1.- BUSCA EN LA BASE DE DATOS SI EL USUARIO TIENE ALGUNA CONFIUGURACION PERSONALIZADA
				
				//SE LE ASIGNA AL USUARIO LA CONFIGURACION DEFAULT DEL PORTAL
				//ABRE CONEXION A BASE DE DATOS
				configuracionPortalDao.abreConexion("java:comp/env/jdbc/PortalDs");
				
				//OBTIENE LA CONFIGURACION POR DEFAULT PARA EL PORTAL
				PortalDefault portalDefault = getPortalDefault(sCvePortalDefault);

				//VERIFICA SI ENCONTRO LA CONFIGURACION DEFAULT DEL PORTAL
				if (portalDefault != null) {
					
					usuario = new Usuario();
					usuario.bValidado = false;
					usuario.sAplicacionActual = portalDefault.sCveAplicacion;
					usuario.sCveGpoEmpresa = portalDefault.sCveGpoEmpresa;
					usuario.sNombreVentanaBrowser = portalDefault.sNomVentana;
					usuario.sUrlEncabezado = portalDefault.sUrlEncabezado;
					usuario.sUrlHojaEstilo = portalDefault.sUrlEstilo;
					usuario.sUrlPiePagina = portalDefault.sUrlPiePagina;
					usuario.sUrlMenuAplicacion = portalDefault.sUrlMenuAplicacion;
					usuario.sUrlContenidoInicio = portalDefault.sUrlContenidoInicio;
					usuario.sUrlContenidoFin = portalDefault.sUrlContenidoFin;
					usuario.sUrlDirectorioDefault = portalDefault.sUrlDirectorioDefault;
					usuario.sNomAplicacionWeb = sNomAplicacionWeb;
					usuario.sCvePortal = portalDefault.sCvePortal;
					usuario.sCvePortalDefault = portalDefault.sCvePortalDefault;
					usuario.sCvePerfilActual = portalDefault.sCvePerfil;
					usuario.sTipoLetraEmpresa = portalDefault.sTipoLetraEmpresa;
					usuario.sTipoLetraPortal = portalDefault.sTipoLetraPortal;	
					usuario.sTipoMenu = portalDefault.sTipoMenu;
					
					//DATOS PERSONALES DEL USUARIO
					Registro datosUsuario = configuracionPortalDao.getUsuario(usuario.sCveGpoEmpresa, sCveUsuario);					
					usuario.sIdPersona = (String)datosUsuario.getDefCampo("ID_PERSONA");
					usuario.sNomAlias = (String)datosUsuario.getDefCampo("NOM_ALIAS");
					usuario.sLocalidad = (String)datosUsuario.getDefCampo("LOCALIDAD");
					usuario.sNomCompleto = (String)datosUsuario.getDefCampo("NOM_COMPLETO");
					usuario.sCveUsuario = (String)datosUsuario.getDefCampo("CVE_USUARIO");
					usuario.sCveEmpresa = (String)datosUsuario.getDefCampo("CVE_EMPRESA");
					

					//OBTIENE LA CONFIGURACION DE LAS APLICACIONES ASIGNADAS AL USUARIO
					configuracionAplicacionesUsuario(usuario, portalDefault.sCveAplicacion);
				}//VERIFICA SI ENCONTRO LA CONFIGURACION DEFAULT DEL PORTAL
			}//SIN CLAVE DE USUARIO
		}
		catch (Exception ex) {
			System.out.println("PortalSLBean exception");
			
			ex.printStackTrace();
		}
		finally{
			if (configuracionPortalDao.isConectado()){
				//CIERRA LA CONEXION QUE SE ABRIO INICIALMENTE
				configuracionPortalDao.cierraConexion();
			}
		}
		return usuario;
	}

	/**
	 * Obtiene los datos del portal default.
	 * @param sCvePortalDefault Clave del portal default.
	 * @return Datos del portal default.
	 */
	private PortalDefault getPortalDefault(String sCvePortalDefault) throws Exception {
		com.rapidsist.portal.configuracion.PortalDefault portalDefault;

		try{
			//BUSCA EL OBJETO CON LA CONFIGURACION DEL PORTAL EN EL JNDI
			portalDefault= (com.rapidsist.portal.configuracion.PortalDefault)context.lookup("PortalDefault_" + sCvePortalDefault);
		}
		//VERIFICA SI NO ENCONTRO LA CONFIGURACION DEL PORTAL POR DEFAULT
		catch(javax.naming.NamingException e){
			portalDefault = configuracionPortalDao.getPortalDefault(sCvePortalDefault);
			//VERIFICA SI ENCONTRO LA CONFIGURACION DEFAULT DEL PORTAL
			if (portalDefault != null){
				try{
					//ALMACENA LA CONFIGURACION EN EL JNDI
					context.rebind("PortalDefault_" + sCvePortalDefault, portalDefault);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return portalDefault;
	}

	/**
	 * Configuración de las aplicaciones del usuario.
	 * @param usuario Objeto que contiene el usuario.
	 * @param sCveAplicacionDefault Clave de la aplicación default.
	 * @throws java.lang.Exception Si hubo algún error en la clase controladora.
	 */
	private void configuracionAplicacionesUsuario(Usuario usuario, String sCveAplicacionDefault) throws Exception {

		logger.debug("Se obtienen las aplicaciones asignadas al usuario");
		//OBTIENE LA LISTA DE APLICACIONES ASOCIADAS AL USUARIO
		LinkedList aplicacionesUsuario = configuracionPortalDao.getAplicacionesUsuario(usuario.sCveGpoEmpresa, usuario.sCveUsuario);

		//VERIFICA SI ENCONTRO LAS APLICACIONES
		if (aplicacionesUsuario != null){
			Iterator listaAplicacionesUsuario = aplicacionesUsuario.iterator();
			String sAplicacionInicial = "";
			usuario.configuracionAplicaciones = new HashMap();
			usuario.listaAplicaciones = new LinkedList();
			AplicacionUsuario aplicacionPerfilUsuario = null;
			while (listaAplicacionesUsuario.hasNext()){
				//OBTIENE LA APLICACION
				Registro aplicacionUsuario = (Registro)listaAplicacionesUsuario.next();
				String sCveAplicacion = (String)aplicacionUsuario.getDefCampo("CVE_APLICACION");

				//VERIFICA SI LA APLICACION ES DISTINTA A LA INICIAL
				if ( !sAplicacionInicial.equals(sCveAplicacion)){
					//INICIALIZA LA BANDERA DE APLICACION INICIAL
					sAplicacionInicial = sCveAplicacion;
					//AGREGA LA APLICACION A LA LISTA DE CONFIGURACION DE APLICACIONES DEL
					//USUARIO E INICILIZA SU LISTA DE PERFILES
					aplicacionPerfilUsuario = new AplicacionUsuario();
					aplicacionPerfilUsuario.sCveAplicacion=sCveAplicacion;
					aplicacionPerfilUsuario.sNomAplicacon = (String)aplicacionUsuario.getDefCampo("NOM_APLICACION");
					aplicacionPerfilUsuario.sUrlAplicacion = (String)aplicacionUsuario.getDefCampo("URL_APLICACION");
					usuario.configuracionAplicaciones.put(sAplicacionInicial, aplicacionPerfilUsuario);

					//AGREGA LA APLICACION A LA LISTA DE APLICACIONES DE USUARIO
					Registro aplicacion = new Registro();
					aplicacion.addDefCampo("CVE_APLICACION", sCveAplicacion);
					aplicacion.addDefCampo("NOM_APLICACION", (String)aplicacionUsuario.getDefCampo("NOM_APLICACION"));
					aplicacion.addDefCampo("URL_APLICACION", (String)aplicacionUsuario.getDefCampo("URL_APLICACION"));
					aplicacion.addDefCampo("NOM_ETIQUETA_MENU",(String)aplicacionUsuario.getDefCampo("NOM_ETIQUETA_MENU"));
					usuario.listaAplicaciones.add(aplicacion);
					

					//OBTIENE LA URL PARA LA APLICACION INICIAL PARA EL PORTAL DEFAULT
					if (sCveAplicacion.equals(sCveAplicacionDefault)){
						usuario.sAplicacionActualUrl = (String)aplicacion.getDefCampo("URL_APLICACION");
						usuario.sAplicacionDefaultUrl = (String)aplicacion.getDefCampo("URL_APLICACION");
					}
				}//VERIFICA SI LA APLICACION ES DISTINTA A LA INICIAL

				//AGREGA EL PERFIL A LA LISTA DE LA APLICACION
				Perfil perfil = new Perfil();
				perfil.sCvePerfil = (String)aplicacionUsuario.getDefCampo("CVE_PERFIL");
				perfil.sNomPerfil = (String)aplicacionUsuario.getDefCampo("NOM_PERFIL");
				aplicacionPerfilUsuario.listaPerfiles.add(perfil);
			}//WHILE

			//OBTINE EL COMPONENETE SELECTOR DE APLICACIONES
			usuario.sSelectorAplicaciones = getComponenteSelectorAplicacion(usuario.listaAplicaciones, usuario.sNomAplicacionWeb);
			//OBTIENE LA LISTA DE APLICACIONES PARA EL SELECTOR DE APLICACIONES DE TABULADORES
			usuario.listaAplicacionesSelector = configuracionPortalDao.getAplicacionesUsuarioSelector(usuario.sCveGpoEmpresa, usuario.sCveUsuario, usuario.sCvePortal);
			usuario.listaMenuColor = configuracionPortalDao.getMenuColor(usuario.sCveGpoEmpresa, usuario.sCvePortal);
		}
		else{
			logger.debug("El usuario no tiene aplicaciones configuradas");
		}//VERIFICA SI ENCONTRO LAS APLICACIONES
	}

	/**
	 * Componente para cambio de aplicaciones.
	 * @param listaAplicaciones Lista de las aplicaciones.
	 * @param sNomAplicacionWeb Nombre de la aplicación web
	 * @throws java.lang.Exception Si hubo algún error en la clase controladora.
	 */
	private String getComponenteSelectorAplicacion(LinkedList listaAplicaciones, String sNomAplicacionWeb) throws Exception {
		String sCadena =    "          <!--COMPONENTE PARA CAMBIO DE APLICACIONES -->\n";
		sCadena = sCadena + "          <script> \n";
		sCadena = sCadena + "             function MM_jumpMenu(targ,selObj,restore){ \n";
		sCadena = sCadena + "                 eval(targ + \".location='" + sNomAplicacionWeb + "/CambiaAplicacion?CveAplicacion=\"+ selObj.options[selObj.selectedIndex].value + \"'\"); \n";
		sCadena = sCadena + "                 if (restore) selObj.selectedIndex=0; \n";
		sCadena = sCadena + "             } \n";
		sCadena = sCadena + "          </script> \n";
		sCadena = sCadena + "          <select name='menuAplicacion' class='CapturaTexto' onChange=\"MM_jumpMenu('parent',this,0)\"> \n";
		Iterator lista = listaAplicaciones.iterator();
		while (lista.hasNext()){
			Registro aplicacion = (Registro)lista.next();
			sCadena = sCadena + "              <option value='" + aplicacion.getDefCampo("CVE_APLICACION") + "'>" + aplicacion.getDefCampo("NOM_APLICACION") + "</option>\n";
		}
		sCadena = sCadena + "          </select> \n";
		return sCadena;
	}
}
