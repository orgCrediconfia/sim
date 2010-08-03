<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCargoComisionSeguroVida">
	<Portal:PaginaNombre titulo="Cargos y comisiones del préstamo" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta" funcion='SimPrestamoCargoComision' operacion='BA' parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Cargo o comisi&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_CARGO_COMISION"]}' />		
				<c:if test='${(registro.campos["NOM_CARGO_COMISION"] == "Seguro de vida")}'>
					<Portal:Columna tipovalor='texto' ancho='150' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_CARGO_COMISION"]}' funcion='SimPrestamoCargoComisionSeguroVida' operacion='CR' parametros='IdCargoComision=${registro.campos["ID_CARGO_COMISION"]}&IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdProducto=${param.IdProducto}'/>
					</Portal:Columna>
				</c:if>
				<c:if test='${(registro.campos["NOM_CARGO_COMISION"] != "Seguro de vida")}'>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_CARGO_COMISION"]}'/>
				</c:if>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_CARGO_COMISION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>	
	</Portal:TablaForma>
</Portal:Pagina>	
