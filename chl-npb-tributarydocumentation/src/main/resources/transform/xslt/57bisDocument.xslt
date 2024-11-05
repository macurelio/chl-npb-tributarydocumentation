<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" 
	xmlns:xalan="http://xml.apache.org/xslt">

  <xsl:output indent="yes"/>

  <xsl:param name="headers"/>
  <xsl:param name="flows" select="$headers/@flows"/>
  <xsl:param name="registeredname" select="$headers/@registeredname"/>
  <xsl:param name="customerIdformat" select="$headers/@customerIdformat"/>
  <xsl:param name="businessActivity" select="$headers/@businessActivity"/>
  <xsl:param name="customerName" select="$headers/@customerName"/>
  <xsl:param name="customerId" select="$flows/@formatedCustomerId"/>
   

  <xsl:template match="/">
        <Documents PublicationName="CERT_FFMM_57BIS" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="CERT_FFMM_57BIS.xsd">
            <xsl:call-template name="Documents"/>
        </Documents>
    </xsl:template>

    <xsl:template name="Documents">
        <CERT_FFMM_57BIS>
	        <DocumentIdentifier>
	        	<Empresa>0022</Empresa>
	        	<Idioma>es-CL</Idioma>
	        	<CanalMarco>003</CanalMarco>
	        </DocumentIdentifier>
	        <Num_certificado>1</Num_certificado>
	        <Fecha_emision>
                <xsl:value-of select="$issuedate"/>
            </Fecha_emision>
	        <Razon_social>
                <xsl:value-of select="$registeredname"/>
            </Razon_social>
	        <Numero_rut>
                <xsl:value-of select="$bankIdformat"/>
            </Numero_rut>
	        <Direccion_banco>
                <xsl:value-of select="$bankAdress"/>
            </Direccion_banco>
	        <Giro_actividad>
                <xsl:value-of select="$businessActivity"/>
            </Giro_actividad>
	        <Nombre_cliente>
                <xsl:value-of select="$customerName"/>
            </Nombre_cliente>
	        <Rut_cliente>
                <xsl:value-of select="$customerId"/>
            </Rut_cliente>
	        <Direccion_cliente>
                <xsl:value-of select="$customerAdress"/>
            </Direccion_cliente>
	        <Annio>
                <xsl:value-of select="$year"/>
            </Annio>
	        <Saldo_ahorro_con_ant_neg>
                <xsl:value-of select="$prevSavingsNegativeBalance"/>
            </Saldo_ahorro_con_ant_neg>
	        <Saldo_ahorro_a_contar_neg>
                <xsl:value-of select="$countSavingsNegativeBalance"/>
            </Saldo_ahorro_a_contar_neg>
	        <Inv_saldo_ahorro_negativo>
                <xsl:value-of select="$invSanvingsNegativeBalance"/>
            </Inv_saldo_ahorro_negativo>
	        <Inv_monto_rent_positiva>
                <xsl:value-of select="$invPositiveRentAmount"/>
            </Inv_monto_rent_positiva>
	        <Inv_monto_rent_negativa>
                <xsl:value-of select="$invNegativeRentAmount"/>
            </Inv_monto_rent_negativa>
	        <Nombre_firma>
                <xsl:value-of select="$signName"/>
            </Nombre_firma>
	        <Rut_firma>
                <xsl:value-of select="$signId"/>
            </Rut_firma>
            </CERT_FFMM_57BIS>
    </xsl:template>
</xsl:stylesheet>