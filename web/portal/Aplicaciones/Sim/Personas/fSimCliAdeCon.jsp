<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteAdeudo">
	<Portal:PaginaNombre titulo="Adeudos del Cliente" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='250' valor='Persona o Institución a quién adeuda'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Saldo actual'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ADEUDO_A"]}' funcion='SimClienteAdeudo' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&IdAdeudo=${registro.campos["ID_ADEUDO"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["SALDO_ACTUAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteAdeudo&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${param.IdPersona}'/>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
</Portal:Pagina>	
