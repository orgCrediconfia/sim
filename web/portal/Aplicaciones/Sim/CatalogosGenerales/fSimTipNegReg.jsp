<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoNegocio">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de tipo de negocio" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoTipoNegocio'>
	
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdTipoNegocio' controlvalor='${param.IdTipoNegocio}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre de tipo de negocio' control='Texto' controlnombre='NomTipoNegocio' controlvalor='${requestScope.registro.campos["NOM_TIPO_NEGOCIO"]}' controllongitud='30' controllongitudmax='50' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='TextoArea' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='35' controllongitudmax='5' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	
	</Portal:Forma>
</Portal:Pagina>	