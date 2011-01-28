<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoGrupalCreditoIndividual" precarga="SimPrestamoCargoComisionCliente SimPrestamoGrupalMontoEtapa">
	<Portal:PaginaNombre titulo="Prestamo Grupal" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimProductoCiclo' parametros='IdProducto=${param.IdProducto}&IdCiclo=${param.IdCiclo}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<c:if test='${param.BExisteCicloSig == "F"}'>
			<Portal:FormaElemento etiqueta='N&uacute;mero de ciclo' control='etiqueta-controloculto' controlnombre='NumCiclo' controlvalor='${requestScope.registroCiclo.campos["NUMERO_CICLO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false' obligatorio='true'/>			
		</c:if>	
		<c:if test='${param.BExisteCicloSig == "V"}'>
			<Portal:FormaElemento etiqueta='N&uacute;mero de ciclo' control='etiqueta-controloculto' controlnombre='NumCiclo' controlvalor='${requestScope.registro.campos["NUM_CICLO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false' obligatorio='true'/>			
		</c:if>	
		
		
		<Portal:FormaElemento etiqueta='Plazo' control='Texto' controlnombre='Plazo' controlvalor='${requestScope.registro.campos["PLAZO"]}' controllongitud='3' controllongitudmax='3' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Periodicidad del Producto' control='Texto' controlnombre='IdPeriodicidad' controlvalor='${requestScope.registro.campos["NOM_PERIODICIDAD"]}' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Valor de la tasa' control='Texto' controlnombre='ValorTasa' controlvalor='${requestScope.registro.campos["VALOR_TASA"]}' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='false' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>
		<c:if test='${(requestScope.registro.campos["ID_TASA_REFERENCIA"] != null)}'>	
			<Portal:FormaElemento etiqueta='Tasa de referencia' control='Texto' controlnombre='IdTasaReferencia' controlvalor='${requestScope.registro.campos["NOM_TASA_REFERENCIA"]}' editarinicializado='false'/>
		</c:if>	
		<Portal:FormaElemento etiqueta='Monto m&aacute;ximo' control='Texto' controlnombre='MontoMaximo' controlvalor='${requestScope.registro.campos["MONTO_MAXIMO"]}' editarinicializado='false' obligatorio='true'/>
		
		<Portal:FormaSeparador nombre="Recargos"/>
		
		<Portal:FormaElemento etiqueta='Recargos' control='Texto' controlnombre='TipoRecargo' controlvalor='${requestScope.registro.campos["NOM_ACCESORIO"]}' controllongitud='17' controllongitudmax='17' editarinicializado='false' obligatorio='true'/>
		
		<c:if test='${(requestScope.registro.campos["ID_ACCESORIO"] == "3") or (requestScope.registro.campos["ID_ACCESORIO"] == "5")}'>
			<c:if test='${(requestScope.registro.campos["TIPO_TASA_RECARGO"] == "1")}'>
				<Portal:FormaElemento etiqueta='Tipo de tasa' control='Texto' controlnombre='TipoTasaRecargo' controlvalor='${requestScope.registro.campos["NOM_TIPO_TASA_RECARGO"]}' controllongitud='17' controllongitudmax='17' editarinicializado='false' obligatorio='true'/>
				<c:if test='${(requestScope.registro.campos["TASA_RECARGO"] != null)}'>
					<Portal:FormaElemento etiqueta='Tasa de recargo' control='Texto' controlnombre='TasaRecargo' controlvalor='${requestScope.registro.campos["TASA_RECARGO"]}' editarinicializado='false'/>
					<Portal:FormaElemento etiqueta='Periodicidad' control='Texto' controlnombre='IdPeriodicidadTasaRecargo' controlvalor='${requestScope.registro.campos["NOM_PERIODICIDAD_TASA_RECARGO"]}' editarinicializado='false' obligatorio='false'/>
				</c:if>	
				<c:if test='${(requestScope.registro.campos["ID_TASA_REFERENCIA_RECARGO"] != null)}'>
					<Portal:FormaElemento etiqueta='Tasa de recargo Papeles' control='Texto' controlnombre='IdTasaReferenciaRecargo' controlvalor='${requestScope.registro.campos["NOM_TASA_REFERENCIA_RECARGO"]}' editarinicializado='false' obligatorio='false'/>
				</c:if>	
			</c:if>
			
			<c:if test='${(requestScope.registro.campos["TIPO_TASA_RECARGO"] == "2")}'>		
				<Portal:FormaElemento etiqueta='Tipo de tasa' control='Texto' controlnombre='TipoTasaRecargo' controlvalor='${requestScope.registro.campos["NOM_TIPO_TASA_RECARGO"]}' editarinicializado='false'/>
				<Portal:FormaElemento etiqueta='Factor sobre la tasa de interes' control='Texto' controlnombre='FactorTasaRecargo' controlvalor='${requestScope.registro.campos["FACTOR_TASA_RECARGO"]}' editarinicializado='false'/>
			</c:if>
		</c:if>
		
		<c:if test='${(requestScope.registro.campos["ID_ACCESORIO"] == "4") or (requestScope.registro.campos["ID_ACCESORIO"] == "5")}'>
			<Portal:FormaElemento etiqueta='Monto fijo por periodo' control='Texto' controlnombre='MontoFijoPeriodo' controlvalor='${requestScope.registro.campos["MONTO_FIJO_PERIODO"]}' editarinicializado='false'/>		
		</c:if>
		
		
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	
	<Portal:TablaLista tipo="consulta" nombre="Cargos y comisiones del Producto">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Cargo o comisi&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='180' valor='Forma de aplicaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Cargo inicial'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Porcentaje del monto'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Cantidad fija'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Valor'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Unidad'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Periodicidad'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaCargoComision}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_CARGO_COMISION"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ACCESORIO"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='180' valor='${registro.campos["NOM_FORMA_APLICACION"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CARGO_INICIAL"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["PORCENTAJE_MONTO"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CANTIDAD_FIJA"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["VALOR"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_UNIDAD"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_PERIODICIDAD"]}'/>	
			</Portal:TablaListaRenglon>
		</c:forEach>	
	</Portal:TablaLista>
	

	<Portal:TablaForma maestrodetallefuncion="SimPrestamoGrupalMontoEtapa" nombre="Montos solicitados" funcion="SimPrestamoGrupalCreditoIndividual" operacion="AL" parametros='IdProducto=${param.IdProducto}&IdGrupo=${param.IdGrupo}&NumCiclo=${param.NumCiclo}&BExisteCicloSig=${param.BExisteCicloSig}&Ciclo=${param.Ciclo}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre del cliente'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo de negocio'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Giro'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Monto solicitado'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Monto autorizado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaMontoEtapa}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<input type='hidden' name='IdCliente' value='<c:out value='${registro.campos["ID_INTEGRANTE"]}'/>'>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_TIPO_NEGOCIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_GIRO"]}'/>		
				<td>
				$ <input type='text' name='MontoSolicitado' size='18' maxlength='22' value='<c:out value='${registro.campos["MONTO_SOLICITADO"]}'/>'>
				</td>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<input type="button" name="Aceptar" value="Aceptar" onclick='javascript:fAceptar();'>
		</Portal:FormaBotones>
	</Portal:TablaForma>
	
	<script>
		function fAceptar(){
			document.frmTablaForma.Aceptar.disabled = true;
			document.frmTablaForma.Aceptar.value = "Alta de Crédito"; 
			document.frmTablaForma.submit();
		}
	</script>
	
</Portal:Pagina>
