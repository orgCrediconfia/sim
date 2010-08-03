<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoCargoComision">
	<Portal:PaginaNombre titulo="Cargo o Comisi&oacute;n" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimProductoCargoComision' parametros='IdProducto=${param.IdProducto}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			    <Portal:Url tipo='catalogo-ventana' url='/Aplicaciones/Sim/Producto/fSimProCarComCon.jsp?IdProducto=${param.IdProducto}' nombreliga='Asignar cargo o comisi&oacute;n' nomventana='VentanaCargoComision'/>
			</Portal:FormaElemento>
		</c:if>
		<Portal:FormaElemento etiqueta='Cargo o comisi&oacute;n' control='etiqueta-controlreferencia' controlnombre='NomCargoComision' controlvalor='${requestScope.registro.campos["NOM_ACCESORIO"]}' />
		<input type="hidden" name="IdCargoComision" value='<c:out value='${requestScope.registro.campos["ID_CARGO_COMISION"]}'/>' />
		<c:if test='${(requestScope.registro == null)}'>
			<tr>
				<th>Forma de aplicaci&oacute;n</th>
				<td>
					<select name='FormaAplicacion' size='1' onchange='fFormaAplicacion();'>
						<option value='null'></option>
						<option value='1'>Cargo inicial</option>
						<option value='2'>Como depósito inicial</option>
						<option value='3'>Periódicamente dependiendo del monto prestado</option>
						<option value='4'>Periódicamente dependiendo del saldo a la fecha de cálculo</option>
						<option value='5'>Fijo</option>
					</select>
				</td>
			</tr>
		</c:if>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Forma de aplicaci&oacute;n' control='etiqueta-controlreferencia' controlnombre='NomFormaAplicacion' controlvalor='${requestScope.registro.campos["NOM_FORMA_APLICACION"]}' />
			<input type="hidden" name="FormaAplicacion" value='<c:out value='${requestScope.registro.campos["ID_FORMA_APLICACION"]}'/>' />
		</c:if>
		<Portal:FormaElemento etiqueta='Cargo incial' control='Texto' controlnombre='CargoInicial' controlvalor='${requestScope.registro.campos["CARGO_INICIAL"]}' controllongitud='15' controllongitudmax='15' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
		
		
		<tr>
			<th>Porcentaje del monto prestado</th>
			<td>
				<input type='text' name='PorcentajeMonto' size='6' maxlength='6' value='<c:out value='${requestScope.registro.campos["PORCENTAJE_MONTO"]}'/>' onkeydown="CantidadesMonetarias();"> &#37
			</td>
		</tr>
		
		
		<Portal:FormaElemento etiqueta='Cantidad fija' control='Texto' controlnombre='CantidadFija' controlvalor='${requestScope.registro.campos["CANTIDAD_FIJA"]}' controllongitud='15' controllongitudmax='15' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Valor' control='Texto' controlnombre='Valor' controlvalor='${requestScope.registro.campos["VALOR"]}' controllongitud='11' controllongitudmax='11' editarinicializado='true' obligatorio='false' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Unidad' control='selector' controlnombre='Unidad' controlvalor='${registro.campos["ID_UNIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_UNIDAD" campodescripcion="NOM_UNIDAD" datosselector='${requestScope.ListaUnidad}'/>	
		<Portal:FormaElemento etiqueta='Periodicidad' control='selector' controlnombre='IdPeriodicidad' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>
	
	<script>
	
	
	
	if(document.frmRegistro.FormaAplicacion.value == "1"){
		document.frmRegistro.CargoInicial.disabled = false;
		document.frmRegistro.PorcentajeMonto.disabled = false;
		document.frmRegistro.CantidadFija.disabled = true;
		document.frmRegistro.Valor.disabled = true;
		document.frmRegistro.Unidad.disabled = true;
		document.frmRegistro.IdPeriodicidad.disabled = true;
			
		document.frmRegistro.CargoInicial.style.backgroundColor = 'white';
		document.frmRegistro.PorcentajeMonto.style.backgroundColor = 'white';
		document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.Valor.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.Unidad.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.IdPeriodicidad.style.backgroundColor = '#CCCCCC';
			
			
	}else if(document.frmRegistro.FormaAplicacion.value == "2"){
		document.frmRegistro.CargoInicial.disabled = true;
		document.frmRegistro.PorcentajeMonto.disabled = false;
		document.frmRegistro.CantidadFija.disabled = false;
		document.frmRegistro.Valor.disabled = true;
		document.frmRegistro.Unidad.disabled = true;
		document.frmRegistro.IdPeriodicidad.disabled = true;
			
		document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.PorcentajeMonto.style.backgroundColor = 'white';
		document.frmRegistro.CantidadFija.style.backgroundColor = 'white';
		document.frmRegistro.Valor.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.Unidad.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.IdPeriodicidad.style.backgroundColor = '#CCCCCC';
			
	}else if(document.frmRegistro.FormaAplicacion.value == "3"){
		document.frmRegistro.CargoInicial.disabled = true;
		document.frmRegistro.PorcentajeMonto.disabled = true;
		document.frmRegistro.CantidadFija.disabled = true;
		document.frmRegistro.Valor.disabled = false;
		document.frmRegistro.Unidad.disabled = false;
		document.frmRegistro.IdPeriodicidad.disabled = false;
			
		document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.PorcentajeMonto.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.Valor.style.backgroundColor = 'white';
		document.frmRegistro.Unidad.style.backgroundColor = 'white';
		document.frmRegistro.IdPeriodicidad.style.backgroundColor = 'white';
		
	}else if(document.frmRegistro.FormaAplicacion.value == "4"){
		document.frmRegistro.CargoInicial.disabled = true;
		document.frmRegistro.PorcentajeMonto.disabled = true;
		document.frmRegistro.CantidadFija.disabled = true;
		document.frmRegistro.Valor.disabled = false;
		document.frmRegistro.Unidad.disabled = false;
		document.frmRegistro.IdPeriodicidad.disabled = false;
			
		document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.PorcentajeMonto.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.Valor.style.backgroundColor = 'white';
		document.frmRegistro.Unidad.style.backgroundColor = 'white';
		document.frmRegistro.IdPeriodicidad.style.backgroundColor = 'white';
	} 
	
	else if(document.frmRegistro.FormaAplicacion.value == "5"){
		document.frmRegistro.CargoInicial.disabled = true;
		document.frmRegistro.PorcentajeMonto.disabled = true;
		document.frmRegistro.CantidadFija.disabled = true;
		document.frmRegistro.Valor.disabled = false;
		document.frmRegistro.Unidad.disabled = true;
		document.frmRegistro.IdPeriodicidad.disabled = false;
			
		document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.PorcentajeMonto.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.Valor.style.backgroundColor = 'white';
		document.frmRegistro.Unidad.style.backgroundColor = '#CCCCCC';
		document.frmRegistro.IdPeriodicidad.style.backgroundColor = 'white';
	}	
	
	function fFormaAplicacion(){
		if(document.frmRegistro.FormaAplicacion.value == "1"){
			document.frmRegistro.CargoInicial.disabled = false;
			document.frmRegistro.PorcentajeMonto.disabled = false;
			document.frmRegistro.CantidadFija.disabled = true;
			document.frmRegistro.Valor.disabled = true;
			document.frmRegistro.Unidad.disabled = true;
			document.frmRegistro.IdPeriodicidad.disabled = true;
				
			document.frmRegistro.CargoInicial.style.backgroundColor = 'white';
			document.frmRegistro.PorcentajeMonto.style.backgroundColor = 'white';
			document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.Valor.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.Unidad.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.IdPeriodicidad.style.backgroundColor = '#CCCCCC';
				
				
		}else if(document.frmRegistro.FormaAplicacion.value == "2"){
			document.frmRegistro.CargoInicial.disabled = true;
			document.frmRegistro.PorcentajeMonto.disabled = false;
			document.frmRegistro.CantidadFija.disabled = false;
			document.frmRegistro.Valor.disabled = true;
			document.frmRegistro.Unidad.disabled = true;
			document.frmRegistro.IdPeriodicidad.disabled = true;
				
			document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.PorcentajeMonto.style.backgroundColor = 'white';
			document.frmRegistro.CantidadFija.style.backgroundColor = 'white';
			document.frmRegistro.Valor.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.Unidad.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.IdPeriodicidad.style.backgroundColor = '#CCCCCC';
				
		}else if(document.frmRegistro.FormaAplicacion.value == "3"){
			document.frmRegistro.CargoInicial.disabled = true;
			document.frmRegistro.PorcentajeMonto.disabled = true;
			document.frmRegistro.CantidadFija.disabled = true;
			document.frmRegistro.Valor.disabled = false;
			document.frmRegistro.Unidad.disabled = false;
			document.frmRegistro.IdPeriodicidad.disabled = false;
				
			document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.PorcentajeMonto.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.Valor.style.backgroundColor = 'white';
			document.frmRegistro.Unidad.style.backgroundColor = 'white';
			document.frmRegistro.IdPeriodicidad.style.backgroundColor = 'white';
			
		}else if(document.frmRegistro.FormaAplicacion.value == "4"){
			document.frmRegistro.CargoInicial.disabled = true;
			document.frmRegistro.PorcentajeMonto.disabled = true;
			document.frmRegistro.CantidadFija.disabled = true;
			document.frmRegistro.Valor.disabled = false;
			document.frmRegistro.Unidad.disabled = false;
			document.frmRegistro.IdPeriodicidad.disabled = false;
				
			document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.PorcentajeMonto.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.Valor.style.backgroundColor = 'white';
			document.frmRegistro.Unidad.style.backgroundColor = 'white';
			document.frmRegistro.IdPeriodicidad.style.backgroundColor = 'white';
		} 
		
		else if(document.frmRegistro.FormaAplicacion.value == "5"){
			document.frmRegistro.CargoInicial.disabled = true;
			document.frmRegistro.PorcentajeMonto.disabled = true;
			document.frmRegistro.CantidadFija.disabled = true;
			document.frmRegistro.Valor.disabled = false;
			document.frmRegistro.Unidad.disabled = true;
			document.frmRegistro.IdPeriodicidad.disabled = false;
				
			document.frmRegistro.CargoInicial.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.PorcentajeMonto.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.CantidadFija.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.Valor.style.backgroundColor = 'white';
			document.frmRegistro.Unidad.style.backgroundColor = '#CCCCCC';
			document.frmRegistro.IdPeriodicidad.style.backgroundColor = 'white';
		}	
	}
	
	</script>
	
</Portal:Pagina>	