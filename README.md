Application supports customer statements files of type csv and xml. 

run `mvn install` to generate the artifact.

Example usage

```java -jar target/customer-statement-processor-1.0-SNAPSHOT-jar-with-dependencies.jar```
 
Enter the file path when prompted 

```
[main] INFO com.rabo.customer.statementprocessor.Application - Name of the file to process
 /Users/Documents/java/customer-statement-processor/records.csv
```

The output of invalid records will be reported in a file named 'InvalidRecords.json'
