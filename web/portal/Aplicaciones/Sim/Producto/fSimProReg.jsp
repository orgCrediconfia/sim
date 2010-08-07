<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProducto" precarga="SimProductoParticipantes SimProductoSucursal SimProductoCiclo SimProductoCargoComision SimProductoCicloAccesorio SimProductoActividadRequisito SimProductoDocumentacion SimProductoFlujoEfectivo">
	<Portal:PaginaNombre titulo="Producto" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimProducto'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdProducto' controlvalor='${requestScope.registro.campos["ID_PRODUCTO"]}' editarinicializado='false'/>
		</c:if>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomProducto' controlvalor='${requestScope.registro.campos["NOM_PRODUCTO"]}' controllongitud='25' controllongitudmax='30' editarinicializado='true' obligatorio='true'/>
		<tr>
			<th>Aplica a</th>
			<td>
				<select name='AplicaA' size='1'  >
					<option value='Individual'>Individual</option>
					<option value='Grupo'>Grupo</option>
				</select>
			</td>
		</tr>
		
		<Portal:FormaElemento etiqueta='M&eacute;todo de c&aacute;lculo del producto' control='selector' controlnombre='CveMetodo' controlvalor='${requestScope.registro.campos["CVE_METODO"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_METODO" campodescripcion="NOM_METODO" datosselector='${requestScope.ListaMetodo}'/>	
		<Portal:FormaElemento etiqueta='Periodicidad' control='selector' controlnombre='IdPeriodicidad' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD"]}' editarinicializado='true' obligatorio='true' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
		<Portal:FormaElemento etiqueta='Monto m&iacute;nimo' control='Texto' controlnombre='MontoMinimo' controlvalor='${requestScope.registro.campos["MONTO_MINIMO"]}' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:Calendario2 etiqueta='Inicio de activaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaInicioActivacion' controlvalor='${requestScope.registro.campos["FECHA_INICIO_ACTIVACION"]}'  esfechasis='true'/>
		<Portal:Calendario2 etiqueta='Fin de activaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaFinActivacion' controlvalor='${requestScope.registro.campos["FECHA_FIN_ACTIVACION"]}'  esfechasis='true'/>
		<tr>
			<th>Forma de entrega</th>
			<td>
				<select name='FormaEntrega' size='1'  >
					<option value='null'></option>
					<option value='Directamente en caja'>Directamente en caja</option>
					<option value='En cheque'>En cheque</option>
					<option value='Transferencia electronica'>Transferencia electr&oacute;nica</option>
					<option value='Dispersion de fondos'>Dispersi&oacute;n de fondos</option>
				</select>
			</td>
		</tr>
		<c:if test='${(requestScope.registro == null)}'>
			<tr> 
			   	<th>Garant&iacute;as</th>
				<td> 
					<input type='checkbox' name='GarantiaSi' />S&iacute &nbsp;&nbsp; <input type='checkbox' name='GarantiaNo'/>No
				</td>
			</tr>
			
			<Portal:FormaElemento etiqueta='Asignar a todas las sucursales' control='checkbox' controlnombre='BSucursales' controlvalor='${requestScope.registro.campos["B_SUCURSALES"]}'/>	
		</c:if>
		<c:if test='${(requestScope.registro != null)}'>
			<c:if test='${(requestScope.registro.campos["B_GARANTIA"] == "V")}'>
				<tr> 
				   	<th>Garant&iacute;as</th>
					<td> 
						<input type='checkbox' name='GarantiaSi'/ checked >S&iacute &nbsp;&nbsp; <input type='checkbox' name='GarantiaNo'/>No
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_GARANTIA"] == "F")}'>
				<tr> 
				   	<th>Garant&iacute;as</th>
					<td> 
						<input type='checkbox' name='GarantiaSi' />S&iacute &nbsp;&nbsp; <input type='checkbox' name='GarantiaNo'/ checked >No
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_SUCURSALES"] == "V")}'>
				<tr> 
				   	<th>Asignar a todas las sucursales</th>
					<td> 
						<input type='checkbox' name='BSucursales'/ checked >
					</td>
				</tr>
			</c:if>
			<c:if test='${(requestScope.registro.campos["B_SUCURSALES"] == "F")}'>
				<tr> 
				   	<th>Asignar a todas las sucursales</th>
					<td> 
						<input type='checkbox' name='BSucursales'/>
					</td>
				</tr>
			</c:if>
		</c:if>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
			<c:if test='${(requestScope.registro != null)}'>
				<c:if test='${(requestScope.registro.campos["B_SUCURSALES"] == "F")}'>
					<Portal:Boton tipo='catalogo' etiqueta='Asignar Sucursales al Producto' funcion='SimProductoSucursal' operacion='IN' parametros='&IdProducto=${requestScope.registro.campos["ID_PRODUCTO"]}&Filtro=Inicio' />
				</c:if>	
			</c:if>	
		</Portal:FormaBotones>
		
	</Portal:Forma>	

		<Portal:TablaForma maestrodetallefuncion="SimProductoParticipantes" nombre="Participantes del cr&eacute;dito" funcion="SimProductoParticipantes" operacion="BA" parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}'>
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Participantes'/>
			</Portal:TablaListaTitulos>
			<c:forEach var="registro" items="${requestScope.ListaParticipantes}">		
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["CVE_TIPO_PERSONA"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_TIPO_PERSONA"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimProductoParticipantes&OperacionCatalogo=CT&Filtro=Todos&Participantes=Disponibles&IdProducto=${requestScope.registro.campos["ID_PRODUCTO"]}'/>
				<Portal:Boton tipo='submit' etiqueta='Baja' />
			</Portal:FormaBotones>				
		</Portal:TablaForma>
	
		<Portal:TablaForma maestrodetallefuncion="SimProductoSucursal" nombre="Sucursales asignadas al Producto" funcion="SimProductoSucursal" operacion="BA" parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}'>
			<Portal:TablaListaTitulos>
				<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
			</Portal:TablaListaTitulos>
			<c:forEach var="registro" items="${requestScope.ListaSucursal}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_SUCURSAL"]}' />						
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_SUCURSAL"]}'/>			
					<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUCURSAL"]}'/>					
				</Portal:TablaListaRenglon>
			</c:forEach>
			<Portal:FormaBotones>
				<c:if test='${(requestScope.ListaSucursal != null)}'>
					<Portal:Boton tipo='submit' etiqueta='Eliminar' />
				</c:if>	
			</Portal:FormaBotones>					
		</Portal:TablaForma>
	
	
	<Portal:TablaForma maestrodetallefuncion="SimProductoCiclo" nombre="Ciclos" funcion="SimProductoCiclo" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='N&uacute;mero de ciclo'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Plazo'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tasa'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Valor de la Tasa'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Monto m&aacute;ximo'/>
			<Portal:Columna tipovalor='texto' ancho='130' valor='Porcentaje de flujo a financiar'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Tipo de Recargo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaCiclo}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NUM_CICLO"]}' funcion='SimProductoCiclo' operacion='CR' parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}&NumCiclo=${registro.campos["NUM_CICLO"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["PLAZO"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TIPO_TASA"]}'/>	
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["VALOR_TASA"]}'/>			
				<Portal:Columna tipovalor='moneda' ancho='150' valor='$ ${registro.campos["MONTO_MAXIMO"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='130' valor='${registro.campos["PORC_FLUJO_CAJA"]} %'/>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ACCESORIO"]}'/>			
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='SimProductoCiclo' operacion='IN' parametros='&IdProducto=${requestScope.registro.campos["ID_PRODUCTO"]}' />
		</Portal:FormaBotones>					
	</Portal:TablaForma>
	
	
	<Portal:TablaForma maestrodetallefuncion="SimProductoCargoComision" nombre="Cargos y comisiones" funcion="SimProductoCargoComision" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Cargos o comisi&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Forma de aplicaci&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaCargoComision}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_CARGO_COMISION"]}' funcion='SimProductoCargoComision' operacion='CR' parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}&IdCargoComision=${registro.campos["ID_CARGO_COMISION"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACCESORIO"]}'/>			
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_FORMA_APLICACION"]}'/>	
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='SimProductoCargoComision' operacion='IN' parametros='&IdProducto=${requestScope.registro.campos["ID_PRODUCTO"]}' />
		</Portal:FormaBotones>					
	</Portal:TablaForma>
	
	
	<Portal:TablaForma maestrodetallefuncion="SimProductoCicloAccesorio" nombre="Accesorios por producto-ciclo" funcion="SimProductoCicloAccesorio" operacion="BA">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='1100' valor=''/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaCicloAccesorio}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='1100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='Accesorios del producto-ciclo ${registro.campos["ID_PRODUCTO"]}-${registro.campos["NUM_CICLO"]}' funcion='SimProductoCicloAccesorio' operacion='CR' parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}&NumCiclo=${registro.campos["NUM_CICLO"]}'/>
				</Portal:Columna>
			</Portal:TablaListaRenglon>
		</c:forEach>		
		<Portal:FormaBotones>
		</Portal:FormaBotones>			
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimProductoActividadRequisito" nombre="Actividades o requisitos" funcion="SimProductoActividadRequisito" operacion="BA" parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='300' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Etapa de verificaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Orden de la Etapa'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaActividadRequisito}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='300' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}' funcion='SimProductoActividadRequisito' operacion='CR' parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}&IdActividadRequisito=${registro.campos["ID_ACTIVIDAD_REQUISITO"]}&IdEstatusPrestamo=${registro.campos["ID_ETAPA_PRESTAMO"]}'/>
				</Portal:Columna>							
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TIPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["ORDEN_ETAPA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='SimProductoActividadRequisito' operacion='IN' parametros='&Filtro=Alta&IdProducto=${requestScope.registro.campos["ID_PRODUCTO"]}&AplicaA=${requestScope.registro.campos["APLICA_A"]}' />
		</Portal:FormaBotones>					
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimProductoDocumentacion" nombre="Documentaci&oacute;n" funcion="SimProductoDocumentacion" operacion="BA" parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='180' valor='Nombre del documento'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Documento a imprimir'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaDocumentacion}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_DOCUMENTO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_DOCUMENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='180' valor='${registro.campos["NOM_DOCUMENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_REPORTE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimProductoDocumentacionDisponible&OperacionCatalogo=CT&Filtro=Todos&IdProducto=${requestScope.registro.campos["ID_PRODUCTO"]}&AplicaA=${requestScope.registro.campos["APLICA_A"]}'/>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimProductoFlujoEfectivo" nombre="Archivos de flujo de efectivo" funcion="SimProductoFlujoEfectivo" operacion="BA" parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaArchivo}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_ARCHIVO_FLUJO"]}' funcion='SimProductoFlujoEfectivo' operacion='CR' parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}&IdArchivoFlujo=${registro.campos["ID_ARCHIVO_FLUJO"]}'/>
				</Portal:Columna>							
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ARCHIVO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='SimProductoFlujoEfectivo' operacion='IN' parametros='&IdProducto=${requestScope.registro.campos["ID_PRODUCTO"]}' />
		</Portal:FormaBotones>					
	</Portal:TablaForma>
		
	<script>
		<c:if test='${(requestScope.registro != null)}'>
			BuscaSelectOpcion(document.frmRegistro.AplicaA,'<c:out value='${requestScope.registro.campos["APLICA_A"]}'/>'); 
			BuscaSelectOpcion(document.frmRegistro.FormaEntrega,'<c:out value='${requestScope.registro.campos["FORMA_ENTREGA"]}'/>'); 
		</c:if>
	</script>
</Portal:Pagina>	