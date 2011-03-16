<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoIdentificador">
	
	<Portal:PaginaNombre titulo="Catálogo Tipo de Identificador" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoTipoIdentificador'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Id Tipo Identificador' control='etiqueta-controloculto' controlnombre='IdTipoIdentificador' controlvalor='${param.IdTipoIdentificador}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='DescTipoIdentificador' controlvalor='${requestScope.registro.campos["DESC_TIPO_IDENTIFICADOR"]}' controllongitud='60' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
