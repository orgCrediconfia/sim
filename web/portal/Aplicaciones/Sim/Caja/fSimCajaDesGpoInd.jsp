<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCajaDesembolsoGrupal">
	<Portal:PaginaNombre titulo="Entrega de préstamo o Desembolso" subtitulo="Grupal"/>

	<Portal:Forma tipo='catalogo' funcion='SimCatalogoPapel'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Caja' control='etiqueta-controloculto' controlnombre='Caja' controlvalor='${registroCaja.campos["NOM_SUCURSAL"]} - ${registroCaja.campos["CAJA"]}'/>
		<Portal:FormaElemento etiqueta='Cargo inicial' control='etiqueta-controloculto' controlnombre='CargoInicial' controlvalor='$ ${registro.campos["CARGO_INICIAL"]}'/>
		<input type="hidden" name="IdPrestamoGrupo" value='<c:out value='${param.IdPrestamoGrupo}'/>' />
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>	


	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre integrante'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Monto autorizado'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaIndividuales}">		
			<Portal:TablaListaRenglon>			
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.Total}">		
			<Portal:TablaListaRenglon>			
				<Portal:Columna tipovalor='texto' ancho='250' valor='Total'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["TOTAL"]}'/>
			</Portal:TablaListaRenglon>
	    </c:forEach>
		<Portal:FormaBotones>
			<input type='button' name='Desembolso' value='Desembolso' onClick='fDesembolso()'/>
		</Portal:FormaBotones>
	</Portal:TablaLista>
		
	<script>
	
		function fDesembolso(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimCajaDesembolsoGrupal&OperacionCatalogo=AL&IdPrestamoGrupo="+document.frmRegistro.IdPrestamoGrupo.value+"&IdCaja="+document.frmRegistro.IdCaja.value;
			document.frmRegistro.submit();
		}
			
	</script>
</Portal:Pagina>