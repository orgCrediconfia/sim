<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionAplicacion" clavepagina="fApliAplCon">

	<Portal:PaginaNombre titulo="Aplicaciones" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='HerramientasConfiguracionAplicacion' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveAplicacion' controlvalor='' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomAplicacion' controlvalor='' controllongitud='35' controllongitudmax='256' editarinicializado='true' obligatorio='false' />	
		<Portal:FormaElemento etiqueta='Bloqueado' control='SelectorLogico' controlnombre='Bloqueado' controlvalor='' editarinicializado='false' obligatorio='true' />
	</Portal:Forma>

	<input type="button" name="Imprimir" value="Imprimir cat&aacute;logo" onClick="javascript:MM_openBrWindow('/portal/ProcesaReporte?Funcion=HerramientasConfiguracionAplicacionReporte  &TipoReporte=Pdf','ReporteEstadisticaAplicacion','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');return document.MM_returnValue">
	
	<Portal:TablaLista tipo="alta" nombre="Consulta aplicaciones" botontipo="url" url="/Aplicaciones/HerramientasConfiguracion/fApliAplReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Bloqueado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_APLICACION"]}' funcion='HerramientasConfiguracionAplicacion' operacion='CR' parametros='CveAplicacion=${registro.campos["CVE_APLICACION"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_APLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_BLOQUEADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>