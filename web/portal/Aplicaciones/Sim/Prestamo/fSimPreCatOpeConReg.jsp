<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoCatalogoOperacionConcepto">
	<Portal:PaginaNombre titulo="Conceptos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoCatalogoOperacionConcepto' parametros='CveOperacion=${param.CveOperacion}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Operaci&oacute;n' control='Texto' controlnombre='DescLargaOperacion' controlvalor='${requestScope.registro.campos["DESC_LARGA_OPERACION"]}' editarinicializado='false' obligatorio='true' />
		</c:if>
		
		<Portal:FormaElemento etiqueta='Concepto' control='selector' controlnombre='CveConcepto' controlvalor='${requestScope.registro.campos["CVE_CONCEPTO"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_CONCEPTO" campodescripcion="DESC_LARGA" datosselector='${requestScope.ListaConcepto}'/>
		
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>	