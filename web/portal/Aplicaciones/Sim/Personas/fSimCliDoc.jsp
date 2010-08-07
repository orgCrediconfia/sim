<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteDocumentacion">
	<Portal:PaginaNombre titulo="Documentaci&oacute;n del Cliente" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='10' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Integrante'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Documento'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fecha de recibido'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='10' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PERFIL"]}' funcion='CataUsuPerEmpresa' operacion='CR' parametros='CvePerfil=${registro.campos["CVE_PERFIL"]}&CveAplicacion=${registro.campos["CVE_APLICACION"]}}'parametrosregreso='\'${registro.campos["CVE_PERFIL"]}\', \'${registro.campos["CVE_APLICACION"]}\', \'${registro.campos["TX_DESC_APLICACION"]}\', \'${registro.campos["NOM_PERFIL"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_PERFIL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TX_DESC_APLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["TX_DESC_APLICACION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteDocumentacion&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${registro.campos["ID_PERSONA"]}'/>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
</Portal:Pagina>	
