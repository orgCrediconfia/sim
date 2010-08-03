<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoPapelDetalle">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Tasa de Referencia" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoPapelDetalle' parametros='IdPapel=${param.IdPapel}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdPapelDetalle' controlvalor='${requestScope.registro.campos["ID_TASA_REFERENCIA_DETALLE"]}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:Calendario2 etiqueta='Fecha de publicaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaPublicacion' controlvalor='${requestScope.registro.campos["FECHA_PUBLICACION"]}'  esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Valor' control='Texto' controlnombre='Valor' controlvalor='${requestScope.registro.campos["VALOR"]}' controllongitud='9' controllongitudmax='8' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
