package com.rabo.customer.statementprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedRecord {

  private Integer referenceId;
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FailedRecord that = (FailedRecord) o;

    return new EqualsBuilder()
        .append(referenceId, that.referenceId)
        .append(description, that.description)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(referenceId)
        .append(description)
        .toHashCode();
  }
}

