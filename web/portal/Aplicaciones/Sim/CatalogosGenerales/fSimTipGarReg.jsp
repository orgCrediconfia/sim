<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoGarantia">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Tipo de Garant&iacute;a" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoTipoGarantia'>
	
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdTipoGarantia' controlvalor='${param.IdTipoGarantia}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Tipo de garant&iacute;a' control='Texto' controlnombre='NomTipoGarantia' controlvalor='${requestScope.registro.campos["NOM_TIPO_GARANTIA"]}' controllongitud='45' controllongitudmax='100' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='80' controllongitudmax='100' />
		<Portal:FormaElemento etiqueta='Requisitos' control='Texto' controlnombre='Requisitos' controlvalor='${requestScope.registro.campos["REQUISITOS"]}' controllongitud='80' controllongitudmax='100' />		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	
	</Portal:Forma>
</Portal:Pagina>	