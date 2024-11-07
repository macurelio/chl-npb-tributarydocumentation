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
 * RequestTributaryCertificate
 */

@JsonTypeName("requestTributaryCertificate")
@Generated(value = "org.openapitools.codegen.languages.JavaCamelServerCodegen", date = "2024-09-13T08:27:05.294701635-03:00[America/Santiago]", comments = "Generator version: 7.7.0")
public class RequestTributaryCertificate {

  private String certificateId;

  private String year;

  private String affidavit;

  public RequestTributaryCertificate certificateId(String certificateId) {
    this.certificateId = certificateId;
    return this;
  }

  /**
   * Get certificateId
   * @return certificateId
   */
  
  @Schema(name = "certificateId", example = "bis-57", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("certificateId")
  public String getCertificateId() {
    return certificateId;
  }

  public void setCertificateId(String certificateId) {
    this.certificateId = certificateId;
  }

  public RequestTributaryCertificate year(String year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @Schema(name = "year", example = "2023", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year")
  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public RequestTributaryCertificate affidavit(String affidavit) {
    this.affidavit = affidavit;
    return this;
  }

  /**
   * Get affidavit
   * @return affidavit
   */
  
  @Schema(name = "affidavit", example = "1920-3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affidavit")
  public String getAffidavit() {
    return affidavit;
  }

  public void setAffidavit(String affidavit) {
    this.affidavit = affidavit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestTributaryCertificate requestTributaryCertificate = (RequestTributaryCertificate) o;
    return Objects.equals(this.certificateId, requestTributaryCertificate.certificateId) &&
        Objects.equals(this.year, requestTributaryCertificate.year) &&
        Objects.equals(this.affidavit, requestTributaryCertificate.affidavit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(certificateId, year, affidavit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestTributaryCertificate {\n");
    sb.append("    certificateId: ").append(toIndentedString(certificateId)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    affidavit: ").append(toIndentedString(affidavit)).append("\n");
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

