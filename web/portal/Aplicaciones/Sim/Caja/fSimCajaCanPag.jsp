<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaCancelacionPago">
	<Portal:PaginaNombre titulo="Cancelación de pagos" subtitulo=""/>

	<Portal:Forma tipo='catalogo' funcion='SimCajaCancelacionPago' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaSeparador nombre="Filtros de búsqueda"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='18' controllongitudmax='20' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del Cliente' control='Texto' controlnombre='NomCliente' controlvalor='${param.NomCliente}' controllongitud='30' controllongitudmax='28' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del Grupo' control='Texto' controlnombre='NomGrupo' controlvalor='${param.NomGrupo}' controllongitud='20' controllongitudmax='19' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Producto' control='Texto' controlnombre='NomProducto' controlvalor='${param.NomProducto}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${param.NumCiclo}' controllongitud='2' controllongitudmax='3' editarinicializado='true' obligatorio='true'  validadato='cantidades'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="TxRespuesta" value='<c:out value='${param.Respuesta}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Buscar"  value="Buscar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del prestamo'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='45' valor='Ciclo'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Fecha de aplicación'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto pagado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPago}">		
			<Portal:TablaListaRenglon>
				<c:if test='${(registro.campos["FECHA_CANCELACION"] == null)}'>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>		
						<a id="CancelarPago" href="javascript:fCancelarPago('<c:out value='${registro.campos["ID_TRANSACCION"]}'/>','<c:out value='${registro.campos["ID_PRESTAMO"]}'/>','<c:out value='${registro.campos["APLICA_A"]}'/>','<c:out value='${registro.campos["NUM_CICLO"]}'/>','<c:out value='${registro.campos["FECHA_TRANSACCION"]}'/>','<c:out value='${registro.campos["MONTO"]}'/>','<c:out value='${registro.campos["CVE_NOMBRE"]}'/>');">Cancelar pago</a>
					</Portal:Columna>	
				</c:if>
				<c:if test='${(registro.campos["FECHA_CANCELACION"] != null)}'>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					</Portal:Columna>	
				</c:if>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOMBRE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='45' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='45' valor='${registro.campos["FECHA_TRANSACCION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["IMPORTE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
		
	<script>
	
		if(document.frmRegistro.TxRespuesta.value != ''){
			alert(document.frmRegistro.TxRespuesta.value);
		} 
	
		function fAceptar(){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCancelacionPago&OperacionCatalogo=CT&Filtro=Todos&IdCaja="+document.frmRegistro.IdCaja.value+"&NomGrupo="+document.frmRegistro.NomGrupo.value+"&NomProducto="+document.frmRegistro.NomProducto.value+"&NumCiclo="+document.frmRegistro.NumCiclo.value+"&CvePrestamo="+document.frmRegistro.CvePrestamo.value+"&NomCliente="+document.frmRegistro.NomCliente.value;
				document.frmRegistro.submit();
		}
		
		function fCancelarPago(sIdTransaccion,sIdPrestamo,sAplicaA,sNumCiclo,sFAplicacion,sMonto,sCveNombre){
			var answer;
			answer = confirm('¿Esta seguro que desea cancelar el pago del día '+sFAplicacion+ ' por el importe de $'+ sMonto);
		
			if (answer){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCancelacionPago&OperacionCatalogo=AL&IdTransaccion="+sIdTransaccion+"&IdPrestamo="+sIdPrestamo+"&AplicaA="+sAplicaA+"&NumCiclo="+sNumCiclo+"&FAplicacion="+sFAplicacion+"&Monto="+sMonto+"&CveNombre="+sCveNombre;
				document.frmRegistro.submit();
			}	
		}
		
	</script>
	
</Portal:Pagina>