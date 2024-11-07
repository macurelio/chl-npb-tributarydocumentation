package com.santander.application.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ResponseTributaryCertificate
 */

@JsonTypeName("responseTributaryCertificate")
@Generated(value = "org.openapitools.codegen.languages.JavaCamelServerCodegen", date = "2024-09-13T08:27:05.294701635-03:00[America/Santiago]", comments = "Generator version: 7.7.0")
public class ResponseTributaryCertificate {

  private String documentId;

  private String documentUrl;

  private String documentB64;

  public ResponseTributaryCertificate documentId(String documentId) {
    this.documentId = documentId;
    return this;
  }

  /**
   * Get documentId
   * @return documentId
   */
  
  @Schema(name = "documentId", example = "ASJK6BD19DE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentId")
  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public ResponseTributaryCertificate documentUrl(String documentUrl) {
    this.documentUrl = documentUrl;
    return this;
  }

  /**
   * Get documentUrl
   * @return documentUrl
   */
  
  @Schema(name = "documentUrl", example = "http://viphbisi1.cl.bsch/document/document_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentUrl")
  public String getDocumentUrl() {
    return documentUrl;
  }

  public void setDocumentUrl(String documentUrl) {
    this.documentUrl = documentUrl;
  }

  public ResponseTributaryCertificate documentB64(String documentB64) {
    this.documentB64 = documentB64;
    return this;
  }

  /**
   * Get documentB64
   * @return documentB64
   */
  
  @Schema(name = "documentB64", example = "JVBERi0xLjQNCiVET0MxIFJlc291cmNlR1VJRD1FNUUyMzE3MUI1NzE0Q0M3QTZGNzNBOTNDMjUy  RDNDMg0KMSAwIG9iag0KPDwgL1R5cGUgL1BhZ2UNCi9QYXJlbnQgMyAwIFINCi9SZXNvdXJjZXMg  NCAwIFINCi9Db250ZW50cyAyIDAgUg0KL01lZGlhQm94IFswIDAgNjExLjk2OCA3OTEuOTU4XQ0K  Pj4gZW5kb2JqDQoyIDAgb2JqIDw8L0xlbmd0aCA0Mzc2IC9GaWx0ZXIgWy9BU0NJSTg1RGVjb2Rl  IC9GbGF0ZURlY29kZV0+Pg0Kc3RyZWFtDQpHYXQlJmgvOl81KDs8O3UqRDBTXTg3OmA/Ry04OEYh  ZkBhKzJUXTosMWNOR0EiT0QyIUhnR1csYlxQISVFXUZxDQpAWFFCXVs0KVQ5JTNvcGlTNVJPMTE8  SFw7VUYvaU9mP1JcaXE2TUExK28nKUBxTCFtXjhRWjluLWAyUV8zZWdKDQoiJDdDYSpjY19OTzdg  NHFtLUdyXjhQcVZVcjBYKSVZJSl0Yi1MPGYlTEEydHMhQm9rZy9ZO0BBMSNgZXMqWGRqDQpJQCNe  TmhEOyNWRUlbPV47IidvWj9AakRDcUBELGEwUmpeQWlAXFk2bykqWUNtR0ZabyxXW09vPGo3UmAq  cmNVDQp0Ui5yXG1fWHRvQlhUUW9uQFgzSEBDMCNIUmhpZGBUckhSUERjSlxYXDokSS8hM0dwLiVI  dDFwOFY1M01VZkBTDQpgTjhDKk5IZU8rTVZEbEtYWiMvZ1IzYzxjbydZbEJsakUnTj5bNVInWmlh  N1ZUUDxqaEBVMW41ZzlmZnRoQmtGDQohVFc2YUcrWSZnTVdxJF4kUGFnZlwjWkU0Lj4kWnNdYlgn  NW9hdHFEVUJGPiU5MTltRkNtJiVZJj8mNyEuLSgvDQpBQ1Jra0FYYjI9Ty8nIiVHPFc2MHVIN0Fp  PnM6NTdbcV89dTJbVTpJWEJaXlRLV2g1aSlcMk5VRWpHKEFKWm0kDQpLK1U4WksqLmVRMC8lcWVh  Ozc9ZHBbOE1QJCNeOVVcTjxxL1ZDZlhULkdLT2FnIUhbQz9LPiVfQzVHL2JtJW4yDQpyRGYpYyIo  Zmg2S0tRQmNYX2lPVm8/LUBnRnU2ZUxXRCNEUjEtP0sqJjN0O2t0LmRfZ2pQdFFVWnI3Mm1iJVVQ  DQpNW1cpM0MiU2xccTZGPldoNTJkZCU8bElGQ20kZG5JZGo0PDxHZlFPQ2g4SixpQmVmNkI8KVZZ  SShAa3U0V1lTDQoqLVksIVNFW0tXcjU/MDRPQW41T2k2PEY4LEowaF5qOEo9KkdEV0hTKjxRUTk/  WlRbR0RXMzM9JFJcMWxicDxCDQotWUU0RmI/RCUobzsyMSwoVmlEPWEiQiIzUyg+YjRMLDBPLGxv  Zy1mL2FjZSJEKDIpIiohSiwsTWNobldXYTZIDQo9JkpHMnFNW2pGImVgbHVXRk4vJk85KicxbmND  Y0lpYl9iUzlsODpVNXE5SDY+TUwtOEUlO1RpIWYoKidvdWIvDQpjdFc6TEkpU1VYJnE9Skw4UmIk  alBmXF03IjFhJC9uRG1XXTFbME09W108YU5fLkVVNEw+Z3VeMTxtV2dNRC02DQo9LWMwNiRcQFlY  JD1XQyUrKEVnPy9RW2ImUEdRc00tZSEvbV9hJjNFZE9uLGVxPCltVUAsIkVsLTRsPihqI3NjDQpX s/Sm9TVkJMVkItZGlUDQouUzoiPmVoYDFvTVoyISNWX1FwImpWMSQzVVlJT3EvaW0iTmtAa2g2  MClqJSxSXzRXVyE0YycoNzFDNkcmbTVKDQpYcWZoQS5cS1lrM1ZFa2cicio7XkY6PjBJKUAsPzYs  dS0hPTJGYyJdPjFmPmI1N0JjUTY0Lls4cHNtI0RLaFA3DQoqXkc3Z0g1VGpYMFAhYj86Tzw9RCEh  TF0sLVBfNWVZNTszKG83KCpKKzpNKzFrQDhIP3JUVkM/VEZAdSEjMmkrDQopQnUoU0xTdGhrKkBH  TDNoVDpJOjo4cW4yOkAyUWwzY0dtQyxnQW9KJjhmdGxQTzVpaDIpO2BBSkxpLWpjblgjDQosajlY  LzUnTTRSbChRQXVnRyNGJlxbUllBazZqTCorW0Uzbzs1UldkT1ctXzUhV05pN09vNk4lNGUxYl1Y  PDE8DQo3OXBUbWs+Y2ByTlg1PllMU1RaJXBSKy0uTmddM144YztlQmdAQyNUYStrKCdFR2tkWF0x  XWxjLW8+I1tdUzQsDQpYKHQ5X1M4PjheWkhmTCFXQ3FDSV43SytlczMyNzFQJ0AhdGEyXS5fPCFc  V0ZzQWxoSVlUM21NYDFsNDlQMG02DQo6VSMvU0Vrbj8tdC1KTjBiJTE1LTFlOmpVNytvXSU0LyI5  RlpeXz9fKiRPViM+ZTEmTS1cUFswYF5TWm9fJC5eDQpedS9Mb2NMZ1AhTVErU1w2c0lcczl0OzMk  MFRYUk9AUnFuYF8lZ19fJDwzQyo0SSdTKzZoJlZZQF5nYTNCXXFjDQoxJiRfYVBiMChBckNxXGtA  XFNkbEknPm05WjxCSkhkYXAxNTxeTT0wQTRYOHIjZW11XWxEMkA3LUJMJnVBXkktDQpBISs9LCJT  TmJwb2tLUUo6YS9ecXBjJjM9ZFpTPWFkXlRDIVo6bGksMGNuNEdPY0BjVUJMYE90ZElFR29dNkIx  DQpxYnROOWllVGljO05vMW83Iis2XmpTPl0zWjs5UVFeV2VOa29yW0E2RyhpL0k9KjlJI0JbOEMo  SVFIKDxCclwkDQpJXmBsVFByNG9NL0ckTEZIaltWPktZWjBkKkIrRlo4NFdRJFwyV19yKU9ERzpg  QiMyPXEhUWc7IXEyLDEjQE9BDQpxWmxkXDIqPUJ0SCg4MWwsLichUyYqXyddW1lBV1xwXj5EaitD  TnQ6cGJeZGolJktPZTRLbUxjbmpVUkRzLFwtDQp1W1woQjJdWExJUic3cVEwXTBPlgNCmY9K0NvbjZablksSk5AciEnaV86WUwmREM7YFs9  SmZGPzxbWnQ+L0IkUl5XVCthIm5UWkUiZSsiRFJbNSI5TG0NCldiYm4", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentB64")
  public String getDocumentB64() {
    return documentB64;
  }

  public void setDocumentB64(String documentB64) {
    this.documentB64 = documentB64;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseTributaryCertificate responseTributaryCertificate = (ResponseTributaryCertificate) o;
    return Objects.equals(this.documentId, responseTributaryCertificate.documentId) &&
        Objects.equals(this.documentUrl, responseTributaryCertificate.documentUrl) &&
        Objects.equals(this.documentB64, responseTributaryCertificate.documentB64);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentId, documentUrl, documentB64);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseTributaryCertificate {\n");
    sb.append("    documentId: ").append(toIndentedString(documentId)).append("\n");
    sb.append("    documentUrl: ").append(toIndentedString(documentUrl)).append("\n");
    sb.append("    documentB64: ").append(toIndentedString(documentB64)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

