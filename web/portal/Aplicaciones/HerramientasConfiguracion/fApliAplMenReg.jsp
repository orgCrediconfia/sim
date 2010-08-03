<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionMenu">
	<Portal:PaginaNombre titulo="Men&uacute;" subtitulo="Modificaci&oacuten de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionMenu'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave aplicaci&oacute;n' control='etiqueta-controloculto' controlnombre='CveAplicacion' controlvalor='${param.CveAplicacion}' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave opci&oacute;n' control='Texto' controlnombre='CveOpcion' controlvalor='${requestScope.registro.campos["CVE_OPCION"]}' controllongitud='80' controllongitudmax='80' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomOpcion' controlvalor='${requestScope.registro.campos["NOM_OPCION"]}' controllongitud='30' controllongitudmax='30' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Funci&oacute;n' control='selector' controlnombre='CveFuncion' controlvalor='${requestScope.registro.campos["CVE_FUNCION"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_FUNCION" campodescripcion="NOM_FUNCION" datosselector='${requestScope.ListaFunciones}' />
		<Portal:FormaElemento etiqueta='Opci&oacute;n padre' control='selector' controlnombre='CveOpcionPadre' controlvalor='${requestScope.registro.campos["CVE_OPCION_PADRE"]}' editarinicializado='true' obligatorio='false' campoclave="CVE_OPCION" campodescripcion="NOM_OPCION_INDENTADO" datosselector='${requestScope.ListaMenu}' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>