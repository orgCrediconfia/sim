<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionMensaje">
	<Portal:PaginaNombre titulo="Mensajes del Sistema" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='HerramientasConfiguracionMensaje' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveMensaje' controlvalor='${param.CveMensaje}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Tipo' control='Texto' controlnombre='TipoMensaje' controlvalor='${param.TipoMensaje}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />	
	    <Portal:FormaElemento etiqueta='Mensaje' control='Texto' controlnombre='TxMensaje' controlvalor='${param.TxMensaje}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
	</Portal:Forma>
	
	<input type="button" name="Imprimir" value="Imprimir cat&aacute;logo" onClick="javascript:MM_openBrWindow('/portal/ProcesaReporte?Funcion=HerramientasConfiguracionMensajeReporte&TipoReporte=Pdf','ReporteEstadisticaFuncion','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');return document.MM_returnValue">
	
	<Portal:TablaLista tipo="alta" nombre="Consulta mensajes" botontipo="url" url="/Aplicaciones/HerramientasConfiguracion/fCataMenReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='50' valor='Tipo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Mensaje'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='350' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_MENSAJE"]}' funcion='HerramientasConfiguracionMensaje' operacion='CR' parametros='CveMensaje=${registro.campos["CVE_MENSAJE"]}' clonarregistro='true' />
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["TIPO_MENSAJE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='300' valor='${registro.campos["TX_MENSAJE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>
</Portal:Pagina>