<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimUsuarioRegional">

	<Portal:PaginaNombre titulo="Regionales" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de Regionales" funcion="SimUsuarioRegional" operacion="AL" parametros='CveUsuario=${param.CveUsuario}&IdPersona=${param.IdPersona}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Regional'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_REGIONAL"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_REGIONAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_REGIONAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	