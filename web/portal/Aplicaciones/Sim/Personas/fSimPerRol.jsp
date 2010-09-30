<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPersonaRoles">
	<Portal:PaginaNombre titulo="Roles" subtitulo="Consulta de datos"/>
	<Portal:TablaForma nombre="Roles" funcion="SimPersonaRoles" operacion="AL" parametros='IdPersona=${param.IdPersona}&IdExConyuge=${param.IdExConyuge}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Roles'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='RolAlta${registro.campos["CVE_TIPO_PERSONA"]}'>
					<input type="hidden" name="CveTipoPersona" value='<c:out value='${registro.campos["CVE_TIPO_PERSONA"]}'/>'>		
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_TIPO_PERSONA"]}'/>
			</Portal:TablaListaRenglon>
			
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	
