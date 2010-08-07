<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimGeneralesFirmaElectronica" >
	<Portal:PaginaNombre titulo="Firma electrónica" subtitulo="Validación electrónica" subtituloalta="Validación electrónica"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimGeneralesFirmaElectronica' parametros='Modulo=${param.Modulo}'>
		<Portal:FormaSeparador nombre='<%=request.getParameter("TituloVentana")%>'/>
		<Portal:FormaElemento etiqueta='Responsable (Clave)' control='etiqueta'>
			<input type="password" name="PasswordFirma" size="50" maxlength="50"/>
		</Portal:FormaElemento>
		<c:if test='${(param.PasswordIncorrecto != null)}'>
			<Portal:FormaElemento etiqueta='Verifique' 	control='Texto'    controlnombre='' controlvalor='La contraseña que introdujo es incorrecta, intente de nuevo' controllongitud='30' controllongitudmax='100' editarinicializado='false' obligatorio='false' />
		</c:if>	
		<c:if test='${(param.Modulo == "Caja")}'>
			<!--Portal:FormaElemento etiqueta='Cajero' 	control='Texto'    controlnombre='NomIntegrante1' controlvalor='${requestScope.registro.campos["NOM_CAJERO"]}'controllongitud='30' controllongitudmax='100' editarinicializado='false' obligatorio='true' /-->
			<Portal:FormaElemento etiqueta='Coordinador' 	control='Texto'    controlnombre='NomIntegrante1' controlvalor='${requestScope.registro.campos["NOM_COORDINADOR"]}'controllongitud='30' controllongitudmax='100' editarinicializado='false' obligatorio='true' />
		</c:if>	
		<c:if test='${(param.Modulo == "Cierre")}'>
			<Portal:FormaElemento etiqueta='Nombre' 	control='Texto'    controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}'controllongitud='30' controllongitudmax='100' editarinicializado='false' obligatorio='true' />
		</c:if>	
		<Portal:FormaElemento etiqueta='Fecha hora'	control='Texto'    controlnombre='FHDespliegue'   controlvalor='${requestScope.registro.campos["FH_DESPLIEGUE"]}' controllongitud='12' controllongitudmax='7'   editarinicializado='false' obligatorio='false'>
			<input type="hidden" name="IdIntegrante" 	value='<c:out value='${requestScope.registro.campos["ID_INTEGRANTE"]}'/>'  />
			<input type="hidden" name="IdUsuario" 		value='<c:out value='${param.IdUsuario}'/>'  />
			<input type="hidden" name="IdCajero" 		value='<c:out value='${param.IdCajero}'/>'  />
			<input type="hidden" name="IdCoordinador" 	value='<c:out value='${param.IdCoordinador}'/>'  />
			<input type="hidden" name="IdGerente" 		value='<c:out value='${param.IdGerente}'/>'  />
			<input type="hidden" name="IdCaja" 			value='<c:out value='${param.IdCaja}'/>'  />
			<input type="hidden" name="IdBitacora" 		value='<c:out value='${param.IdBitacora}'/>'  />
			<input type="hidden" name="CveIntegrante"	value='<c:out value='${param.CveIntegrante}'/>'  />
			<input type="hidden" name="RegresaControl"	value='<c:out value='${param.RegresaControl}'/>'  />
			<input type="hidden" name="TxNota"			value='<c:out value='${param.TxNota}'/>'  />
			<input type="hidden" name="Url"				value='<c:out value='${param.Url}'/>'  />
		</Portal:FormaElemento>
		<script> 
			fAgregaCampo('PasswordFirma', 'Responsable (Clave)');
		</script>	    

		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<script>
	
		if(document.frmRegistro.RegresaControl.value == 'SI'){
				var padre = window.opener;
				//padre.document.frmRegistro.IdBitacora.value   = document.frmRegistro.IdBitacora.value;
				padre.document.frmRegistro.submit();
				
				//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
				window.close();
		}
		
	</script>
	
</Portal:Pagina>

