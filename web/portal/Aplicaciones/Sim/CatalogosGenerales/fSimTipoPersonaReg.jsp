<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoPersona">
	<Portal:PaginaNombre titulo="Catálogo Tipo de Persona" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoTipoPersona'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Id Tipo Persona' control='Texto' controlnombre='IdTipoPersona' controlvalor='${param.IdTipoPersona}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomTipoPersona' controlvalor='${requestScope.registro.campos["NOM_TIPO_PERSONA"]}' controllongitud='60' controllongitudmax='50' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripción' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}'  	controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='false' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
