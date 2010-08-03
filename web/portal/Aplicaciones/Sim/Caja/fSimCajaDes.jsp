<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaDesembolso">
	<Portal:PaginaNombre titulo="Entrega de préstamo o Desembolso" subtitulo=""/>

	<Portal:Forma tipo='busqueda' funcion='SimCajaDesembolso' operacion='CT' filtro='Todos' parametros='IdCaja=${param.IdCaja}'>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='IdPrestamo' controlvalor='${param.IdPrestamo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre del cliente' control='Texto' controlnombre='NomCompleto' controlvalor='${param.NomCompleto}' controllongitud='80' controllongitudmax='100' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Clave del grupo' control='Texto' controlnombre='IdGrupo' controlvalor='${param.IdGrupo}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Nombre del grupo' control='Texto' controlnombre='NomGrupo' controlvalor='${param.NomGrupo}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Clave del producto' control='Texto' controlnombre='IdProducto' controlvalor='${param.IdProducto}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${param.NumCiclo}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
	</Portal:Forma>	
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='400' valor='Nombre'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto autorizado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaMonto}">		
			<Portal:TablaListaRenglon>			
				<Portal:Columna tipovalor='texto' ancho='600' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.Total}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='400' valor='Total'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["TOTAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<input type="button" name="Aceptar"  value="Desembolso" onclick='javascript:fDesembolso()'>
		</Portal:FormaBotones>
	</Portal:TablaLista>
		
	<script>
		function fAceptar(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaDesembolso&OperacionCatalogo=CT&Filtro=Inicio&IdCaja="+document.frmRegistro.IdCaja.value;
			document.frmRegistro.submit();
		}
		
		function fDesembolso(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaDesembolso&OperacionCatalogo=AL&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdCaja="+document.frmRegistro.IdCaja.value;
			document.frmRegistro.submit();
			if (document.frmRegistro.IdGrupo.value == ""){
				MM_openBrWindow('/portal/ProcesaReporte?Funcion=SimCajaEntregaPrestamo&TipoReporte=Pdf&IdPrestamo='+document.frmRegistro.IdPrestamo.value+'&IdCaja='+document.frmRegistro.IdCaja.value,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
		}
			
	</script>
	
</Portal:Pagina>