<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaCancelacionPago">
	<Portal:PaginaNombre titulo="Cancelación de pagos*********" subtitulo=""/>

	<Portal:Forma tipo='catalogo' funcion='SimCajaCancelacionPago' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaSeparador nombre="Filtros de búsqueda"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='IdPrestamo' controlvalor='${param.IdPrestamo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Clave del grupo' control='Texto' controlnombre='IdGrupo' controlvalor='${param.IdGrupo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Clave del producto' control='Texto' controlnombre='IdProducto' controlvalor='${param.IdProducto}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${param.NumCiclo}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Buscar"  value="Buscar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Cliente'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Grupo'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='45' valor='Ciclo'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto pagado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPago}">		
			<Portal:TablaListaRenglon>
				<c:if test='${(registro.campos["FECHA_CANCELACION"] == null)}'>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>		
						<a id="CancelarPago" href="javascript:fCancelarPago('<c:out value='${registro.campos["ID_TRANSACCION"]}'/>','<c:out value='${registro.campos["CVE_MOVIMIENTO_CAJA"]}'/>');">Cancelar pago</a>			
					</Portal:Columna>	
				</c:if>
				<c:if test='${(registro.campos["FECHA_CANCELACION"] != null)}'>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					</Portal:Columna>	
				</c:if>
				<c:if test='${(registro.campos["ID_PRESTAMO"] != null)}'>	
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PRESTAMO"]}'/>
				</c:if>
				<c:if test='${(registro.campos["ID_PRESTAMO_GRUPO"] != null)}'>	
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PRESTAMO_GRUPO"]}'/>
				</c:if>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='45' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
		
	<script>
		function fAceptar(){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCancelacionPago&OperacionCatalogo=CT&Filtro=Inicio&IdCaja="+document.frmRegistro.IdCaja.value+"&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value+"&IdPrestamo="+document.frmRegistro.IdPrestamo.value;
				document.frmRegistro.submit();
		}
		
		function fCancelarPago(sIdTransaccion,sCveMovimientoCaja){
			var answer;
			answer = confirm('¿Esta seguro que desea cancelar este pago?');
		
			if (answer){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCancelacionPago&OperacionCatalogo=AL&IdTransaccion="+sIdTransaccion+"&CveMovimientoCaja="+sCveMovimientoCaja;
				document.frmRegistro.submit();
			}	
		}
		
	</script>
	
</Portal:Pagina>