package br.com.rodrigo.conversorxmljsonscheduler.config;

import br.com.rodrigo.conversorxmljsonscheduler.ServicesImpl.CustomerConverter;
import br.com.rodrigo.conversorxmljsonscheduler.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JobConfiguration {
    public static final String HOME_GUIDE_DESKTOP_ARQUIVOS_TESTE_XML = "/home/guide/Desktop/arquivos/customer.xml";
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public StaxEventItemReader<Customer> customerItemReader(){
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        CustomerConverter converter = new CustomerConverter();
        XStreamMarshaller ummarshaller = new XStreamMarshaller();
        ummarshaller.setAliases(aliases);
        ummarshaller.setConverters(converter);

        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        PathResource resource = new PathResource(HOME_GUIDE_DESKTOP_ARQUIVOS_TESTE_XML);
        reader.setResource(resource);
        reader.setFragmentRootElementName("customer");
        reader.setUnmarshaller(ummarshaller);

        return reader;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(200)
                .reader(customerItemReader())
                .writer(writer())
                .build();
    }
    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> writer() {
        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(this.dataSource);
        writer.setSql("INSERT INTO customer (id, birthdate, first_name, last_name) VALUES (?,?,?,?)");
        writer.setItemPreparedStatementSetter(new CustomerItemPreparedStmSetter());
        return writer;
    }

    private static class CustomerItemPreparedStmSetter implements ItemPreparedStatementSetter<Customer> {
        public void setValues(Customer result, PreparedStatement ps) throws SQLException {
            ps.setLong(1, result.getId());
            ps.setTimestamp(2, Timestamp.valueOf(result.getBirthdate()));
            ps.setString(3, result.getFirstName());
            ps.setString(4, result.getLastName());
        }
    }
}
