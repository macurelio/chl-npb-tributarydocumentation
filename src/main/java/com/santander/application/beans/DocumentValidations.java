package com.santander.application.beans;

import org.apache.camel.Exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;        
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.santander.application.model.exception.ContextException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
public class DocumentValidations {

    static final String FLOWS_HEADERS = "flows";
    static final String FLOWS_HEADERS_CONTEXT = "context";
    static final String HTTP_STATUS_CODE_RESPONSE = "HttpStatusCodeResponse";

    public void  bis57DocumentDataFormat(Exchange __exchange){
        
        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");
        //System.out.println("ACA EL LOG DEL JAVA-------------"+ __exchange.toString());
        //Map<String, Object> session = (Map<String, Object>) flows.get("session");
        //Map<String, Object> login = (Map<String, Object>) session.get("login");
        //Map<String, Object> requestBody = (Map<String, Object>) flows.get("request-body");
        Map<String, Object> bis57Data = (Map<String, Object>) flows.get("57bis-data");
        Map<String, Object> response = (Map<String, Object>) bis57Data.get("response");
        Map<String, Object> request = (Map<String, Object>) bis57Data.get("request");
        //Map<String, Object> headers = (Map<String, Object>) response.get("headers");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> customerData = (Map<String, Object>) body.get("customerData");
        List<Map<String, Object>> bis57DataResponse = (List<Map<String, Object>>) body.get("57bisData");
        String issueDate = "Santiago, 01 de Noviembre del 2024";

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Documents");
            doc.appendChild(rootElement);
        
            rootElement.setAttributeNode(createAttr(doc, "PublicationName", "CERT_FFMM_57BIS"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xalan", "http://xml.apache.org/xslt"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
            rootElement.setAttributeNode(createAttr(doc, "xsi:noNamespaceSchemaLocation", "CERT_FFMM_57BIS.xsd"));
        
            Element cert = doc.createElement("CERT_FFMM_57BIS");
            rootElement.appendChild(cert);
        
            Element documentIdentifier = doc.createElement("DocumentIdentifier");
            cert.appendChild(documentIdentifier);
        
            documentIdentifier.appendChild(createElement(doc, "Empresa", "0022"));
            documentIdentifier.appendChild(createElement(doc, "Idioma", "es-CL"));
            documentIdentifier.appendChild(createElement(doc, "CanalMarco", "003"));
        
            cert.appendChild(createElement(doc, "Num_certificado", "12345467"));
            cert.appendChild(createElement(doc, "Fecha_emision", issueDate));
            cert.appendChild(createElement(doc, "Razon_social", customerData.get("fundManagerDescription").toString()));
            cert.appendChild(createElement(doc, "Numero_rut", customerData.get("fundManagerId").toString()));
            cert.appendChild(createElement(doc, "Direccion_banco", customerData.get("adress").toString()));
            cert.appendChild(createElement(doc, "Giro_actividad",  customerData.get("category").toString()));
            cert.appendChild(createElement(doc, "Nombre_cliente",  customerData.get("customerName").toString()));
            //cert.appendChild(createElement(doc, "Rut_cliente", login.get("username")));
            cert.appendChild(createElement(doc, "Rut_cliente", "17.683.826-4"));
            cert.appendChild(createElement(doc, "Direccion_cliente", customerData.get("customerAdress").toString()+" - "+customerData.get("customerCommune")));
            //cert.appendChild(createElement(doc, "Annio", requestBody.get("year").toString()));
            cert.appendChild(createElement(doc, "Annio", "2023"));
            cert.appendChild(createElement(doc, "Saldo_ahorro_con_ant_neg",  (String)bis57DataResponse.get(0).get("previousNegativeBalance")));
            cert.appendChild(createElement(doc, "Saldo_ahorro_a_contar_neg", (String)bis57DataResponse.get(0).get("negativeIncomeInvestment")));
            cert.appendChild(createElement(doc, "Inv_saldo_ahorro_negativo", (String)bis57DataResponse.get(0).get("2015negativeBalance")));
            cert.appendChild(createElement(doc, "Inv_monto_rent_positiva",   (String)bis57DataResponse.get(0).get("positiveBalance")));
            cert.appendChild(createElement(doc, "Inv_monto_rent_negativa",   (String)bis57DataResponse.get(0).get("negativeBalance")));
            cert.appendChild(createElement(doc, "Nombre_firma", customerData.get("manager").toString()));
            cert.appendChild(createElement(doc, "Rut_firma", customerData.get("managerId").toString()));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            Result destino = new StreamResult(writer);
            transformer.transform(source, destino);
            System.out.println(writer.toString());
            String xmlString = writer.toString();
            xmltoBase64(__exchange, xmlString, null);
            System.out.println("ACA ESTA EL BASE64 OUYES  ----    " + flows.get("base64XmlString"));
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }/*
      <Num_certificado>1</Num_certificado>

	  OK - issueDate                 <Fecha_emision>Santiago, 22 de Noviembre del 2021</Fecha_emision>
	  OK - fundManagerDescription    <Razon_social>SANTANDER ASSET MANAGEMENT S.A. ADMINISTRADORA GENERAL DE FONDOS</Razon_social> 
	  OK - fundManagerId             <Numero_rut>96.667.040-1</Numero_rut>
	  OK - address                   <Direccion_banco>BANDERA 140 P. 3 - SANTIAGO</Direccion_banco>
	  OK - category                  <Giro_actividad>ADMINISTRADORA GENERAL DE FONDOS</Giro_actividad>
	  OK - customerName              <Nombre_cliente>TEODORO RINSCHE NUÑEZ</Nombre_cliente>
	  OK - customerRut               <Rut_cliente>1.809.976-4</Rut_cliente>
	  OK - customerAdress            <Direccion_cliente>LOS RETAMOS 12 B.LAS FLORES - PIEDRA ROJA - COLINA</Direccion_cliente>
	  OK - parametro entrada yearid  <Annio>2020</Annio>
    */

    public void  cert1922DocDataFormat(Exchange __exchange){

        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");
        //System.out.println("ACA EL FLOWS"+flows);
        //System.out.println("ACA EL LOG DEL JAVA-------------"+ __exchange.toString());
        //Map<String, Object> session = (Map<String, Object>) flows.get("session");
        //Map<String, Object> login = (Map<String, Object>) session.get("login");
        //Map<String, Object> requestBody = (Map<String, Object>) flows.get("request-body");
        Map<String, Object> cert1922Data = (Map<String, Object>) flows.get("1922-data");
        Map<String, Object> response = (Map<String, Object>) cert1922Data.get("response");
        Map<String, Object> request = (Map<String, Object>) cert1922Data.get("request");
        //Map<String, Object> headers = (Map<String, Object>) response.get("headers");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> customerData = (Map<String, Object>) body.get("customerData");
        List<Map<String, Object>> cert1922DataResponse = (List<Map<String, Object>>) body.get("1922Data");
        String issueDate = "Santiago, 01 de Noviembre del 2024";

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // Elemento raíz
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Documents");
            doc.appendChild(rootElement);
      
            rootElement.setAttributeNode(createAttr(doc, "PublicationName", "CERT_FFMM_1922"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xalan", "http://xml.apache.org/xslt"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
            rootElement.setAttributeNode(createAttr(doc, "xsi:noNamespaceSchemaLocation", "CERT_FFMM_1922.xsd"));
      
            Element cert = doc.createElement("CERT_FFMM_1922");
            rootElement.appendChild(cert);
      
            Element documentIdentifier = doc.createElement("DocumentIdentifier");
            cert.appendChild(documentIdentifier);
      
            documentIdentifier.appendChild(createElement(doc, "Empresa", "0022"));
            documentIdentifier.appendChild(createElement(doc, "Idioma", "es-CL"));
            documentIdentifier.appendChild(createElement(doc, "CanalMarco", "003"));
      
            cert.appendChild(createElement(doc, "Razon_Social", customerData.get("fundAdminName").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Numero_rut", customerData.get("fundAdminId").toString().toString().trim()+"-"+customerData.get("fundAdminDv").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Direccion", customerData.get("adress").toString().toString().trim()));
            cert.appendChild(createElement(doc, "giro_actividad", customerData.get("fundAdminActivity").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Numero_Certificado", cert1922DataResponse.get(0).get("certificateId").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Ciudad", customerData.get("commune").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Fecha_dia", formatDate("")));
            cert.appendChild(createElement(doc, "Nombre_Cliente", customerData.get("name").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Rut_Cliente", customerData.get("id").toString().toString().trim()+customerData.get("dv").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Nombre_fondo_mutuo", cert1922DataResponse.get(0).get("fundName").toString().toString().trim()));
            cert.appendChild(createElement(doc, "Anio", customerData.get("businessYear").toString().toString().trim()));
      
            Element tabla_1 = doc.createElement("Tabla_1");
            cert.appendChild(tabla_1);
            for (int indice = 0; indice < cert1922DataResponse.size(); indice++) {
              Element datos_1 = doc.createElement("Datos_1");
              tabla_1.appendChild(datos_1);
      
              datos_1.appendChild(createElement(doc, "tipo_fondo", cert1922DataResponse.get(indice).get("fundType").toString().toString().trim()));
              datos_1.appendChild(createElement(doc, "rut_fondo", cert1922DataResponse.get(indice).get("fundId").toString().toString().trim()));
              datos_1.appendChild(createElement(doc, "run_fondo", cert1922DataResponse.get(indice).get("fundRun").toString().toString().trim()));
              datos_1.appendChild(createElement(doc, "fecha_operacion", cert1922DataResponse.get(indice).get("operationDate").toString().toString().trim()));
              datos_1.appendChild(createElement(doc, "tipo_operacion", cert1922DataResponse.get(indice).get("operationType").toString().toString().trim()));
              datos_1.appendChild(createElement(doc, "monto_historico", cert1922DataResponse.get(indice).get("historicAmount").toString()));
              datos_1.appendChild(createElement(doc, "factor_actualizacion", cert1922DataResponse.get(indice).get("actualizationFactor").toString()));      
              datos_1.appendChild(createElement(doc, "monto_actualizado", cert1922DataResponse.get(indice).get("updatedAmount").toString()));
			  
              datos_1.appendChild(createElement(doc, "dif_obt_res_mayor_valor", cert1922DataResponse.get(indice).get("tax1").toString()));
              datos_1.appendChild(createElement(doc, "dif_obt_res_menor_valor", cert1922DataResponse.get(indice).get("tax2").toString()));
              datos_1.appendChild(createElement(doc, "dif_obt_res_enaj_mayor_valor", cert1922DataResponse.get(indice).get("tax3").toString()));
              datos_1.appendChild(createElement(doc, "dif_obt_res_enaj_menor_valor", cert1922DataResponse.get(indice).get("tax4").toString()));      
              datos_1.appendChild(createElement(doc, "div_con_cred_gen", cert1922DataResponse.get(indice).get("tax5").toString()));
              datos_1.appendChild(createElement(doc, "div_con_cred_acum", cert1922DataResponse.get(indice).get("tax6").toString()));
              datos_1.appendChild(createElement(doc, "div_con_cred_por_pago_vol", cert1922DataResponse.get( 0).get("tax7").toString()));
              datos_1.appendChild(createElement(doc, "div_sin_der_cred", cert1922DataResponse.get(indice).get("tax8").toString()));      
              datos_1.appendChild(createElement(doc, "rtc_registro_rap", cert1922DataResponse.get(indice).get("tax9").toString()));
              datos_1.appendChild(createElement(doc, "rtc_otras_rentas", cert1922DataResponse.get(indice).get("tax10").toString()));
              datos_1.appendChild(createElement(doc, "rtc_exceso_dist", cert1922DataResponse.get(indice).get("tax11").toString()));
              datos_1.appendChild(createElement(doc, "rtc_utilidades_afectadas", cert1922DataResponse.get( 0).get("tax12").toString()));
              datos_1.appendChild(createElement(doc, "rtc_rentas_gen", cert1922DataResponse.get(indice).get("tax13").toString()));      
              datos_1.appendChild(createElement(doc, "re_afectas_imp_adic", cert1922DataResponse.get(indice).get("tax14").toString()));
              datos_1.appendChild(createElement(doc, "re_igc_imp_adic", cert1922DataResponse.get(indice).get("tax15").toString()));      
              datos_1.appendChild(createElement(doc, "div_ncr_monto_no_const", cert1922DataResponse.get(indice).get("tax16").toString()));
              datos_1.appendChild(createElement(doc, "div_ncr_fuente_ext", cert1922DataResponse.get(indice).get("tax17").toString()));
              datos_1.appendChild(createElement(doc, "div_imp_unico_10", cert1922DataResponse.get(indice).get("tax18").toString()));
              datos_1.appendChild(createElement(doc, "div_imp_unico_4", cert1922DataResponse.get(indice).get("tax19").toString()));      
              datos_1.appendChild(createElement(doc, "dis_efect_dis_calor_cuota", cert1922DataResponse.get( indice).get("tax20").toString()));
              datos_1.appendChild(createElement(doc, "dev_capital", cert1922DataResponse.get( indice).get("tax21").toString()));      
              datos_1.appendChild(createElement(doc, "gen_ara_nsrg_sin_der_dev_1", cert1922DataResponse.get( indice).get("tax22").toString()));
              datos_1.appendChild(createElement(doc, "gen_ara_nsrg_con_der_dev_1", cert1922DataResponse.get( indice).get("tax23").toString()));
              datos_1.appendChild(createElement(doc, "gen_ara_nsrg_sin_der_dev_2", cert1922DataResponse.get( indice).get("tax24").toString()));
              datos_1.appendChild(createElement(doc, "gen_ara_nsrg_con_der_dev_2", cert1922DataResponse.get( indice).get("tax25").toString()));
              datos_1.appendChild(createElement(doc, "gen_ara_sr_sin_der_dev", cert1922DataResponse.get( indice).get("tax26").toString()));
              datos_1.appendChild(createElement(doc, "gen_ara_sr_con_der_dev", cert1922DataResponse.get( indice).get("tax27").toString()));
              datos_1.appendChild(createElement(doc, "gen_are_sr_sin_der_dev", cert1922DataResponse.get( indice).get("tax28").toString()));
              datos_1.appendChild(createElement(doc, "gen_are_sr_con_der_dev", cert1922DataResponse.get( indice).get("tax29").toString()));
              datos_1.appendChild(createElement(doc, "gen_credito_por_ipe", cert1922DataResponse.get( indice).get("tax30").toString()));      
              datos_1.appendChild(createElement(doc, "acum_ara_sr_sin_der_dev", cert1922DataResponse.get( indice).get("tax31").toString()));
              datos_1.appendChild(createElement(doc, "acum_ara_sr_con_der_dev", cert1922DataResponse.get( indice).get("tax32").toString()));
              datos_1.appendChild(createElement(doc, "acum_are_sr_sin_der_dev", cert1922DataResponse.get( indice).get("tax33").toString()));
              datos_1.appendChild(createElement(doc, "acum_are_sr_con_der_dev", cert1922DataResponse.get( indice).get("tax34").toString()));
              datos_1.appendChild(createElement(doc, "acum_credito_por_ipe", cert1922DataResponse.get( indice).get("tax35").toString()));      
              datos_1.appendChild(createElement(doc, "cred_imp_tasa_adic", cert1922DataResponse.get( indice).get("tax36").toString()));
             }
      
            cert.appendChild(createElement(doc, "total_monto_historico", cert1922DataResponse.get( 0).get("historicAmountTotal").toString()));
            cert.appendChild(createElement(doc, "total_monto_actualizado", cert1922DataResponse.get( 0).get("updatedAmountTotal").toString()));
            cert.appendChild(createElement(doc, "total_dif_obt_res_mayor_valor", cert1922DataResponse.get( 0).get("totalTax6").toString()));
            cert.appendChild(createElement(doc, "total_dif_obt_res_menor_valor", cert1922DataResponse.get( 0).get("totalTax7").toString()));
            cert.appendChild(createElement(doc, "total_dif_obtres_enaj_mayor_valor", cert1922DataResponse.get( 0).get("totalTax8").toString()));
            cert.appendChild(createElement(doc, "total_dif_obtres_enaj_menor_valor", cert1922DataResponse.get( 0).get("totalTax9").toString()));
            cert.appendChild(createElement(doc, "total_div_con_cred_gen", cert1922DataResponse.get( 0).get("totalTax10").toString()));
            cert.appendChild(createElement(doc, "total_div_con_cred_acum", cert1922DataResponse.get( 0).get("totalTax11").toString()));
            cert.appendChild(createElement(doc, "total_div_con_cred_por_pago_vol", cert1922DataResponse.get( 0).get("totalTax12").toString()));
            cert.appendChild(createElement(doc, "total_div_sin_der_cred", cert1922DataResponse.get( 0).get("totalTax13").toString()));
            cert.appendChild(createElement(doc, "total_rtc_registro_rap", cert1922DataResponse.get( 0).get("totalTax14").toString()));
            cert.appendChild(createElement(doc, "total_rtc_otras_rentas", cert1922DataResponse.get( 0).get("totalTax15").toString()));
            cert.appendChild(createElement(doc, "total_rtc_exceso_dist", cert1922DataResponse.get( 0).get("totalTax16").toString()));
            cert.appendChild(createElement(doc, "total_rtc_utilidades_afectadas", cert1922DataResponse.get( 0).get("totalTax17").toString()));
            cert.appendChild(createElement(doc, "total_rtc_rentas_gen", cert1922DataResponse.get( 0).get("totalTax18").toString()));
            cert.appendChild(createElement(doc, "total_re_afectas_imp_adic", cert1922DataResponse.get( 0).get("totalTax19").toString()));
            cert.appendChild(createElement(doc, "total_re_igc_imp_adic", cert1922DataResponse.get( 0).get("totalTax20").toString()));
            cert.appendChild(createElement(doc, "total_div_ncr_monto_no_const", cert1922DataResponse.get( 0).get("totalTax21").toString()));
            cert.appendChild(createElement(doc, "total_div_ncr_fuente_ext", cert1922DataResponse.get( 0).get("totalTax22").toString()));
            cert.appendChild(createElement(doc, "total_div_imp_unico_10", cert1922DataResponse.get( 0).get("totalTax23").toString()));
            cert.appendChild(createElement(doc, "total_div_imp_unico_4", cert1922DataResponse.get( 0).get("totalTax24").toString()));
            cert.appendChild(createElement(doc, "total_dis_efect_dis_calor_cuota", cert1922DataResponse.get( 0).get("totalTax25").toString()));
            cert.appendChild(createElement(doc, "total_dev_capital", cert1922DataResponse.get( 0).get("totalTax26").toString()));
            cert.appendChild(createElement(doc, "total_gen_ara_nsrg_sin_der_dev_1", cert1922DataResponse.get( 0).get("totalTax27").toString()));
            cert.appendChild(createElement(doc, "total_gen_ara_nsrg_con_der_dev_1", cert1922DataResponse.get( 0).get("totalTax28").toString()));
            cert.appendChild(createElement(doc, "total_gen_ara_nsrg_sin_der_dev_2", cert1922DataResponse.get( 0).get("totalTax29").toString()));
            cert.appendChild(createElement(doc, "total_gen_ara_nsrg_con_der_dev_2", cert1922DataResponse.get( 0).get("totalTax30").toString()));
            cert.appendChild(createElement(doc, "total_gen_ara_sr_sin_der_dev", cert1922DataResponse.get( 0).get("totalTax31").toString()));
            cert.appendChild(createElement(doc, "total_gen_ara_sr_con_der_dev", cert1922DataResponse.get( 0).get("totalTax32").toString()));
            cert.appendChild(createElement(doc, "total_gen_are_sr_sin_der_dev", cert1922DataResponse.get( 0).get("totalTax33").toString()));
            cert.appendChild(createElement(doc, "total_gen_are_sr_con_der_dev", cert1922DataResponse.get( 0).get("totalTax34").toString()));
            cert.appendChild(createElement(doc, "total_gen_credito_por_ipe", cert1922DataResponse.get( 0).get("totalTax35").toString()));
            cert.appendChild(createElement(doc, "total_acum_ara_sr_sin_der_dev", cert1922DataResponse.get( 0).get("totalTax36").toString()));
            cert.appendChild(createElement(doc, "total_acum_ara_sr_con_der_dev", cert1922DataResponse.get( 0).get("totalTax37").toString()));
            cert.appendChild(createElement(doc, "total_acum_are_sr_sin_der_dev", cert1922DataResponse.get( 0).get("totalTax38").toString()));
            cert.appendChild(createElement(doc, "total_acum_are_sr_con_der_dev", cert1922DataResponse.get( 0).get("totalTax39").toString()));
            cert.appendChild(createElement(doc, "total_acum_credito_por_ipe", cert1922DataResponse.get( 0).get("totalTax40").toString()));
            cert.appendChild(createElement(doc, "total_cred_imp_tasa_adic", cert1922DataResponse.get( 0).get("totalTax41").toString()));
            
            String rutFirma = customerData.get("managerId").toString().toString().trim()+"-"+customerData.get("managerDvId").toString().toString().trim();
            cert.appendChild(createElement(doc, "nombre_firma", customerData.get("managerName").toString().toString().trim()));
            cert.appendChild(createElement(doc, "rut_firma", rutFirma));
            
      
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            Result destino = new StreamResult(writer);
            transformer.transform(source, destino);
            System.out.println(writer.toString());
            xmltoBase64(__exchange, writer.toString(), null);
          } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
          } catch (TransformerException tfe) {
            tfe.printStackTrace();
          }
    }

    public void  cert1894DocDataFormat(Exchange __exchange){
        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");
        //System.out.println("ACA EL FLOWS"+flows);
        //System.out.println("ACA EL LOG DEL JAVA-------------"+ __exchange.toString());
        //Map<String, Object> session = (Map<String, Object>) flows.get("session");
        //Map<String, Object> login = (Map<String, Object>) session.get("login");
        //Map<String, Object> requestBody = (Map<String, Object>) flows.get("request-body");
        Map<String, Object> cert1894Data = (Map<String, Object>) flows.get("1894-data");
        Map<String, Object> response = (Map<String, Object>) cert1894Data.get("response");
        Map<String, Object> request = (Map<String, Object>) cert1894Data.get("request");
        //Map<String, Object> headers = (Map<String, Object>) response.get("headers");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> customerData = (Map<String, Object>) body.get("customerData");
        List<Map<String, Object>> cert1894DataResponse = (List<Map<String, Object>>) body.get("1892_1894Data");

        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Documents");
            doc.appendChild(rootElement);
            rootElement.setAttributeNode(createAttr(doc, "PublicationName", "CERT_FFMM_1894"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xalan", "http://xml.apache.org/xslt"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
            rootElement.setAttributeNode(createAttr(doc, "xsi:noNamespaceSchemaLocation", "CERT_FFMM_1894.xsd"));

            Element cert = doc.createElement("CERT_FFMM_1894");
            rootElement.appendChild(cert);
            Element documentIdentifier = doc.createElement("DocumentIdentifier");
            cert.appendChild(documentIdentifier);
            documentIdentifier.appendChild(createElement(doc, "Empresa", "0022"));
            documentIdentifier.appendChild(createElement(doc, "Idioma", "es-CL"));
            documentIdentifier.appendChild(createElement(doc, "CanalMarco", "003"));
            cert.appendChild(createElement(doc, "Razon_Social", customerData.get("fundManagerDescription").toString().trim()));
            cert.appendChild(createElement(doc, "Numero_rut", customerData.get("fundManagerId").toString().trim()));
            cert.appendChild(createElement(doc, "Direccion", customerData.get("adress").toString().trim()));
            cert.appendChild(createElement(doc, "nombre_firma", customerData.get("manager").toString().trim()));
            cert.appendChild(createElement(doc, "rut_firma", customerData.get("managerId").toString().trim()));
            Element tabla_1 = doc.createElement("Tabla_1");
            cert.appendChild(tabla_1);
            int j=0;
            for(int i=0; i<(cert1894DataResponse.size()/12);i++){
                Element datos_1 = doc.createElement("Datos_1");
                tabla_1.appendChild(datos_1);
            datos_1.appendChild(createElement(doc, "Numero_Certificado", cert1894DataResponse.get(j+0).get("certificateId").toString().trim()));
            datos_1.appendChild(createElement(doc, "Fecha_dia", formatDate("")));
            datos_1.appendChild(createElement(doc, "Nombre_Cliente", customerData.get("customerName").toString().trim()));
            datos_1.appendChild(createElement(doc, "Rut_Cliente", "17.683.826-4"));
            datos_1.appendChild(createElement(doc, "Nombre_fondo_mutuo", cert1894DataResponse.get(j+0).get("mutualFund").toString().trim()));
            datos_1.appendChild(createElement(doc, "Razon_Social_2", customerData.get("fundManagerDescription").toString().trim()));
            datos_1.appendChild(createElement(doc, "Anio", customerData.get("businessYear").toString().trim()));
            datos_1.appendChild(createElement(doc, "valor_coutas_enero", formattedNumber(cert1894DataResponse.get(j+0).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_feb",   formattedNumber(cert1894DataResponse.get(j+1).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_mar",   formattedNumber(cert1894DataResponse.get(j+2).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_abril", formattedNumber(cert1894DataResponse.get(j+3).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_may",   formattedNumber(cert1894DataResponse.get(j+4).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_jun",   formattedNumber(cert1894DataResponse.get(j+5).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_jul",   formattedNumber(cert1894DataResponse.get(j+6).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_agos",  formattedNumber(cert1894DataResponse.get(j+7).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_sep",   formattedNumber(cert1894DataResponse.get(j+8).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_oct",   formattedNumber(cert1894DataResponse.get(j+9).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_nov",   formattedNumber(cert1894DataResponse.get(j+10).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_coutas_dic",   formattedNumber(cert1894DataResponse.get(j+11).get("quotaValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_enero", formattedNumber(cert1894DataResponse.get(j+0).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_feb",   formattedNumber(cert1894DataResponse.get(j+1).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_mar",   formattedNumber(cert1894DataResponse.get(j+2).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_abril", formattedNumber(cert1894DataResponse.get(j+3).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_may",   formattedNumber(cert1894DataResponse.get(j+4).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_jun",   formattedNumber(cert1894DataResponse.get(j+5).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_jul",   formattedNumber(cert1894DataResponse.get(j+6).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_agos",  formattedNumber(cert1894DataResponse.get(j+7).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_sep",   formattedNumber(cert1894DataResponse.get(j+8).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_oct",   formattedNumber(cert1894DataResponse.get(j+9).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_nov",   formattedNumber(cert1894DataResponse.get(j+10).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "valor_adquisicion_dic",   formattedNumber(cert1894DataResponse.get(j+11).get("acquisitionValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_enero", formattedNumber(cert1894DataResponse.get(j+ 0).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_feb",   formattedNumber(cert1894DataResponse.get(j+ 1).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_mar",   formattedNumber(cert1894DataResponse.get(j+ 2).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_abril", formattedNumber(cert1894DataResponse.get(j+ 3).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_may",   formattedNumber(cert1894DataResponse.get(j+ 4).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_jun",   formattedNumber(cert1894DataResponse.get(j+ 5).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_jul",   formattedNumber(cert1894DataResponse.get(j+ 6).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_agos",  formattedNumber(cert1894DataResponse.get(j+ 7).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_sep",   formattedNumber(cert1894DataResponse.get(j+ 8).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_oct",   formattedNumber(cert1894DataResponse.get(j+ 9).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_nov",   formattedNumber(cert1894DataResponse.get(j+10).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "mayor_valor_dic",   formattedNumber(cert1894DataResponse.get(j+11).get("maximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_enero", formattedNumber(cert1894DataResponse.get(j+ 0).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_feb",   formattedNumber(cert1894DataResponse.get(j+ 1).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_mar",   formattedNumber(cert1894DataResponse.get(j+ 2).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_abril", formattedNumber(cert1894DataResponse.get(j+ 3).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_may",   formattedNumber(cert1894DataResponse.get(j+ 4).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_jun",   formattedNumber(cert1894DataResponse.get(j+ 5).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_jul",   formattedNumber(cert1894DataResponse.get(j+ 6).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_agos",  formattedNumber(cert1894DataResponse.get(j+ 7).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_sep",   formattedNumber(cert1894DataResponse.get(j+ 8).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_oct",   formattedNumber(cert1894DataResponse.get(j+ 9).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_nov",   formattedNumber(cert1894DataResponse.get(j+10).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "menor_valor_dic",   formattedNumber(cert1894DataResponse.get(j+11).get("minimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_enero", formattedNumber(cert1894DataResponse.get(j+ 0).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_feb",   formattedNumber(cert1894DataResponse.get(j+ 1).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_mar",   formattedNumber(cert1894DataResponse.get(j+ 2).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_abril", formattedNumber(cert1894DataResponse.get(j+ 3).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_may",   formattedNumber(cert1894DataResponse.get(j+ 4).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_jun",   formattedNumber(cert1894DataResponse.get(j+ 5).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_jul",   formattedNumber(cert1894DataResponse.get(j+ 6).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_agos",  formattedNumber(cert1894DataResponse.get(j+ 7).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_sep",   formattedNumber(cert1894DataResponse.get(j+ 8).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_oct",   formattedNumber(cert1894DataResponse.get(j+ 9).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_nov",   formattedNumber(cert1894DataResponse.get(j+10).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "factor_act_dic",   formattedNumber(cert1894DataResponse.get(j+11).get("actualFactor").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_enero", formattedNumber(cert1894DataResponse.get(j+ 0).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_feb",   formattedNumber(cert1894DataResponse.get(j+ 1).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_mar",   formattedNumber(cert1894DataResponse.get(j+ 2).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_abril", formattedNumber(cert1894DataResponse.get(j+ 3).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_may",   formattedNumber(cert1894DataResponse.get(j+ 4).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_jun",   formattedNumber(cert1894DataResponse.get(j+ 5).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_jul",   formattedNumber(cert1894DataResponse.get(j+ 6).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_agos",  formattedNumber(cert1894DataResponse.get(j+ 7).get("actualMaximumValue").toString().trim())));;
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_sep",   formattedNumber(cert1894DataResponse.get(j+ 8).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_oct",   formattedNumber(cert1894DataResponse.get(j+ 9).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_nov",   formattedNumber(cert1894DataResponse.get(j+10).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_mayor_valor_dic",   formattedNumber(cert1894DataResponse.get(j+11).get("actualMaximumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_enero", formattedNumber(cert1894DataResponse.get(j+ 0).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_feb",   formattedNumber(cert1894DataResponse.get(j+ 1).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_mar",   formattedNumber(cert1894DataResponse.get(j+ 2).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_abril", formattedNumber(cert1894DataResponse.get(j+ 3).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_may",   formattedNumber(cert1894DataResponse.get(j+ 4).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_jun",   formattedNumber(cert1894DataResponse.get(j+ 5).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_jul",   formattedNumber(cert1894DataResponse.get(j+ 6).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_agos",  formattedNumber(cert1894DataResponse.get(j+ 7).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_sep",   formattedNumber(cert1894DataResponse.get(j+ 8).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_oct",   formattedNumber(cert1894DataResponse.get(j+ 9).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_nov",   formattedNumber(cert1894DataResponse.get(j+10).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "monto_act_menor_valor_dic",   formattedNumber(cert1894DataResponse.get(j+11).get("actualMinimumValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "total_monto_act_mayor_valor", formattedNumber(cert1894DataResponse.get(j+11).get("actualMaximumTotalValue").toString().trim())));
            datos_1.appendChild(createElement(doc, "total_monto_act_menor_valor", formattedNumber(cert1894DataResponse.get(j+11).get("actualMinimumTotalValue").toString().trim())));
            j=j+12;
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        Result destino = new StreamResult(writer);
        transformer.transform(source, destino);
        System.out.println(writer.toString());
        xmltoBase64(__exchange, writer.toString(), null);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

        
    public void  DocumentValidationDataFormat(Exchange __exchange){
        
        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");
        //System.out.println("ACA EL LOG QUE QUEREMOS VER LOCOOOOOOOOOOOOO-------------"+ flows);
        System.out.println("ACA EL LOG DEL JAVA-------------"+ __exchange.toString());
        //Map<String, Object> session = (Map<String, Object>) flows.get("session");
        //Map<String, Object> login = (Map<String, Object>) session.get("login");
        //Map<String, Object> requestBody = (Map<String, Object>) flows.get("request-body");
        Map<String, Object> stocks_dividend = (Map<String, Object>) flows.get("stocks_dividend");
        Map<String, Object> response = (Map<String, Object>) stocks_dividend.get("response");
        Map<String, Object> request = (Map<String, Object>) stocks_dividend.get("request");
        Map<String, Object> headers = (Map<String, Object>) response.get("headers");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        //Map<String, Object> dividend = (Map<String, Object>) body.get("dividend");
        List<Map<String, Object>> stocks_dividendResponse = (List<Map<String, Object>>) body.get("dividend");
        String issueDate = "Santiago, 01 de Noviembre del 2024";
        //Map<String, Object> stocks_dividendResponse = (Map<String, Object>) response.get("57bisData");

        //$headers.properties.flows.session.login.username
        //customer_data
        /*flows.put("fundManagerDescription", customerData.get("fundManagerDescription"));
        flows.put("fundManagerId", customerData.get("fundManagerId"));
        flows.put("category", customerData.get("category"));
        flows.put("address", customerData.get("adress"));
        flows.put("manager", customerData.get("manager"));
        flows.put("managerId", customerData.get("managerId"));
        //flows.put("customerId", customerData.get("customerId"));
        flows.put("managerId", customerData.get("managerId"));
        flows.put("customerName", customerData.get("customerName"));
        flows.put("customerAdress", customerData.get("customerAdress"));
        flows.put("issueDate", issueDate);
        flows.put("customerRut", login.get("username"));
        */
        //System.out.println("CLASSPATH: " +System.getProperty("java.class.path") );
        //System.out.println("ACA EL LOG QUE QUEREMOS VER LOCOOOOOOOOOOOOO-------------"+ __exchange.toString());

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // Elemento raíz
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Documents");
            doc.appendChild(rootElement);
        
            rootElement.setAttributeNode(createAttr(doc, "PublicationName", "CERT_FFMM_57BIS"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xalan", "http://xml.apache.org/xslt"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
            rootElement.setAttributeNode(createAttr(doc, "xsi:noNamespaceSchemaLocation", "CERT_FFMM_57BIS.xsd"));
        
            Element cert = doc.createElement("CERT_FFMM_57BIS");
            rootElement.appendChild(cert);
        
            Element documentIdentifier = doc.createElement("DocumentIdentifier");
            cert.appendChild(documentIdentifier);
        
            documentIdentifier.appendChild(createElement(doc, "Empresa", "0022"));
            documentIdentifier.appendChild(createElement(doc, "Idioma", "es-CL"));
            documentIdentifier.appendChild(createElement(doc, "CanalMarco", "003"));
        
            cert.appendChild(createElement(doc, "Nombre_Corredora", "12345467"));
            cert.appendChild(createElement(doc, "RUT_Corredora", issueDate));
            cert.appendChild(createElement(doc, "Direccion_Corredora", body.get("brokerAdress").toString()));
            cert.appendChild(createElement(doc, "Ciudad_Corredora", body.get("brokerCity").toString()));
            cert.appendChild(createElement(doc, "Giro_Corredora", body.get("comercialDraft").toString()));
            cert.appendChild(createElement(doc, "Fono_Corredora",  body.get("brokerPhone").toString()));
            cert.appendChild(createElement(doc, "No_Certificado",  body.get("certificateNumber").toString()));
            cert.appendChild(createElement(doc, "Fecha_Emision", body.get("issueDate").toString()));
            cert.appendChild(createElement(doc, "Anio_Tributario", body.get("reportedYear").toString()+" - "+body.get("customerCommune")));
            cert.appendChild(createElement(doc, "Anio_Informado", body.get("reportedYear").toString()));
            cert.appendChild(createElement(doc, "Disclaimer_1",  body.get("disclaimer1").toString()));
            cert.appendChild(createElement(doc, "Disclaimer_2", body.get("disclaimer2").toString()));
            cert.appendChild(createElement(doc, "Nombre_Cliente", body.get("customerName").toString()));
            cert.appendChild(createElement(doc, "RUT_Cliente", body.get("brokedId").toString()));
            cert.appendChild(createElement(doc, "Direccion_Cliente",   body.get("brokerAdress").toString()));
            cert.appendChild(createElement(doc, "Comuna_Cliente", body.get("customerCommune").toString()));
            cert.appendChild(createElement(doc, "Ciudad_Cliente", body.get("brokerCity").toString()));
            cert.appendChild(createElement(doc, "Giro_Cliente", body.get("certificateNumber").toString()));
            cert.appendChild(createElement(doc, "Nombre_Corto_Cliente",  body.get("shortCustomerName").toString()));
            cert.appendChild(createElement(doc, "Dividendos", body.get("dividend").toString()));
            cert.appendChild(createElement(doc, "Dividendo", body.get("dividend").toString()));
            cert.appendChild(createElement(doc, "Fecha_Dividendo",   body.get("dividendDate").toString()));
            cert.appendChild(createElement(doc, "No_Dividendo",  body.get("dividendNumber").toString()));
            cert.appendChild(createElement(doc, "Nombre_Compania", body.get("companyName").toString()));
            cert.appendChild(createElement(doc, "RUT_Compania", body.get("companyId").toString()));
            cert.appendChild(createElement(doc, "No_Certificado_Compania",   body.get("companyCertificateNumber").toString()));
            cert.appendChild(createElement(doc, "Monto_Historico",   body.get("historicAmount").toString()));
            cert.appendChild(createElement(doc, "Factor_Actualizacion", body.get("updateFactor").toString()));
            cert.appendChild(createElement(doc, "Monto_Actualizado", body.get("updatedAmount").toString()));
            cert.appendChild(createElement(doc, "Monto_09", body.get("stockTax09").toString()));
            cert.appendChild(createElement(doc, "Monto_10", body.get("stockTax10").toString()));
            cert.appendChild(createElement(doc, "Monto_11", body.get("stockTax11").toString()));
            cert.appendChild(createElement(doc, "Monto_12", body.get("stockTax12").toString()));
            cert.appendChild(createElement(doc, "Monto_13", body.get("stockTax13").toString()));
            cert.appendChild(createElement(doc, "Monto_14", body.get("stockTax14").toString()));
            cert.appendChild(createElement(doc, "Monto_15", body.get("stockTax15").toString()));
            cert.appendChild(createElement(doc, "Monto_16", body.get("stockTax16").toString()));
            cert.appendChild(createElement(doc, "Monto_17", body.get("stockTax17").toString()));
            cert.appendChild(createElement(doc, "Monto_18", body.get("stockTax18").toString()));
            cert.appendChild(createElement(doc, "Monto_19", body.get("stockTax19").toString()));
            cert.appendChild(createElement(doc, "Monto_20", body.get("stockTax20").toString()));
            cert.appendChild(createElement(doc, "Monto_21", body.get("stockTax21").toString()));
            cert.appendChild(createElement(doc, "Monto_22", body.get("stockTax22").toString()));
            cert.appendChild(createElement(doc, "Monto_23", body.get("stockTax23").toString()));
            cert.appendChild(createElement(doc, "Monto_24", body.get("stockTax24").toString()));
            cert.appendChild(createElement(doc, "Monto_25", body.get("stockTax25").toString()));
            cert.appendChild(createElement(doc, "Monto_26", body.get("stockTax26").toString()));
            cert.appendChild(createElement(doc, "Monto_27", body.get("stockTax27").toString()));
            cert.appendChild(createElement(doc, "Monto_28", body.get("stockTax28").toString()));
            cert.appendChild(createElement(doc, "Monto_29", body.get("stockTax29").toString()));
            cert.appendChild(createElement(doc, "Monto_30", body.get("stockTax30").toString()));
            cert.appendChild(createElement(doc, "Monto_31", body.get("stockTax31").toString()));
            cert.appendChild(createElement(doc, "Monto_32", body.get("stockTax32").toString()));
            cert.appendChild(createElement(doc, "Monto_33", body.get("stockTax33").toString()));
            cert.appendChild(createElement(doc, "Monto_34", body.get("stockTax34").toString()));
            cert.appendChild(createElement(doc, "Monto_35", body.get("stockTax35").toString()));
            cert.appendChild(createElement(doc, "Monto_36", body.get("stockTax36").toString()));
            cert.appendChild(createElement(doc, "Monto_37", body.get("stockTax37").toString()));
            cert.appendChild(createElement(doc, "Monto_38", body.get("stockTax38").toString()));
           
           
   
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            Result destino = new StreamResult(writer);
            transformer.transform(source, destino);
            System.out.println(writer.toString());
            xmltoBase64(__exchange, writer.toString(), null);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            } catch (TransformerException tfe) {
                tfe.printStackTrace();
            }
    }
    
    



    public static Attr createAttr(Document doc, String nombreAtributo, String valor) {
        Attr atributo = doc.createAttribute(nombreAtributo);
        atributo.setValue(valor);
        return atributo;
    }

    public static Element createElement(Document doc, String nomElemento, String contenido) {
        Element elemento = doc.createElement(nomElemento);
        elemento.setTextContent(contenido);
        return elemento;
    }

    public void xmltoBase64(Exchange __exchange, String xmlString, File xmlDocument){
        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");

        if (xmlString != null && !xmlString.isEmpty()) {
            String base64XmlString = Base64.getEncoder().encodeToString(xmlString.getBytes());
            flows.put("base64XmlString", base64XmlString);
        }

        if (xmlDocument != null) {
            String base64XmlFile = convertFileToBase64(xmlDocument);
            flows.put("base64XmlString", base64XmlFile);
        }
    }

    private String convertFileToBase64(File xmlDocument) {
        try {
            byte[] fileBytes = readBytesFromFile(xmlDocument);
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    public static String formatDate(String fecha){
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy", new Locale("es","ES"));		
        String fechaformat = "";
        if(fecha.isEmpty()){
            Date now = new Date();
            fechaformat = sdfDate.format(now);
        }else{
            fechaformat = sdfDate.format(fecha);
        }
		return fechaformat;
	}

    public static String formattedNumber(String number) {
        if("0".equals(number.trim()) || "".equals(number.trim())){
          return number;
        }else{
          if(number.charAt(0)=='.'){
            number="0"+number.trim();
          }
          String pattern = "###,###,###";
          DecimalFormat myFormatter = new DecimalFormat(pattern);
          myFormatter = new DecimalFormat(pattern,DecimalFormatSymbols.getInstance(Locale.GERMANY));
          number = number.replace(".", ",");
          if(number.indexOf(",")>=0){
            return number = myFormatter.format(Integer.parseInt(number.trim().split(",")[0])) + "," + number.trim().split(",")[1];
          }else{
            return myFormatter.format(Integer.parseInt(number.trim()));
          }
        }
      }

}
