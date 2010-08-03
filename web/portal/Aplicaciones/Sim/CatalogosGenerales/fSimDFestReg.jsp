<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 



<Portal:Pagina funcion="SimCatalogoDiaFestivo">
	<Portal:PaginaNombre titulo="Días Festivos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoDiaFestivo'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Día Festivo' control='Texto' controlnombre='FDiaFestivo' controlvalor='${param.FDiaFestivo}' controllongitud='10' controllongitudmax='13' editarinicializado='false' obligatorio='true'/>
		</c:if>
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:Calendario2 etiqueta='Día Festivo' contenedor='frmRegistro' controlnombre='FDiaFestivo' esfechasis='false'/>
		</c:if>
		<Portal:FormaElemento etiqueta='Descripción' control='Texto' controlnombre='DescDiaFestivo' controlvalor='${requestScope.registro.campos["DESC_DIA_FESTIVO"]}' controllongitud='50' controllongitudmax='100' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
