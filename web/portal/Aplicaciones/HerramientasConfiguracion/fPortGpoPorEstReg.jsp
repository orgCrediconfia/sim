<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionEstilo">
	<Portal:PaginaNombre titulo="Estilos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionEstilo'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave grupo empresa' control='etiqueta-controloculto' controlnombre='CveGpoEmpresa' controlvalor='${param.CveGpoEmpresa}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave portal' control='etiqueta-controloculto' controlnombre='CvePortal' controlvalor='${param.CvePortal}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Clave estilo' control='Texto' controlnombre='CveEstilo' controlvalor='${requestScope.registro.campos["CVE_ESTILO"]}' controllongitud='20' controllongitudmax='15' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre estilo' control='Texto' controlnombre='NomEstilo' controlvalor='${requestScope.registro.campos["NOM_ESTILO"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='URL estilo' control='Texto' controlnombre='UrlEstilo' controlvalor='${requestScope.registro.campos["URL_ESTILO"]}' controllongitud='50' controllongitudmax='150' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>