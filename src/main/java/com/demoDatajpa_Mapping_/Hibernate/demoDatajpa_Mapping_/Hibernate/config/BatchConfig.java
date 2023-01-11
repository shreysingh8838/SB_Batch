package com.demoDatajpa_Mapping_.Hibernate.demoDatajpa_Mapping_.Hibernate.config;

import com.demoDatajpa_Mapping_.Hibernate.demoDatajpa_Mapping_.Hibernate.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

// SO THE CONFIG IS JOB START -> CALLS STEPS -> THEN IN EACH STEP -> READER EXECUTED -> PROCESSOR EXECUTED -> WRITER EXECUTED

// For making any file as Config file we need to add these annotations
@Configuration
@EnableBatchProcessing  // provide a base configuration for setting up batch jobs in an @Configuration class
public class BatchConfig {

    // We need to autowire certain objects
    // Not made by us, but provided by Spring Batch
    @Autowired
    private DataSource dataSource;  // In application.prop file it has the login credentials for the DB

    @Autowired
    private StepBuilderFactory stepBuilderFactory;  // Object for building step
    @Autowired
    private JobBuilderFactory jobBuilderFactory;    // Object for building JOB


    @Bean
    // Reader
    // we will make a Bean of FlatFileItemReader class's method
    public FlatFileItemReader<User> reader(){
        // to use this class methods
        // we will create its object and in generics we will provide the model class as its datatype
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        // to get the target file from the resources location.
        // we will use setResource method oF FlatFileItemReader Class.
        // and we will pass a new object of ClassPathResource and inside its parameter we will pass the file from resources location
        reader.setResource(new ClassPathResource("employees.csv"));
        // we will call setLineMapper method of FlatFileItemReader class and will pass that method which
        // we will create a new method with getLineMapper where we will write our logic, so how to map the data
        reader.setLineMapper(getLineMapper());

        // we will call setLinesToSkip method of FlatFileItemReader class, to skip the number of lines if get any errors
        reader.setLinesToSkip(1);
        return reader;
    }

    private LineMapper<User> getLineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

        // DELIMITED LINE TOKENIZER - used to split the input string on a configurable delimiter
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(new String[]{"userID", "firstname", "lastname", "jobid", "salary", "managerId", "departmentID"});
        delimitedLineTokenizer.setIncludedFields(new int[]{0, 1, 2, 6, 7, 9, 10});  // to list the number of column number which we want to take from csv file


        // FIELD SET MAPPER - mapped should have field name meta data corresponding to bean property paths in an instance of the desired type
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);   //Public setter for the type of bean to create instead of using a prototype bean.


        // GIVING THE OBJECTS TO linemapper
        lineMapper.setLineTokenizer(delimitedLineTokenizer);  //delimitedLineTokenizer
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    // PROCESSOR - USERITEMPROCESSOR BEAN CONFIG
    @Bean
    public UserItemProcessor processor(){
        return new UserItemProcessor();
    }

    // WRITER
    @Bean
    public JdbcBatchItemWriter<User> writer(){
      JdbcBatchItemWriter writer = new JdbcBatchItemWriter<>();
      writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
      writer.setSql("insert into user(userId, firstname, lastname, jobid, salary, managerId, departmentID) values (:userID, :firstname, :lastname, :jobid, :salary, :managerId, :departmentID)");
      writer.setDataSource(this.dataSource);
      return writer;
    }


    // JOB
    @Bean
    public Job importUserJobs(){
        return this.jobBuilderFactory.get("USER-IMPORT-JOB")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    // STEP
    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                .<User, User>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();

    }
}
