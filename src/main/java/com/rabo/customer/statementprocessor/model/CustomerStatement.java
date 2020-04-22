package com.rabo.customer.statementprocessor.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class CustomerStatement {

    @NonNull
    private Integer reference;
    @NonNull
    private String accountNumber;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal startBalance;
    @NonNull
    private BigDecimal mutation;
    @NonNull
    private BigDecimal endBalance;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerStatement statement = (CustomerStatement) o;

        return new EqualsBuilder()
            .append(reference, statement.reference)
            .append(accountNumber, statement.accountNumber)
            .append(description, statement.description)
            .append(startBalance, statement.startBalance)
            .append(mutation, statement.mutation)
            .append(endBalance, statement.endBalance)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(reference)
            .append(accountNumber)
            .append(description)
            .append(startBalance)
            .append(mutation)
            .append(endBalance)
            .toHashCode();
    }
}
