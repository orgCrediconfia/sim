<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionFuncion">
	<Portal:PaginaNombre titulo="Funciones" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='HerramientasConfiguracionFuncion' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveFuncion' controlvalor='${param.CveFuncion}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomFuncion' controlvalor='${param.NomFuncion}' controllongitud='35' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Bloqueado' control='SelectorLogico' controlnombre='Bloqueado' controlvalor='' editarinicializado='false' obligatorio='true' />
	</Portal:Forma>
	
	<input type="button" name="Imprimir" value="Imprimir cat&aacute;logo" onClick="javascript:MM_openBrWindow('/portal/ProcesaReporte?Funcion=HerramientasConfiguracionFuncionReporte&TipoReporte=Pdf','ReporteEstadisticaFuncion','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');return document.MM_returnValue">	
	
	<Portal:TablaLista tipo="alta" nombre="Consulta funciones" botontipo="url" url="/Aplicaciones/HerramientasConfiguracion/fApliFunReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='400' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Bloqueado'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='400' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_FUNCION"]}' funcion='HerramientasConfiguracionFuncion' operacion='CR' parametros='CveFuncion=${registro.campos["CVE_FUNCION"]}' clonarregistro='true' />
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='70' valor='${registro.campos["B_BLOQUEADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>
</Portal:Pagina>