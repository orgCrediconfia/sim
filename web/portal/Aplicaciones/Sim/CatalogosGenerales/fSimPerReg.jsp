<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCatalogoPerfil">
	<Portal:PaginaNombre titulo="Catálogo de Perfiles" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoPerfil'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CvePerfil' controlvalor='${param.CvePerfil}' controllongitud='20' controllongitudmax='20' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPerfil' controlvalor='${requestScope.registro.campos["NOM_PERFIL"]}' controllongitud='75' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>	
