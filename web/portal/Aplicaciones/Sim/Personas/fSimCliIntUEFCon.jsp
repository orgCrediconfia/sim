<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteIntegranteUEF">
	<Portal:PaginaNombre titulo="Integrantes de la UEF" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='300' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Parentesco'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='300' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_COMPLETO"]}' funcion='SimClienteIntegranteUEF' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&IdIntegrante=${registro.campos["ID_INTEGRANTE"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_PARENTESCO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteIntegranteUEF&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${param.IdPersona}'/>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
</Portal:Pagina>	
