<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimSucursalSupervisor">

	<Portal:PaginaNombre titulo="Supervisores" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de Regionales" funcion="SimSucursalSupervisor" operacion="AL" parametros='IdSucursal=${param.IdSucursal}&IdDomicilio=${param.IdDomicilio}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Supervisor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_SUPERVISOR"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_SUPERVISOR"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	