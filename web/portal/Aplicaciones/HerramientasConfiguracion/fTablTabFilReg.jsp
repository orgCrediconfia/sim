<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionTablaFiltro">
	<Portal:PaginaNombre titulo="Filtros de b&uacute;squeda" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionTablaFiltro'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave tabla' control='etiqueta-controloculto' controlnombre='CveTabla' controlvalor='${param.CveTabla}'/>		
		<Portal:FormaElemento etiqueta='Clave filtro' control='Texto' controlnombre='CveFiltro' controlvalor='${requestScope.registro.campos["CVE_FILTRO"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Muestra sql en consola' control='SelectorLogico' controlnombre='bMuestraCodigo' controlvalor='${requestScope.registro.campos["B_MUESTRA_CODIGO"]}' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='C&oacute;digo sql' control='TextoArea' controlnombre='TxSqlFiltro' controlvalor='${requestScope.registro.campos["TX_SQL_FILTRO"]}' controllongitud='135' controllongitudmax='33' editarinicializado='true' obligatorio='true' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<Portal:FormaBotonBitacora/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>