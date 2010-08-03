<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaCorte">
	<Portal:PaginaNombre titulo="Corte de caja" subtitulo=""/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaCorte' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaSeparador nombre="Corte de caja"/>
		<Portal:Calendario2 etiqueta='Fecha inicial' contenedor='frmRegistro' controlnombre='FechaInicial' controlvalor='${requestScope.registro.campos["FECHA_INICIAL"]}'  esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fecha final' contenedor='frmRegistro' controlnombre='FechaFinal' controlvalor='${requestScope.registro.campos["FECHA_FINAL"]}'  esfechasis='false'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdMovimiento" value='<c:out value='${param.IdMovimiento}'/>' />
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Aceptar" onclick='javascript:fAceptar()'>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaLista tipo="Corte de Caja" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha'/>
			<Portal:Columna tipovalor='texto' ancho='400' valor='Movimiento'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto'/>
		</Portal:TablaListaTitulos>
		<Portal:TablaListaRenglon>
			<c:if test='${(requestScope.ListaMovimiento != null)}'>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Saldo inicial'/>
				<Portal:Columna tipovalor='texto' ancho='400' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registroFechaInicial.campos["SALDO_INICIAL"]}'/>
			</c:if>
		</Portal:TablaListaRenglon>
		<c:forEach var="registro" items="${requestScope.ListaMovimiento}">		
			<Portal:TablaListaRenglon>			
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='400' valor='${registro.campos["NOM_MOVIMIENTO_CAJA"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:TablaListaRenglon>
			<c:if test='${(requestScope.ListaMovimiento != null)}'>
				<Portal:Columna tipovalor='texto' ancho='100' valor='Saldo final'/>
				<Portal:Columna tipovalor='texto' ancho='400' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registroFechaFinal.campos["SALDO_FINAL"]}'/>
			</c:if>
		</Portal:TablaListaRenglon>
	</Portal:TablaLista>
	
	<script>
	function fAceptar(){
		if (document.frmRegistro.FechaInicial.value == ""){
			alert ("Ingrese la fecha inicial");
		}else if (document.frmRegistro.FechaFinal.value == ""){
			alert ("Ingrese la fecha final");
		} else {
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaCorte&OperacionCatalogo=CT&Filtro=Todos&IdCaja="+document.frmRegistro.IdCaja.value+"&FechaFinal="+document.frmRegistro.FechaFinal.value+"&FechaInicial="+document.frmRegistro.FechaInicial.value;
				document.frmRegistro.submit();
		}
	}
	</script>
	
</Portal:Pagina>