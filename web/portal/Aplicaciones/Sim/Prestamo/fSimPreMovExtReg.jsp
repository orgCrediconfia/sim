<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoMovimientoExtraordinario" precarga="SimPrestamoMovimientoExtraordinario">

	<Portal:PaginaNombre titulo="Movimiento Extraordinario" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<%
	String sAccesorios = ((request.getParameter("Accesorio")!=null) ? request.getParameter("Accesorio"):"0");
	int iNumAccesorios = (int)Float.valueOf(sAccesorios).floatValue();
	String sConceptos = ((request.getParameter("Conceptos")!=null) ? request.getParameter("Conceptos"):"0");
	int iNumConceptos = (int)Float.valueOf(sConceptos).floatValue();
	%>
	
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoMovimientoExtraordinario' >
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${requestScope.registro.campos["CVE_PRESTAMO"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='Nombre' controlvalor='${requestScope.registro.campos["NOMBRE"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Producto' control='Texto' controlnombre='Producto' controlvalor='${requestScope.registro.campos["NOM_PRODUCTO"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${requestScope.registro.campos["NUM_CICLO"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Movimiento extraordinario' control='selector' controlnombre='CveOperacion' controlvalor='${param.MovimientoExtraordinario}' editarinicializado='true' obligatorio='false' campoclave="CVE_OPERACION" campodescripcion="DESC_LARGA" datosselector='${requestScope.ListaOperaciones}' evento="onchange=fPantalla();" />
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		<input type="hidden" name="Consulta" value='<c:out value='${param.Consulta}'/>' />
		<input type="hidden" name="AplicaA" value='<c:out value='${requestScope.registro.campos["APLICA_A"]}'/>' />
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<c:if test='${(param.Consulta == "Pantalla")}'>	
		<Portal:TablaLista tipo="consulta" nombre="Saldo Actual">
			<Portal:TablaListaTitulos> 
			    <Portal:Columna tipovalor='texto' ancho='50' valor='Clave del cliente'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Monto préstado'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Capital'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Inter&eacute;s'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='I.V.A.'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Recargos'/>
			    <c:forEach var="registro" items="${requestScope.ListaTituloAccesorio}">
					<th  width='100' align='left'  nowrap  ><c:out value='${registro.campos["NOM_ACCESORIO"]}'/>
					</th>
			    </c:forEach>
			    <Portal:Columna tipovalor='texto' ancho='100%' valor='Total'/>  
			</Portal:TablaListaTitulos>
			<c:forEach var="registro" items="${requestScope.ListaSaldos}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["ID_PERSONA"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["CAPITAL_SALDO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["INTERES_SALDO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IVA_SALDO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["RECARGOS_SALDO"]}'/>
					<% for(int i = 0; i < iNumAccesorios; i++){
						String sRegistro = "$ ${registro.campos[\"ACCESORIO_"+ i +"_SALDO\"]}";%>
						<Portal:Columna tipovalor='moneda' ancho='100' valor='<%=sRegistro%>'/>
					<%}%>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["TOTAL"]}'/>  
				</Portal:TablaListaRenglon>
			</c:forEach>
			<c:forEach var="registro" items="${requestScope.TotalSaldos}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["CAPITAL_SALDO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["INTERES_SALDO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IVA_SALDO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["RECARGOS_SALDO_TOTAL"]}'/>
					<% for(int j = 0; j < iNumAccesorios; j++){
						String sTotales = "$ ${registro.campos[\"ACCESORIO_"+ j +"_SALDO_TOTAL\"]}";%>
						<Portal:Columna tipovalor='moneda' ancho='100' valor='<%=sTotales%>'/>
					<%}%>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDOS_TOTALES"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
		</Portal:TablaLista>
	
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoMovimientoExtraordinario" nombre="Montos Aplicados" funcion="SimPrestamoMovimientoExtraordinario" operacion="AL" parametros="&CveOperacion=${param.CveOperacion}&Conceptos=${param.Conceptos}&IdPrestamo=${param.IdPrestamo}&CvePrestamo=${param.CvePrestamo}&AplicaA=${param.AplicaA}&Consulta=PantallaFinal">
			<Portal:TablaListaTitulos> 
			    <Portal:Columna tipovalor='texto' ancho='50' valor='Clave del cliente'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Monto préstado'/>
			    <c:forEach var="registro" items="${requestScope.ListaTituloMontoAplicados}">
					<th  width='100' align='left'  nowrap  ><c:out value='${registro.campos["DESC_LARGA"]}'/>
					</th>
			    </c:forEach>
			    <!--Portal:Columna tipovalor='texto' ancho='100%' valor='Total'/-->  
			</Portal:TablaListaTitulos>
			
			<c:forEach var="registro" items="${requestScope.ListaMontoAplicados}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["ID_PERSONA"]}'/>
					<input type='hidden' name='IdPrestamo' 	value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
					
					<% for(int i = 0; i < iNumConceptos; i++){	
						String sRegistro = "CVE_CONCEPTO_"+ i +"";
						String sImporte = "IMPORTE_"+ i +"";
						String sCveConceptos = "${registro.campos[\"CVE_CONCEPTO_"+ i +"\"]}";%>
						<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='<%=sImporte%>' controlvalor='' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
						<Portal:FormaElemento etiqueta='Ninguna' control='variableoculta-horizontal' controlnombre='<%=sRegistro%>' controlvalor='<%=sCveConceptos%>'/>
					<%}%>
					<!--Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='TotalHorizontal' controlvalor='' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/-->
				</Portal:TablaListaRenglon>
			</c:forEach>
			<!--
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					
					<% for(int j = 0; j < iNumConceptos; j++){
						String sSumConceptos = "CVE_CONCEPTO_"+ j +"";%>
						<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='<%=sSumConceptos%>' controlvalor='' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
					<%}%>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDOS_TOTALES"]}'/>
				</Portal:TablaListaRenglon>
			-->
			
			<Portal:FormaBotones>
				<Portal:Boton tipo='submit' etiqueta='Modificar' />
			</Portal:FormaBotones>
		</Portal:TablaForma>
	</c:if>
	
	<c:if test='${(param.Consulta == "PantallaFinal")}'>	
		
	
		<Portal:TablaForma maestrodetallefuncion="SimPrestamoMovimientoExtraordinario" nombre="Montos Aplicados" funcion="SimPrestamoMovimientoExtraordinario" operacion="AL" parametros="&CveOperacion=${param.CveOperacion}&Conceptos=${param.Conceptos}&IdPrestamo=${param.IdPrestamo}&CvePrestamo=${param.CvePrestamo}&AplicaA=${param.AplicaA}">
			<Portal:TablaListaTitulos> 
			    <Portal:Columna tipovalor='texto' ancho='50' valor='Clave del cliente'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Monto préstado'/>
			    <c:forEach var="registro" items="${requestScope.ListaTituloMontoAplicados}">
					<th  width='100' align='left'  nowrap  ><c:out value='${registro.campos["DESC_LARGA"]}'/>
					</th>
			    </c:forEach>
			    <!--Portal:Columna tipovalor='texto' ancho='100%' valor='Total'/-->  
			</Portal:TablaListaTitulos>
			
			<c:forEach var="registro" items="${requestScope.ListaMontoAplicados}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["ID_PERSONA"]}'/>
					<input type='hidden' name='IdPrestamo' 	value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
					
					<% for(int i = 0; i < iNumConceptos; i++){	
						String sRegistro = "CVE_CONCEPTO_"+ i +"";
						String sImporte = "IMPORTE_"+ i +"";
						String sCveConceptos = "${registro.campos[\"CVE_CONCEPTO_"+ i +"\"]}";%>
						<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='<%=sImporte%>' controlvalor='' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
						<Portal:FormaElemento etiqueta='Ninguna' control='variableoculta-horizontal' controlnombre='<%=sRegistro%>' controlvalor='<%=sCveConceptos%>'/>
					<%}%>
					<!--Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='TotalHorizontal' controlvalor='' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/-->
				</Portal:TablaListaRenglon>
			</c:forEach>
			<!--
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					
					<% for(int j = 0; j < iNumConceptos; j++){
						String sSumConceptos = "CVE_CONCEPTO_"+ j +"";%>
						<Portal:FormaElemento etiqueta='' control='Texto-horizontal'    controlnombre='<%=sSumConceptos%>' controlvalor='' controllongitud='17' controllongitudmax='17' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
					<%}%>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDOS_TOTALES"]}'/>
				</Portal:TablaListaRenglon>
			-->
			<Portal:FormaBotones>
			</Portal:FormaBotones>
		</Portal:TablaForma>
			
		<Portal:TablaLista tipo="consulta" nombre="Saldos Actuales después de agregar el Movimiento Extraordinario">
			<Portal:TablaListaTitulos> 
			    <Portal:Columna tipovalor='texto' ancho='50' valor='Clave del cliente'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Monto préstado'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Capital'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Inter&eacute;s'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='I.V.A.'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Recargos'/>
			    <c:forEach var="registro" items="${requestScope.ListaTituloAccesorio}">
					<th  width='100' align='left'  nowrap  ><c:out value='${registro.campos["NOM_ACCESORIO"]}'/>
					</th>
			    </c:forEach>
			    <Portal:Columna tipovalor='texto' ancho='100%' valor='Total'/>  
			</Portal:TablaListaTitulos>
			<c:forEach var="registro" items="${requestScope.ListaSaldos}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["ID_PERSONA"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["CAPITAL_SALDO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["INTERES_SALDO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IVA_SALDO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["RECARGOS_SALDO"]}'/>
					<% for(int i = 0; i < iNumAccesorios; i++){
						String sRegistro = "$ ${registro.campos[\"ACCESORIO_"+ i +"_SALDO\"]}";%>
						<Portal:Columna tipovalor='moneda' ancho='100' valor='<%=sRegistro%>'/>
					<%}%>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["TOTAL"]}'/>  
				</Portal:TablaListaRenglon>
			</c:forEach>
			<c:forEach var="registro" items="${requestScope.TotalSaldos}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor=''/>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["CAPITAL_SALDO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["INTERES_SALDO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IVA_SALDO_TOTAL"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["RECARGOS_SALDO_TOTAL"]}'/>
					<% for(int j = 0; j < iNumAccesorios; j++){
						String sTotales = "$ ${registro.campos[\"ACCESORIO_"+ j +"_SALDO_TOTAL\"]}";%>
						<Portal:Columna tipovalor='moneda' ancho='100' valor='<%=sTotales%>'/>
					<%}%>
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDOS_TOTALES"]}'/>
				</Portal:TablaListaRenglon>
			</c:forEach>
		</Portal:TablaLista>
	</c:if>
	
	<script>
		function fPantalla(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoMovimientoExtraordinario&OperacionCatalogo=CR&Consulta=Pantalla&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&CvePrestamo="+document.frmRegistro.CvePrestamo.value+"&MovimientoExtraordinario="+document.frmRegistro.CveOperacion.value+"&AplicaA="+document.frmRegistro.AplicaA.value;
			document.frmRegistro.submit();
		}
	</script>
</Portal:Pagina>


	