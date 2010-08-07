<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoCargoComisionCliente">
	<Portal:PaginaNombre titulo="Cargos y comisiones del Cliente" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoCargoComisionCliente'>
		<Portal:FormaSeparador nombre="Cliente"/>
		<Portal:FormaElemento etiqueta='Cliente' control='etiqueta-controlreferencia' controlnombre='IdCliente' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' editarinicializado='false'/>
		
		<Portal:FormaBotones>
		</Portal:FormaBotones>	
	</Portal:Forma>	
	
	<Portal:TablaForma nombre="Cargos y comisiones" funcion="SimPrestamoCargoComisionCliente" operacion="AL" parametros='&IdPrestamo=${param.IdPrestamo}&IdCliente=${param.IdCliente}&OperacionCatalogo=AL'>
			<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Cargo o comisi&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Forma de aplicaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Cargo inicial'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Porcentaje del monto'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Cantidad fija'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Valor'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Unidad'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Periodicidad'/>
			</Portal:TablaListaTitulos>
			
			<c:forEach var="registro" items="${requestScope.ListaBusqueda}">	
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_CARGO_COMISION"]}'/>
					<input type='hidden' name='IdCargoComision' 	value='<c:out value='${registro.campos["ID_CARGO_COMISION"]}'/>'>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ACCESORIO"]}'/>	
					<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='IdFormaAplicacion' controlvalor='${registro.campos["ID_FORMA_APLICACION"]}' editarinicializado='true' obligatorio='false' campoclave="ID_FORMA_APLICACION" campodescripcion="NOM_FORMA_APLICACION" datosselector='${requestScope.ListaFormaAplicacion}'/>	
					<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='CargoInicial' controlvalor='${registro.campos["CARGO_INICIAL"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
					
					
					<td>
						<input type='text' name='PorcentajeMonto' size='9' maxlength='9' value='<c:out value='${requestScope.registro.campos["PORCENTAJE_MONTO"]}'/>' onkeydown="CantidadesMonetarias();"> &#37
						<script>fAgregaCampo('PorcentajeMonto', '');</script>
					</td>
					
					
					<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='CantidadFija' controlvalor='${registro.campos["CANTIDAD_FIJA"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
					<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='Valor' controlvalor='${registro.campos["VALOR"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
					<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='IdUnidad' controlvalor='${registro.campos["ID_UNIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_UNIDAD" campodescripcion="NOM_UNIDAD" datosselector='${requestScope.ListaUnidad}'/>	
					<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='IdPeriodicidad' controlvalor='${registro.campos["ID_PERIODICIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
				</Portal:TablaListaRenglon>
			</c:forEach>
			
			<Portal:FormaBotones>
				<Portal:Boton tipo='submit' etiqueta='Aceptar' />
			</Portal:FormaBotones>	 
		</Portal:TablaForma>	
		
		<script>
		
	</script>
	
</Portal:Pagina>	
