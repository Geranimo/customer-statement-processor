package com.rabo.customer.statementprocessor;

import com.rabo.customer.statementprocessor.model.CustomerStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationService {

  public ValidationService() {

  }

  /**
   * Get a list of statements where the endbalance doesnt add up
   *
   * @param customerStatements
   * @return
   */
  public List<CustomerStatement> getInvalidEndBalanceInStatements(
      List<CustomerStatement> customerStatements) {
    return customerStatements
        .stream()
        .filter(statement ->
            (!statement.getStartBalance().add(statement.getMutation())
                .equals(statement.getEndBalance())))
        .collect(Collectors.toList());
  }

  /**
   * Get a list of statements with reference ids duplicated
   *
   * @param customerStatements
   * @return
   */
  public List<CustomerStatement> getAllNonUniqueReferenceInStatements(
      List<CustomerStatement> customerStatements) {
    final Set<Integer> statementsReference = new HashSet<>();

    List<CustomerStatement> statementWithDuplicateReference = customerStatements.stream()
        .filter(customerStatement -> !statementsReference.add(customerStatement.getReference()))
        .collect(Collectors.toList());

    return statementWithDuplicateReference;
  }
}

