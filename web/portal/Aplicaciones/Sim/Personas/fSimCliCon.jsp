<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClientes">
	<Portal:PaginaNombre titulo="Personas" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimClientes' operacion='CT' filtro='Todos' parametros='CveTipoPersona=${param.CveTipoPersona}'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPersona' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controllongitud='80' controllongitudmax='256' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Consulta de personas" funcion="SimClientes" operacion="IN" parametros="Filtro=Alta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='25' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='350' valor='Nombre'/>	
			<Portal:Columna tipovalor='texto' ancho='200' valor='Sucursal'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='RFC'/>	
				
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='25' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PERSONA"]}' funcion='SimClientes' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&IdExConyuge=${registro.campos["ID_EX_CONYUGE"]}' parametrosregreso='\'${registro.campos["ID_PERSONA"]}\', \'${registro.campos["NOM_COMPLETO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='350' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["RFC"]}'/>
				
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:if test='${(requestScope.ListaBusqueda == null)}'>
			<Portal:FormaBotones>
				<Portal:Boton tipo='submit' etiqueta='Alta' />
			</Portal:FormaBotones>
		</c:if>		
		<c:if test='${(requestScope.ListaBusqueda != null)}'>
			<Portal:FormaBotones>
				<Portal:Boton tipo='submit' etiqueta='Alta' />
				<c:if test='${(param.Paginas != 0)}'>
					<input type="button" name="Siguiente" value="Siguiente" onclick="javascript:MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimPersonaPaginacion&OperacionCatalogo=CT&Filtro=Todos&Paginas=<c:out value='${param.Paginas}'/>&Superior=<c:out value='${param.Superior}'/>&IdPersona=<c:out value='${param.IdPersona}'/>&NomCompleto=<c:out value='${param.NomCompleto}'/>&Rfc=<c:out value='${param.Rfc}'/>');" >
				</c:if>
			</Portal:FormaBotones>
		</c:if>		
	</Portal:TablaForma>
	
	<script>
		
		function RegresaDatos(IdPersona, NomCompleto){
			var padre = window.opener;
			
			padre.document.frmRegistro.IdPersona.value  = IdPersona;
			padre.document.getElementById('NomCompleto').innerHTML = NomCompleto;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		
	</script>
		
</Portal:Pagina>
